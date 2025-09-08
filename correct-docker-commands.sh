#!/bin/bash

echo "=== Comandos Corregidos para el Servidor ==="
echo ""

# Colores para output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo -e "${YELLOW}📋 Situación actual:${NC}"
echo "   - Contenedor actual: hospital-backend-qa-cloud"
echo "   - Imagen actual: humbertovenavente/hospital-backend-qa:latest"
echo "   - La imagen hospital-backend-cors:latest no está en el servidor"
echo ""

echo -e "${BLUE}🔧 Opción 1: Usar la imagen existente (más rápido)${NC}"
echo "   sudo docker stop hospital-backend-qa-cloud"
echo "   sudo docker rm hospital-backend-qa-cloud"
echo "   sudo docker run -d \\"
echo "     --name hospital-backend-qa-cloud \\"
echo "     -p 8030:8080 \\"
echo "     -e QUARKUS_PROFILE=qa \\"
echo "     -e QUARKUS_DATASOURCE_JDBC_URL=jdbc:oracle:thin:@34.56.133.126:1521/XEPDB1 \\"
echo "     -e QUARKUS_DATASOURCE_USERNAME=C##PROYECTO \\"
echo "     -e QUARKUS_DATASOURCE_PASSWORD=Unis \\"
echo "     humbertovenavente/hospital-backend-qa:latest"
echo ""

echo -e "${BLUE}🔧 Opción 2: Subir la nueva imagen (requiere más tiempo)${NC}"
echo "   # En tu máquina local:"
echo "   docker tag hospital-backend-cors:latest humbertovenavente/hospital-backend-qa:latest"
echo "   docker push humbertovenavente/hospital-backend-qa:latest"
echo "   # Luego en el servidor:"
echo "   sudo docker pull humbertovenavente/hospital-backend-qa:latest"
echo "   sudo docker stop hospital-backend-qa-cloud"
echo "   sudo docker rm hospital-backend-qa-cloud"
echo "   sudo docker run -d \\"
echo "     --name hospital-backend-qa-cloud \\"
echo "     -p 8030:8080 \\"
echo "     -e QUARKUS_PROFILE=qa \\"
echo "     -e QUARKUS_DATASOURCE_JDBC_URL=jdbc:oracle:thin:@34.56.133.126:1521/XEPDB1 \\"
echo "     -e QUARKUS_DATASOURCE_USERNAME=C##PROYECTO \\"
echo "     -e QUARKUS_DATASOURCE_PASSWORD=Unis \\"
echo "     humbertovenavente/hospital-backend-qa:latest"
echo ""

echo -e "${YELLOW}⚠️  IMPORTANTE:${NC}"
echo "   El problema de CORS está en la configuración del backend."
echo "   Si usas la Opción 1, necesitarás actualizar la configuración CORS"
echo "   directamente en el servidor o usar la Opción 2 con la nueva imagen."
echo ""

echo -e "${GREEN}✅ Recomendación: Usar Opción 2 para aplicar la corrección de CORS.${NC}"
