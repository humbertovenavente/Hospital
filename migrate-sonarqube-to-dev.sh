#!/bin/bash

echo "🚀 Migración de SonarQube Community a Developer Edition"
echo "======================================================"

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Función para mostrar mensajes
print_status() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Verificar si Docker está corriendo
if ! docker info > /dev/null 2>&1; then
    print_error "Docker no está corriendo. Por favor inicia Docker primero."
    exit 1
fi

# Verificar si el directorio de licencia existe
if [ ! -d "sonar-license" ]; then
    print_warning "Directorio de licencia no encontrado. Creando..."
    mkdir -p sonar-license
    print_warning "Por favor coloca tu archivo de licencia de SonarQube Developer Edition en el directorio 'sonar-license'"
    print_warning "El archivo debe llamarse 'sonar-license.txt'"
    echo ""
    print_status "Puedes continuar con la instalación y agregar la licencia después"
    echo ""
fi

# Detener SonarQube Community si está corriendo
print_status "Deteniendo SonarQube Community Edition..."
docker-compose -f docker-compose.sonarqube.yml down 2>/dev/null || true

# Crear directorio para la licencia si no existe
mkdir -p sonar-license

# Iniciar PostgreSQL primero
print_status "Iniciando PostgreSQL..."
docker-compose -f docker-compose.sonarqube-dev.yml up -d db

# Esperar a que PostgreSQL esté listo
print_status "Esperando a que PostgreSQL esté listo..."
sleep 10

# Verificar que PostgreSQL esté funcionando
if docker exec sonarqube-postgres pg_isready -U sonar > /dev/null 2>&1; then
    print_status "PostgreSQL está funcionando correctamente"
else
    print_error "PostgreSQL no está funcionando. Verificando logs..."
    docker logs sonarqube-postgres
    exit 1
fi

# Iniciar SonarQube Developer Edition
print_status "Iniciando SonarQube Developer Edition..."
docker-compose -f docker-compose.sonarqube-dev.yml up -d sonarqube

# Esperar a que SonarQube esté listo
print_status "Esperando a que SonarQube esté listo (esto puede tomar varios minutos)..."
echo "Puedes monitorear el progreso con: docker logs sonarqube-dev -f"

# Mostrar información de acceso
echo ""
echo "📋 Información de acceso:"
echo "=========================="
echo "URL: http://localhost:9000"
echo "Usuario por defecto: admin"
echo "Contraseña por defecto: admin"
echo ""
echo "🗄️  Base de datos PostgreSQL:"
echo "Host: localhost"
echo "Puerto: 5432 (accesible desde el host)"
echo "Usuario: sonar"
echo "Contraseña: sonar123"
echo "Base de datos: sonar"
echo ""
echo "📁 Directorio de licencia: ./sonar-license/"
echo ""
print_warning "IMPORTANTE: Debes obtener una licencia válida de SonarQube Developer Edition"
print_warning "y colocarla en el directorio 'sonar-license' con el nombre 'sonar-license.txt'"
echo ""
print_status "Para verificar el estado: docker-compose -f docker-compose.sonarqube-dev.yml ps"
print_status "Para ver logs: docker-compose -f docker-compose.sonarqube-dev.yml logs -f"



