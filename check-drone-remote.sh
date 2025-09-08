scp setup-drone-service-remote.sh root@104.197.237.11:/root/
#!/bin/bash

# Script para verificar el estado de Drone en servidor remoto
echo "🔍 Verificando estado de Drone en servidor remoto..."

# Verificar contenedores Docker
echo "📦 Contenedores Docker:"
ssh root@104.197.237.11 "docker ps | grep drone"

echo ""
echo "🌐 Estado del servicio systemd:"
ssh root@104.197.237.11 "sudo systemctl status drone.service --no-pager"

echo ""
echo "📊 Logs recientes de Drone:"
ssh root@104.197.237.11 "docker logs --tail 20 drone-server"

echo ""
echo "🔗 URL de Drone: http://104.197.237.11:8002"
