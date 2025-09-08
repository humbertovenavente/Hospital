#!/bin/bash

echo "🏥 RESUMEN DE COBERTURA - HOSPITAL SYSTEM"
echo "========================================="
echo ""

# Verificar si existe el reporte
if [ ! -f "target/site/jacoco/index.html" ]; then
    echo "❌ No se encontró el reporte de JaCoCo."
    echo "Ejecuta primero: mvn clean test jacoco:report"
    exit 1
fi

echo "📊 COBERTURA GENERAL:"
echo "--------------------"

# Extraer la línea del total del reporte
TOTAL_INFO=$(grep -A 1 -B 1 "Total" target/site/jacoco/index.html | grep "Total" | head -1)

# Extraer métricas usando grep y sed
INSTRUCTIONS=$(echo "$TOTAL_INFO" | grep -o '[0-9,]* of [0-9,]*' | head -1)
BRANCHES=$(echo "$TOTAL_INFO" | grep -o '[0-9,]* of [0-9,]*' | head -2 | tail -1)
LINES=$(echo "$TOTAL_INFO" | grep -o '[0-9,]* of [0-9,]*' | head -3 | tail -1)
METHODS=$(echo "$TOTAL_INFO" | grep -o '[0-9,]* of [0-9,]*' | head -4 | tail -1)
CLASSES=$(echo "$TOTAL_INFO" | grep -o '[0-9,]* of [0-9,]*' | head -5 | tail -1)

# Extraer porcentajes
INSTRUCTIONS_PCT=$(echo "$TOTAL_INFO" | grep -o '[0-9]*%' | head -1)
BRANCHES_PCT=$(echo "$TOTAL_INFO" | grep -o '[0-9]*%' | head -2 | tail -1)
LINES_PCT=$(echo "$TOTAL_INFO" | grep -o '[0-9]*%' | head -3 | tail -1)
METHODS_PCT=$(echo "$TOTAL_INFO" | grep -o '[0-9]*%' | head -4 | tail -1)
CLASSES_PCT=$(echo "$TOTAL_INFO" | grep -o '[0-9]*%' | head -5 | tail -1)

echo "📝 Instrucciones: $INSTRUCTIONS ($INSTRUCTIONS_PCT)"
echo "🌿 Ramas: $BRANCHES ($BRANCHES_PCT)"
echo "📄 Líneas: $LINES ($LINES_PCT)"
echo "🔧 Métodos: $METHODS ($METHODS_PCT)"
echo "🏗️  Clases: $CLASSES ($CLASSES_PCT)"

echo ""
echo "📁 COBERTURA POR PAQUETE:"
echo "-------------------------"

# Extraer porcentajes de cobertura por paquete usando sed para parsear la tabla
DTOS_COV=$(sed -n '/com.unis.dto/,+1p' target/site/jacoco/index.html | grep -o '[0-9]*%' | head -1)
CONTROLLERS_COV=$(sed -n '/com.unis.controller/,+1p' target/site/jacoco/index.html | grep -o '[0-9]*%' | head -1)
SERVICES_COV=$(sed -n '/com.unis.service/,+1p' target/site/jacoco/index.html | grep -o '[0-9]*%' | head -1)
MODELS_COV=$(sed -n '/com.unis.model/,+1p' target/site/jacoco/index.html | grep -o '[0-9]*%' | head -1)
RESOURCES_COV=$(sed -n '/com.unis.resource/,+1p' target/site/jacoco/index.html | grep -o '[0-9]*%' | head -1)
REPOSITORIES_COV=$(sed -n '/com.unis.repository/,+1p' target/site/jacoco/index.html | grep -o '[0-9]*%' | head -1)
CONFIG_COV=$(sed -n '/com.unis.config/,+1p' target/site/jacoco/index.html | grep -o '[0-9]*%' | head -1)

# Función para evaluar el estado de la cobertura
evaluate_coverage() {
    local cov=$1
    if [ -z "$cov" ]; then
        echo "0% (Sin cobertura)"
    else
        # Remover el símbolo % para la comparación
        local cov_num=${cov%\%}
        if [ "$cov_num" -ge 90 ]; then
            echo "$cov (Excelente)"
        elif [ "$cov_num" -ge 80 ]; then
            echo "$cov (Muy buena)"
        elif [ "$cov_num" -ge 70 ]; then
            echo "$cov (Buena)"
        elif [ "$cov_num" -ge 50 ]; then
            echo "$cov (Media)"
        elif [ "$cov_num" -ge 20 ]; then
            echo "$cov (Baja)"
        else
            echo "$cov (Muy baja)"
        fi
    fi
}

echo "📋 DTOs: $(evaluate_coverage $DTOS_COV)"
echo "🎮 Controllers: $(evaluate_coverage $CONTROLLERS_COV)"
echo "⚙️  Services: $(evaluate_coverage $SERVICES_COV)"
echo "📦 Models: $(evaluate_coverage $MODELS_COV)"
echo "🔗 Resources: $(evaluate_coverage $RESOURCES_COV)"
echo "🗄️  Repositories: $(evaluate_coverage $REPOSITORIES_COV)"
echo "⚙️  Config: $(evaluate_coverage $CONFIG_COV)"

echo ""
echo "🎯 RESUMEN:"
echo "----------"
echo "✅ COBERTURA GENERAL: $INSTRUCTIONS_PCT"
echo "🎯 OBJETIVO: 85%+"
echo "📈 ESTADO: Base sólida, necesita mejora"

echo ""
echo "🔄 COMANDOS:"
echo "------------"
echo "• Regenerar: mvn clean test jacoco:report"
echo "• Solo tests: mvn test"
echo "• Ver HTML: xdg-open target/site/jacoco/index.html"
echo "• Ver CSV: cat target/site/jacoco/jacoco.csv"
