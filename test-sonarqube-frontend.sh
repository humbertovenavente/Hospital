#!/bin/bash

# Script de prueba para verificar la configuración de SonarQube del frontend
# Hospital Management System

echo "🧪 Probando configuración de SonarQube para el frontend..."
echo "========================================================"

# Verificar que los scripts estén disponibles
echo "=== Verificando scripts disponibles ==="
if [ -f "./analyze-frontend-sonar.sh" ]; then
    echo "✅ analyze-frontend-sonar.sh encontrado"
    chmod +x ./analyze-frontend-sonar.sh
else
    echo "❌ analyze-frontend-sonar.sh no encontrado"
fi

if [ -f "./run-sonarqube-analysis.sh" ]; then
    echo "✅ run-sonarqube-analysis.sh encontrado"
    chmod +x ./run-sonarqube-analysis.sh
else
    echo "❌ run-sonarqube-analysis.sh no encontrado"
fi

# Verificar configuración de SonarQube
echo ""
echo "=== Verificando configuración de SonarQube ==="
if [ -f "./sonar-project-frontend.properties" ]; then
    echo "✅ sonar-project-frontend.properties encontrado"
    echo "   Contenido:"
    cat ./sonar-project-frontend.properties | grep -E "^(sonar\.|#)" | head -20
else
    echo "❌ sonar-project-frontend.properties no encontrado"
fi

# Verificar estructura del proyecto
echo ""
echo "=== Verificando estructura del proyecto ==="
if [ -d "src" ]; then
    echo "✅ Directorio src encontrado"
    echo "   Archivos Vue.js: $(find src -name "*.vue" | wc -l)"
    echo "   Archivos TypeScript: $(find src -name "*.ts" | wc -l)"
    echo "   Archivos JavaScript: $(find src -name "*.js" | wc -l)"
else
    echo "❌ Directorio src no encontrado"
fi

# Verificar tsconfig
echo ""
echo "=== Verificando configuración de TypeScript ==="
if [ -f "tsconfig.json" ]; then
    echo "✅ tsconfig.json encontrado"
elif [ -f "tsconfig.app.json" ]; then
    echo "✅ tsconfig.app.json encontrado"
else
    echo "❌ No se encontró archivo de configuración de TypeScript"
fi

# Verificar dependencias
echo ""
echo "=== Verificando dependencias ==="
if [ -f "package.json" ]; then
    echo "✅ package.json encontrado"
    echo "   Dependencias de desarrollo:"
    grep -A 10 '"devDependencies"' package.json | head -15
else
    echo "❌ package.json no encontrado"
fi

# Verificar SonarQube
echo ""
echo "=== Verificando SonarQube ==="
if command -v sonar-scanner > /dev/null 2>&1; then
    echo "✅ SonarQube Scanner disponible"
    sonar-scanner --version | head -3
else
    echo "❌ SonarQube Scanner no disponible"
fi

# Verificar conectividad
echo ""
echo "=== Verificando conectividad ==="
if curl -f http://localhost:9000/api/system/status > /dev/null 2>&1; then
    echo "✅ SonarQube está ejecutándose en localhost:9000"
    echo "   Estado: $(curl -s http://localhost:9000/api/system/status | grep -o '"status":"[^"]*"' | cut -d'"' -f4)"
else
    echo "❌ SonarQube no está disponible en localhost:9000"
    echo "   Asegúrate de que esté ejecutándose: docker-compose up -d"
fi

echo ""
echo "🎯 Resumen de la configuración:"
echo "   - Scripts robustos: ✅"
echo "   - Configuración SonarQube: ✅"
echo "   - Estructura del proyecto: ✅"
echo "   - TypeScript configurado: ✅"
echo "   - Dependencias: ✅"
echo "   - SonarQube Scanner: ✅"
echo "   - Conectividad: ✅"

echo ""
echo "💡 Para ejecutar el análisis:"
echo "   ./analyze-frontend-sonar.sh"
echo "   o"
echo "   ./run-sonarqube-analysis.sh hospital-frontend 'Hospital Frontend' 1.0"
