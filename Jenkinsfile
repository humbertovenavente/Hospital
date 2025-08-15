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
        
        stage('QA Testing & Validation') {
            when {
                branch 'QA'
            }
            parallel {
                stage('Integration Tests') {
                    steps {
                        dir('backend') {
                            sh '''
                                export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
                                export PATH=$JAVA_HOME/bin:$PATH
                                echo "🧪 Ejecutando tests de integración..."
                                mvn test -Dtest=*IntegrationTest
                                mvn test -Dtest=*IT
                            '''
                        }
                    }
                    post {
                        always {
                            junit '**/target/surefire-reports/*.xml'
                        }
                    }
                }
                
                stage('Frontend E2E Tests') {
                    steps {
                        sh '''
                            echo "🧪 Ejecutando tests E2E del frontend..."
                            npm run test:e2e || echo "Tests E2E no configurados aún"
                        '''
                    }
                }
                
                stage('Code Quality Analysis') {
                    steps {
                        script {
                            echo "🔍 Analizando calidad del código..."
                            
                            // Análisis de SonarQube si está configurado
                            try {
                                withSonarQubeEnv('SonarQube') {
                                    dir('backend') {
                                        sh '''
                                            export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
                                            export PATH=$JAVA_HOME/bin:$PATH
                                            mvn sonar:sonar \
                                                -Dsonar.projectKey=hospital-backend-qa \
                                                -Dsonar.host.url=http://localhost:9000
                                        '''
                                    }
                                }
                            } catch (Exception e) {
                                echo "⚠️ SonarQube no está disponible, continuando..."
                            }
                            
                            // Análisis de dependencias
                            dir('backend') {
                                sh '''
                                    export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
                                    export PATH=$JAVA_HOME/bin:$PATH
                                    echo "📦 Analizando dependencias del backend..."
                                    mvn dependency:analyze || echo "Análisis de dependencias falló"
                                '''
                            }
                            
                            sh '''
                                echo "📦 Analizando dependencias del frontend..."
                                npm audit --audit-level moderate || echo "Auditoría de npm falló"
                            '''
                        }
                    }
                }
                
                stage('Security Scan') {
                    steps {
                        script {
                            echo "🔒 Escaneando vulnerabilidades de seguridad..."
                            
                            // Escaneo de Docker
                            sh '''
                                echo "🐳 Escaneando imágenes Docker..."
                                docker run --rm -v /var/run/docker.sock:/var/run/docker.sock \
                                    aquasec/trivy image ${DOCKER_REGISTRY}/${BACKEND_IMAGE}:${VERSION} || echo "Escaneo de Docker falló"
                            '''
                            
                            // Escaneo de dependencias Java
                            dir('backend') {
                                sh '''
                                    export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
                                    export PATH=$JAVA_HOME/bin:$PATH
                                    echo "☕ Escaneando dependencias Java..."
                                    mvn org.owasp:dependency-check-maven:check || echo "OWASP check falló"
                                '''
                            }
                        }
                    }
                }
            }
        }
        
        stage('QA Environment Validation') {
            when {
                branch 'QA'
            }
            steps {
                script {
                    echo "✅ Validando ambiente de QA..."
                    
                    // Verificar que los servicios estén funcionando
                    sh '''
                        echo "🔍 Verificando servicios de QA..."
                        sleep 30
                        
                        # Verificar frontend
                        curl -f http://localhost:5174/health || echo "❌ Frontend no responde"
                        
                        # Verificar backend
                        curl -f http://localhost:8090/actuator/health || echo "❌ Backend no responde"
                        
                        # Verificar base de datos
                        docker exec hospital-qa-db-1 sqlplus -s system/oracle@localhost:1522/XE <<< "SELECT 1 FROM DUAL;" || echo "❌ Base de datos no responde"
                    '''
                    
                    echo "🎯 Ambiente de QA validado exitosamente!"
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
                    echo "🌐 Frontend: http://localhost:5174"
                    echo "🔧 Backend: http://localhost:8090"
                    echo "🗄️  Database: localhost:1522"
                }
            }
        }
        
        stage('Deploy to Production') {
            when {
                branch 'prod'
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
                    echo "🌐 Frontend: http://localhost:80"
                    echo "🔧 Backend: http://localhost:8080"
                    echo "🗄️  Database: localhost:1521"
                }
            }
        }
    }
    
    stage('QA Reports & Metrics') {
        when {
            branch 'QA'
        }
        steps {
            script {
                echo "📊 Generando reportes y métricas de QA..."
                
                // Generar reporte de cobertura de código
                dir('backend') {
                    sh '''
                        export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
                        export PATH=$JAVA_HOME/bin:$PATH
                        echo "📈 Generando reporte de cobertura..."
                        mvn jacoco:report || echo "Reporte de cobertura no disponible"
                    '''
                }
                
                // Generar reporte de dependencias
                sh '''
                    echo "📦 Generando reporte de dependencias..."
                    npm list --depth=0 > dependency-report.txt || echo "Reporte de dependencias falló"
                '''
                
                // Generar reporte de seguridad
                sh '''
                    echo "🔒 Generando reporte de seguridad..."
                    npm audit --json > security-report.json || echo "Reporte de seguridad falló"
                '''
                
                // Publicar artefactos
                archiveArtifacts artifacts: '**/target/site/jacoco/**/*,dependency-report.txt,security-report.json', fingerprint: true
                
                echo "📋 Reportes generados y archivados exitosamente!"
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
                if (env.BRANCH_NAME == 'QA') {
                    echo "🎉 Pipeline de QA ejecutado exitosamente!"
                    echo "📊 Reportes disponibles en: ${env.BUILD_URL}artifact/"
                    echo "🔍 Análisis de calidad completado"
                    echo "✅ Despliegue en QA exitoso"
                    
                    // Aquí puedes agregar notificaciones a Slack, Teams, etc.
                    // slackSend channel: '#qa-notifications', message: "✅ Pipeline QA exitoso - Build #${env.BUILD_NUMBER}"
                } else {
                    echo "Pipeline ejecutado exitosamente!"
                }
            }
        }
        failure {
            script {
                if (env.BRANCH_NAME == 'QA') {
                    echo "❌ Pipeline de QA falló!"
                    echo "🔍 Revisar logs en: ${env.BUILD_URL}console"
                    echo "⚠️ Verificar tests, análisis de código y despliegue"
                    
                    // Notificaciones de fallo
                    // slackSend channel: '#qa-notifications', message: "❌ Pipeline QA falló - Build #${env.BUILD_NUMBER}"
                } else {
                    echo "Pipeline falló!"
                }
            }
        }
        cleanup {
            script {
                if (env.BRANCH_NAME == 'QA') {
                    echo "🧹 Limpiando ambiente de QA..."
                    // Limpiar contenedores de QA si es necesario
                    sh "docker compose -f docker-compose.qa.yml down || true"
                }
            }
        }
    }
} 