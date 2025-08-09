#!/bin/bash

# Script para iniciar Jenkins manualmente
echo "🚀 Iniciando Jenkins en puerto 8081..."

# Configurar variables de entorno
export JENKINS_HOME=/var/lib/jenkins/.jenkins
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH

# Crear directorio si no existe
sudo mkdir -p $JENKINS_HOME
sudo chown -R jenkins:jenkins /var/lib/jenkins

# Iniciar Jenkins
echo "📋 Jenkins se iniciará en: http://localhost:8081"
echo "🔑 Contraseña inicial: $(sudo cat $JENKINS_HOME/secrets/initialAdminPassword 2>/dev/null || echo 'Se generará al iniciar')"
echo ""
echo "Presiona Ctrl+C para detener Jenkins"
echo ""

# Ejecutar Jenkins
sudo -u jenkins java -jar /usr/share/java/jenkins.war --httpPort=8081 