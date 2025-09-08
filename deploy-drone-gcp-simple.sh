#!/bin/bash

# Script simplificado para desplegar Drone CI/CD en Google Cloud Platform
# Sin entrada interactiva - usa valores predefinidos

set -e

echo "🚀 Iniciando despliegue de Drone CI/CD en Google Cloud (modo simplificado)..."
echo "=================================================="

# Variables de configuración
PROJECT_ID="hospital-470223"
REGION="us-central1"
ZONE="us-central1-a"
INSTANCE_NAME="drone-ci-gcp"
MACHINE_TYPE="e2-standard-4"
DISK_SIZE="50GB"

# Variables de Drone predefinidas
DRONE_GITHUB_CLIENT_ID="Ov23liUkZPPY6NxDbi6j"
DRONE_GITHUB_CLIENT_SECRET="f09f9703350f6e04a8fdefa36c26f185415f5cc2"
DRONE_RPC_SECRET=$(openssl rand -hex 16)
DRONE_SERVER_HOST="34.10.223.20"
DRONE_SERVER_PROTO="http"
DRONE_ADMIN_USER="humbertovenavente"
DRONE_SECRET=$(openssl rand -hex 16)

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

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
    
    # Verificar que gcloud esté instalado
    if ! command -v gcloud &> /dev/null; then
        print_error "gcloud CLI no está instalado"
        exit 1
    fi
    
    # Verificar que docker esté instalado
    if ! command -v docker &> /dev/null; then
        print_error "Docker no está instalado"
        exit 1
    fi
    
    print_success "Prerequisitos verificados correctamente"
}

# Configurar variables de Drone
setup_drone_vars() {
    print_status "Configurando variables de Drone..."
    
    print_status "RPC Secret generado: $DRONE_RPC_SECRET"
    print_status "Drone Secret generado: $DRONE_SECRET"
    print_status "Server Host: $DRONE_SERVER_HOST"
    print_status "Admin User: $DRONE_ADMIN_USER"
    
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
    
    # Configurar el proyecto
    gcloud config set project "$PROJECT_ID"
    
    # Configurar la región y zona
    gcloud config set compute/region "$REGION"
    gcloud config set compute/zone "$ZONE"
    
    # Habilitar APIs necesarias
    print_status "Habilitando APIs necesarias..."
    gcloud services enable compute.googleapis.com
    gcloud services enable container.googleapis.com
    gcloud services enable cloudbuild.googleapis.com
    
    print_success "Google Cloud configurado correctamente"
}

# Crear instancia de Compute Engine
create_instance() {
    print_status "Creando instancia de Compute Engine..."
    
    # Verificar si la instancia ya existe
    if gcloud compute instances describe "$INSTANCE_NAME" --zone="$ZONE" &> /dev/null; then
        print_warning "La instancia $INSTANCE_NAME ya existe"
        return 0
    fi
    
    # Crear la instancia
    gcloud compute instances create "$INSTANCE_NAME" \
        --zone="$ZONE" \
        --machine-type="$MACHINE_TYPE" \
        --boot-disk-size="$DISK_SIZE" \
        --boot-disk-type=pd-standard \
        --image-family=ubuntu-2004-lts \
        --image-project=ubuntu-os-cloud \
        --tags=drone-server,http-server,https-server \
        --metadata=startup-script='#!/bin/bash
apt-get update
apt-get install -y docker.io docker-compose
systemctl start docker
systemctl enable docker
usermod -aG docker ubuntu'
    
    print_success "Instancia creada: $INSTANCE_NAME"
}

# Configurar firewall
setup_firewall() {
    print_status "Configurando reglas de firewall..."
    
    # Crear regla para Drone (puerto 8080)
    gcloud compute firewall-rules create allow-drone \
        --allow tcp:8080 \
        --source-ranges 0.0.0.0/0 \
        --target-tags drone-server \
        --description "Allow Drone CI access" || true
    
    # Crear regla para SSH (puerto 22)
    gcloud compute firewall-rules create allow-ssh \
        --allow tcp:22 \
        --source-ranges 0.0.0.0/0 \
        --target-tags drone-server \
        --description "Allow SSH access" || true
    
    print_success "Reglas de firewall configuradas"
}

# Obtener IP de la instancia
get_instance_ip() {
    print_status "Obteniendo IP de la instancia..."
    
    INSTANCE_IP=$(gcloud compute instances describe "$INSTANCE_NAME" \
        --zone="$ZONE" \
        --format='get(networkInterfaces[0].accessConfigs[0].natIP)')
    
    print_success "IP de la instancia: $INSTANCE_IP"
}

# Crear archivo docker-compose para GCP
create_docker_compose() {
    print_status "Creando archivo docker-compose para GCP..."
    
    cat > docker-compose.gcp.yml << EOF
version: '3.8'

services:
  # Drone Server
  drone-server:
    image: drone/drone:2
    container_name: drone-server
    ports:
      - "8080:80"
    volumes:
      - drone_data:/data
      - /var/run/docker.sock:/var/run/docker.sock
    environment:
      # Configuración de Drone
      - DRONE_GITHUB_CLIENT_ID=$DRONE_GITHUB_CLIENT_ID
      - DRONE_GITHUB_CLIENT_SECRET=$DRONE_GITHUB_CLIENT_SECRET
      - DRONE_RPC_SECRET=$DRONE_RPC_SECRET
      - DRONE_SERVER_HOST=$DRONE_SERVER_HOST:8080
      - DRONE_SERVER_PROTO=$DRONE_SERVER_PROTO
      - DRONE_USER_CREATE=username:$DRONE_ADMIN_USER,admin:true
      - DRONE_LOGS_DEBUG=true
      - DRONE_LOGS_TRACE=true
      # Configuración de Docker
      - DRONE_DOCKER_ENABLED=true
      - DRONE_DOCKER_HOST=unix:///var/run/docker.sock
      # Configuración de ambientes
      - DRONE_ENVIRONMENT=dev,qa,prod
    restart: unless-stopped
    networks:
      - drone-network

  # Drone Runner
  drone-runner:
    image: drone/drone-runner-docker:1
    container_name: drone-runner
    volumes:
      - /var/run/docker.sock:/var/run/docker.sock
    environment:
      - DRONE_RPC_PROTO=$DRONE_SERVER_PROTO
      - DRONE_RPC_HOST=drone-server:80
      - DRONE_RPC_SECRET=$DRONE_RPC_SECRET
      - DRONE_DEBUG=true
      - DRONE_TRACE=true
      # Configuración de Docker
      - DRONE_DOCKER_ENABLED=true
      - DRONE_DOCKER_HOST=unix:///var/run/docker.sock
      # Límites de recursos
      - DRONE_RUNNER_CAPACITY=3
      - DRONE_RUNNER_LABELS=os:linux,arch:amd64
    restart: unless-stopped
    depends_on:
      - drone-server
    networks:
      - drone-network

volumes:
  drone_data:
    driver: local

networks:
  drone-network:
    driver: bridge
EOF
    
    print_success "Archivo docker-compose.gcp.yml creado"
}

# Mostrar información final
show_info() {
    print_success "Despliegue completado exitosamente!"
    echo ""
    echo "🎉 DRONE CI/CD DESPLEGADO EN GOOGLE CLOUD"
    echo "=========================================="
    echo ""
    echo "📋 Información del despliegue:"
    echo "  - Proyecto: $PROJECT_ID"
    echo "  - Instancia: $INSTANCE_NAME"
    echo "  - IP: $INSTANCE_IP"
    echo "  - Región: $REGION"
    echo "  - Zona: $ZONE"
    echo ""
    echo "🔧 Configuración de Drone:"
    echo "  - Server Host: $DRONE_SERVER_HOST:8080"
    echo "  - Admin User: $DRONE_ADMIN_USER"
    echo "  - GitHub Client ID: $DRONE_GITHUB_CLIENT_ID"
    echo ""
    echo "🌐 Acceso:"
    echo "  - Drone UI: http://$INSTANCE_IP:8080"
    echo "  - SSH: gcloud compute ssh $INSTANCE_NAME --zone=$ZONE"
    echo ""
    echo "📋 Próximos pasos:"
    echo "  1. Conectarse a la instancia: gcloud compute ssh $INSTANCE_NAME --zone=$ZONE"
    echo "  2. Subir archivos: gcloud compute scp docker-compose.gcp.yml $INSTANCE_NAME:~/ --zone=$ZONE"
    echo "  3. Ejecutar: docker-compose -f docker-compose.gcp.yml up -d"
    echo "  4. Acceder a: http://$INSTANCE_IP:8080"
    echo ""
}

# Función principal
main() {
    check_prerequisites
    setup_drone_vars
    setup_gcloud
    create_instance
    setup_firewall
    get_instance_ip
    create_docker_compose
    show_info
}

# Ejecutar función principal
main "$@"


