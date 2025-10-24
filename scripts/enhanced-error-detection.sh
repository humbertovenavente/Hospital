#!/bin/bash

# Script mejorado para detección de errores en Drone CI
# Este script analiza logs y extrae información específica sobre fallos

set -e

# Función para analizar errores de compilación
analyze_build_errors() {
    local log_file="$1"
    local error_type=""
    local error_details=""
    
    if [ -f "$log_file" ]; then
        # Detectar errores de Maven/Java
        if grep -q "BUILD FAILURE\|COMPILATION ERROR\|mvn.*failed" "$log_file"; then
            error_type="Maven Build Failure"
            error_details=$(grep -A 5 -B 5 "BUILD FAILURE\|COMPILATION ERROR" "$log_file" | tail -10)
        fi
        
        # Detectar errores de Node.js/npm
        if grep -q "npm.*failed\|node.*error\|webpack.*error" "$log_file"; then
            error_type="Node.js Build Failure"
            error_details=$(grep -A 5 -B 5 "npm.*failed\|node.*error\|webpack.*error" "$log_file" | tail -10)
        fi
        
        # Detectar errores de Docker
        if grep -q "docker.*failed\|Dockerfile.*error" "$log_file"; then
            error_type="Docker Build Failure"
            error_details=$(grep -A 5 -B 5 "docker.*failed\|Dockerfile.*error" "$log_file" | tail -10)
        fi
        
        # Detectar errores de tests
        if grep -q "test.*failed\|TEST.*FAILED\|AssertionError" "$log_file"; then
            error_type="Test Failure"
            error_details=$(grep -A 3 -B 3 "test.*failed\|TEST.*FAILED\|AssertionError" "$log_file" | tail -10)
        fi
        
        # Detectar errores de SonarQube
        if grep -q "sonar.*failed\|SonarQube.*error" "$log_file"; then
            error_type="SonarQube Analysis Failure"
            error_details=$(grep -A 5 -B 5 "sonar.*failed\|SonarQube.*error" "$log_file" | tail -10)
        fi
    fi
    
    echo "ERROR_TYPE:$error_type"
    echo "ERROR_DETAILS:$error_details"
}

# Función para extraer archivos específicos que fallaron
extract_failed_files() {
    local log_file="$1"
    local failed_files=""
    
    if [ -f "$log_file" ]; then
        # Extraer archivos Java que fallaron
        failed_files=$(grep -o "src/main/java/[^:]*\.java" "$log_file" | head -5 | tr '\n' ', ')
        
        # Extraer archivos JavaScript/TypeScript que fallaron
        if [ -z "$failed_files" ]; then
            failed_files=$(grep -o "src/[^:]*\.\(js\|ts\|vue\)" "$log_file" | head -5 | tr '\n' ', ')
        fi
        
        # Extraer archivos de test que fallaron
        if [ -z "$failed_files" ]; then
            failed_files=$(grep -o "src/test/[^:]*\.java" "$log_file" | head -5 | tr '\n' ', ')
        fi
    fi
    
    echo "$failed_files"
}

# Función para obtener métricas de SonarQube si están disponibles
get_sonar_metrics() {
    local sonar_url="http://34.10.223.20:9000"
    local project_key="humbertovenavente_Hospital"
    
    # Intentar obtener métricas básicas (esto requeriría autenticación en un caso real)
    echo "SonarQube URL: $sonar_url"
    echo "Project Key: $project_key"
}

# Función para generar resumen de errores
generate_error_summary() {
    local log_file="$1"
    local build_number="$2"
    local commit_sha="$3"
    
    echo "=== RESUMEN DE ERRORES PARA BUILD #$build_number ==="
    echo "Commit: $commit_sha"
    echo "Timestamp: $(date)"
    echo ""
    
    # Analizar errores
    local error_analysis=$(analyze_build_errors "$log_file")
    local error_type=$(echo "$error_analysis" | grep "ERROR_TYPE:" | cut -d: -f2-)
    local error_details=$(echo "$error_analysis" | grep "ERROR_DETAILS:" | cut -d: -f2-)
    
    echo "Tipo de Error: $error_type"
    echo ""
    echo "Detalles del Error:"
    echo "$error_details"
    echo ""
    
    # Extraer archivos fallidos
    local failed_files=$(extract_failed_files "$log_file")
    if [ -n "$failed_files" ]; then
        echo "Archivos que Fallaron:"
        echo "$failed_files"
        echo ""
    fi
    
    # Sugerencias de solución
    echo "=== SUGERENCIAS DE SOLUCIÓN ==="
    case "$error_type" in
        "Maven Build Failure")
            echo "1. Verificar dependencias en pom.xml"
            echo "2. Revisar errores de compilación Java"
            echo "3. Ejecutar 'mvn clean compile' localmente"
            ;;
        "Node.js Build Failure")
            echo "1. Verificar package.json y dependencias"
            echo "2. Ejecutar 'npm ci' localmente"
            echo "3. Revisar errores de TypeScript/JavaScript"
            ;;
        "Docker Build Failure")
            echo "1. Verificar Dockerfile"
            echo "2. Revisar contexto de build"
            echo "3. Verificar imágenes base"
            ;;
        "Test Failure")
            echo "1. Revisar tests unitarios específicos"
            echo "2. Verificar datos de prueba"
            echo "3. Ejecutar tests localmente"
            ;;
        "SonarQube Analysis Failure")
            echo "1. Verificar configuración de SonarQube"
            echo "2. Revisar token de autenticación"
            echo "3. Verificar conectividad con SonarQube"
            ;;
        *)
            echo "1. Revisar logs completos en Drone"
            echo "2. Verificar configuración del pipeline"
            echo "3. Contactar al equipo de desarrollo"
            ;;
    esac
}

# Función principal
main() {
    local log_file="${1:-/drone/src/.drone/logs.txt}"
    local build_number="${2:-$DRONE_BUILD_NUMBER}"
    local commit_sha="${3:-$DRONE_COMMIT_SHA}"
    
    echo "🔍 Analizando errores para Build #$build_number..."
    echo "📁 Archivo de log: $log_file"
    echo ""
    
    generate_error_summary "$log_file" "$build_number" "$commit_sha"
}

# Ejecutar si se llama directamente
if [ "${BASH_SOURCE[0]}" == "${0}" ]; then
    main "$@"
fi
