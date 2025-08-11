#!/bin/bash

echo "🔍 ANÁLISIS DE SONARQUBE PARA RAMA QA"
echo "======================================"

# Verificar que SonarQube esté funcionando
echo "📡 Verificando conexión con SonarQube..."
if ! curl -s http://localhost:9000/api/system/status | grep -q "UP"; then
    echo "❌ SonarQube no está funcionando en http://localhost:9000"
    echo "💡 Inicia SonarQube con: docker-compose -f docker-compose.sonarqube.yml up -d"
    exit 1
fi

echo "✅ SonarQube está funcionando correctamente"

# Limpiar archivos de análisis anteriores
echo "🧹 Limpiando archivos de análisis anteriores..."
rm -rf .scannerwork/
rm -rf coverage/

# Analizar el frontend
echo "🎨 Analizando FRONTEND (Vue.js/TypeScript)..."
if sonar-scanner -Dproject.settings=sonar-project-frontend.properties; then
    echo "✅ Análisis del frontend completado exitosamente"
else
    echo "❌ Error en el análisis del frontend"
    exit 1
fi

# Analizar el backend
echo "🔧 Analizando BACKEND (Java/Quarkus)..."
cd backend
if sonar-scanner -Dproject.settings=../sonar-project-backend.properties; then
    echo "✅ Análisis del backend completado exitosamente"
else
    echo "❌ Error en el análisis del backend"
    exit 1
fi
cd ..

echo ""
echo "🎉 ANÁLISIS COMPLETADO EXITOSAMENTE"
echo "=================================="
echo "📊 Frontend: http://localhost:9000/dashboard?id=hospital-frontend"
echo "📊 Backend: http://localhost:9000/dashboard?id=hospital-backend"
echo "🌿 Análisis completado (Community no soporta ramas específicas)"
echo ""
echo "💡 Para ver los resultados, ve a SonarQube: http://localhost:9000"
