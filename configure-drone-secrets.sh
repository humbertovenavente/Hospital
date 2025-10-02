#!/bin/bash

# Script para configurar secretos en Drone CI
# Ejecutar después de activar el repositorio

set -e

echo "🔧 Configurando secretos en Drone CI..."

# Configurar variables de entorno
export DRONE_SERVER=http://34.61.228.49:8002

# Verificar que el token esté configurado
if [ -z "$DRONE_TOKEN" ]; then
    echo "❌ Error: DRONE_TOKEN no está configurado"
    echo "Por favor, obtén un token desde la interfaz web de Drone:"
    echo "1. Ve a http://34.61.228.49:8002"
    echo "2. Haz login con GitHub"
    echo "3. Ve a tu perfil y genera un token"
    echo "4. Ejecuta: export DRONE_TOKEN=tu_token_aqui"
    exit 1
fi

echo "✅ Token configurado: ${DRONE_TOKEN:0:10}..."

# Configurar secretos del repositorio
echo "🔐 Configurando secretos para humbertovenavente/Hospital..."

# GitHub Token
drone secret add --repository humbertovenavente/Hospital --name GITHUB_TOKEN --data "Mg5tpUEVScyUNBGv3o5GyVbnz6ctsD2A"

# SonarQube Token
drone secret add --repository humbertovenavente/Hospital --name SONAR_TOKEN --data "sqa_9e95b3a3e0b243715a9b438fb7a08c1060e7123a"

# SonarQube Host URL
drone secret add --repository humbertovenavente/Hospital --name SONAR_HOST_URL --data "http://34.61.228.49:9003"

# Grok API Key
drone secret add --repository humbertovenavente/Hospital --name GROK_API_KEY --data "2y0VGgZEKDSVsjkImjiNE1feiRd_38io6TPdS9To8E717e8rb"

# Grok Base URL
drone secret add --repository humbertovenavente/Hospital --name GROK_BASE_URL --data "https://api.groq.com/openai/v1"

# Docker Registry
drone secret add --repository humbertovenavente/Hospital --name DOCKER_REGISTRY --data "34.61.228.49:5000"

# Slack Webhook (opcional)
drone secret add --repository humbertovenavente/Hospital --name SLACK_WEBHOOK --data "your_slack_webhook_here"

echo "✅ Secretos configurados exitosamente!"
echo ""
echo "📋 Secretos configurados:"
echo "  - GITHUB_TOKEN"
echo "  - SONAR_TOKEN"
echo "  - SONAR_HOST_URL"
echo "  - GROK_API_KEY"
echo "  - GROK_BASE_URL"
echo "  - DOCKER_REGISTRY"
echo "  - SLACK_WEBHOOK"
echo ""
echo "🚀 El repositorio está listo para ejecutar pipelines!"
