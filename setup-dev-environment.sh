#!/bin/bash

# Script maestro para configurar el entorno de desarrollo completo
echo "🚀 Configurando entorno de desarrollo completo para Hospital..."

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Función para imprimir mensajes con colores
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

# Paso 1: Verificar dependencias
print_step "📋 Verificando dependencias..."
if ! command -v docker &> /dev/null; then
    print_error "Docker no está instalado"
    exit 1
fi

if ! command -v docker-compose &> /dev/null; then
    print_error "Docker Compose no está instalado"
    exit 1
fi

print_success "Dependencias verificadas"

# Paso 2: Configurar proyectos SonarQube
print_step "🔧 Configurando proyectos SonarQube..."
if [ -f "./setup-sonarqube-dev-projects.sh" ]; then
    ./setup-sonarqube-dev-projects.sh
    print_success "Proyectos SonarQube configurados"
else
    print_warning "Script de SonarQube no encontrado"
fi

# Paso 3: Construir contenedores de desarrollo
print_step "🏗️  Construyendo contenedores de desarrollo..."
docker-compose -f docker-compose.dev.yml build
print_success "Contenedores construidos"

# Paso 4: Levantar servicios de desarrollo
print_step "🚀 Levantando servicios de desarrollo..."
docker-compose -f docker-compose.dev.yml up -d
print_success "Servicios levantados"

# Paso 5: Verificar que los servicios estén funcionando
print_step "🔍 Verificando servicios..."

echo "⏳ Esperando a que los servicios se inicien..."
sleep 15

# Verificar backend
if curl -s http://localhost:8060/q/health > /dev/null; then
    print_success "Backend funcionando en http://localhost:8060"
else
    print_warning "Backend no responde aún"
fi

# Verificar frontend
if curl -s http://localhost:5180 > /dev/null; then
    print_success "Frontend funcionando en http://localhost:5180"
else
    print_warning "Frontend no responde aún"
fi

# Verificar SonarQube
if curl -s http://localhost:9000 > /dev/null; then
    print_success "SonarQube funcionando en http://localhost:9000"
else
    print_warning "SonarQube no responde aún"
fi

# Resumen final
echo ""
echo "🎉 ¡Entorno de desarrollo configurado!"
echo ""
echo "📊 Servicios disponibles:"
echo "  🖥️  Frontend:  http://localhost:5180"
echo "  🔧 Backend:   http://localhost:8060"
echo "  📈 SonarQube: http://localhost:9000"
echo ""
echo "🔑 Credenciales SonarQube:"
echo "  Usuario: admin"
echo "  Contraseña: admin"
echo ""
echo "📝 Proyectos SonarQube creados:"
echo "  - hospital-backend-dev (Backend con cobertura)"
echo "  - hospital-frontend-dev (Frontend sin cobertura)"
echo ""
echo "🧪 Para ejecutar análisis:"
echo "  Backend:  ./analyze-backend-sonar-dev.sh"
echo "  Frontend: ./analyze-frontend-sonar-dev.sh"
echo ""
echo "📧 Configuración de email:"
echo "  - Desarrollo: hospital.dev.test@gmail.com"
echo "  - Configurado en: backend/src/main/resources/application-dev.properties"
