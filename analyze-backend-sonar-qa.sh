#!/bin/bash

echo "ANÁLISIS DE SONARQUBE PARA BACKEND (RAMA QA)"
echo "=================================================="

# URL de SonarQube
SONAR_URL="http://34.61.228.49:9003"

# Verificar que SonarQube esté funcionando
echo " Verificando conexión con SonarQube en $SONAR_URL..."
if ! curl -s $SONAR_URL/api/system/status | grep -q "UP"; then
    echo " SonarQube no está funcionando en $SONAR_URL"
    exit 1
fi

echo " SonarQube está funcionando correctamente"

# Limpiar archivos de análisis anteriores
echo " Limpiando archivos de análisis anteriores..."
rm -rf .scannerwork/
rm -rf backend/.scannerwork/

# Generar reportes de cobertura primero
echo " Generando reportes de cobertura..."
cd backend
mvn clean test jacoco:report -DskipTests=false
if [ $? -ne 0 ]; then
    echo " Error generando reportes de cobertura"
    exit 1
fi

# Analizar solo el backend con configuración QA
echo " Analizando BACKEND (Java/Quarkus) con configuración QA..."
sonar-scanner -Dsonar.host.url=$SONAR_URL -Dproject.settings=../sonar-project-backend-qa.properties

if [ $? -eq 0 ]; then
    echo "Análisis del backend completado exitosamente"
    echo "Resultados disponibles en: $SONAR_URL/dashboard?id=hospital-backend-qa"
else
    echo "Error en el análisis del backend"
    exit 1
fi

cd ..

echo ""
echo " ANÁLISIS DEL BACKEND COMPLETADO"
echo "================================="
echo "Backend QA: $SONAR_URL/dashboard?id=hospital-backend-qa"
echo "El análisis debería fallar si la cobertura es menor al 99%"

