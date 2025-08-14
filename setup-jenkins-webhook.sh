#!/bin/bash

echo "🚀 Configurando Webhooks Automáticos en Jenkins..."
echo "=================================================="

# Configuración
JENKINS_URL="http://localhost:8080"
GITHUB_USERNAME=""
GITHUB_TOKEN=""
REPO_URL=""
WEBHOOK_SECRET=""

# Función para obtener input del usuario
get_input() {
    local prompt="$1"
    local var_name="$2"
    local is_secret="$3"
    
    if [ "$is_secret" = "true" ]; then
        read -s -p "$prompt: " input
        echo
    else
        read -p "$prompt: " input
    fi
    
    eval "$var_name='$input'"
}

echo "📋 Configuración de Webhooks Automáticos"
echo ""

# Obtener información del usuario
get_input "Tu usuario de GitHub" GITHUB_USERNAME false
get_input "Tu token personal de GitHub (NO contraseña)" GITHUB_TOKEN true
get_input "URL de tu repositorio (ej: https://github.com/usuario/repo.git)" REPO_URL false
get_input "Secret para webhook (opcional, presiona Enter si no quieres)" WEBHOOK_SECRET false

echo ""
echo "🔧 Verificando Jenkins..."

# Verificar que Jenkins esté disponible
if curl -f "$JENKINS_URL" >/dev/null 2>&1; then
    echo "✅ Jenkins está disponible en $JENKINS_URL"
else
    echo "❌ Jenkins no está disponible en $JENKINS_URL"
    echo "💡 Asegúrate de que Jenkins esté ejecutándose"
    exit 1
fi

echo ""
echo "📝 Pasos para configurar webhooks automáticos:"
echo "=============================================="

echo ""
echo "1️⃣ CONFIGURAR CREDENCIALES EN JENKINS:"
echo "   • Ir a: $JENKINS_URL/manage/credentials/system/domain/_/"
echo "   • Click en 'Add Credentials'"
echo "   • Kind: Username with password"
echo "   • Scope: Global"
echo "   • Username: $GITHUB_USERNAME"
echo "   • Password: [Tu token]"
echo "   • ID: github-credentials"
echo "   • Description: Credenciales de GitHub para webhooks"

echo ""
echo "2️⃣ CONFIGURAR EL JOB EN JENKINS:"
echo "   • Crear nuevo job → Pipeline"
echo "   • Pipeline → Definition: Pipeline script from SCM"
echo "   • SCM: Git"
echo "   • Repository URL: $REPO_URL"
echo "   • Credentials: Seleccionar 'github-credentials'"
echo "   • Script Path: Jenkinsfile"
echo "   • Branches to build: */main, */develop, */feature/*"

echo ""
echo "3️⃣ CONFIGURAR WEBHOOK EN GITHUB:"
echo "   • Ir a tu repositorio → Settings → Webhooks"
echo "   • Add webhook:"
echo "     - Payload URL: $JENKINS_URL/github-webhook/"
echo "     - Content type: application/json"
if [ ! -z "$WEBHOOK_SECRET" ]; then
    echo "     - Secret: $WEBHOOK_SECRET"
else
    echo "     - Secret: (dejar vacío)"
fi
echo "     - Events:"
echo "       ✅ Just the push event"
echo "       ✅ Pull requests"
echo "       ✅ Pushes"

echo ""
echo "4️⃣ VERIFICAR CONFIGURACIÓN:"
echo "   • Hacer un commit pequeño en tu repositorio"
echo "   • Verificar que Jenkins inicie un build automáticamente"
echo "   • Revisar logs en Jenkins para confirmar"

echo ""
echo "🔍 VERIFICACIÓN RÁPIDA:"
echo "======================="

# Verificar plugins necesarios
echo "📦 Plugins necesarios en Jenkins:"
echo "   • GitHub Integration Plugin: $(curl -s "$JENKINS_URL/pluginManager/api/json?depth=1" | grep -o '"shortName":"github"' >/dev/null && echo "✅ Instalado" || echo "❌ No instalado")"
echo "   • Git plugin: $(curl -s "$JENKINS_URL/pluginManager/api/json?depth=1" | grep -o '"shortName":"git"' >/dev/null && echo "✅ Instalado" || echo "❌ No instalado")"
echo "   • Pipeline plugin: $(curl -s "$JENKINS_URL/pluginManager/api/json?depth=1" | grep -o '"shortName":"workflow-aggregator"' >/dev/null && echo "✅ Instalado" || echo "❌ No instalado")"

echo ""
echo "🌐 URLs importantes:"
echo "   • Jenkins: $JENKINS_URL"
echo "   • Webhook URL: $JENKINS_URL/github-webhook/"
echo "   • Repositorio: $REPO_URL"

echo ""
echo "📚 COMANDOS ÚTILES PARA TESTING:"
echo "================================="
echo "• Test webhook manual:"
echo "  curl -X POST $JENKINS_URL/github-webhook/ \\"
echo "    -H 'Content-Type: application/json' \\"
echo "    -d '{\"ref\":\"refs/heads/main\",\"repository\":{\"full_name\":\"test/repo\"}}'"

echo ""
echo "• Ver logs de Jenkins:"
echo "  curl -s $JENKINS_URL/log/all/"

echo ""
echo "🎯 BENEFICIOS DE ESTA CONFIGURACIÓN:"
echo "===================================="
echo "✅ Builds automáticos al hacer push"
echo "✅ Builds automáticos en Pull Requests"
echo "✅ Ejecución inmediata (sin polling)"
echo "✅ Integración continua real"
echo "✅ Feedback rápido sobre calidad del código"

echo ""
echo "🚀 ¡Configuración completada!"
echo "💡 Sigue los pasos arriba para activar los webhooks automáticos"
echo "🔍 Revisa la documentación en: jenkins-webhook-setup.md"
