#!/bin/bash

# Script para probar el análisis de SonarQube DEV localmente
echo "🧪 Probando análisis de SonarQube DEV..."

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

if ! command -v mvn &> /dev/null && ! [ -f "backend/mvnw" ]; then
    print_error "Maven o mvnw no encontrado"
    exit 1
fi

if ! command -v sonar-scanner &> /dev/null; then
    print_error "sonar-scanner no encontrado. Instalalo primero."
    exit 1
fi

print_success "Dependencias verificadas"

# Compilar el backend
print_step "🔨 Compilando backend..."
cd backend
if [ -f "mvnw" ]; then
    ./mvnw clean compile test-compile
else
    mvn clean compile test-compile
fi

if [ $? -ne 0 ]; then
    print_error "Error compilando el backend"
    exit 1
fi

print_success "Backend compilado"

# Ejecutar tests con cobertura JaCoCo
print_step "🧪 Ejecutando tests con cobertura JaCoCo..."
if [ -f "mvnw" ]; then
    ./mvnw test jacoco:report
else
    mvn test jacoco:report
fi

if [ $? -ne 0 ]; then
    print_warning "Tests fallaron, pero continuando con análisis..."
fi

# Verificar que se generó el reporte JaCoCo
print_step "🔍 Verificando reportes JaCoCo..."
if [ -f "target/site/jacoco/jacoco.xml" ]; then
    print_success "Reporte JaCoCo XML encontrado en target/site/jacoco/jacoco.xml"
elif [ -f "target/jacoco.xml" ]; then
    print_success "Reporte JaCoCo XML encontrado en target/jacoco.xml"
else
    print_warning "No se encontró reporte JaCoCo XML"
    echo "📁 Buscando archivos JaCoCo..."
    find target -name "*.xml" -path "*/jacoco*" -type f 2>/dev/null || echo "No se encontraron archivos XML de JaCoCo"
fi

# Verificar reportes de tests
if [ -d "target/surefire-reports" ]; then
    test_count=$(find target/surefire-reports -name "*.xml" | wc -l)
    print_success "Encontrados $test_count reportes de tests en target/surefire-reports"
else
    print_warning "No se encontró directorio target/surefire-reports"
fi

cd ..

# Verificar archivos necesarios
print_step "🔍 Verificando archivos necesarios..."
if [ ! -d "backend/target/classes" ]; then
    print_error "backend/target/classes no existe"
    exit 1
fi

if [ ! -f "sonar-project-backend-dev.properties" ]; then
    print_error "sonar-project-backend-dev.properties no encontrado"
    exit 1
fi

print_success "Archivos verificados"

# Ejecutar análisis de SonarQube
print_step "📊 Ejecutando análisis de SonarQube DEV..."
sonar-scanner -Dproject.settings=sonar-project-backend-dev.properties

if [ $? -eq 0 ]; then
    print_success "Análisis de SonarQube completado exitosamente"
    echo ""
    echo "🌐 Ver resultados en: http://localhost:9000/dashboard?id=hospital-backend-dev"
    echo "📊 Proyecto: Hospital Backend - DEV"
    echo "🔧 Configuración: sonar-project-backend-dev.properties"
else
    print_error "Error en el análisis de SonarQube"
    exit 1
fi
