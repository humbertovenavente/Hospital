#!/bin/bash

# Script para iniciar SonarQube en el servidor remoto
echo "🚀 Iniciando SonarQube en servidor remoto..."

# Verificar si SonarQube ya está ejecutándose
if curl -s --connect-timeout 5 http://localhost:9000 > /dev/null; then
    echo "✅ SonarQube ya está ejecutándose en http://localhost:9000"
    exit 0
fi

# Crear directorio de datos si no existe
mkdir -p /opt/sonarqube/data
mkdir -p /opt/sonarqube/logs
mkdir -p /opt/sonarqube/extensions

# Crear docker-compose para SonarQube
cat > docker-compose.sonarqube.yml << 'EOF'
version: '3.8'

services:
  sonarqube:
    image: sonarqube:9-community
    container_name: sonarqube
    ports:
      - "9000:9000"
    volumes:
      - sonarqube_data:/opt/sonarqube/data
      - sonarqube_logs:/opt/sonarqube/logs
      - sonarqube_extensions:/opt/sonarqube/extensions
    environment:
      - SONAR_ES_BOOTSTRAP_CHECKS_DISABLE=true
    restart: unless-stopped

volumes:
  sonarqube_data:
  sonarqube_logs:
  sonarqube_extensions:
EOF

# Iniciar SonarQube
echo "🐳 Iniciando contenedor de SonarQube..."
docker-compose -f docker-compose.sonarqube.yml up -d

# Esperar a que SonarQube esté listo
echo "⏳ Esperando a que SonarQube esté listo..."
for i in {1..30}; do
    if curl -s --connect-timeout 5 http://localhost:9000 > /dev/null; then
        echo "✅ SonarQube está listo en http://localhost:9000"
        break
    fi
    echo "   Intento $i/30 - Esperando..."
    sleep 10
done

# Verificar estado
echo "📊 Estado de SonarQube:"
docker ps | grep sonarqube

echo "🌐 SonarQube disponible en: http://104.197.237.11:9000"
echo "🔑 Token: sqa_8f9c9ffeaf833e1486015527efadabc251e75755"
