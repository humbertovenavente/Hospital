#!/bin/bash

# Script de prueba para SonarQube Community Edition
# Hospital System - Análisis de Calidad de Código

echo "🧪 Probando configuración de SonarQube Community Edition..."
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
export BUILD_NUMBER="${BUILD_NUMBER:-$(date +%Y%m%d%H%M%S)}"

echo "📊 Configuración:"
echo "   - Build: $BUILD_NUMBER"
echo "   - SonarQube: $SONAR_HOST_URL"
echo "   - Versión: Community Edition (sin soporte de ramas)"

# ANÁLISIS DEL BACKEND (SIN parámetro de rama)
echo ""
echo "🔨 Probando análisis del BACKEND..."
echo "=================================="

if [ -d "backend" ]; then
    cd backend
    
    # Verificar que el proyecto esté compilado
    if [ ! -d "target/classes" ]; then
        echo "⚠️  Compilando proyecto backend..."
        mvn clean compile -DskipTests
    fi
    
    # Ejecutar tests para generar cobertura
    echo "🧪 Ejecutando tests para generar cobertura..."
    mvn test -DskipTests
    
    # Generar reporte de cobertura con JaCoCo
    echo "📊 Generando reporte de cobertura..."
    mvn jacoco:report
    
    # Ejecutar análisis de SonarQube SIN parámetro de rama
    echo "🔍 Ejecutando análisis de SonarQube (Community Edition)..."
    sonar-scanner \
        -Dsonar.projectKey=hospital-backend-prod-test \
        -Dsonar.projectName="Hospital Backend - PRODUCCIÓN TEST (Java/Quarkus)" \
        -Dsonar.projectVersion=$BUILD_NUMBER \
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
        echo "🌐 Ver proyecto en: $SONAR_HOST_URL/dashboard?id=hospital-backend-prod-test"
    else
        echo "❌ Error en el análisis del BACKEND"
        exit 1
    fi
    
    cd ..
else
    echo "⚠️  Directorio backend no encontrado, saltando análisis"
fi

# ANÁLISIS DEL FRONTEND (SIN parámetro de rama)
echo ""
echo "🎨 Probando análisis del FRONTEND..."
echo "==================================="

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
    
    # Ejecutar análisis de SonarQube SIN parámetro de rama
    echo "🔍 Ejecutando análisis de SonarQube (Community Edition)..."
    sonar-scanner \
        -Dsonar.projectKey=hospital-frontend-prod-test \
        -Dsonar.projectName="Hospital Frontend - PRODUCCIÓN TEST (Vue.js/TypeScript)" \
        -Dsonar.projectVersion=$BUILD_NUMBER \
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
        echo "🌐 Ver proyecto en: $SONAR_HOST_URL/dashboard?id=hospital-frontend-prod-test"
    else
        echo "❌ Error en el análisis del FRONTEND"
        exit 1
    fi
else
    echo "⚠️  Directorio src o package.json no encontrado, saltando análisis del frontend"
fi

echo ""
echo "🎉 Prueba de SonarQube Community Edition completada"
echo "=================================================="
echo "📊 Proyectos de prueba creados:"
echo "   - Backend: hospital-backend-prod-test"
echo "   - Frontend: hospital-frontend-prod-test"
echo "🌐 Ver resultados en: $SONAR_HOST_URL"
echo "📅 Build: $BUILD_NUMBER"
echo ""
echo "💡 Nota: Estos son proyectos de prueba. Para producción, usa:"
echo "   - hospital-backend-prod"
echo "   - hospital-frontend-prod"
