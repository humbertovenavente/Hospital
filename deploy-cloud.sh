#!/bin/bash

# Script de despliegue en Google Cloud Platform para 3 contenedores Oracle
set -e

# Configuración
PROJECT_NAME="hospital-470223"
REGION="us-central1"
ZONE="us-central1-a"
MACHINE_TYPE="e2-standard-4"
DISK_SIZE="200GB"

log() {
    echo "[$(date +'%Y-%m-%d %H:%M:%S')] $1"
}

# Función para desplegar en Google Cloud Platform
deploy_gcp() {
    log "Desplegando en Google Cloud Platform..."
    
    # Verificar si gcloud está instalado
    if ! command -v gcloud &> /dev/null; then
        log "ERROR: gcloud CLI no está instalado"
        log "Instala gcloud desde: https://cloud.google.com/sdk/docs/install"
        exit 1
    fi
    
    # Usar proyecto existente
    log "Usando proyecto existente: $PROJECT_NAME"
    gcloud config set project "$PROJECT_NAME"
    
    # Habilitar APIs necesarias
    log "Habilitando APIs necesarias..."
    gcloud services enable compute.googleapis.com
    gcloud services enable container.googleapis.com
    
    # Crear instancia de Compute Engine
    log "Creando instancia de Compute Engine..."
    gcloud compute instances create oracle-multi-db \
        --zone="$ZONE" \
        --machine-type="$MACHINE_TYPE" \
        --image-family=ubuntu-2204-lts \
        --image-project=ubuntu-os-cloud \
        --boot-disk-size="$DISK_SIZE" \
        --tags=http-server,https-server \
        --metadata=startup-script="
            #!/bin/bash
            apt-get update
            apt-get install -y docker.io docker-compose
            systemctl start docker
            systemctl enable docker
            usermod -aG docker \$USER
        "
    
    # Esperar un momento para que la instancia esté completamente lista
    log "Esperando a que la instancia esté lista..."
    sleep 30
    
    # Copiar archivos del proyecto
    log "Copiando archivos del proyecto..."
    gcloud compute scp --recurse . oracle-multi-db:~/oracle-project --zone="$ZONE"
    
    # Copiar la imagen Docker
    log "Copiando imagen Docker..."
    gcloud compute scp /home/jose/Downloads/oracle_xe_con_datos/oracle_xe_con_datos.tar oracle-multi-db:~/oracle-project/ --zone="$ZONE"
    
    # Ejecutar comandos en la instancia
    log "Configurando Docker y ejecutando contenedores..."
    gcloud compute ssh oracle-multi-db --zone="$ZONE" --command="
        cd ~/oracle-project
        chmod +x load-image.sh
        chmod +x scripts/setup/extract_data.sh
        chmod +x scripts/startup/*.sh
        
        # Cargar la imagen Docker
        ./load-image.sh
        
        # Extraer datos si es necesario
        ./scripts/setup/extract_data.sh
        
        # Ejecutar contenedores
        docker-compose up -d
    "
    
    log "Despliegue en GCP completado exitosamente"
    log "Instancia: oracle-multi-db en zona $ZONE"
    log "Puertos: 1521, 1522, 1523"
    log ""
    log "Para conectarte a la instancia:"
    log "gcloud compute ssh oracle-multi-db --zone=$ZONE"
    log ""
    log "Para ver los logs de los contenedores:"
    log "docker-compose logs -f"
}

# Función para mostrar ayuda
show_help() {
    echo "Uso: $0 [COMANDO]"
    echo ""
    echo "Comandos disponibles:"
    echo "  deploy  - Desplegar en Google Cloud Platform"
    echo "  help    - Mostrar esta ayuda"
    echo ""
    echo "Ejemplos:"
    echo "  $0 deploy"
    echo "  $0 help"
}

# Función principal
main() {
    case "${1:-}" in
        deploy)
            deploy_gcp
            ;;
        help|--help|-h)
            show_help
            ;;
        *)
            show_help
            exit 1
            ;;
    esac
}

# Ejecutar función principal
main "$@"
