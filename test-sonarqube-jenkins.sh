#!/bin/bash

echo "🔍 PROBANDO SONARQUBE Y JENKINS"
echo "================================"
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
echo "--------------------------------"

# Verificar si SonarQube está corriendo
echo "🔍 Verificando estado de SonarQube..."
if curl -s http://localhost:9000 > /dev/null; then
    show_status 0 "SonarQube está corriendo en http://localhost:9000"
    
    # Verificar API de estado
    echo "🔍 Verificando API de SonarQube..."
    STATUS_RESPONSE=$(curl -s http://localhost:9000/api/system/status)
    if echo "$STATUS_RESPONSE" | grep -q "UP"; then
        show_status 0 "API de SonarQube responde correctamente"
        echo "   Versión: $(echo "$STATUS_RESPONSE" | jq -r '.version' 2>/dev/null || echo 'N/A')"
    else
        show_status 1 "API de SonarQube no responde correctamente"
    fi
    
    # Verificar autenticación
    echo "🔍 Verificando autenticación..."
    if curl -s -u admin:admin http://localhost:9000/api/system/status > /dev/null; then
        show_status 0 "Autenticación con admin/admin funciona"
    else
        show_status 1 "Autenticación con admin/admin falló"
    fi
    
else
    show_status 1 "SonarQube no está corriendo"
fi

echo ""
echo -e "${BLUE}2. VERIFICANDO JENKINS${NC}"
echo "-------------------------------"

# Verificar si Jenkins está corriendo
echo "🔍 Verificando estado de Jenkins..."
if curl -s http://localhost:8081 > /dev/null; then
    show_status 0 "Jenkins está corriendo en http://localhost:8081"
    
    # Verificar si requiere autenticación
    AUTH_RESPONSE=$(curl -s http://localhost:8081/api/json)
    if echo "$AUTH_RESPONSE" | grep -q "Authentication required"; then
        show_status 0 "Jenkins requiere autenticación (configurado correctamente)"
        echo "   💡 Accede a http://localhost:8081 para configurar"
    else
        show_status 1 "Jenkins no requiere autenticación (inseguro)"
    fi
    
else
    show_status 1 "Jenkins no está corriendo"
fi

echo ""
echo -e "${BLUE}3. VERIFICANDO SONARQUBE SCANNER${NC}"
echo "----------------------------------------"

# Verificar SonarQube Scanner
echo "🔍 Verificando SonarQube Scanner..."
if which sonar-scanner > /dev/null 2>&1; then
    show_status 0 "SonarQube Scanner está instalado"
    echo "   Ubicación: $(which sonar-scanner)"
    
    # Verificar versión
    SCANNER_VERSION=$(sonar-scanner --version 2>&1 | grep "SonarScanner" | head -1)
    if [ ! -z "$SCANNER_VERSION" ]; then
        show_status 0 "Scanner funciona correctamente"
        echo "   $SCANNER_VERSION"
    else
        show_status 1 "Scanner no funciona correctamente"
    fi
    
else
    show_status 1 "SonarQube Scanner no está instalado"
fi

echo ""
echo -e "${BLUE}4. VERIFICANDO CONFIGURACIÓN DEL PROYECTO${NC}"
echo "------------------------------------------------"

# Verificar archivo de configuración de SonarQube
echo "🔍 Verificando sonar-project.properties..."
if [ -f "sonar-project.properties" ]; then
    show_status 0 "Archivo sonar-project.properties encontrado"
    
    # Verificar configuración básica
    if grep -q "sonar.projectKey" sonar-project.properties; then
        show_status 0 "Project Key configurado"
        echo "   Project Key: $(grep "sonar.projectKey" sonar-project.properties | cut -d'=' -f2)"
    else
        show_status 1 "Project Key no configurado"
    fi
    
    if grep -q "sonar.host.url" sonar-project.properties; then
        show_status 0 "URL de SonarQube configurada"
        echo "   URL: $(grep "sonar.host.url" sonar-project.properties | cut -d'=' -f2)"
    else
        show_status 1 "URL de SonarQube no configurada"
    fi
    
else
    show_status 1 "Archivo sonar-project.properties no encontrado"
fi

# Verificar Jenkinsfile
echo "🔍 Verificando Jenkinsfile..."
if [ -f "Jenkinsfile" ]; then
    show_status 0 "Jenkinsfile encontrado"
    
    # Verificar etapas del pipeline
    STAGES=$(grep -c "stage(" Jenkinsfile)
    if [ $STAGES -gt 0 ]; then
        show_status 0 "Pipeline configurado con $STAGES etapas"
        echo "   Etapas encontradas:"
        grep "stage(" Jenkinsfile | sed 's/.*stage('\''//' | sed 's/'\'').*//' | sed 's/^/     - /'
    else
        show_status 1 "No se encontraron etapas en el pipeline"
    fi
    
else
    show_status 1 "Jenkinsfile no encontrado"
fi

echo ""
echo -e "${BLUE}5. INSTRUCCIONES PARA ACCEDER${NC}"
echo "====================================="
echo ""
echo -e "${YELLOW}🌐 SONARQUBE:${NC}"
echo "   URL: http://localhost:9000"
echo "   Usuario: admin"
echo "   Contraseña: admin"
echo "   💡 Cambia la contraseña por defecto después del primer login"
echo ""
echo -e "${YELLOW}🌐 JENKINS:${NC}"
echo "   URL: http://localhost:8081"
echo "   💡 Si es la primera vez, sigue las instrucciones en pantalla"
echo "   💡 Si ya está configurado, usa tus credenciales"
echo ""
echo -e "${YELLOW}🔧 PRÓXIMOS PASOS:${NC}"
echo "   1. Accede a SonarQube y cambia la contraseña de admin"
echo "   2. Crea un token de análisis en SonarQube"
echo "   3. Configura el pipeline en Jenkins"
echo "   4. Ejecuta un análisis de código completo"
echo ""
echo -e "${GREEN}✅ Verificación completada${NC}"


