#!/bin/bash

# Script para configurar SonarQube en la nube junto con Drone
echo "🚀 Configurando SonarQube en la nube..."

# Verificar si ya está ejecutándose
if docker ps | grep -q sonarqube; then
    echo "✅ SonarQube ya está ejecutándose en la nube"
    docker ps | grep sonarqube
    echo "🌐 Acceso: http://104.197.237.11:9000"
    exit 0
fi

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
echo "🐳 Iniciando SonarQube en la nube..."
docker-compose -f docker-compose.sonarqube.yml up -d

# Esperar a que esté listo
echo "⏳ Esperando a que SonarQube esté listo..."
for i in {1..60}; do
    if curl -s --connect-timeout 5 http://localhost:9000 > /dev/null; then
        echo "✅ SonarQube está listo en la nube!"
        break
    fi
    echo "   Intento $i/60 - Esperando..."
    sleep 5
done

# Verificar estado
echo "📊 Estado de SonarQube:"
docker ps | grep sonarqube

echo ""
echo "🎉 SonarQube configurado en la nube!"
echo "🌐 Acceso: http://104.197.237.11:9000"
echo "🔑 Token: sqa_8f9c9ffeaf833e1486015527efadabc251e75755"
echo "📱 Proyectos: http://104.197.237.11:9000/projects"
echo ""
echo "✅ Ahora Drone podrá conectar a SonarQube correctamente"
