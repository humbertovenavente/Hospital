#!/bin/bash

echo "🔄 Reiniciando Oracle y Backend para puerto 1524..."
echo "=================================================="

# Detener todo
echo "1. Deteniendo todos los servicios..."
docker-compose down

# Iniciar Oracle
echo "2. Iniciando Oracle en puerto 1524..."
docker-compose up -d oracle

# Esperar a que Oracle esté listo
echo "3. Esperando a que Oracle esté listo..."
sleep 30

# Verificar que Oracle esté funcionando
echo "4. Verificando Oracle..."
if docker exec hospital-oracle-xe sqlplus -L "sys/Unis@//localhost:1521/XEPDB1" AS SYSDBA <<< "SELECT 1 FROM DUAL;" > /dev/null 2>&1; then
    echo "   ✅ Oracle funcionando en puerto 1524"
else
    echo "   ❌ Error: Oracle no responde"
    docker logs hospital-oracle-xe --tail 20
    exit 1
fi

# Iniciar backend
echo "5. Iniciando backend..."
docker-compose up -d backend

# Esperar a que backend esté listo
echo "6. Esperando a que backend esté listo..."
sleep 15

# Probar conexión
echo "7. Probando conexión del backend..."
if curl -s http://localhost:8084/usuarios/login > /dev/null 2>&1; then
    echo "   ✅ Backend respondiendo"
else
    echo "   ⚠️  Backend no responde (puede estar iniciando)"
fi

# Iniciar frontend
echo "8. Iniciando frontend..."
docker-compose up -d frontend

echo ""
echo "✅ ¡Listo! Servicios reiniciados con puerto 1524."
echo "🌐 URLs:"
echo "   Oracle: localhost:1524"
echo "   Backend: http://localhost:8084"
echo "   Frontend: http://localhost:5176"
echo ""
echo "🔍 Para verificar:"
echo "   docker ps"
echo "   docker logs hospital-oracle-xe"
echo "   docker logs hospital-backend-prod"

