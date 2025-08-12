# 🎯 SonarQube - Análisis por Ramas

## 📋 Descripción

Este sistema está configurado para realizar análisis de calidad de código con SonarQube específicamente para cada rama del proyecto, creando proyectos separados y organizados.

> **⚠️ IMPORTANTE**: Esta configuración está optimizada para SonarQube Community Edition. Para funcionalidades avanzadas de ramas, se requiere Developer Edition o superior.

## 🌿 Ramas Soportadas

### 1. **Rama `prod` (PRODUCCIÓN)**
- **Backend**: `hospital-backend-prod`
- **Frontend**: `hospital-frontend-prod`
- **Configuración**: Análisis completo con Quality Gate

### 2. **Rama `QA` (Testing)**
- **Backend**: `hospital-backend-qa`
- **Frontend**: `hospital-frontend-qa`
- **Configuración**: Análisis completo con Quality Gate

### 3. **Rama `dev` (Desarrollo)**
- **Backend**: `hospital-backend-dev`
- **Frontend**: `hospital-frontend-dev`
- **Configuración**: Análisis completo con Quality Gate

### 4. **Otras ramas**
- **Backend**: `hospital-backend-{nombre-rama}`
- **Frontend**: `hospital-frontend-{nombre-rama}`
- **Configuración**: Análisis estándar

## 🔧 Configuración

### Archivos de Configuración

#### Backend
- `sonar-project-backend-prod.properties` - Configuración específica para producción
- `sonar-project-backend.properties` - Configuración general

#### Frontend
- `sonar-project-frontend-prod.properties` - Configuración específica para producción
- `sonar-project-frontend.properties` - Configuración general

### Scripts de Análisis

- `analyze-sonarqube-prod.sh` - Análisis completo para rama producción
- `analyze-frontend-sonar.sh` - Análisis del frontend (existente)

## 🚀 Uso

### 1. **Análisis Automático (Jenkins)**
El pipeline de Jenkins ejecuta automáticamente el análisis según la rama:

```bash
# El Jenkinsfile detecta automáticamente la rama y configura:
- sonar.projectKey=hospital-backend-{rama}
- sonar.projectName=Hospital Backend - {RAMA} (Java/Quarkus)
- sonar.branch.name={rama}
```

### 2. **Análisis Manual para Producción**
```bash
# Ejecutar análisis completo para rama prod
./analyze-sonarqube-prod.sh

# O ejecutar solo backend
cd backend
sonar-scanner -Dsonar.projectKey=hospital-backend-prod -Dsonar.branch.name=prod

# O ejecutar solo frontend
sonar-scanner -Dsonar.projectKey=hospital-frontend-prod -Dsonar.branch.name=prod
```

### 3. **Análisis para Otras Ramas**
```bash
# Para rama QA
sonar-scanner -Dsonar.projectKey=hospital-backend-qa -Dsonar.branch.name=QA

# Para rama dev
sonar-scanner -Dsonar.projectKey=hospital-backend-dev -Dsonar.branch.name=dev
```

## 📊 Proyectos en SonarQube

### Estructura de Proyectos
```
SonarQube Dashboard
├── hospital-backend-prod      (Rama: prod)
├── hospital-backend-qa        (Rama: QA)
├── hospital-backend-dev       (Rama: dev)
├── hospital-frontend-prod     (Rama: prod)
├── hospital-frontend-qa       (Rama: QA)
└── hospital-frontend-dev      (Rama: dev)
```

### Características por Rama

#### **PRODUCCIÓN (prod)**
- ✅ Quality Gate estricto
- ✅ Análisis completo de seguridad
- ✅ Reportes de cobertura detallados
- ✅ Análisis de duplicados
- ✅ Métricas de mantenibilidad

#### **QA**
- ✅ Quality Gate estándar
- ✅ Análisis de seguridad
- ✅ Reportes de cobertura
- ✅ Métricas de calidad

#### **DESARROLLO (dev)**
- ✅ Quality Gate básico
- ✅ Análisis de código
- ✅ Métricas básicas

## 🔍 Configuración del Jenkinsfile

### Cambios Implementados

1. **Detección automática de rama**
   ```groovy
   export BRANCH_NAME=''' + env.BRANCH_NAME + '''
   ```

2. **Configuración dinámica de proyectos**
   ```groovy
   if [ "$BRANCH_NAME" = "prod" ]; then
       PROJECT_KEY="hospital-backend-prod"
       PROJECT_NAME="Hospital Backend - PRODUCCIÓN (Java/Quarkus)"
   ```

3. **Proyectos separados por rama** (Community Edition)
   ```groovy
   # En lugar de sonar.branch.name (solo Developer Edition+)
   -Dsonar.projectKey=hospital-backend-{rama}
   -Dsonar.projectName=Hospital Backend - {RAMA}
   ```

## ⚠️ Limitaciones de Community Edition

### **Lo que SÍ funciona:**
- ✅ Proyectos separados por rama (`hospital-backend-prod`, `hospital-backend-qa`)
- ✅ Nombres descriptivos por rama
- ✅ Análisis completo de código
- ✅ Quality Gates
- ✅ Métricas y reportes

### **Lo que NO funciona (requiere Developer Edition+):**
- ❌ `sonar.branch.name` - Análisis de ramas nativo
- ❌ Historial de ramas en un solo proyecto
- ❌ Comparación automática entre ramas
- ❌ Merge de análisis de ramas

### **Alternativa para Community Edition:**
- 🔄 **Proyectos separados**: Cada rama tiene su propio proyecto
- 🔄 **Nombres descriptivos**: Identificación clara por rama
- 🔄 **Análisis independiente**: Cada proyecto mantiene su historial

## 📈 Métricas y Quality Gates

### Backend (Java/Quarkus)
- **Cobertura de código**: Mínimo 80%
- **Duplicados**: Máximo 3%
- **Vulnerabilidades**: 0 críticas
- **Code smells**: Máximo 100
- **Technical debt**: Máximo 5%

### Frontend (Vue.js/TypeScript)
- **Cobertura de código**: Mínimo 70%
- **Duplicados**: Máximo 5%
- **Vulnerabilidades**: 0 críticas
- **Code smells**: Máximo 150
- **Technical debt**: Máximo 8%

## 🛠️ Troubleshooting

### Problemas Comunes

1. **SonarQube no responde**
   ```bash
   curl -f http://localhost:9000/api/system/status
   ```

2. **Token inválido**
   - Verificar credenciales en Jenkins
   - Regenerar token en SonarQube

3. **Proyecto no encontrado**
   - Verificar `sonar.projectKey`
   - Crear proyecto manualmente en SonarQube

4. **Análisis falla**
   - Verificar logs de sonar-scanner
   - Comprobar permisos de archivos

### Logs y Debugging

```bash
# Habilitar logs detallados
export SONAR_VERBOSE=true

# Ver logs de sonar-scanner
sonar-scanner -X

# Verificar configuración
sonar-scanner -Dsonar.projectKey=test -Dsonar.projectName=Test
```

## 🔗 Enlaces Útiles

- **SonarQube**: http://localhost:9000
- **Jenkins**: http://localhost:8080
- **Documentación SonarQube**: https://docs.sonarqube.org/
- **Jenkins SonarQube Plugin**: https://plugins.jenkins.io/sonar/

## 📝 Notas Importantes

1. **Primera ejecución**: Los proyectos se crean automáticamente en SonarQube
2. **Ramas**: Cada rama mantiene su historial de análisis
3. **Quality Gates**: Se aplican según la configuración de cada rama
4. **Cobertura**: Se genera automáticamente al ejecutar tests
5. **Backup**: Los resultados se almacenan en SonarQube

## 🎉 Beneficios

- ✅ **Separación clara** entre entornos
- ✅ **Historial completo** por rama
- ✅ **Quality Gates** específicos por ambiente
- ✅ **Métricas organizadas** y fáciles de comparar
- ✅ **Integración automática** con Jenkins
- ✅ **Configuración flexible** para nuevas ramas
