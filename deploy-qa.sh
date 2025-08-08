#!/bin/bash

echo "🚀 Iniciando despliegue en QA..."

# Variables
ENVIRONMENT="qa"
DOCKER_REGISTRY="hospital-registry"
BACKEND_IMAGE="hospital-backend"
FRONTEND_IMAGE="hospital-frontend"
VERSION=$(date +%Y%m%d_%H%M%S)

echo "📋 Configuración:"
echo "   Ambiente: $ENVIRONMENT"
echo "   Versión: $VERSION"
echo "   Registro: $DOCKER_REGISTRY"

# Detener contenedores existentes
echo "🛑 Deteniendo contenedores existentes..."
docker-compose -f docker-compose.qa.yml down

# Limpiar imágenes antiguas
echo "🧹 Limpiando imágenes antiguas..."
docker system prune -f

# Construir nuevas imágenes
echo "🔨 Construyendo imágenes Docker..."
docker build -t $DOCKER_REGISTRY/$BACKEND_IMAGE:$VERSION ./backend
docker build -t $DOCKER_REGISTRY/$FRONTEND_IMAGE:$VERSION -f Dockerfile.frontend .

# Tag de imágenes para QA
echo "🏷️  Etiquetando imágenes para QA..."
docker tag $DOCKER_REGISTRY/$BACKEND_IMAGE:$VERSION $DOCKER_REGISTRY/$BACKEND_IMAGE:qa
docker tag $DOCKER_REGISTRY/$FRONTEND_IMAGE:$VERSION $DOCKER_REGISTRY/$FRONTEND_IMAGE:qa

# Desplegar con docker-compose
echo "🚀 Desplegando aplicación en QA..."
docker-compose -f docker-compose.qa.yml up -d

# Verificar salud de los servicios
echo "🏥 Verificando salud de los servicios..."
sleep 45

# Health checks
echo "🔍 Realizando health checks..."

# Backend health check
if curl -f http://localhost:8082/q/health > /dev/null 2>&1; then
    echo "✅ Backend QA: OK"
else
    echo "❌ Backend QA: FAILED"
    exit 1
fi

# Frontend health check
if curl -f http://localhost:5175 > /dev/null 2>&1; then
    echo "✅ Frontend QA: OK"
else
    echo "❌ Frontend QA: FAILED"
    exit 1
fi

# Nginx health check
if curl -f http://localhost:8083 > /dev/null 2>&1; then
    echo "✅ Nginx QA: OK"
else
    echo "❌ Nginx QA: FAILED"
    exit 1
fi

echo "🎉 Despliegue en QA completado exitosamente!"
echo "📊 URLs de acceso:"
echo "   Frontend: http://localhost:5175"
echo "   Backend: http://localhost:8082"
echo "   Nginx Proxy: http://localhost:8083"
echo "   Health Check: http://localhost:8082/q/health" 