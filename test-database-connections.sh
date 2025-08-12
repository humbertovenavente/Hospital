#!/bin/bash

# Script para probar las conexiones de base de datos de todos los entornos
# Este script verifica que cada entorno pueda conectarse a Oracle con su usuario correspondiente

set -e

echo "🔍 Probando conexiones de base de datos para todos los entornos..."
echo "================================================================"
echo ""

# Función para verificar si un contenedor está ejecutándose
check_container() {
    local container_name=$1
    if docker ps --format "table {{.Names}}" | grep -q "^${container_name}$"; then
        return 0
    else
        return 1
    fi
}

# Función para probar conexión de base de datos
test_db_connection() {
    local container_name=$1
    local username=$2
    local password=$3
    local profile=$4
    
    echo "🧪 Probando conexión para $container_name (Usuario: $username, Perfil: $profile)..."
    
    if check_container "$container_name"; then
        # Probar conexión usando sqlplus desde el contenedor Oracle
        if docker exec hospital-oracle-xe sqlplus -L "$username/$password@//localhost:1521/XE" <<< "SELECT USER FROM DUAL;" > /dev/null 2>&1; then
            echo "   ✅ Conexión exitosa para $username"
            
            # Probar que la aplicación puede conectarse
            if docker exec "$container_name" curl -s http://localhost:8080/health > /dev/null 2>&1; then
                echo "   ✅ Aplicación respondiendo correctamente"
            else
                echo "   ⚠️  Aplicación no responde (puede estar iniciando)"
            fi
        else
            echo "   ❌ Error de conexión para $username"
            return 1
        fi
    else
        echo "   ❌ Contenedor $container_name no está ejecutándose"
        return 1
    fi
    
    echo ""
}

# Función para verificar usuarios en Oracle
verify_oracle_users() {
    echo "👥 Verificando usuarios en Oracle..."
    echo "=================================="
    
    if check_container "hospital-oracle-xe"; then
        echo "   Usuarios configurados:"
        docker exec hospital-oracle-xe sqlplus -L "sys/Unis@//localhost:1521/XE" AS SYSDBA <<< "
        SELECT username, account_status, default_tablespace 
        FROM dba_users 
        WHERE username IN ('C##PROYECTO', 'Hospital2', 'Hospital3')
        ORDER BY username;
        " | grep -E "(C##PROYECTO|Hospital2|Hospital3)" || echo "   ⚠️  No se pudieron obtener los usuarios"
    else
        echo "   ❌ Contenedor Oracle no está ejecutándose"
    fi
    
    echo ""
}

# Función para mostrar estado de contenedores
show_container_status() {
    echo "📊 Estado de los contenedores:"
    echo "=============================="
    
    docker ps --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}" | grep -E "(hospital|oracle)" || echo "   No hay contenedores del hospital ejecutándose"
    
    echo ""
}

# Función para mostrar logs de errores
show_error_logs() {
    echo "📝 Últimos logs de error (si los hay):"
    echo "======================================"
    
    for container in hospital-backend-prod hospital-backend-dev hospital-backend-qa; do
        if check_container "$container"; then
            echo "   🔍 $container:"
            docker logs --tail 5 "$container" 2>&1 | grep -i "error\|exception\|failed" || echo "      No hay errores recientes"
        fi
    done
    
    echo ""
}

# Función principal
main() {
    echo "🏥 Hospital - Test de Conexiones de Base de Datos"
    echo "================================================"
    echo ""
    
    # Verificar que Oracle esté ejecutándose
    if ! check_container "hospital-oracle-xe"; then
        echo "❌ Error: El contenedor Oracle no está ejecutándose"
        echo "   Ejecuta primero: docker-compose up -d oracle"
        exit 1
    fi
    
    # Mostrar estado de contenedores
    show_container_status
    
    # Verificar usuarios en Oracle
    verify_oracle_users
    
    # Probar conexiones de cada entorno
    echo "🔌 Probando conexiones de base de datos:"
    echo "========================================"
    
    # Producción
    if check_container "hospital-backend-prod"; then
        test_db_connection "hospital-backend-prod" "Hospital3" "Unis" "prod"
    else
        echo "⚠️  Contenedor de producción no está ejecutándose"
    fi
    
    # Desarrollo
    if check_container "hospital-backend-dev"; then
        test_db_connection "hospital-backend-dev" "C##PROYECTO" "Unis" "dev"
    else
        echo "⚠️  Contenedor de desarrollo no está ejecutándose"
    fi
    
    # QA
    if check_container "hospital-backend-qa"; then
        test_db_connection "hospital-backend-qa" "Hospital2" "Unis" "qa"
    else
        echo "⚠️  Contenedor de QA no está ejecutándose"
    fi
    
    # Mostrar logs de errores si los hay
    show_error_logs
    
    echo "🎯 Resumen de la prueba:"
    echo "========================"
    echo "   ✅ Oracle: $(check_container "hospital-oracle-xe" && echo "Ejecutándose" || echo "No ejecutándose")"
    echo "   ✅ Producción: $(check_container "hospital-backend-prod" && echo "Ejecutándose" || echo "No ejecutándose")"
    echo "   ✅ Desarrollo: $(check_container "hospital-backend-dev" && echo "Ejecutándose" || echo "No ejecutándose")"
    echo "   ✅ QA: $(check_container "hospital-backend-qa" && echo "Ejecutándose" || echo "No ejecutándose")"
    echo ""
    
    echo "💡 Para iniciar todos los entornos:"
    echo "   ./deploy-all-environments.sh"
    echo ""
    echo "💡 Para ver logs de un contenedor específico:"
    echo "   docker logs <nombre-contenedor>"
}

# Ejecutar función principal
main
