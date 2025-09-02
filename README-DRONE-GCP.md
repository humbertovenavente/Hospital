# Despliegue de Drone CI/CD en Google Cloud Platform

Este documento explica cómo desplegar Drone CI/CD en Google Cloud Platform, evitando los problemas de webhooks que pueden ocurrir en entornos locales.

## 🎯 Objetivo

Desplegar Drone CI/CD en una instancia de Google Cloud Platform para:
- Evitar problemas de webhooks locales
- Tener un entorno estable y escalable
- Integrar con GitHub de forma confiable
- Monitorear el sistema con Prometheus

## 🏗️ Arquitectura

```
┌─────────────────────────────────────────────────────────────┐
│                    Google Cloud Platform                    │
│                                                             │
│  ┌─────────────────────────────────────────────────────┐   │
│  │              Instancia Compute Engine               │   │
│  │                                                     │   │
│  │  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐ │   │
│  │  │ Drone Server│  │ Drone Runner│  │   Nginx     │ │   │
│  │  │ Puerto 80   │  │ Puerto 3000 │  │ Puerto 8080 │ │   │
│  │  └─────────────┘  └─────────────┘  └─────────────┘ │   │
│  │                                                     │   │
│  │  ┌─────────────┐  ┌─────────────┐                  │   │
│  │  │ Prometheus  │  │   Docker    │                  │   │
│  │  │ Puerto 9090 │  │   Engine    │                  │   │
│  │  └─────────────┘  └─────────────┘                  │   │
│  └─────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
```

## 📋 Requisitos Previos

1. **Google Cloud SDK** instalado y configurado
2. **Cuenta de Google Cloud** con facturación habilitada
3. **Permisos** para crear instancias de Compute Engine
4. **GitHub OAuth App** configurado
5. **Archivo de credenciales** de Google Cloud (`hospital-credentials.json`)

## 🔧 Configuración de GitHub OAuth App

### 1. Crear OAuth App en GitHub

1. Ve a GitHub → Settings → Developer settings → OAuth Apps
2. Haz clic en "New OAuth App"
3. Completa los campos:
   - **Application name**: `Drone CI/CD - Hospital`
   - **Homepage URL**: `https://tu-dominio.com`
   - **Authorization callback URL**: `http://104.197.237.11:8002/login`
4. Guarda el **Client ID** y **Client Secret**

### 2. Configurar Webhooks (Opcional)

Si quieres usar webhooks (recomendado para producción):
1. Ve a tu repositorio → Settings → Webhooks
2. Agrega webhook con URL: `http://104.197.237.11:8002/hook`
3. Selecciona eventos: `Push`, `Pull Request`, `Release`

## 🚀 Despliegue Automático

### 1. Ejecutar el Script de Despliegue

```bash
# Dar permisos de ejecución
chmod +x deploy-drone-gcp.sh

# Ejecutar el despliegue
./deploy-drone-gcp.sh
```

### 2. Seguir las Instrucciones

El script te pedirá:
- GitHub Client ID
- GitHub Client Secret
- Host del servidor Drone
- Usuario administrador

## 🔧 Despliegue Manual

Si prefieres hacerlo paso a paso:

### 1. Configurar Google Cloud

```bash
# Autenticar
gcloud auth activate-service-account --key-file="hospital-credentials.json"

# Configurar proyecto
gcloud config set project hospital-470223
gcloud config set compute/region us-central1
gcloud config set compute/zone us-central1-a

# Habilitar APIs
gcloud services enable compute.googleapis.com
gcloud services enable container.googleapis.com
```

### 2. Crear Instancia

```bash
# Crear instancia
gcloud compute instances create drone-ci-gcp \
    --zone=us-central1-a \
    --machine-type=e2-standard-4 \
    --image-family=ubuntu-2004-lts \
    --image-project=ubuntu-os-cloud \
    --boot-disk-size=50GB \
    --tags=drone-server,http-server,https-server
```

### 3. Configurar Firewall

```bash
# Crear regla de firewall
gcloud compute firewall-rules create drone-server \
    --allow tcp:80,tcp:443,tcp:8080,tcp:9090 \
    --source-ranges=0.0.0.0/0 \
    --target-tags=drone-server \
    --description="Drone CI/CD server ports"
```

### 4. Copiar Archivos

```bash
# Copiar archivos a la instancia
gcloud compute scp --recurse . drone-ci-gcp:~/drone/ --zone=us-central1-a
```

### 5. Desplegar Drone

```bash
# Conectar a la instancia
gcloud compute ssh drone-ci-gcp --zone=us-central1-a

# En la instancia, ejecutar:
cd ~/drone
sudo docker-compose -f docker-compose.drone-gcp.yml up -d
```

## 📁 Estructura de Archivos

```
Hospital/
├── docker-compose.drone-gcp.yml    # Configuración de Drone para GCP
├── nginx/
│   └── drone-gcp.conf              # Configuración de Nginx
├── prometheus.drone.yml            # Configuración de Prometheus
├── deploy-drone-gcp.sh             # Script de despliegue
├── .env.drone                      # Variables de entorno (generado)
└── README-DRONE-GCP.md             # Este archivo
```

## 🔐 Variables de Entorno

El archivo `.env.drone` contiene:

```bash
# Variables de Drone para Google Cloud
DRONE_GITHUB_CLIENT_ID=tu_client_id
DRONE_GITHUB_CLIENT_SECRET=tu_client_secret
DRONE_RPC_SECRET=secreto_generado_automaticamente
DRONE_SERVER_HOST=tu-dominio.com
DRONE_SERVER_PROTO=https
DRONE_ADMIN_USER=tu_usuario_github
DRONE_SECRET=secreto_generado_automaticamente
DRONE_RPC_PROTO=https
DRONE_RPC_HOST=tu-dominio.com
```

## 🌐 Acceso a los Servicios

Una vez desplegado, tendrás acceso a:

- **Drone Server**: `http://IP_EXTERNA:8002`
- **Drone UI**: `http://IP_EXTERNA:8003`
- **Prometheus**: `http://IP_EXTERNA:9090`

## 📊 Monitoreo

### Prometheus

Prometheus está configurado para monitorear:
- Drone Server
- Drone Runner
- Nginx
- Métricas del sistema

### Logs

```bash
# Ver logs de Drone
gcloud compute ssh drone-ci-gcp --zone=us-central1-a --command="cd ~/drone && sudo docker-compose -f docker-compose.drone-gcp.yml logs -f"

# Ver logs específicos
gcloud compute ssh drone-ci-gcp --zone=us-central1-a --command="cd ~/drone && sudo docker-compose -f docker-compose.drone-gcp.yml logs -f drone-server"
```

## 🔄 Comandos Útiles

### Gestión de la Instancia

```bash
# Ver estado de la instancia
gcloud compute instances describe drone-ci-gcp --zone=us-central1-a

# Reiniciar instancia
gcloud compute instances reset drone-ci-gcp --zone=us-central1-a

# Detener instancia
gcloud compute instances stop drone-ci-gcp --zone=us-central1-a

# Iniciar instancia
gcloud compute instances start drone-ci-gcp --zone=us-central1-a
```

### Gestión de Drone

```bash
# Conectar a la instancia
gcloud compute ssh drone-ci-gcp --zone=us-central1-a

# En la instancia:
cd ~/drone

# Ver estado de contenedores
sudo docker-compose -f docker-compose.drone-gcp.yml ps

# Reiniciar Drone
sudo docker-compose -f docker-compose.drone-gcp.yml restart

# Actualizar Drone
sudo docker-compose -f docker-compose.drone-gcp.yml pull
sudo docker-compose -f docker-compose.drone-gcp.yml up -d

# Ver logs
sudo docker-compose -f docker-compose.drone-gcp.yml logs -f
```

## 🛠️ Solución de Problemas

### Problemas Comunes

1. **Error de autenticación GitHub**
   - Verificar Client ID y Client Secret
   - Verificar callback URL en GitHub OAuth App

2. **Error de conexión a la instancia**
   - Verificar que la instancia esté ejecutándose
   - Verificar reglas de firewall

3. **Drone no inicia**
   - Verificar logs: `sudo docker-compose -f docker-compose.drone-gcp.yml logs`
   - Verificar variables de entorno en `.env.drone`

4. **Problemas de webhooks**
   - Verificar que la URL del webhook sea accesible
   - Verificar configuración de GitHub

### Logs de Depuración

```bash
# Ver logs detallados
gcloud compute ssh drone-ci-gcp --zone=us-central1-a --command="cd ~/drone && sudo docker-compose -f docker-compose.drone-gcp.yml logs --tail=100"

# Ver logs del sistema
gcloud compute ssh drone-ci-gcp --zone=us-central1-a --command="journalctl -u docker"

# Ver uso de recursos
gcloud compute ssh drone-ci-gcp --zone=us-central1-a --command="htop"
```

## 💰 Costos Estimados

### Google Cloud Platform

- **Instancia e2-standard-4**: ~$0.15/hora (~$110/mes)
- **Disco de 50GB**: ~$0.08/GB/mes (~$4/mes)
- **Transferencia de datos**: Variable según uso
- **Total estimado**: ~$120-150/mes

### Optimización de Costos

```bash
# Detener instancia cuando no se use
gcloud compute instances stop drone-ci-gcp --zone=us-central1-a

# Iniciar cuando se necesite
gcloud compute instances start drone-ci-gcp --zone=us-central1-a

# Usar instancias preemptibles para desarrollo (más baratas)
gcloud compute instances create drone-ci-dev \
    --preemptible \
    --zone=us-central1-a \
    --machine-type=e2-standard-2
```

## 🔒 Seguridad

### Configuraciones Recomendadas

1. **Firewall**: Solo abrir puertos necesarios
2. **HTTPS**: Configurar certificados SSL
3. **Autenticación**: Usar GitHub OAuth
4. **Secrets**: Rotar secretos regularmente

### Configurar HTTPS

```bash
# Instalar Certbot
sudo apt-get install certbot

# Obtener certificado
sudo certbot certonly --standalone -d tu-dominio.com

# Configurar renovación automática
sudo crontab -e
# Agregar: 0 12 * * * /usr/bin/certbot renew --quiet
```

## 📈 Escalabilidad

### Aumentar Capacidad

```bash
# Cambiar tipo de máquina
gcloud compute instances set-machine-type drone-ci-gcp \
    --machine-type=e2-standard-8 \
    --zone=us-central1-a
```

### Múltiples Runners

Para aumentar la capacidad de procesamiento:

```bash
# En la instancia, editar docker-compose.drone-gcp.yml
# Cambiar DRONE_RUNNER_CAPACITY de 5 a 10
sudo docker-compose -f docker-compose.drone-gcp.yml up -d
```

## 🎯 Próximos Pasos

1. **Configurar dominios personalizados**
2. **Implementar backup automático**
3. **Configurar alertas de monitoreo**
4. **Integrar con otros servicios de GCP**
5. **Implementar CI/CD para el propio Drone**

## 📞 Soporte

Para problemas específicos:

1. **Google Cloud**: [Documentación oficial](https://cloud.google.com/docs)
2. **Drone CI**: [Documentación oficial](https://docs.drone.io/)
3. **Docker Compose**: [Documentación oficial](https://docs.docker.com/compose/)

## 🔄 Flujo de Trabajo Recomendado

### 1. Desarrollo Local
```bash
# Probar cambios localmente
docker-compose -f docker-compose.drone.yml up -d
```

### 2. Despliegue en GCP
```bash
# Desplegar en Google Cloud
./deploy-drone-gcp.sh
```

### 3. Verificación
```bash
# Verificar que todo funcione
gcloud compute ssh drone-ci-gcp --zone=us-central1-a --command="cd ~/drone && sudo docker-compose -f docker-compose.drone-gcp.yml ps"
```

### 4. Monitoreo
```bash
# Monitorear logs y métricas
# Acceder a Prometheus: http://IP_EXTERNA:9090
```
