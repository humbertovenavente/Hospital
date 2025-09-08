#!/bin/bash

# Script para cargar la imagen Docker existente
set -e

IMAGE_TAR="/home/jose/Downloads/oracle_xe_con_datos/oracle_xe_con_datos.tar"
IMAGE_NAME="oracle_xe_con_datos"
IMAGE_TAG="latest"

log() {
    echo "[$(date +'%Y-%m-%d %H:%M:%S')] $1"
}

# Función para cargar la imagen
load_image() {
    log "Cargando imagen Docker desde: $IMAGE_TAR"
    
    if [ ! -f "$IMAGE_TAR" ]; then
        log "ERROR: Archivo $IMAGE_TAR no encontrado"
        exit 1
    fi
    
    # Cargar la imagen Docker
    log "Cargando imagen..."
    docker load < "$IMAGE_TAR"
    
    # Verificar que la imagen se cargó correctamente
    if docker images | grep -q "$IMAGE_NAME"; then
        log "Imagen cargada exitosamente:"
        docker images | grep "$IMAGE_NAME"
    else
        log "ERROR: No se pudo cargar la imagen"
        exit 1
    fi
}

# Función para verificar la imagen
verify_image() {
    log "Verificando imagen..."
    
    # Ver detalles de la imagen
    log "Detalles de la imagen:"
    docker inspect "$IMAGE_NAME:$IMAGE_TAG" | grep -E "(Id|Created|Size|Architecture|Os)"
    
    # Verificar que se puede ejecutar
    log "Verificando que la imagen se puede ejecutar..."
    docker run --rm "$IMAGE_NAME:$IMAGE_TAG" echo "Imagen funcionando correctamente"
}

# Función principal
main() {
    log "Iniciando carga de imagen Docker..."
    
    load_image
    verify_image
    
    log "Proceso completado exitosamente"
    log "Ahora puedes ejecutar: docker-compose up -d"
}

# Ejecutar función principal
main "$@"



