#!/bin/bash

# Script para configurar Drone como servicio en servidor remoto
echo "🚀 Configurando Drone como servicio en servidor remoto..."

# Verificar si docker-compose está instalado
if ! command -v docker-compose &> /dev/null; then
    echo "❌ docker-compose no está instalado. Instalando..."
    curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
    chmod +x /usr/local/bin/docker-compose
fi

# Crear archivo de servicio systemd
sudo tee /etc/systemd/system/drone.service > /dev/null <<EOF
[Unit]
Description=Drone CI/CD Server
Requires=docker.service
After=docker.service

[Service]
Type=oneshot
RemainAfterExit=yes
WorkingDirectory=/root/Hospital
ExecStart=/usr/local/bin/docker-compose -f docker-compose.drone-simple.yml up -d
ExecStop=/usr/local/bin/docker-compose -f docker-compose.drone-simple.yml down
TimeoutStartSec=0

[Install]
WantedBy=multi-user.target
EOF

# Recargar systemd
sudo systemctl daemon-reload

# Habilitar el servicio
sudo systemctl enable drone.service

# Iniciar el servicio
sudo systemctl start drone.service

# Verificar estado
echo "📊 Estado del servicio Drone:"
sudo systemctl status drone.service --no-pager

echo "✅ Drone configurado como servicio. Se iniciará automáticamente al reiniciar el servidor."
echo "🌐 Accede a: http://104.197.237.11:8002"
