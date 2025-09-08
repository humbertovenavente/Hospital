#!/bin/bash

echo "=== Comandos para Actualizar Frontend en el Servidor ==="
echo ""

# Colores para output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${GREEN}✅ Imagen del frontend construida y subida exitosamente${NC}"
echo "   - humbertovenavente/hospital-frontend-qa:latest"
echo "   - Incluye URL correcta: http://34.46.73.44:8030"
echo ""

echo -e "${YELLOW}🚀 Comandos para ejecutar en el servidor:${NC}"
echo ""

echo "1. Actualizar la imagen del frontend:"
echo "   sudo docker pull humbertovenavente/hospital-frontend-qa:latest"
echo ""

echo "2. Detener y eliminar el contenedor actual del frontend:"
echo "   sudo docker stop hospital-frontend-qa-cloud"
echo "   sudo docker rm hospital-frontend-qa-cloud"
echo ""

echo "3. Crear y ejecutar el nuevo contenedor del frontend:"
echo "   sudo docker run -d \\"
echo "     --name hospital-frontend-qa-cloud \\"
echo "     -p 8031:80 \\"
echo "     humbertovenavente/hospital-frontend-qa:latest"
echo ""

echo "4. Verificar que ambos contenedores están funcionando:"
echo "   sudo docker ps | grep hospital"
echo ""

echo "5. Probar la conectividad:"
echo "   curl http://localhost:8031"
echo "   curl http://localhost:8030/q/health"
echo ""

echo "6. Probar CORS desde el frontend:"
echo "   curl -X OPTIONS http://localhost:8030/usuarios/login \\"
echo "     -H 'Origin: http://localhost:8031' \\"
echo "     -H 'Access-Control-Request-Method: POST' \\"
echo "     -H 'Access-Control-Request-Headers: Content-Type' \\"
echo "     -v"
echo ""

echo -e "${BLUE}💡 Lo que se ha corregido:${NC}"
echo "   - Frontend ahora usa http://34.46.73.44:8030 (puerto correcto)"
echo "   - Backend con CORS configurado como origins=*"
echo "   - Ambos contenedores actualizados"
echo ""

echo -e "${GREEN}✅ Después de ejecutar estos comandos, el problema de CORS estará completamente resuelto.${NC}"
