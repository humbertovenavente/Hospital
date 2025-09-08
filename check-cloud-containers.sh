#!/bin/bash

# Script para verificar el estado de los contenedores en la nube
# Verifica que todos los servicios estén funcionando correctamente

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

# Función para verificar si un puerto está abierto
check_port() {
    local port=$1
    local service_name=$2
    
    if netstat -tuln | grep -q ":$port "; then
        print_success "Puerto $port ($service_name) está abierto"
        return 0
    else
        print_error "Puerto $port ($service_name) NO está abierto"
        return 1
    fi
}

# Función para verificar si un contenedor está ejecutándose
check_container() {
    local container_name=$1
    local environment=$2
    
    if docker ps --format "table {{.Names}}" | grep -q "^$container_name$"; then
        print_success "Contenedor $container_name ($environment) está ejecutándose"
        return 0
    else
        print_error "Contenedor $container_name ($environment) NO está ejecutándose"
        return 1
    fi
}

# Función para verificar la salud de un servicio HTTP
check_http_health() {
    local url=$1
    local service_name=$2
    local timeout=10
    
    if curl -s --max-time $timeout "$url" > /dev/null 2>&1; then
        print_success "Servicio $service_name responde correctamente en $url"
        return 0
    else
        print_error "Servicio $service_name NO responde en $url"
        return 1
    fi
}

# Función para verificar todos los contenedores
check_all_containers() {
    print_message "Verificando estado de todos los contenedores..."
    echo ""
    
    local all_healthy=true
    
    # Verificar contenedores de desarrollo
    echo "=== DESARROLLO ==="
    check_container "hospital-backend-dev-cloud" "dev" || all_healthy=false
    check_container "hospital-frontend-dev-cloud" "dev" || all_healthy=false
    check_port "8060" "Backend Dev" || all_healthy=false
    check_port "8061" "Frontend Dev" || all_healthy=false
    check_http_health "http://104.197.237.11:8060/q/health" "Backend Dev Health" || all_healthy=false
    check_http_health "http://104.197.237.11:8061" "Frontend Dev" || all_healthy=false
    echo ""
    
    # Verificar contenedores de producción
    echo "=== PRODUCCIÓN ==="
    check_container "hospital-backend-prod-cloud" "prod" || all_healthy=false
    check_container "hospital-frontend-prod-cloud" "prod" || all_healthy=false
    check_port "8020" "Backend Prod" || all_healthy=false
    check_port "8021" "Frontend Prod" || all_healthy=false
    check_http_health "http://104.197.237.11:8020/q/health" "Backend Prod Health" || all_healthy=false
    check_http_health "http://104.197.237.11:8021" "Frontend Prod" || all_healthy=false
    echo ""
    
    # Verificar contenedores de QA
    echo "=== QA ==="
    check_container "hospital-backend-qa-cloud" "qa" || all_healthy=false
    check_container "hospital-frontend-qa-cloud" "qa" || all_healthy=false
    check_port "8030" "Backend QA" || all_healthy=false
    check_port "8031" "Frontend QA" || all_healthy=false
    check_http_health "http://104.197.237.11:8030/q/health" "Backend QA Health" || all_healthy=false
    check_http_health "http://104.197.237.11:8031" "Frontend QA" || all_healthy=false
    echo ""
    
    if [ "$all_healthy" = true ]; then
        print_success "¡Todos los contenedores están funcionando correctamente!"
        echo ""
        print_message "URLs de acceso:"
        echo "  🔧 DESARROLLO:"
        echo "     Backend:  http://104.197.237.11:8060"
        echo "     Frontend: http://104.197.237.11:8061"
        echo ""
        echo "  🚀 PRODUCCIÓN:"
        echo "     Backend:  http://104.197.237.11:8020"
        echo "     Frontend: http://104.197.237.11:8021"
        echo ""
        echo "  🧪 QA:"
        echo "     Backend:  http://104.197.237.11:8030"
        echo "     Frontend: http://104.197.237.11:8031"
    else
        print_error "Algunos contenedores no están funcionando correctamente"
        exit 1
    fi
}

# Función para mostrar logs de un entorno específico
show_logs() {
    local environment=$1
    case $environment in
        "dev")
            print_message "Mostrando logs de DESARROLLO..."
            docker-compose -f docker-compose.cloud-dev.yml logs --tail=50
            ;;
        "prod")
            print_message "Mostrando logs de PRODUCCIÓN..."
            docker-compose -f docker-compose.cloud-prod.yml logs --tail=50
            ;;
        "qa")
            print_message "Mostrando logs de QA..."
            docker-compose -f docker-compose.cloud-qa.yml logs --tail=50
            ;;
        *)
            print_error "Entorno no válido. Usa: dev, prod, o qa"
            exit 1
            ;;
    esac
}

# Función para mostrar estadísticas de recursos
show_stats() {
    print_message "Estadísticas de recursos de los contenedores:"
    echo ""
    docker stats --no-stream --format "table {{.Container}}\t{{.CPUPerc}}\t{{.MemUsage}}\t{{.NetIO}}\t{{.BlockIO}}"
}

# Función para mostrar ayuda
show_help() {
    echo "Uso: $0 [COMANDO] [ENTORNO]"
    echo ""
    echo "Comandos disponibles:"
    echo "  check           Verificar estado de todos los contenedores"
    echo "  logs [env]      Mostrar logs (dev|prod|qa)"
    echo "  stats           Mostrar estadísticas de recursos"
    echo "  help            Mostrar esta ayuda"
    echo ""
    echo "Ejemplos:"
    echo "  $0 check                    # Verificar todos los contenedores"
    echo "  $0 logs dev                 # Ver logs de desarrollo"
    echo "  $0 stats                    # Ver estadísticas de recursos"
}

# Función principal
main() {
    local command=$1
    local environment=$2
    
    case $command in
        "check"|"")
            check_all_containers
            ;;
        "logs")
            show_logs $environment
            ;;
        "stats")
            show_stats
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
