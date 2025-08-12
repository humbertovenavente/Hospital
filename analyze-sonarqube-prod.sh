#!/bin/bash

# Script para análisis de SonarQube en rama PRODUCCIÓN
# Hospital System - Análisis de Calidad de Código

echo "🔍 Iniciando análisis de SonarQube para rama PRODUCCIÓN..."
echo "=================================================="

# Verificar que SonarQube esté disponible
echo "📡 Verificando conexión con SonarQube..."
if ! curl -f http://localhost:9000/api/system/status > /dev/null 2>&1; then
    echo "❌ Error: SonarQube no está disponible en http://localhost:9000"
    exit 1
fi
echo "✅ SonarQube está disponible"

# Verificar que SonarQube Scanner esté instalado
echo "🔧 Verificando SonarQube Scanner..."
if ! command -v sonar-scanner > /dev/null 2>&1; then
    echo "❌ Error: sonar-scanner no está instalado"
    echo "   Instala con: sudo apt-get install -y sonar-scanner"
    exit 1
fi
echo "✅ SonarQube Scanner está instalado"

# Configurar variables de entorno
export SONAR_HOST_URL="http://localhost:9000"
export SONAR_TOKEN="${SONAR_TOKEN:-$SONAR_AUTH_TOKEN}"
export BRANCH_NAME="prod"
export BUILD_NUMBER="${BUILD_NUMBER:-$(date +%Y%m%d%H%M%S)}"

echo "📊 Configuración:"
echo "   - Rama: $BRANCH_NAME"
echo "   - Build: $BUILD_NUMBER"
echo "   - SonarQube: $SONAR_HOST_URL"

# ANÁLISIS DEL BACKEND
echo ""
echo "🔨 Analizando BACKEND (Java/Quarkus)..."
echo "======================================"

if [ -d "backend" ]; then
    cd backend
    
    # Verificar que el proyecto esté compilado
    if [ ! -d "target/classes" ]; then
        echo "⚠️  Compilando proyecto backend..."
        mvn clean compile -DskipTests
    fi
    
    # Ejecutar tests para generar cobertura
    echo "🧪 Ejecutando tests para generar cobertura..."
    mvn test -DskipITs
    
    # Generar reporte de cobertura con JaCoCo
    echo "📊 Generando reporte de cobertura..."
    mvn jacoco:report
    
    # Ejecutar análisis de SonarQube
    echo "🔍 Ejecutando análisis de SonarQube..."
    sonar-scanner \
        -Dsonar.projectKey=hospital-backend-prod \
        -Dsonar.projectName="Hospital Backend - PRODUCCIÓN (Java/Quarkus)" \
        -Dsonar.projectVersion=$BUILD_NUMBER \
        -Dsonar.branch.name=$BRANCH_NAME \
        -Dsonar.sources=src/main/java \
        -Dsonar.java.source=17 \
        -Dsonar.java.binaries=target/classes \
        -Dsonar.java.libraries=target/lib/*.jar \
        -Dsonar.tests=src/test/java \
        -Dsonar.java.test.binaries=target/test-classes \
        -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml \
        -Dsonar.host.url=$SONAR_HOST_URL \
        -Dsonar.token=$SONAR_TOKEN \
        -Dsonar.qualitygate.wait=true \
        -Dsonar.qualitygate.timeout=300
    
    if [ $? -eq 0 ]; then
        echo "✅ Análisis del BACKEND completado exitosamente"
    else
        echo "❌ Error en el análisis del BACKEND"
        exit 1
    fi
    
    cd ..
else
    echo "⚠️  Directorio backend no encontrado, saltando análisis"
fi

# ANÁLISIS DEL FRONTEND
echo ""
echo "🎨 Analizando FRONTEND (Vue.js/TypeScript)..."
echo "============================================"

if [ -d "src" ] && [ -f "package.json" ]; then
    # Verificar que las dependencias estén instaladas
    if [ ! -d "node_modules" ]; then
        echo "📦 Instalando dependencias del frontend..."
        npm ci
    fi
    
    # Ejecutar tests para generar cobertura
    echo "🧪 Ejecutando tests para generar cobertura..."
    npm run test:unit || echo "⚠️  Tests unitarios no configurados"
    
    # Construir proyecto
    echo "🔨 Construyendo proyecto frontend..."
    npm run build
    
    # Ejecutar análisis de SonarQube
    echo "🔍 Ejecutando análisis de SonarQube..."
    sonar-scanner \
        -Dsonar.projectKey=hospital-frontend-prod \
        -Dsonar.projectName="Hospital Frontend - PRODUCCIÓN (Vue.js/TypeScript)" \
        -Dsonar.projectVersion=$BUILD_NUMBER \
        -Dsonar.branch.name=$BRANCH_NAME \
        -Dsonar.sources=src \
        -Dsonar.javascript.lcov.reportsPaths=coverage/lcov.info \
        -Dsonar.typescript.lcov.reportsPaths=coverage/lcov.info \
        -Dsonar.host.url=$SONAR_HOST_URL \
        -Dsonar.token=$SONAR_TOKEN \
        -Dsonar.exclusions=**/node_modules/**,**/dist/**,**/coverage/**,**/*.min.js,**/*.min.css,**/e2e/**,**/public/** \
        -Dsonar.qualitygate.wait=true \
        -Dsonar.javascript.timeout=600000 \
        -Dsonar.typescript.timeout=600000 \
        -Dsonar.javascript.bridge.timeout=600000 \
        -Dsonar.javascript.bridge.connectionTimeout=600000 \
        -Dsonar.javascript.bridge.readTimeout=600000 \
        -Dsonar.javascript.bridge.serverTimeout=600000 \
        -Dsonar.javascript.bridge.keepAlive=true \
        -Dsonar.javascript.bridge.maxRetries=5 \
        -Dsonar.javascript.bridge.memory=4096 \
        -Dsonar.javascript.bridge.maxMemory=8192
    
    if [ $? -eq 0 ]; then
        echo "✅ Análisis del FRONTEND completado exitosamente"
    else
        echo "❌ Error en el análisis del FRONTEND"
        exit 1
    fi
else
    echo "⚠️  Directorio src o package.json no encontrado, saltando análisis del frontend"
fi

echo ""
echo "🎉 Análisis de SonarQube para rama PRODUCCIÓN completado"
echo "=================================================="
echo "📊 Proyectos analizados:"
echo "   - Backend: hospital-backend-prod"
echo "   - Frontend: hospital-frontend-prod"
echo "🌐 Ver resultados en: $SONAR_HOST_URL"
echo "🔗 Rama: $BRANCH_NAME"
echo "📅 Build: $BUILD_NUMBER"
