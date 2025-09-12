#!/bin/bash

# Script para crear contenedores de producción en Portainer
# Las imágenes ya están disponibles en Docker Hub

echo "=== CREANDO CONTENEDORES DE PRODUCCIÓN ==="
echo "Imágenes disponibles en Docker Hub:"
echo "- humbertovenavente/hospital-backend-prod"
echo "- humbertovenavente/hospital-frontend-prod"
echo ""

# Detener contenedores existentes si existen
echo "Deteniendo contenedores existentes..."
docker stop hospital-backend-prod-cloud hospital-frontend-prod-cloud 2>/dev/null || true
docker rm -f hospital-backend-prod-cloud hospital-frontend-prod-cloud 2>/dev/null || true

echo ""
echo "=== CREANDO CONTENEDOR BACKEND ==="
docker run -d \
  --name hospital-backend-prod-cloud \
  --restart=always \
  -p 8020:8080 \
  -e QUARKUS_PROFILE=prod \
  -e QUARKUS_DATASOURCE_JDBC_URL=jdbc:oracle:thin:@34.56.133.126:1521/XE \
  -e QUARKUS_DATASOURCE_USERNAME=system \
  -e QUARKUS_DATASOURCE_PASSWORD=Oracle123 \
  -e QUARKUS_HTTP_CORS=true \
  -e QUARKUS_HTTP_CORS_ORIGINS=http://34.46.73.44:8021,http://localhost:8021,http://localhost:5173,http://localhost:8080 \
  -e QUARKUS_HTTP_CORS_METHODS=GET,POST,PUT,DELETE,OPTIONS,PATCH,HEAD \
  -e QUARKUS_HTTP_CORS_HEADERS=* \
  -e QUARKUS_HTTP_CORS_ACCESS_CONTROL_ALLOW_CREDENTIALS=true \
  -e QUARKUS_HTTP_CORS_EXPOSED_HEADERS=Content-Disposition \
  humbertovenavente/hospital-backend-prod

echo ""
echo "=== CREANDO CONTENEDOR FRONTEND ==="
docker run -d \
  --name hospital-frontend-prod-cloud \
  --restart=always \
  -p 8021:80 \
  humbertovenavente/hospital-frontend-prod

echo ""
echo "=== VERIFICANDO CONTENEDORES ==="
sleep 5
docker ps | grep hospital-.*-prod-cloud

echo ""
echo "=== LOGS DEL BACKEND ==="
docker logs hospital-backend-prod-cloud --tail=20

echo ""
echo "=== LOGS DEL FRONTEND ==="
docker logs hospital-frontend-prod-cloud --tail=10

echo ""
echo "=== CONTENEDORES CREADOS EXITOSAMENTE ==="
echo "Backend: http://34.46.73.44:8020"
echo "Frontend: http://34.46.73.44:8021"
echo ""
echo "Para ver logs en tiempo real:"
echo "  docker logs -f hospital-backend-prod-cloud"
echo "  docker logs -f hospital-frontend-prod-cloud"

