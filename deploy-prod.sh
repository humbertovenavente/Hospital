#!/bin/bash

# Script de despliegue para ambiente de producción
echo "🚀 Desplegando en ambiente de producción..."

# Variables de entorno para producción
export ENVIRONMENT="production"
export DB_USERNAME="C##HOSPITAL3"
export DB_PASSWORD="Unis"
export DB_URL="jdbc:oracle:thin:@prod-db-server:1521/XEPDB1"

# Crear backup antes del despliegue
echo "💾 Creando backup de la aplicación actual..."
docker-compose -f docker-compose.prod.yml exec backend pg_dump > backup_$(date +%Y%m%d_%H%M%S).sql

# Detener contenedores existentes
docker-compose -f docker-compose.prod.yml down

# Construir y levantar servicios
docker-compose -f docker-compose.prod.yml up -d --build

# Verificar que los servicios estén funcionando
echo "⏳ Esperando que los servicios estén listos..."
sleep 30

# Verificar estado de los servicios
docker-compose -f docker-compose.prod.yml ps

# Ejecutar health checks
echo "🏥 Ejecutando health checks..."
curl -f http://prod-api.hospital.com/health || exit 1

echo "✅ Despliegue en producción completado!"
echo "🌐 Frontend: https://hospital.com"
echo "🔧 Backend: https://api.hospital.com"
echo "🗄️  Base de datos: prod-db-server:1521" 