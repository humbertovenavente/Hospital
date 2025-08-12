#!/bin/bash

echo "=== VERIFICACIÓN DEL SERVICIO SONARQUBE ==="
echo "Timestamp: $(date)"
echo ""

# Verificar estado del servicio systemd
echo "1. Estado del servicio systemd:"
if sudo systemctl is-active sonarqube.service > /dev/null; then
    echo "   ✅ Servicio activo"
else
    echo "   ❌ Servicio inactivo"
fi

if sudo systemctl is-enabled sonarqube.service > /dev/null; then
    echo "   ✅ Servicio habilitado (auto-inicio)"
else
    echo "   ❌ Servicio no habilitado"
fi

echo ""

# Verificar contenedor Docker
echo "2. Estado del contenedor Docker:"
if docker ps | grep -q sonarqube; then
    echo "   ✅ Contenedor corriendo"
    docker ps | grep sonarqube | awk '{print "   Puerto: " $9 " | Estado: " $8}'
else
    echo "   ❌ Contenedor no está corriendo"
fi

echo ""

# Verificar conectividad web
echo "3. Conectividad web:"
if curl -s http://localhost:9000/api/system/status > /dev/null; then
    echo "   ✅ SonarQube responde en http://localhost:9000"
    STATUS=$(curl -s http://localhost:9000/api/system/status | grep -o '"status":"[^"]*"' | cut -d'"' -f4)
    echo "   Estado: $STATUS"
else
    echo "   ❌ SonarQube no responde en http://localhost:9000"
fi

echo ""

# Verificar logs del servicio
echo "4. Últimos logs del servicio:"
sudo journalctl -u sonarqube.service --no-pager -n 5 | grep -E "(Starting|Started|Stopping|Stopped|ERROR|WARN)" | tail -5

echo ""

echo "=== COMANDOS ÚTILES ==="
echo "• Ver estado del servicio: sudo systemctl status sonarqube.service"
echo "• Iniciar servicio: sudo systemctl start sonarqube.service"
echo "• Detener servicio: sudo systemctl stop sonarqube.service"
echo "• Reiniciar servicio: sudo systemctl restart sonarqube.service"
echo "• Ver logs: sudo journalctl -u sonarqube.service -f"
echo "• Acceder a SonarQube: http://localhost:9000"
echo ""

echo "=== FIN DE VERIFICACIÓN ==="
