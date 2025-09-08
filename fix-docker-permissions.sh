#!/bin/bash

echo "=== Comandos Corregidos para Docker con Sudo ==="
echo ""

# Colores para output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo -e "${RED}⚠️  Problema detectado: Permisos de Docker${NC}"
echo "   Necesitas usar 'sudo' para ejecutar comandos de Docker"
echo ""

echo -e "${YELLOW}🔧 Comandos corregidos para ejecutar en el servidor:${NC}"
echo ""

echo "1. Detener y eliminar el contenedor actual:"
echo "   sudo docker stop \$(sudo docker ps -q --filter ancestor=hospital-backend)"
echo "   sudo docker rm \$(sudo docker ps -aq --filter ancestor=hospital-backend)"
echo ""

echo "2. O si prefieres ver los contenedores primero:"
echo "   sudo docker ps -a | grep hospital"
echo "   sudo docker stop nombre_del_contenedor"
echo "   sudo docker rm nombre_del_contenedor"
echo ""

echo "3. Crear y ejecutar el nuevo contenedor:"
echo "   sudo docker run -d \\"
echo "     --name hospital-backend-qa \\"
echo "     -p 8030:8080 \\"
echo "     -e QUARKUS_PROFILE=qa \\"
echo "     -e QUARKUS_DATASOURCE_JDBC_URL=jdbc:oracle:thin:@34.56.133.126:1521/XEPDB1 \\"
echo "     -e QUARKUS_DATASOURCE_USERNAME=C##PROYECTO \\"
echo "     -e QUARKUS_DATASOURCE_PASSWORD=Unis \\"
echo "     hospital-backend-cors:latest"
echo ""

echo "4. Verificar que funciona:"
echo "   sudo docker ps | grep hospital-backend"
echo "   sudo docker logs hospital-backend-qa"
echo "   curl http://localhost:8030/q/health"
echo ""

echo -e "${BLUE}💡 Alternativa: Agregar tu usuario al grupo docker${NC}"
echo "   sudo usermod -aG docker \$USER"
echo "   # Luego cerrar sesión y volver a conectar"
echo ""

echo -e "${GREEN}✅ El backend ya está funcionando (viste el health check), solo necesitas actualizar el contenedor.${NC}"
