#!/bin/bash

# Script para analizar el backend de desarrollo con SonarQube
echo "�� Analizando Backend Hospital - DEV con SonarQube..."

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
# Limpiar y compilar el proyecto
./mvnw clean compile test-compile -Dmaven.repo.local=/tmp/maven-repo

echo "🧪 Ejecutando tests..."
# Ejecutar tests
./mvnw test -Dmaven.repo.local=/tmp/maven-repo

echo "📈 Generando reporte de cobertura JaCoCo..."
# Generar reporte de cobertura
./mvnw jacoco:report -Dmaven.repo.local=/tmp/maven-repo

echo "�� Verificando reporte de cobertura..."
# Verificar que el reporte se generó
if [ -f "target/site/jacoco/jacoco.xml" ]; then
    echo "✅ Reporte JaCoCo generado correctamente"
else
    echo "❌ Error: No se generó el reporte JaCoCo"
    exit 1
fi

echo "📊 Ejecutando análisis SonarQube..."
# Ejecutar análisis SonarQube con nombre correcto
./mvnw sonar:sonar -Dsonar.projectKey=sonar-backend-dev-drone -Dsonar.projectSettingsFile=../$SONAR_CONFIG -Dmaven.repo.local=/tmp/maven-repo

# Regresar al directorio raíz
cd ..

echo "✅ Análisis de Backend DEV completado"
echo "�� Ver resultados en: http://104.197.237.11:9003/dashboard?id=sonar-backend-dev-drone"
