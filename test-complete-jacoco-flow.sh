#!/bin/bash

# Script completo para probar JaCoCo y SonarQube
echo "🧪 Probando flujo completo de JaCoCo y SonarQube..."

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

print_step() {
    echo -e "${BLUE}$1${NC}"
}

print_success() {
    echo -e "${GREEN}✅ $1${NC}"
}

print_warning() {
    echo -e "${YELLOW}⚠️  $1${NC}"
}

print_error() {
    echo -e "${RED}❌ $1${NC}"
}

# Verificar dependencias
print_step "📋 Verificando dependencias..."

if ! [ -f "backend/mvnw" ] && ! command -v mvn &> /dev/null; then
    print_error "Maven o mvnw no encontrado"
    exit 1
fi

if ! command -v sonar-scanner &> /dev/null; then
    print_error "sonar-scanner no encontrado"
    exit 1
fi

print_success "Dependencias verificadas"

# Limpiar builds anteriores
print_step "🧹 Limpiando builds anteriores..."
cd backend
if [ -f "mvnw" ]; then
    ./mvnw clean
else
    mvn clean
fi
cd ..

# Compilar el backend
print_step "🔨 Compilando backend..."
cd backend
if [ -f "mvnw" ]; then
    ./mvnw compile test-compile
else
    mvn compile test-compile
fi

if [ $? -ne 0 ]; then
    print_error "Error compilando el backend"
    exit 1
fi

print_success "Backend compilado"

# Ejecutar tests con JaCoCo
print_step "🧪 Ejecutando tests con JaCoCo..."
if [ -f "mvnw" ]; then
    ./mvnw test jacoco:report -DskipITs
else
    mvn test jacoco:report -DskipITs
fi

test_result=$?
if [ $test_result -eq 0 ]; then
    print_success "Tests ejecutados exitosamente"
else
    print_warning "Tests fallaron (código: $test_result), pero continuando..."
fi

# Verificar archivos generados
print_step "🔍 Verificando archivos generados por JaCoCo..."

echo "📁 Estructura de target/:"
find target -type f -name "*.xml" -o -name "*.exec" | head -10 || echo "No se encontraron archivos relevantes"

if [ -f "target/site/jacoco/jacoco.xml" ]; then
    print_success "Reporte JaCoCo XML encontrado: target/site/jacoco/jacoco.xml"
    file_size=$(stat -c%s "target/site/jacoco/jacoco.xml" 2>/dev/null || echo "0")
    echo "   📊 Tamaño del archivo: $file_size bytes"
    
    # Mostrar un preview del XML
    echo "   📄 Contenido del reporte (primeras líneas):"
    head -10 "target/site/jacoco/jacoco.xml" || echo "No se pudo leer el archivo"
else
    print_warning "Reporte JaCoCo XML no encontrado en target/site/jacoco/jacoco.xml"
fi

if [ -f "target/jacoco.exec" ]; then
    print_success "Archivo de datos JaCoCo encontrado: target/jacoco.exec"
    file_size=$(stat -c%s "target/jacoco.exec" 2>/dev/null || echo "0")
    echo "   📊 Tamaño del archivo: $file_size bytes"
else
    print_warning "Archivo jacoco.exec no encontrado"
fi

if [ -d "target/surefire-reports" ]; then
    test_count=$(find target/surefire-reports -name "*.xml" | wc -l)
    print_success "Encontrados $test_count reportes de tests en target/surefire-reports"
    echo "   📁 Archivos de tests:"
    ls -la target/surefire-reports/*.xml | head -5 || echo "No se encontraron archivos XML"
else
    print_warning "Directorio target/surefire-reports no encontrado"
fi

cd ..

# Verificar archivos de configuración de SonarQube
print_step "🔧 Verificando configuración de SonarQube..."
if [ -f "sonar-project-backend-dev.properties" ]; then
    print_success "Archivo de configuración encontrado"
    echo "   📄 Configuración de cobertura:"
    grep -E "(jacoco|coverage)" sonar-project-backend-dev.properties || echo "No se encontró configuración de cobertura"
else
    print_error "Archivo sonar-project-backend-dev.properties no encontrado"
    exit 1
fi

# Verificar conectividad con SonarQube
print_step "🌐 Verificando conectividad con SonarQube..."
if curl -f -s http://localhost:9000/api/system/status > /dev/null; then
    print_success "SonarQube está disponible"
    sonar_version=$(curl -s http://localhost:9000/api/system/status | grep -o '"version":"[^"]*"' | cut -d'"' -f4)
    echo "   📊 Versión de SonarQube: $sonar_version"
else
    print_error "SonarQube no está disponible en http://localhost:9000"
    echo "   💡 Asegúrate de que SonarQube esté corriendo antes de continuar"
    exit 1
fi

# Ejecutar análisis de SonarQube
print_step "📊 Ejecutando análisis de SonarQube..."
sonar-scanner -Dproject.settings=sonar-project-backend-dev.properties

if [ $? -eq 0 ]; then
    print_success "Análisis de SonarQube completado exitosamente"
    echo ""
    echo "🌐 Ver resultados en: http://localhost:9000/dashboard?id=hospital-backend-dev"
    echo "📊 Proyecto: Hospital Backend - DEV"
    echo "📋 Configuración: sonar-project-backend-dev.properties"
    echo ""
    echo "📈 Métricas esperadas:"
    echo "   - Cobertura de código (de JaCoCo)"
    echo "   - Número de tests ejecutados"
    echo "   - Análisis de calidad de código"
    echo "   - Issues y vulnerabilidades"
else
    print_error "Error en el análisis de SonarQube"
    exit 1
fi

print_success "¡Flujo completo de JaCoCo y SonarQube completado!"
