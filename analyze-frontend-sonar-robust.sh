#!/bin/bash

set -e

echo "🔍 Iniciando análisis ROBUSTO del FRONTEND con SonarQube..."

# Configuración
PROJECT_KEY="hospital-frontend"
PROJECT_NAME="Hospital Frontend - Vue.js/TypeScript"
BUILD_NUMBER="${BUILD_NUMBER:-1.0}"
SONAR_HOST="${SONAR_HOST:-http://localhost:9000}"
SONAR_TOKEN="${SONAR_TOKEN:-sqa_3d84d5732d838f7dc6993dfbf6f9356b819f8a53}"

echo "=== Verificando SonarQube ==="
if curl -f "${SONAR_HOST}/api/system/status" > /dev/null 2>&1; then
    echo "✅ SonarQube está disponible"
else
    echo "❌ SonarQube no está disponible"
    exit 1
fi

echo "=== Verificando SonarQube Scanner ==="
if command -v sonar-scanner > /dev/null 2>&1; then
    echo "✅ SonarQube Scanner está disponible"
else
    echo "❌ SonarQube Scanner no está disponible"
    exit 1
fi

echo "=== Limpiando directorio de trabajo anterior ==="
rm -rf .scannerwork
echo "✅ Directorio limpiado"

echo "=== Ejecutando análisis del frontend ==="
echo "   Proyecto: ${PROJECT_KEY}"
echo "   Versión: ${BUILD_NUMBER}"
echo "   Host: ${SONAR_HOST}"

# Configuración específica para evitar problemas de bridge server
export SONAR_SCANNER_OPTS="-Xmx8g"

# INTENTO 1: Análisis completo con configuración robusta
echo "🔄 INTENTO 1: Análisis completo del frontend..."
if sonar-scanner \
    -Dproject.settings=./sonar-project-frontend-robust.properties \
    -Dsonar.projectVersion="${BUILD_NUMBER}"; then
    
    echo "🎉 Análisis COMPLETO del frontend exitoso!"
    echo "📊 Resultados disponibles en: ${SONAR_HOST}/dashboard?id=${PROJECT_KEY}"
    exit 0
else
    echo "⚠️  Análisis completo falló, intentando análisis básico..."
fi

# INTENTO 2: Análisis básico (fallback)
echo "🔄 INTENTO 2: Análisis básico del frontend (sin JS/TS)..."
if sonar-scanner \
    -Dproject.settings=./sonar-project-frontend-basic.properties \
    -Dsonar.projectVersion="${BUILD_NUMBER}"; then
    
    echo "✅ Análisis BÁSICO del frontend exitoso (fallback)"
    echo "📊 Resultados disponibles en: ${SONAR_HOST}/dashboard?id=hospital-frontend-basic"
    echo "⚠️  NOTA: Este es un análisis básico que excluye JavaScript/TypeScript"
    echo "   Para análisis completo, revisar problemas de memoria del bridge server"
    exit 0
else
    echo "❌ Ambos análisis fallaron"
    exit 1
fi
