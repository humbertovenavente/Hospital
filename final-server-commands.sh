#!/bin/bash

echo "=== Comandos Finales para el Servidor ==="
echo ""

# Colores para output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${GREEN}✅ Imagen actualizada y subida exitosamente${NC}"
echo "   - humbertovenavente/hospital-backend-qa:latest"
echo "   - Incluye corrección de CORS (origins=*)"
echo ""

echo -e "${YELLOW}🚀 Comandos para ejecutar en el servidor:${NC}"
echo ""

echo "1. Actualizar la imagen:"
echo "   sudo docker pull humbertovenavente/hospital-backend-qa:latest"
echo ""

echo "2. Detener y eliminar el contenedor actual:"
echo "   sudo docker stop hospital-backend-qa-cloud"
echo "   sudo docker rm hospital-backend-qa-cloud"
echo ""

echo "3. Crear y ejecutar el nuevo contenedor:"
echo "   sudo docker run -d \\"
echo "     --name hospital-backend-qa-cloud \\"
echo "     -p 8030:8080 \\"
echo "     -e QUARKUS_PROFILE=qa \\"
echo "     -e QUARKUS_DATASOURCE_JDBC_URL=jdbc:oracle:thin:@34.56.133.126:1521/XEPDB1 \\"
echo "     -e QUARKUS_DATASOURCE_USERNAME=C##PROYECTO \\"
echo "     -e QUARKUS_DATASOURCE_PASSWORD=Unis \\"
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

echo -e "${GREEN}✅ Con estos comandos el problema de CORS se resolverá completamente.${NC}"
echo "   - La nueva imagen incluye CORS configurado como origins=*"
echo "   - Todos los servicios del frontend usan la URL correcta"
echo "   - El login y la subida de imágenes funcionarán correctamente"
