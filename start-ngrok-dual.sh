#!/bin/bash

# Script para iniciar dos túneles ngrok: Jenkins y SonarQube
echo "🚀 Iniciando túneles ngrok para Jenkins y SonarQube..."
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
if pgrep -f "ngrok http 8081" >/dev/null; then
    echo "✅ Túnel Jenkins ya está ejecutándose"
else
    # Iniciar túnel para Jenkins
    echo "🔧 Iniciando túnel ngrok para Jenkins (puerto 8081)..."
    ngrok http 8081 >/tmp/ngrok-jenkins.log 2>&1 &
    sleep 5
fi

if pgrep -f "ngrok http 9000" >/dev/null; then
    echo "✅ Túnel SonarQube ya está ejecutándose"
else
    # Iniciar túnel para SonarQube
    echo "🔧 Iniciando túnel ngrok para SonarQube (puerto 9000)..."
    ngrok http 9000 >/tmp/ngrok-sonarqube.log 2>&1 &
    sleep 5
fi

# Esperar a que ambos túneles se inicien
echo "⏳ Esperando que los túneles se inicien..."
sleep 10

# Obtener URLs públicas
JENKINS_URL=$(curl -s http://localhost:4040/api/tunnels | jq -r '.tunnels[] | select(.config.addr == "http://localhost:8081") | .public_url' 2>/dev/null)
SONARQUBE_URL=$(curl -s http://localhost:4040/api/tunnels | jq -r '.tunnels[] | select(.config.addr == "http://localhost:9000") | .public_url' 2>/dev/null)

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
echo "2. Actualizar Jenkinsfile con URL de SonarQube: $SONARQUBE_URL"
echo "3. Probar pipeline para ver métricas reales"
