#!/bin/bash

# Script para configurar Drone como servicio persistente en servidor remoto
# Ejecutar este script en el servidor remoto (104.197.237.11)

echo "🚀 Configurando Drone como servicio persistente..."

# Verificar si estamos en el directorio correcto
if [ ! -f "docker-compose.drone-simple.yml" ]; then
    echo "❌ Error: No se encuentra docker-compose.drone-simple.yml"
    echo "   Asegúrate de estar en el directorio /root/Hospital o donde esté el proyecto"
    exit 1
fi

# Verificar si docker-compose está instalado
if ! command -v docker-compose &> /dev/null; then
    echo "📦 Instalando docker-compose..."
    curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
    chmod +x /usr/local/bin/docker-compose
    echo "✅ docker-compose instalado"
fi

# Detener contenedores actuales
echo "🛑 Deteniendo contenedores actuales..."
docker-compose -f docker-compose.drone-simple.yml down

# Crear archivo de servicio systemd
echo "📝 Creando servicio systemd..."
sudo tee /etc/systemd/system/drone.service > /dev/null <<EOF
[Unit]
Description=Drone CI/CD Server
Requires=docker.service
After=docker.service

[Service]
Type=oneshot
RemainAfterExit=yes
WorkingDirectory=$(pwd)
ExecStart=/usr/local/bin/docker-compose -f docker-compose.drone-simple.yml up -d
ExecStop=/usr/local/bin/docker-compose -f docker-compose.drone-simple.yml down
TimeoutStartSec=0

[Install]
WantedBy=multi-user.target
EOF

# Recargar systemd
echo "🔄 Recargando systemd..."
sudo systemctl daemon-reload

# Habilitar el servicio
echo "✅ Habilitando servicio para inicio automático..."
sudo systemctl enable drone.service

# Iniciar el servicio
echo "🚀 Iniciando servicio Drone..."
sudo systemctl start drone.service

# Verificar estado
echo "📊 Verificando estado del servicio..."
sudo systemctl status drone.service --no-pager

echo ""
echo "🎉 ¡Drone configurado como servicio persistente!"
echo "🌐 URL: http://104.197.237.11:8002"
echo "📱 Repositorio: http://104.197.237.11:8002/humbertovenavente/Hospital"
echo ""
echo "🔧 Comandos útiles:"
echo "   - Ver estado: sudo systemctl status drone.service"
echo "   - Reiniciar: sudo systemctl restart drone.service"
echo "   - Ver logs: docker logs drone-server"
echo "   - Detener: sudo systemctl stop drone.service"
