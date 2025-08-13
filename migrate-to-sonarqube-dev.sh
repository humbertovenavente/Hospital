#!/bin/bash

echo "🚀 MIGRACIÓN A SONARQUBE DEVELOPER EDITION"
echo "=========================================="

# Detener SonarQube Community actual
echo "🛑 Deteniendo SonarQube Community..."
docker stop sonarqube
docker rm sonarqube

echo "🧹 Limpiando contenedores y volúmenes antiguos..."
docker container prune -f
docker volume prune -f

# Desplegar SonarQube Developer Edition
echo "🚀 Desplegando SonarQube Developer Edition..."
docker compose -f docker-compose.sonarqube-dev.yml up -d

echo "⏳ Esperando que SonarQube Developer Edition esté listo..."
echo "💡 Esto puede tomar varios minutos..."

# Esperar hasta que SonarQube esté funcionando
max_attempts=30
attempt=1

while [ $attempt -le $max_attempts ]; do
    echo "📡 Intento $attempt/$max_attempts - Verificando SonarQube..."
    
    if curl -s http://localhost:9001/api/system/status | grep -q "UP"; then
        echo "✅ SonarQube Developer Edition está funcionando correctamente!"
        break
    fi
    
    if [ $attempt -eq $max_attempts ]; then
        echo "❌ Timeout: SonarQube no se inició en el tiempo esperado"
        echo "💡 Verifica los logs con: docker logs sonarqube-developer"
        exit 1
    fi
    
    echo "⏳ Esperando 10 segundos más..."
    sleep 10
    attempt=$((attempt + 1))
done

echo ""
echo "🎉 MIGRACIÓN COMPLETADA EXITOSAMENTE"
echo "===================================="
echo "🌐 SonarQube Developer Edition: http://localhost:9001"
echo "🔑 Usuario por defecto: admin"
echo "🔑 Contraseña por defecto: admin"
echo ""
echo "📋 Próximos pasos:"
echo "1. Ve a http://localhost:9001"
echo "2. Inicia sesión con admin/admin"
echo "3. Cambia la contraseña cuando se te solicite"
echo "4. Ejecuta el análisis de SonarQube: ./analyze-qa-sonar.sh"
