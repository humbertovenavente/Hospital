#!/bin/bash

echo "🔄 Reiniciando backend para CORS simple..."
echo "=========================================="

# Detener backend
echo "1. Deteniendo backend..."
docker-compose stop backend

# Reconstruir
echo "2. Reconstruyendo..."
docker-compose build --no-cache backend

# Iniciar
echo "3. Iniciando backend..."
docker-compose up -d backend

# Esperar
echo "4. Esperando a que esté listo..."
sleep 10

# Probar
echo "5. Probando CORS..."
curl -H "Origin: http://localhost:5173" http://localhost:8084/usuarios/login

echo ""
echo "✅ ¡Listo! Backend reiniciado con CORS simple."
echo "🌐 URL: http://localhost:8084"

