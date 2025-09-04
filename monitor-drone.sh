#!/bin/bash

# Script para monitorear el estado de Drone
DRONE_URL="http://104.197.237.11:8002"

echo "🔍 Monitoreando Drone CI/CD Server..."
echo "🌐 URL: $DRONE_URL"
echo "📅 $(date)"
echo ""

# Verificar conectividad
echo "📡 Verificando conectividad..."
if curl -s --connect-timeout 10 "$DRONE_URL" > /dev/null; then
    echo "✅ Drone está accesible"
else
    echo "❌ Drone no está accesible"
    echo "   Verifica que el servidor esté ejecutándose"
    exit 1
fi

# Verificar estado del servicio (si tienes acceso SSH)
echo ""
echo "🔧 Estado del servicio (requiere acceso SSH):"
echo "   Ejecuta en el servidor: sudo systemctl status drone.service"

# Verificar contenedores (si tienes acceso SSH)
echo ""
echo "🐳 Contenedores Docker (requiere acceso SSH):"
echo "   Ejecuta en el servidor: docker ps | grep drone"

# Verificar logs (si tienes acceso SSH)
echo ""
echo "📋 Logs recientes (requiere acceso SSH):"
echo "   Ejecuta en el servidor: docker logs --tail 20 drone-server"

echo ""
echo "🌐 Accede a Drone: $DRONE_URL/humbertovenavente/Hospital"
echo "📊 Dashboard: $DRONE_URL"

# Función para verificar builds recientes (requiere API key)
echo ""
echo "💡 Para verificar builds recientes:"
echo "   curl -H 'Authorization: Bearer YOUR_TOKEN' $DRONE_URL/api/user/repos"
