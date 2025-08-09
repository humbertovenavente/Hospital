#!/bin/bash

# Script para configurar Jenkins con plugins necesarios para CI/CD
echo "🔧 Configurando Jenkins para CI/CD..."

# Esperar a que Jenkins esté completamente iniciado
echo "⏳ Esperando que Jenkins esté listo..."
sleep 30

# URL de Jenkins
JENKINS_URL="http://localhost:8081"

# Verificar que Jenkins esté funcionando
if ! curl -s "$JENKINS_URL" > /dev/null; then
    echo "❌ Jenkins no está funcionando en $JENKINS_URL"
    exit 1
fi

echo "✅ Jenkins está funcionando en $JENKINS_URL"
echo ""
echo "📋 Pasos para configurar Jenkins:"
echo "1. Abre tu navegador y ve a: $JENKINS_URL"
echo "2. Usa la contraseña inicial: $(sudo cat /var/lib/jenkins/.jenkins/secrets/initialAdminPassword)"
echo "3. Instala los plugins sugeridos o selecciona:"
echo "   - Git plugin"
echo "   - Pipeline plugin"
echo "   - Docker plugin"
echo "   - Credentials plugin"
echo "4. Crea un usuario administrador"
echo "5. Configura la URL de Jenkins como: $JENKINS_URL"
echo ""
echo "🔗 Una vez configurado, podrás crear un pipeline job usando el Jenkinsfile del proyecto"
echo ""
echo "📁 Archivos del pipeline disponibles:"
echo "   - Jenkinsfile (pipeline principal)"
echo "   - .github/workflows/ci-cd.yml (GitHub Actions)"
echo "   - deploy-dev.sh, deploy-qa.sh, deploy-prod.sh (scripts de despliegue)"
echo "   - docker-compose.yml, docker-compose.qa.yml, docker-compose.prod.yml"
echo ""
echo "🚀 Para crear el pipeline job:"
echo "1. Ve a 'New Item' en Jenkins"
echo "2. Selecciona 'Pipeline'"
echo "3. Configura el repositorio Git: https://github.com/humbertovenavente/Hospital.git"
echo "4. Selecciona la rama: dev"
echo "5. En 'Pipeline script from SCM', selecciona 'Git'"
echo "6. Script path: Jenkinsfile"
echo "" 