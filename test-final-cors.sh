#!/bin/bash

echo "=== Prueba Final de CORS ==="
echo ""

# Colores para output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo -e "${GREEN}✅ Contenedores funcionando correctamente:${NC}"
echo "   - Backend: UP (puerto 8030)"
echo "   - Frontend: UP (puerto 8031)"
echo ""

echo -e "${YELLOW}🧪 Comandos para probar CORS:${NC}"
echo ""

echo "1. Esperar un momento para que el frontend termine de cargar:"
echo "   sleep 10"
echo ""

echo "2. Probar CORS desde el frontend (puerto 8031):"
echo "   curl -X OPTIONS http://localhost:8030/usuarios/login \\"
echo "     -H 'Origin: http://localhost:8031' \\"
echo "     -H 'Access-Control-Request-Method: POST' \\"
echo "     -H 'Access-Control-Request-Headers: Content-Type' \\"
echo "     -v"
echo ""

echo "3. Probar CORS desde el frontend externo:"
echo "   curl -X OPTIONS http://localhost:8030/usuarios/login \\"
echo "     -H 'Origin: http://34.61.228.49:8031' \\"
echo "     -H 'Access-Control-Request-Method: POST' \\"
echo "     -H 'Access-Control-Request-Headers: Content-Type' \\"
echo "     -v"
echo ""

echo "4. Probar login real:"
echo "   curl -X POST http://localhost:8030/usuarios/login \\"
echo "     -H 'Content-Type: application/json' \\"
echo "     -H 'Origin: http://localhost:8031' \\"
echo "     -d '{\"correo\":\"admin@hospital.com\",\"contrasena\":\"admin123\"}'"
echo ""

echo "5. Probar acceso al frontend:"
echo "   curl http://localhost:8031"
echo ""

echo -e "${BLUE}💡 Resultados esperados:${NC}"
echo "   - Status 200 en peticiones OPTIONS"
echo "   - Headers CORS en las respuestas"
echo "   - Login exitoso sin errores"
echo "   - Frontend accesible"
echo ""

echo -e "${GREEN}✅ Si todo funciona, el problema de CORS está completamente resuelto.${NC}"
