# Pipeline CI/CD Hospital - 11 Pasos

## 🚀 Descripción General

Este pipeline sigue exactamente el flujo de Jenkins con 11 pasos secuenciales, detectando automáticamente la rama y ejecutando la configuración correspondiente.

## 📋 Flujo del Pipeline

### 🔧 PASO 1: Setup Environment
- **Duración estimada**: 0.86 segundos
- **Descripción**: Configura el ambiente basado en la rama detectada
- **Ramas**: `dev`, `QA`, `prod`
- **Configuración**:
  - `dev`: Puerto 80, DB 1521, NODE_ENV=development
  - `QA`: Puerto 81, DB 1522, NODE_ENV=qa
  - `prod`: Puerto 82, DB 1523, NODE_ENV=production

### 🔨 PASO 2: Build Backend
- **Duración estimada**: 18 segundos
- **Descripción**: Construye el backend de la aplicación
- **Comandos**: `npm install` + `npm run build`
- **Dependencias**: Setup Environment

### 🧪 PASO 3: Unit Tests Backend
- **Duración estimada**: 18 segundos
- **Descripción**: Ejecuta tests unitarios del backend
- **Comandos**: `npm run test`
- **Dependencias**: Build Backend

### 🔍 PASO 4: Code Quality Check
- **Duración estimada**: 2 minutos 3 segundos
- **Descripción**: Análisis de calidad de código con SonarQube
- **Herramienta**: SonarQube
- **Token**: `sqa_8f9c9ffeaf833e1486015527efadabc251e75755`
- **Dependencias**: Unit Tests Backend

### 🎨 PASO 5: Build Frontend
- **Duración estimada**: 12 segundos
- **Descripción**: Construye el frontend de la aplicación
- **Comandos**: `npm install` + `npm run build`
- **Dependencias**: Code Quality Check

### 🧪 PASO 6: Unit Tests Frontend
- **Duración estimada**: 3.3 segundos
- **Descripción**: Ejecuta tests unitarios del frontend
- **Comandos**: `npm run test`
- **Dependencias**: Build Frontend

### 🔗 PASO 7: Integration Tests
- **Duración estimada**: 0.34 segundos
- **Descripción**: Ejecuta tests de integración
- **Herramienta**: Cypress
- **Dependencias**: Unit Tests Frontend

### 🐳 PASO 8: Build Docker Images
- **Duración estimada**: 14 segundos
- **Descripción**: Construye las imágenes Docker
- **Registry**: `34.10.223.20:5000`
- **Dependencias**: Integration Tests

### 🚀 PASO 9: Deploy to Development
- **Duración estimada**: 34 milisegundos
- **Descripción**: Despliega a ambiente de desarrollo
- **Ramas**: `dev`, `feature/*`
- **Puerto**: 80
- **Dependencias**: Build Docker Images

### 🧪 PASO 10: Deploy to QA
- **Duración estimada**: 31 milisegundos
- **Descripción**: Despliega a ambiente de QA
- **Ramas**: `QA`
- **Puerto**: 81
- **Dependencias**: Build Docker Images

### 🏭 PASO 11: Deploy to Production
- **Duración estimada**: 22 segundos
- **Descripción**: Despliega a ambiente de producción
- **Ramas**: `prod`
- **Puerto**: 82
- **Dependencias**: Build Docker Images

## 🔄 Flujo de Ramas

```
feature/nueva-funcionalidad → dev → QA → prod
```

### Desarrollo
- **Rama**: `dev`, `feature/*`
- **Deploy**: Automático a desarrollo
- **Puerto**: 80
- **Base de datos**: Oracle DB1 (1521)

### QA
- **Rama**: `QA`
- **Deploy**: Automático a QA
- **Puerto**: 81
- **Base de datos**: Oracle DB2 (1522)

### Producción
- **Rama**: `prod`
- **Deploy**: Automático a producción
- **Puerto**: 82
- **Base de datos**: Oracle DB3 (1523)

## 🛠️ Herramientas Integradas

### SonarQube
- **URL**: http://34.10.223.20:9000
- **Token**: `sqa_8f9c9ffeaf833e1486015527efadabc251e75755`
- **Uso**: Análisis de calidad de código

### Grok AI
- **API Key**: `2y0VGgZEKDSVsjkImjiNE1feiRd_38io6TPdS9To8E717e8rb`
- **Modelo**: `llama-3.1-70b-versatile`
- **Uso**: Análisis y recomendaciones (solo producción)

### GitHub
- **Usuario**: `humbertovenavente`
- **Token**: `your_github_token_here`
- **Repositorio**: `humbertovenavente/hospital`

## 📊 Monitoreo

### Health Checks
- **URL**: http://34.10.223.20:{PORT}/health
- **Frecuencia**: Después de cada deploy
- **Timeout**: 30 segundos

### Notificaciones
- **Slack**: Canal `deployments`
- **Trigger**: Éxito del pipeline
- **Mensaje**: Incluye ambiente y rama

## 🚨 Troubleshooting

### Errores Comunes

1. **Build Fallido**
   - Verificar dependencias en `package.json`
   - Revisar logs del paso específico

2. **Tests Fallidos**
   - Verificar configuración de tests
   - Revisar cobertura de código

3. **Deploy Fallido**
   - Verificar configuración de Docker
   - Revisar conectividad con base de datos

4. **SonarQube Fallido**
   - Verificar token de acceso
   - Revisar configuración del proyecto

### Comandos Útiles

```bash
# Ver logs del pipeline
docker-compose -f docker-compose.drone.yml logs

# Ver logs de SonarQube
docker-compose -f docker-compose.sonarqube.yml logs

# Reiniciar servicios
docker-compose -f docker-compose.drone.yml restart
docker-compose -f docker-compose.sonarqube.yml restart

# Ver estado de contenedores
docker ps --filter "name=hospital-*"
```

## 📈 Métricas

### Tiempos de Ejecución
- **Total estimado**: ~3 minutos 30 segundos
- **Paso más lento**: Code Quality Check (2m 3s)
- **Paso más rápido**: Integration Tests (0.34s)

### Recursos
- **CPU**: 4 cores
- **RAM**: 8GB
- **Disco**: 200GB

## 🔐 Seguridad

### Tokens y Claves
- **GitHub**: `your_github_token_here`
- **SonarQube**: `your_sonar_token_here`
- **Grok**: `your_grok_api_key_here`

### Accesos
- **Drone**: http://34.10.223.20:8080
- **SonarQube**: http://34.10.223.20:9000
- **Admin**: `humbertovenavente`

## 🎯 Próximos Pasos

1. ✅ Configurar ramas en GitHub
2. ✅ Instalar Drone CI/CD
3. ✅ Instalar SonarQube
4. ✅ Configurar pipeline de 11 pasos
5. 🔄 Probar pipeline con rama `dev`
6. 🔄 Probar pipeline con rama `QA`
7. 🔄 Probar pipeline con rama `prod`

---

**Pipeline configurado exitosamente** 🚀
