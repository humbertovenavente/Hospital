#!/bin/bash

# Script de despliegue del sistema de monitoreo con alertas
# Incluye Prometheus, Alertmanager, Grafana, Node Exporter y cAdvisor

set -e

echo "🚀 Desplegando sistema de monitoreo con alertas..."
echo ""

# Verificar Docker
if ! command -v docker &> /dev/null; then
    echo "❌ Docker no está instalado. Por favor, instala Docker primero."
    exit 1
fi

# Verificar Docker Compose
if command -v docker-compose &> /dev/null; then
    DC="docker-compose"
elif docker compose version &> /dev/null; then
    DC="docker compose"
else
    echo "❌ Docker Compose no está instalado."
    exit 1
fi

echo "✅ Docker y Docker Compose están instalados"
echo ""

# Solicitar variables de entorno si no están configuradas
if [ -z "$GMAIL_USER" ]; then
    read -p "📧 Ingresa tu email de Gmail (para notificaciones): " GMAIL_USER
    export GMAIL_USER
fi

if [ -z "$GMAIL_PASSWORD" ]; then
    read -sp "🔐 Ingresa tu App Password de Gmail (se ocultará mientras escribes): " GMAIL_PASSWORD
    echo ""
    export GMAIL_PASSWORD
fi

if [ -z "$GMAIL_FROM_ADDRESS" ]; then
    export GMAIL_FROM_ADDRESS="$GMAIL_USER"
fi

echo ""
echo "📧 Configuración de email:"
echo "   From: $GMAIL_FROM_ADDRESS"
echo "   To: jflores@unis.edu.gt, jnajar@unis.edu.gt"
echo ""

# Preguntar sobre Slack
if [ -z "$SLACK_WEBHOOK_URL" ]; then
    read -p "💬 ¿Deseas configurar notificaciones de Slack? (s/n): " CONFIGURE_SLACK
    if [ "$CONFIGURE_SLACK" == "s" ]; then
        read -p "🔗 Ingresa tu Slack Webhook URL: " SLACK_WEBHOOK_URL
        export SLACK_WEBHOOK_URL
    fi
fi

# Crear archivo .env para Docker Compose
cat > .env.monitoring <<EOF
GMAIL_USER=$GMAIL_USER
GMAIL_PASSWORD=$GMAIL_PASSWORD
GMAIL_FROM_ADDRESS=$GMAIL_FROM_ADDRESS
EOF

if [ ! -z "$SLACK_WEBHOOK_URL" ]; then
    echo "SLACK_WEBHOOK_URL=$SLACK_WEBHOOK_URL" >> .env.monitoring
fi

echo ""
echo "📝 Verificando archivos de configuración..."
echo ""

# Verificar que los archivos de configuración existen
REQUIRED_FILES=(
    "prometheus.yml"
    "prometheus-alerts.yml"
    "alertmanager.yml"
    "grafana-provisioning-datasources.yml"
    "grafana-alerts-notification-channels.yml"
    "grafana-alert-rules.yml"
    "docker-compose.monitoring.yml"
)

for file in "${REQUIRED_FILES[@]}"; do
    if [ ! -f "$file" ]; then
        echo "❌ Archivo no encontrado: $file"
        exit 1
    fi
done

echo "✅ Todos los archivos de configuración están presentes"
echo ""

# Detener contenedores existentes si existen
echo "🛑 Deteniendo contenedores existentes..."
$DC -f docker-compose.monitoring.yml down 2>/dev/null || true
echo ""

# Iniciar el stack de monitoreo
echo "🐳 Iniciando stack de monitoreo..."
$DC -f docker-compose.monitoring.yml up -d

# Esperar a que los servicios estén listos
echo ""
echo "⏳ Esperando a que los servicios estén listos..."
sleep 10

# Verificar estado de los servicios
echo ""
echo "📊 Estado de los servicios:"
$DC -f docker-compose.monitoring.yml ps

echo ""
echo "✅ Sistema de monitoreo desplegado exitosamente!"
echo ""
echo "🌐 URLs de acceso:"
echo "   - Prometheus: http://localhost:9090"
echo "   - Alertmanager: http://localhost:9093"
echo "   - Grafana: http://localhost:3000"
echo "   - Node Exporter: http://localhost:9100/metrics"
echo "   - cAdvisor: http://localhost:8080"
echo ""
echo "👤 Credenciales de Grafana:"
echo "   Usuario: admin"
echo "   Contraseña: admin123"
echo ""
echo "📋 Configuración de alertas:"
echo "   ✓ Alertas de CPU (umbral: 70%)"
echo "   ✓ Alertas de memoria (umbral: 80%)"
echo "   ✓ Alertas de disco (umbral: 20%)"
echo "   ✓ Alertas del backend (disponibilidad, errores, latencia)"
echo "   ✓ Alertas del frontend (disponibilidad)"
echo "   ✓ Alertas de CI/CD (Drone)"
echo ""
echo "📧 Notificaciones:"
echo "   ✓ Email configurado: $GMAIL_USER"
if [ ! -z "$SLACK_WEBHOOK_URL" ]; then
    echo "   ✓ Slack configurado"
else
    echo "   - Slack no configurado"
fi
echo ""
echo "📖 Para más información, ver README-ALERTAS.md"
echo ""
echo "🔍 Para verificar que las alertas están funcionando:"
echo "   curl http://localhost:9090/api/v1/alerts"
echo ""
echo "🔍 Para ver logs de los servicios:"
echo "   docker logs hospital-prometheus"
echo "   docker logs hospital-alertmanager"
echo "   docker logs hospital-grafana"
echo ""

