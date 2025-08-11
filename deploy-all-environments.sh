#!/bin/bash

# Script para desplegar todos los entornos del hospital usando Oracle compartido
# Este script inicia primero el contenedor Oracle y luego los demás servicios

set -e

echo "🏥 Iniciando despliegue de todos los entornos del Hospital..."
echo "=========================================================="

# Función para verificar si un contenedor está ejecutándose
check_container() {
    local container_name=$1
    if docker ps --format "table {{.Names}}" | grep -q "^${container_name}$"; then
        return 0
    else
        return 1
    fi
}

# Función para esperar a que Oracle esté listo
wait_for_oracle() {
    echo "⏳ Esperando a que Oracle esté listo..."
    local max_attempts=30
    local attempt=1
    
    while [ $attempt -le $max_attempts ]; do
        if docker exec hospital-oracle-xe sqlplus -L "sys/Unis@//localhost:1521/XE" AS SYSDBA @/opt/oracle/scripts/startup/setup-oracle-users.sql > /dev/null 2>&1; then
            echo "✅ Oracle está listo y los usuarios han sido configurados"
            return 0
        fi
        
        echo "   Intento $attempt/$max_attempts - Oracle aún no está listo..."
        sleep 10
        attempt=$((attempt + 1))
    done
    
    echo "❌ Error: Oracle no se pudo inicializar en el tiempo esperado"
    return 1
}

# Paso 1: Iniciar Oracle (base de datos compartida)
echo "📊 Paso 1: Iniciando contenedor Oracle compartido..."
if ! check_container "hospital-oracle-xe"; then
    docker-compose up -d oracle
    echo "   Oracle iniciado"
else
    echo "   Oracle ya está ejecutándose"
fi

# Esperar a que Oracle esté listo
if ! wait_for_oracle; then
    echo "❌ Error: No se pudo inicializar Oracle. Abortando despliegue."
    exit 1
fi

# Paso 2: Iniciar entorno de Producción
echo "🚀 Paso 2: Iniciando entorno de Producción..."
docker-compose up -d backend frontend
echo "   Producción iniciada (Backend: puerto 8084, Frontend: puerto 5176)"

# Paso 3: Iniciar entorno de Desarrollo
echo "🔧 Paso 3: Iniciando entorno de Desarrollo..."
docker-compose -f docker-compose.dev.yml up -d
echo "   Desarrollo iniciado (Backend: puerto 8081, Frontend: puerto 5173, Nginx: puerto 8085)"

# Paso 4: Iniciar entorno de QA
echo "🧪 Paso 4: Iniciando entorno de QA..."
docker-compose -f docker-compose.qa.yml up -d
echo "   QA iniciado (Backend: puerto 8082, Frontend: puerto 5175, Nginx: puerto 8083)"

# Paso 5: Iniciar herramientas de monitoreo
echo "📈 Paso 5: Iniciando herramientas de monitoreo..."
docker-compose -f docker-compose.dev.yml up -d prometheus-dev grafana-dev
echo "   Monitoreo iniciado (Prometheus: puerto 9090, Grafana: puerto 3000)"

# Paso 6: Verificar estado de todos los contenedores
echo "🔍 Paso 6: Verificando estado de todos los contenedores..."
echo ""
docker ps --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}"

echo ""
echo "🎉 ¡Despliegue completado exitosamente!"
echo ""
echo "📋 Resumen de puertos:"
echo "   Oracle: localhost:1521"
echo "   Producción: Backend (8084), Frontend (5176)"
echo "   Desarrollo: Backend (8081), Frontend (5173), Nginx (8085)"
echo "   QA: Backend (8082), Frontend (5175), Nginx (8083)"
echo "   Monitoreo: Prometheus (9090), Grafana (3000)"
echo ""
echo "🔗 URLs de acceso:"
echo "   Producción: http://localhost:5176"
echo "   Desarrollo: http://localhost:8085"
echo "   QA: http://localhost:8083"
echo "   Grafana: http://localhost:3000 (admin/admin123)"
echo ""
echo "💡 Todos los entornos ahora usan la misma base de datos Oracle con usuarios separados:"
echo "   - C##PROYECTO (Desarrollo)"
echo "   - Hospital2 (QA)"
echo "   - Hospital3 (Producción)"
