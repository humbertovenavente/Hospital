#!/bin/bash

echo "=== Corrigiendo Puerto del Frontend ==="
echo ""

# Colores para output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo -e "${RED}⚠️  Problema detectado: Frontend usando puerto 8060 en lugar de 8030${NC}"
echo ""

echo -e "${YELLOW}🔧 Archivos corregidos:${NC}"
echo "   - Dockerfile.frontend.cloud"
echo "   - Dockerfile.frontend"
echo "   - docker-compose.cloud-dev.yml"
echo ""

echo -e "${BLUE}💡 El problema puede ser:${NC}"
echo "   - El frontend está usando una imagen antigua"
echo "   - Hay caché del navegador"
echo "   - Variables de entorno no se actualizaron"
echo ""

echo -e "${YELLOW}🚀 Soluciones:${NC}"
echo ""

echo "1. Limpiar caché del navegador:"
echo "   - Presiona Ctrl+Shift+R (hard refresh)"
echo "   - O abre DevTools > Application > Storage > Clear storage"
echo ""

echo "2. Si el frontend está en Docker, reconstruir:"
echo "   sudo docker stop hospital-frontend-qa-cloud"
echo "   sudo docker rm hospital-frontend-qa-cloud"
echo "   sudo docker build -t hospital-frontend-qa:latest -f Dockerfile.frontend.qa ."
echo "   sudo docker run -d --name hospital-frontend-qa-cloud -p 8031:80 hospital-frontend-qa:latest"
echo ""

echo "3. Si el frontend está corriendo localmente:"
echo "   npm run build"
echo "   # O reiniciar el servidor de desarrollo"
echo ""

echo "4. Verificar que no hay variables de entorno incorrectas:"
echo "   echo \$VITE_API_URL"
echo "   # Debería mostrar: http://34.61.228.49:8030"
echo ""

echo -e "${GREEN}✅ Después de aplicar estas soluciones, el frontend debería usar el puerto correcto.${NC}"
