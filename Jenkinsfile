pipeline {
    agent any
    
    environment {
        DOCKER_REGISTRY = 'hospital-registry'
        BACKEND_IMAGE = 'hospital-backend'
        FRONTEND_IMAGE = 'hospital-frontend'
        VERSION = "${env.BUILD_NUMBER}"
        SONAR_TOKEN = credentials('sonar-token')
        EMAIL_TO = 'lead-developer@hospital.com,product-owner@hospital.com'
    }
    
    stages {
        stage('Checkout') {
            steps {
                checkout scm
                script {
                    // Obtener información del pull request
                    if (env.CHANGE_ID) {
                        echo "Pull Request #${env.CHANGE_ID} detectado"
                        echo "Rama origen: ${env.CHANGE_BRANCH}"
                        echo "Rama destino: ${env.CHANGE_TARGET}"
                    }
                }
            }
        }
        
        stage('Code Quality Check') {
            steps {
                script {
                    // Verificar que el código cumple con los estándares
                    echo "Verificando calidad del código..."
                    
                    // Aquí irían las verificaciones de SonarQube
                    // sh 'mvn sonar:sonar -Dsonar.host.url=http://localhost:9000 -Dsonar.login=${SONAR_TOKEN}'
                }
            }
        }
        
        stage('Build Backend') {
            steps {
                dir('backend') {
                    sh 'mvn clean compile -DskipTests'
                }
            }
        }
        
        stage('Unit Tests Backend') {
            steps {
                dir('backend') {
                    sh 'mvn test'
                }
            }
            post {
                always {
                    publishTestResults testResultsPattern: '**/target/surefire-reports/*.xml'
                }
            }
        }
        
        stage('Build Frontend') {
            steps {
                sh 'npm ci'
                sh 'npm run build'
            }
        }
        
        stage('Unit Tests Frontend') {
            steps {
                sh 'npm run test:unit'
            }
            post {
                always {
                    publishTestResults testResultsPattern: '**/test-results/*.xml'
                }
            }
        }
        
        stage('Integration Tests') {
            steps {
                script {
                    echo "Ejecutando pruebas de integración..."
                    // Aquí irían las pruebas de integración
                }
            }
        }
        
        stage('Build Docker Images') {
            when {
                anyOf {
                    branch 'dev'
                    branch 'QA'
                    branch 'master'
                }
            }
            steps {
                script {
                    // Construir imagen del backend
                    docker.build("${DOCKER_REGISTRY}/${BACKEND_IMAGE}:${VERSION}")
                    
                    // Construir imagen del frontend
                    docker.build("${DOCKER_REGISTRY}/${FRONTEND_IMAGE}:${VERSION}", "-f Dockerfile.frontend .")
                }
            }
        }
        
        stage('Deploy to Development') {
            when {
                allOf {
                    branch 'dev'
                    not { changeRequest() }
                }
            }
            steps {
                script {
                    echo "🚀 Desplegando en ambiente de DESARROLLO..."
                    
                    // Tag de imágenes para desarrollo
                    sh "docker tag ${DOCKER_REGISTRY}/${BACKEND_IMAGE}:${VERSION} ${DOCKER_REGISTRY}/${BACKEND_IMAGE}:dev"
                    sh "docker tag ${DOCKER_REGISTRY}/${FRONTEND_IMAGE}:${VERSION} ${DOCKER_REGISTRY}/${FRONTEND_IMAGE}:dev"
                    
                    // Desplegar usando docker-compose
                    sh 'docker-compose -f docker-compose.yml up -d'
                    
                    echo "✅ Despliegue en desarrollo completado"
                }
            }
            post {
                success {
                    echo "🎉 Despliegue en desarrollo exitoso"
                }
                failure {
                    script {
                        echo "❌ Fallo en despliegue de desarrollo"
                        // Enviar email de notificación
                        emailext (
                            subject: "❌ Fallo en Despliegue DEV - Hospital Pipeline",
                            body: """
                            <h2>Fallo en Despliegue de Desarrollo</h2>
                            <p><strong>Build:</strong> #${env.BUILD_NUMBER}</p>
                            <p><strong>Rama:</strong> ${env.BRANCH_NAME}</p>
                            <p><strong>Commit:</strong> ${env.GIT_COMMIT}</p>
                            <p><strong>Autor:</strong> ${env.GIT_AUTHOR_NAME}</p>
                            <p><strong>URL del Build:</strong> <a href="${env.BUILD_URL}">${env.BUILD_URL}</a></p>
                            """,
                            to: "${EMAIL_TO}",
                            mimeType: 'text/html'
                        )
                    }
                }
            }
        }
        
        stage('Deploy to QA') {
            when {
                allOf {
                    branch 'QA'
                    not { changeRequest() }
                }
            }
            steps {
                script {
                    echo "🚀 Desplegando en ambiente de QA..."
                    
                    // Tag de imágenes para QA
                    sh "docker tag ${DOCKER_REGISTRY}/${BACKEND_IMAGE}:${VERSION} ${DOCKER_REGISTRY}/${BACKEND_IMAGE}:qa"
                    sh "docker tag ${DOCKER_REGISTRY}/${FRONTEND_IMAGE}:${VERSION} ${DOCKER_REGISTRY}/${FRONTEND_IMAGE}:qa"
                    
                    // Desplegar usando docker-compose para QA
                    sh 'docker-compose -f docker-compose.qa.yml up -d'
                    
                    echo "✅ Despliegue en QA completado"
                }
            }
            post {
                success {
                    echo "🎉 Despliegue en QA exitoso"
                }
                failure {
                    script {
                        echo "❌ Fallo en despliegue de QA"
                        emailext (
                            subject: "❌ Fallo en Despliegue QA - Hospital Pipeline",
                            body: """
                            <h2>Fallo en Despliegue de QA</h2>
                            <p><strong>Build:</strong> #${env.BUILD_NUMBER}</p>
                            <p><strong>Rama:</strong> ${env.BRANCH_NAME}</p>
                            <p><strong>Commit:</strong> ${env.GIT_COMMIT}</p>
                            <p><strong>Autor:</strong> ${env.GIT_AUTHOR_NAME}</p>
                            <p><strong>URL del Build:</strong> <a href="${env.BUILD_URL}">${env.BUILD_URL}</a></p>
                            """,
                            to: "${EMAIL_TO}",
                            mimeType: 'text/html'
                        )
                    }
                }
            }
        }
        
        stage('Deploy to Production') {
            when {
                allOf {
                    branch 'master'
                    not { changeRequest() }
                }
            }
            steps {
                script {
                    echo "🚀 Desplegando en ambiente de PRODUCCIÓN..."
                    
                    // Tag de imágenes para producción
                    sh "docker tag ${DOCKER_REGISTRY}/${BACKEND_IMAGE}:${VERSION} ${DOCKER_REGISTRY}/${BACKEND_IMAGE}:prod"
                    sh "docker tag ${DOCKER_REGISTRY}/${FRONTEND_IMAGE}:${VERSION} ${DOCKER_REGISTRY}/${FRONTEND_IMAGE}:prod"
                    
                    // Desplegar usando docker-compose para producción
                    sh 'docker-compose -f docker-compose.prod.yml up -d'
                    
                    echo "✅ Despliegue en producción completado"
                }
            }
            post {
                success {
                    echo "🎉 Despliegue en producción exitoso"
                    script {
                        emailext (
                            subject: "✅ Despliegue Exitoso en PRODUCCIÓN - Hospital Pipeline",
                            body: """
                            <h2>Despliegue Exitoso en Producción</h2>
                            <p><strong>Build:</strong> #${env.BUILD_NUMBER}</p>
                            <p><strong>Rama:</strong> ${env.BRANCH_NAME}</p>
                            <p><strong>Commit:</strong> ${env.GIT_COMMIT}</p>
                            <p><strong>Autor:</strong> ${env.GIT_AUTHOR_NAME}</p>
                            <p><strong>URL del Build:</strong> <a href="${env.BUILD_URL}">${env.BUILD_URL}</a></p>
                            <p><strong>Fecha:</strong> ${new Date().format("yyyy-MM-dd HH:mm:ss")}</p>
                            """,
                            to: "${EMAIL_TO}",
                            mimeType: 'text/html'
                        )
                    }
                }
                failure {
                    script {
                        echo "❌ Fallo en despliegue de producción"
                        emailext (
                            subject: "🚨 FALLO CRÍTICO en Despliegue PRODUCCIÓN - Hospital Pipeline",
                            body: """
                            <h2>FALLO CRÍTICO en Despliegue de Producción</h2>
                            <p><strong>Build:</strong> #${env.BUILD_NUMBER}</p>
                            <p><strong>Rama:</strong> ${env.BRANCH_NAME}</p>
                            <p><strong>Commit:</strong> ${env.GIT_COMMIT}</p>
                            <p><strong>Autor:</strong> ${env.GIT_AUTHOR_NAME}</p>
                            <p><strong>URL del Build:</strong> <a href="${env.BUILD_URL}">${env.BUILD_URL}</a></p>
                            <p><strong>Fecha:</strong> ${new Date().format("yyyy-MM-dd HH:mm:ss")}</p>
                            <p style="color: red; font-weight: bold;">⚠️ REQUIERE ATENCIÓN INMEDIATA</p>
                            """,
                            to: "${EMAIL_TO}",
                            mimeType: 'text/html'
                        )
                    }
                }
            }
        }
    }
    
    post {
        always {
            // Limpiar imágenes Docker
            sh 'docker system prune -f'
            
            // Limpiar workspace
            cleanWs()
        }
        success {
            script {
                if (env.CHANGE_ID) {
                    echo "✅ Pull Request #${env.CHANGE_ID} procesado exitosamente"
                } else {
                    echo "✅ Pipeline ejecutado exitosamente en rama ${env.BRANCH_NAME}"
                }
            }
        }
        failure {
            script {
                if (env.CHANGE_ID) {
                    echo "❌ Pull Request #${env.CHANGE_ID} falló"
                    emailext (
                        subject: "❌ Pull Request Falló - Hospital Pipeline",
                        body: """
                        <h2>Pull Request Falló</h2>
                        <p><strong>Pull Request:</strong> #${env.CHANGE_ID}</p>
                        <p><strong>Rama origen:</strong> ${env.CHANGE_BRANCH}</p>
                        <p><strong>Rama destino:</strong> ${env.CHANGE_TARGET}</p>
                        <p><strong>Build:</strong> #${env.BUILD_NUMBER}</p>
                        <p><strong>URL del Build:</strong> <a href="${env.BUILD_URL}">${env.BUILD_URL}</a></p>
                        <p><strong>URL del Pull Request:</strong> <a href="${env.CHANGE_URL}">${env.CHANGE_URL}</a></p>
                        """,
                        to: "${EMAIL_TO}",
                        mimeType: 'text/html'
                    )
                } else {
                    echo "❌ Pipeline falló en rama ${env.BRANCH_NAME}"
                }
            }
        }
    }
} 