#!/bin/bash

# Script para generar notificaciones de error mejoradas en Drone CI
# Integra análisis detallado de errores y contexto específico

set -e

# Función para obtener información detallada del commit
get_commit_info() {
    local commit_sha="$1"
    local repo_url="https://github.com/humbertovenavente/Hospital"
    
    echo "INFORMACIÓN DEL COMMIT:"
    echo "• SHA: $commit_sha"
    echo "• URL: $repo_url/commit/$commit_sha"
    echo "• Autor: $DRONE_COMMIT_AUTHOR"
    echo "• Mensaje: $DRONE_COMMIT_MESSAGE"
    echo "• Rama: $DRONE_COMMIT_BRANCH"
    echo ""
}

# Función para analizar el tipo de fallo específico
analyze_failure_type() {
    local log_content="$1"
    local failure_type=""
    local specific_error=""
    
    # Detectar errores de compilación Java/Maven
    if echo "$log_content" | grep -q "BUILD FAILURE\|COMPILATION ERROR\|mvn.*failed"; then
        failure_type="Error de Compilación Java/Maven"
        specific_error=$(echo "$log_content" | grep -A 5 -B 5 "BUILD FAILURE\|COMPILATION ERROR" | tail -10)
    fi
    
    # Detectar errores de Node.js/npm
    if echo "$log_content" | grep -q "npm.*failed\|node.*error\|webpack.*error"; then
        failure_type="Error de Build Frontend (Node.js)"
        specific_error=$(echo "$log_content" | grep -A 5 -B 5 "npm.*failed\|node.*error\|webpack.*error" | tail -10)
    fi
    
    # Detectar errores de Docker
    if echo "$log_content" | grep -q "docker.*failed\|Dockerfile.*error"; then
        failure_type="Error de Build Docker"
        specific_error=$(echo "$log_content" | grep -A 5 -B 5 "docker.*failed\|Dockerfile.*error" | tail -10)
    fi
    
    # Detectar errores de tests
    if echo "$log_content" | grep -q "test.*failed\|TEST.*FAILED\|AssertionError"; then
        failure_type="Error en Tests Unitarios"
        specific_error=$(echo "$log_content" | grep -A 3 -B 3 "test.*failed\|TEST.*FAILED\|AssertionError" | tail -10)
    fi
    
    # Detectar errores de SonarQube
    if echo "$log_content" | grep -q "sonar.*failed\|SonarQube.*error"; then
        failure_type="Error en Análisis SonarQube"
        specific_error=$(echo "$log_content" | grep -A 5 -B 5 "sonar.*failed\|SonarQube.*error" | tail -10)
    fi
    
    # Detectar errores de deploy
    if echo "$log_content" | grep -q "deploy.*failed\|docker.*run.*failed"; then
        failure_type="Error en Deploy"
        specific_error=$(echo "$log_content" | grep -A 3 -B 3 "deploy.*failed\|docker.*run.*failed" | tail -10)
    fi
    
    # Si no se detecta un tipo específico
    if [ -z "$failure_type" ]; then
        failure_type="Error General del Pipeline"
        specific_error=$(echo "$log_content" | grep -E "(error|Error|ERROR|failed|Failed|FAILED)" | tail -5)
    fi
    
    echo "FAILURE_TYPE:$failure_type"
    echo "SPECIFIC_ERROR:$specific_error"
}

# Función para extraer archivos específicos que fallaron
extract_failed_files() {
    local log_content="$1"
    local failed_files=""
    
    # Extraer archivos Java que fallaron
    local java_files=$(echo "$log_content" | grep -o "src/main/java/[^:]*\.java" | head -3 | tr '\n' ', ')
    if [ -n "$java_files" ]; then
        failed_files="$failed_files Java: $java_files"
    fi
    
    # Extraer archivos JavaScript/TypeScript que fallaron
    local js_files=$(echo "$log_content" | grep -o "src/[^:]*\.\(js\|ts\|vue\)" | head -3 | tr '\n' ', ')
    if [ -n "$js_files" ]; then
        failed_files="$failed_files Frontend: $js_files"
    fi
    
    # Extraer archivos de test que fallaron
    local test_files=$(echo "$log_content" | grep -o "src/test/[^:]*\.java" | head -3 | tr '\n' ', ')
    if [ -n "$test_files" ]; then
        failed_files="$failed_files Tests: $test_files"
    fi
    
    echo "$failed_files"
}

# Función para generar sugerencias específicas de solución
generate_solution_suggestions() {
    local failure_type="$1"
    local environment="$2"
    
    echo "SUGERENCIAS DE SOLUCIÓN:"
    echo ""
    
    case "$failure_type" in
        *"Compilación Java/Maven"*)
            echo "1. Verificar errores de sintaxis en archivos Java"
            echo "2. Revisar dependencias en pom.xml"
            echo "3. Ejecutar 'mvn clean compile' localmente"
            echo "4. Verificar versiones de Java y Maven"
            ;;
        *"Build Frontend"*)
            echo "1. Verificar package.json y dependencias"
            echo "2. Ejecutar 'npm ci' localmente"
            echo "3. Revisar errores de TypeScript/JavaScript"
            echo "4. Verificar configuración de Vite/Webpack"
            ;;
        *"Build Docker"*)
            echo "1. Verificar Dockerfile y contexto"
            echo "2. Revisar imágenes base y dependencias"
            echo "3. Limpiar imágenes Docker locales"
            echo "4. Verificar permisos de Docker"
            ;;
        *"Tests Unitarios"*)
            echo "1. Ejecutar tests específicos que fallaron"
            echo "2. Revisar datos de prueba y mocks"
            echo "3. Verificar cobertura de código"
            echo "4. Corregir lógica de tests"
            ;;
        *"Análisis SonarQube"*)
            echo "1. Verificar token de SonarQube"
            echo "2. Comprobar conectividad con SonarQube"
            echo "3. Revisar configuración sonar-project.properties"
            echo "4. Verificar métricas de calidad"
            ;;
        *"Deploy"*)
            echo "1. Verificar configuración de deploy"
            echo "2. Comprobar contenedores Docker"
            echo "3. Verificar conectividad de red"
            echo "4. Revisar variables de entorno"
            ;;
        *)
            echo "1. Revisar logs completos en Drone"
            echo "2. Contactar al equipo de desarrollo"
            echo "3. Verificar configuración del pipeline"
            echo "4. Revisar métricas de calidad"
            ;;
    esac
    
    echo ""
    echo "ACCIONES INMEDIATAS:"
    echo "1. Revisar el commit específico en GitHub"
    echo "2. Ejecutar el pipeline localmente si es posible"
    echo "3. Corregir los errores identificados"
    echo "4. Reintentar el pipeline después de correcciones"
    
    if [ "$environment" = "prod" ]; then
        echo ""
        echo "CRÍTICO - PRODUCCIÓN:"
        echo "1. Considerar rollback inmediato si es necesario"
        echo "2. Notificar a todo el equipo de desarrollo"
        echo "3. Escalar a dirección si no se resuelve en 30 minutos"
    fi
}

# Función para generar el mensaje de correo mejorado
generate_enhanced_email() {
    local environment="$1"
    local build_number="$2"
    local commit_sha="$3"
    local commit_message="$4"
    local commit_author="$5"
    local build_url="$6"
    
    # Obtener logs del build (simulado - en realidad vendría de Drone)
    local log_content=""
    if [ -f "/drone/src/.drone/logs.txt" ]; then
        log_content=$(cat /drone/src/.drone/logs.txt)
    fi
    
    # Analizar el tipo de fallo
    local failure_analysis=$(analyze_failure_type "$log_content")
    local failure_type=$(echo "$failure_analysis" | grep "FAILURE_TYPE:" | cut -d: -f2-)
    local specific_error=$(echo "$failure_analysis" | grep "SPECIFIC_ERROR:" | cut -d: -f2-)
    
    # Extraer archivos fallidos
    local failed_files=$(extract_failed_files "$log_content")
    
    # Determinar nivel de criticidad
    local criticality=""
    case "$environment" in
        "dev")
            criticality="MEDIO"
            ;;
        "qa")
            criticality="ALTO"
            ;;
        "prod")
            criticality="CRÍTICO"
            ;;
    esac
    
    # Generar mensaje
    cat << EOF

FALLO EN PIPELINE DE ${environment^^}

═══════════════════════════════════════════════════════════════

INFORMACIÓN DEL BUILD:
• Pipeline: Hospital ${environment^^}
• Rama: $DRONE_COMMIT_BRANCH  
• Build #: $build_number
• Commit: $commit_sha
• Autor: $commit_author
• Mensaje: $commit_message
• Fecha/Hora: $(date '+%Y-%m-%d %H:%M:%S')
• URL Build: $build_url
• Criticidad: $criticality

DETALLES DEL FALLO:
• Tipo de Fallo: $failure_type
• Estado: FALLÓ
• Entorno: ${environment^^}

ARCHIVOS AFECTADOS:
• Repositorio: humbertovenavente/Hospital
• Rama: $DRONE_COMMIT_BRANCH
• Último Commit: $commit_sha
$([ -n "$failed_files" ] && echo "• Archivos Específicos:$failed_files")

PASOS DEL PIPELINE:
1. Clonado del repositorio
2. Instalación de dependencias
3. Build Frontend (Node.js)
4. Build Backend (Java/Maven)
5. Tests Unitarios
6. Análisis SonarQube
7. Deploy a entorno ${environment^^}

MÉTRICAS DE CALIDAD:
• Tests Backend: FALLARON
• Tests Frontend: FALLARON  
• Análisis SonarQube: FALLÓ
• Cobertura de código: NO DISPONIBLE
• Deuda técnica: NO DISPONIBLE

ERRORES DETECTADOS:
$specific_error

ENLACES ÚTILES:
• Dashboard Drone: $build_url
• SonarQube ${environment^^}: http://34.10.223.20:9000
• Logs Detallados: $build_url/logs
• GitHub Commit: https://github.com/humbertovenavente/Hospital/commit/$commit_sha

$(generate_solution_suggestions "$failure_type" "$environment")

CONTACTO:
• Lead Developer: jflores@unis.edu.gt
• Product Owner: jnajar@unis.edu.gt

═══════════════════════════════════════════════════════════════

$([ "$environment" = "prod" ] && echo "URGENTE: FALLO EN PRODUCCIÓN - ACCIÓN INMEDIATA REQUERIDA" || echo "Por favor revisar y corregir los problemas identificados.")

Saludos,
Sistema CI/CD Hospital - ${environment^^}
EOF
}

# Función principal
main() {
    local environment="${1:-dev}"
    local build_number="${2:-$DRONE_BUILD_NUMBER}"
    local commit_sha="${3:-$DRONE_COMMIT_SHA}"
    local commit_message="${4:-$DRONE_COMMIT_MESSAGE}"
    local commit_author="${5:-$DRONE_COMMIT_AUTHOR}"
    local build_url="${6:-http://34.10.223.20:8080/humbertovenavente/Hospital/$build_number}"
    
    echo "Generando notificación mejorada para entorno $environment..."
    generate_enhanced_email "$environment" "$build_number" "$commit_sha" "$commit_message" "$commit_author" "$build_url"
}

# Ejecutar si se llama directamente
if [ "${BASH_SOURCE[0]}" == "${0}" ]; then
    main "$@"
fi