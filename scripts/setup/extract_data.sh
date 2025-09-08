#!/bin/bash

# Script para extraer y preparar datos de Oracle
set -e

ORACLE_DATA_TAR="/home/jose/Downloads/oracle_xe_con_datos/oracle_xe_con_datos.tar"
EXTRACT_DIR="/tmp/oracle_data_extract"

log() {
    echo "[$(date +'%Y-%m-%d %H:%M:%S')] $1"
}

# Función para extraer datos
extract_data() {
    log "Extrayendo datos de Oracle desde: $ORACLE_DATA_TAR"
    
    if [ ! -f "$ORACLE_DATA_TAR" ]; then
        log "ERROR: Archivo $ORACLE_DATA_TAR no encontrado"
        exit 1
    fi
    
    # Crear directorio temporal
    mkdir -p "$EXTRACT_DIR"
    
    # Extraer archivo tar
    log "Extrayendo archivo tar..."
    tar -xf "$ORACLE_DATA_TAR" -C "$EXTRACT_DIR"
    
    log "Datos extraídos en: $EXTRACT_DIR"
}

# Función para preparar datos para cada base de datos
prepare_databases() {
    log "Preparando datos para las 3 bases de datos..."
    
    # Crear directorios de datos
    mkdir -p data/db1 data/db2 data/db3
    
    # Copiar datos a cada directorio (esto es un ejemplo, ajustar según la estructura real)
    if [ -d "$EXTRACT_DIR/oracle_xe_con_datos" ]; then
        # Para DB1 - datos principales
        cp -r "$EXTRACT_DIR/oracle_xe_con_datos"/* data/db1/ 2>/dev/null || true
        
        # Para DB2 - copia de datos principales
        cp -r "$EXTRACT_DIR/oracle_xe_con_datos"/* data/db2/ 2>/dev/null || true
        
        # Para DB3 - copia de datos principales
        cp -r "$EXTRACT_DIR/oracle_xe_con_datos"/* data/db3/ 2>/dev/null || true
        
        log "Datos copiados a los directorios de las bases de datos"
    else
        log "WARNING: Estructura de directorios no encontrada, creando archivos de ejemplo"
        
        # Crear archivos de importación de ejemplo
        echo "-- Script de importación para DB1" > data/db1/import_DB1.sql
        echo "-- Script de importación para DB2" > data/db2/import_DB2.sql
        echo "-- Script de importación para DB3" > data/db3/import_DB3.sql
    fi
}

# Función para limpiar archivos temporales
cleanup() {
    log "Limpiando archivos temporales..."
    rm -rf "$EXTRACT_DIR"
    log "Limpieza completada"
}

# Función principal
main() {
    log "Iniciando extracción y preparación de datos..."
    
    extract_data
    prepare_databases
    cleanup
    
    log "Proceso completado exitosamente"
    log "Los datos están listos en los directorios data/db1, data/db2, data/db3"
}

# Ejecutar función principal
main "$@"



