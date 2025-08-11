#!/bin/bash

echo "🧹 LIMPIEZA DE AMBIENTE QA"
echo "=========================="

echo "📋 Deteniendo y eliminando contenedores existentes..."
docker compose -f docker-compose.qa.yml down -v --remove-orphans

echo "🗑️  Eliminando contenedores huérfanos..."
docker container prune -f

echo "🗑️  Eliminando volúmenes no utilizados..."
docker volume prune -f

echo "🗑️  Eliminando redes no utilizadas..."
docker network prune -f

echo "✅ Limpieza completada. Ambiente QA listo para nuevo despliegue."
