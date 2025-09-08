#!/bin/bash

echo "🚀 Configurando SonarQube en la nube con puerto 9003..."

SONARQUBE_CONTAINER_NAME="sonarqube-cloud"
SONARQUBE_IMAGE="sonarqube:10.4.1-community"
SONARQUBE_PORT="9003"
SONARQUBE_DATA_DIR="/opt/sonarqube/data"
SONARQUBE_EXTENSIONS_DIR="/opt/sonarqube/extensions"
SONARQUBE_LOGS_DIR="/opt/sonarqube/logs"

# 1. Verificar si SonarQube ya está ejecutándose
if docker ps -f name=$SONARQUBE_CONTAINER_NAME --format "{{.Names}}" | grep -q $SONARQUBE_CONTAINER_NAME; then
    echo "✅ SonarQube ya está ejecutándose en la nube"
    docker ps -f name=$SONARQUBE_CONTAINER_NAME
    echo "🌐 Acceso: http://104.197.237.11:$SONARQUBE_PORT"
    exit 0
fi

# 2. Detener y eliminar contenedores existentes si hay alguno
if docker ps -a -f name=$SONARQUBE_CONTAINER_NAME --format "{{.Names}}" | grep -q $SONARQUBE_CONTAINER_NAME; then
    echo "🛑 Deteniendo y eliminando contenedor SonarQube existente..."
    docker stop $SONARQUBE_CONTAINER_NAME
    docker rm $SONARQUBE_CONTAINER_NAME
    echo "✅ Contenedor eliminado."
fi

# 3. Crear directorios persistentes si no existen
echo "📁 Creando directorios persistentes para SonarQube..."
mkdir -p $SONARQUBE_DATA_DIR
mkdir -p $SONARQUBE_EXTENSIONS_DIR
mkdir -p $SONARQUBE_LOGS_DIR
echo "✅ Directorios creados."

# 4. Iniciar SonarQube con Docker
echo "🐳 Iniciando SonarQube Docker container en puerto $SONARQUBE_PORT..."
docker run -d --name $SONARQUBE_CONTAINER_NAME \
    --restart unless-stopped \
    -p $SONARQUBE_PORT:9000 \
    -v $SONARQUBE_DATA_DIR:/opt/sonarqube/data \
    -v $SONARQUBE_EXTENSIONS_DIR:/opt/sonarqube/extensions \
    -v $SONARQUBE_LOGS_DIR:/opt/sonarqube/logs \
    $SONARQUBE_IMAGE

if [ $? -eq 0 ]; then
    echo "✅ Contenedor SonarQube iniciado. Esperando a que esté listo (esto puede tardar unos minutos)..."
    # Esperar a que SonarQube esté listo (puerto 9000)
    timeout=300 # 5 minutos
    start_time=$(date +%s)
    while ! curl -s http://localhost:$SONARQUBE_PORT > /dev/null; do
        current_time=$(date +%s)
        elapsed_time=$((current_time - start_time))
        if [ $elapsed_time -ge $timeout ]; then
            echo "❌ SonarQube no se inició a tiempo. Revisa los logs."
            exit 1
        fi
        echo "⏳ Esperando a SonarQube en puerto $SONARQUBE_PORT... ($elapsed_time s)"
        sleep 10
    done
    echo "✅ SonarQube está listo y escuchando en el puerto $SONARQUBE_PORT."
else
    echo "❌ Error al iniciar el contenedor SonarQube."
    exit 1
fi

# 5. Verificar el estado final
echo "📋 Verificando estado final de SonarQube:"
docker ps -f name=$SONARQUBE_CONTAINER_NAME
echo "🌐 Acceso a SonarQube: http://104.197.237.11:$SONARQUBE_PORT"
echo "🎉 SonarQube configurado y ejecutándose en la nube con puerto $SONARQUBE_PORT."
