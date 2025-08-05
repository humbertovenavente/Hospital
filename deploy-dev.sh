#!/bin/bash

# Script de despliegue para ambiente de desarrollo
echo "🚀 Desplegando en ambiente de desarrollo..."

# Variables de entorno para desarrollo
export ENVIRONMENT="development"
export DB_USERNAME="C##PROYECTO"
export DB_PASSWORD="Unis"
export DB_URL="jdbc:oracle:thin:@localhost:1521/XEPDB1"

# Detener contenedores existentes
docker-compose down

# Construir y levantar servicios
docker-compose up -d --build

# Verificar que los servicios estén funcionando
echo "⏳ Esperando que los servicios estén listos..."
sleep 30

# Verificar estado de los servicios
docker-compose ps

echo "✅ Despliegue en desarrollo completado!"
echo "🌐 Frontend: http://localhost"
echo "🔧 Backend: http://localhost:8080"
echo "🗄️  Base de datos: localhost:1521" 