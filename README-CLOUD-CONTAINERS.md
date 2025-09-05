# 🏥 Hospital - Contenedores en la Nube

Este documento explica cómo desplegar y gestionar los 6 contenedores del sistema Hospital en la nube.

## 📋 Arquitectura de Contenedores

### Entornos Disponibles

| Entorno | Backend | Frontend | Puerto Backend | Puerto Frontend |
|---------|---------|----------|----------------|-----------------|
| **Desarrollo** | hospital-backend-dev-cloud | hospital-frontend-dev-cloud | 8060 | 8061 |
| **Producción** | hospital-backend-prod-cloud | hospital-frontend-prod-cloud | 8020 | 8021 |
| **QA** | hospital-backend-qa-cloud | hospital-frontend-qa-cloud | 8030 | 8031 |

## Despliegue Rápido

### 1. Desplegar Todos los Contenedores

```bash
./deploy-cloud-containers.sh deploy
```

Este comando:
- ✅ Verifica que Docker esté ejecutándose
- 🧹 Limpia contenedores existentes
- 🔨 Construye todas las imágenes
- 🚀 Despliega los 6 contenedores
- ✅ Verifica el estado de los contenedores
- 📋 Muestra las URLs de acceso

### 2. Verificar Estado de los Contenedores

```bash
./check-cloud-containers.sh check
```

Este comando verifica:
- 🔍 Estado de todos los contenedores
- 🌐 Puertos abiertos
- ❤️ Salud de los servicios HTTP
- 📊 URLs de acceso

## 🛠️ Comandos Disponibles

### Script de Despliegue (`deploy-cloud-containers.sh`)

```bash
# Desplegar todos los contenedores
./deploy-cloud-containers.sh deploy

# Solo construir las imágenes
./deploy-cloud-containers.sh build

# Iniciar contenedores existentes
./deploy-cloud-containers.sh start

# Detener contenedores específicos
./deploy-cloud-containers.sh stop dev      # Solo desarrollo
./deploy-cloud-containers.sh stop prod     # Solo producción
./deploy-cloud-containers.sh stop qa       # Solo QA
./deploy-cloud-containers.sh stop all      # Todos los entornos

# Reiniciar contenedores
./deploy-cloud-containers.sh restart dev   # Reiniciar desarrollo
./deploy-cloud-containers.sh restart all   # Reiniciar todos

# Ver logs
./deploy-cloud-containers.sh logs dev      # Logs de desarrollo
./deploy-cloud-containers.sh logs prod     # Logs de producción
./deploy-cloud-containers.sh logs qa       # Logs de QA

# Ver estado
./deploy-cloud-containers.sh status

# Limpiar todo
./deploy-cloud-containers.sh cleanup

# Ayuda
./deploy-cloud-containers.sh help
```

### Script de Verificación (`check-cloud-containers.sh`)

```bash
# Verificar todos los contenedores
./check-cloud-containers.sh check

# Ver logs de un entorno específico
./check-cloud-containers.sh logs dev
./check-cloud-containers.sh logs prod
./check-cloud-containers.sh logs qa

# Ver estadísticas de recursos
./check-cloud-containers.sh stats

# Ayuda
./check-cloud-containers.sh help
```

## 🌐 URLs de Acceso

Una vez desplegados los contenedores, puedes acceder a:

### 🔧 Desarrollo
- **Backend**: http://104.197.237.11:8060
- **Frontend**: http://104.197.237.11:8061
- **Health Check**: http://104.197.237.11:8060/q/health

### 🚀 Producción
- **Backend**: http://104.197.237.11:8020
- **Frontend**: http://104.197.237.11:8021
- **Health Check**: http://104.197.237.11:8020/q/health

### 🧪 QA
- **Backend**: http://104.197.237.11:8030
- **Frontend**: http://104.197.237.11:8031
- **Health Check**: http://104.197.237.11:8030/q/health

## 📁 Archivos de Configuración

### Docker Compose Files
- `docker-compose.cloud-dev.yml` - Configuración para desarrollo
- `docker-compose.cloud-prod.yml` - Configuración para producción
- `docker-compose.cloud-qa.yml` - Configuración para QA

### Dockerfiles
- `backend/Dockerfile.jvm` - Dockerfile para el backend (Quarkus)
- `Dockerfile.frontend.cloud` - Dockerfile para el frontend (Vue.js + Nginx)

## 🔧 Configuración Técnica

### Backend (Quarkus)
- **Base Image**: eclipse-temurin:21-jdk
- **Puerto Interno**: 8080
- **Puertos Externos**: 8060 (dev), 8020 (prod), 8030 (qa)
- **Health Check**: `/q/health`

### Frontend (Vue.js + Nginx)
- **Base Image**: node:18-alpine (build) + nginx:alpine (runtime)
- **Puerto Interno**: 80
- **Puertos Externos**: 8061 (dev), 8021 (prod), 8031 (qa)
- **Configuración**: Nginx con CORS habilitado

### Base de Datos
- **Tipo**: Oracle XE
- **Host**: 34.10.223.20
- **Puerto**: 1521
- **SID**: XE
- **Usuario**: system
- **Contraseña**: Oracle123

## 🐛 Solución de Problemas

### Verificar Estado de Docker
```bash
docker info
```

### Ver Contenedores en Ejecución
```bash
docker ps
```

### Ver Logs de un Contenedor Específico
```bash
docker logs hospital-backend-dev-cloud
docker logs hospital-frontend-dev-cloud
```

### Reiniciar un Contenedor Específico
```bash
docker restart hospital-backend-dev-cloud
```

### Limpiar Todo y Empezar de Nuevo
```bash
./deploy-cloud-containers.sh cleanup
./deploy-cloud-containers.sh deploy
```

## 📊 Monitoreo

### Ver Estadísticas de Recursos
```bash
./check-cloud-containers.sh stats
```

### Ver Logs en Tiempo Real
```bash
./deploy-cloud-containers.sh logs dev
```

### Verificar Salud de los Servicios
```bash
curl http://104.197.237.11:8060/q/health  # Desarrollo
curl http://104.197.237.11:8020/q/health  # Producción
curl http://104.197.237.11:8030/q/health  # QA
```

## 🔄 Flujo de Trabajo Recomendado

1. **Desarrollo**: Usa el entorno de desarrollo (puertos 8060/8061)
2. **Testing**: Usa el entorno de QA (puertos 8030/8031)
3. **Producción**: Usa el entorno de producción (puertos 8020/8021)

## 📝 Notas Importantes

- Los contenedores están configurados para reiniciarse automáticamente (`restart: unless-stopped`)
- Cada entorno tiene su propia red Docker para aislamiento
- Los health checks verifican que los servicios estén funcionando correctamente
- Las imágenes se construyen sin caché para asegurar la última versión del código
- Los logs están disponibles para debugging y monitoreo

## 🆘 Soporte

Si encuentras problemas:

1. Verifica que Docker esté ejecutándose: `docker info`
2. Revisa los logs: `./deploy-cloud-containers.sh logs [entorno]`
3. Verifica el estado: `./check-cloud-containers.sh check`
4. Si es necesario, limpia todo: `./deploy-cloud-containers.sh cleanup`
5. Vuelve a desplegar: `./deploy-cloud-containers.sh deploy`
