# Despliegue en Google Cloud Platform - 3 Contenedores Oracle

Este proyecto despliega 3 contenedores Docker de Oracle Database en Google Cloud Platform, cada uno con su propia base de datos independiente, **usando tu imagen Docker existente** (`oracle_xe_con_datos.tar`).

## Arquitectura

```
┌─────────────────────────────────────────────────────────────┐
│                    Google Cloud Platform                    │
│                                                             │
│  ┌─────────────────────────────────────────────────────┐   │
│  │              Instancia Compute Engine               │   │
│  │                                                     │   │
│  │  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐ │   │
│  │  │ Oracle DB1  │  │ Oracle DB2  │  │ Oracle DB3  │ │   │
│  │  │ Puerto 1521 │  │ Puerto 1522 │  │ Puerto 1523 │ │   │
│  │  │ SID: DB1    │  │ SID: DB2    │  │ SID: DB3    │ │   │
│  │  └─────────────┘  └─────────────┘  └─────────────┘ │   │
│  └─────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
```

## Requisitos Previos

1. **Google Cloud SDK** instalado y configurado
2. **Cuenta de Google Cloud** con facturación habilitada
3. **Permisos** para crear instancias de Compute Engine
4. **Imagen Docker** Oracle en `/home/jose/Downloads/oracle_xe_con_datos/oracle_xe_con_datos.tar`

## Instalación del Google Cloud SDK

```bash
# Descargar e instalar Google Cloud SDK
curl https://sdk.cloud.google.com | bash
exec -l $SHELL

# Inicializar y autenticar
gcloud init
gcloud auth login
```

## Configuración

### 1. Variables de Entorno (opcional)

Puedes modificar las siguientes variables en `deploy-cloud.sh`:

```bash
PROJECT_NAME="oracle-multi-db"    # Nombre del proyecto GCP
REGION="us-central1"              # Región de GCP
ZONE="us-central1-a"              # Zona específica
MACHINE_TYPE="e2-standard-4"      # Tipo de máquina
DISK_SIZE="50GB"                  # Tamaño del disco
```

### 2. Configurar el Proyecto

```bash
# Verificar el proyecto actual
gcloud config get-value project

# Cambiar a tu proyecto existente (si ya tienes uno)
gcloud config set project TU_PROYECTO_EXISTENTE
```

## Uso Local (Antes de subir a la nube)

### 1. Cargar la Imagen Docker

```bash
# Cargar tu imagen Oracle existente
./load-image.sh
```

### 2. Ejecutar Contenedores Localmente

```bash
# Iniciar los 3 contenedores
./run-local.sh start

# Ver estado
./run-local.sh status

# Detener contenedores
./run-local.sh stop
```

### 3. Verificar Funcionamiento

```bash
# Ver contenedores en ejecución
docker-compose ps

# Ver logs
docker-compose logs -f

# Conectar a una base de datos
sqlplus system/Oracle123@localhost:1521/DB1
```

## Despliegue en Google Cloud

### Despliegue Automático

```bash
# Dar permisos de ejecución
chmod +x deploy-cloud.sh

# Ejecutar el despliegue
./deploy-cloud.sh deploy
```

### Despliegue Manual

Si prefieres hacerlo paso a paso:

```bash
# 1. Crear proyecto (opcional)
gcloud projects create oracle-multi-db --name="Oracle Multi Database"

# 2. Configurar proyecto
gcloud config set project oracle-multi-db

# 3. Habilitar APIs
gcloud services enable compute.googleapis.com
gcloud services enable container.googleapis.com

# 4. Crear instancia
gcloud compute instances create oracle-multi-db \
    --zone=us-central1-a \
    --machine-type=e2-standard-4 \
    --image-family=ubuntu-2004-lts \
    --image-project=ubuntu-os-cloud \
    --boot-disk-size=50GB

# 5. Copiar archivos del proyecto
gcloud compute scp --recurse . oracle-multi-db:~/oracle-project --zone=us-central1-a

# 6. Copiar imagen Docker
gcloud compute scp /home/jose/Downloads/oracle_xe_con_datos/oracle_xe_con_datos.tar oracle-multi-db:~/oracle-project/ --zone=us-central1-a

# 7. Conectar y ejecutar
gcloud compute ssh oracle-multi-db --zone=us-central1-a
cd ~/oracle-project
chmod +x load-image.sh
./load-image.sh
docker-compose up -d
```

## Estructura de Archivos

```
oracle-multi-db/
├── docker-compose.yml           # Orquestación de contenedores
├── load-image.sh                # Script para cargar imagen Docker
├── run-local.sh                 # Script para ejecutar localmente
├── deploy-cloud.sh              # Script de despliegue en GCP
├── scripts/
│   ├── setup/
│   │   └── extract_data.sh      # Extracción de datos (opcional)
│   └── startup/
│       ├── runOracle.sh         # Script de inicio (opcional)
│       └── healthcheck.sql      # Verificación de salud
└── data/
    ├── db1/                     # Datos para DB1
    ├── db2/                     # Datos para DB2
    └── db3/                     # Datos para DB3
```

## Acceso a las Bases de Datos

### Desde la Instancia Local

```bash
# Verificar estado de contenedores
./run-local.sh status

# Ver logs
docker-compose logs -f oracle-db1
docker-compose logs -f oracle-db2
docker-compose logs -f oracle-db3
```

### Conexión a las Bases de Datos

```bash
# DB1 (Puerto 1521)
sqlplus system/Oracle123@localhost:1521/DB1

# DB2 (Puerto 1522)
sqlplus system/Oracle123@localhost:1522/DB2

# DB3 (Puerto 1523)
sqlplus system/Oracle123@localhost:1523/DB3
```

### Desde la Instancia GCP

```bash
# Conectar a la instancia GCP
gcloud compute ssh oracle-multi-db --zone=us-central1-a

# Verificar estado de contenedores
docker-compose ps

# Ver logs
docker-compose logs -f oracle-db1
docker-compose logs -f oracle-db2
docker-compose logs -f oracle-db3
```

## Monitoreo y Mantenimiento

### Verificar Estado de Salud

```bash
# Verificar healthcheck de todos los contenedores
docker-compose ps

# Ver logs en tiempo real
docker-compose logs -f

# Ver logs de un contenedor específico
docker-compose logs -f oracle-db1
```

### Backup y Restauración

```bash
# Crear backup de una base de datos
docker exec oracle-db1 expdp system/Oracle123@DB1 \
    directory=DATA_PUMP_DIR \
    dumpfile=backup_$(date +%Y%m%d).dmp \
    schemas=TU_SCHEMA

# Restaurar desde backup
docker exec oracle-db1 impdp system/Oracle123@DB1 \
    directory=DATA_PUMP_DIR \
    dumpfile=backup_20241201.dmp \
    schemas=TU_SCHEMA
```

## Solución de Problemas

### Problemas Comunes

1. **Error de permisos**: Verificar que el usuario tenga permisos de Compute Engine
2. **Puerto ocupado**: Verificar que los puertos 1521-1523 estén disponibles
3. **Memoria insuficiente**: Aumentar el tipo de máquina en `deploy-cloud.sh`
4. **Disco lleno**: Aumentar el tamaño del disco en `deploy-cloud.sh`
5. **Imagen no encontrada**: Ejecutar `./load-image.sh` antes de `docker-compose up`

### Logs de Depuración

```bash
# Ver logs detallados de Docker
docker-compose logs --tail=100 oracle-db1

# Ver logs del sistema (en GCP)
gcloud compute ssh oracle-multi-db --zone=us-central1-a --command="journalctl -u docker"

# Ver uso de recursos (en GCP)
gcloud compute ssh oracle-multi-db --zone=us-central1-a --command="htop"
```

## Costos Estimados

### Google Cloud Platform

- **Instancia e2-standard-4**: ~$0.15/hora (~$110/mes)
- **Disco de 50GB**: ~$0.08/GB/mes (~$4/mes)
- **Transferencia de datos**: Variable según uso
- **Total estimado**: ~$120-150/mes

### Optimización de Costos

```bash
# Detener instancia cuando no se use
gcloud compute instances stop oracle-multi-db --zone=us-central1-a

# Iniciar cuando se necesite
gcloud compute instances start oracle-multi-db --zone=us-central1-a

# Usar instancias preemptibles para desarrollo (más baratas)
gcloud compute instances create oracle-multi-db-dev \
    --preemptible \
    --zone=us-central1-a \
    --machine-type=e2-standard-2
```

## Seguridad

### Configuraciones Recomendadas

1. **Firewall**: Solo abrir puertos necesarios (1521-1523)
2. **Contraseñas**: Cambiar `Oracle123` por contraseñas seguras
3. **Redes**: Usar VPC privada para bases de datos sensibles
4. **Backup**: Configurar backup automático en Cloud Storage

### Configurar Firewall

```bash
# Crear regla de firewall para Oracle
gcloud compute firewall-rules create oracle-db \
    --allow tcp:1521-1523 \
    --source-ranges=0.0.0.0/0 \
    --description="Oracle Database ports"

# Restringir a IPs específicas (recomendado)
gcloud compute firewall-rules create oracle-db-secure \
    --allow tcp:1521-1523 \
    --source-ranges=TU_IP_PUBLICA/32 \
    --description="Oracle Database ports (restricted)"
```

## Comandos Útiles

```bash
# Ver instancias en ejecución
gcloud compute instances list

# Ver información de la instancia
gcloud compute instances describe oracle-multi-db --zone=us-central1-a

# Reiniciar instancia
gcloud compute instances reset oracle-multi-db --zone=us-central1-a

# Eliminar instancia (¡CUIDADO!)
gcloud compute instances delete oracle-multi-db --zone=us-central1-a

# Ver logs de la instancia
gcloud compute instances get-serial-port-output oracle-multi-db --zone=us-central1-a
```

## Flujo de Trabajo Recomendado

### 1. Prueba Local
```bash
# Cargar imagen y probar localmente
./load-image.sh
./run-local.sh start
./run-local.sh status
```

### 2. Despliegue en GCP
```bash
# Una vez que funcione localmente, desplegar en la nube
./deploy-cloud.sh deploy
```

### 3. Verificación en GCP
```bash
# Conectar y verificar
gcloud compute ssh oracle-multi-db --zone=us-central1-a
docker-compose ps
```

## Soporte

Para problemas específicos:

1. **Google Cloud**: [Documentación oficial](https://cloud.google.com/docs)
2. **Oracle Docker**: [Documentación oficial](https://docs.oracle.com/en/database/oracle/oracle-database/19/multi/)
3. **Docker Compose**: [Documentación oficial](https://docs.docker.com/compose/)

## Próximos Pasos

1. **Monitoreo**: Configurar Cloud Monitoring para métricas de base de datos
2. **Backup**: Configurar backup automático en Cloud Storage
3. **Escalabilidad**: Implementar balanceador de carga si es necesario
4. **Seguridad**: Configurar VPC privada y Cloud Armor
