#!/bin/bash

echo "🚀 INICIANDO JENKINS - SCRIPT ROBUSTO"
echo "====================================="

# Configurar variables de entorno
export JENKINS_HOME=/var/lib/jenkins/.jenkins
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH

# Verificar que Java esté disponible
echo "🔍 Verificando Java..."
if ! java -version > /dev/null 2>&1; then
    echo "❌ Java no está disponible"
    exit 1
fi
echo "✅ Java disponible: $(java -version 2>&1 | head -1)"

# Verificar que el archivo WAR exista
echo "🔍 Verificando archivo Jenkins WAR..."
if [ ! -f /usr/share/java/jenkins.war ]; then
    echo "❌ Archivo jenkins.war no encontrado"
    exit 1
fi
echo "✅ Archivo jenkins.war encontrado"

# Crear directorio si no existe
echo "🔍 Configurando directorio de Jenkins..."
sudo mkdir -p $JENKINS_HOME
sudo chown -R jenkins:jenkins /var/lib/jenkins

# Verificar que el puerto esté libre
echo "🔍 Verificando puerto 8081..."
if sudo lsof -i :8081 > /dev/null 2>&1; then
    echo "⚠️  Puerto 8081 está en uso. Deteniendo proceso..."
    sudo lsof -ti :8081 | xargs sudo kill -9
    sleep 2
fi

# Iniciar Jenkins
echo "🚀 Iniciando Jenkins en puerto 8081..."
echo "📋 Jenkins se iniciará en: http://localhost:8081"
echo "🔑 Contraseña inicial: $(sudo cat $JENKINS_HOME/secrets/initialAdminPassword 2>/dev/null || echo 'Se generará al iniciar')"
echo ""
echo "Presiona Ctrl+C para detener Jenkins"
echo ""

# Ejecutar Jenkins con logs detallados
sudo -u jenkins java -jar /usr/share/java/jenkins.war --httpPort=8081 --debug 