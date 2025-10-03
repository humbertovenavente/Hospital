#!/bin/bash

# Script para desplegar producción con configuración CORRECTA
# Resuelve el problema de configuración de base de datos Oracle

set -e

echo "🏥 Desplegando Hospital - PRODUCCIÓN con configuración CORRECTA..."

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

print_message() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Función para limpiar contenedores existentes
cleanup_containers() {
    print_message "🧹 Limpiando contenedores existentes..."
    
    # Forzar eliminación de contenedores
    docker stop hospital-backend-prod-cloud hospital-frontend-prod-cloud 2>/dev/null || true
    docker rm -f hospital-backend-prod-cloud hospital-frontend-prod-cloud 2>/dev/null || true
    
    # Esperar un momento para asegurar limpieza completa
    sleep 3
    
    print_success "Contenedores limpiados"
}

# Función para construir imágenes
build_images() {
    print_message "🔨 Construyendo imágenes Docker..."
    
    # Construir imagen del backend
    print_message "Construyendo imagen del backend..."
    cd backend
    docker build -f Dockerfile.jvm -t hospital-backend-prod .
    cd ..
    
    # Construir imagen del frontend
    print_message "Construyendo imagen del frontend..."
    docker build -f Dockerfile.frontend.cloud -t hospital-frontend-prod .
    
    print_success "Imágenes construidas correctamente"
}

# Función para desplegar contenedores con configuración CORRECTA
deploy_containers() {
    print_message "🚀 Desplegando contenedores con configuración CORRECTA..."
    
    # Crear contenedor del backend con configuración CORRECTA
    print_message "Desplegando Backend - PROD..."
    docker run -d \
      --name hospital-backend-prod-cloud \
      --restart=always \
      -p 8020:8080 \
      -e QUARKUS_PROFILE=prod \
      -e QUARKUS_DATASOURCE_JDBC_URL=jdbc:oracle:thin:@35.208.14.178:1523/XE \
      -e QUARKUS_DATASOURCE_USERNAME=C##HOSPITAL \
      -e QUARKUS_DATASOURCE_PASSWORD=hospital123 \
      -e QUARKUS_HTTP_CORS=true \
      -e QUARKUS_HTTP_CORS_ORIGINS=http://34.61.228.49:8021,http://localhost:8021,http://localhost:5173,http://localhost:8080 \
      -e QUARKUS_HTTP_CORS_METHODS=GET,POST,PUT,DELETE,OPTIONS,PATCH,HEAD \
      -e QUARKUS_HTTP_CORS_HEADERS=* \
      -e QUARKUS_HTTP_CORS_ACCESS_CONTROL_ALLOW_CREDENTIALS=true \
      -e QUARKUS_HTTP_CORS_EXPOSED_HEADERS=Content-Disposition \
      hospital-backend-prod
    
    # Crear contenedor del frontend
    print_message "Desplegando Frontend - PROD..."
    docker run -d \
      --name hospital-frontend-prod-cloud \
      --restart=always \
      -p 8021:80 \
      hospital-frontend-prod
    
    print_success "Contenedores desplegados correctamente"
}

# Función para verificar el estado
verify_deployment() {
    print_message "✅ Verificando despliegue..."
    sleep 10
    
    echo ""
    echo "📊 Estado de los contenedores:"
    docker ps --filter "name=hospital-.*-prod-cloud" --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}"
    
    echo ""
    print_message "🔍 Verificando conectividad..."
    
    # Verificar backend
    if curl -f http://localhost:8020/q/health > /dev/null 2>&1; then
        print_success "Backend respondiendo correctamente"
    else
        print_error "Backend no responde - revisar logs: docker logs hospital-backend-prod-cloud"
    fi
    
    # Verificar frontend
    if curl -f http://localhost:8021 > /dev/null 2>&1; then
        print_success "Frontend respondiendo correctamente"
    else
        print_error "Frontend no responde - revisar logs: docker logs hospital-frontend-prod-cloud"
    fi
    
    echo ""
    print_success "🌐 URLs de acceso:"
    echo "   - Frontend: http://34.61.228.49:8021"
    echo "   - Backend:  http://34.61.228.49:8020"
    echo "   - Health:   http://34.61.228.49:8020/q/health"
}

# Ejecutar despliegue
main() {
    print_message "Iniciando despliegue de producción con configuración CORRECTA..."
    
    cleanup_containers
    build_images
    deploy_containers
    verify_deployment
    
    print_success "🎉 Despliegue completado exitosamente!"
}

# Ejecutar función principal
main "$@"
