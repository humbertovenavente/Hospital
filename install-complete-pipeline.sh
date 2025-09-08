#!/bin/bash

# Script de instalación completa del pipeline CI/CD
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
    echo "   - Homepage URL: http://34.10.223.20:8080"
    echo "   - Authorization callback URL: http://34.10.223.20:8080/login"
    echo ""
    echo "3. Copia el Client ID y Client Secret"
    echo ""
    read -p "Ingresa el GitHub Client ID: " GITHUB_CLIENT_ID
    read -p "Ingresa el GitHub Client Secret: " GITHUB_CLIENT_SECRET
    
    # Actualizar archivo de configuración
    sed -i "s/your_github_client_id_here/$GITHUB_CLIENT_ID/g" drone.env
    sed -i "s/your_github_client_secret_here/$GITHUB_CLIENT_SECRET/g" drone.env
    
    log "GitHub OAuth configurado exitosamente"
}

# Función para instalar SonarQube
install_sonarqube() {
    log "Instalando SonarQube..."
    
    # Crear docker-compose para SonarQube
    cat > docker-compose.sonarqube.yml << EOF
version: '3.8'

services:
  sonarqube:
    image: sonarqube:latest
    container_name: sonarqube
    ports:
      - "9000:9000"
    environment:
      - SONAR_ES_BOOTSTRAP_CHECKS_DISABLE=true
      - SONAR_JDBC_URL=jdbc:postgresql://postgres:5432/sonar
      - SONAR_JDBC_USERNAME=sonar
      - SONAR_JDBC_PASSWORD=sonar
    volumes:
      - sonarqube_data:/opt/sonarqube/data
      - sonarqube_logs:/opt/sonarqube/logs
      - sonarqube_extensions:/opt/sonarqube/extensions
    networks:
      - sonarqube-network
    restart: unless-stopped
    depends_on:
      - postgres

  postgres:
    image: postgres:13
    container_name: sonarqube-postgres
    environment:
      - POSTGRES_USER=sonar
      - POSTGRES_PASSWORD=sonar
      - POSTGRES_DB=sonar
    volumes:
      - postgres_data:/var/lib/postgresql/data
    networks:
      - sonarqube-network
    restart: unless-stopped

volumes:
  sonarqube_data:
  sonarqube_logs:
  sonarqube_extensions:
  postgres_data:

networks:
  sonarqube-network:
    driver: bridge
EOF

    # Ejecutar SonarQube
    docker-compose -f docker-compose.sonarqube.yml up -d
    
    log "SonarQube instalado exitosamente"
    log "🌐 Accede a: http://34.10.223.20:9000"
    log "👤 Usuario por defecto: admin / admin"
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
    
    # Esperar a que los servicios se inicien
    sleep 60
    
    # Verificar contenedores
    log "Estado de los contenedores:"
    docker-compose -f docker-compose.drone.yml ps
    docker-compose -f docker-compose.sonarqube.yml ps
    
    # Verificar que Drone esté respondiendo
    if curl -f http://localhost:8080 > /dev/null 2>&1; then
        log "✅ Drone está funcionando correctamente"
        log "🌐 Accede a: http://34.10.223.20:8080"
    else
        log "❌ Error: Drone no está respondiendo"
        log "📋 Revisa los logs: docker-compose -f docker-compose.drone.yml logs"
    fi
    
    # Verificar que SonarQube esté respondiendo
    if curl -f http://localhost:9000 > /dev/null 2>&1; then
        log "✅ SonarQube está funcionando correctamente"
        log "🌐 Accede a: http://34.10.223.20:9000"
    else
        log "❌ Error: SonarQube no está respondiendo"
        log "📋 Revisa los logs: docker-compose -f docker-compose.sonarqube.yml logs"
    fi
}

# Función para mostrar información
show_info() {
    log "Información del despliegue:"
    echo ""
    echo "🚀 PIPELINE CI/CD COMPLETO INSTALADO"
    echo "===================================="
    echo ""
    echo "🔧 Servicios instalados:"
    echo "  - Drone CI/CD: http://34.10.223.20:8080"
    echo "  - SonarQube: http://34.10.223.20:9000"
    echo "  - Oracle DB1: 34.10.223.20:1521"
    echo "  - Oracle DB2: 34.10.223.20:1522"
    echo "  - Oracle DB3: 34.10.223.20:1523"
    echo ""
    echo "👤 Credenciales:"
    echo "  - Drone Admin: humbertovenavente"
    echo "  - SonarQube: admin / admin"
    echo "  - GitHub Token: your_github_token_here"
    echo "  - SonarQube Token: your_sonar_token_here"
    echo "  - Grok API Key: your_grok_api_key_here"
    echo ""
    echo "🏥 Ambientes configurados:"
    echo "  - Desarrollo (dev): http://34.10.223.20:80"
    echo "  - QA: http://34.10.223.20:81"
    echo "  - Producción (prod): http://34.10.223.20:82"
    echo ""
    echo "📋 Comandos útiles:"
    echo "  - Ver logs Drone: docker-compose -f docker-compose.drone.yml logs"
    echo "  - Ver logs SonarQube: docker-compose -f docker-compose.sonarqube.yml logs"
    echo "  - Reiniciar todo: docker-compose -f docker-compose.drone.yml restart && docker-compose -f docker-compose.sonarqube.yml restart"
    echo "  - Detener todo: docker-compose -f docker-compose.drone.yml down && docker-compose -f docker-compose.sonarqube.yml down"
    echo ""
    echo "🔗 Integración con GitHub:"
    echo "  - Repositorio: https://github.com/humbertovenavente/hospital"
    echo "  - Pipeline: .drone.yml"
    echo "  - Ramas: dev, QA, prod"
    echo ""
    echo "🤖 IA integrada:"
    echo "  - SonarQube para análisis de código"
    echo "  - Grok AI para análisis y recomendaciones"
    echo ""
}

# Función principal
main() {
    log "Iniciando instalación completa del pipeline CI/CD..."
    
    generate_secrets
    setup_github_oauth
    install_sonarqube
    install_drone
    verify_installation
    show_info
    
    log "Instalación completada exitosamente!"
    echo ""
    echo "🎉 PIPELINE MULTIPIPELINE CON IA INSTALADO"
    echo "=========================================="
    echo ""
    echo "El pipeline ahora incluye:"
    echo "  ✅ Detección automática de ramas (dev, QA, prod)"
    echo "  ✅ Análisis de código con SonarQube"
    echo "  ✅ Análisis con Grok AI"
    echo "  ✅ Tests automáticos por ambiente"
    echo "  ✅ Deploy automático por rama"
    echo "  ✅ Health checks"
    echo "  ✅ Notificaciones"
    echo ""
    echo "¡Tu pipeline está listo para usar! 🚀"
}

# Ejecutar función principal
main "$@"
