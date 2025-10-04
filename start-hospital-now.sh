#!/bin/bash

# Script SIMPLE para iniciar Hospital AHORA
# Soluciona el problema de conectividad inmediatamente

echo "🏥 INICIANDO HOSPITAL AHORA..."

# Limpiar todo
echo "🧹 Limpiando contenedores existentes..."
docker stop $(docker ps -q --filter name=hospital) 2>/dev/null || true
docker rm -f $(docker ps -aq --filter name=hospital) 2>/dev/null || true

# Construir backend
echo "🔨 Construyendo backend..."
cd backend
docker build -f Dockerfile.jvm -t hospital-backend-prod .
cd ..

# Construir frontend  
echo "🔨 Construyendo frontend..."
docker build -f Dockerfile.frontend.cloud -t hospital-frontend-prod .

# Iniciar backend
echo "🚀 Iniciando backend..."
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

# Iniciar frontend
echo "🚀 Iniciando frontend..."
docker run -d \
  --name hospital-frontend-prod-cloud \
  --restart=always \
  -p 8021:80 \
  hospital-frontend-prod

# Esperar
echo "⏳ Esperando que los servicios inicien..."
sleep 15

# Verificar
echo "✅ Verificando servicios..."
echo "📊 Contenedores:"
docker ps --filter "name=hospital" --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}"

echo ""
echo "🔍 Conectividad:"
echo -n "Backend (8020): "
if curl -f http://localhost:8020/q/health > /dev/null 2>&1; then
    echo "✅ OK"
else
    echo "❌ ERROR"
fi

echo -n "Frontend (8021): "
if curl -f http://localhost:8021 > /dev/null 2>&1; then
    echo "✅ OK"
else
    echo "❌ ERROR"
fi

echo ""
echo "🌐 URLs de acceso:"
echo "   Frontend: http://34.61.228.49:8021"
echo "   Backend:  http://34.61.228.49:8020"
echo "   Health:   http://34.61.228.49:8020/q/health"

echo ""
echo "🎉 HOSPITAL INICIADO!"

