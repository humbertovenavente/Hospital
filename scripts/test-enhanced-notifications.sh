#!/bin/bash

# Script de prueba para demostrar las notificaciones mejoradas de Drone CI
# Simula diferentes tipos de fallos y genera notificaciones detalladas

set -e

echo "DEMOSTRACIÓN DE NOTIFICACIONES MEJORADAS DE DRONE CI"
echo "========================================================"
echo ""

# Función para simular diferentes tipos de errores
simulate_error() {
    local error_type="$1"
    local environment="$2"
    
    echo "Simulando error tipo: $error_type en entorno: $environment"
    echo ""
    
    # Crear archivo de log simulado
    local log_file="/tmp/drone_test_log.txt"
    
    case "$error_type" in
        "maven")
            cat > "$log_file" << EOF
[INFO] Scanning for projects...
[INFO] Building Hospital Backend
[INFO] Compiling source files...
[ERROR] COMPILATION ERROR in src/main/java/com/unis/controller/UserController.java:45
[ERROR] cannot find symbol: method getUserById()
[ERROR] symbol: method getUserById()
[ERROR] location: class UserService
[ERROR] BUILD FAILURE
[ERROR] Total time: 2.5 s
[ERROR] Failed to execute goal org.apache.maven.plugins:maven-compiler-plugin:3.8.1:compile
EOF
            ;;
        "nodejs")
            cat > "$log_file" << EOF
npm ci
npm ERR! code ERESOLVE
npm ERR! ERESOLVE unable to resolve dependency tree
npm ERR! 
npm ERR! While resolving: hospital-frontend@1.0.0
npm ERR! Found: vue@3.2.0
npm ERR! node_modules/vue
npm ERR!   vue@"^3.2.0" from the root project
npm ERR! 
npm ERR! Could not resolve dependency:
npm ERR! peer vue@"^2.6.0" from vue-router@4.0.0
npm ERR! node_modules/vue-router
npm ERR!   vue-router@"^4.0.0" from the root project
npm ERR! 
npm ERR! Fix the upstream dependency conflict
npm ERR! npm ci failed
EOF
            ;;
        "docker")
            cat > "$log_file" << EOF
Step 1/10 : FROM openjdk:17-alpine
 ---> 1234567890ab
Step 2/10 : WORKDIR /app
 ---> Running in abc123def456
Step 3/10 : COPY pom.xml .
 ---> abc123def456
Step 4/10 : RUN mvn dependency:go-offline
 ---> Running in def456ghi789
[ERROR] Failed to execute goal org.apache.maven.plugins:maven-dependency-plugin:3.1.2:go-offline
[ERROR] Could not resolve dependencies for project com.unis:hospital-backend:jar:1.0.0
[ERROR] The following artifacts could not be resolved: com.oracle:ojdbc8:jar:21.1.0.0
[ERROR] BUILD FAILURE
[ERROR] The command '/bin/sh -c mvn dependency:go-offline' returned a non-zero code: 1
EOF
            ;;
        "tests")
            cat > "$log_file" << EOF
[INFO] Running com.unis.service.UserServiceTest
[ERROR] Tests run: 5, Failures: 2, Errors: 0, Skipped: 0
[ERROR] 
[ERROR] Failures:
[ERROR] testCreateUser(com.unis.service.UserServiceTest): AssertionError: expected:<true> but was:<false>
[ERROR] testUpdateUser(com.unis.service.UserServiceTest): AssertionError: expected:<"John Doe"> but was:<null>
[ERROR] 
[ERROR] Results:
[ERROR] Failed tests: 
[ERROR]   UserServiceTest.testCreateUser: AssertionError
[ERROR]   UserServiceTest.testUpdateUser: AssertionError
[ERROR] 
[ERROR] BUILD FAILURE
EOF
            ;;
        "sonar")
            cat > "$log_file" << EOF
[INFO] SonarQube analysis starting...
[ERROR] Failed to execute goal org.sonarsource.scanner.maven:sonar-maven-plugin:3.9.1.2184:sonar
[ERROR] Unable to connect to SonarQube server
[ERROR] Connection refused: connect
[ERROR] Please check the properties sonar.host.url, sonar.login and sonar.password
[ERROR] BUILD FAILURE
EOF
            ;;
    esac
    
    # Generar notificación usando el script mejorado
    echo "Generando notificación mejorada..."
    echo ""
    
    # Simular variables de entorno de Drone
    export DRONE_BUILD_NUMBER="123"
    export DRONE_COMMIT_SHA="abc123def456789"
    export DRONE_COMMIT_MESSAGE="Fix user authentication issue"
    export DRONE_COMMIT_AUTHOR="jflores@unis.edu.gt"
    export DRONE_COMMIT_BRANCH="$environment"
    
    # Usar el script de generación de notificaciones
    /home/jose/Hospital-2/scripts/enhanced-notification-generator.sh "$environment" > "/tmp/notification_${error_type}_${environment}.txt"
    
    echo "Notificación generada: /tmp/notification_${error_type}_${environment}.txt"
    echo ""
    echo "PREVIEW DE LA NOTIFICACIÓN:"
    echo "================================"
    head -30 "/tmp/notification_${error_type}_${environment}.txt"
    echo "..."
    echo "================================"
    echo ""
}

# Función para mostrar comparación antes/después
show_comparison() {
    echo "COMPARACIÓN: ANTES vs DESPUÉS"
    echo "================================"
    echo ""
    
    echo "NOTIFICACIÓN ANTERIOR (Simple):"
    echo "-----------------------------------"
    cat << EOF
Subject: Pipeline Failure - Hospital DEV

Pipeline failed in branch: dev
Build Number: 123
Commit: abc123def456789
Time: $(date)

Please check the Drone CI dashboard for details.
EOF
    echo ""
    echo ""
    
    echo "NOTIFICACIÓN NUEVA (Detallada):"
    echo "----------------------------------"
    head -50 "/tmp/notification_maven_dev.txt"
    echo "..."
    echo ""
}

# Función principal
main() {
    echo "Iniciando demostración de notificaciones mejoradas..."
    echo ""
    
    # Simular diferentes tipos de errores
    simulate_error "maven" "dev"
    simulate_error "nodejs" "qa"
    simulate_error "docker" "prod"
    simulate_error "tests" "dev"
    simulate_error "sonar" "qa"
    
    # Mostrar comparación
    show_comparison
    
    echo "RESUMEN DE MEJORAS IMPLEMENTADAS:"
    echo "===================================="
    echo ""
    echo "Información detallada del commit y autor"
    echo "Análisis específico del tipo de error"
    echo "Identificación de archivos afectados"
    echo "Sugerencias específicas de solución"
    echo "Enlaces directos a recursos útiles"
    echo "Niveles de criticidad por entorno"
    echo "Formato visual mejorado sin emojis"
    echo "Acciones inmediatas requeridas"
    echo "Información de escalación para PROD"
    echo ""
    
    echo "Archivos generados:"
    ls -la /tmp/notification_*.txt
    echo ""
    
    echo "Para usar en producción:"
    echo "1. Los scripts están en /home/jose/Hospital-2/scripts/"
    echo "2. Se integran automáticamente en .drone.yml"
    echo "3. Proporcionan análisis detallado de errores"
    echo "4. Generan notificaciones contextuales"
    echo ""
    
    echo "Demostración completada exitosamente!"
}

# Ejecutar si se llama directamente
if [ "${BASH_SOURCE[0]}" == "${0}" ]; then
    main "$@"
fi
