#!/bin/bash

echo "🐳 Iniciando VictoriaMetrics con Docker..."

# Verificar que el archivo docker-compose existe
if [ ! -f "docker-compose.victoria-metrics.yml" ]; then
    echo "❌ Error: No se encontró docker-compose.victoria-metrics.yml"
    exit 1
fi

# Iniciar servicios
echo "🔄 Iniciando servicios de monitoreo..."
docker-compose -f docker-compose.victoria-metrics.yml up -d

# Esperar a que los servicios estén listos
echo "⏳ Esperando a que los servicios estén listos..."
sleep 10

# Verificar estado de los servicios
echo "🔍 Verificando estado de los servicios..."
docker-compose -f docker-compose.victoria-metrics.yml ps

echo ""
echo "✅ Servicios de monitoreo iniciados:"
echo "🌐 VictoriaMetrics UI: http://localhost:8428/vmui"
echo "📊 VictoriaMetrics API: http://localhost:8428"
echo "📈 Prometheus UI: http://localhost:9090"
echo "🔍 Métricas internas: http://localhost:8429/metrics"
echo ""
echo "Para detener los servicios:"
echo "docker-compose -f docker-compose.victoria-metrics.yml down"
