# 🏥 Despliegue del Sistema Hospital en Google Cloud

Este documento explica cómo desplegar los contenedores del Sistema Hospital en Google Cloud Platform.

## 📋 Prerequisitos

- ✅ Google Cloud CLI instalado (`gcloud`)
- ✅ Docker instalado y funcionando
- ✅ Cuenta de servicio configurada con permisos adecuados
- ✅ Proyecto de Google Cloud creado (`hospital-470223`)

## 🔑 Configuración de Credenciales

### 1. Archivo de Credenciales
El archivo `hospital-credentials.json` ya está configurado con la cuenta de servicio:
- **Proyecto**: `hospital-470223`
- **Cuenta**: `hospital-deployer@hospital-470223.iam.gserviceaccount.com`
- **Roles**: Storage Admin, Artifact Registry Admin

### 2. Verificar Configuración
```bash
# Verificar que gcloud esté configurado
gcloud config list

# Verificar autenticación
gcloud auth list
```

## 🚀 Despliegue Automático

### Opción 1: Script Automático (Recomendado)
```bash
# Ejecutar el script de despliegue completo
./deploy-to-gcp.sh
```

Este script:
- ✅ Verifica prerequisitos
- ✅ Configura Google Cloud
- ✅ Construye imágenes Docker
- ✅ Sube contenedores a Google Cloud
- ✅ Muestra información de despliegue

### Opción 2: Despliegue Manual
```bash
# 1. Autenticar con Google Cloud
gcloud auth activate-service-account --key-file=hospital-credentials.json

# 2. Configurar proyecto
gcloud config set project hospital-470223

# 3. Configurar Docker
gcloud auth configure-docker

# 4. Construir frontend
docker build -f Dockerfile.frontend -t hospital-frontend:v1.0.0 .
docker tag hospital-frontend:v1.0.0 gcr.io/hospital-470223/hospital-frontend:v1.0.0

# 5. Construir backend (requiere JAR compilado)
docker build -f Dockerfile -t hospital-backend:v1.0.0 .
docker tag hospital-backend:v1.0.0 gcr.io/hospital-470223/hospital-backend:v1.0.0

# 6. Subir imágenes
docker push gcr.io/hospital-470223/hospital-frontend:v1.0.0
docker push gcr.io/hospital-470223/hospital-backend:v1.0.0
```

## 🐳 Ejecutar Contenedores

### Desde Google Cloud
```bash
# Frontend
docker run -p 80:80 gcr.io/hospital-470223/hospital-frontend:v1.0.0

# Backend
docker run -p 8080:8080 gcr.io/hospital-470223/hospital-backend:v1.0.0
```

### Con Docker Compose
```bash
# Usar el archivo que incluye imágenes de Google Cloud
docker-compose -f docker-compose.gcp.yml up -d
```

## 📊 Monitoreo y Logs

### Ver Logs de Contenedores
```bash
# Frontend
docker logs hospital-frontend-gcp

# Backend
docker logs hospital-backend-gcp
```

### Verificar Estado
```bash
# Estado de contenedores
docker ps -a

# Información de imágenes
docker images | grep hospital
```

## 🔧 Configuración Avanzada

### Variables de Entorno
- `NODE_ENV`: Entorno del frontend (dev/prod)
- `JAVA_OPTS`: Opciones de JVM para el backend
- `QUARKUS_PROFILE`: Perfil de Quarkus

### Puertos
- **Frontend**: 80 (HTTP)
- **Backend**: 8080 (HTTP)
- **Base de Datos**: 1521 (Oracle)

### Redes
- **Subnet**: 172.20.0.0/16
- **Driver**: bridge

## 🚨 Solución de Problemas

### Error: "No se pudo autenticar"
```bash
# Verificar archivo de credenciales
cat hospital-credentials.json

# Reautenticar
gcloud auth activate-service-account --key-file=hospital-credentials.json
```

### Error: "Proyecto no encontrado"
```bash
# Verificar proyecto configurado
gcloud config get-value project

# Configurar proyecto correcto
gcloud config set project hospital-470223
```

### Error: "Docker no puede autenticarse"
```bash
# Reconfigurar Docker
gcloud auth configure-docker

# Verificar configuración
cat ~/.docker/config.json
```

### Error: "JAR del backend no encontrado"
```bash
# Construir proyecto Java primero
cd backend
mvn clean package -DskipTests
cd ..
```

## 📈 Escalabilidad

### Kubernetes (GKE)
Para producción, considera usar Google Kubernetes Engine:
```bash
# Crear cluster GKE
gcloud container clusters create hospital-cluster \
  --zone=us-central1-a \
  --num-nodes=3 \
  --machine-type=e2-medium

# Desplegar con kubectl
kubectl apply -f k8s/
```

### Cloud Run
Para serverless:
```bash
# Desplegar frontend
gcloud run deploy hospital-frontend \
  --image gcr.io/hospital-470223/hospital-frontend:latest \
  --platform managed \
  --region us-central1 \
  --allow-unauthenticated
```

## 🔒 Seguridad

### Buenas Prácticas
- ✅ Usar cuentas de servicio con permisos mínimos
- ✅ Rotar claves regularmente
- ✅ Usar imágenes escaneadas
- ✅ Implementar health checks
- ✅ Configurar logs centralizados

### IAM Roles Recomendados
- **Storage Admin**: Para Container Registry
- **Artifact Registry Admin**: Para Artifact Registry
- **Cloud Build Editor**: Para builds automatizados

## 📞 Soporte

Si encuentras problemas:
1. Verifica los logs del script
2. Revisa la configuración de Google Cloud
3. Verifica permisos de la cuenta de servicio
4. Consulta la documentación oficial de Google Cloud

## 🎯 Próximos Pasos

- [ ] Configurar CI/CD con Cloud Build
- [ ] Implementar monitoreo con Cloud Monitoring
- [ ] Configurar backups automáticos
- [ ] Implementar blue-green deployments
- [ ] Configurar alertas y notificaciones

---

**¡Feliz despliegue! 🚀**

