#!/bin/bash

# Script para configurar proyectos de desarrollo en SonarQube
# Configuración
SONAR_URL="http://localhost:9000"
SONAR_USER="admin"
SONAR_PASS="Humberto2003!"

echo "🔧 Configurando proyectos de desarrollo en SonarQube..."

# Función para crear proyecto
create_project() {
    local project_key=$1
    local project_name=$2
    
    echo "📝 Creando proyecto: $project_name"
    
    # Crear el proyecto
    curl -u $SONAR_USER:$SONAR_PASS -X POST \
        "$SONAR_URL/api/projects/create" \
        -d "project=$project_key" \
        -d "name=$project_name"
    
    echo ""
}

# Función para verificar si SonarQube está disponible
wait_for_sonarqube() {
    echo "⏳ Esperando a que SonarQube esté disponible..."
    
    max_attempts=30
    attempt=1
    
    while [ $attempt -le $max_attempts ]; do
        if curl -s -u $SONAR_USER:$SONAR_PASS "$SONAR_URL/api/system/status" | grep -q "UP"; then
            echo "✅ SonarQube está disponible"
            return 0
        fi
        
        echo "⏳ Intento $attempt/$max_attempts - SonarQube no está listo aún..."
        sleep 5
        ((attempt++))
    done
    
    echo "❌ Error: SonarQube no está disponible después de $max_attempts intentos"
    exit 1
}

# Esperar a que SonarQube esté disponible
wait_for_sonarqube

# Crear proyectos de desarrollo
create_project "hospital-backend-dev" "Hospital Backend - DEV"
create_project "hospital-frontend-dev" "Hospital Frontend - DEV"

echo "✅ Proyectos de desarrollo configurados en SonarQube"
echo ""
echo "📊 Proyectos creados:"
echo "  - Hospital Backend - DEV (hospital-backend-dev)"
echo "  - Hospital Frontend - DEV (hospital-frontend-dev)"
echo ""
echo "🌐 Accede a SonarQube en: $SONAR_URL"
echo "👤 Usuario: $SONAR_USER"
echo "🔑 Contraseña: $SONAR_PASS"
