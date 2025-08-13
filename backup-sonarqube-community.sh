#!/bin/bash

echo "💾 Backup de SonarQube Community Edition"
echo "========================================"

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

# Crear directorio de backup
BACKUP_DIR="sonarqube-backup-$(date +%Y%m%d-%H%M%S)"
mkdir -p "$BACKUP_DIR"

print_status "Creando backup en directorio: $BACKUP_DIR"

# Backup de docker-compose actual
if [ -f "docker-compose.sonarqube.yml" ]; then
    cp docker-compose.sonarqube.yml "$BACKUP_DIR/"
    print_status "Backup de docker-compose.sonarqube.yml creado"
fi

# Backup de volúmenes de Docker
print_status "Creando backup de volúmenes de Docker..."

# Listar volúmenes de SonarQube
VOLUMES=$(docker volume ls --format "{{.Name}}" | grep sonarqube)

if [ -n "$VOLUMES" ]; then
    for volume in $VOLUMES; do
        print_status "Backup del volumen: $volume"
        docker run --rm -v "$volume":/data -v "$(pwd)/$BACKUP_DIR":/backup alpine tar czf "/backup/$volume-backup.tar.gz" -C /data .
    done
else
    print_warning "No se encontraron volúmenes de SonarQube para hacer backup"
fi

# Backup de archivos de configuración
print_status "Buscando archivos de configuración..."

# Buscar archivos de configuración de SonarQube
find . -name "sonar-project*.properties" -exec cp {} "$BACKUP_DIR/" \;
find . -name "*sonar*" -type f -exec cp {} "$BACKUP_DIR/" \;

# Crear archivo de información del backup
cat > "$BACKUP_DIR/backup-info.txt" << EOF
Backup de SonarQube Community Edition
Fecha: $(date)
Directorio: $BACKUP_DIR

Contenido del backup:
- docker-compose.sonarqube.yml
- Volúmenes de Docker (si existen)
- Archivos de configuración de SonarQube

Para restaurar:
1. Copiar los archivos de vuelta al directorio principal
2. Restaurar volúmenes si es necesario
3. Ejecutar: docker-compose -f docker-compose.sonarqube.yml up -d

Nota: Este backup es solo para la configuración, no incluye datos de análisis
EOF

print_status "Backup completado en: $BACKUP_DIR"
echo ""
echo "📁 Contenido del backup:"
ls -la "$BACKUP_DIR"
echo ""
print_warning "IMPORTANTE: Este backup solo incluye configuración, no datos de análisis"
print_warning "Los datos de análisis se perderán al cambiar a Developer Edition"
echo ""
print_status "Puedes proceder con la migración ejecutando: ./migrate-sonarqube-to-dev.sh"



