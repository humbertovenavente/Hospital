#!/bin/bash

# Script específico para análisis del frontend con SonarQube
# Enfocado en resolver problemas de timeout del bridge server

set -e

echo "🔍 Iniciando análisis del FRONTEND con SonarQube..."

# Configuración
SONAR_HOST="${SONAR_HOST:-http://localhost:9000}"
SONAR_TOKEN="${SONAR_TOKEN:-}"
BUILD_NUMBER="${BUILD_NUMBER:-1.0}"

# Verificar SonarQube
echo "=== Verificando SonarQube ==="
if ! curl -f "${SONAR_HOST}/api/system/status" > /dev/null 2>&1; then
    echo "❌ SonarQube no está disponible"
    exit 1
fi
echo "✅ SonarQube está disponible"

# Verificar SonarQube Scanner
echo "=== Verificando SonarQube Scanner ==="
if ! command -v sonar-scanner > /dev/null 2>&1; then
    echo "❌ SonarQube Scanner no está disponible"
    exit 1
fi
echo "✅ SonarQube Scanner está disponible"

# Limpiar directorio de trabajo anterior
echo "=== Limpiando directorio de trabajo anterior ==="
rm -rf .scannerwork
echo "✅ Directorio limpiado"

# Ejecutar análisis con configuración robusta
echo "=== Ejecutando análisis del frontend ==="
echo "   Proyecto: hospital-frontend"
echo "   Versión: ${BUILD_NUMBER}"
echo "   Host: ${SONAR_HOST}"

# Configuración específica para evitar problemas de bridge server
export SONAR_SCANNER_OPTS="-Xmx4g"

sonar-scanner \
    -Dproject.settings=./sonar-project-frontend.properties \
    -Dsonar.projectVersion="${BUILD_NUMBER}"

if [ $? -eq 0 ]; then
    echo "🎉 Análisis del frontend completado exitosamente"
    echo "📊 Resultados disponibles en: ${SONAR_HOST}/dashboard?id=hospital-frontend"
else
    echo "❌ Análisis del frontend falló"
    exit 1
fi
