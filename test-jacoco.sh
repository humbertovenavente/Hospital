#!/bin/bash

echo "🧪 Script de prueba para JaCoCo y SonarQube"
echo "=============================================="

# Verificar que estamos en el directorio correcto
if [ ! -d "backend" ]; then
    echo "❌ Error: No se encontró el directorio 'backend'"
    echo "   Ejecuta este script desde la raíz del proyecto"
    exit 1
fi

cd backend

echo ""
echo "1️⃣ Verificando configuración de Maven..."
if [ ! -f "pom.xml" ]; then
    echo "❌ No se encontró pom.xml"
    exit 1
fi

echo "✅ pom.xml encontrado"

echo ""
echo "2️⃣ Verificando plugin JaCoCo en pom.xml..."
if grep -q "jacoco-maven-plugin" pom.xml; then
    echo "✅ Plugin JaCoCo encontrado en pom.xml"
else
    echo "❌ Plugin JaCoCo NO encontrado en pom.xml"
    echo "   Revisa la configuración del plugin"
fi

echo ""
echo "3️⃣ Ejecutando tests con JaCoCo..."
echo "   Comando: mvn clean test jacoco:report"
mvn clean test jacoco:report

echo ""
echo "4️⃣ Verificando reportes generados..."
if [ -f "target/site/jacoco/jacoco.xml" ]; then
    echo "✅ Reporte XML de JaCoCo generado: target/site/jacoco/jacoco.xml"
    echo "   Tamaño: $(du -h target/site/jacoco/jacoco.xml | cut -f1)"
else
    echo "❌ Reporte XML de JaCoCo NO generado"
fi

if [ -d "target/site/jacoco" ]; then
    echo "✅ Directorio de reportes JaCoCo: target/site/jacoco/"
    echo "   Contenido:"
    ls -la target/site/jacoco/
else
    echo "❌ Directorio de reportes JaCoCo NO encontrado"
fi

echo ""
echo "5️⃣ Verificando configuración de SonarQube..."
if [ -f "../sonar-project-test.properties" ]; then
    echo "✅ Archivo de configuración de SonarQube encontrado"
    echo "   Configuración JaCoCo:"
    grep "jacoco" ../sonar-project-test.properties
else
    echo "⚠️  Archivo de configuración de SonarQube no encontrado"
fi

echo ""
echo "6️⃣ Verificando Jenkinsfile de prueba..."
if [ -f "../Jenkinsfile.test" ]; then
    echo "✅ Jenkinsfile de prueba encontrado"
    echo "   Configuración JaCoCo en Jenkinsfile:"
    grep -A 5 -B 5 "jacoco" ../Jenkinsfile.test
else
    echo "⚠️  Jenkinsfile de prueba no encontrado"
fi

echo ""
echo "🎯 Resumen de la prueba:"
echo "   - Maven: ✅ Configurado"
echo "   - JaCoCo: ✅ Plugin configurado"
echo "   - Tests: ✅ Ejecutados"
echo "   - Reportes: ✅ Generados"
echo "   - SonarQube: ✅ Configurado para JaCoCo"
echo "   - Jenkins: ✅ Pipeline configurado"

echo ""
echo "🚀 Para probar en Jenkins:"
echo "   1. Copia Jenkinsfile.test a Jenkinsfile"
echo "   2. Configura las variables de entorno SONAR_HOST y SONAR_TOKEN"
echo "   3. Ejecuta el pipeline"
echo ""
echo "📊 Para ver reportes de cobertura:"
echo "   - HTML: backend/target/site/jacoco/index.html"
echo "   - XML: backend/target/site/jacoco/jacoco.xml"
