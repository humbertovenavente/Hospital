#!/bin/bash

# Script para ejecutar los contenedores Oracle localmente
set -e

log() {
    echo "[$(date +'%Y-%m-%d %H:%M:%S')] $1"
}

# Función para verificar Docker
check_docker() {
    if ! command -v docker &> /dev/null; then
        log "ERROR: Docker no está instalado"
        exit 1
    fi
    
    if ! command -v docker-compose &> /dev/null; then
        log "ERROR: Docker Compose no está instalado"
        exit 1
    fi
    
    log "Docker y Docker Compose están instalados"
}

# Función para cargar imagen si no existe
load_image_if_needed() {
    if ! docker images | grep -q "oracle_xe_con_datos"; then
        log "Imagen oracle_xe_con_datos no encontrada, cargando..."
        ./load-image.sh
    else
        log "Imagen oracle_xe_con_datos ya está disponible"
    fi
}

# Función para ejecutar contenedores
run_containers() {
    log "Ejecutando contenedores Oracle..."
    
    # Crear directorios de datos si no existen
    mkdir -p data/db1 data/db2 data/db3
    
    # Ejecutar con docker-compose
    docker-compose up -d
    
    log "Contenedores iniciados"
    log ""
    log "Estado de los contenedores:"
    docker-compose ps
    log ""
    log "Para ver logs: docker-compose logs -f"
    log "Para detener: docker-compose down"
}

# Función para mostrar estado
show_status() {
    log "Estado actual de los contenedores:"
    docker-compose ps
    
    log ""
    log "Información de conexión:"
    log "DB1: localhost:1521 (SID: DB1)"
    log "DB2: localhost:1522 (SID: DB2)"
    log "DB3: localhost:1523 (SID: DB3)"
    log ""
    log "Contraseña: Oracle123"
}

# Función para detener contenedores
stop_containers() {
    log "Deteniendo contenedores..."
    docker-compose down
    log "Contenedores detenidos"
}

# Función para mostrar ayuda
show_help() {
    echo "Uso: $0 [COMANDO]"
    echo ""
    echo "Comandos disponibles:"
    echo "  start   - Iniciar contenedores Oracle"
    echo "  stop    - Detener contenedores"
    echo "  status  - Mostrar estado de contenedores"
    echo "  help    - Mostrar esta ayuda"
    echo ""
    echo "Ejemplos:"
    echo "  $0 start"
    echo "  $0 status"
    echo "  $0 stop"
}

# Función principal
main() {
    case "${1:-}" in
        start)
            check_docker
            load_image_if_needed
            run_containers
            show_status
            ;;
        stop)
            stop_containers
            ;;
        status)
            show_status
            ;;
        help|--help|-h)
            show_help
            ;;
        *)
            show_help
            exit 1
            ;;
    esac
}

# Ejecutar función principal
main "$@"



