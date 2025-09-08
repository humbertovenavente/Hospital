#!/bin/bash

echo "=== Actualizando Imagen del Backend ==="
echo ""

# Colores para output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${YELLOW}1. Haciendo commit de los cambios...${NC}"
git add .
git commit -m "Fix CORS and API URLs for QA environment"

echo ""
echo -e "${YELLOW}2. Subiendo cambios al repositorio...${NC}"
git push origin QA

echo ""
echo -e "${BLUE}3. Instrucciones para actualizar la imagen en el servidor:${NC}"
echo ""
echo "   SSH al servidor:"
echo "   ssh usuario@34.46.73.44"
echo ""
echo "   Luego ejecuta estos comandos:"
echo "   cd /ruta/del/proyecto"
echo "   git pull origin QA"
echo "   docker build -t hospital-backend:latest ./backend"
echo "   docker stop nombre_del_contenedor_backend"
echo "   docker rm nombre_del_contenedor_backend"
echo "   docker run -d --name nombre_del_contenedor_backend -p 8030:8080 hospital-backend:latest"
echo ""
echo -e "${GREEN}¡Listo! Los cambios de CORS se aplicarán con la nueva imagen.${NC}"
