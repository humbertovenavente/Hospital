#!/bin/bash

# Script para desplegar Drone CI/CD en Google Cloud Platform
# Autor: Hospital Team
# Fecha: $(date)

set -e  # Salir si hay algún error

echo "🚀 Iniciando despliegue de Drone CI/CD en Google Cloud..."
echo "=================================================="

# Variables de configuración
PROJECT_ID="hospital-470223"
REGION="us-central1"
ZONE="us-central1-a"
INSTANCE_NAME="drone-ci-gcp"
MACHINE_TYPE="e2-standard-4"
DISK_SIZE="50GB"
CREDENTIALS_FILE="hospital-credentials.json"

# Variables de Drone
DRONE_GITHUB_CLIENT_ID=""
DRONE_GITHUB_CLIENT_SECRET=""
DRONE_RPC_SECRET=""
DRONE_SERVER_HOST=""
DRONE_SERVER_PROTO="https"
DRONE_ADMIN_USER=""
DRONE_SECRET=""

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Función para imprimir mensajes con colores
print_status() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Verificar prerequisitos
check_prerequisites() {
    print_status "Verificando prerequisitos..."
    
    # Verificar Google Cloud SDK
    if ! command -v gcloud &> /dev/null; then
        print_error "Google Cloud SDK no está instalado"
        print_status "Instala con: curl https://sdk.cloud.google.com | bash"
        exit 1
    fi
    
    # Verificar Docker
    if ! command -v docker &> /dev/null; then
        print_error "Docker no está instalado"
        exit 1
    fi
    
    # Verificar autenticación de Google Cloud
    if ! gcloud auth list --filter=status:ACTIVE --format="value(account)" | grep -q "@"; then
        print_error "No hay autenticación activa de Google Cloud"
        print_status "Ejecuta: gcloud auth login"
        exit 1
    fi
    
    # Verificar archivos de configuración
    if [ ! -f "docker-compose.drone-gcp.yml" ]; then
        print_error "docker-compose.drone-gcp.yml no encontrado"
        exit 1
    fi
    
    print_success "Prerequisitos verificados correctamente"
}

# Configurar variables de Drone
setup_drone_variables() {
    print_status "Configurando variables de Drone..."
    
    if [ -z "$DRONE_GITHUB_CLIENT_ID" ]; then
        read -p "Ingresa tu GitHub Client ID: " DRONE_GITHUB_CLIENT_ID
    fi
    
    if [ -z "$DRONE_GITHUB_CLIENT_SECRET" ]; then
        read -s -p "Ingresa tu GitHub Client Secret: " DRONE_GITHUB_CLIENT_SECRET
        echo
    fi
    
    if [ -z "$DRONE_RPC_SECRET" ]; then
        DRONE_RPC_SECRET=$(openssl rand -hex 16)
        print_status "RPC Secret generado: $DRONE_RPC_SECRET"
    fi
    
    if [ -z "$DRONE_SECRET" ]; then
        DRONE_SECRET=$(openssl rand -hex 16)
        print_status "Drone Secret generado: $DRONE_SECRET"
    fi
    
    if [ -z "$DRONE_SERVER_HOST" ]; then
        read -p "Ingresa el host del servidor Drone (ej: drone.tudominio.com): " DRONE_SERVER_HOST
    fi
    
    if [ -z "$DRONE_ADMIN_USER" ]; then
        read -p "Ingresa el usuario administrador de Drone: " DRONE_ADMIN_USER
    fi
    
    # Crear archivo de variables de entorno
    cat > .env.drone << EOF
# Variables de Drone para Google Cloud
DRONE_GITHUB_CLIENT_ID=$DRONE_GITHUB_CLIENT_ID
DRONE_GITHUB_CLIENT_SECRET=$DRONE_GITHUB_CLIENT_SECRET
DRONE_RPC_SECRET=$DRONE_RPC_SECRET
DRONE_SERVER_HOST=$DRONE_SERVER_HOST
DRONE_SERVER_PROTO=$DRONE_SERVER_PROTO
DRONE_ADMIN_USER=$DRONE_ADMIN_USER
DRONE_SECRET=$DRONE_SECRET
DRONE_RPC_PROTO=$DRONE_SERVER_PROTO
DRONE_RPC_HOST=$DRONE_SERVER_HOST
EOF
    
    print_success "Variables de Drone configuradas"
}

# Configurar Google Cloud
setup_gcloud() {
    print_status "Configurando Google Cloud..."
    
    # Verificar autenticación actual
    if ! gcloud auth list --filter=status:ACTIVE --format="value(account)" | grep -q "@"; then
        print_error "No hay autenticación activa. Ejecuta: gcloud auth login"
        exit 1
    fi
    print_status "Usando autenticación actual: $(gcloud auth list --filter=status:ACTIVE --format='value(account)')"
    
    # Configurar el proyecto
    gcloud config set project "$PROJECT_ID"
    
    # Configurar la región y zona
    gcloud config set compute/region "$REGION"
    gcloud config set compute/zone "$ZONE"
    
    # Habilitar APIs necesarias
    print_status "Habilitando APIs de Google Cloud..."
    gcloud services enable compute.googleapis.com
    gcloud services enable container.googleapis.com
    gcloud services enable containerregistry.googleapis.com
    gcloud services enable artifactregistry.googleapis.com
    
    print_success "Google Cloud configurado correctamente"
}

# Crear instancia de Compute Engine
create_instance() {
    print_status "Creando instancia de Compute Engine..."
    
    # Verificar si la instancia ya existe
    if gcloud compute instances describe "$INSTANCE_NAME" --zone="$ZONE" &> /dev/null; then
        print_warning "La instancia $INSTANCE_NAME ya existe"
        read -p "¿Quieres continuar con la instancia existente? (y/N): " -n 1 -r
        echo
        if [[ ! $REPLY =~ ^[Yy]$ ]]; then
            print_error "Operación cancelada"
            exit 1
        fi
        return 0
    fi
    
    # Crear la instancia
    gcloud compute instances create "$INSTANCE_NAME" \
        --zone="$ZONE" \
        --machine-type="$MACHINE_TYPE" \
        --image-family=ubuntu-2204-lts \
        --image-project=ubuntu-os-cloud \
        --boot-disk-size="$DISK_SIZE" \
        --boot-disk-type=pd-standard \
        --tags=drone-server,http-server,https-server \
        --metadata=startup-script='#!/bin/bash
apt-get update
apt-get install -y docker.io docker-compose git curl
systemctl start docker
systemctl enable docker
usermod -aG docker ubuntu
curl -fsSL https://get.docker.com -o get-docker.sh
sh get-docker.sh'
    
    print_success "Instancia creada correctamente"
}

# Configurar firewall
setup_firewall() {
    print_status "Configurando reglas de firewall..."
    
    # Crear regla para Drone
    if ! gcloud compute firewall-rules describe drone-server &> /dev/null; then
        gcloud compute firewall-rules create drone-server \
            --allow tcp:8002,tcp:8003,tcp:443,tcp:9090 \
            --source-ranges=0.0.0.0/0 \
            --target-tags=drone-server \
            --description="Drone CI/CD server ports"
        
        print_success "Regla de firewall creada"
    else
        print_warning "Regla de firewall ya existe"
    fi
}

# Copiar archivos a la instancia
copy_files() {
    print_status "Copiando archivos a la instancia..."
    
    # Crear directorio temporal
    mkdir -p /tmp/drone-deploy
    
    # Copiar archivos necesarios
    cp docker-compose.drone-gcp.yml /tmp/drone-deploy/
    cp nginx/drone-gcp.conf /tmp/drone-deploy/
    cp prometheus.drone.yml /tmp/drone-deploy/
    cp .env.drone /tmp/drone-deploy/
    
    # Copiar a la instancia
    gcloud compute scp --recurse /tmp/drone-deploy/* "$INSTANCE_NAME":~/drone/ --zone="$ZONE"
    
    # Limpiar directorio temporal
    rm -rf /tmp/drone-deploy
    
    print_success "Archivos copiados correctamente"
}

# Desplegar Drone en la instancia
deploy_drone() {
    print_status "Desplegando Drone en la instancia..."
    
    # Ejecutar comandos en la instancia
    gcloud compute ssh "$INSTANCE_NAME" --zone="$ZONE" --command="
        cd ~/drone
        sudo docker-compose -f docker-compose.drone-gcp.yml down
        sudo docker-compose -f docker-compose.drone-gcp.yml up -d
        sudo docker-compose -f docker-compose.drone-gcp.yml ps
    "
    
    print_success "Drone desplegado correctamente"
}

# Obtener información de la instancia
get_instance_info() {
    print_status "Obteniendo información de la instancia..."
    
    # Obtener IP externa
    EXTERNAL_IP=$(gcloud compute instances describe "$INSTANCE_NAME" --zone="$ZONE" --format="get(networkInterfaces[0].accessConfigs[0].natIP)")
    
    echo ""
    echo "=================================================="
    print_success "🎉 ¡Drone CI/CD desplegado exitosamente!"
    echo "=================================================="
    echo ""
    echo "Información de acceso:"
    echo "  Instancia: $INSTANCE_NAME"
    echo "  IP Externa: $EXTERNAL_IP"
    echo "  Zona: $ZONE"
    echo ""
    echo "URLs de acceso:"
    echo "  Drone Server: http://$EXTERNAL_IP:8002"
    echo "  Drone UI: http://$EXTERNAL_IP:8003"
    echo "  Prometheus: http://$EXTERNAL_IP:9090"
    echo ""
    echo "Comandos útiles:"
    echo "  Conectar: gcloud compute ssh $INSTANCE_NAME --zone=$ZONE"
    echo "  Ver logs: gcloud compute ssh $INSTANCE_NAME --zone=$ZONE --command='cd ~/drone && sudo docker-compose -f docker-compose.drone-gcp.yml logs -f'"
    echo "  Reiniciar: gcloud compute ssh $INSTANCE_NAME --zone=$ZONE --command='cd ~/drone && sudo docker-compose -f docker-compose.drone-gcp.yml restart'"
    echo ""
    echo "=================================================="
}

# Función principal
main() {
    echo "🚀 Iniciando proceso de despliegue de Drone en Google Cloud..."
    echo ""
    
    check_prerequisites
    setup_drone_variables
    setup_gcloud
    create_instance
    setup_firewall
    copy_files
    deploy_drone
    get_instance_info
    
    echo ""
    print_success "🎉 ¡Despliegue completado exitosamente!"
    print_status "Drone CI/CD está ejecutándose en Google Cloud Platform"
}

# Ejecutar función principal
main "$@"
