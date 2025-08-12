#!/bin/bash

echo "=== INSTALACIÓN DEL SERVICIO SONARQUBE ==="
echo "Este script configurará SonarQube para iniciar automáticamente"
echo ""

# Verificar si Docker está instalado
if ! command -v docker &> /dev/null; then
    echo "❌ Docker no está instalado. Por favor instala Docker primero."
    exit 1
fi

# Verificar si el contenedor de SonarQube existe
if ! docker ps -a | grep -q sonarqube; then
    echo "❌ Contenedor de SonarQube no encontrado."
    echo "   Ejecuta primero: docker run -d --name sonarqube -p 9000:9000 -e SONAR_ES_BOOTSTRAP_CHECKS_DISABLE=true sonarqube:10.4.1-community"
    exit 1
fi

echo "✅ Verificaciones previas completadas"
echo ""

# Crear el archivo de servicio
echo "1. Creando archivo de servicio systemd..."
sudo tee /etc/systemd/system/sonarqube.service > /dev/null << 'EOF'
[Unit]
Description=SonarQube Service
Documentation=https://docs.sonarqube.org
After=docker.service
Requires=docker.service

[Service]
Type=oneshot
RemainAfterExit=yes
ExecStart=/usr/bin/docker start sonarqube
ExecStop=/usr/bin/docker stop sonarqube
ExecReload=/usr/bin/docker restart sonarqube
TimeoutStartSec=0

[Install]
WantedBy=multi-user.target
EOF

echo "   ✅ Archivo de servicio creado en /etc/systemd/system/sonarqube.service"
echo ""

# Recargar configuración de systemd
echo "2. Recargando configuración de systemd..."
sudo systemctl daemon-reload
echo "   ✅ Configuración recargada"
echo ""

# Habilitar el servicio
echo "3. Habilitando servicio para auto-inicio..."
sudo systemctl enable sonarqube.service
echo "   ✅ Servicio habilitado"
echo ""

# Iniciar el servicio
echo "4. Iniciando servicio..."
sudo systemctl start sonarqube.service
echo "   ✅ Servicio iniciado"
echo ""

# Verificar estado
echo "5. Verificando estado del servicio..."
sleep 5
if sudo systemctl is-active sonarqube.service > /dev/null; then
    echo "   ✅ Servicio funcionando correctamente"
else
    echo "   ❌ Error al iniciar el servicio"
    sudo systemctl status sonarqube.service
    exit 1
fi

echo ""

# Verificar que SonarQube esté respondiendo
echo "6. Verificando conectividad de SonarQube..."
sleep 10
if curl -s http://localhost:9000/api/system/status > /dev/null; then
    echo "   ✅ SonarQube responde correctamente"
    STATUS=$(curl -s http://localhost:9000/api/system/status | grep -o '"status":"[^"]*"' | cut -d'"' -f4)
    echo "   Estado: $STATUS"
else
    echo "   ⚠️  SonarQube aún no responde (puede tardar unos minutos en inicializarse)"
fi

echo ""
echo "=== INSTALACIÓN COMPLETADA ==="
echo "✅ SonarQube se iniciará automáticamente en cada reinicio"
echo ""
echo "=== COMANDOS ÚTILES ==="
echo "• Ver estado: sudo systemctl status sonarqube.service"
echo "• Iniciar: sudo systemctl start sonarqube.service"
echo "• Detener: sudo systemctl stop sonarqube.service"
echo "• Reiniciar: sudo systemctl restart sonarqube.service"
echo "• Ver logs: sudo journalctl -u sonarqube.service -f"
echo "• Acceder: http://localhost:9000"
echo ""
echo "=== VERIFICACIÓN ==="
echo "Para verificar que todo funcione, ejecuta: ./check-sonarqube-service.sh"
echo ""
echo "¡SonarQube está configurado para iniciar automáticamente! 🎉"
