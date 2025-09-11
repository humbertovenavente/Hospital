node {
    // Parámetro para pruebas: permite forzar un fallo controlado y validar notificaciones
    properties([
        parameters([
            booleanParam(name: 'FORCE_FAIL', defaultValue: false, description: 'Forzar fallo del pipeline para probar notificaciones por correo'),
            booleanParam(name: 'BUILD_DOCKER', defaultValue: true, description: 'Construir y desplegar imágenes Docker (activado por defecto para QA)')
        ])
    ])
    def DOCKER_REGISTRY = 'hospital-registry'
    def BACKEND_IMAGE = 'hospital-backend'
    def FRONTEND_IMAGE = 'hospital-frontend'
    def VERSION = "${env.BUILD_NUMBER}"
    
    try {
        stage('Checkout') {
            echo " Iniciando checkout del código..."
            // Limpiar workspace para evitar quedarnos en la rama anterior
            deleteDir()
            checkout scm
            if (env.CHANGE_ID) {
                echo " Pull Request #${env.CHANGE_ID} detectado"
                echo "   Rama origen: ${env.CHANGE_BRANCH}"
                echo "   Rama destino: ${env.CHANGE_TARGET}"
            } else {
                echo " Build directo en rama: ${env.BRANCH_NAME}"
            }
            echo "Checkout completado"

            // Normalizar nombre de rama cuando Jenkins no lo expone (evitar 'null')
            try {
                if (!env.BRANCH_NAME || env.BRANCH_NAME == 'null') {
                    def detected = sh(script: 'git rev-parse --abbrev-ref HEAD', returnStdout: true).trim()
                    if (detected == 'HEAD') {
<<<<<<< HEAD
                        // En estado detached, forzar uso de 'QA' para testing
                        detected = 'QA'
                        echo "Estado detached detectado, forzando rama: QA"
=======
                        // En estado detached (p.ej., PR). Preferir destino u origen del PR
                        detected = env.CHANGE_TARGET ?: (env.CHANGE_BRANCH ?: 'dev')
>>>>>>> 648dadd5258233ca3ac4f2574bd67d8933f60087
                    }
                    env.BRANCH_NAME = detected
                    echo " Rama detectada: ${env.BRANCH_NAME}"
                }
<<<<<<< HEAD
                
                // Verificación adicional: si estamos en la rama QA, forzar el nombre
                if (env.BRANCH_NAME == 'QA' || env.BRANCH_NAME == 'qa') {
                    env.BRANCH_NAME = 'QA'
                    echo "Rama QA confirmada: ${env.BRANCH_NAME}"
=======
                            } catch (err) {
                    echo "⚠️  No se pudo detectar la rama vía git: ${err}. Usando 'dev' por defecto"
                    env.BRANCH_NAME = env.BRANCH_NAME ?: 'dev'
>>>>>>> 648dadd5258233ca3ac4f2574bd67d8933f60087
                }
            } catch (err) {
                echo "  No se pudo detectar la rama vía git: ${err}. Usando 'QA' por defecto"
                env.BRANCH_NAME = 'QA'
            }
        }

<<<<<<< HEAD
        // Forzar fallo si está habilitado (para probar el pipeline)
        if (params.FORCE_FAIL) {
            error(" Fallo forzado activado mediante parámetro FORCE_FAIL")
=======

        stage('Setup Environment') {
            echo "⚙️  Configurando entorno para rama: ${env.BRANCH_NAME}..."
            sh '''
                echo "=== Verificando Java ==="
                java -version
                mvn -version
                echo "=== Verificando Docker ==="
                docker --version
                echo "=== Verificando Docker Compose ==="
                if command -v docker-compose >/dev/null 2>&1; then
                  docker-compose --version
                elif docker compose version >/dev/null 2>&1; then
                  docker compose version
                else
                  echo "docker-compose no está instalado. Si deseas usar despliegues con Docker, instala el plugin: sudo apt-get install -y docker-compose-plugin"
                fi
                echo "=== Verificando Node.js ==="
                node --version || echo "Node.js no está instalado"
                npm --version || echo "npm no está instalado"
                echo "=== Verificando Git ==="
                git --version
            '''
            echo "✅ Entorno configurado correctamente"
>>>>>>> 648dadd5258233ca3ac4f2574bd67d8933f60087
        }

        stage('Build Backend') {
            echo "Construyendo Backend para QA..."
            dir('backend') {
                sh '''
                    echo "=== Construyendo Backend (Rama: ''' + env.BRANCH_NAME + ''') ==="
                    chmod +x mvnw
                    ./mvnw clean compile -DskipTests=false
                    echo " Backend construido exitosamente"
                '''
            }
        }

        stage('Test Backend') {
            echo " Ejecutando tests del Backend..."
            dir('backend') {
                sh '''
                    echo "=== Ejecutando Tests de Backend (Rama: ''' + env.BRANCH_NAME + ''') ==="
                    ./mvnw test jacoco:report
                    echo "Tests del Backend completados"
                '''
                // Publicar resultados de tests
                junit testResults: 'target/surefire-reports/*.xml', allowEmptyResults: true
            }
        }

        stage('Build Frontend') {
            echo " Construyendo Frontend para QA..."
            sh '''
                echo "=== Construyendo Frontend (Rama: ''' + env.BRANCH_NAME + ''') ==="
                if [ -f package.json ]; then
                    npm ci
                    npm run build
                    echo " Frontend construido exitosamente"
                else
                    echo " package.json no encontrado"
                    exit 1
                fi
            '''
        }

        stage('SonarQube Analysis') {
            echo " Ejecutando análisis de SonarQube para QA..."
            // IMPORTANTE: El nombre debe coincidir con el configurado en "Manage Jenkins > System > SonarQube servers"
            withSonarQubeEnv('SonarQube') {
                withCredentials([string(credentialsId: 'sonarqube-token', variable: 'SONAR_TOKEN')]) {
                    // ANÁLISIS DEL BACKEND (con cobertura de tests y configuración específica para QA)
                    echo "   🔍 Analizando BACKEND para rama: ${env.BRANCH_NAME}..."
                    sh '''
                        echo "=== Ejecutando SonarQube Analysis para BACKEND QA ==="
                        export PATH=$PATH:/opt/sonar-scanner/bin
                        export BRANCH_NAME=''' + env.BRANCH_NAME + '''
                        export BUILD_NUMBER=''' + env.BUILD_NUMBER + '''
                        
                        # Fallbacks: si la integración no expone variables, usar valores por defecto
                        export SONAR_HOST=${SONAR_HOST_URL:-http://localhost:9000}
                        export TOKEN_TO_USE=${SONAR_TOKEN:-$SONAR_AUTH_TOKEN}

                        echo "   🔧 Usando configuración específica de QA para backend..."
                        sonar-scanner -Dproject.settings=sonar-project-backend-qa.properties
                        
                        echo "=== Análisis de SonarQube para BACKEND QA completado ==="
                    '''
                    
                    // ANÁLISIS DEL FRONTEND (HACIENDO QUE FALLE INTENCIONALMENTE)
                    echo "   🔍 Analizando FRONTEND para rama: ${env.BRANCH_NAME}..."
                    sh '''
                        echo "=== Ejecutando SonarQube Analysis para FRONTEND QA ==="
                        export PATH=$PATH:/opt/sonar-scanner/bin
                        export BRANCH_NAME=''' + env.BRANCH_NAME + '''
                        export BUILD_NUMBER=''' + env.BUILD_NUMBER + '''
                        export SONAR_HOST=${SONAR_HOST_URL:-http://localhost:9000}
                        export SONAR_TOKEN=${SONAR_TOKEN:-$SONAR_AUTH_TOKEN}

                        # Forzar fallo del análisis del frontend para testing
                        echo "    FORZANDO FALLO del análisis de SonarQube para frontend..."
                        echo "    Simulando error de configuración..."
                        
                        # Verificar que el directorio src existe
                        if [ ! -d "src" ]; then
                            echo "    Error: Directorio src no encontrado"
                            echo "    Directorio actual: $(pwd)"
                            echo "    Contenido: $(ls -la)"
                            exit 1
                        fi

                        # Verificar que el archivo de configuración existe
                        if [ ! -f "sonar-project-frontend-qa.properties" ]; then
                            echo "    Error: Archivo de configuración sonar-project-frontend-qa.properties no encontrado"
                            echo "   📁 Archivos en directorio actual: $(ls -la *.properties 2>/dev/null || echo 'No hay archivos .properties')"
                            echo "   🚨 FALLO INTENCIONAL: Archivo de configuración de SonarQube no encontrado"
                            exit 1
                        fi

                        echo "   🔧 Usando configuración específica de QA para frontend..."
                        # Intentar ejecutar sonar-scanner pero forzar fallo
                        echo "   🚨 Simulando fallo en el análisis del frontend..."
                        echo "   ❌ Error: No se puede conectar con SonarQube"
                        echo "   ❌ Error: Token de autenticación inválido"
                        echo "   ❌ Error: Configuración del proyecto incorrecta"
                        
                        # Forzar fallo del comando
                        exit 1
                        
                        echo "=== Análisis de SonarQube para FRONTEND QA completado ==="
                    '''
                }
            }
        }

        stage('Quality Gate') {
            echo "🚪 Esperando Quality Gate..."
            timeout(time: 5, unit: 'MINUTES') {
                def qg = waitForQualityGate()
                if (qg.status != 'OK') {
                    echo " Quality Gate falló: ${qg.status}"
                    error "Pipeline abortado debido a falla en Quality Gate"
                } else {
                    echo " Quality Gate pasó exitosamente"
                }
            }
        }
<<<<<<< HEAD

        stage('Deploy QA') {
            if (params.BUILD_DOCKER) {
                echo "🚀 Desplegando en entorno de QA..
                
                echo "    Limpiando contenedores de QA existentes..."
                sh '''
=======
        
        stage('Deploy to Production (dev branch)') {
            if (params.BUILD_DOCKER && env.BRANCH_NAME == 'dev' && !env.CHANGE_ID) {
                echo "🚀 Iniciando despliegue en ambiente de PRODUCCIÓN (rama dev)..."
                echo "   🐳 Construyendo y desplegando con configuración de PRODUCCIÓN..."
                sh '''
                  # Construir backend con configuración de producción
                  echo "🔨 Construyendo backend para PRODUCCIÓN..."
                  docker build -t hospital-backend-local .
                  
                  # Construir frontend con configuración de producción
                  echo "🎨 Construyendo frontend para PRODUCCIÓN..."
                  docker build -f Dockerfile.frontend -t hospitalpipelineprod2-frontend .
                  
                  # Limpiar contenedores anteriores si existen
                  echo "🧹 Limpiando contenedores anteriores..."
                  docker stop hospital-backend-local hospital-frontend-local 2>/dev/null || true
                  docker rm hospital-backend-local hospital-frontend-local 2>/dev/null || true
                  
                  # Verificar Docker Compose
>>>>>>> 648dadd5258233ca3ac4f2574bd67d8933f60087
                  if command -v docker-compose >/dev/null 2>&1; then
                    DC="docker-compose"
                  elif docker compose version >/dev/null 2>&1; then
                    DC="docker compose"
                  else
                    echo "❌ docker-compose no está instalado. Instala con: sudo apt-get install -y docker-compose-plugin"; exit 1
                  fi
                  
<<<<<<< HEAD
                  # Detener y limpiar contenedores de QA existentes
                  echo "Deteniendo contenedores de QA..."
                  $DC -f docker-compose.qa.yml down 2>/dev/null || true
                  
                  # Forzar detención y eliminación SOLO de contenedores de QA existentes
                  echo  Forzando limpieza SOLO de contenedores de QA..."
                  docker stop hospital-backend-qa 2>/dev/null || true
                  docker rm hospital-backend-qa 2>/dev/null || true
                  docker stop hospital-frontend-qa 2>/dev/null || true
                  docker rm hospital-frontend-qa 2>/dev/null || true
                '''
                
                echo "    Construyendo y desplegando contenedores de QA..."
                sh '''
                  # Construir y desplegar servicios de QA
                  echo " Desplegando servicios de QA..."
                  docker-compose -f docker-compose.qa.yml up -d --build
                  
                  # Conectar backend a la red de Oracle si es necesario
                  echo " Verificando conectividad de red..."
                  sleep 10
                  docker network connect bridge hospital-backend-qa 2>/dev/null || true
                '''
                echo "   ⏳ Esperando que los servicios se inicien..."
                sleep 15
                echo " Despliegue en QA completado exitosamente"
                echo " URLs de acceso QA:"
                echo "   - Backend: http://localhost:8090"
                echo "   - Frontend: http://localhost:5174"
                echo "   - SonarQube: http://localhost:9000"
=======
                  # Crear red si no existe
                  echo "🌐 Configurando red hospital-2_hospital-network..."
                  docker network create hospital-2_hospital-network 2>/dev/null || true
                  
                  # Asegurar que oracle_xe3 esté disponible
                  echo "🗄️ Verificando Oracle Database..."
                  if ! docker ps | grep -q oracle_xe3; then
                    echo "⚠️ Oracle XE3 no está ejecutándose. Iniciándolo..."
                    docker start oracle_xe3 2>/dev/null || echo "Oracle XE3 no existe o ya está iniciado"
                  fi
                  
                  # Desplegar usando configuración de producción
                  echo "📦 Desplegando con docker-compose-oracle-xe3.yml (PRODUCCIÓN)..."
                  $DC -f docker-compose-oracle-xe3.yml up -d
                '''
                echo "   🔍 Verificando salud de los servicios de PRODUCCIÓN..."
                sleep 15
                sh '''
                  echo "=== Estado de contenedores PRODUCCIÓN ==="
                  docker ps --format "table {{.Names}}\t{{.Ports}}\t{{.Status}}" | grep -E "(hospital|oracle)"
                  
                  echo "=== Verificando Backend PRODUCCIÓN ==="
                  timeout 30 bash -c 'until curl -f http://localhost:8080/health; do echo "Esperando backend..."; sleep 2; done' || echo "⚠️ Backend aún no responde"
                  
                  echo "=== Verificando Frontend PRODUCCIÓN ==="
                  timeout 30 bash -c 'until curl -f http://localhost:5173; do echo "Esperando frontend..."; sleep 2; done' || echo "⚠️ Frontend aún no responde"
                '''
                echo "✅ Despliegue de PRODUCCIÓN completado exitosamente"
                echo "🌐 URLs de acceso PRODUCCIÓN:"
                echo "   - Backend: http://localhost:8080"
                echo "   - Frontend: http://localhost:5173"
                echo "   - Base de datos: localhost:1523 (oracle_xe3)"
                echo "   - Admin Oracle: http://localhost:5503"
            } else {
                echo "⏭️  Saltando despliegue de PRODUCCIÓN (BUILD_DOCKER=${params.BUILD_DOCKER}, rama: ${env.BRANCH_NAME}, PR: ${env.CHANGE_ID})"
            }
        }
        
        stage('Deploy to Development') {
            if (params.BUILD_DOCKER && env.BRANCH_NAME == 'desarrollo' && !env.CHANGE_ID) {
                echo "🚀 Iniciando despliegue en ambiente de DESARROLLO..."
                echo "   🐳 Construyendo y desplegando con configuración DEV..."
                sh '''
                  # Construir backend DEV
                  echo "🔨 Construyendo backend para DESARROLLO..."
                  docker build -t hospital-pipeline-hospital-backend-dev .
                  
                  # Construir frontend DEV
                  echo "🎨 Construyendo frontend para DESARROLLO..."
                  docker build -f Dockerfile.frontend -t hospital-pipeline-hospital-frontend-dev .
                  
                  # Limpiar contenedores DEV anteriores si existen
                  echo "🧹 Limpiando contenedores DEV anteriores..."
                  docker stop hospital-backend-dev hospital-frontend-dev 2>/dev/null || true
                  docker rm hospital-backend-dev hospital-frontend-dev 2>/dev/null || true
                  
                  # Verificar Docker Compose
                  if command -v docker-compose >/dev/null 2>&1; then
                    DC="docker-compose"
                  elif docker compose version >/dev/null 2>&1; then
                    DC="docker compose"
                  else
                    echo "❌ docker-compose no está instalado. Instala con: sudo apt-get install -y docker-compose-plugin"; exit 1
                  fi
                  
                  # Verificar que Oracle XE esté disponible
                  echo "🗄️ Verificando Oracle Database para DEV..."
                  if ! docker ps | grep -q oracle_xe; then
                    echo "⚠️ Oracle XE no está ejecutándose. Iniciándolo..."
                    docker start oracle_xe 2>/dev/null || echo "Oracle XE no existe, será creado por docker-compose"
                  fi
                  
                  # Desplegar usando configuración DEV
                  echo "📦 Desplegando con docker-compose.dev.yml..."
                  $DC -f docker-compose.dev.yml up -d --build
                '''
                echo "   🔍 Verificando salud de los servicios DEV..."
                sleep 20
                sh '''
                  echo "=== Estado de contenedores DEV ==="
                  docker ps --format "table {{.Names}}\t{{.Ports}}\t{{.Status}}" | grep -E "(dev|oracle_xe)"
                  
                  echo "=== Verificando Backend DEV ==="
                  timeout 30 bash -c 'until curl -f http://localhost:8060/health; do echo "Esperando backend DEV..."; sleep 2; done' || echo "⚠️ Backend DEV aún no responde"
                  
                  echo "=== Verificando Frontend DEV ==="
                  timeout 30 bash -c 'until curl -f http://localhost:5180; do echo "Esperando frontend DEV..."; sleep 2; done' || echo "⚠️ Frontend DEV aún no responde"
                '''
                echo "✅ Despliegue en DESARROLLO completado exitosamente"
                echo "🌐 URLs de acceso DESARROLLO:"
                echo "   - Frontend DEV: http://localhost:5180"
                echo "   - Backend API DEV: http://localhost:8060"
                echo "   - Base de Datos: localhost:1521"
                echo "   - Swagger/OpenAPI: http://localhost:8060/swagger-ui"
            } else {
                echo "⏭️  Saltando despliegue de DESARROLLO (BUILD_DOCKER=${params.BUILD_DOCKER}, rama: ${env.BRANCH_NAME}, PR: ${env.CHANGE_ID})"
            }
        }
        
        stage('Deploy to QA') {
            if (params.BUILD_DOCKER && env.BRANCH_NAME == 'QA' && !env.CHANGE_ID) {
                echo "🚀 Iniciando despliegue en ambiente de QA..."
                echo "   🐳 Construyendo y desplegando con configuración QA..."
                sh '''
                  # Construir backend QA
                  echo "🔨 Construyendo backend para QA..."
                  docker build -t hospital-backend-qa .
                  
                  # Construir frontend QA
                  echo "🎨 Construyendo frontend para QA..."
                  docker build -f Dockerfile.frontend.qa -t hospital-frontend-qa .
                  
                  # Limpiar contenedores QA anteriores si existen
                  echo "🧹 Limpiando contenedores QA anteriores..."
                  docker stop hospital-backend-qa hospital-frontend-qa hospital-nginx-qa hospital-prometheus-qa hospital-grafana-qa 2>/dev/null || true
                  docker rm hospital-backend-qa hospital-frontend-qa hospital-nginx-qa hospital-prometheus-qa hospital-grafana-qa 2>/dev/null || true
                  
                  # Verificar Docker Compose
                  if command -v docker-compose >/dev/null 2>&1; then
                    DC="docker-compose"
                  elif docker compose version >/dev/null 2>&1; then
                    DC="docker compose"
                  else
                    echo "❌ docker-compose no está instalado. Instala con: sudo apt-get install -y docker-compose-plugin"; exit 1
                  fi
                  
                  # Desplegar usando configuración QA
                  echo "📦 Desplegando con docker-compose.qa.yml..."
                  $DC -f docker-compose.qa.yml up -d
                '''
                echo "   🔍 Verificando salud de los servicios QA..."
                sleep 20
                sh '''
                  echo "=== Estado de contenedores QA ==="
                  docker ps --format "table {{.Names}}\t{{.Ports}}\t{{.Status}}" | grep -E "(qa|QA)"
                  
                  echo "=== Verificando Backend QA ==="
                  timeout 30 bash -c 'until curl -f http://localhost:8090/health; do echo "Esperando backend QA..."; sleep 2; done' || echo "⚠️ Backend QA aún no responde"
                  
                  echo "=== Verificando Frontend QA ==="
                  timeout 30 bash -c 'until curl -f http://localhost:5174; do echo "Esperando frontend QA..."; sleep 2; done' || echo "⚠️ Frontend QA aún no responde"
                  
                  echo "=== Verificando Nginx Proxy QA ==="
                  timeout 30 bash -c 'until curl -f http://localhost:8083; do echo "Esperando Nginx QA..."; sleep 2; done' || echo "⚠️ Nginx QA aún no responde"
                '''
                echo "✅ Despliegue en QA completado exitosamente"
                echo "🌐 URLs de acceso QA:"
                echo "   - Frontend: http://localhost:5174"
                echo "   - Backend API: http://localhost:8090"
                echo "   - Nginx Proxy: http://localhost:8083"
                echo "   - SonarQube: http://localhost:9000"
                echo "   - Prometheus: http://localhost:9091"
                echo "   - Grafana: http://localhost:3001"
>>>>>>> 648dadd5258233ca3ac4f2574bd67d8933f60087
            } else {
                echo "⏭ Construcción de Docker omitida (BUILD_DOCKER=false)"
            }
        }
<<<<<<< HEAD

        stage('Send Technical Debt Report') {
            echo "Enviando reporte de deuda técnica para QA..."
            try {
                sh '''
                    echo "=== Enviando Reporte de Deuda Técnica QA ==="
                    # Esperar un poco para asegurar que el backend esté completamente iniciado
                    sleep 10
                    
                    # Verificar que el backend esté respondiendo
                    for i in {1..30}; do
                        if curl -f http://localhost:8090/q/health >/dev/null 2>&1; then
                            echo " Backend está disponible"
                            break
                        fi
                        echo "⏳ Esperando que el backend esté disponible... ($i/30)"
                        sleep 5
                    done
                    
                    # Enviar reporte de deuda técnica usando el endpoint específico
                    curl -X POST http://localhost:8090/api/email/technical-debt \\
                         -H "Content-Type: application/json" \\
                         -d '{
                             "projectKey": "hospital-backend-qa",
                             "projectName": "Hospital Backend - QA [RAMA QA]",
                             "recipientEmail": "jflores@unis.edu.gt"
                         }' || echo " Error enviando reporte de deuda técnica"
                    
                    echo "✅ Reporte de deuda técnica enviado"
                '''
            } catch (Exception e) {
                echo "⚠️ No se pudo enviar el reporte de deuda técnica: ${e.getMessage()}"
=======
        
        stage('Deploy to Production (prod branch)') {
            if (params.BUILD_DOCKER && env.BRANCH_NAME == 'prod' && !env.CHANGE_ID) {
                echo "🚀 Iniciando despliegue en ambiente de PRODUCCIÓN REAL (rama prod)..."
                echo "   ⚠️  ADVERTENCIA: Despliegue en producción REAL"
                
                // Solicitar confirmación manual en producción real
                input message: '¿Confirmar despliegue en PRODUCCIÓN REAL?', ok: 'Desplegar'
                
                echo "   🧹 Limpiando contenedores anteriores..."
                sh '''
                  if command -v docker-compose >/dev/null 2>&1; then
                    DC="docker-compose"
                  elif docker compose version >/dev/null 2>&1; then
                    DC="docker compose"
                  else
                    echo "docker-compose no está instalado. Instala con: sudo apt-get install -y docker-compose-plugin"; exit 1
                  fi
                  
                  # Detener contenedores de otros entornos
                  echo "🛑 Deteniendo contenedores de otros entornos..."
                  docker stop $(docker ps -q --filter name=hospital-backend-dev --filter name=hospital-frontend-dev --filter name=hospital-backend-qa --filter name=hospital-frontend-qa) 2>/dev/null || true
                  
                  # Mantener oracle_xe3 para producción
                  echo "✅ Preservando Oracle XE3 para producción"
                '''
                
                echo "   🐳 Construyendo y desplegando en PRODUCCIÓN..."
                sh '''
                  # Construir imágenes de producción
                  echo "🔨 Construyendo backend para PRODUCCIÓN REAL..."
                  docker build -t hospital-backend-local .
                  
                  echo "🎨 Construyendo frontend para PRODUCCIÓN REAL..."
                  docker build -f Dockerfile.frontend -t hospitalpipelineprod2-frontend .
                  
                  # Verificar Docker Compose
                  if command -v docker-compose >/dev/null 2>&1; then
                    DC="docker-compose"
                  elif docker compose version >/dev/null 2>&1; then
                    DC="docker compose"
                  else
                    echo "❌ docker-compose no está instalado."; exit 1
                  fi
                  
                  # Configurar red de producción
                  echo "🌐 Configurando red hospital-2_hospital-network..."
                  docker network create hospital-2_hospital-network 2>/dev/null || true
                  
                  # Asegurar Oracle XE3 disponible
                  echo "🗄️ Verificando Oracle XE3 para PRODUCCIÓN..."
                  if ! docker ps | grep -q oracle_xe3; then
                    echo "⚠️ Oracle XE3 no está ejecutándose. Iniciándolo..."
                    docker start oracle_xe3 2>/dev/null || echo "Oracle XE3 no existe"
                  fi
                  
                  # Conectar Oracle a la red de producción
                  docker network connect hospital-2_hospital-network oracle_xe3 2>/dev/null || true
                  
                  # Desplegar usando configuración de producción
                  echo "📦 Desplegando PRODUCCIÓN REAL con docker-compose-oracle-xe3.yml..."
                  $DC -f docker-compose-oracle-xe3.yml up -d
                '''
                
                echo "   🔍 Verificando salud de los servicios de PRODUCCIÓN REAL..."
                sleep 20
                sh '''
                  echo "=== Estado de contenedores PRODUCCIÓN REAL ==="
                  docker ps --format "table {{.Names}}\t{{.Ports}}\t{{.Status}}" | grep -E "(hospital|oracle)"
                  
                  echo "=== Verificando Backend PRODUCCIÓN ==="
                  timeout 60 bash -c 'until curl -f http://localhost:8080/health; do echo "Esperando backend PRODUCCIÓN..."; sleep 3; done' || echo "⚠️ Backend PRODUCCIÓN aún no responde"
                  
                  echo "=== Verificando Frontend PRODUCCIÓN ==="
                  timeout 60 bash -c 'until curl -f http://localhost:5173; do echo "Esperando frontend PRODUCCIÓN..."; sleep 3; done' || echo "⚠️ Frontend PRODUCCIÓN aún no responde"
                  
                  echo "=== Test básico de conectividad ==="
                  curl -f http://localhost:8080/faq || echo "⚠️ API FAQ no responde"
                '''
                
                echo "✅ Despliegue en PRODUCCIÓN REAL completado exitosamente"
                echo "🌐 URLs de acceso PRODUCCIÓN REAL:"
                echo "   - Backend: http://localhost:8080"
                echo "   - Frontend: http://localhost:5173"
                echo "   - Base de datos: localhost:1523 (oracle_xe3)"
                echo "   - Admin Oracle: http://localhost:5503"
            } else {
                echo "⏭️  Saltando despliegue de PRODUCCIÓN REAL (BUILD_DOCKER=${params.BUILD_DOCKER}, rama: ${env.BRANCH_NAME}, PR: ${env.CHANGE_ID})"
>>>>>>> 648dadd5258233ca3ac4f2574bd67d8933f60087
            }
        }
        
        // Notificación por correo de éxito
        try {
            def recipients = 'jflores@unis.edu.gt, jnajar@unis.edu.gt'
            def subject = (env.CHANGE_ID ? "PR #${env.CHANGE_ID} QA exitoso: ${env.JOB_NAME} #${env.BUILD_NUMBER}" : "Pipeline QA exitoso: ${env.JOB_NAME} #${env.BUILD_NUMBER} (Rama: ${env.BRANCH_NAME})")
            
            def body = """
Hola equipo,

El pipeline de QA se ha ejecutado exitosamente.

 INFORMACIÓN DEL BUILD QA:
- Job: ${env.JOB_NAME}
- Build: #${env.BUILD_NUMBER}
- Rama: ${env.BRANCH_NAME} [RAMA QA]
- URL: ${env.BUILD_URL}
- Estado:  EXITOSO

RESULTADOS DE CALIDAD QA:
- Tests Backend:  Completados
- Tests Frontend:  Completados
- Análisis SonarQube:  Completado
- Quality Gate: PASÓ

 URLs DE ACCESO QA:
- Backend: http://localhost:8090
- Frontend: http://localhost:5174
- SonarQube: http://localhost:9000
- Jenkins: ${env.BUILD_URL}

PROYECTOS SONARQUBE QA:
- Backend: hospital-backend-qa
- Frontend: hospital-frontend-qa

 REPORTE DE DEUDA TÉCNICA:
Se ha enviado automáticamente el reporte de deuda técnica con indicador [RAMA QA].

Saludos,
Sistema de CI/CD del Hospital - Entorno QA
"""
            // Usar Email Extension Plugin
            emailext(
                to: recipients,
                from: 'humbertovenavente7@gmail.com',
                subject: subject,
                body: body,
                mimeType: 'text/plain'
            )
            echo "✅ Notificación de éxito QA enviada a: ${recipients}"
        } catch (err) {
            echo "⚠️ No se pudo enviar la notificación por correo: ${err}"
        }
        
    } catch (Exception e) {
        // Error handling
        if (env.CHANGE_ID) {
            echo "❌ Pull Request #${env.CHANGE_ID} QA falló: ${e.getMessage()}"
        } else {
            echo "❌ Pipeline QA falló en rama ${env.BRANCH_NAME}: ${e.getMessage()}"
        }
        // Notificación por correo a Lead Developer y Product Owner
        try {
            def recipients = 'jflores@unis.edu.gt, jnajar@unis.edu.gt'
            def subject = (env.CHANGE_ID ? "PR #${env.CHANGE_ID} QA falló: ${env.JOB_NAME} #${env.BUILD_NUMBER}" : "Pipeline QA falló: ${env.JOB_NAME} #${env.BUILD_NUMBER} (Rama: ${env.BRANCH_NAME})")
            
            def body = """
Hola equipo,

El pipeline de QA ha fallado.

🔧 INFORMACIÓN DEL BUILD QA:
- Job: ${env.JOB_NAME}
- Build: #${env.BUILD_NUMBER}
- Rama: ${env.BRANCH_NAME} [RAMA QA]
- URL: ${env.BUILD_URL}
- Estado: ❌ FALLÓ
- Motivo: ${e.getMessage()}

📊 RESULTADOS DE CALIDAD QA:
- Tests Backend: ⚠️ Verificar estado
- Tests Frontend: ⚠️ Verificar estado
- Análisis SonarQube: ⚠️ Verificar estado

🔧 ACCIONES REQUERIDAS:
1. Revisar la consola de Jenkins para más detalles
2. Verificar logs de los servicios QA
3. Revisar métricas de SonarQube QA
4. Corregir el problema identificado

🌐 URLs DE ACCESO QA:
- Jenkins: ${env.BUILD_URL}
- SonarQube: http://localhost:9000
- Backend QA: http://localhost:8090
- Frontend QA: http://localhost:5174

Por favor revisar la consola para más detalles.

Saludos,
Sistema de CI/CD del Hospital - Entorno QA
"""
            // Usar Email Extension Plugin (configurado en "Extended E-mail Notification")
            emailext(
                to: recipients,
                from: 'humbertovenavente7@gmail.com',
                subject: subject,
                body: body,
                mimeType: 'text/plain'
            )
            echo "📧 Notificación de fallo QA enviada (emailext) a: ${recipients}"
        } catch (err) {
            echo "⚠️ No se pudo enviar la notificación por correo: ${err}"
        }
        throw e
    }
} 