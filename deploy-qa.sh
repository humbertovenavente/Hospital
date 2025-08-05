#!/bin/bash

# Script de despliegue para ambiente de QA
echo "🚀 Desplegando en ambiente de QA..."

# Variables de entorno para QA
export ENVIRONMENT="qa"
export DB_USERNAME="C##HOSPITAL2"
export DB_PASSWORD="Unis"
export DB_URL="jdbc:oracle:thin:@qa-db-server:1521/XEPDB1"

# Detener contenedores existentes
docker-compose -f docker-compose.qa.yml down

# Construir y levantar servicios
docker-compose -f docker-compose.qa.yml up -d --build

# Verificar que los servicios estén funcionando
echo "⏳ Esperando que los servicios estén listos..."
sleep 30

# Verificar estado de los servicios
docker-compose -f docker-compose.qa.yml ps

echo "✅ Despliegue en QA completado!"
echo "🌐 Frontend: http://qa.hospital.com"
echo "🔧 Backend: http://qa-api.hospital.com"
echo "🗄️  Base de datos: qa-db-server:1521" 