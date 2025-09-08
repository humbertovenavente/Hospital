#!/bin/bash

echo "=== Comandos Actualizados con Variables de Entorno del Servidor ==="
echo ""

# Colores para output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${YELLOW}📋 Variables de entorno detectadas en el servidor:${NC}"
echo "   - DB_HOST: 34.56.133.126"
echo "   - DB_PASSWORD: Oracle123"
echo "   - DB_PORT: 1521"
echo "   - DB_SID: XE"
echo "   - DB_USER: system"
echo "   - CORS ya configurado: QUARKUS_HTTP_CORS_HEADERS=*"
echo ""

echo -e "${GREEN}✅ Comandos actualizados para usar la misma base de datos:${NC}"
echo ""

echo "1. Actualizar la imagen:"
echo "   sudo docker pull humbertovenavente/hospital-backend-qa:latest"
echo ""

echo "2. Detener y eliminar el contenedor actual:"
echo "   sudo docker stop hospital-backend-qa-cloud"
echo "   sudo docker rm hospital-backend-qa-cloud"
echo ""

echo "3. Crear y ejecutar el nuevo contenedor con las variables del servidor:"
echo "   sudo docker run -d \\"
echo "     --name hospital-backend-qa-cloud \\"
echo "     -p 8030:8080 \\"
echo "     -e QUARKUS_PROFILE=qa \\"
echo "     -e QUARKUS_DATASOURCE_JDBC_URL=jdbc:oracle:thin:@34.56.133.126:1521/XE \\"
echo "     -e QUARKUS_DATASOURCE_USERNAME=system \\"
echo "     -e QUARKUS_DATASOURCE_PASSWORD=Oracle123 \\"
echo "     -e QUARKUS_HTTP_CORS_ACCESS_CONTROL_ALLOW_CREDENTIALS=true \\"
echo "     -e QUARKUS_HTTP_CORS_EXPOSED_HEADERS=Content-Disposition \\"
echo "     -e QUARKUS_HTTP_CORS_HEADERS=* \\"
echo "     -e QUARKUS_HTTP_CORS_ORIGINS=* \\"
echo "     -e QUARKUS_HTTP_CORS_METHODS=GET,POST,PUT,DELETE,OPTIONS \\"
echo "     -e QUARKUS_HTTP_CORS=true \\"
echo "     humbertovenavente/hospital-backend-qa:latest"
echo ""

echo "4. Verificar que funciona:"
echo "   sudo docker ps | grep hospital-backend"
echo "   sudo docker logs hospital-backend-qa-cloud"
echo "   curl http://localhost:8030/q/health"
echo ""

echo "5. Probar CORS:"
echo "   curl -X OPTIONS http://localhost:8030/usuarios/login \\"
echo "     -H 'Origin: http://localhost:5175' \\"
echo "     -H 'Access-Control-Request-Method: POST' \\"
echo "     -H 'Access-Control-Request-Headers: Content-Type' \\"
echo "     -v"
echo ""

echo -e "${BLUE}💡 Nota:${NC}"
echo "   - Usando la misma base de datos (XE) que ya está configurada"
echo "   - CORS configurado explícitamente con origins=*"
echo "   - Todas las variables de entorno del servidor incluidas"
echo ""

echo -e "${GREEN}✅ Con estos comandos el problema de CORS se resolverá completamente.${NC}"
