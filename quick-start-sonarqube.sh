#!/bin/bash

# Script rápido para iniciar SonarQube en el servidor remoto
echo "🚀 Iniciando SonarQube rápidamente..."

# Verificar si ya está ejecutándose
if docker ps | grep -q sonarqube; then
    echo "✅ SonarQube ya está ejecutándose"
    docker ps | grep sonarqube
    exit 0
fi

# Detener contenedores existentes
echo "🛑 Deteniendo contenedores existentes..."
docker stop sonarqube 2>/dev/null || true
docker rm sonarqube 2>/dev/null || true

# Crear directorios necesarios
echo "📁 Creando directorios..."
mkdir -p /opt/sonarqube/data
mkdir -p /opt/sonarqube/logs
mkdir -p /opt/sonarqube/extensions

# Iniciar SonarQube
echo "🐳 Iniciando SonarQube..."
docker run -d \
  --name sonarqube \
  -p 9000:9000 \
  -v sonarqube_data:/opt/sonarqube/data \
  -v sonarqube_logs:/opt/sonarqube/logs \
  -v sonarqube_extensions:/opt/sonarqube/extensions \
  -e SONAR_ES_BOOTSTRAP_CHECKS_DISABLE=true \
  --restart unless-stopped \
  sonarqube:9-community

# Esperar a que esté listo
echo "⏳ Esperando a que SonarQube esté listo..."
for i in {1..60}; do
    if curl -s --connect-timeout 5 http://localhost:9000 > /dev/null 2>&1; then
        echo "✅ SonarQube está listo!"
        break
    fi
    echo "   Intento $i/60 - Esperando..."
    sleep 5
done

# Verificar estado
echo "📊 Estado de SonarQube:"
docker ps | grep sonarqube

echo "🌐 SonarQube disponible en: http://104.197.237.11:9000"
echo "🔑 Token: sqa_8f9c9ffeaf833e1486015527efadabc251e75755"
echo "📱 Proyectos: http://104.197.237.11:9000/projects"
