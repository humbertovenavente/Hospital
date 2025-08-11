#!/bin/bash

# Script para ejecutar análisis de SonarQube por separado
# Hospital Management System - Análisis de Calidad

echo "🔍 Iniciando análisis de calidad con SonarQube..."
echo "=================================================="

# Verificar que SonarQube esté disponible
echo "📡 Verificando conexión con SonarQube..."
if ! curl -f http://localhost:9000/api/system/status > /dev/null 2>&1; then
    echo "❌ Error: SonarQube no está disponible en http://localhost:9000"
    echo "   Asegúrate de que esté ejecutándose: docker-compose up -d"
    exit 1
fi
echo "✅ SonarQube está disponible"

# Verificar que SonarQube Scanner esté instalado
if ! command -v sonar-scanner > /dev/null 2>&1; then
    echo "❌ Error: SonarQube Scanner no está instalado"
    echo "   Instala con: sudo apt-get install sonarqube-scanner"
    exit 1
fi
echo "✅ SonarQube Scanner está disponible"

# Variables de configuración
SONAR_HOST="http://localhost:9000"
SONAR_TOKEN="${SONAR_TOKEN:-your-sonarqube-token-here}"

if [ "$SONAR_TOKEN" = "your-sonarqube-token-here" ]; then
    echo "⚠️  ADVERTENCIA: No se ha configurado SONAR_TOKEN"
    echo "   Exporta la variable: export SONAR_TOKEN=tu-token-aqui"
    echo "   O edita este script para incluir tu token"
fi

echo ""
echo "🚀 Iniciando análisis del BACKEND..."
echo "====================================="

# Compilar backend para generar clases
echo "🔨 Compilando backend..."
cd backend
mvn clean compile test-compile -q
if [ $? -ne 0 ]; then
    echo "❌ Error compilando backend"
    exit 1
fi
cd ..

# Análisis del BACKEND
echo "🔍 Analizando BACKEND con SonarQube..."
sonar-scanner \
    -Dsonar.projectKey=hospital-backend \
    -Dsonar.projectName="Hospital Backend - Java/Quarkus" \
    -Dsonar.projectVersion=1.0 \
    -Dsonar.sources=backend/src/main/java \
    -Dsonar.tests=backend/src/test/java \
    -Dsonar.java.source=17 \
    -Dsonar.java.binaries=backend/target/classes \
    -Dsonar.java.test.binaries=backend/target/test-classes \
    -Dsonar.coverage.jacoco.xmlReportPaths=backend/target/site/jacoco/jacoco.xml \
    -Dsonar.host.url="$SONAR_HOST" \
    -Dsonar.token="$SONAR_TOKEN" \
    -Dsonar.exclusions="**/target/**,**/*.min.js,**/*.min.css,**/generated/**" \
    -Dsonar.qualitygate.wait=true

if [ $? -eq 0 ]; then
    echo "✅ Análisis del BACKEND completado exitosamente"
    echo "   📊 Ver resultados en: $SONAR_HOST/dashboard?id=hospital-backend"
else
    echo "❌ Error en el análisis del BACKEND"
fi

echo ""
echo "🚀 Iniciando análisis del FRONTEND..."
echo "======================================"

# Generar cobertura del frontend
echo "🧪 Ejecutando tests del frontend para generar cobertura..."
npm run test:unit -- --coverage
if [ $? -ne 0 ]; then
    echo "⚠️  Advertencia: Los tests del frontend fallaron, pero continuaremos con el análisis"
fi

# Análisis del FRONTEND
echo "🔍 Analizando FRONTEND con SonarQube..."
sonar-scanner \
    -Dsonar.projectKey=hospital-frontend \
    -Dsonar.projectName="Hospital Frontend - Vue.js/TypeScript" \
    -Dsonar.projectVersion=1.0 \
    -Dsonar.sources=src \
    -Dsonar.language=js,ts \
    -Dsonar.typescript.tsconfigPath=tsconfig.json \
    -Dsonar.javascript.lcov.reportsPaths=coverage/lcov.info \
    -Dsonar.typescript.lcov.reportsPaths=coverage/lcov.info \
    -Dsonar.host.url="$SONAR_HOST" \
    -Dsonar.token="$SONAR_TOKEN" \
    -Dsonar.exclusions="**/node_modules/**,**/dist/**,**/coverage/**,**/*.min.js,**/*.min.css,**/public/**,**/e2e/**" \
    -Dsonar.qualitygate.wait=true

if [ $? -eq 0 ]; then
    echo "✅ Análisis del FRONTEND completado exitosamente"
    echo "   📊 Ver resultados en: $SONAR_HOST/dashboard?id=hospital-frontend"
else
    echo "❌ Error en el análisis del FRONTEND"
fi

echo ""
echo "🎉 Análisis de calidad completado!"
echo "=================================="
echo "📊 Proyectos en SonarQube:"
echo "   • Backend: $SONAR_HOST/dashboard?id=hospital-backend"
echo "   • Frontend: $SONAR_HOST/dashboard?id=hospital-frontend"
echo ""
echo "💡 Consejos:"
echo "   • El backend mostrará cobertura de tests si tienes tests unitarios"
echo "   • El frontend mostrará calidad de código JavaScript/TypeScript"
echo "   • Revisa los Quality Gates en cada proyecto"


