# 🚀 Jenkins CI/CD Pipeline - Hospital System

## 📋 Resumen

Jenkins ha sido configurado exitosamente para ejecutar el pipeline CI/CD del sistema hospitalario. El pipeline incluye construcción, pruebas y despliegue automático a tres ambientes: desarrollo, QA y producción.

## 🔧 Configuración de Jenkins

### Estado Actual
- **URL**: http://localhost:8081
- **Puerto**: 8081 (configurado para evitar conflictos con el backend)
- **Estado**: ✅ Funcionando
- **Contraseña inicial**: `21633f4907a94353a7cf0392061afc87`

### Scripts Disponibles

#### 1. `start-jenkins.sh`
Inicia Jenkins manualmente con la configuración correcta:
```bash
./start-jenkins.sh
```

#### 2. `stop-jenkins.sh`
Detiene Jenkins de forma segura:
```bash
./stop-jenkins.sh
```

#### 3. `setup-jenkins.sh`
Muestra las instrucciones de configuración:
```bash
./setup-jenkins.sh
```

## 🏗️ Pipeline CI/CD

### Estructura del Pipeline

```
Hospital/
├── Jenkinsfile                    # Pipeline principal de Jenkins
├── .github/workflows/ci-cd.yml   # GitHub Actions (alternativo)
├── deploy-dev.sh                 # Script de despliegue a desarrollo
├── deploy-qa.sh                  # Script de despliegue a QA
├── deploy-prod.sh                # Script de despliegue a producción
├── docker-compose.yml            # Docker Compose para desarrollo
├── docker-compose.qa.yml         # Docker Compose para QA
├── docker-compose.prod.yml       # Docker Compose para producción
├── Dockerfile                    # Dockerfile para backend
└── Dockerfile.frontend           # Dockerfile para frontend
```

### Ambientes Configurados

| Ambiente | Rama | Base de Datos | URL |
|----------|------|---------------|-----|
| **Desarrollo** | `dev` | `C##PROYECTO` | http://localhost |
| **QA** | `QA` | `C##HOSPITAL2` | http://qa.hospital.com |
| **Producción** | `prod` | `C##HOSPITAL3` | https://hospital.com |

### Flujo de Trabajo

1. **Desarrollo**: Los desarrolladores trabajan en la rama `dev`
2. **QA**: Merge a `QA` → Despliegue automático a QA
3. **Producción**: Merge a `dev` → Despliegue automático a producción

## 🚀 Configuración del Pipeline en Jenkins

### 1. Acceder a Jenkins
- Abrir navegador: http://localhost:8081
- Usar contraseña inicial: `21633f4907a94353a7cf0392061afc87`

### 2. Instalar Plugins
Instalar los siguientes plugins:
- **Git plugin** - Para integración con Git
- **Pipeline plugin** - Para pipelines declarativos
- **Docker plugin** - Para construcción de imágenes Docker
- **Credentials plugin** - Para manejo de credenciales

### 3. Crear Pipeline Job

1. **Nuevo Item** → **Pipeline**
2. **Nombre**: `Hospital-CI-CD`
3. **Configuración**:
   - **Pipeline**: Pipeline script from SCM
   - **SCM**: Git
   - **Repository URL**: `https://github.com/humbertovenavente/Hospital.git`
   - **Branch**: `dev`
   - **Script Path**: `Jenkinsfile`

### 4. Configurar Credenciales (Opcional)
Para repositorios privados, configurar credenciales:
- **Kind**: Username with password
- **Scope**: Global
- **Username**: Tu usuario de GitHub
- **Password**: Tu token de GitHub

## 🔄 Etapas del Pipeline

### 1. Checkout
- Clona el repositorio desde GitHub
- Cambia a la rama correspondiente

### 2. Build Backend
- Compila el proyecto Java/Quarkus
- Ejecuta pruebas unitarias
- Construye imagen Docker

### 3. Build Frontend
- Instala dependencias npm
- Construye aplicación Vue.js
- Construye imagen Docker

### 4. Deploy to Development
- Se ejecuta en rama `dev`
- Despliega usando `docker-compose.yml`

### 5. Deploy to QA
- Se ejecuta en rama `QA`
- Despliega usando `docker-compose.qa.yml`

### 6. Deploy to Production
- Se ejecuta en rama `dev` (rama principal)
- Despliega usando `docker-compose.prod.yml`
- Incluye backup automático

## 🐳 Docker

### Imágenes Construidas
- **Backend**: Java 17 + Quarkus
- **Frontend**: Node.js 18 + Nginx

### Comandos de Despliegue
```bash
# Desarrollo
./deploy-dev.sh

# QA
./deploy-qa.sh

# Producción
./deploy-prod.sh
```

## 🔍 Monitoreo

### Logs de Jenkins
```bash
# Ver logs en tiempo real
sudo journalctl -u jenkins.service -f

# Ver logs recientes
sudo journalctl -u jenkins.service --no-pager -l -n 50
```

### Estado de Servicios
```bash
# Verificar puertos
netstat -tlnp | grep -E "(8080|8081|80)"

# Verificar procesos
ps aux | grep -E "(jenkins|java|node)"
```

## 🛠️ Solución de Problemas

### Jenkins no inicia
1. Verificar puerto disponible: `netstat -tlnp | grep 8081`
2. Verificar permisos: `sudo chown -R jenkins:jenkins /var/lib/jenkins`
3. Usar script manual: `./start-jenkins.sh`

### Pipeline falla
1. Verificar conectividad con GitHub
2. Verificar credenciales configuradas
3. Revisar logs del job en Jenkins

### Docker no funciona
1. Verificar Docker instalado: `docker --version`
2. Verificar Docker Compose: `docker-compose --version`
3. Verificar permisos: `sudo usermod -aG docker $USER`

## 📚 Recursos Adicionales

- [Jenkins Documentation](https://www.jenkins.io/doc/)
- [Pipeline Syntax](https://www.jenkins.io/doc/book/pipeline/syntax/)
- [Docker Documentation](https://docs.docker.com/)
- [GitHub Actions](https://docs.github.com/en/actions)

## 🎯 Próximos Pasos

1. **Configurar Jenkins** siguiendo las instrucciones de `setup-jenkins.sh`
2. **Crear el pipeline job** en Jenkins
3. **Configurar webhooks** para integración automática con GitHub
4. **Configurar notificaciones** (email, Slack, etc.)
5. **Configurar monitoreo** y alertas
6. **Implementar pruebas automatizadas** adicionales

---

**¡Jenkins está listo para ejecutar tu pipeline CI/CD! 🚀** 