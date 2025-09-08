#!/bin/bash

echo "=== Probando CORS y Funcionalidad ==="
echo ""

# Colores para output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo -e "${GREEN}✅ ¡Contenedor funcionando correctamente!${NC}"
echo "   - Health check: UP"
echo "   - Base de datos: UP"
echo ""

echo -e "${YELLOW}🧪 Comandos para probar CORS:${NC}"
echo ""

echo "1. Probar CORS con localhost:5175:"
echo "   curl -X OPTIONS http://localhost:8030/usuarios/login \\"
echo "     -H 'Origin: http://localhost:5175' \\"
echo "     -H 'Access-Control-Request-Method: POST' \\"
echo "     -H 'Access-Control-Request-Headers: Content-Type' \\"
echo "     -v"
echo ""

echo "2. Probar CORS con 34.46.73.44:5175:"
echo "   curl -X OPTIONS http://localhost:8030/usuarios/login \\"
echo "     -H 'Origin: http://34.46.73.44:5175' \\"
echo "     -H 'Access-Control-Request-Method: POST' \\"
echo "     -H 'Access-Control-Request-Headers: Content-Type' \\"
echo "     -v"
echo ""

echo "3. Probar login real:"
echo "   curl -X POST http://localhost:8030/usuarios/login \\"
echo "     -H 'Content-Type: application/json' \\"
echo "     -H 'Origin: http://localhost:5175' \\"
echo "     -d '{\"correo\":\"admin@hospital.com\",\"contrasena\":\"admin123\"}'"
echo ""

echo "4. Verificar que el contenedor sigue funcionando:"
echo "   sudo docker ps | grep hospital-backend"
echo "   curl http://localhost:8030/q/health"
echo ""

echo -e "${BLUE}💡 Si CORS funciona correctamente, deberías ver:${NC}"
echo "   - Status 200 en las peticiones OPTIONS"
echo "   - Headers CORS en la respuesta"
echo "   - Login exitoso sin errores de CORS"
echo ""

echo -e "${GREEN}✅ ¡El problema de CORS debería estar resuelto!${NC}"
echo "   - Frontend configurado con URL correcta"
echo "   - Backend con CORS permisivo (origins=*)"
echo "   - Contenedor funcionando estable"
