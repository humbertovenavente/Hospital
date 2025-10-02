#!/bin/bash

# Script de instalación de Drone CI/CD en Google Cloud
set -e

log() {
    echo "[$(date +'%Y-%m-%d %H:%M:%S')] $1"
}

# Función para generar secretos
generate_secrets() {
    log "Generando secretos para Drone..."
    
    # Generar RPC secret
    RPC_SECRET=$(openssl rand -hex 16)
    echo "DRONE_RPC_SECRET=$RPC_SECRET" >> drone.env
    
    log "Secretos generados exitosamente"
}

# Función para configurar GitHub OAuth
setup_github_oauth() {
    log "Configurando GitHub OAuth..."
    
    echo ""
    echo "🔧 CONFIGURACIÓN DE GITHUB OAUTH"
    echo "================================"
    echo ""
    echo "1. Ve a: https://github.com/settings/applications/new"
    echo "2. Configura:"
    echo "   - Application name: Drone CI/CD - Hospital"
    echo "   - Homepage URL: http://34.61.228.49:8002"
    echo "   - Authorization callback URL: http://34.61.228.49:8002/login"
    echo ""
    echo "3. Copia el Client ID y Client Secret"
    echo ""
    read -p "Ingresa el GitHub Client ID: " GITHUB_CLIENT_ID
    read -p "Ingresa el GitHub Client Secret: " GITHUB_CLIENT_SECRET
    read -p "Ingresa tu username de GitHub: " GITHUB_USERNAME
    
    # Actualizar archivo de configuración
    sed -i "s/your_github_client_id_here/$GITHUB_CLIENT_ID/g" drone.env
    sed -i "s/your_github_client_secret_here/$GITHUB_CLIENT_SECRET/g" drone.env
    sed -i "s/your_github_username/$GITHUB_USERNAME/g" drone.env
    
    log "GitHub OAuth configurado exitosamente"
}

# Función para instalar Drone
install_drone() {
    log "Instalando Drone CI/CD..."
    
    # Cargar variables de entorno
    source drone.env
    
    # Crear directorio para datos de Drone
    mkdir -p drone-data
    
    # Ejecutar Drone
    docker-compose -f docker-compose.drone.yml --env-file drone.env up -d
    
    log "Drone CI/CD instalado exitosamente"
}

# Función para verificar instalación
verify_installation() {
    log "Verificando instalación..."
    
    # Esperar a que Drone se inicie
    sleep 30
    
    # Verificar contenedores
    log "Estado de los contenedores:"
    docker-compose -f docker-compose.drone.yml ps
    
    # Verificar que Drone esté respondiendo
    if curl -f http://localhost:8002 > /dev/null 2>&1; then
        log "✅ Drone está funcionando correctamente"
        log "🌐 Accede a: http://34.61.228.49:8002"
    else
        log "❌ Error: Drone no está respondiendo"
        log "📋 Revisa los logs: docker-compose -f docker-compose.drone.yml logs"
    fi
}

# Función para mostrar información
show_info() {
    log "Información del despliegue:"
    echo ""
    echo "🚀 DRONE CI/CD INSTALADO EXITOSAMENTE"
    echo "====================================="
    echo ""
    echo "🌐 URL de acceso: http://34.61.228.49:8002"
    echo "🔧 Admin user: $(grep DRONE_ADMIN_USER drone.env | cut -d'=' -f2)"
    echo ""
    echo "📋 Comandos útiles:"
    echo "  - Ver logs: docker-compose -f docker-compose.drone.yml logs"
    echo "  - Reiniciar: docker-compose -f docker-compose.drone.yml restart"
    echo "  - Detener: docker-compose -f docker-compose.drone.yml down"
    echo ""
    echo "🔗 Integración con GitHub:"
    echo "  - Repositorio: https://github.com/tu-usuario/hospital"
    echo "  - Pipeline: .drone.yml"
    echo ""
    echo "🏥 Ambientes configurados:"
    echo "  - Desarrollo: http://34.61.228.49:8060"
    echo "  - QA: http://34.61.228.49:8061"
    echo "  - Producción: http://34.61.228.49:8062"
    echo ""
}

# Función principal
main() {
    log "Iniciando instalación de Drone CI/CD..."
    
    generate_secrets
    setup_github_oauth
    install_drone
    verify_installation
    show_info
    
    log "Instalación completada exitosamente!"
}

# Ejecutar función principal
main "$@"
