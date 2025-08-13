#!/bin/bash

echo "🔧 CONFIGURANDO PROYECTOS DE QA EN SONARQUBE 🏥"
echo "=============================================="

# Configuración
SONAR_HOST="http://localhost:9000"
ADMIN_TOKEN="sqp_1234567890abcdef"  # Cambiar por tu token real de admin
QA_TOKEN="sqp_qa1234567890abcdef"   # Token para QA (crear en SonarQube)

# Verificar que SonarQube esté corriendo
echo "🔍 Verificando que SonarQube esté activo..."
if ! curl -f "${SONAR_HOST}/api/system/status" > /dev/null 2>&1; then
    echo "❌ Error: SonarQube no está accesible en ${SONAR_HOST}"
    echo "   Asegúrate de que esté corriendo: docker start sonarqube"
    exit 1
fi

echo "✅ SonarQube está activo"

# Crear proyecto Backend QA
echo ""
echo "🏗️  Creando proyecto Backend QA..."
curl -X POST "${SONAR_HOST}/api/projects/create" \
  -H "Authorization: Bearer ${ADMIN_TOKEN}" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "project=hospital-backend-qa&name=Hospital Backend - QA (Java/Quarkus)" \
  -s | jq -r '.project.key // "ERROR"' > /tmp/backend_result

if grep -q "ERROR" /tmp/backend_result; then
    echo "⚠️  Proyecto backend QA ya existe o hubo un error"
else
    echo "✅ Proyecto Backend QA creado: hospital-backend-qa"
fi

# Crear proyecto Frontend QA
echo ""
echo "🏗️  Creando proyecto Frontend QA..."
curl -X POST "${SONAR_HOST}/api/projects/create" \
  -H "Authorization: Bearer ${ADMIN_TOKEN}" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "project=hospital-frontend-qa&name=Hospital Frontend - QA (Vue.js/TypeScript)" \
  -s | jq -r '.project.key // "ERROR"' > /tmp/frontend_result

if grep -q "ERROR" /tmp/frontend_result; then
    echo "⚠️  Proyecto frontend QA ya existe o hubo un error"
else
    echo "✅ Proyecto Frontend QA creado: hospital-frontend-qa"
fi

# Configurar Quality Gate para QA (más permisivo que producción)
echo ""
echo "🎯 Configurando Quality Gate para QA..."

# Backend QA - Quality Gate más permisivo
echo "   Configurando Backend QA..."
curl -X POST "${SONAR_HOST}/api/qualitygates/select" \
  -H "Authorization: Bearer ${ADMIN_TOKEN}" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "gateName=Sonar way&projectKey=hospital-backend-qa" \
  -s > /dev/null

# Frontend QA - Quality Gate más permisivo
echo "   Configurando Frontend QA..."
curl -X POST "${SONAR_HOST}/api/qualitygates/select" \
  -H "Authorization: Bearer ${ADMIN_TOKEN}" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "gateName=Sonar way&projectKey=hospital-frontend-qa" \
  -s > /dev/null

echo "✅ Quality Gates configurados para QA"

# Crear token de QA si no existe
echo ""
echo "🔑 Creando token de QA..."
curl -X POST "${SONAR_HOST}/api/user_tokens/generate" \
  -H "Authorization: Bearer ${ADMIN_TOKEN}" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "name=hospital-qa-token&login=admin" \
  -s | jq -r '.token // "ERROR"' > /tmp/qa_token

if grep -q "ERROR" /tmp/qa_token; then
    echo "⚠️  Token de QA ya existe o hubo un error"
else
    QA_TOKEN=$(cat /tmp/qa_token)
    echo "✅ Token de QA creado: ${QA_TOKEN}"
fi

# Limpiar archivos temporales
rm -f /tmp/backend_result /tmp/frontend_result /tmp/qa_token

echo ""
echo "🎉 CONFIGURACIÓN DE PROYECTOS QA COMPLETADA!"
echo "=============================================="
echo ""
echo "📊 PROYECTOS CREADOS:"
echo "   🏥 Backend QA: hospital-backend-qa"
echo "   🖥️  Frontend QA: hospital-frontend-qa"
echo ""
echo "🌐 URLs DE ACCESO:"
echo "   SonarQube: ${SONAR_HOST}"
echo "   Backend QA: ${SONAR_HOST}/dashboard?id=hospital-backend-qa"
echo "   Frontend QA: ${SONAR_HOST}/dashboard?id=hospital-frontend-qa"
echo ""
echo "🔑 TOKEN DE QA: ${QA_TOKEN}"
echo ""
echo "💡 PRÓXIMOS PASOS:"
echo "   1. Ejecutar análisis de SonarQube desde Jenkins"
echo "   2. Verificar que los proyectos aparezcan en SonarQube"
echo "   3. Configurar Quality Gates específicos si es necesario"
echo ""
echo "🎯 ¡Proyectos de QA listos para análisis!"
