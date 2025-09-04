#!/bin/bash

# Script alternativo para analizar el backend de desarrollo con SonarQube usando Maven directamente
echo "🔍 Analizando Backend Hospital - DEV con SonarQube (Maven directo)..."

# Configuración
PROJECT_DIR="backend"
SONAR_CONFIG="sonar-project-backend-dev.properties"

# Configurar variables de entorno para Maven
export MAVEN_OPTS="-Dmaven.repo.local=/tmp/maven-repo"
export M2_HOME="/tmp/maven-home"
export MAVEN_HOME="/tmp/maven-home"

# Crear directorios necesarios con permisos
mkdir -p /tmp/maven-repo
mkdir -p /tmp/maven-home
mkdir -p /home/sonarqube 2>/dev/null || true

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
# Limpiar y compilar el proyecto usando Maven directamente
mvn clean compile test-compile -Dmaven.repo.local=/tmp/maven-repo

echo "🧪 Ejecutando tests..."
# Ejecutar tests con cobertura
mvn test jacoco:report -Dmaven.repo.local=/tmp/maven-repo

echo "📊 Ejecutando análisis SonarQube..."
# Ejecutar análisis SonarQube
mvn sonar:sonar -Dsonar.projectSettingsFile=../$SONAR_CONFIG -Dmaven.repo.local=/tmp/maven-repo

# Regresar al directorio raíz
cd ..

echo "✅ Análisis de Backend DEV completado"
echo "🌐 Ver resultados en: http://localhost:9000/dashboard?id=hospital-backend-dev"

