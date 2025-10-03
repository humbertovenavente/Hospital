#!/bin/bash

echo "ANÁLISIS DE SONARQUBE PARA FRONTEND (RAMA QA)"
echo "=================================================="

# URL de SonarQube
SONAR_URL="http://34.46.73.44:9003"

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
rm -rf coverage/

# Instalar dependencias si es necesario
echo " Verificando dependencias de Node.js..."
if [ ! -d "node_modules" ]; then
    echo " Instalando dependencias..."
    npm install
fi

# Generar reportes de cobertura primero
echo " Generando reportes de cobertura para frontend..."
npx vitest run --coverage
if [ $? -ne 0 ]; then
    echo " Error generando reportes de cobertura"
    exit 1
fi

# Verificar que el reporte de cobertura existe
if [ ! -f "coverage/lcov.info" ]; then
    echo " Error: No se encontró el reporte de cobertura en coverage/lcov.info"
    exit 1
fi

echo " Reporte de cobertura generado exitosamente"

# Analizar el frontend con configuración QA
echo " Analizando FRONTEND (Vue.js/TypeScript) con configuración QA..."
sonar-scanner -Dsonar.host.url=$SONAR_URL -Dproject.settings=sonar-project-frontend-qa.properties

if [ $? -eq 0 ]; then
    echo "Análisis del frontend completado exitosamente"
    echo "Resultados disponibles en: $SONAR_URL/dashboard?id=frontend-hospital-qa-drone"
else
    echo "Error en el análisis del frontend"
    exit 1
fi

echo ""
echo " ANÁLISIS DEL FRONTEND COMPLETADO"
echo "=================================="
echo "Frontend QA: $SONAR_URL/dashboard?id=frontend-hospital-qa-drone"
echo "El análisis debería fallar si la cobertura es menor al 99%"
