#!/bin/bash

# Script mejorado para desplegar contenedores del Hospital a Google Cloud
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

# Verificar y configurar Google Cloud
setup_gcloud() {
    print_status "Configurando Google Cloud..."
    
    # Autenticar con la cuenta de servicio
    gcloud auth activate-service-account --key-file="$CREDENTIALS_FILE"
    
    # Configurar el proyecto
    gcloud config set project "$PROJECT_ID"
    
    # Verificar si las APIs están habilitadas
    print_status "Verificando APIs habilitadas..."
    
    # Intentar habilitar APIs si no están habilitadas
    if ! gcloud services list --enabled --filter="name:containerregistry.googleapis.com" --format="value(name)" | grep -q "containerregistry.googleapis.com"; then
        print_warning "Container Registry API no está habilitada"
        print_status "Por favor, habilita las APIs manualmente desde Google Cloud Console:"
        echo "  1. Ve a: https://console.cloud.google.com/apis/library?project=$PROJECT_ID"
        echo "  2. Busca y habilita: Container Registry API"
        echo "  3. Busca y habilita: Artifact Registry API"
        echo "  4. Busca y habilita: Compute Engine API"
        echo "  5. Busca y habilita: Cloud Resource Manager API"
        echo ""
        read -p "¿Ya habilitaste las APIs? (y/N): " -n 1 -r
        echo
        if [[ ! $REPLY =~ ^[Yy]$ ]]; then
            print_error "Debes habilitar las APIs primero"
            exit 1
        fi
    fi
    
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
        print_warning "JAR del backend no encontrado"
        print_status "Opciones:"
        echo "  1. Construir el proyecto Java: mvn clean package -DskipTests"
        echo "  2. Continuar solo con el frontend"
        echo ""
        read -p "¿Quieres continuar solo con el frontend? (y/N): " -n 1 -r
        echo
        if [[ ! $REPLY =~ ^[Yy]$ ]]; then
            print_error "Debes construir el backend primero"
            exit 1
        fi
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
    
    # Subir backend solo si se construyó
    if docker images | grep -q "$BACKEND_IMAGE:$VERSION"; then
        print_status "Subiendo backend..."
        docker push "gcr.io/$PROJECT_ID/$BACKEND_IMAGE:$VERSION"
        docker push "gcr.io/$PROJECT_ID/$BACKEND_IMAGE:latest"
    else
        print_warning "Backend no se subió (no se construyó)"
    fi
    
    print_success "Imágenes subidas correctamente a Google Cloud"
}

# Mostrar información de las imágenes
show_image_info() {
    print_status "Información de las imágenes desplegadas:"
    echo "=================================================="
    echo "Frontend: gcr.io/$PROJECT_ID/$FRONTEND_IMAGE:$VERSION"
    if docker images | grep -q "$BACKEND_IMAGE:$VERSION"; then
        echo "Backend:  gcr.io/$PROJECT_ID/$BACKEND_IMAGE:$VERSION"
    else
        echo "Backend:  No construido"
    fi
    echo "=================================================="
    echo ""
    echo "Para ejecutar localmente:"
    echo "docker run -p 80:80 gcr.io/$PROJECT_ID/$FRONTEND_IMAGE:$VERSION"
    if docker images | grep -q "$BACKEND_IMAGE:$VERSION"; then
        echo "docker run -p 8080:8080 gcr.io/$PROJECT_ID/$BACKEND_IMAGE:$VERSION"
    fi
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

