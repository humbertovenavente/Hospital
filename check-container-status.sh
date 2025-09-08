#!/bin/bash

echo "=== Verificando Estado del Contenedor ==="
echo ""

# Colores para output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo -e "${YELLOW}1. Verificando estado del contenedor:${NC}"
echo "   sudo docker ps | grep hospital-backend"
echo ""

echo -e "${YELLOW}2. Verificando logs del contenedor:${NC}"
echo "   sudo docker logs hospital-backend-qa-cloud"
echo ""

echo -e "${YELLOW}3. Verificando logs en tiempo real (últimas 50 líneas):${NC}"
echo "   sudo docker logs --tail 50 hospital-backend-qa-cloud"
echo ""

echo -e "${YELLOW}4. Esperar un momento y probar la conectividad:${NC}"
echo "   sleep 30"
echo "   curl http://localhost:8030/q/health"
echo ""

echo -e "${YELLOW}5. Si sigue fallando, verificar si el puerto está en uso:${NC}"
echo "   sudo netstat -tlnp | grep 8030"
echo ""

echo -e "${BLUE}💡 Posibles causas del error:${NC}"
echo "   - El contenedor está iniciando (normal en los primeros segundos)"
echo "   - Problema de configuración de la base de datos"
echo "   - Puerto 8030 ya está en uso por otro proceso"
echo "   - El contenedor se está reiniciando por un error"
echo ""

echo -e "${GREEN}✅ Ejecuta estos comandos para diagnosticar el problema:${NC}"
