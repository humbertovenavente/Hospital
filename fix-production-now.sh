#!/bin/bash

# Script URGENTE para corregir producción AHORA
# Resuelve el problema de conectividad inmediatamente

echo "🚨 CORRIGIENDO PRODUCCIÓN AHORA..."

# Detener y eliminar contenedores existentes
echo "🛑 Eliminando contenedores existentes..."
docker stop hospital-backend-prod-cloud hospital-frontend-prod-cloud 2>/dev/null || true
docker rm -f hospital-backend-prod-cloud hospital-frontend-prod-cloud 2>/dev/null || true

# Esperar limpieza completa
sleep 5

# Construir imágenes rápidamente
echo "🔨 Construyendo imágenes..."
cd backend && docker build -f Dockerfile.jvm -t hospital-backend-prod . && cd ..
docker build -f Dockerfile.frontend.cloud -t hospital-frontend-prod .

# Desplegar backend con configuración CORRECTA
echo "🚀 Desplegando backend con configuración CORRECTA..."
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

# Desplegar frontend
echo "🚀 Desplegando frontend..."
docker run -d \
  --name hospital-frontend-prod-cloud \
  --restart=always \
  -p 8021:80 \
  hospital-frontend-prod

# Verificar despliegue
echo "✅ Verificando despliegue..."
sleep 10

echo "📊 Estado de contenedores:"
docker ps --filter "name=hospital-.*-prod-cloud" --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}"

echo ""
echo "🔍 Verificando conectividad..."

# Verificar backend
if curl -f http://localhost:8020/q/health > /dev/null 2>&1; then
    echo "✅ Backend respondiendo correctamente"
else
    echo "❌ Backend no responde - revisar logs: docker logs hospital-backend-prod-cloud"
fi

# Verificar frontend
if curl -f http://localhost:8021 > /dev/null 2>&1; then
    echo "✅ Frontend respondiendo correctamente"
else
    echo "❌ Frontend no responde - revisar logs: docker logs hospital-frontend-prod-cloud"
fi

echo ""
echo "🌐 URLs de acceso:"
echo "   - Frontend: http://34.61.228.49:8021"
echo "   - Backend:  http://34.61.228.49:8020"
echo "   - Health:   http://34.61.228.49:8020/q/health"

echo ""
echo "🎉 CORRECCIÓN COMPLETADA!"

