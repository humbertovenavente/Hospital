#!/bin/bash

# Script para desplegar contenedores del Hospital a Google Cloud
# Autor: Hospital Team
# Fecha: $(date)

set -e  # Salir si hay algún error

echo "🏥 Iniciando despliegue del Sistema Hospital a Google Cloud..."
echo "=================================================="

# Variables de configuración
PROJECT_ID="hospital-470223"
REGION="us-central1"
CREDENTIALS_FILE="hospital-credentials.json"
FRONTEND_IMAGE="hospital-frontend"
BACKEND_IMAGE="hospital-backend"
VERSION="v1.0.0"

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Función para imprimir mensajes con colores
print_status() {
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

# Verificar que existan los archivos necesarios
check_prerequisites() {
    print_status "Verificando prerequisitos..."
    
    if [ ! -f "$CREDENTIALS_FILE" ]; then
        print_error "Archivo de credenciales no encontrado: $CREDENTIALS_FILE"
        exit 1
    fi
    
    if [ ! -f "Dockerfile.frontend" ]; then
        print_error "Dockerfile.frontend no encontrado"
        exit 1
    fi
    
    if [ ! -f "Dockerfile" ]; then
        print_error "Dockerfile (backend) no encontrado"
        exit 1
    fi
    
    print_success "Prerequisitos verificados correctamente"
}

# Configurar Google Cloud
setup_gcloud() {
    print_status "Configurando Google Cloud..."
    
    # Autenticar con la cuenta de servicio
    gcloud auth activate-service-account --key-file="$CREDENTIALS_FILE"
    
    # Configurar el proyecto
    gcloud config set project "$PROJECT_ID"
    
    # Configurar la región
    gcloud config set compute/region "$REGION"
    
    # Habilitar APIs necesarias
    gcloud services enable containerregistry.googleapis.com
    gcloud services enable artifactregistry.googleapis.com
    
    print_success "Google Cloud configurado correctamente"
}

# Configurar Docker para Google Cloud
setup_docker() {
    print_status "Configurando Docker para Google Cloud..."
    
    # Configurar Docker para usar las credenciales de gcloud
    gcloud auth configure-docker
    
    print_success "Docker configurado para Google Cloud"
}

# Construir imagen del frontend
build_frontend() {
    print_status "Construyendo imagen del frontend..."
    
    # Construir la imagen
    docker build -f Dockerfile.frontend -t "$FRONTEND_IMAGE:$VERSION" .
    
    # Etiquetar para Google Cloud
    docker tag "$FRONTEND_IMAGE:$VERSION" "gcr.io/$PROJECT_ID/$FRONTEND_IMAGE:$VERSION"
    docker tag "$FRONTEND_IMAGE:$VERSION" "gcr.io/$PROJECT_ID/$FRONTEND_IMAGE:latest"
    
    print_success "Frontend construido correctamente"
}

# Construir imagen del backend
build_backend() {
    print_status "Construyendo imagen del backend..."
    
    # Verificar que exista el JAR del backend
    if [ ! -f "backend/target/quarkus-app/quarkus-run.jar" ]; then
        print_warning "JAR del backend no encontrado. Construyendo el proyecto..."
        
        # Aquí podrías agregar comandos para construir el proyecto Java
        # mvn clean package -DskipTests
        print_warning "Por favor, construye el proyecto Java primero con: mvn clean package"
        return 1
    fi
    
    # Construir la imagen
    docker build -f Dockerfile -t "$BACKEND_IMAGE:$VERSION" .
    
    # Etiquetar para Google Cloud
    docker tag "$BACKEND_IMAGE:$VERSION" "gcr.io/$PROJECT_ID/$BACKEND_IMAGE:$VERSION"
    docker tag "$BACKEND_IMAGE:$VERSION" "gcr.io/$PROJECT_ID/$BACKEND_IMAGE:latest"
    
    print_success "Backend construido correctamente"
}

# Subir imágenes a Google Cloud
push_images() {
    print_status "Subiendo imágenes a Google Cloud..."
    
    # Subir frontend
    print_status "Subiendo frontend..."
    docker push "gcr.io/$PROJECT_ID/$FRONTEND_IMAGE:$VERSION"
    docker push "gcr.io/$PROJECT_ID/$FRONTEND_IMAGE:latest"
    
    # Subir backend
    print_status "Subiendo backend..."
    docker push "gcr.io/$PROJECT_ID/$BACKEND_IMAGE:$VERSION"
    docker push "gcr.io/$PROJECT_ID/$BACKEND_IMAGE:latest"
    
    print_success "Imágenes subidas correctamente a Google Cloud"
}

# Mostrar información de las imágenes
show_image_info() {
    print_status "Información de las imágenes desplegadas:"
    echo "=================================================="
    echo "Frontend: gcr.io/$PROJECT_ID/$FRONTEND_IMAGE:$VERSION"
    echo "Backend:  gcr.io/$PROJECT_ID/$BACKEND_IMAGE:$VERSION"
    echo "=================================================="
    echo ""
    echo "Para ejecutar localmente:"
    echo "docker run -p 80:80 gcr.io/$PROJECT_ID/$FRONTEND_IMAGE:$VERSION"
    echo "docker run -p 8080:8080 gcr.io/$PROJECT_ID/$BACKEND_IMAGE:$VERSION"
}

# Función principal
main() {
    echo "🚀 Iniciando proceso de despliegue..."
    echo ""
    
    check_prerequisites
    setup_gcloud
    setup_docker
    build_frontend
    build_backend
    push_images
    show_image_info
    
    echo ""
    print_success "🎉 ¡Despliegue completado exitosamente!"
    print_status "Las imágenes están disponibles en Google Cloud Container Registry"
}

# Ejecutar función principal
main "$@"

