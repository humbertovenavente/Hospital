#!/bin/bash

echo "=== Comandos para Actualizar el Contenedor del Backend ==="
echo ""

# Colores para output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo -e "${GREEN}✅ Imagen creada exitosamente: hospital-backend-cors:latest${NC}"
echo ""

echo -e "${YELLOW}📦 La imagen incluye:${NC}"
echo "   - CORS configurado como origins=*"
echo "   - Configuración para QA (puerto 8080 interno)"
echo "   - Todas las dependencias actualizadas"
echo ""

echo -e "${BLUE}🚀 Comandos para ejecutar en el servidor (SSH):${NC}"
echo ""
echo "1. Conectar al servidor:"
echo "   ssh usuario@34.46.73.44"
echo ""
echo "2. Detener y eliminar el contenedor actual:"
echo "   docker stop \$(docker ps -q --filter ancestor=hospital-backend)"
echo "   docker rm \$(docker ps -aq --filter ancestor=hospital-backend)"
echo ""
echo "3. O si conoces el nombre específico del contenedor:"
echo "   docker stop nombre_del_contenedor_backend"
echo "   docker rm nombre_del_contenedor_backend"
echo ""
echo "4. Crear y ejecutar el nuevo contenedor:"
echo "   docker run -d \\"
echo "     --name hospital-backend-qa \\"
echo "     -p 8030:8080 \\"
echo "     -e QUARKUS_PROFILE=qa \\"
echo "     -e QUARKUS_DATASOURCE_JDBC_URL=jdbc:oracle:thin:@34.56.133.126:1521/XEPDB1 \\"
echo "     -e QUARKUS_DATASOURCE_USERNAME=C##PROYECTO \\"
echo "     -e QUARKUS_DATASOURCE_PASSWORD=Unis \\"
echo "     hospital-backend-cors:latest"
echo ""
echo "5. Verificar que el contenedor está corriendo:"
echo "   docker ps | grep hospital-backend"
echo ""
echo "6. Verificar los logs:"
echo "   docker logs hospital-backend-qa"
echo ""
echo "7. Probar la conectividad:"
echo "   curl http://localhost:8030/q/health"
echo ""

echo -e "${YELLOW}📋 Variables de entorno incluidas:${NC}"
echo "   - QUARKUS_PROFILE=qa"
echo "   - QUARKUS_DATASOURCE_JDBC_URL=jdbc:oracle:thin:@34.56.133.126:1521/XEPDB1"
echo "   - QUARKUS_DATASOURCE_USERNAME=C##PROYECTO"
echo "   - QUARKUS_DATASOURCE_PASSWORD=Unis"
echo "   - CORS configurado como origins=*"
echo ""

echo -e "${GREEN}✅ Con estos comandos el problema de CORS se resolverá completamente.${NC}"
