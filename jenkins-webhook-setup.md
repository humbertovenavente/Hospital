# 🚀 Configuración de Webhooks Automáticos en Jenkins

## 📋 Requisitos Previos

### Plugins Necesarios en Jenkins:
- ✅ **GitHub Integration Plugin**
- ✅ **Git plugin** 
- ✅ **Pipeline plugin**
- ✅ **Multibranch Pipeline plugin** (opcional)

## 🔧 Configuración en Jenkins

### 1. Configurar Credenciales de GitHub

1. **Jenkins Dashboard** → **Manage Jenkins** → **Manage Credentials**
2. **System** → **Global credentials** → **Add Credentials**
3. **Kind**: `Username with password`
4. **Scope**: `Global`
5. **Username**: Tu usuario de GitHub
6. **Password**: Tu token personal de GitHub (NO tu contraseña)
7. **ID**: `github-credentials`

### 2. Configurar el Job

#### Opción A: Job Simple
1. **Crear nuevo job** → **Freestyle project**
2. **Source Code Management**:
   - **Git** → **Repository URL**: `https://github.com/tu-usuario/tu-repo.git`
   - **Credentials**: Seleccionar `github-credentials`
   - **Branches to build**: `*/main`, `*/develop`, `*/feature/*`

3. **Build Triggers**:
   - ✅ **GitHub hook trigger for GITScm polling**

#### Opción B: Pipeline (Recomendado)
1. **Crear nuevo job** → **Pipeline**
2. **Pipeline** → **Definition**: `Pipeline script from SCM`
3. **SCM**: `Git`
4. **Repository URL**: `https://github.com/tu-usuario/tu-repo.git`
5. **Credentials**: Seleccionar `github-credentials`
6. **Script Path**: `Jenkinsfile`

### 3. Configurar Webhook en GitHub

1. **Ir a tu repositorio** → **Settings** → **Webhooks**
2. **Add webhook**:
   - **Payload URL**: `http://tu-jenkins:8080/github-webhook/`
   - **Content type**: `application/json`
   - **Secret**: (opcional, para seguridad)
   - **Events**: 
     - ✅ **Just the push event** (para commits)
     - ✅ **Pull requests** (para PRs)
     - ✅ **Pushes** (para todos los pushes)

## 📝 Jenkinsfile Optimizado para Webhooks

```groovy
pipeline {
    agent any
    
    triggers {
        // Webhook trigger (se ejecuta automáticamente)
        // No necesitas pollSCM si usas webhooks
    }
    
    stages {
        stage('Checkout') {
            steps {
                checkout scm
                echo "🔄 Build automático iniciado por: ${env.GIT_COMMIT}"
                echo "📝 Mensaje: ${env.GIT_COMMIT_MESSAGE}"
                echo "👤 Autor: ${env.GIT_AUTHOR_NAME}"
            }
        }
        
        stage('Build Backend') {
            when {
                anyOf {
                    changeset "backend/**"
                    changeset "pom.xml"
                    changeset "Jenkinsfile"
                }
            }
            steps {
                dir('backend') {
                    sh 'mvn clean compile -q'
                }
            }
        }
        
        stage('Build Frontend') {
            when {
                anyOf {
                    changeset "frontend/**"
                    changeset "package.json"
                    changeset "Jenkinsfile"
                }
            }
            steps {
                dir('frontend') {
                    sh 'npm install --silent'
                    sh 'npm run build --silent'
                }
            }
        }
        
        stage('Tests Backend') {
            when {
                anyOf {
                    changeset "backend/**"
                    changeset "pom.xml"
                }
            }
            steps {
                dir('backend') {
                    sh 'mvn test -q'
                }
            }
        }
        
        stage('Tests Frontend') {
            when {
                anyOf {
                    changeset "frontend/**"
                    changeset "package.json"
                }
            }
            steps {
                dir('frontend') {
                    sh 'npm test --silent'
                }
            }
        }
        
        stage('SonarQube Analysis') {
            when {
                anyOf {
                    changeset "**/*.java"
                    changeset "**/*.js"
                    changeset "**/*.ts"
                    changeset "**/*.vue"
                }
            }
            steps {
                script {
                    if (env.BRANCH_NAME == 'main' || env.BRANCH_NAME == 'develop') {
                        // Análisis completo para ramas principales
                        sh 'cd backend && sonar-scanner'
                    } else {
                        // Análisis rápido para feature branches
                        echo "🔍 Análisis rápido para rama: ${env.BRANCH_NAME}"
                    }
                }
            }
        }
    }
    
    post {
        always {
            // Limpiar workspace
            cleanWs()
        }
        success {
            // Notificación de éxito (tu código actual)
        }
        failure {
            // Notificación de fallo (tu código actual)
        }
    }
}
```

## 🔍 Verificación de la Configuración

### 1. Test del Webhook
```bash
# En tu repositorio, hacer un commit pequeño
echo "# Test webhook" >> README.md
git add README.md
git commit -m "Test webhook Jenkins"
git push
```

### 2. Verificar en Jenkins
- **Dashboard** → Ver si aparece un nuevo build
- **Console Output** → Verificar que se ejecutó automáticamente

### 3. Logs de Webhook
- **Jenkins** → **Manage Jenkins** → **System Log**
- Buscar entradas relacionadas con GitHub webhooks

## 🚨 Solución de Problemas Comunes

### Webhook no funciona:
1. ✅ Verificar que Jenkins esté accesible desde GitHub
2. ✅ Verificar credenciales de GitHub
3. ✅ Verificar configuración del job
4. ✅ Revisar logs de Jenkins

### Builds no se ejecutan:
1. ✅ Verificar "Build Triggers" en el job
2. ✅ Verificar permisos del usuario de Jenkins
3. ✅ Verificar configuración de Git en el job

### Errores de autenticación:
1. ✅ Usar token personal de GitHub (NO contraseña)
2. ✅ Verificar permisos del token
3. ✅ Verificar scope del token (repo, admin:repo_hook)

## 🎯 Beneficios de los Webhooks

- ⚡ **Ejecución inmediata** al hacer push/PR
- 🔄 **Builds automáticos** sin intervención manual
- 📊 **Feedback rápido** sobre la calidad del código
- 🚀 **CI/CD verdadero** con integración continua
- 💰 **Ahorro de tiempo** en desarrollo

## 🔒 Seguridad

- Usar **tokens personales** en lugar de contraseñas
- Configurar **webhook secrets** para validación
- Limitar **permisos del token** al mínimo necesario
- Usar **HTTPS** para todas las comunicaciones
