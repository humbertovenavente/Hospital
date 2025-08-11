#!/bin/bash

# Script robusto para ejecutar análisis de SonarQube
# Maneja problemas de timeout y bridge server

set -e

echo "🔍 Iniciando análisis de SonarQube robusto..."

# Configuración de variables
SONAR_HOST="${SONAR_HOST:-http://localhost:9000}"
SONAR_TOKEN="${SONAR_TOKEN:-}"
PROJECT_KEY="${1:-hospital-frontend}"
PROJECT_NAME="${2:-Hospital Frontend - Vue.js/TypeScript}"
PROJECT_VERSION="${3:-1.0}"

# Verificar que SonarQube esté disponible
echo "=== Verificando SonarQube ==="
if ! curl -f "${SONAR_HOST}/api/system/status" > /dev/null 2>&1; then
    echo "❌ SonarQube no está disponible en ${SONAR_HOST}"
    exit 1
fi

echo "✅ SonarQube está disponible"

# Verificar que SonarQube Scanner esté disponible
echo "=== Verificando SonarQube Scanner ==="
if ! command -v sonar-scanner > /dev/null 2>&1; then
    echo "❌ SonarQube Scanner no está disponible"
    exit 1
fi

echo "✅ SonarQube Scanner está disponible"

# Función para ejecutar análisis con reintentos
run_analysis() {
    local max_retries=3
    local retry_count=0
    
    while [ $retry_count -lt $max_retries ]; do
        echo "🔄 Intento $((retry_count + 1)) de $max_retries"
        
        if sonar-scanner \
            -Dsonar.projectKey="${PROJECT_KEY}" \
            -Dsonar.projectName="${PROJECT_NAME}" \
            -Dsonar.projectVersion="${PROJECT_VERSION}" \
            -Dsonar.sources=src \
            -Dsonar.javascript.lcov.reportsPaths=coverage/lcov.info \
            -Dsonar.typescript.lcov.reportsPaths=coverage/lcov.info \
            -Dsonar.host.url="${SONAR_HOST}" \
            -Dsonar.token="${SONAR_TOKEN}" \
            -Dsonar.exclusions="**/node_modules/**,**/dist/**,**/coverage/**,**/*.min.js,**/*.min.css,**/e2e/**,**/public/**" \
            -Dsonar.qualitygate.wait=true \
            -Dsonar.javascript.timeout=300000 \
            -Dsonar.typescript.timeout=300000 \
            -Dsonar.javascript.bridge.timeout=300000 \
            -Dsonar.javascript.bridge.connectionTimeout=300000 \
            -Dsonar.javascript.bridge.readTimeout=300000 \
            -Dsonar.javascript.bridge.serverTimeout=300000 \
            -Dsonar.javascript.bridge.keepAlive=true \
            -Dsonar.javascript.bridge.maxRetries=3 \
            -Dsonar.javascript.bridge.memory=2048 \
            -Dsonar.javascript.bridge.maxMemory=4096; then
            
            echo "✅ Análisis completado exitosamente"
            return 0
        else
            retry_count=$((retry_count + 1))
            if [ $retry_count -lt $max_retries ]; then
                echo "⚠️  Intento falló, esperando antes del siguiente intento..."
                sleep 30
            fi
        fi
    done
    
    echo "❌ Todos los intentos fallaron"
    return 1
}

# Ejecutar análisis
echo "=== Ejecutando análisis de SonarQube ==="
if run_analysis; then
    echo "🎉 Análisis de SonarQube completado exitosamente"
    echo "📊 Resultados disponibles en: ${SONAR_HOST}/dashboard?id=${PROJECT_KEY}"
else
    echo "❌ Análisis de SonarQube falló después de múltiples intentos"
    exit 1
fi


