#!/bin/bash

# Script para verificar el estado de SonarQube
echo "🔍 Verificando estado de SonarQube..."

# Verificar contenedores Docker
echo "🐳 Contenedores Docker:"
docker ps | grep sonarqube || echo "❌ No hay contenedores de SonarQube ejecutándose"

# Verificar puerto 9000
echo ""
echo "🌐 Verificando puerto 9000:"
if netstat -tlnp | grep :9000; then
    echo "✅ Puerto 9000 está en uso"
else
    echo "❌ Puerto 9000 no está en uso"
fi

# Verificar conectividad
echo ""
echo "📡 Verificando conectividad:"
if curl -s --connect-timeout 5 http://localhost:9000 > /dev/null; then
    echo "✅ SonarQube responde en http://localhost:9000"
    echo "🌐 Acceso externo: http://104.197.237.11:9000"
else
    echo "❌ SonarQube no responde en http://localhost:9000"
fi

# Verificar logs
echo ""
echo "📋 Logs recientes de SonarQube:"
docker logs --tail 10 sonarqube 2>/dev/null || echo "❌ No se pueden obtener logs"

echo ""
echo "🔧 Para iniciar SonarQube:"
echo "   ./quick-start-sonarqube.sh"
