pipeline {
    agent any
    
    environment {
        DOCKER_REGISTRY = 'hospital-registry'
        BACKEND_IMAGE = 'hospital-backend'
        FRONTEND_IMAGE = 'hospital-frontend'
        VERSION = "${env.BUILD_NUMBER}"
        JAVA_HOME = '/usr/lib/jvm/java-17-openjdk-amd64'
        PATH = "${env.JAVA_HOME}/bin:${env.PATH}"
    }
    
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        
        stage('Setup Tools') {
            steps {
                sh '''
                    echo "=== Verificando Java ==="
                    export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
                    export PATH=$JAVA_HOME/bin:$PATH
                    java -version
                    mvn -version
                    
                    echo "=== Verificando Docker ==="
                    docker --version
                    
                    echo "=== Verificando Node.js ==="
                    node --version || echo "Node.js no está instalado"
                    npm --version || echo "npm no está instalado"
                '''
            }
        }
        
        stage('Build Backend') {
            steps {
                dir('backend') {
                    sh '''
                        export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
                        export PATH=$JAVA_HOME/bin:$PATH
                        java -version
                        mvn clean package -DskipTests
                    '''
                }
            }
        }
        
        stage('Test Backend') {
            steps {
                dir('backend') {
                    sh '''
                        export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
                        export PATH=$JAVA_HOME/bin:$PATH
                        mvn test
                    '''
                }
            }
                                    post {
                            always {
                                junit '**/target/surefire-reports/*.xml'
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
                    
                    // Desplegar usando docker-compose
                    sh "docker compose down || true"
                    sh "docker compose up -d"
                    
                    echo "✅ Desplegado exitosamente en ambiente de desarrollo!"
                    echo "🌐 Frontend: http://localhost:80"
                    echo "🔧 Backend: http://localhost:8080"
                    echo "🗄️  Database: localhost:1521"
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
                    
                    // Desplegar usando docker-compose.qa.yml
                    sh "docker compose -f docker-compose.qa.yml down || true"
                    sh "docker compose -f docker-compose.qa.yml up -d"
                    
                    echo "✅ Desplegado exitosamente en ambiente de QA!"
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
                    
                    // Desplegar usando docker-compose.prod.yml
                    sh "docker compose -f docker-compose.prod.yml down || true"
                    sh "docker compose -f docker-compose.prod.yml up -d"
                    
                    echo "✅ Desplegado exitosamente en ambiente de producción!"
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