pipeline {
    agent any
    
    environment {
        DOCKER_REGISTRY = 'hospital-registry'
        BACKEND_IMAGE = 'hospital-backend'
        FRONTEND_IMAGE = 'hospital-frontend'
        VERSION = "${env.BUILD_NUMBER}"
    }
    
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        
        stage('Build Backend') {
            steps {
                dir('backend') {
                    sh 'mvn clean package -DskipTests'
                }
            }
        }
        
        stage('Test Backend') {
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
        
        stage('Build Docker Images') {
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
                branch 'dev'
            }
            steps {
                script {
                    // Desplegar en ambiente de desarrollo
                    sh "docker tag ${DOCKER_REGISTRY}/${BACKEND_IMAGE}:${VERSION} ${DOCKER_REGISTRY}/${BACKEND_IMAGE}:dev"
                    sh "docker tag ${DOCKER_REGISTRY}/${FRONTEND_IMAGE}:${VERSION} ${DOCKER_REGISTRY}/${FRONTEND_IMAGE}:dev"
                    
                    // Aquí irían los comandos para desplegar en el servidor de desarrollo
                    echo "Desplegando en ambiente de desarrollo..."
                }
            }
        }
        
        stage('Deploy to QA') {
            when {
                branch 'QA'
            }
            steps {
                script {
                    // Desplegar en ambiente de QA
                    sh "docker tag ${DOCKER_REGISTRY}/${BACKEND_IMAGE}:${VERSION} ${DOCKER_REGISTRY}/${BACKEND_IMAGE}:qa"
                    sh "docker tag ${DOCKER_REGISTRY}/${FRONTEND_IMAGE}:${VERSION} ${DOCKER_REGISTRY}/${FRONTEND_IMAGE}:qa"
                    
                    // Aquí irían los comandos para desplegar en el servidor de QA
                    echo "Desplegando en ambiente de QA..."
                }
            }
        }
        
        stage('Deploy to Production') {
            when {
                branch 'dev'
            }
            steps {
                script {
                    // Desplegar en ambiente de producción
                    sh "docker tag ${DOCKER_REGISTRY}/${BACKEND_IMAGE}:${VERSION} ${DOCKER_REGISTRY}/${BACKEND_IMAGE}:prod"
                    sh "docker tag ${DOCKER_REGISTRY}/${FRONTEND_IMAGE}:${VERSION} ${DOCKER_REGISTRY}/${FRONTEND_IMAGE}:prod"
                    
                    // Aquí irían los comandos para desplegar en el servidor de producción
                    echo "Desplegando en ambiente de producción..."
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
            echo "Pipeline ejecutado exitosamente!"
        }
        failure {
            echo "Pipeline falló!"
        }
    }
} 