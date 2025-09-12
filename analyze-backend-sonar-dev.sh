#!/bin/bash

# Script para analizar el backend de desarrollo con SonarQube
echo " Analizando Backend Hospital - DEV con SonarQube..."

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
    echo "Error: No se encuentra el directorio $PROJECT_DIR"
    exit 1
fi

# Verificar que existe el archivo de configuración
if [ ! -f "$SONAR_CONFIG" ]; then
    echo "Error: No se encuentra el archivo de configuración $SONAR_CONFIG"
    exit 1
fi

# Cambiar al directorio del backend
cd $PROJECT_DIR

echo " Compilando proyecto backend..."
# Limpiar y compilar el proyecto
./mvnw clean compile test-compile -Dmaven.repo.local=/tmp/maven-repo

echo " Ejecutando tests..."
# Ejecutar tests
./mvnw test -Dmaven.repo.local=/tmp/maven-repo

echo " Generando reporte de cobertura JaCoCo..."
# Generar reporte de cobertura
./mvnw jacoco:report -Dmaven.repo.local=/tmp/maven-repo

echo " Verificando reporte de cobertura..."
# Verificar que el reporte se generó
if [ -f "target/site/jacoco/jacoco.xml" ]; then
    echo " Reporte JaCoCo generado correctamente"
else
    echo " Error: No se generó el reporte JaCoCo"
    exit 1
fi

echo " Ejecutando análisis SonarQube..."
# Ejecutar análisis SonarQube con nombre correcto
./mvnw sonar:sonar -Dsonar.projectKey=backend-hospital-dev-drone -Dsonar.projectSettingsFile=../$SONAR_CONFIG -Dmaven.repo.local=/tmp/maven-repo

# Verificar el resultado del análisis SonarQube
SONAR_EXIT_CODE=$?

# Regresar al directorio raíz
cd ..

if [ $SONAR_EXIT_CODE -ne 0 ]; then
    echo " ERROR: El análisis de SonarQube falló con código de salida $SONAR_EXIT_CODE"
    echo " El pipeline de Drone fallará debido a problemas de calidad detectados por SonarQube"
    exit $SONAR_EXIT_CODE
fi

# Verificar el Quality Gate usando la API de SonarQube
echo " Verificando Quality Gate..."
SONAR_HOST="http://34.46.73.44:9003"
SONAR_TOKEN="sqa_9e95b3a3e0b243715a9b438fb7a08c1060e7123a"
PROJECT_KEY="backend-hospital-dev-drone"

# Esperar un momento para que el análisis se complete
sleep 10

# Obtener el estado del Quality Gate
QUALITY_GATE_STATUS=$(curl -s -u "$SONAR_TOKEN:" "$SONAR_HOST/api/qualitygates/project_status?projectKey=$PROJECT_KEY" | grep -o '"status":"[^"]*"' | cut -d'"' -f4)

if [ "$QUALITY_GATE_STATUS" = "ERROR" ]; then
    echo " ERROR: Quality Gate falló - Se detectaron problemas de calidad críticos"
    echo " El pipeline de Drone fallará debido a problemas de calidad"
    exit 1
elif [ "$QUALITY_GATE_STATUS" = "WARN" ]; then
    echo " ADVERTENCIA: Quality Gate tiene advertencias - Se detectaron problemas de calidad menores"
    echo " El pipeline de Drone fallará debido a advertencias de calidad"
    exit 1
elif [ "$QUALITY_GATE_STATUS" = "OK" ]; then
    echo " ÉXITO: Quality Gate pasó correctamente"
else
    echo " ADVERTENCIA: No se pudo verificar el Quality Gate (estado: $QUALITY_GATE_STATUS)"
    echo " El pipeline continuará pero se recomienda verificar manualmente"
fi

echo "Análisis de Backend DEV completado"
echo " Ver resultados en: http://34.46.73.44:9003/dashboard?id=backend-hospital-dev-drone"
