#!/bin/bash

echo "=== Debugging Container Restart Issue ==="
echo ""

# Colores para output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo -e "${RED}⚠️  Problema detectado: El contenedor se está reiniciando constantemente${NC}"
echo ""

echo -e "${YELLOW}🔍 Comandos para diagnosticar el problema:${NC}"
echo ""

echo "1. Ver logs completos del contenedor:"
echo "   sudo docker logs hospital-backend-qa-cloud --details"
echo ""

echo "2. Ver logs en tiempo real para capturar el error:"
echo "   sudo docker logs -f hospital-backend-qa-cloud"
echo ""

echo "3. Verificar si el contenedor está en loop de reinicio:"
echo "   sudo docker ps -a | grep hospital-backend"
echo ""

echo "4. Verificar el estado del contenedor:"
echo "   sudo docker inspect hospital-backend-qa-cloud | grep -A 5 -B 5 RestartCount"
echo ""

echo -e "${BLUE}💡 Posibles causas:${NC}"
echo "   - Error de conexión a la base de datos"
echo "   - Configuración incorrecta de Quarkus"
echo "   - Puerto 8080 ya está en uso dentro del contenedor"
echo "   - Variables de entorno incorrectas"
echo ""

echo -e "${YELLOW}🔧 Solución temporal - Usar configuración más simple:${NC}"
echo "   sudo docker stop hospital-backend-qa-cloud"
echo "   sudo docker rm hospital-backend-qa-cloud"
echo "   sudo docker run -d \\"
echo "     --name hospital-backend-qa-cloud \\"
echo "     -p 8030:8080 \\"
echo "     -e QUARKUS_PROFILE=qa \\"
echo "     -e QUARKUS_DATASOURCE_JDBC_URL=jdbc:oracle:thin:@34.56.133.126:1521/XE \\"
echo "     -e QUARKUS_DATASOURCE_USERNAME=system \\"
echo "     -e QUARKUS_DATASOURCE_PASSWORD=Oracle123 \\"
echo "     -e QUARKUS_HTTP_CORS=true \\"
echo "     -e QUARKUS_HTTP_CORS_ORIGINS=* \\"
echo "     humbertovenavente/hospital-backend-qa:latest"
echo ""

echo -e "${GREEN}✅ Ejecuta primero los comandos de diagnóstico para ver el error específico.${NC}"
