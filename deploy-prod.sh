#!/bin/bash

echo "🚀 Iniciando despliegue en PRODUCCIÓN..."

# Variables
ENVIRONMENT="prod"
DOCKER_REGISTRY="hospital-registry"
BACKEND_IMAGE="hospital-backend"
FRONTEND_IMAGE="hospital-frontend"
VERSION=$(date +%Y%m%d_%H%M%S)

echo "📋 Configuración:"
echo "   Ambiente: $ENVIRONMENT"
echo "   Versión: $VERSION"
echo "   Registro: $DOCKER_REGISTRY"

# Verificar que estamos en la rama master
if [ "$(git branch --show-current)" != "master" ]; then
    echo "❌ ERROR: Solo se puede desplegar en producción desde la rama master"
    echo "   Rama actual: $(git branch --show-current)"
    exit 1
fi

# Confirmar despliegue
echo "⚠️  ADVERTENCIA: Estás a punto de desplegar en PRODUCCIÓN"
read -p "¿Estás seguro? (escribe 'PRODUCCION' para confirmar): " confirm
if [ "$confirm" != "PRODUCCION" ]; then
    echo "❌ Despliegue cancelado"
    exit 1
fi

# Detener contenedores existentes
echo "🛑 Deteniendo contenedores existentes..."
docker-compose -f docker-compose.prod.yml down

# Limpiar imágenes antiguas
echo "🧹 Limpiando imágenes antiguas..."
docker system prune -f

# Construir nuevas imágenes
echo "🔨 Construyendo imágenes Docker..."
docker build -t $DOCKER_REGISTRY/$BACKEND_IMAGE:$VERSION ./backend
docker build -t $DOCKER_REGISTRY/$FRONTEND_IMAGE:$VERSION -f Dockerfile.frontend .

# Tag de imágenes para producción
echo "🏷️  Etiquetando imágenes para producción..."
docker tag $DOCKER_REGISTRY/$BACKEND_IMAGE:$VERSION $DOCKER_REGISTRY/$BACKEND_IMAGE:prod
docker tag $DOCKER_REGISTRY/$FRONTEND_IMAGE:$VERSION $DOCKER_REGISTRY/$FRONTEND_IMAGE:prod

# Backup de la base de datos actual
echo "💾 Realizando backup de la base de datos..."
docker exec hospital-oracle-prod expdp C##PROYECTO/Unis@localhost:1521/XEPDB1 \
    directory=DATA_PUMP_DIR \
    dumpfile=backup_$(date +%Y%m%d_%H%M%S).dmp \
    logfile=backup_$(date +%Y%m%d_%H%M%S).log

# Desplegar con docker-compose
echo "🚀 Desplegando aplicación en producción..."
docker-compose -f docker-compose.prod.yml up -d

# Verificar salud de los servicios
echo "🏥 Verificando salud de los servicios..."
sleep 60

# Health checks
echo "🔍 Realizando health checks..."

# Backend health check
if curl -f http://localhost:8084/q/health > /dev/null 2>&1; then
    echo "✅ Backend Producción: OK"
else
    echo "❌ Backend Producción: FAILED"
    echo "🚨 ROLLBACK AUTOMÁTICO..."
    docker-compose -f docker-compose.prod.yml down
    exit 1
fi

# Frontend health check
if curl -f http://localhost:5176 > /dev/null 2>&1; then
    echo "✅ Frontend Producción: OK"
else
    echo "❌ Frontend Producción: FAILED"
    echo "🚨 ROLLBACK AUTOMÁTICO..."
    docker-compose -f docker-compose.prod.yml down
    exit 1
fi

# Nginx health check
if curl -f http://localhost:8085 > /dev/null 2>&1; then
    echo "✅ Nginx Producción: OK"
else
    echo "❌ Nginx Producción: FAILED"
    echo "🚨 ROLLBACK AUTOMÁTICO..."
    docker-compose -f docker-compose.prod.yml down
    exit 1
fi

# Prometheus health check
if curl -f http://localhost:9090/-/healthy > /dev/null 2>&1; then
    echo "✅ Prometheus: OK"
else
    echo "❌ Prometheus: FAILED"
fi

# Grafana health check
if curl -f http://localhost:3000/api/health > /dev/null 2>&1; then
    echo "✅ Grafana: OK"
else
    echo "❌ Grafana: FAILED"
fi

echo "🎉 Despliegue en PRODUCCIÓN completado exitosamente!"
echo "📊 URLs de acceso:"
echo "   Frontend: http://localhost:5176"
echo "   Backend: http://localhost:8084"
echo "   Nginx Proxy: http://localhost:8085"
echo "   Health Check: http://localhost:8084/q/health"
echo "   Prometheus: http://localhost:9090"
echo "   Grafana: http://localhost:3000 (admin/admin123)"

# Enviar notificación de éxito
echo "📧 Enviando notificación de despliegue exitoso..."
# Aquí iría el comando para enviar email de notificación 