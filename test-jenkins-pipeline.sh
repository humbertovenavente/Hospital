#!/bin/bash

echo "🔧 PROBANDO PIPELINE DE JENKINS"
echo "==============================="
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

echo -e "${BLUE}1. VERIFICANDO JENKINS${NC}"
echo "---------------------------"

# Verificar que Jenkins esté corriendo
if curl -s http://localhost:8081 > /dev/null; then
    show_status 0 "Jenkins está corriendo en http://localhost:8081"
else
    show_status 1 "Jenkins no está corriendo"
    echo "   Inicia Jenkins primero con: ./start-jenkins.sh"
    exit 1
fi

echo ""
echo -e "${BLUE}2. VERIFICANDO HERRAMIENTAS NECESARIAS${NC}"
echo "--------------------------------------------"

# Verificar Java
echo "🔍 Verificando Java..."
if java -version > /dev/null 2>&1; then
    JAVA_VERSION=$(java -version 2>&1 | head -1 | cut -d'"' -f2)
    show_status 0 "Java está instalado (versión: $JAVA_VERSION)"
else
    show_status 1 "Java no está instalado"
fi

# Verificar Maven
echo "🔍 Verificando Maven..."
if mvn -version > /dev/null 2>&1; then
    MVN_VERSION=$(mvn -version 2>&1 | head -1 | cut -d' ' -f3)
    show_status 0 "Maven está instalado (versión: $MVN_VERSION)"
else
    show_status 1 "Maven no está instalado"
fi

# Verificar Node.js
echo "🔍 Verificando Node.js..."
if node --version > /dev/null 2>&1; then
    NODE_VERSION=$(node --version)
    show_status 0 "Node.js está instalado (versión: $NODE_VERSION)"
else
    show_status 1 "Node.js no está instalado"
fi

# Verificar npm
echo "🔍 Verificando npm..."
if npm --version > /dev/null 2>&1; then
    NPM_VERSION=$(npm --version)
    show_status 0 "npm está instalado (versión: $NPM_VERSION)"
else
    show_status 1 "npm no está instalado"
fi

# Verificar Docker
echo "🔍 Verificando Docker..."
if docker --version > /dev/null 2>&1; then
    DOCKER_VERSION=$(docker --version | cut -d' ' -f3 | sed 's/,//')
    show_status 0 "Docker está instalado (versión: $DOCKER_VERSION)"
else
    show_status 1 "Docker no está instalado"
fi

# Verificar Git
echo "🔍 Verificando Git..."
if git --version > /dev/null 2>&1; then
    GIT_VERSION=$(git --version | cut -d' ' -f3)
    show_status 0 "Git está instalado (versión: $GIT_VERSION)"
else
    show_status 1 "Git no está instalado"
fi

echo ""
echo -e "${BLUE}3. VERIFICANDO CONFIGURACIÓN DEL PROYECTO${NC}"
echo "------------------------------------------------"

# Verificar Jenkinsfile
echo "🔍 Verificando Jenkinsfile..."
if [ -f "Jenkinsfile" ]; then
    show_status 0 "Jenkinsfile encontrado"
    
    # Contar etapas
    STAGES=$(grep -c "stage(" Jenkinsfile)
    echo "   • Número de etapas: $STAGES"
    
    # Listar etapas
    echo "   • Etapas del pipeline:"
    grep "stage(" Jenkinsfile | sed 's/.*stage('\''//' | sed 's/'\'').*//' | sed 's/^/     - /'
    
else
    show_status 1 "Jenkinsfile no encontrado"
fi

# Verificar package.json
echo "🔍 Verificando package.json..."
if [ -f "package.json" ]; then
    show_status 0 "package.json encontrado"
    
    # Verificar scripts
    if grep -q '"build"' package.json; then
        show_status 0 "Script de build configurado"
    else
        show_status 1 "Script de build no encontrado"
    fi
    
    if grep -q '"test"' package.json; then
        show_status 0 "Script de test configurado"
    else
        show_status 1 "Script de test no encontrado"
    fi
    
else
    show_status 1 "package.json no encontrado"
fi

# Verificar pom.xml
echo "🔍 Verificando pom.xml..."
if [ -f "backend/pom.xml" ]; then
    show_status 0 "pom.xml encontrado"
    
    # Verificar dependencias
    if grep -q "quarkus" backend/pom.xml; then
        show_status 0 "Quarkus configurado"
    else
        show_status 1 "Quarkus no encontrado en pom.xml"
    fi
    
else
    show_status 1 "pom.xml no encontrado"
fi

echo ""
echo -e "${BLUE}4. PROBANDO COMPILACIÓN MANUAL${NC}"
echo "-------------------------------------"

# Probar compilación del backend
echo "🔨 Probando compilación del backend..."
if [ -d "backend" ]; then
    cd backend
    if mvn clean compile -DskipTests; then
        show_status 0 "Backend compila correctamente"
    else
        show_status 1 "Backend no compila"
    fi
    cd ..
else
    show_status 1 "Directorio backend no encontrado"
fi

# Probar instalación de dependencias del frontend
echo "📦 Probando instalación de dependencias del frontend..."
if [ -f "package.json" ]; then
    if npm ci --silent; then
        show_status 0 "Dependencias del frontend instaladas"
    else
        show_status 1 "Error instalando dependencias del frontend"
    fi
else
    show_status 1 "package.json no encontrado"
fi

# Probar build del frontend
echo "🏗️  Probando build del frontend..."
if [ -f "package.json" ]; then
    if npm run build --silent; then
        show_status 0 "Frontend se construye correctamente"
    else
        show_status 1 "Error construyendo frontend"
    fi
else
    show_status 1 "package.json no encontrado"
fi

echo ""
echo -e "${BLUE}5. INSTRUCCIONES PARA JENKINS${NC}"
echo "====================================="
echo ""
echo -e "${YELLOW}🌐 ACCESO A JENKINS:${NC}"
echo "   URL: http://localhost:8081"
echo ""
echo -e "${YELLOW}📋 CONFIGURACIÓN DEL PIPELINE:${NC}"
echo "   1. Accede a Jenkins"
echo "   2. Crea un nuevo job: 'New Item'"
echo "   3. Selecciona 'Pipeline'"
echo "   4. Configura el repositorio Git"
echo "   5. Especifica la rama (ej: dev)"
echo "   6. Script path: Jenkinsfile"
echo ""
echo -e "${YELLOW}🔧 CONFIGURACIÓN ADICIONAL:${NC}"
echo "   • Instala plugins necesarios:"
echo "     - Git plugin"
echo "     - Pipeline plugin"
echo "     - Docker plugin"
echo "     - JUnit plugin"
echo "     - SonarQube plugin"
echo ""
echo -e "${YELLOW}🚀 EJECUCIÓN:${NC}"
echo "   • Una vez configurado, haz clic en 'Build Now'"
echo "   • El pipeline ejecutará todas las etapas automáticamente"
echo "   • Revisa los logs para ver el progreso"
echo ""
echo -e "${YELLOW}📊 MONITOREO:${NC}"
echo "   • Blue Ocean: http://localhost:8081/blue"
echo "   • Dashboard: http://localhost:8081"
echo "   • Logs detallados en cada build"
echo ""

echo -e "${GREEN}✅ Verificación completada${NC}"


