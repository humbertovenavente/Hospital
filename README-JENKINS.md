# 🚀 Configuración de Jenkins CI/CD para Hospital

## 📋 Resumen

Se ha configurado un pipeline completo de CI/CD usando Jenkins, Docker y GitHub para el proyecto Hospital con tres ambientes: **Development**, **QA** y **Production**.

## 🏗️ Arquitectura del Pipeline

```
GitHub (dev branch) → Jenkins → Docker → Despliegue
```

### 🌿 Branches y Ambientes

| Branch | Ambiente | Base de Datos | Usuario |
|--------|----------|---------------|---------|
| `dev` | Development | Oracle XE | `C##PROYECTO` |
| `QA` | QA/UAT | Oracle XE | `C##HOSPITAL2` |
| `prod` | Production | Oracle XE | `C##HOSPITAL3` |

## 🔧 Configuración de Jenkins

### 📦 Plugins Requeridos

- **Git Plugin**: Para clonar repositorios
- **Pipeline Plugin**: Para ejecutar pipelines declarativos
- **Docker Plugin**: Para construir imágenes Docker
- **JUnit Plugin**: Para publicar resultados de tests
- **Credentials Plugin**: Para manejar credenciales

### 🛠️ Herramientas Instaladas

- **Java 17**: Para compilar el backend
- **Maven**: Para gestionar dependencias Java
- **Node.js**: Para construir el frontend
- **Docker**: Para containerización
- **Git**: Para control de versiones

## 📁 Archivos de Configuración

### 🔄 Pipeline Principal
- **`Jenkinsfile`**: Pipeline declarativo con todas las etapas

### 🐳 Docker
- **`Dockerfile`**: Imagen del backend Java/Quarkus
- **`Dockerfile.frontend`**: Imagen del frontend Vue.js
- **`docker-compose.yml`**: Desarrollo local
- **`docker-compose.qa.yml`**: Ambiente QA
- **`docker-compose.prod.yml`**: Ambiente producción

### 🚀 Scripts de Despliegue
- **`deploy-dev.sh`**: Despliegue a desarrollo
- **`deploy-qa.sh`**: Despliegue a QA
- **`deploy-prod.sh`**: Despliegue a producción

### ⚙️ Scripts de Jenkins
- **`start-jenkins.sh`**: Iniciar Jenkins
- **`stop-jenkins.sh`**: Detener Jenkins
- **`setup-jenkins-final.sh`**: Configuración completa
- **`verify-jenkins-setup.sh`**: Verificar configuración
- **`install-junit-plugin.sh`**: Instalar plugin JUnit
- **`fix-jenkins-permissions.sh`**: Solucionar permisos

## 🔄 Etapas del Pipeline

### 1. **Checkout**
- Clona el repositorio desde GitHub
- Branch: `dev`

### 2. **Setup Tools**
- Verifica Java, Maven, Docker y Node.js
- Configura variables de entorno

### 3. **Build Backend**
- Compila el proyecto Java con Maven
- Genera el JAR ejecutable

### 4. **Test Backend**
- Ejecuta tests unitarios
- Publica resultados con JUnit

### 5. **Build Frontend**
- Instala dependencias npm
- Construye la aplicación Vue.js

### 6. **Build Docker Images**
- Construye imagen del backend
- Construye imagen del frontend

### 7. **Deploy to Development**
- Despliega automáticamente al ambiente dev
- Usa `docker-compose.yml`

### 8. **Deploy to QA** (condicional)
- Se ejecuta solo en branch `QA`
- Usa `docker-compose.qa.yml`

### 9. **Deploy to Production** (condicional)
- Se ejecuta solo en branch `dev` (configuración especial)
- Usa `docker-compose.prod.yml`

## 🚀 Cómo Usar

### 1. **Iniciar Jenkins**
```bash
./start-jenkins.sh
```

### 2. **Acceder a Jenkins**
- URL: http://localhost:8081

### 3. **Instalar Plugin JUnit**
```bash
./install-junit-plugin.sh
```

### 4. **Crear Pipeline Job**
- Nombre: `Hospital-Pipeline`
- Tipo: `Pipeline`
- SCM: `Git`
- Repository: `https://github.com/humbertovenavente/Hospital.git`
- Branch: `dev`
- Script Path: `Jenkinsfile`

### 5. **Ejecutar Pipeline**
- Haz clic en `Build Now`

## 🔗 URLs Importantes

| Servicio | URL | Descripción |
|----------|-----|-------------|
| Jenkins | http://localhost:8081 | Panel de control |
| Backend Dev | http://localhost:8080 | API de desarrollo |
| Frontend Dev | http://localhost:80 | Aplicación web |
| Oracle DB | localhost:1521 | Base de datos |

## 🛠️ Solución de Problemas

### ❌ Error: "No such DSL method 'publishTestResults'"
**Solución**: Instalar plugin JUnit
```bash
./install-junit-plugin.sh
```

### ❌ Error: "permission denied while trying to connect to the Docker daemon"
**Solución**: Configurar permisos de Docker
```bash
sudo chmod 666 /var/run/docker.sock
sudo usermod -aG docker jenkins
```

### ❌ Error: "release version 17 not supported"
**Solución**: Configurar Java 17
```bash
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH
```

### ❌ Error: Jenkins no inicia
**Solución**: Verificar puerto y permisos
```bash
./stop-jenkins.sh
./start-jenkins.sh
```

## 📊 Monitoreo

### 🔍 Logs del Pipeline
- Jenkins: http://localhost:8081/job/Hospital-Pipeline
- Docker: `docker logs <container-name>`

### 📈 Métricas
- Tests ejecutados
- Tiempo de build
- Tasa de éxito
- Tiempo de despliegue

## 🔐 Seguridad

### 🔑 Credenciales
- Base de datos: Configuradas en `application.properties`
- Docker: Usuario jenkins en grupo docker
- Jenkins: Configurado localmente

### 🛡️ Buenas Prácticas
- Usar variables de entorno para credenciales
- No hardcodear passwords en código
- Mantener Jenkins actualizado
- Revisar logs regularmente

## 📝 Notas Importantes

1. **Branch Principal**: `dev` es ahora el branch principal para producción
2. **Base de Datos**: Cada ambiente tiene su propio usuario de Oracle
3. **Docker**: Todas las imágenes se construyen localmente
4. **Backup**: El script de producción incluye backup automático
5. **Health Checks**: Se incluyen verificaciones de salud en producción

## 🎯 Próximos Pasos

1. ✅ Configurar Jenkins
2. ✅ Crear pipeline
3. ✅ Configurar Docker
4. 🔄 Instalar plugin JUnit
5. 🔄 Crear job en Jenkins
6. 🔄 Ejecutar pipeline completo
7. 🔄 Configurar monitoreo
8. 🔄 Implementar notificaciones

---

**Estado**: ✅ Configuración completa lista
**Última actualización**: $(date)
**Versión**: 1.0 