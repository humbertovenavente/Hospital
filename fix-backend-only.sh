#!/bin/bash

# Script para corregir SOLO el backend con configuración correcta
# Más rápido que reconstruir todo

echo "🔧 Corrigiendo SOLO el backend..."

# Detener y eliminar solo el backend
echo "🛑 Eliminando backend existente..."
docker stop hospital-backend-prod-cloud 2>/dev/null || true
docker rm -f hospital-backend-prod-cloud 2>/dev/null || true

# Esperar limpieza
sleep 3

# Recrear backend con configuración CORRECTA
echo "🚀 Recreando backend con configuración CORRECTA..."
docker run -d \
  --name hospital-backend-prod-cloud \
  --restart=always \
  -p 8020:8080 \
  -e QUARKUS_PROFILE=prod \
  -e QUARKUS_DATASOURCE_JDBC_URL=jdbc:oracle:thin:@35.208.14.178:1523/XE \
  -e QUARKUS_DATASOURCE_USERNAME=C##HOSPITAL \
  -e QUARKUS_DATASOURCE_PASSWORD=hospital123 \
  -e QUARKUS_HTTP_CORS=true \
  -e QUARKUS_HTTP_CORS_ORIGINS=http://34.61.228.49:8021,http://localhost:8021,http://localhost:5173,http://localhost:8080 \
  -e QUARKUS_HTTP_CORS_METHODS=GET,POST,PUT,DELETE,OPTIONS,PATCH,HEAD \
  -e QUARKUS_HTTP_CORS_HEADERS=* \
  -e QUARKUS_HTTP_CORS_ACCESS_CONTROL_ALLOW_CREDENTIALS=true \
  -e QUARKUS_HTTP_CORS_EXPOSED_HEADERS=Content-Disposition \
  hospital-backend-prod

# Verificar
echo "✅ Verificando backend..."
sleep 10

if curl -f http://localhost:8020/q/health > /dev/null 2>&1; then
    echo "✅ Backend respondiendo correctamente"
    echo "🌐 Backend: http://34.61.228.49:8020"
    echo "🔍 Health: http://34.61.228.49:8020/q/health"
else
    echo "❌ Backend aún no responde"
    echo "📋 Revisar logs: docker logs hospital-backend-prod-cloud"
fi

echo "🎉 Corrección del backend completada!"

