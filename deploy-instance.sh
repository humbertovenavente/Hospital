#!/bin/bash

# Script para ejecutar directamente en la instancia de Google Cloud
set -e

log() {
    echo "[$(date +'%Y-%m-%d %H:%M:%S')] $1"
}

# Función para configurar Docker
setup_docker() {
    log "Configurando Docker..."
    
    # Agregar usuario al grupo docker
    sudo usermod -aG docker $USER
    
    # Verificar que Docker esté funcionando
    if ! sudo systemctl is-active --quiet docker; then
        log "Iniciando Docker..."
        sudo systemctl start docker
        sudo systemctl enable docker
    fi
    
    log "Docker configurado correctamente"
}

# Función para cargar imagen Docker
load_image() {
    log "Cargando imagen Docker..."
    
    if [ -f "oracle_xe_con_datos.tar" ]; then
        # Usar sudo para cargar la imagen
        sudo docker load < oracle_xe_con_datos.tar
        
        # Verificar que la imagen se cargó
        if sudo docker images | grep -q "oracle_xe_con_datos"; then
            log "Imagen cargada exitosamente:"
            sudo docker images | grep "oracle_xe_con_datos"
        else
            log "ERROR: No se pudo cargar la imagen"
            exit 1
        fi
    else
        log "ERROR: Archivo oracle_xe_con_datos.tar no encontrado"
        exit 1
    fi
}

# Función para ejecutar contenedores
run_containers() {
    log "Ejecutando contenedores Oracle..."
    
    # Crear directorios de datos si no existen
    mkdir -p data/db1 data/db2 data/db3
    
    # Ejecutar con docker-compose usando sudo
    sudo docker-compose up -d
    
    log "Contenedores iniciados"
    log ""
    log "Estado de los contenedores:"
    sudo docker-compose ps
}

# Función para mostrar información de conexión
show_connection_info() {
    log "Información de conexión:"
    log "DB1: localhost:1521 (SID: DB1)"
    log "DB2: localhost:1522 (SID: DB2)"
    log "DB3: localhost:1523 (SID: DB3)"
    log ""
    log "Contraseña: Oracle123"
    log ""
    log "Para ver logs: sudo docker-compose logs -f"
    log "Para detener: sudo docker-compose down"
}

# Función principal
main() {
    log "Iniciando despliegue de Oracle en Google Cloud..."
    
    setup_docker
    load_image
    run_containers
    show_connection_info
    
    log "Despliegue completado exitosamente!"
}

# Ejecutar función principal
main "$@"
