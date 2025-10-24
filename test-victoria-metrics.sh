#!/bin/bash

echo "🧪 Enviando métricas de prueba a VictoriaMetrics..."

# Esperar a que VictoriaMetrics esté listo
echo "⏳ Esperando a que VictoriaMetrics esté listo..."
until curl -s http://localhost:8428/health > /dev/null; do
    echo "Esperando a VictoriaMetrics..."
    sleep 2
done

echo "✅ VictoriaMetrics está listo"

# Enviar métricas de prueba
echo "📊 Enviando métricas de prueba..."

# Métricas del sistema
curl -X POST http://localhost:8428/api/v1/import/prometheus -d '
# HELP hospital_system_info Información del sistema hospital
# TYPE hospital_system_info gauge
hospital_system_info{environment="dev",service="hospital-backend"} 1
hospital_system_info{environment="qa",service="hospital-backend"} 1
hospital_system_info{environment="prod",service="hospital-backend"} 1

# HELP hospital_requests_total Total de requests al hospital
# TYPE hospital_requests_total counter
hospital_requests_total{environment="dev",method="GET",status="200"} 150
hospital_requests_total{environment="dev",method="POST",status="200"} 45
hospital_requests_total{environment="qa",method="GET",status="200"} 200
hospital_requests_total{environment="qa",method="POST",status="200"} 60
hospital_requests_total{environment="prod",method="GET",status="200"} 500
hospital_requests_total{environment="prod",method="POST",status="200"} 120

# HELP hospital_response_time_seconds Tiempo de respuesta del hospital
# TYPE hospital_response_time_seconds histogram
hospital_response_time_seconds_bucket{environment="dev",le="0.1"} 100
hospital_response_time_seconds_bucket{environment="dev",le="0.5"} 120
hospital_response_time_seconds_bucket{environment="dev",le="1.0"} 130
hospital_response_time_seconds_bucket{environment="dev",le="+Inf"} 150
hospital_response_time_seconds_sum{environment="dev"} 45.2
hospital_response_time_seconds_count{environment="dev"} 150

# HELP hospital_active_users Usuarios activos en el hospital
# TYPE hospital_active_users gauge
hospital_active_users{environment="dev"} 25
hospital_active_users{environment="qa"} 15
hospital_active_users{environment="prod"} 150
'

echo "✅ Métricas de prueba enviadas"
echo "🌐 Visita http://localhost:8428/vmui para ver las métricas"
