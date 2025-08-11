#!/bin/bash

echo "🧹 LIMPIEZA DE AMBIENTE QA"
echo "=========================="

echo "📋 Deteniendo y eliminando contenedores existentes..."
docker compose -f docker-compose.qa.yml down -v --remove-orphans

echo "🗑️  Eliminando contenedores específicos de QA por nombre..."
docker rm -f hospital-oracle-qa 2>/dev/null || echo "Contenedor hospital-oracle-qa no encontrado"
docker rm -f hospital-backend-qa 2>/dev/null || echo "Contenedor hospital-backend-qa no encontrado"
docker rm -f hospital-frontend-qa 2>/dev/null || echo "Contenedor hospital-frontend-qa no encontrado"
docker rm -f hospital-nginx-qa 2>/dev/null || echo "Contenedor hospital-nginx-qa no encontrado"

echo "🗑️  Eliminando contenedores huérfanos..."
docker container prune -f

echo "🗑️  Eliminando volúmenes no utilizados..."
docker volume prune -f

echo "🗑️  Eliminando redes no utilizadas..."
docker network prune -f

echo "🔍 Verificando que no queden contenedores de QA..."
docker ps -a | grep -E "(hospital-.*-qa|oracle-qa)" || echo "✅ No se encontraron contenedores de QA"

echo "✅ Limpieza completada. Ambiente QA listo para nuevo despliegue."
