#!/bin/bash

# Script para solucionar problemas de permisos de Jenkins
echo "🔧 Solucionando problemas de permisos de Jenkins..."

# Agregar usuario jenkins al grupo del usuario actual
echo "👥 Agregando jenkins al grupo del usuario..."
sudo usermod -aG $(id -gn) jenkins

# Cambiar permisos del directorio del proyecto
echo "📁 Cambiando permisos del directorio del proyecto..."
sudo chown -R jenkins:jenkins /home/jose/Desktop/Hospital
sudo chmod -R 755 /home/jose/Desktop/Hospital

# Verificar que Jenkins pueda acceder al directorio
echo "🔍 Verificando acceso de Jenkins..."
sudo -u jenkins ls -la /home/jose/Desktop/Hospital > /dev/null 2>&1
if [ $? -eq 0 ]; then
    echo "✅ Jenkins puede acceder al directorio"
else
    echo "❌ Jenkins aún no puede acceder al directorio"
fi

# Verificar acceso al repositorio remoto
echo "🌐 Verificando acceso al repositorio remoto..."
sudo -u jenkins git ls-remote -h -- https://github.com/humbertovenavente/Hospital.git HEAD > /dev/null 2>&1
if [ $? -eq 0 ]; then
    echo "✅ Jenkins puede acceder al repositorio remoto"
else
    echo "❌ Jenkins no puede acceder al repositorio remoto"
fi

echo ""
echo "📋 Configuración recomendada en Jenkins:"
echo "   - Repository URL: https://github.com/humbertovenavente/Hospital.git"
echo "   - Branch: dev"
echo "   - Script Path: Jenkinsfile"
echo "   - NO uses directorio local, usa repositorio remoto"
echo ""
echo "🔄 Reinicia Jenkins después de estos cambios:"
echo "   ./stop-jenkins.sh && ./start-jenkins.sh" 