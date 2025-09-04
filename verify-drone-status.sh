#!/bin/bash

# Script para verificar el estado de Drone usando las credenciales de GitHub
echo "🔍 Verificando estado de Drone en la nube..."

# Credenciales de Drone
DRONE_CLIENT_ID="Iv23liU88Qhzg9tDXE8X"
DRONE_CLIENT_SECRET="8e14d52e92b9a440ef812770ee837cf885b23973"
DRONE_URL="http://104.197.237.11:8002"

echo "📊 Información de la aplicación Drone:"
echo "   App ID: 1882558"
echo "   Client ID: $DRONE_CLIENT_ID"
echo "   URL: $DRONE_URL"
echo "   Owner: @humbertovenavente"

echo ""
echo "🌐 Verificando conectividad con Drone..."
if curl -s --connect-timeout 10 "$DRONE_URL" > /dev/null; then
    echo "✅ Drone está accesible en $DRONE_URL"
    echo "🔗 Puedes acceder a: $DRONE_URL/humbertovenavente/Hospital"
else
    echo "❌ No se puede conectar a Drone en $DRONE_URL"
    echo "   Verifica que el servidor esté ejecutándose"
fi

echo ""
echo "📋 Para mantener Drone siempre disponible:"
echo "   1. Configura un servicio systemd en el servidor"
echo "   2. Usa 'restart: unless-stopped' en docker-compose"
echo "   3. Configura monitoreo de salud del servicio"
echo "   4. Considera usar un balanceador de carga"

echo ""
echo "🔧 Comandos útiles para el servidor remoto:"
echo "   # Verificar contenedores: docker ps | grep drone"
echo "   # Ver logs: docker logs drone-server"
echo "   # Reiniciar: docker-compose -f docker-compose.drone-simple.yml restart"
echo "   # Estado del servicio: systemctl status drone.service"
