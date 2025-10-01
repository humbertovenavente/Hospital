#!/bin/bash

# Script para copiar archivos al servidor de la nube
echo "📤 COPIANDO ARCHIVOS AL SERVIDOR DE LA NUBE"
echo "==========================================="

SERVER_IP="34.61.228.49"
SERVER_USER="root"

echo "🌐 Servidor: $SERVER_IP"
echo "👤 Usuario: $SERVER_USER"
echo ""

# Función para copiar archivos
copy_files() {
    echo "📁 Copiando archivos al servidor..."
    
    # Copiar script de instalación
    scp setup-monitoring-cloud.sh $SERVER_USER@$SERVER_IP:/root/
    
    # Copiar configuración de Prometheus
    scp prometheus-cloud.yml $SERVER_USER@$SERVER_IP:/root/ 2>/dev/null || echo "⚠️ prometheus-cloud.yml no existe localmente (se creará en el servidor)"
    
    # Copiar docker-compose
    scp docker-compose.monitoring-cloud.yml $SERVER_USER@$SERVER_IP:/root/ 2>/dev/null || echo "⚠️ docker-compose.monitoring-cloud.yml no existe localmente (se creará en el servidor)"
    
    echo "✅ Archivos copiados"
}

# Función para ejecutar instalación en el servidor
run_installation() {
    echo "🚀 Ejecutando instalación en el servidor..."
    
    ssh $SERVER_USER@$SERVER_IP << 'EOF'
        echo "🌐 Conectado al servidor de la nube"
        echo "=================================="
        
        # Hacer ejecutable el script
        chmod +x setup-monitoring-cloud.sh
        
        # Ejecutar instalación
        ./setup-monitoring-cloud.sh
EOF
}

# Función para mostrar información
show_info() {
    echo ""
    echo "📋 INFORMACIÓN DE ACCESO:"
    echo "========================="
    echo ""
    echo "🌐 PROMETHEUS:"
    echo "   URL: http://34.61.228.49:9090"
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
    echo "🔧 COMANDOS PARA EL SERVIDOR:"
    echo "   ssh root@34.61.228.49"
    echo "   docker ps"
    echo "   docker logs prometheus-cloud"
    echo "   docker logs grafana-cloud"
}

# Función principal
main() {
    echo "¿Quieres copiar los archivos al servidor y ejecutar la instalación?"
    read -p "[y/N]: " -n 1 -r
    echo
    
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        copy_files
        run_installation
        show_info
    else
        echo "❌ Instalación cancelada"
        echo ""
        echo "💡 Para instalar manualmente:"
        echo "   1. ssh root@34.61.228.49"
        echo "   2. Copia el archivo setup-monitoring-cloud.sh"
        echo "   3. Ejecuta: chmod +x setup-monitoring-cloud.sh"
        echo "   4. Ejecuta: ./setup-monitoring-cloud.sh"
    fi
}

# Ejecutar función principal
main "$@"
