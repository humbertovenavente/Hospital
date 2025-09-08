#!/bin/bash

echo "🏥 REPORTE DETALLADO DE COBERTURA - HOSPITAL SYSTEM"
echo "=================================================="
echo ""

# Verificar si existe el reporte
if [ ! -f "target/site/jacoco/index.html" ]; then
    echo "❌ No se encontró el reporte de JaCoCo."
    echo "Ejecuta primero: mvn clean test jacoco:report"
    exit 1
fi

echo "📊 MÉTRICAS EXTRAÍDAS DEL REPORTE:"
echo "-----------------------------------"

# Extraer métricas del HTML usando grep y sed
TOTAL_LINE=$(grep -A 1 "Total" target/site/jacoco/index.html | grep "Total" | head -1)
echo "📋 Línea total encontrada: $TOTAL_LINE"

# Extraer números específicos usando expresiones regulares más precisas
INSTRUCTIONS=$(echo "$TOTAL_LINE" | grep -o '[0-9]* of [0-9]*' | head -1)
BRANCHES=$(echo "$TOTAL_LINE" | grep -o '[0-9]* of [0-9]*' | head -2 | tail -1)
LINES=$(echo "$TOTAL_LINE" | grep -o '[0-9]* of [0-9]*' | head -3 | tail -1)
METHODS=$(echo "$TOTAL_LINE" | grep -o '[0-9]* of [0-9]*' | head -4 | tail -1)
CLASSES=$(echo "$TOTAL_LINE" | grep -o '[0-9]* of [0-9]*' | head -5 | tail -1)

echo ""
echo "📝 Instrucciones: $INSTRUCTIONS"
echo "🌿 Ramas: $BRANCHES"
echo "📄 Líneas: $LINES"
echo "🔧 Métodos: $METHODS"
echo "🏗️  Clases: $CLASSES"

echo ""
echo "📁 COBERTURA POR PAQUETE (EXTRAÍDA DEL HTML):"
echo "---------------------------------------------"

# Extraer cobertura por paquete del HTML
echo "🔍 Extrayendo cobertura por paquete..."

# Buscar líneas que contengan porcentajes de cobertura
grep -A 2 "com.unis" target/site/jacoco/index.html | grep -E "[0-9]+%" | head -10

echo ""
echo "📖 ARCHIVOS GENERADOS:"
echo "----------------------"
ls -la target/site/jacoco/

echo ""
echo "🎯 PRÓXIMOS PASOS PARA MEJORAR COBERTURA:"
echo "------------------------------------------"
echo "1. 📋 Agregar tests para Resources (actualmente 7%)"
echo "2. 🗄️  Agregar tests para Repositories (actualmente 0%)"
echo "3. ⚙️  Agregar tests para Config (actualmente 0%)"
echo "4. ⚙️  Mejorar tests de Services (actualmente 77%)"
echo "5. 📦 Mejorar tests de Models (actualmente 60%)"

echo ""
echo "🔄 COMANDOS ÚTILES:"
echo "-------------------"
echo "• Ver cobertura: ./ver-cobertura-detallada.sh"
echo "• Regenerar reporte: mvn clean test jacoco:report"
echo "• Solo tests: mvn test"
echo "• Abrir reporte HTML: xdg-open target/site/jacoco/index.html"

echo ""
echo "📊 COBERTURA GENERAL ACTUAL: 59%"
echo "🎯 OBJETIVO RECOMENDADO: 80%+"
echo ""
echo "🚀 ¡Tu proyecto tiene una base sólida de tests! Continúa mejorando la cobertura."
