#!/bin/bash

# Script para iniciar túneles ngrok usando configuración
echo "🚀 Iniciando túneles ngrok usando configuración..."
echo "⏰ $(date)"

# Verificar si Jenkins está corriendo
if ! curl -s "http://localhost:8081" >/dev/null 2>&1; then
    echo "❌ Jenkins no está disponible en puerto 8081"
    echo "💡 Asegúrate de que Jenkins esté ejecutándose"
    exit 1
fi

# Verificar si SonarQube está corriendo
if ! curl -s "http://localhost:9000" >/dev/null 2>&1; then
    echo "❌ SonarQube no está disponible en puerto 9000"
    echo "💡 Asegúrate de que SonarQube esté ejecutándose"
    exit 1
fi

# Verificar si ngrok ya está corriendo
if pgrep -f "ngrok start --all" >/dev/null; then
    echo "✅ ngrok ya está ejecutándose con configuración"
else
    # Iniciar ngrok con configuración
    echo "🔧 Iniciando ngrok con configuración..."
    ngrok start --all --config ngrok.yml >/tmp/ngrok-dual.log 2>&1 &
    sleep 10
fi

# Obtener URLs públicas
echo "⏳ Obteniendo URLs públicas..."
sleep 5

JENKINS_URL=$(curl -s http://localhost:4040/api/tunnels | jq -r '.tunnels[] | select(.name == "jenkins") | .public_url' 2>/dev/null)
SONARQUBE_URL=$(curl -s http://localhost:4040/api/tunnels | jq -r '.tunnels[] | select(.name == "sonarqube") | .public_url' 2>/dev/null)

echo ""
echo "🌐 TÚNELES CONFIGURADOS:"
echo "========================="

if [ "$JENKINS_URL" != "null" ] && [ "$JENKINS_URL" != "" ]; then
    echo "✅ Jenkins: $JENKINS_URL"
    echo "🔗 Webhook URL: $JENKINS_URL/github-webhook/"
    echo "$JENKINS_URL" > /tmp/ngrok-jenkins-url.txt
else
    echo "❌ Error obteniendo URL de Jenkins"
fi

if [ "$SONARQUBE_URL" != "null" ] && [ "$SONARQUBE_URL" != "" ]; then
    echo "✅ SonarQube: $SONARQUBE_URL"
    echo "$SONARQUBE_URL" > /tmp/ngrok-sonarqube-url.txt
else
    echo "❌ Error obteniendo URL de SonarQube"
fi

echo ""
echo "📋 URLs guardadas en:"
echo "   • /tmp/ngrok-jenkins-url.txt"
echo "   • /tmp/ngrok-sonarqube-url.txt"

# Mostrar notificación del sistema
if command -v notify-send >/dev/null 2>&1; then
    notify-send "ngrok Dual Tunnel" "Jenkins: $JENKINS_URL\nSonarQube: $SONARQUBE_URL"
fi

echo ""
echo "🎯 PRÓXIMOS PASOS:"
echo "1. Actualizar webhook en GitHub con: $JENKINS_URL/github-webhook/"
echo "2. El Jenkinsfile ya está configurado para usar SonarQube automáticamente"
echo "3. Probar pipeline para ver métricas reales de SonarQube"
echo ""
echo "🔧 Comandos útiles:"
echo "   • ngrok-status      - Ver estado de túneles"
echo "   • ngrok-stop        - Detener túneles"
echo "   • ./start-ngrok-config.sh - Reiniciar túneles"
