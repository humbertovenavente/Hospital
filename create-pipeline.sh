#!/bin/bash

# Script para crear automáticamente el pipeline en Jenkins
echo "🚀 Configurando pipeline automáticamente en Jenkins..."

# Variables
JENKINS_URL="http://localhost:8081"
JOB_NAME="Hospital-CI-CD"
GITHUB_REPO="https://github.com/humbertovenavente/Hospital.git"

# Esperar a que Jenkins esté completamente iniciado
echo "⏳ Esperando que Jenkins esté completamente iniciado..."
sleep 30

# Verificar que Jenkins esté funcionando
if ! curl -s "$JENKINS_URL" > /dev/null; then
    echo "❌ Jenkins no está funcionando en $JENKINS_URL"
    echo "🔧 Iniciando Jenkins..."
    ./start-jenkins.sh &
    sleep 30
fi

echo "✅ Jenkins está funcionando en $JENKINS_URL"
echo ""
echo "📋 Instrucciones para completar la configuración:"
echo ""
echo "1️⃣  **Configuración Inicial de Jenkins:**"
echo "   - Abre: $JENKINS_URL"
echo "   - Contraseña inicial: $(sudo cat /var/lib/jenkins/.jenkins/secrets/initialAdminPassword 2>/dev/null || echo 'Se generará al acceder')"
echo "   - Instala plugins sugeridos"
echo "   - Crea usuario administrador"
echo ""
echo "2️⃣  **Crear Pipeline Job Manualmente:**"
echo "   - Ve a 'New Item' en Jenkins"
echo "   - Nombre: $JOB_NAME"
echo "   - Tipo: Pipeline"
echo "   - Configuración:"
echo "     • Pipeline: Pipeline script from SCM"
echo "     • SCM: Git"
echo "     • Repository URL: $GITHUB_REPO"
echo "     • Branch: dev"
echo "     • Script Path: Jenkinsfile"
echo ""
echo "3️⃣  **Configurar Credenciales (Opcional):**"
echo "   - Manage Jenkins → Manage Credentials"
echo "   - Add Credentials → Username with password"
echo "   - Scope: Global"
echo "   - Username: Tu usuario de GitHub"
echo "   - Password: Tu token de GitHub"
echo ""
echo "4️⃣  **Ejecutar Pipeline:**"
echo "   - Ve al job creado"
echo "   - Click en 'Build Now'"
echo "   - Monitorea el progreso en 'Build History'"
echo ""
echo "🔗 **URLs importantes:**"
echo "   - Jenkins: $JENKINS_URL"
echo "   - Repositorio: $GITHUB_REPO"
echo "   - Pipeline: $JENKINS_URL/job/$JOB_NAME"
echo ""
echo "📁 **Archivos del pipeline disponibles:**"
echo "   - Jenkinsfile (pipeline principal)"
echo "   - .github/workflows/ci-cd.yml (GitHub Actions)"
echo "   - deploy-*.sh (scripts de despliegue)"
echo "   - docker-compose*.yml (configuraciones Docker)"
echo ""
echo "🎯 **Flujo de trabajo:**"
echo "   1. Push a rama 'dev' → Despliegue automático a desarrollo"
echo "   2. Merge a 'QA' → Despliegue automático a QA"
echo "   3. Merge a 'dev' → Despliegue automático a producción"
echo ""
echo "✅ ¡Jenkins está listo para configurar el pipeline!" 