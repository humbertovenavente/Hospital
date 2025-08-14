#!/bin/bash

# Script para analizar el frontend de desarrollo con SonarQube (sin cobertura)
echo "🔍 Analizando Frontend Hospital - DEV con SonarQube..."

# Configuración
SONAR_CONFIG="sonar-project-frontend-dev.properties"

# Verificar que existe el archivo de configuración
if [ ! -f "$SONAR_CONFIG" ]; then
    echo "❌ Error: No se encuentra el archivo de configuración $SONAR_CONFIG"
    exit 1
fi

# Verificar si sonar-scanner está instalado
if ! command -v sonar-scanner &> /dev/null; then
    echo "⚠️  Advertencia: sonar-scanner no está instalado globalmente"
    echo "🐳 Intentando usar sonar-scanner via Docker..."
    
    # Usar sonar-scanner via Docker
    docker run --rm \
        --network host \
        -v "$(pwd):/usr/src" \
        sonarsource/sonar-scanner-cli:latest \
        -Dproject.settings=/usr/src/$SONAR_CONFIG
else
    echo "📊 Ejecutando análisis SonarQube con sonar-scanner local..."
    # Usar sonar-scanner local
    sonar-scanner -Dproject.settings=$SONAR_CONFIG
fi

echo "✅ Análisis de Frontend DEV completado (sin cobertura)"
echo "🌐 Ver resultados en: http://localhost:9000/dashboard?id=hospital-frontend-dev"
echo "ℹ️  Nota: Este análisis no incluye métricas de cobertura como solicitado"
