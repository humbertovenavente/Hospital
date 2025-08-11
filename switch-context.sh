#!/bin/bash

echo "🔄 Script para cambiar contexto del proyecto Hospital"

# Función para mostrar el estado actual
show_status() {
    echo "📋 Estado actual:"
    echo "   Rama Git: $(git branch --show-current)"
    echo "   Archivo principal: docker-compose.yml"
    echo "   Contexto: $(grep -q "oracle-dev" docker-compose.yml && echo "DESARROLLO" || echo "PRODUCCIÓN")"
    echo ""
}

# Función para cambiar a contexto de producción
switch_to_prod() {
    echo "🚀 Cambiando a contexto de PRODUCCIÓN..."
    
    # Verificar que estamos en la rama prod
    if [ "$(git branch --show-current)" != "prod" ]; then
        echo "❌ ERROR: Solo se puede cambiar a producción desde la rama prod"
        echo "   Rama actual: $(git branch --show-current)"
        echo "   Ejecuta: git checkout prod"
        exit 1
    fi
    
    # Hacer backup del docker-compose.yml actual
    if [ -f "docker-compose.yml.backup" ]; then
        echo "📦 Eliminando backup anterior..."
        rm docker-compose.yml.backup
    fi
    
    echo "📦 Creando backup del docker-compose.yml actual..."
    cp docker-compose.yml docker-compose.yml.backup
    
    # Copiar configuración de producción
    echo "🔄 Copiando configuración de producción..."
    cp docker-compose.prod.yml docker-compose.yml
    
    echo "✅ Contexto cambiado a PRODUCCIÓN"
    echo "   Archivo principal: docker-compose.yml (configuración de PRODUCCIÓN)"
    echo "   Para desarrollo: usar docker-compose.dev.yml"
    echo "   Para QA: usar docker-compose.qa.yml"
}

# Función para cambiar a contexto de desarrollo
switch_to_dev() {
    echo "🔧 Cambiando a contexto de DESARROLLO..."
    
    # Verificar que estamos en la rama prod
    if [ "$(git branch --show-current)" != "prod" ]; then
        echo "❌ ERROR: Solo se puede cambiar contexto desde la rama prod"
        echo "   Rama actual: $(git branch --show-current)"
        echo "   Ejecuta: git checkout prod"
        exit 1
    fi
    
    # Hacer backup del docker-compose.yml actual
    if [ -f "docker-compose.yml.backup" ]; then
        echo "📦 Eliminando backup anterior..."
        rm docker-compose.yml.backup
    fi
    
    echo "📦 Creando backup del docker-compose.yml actual..."
    cp docker-compose.yml docker-compose.yml.backup
    
    # Copiar configuración de desarrollo (con configuración de producción)
    echo "🔄 Copiando configuración de desarrollo (con configuración de PRODUCCIÓN)..."
    cp docker-compose.dev.yml docker-compose.yml
    
    echo "✅ Contexto cambiado a DESARROLLO (con configuración de PRODUCCIÓN)"
    echo "   Archivo principal: docker-compose.yml (configuración de DESARROLLO)"
    echo "   Para producción: usar docker-compose.prod.yml"
    echo "   Para QA: usar docker-compose.qa.yml"
}

# Función para restaurar backup
restore_backup() {
    if [ -f "docker-compose.yml.backup" ]; then
        echo "🔄 Restaurando backup..."
        cp docker-compose.yml.backup docker-compose.yml
        rm docker-compose.yml.backup
        echo "✅ Backup restaurado"
    else
        echo "❌ No hay backup disponible"
    fi
}

# Función para mostrar ayuda
show_help() {
    echo "📖 Uso: $0 [OPCIÓN]"
    echo ""
    echo "Opciones:"
    echo "  prod     Cambiar a contexto de PRODUCCIÓN"
    echo "  dev      Cambiar a contexto de DESARROLLO (con configuración de PRODUCCIÓN)"
    echo "  restore  Restaurar backup del docker-compose.yml"
    echo "  status   Mostrar estado actual"
    echo "  help     Mostrar esta ayuda"
    echo ""
    echo "Ejemplos:"
    echo "  $0 prod     # Cambiar a producción"
    echo "  $0 dev      # Cambiar a desarrollo"
    echo "  $0 status   # Ver estado actual"
}

# Función principal
main() {
    case "$1" in
        "prod")
            switch_to_prod
            ;;
        "dev")
            switch_to_dev
            ;;
        "restore")
            restore_backup
            ;;
        "status")
            show_status
            ;;
        "help"|"--help"|"-h"|"")
            show_help
            ;;
        *)
            echo "❌ Opción desconocida: $1"
            echo "   Usa '$0 help' para ver las opciones disponibles"
            exit 1
            ;;
    esac
}

# Ejecutar función principal
main "$@"
