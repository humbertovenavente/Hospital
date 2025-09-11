#!/bin/bash

# Script para subir las imágenes Docker a un registro de contenedores en la nube
# Este script construye y sube las imágenes para los 6 contenedores

set -e

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Configuración del registro (puedes cambiar esto por tu registro preferido)
REGISTRY_URL="104.197.237.11:5000"  # Cambia por tu registro Docker
PROJECT_NAME="hospital"

# Función para imprimir mensajes con color
print_message() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Función para verificar si Docker está ejecutándose
check_docker() {
    if ! docker info > /dev/null 2>&1; then
        print_error "Docker no está ejecutándose. Por favor, inicia Docker primero."
        exit 1
    fi
    print_success "Docker está ejecutándose correctamente"
}

# Función para construir las imágenes
build_images() {
    print_message "Construyendo imágenes Docker..."
    
    # Construir imágenes para desarrollo
    print_message "Construyendo imágenes para DESARROLLO..."
    docker-compose -f docker-compose.cloud-dev.yml build --no-cache
    
    # Construir imágenes para producción
    print_message "Construyendo imágenes para PRODUCCIÓN..."
    docker-compose -f docker-compose.cloud-prod.yml build --no-cache
    
    # Construir imágenes para QA
    print_message "Construyendo imágenes para QA..."
    docker-compose -f docker-compose.cloud-qa.yml build --no-cache
    
    print_success "Todas las imágenes construidas correctamente"
}

# Función para etiquetar las imágenes
tag_images() {
    print_message "Etiquetando imágenes para el registro..."
    
    # Etiquetar imágenes de desarrollo
    docker tag hospital-backend-dev-cloud:latest ${REGISTRY_URL}/${PROJECT_NAME}-backend-dev:latest
    docker tag hospital-frontend-dev-cloud:latest ${REGISTRY_URL}/${PROJECT_NAME}-frontend-dev:latest
    
    # Etiquetar imágenes de producción
    docker tag hospital-backend-prod-cloud:latest ${REGISTRY_URL}/${PROJECT_NAME}-backend-prod:latest
    docker tag hospital-frontend-prod-cloud:latest ${REGISTRY_URL}/${PROJECT_NAME}-frontend-prod:latest
    
    # Etiquetar imágenes de QA
    docker tag hospital-backend-qa-cloud:latest ${REGISTRY_URL}/${PROJECT_NAME}-backend-qa:latest
    docker tag hospital-frontend-qa-cloud:latest ${REGISTRY_URL}/${PROJECT_NAME}-frontend-qa:latest
    
    print_success "Imágenes etiquetadas correctamente"
}

# Función para subir las imágenes
push_images() {
    print_message "Subiendo imágenes al registro..."
    
    # Subir imágenes de desarrollo
    print_message "Subiendo imágenes de DESARROLLO..."
    docker push ${REGISTRY_URL}/${PROJECT_NAME}-backend-dev:latest
    docker push ${REGISTRY_URL}/${PROJECT_NAME}-frontend-dev:latest
    
    # Subir imágenes de producción
    print_message "Subiendo imágenes de PRODUCCIÓN..."
    docker push ${REGISTRY_URL}/${PROJECT_NAME}-backend-prod:latest
    docker push ${REGISTRY_URL}/${PROJECT_NAME}-frontend-prod:latest
    
    # Subir imágenes de QA
    print_message "Subiendo imágenes de QA..."
    docker push ${REGISTRY_URL}/${PROJECT_NAME}-backend-qa:latest
    docker push ${REGISTRY_URL}/${PROJECT_NAME}-frontend-qa:latest
    
    print_success "Todas las imágenes subidas correctamente"
}

# Función para mostrar las imágenes disponibles
show_images() {
    print_message "Imágenes disponibles en el registro:"
    echo ""
    echo "🔧 DESARROLLO:"
    echo "   Backend:  ${REGISTRY_URL}/${PROJECT_NAME}-backend-dev:latest"
    echo "   Frontend: ${REGISTRY_URL}/${PROJECT_NAME}-frontend-dev:latest"
    echo ""
    echo "🚀 PRODUCCIÓN:"
    echo "   Backend:  ${REGISTRY_URL}/${PROJECT_NAME}-backend-prod:latest"
    echo "   Frontend: ${REGISTRY_URL}/${PROJECT_NAME}-frontend-prod:latest"
    echo ""
    echo "🧪 QA:"
    echo "   Backend:  ${REGISTRY_URL}/${PROJECT_NAME}-backend-qa:latest"
    echo "   Frontend: ${REGISTRY_URL}/${PROJECT_NAME}-frontend-qa:latest"
    echo ""
}

# Función para crear un archivo docker-compose para producción en la nube
create_production_compose() {
    print_message "Creando archivo docker-compose para producción en la nube..."
    
    cat > docker-compose.cloud-production.yml << EOF
version: '3.8'

services:
  # Backend para producción en la nube
  backend-prod:
    image: ${REGISTRY_URL}/${PROJECT_NAME}-backend-prod:latest
    container_name: hospital-backend-prod-cloud
    ports:
      - "8020:8080"
    environment:
      - NODE_ENV=production
      - QUARKUS_PROFILE=prod
      - DB_HOST=34.10.223.20
      - DB_PORT=1521
      - DB_SID=XE
      - DB_USER=system
      - DB_PASSWORD=Oracle123
      - LOG_LEVEL=info
    networks:
      - hospital-prod-network
    restart: unless-stopped
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:8080/q/health"]
      interval: 30s
      timeout: 10s
      retries: 3

  # Frontend para producción en la nube
  frontend-prod:
    image: ${REGISTRY_URL}/${PROJECT_NAME}-frontend-prod:latest
    container_name: hospital-frontend-prod-cloud
    ports:
      - "8021:80"
    environment:
      - NODE_ENV=production
      - VITE_API_URL=http://104.197.237.11:8020
    networks:
      - hospital-prod-network
    restart: unless-stopped
    depends_on:
      - backend-prod
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:80"]
      interval: 30s
      timeout: 10s
      retries: 3

networks:
  hospital-prod-network:
    driver: bridge
EOF

    print_success "Archivo docker-compose.cloud-production.yml creado"
}

# Función para mostrar ayuda
show_help() {
    echo "Uso: $0 [COMANDO]"
    echo ""
    echo "Comandos disponibles:"
    echo "  build           Solo construir las imágenes"
    echo "  tag             Solo etiquetar las imágenes"
    echo "  push            Solo subir las imágenes (requiere que estén etiquetadas)"
    echo "  all             Construir, etiquetar y subir todas las imágenes"
    echo "  show            Mostrar las imágenes disponibles"
    echo "  create-compose  Crear archivo docker-compose para producción"
    echo "  help            Mostrar esta ayuda"
    echo ""
    echo "Ejemplos:"
    echo "  $0 all                    # Construir, etiquetar y subir todo"
    echo "  $0 build                  # Solo construir las imágenes"
    echo "  $0 push                   # Solo subir las imágenes"
    echo "  $0 show                   # Ver imágenes disponibles"
}

# Función principal
main() {
    local command=$1
    
    case $command in
        "build")
            check_docker
            build_images
            ;;
        "tag")
            check_docker
            tag_images
            ;;
        "push")
            check_docker
            push_images
            ;;
        "all")
            check_docker
            build_images
            tag_images
            push_images
            show_images
            ;;
        "show")
            show_images
            ;;
        "create-compose")
            create_production_compose
            ;;
        "help"|"--help"|"-h")
            show_help
            ;;
        *)
            print_error "Comando no reconocido: $command"
            show_help
            exit 1
            ;;
    esac
}

# Ejecutar función principal con argumentos
main "$@"




