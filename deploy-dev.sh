#!/bin/bash

echo "🚀 Iniciando despliegue en DESARROLLO..."

# Variables
ENVIRONMENT="dev"
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
docker-compose down

# Limpiar imágenes antiguas
echo "🧹 Limpiando imágenes antiguas..."
docker system prune -f

# Construir nuevas imágenes
echo "🔨 Construyendo imágenes Docker..."
docker build -t $DOCKER_REGISTRY/$BACKEND_IMAGE:$VERSION ./backend
docker build -t $DOCKER_REGISTRY/$FRONTEND_IMAGE:$VERSION -f Dockerfile.frontend .

# Tag de imágenes para desarrollo
echo "🏷️  Etiquetando imágenes para desarrollo..."
docker tag $DOCKER_REGISTRY/$BACKEND_IMAGE:$VERSION $DOCKER_REGISTRY/$BACKEND_IMAGE:dev
docker tag $DOCKER_REGISTRY/$FRONTEND_IMAGE:$VERSION $DOCKER_REGISTRY/$FRONTEND_IMAGE:dev

# Desplegar con docker-compose
echo "🚀 Desplegando aplicación..."
docker-compose up -d

# Verificar salud de los servicios
echo "🏥 Verificando salud de los servicios..."
sleep 30

# Health checks
echo "🔍 Realizando health checks..."

# Backend health check
if curl -f http://localhost:8080/q/health > /dev/null 2>&1; then
    echo "✅ Backend: OK"
else
    echo "❌ Backend: FAILED"
    exit 1
fi

# Frontend health check
if curl -f http://localhost:5174 > /dev/null 2>&1; then
    echo "✅ Frontend: OK"
else
    echo "❌ Frontend: FAILED"
    exit 1
fi

echo "🎉 Despliegue en DESARROLLO completado exitosamente!"
echo "📊 URLs de acceso:"
echo "   Frontend: http://localhost:5174"
echo "   Backend: http://localhost:8080"
echo "   Health Check: http://localhost:8080/q/health" 