# Pipeline CI/CD - Sistema Hospital

Este documento describe la configuración del pipeline de CI/CD para el sistema hospital utilizando GitHub, Jenkins y Docker.

## 🏗️ Arquitectura del Pipeline

### Ambientes
- **Development**: Se actualiza tras merge al branch `dev`
- **QA/UAT**: Se actualiza tras merge al branch `QA`
- **Production**: Se actualiza tras merge al branch `dev`

### Base de Datos por Ambiente
- **Development**: `C##PROYECTO`
- **QA**: `C##HOSPITAL2`
- **Production**: `C##HOSPITAL3`

## 🚀 Configuración del Pipeline

### 1. Jenkins Pipeline

El archivo `Jenkinsfile` contiene la configuración del pipeline que incluye:

- **Checkout**: Descarga del código desde GitHub
- **Build Backend**: Compilación del backend Java con Maven
- **Test Backend**: Ejecución de pruebas unitarias
- **Build Frontend**: Construcción del frontend Vue.js
- **Build Docker Images**: Creación de imágenes Docker
- **Deploy**: Despliegue automático según la rama

### 2. GitHub Actions

El archivo `.github/workflows/ci-cd.yml` proporciona:

- Ejecución de pruebas en cada push/PR
- Construcción de imágenes Docker
- Despliegue automático según la rama

### 3. Docker Configuration

#### Dockerfiles
- `Dockerfile`: Para el backend Java/Quarkus
- `Dockerfile.frontend`: Para el frontend Vue.js

#### Docker Compose
- `docker-compose.yml`: Configuración local/desarrollo
- `docker-compose.qa.yml`: Configuración para QA
- `docker-compose.prod.yml`: Configuración para producción

## 📋 Scripts de Despliegue

### Desarrollo
```bash
chmod +x deploy-dev.sh
./deploy-dev.sh
```

### QA
```bash
chmod +x deploy-qa.sh
./deploy-qa.sh
```

### Producción
```bash
chmod +x deploy-prod.sh
./deploy-prod.sh
```

## 🔧 Configuración de Jenkins

### Plugins Requeridos
- Pipeline
- Git
- Docker
- Credentials
- SSH Agent

### Configuración del Job
1. Crear un nuevo Pipeline job
2. Configurar SCM con GitHub
3. Especificar el Jenkinsfile path
4. Configurar webhook desde GitHub

### Variables de Entorno
```bash
DOCKER_REGISTRY=hospital-registry
BACKEND_IMAGE=hospital-backend
FRONTEND_IMAGE=hospital-frontend
```

## 🌐 URLs de los Ambientes

### Development
- Frontend: http://localhost
- Backend: http://localhost:8080
- Base de datos: localhost:1521

### QA
- Frontend: http://qa.hospital.com
- Backend: http://qa-api.hospital.com
- Base de datos: qa-db-server:1521

### Production
- Frontend: https://hospital.com
- Backend: https://api.hospital.com
- Base de datos: prod-db-server:1521

## 🔄 Flujo de Trabajo

1. **Desarrollo**: Los desarrolladores trabajan en ramas feature
2. **Merge a dev**: Se hace merge a la rama `dev` → Despliegue automático a desarrollo
3. **Merge a QA**: Se hace merge a la rama `QA` → Despliegue automático a QA
4. **Merge a dev**: Se hace merge a la rama `dev` → Despliegue automático a producción

## 🛠️ Comandos Útiles

### Verificar estado de los servicios
```bash
docker-compose ps
```

### Ver logs de los servicios
```bash
docker-compose logs -f backend
docker-compose logs -f frontend
```

### Reconstruir servicios
```bash
docker-compose up -d --build
```

### Detener todos los servicios
```bash
docker-compose down
```

## 🔒 Seguridad

- Las credenciales de base de datos están en variables de entorno
- Los certificados SSL están en volúmenes Docker
- Health checks verifican el estado de los servicios
- Backups automáticos antes de despliegues a producción

## 📞 Soporte

Para problemas con el pipeline, contactar al equipo de DevOps. 