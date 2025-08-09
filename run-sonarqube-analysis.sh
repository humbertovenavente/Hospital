#!/bin/bash

echo "🔍 EJECUTANDO ANÁLISIS COMPLETO DE SONARQUBE"
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

# Verificar que SonarQube esté corriendo
echo -e "${BLUE}1. VERIFICANDO SONARQUBE${NC}"
if ! curl -s http://localhost:9000 > /dev/null; then
    echo -e "${RED}❌ SonarQube no está corriendo${NC}"
    echo "   Inicia SonarQube primero"
    exit 1
fi
show_status 0 "SonarQube está corriendo"

# Verificar autenticación
echo -e "${BLUE}2. VERIFICANDO AUTENTICACIÓN${NC}"
if curl -s -u admin:admin http://localhost:9000/api/system/status > /dev/null; then
    show_status 0 "Autenticación exitosa"
else
    echo -e "${RED}❌ Error de autenticación${NC}"
    echo "   Verifica las credenciales de SonarQube"
    exit 1
fi

# Verificar que el scanner esté disponible
echo -e "${BLUE}3. VERIFICANDO SONARQUBE SCANNER${NC}"
export PATH=$PATH:/opt/sonar-scanner/bin
if ! which sonar-scanner > /dev/null 2>&1; then
    echo -e "${RED}❌ SonarQube Scanner no está instalado${NC}"
    exit 1
fi
show_status 0 "SonarQube Scanner disponible"

# Verificar archivo de configuración
echo -e "${BLUE}4. VERIFICANDO CONFIGURACIÓN${NC}"
if [ ! -f "sonar-project.properties" ]; then
    echo -e "${RED}❌ Archivo sonar-project.properties no encontrado${NC}"
    exit 1
fi
show_status 0 "Archivo de configuración encontrado"

# Compilar el backend primero (necesario para el análisis)
echo -e "${BLUE}5. COMPILANDO BACKEND${NC}"
if [ -d "backend" ]; then
    echo "🔨 Compilando backend..."
    cd backend
    if mvn clean compile -DskipTests; then
        show_status 0 "Backend compilado exitosamente"
    else
        echo -e "${YELLOW}⚠️  Compilación del backend falló, continuando sin ella${NC}"
    fi
    cd ..
else
    echo -e "${YELLOW}⚠️  Directorio backend no encontrado${NC}"
fi

# Ejecutar análisis de SonarQube
echo -e "${BLUE}6. EJECUTANDO ANÁLISIS DE SONARQUBE${NC}"
echo "🔍 Iniciando análisis..."
echo "   Esto puede tomar varios minutos..."

# Crear un archivo temporal con la configuración
cat > sonar-temp.properties << EOF
sonar.projectKey=hospital-project
sonar.projectName=Hospital Management System
sonar.projectVersion=1.0
sonar.sources=src,backend/src/main/java
sonar.tests=backend/src/test/java
sonar.java.source=17
sonar.java.binaries=backend/target/classes
sonar.java.test.binaries=backend/target/test-classes
sonar.host.url=http://localhost:9000
sonar.login=admin
sonar.password=admin
sonar.exclusions=**/node_modules/**,**/dist/**,**/target/**,**/*.min.js,**/*.min.css
sonar.qualitygate.wait=true
EOF

# Ejecutar el análisis
if sonar-scanner -Dproject.settings=sonar-temp.properties; then
    show_status 0 "Análisis de SonarQube completado exitosamente"
    
    # Limpiar archivo temporal
    rm -f sonar-temp.properties
    
    echo ""
    echo -e "${GREEN}🎉 ANÁLISIS COMPLETADO${NC}"
    echo "================================"
    echo ""
    echo -e "${YELLOW}📊 RESULTADOS:${NC}"
    echo "   • Accede a SonarQube: http://localhost:9000"
    echo "   • Busca el proyecto: hospital-project"
    echo "   • Revisa los resultados de calidad del código"
    echo ""
    echo -e "${YELLOW}📋 MÉTRICAS DISPONIBLES:${NC}"
    echo "   • Cobertura de código"
    echo "   • Duplicaciones"
    echo "   • Vulnerabilidades de seguridad"
    echo "   • Code smells"
    echo "   • Bugs"
    echo "   • Deuda técnica"
    echo ""
    echo -e "${YELLOW}🔧 PRÓXIMOS PASOS:${NC}"
    echo "   1. Revisa los resultados en SonarQube"
    echo "   2. Corrige los problemas identificados"
    echo "   3. Configura Quality Gates"
    echo "   4. Integra con Jenkins para análisis automático"
    
else
    show_status 1 "Análisis de SonarQube falló"
    echo ""
    echo -e "${RED}❌ ERRORES ENCONTRADOS:${NC}"
    echo "   • Revisa los logs anteriores"
    echo "   • Verifica la configuración de SonarQube"
    echo "   • Asegúrate de que el proyecto compile correctamente"
    
    # Limpiar archivo temporal
    rm -f sonar-temp.properties
    exit 1
fi

echo ""
echo -e "${GREEN}✅ Proceso completado${NC}"


