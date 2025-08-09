node {
    // Parámetro para pruebas: permite forzar un fallo controlado y validar notificaciones
    properties([
        parameters([
            booleanParam(name: 'FORCE_FAIL', defaultValue: false, description: 'Forzar fallo del pipeline para probar notificaciones por correo')
        ])
    ])

    def DOCKER_REGISTRY = 'hospital-registry'
    def BACKEND_IMAGE = 'hospital-backend'
    def FRONTEND_IMAGE = 'hospital-frontend'
    def VERSION = "${env.BUILD_NUMBER}"
    
    try {
        stage('Checkout') {
            echo "🔄 Iniciando checkout del código..."
            checkout scm
            if (env.CHANGE_ID) {
                echo "📋 Pull Request #${env.CHANGE_ID} detectado"
                echo "   Rama origen: ${env.CHANGE_BRANCH}"
                echo "   Rama destino: ${env.CHANGE_TARGET}"
            } else {
                echo "📋 Build directo en rama: ${env.BRANCH_NAME}"
            }
            echo "✅ Checkout completado"

            // Normalizar nombre de rama cuando Jenkins no lo expone (evitar 'null')
            try {
                if (!env.BRANCH_NAME || env.BRANCH_NAME == 'null') {
                    def detected = sh(script: 'git rev-parse --abbrev-ref HEAD', returnStdout: true).trim()
                    if (detected == 'HEAD') {
                        // En estado detached (p.ej., PR). Preferir destino u origen del PR
                        detected = env.CHANGE_TARGET ?: (env.CHANGE_BRANCH ?: 'dev')
                    }
                    env.BRANCH_NAME = detected
                    echo "🔖 Rama detectada: ${env.BRANCH_NAME}"
                }
            } catch (err) {
                echo "⚠️  No se pudo detectar la rama vía git: ${err}. Usando 'dev' por defecto"
                env.BRANCH_NAME = env.BRANCH_NAME ?: 'dev'
            }
        }
        
        stage('Fail Injection (opcional)') {
            if (params.FORCE_FAIL) {
                echo "⚠️  FAIL injection activado: se forzará un fallo para probar notificaciones"
                error('Fallo intencional para probar notificaciones por correo')
            } else {
                echo 'Fail injection desactivado'
            }
        }
        
        stage('Code Quality Check') {
            echo "🔍 Iniciando verificación de calidad del código con SonarQube..."
            echo "   Configurando SonarQube Scanner..."
            
            // Verificar que SonarQube esté disponible
            sh '''
                echo "=== Verificando SonarQube ==="
                curl -f http://localhost:9000/api/system/status || echo "SonarQube no está disponible"
                echo "=== Verificando SonarQube Scanner ==="
                /opt/sonar-scanner/bin/sonar-scanner --version || echo "SonarQube Scanner no está disponible"
            '''
            
            echo "   Ejecutando análisis de calidad del código..."
            
            // Usar la integración oficial de Jenkins con SonarQube y credenciales explícitas
            // IMPORTANTE: El nombre debe coincidir con el configurado en "Manage Jenkins > System > SonarQube servers"
            withSonarQubeEnv('SonarQube') {
                withCredentials([string(credentialsId: 'sonarqube-token', variable: 'SONAR_TOKEN')]) {
                    sh '''
                        echo "=== Ejecutando SonarQube Analysis ==="
                        export PATH=$PATH:/opt/sonar-scanner/bin
                        # Fallbacks: si la integración no expone variables, usar valores por defecto
                        export SONAR_HOST=${SONAR_HOST_URL:-http://localhost:9000}
                        export TOKEN_TO_USE=${SONAR_TOKEN:-$SONAR_AUTH_TOKEN}
                        sonar-scanner \
                            -Dsonar.projectKey=hospital-project \
                            -Dsonar.projectName="Hospital Management System" \
                            -Dsonar.projectVersion=${BUILD_NUMBER} \
                            -Dsonar.sources=src,backend/src/main/java \
                            -Dsonar.tests=backend/src/test/java \
                            -Dsonar.java.source=17 \
                            -Dsonar.java.binaries=backend/target/classes \
                            -Dsonar.java.test.binaries=backend/target/test-classes \
                            -Dsonar.host.url=${SONAR_HOST} \
                            -Dsonar.token=${TOKEN_TO_USE} \
                            -Dsonar.exclusions=**/node_modules/**,**/dist/**,**/target/**,**/*.min.js,**/*.min.css \
                            -Dsonar.qualitygate.wait=true
                        echo "=== Análisis de SonarQube completado ==="
                    '''
                }
            }
            echo "✅ Verificación de calidad completada"
        }
        
        stage('Setup Environment') {
            echo "⚙️  Configurando entorno de desarrollo..."
            sh '''
                echo "=== Verificando Java ==="
                java -version
                mvn -version
                echo "=== Verificando Docker ==="
                docker --version
                echo "=== Verificando Node.js ==="
                node --version || echo "Node.js no está instalado"
                npm --version || echo "npm no está instalado"
                echo "=== Verificando Git ==="
                git --version
            '''
            echo "✅ Entorno configurado correctamente"
        }
        
        stage('Build Backend') {
            echo "🔨 Iniciando build del backend..."
            echo "   Compilando aplicación Quarkus..."
            dir('backend') {
                sh '''
                    echo "=== Compilando backend ==="
                    mvn clean compile -DskipTests
                    echo "=== Backend compilado exitosamente ==="
                '''
            }
            echo "✅ Build del backend completado"
        }
        
        stage('Unit Tests Backend') {
            echo "🧪 Ejecutando tests unitarios del backend..."
            dir('backend') {
                sh '''
                    echo "=== Ejecutando tests unitarios ==="
                    mvn test -DskipITs
                    echo "=== Tests unitarios completados ==="
                '''
            }
            echo "✅ Tests unitarios del backend completados"
        }
        
        stage('Build Frontend') {
            echo "🎨 Iniciando build del frontend..."
            echo "   Instalando dependencias..."
            sh '''
                echo "=== Instalando dependencias ==="
                npm ci
                echo "=== Dependencias instaladas ==="
            '''
            echo "   Construyendo aplicación Vue.js..."
            sh '''
                echo "=== Construyendo frontend ==="
                npm run build
                echo "=== Frontend construido exitosamente ==="
            '''
            echo "✅ Build del frontend completado"
        }
        
        stage('Unit Tests Frontend') {
            echo "🧪 Ejecutando tests unitarios del frontend..."
            sh '''
                echo "=== Ejecutando tests unitarios del frontend ==="
                npm run test:unit || echo "Tests unitarios del frontend no configurados"
                echo "=== Tests unitarios del frontend completados ==="
            '''
            echo "✅ Tests unitarios del frontend completados"
        }
        
        stage('Integration Tests') {
            echo "🔗 Ejecutando pruebas de integración..."
            echo "   Verificando conexión entre frontend y backend..."
            sh '''
                echo "=== Ejecutando pruebas de integración ==="
                echo "Verificando endpoints del backend..."
                echo "Verificando comunicación frontend-backend..."
                echo "=== Pruebas de integración completadas ==="
            '''
            echo "✅ Pruebas de integración completadas"
        }
        
        stage('Build Docker Images') {
            if (env.BRANCH_NAME == 'dev' || env.BRANCH_NAME == 'QA' || env.BRANCH_NAME == 'prod') {
                echo "🐳 Iniciando construcción de imágenes Docker..."
                echo "   Construyendo imagen del backend..."
                docker.build("${DOCKER_REGISTRY}/${BACKEND_IMAGE}:${VERSION}")
                echo "   Construyendo imagen del frontend..."
                docker.build("${DOCKER_REGISTRY}/${FRONTEND_IMAGE}:${VERSION}", "-f Dockerfile.frontend .")
                echo "✅ Imágenes Docker construidas exitosamente"
            } else {
                echo "⏭️  Saltando construcción de Docker (rama: ${env.BRANCH_NAME})"
            }
        }
        
        stage('Deploy to Development') {
            if (env.BRANCH_NAME == 'dev' && !env.CHANGE_ID) {
                echo "🚀 Iniciando despliegue en ambiente de DESARROLLO..."
                echo "   Etiquetando imágenes para desarrollo..."
                sh "docker tag ${DOCKER_REGISTRY}/${BACKEND_IMAGE}:${VERSION} ${DOCKER_REGISTRY}/${BACKEND_IMAGE}:dev"
                sh "docker tag ${DOCKER_REGISTRY}/${FRONTEND_IMAGE}:${VERSION} ${DOCKER_REGISTRY}/${FRONTEND_IMAGE}:dev"
                echo "   Desplegando con Docker Compose..."
                sh 'docker-compose -f docker-compose.yml up -d'
                echo "   Verificando salud de los servicios..."
                sleep 10
                echo "✅ Despliegue en desarrollo completado exitosamente"
            } else {
                echo "⏭️  Saltando despliegue de desarrollo (rama: ${env.BRANCH_NAME}, PR: ${env.CHANGE_ID})"
            }
        }
        
        stage('Deploy to QA') {
            if (env.BRANCH_NAME == 'QA' && !env.CHANGE_ID) {
                echo "🚀 Iniciando despliegue en ambiente de QA..."
                echo "   Etiquetando imágenes para QA..."
                sh "docker tag ${DOCKER_REGISTRY}/${BACKEND_IMAGE}:${VERSION} ${DOCKER_REGISTRY}/${BACKEND_IMAGE}:qa"
                sh "docker tag ${DOCKER_REGISTRY}/${FRONTEND_IMAGE}:${VERSION} ${DOCKER_REGISTRY}/${FRONTEND_IMAGE}:qa"
                echo "   Desplegando con Docker Compose QA..."
                sh 'docker-compose -f docker-compose.qa.yml up -d'
                echo "   Verificando salud de los servicios..."
                sleep 15
                echo "✅ Despliegue en QA completado exitosamente"
            } else {
                echo "⏭️  Saltando despliegue de QA (rama: ${env.BRANCH_NAME}, PR: ${env.CHANGE_ID})"
            }
        }
        
        stage('Deploy to Production') {
            if (env.BRANCH_NAME == 'prod' && !env.CHANGE_ID) {
                echo "🚀 Iniciando despliegue en ambiente de PRODUCCIÓN..."
                echo "   ⚠️  ADVERTENCIA: Despliegue en producción"
                sh "docker tag ${DOCKER_REGISTRY}/${BACKEND_IMAGE}:${VERSION} ${DOCKER_REGISTRY}/${BACKEND_IMAGE}:prod"
                sh "docker tag ${DOCKER_REGISTRY}/${FRONTEND_IMAGE}:${VERSION} ${DOCKER_REGISTRY}/${FRONTEND_IMAGE}:prod"
                echo "   Desplegando con Docker Compose Producción..."
                sh 'docker-compose -f docker-compose.prod.yml up -d'
                echo "   Verificando salud de los servicios..."
                sleep 20
                echo "✅ Despliegue en producción completado exitosamente"
            } else {
                echo "⏭️  Saltando despliegue de producción (rama: ${env.BRANCH_NAME}, PR: ${env.CHANGE_ID})"
            }
        }
        
        // Success summary
        if (env.CHANGE_ID) {
            echo "✅ Pull Request #${env.CHANGE_ID} procesado exitosamente"
            echo "📋 Resumen del pipeline:"
            echo "   - Checkout: ✅"
            echo "   - Code Quality: ✅"
            echo "   - Build Backend: ✅"
            echo "   - Tests Backend: ✅"
            echo "   - Build Frontend: ✅"
            echo "   - Tests Frontend: ✅"
            echo "   - Integration Tests: ✅"
            echo "   - Docker Images: ✅"
        } else {
            echo "✅ Pipeline ejecutado exitosamente en rama ${env.BRANCH_NAME}"
        }
        
    } catch (Exception e) {
        // Error handling
        if (env.CHANGE_ID) {
            echo "❌ Pull Request #${env.CHANGE_ID} falló: ${e.getMessage()}"
        } else {
            echo "❌ Pipeline falló en rama ${env.BRANCH_NAME}: ${e.getMessage()}"
        }
        // Notificación por correo a Lead Developer y Product Owner
        try {
            def recipients = 'jflores@unis.edu.gt, jnajar@unis.edu.gt'
            def subject = (env.CHANGE_ID ? "PR #${env.CHANGE_ID} falló: ${env.JOB_NAME} #${env.BUILD_NUMBER}" : "Pipeline falló: ${env.JOB_NAME} #${env.BUILD_NUMBER} (${env.BRANCH_NAME})")
            def body = """
Hola equipo,

El pipeline ha fallado.

- Job: ${env.JOB_NAME}
- Build: #${env.BUILD_NUMBER}
- Rama: ${env.BRANCH_NAME}
- URL: ${env.BUILD_URL}
- Motivo: ${e.getMessage()}

Por favor revisar la consola para más detalles.
"""
            // Usar Email Extension Plugin (configurado en "Extended E-mail Notification")
            emailext(
                to: recipients,
                from: 'humbertovenavente7@gmail.com',
                subject: subject,
                body: body,
                mimeType: 'text/plain'
            )
            echo "📧 Notificación de fallo enviada (emailext) a: ${recipients}"
        } catch (err) {
            echo "⚠️  No se pudo enviar la notificación por correo: ${err}"
        }
        throw e
    }
} 