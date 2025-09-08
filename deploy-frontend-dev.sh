#!/bin/bash

# Detener y eliminar el contenedor actual si existe
if [ "$(docker ps -aq -f name=hospital-frontend-dev-cloud)" ]; then
    echo "Deteniendo y eliminando el contenedor existente..."
    docker stop hospital-frontend-dev-cloud
    docker rm hospital-frontend-dev-cloud
fi

# Eliminar la imagen antigua si existe
if [ "$(docker images -q hospital-frontend-dev)" ]; then
    echo "Eliminando la imagen antigua..."
    docker rmi hospital-frontend-dev
fi

# Construir la nueva imagen
echo "Construyendo la nueva imagen..."
docker build -t hospital-frontend-dev -f Dockerfile.frontend .

# Ejecutar el contenedor
echo "Iniciando el contenedor..."
docker run -d \
  --name hospital-frontend-dev-cloud \
  -p 8061:80 \
  --restart unless-stopped \
  hospital-frontend-dev

echo "Despliegue completado. El frontend está disponible en http://34.46.73.44:8061"
