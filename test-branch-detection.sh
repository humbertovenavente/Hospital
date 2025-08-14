#!/bin/bash

# Script para probar la detección de rama como lo haría Jenkins
echo "🔍 Probando detección de rama como Jenkins..."

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

# Simular variables de Jenkins
export BRANCH_NAME=""
export CHANGE_ID=""

print_step "📋 Información actual del repositorio:"
echo "  🌿 Rama actual: $(git rev-parse --abbrev-ref HEAD)"
echo "  📍 Commit actual: $(git rev-parse --short HEAD)"
echo "  📊 Estado: $(git status --porcelain | wc -l) archivos modificados"

print_step "🔍 Simulando detección de Jenkins..."

# Simular la lógica del Jenkinsfile
if [ -z "$BRANCH_NAME" ] || [ "$BRANCH_NAME" = "null" ]; then
    detected=$(git rev-parse --abbrev-ref HEAD)
    if [ "$detected" = "HEAD" ]; then
        # En estado detached, forzar uso de 'dev' para desarrollo por defecto
        detected='dev'
        echo "🔍 Estado detached detectado, forzando rama: dev"
    fi
    BRANCH_NAME=$detected
    echo "🔖 Rama detectada: $BRANCH_NAME"
fi

# Verificación adicional para ramas específicas
if [ "$BRANCH_NAME" = "QA" ] || [ "$BRANCH_NAME" = "qa" ]; then
    BRANCH_NAME='qa'
    echo "✅ Rama QA confirmada: $BRANCH_NAME"
elif [ "$BRANCH_NAME" = "prod" ] || [ "$BRANCH_NAME" = "production" ]; then
    BRANCH_NAME='prod'
    echo "✅ Rama PROD confirmada: $BRANCH_NAME"
else
    # Cualquier otra rama se trata como desarrollo
    echo "✅ Rama DEV confirmada: $BRANCH_NAME"
fi

print_step "🎯 Resultado de la detección:"
echo "  📋 BRANCH_NAME: $BRANCH_NAME"

# Mostrar qué entorno se usará
if [ "$BRANCH_NAME" = "qa" ] || [ "$BRANCH_NAME" = "QA" ]; then
    print_warning "ENTORNO: QA"
    echo "📊 PROYECTOS SONARQUBE: hospital-backend-qa, hospital-frontend-qa"
    echo "🐳 DOCKER COMPOSE: docker-compose.qa.yml"
    echo "🌐 PUERTOS: Backend 8060, Frontend 5174"
elif [ "$BRANCH_NAME" = "prod" ] || [ "$BRANCH_NAME" = "production" ]; then
    print_warning "ENTORNO: PRODUCCIÓN"
    echo "📊 PROYECTOS SONARQUBE: hospital-backend-prod, hospital-frontend-prod"
    echo "🐳 DOCKER COMPOSE: docker-compose.yml"
    echo "🌐 PUERTOS: Backend 8084, Frontend 5176"
else
    print_success "ENTORNO: DESARROLLO"
    echo "📊 PROYECTOS SONARQUBE: hospital-backend-dev, hospital-frontend-dev"
    echo "🐳 DOCKER COMPOSE: docker-compose.dev.yml"
    echo "🌐 PUERTOS: Backend 8060, Frontend 5180"
    echo "📧 EMAILS: Con indicador [RAMA DEV]"
fi

echo ""
print_step "✨ El pipeline de Jenkins usará la configuración mostrada arriba"
