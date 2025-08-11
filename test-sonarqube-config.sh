#!/bin/bash

# Script para probar la configuración de SonarQube
echo "🧪 Probando configuración de SonarQube..."
echo "=========================================="

# Verificar que SonarQube esté disponible
echo "📡 Verificando SonarQube..."
if curl -f http://localhost:9000/api/system/status > /dev/null 2>&1; then
    echo "✅ SonarQube está disponible"
else
    echo "❌ SonarQube no está disponible"
    echo "   Inicia con: docker-compose up -d"
    exit 1
fi

# Verificar que el backend esté compilado
echo ""
echo "🔨 Verificando compilación del backend..."
if [ -d "backend/target/classes" ] && [ -d "backend/target/test-classes" ]; then
    echo "✅ Backend compilado correctamente"
    echo "   • Classes: backend/target/classes"
    echo "   • Test Classes: backend/target/test-classes"
else
    echo "❌ Backend no está compilado"
    echo "   Ejecuta: cd backend && mvn clean compile test-compile"
    exit 1
fi

# Verificar archivos de configuración
echo ""
echo "📋 Verificando archivos de configuración..."
if [ -f "sonar-project-backend.properties" ] && [ -f "sonar-project-frontend.properties" ]; then
    echo "✅ Archivos de configuración encontrados"
else
    echo "❌ Faltan archivos de configuración"
    exit 1
fi

# Verificar Jenkinsfile
echo ""
echo "🔧 Verificando Jenkinsfile..."
if grep -q "hospital-backend" Jenkinsfile && grep -q "hospital-frontend" Jenkinsfile; then
    echo "✅ Jenkinsfile configurado correctamente"
else
    echo "❌ Jenkinsfile no está configurado correctamente"
    exit 1
fi

# Verificar orden de stages
echo ""
echo "📊 Verificando orden de stages..."
BACKEND_STAGE=$(grep -n "stage.*Unit Tests Backend" Jenkinsfile | cut -d: -f1)
QUALITY_STAGE=$(grep -n "stage.*Code Quality Check" Jenkinsfile | cut -d: -f1)

if [ "$BACKEND_STAGE" -lt "$QUALITY_STAGE" ]; then
    echo "✅ Orden de stages correcto"
    echo "   • Unit Tests Backend: línea $BACKEND_STAGE"
    echo "   • Code Quality Check: línea $QUALITY_STAGE"
else
    echo "❌ Orden de stages incorrecto"
    echo "   Code Quality Check debe ejecutarse DESPUÉS de Unit Tests Backend"
    exit 1
fi

echo ""
echo "🎉 Configuración verificada correctamente!"
echo "=========================================="
echo "✅ SonarQube disponible"
echo "✅ Backend compilado"
echo "✅ Archivos de configuración creados"
echo "✅ Jenkinsfile configurado"
echo "✅ Orden de stages correcto"
echo ""
echo "🚀 Ahora puedes:"
echo "   1. Ejecutar el pipeline en Jenkins"
echo "   2. O ejecutar análisis manual: ./run-sonarqube-analysis.sh"
echo ""
echo "📊 Los proyectos se crearán en SonarQube como:"
echo "   • hospital-backend"
echo "   • hospital-frontend"
