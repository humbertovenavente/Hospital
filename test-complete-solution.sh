#!/bin/bash

echo "=== Prueba Completa de la Solución CORS ==="
echo ""

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${YELLOW}1. Verificando configuración del frontend...${NC}"
echo "   - API_URL configurada:"
grep "API_URL.*34.46.73.44:8030" src/services/authService.js
if [ $? -eq 0 ]; then
    echo -e "   ${GREEN}✓ Frontend configurado correctamente${NC}"
else
    echo -e "   ${RED}✗ Frontend no está configurado correctamente${NC}"
fi

echo ""
echo -e "${YELLOW}2. Verificando configuración del backend...${NC}"
echo "   - CORS configurado en application-qa.properties:"
grep "quarkus.http.cors.origins" backend/src/main/resources/application-qa.properties
if [ $? -eq 0 ]; then
    echo -e "   ${GREEN}✓ Backend configurado correctamente${NC}"
else
    echo -e "   ${RED}✗ Backend no está configurado correctamente${NC}"
fi

echo ""
echo -e "${YELLOW}3. Probando conectividad del backend...${NC}"
HEALTH_STATUS=$(curl -s -o /dev/null -w "%{http_code}" http://34.46.73.44:8030/q/health)
if [ "$HEALTH_STATUS" = "200" ]; then
    echo -e "   ${GREEN}✓ Backend respondiendo correctamente${NC}"
else
    echo -e "   ${RED}✗ Backend no responde (Status: $HEALTH_STATUS)${NC}"
fi

echo ""
echo -e "${YELLOW}4. Probando endpoint de login...${NC}"
LOGIN_STATUS=$(curl -s -o /dev/null -w "%{http_code}" -X POST http://34.46.73.44:8030/usuarios/login -H "Content-Type: application/json" -d '{"correo":"admin@hospital.com","contrasena":"admin123"}')
if [ "$LOGIN_STATUS" = "200" ]; then
    echo -e "   ${GREEN}✓ Endpoint de login funcionando${NC}"
else
    echo -e "   ${RED}✗ Endpoint de login no funciona (Status: $LOGIN_STATUS)${NC}"
fi

echo ""
echo -e "${YELLOW}5. Probando CORS con diferentes orígenes...${NC}"

# Probar con localhost:5175
CORS_STATUS_LOCAL=$(curl -s -o /dev/null -w "%{http_code}" -X OPTIONS http://34.46.73.44:8030/usuarios/login -H "Origin: http://localhost:5175" -H "Access-Control-Request-Method: POST" -H "Access-Control-Request-Headers: Content-Type")
if [ "$CORS_STATUS_LOCAL" = "200" ]; then
    echo -e "   ${GREEN}✓ CORS funciona con localhost:5175${NC}"
else
    echo -e "   ${RED}✗ CORS no funciona con localhost:5175 (Status: $CORS_STATUS_LOCAL)${NC}"
fi

# Probar con 34.46.73.44:5175
CORS_STATUS_REMOTE=$(curl -s -o /dev/null -w "%{http_code}" -X OPTIONS http://34.46.73.44:8030/usuarios/login -H "Origin: http://34.46.73.44:5175" -H "Access-Control-Request-Method: POST" -H "Access-Control-Request-Headers: Content-Type")
if [ "$CORS_STATUS_REMOTE" = "200" ]; then
    echo -e "   ${GREEN}✓ CORS funciona con 34.46.73.44:5175${NC}"
else
    echo -e "   ${RED}✗ CORS no funciona con 34.46.73.44:5175 (Status: $CORS_STATUS_REMOTE)${NC}"
fi

echo ""
echo -e "${YELLOW}6. Resumen de la solución:${NC}"
echo "   - Se actualizaron todos los servicios del frontend para usar la URL correcta"
echo "   - Se configuró CORS permisivo en el backend (origins=*)"
echo "   - Se mejoró el manejo de errores en el frontend"

echo ""
echo -e "${YELLOW}7. Próximos pasos:${NC}"
if [ "$CORS_STATUS_LOCAL" != "200" ] || [ "$CORS_STATUS_REMOTE" != "200" ]; then
    echo "   - El backend necesita ser reiniciado para aplicar los cambios de CORS"
    echo "   - Ejecuta: ./update-backend-cors.sh"
    echo "   - Luego reinicia el backend en la nube"
else
    echo "   - Todo está funcionando correctamente"
    echo "   - Puedes probar el login en el frontend"
fi

echo ""
echo "=== Prueba completada ==="
