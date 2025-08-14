#!/bin/bash

# Script para analizar el backend de desarrollo con SonarQube
echo "🔍 Analizando Backend Hospital - DEV con SonarQube..."

# Configuración
PROJECT_DIR="backend"
SONAR_CONFIG="sonar-project-backend-dev.properties"

# Verificar que existe el directorio del proyecto
if [ ! -d "$PROJECT_DIR" ]; then
    echo "❌ Error: No se encuentra el directorio $PROJECT_DIR"
    exit 1
fi

# Verificar que existe el archivo de configuración
if [ ! -f "$SONAR_CONFIG" ]; then
    echo "❌ Error: No se encuentra el archivo de configuración $SONAR_CONFIG"
    exit 1
fi

# Cambiar al directorio del backend
cd $PROJECT_DIR

echo "📦 Compilando proyecto backend..."
# Limpiar y compilar el proyecto
./mvnw clean compile test-compile

echo "🧪 Ejecutando tests..."
# Ejecutar tests con cobertura
./mvnw test jacoco:report

echo "📊 Ejecutando análisis SonarQube..."
# Ejecutar análisis SonarQube
./mvnw sonar:sonar -Dsonar.projectSettingsFile=../$SONAR_CONFIG

# Regresar al directorio raíz
cd ..

echo "✅ Análisis de Backend DEV completado"
echo "🌐 Ver resultados en: http://localhost:9000/dashboard?id=hospital-backend-dev"
