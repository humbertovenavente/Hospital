#!/bin/bash

# Script de instalación de Drone CI/CD LOCAL
set -e

log() {
    echo "[$(date +'%Y-%m-%d %H:%M:%S')] $1"
}

# Función para generar secretos
generate_secrets() {
    log "Generando secretos para Drone..."
    
    # Generar RPC secret
    RPC_SECRET=$(openssl rand -hex 16)
    sed -i "s/your_super_secret_rpc_key_here/$RPC_SECRET/g" drone-local.env
    
    log "✅ Secretos generados exitosamente"
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
    echo "   - Application name: Drone CI/CD - Hospital Local"
    echo "   - Homepage URL: http://localhost:8001"
    echo "   - Authorization callback URL: http://localhost:8001/login"
    echo ""
    echo "3. Copia el Client ID y Client Secret"
    echo ""
    read -p "Ingresa el GitHub Client ID: " GITHUB_CLIENT_ID
    read -p "Ingresa el GitHub Client Secret: " GITHUB_CLIENT_SECRET
    
    # Actualizar archivo de configuración
    sed -i "s/your_github_client_id_here/$GITHUB_CLIENT_ID/g" drone-local.env
    sed -i "s/your_github_client_secret_here/$GITHUB_CLIENT_SECRET/g" drone-local.env
    
    log "✅ GitHub OAuth configurado exitosamente"
}

# Función para instalar Drone
install_drone() {
    log "Instalando Drone CI/CD localmente..."
    
    # Cargar variables de entorno
    source drone-local.env
    
    # Crear directorio para datos de Drone
    mkdir -p drone-data
    
    # Ejecutar Drone
    docker-compose -f docker-compose.drone-local.yml --env-file drone-local.env up -d
    
    log "✅ Drone CI/CD instalado exitosamente"
}

# Función para verificar instalación
verify_installation() {
    log "Verificando instalación..."
    
    # Esperar a que Drone se inicie
    sleep 30
    
    # Verificar contenedores
    log "Estado de los contenedores:"
    docker-compose -f docker-compose.drone-local.yml ps
    
    # Verificar que Drone esté respondiendo
    if curl -f http://localhost:8001 > /dev/null 2>&1; then
        log "✅ Drone está funcionando correctamente"
        log "🌐 Accede a: http://localhost:8001"
    else
        log "❌ Error: Drone no está respondiendo"
        log "📋 Revisa los logs: docker-compose -f docker-compose.drone-local.yml logs"
    fi
}

# Función para mostrar información
show_info() {
    log "Información del despliegue:"
    echo ""
    echo "🚀 DRONE CI/CD INSTALADO LOCALMENTE"
    echo "==================================="
    echo ""
    echo "🌐 URL de acceso: http://localhost:8001"
    echo "👤 Admin user: humbertovenavente"
    echo ""
    echo "📋 Comandos útiles:"
    echo "  - Ver logs: docker-compose -f docker-compose.drone-local.yml logs"
    echo "  - Reiniciar: docker-compose -f docker-compose.drone-local.yml restart"
    echo "  - Detener: docker-compose -f docker-compose.drone-local.yml down"
    echo "  - Ver estado: docker-compose -f docker-compose.drone-local.yml ps"
    echo ""
    echo "🔗 Integración con GitHub:"
    echo "  - Repositorio: https://github.com/humbertovenavente/hospital"
    echo "  - Pipeline: .drone.yml"
    echo "  - Ramas: dev, QA, prod"
    echo ""
    echo "🏥 Ambientes configurados:"
    echo "  - Desarrollo (dev): http://localhost:80"
    echo "  - QA: http://localhost:81"
    echo "  - Producción (prod): http://localhost:82"
    echo ""
    echo "🤖 IA integrada:"
    echo "  - SonarQube para análisis de código"
    echo "  - Grok AI para análisis y recomendaciones"
    echo ""
    echo "📁 Archivos del proyecto:"
    echo "  - .drone.yml (Pipeline de 11 pasos)"
    echo "  - docker-compose.drone-local.yml (Configuración de Drone)"
    echo "  - drone-local.env (Variables de entorno)"
    echo ""
}

# Función principal
main() {
    log "Iniciando instalación de Drone CI/CD local..."
    
    generate_secrets
    setup_github_oauth
    install_drone
    verify_installation
    show_info
    
    log "Instalación completada exitosamente!"
    echo ""
    echo "🎉 DRONE CI/CD LOCAL INSTALADO"
    echo "=============================="
    echo ""
    echo "El pipeline incluye:"
    echo "  ✅ Detección automática de ramas (dev, QA, prod)"
    echo "  ✅ Pipeline de 11 pasos como Jenkins"
    echo "  ✅ Análisis de código con SonarQube"
    echo "  ✅ Análisis con Grok AI"
    echo "  ✅ Tests automáticos por ambiente"
    echo "  ✅ Deploy automático por rama"
    echo "  ✅ Health checks"
    echo "  ✅ Notificaciones"
    echo ""
    echo "¡Tu pipeline local está listo para usar! 🚀"
    echo ""
    echo "Próximos pasos:"
    echo "1. Accede a http://localhost:8001"
    echo "2. Inicia sesión con tu cuenta de GitHub"
    echo "3. Activa tu repositorio hospital"
    echo "4. Haz un push a la rama 'dev' para probar el pipeline"
}

# Ejecutar función principal
main "$@"
