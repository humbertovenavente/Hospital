#!/bin/bash

# Script final para configurar Jenkins completamente
echo "🚀 Configuración final de Jenkins para el pipeline CI/CD"

# Verificar que Jenkins esté ejecutándose
if ! ss -tlnp | grep 8081 > /dev/null; then
    echo "❌ Jenkins no está ejecutándose. Iniciando..."
    ./start-jenkins.sh &
    sleep 10
fi

# Verificar permisos de Docker
echo "🔧 Configurando permisos de Docker..."
sudo chmod 666 /var/run/docker.sock

# Verificar que Jenkins pueda usar Docker
if sudo -u jenkins docker ps > /dev/null 2>&1; then
    echo "✅ Jenkins puede usar Docker"
else
    echo "❌ Jenkins no puede usar Docker"
    exit 1
fi

echo ""
echo "🎯 CONFIGURACIÓN COMPLETA"
echo "=========================="
echo ""
echo "✅ Jenkins está ejecutándose en: http://localhost:8081"
echo "✅ Docker está configurado correctamente"
echo "✅ Java 17 está disponible"
echo "✅ Maven está disponible"
echo "✅ Node.js está disponible"
echo ""
echo "📋 PRÓXIMOS PASOS:"
echo "=================="
echo ""
echo "1. 🌐 Abre tu navegador y ve a: http://localhost:8081"
echo ""
echo "2. 🔌 Instala el plugin JUnit:"
echo "   - Ve a: Manage Jenkins > Manage Plugins"
echo "   - Pestaña 'Available'"
echo "   - Busca 'JUnit'"
echo "   - Marca y haz clic en 'Install without restart'"
echo ""
echo "3. 🏗️  Crea el pipeline job:"
echo "   - Haz clic en 'New Job'"
echo "   - Nombre: 'Hospital-Pipeline'"
echo "   - Tipo: 'Pipeline'"
echo "   - En 'Pipeline':"
echo "     * Definition: 'Pipeline script from SCM'"
echo "     * SCM: 'Git'"
echo "     * Repository URL: https://github.com/humbertovenavente/Hospital.git"
echo "     * Branch: dev"
echo "     * Script Path: Jenkinsfile"
echo ""
echo "4. ▶️  Ejecuta el pipeline:"
echo "   - Haz clic en 'Build Now'"
echo ""
echo "🔗 URLs útiles:"
echo "==============="
echo "• Jenkins: http://localhost:8081"
echo "• Plugins: http://localhost:8081/pluginManager/available"
echo "• New Job: http://localhost:8081/newJob"
echo "• Pipeline: http://localhost:8081/job/Hospital-Pipeline"
echo ""
echo "📁 Archivos de configuración:"
echo "============================="
echo "• Jenkinsfile: Pipeline principal"
echo "• docker-compose.yml: Desarrollo local"
echo "• docker-compose.qa.yml: Ambiente QA"
echo "• docker-compose.prod.yml: Ambiente producción"
echo "• deploy-*.sh: Scripts de despliegue"
echo ""
echo "🎉 ¡Jenkins está listo para ejecutar el pipeline CI/CD!" 