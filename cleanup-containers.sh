#!/bin/bash

echo "🧹 Script de limpieza de contenedores Docker del Hospital"
echo "========================================================"

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Función para mostrar mensajes con colores
print_status() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Verificar si Docker está ejecutándose
if ! docker info > /dev/null 2>&1; then
    print_error "Docker no está ejecutándose. Inicia Docker primero."
    exit 1
fi

print_status "Verificando contenedores existentes..."

# Mostrar contenedores existentes con nombres del hospital
echo ""
echo "Contenedores existentes con nombres 'hospital-':"
docker ps -a --filter "name=hospital-" --format "table {{.Names}}\t{{.Status}}\t{{.Image}}"

echo ""
print_warning "¿Deseas continuar con la limpieza? (y/N)"
read -r response

if [[ ! "$response" =~ ^[Yy]$ ]]; then
    print_status "Operación cancelada por el usuario."
    exit 0
fi

# Detener y eliminar contenedores del hospital
print_status "Deteniendo contenedores del hospital..."
docker stop $(docker ps -q --filter "name=hospital-") 2>/dev/null || true

print_status "Eliminando contenedores del hospital..."
docker rm $(docker ps -aq --filter "name=hospital-") 2>/dev/null || true

# Limpiar contenedores huérfanos
print_status "Limpiando contenedores huérfanos..."
docker container prune -f

# Limpiar redes no utilizadas
print_status "Limpiando redes no utilizadas..."
docker network prune -f

# Limpiar volúmenes no utilizados (cuidado: esto puede eliminar datos)
echo ""
print_warning "¿Deseas limpiar volúmenes no utilizados? (y/N)"
read -r response

if [[ "$response" =~ ^[Yy]$ ]]; then
    print_status "Limpiando volúmenes no utilizados..."
    docker volume prune -f
else
    print_status "Saltando limpieza de volúmenes."
fi

# Verificar estado final
echo ""
print_status "Estado final después de la limpieza:"
echo "Contenedores activos:"
docker ps --filter "name=hospital-" --format "table {{.Names}}\t{{.Status}}\t{{.Image}}"

echo ""
print_status "Limpieza completada exitosamente!"
print_status "Ahora puedes ejecutar tu pipeline sin conflictos de nombres de contenedores."
