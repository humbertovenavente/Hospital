#!/bin/bash

# Script para desplegar los 6 contenedores del Hospital en la nube
# Dev: Backend 8060, Frontend 8061
# Prod: Backend 8020, Frontend 8021  
# QA: Backend 8030, Frontend 8031

set -e

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

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

# Función para limpiar contenedores existentes
cleanup_containers() {
    print_message "Limpiando contenedores existentes..."
    
    # Detener y eliminar contenedores existentes
    docker-compose -f docker-compose.cloud-dev.yml down --remove-orphans 2>/dev/null || true
    docker-compose -f docker-compose.cloud-prod.yml down --remove-orphans 2>/dev/null || true
    docker-compose -f docker-compose.cloud-qa.yml down --remove-orphans 2>/dev/null || true
    
    print_success "Contenedores existentes limpiados"
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

# Función para desplegar contenedores
deploy_containers() {
    print_message "Desplegando contenedores..."
    
    # Desplegar desarrollo
    print_message "Desplegando contenedores de DESARROLLO..."
    docker-compose -f docker-compose.cloud-dev.yml up -d
    print_success "Desarrollo desplegado - Backend: 8060, Frontend: 8061"
    
    # Desplegar producción
    print_message "Desplegando contenedores de PRODUCCIÓN..."
    docker-compose -f docker-compose.cloud-prod.yml up -d
    print_success "Producción desplegada - Backend: 8020, Frontend: 8021"
    
    # Desplegar QA
    print_message "Desplegando contenedores de QA..."
    docker-compose -f docker-compose.cloud-qa.yml up -d
    print_success "QA desplegado - Backend: 8030, Frontend: 8031"
}

# Función para verificar el estado de los contenedores
check_containers() {
    print_message "Verificando estado de los contenedores..."
    
    echo ""
    echo "=== CONTENEDORES DESARROLLO ==="
    docker-compose -f docker-compose.cloud-dev.yml ps
    
    echo ""
    echo "=== CONTENEDORES PRODUCCIÓN ==="
    docker-compose -f docker-compose.cloud-prod.yml ps
    
    echo ""
    echo "=== CONTENEDORES QA ==="
    docker-compose -f docker-compose.cloud-qa.yml ps
    
    echo ""
    print_success "Estado de contenedores verificado"
}

# Función para mostrar URLs de acceso
show_access_urls() {
    echo ""
    print_success "=== URLs DE ACCESO ==="
    echo ""
    echo "🔧 DESARROLLO:"
    echo "   Backend:  http://104.197.237.11:8060"
    echo "   Frontend: http://104.197.237.11:8061"
    echo ""
    echo "🚀 PRODUCCIÓN:"
    echo "   Backend:  http://104.197.237.11:8020"
    echo "   Frontend: http://104.197.237.11:8021"
    echo ""
    echo "🧪 QA:"
    echo "   Backend:  http://104.197.237.11:8030"
    echo "   Frontend: http://104.197.237.11:8031"
    echo ""
}

# Función para mostrar logs
show_logs() {
    local environment=$1
    case $environment in
        "dev")
            print_message "Mostrando logs de DESARROLLO..."
            docker-compose -f docker-compose.cloud-dev.yml logs -f
            ;;
        "prod")
            print_message "Mostrando logs de PRODUCCIÓN..."
            docker-compose -f docker-compose.cloud-prod.yml logs -f
            ;;
        "qa")
            print_message "Mostrando logs de QA..."
            docker-compose -f docker-compose.cloud-qa.yml logs -f
            ;;
        *)
            print_error "Entorno no válido. Usa: dev, prod, o qa"
            exit 1
            ;;
    esac
}

# Función para detener contenedores
stop_containers() {
    local environment=$1
    case $environment in
        "dev")
            print_message "Deteniendo contenedores de DESARROLLO..."
            docker-compose -f docker-compose.cloud-dev.yml down
            ;;
        "prod")
            print_message "Deteniendo contenedores de PRODUCCIÓN..."
            docker-compose -f docker-compose.cloud-prod.yml down
            ;;
        "qa")
            print_message "Deteniendo contenedores de QA..."
            docker-compose -f docker-compose.cloud-qa.yml down
            ;;
        "all")
            print_message "Deteniendo TODOS los contenedores..."
            docker-compose -f docker-compose.cloud-dev.yml down
            docker-compose -f docker-compose.cloud-prod.yml down
            docker-compose -f docker-compose.cloud-qa.yml down
            ;;
        *)
            print_error "Entorno no válido. Usa: dev, prod, qa, o all"
            exit 1
            ;;
    esac
    print_success "Contenedores detenidos"
}

# Función para mostrar ayuda
show_help() {
    echo "Uso: $0 [COMANDO] [ENTORNO]"
    echo ""
    echo "Comandos disponibles:"
    echo "  deploy          Desplegar todos los contenedores"
    echo "  build           Solo construir las imágenes"
    echo "  start           Iniciar contenedores existentes"
    echo "  stop [env]      Detener contenedores (dev|prod|qa|all)"
    echo "  restart [env]   Reiniciar contenedores (dev|prod|qa|all)"
    echo "  logs [env]      Mostrar logs (dev|prod|qa)"
    echo "  status          Mostrar estado de contenedores"
    echo "  cleanup         Limpiar contenedores y volúmenes"
    echo "  help            Mostrar esta ayuda"
    echo ""
    echo "Ejemplos:"
    echo "  $0 deploy                    # Desplegar todos los entornos"
    echo "  $0 stop dev                  # Detener solo desarrollo"
    echo "  $0 logs prod                 # Ver logs de producción"
    echo "  $0 restart all               # Reiniciar todos los entornos"
}

# Función principal
main() {
    local command=$1
    local environment=$2
    
    case $command in
        "deploy")
            check_docker
            cleanup_containers
            build_images
            deploy_containers
            check_containers
            show_access_urls
            ;;
        "build")
            check_docker
            build_images
            ;;
        "start")
            check_docker
            deploy_containers
            check_containers
            show_access_urls
            ;;
        "stop")
            stop_containers $environment
            ;;
        "restart")
            stop_containers $environment
            sleep 2
            deploy_containers
            check_containers
            show_access_urls
            ;;
        "logs")
            show_logs $environment
            ;;
        "status")
            check_containers
            ;;
        "cleanup")
            cleanup_containers
            print_message "Limpiando imágenes no utilizadas..."
            docker image prune -f
            print_success "Limpieza completada"
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
