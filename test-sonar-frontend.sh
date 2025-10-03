#!/bin/bash

echo "🧪 Probando configuración de SonarQube para Frontend..."

# Verificar que Node.js esté instalado
if ! command -v node &> /dev/null; then
    echo "❌ Node.js no está instalado"
    exit 1
fi

echo "✅ Node.js version: $(node --version)"

# Verificar que sonar-scanner esté instalado
if ! command -v sonar-scanner &> /dev/null; then
    echo "❌ sonar-scanner no está instalado"
    exit 1
fi

echo "✅ SonarQube Scanner version: $(sonar-scanner --version | head -1)"

# Verificar que el archivo de cobertura existe
if [ ! -f "coverage/lcov.info" ]; then
    echo "❌ Archivo de cobertura no encontrado. Ejecutando tests..."
    npm run test:unit -- --coverage
fi

if [ -f "coverage/lcov.info" ]; then
    echo "✅ Archivo de cobertura encontrado"
    echo "📊 Líneas en lcov.info: $(wc -l < coverage/lcov.info)"
else
    echo "❌ No se pudo generar el archivo de cobertura"
    exit 1
fi

# Probar comando de SonarQube (solo validación, sin enviar)
echo "🔍 Validando configuración de SonarQube..."
npx sonar-scanner -Dsonar.projectKey=frontend-hospital-dev-drone \
  -Dsonar.host.url=http://34.61.228.49:9003 \
  -Dsonar.login=sqa_9e95b3a3e0b243715a9b438fb7a08c1060e7123a \
  -Dsonar.sources=src \
  -Dsonar.tests=src/components/__tests__ \
  -Dsonar.test.inclusions=**/*.spec.ts,**/*.test.ts \
  -Dsonar.typescript.lcov.reportPaths=coverage/lcov.info \
  -Dsonar.javascript.lcov.reportPaths=coverage/lcov.info \
  -Dsonar.qualitygate.wait=false \
  -Dsonar.scanner.dumpToFile=sonar-scanner-debug.txt

if [ $? -eq 0 ]; then
    echo "✅ Configuración de SonarQube válida"
    echo "📄 Archivo de debug generado: sonar-scanner-debug.txt"
else
    echo "❌ Error en la configuración de SonarQube"
    exit 1
fi

echo "🎉 Prueba completada exitosamente"
