#!/bin/bash

# Script para instalar Prometheus y Grafana en el servidor de la nube
echo "🌐 INSTALANDO MONITOREO EN EL SERVIDOR DE LA NUBE"
echo "================================================"

# Función para logging
log() {
    echo "[$(date +'%Y-%m-%d %H:%M:%S')] $1"
}

# Función para crear configuración de Prometheus
create_prometheus_config() {
    log "📝 Creando configuración de Prometheus..."
    
    cat > prometheus-cloud.yml << 'EOF'
global:
  scrape_interval: 15s
  evaluation_interval: 15s

scrape_configs:
  # Prometheus itself
  - job_name: 'prometheus'
    static_configs:
      - targets: ['localhost:9090']

  # Sistema (Node Exporter)
  - job_name: 'node-exporter'
    static_configs:
      - targets: ['node-exporter:9100']

  # Contenedores (cAdvisor)
  - job_name: 'cadvisor'
    static_configs:
      - targets: ['cadvisor:8080']

  # Backend Hospital - Producción
  - job_name: 'hospital-backend-prod'
    static_configs:
      - targets: ['localhost:8082']
    metrics_path: '/q/metrics'
    scrape_interval: 10s

  # Backend Hospital - QA
  - job_name: 'hospital-backend-qa'
    static_configs:
      - targets: ['localhost:8081']
    metrics_path: '/q/metrics'
    scrape_interval: 10s

  # Backend Hospital - Dev
  - job_name: 'hospital-backend-dev'
    static_configs:
      - targets: ['localhost:8080']
    metrics_path: '/q/metrics'
    scrape_interval: 10s

  # SonarQube
  - job_name: 'sonarqube'
    static_configs:
      - targets: ['localhost:9000']
    metrics_path: '/api/monitoring/metrics'
    scrape_interval: 60s
EOF
    
    log "✅ Configuración de Prometheus creada"
}

# Función para crear docker-compose
create_docker_compose() {
    log "🐳 Creando docker-compose para monitoreo..."
    
    cat > docker-compose.monitoring-cloud.yml << 'EOF'
version: '3.8'

services:
  # Prometheus
  prometheus:
    image: prom/prometheus:latest
    container_name: prometheus-cloud
    ports:
      - "9090:9090"
    volumes:
      - ./prometheus-cloud.yml:/etc/prometheus/prometheus.yml
      - prometheus_data:/prometheus
    command:
      - '--config.file=/etc/prometheus/prometheus.yml'
      - '--storage.tsdb.path=/prometheus'
      - '--web.console.libraries=/etc/prometheus/console_libraries'
      - '--web.console.templates=/etc/prometheus/consoles'
      - '--storage.tsdb.retention.time=30d'
      - '--web.enable-lifecycle'
    restart: unless-stopped
    networks:
      - monitoring-network

  # Grafana
  grafana:
    image: grafana/grafana:latest
    container_name: grafana-cloud
    ports:
      - "3000:3000"
    environment:
      - GF_SECURITY_ADMIN_PASSWORD=admin123
      - GF_USERS_ALLOW_SIGN_UP=false
      - GF_INSTALL_PLUGINS=grafana-piechart-panel
    volumes:
      - grafana_data:/var/lib/grafana
    restart: unless-stopped
    depends_on:
      - prometheus
    networks:
      - monitoring-network

  # Node Exporter (métricas del sistema)
  node-exporter:
    image: prom/node-exporter:latest
    container_name: node-exporter-cloud
    ports:
      - "9100:9100"
    volumes:
      - /proc:/host/proc:ro
      - /sys:/host/sys:ro
      - /:/rootfs:ro
    command:
      - '--path.procfs=/host/proc'
      - '--path.rootfs=/rootfs'
      - '--path.sysfs=/host/sys'
      - '--collector.filesystem.mount-points-exclude=^/(sys|proc|dev|host|etc)($$|/)'
    restart: unless-stopped
    networks:
      - monitoring-network

  # cAdvisor (métricas de contenedores)
  cadvisor:
    image: gcr.io/cadvisor/cadvisor:latest
    container_name: cadvisor-cloud
    ports:
      - "8080:8080"
    volumes:
      - /:/rootfs:ro
      - /var/run:/var/run:ro
      - /sys:/sys:ro
      - /var/lib/docker/:/var/lib/docker:ro
      - /dev/disk/:/dev/disk:ro
    privileged: true
    devices:
      - /dev/kmsg
    restart: unless-stopped
    networks:
      - monitoring-network

volumes:
  prometheus_data:
  grafana_data:

networks:
  monitoring-network:
    driver: bridge
EOF
    
    log "✅ Docker-compose creado"
}

# Función para instalar servicios
install_services() {
    log "🚀 Instalando servicios de monitoreo..."
    
    # Crear directorios necesarios
    mkdir -p grafana-data prometheus-data
    
    # Iniciar servicios
    docker-compose -f docker-compose.monitoring-cloud.yml up -d
    
    log "⏳ Esperando que los servicios estén listos..."
    sleep 30
    
    # Verificar servicios
    log "🔍 Verificando servicios..."
    docker ps | grep -E "(prometheus|grafana|node-exporter|cadvisor)"
}

# Función para mostrar información de acceso
show_access_info() {
    log "📊 Información de acceso:"
    echo ""
    echo "🌐 PROMETHEUS:"
    echo "   URL: http://34.61.228.49:9090"
    echo "   Usuario: (sin autenticación)"
    echo ""
    echo "📈 GRAFANA:"
    echo "   URL: http://34.61.228.49:3000"
    echo "   Usuario: admin"
    echo "   Contraseña: admin123"
    echo ""
    echo "📊 NODE EXPORTER:"
    echo "   URL: http://34.61.228.49:9100"
    echo ""
    echo "🐳 CADVISOR:"
    echo "   URL: http://34.61.228.49:8080"
    echo ""
    echo "🔧 COMANDOS ÚTILES:"
    echo "   # Ver logs de Prometheus:"
    echo "   docker logs prometheus-cloud"
    echo ""
    echo "   # Ver logs de Grafana:"
    echo "   docker logs grafana-cloud"
    echo ""
    echo "   # Reiniciar servicios:"
    echo "   docker-compose -f docker-compose.monitoring-cloud.yml restart"
    echo ""
    echo "   # Detener servicios:"
    echo "   docker-compose -f docker-compose.monitoring-cloud.yml down"
}

# Función principal
main() {
    log "Iniciando instalación de monitoreo en la nube..."
    
    create_prometheus_config
    create_docker_compose
    install_services
    show_access_info
    
    log "🎉 Instalación completada!"
    log "📊 Accede a Grafana: http://34.61.228.49:3000"
    log "📈 Accede a Prometheus: http://34.61.228.49:9090"
}

# Ejecutar función principal
main "$@"
