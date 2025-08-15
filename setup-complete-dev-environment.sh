#!/bin/bash

# Script completo para configurar el entorno de desarrollo del Hospital
echo "🚀 Configurando entorno completo de desarrollo del Hospital..."

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

# Paso 2: Detener contenedores existentes si están corriendo
print_step "🛑 Deteniendo contenedores existentes..."
docker-compose -f docker-compose.dev.yml down 2>/dev/null || true
print_success "Contenedores detenidos"

# Paso 3: Construir contenedores de desarrollo
print_step "🏗️  Construyendo contenedores de desarrollo..."
docker-compose -f docker-compose.dev.yml build
print_success "Contenedores construidos"

# Paso 4: Levantar servicios de desarrollo
print_step "🚀 Levantando servicios de desarrollo..."
docker-compose -f docker-compose.dev.yml up -d
print_success "Servicios levantados"

# Paso 5: Conectar backend a la red de Oracle
print_step "🔗 Configurando conectividad con Oracle..."
sleep 10
docker network connect bridge hospital-backend-dev 2>/dev/null || true
print_success "Red configurada"

# Paso 6: Verificar que los servicios estén funcionando
print_step "🔍 Verificando servicios..."

echo "⏳ Esperando a que los servicios se inicien..."
sleep 20

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

# Paso 7: Probar envío de reporte de deuda técnica
print_step "📧 Probando envío de reporte de deuda técnica..."
if [ -f "./send-technical-debt-report-dev.sh" ]; then
    ./send-technical-debt-report-dev.sh
    print_success "Reporte de deuda técnica probado"
else
    print_warning "Script de reporte no encontrado"
fi

# Resumen final
echo ""
echo "🎉 ¡Entorno de desarrollo configurado completamente!"
echo ""
echo "📊 Servicios disponibles:"
echo "  🖥️  Frontend DEV:  http://localhost:5180"
echo "  🔧 Backend DEV:   http://localhost:8060"
echo "  📈 SonarQube:     http://localhost:9000"
echo ""
echo "🔧 Configuración DEV:"
echo "  📧 Email: humbertovenavente7@gmail.com (con indicador [RAMA DEV])"
echo "  🗄️  Base de datos: Oracle en 172.17.0.2:1521"
echo "  🔑 Perfil: dev"
echo ""
echo "📝 Proyectos SonarQube DEV:"
echo "  - hospital-backend-dev (Backend con cobertura)"
echo "  - hospital-frontend-dev (Frontend sin cobertura)"
echo ""
echo "🧪 Para ejecutar análisis SonarQube:"
echo "  Backend:  ./analyze-backend-sonar-dev.sh"
echo "  Frontend: ./analyze-frontend-sonar-dev.sh"
echo ""
echo "🚀 Para ejecutar pipeline Jenkins DEV:"
echo "  Usa: Jenkinsfile.dev"
echo ""
echo "📧 Para enviar reporte de deuda técnica:"
echo "  ./send-technical-debt-report-dev.sh"
echo ""
echo "🔧 URLs Jenkins DEV:"
echo "  - Crear job Jenkins con Jenkinsfile.dev"
echo "  - Configurar rama: dev"
echo "  - Configurar SonarQube token: sqa_8f9c9ffeaf833e1486015527efadabc251e75755"
