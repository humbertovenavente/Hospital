#!/bin/bash

echo "🔍 VERIFICANDO INTEGRACIÓN SONARQUBE-JENKINS"
echo "============================================="
echo ""

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Función para mostrar estado
show_status() {
    if [ $1 -eq 0 ]; then
        echo -e "${GREEN}✅ $2${NC}"
    else
        echo -e "${RED}❌ $2${NC}"
    fi
}

echo -e "${BLUE}1. VERIFICANDO SONARQUBE${NC}"
echo "---------------------------"

# Verificar que SonarQube esté corriendo
if curl -s http://localhost:9000 > /dev/null; then
    show_status 0 "SonarQube está corriendo"
    
    # Verificar API
    STATUS_RESPONSE=$(curl -s http://localhost:9000/api/system/status)
    if echo "$STATUS_RESPONSE" | grep -q "UP"; then
        show_status 0 "API de SonarQube responde"
    else
        show_status 1 "API de SonarQube no responde"
    fi
else
    show_status 1 "SonarQube no está corriendo"
    exit 1
fi

echo ""
echo -e "${BLUE}2. VERIFICANDO JENKINS${NC}"
echo "---------------------------"

# Verificar que Jenkins esté corriendo
if curl -s http://localhost:8081 > /dev/null; then
    show_status 0 "Jenkins está corriendo"
else
    show_status 1 "Jenkins no está corriendo"
    exit 1
fi

echo ""
echo -e "${BLUE}3. VERIFICANDO CONFIGURACIÓN DE SONARQUBE EN JENKINS${NC}"
echo "--------------------------------------------------------"

# Verificar si el plugin de SonarQube está instalado
if curl -s http://localhost:8081/pluginManager/api/json | grep -q "sonar"; then
    show_status 0 "Plugin de SonarQube está instalado"
else
    show_status 1 "Plugin de SonarQube NO está instalado"
    echo "   💡 Instala el plugin: Manage Jenkins > Manage Plugins > Available > SonarQube"
fi

echo ""
echo -e "${BLUE}4. VERIFICANDO CREDENCIALES${NC}"
echo "--------------------------------"

# Verificar si hay credenciales configuradas
if curl -s http://localhost:8081/credentials/store/system/domain/_/api/json | grep -q "sonarqube"; then
    show_status 0 "Credenciales de SonarQube configuradas"
else
    show_status 1 "Credenciales de SonarQube NO configuradas"
    echo "   💡 Configura las credenciales: Manage Jenkins > Manage Credentials > System > Global credentials"
fi

echo ""
echo -e "${BLUE}5. VERIFICANDO CONFIGURACIÓN DEL SERVIDOR${NC}"
echo "----------------------------------------------"

# Verificar configuración del servidor
if curl -s http://localhost:8081/configure | grep -q "Hospital Management System"; then
    show_status 0 "Servidor de SonarQube configurado"
else
    show_status 1 "Servidor de SonarQube NO configurado"
    echo "   💡 Configura el servidor: Manage Jenkins > Configure System > SonarQube servers"
fi

echo ""
echo -e "${BLUE}6. VERIFICANDO JENKINSFILE${NC}"
echo "--------------------------------"

# Verificar que el Jenkinsfile tenga la integración correcta
if grep -q "withSonarQubeEnv" Jenkinsfile; then
    show_status 0 "Jenkinsfile tiene integración con SonarQube"
else
    show_status 1 "Jenkinsfile NO tiene integración con SonarQube"
    echo "   💡 Actualiza el Jenkinsfile para usar withSonarQubeEnv"
fi

echo ""
echo -e "${BLUE}7. INSTRUCCIONES PARA SOLUCIONAR PROBLEMAS${NC}"
echo "================================================="
echo ""

echo -e "${YELLOW}🔧 SI EL ANÁLISIS NO SE EJECUTA:${NC}"
echo ""
echo "1. Verifica que el plugin de SonarQube esté instalado:"
echo "   • Manage Jenkins > Manage Plugins > Available"
echo "   • Busca 'SonarQube' e instálalo"
echo ""
echo "2. Configura las credenciales:"
echo "   • Manage Jenkins > Manage Credentials > System > Global credentials"
echo "   • Add Credentials > Secret text"
echo "   • Secret: sqa_a89f100aca013a8b52de0bb4b63cfa9addcab494"
echo "   • ID: sonarqube-token"
echo ""
echo "3. Configura el servidor:"
echo "   • Manage Jenkins > Configure System > SonarQube servers"
echo "   • Name: Hospital Management System"
echo "   • Server URL: http://localhost:9000"
echo "   • Server authentication token: sonarqube-token"
echo ""
echo "4. Ejecuta un nuevo build:"
echo "   • Ve a tu pipeline job"
echo "   • Build Now"
echo "   • Revisa los logs de la etapa 'Code Quality Check'"
echo ""

echo -e "${YELLOW}🔍 PARA VERIFICAR QUE FUNCIONA:${NC}"
echo ""
echo "1. Ejecuta un build en Jenkins"
echo "2. Ve a SonarQube: http://localhost:9000"
echo "3. Busca el proyecto 'hospital-project'"
echo "4. Verifica que la fecha del último análisis sea reciente"
echo ""

echo -e "${GREEN}✅ Verificación completada${NC}"

