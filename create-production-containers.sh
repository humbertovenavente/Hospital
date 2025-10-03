#!/bin/bash

# Script para crear contenedores de producción en Portainer
# Basado en la configuración de QA que funciona correctamente

echo "🏥 Creando contenedores de producción para Hospital..."

# Detener contenedores existentes si existen
echo "🛑 Deteniendo contenedores existentes..."
docker stop hospital-backend-prod-cloud hospital-frontend-prod-cloud 2>/dev/null || true
docker rm -f hospital-backend-prod-cloud hospital-frontend-prod-cloud 2>/dev/null || true

# Construir imágenes de producción
echo "🔨 Construyendo imagen del backend..."
cd backend
docker build -f Dockerfile.jvm -t hospital-backend-prod .
cd ..

echo "🔨 Construyendo imagen del frontend..."
docker build -f Dockerfile.frontend.cloud -t hospital-frontend-prod .

# Crear contenedor del backend con configuración de producción
echo "🚀 Creando contenedor del backend..."
docker run -d \
  --name hospital-backend-prod-cloud \
  --restart=always \
  -p 8020:8080 \
  -e QUARKUS_PROFILE=prod \
  -e QUARKUS_DATASOURCE_JDBC_URL=jdbc:oracle:thin:@34.56.133.126:1521/XE \
  -e QUARKUS_DATASOURCE_USERNAME=system \
  -e QUARKUS_DATASOURCE_PASSWORD=Oracle123 \
  -e QUARKUS_HTTP_CORS=true \
  -e QUARKUS_HTTP_CORS_ORIGINS=http://34.61.228.49:8021,http://localhost:8021,http://localhost:5173,http://localhost:8080 \
  -e QUARKUS_HTTP_CORS_METHODS=GET,POST,PUT,DELETE,OPTIONS,PATCH,HEAD \
  -e QUARKUS_HTTP_CORS_HEADERS=* \
  -e QUARKUS_HTTP_CORS_ACCESS_CONTROL_ALLOW_CREDENTIALS=true \
  -e QUARKUS_HTTP_CORS_EXPOSED_HEADERS=Content-Disposition \
  hospital-backend-prod

# Crear contenedor del frontend
echo "🚀 Creando contenedor del frontend..."
docker run -d \
  --name hospital-frontend-prod-cloud \
  --restart=always \
  -p 8021:80 \
  hospital-frontend-prod

# Verificar que los contenedores estén funcionando
echo "✅ Verificando estado de los contenedores..."
sleep 10

echo "📊 Estado de los contenedores:"
docker ps --filter "name=hospital-.*-prod-cloud" --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}"

echo ""
echo "🌐 URLs de acceso:"
echo "Frontend: http://34.61.228.49:8021"
echo "Backend:  http://34.61.228.49:8020"
echo "Health:   http://34.61.228.49:8020/q/health"

echo ""
echo "🔍 Para ver los logs:"
echo "Backend:  docker logs hospital-backend-prod-cloud"
echo "Frontend: docker logs hospital-frontend-prod-cloud"

echo ""
echo "✅ ¡Contenedores de producción creados exitosamente!"
