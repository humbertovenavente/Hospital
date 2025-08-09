#!/bin/bash

echo "🔍 PROBANDO CONEXIÓN JENKINS-SONARQUBE"
echo "======================================"
echo ""

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}1. PROBANDO CONEXIÓN DIRECTA A SONARQUBE${NC}"
echo "----------------------------------------------"

# Probar conexión directa
if curl -s -u admin:admin http://localhost:9000/api/system/status > /dev/null; then
    echo -e "${GREEN}✅ Conexión directa a SonarQube exitosa${NC}"
else
    echo -e "${RED}❌ No se puede conectar a SonarQube${NC}"
    exit 1
fi

echo ""
echo -e "${BLUE}2. PROBANDO TOKEN DE SONARQUBE${NC}"
echo "-------------------------------------"

# Probar con el token que tienes
TOKEN="sqa_a89f100aca013a8b52de0bb4b63cfa9addcab494"

if curl -s -H "Authorization: Bearer $TOKEN" http://localhost:9000/api/system/status > /dev/null; then
    echo -e "${GREEN}✅ Token de SonarQube válido${NC}"
else
    echo -e "${RED}❌ Token de SonarQube inválido${NC}"
    echo "   💡 Genera un nuevo token en SonarQube"
fi

echo ""
echo -e "${BLUE}3. PROBANDO ANÁLISIS MANUAL${NC}"
echo "--------------------------------"

# Probar análisis manual
echo "🔍 Ejecutando análisis manual de SonarQube..."
export PATH=$PATH:/opt/sonar-scanner/bin

if sonar-scanner \
    -Dsonar.projectKey=hospital-project-test \
    -Dsonar.projectName="Hospital Management System Test" \
    -Dsonar.projectVersion=1.0 \
    -Dsonar.sources=src \
    -Dsonar.host.url=http://localhost:9000 \
    -Dsonar.login=admin \
    -Dsonar.password=admin \
    -Dsonar.exclusions=**/node_modules/**,**/dist/**,**/target/** > /dev/null 2>&1; then
    
    echo -e "${GREEN}✅ Análisis manual exitoso${NC}"
    echo "   💡 Ve a http://localhost:9000 y busca 'hospital-project-test'"
else
    echo -e "${RED}❌ Análisis manual falló${NC}"
fi

echo ""
echo -e "${BLUE}4. VERIFICANDO LOGS DE JENKINS${NC}"
echo "-----------------------------------"

# Verificar logs de Jenkins
echo "📋 Últimos logs de Jenkins:"
sudo tail -n 20 /var/log/jenkins/jenkins.log | grep -i sonar || echo "No se encontraron logs de SonarQube"

echo ""
echo -e "${BLUE}5. INSTRUCCIONES PARA SOLUCIONAR${NC}"
echo "====================================="
echo ""

echo -e "${YELLOW}🔧 SI EL TOKEN NO FUNCIONA:${NC}"
echo "1. Ve a SonarQube: http://localhost:9000"
echo "2. Haz clic en tu usuario (arriba a la derecha)"
echo "3. Ve a 'My Account' > 'Security'"
echo "4. Revoca el token actual"
echo "5. Genera un nuevo token"
echo "6. Actualiza las credenciales en Jenkins"
echo ""

echo -e "${YELLOW}🔧 SI EL PLUGIN NO ESTÁ INSTALADO:${NC}"
echo "1. Ve a Jenkins: http://localhost:8081"
echo "2. Manage Jenkins > Manage Plugins"
echo "3. Pestaña 'Available'"
echo "4. Busca 'SonarQube' e instálalo"
echo ""

echo -e "${YELLOW}🔧 PARA VERIFICAR LA CONFIGURACIÓN:${NC}"
echo "1. Ejecuta: ./verify-sonarqube-integration.sh"
echo "2. Todos los puntos deben estar en verde"
echo "3. Ejecuta un nuevo build en Jenkins"
echo "4. Revisa los logs de la etapa 'Code Quality Check'"
echo ""

echo -e "${GREEN}✅ Prueba completada${NC}"

