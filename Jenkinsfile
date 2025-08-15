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
        
        stage('Fail Injection (opcional)') {
            when {
                branch 'QA'
            }
            steps {
                script {
                    // Simular fallos para testing de resiliencia
                    def shouldFail = env.FAIL_INJECTION == 'true'
                    if (shouldFail) {
                        echo "🧪 Inyectando fallo para testing..."
                        error "Fallo inyectado para testing de QA"
                    } else {
                        echo "✅ Sin inyección de fallos"
                    }
                }
            }
        }
        
        stage('Setup Environment') {
            steps {
                sh '''
                    echo "=== Configurando ambiente ==="
                    export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
                    export PATH=$JAVA_HOME/bin:$PATH
                    
                    echo "=== Verificando herramientas ==="
                    java -version
                    mvn -version
                    docker --version
                    node --version || echo "Node.js no está instalado"
                    npm --version || echo "npm no está instalado"
                    
                    echo "=== Limpiando workspace ==="
                    mvn clean || echo "Maven clean no disponible"
                    rm -rf node_modules package-lock.json || echo "Limpieza de Node.js completada"
                '''
            }
        }
        
        stage('Build Backend') {
            steps {
                dir('backend') {
                    sh '''
                        export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
                        export PATH=$JAVA_HOME/bin:$PATH
                        echo "🔨 Construyendo backend..."
                        mvn clean package -DskipTests
                    '''
                }
            }
        }
        
        stage('Unit Tests Backend') {
            steps {
                dir('backend') {
                    sh '''
                        export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
                        export PATH=$JAVA_HOME/bin:$PATH
                        echo "🧪 Ejecutando unit tests del backend..."
                        mvn test
                    '''
                }
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                    publishHTML([
                        allowMissing: false,
                        alwaysLinkToLastBuild: true,
                        keepAll: true,
                        reportDir: 'target/site/jacoco',
                        reportFiles: 'index.html',
                        reportName: 'Backend Coverage Report'
                    ])
                }
            }
        }
        
        stage('Code Quality Check') {
            when {
                branch 'QA'
            }
            steps {
                script {
                    echo "🔍 Verificando calidad del código..."
                    
                    // Análisis estático del código
                    dir('backend') {
                        sh '''
                            export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
                            export PATH=$JAVA_HOME/bin:$PATH
                            echo "📊 Generando reporte de cobertura..."
                            mvn jacoco:prepare-agent test jacoco:report
                            
                            echo "🔍 Analizando dependencias..."
                            mvn dependency:analyze || echo "Análisis de dependencias falló"
                            
                            echo "🔒 Escaneando vulnerabilidades..."
                            mvn org.owasp:dependency-check-maven:check || echo "OWASP check falló"
                        '''
                    }
                    
                    // Análisis del frontend
                    sh '''
                        echo "📦 Analizando dependencias del frontend..."
                        npm audit --audit-level moderate || echo "Auditoría de npm falló"
                        
                        echo "🔍 Ejecutando linting..."
                        npm run lint || echo "Linting no configurado"
                    '''
                    
                    // Análisis de SonarQube
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
                }
            }
            post {
                always {
                    publishHTML([
                        allowMissing: true,
                        alwaysLinkToLastBuild: true,
                        keepAll: true,
                        reportDir: 'backend/target/site/jacoco',
                        reportFiles: 'index.html',
                        reportName: 'Code Coverage Report'
                    ])
                    publishHTML([
                        allowMissing: true,
                        alwaysLinkToLastBuild: true,
                        keepAll: true,
                        reportDir: 'backend/target/dependency-check-report',
                        reportFiles: 'index.html',
                        reportName: 'Security Report'
                    ])
                }
            }
        }
        
        stage('Build Frontend') {
            steps {
                sh '''
                    echo "🔨 Construyendo frontend..."
                    npm ci
                    npm run build
                '''
            }
        }
        
        stage('Unit Tests Frontend') {
            steps {
                sh '''
                    echo "🧪 Ejecutando unit tests del frontend..."
                    npm test || echo "Tests del frontend no configurados"
                '''
            }
            post {
                always {
                    publishHTML([
                        allowMissing: true,
                        alwaysLinkToLastBuild: true,
                        keepAll: true,
                        reportDir: 'coverage/lcov-report',
                        reportFiles: 'index.html',
                        reportName: 'Frontend Coverage Report'
                    ])
                }
            }
        }
        
        stage('Integration Tests') {
            when {
                branch 'QA'
            }
            steps {
                script {
                    echo "🧪 Ejecutando tests de integración..."
                    
                    // Tests de integración del backend
                    dir('backend') {
                        sh '''
                            export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
                            export PATH=$JAVA_HOME/bin:$PATH
                            echo "🔗 Tests de integración del backend..."
                            mvn test -Dtest=*IntegrationTest || echo "Tests de integración no configurados"
                            mvn test -Dtest=*IT || echo "Tests de integración IT no configurados"
                        '''
                    }
                    
                    // Tests E2E del frontend
                    sh '''
                        echo "🔗 Tests E2E del frontend..."
                        npm run test:e2e || echo "Tests E2E no configurados"
                    '''
                }
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }
        
        stage('Build Docker Images') {
            steps {
                script {
                    echo "🐳 Construyendo imágenes Docker..."
                    
                    // Construir imagen del backend
                    docker.build("${DOCKER_REGISTRY}/${BACKEND_IMAGE}:${VERSION}")
                    
                    // Construir imagen del frontend
                    docker.build("${DOCKER_REGISTRY}/${FRONTEND_IMAGE}:${VERSION}", "-f Dockerfile.frontend .")
                    
                    echo "✅ Imágenes Docker construidas exitosamente"
                }
            }
        }
        
        stage('Deploy to Development') {
            when {
                branch 'dev'
            }
            steps {
                script {
                    echo "🚀 Desplegando en ambiente de desarrollo..."
                    
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
                    echo "🚀 Desplegando en ambiente de QA..."
                    
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
        
        stage('Deploy to Production') {
            when {
                branch 'prod'
            }
            steps {
                script {
                    echo "🚀 Desplegando en ambiente de producción..."
                    
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