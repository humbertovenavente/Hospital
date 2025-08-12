#!/bin/bash

echo "🔄 Reiniciando con configuración correcta localhost:1524/XEPDB1..."
echo "=============================================================="

# Detener todo
echo "1. Deteniendo todos los servicios..."
docker-compose down

# Limpiar redes si es necesario
echo "2. Limpiando redes..."
docker network prune -f

# Iniciar Oracle
echo "3. Iniciando Oracle en puerto 1524..."
docker-compose up -d oracle

# Esperar a que Oracle esté listo
echo "4. Esperando a que Oracle esté listo..."
sleep 30

# Verificar que Oracle esté funcionando en puerto 1524
echo "5. Verificando Oracle en puerto 1524..."
if docker exec hospital-oracle-xe sqlplus -L "sys/Unis@//localhost:1521/XEPDB1" AS SYSDBA <<< "SELECT 1 FROM DUAL;" > /dev/null 2>&1; then
    echo "   ✅ Oracle funcionando internamente"
else
    echo "   ❌ Error: Oracle no responde internamente"
    docker logs hospital-oracle-xe --tail 20
    exit 1
fi

# Verificar acceso desde el host
echo "6. Verificando acceso desde host en puerto 1524..."
if sqlplus -L "Hospital3/Unis@localhost:1524/XEPDB1" <<< "SELECT 1 FROM DUAL;" > /dev/null 2>&1; then
    echo "   ✅ Acceso desde host funcionando"
else
    echo "   ⚠️  Acceso desde host no funciona (puede que no tengas sqlplus instalado)"
fi

# Iniciar backend
echo "7. Iniciando backend..."
docker-compose up -d backend

# Esperar a que backend esté listo
echo "8. Esperando a que backend esté listo..."
sleep 20

# Probar conexión del backend
echo "9. Probando conexión del backend..."
if curl -s http://localhost:8084/usuarios/login > /dev/null 2>&1; then
    echo "   ✅ Backend respondiendo"
else
    echo "   ⚠️  Backend no responde (puede estar iniciando)"
fi

# Iniciar frontend
echo "10. Iniciando frontend..."
docker-compose up -d frontend

echo ""
echo "✅ ¡Listo! Configuración aplicada:"
echo "=================================="
echo "   🔌 Oracle: localhost:1524/XEPDB1"
echo "   👤 Usuario: Hospital3"
echo "   🌐 Backend: http://localhost:8084"
echo "   🖥️  Frontend: http://localhost:5176"
echo ""
echo "🔍 Para verificar:"
echo "   docker ps"
echo "   docker logs hospital-backend-prod | grep -i 'datasource\|oracle'"
echo "   curl -H 'Origin: http://localhost:5173' http://localhost:8084/usuarios/login"

