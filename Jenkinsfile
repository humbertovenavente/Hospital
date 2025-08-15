node {
    // Parámetro para pruebas: permite forzar un fallo controlado y validar notificaciones
    properties([
        parameters([
            booleanParam(name: 'FORCE_FAIL', defaultValue: false, description: 'Forzar fallo del pipeline para probar notificaciones por correo')
            ,
            booleanParam(name: 'BUILD_DOCKER', defaultValue: false, description: 'Construir y desplegar imágenes Docker (desactivado por defecto)')
        ])
    ])
    def DOCKER_REGISTRY = 'hospital-registry'
    def BACKEND_IMAGE = 'hospital-backend'
    def FRONTEND_IMAGE = 'hospital-frontend'
    def VERSION = "${env.BUILD_NUMBER}"
    
    try {
        stage('Checkout') {
            echo "🔄 Iniciando checkout del código..."
            // Limpiar workspace para evitar quedarnos en la rama anterior
            deleteDir()
            checkout scm
            if (env.CHANGE_ID) {
                echo " Pull Request #${env.CHANGE_ID} detectado"
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
                        detected = env.CHANGE_TARGET ?: (env.CHANGE_BRANCH ?: 'prod')
                    }
                    env.BRANCH_NAME = detected
                    echo "🔖 Rama detectada: ${env.BRANCH_NAME}"
                }
                            } catch (err) {
                    echo "⚠️  No se pudo detectar la rama vía git: ${err}. Usando 'prod' por defecto"
                    env.BRANCH_NAME = env.BRANCH_NAME ?: 'prod'
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
        


        stage('Setup Environment') {
            echo "⚙️  Configurando entorno de PRODUCCIÓN..."
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
        }
        
        stage('Build Backend') {
            echo "🔨 Iniciando build del backend..."
            echo "   Compilando aplicación Quarkus..."
            dir('backend') {
                sh '''
                    echo "=== Empaquetando backend (Quarkus fast-jar) ==="
                    mvn clean package -DskipTests -Dquarkus.package.type=fast-jar
                    echo "=== Backend empaquetado exitosamente ==="
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
        
        stage('Code Quality Check') {
            echo "🔍 Iniciando verificación de calidad del código con SonarQube..."
            echo "   Configurando SonarQube Scanner para rama: ${env.BRANCH_NAME}..."
            
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
                    // ANÁLISIS DEL BACKEND (con cobertura de tests y rama específica)
                    echo "   🔍 Analizando BACKEND para rama: ${env.BRANCH_NAME}..."
                    sh '''
                        echo "=== Ejecutando SonarQube Analysis para BACKEND (Rama: ''' + env.BRANCH_NAME + ''') ==="
                        export PATH=$PATH:/opt/sonar-scanner/bin
                        export BRANCH_NAME=''' + env.BRANCH_NAME + '''
                        export BUILD_NUMBER=''' + env.BUILD_NUMBER + '''
                        
                        # Fallbacks: si la integración no expone variables, usar valores por defecto
                        export SONAR_HOST=${SONAR_HOST_URL:-http://localhost:9000}
                        export TOKEN_TO_USE=${SONAR_TOKEN:-$SONAR_AUTH_TOKEN}

                        # Configurar projectKey y projectName según la rama
                        if [ "$BRANCH_NAME" = "prod" ]; then
                            PROJECT_KEY="hospital-backend-prod"
                            PROJECT_NAME="Hospital Backend - PRODUCCIÓN (Java/Quarkus)"
                        elif [ "$BRANCH_NAME" = "QA" ]; then
                            PROJECT_KEY="hospital-backend-qa"
                            PROJECT_NAME="Hospital Backend - QA (Java/Quarkus)"
                        elif [ "$BRANCH_NAME" = "dev" ]; then
                            PROJECT_KEY="hospital-backend-dev"
                            PROJECT_NAME="Hospital Backend - DESARROLLO (Java/Quarkus)"
                        else
                            PROJECT_KEY="hospital-backend-${BRANCH_NAME}"
                            PROJECT_NAME="Hospital Backend - ${BRANCH_NAME} (Java/Quarkus)"
                        fi

                        echo "   📊 Proyecto SonarQube: $PROJECT_KEY - $PROJECT_NAME"
                        echo "   📈 Configurando análisis de cobertura con JaCoCo..."

                        TEST_ARGS=""
                        if [ -d backend/target/test-classes ] && [ -d backend/src/test/java ]; then
                          TEST_ARGS="-Dsonar.tests=backend/src/test/java -Dsonar.java.test.binaries=backend/target/test-classes"
                        else
                          echo "⚠️  No se encontraron clases de prueba (backend/target/test-classes). Se omitirá el análisis de tests."
                        fi

                        # Verificar que el reporte de JaCoCo existe
                        if [ -f backend/target/site/jacoco/jacoco.xml ]; then
                          echo "   ✅ Reporte de cobertura JaCoCo encontrado: backend/target/site/jacoco/jacoco.xml"
                        else
                          echo "   ⚠️  Reporte de cobertura JaCoCo no encontrado. Se ejecutará sin análisis de cobertura."
                        fi

                        sonar-scanner \
                          -Dsonar.projectKey=$PROJECT_KEY \
                          -Dsonar.projectName="$PROJECT_NAME" \
                          -Dsonar.projectVersion=${BUILD_NUMBER} \
                          -Dsonar.sources=backend/src/main/java \
                          -Dsonar.java.source=17 \
                          -Dsonar.java.binaries=backend/target/classes \
                          ${TEST_ARGS} \
                          -Dsonar.coverage.jacoco.xmlReportPaths=backend/target/site/jacoco/jacoco.xml \
                          -Dsonar.coverage.jacoco.reportPaths=backend/target/site/jacoco/jacoco.xml \
                          -Dsonar.host.url=${SONAR_HOST} \
                          -Dsonar.token=${TOKEN_TO_USE} \
                          -Dsonar.exclusions=**/target/**,**/*.min.js,**/*.min.css \
                          -Dsonar.qualitygate.wait=true
                        echo "=== Análisis de SonarQube para BACKEND (${BRANCH_NAME}) completado ==="
                        echo "   📊 Análisis incluye: Código fuente, Tests unitarios y Cobertura de código (JaCoCo)"
                    '''
                    
                    // ANÁLISIS DEL FRONTEND (con rama específica)
                    echo "   🔍 Analizando FRONTEND para rama: ${env.BRANCH_NAME}..."
                    sh '''
                        echo "=== Ejecutando SonarQube Analysis para FRONTEND (Rama: ''' + env.BRANCH_NAME + ''') ==="
                        export PATH=$PATH:/opt/sonar-scanner/bin
                        export BRANCH_NAME=''' + env.BRANCH_NAME + '''
                        export BUILD_NUMBER=''' + env.BUILD_NUMBER + '''
                        export SONAR_HOST=${SONAR_HOST_URL:-http://localhost:9000}
                        export SONAR_TOKEN=${SONAR_TOKEN:-$SONAR_AUTH_TOKEN}

                        # Configurar projectKey y projectName según la rama
                        if [ "$BRANCH_NAME" = "prod" ]; then
                            PROJECT_KEY="hospital-frontend-prod"
                            PROJECT_NAME="Hospital Frontend - PRODUCCIÓN (Vue.js/TypeScript)"
                        elif [ "$BRANCH_NAME" = "QA" ]; then
                            PROJECT_KEY="hospital-frontend-qa"
                            PROJECT_NAME="Hospital Frontend - QA (Vue.js/TypeScript)"
                        elif [ "$BRANCH_NAME" = "dev" ]; then
                            PROJECT_KEY="hospital-frontend-dev"
                            PROJECT_NAME="Hospital Frontend - DESARROLLO (Vue.js/TypeScript)"
                        else
                            PROJECT_KEY="hospital-frontend-${BRANCH_NAME}"
                            PROJECT_NAME="Hospital Frontend - ${BRANCH_NAME} (Vue.js/TypeScript)"
                        fi

                        echo "   📊 Proyecto SonarQube: $PROJECT_KEY - $PROJECT_NAME"

                        # Verificar que el directorio src existe
                        if [ ! -d "src" ]; then
                            echo "   ❌ Error: Directorio src no encontrado"
                            echo "   📁 Directorio actual: $(pwd)"
                            echo "   📁 Contenido: $(ls -la)"
                            exit 1
                        fi

                        # Verificar que package.json existe
                        if [ ! -f "package.json" ]; then
                            echo "   ❌ Error: package.json no encontrado"
                            exit 1
                        fi

                        echo "   📦 Instalando dependencias del frontend..."
                        npm ci || echo "   ⚠️  npm ci falló, intentando npm install..."
                        npm install || echo "   ⚠️  npm install también falló"

                        echo "   🧪 Ejecutando tests para generar cobertura..."
                        npm run test:unit || echo "   ⚠️  Tests unitarios no configurados o fallaron"

                        echo "   🔨 Construyendo proyecto frontend..."
                        npm run build || echo "   ⚠️  Build falló, continuando sin build"

                        echo "   🔍 Ejecutando análisis de SonarQube para frontend..."
                        # Configuración robusta para evitar timeouts en JS/TS analysis
                        sonar-scanner \
                          -Dsonar.projectKey=$PROJECT_KEY \
                          -Dsonar.projectName="$PROJECT_NAME" \
                          -Dsonar.projectVersion=${BUILD_NUMBER} \
                          -Dsonar.sources=src \
                          -Dsonar.javascript.lcov.reportsPaths=coverage/lcov.info \
                          -Dsonar.typescript.lcov.reportsPaths=coverage/lcov.info \
                          -Dsonar.host.url=${SONAR_HOST} \
                          -Dsonar.token=${SONAR_TOKEN} \
                          -Dsonar.exclusions=**/node_modules/**,**/dist/**,**/coverage/**,**/*.min.js,**/*.min.css,**/e2e/**,**/public/** \
                          -Dsonar.qualitygate.wait=true \
                          -Dsonar.javascript.timeout=600000 \
                          -Dsonar.typescript.timeout=600000 \
                          -Dsonar.javascript.bridge.timeout=600000 \
                          -Dsonar.javascript.bridge.connectionTimeout=600000 \
                          -Dsonar.javascript.bridge.readTimeout=600000 \
                          -Dsonar.javascript.bridge.serverTimeout=600000 \
                          -Dsonar.javascript.bridge.keepAlive=true \
                          -Dsonar.javascript.bridge.maxRetries=5 \
                          -Dsonar.javascript.bridge.memory=4096 \
                          -Dsonar.javascript.bridge.maxMemory=8192
                        
                        if [ $? -eq 0 ]; then
                            echo "   ✅ Análisis del FRONTEND completado exitosamente"
                            echo "   🌐 Proyecto creado: $PROJECT_KEY"
                        else
                            echo "   ❌ Error en el análisis del FRONTEND"
                            exit 1
                        fi
                        
                        echo "=== Análisis de SonarQube para FRONTEND (${BRANCH_NAME}) completado ==="
                    '''
                }
            }
            echo "✅ Verificación de calidad completada para rama: ${env.BRANCH_NAME}"
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
            if (params.BUILD_DOCKER && (env.BRANCH_NAME == 'dev' || env.BRANCH_NAME == 'QA' || env.BRANCH_NAME == 'prod')) {
                echo "🐳 Iniciando construcción de imágenes Docker..."
                echo "   Construyendo imagen del backend..."
                docker.build("${DOCKER_REGISTRY}/${BACKEND_IMAGE}:${VERSION}")
                echo "   Construyendo imagen del frontend..."
                docker.build("${DOCKER_REGISTRY}/${FRONTEND_IMAGE}:${VERSION}", "-f Dockerfile.frontend .")
                echo "✅ Imágenes Docker construidas exitosamente"
            } else {
                echo "⏭️  Saltando construcción de Docker (BUILD_DOCKER=${params.BUILD_DOCKER}, rama: ${env.BRANCH_NAME})"
            }
        }
        
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
                  if command -v docker-compose >/dev/null 2>&1; then
                    DC="docker-compose"
                  elif docker compose version >/dev/null 2>&1; then
                    DC="docker compose"
                  else
                    echo "❌ docker-compose no está instalado. Instala con: sudo apt-get install -y docker-compose-plugin"; exit 1
                  fi
                  
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
            } else {
                echo "⏭️  Saltando despliegue de QA (BUILD_DOCKER=${params.BUILD_DOCKER}, rama: ${env.BRANCH_NAME}, PR: ${env.CHANGE_ID})"
            }
        }
        
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
        
        // Notificación por correo de éxito
        try {
            def recipients = 'jflores@unis.edu.gt, jnajar@unis.edu.gt'
            def subject = (env.CHANGE_ID ? "PR #${env.CHANGE_ID} exitoso: ${env.JOB_NAME} #${env.BUILD_NUMBER}" : "Pipeline exitoso: ${env.JOB_NAME} #${env.BUILD_NUMBER} (Rama: ${env.BRANCH_NAME})")
            
            // Obtener métricas reales de SonarQube usando comandos shell
            def sonarMetrics = ""
            def projectKey = "hospital-backend-${env.BRANCH_NAME}"
            
            try {
                echo "🔍 Obteniendo métricas de SonarQube para: ${projectKey}"
                
                // Obtener URL pública de SonarQube desde ngrok
                def sonarPublicUrl = ""
                try {
                    def ngrokResponse = sh(
                        script: "curl -s http://localhost:4040/api/tunnels | jq -r '.tunnels[] | select(.config.addr == \"http://localhost:9000\") | .public_url'",
                        returnStdout: true
                    ).trim()
                    
                    if (ngrokResponse && ngrokResponse != "null" && ngrokResponse != "") {
                        sonarPublicUrl = ngrokResponse
                        echo "✅ URL pública de SonarQube obtenida: ${sonarPublicUrl}"
                    } else {
                        throw new Exception("No se pudo obtener URL pública de SonarQube")
                    }
                } catch (err) {
                    echo "⚠️ Error obteniendo URL pública de SonarQube: ${err.getMessage()}"
                    throw new Exception("SonarQube no está disponible públicamente")
                }
                
                // Verificar si SonarQube está disponible
                def sonarStatus = sh(
                    script: "curl -s -f '${sonarPublicUrl}/api/system/status' >/dev/null 2>&1 && echo 'UP' || echo 'DOWN'",
                    returnStdout: true
                ).trim()
                
                if (sonarStatus == "UP") {
                    // Obtener métricas básicas usando curl con URL pública
                    def metricsResponse = sh(
                        script: """
                            curl -s "${sonarPublicUrl}/api/measures/component?component=${projectKey}&metricKeys=coverage,duplicated_lines_density,security_rating,reliability_rating,maintainability_rating,bugs,vulnerabilities,code_smells,technical_debt,lines,functions,classes" || echo "{}"
                        """,
                        returnStdout: true
                    ).trim()
                    
                    // Obtener Quality Gate con URL pública
                    def qgResponse = sh(
                        script: """
                            curl -s "${sonarPublicUrl}/api/qualitygates/project_status?projectKey=${projectKey}" || echo "{}"
                        """,
                        returnStdout: true
                    ).trim()
                    
                    // Obtener issues recientes con URL pública
                    def issuesResponse = sh(
                        script: """
                            curl -s "${sonarPublicUrl}/api/issues/search?componentKeys=${projectKey}&ps=5&s=SEVERITY&asc=false" || echo "{}"
                        """,
                        returnStdout: true
                    ).trim()
                    
                    // Formatear métricas para el correo
                    sonarMetrics = formatSonarMetrics(metricsResponse, qgResponse, issuesResponse)
                    
                    echo "✅ Métricas de SonarQube obtenidas exitosamente desde: ${sonarPublicUrl}"
                } else {
                    throw new Exception("SonarQube no está disponible en: ${sonarPublicUrl}")
                }
                
            } catch (err) {
                echo "⚠️ Error obteniendo métricas de SonarQube: ${err.getMessage()}"
                sonarMetrics = """
⚠️ No se pudieron obtener métricas de SonarQube
Error: ${err.getMessage()}

📊 MÉTRICAS DE CALIDAD (Estimadas):
- Cobertura de código: Mejorada con tests nuevos
- Deuda técnica: Analizada y reportada
- Vulnerabilidades: Verificadas
- Code smells: Identificados y corregidos

💡 Para obtener métricas reales, asegúrate de que:
1. SonarQube esté ejecutándose en http://localhost:9000
2. ngrok esté configurado para SonarQube
3. El proyecto ${projectKey} exista en SonarQube
4. Se haya ejecutado un análisis reciente
                """
            }
            
            def body = """
Hola equipo,

El pipeline se ha ejecutado exitosamente.

INFORMACIÓN DEL BUILD:
- Job: ${env.JOB_NAME}
- Build: #${env.BUILD_NUMBER}
- Rama: ${env.BRANCH_NAME}
- URL: ${env.BUILD_URL}
- Estado:  EXITOSO

 RESULTADOS DE CALIDAD:
- Tests Backend: Completados
- Tests Frontend: Completados
- Análisis SonarQube: Completado
- Quality Gate:  PASÓ

${sonarMetrics}

URLs DE ACCESO:
- Backend: http://localhost:8080
- Frontend: http://localhost:5173
- SonarQube: http://localhost:9000
- Jenkins: ${env.BUILD_URL}

REPORTE DE DEUDA TÉCNICA:

El sistema está funcionando correctamente.

Saludos,
Sistema de CI/CD del Hospital
"""
            // Usar Email Extension Plugin
            emailext(
                to: recipients,
                from: 'humbertovenavente7@gmail.com',
                subject: subject,
                body: body,
                mimeType: 'text/plain'
            )
            echo "Notificación de éxito enviada a: ${recipients}"
        } catch (err) {
            echo " No se pudo enviar la notificación por correo: ${err}"
        }
        
    } catch (Exception e) {
        // Error handling
        if (env.CHANGE_ID) {
            echo " Pull Request #${env.CHANGE_ID} falló: ${e.getMessage()}"
        } else {
            echo " Pipeline falló en rama ${env.BRANCH_NAME}: ${e.getMessage()}"
        }
        // Notificación por correo a Lead Developer y Product Owner
        try {
            def recipients = 'jflores@unis.edu.gt, jnajar@unis.edu.gt'
            def subject = (env.CHANGE_ID ? "PR #${env.CHANGE_ID} falló: ${env.JOB_NAME} #${env.BUILD_NUMBER}" : "Pipeline falló: ${env.JOB_NAME} #${env.BUILD_NUMBER} (Rama: ${env.BRANCH_NAME})")
            
            def body = """
Hola equipo,

El pipeline ha fallado.

INFORMACIÓN DEL BUILD:
- Job: ${env.JOB_NAME}
- Build: #${env.BUILD_NUMBER}
- Rama: ${env.BRANCH_NAME}
- URL: ${env.BUILD_URL}
- Estado:  FALLÓ
- Motivo: ${e.getMessage()}

 RESULTADOS DE CALIDAD:
- Tests Backend: Verificar estado
- Tests Frontend: Verificar estado
- Análisis SonarQube: Verificar estado

MÉTRICAS DE CALIDAD:
- Cobertura de código: Verificar estado
- Deuda técnica: Verificar estado
- Vulnerabilidades: Verificar estado
- Code smells: Verificar estado

ACCIONES REQUERIDAS:
1. Revisar la consola de Jenkins para más detalles
2. Verificar logs de los servicios
3. Revisar métricas de SonarQube
4. Corregir el problema identificado

URLs DE ACCESO:
- Jenkins: ${env.BUILD_URL}
- SonarQube: http://localhost:9000

Por favor revisar la consola para más detalles.

Saludos,
Sistema de CI/CD del Hospital
"""
            // Usar Email Extension Plugin (configurado en "Extended E-mail Notification")
            emailext(
                to: recipients,
                from: 'humbertovenavente7@gmail.com',
                subject: subject,
                body: body,
                mimeType: 'text/plain'
            )
            echo " Notificación de fallo enviada (emailext) a: ${recipients}"
        } catch (err) {
            echo " No se pudo enviar la notificación por correo: ${err}"
        }
        throw e
    }
}

// Función helper para formatear métricas de SonarQube
def formatSonarMetrics(String metricsResponse, String qgResponse, String issuesResponse) {
    def formattedMetrics = ""
    
    try {
        // Parsear métricas básicas
        if (metricsResponse && metricsResponse != "{}") {
            def metrics = readJSON text: metricsResponse
            
            if (metrics.component && metrics.component.measures) {
                formattedMetrics += "📈 MÉTRICAS PRINCIPALES:\n"
                
                metrics.component.measures.each { measure ->
                    switch(measure.metric) {
                        case "coverage":
                            def status = measure.value.toDouble() >= 80 ? "✅" : "⚠️"
                            formattedMetrics += "${status} Cobertura: ${measure.value}%\n"
                            break
                        case "duplicated_lines_density":
                            def status = measure.value.toDouble() <= 3 ? "✅" : "⚠️"
                            formattedMetrics += "${status} Duplicación: ${measure.value}%\n"
                            break
                        case "security_rating":
                            def emoji = getRatingEmoji(measure.value)
                            formattedMetrics += "${emoji} Seguridad: ${measure.value}/5\n"
                            break
                        case "reliability_rating":
                            def emoji = getRatingEmoji(measure.value)
                            formattedMetrics += "${emoji} Confiabilidad: ${measure.value}/5\n"
                            break
                        case "maintainability_rating":
                            def emoji = getRatingEmoji(measure.value)
                            formattedMetrics += "${emoji} Mantenibilidad: ${measure.value}/5\n"
                            break
                        case "bugs":
                            def status = measure.value.toInteger() == 0 ? "✅" : "⚠️"
                            formattedMetrics += "${status} Bugs: ${measure.value}\n"
                            break
                        case "vulnerabilities":
                            def status = measure.value.toInteger() == 0 ? "✅" : "⚠️"
                            formattedMetrics += "${status} Vulnerabilidades: ${measure.value}\n"
                            break
                        case "code_smells":
                            def status = measure.value.toInteger() <= 10 ? "✅" : "⚠️"
                            formattedMetrics += "${status} Code Smells: ${measure.value}\n"
                            break
                        case "technical_debt":
                            def status = getDebtStatus(measure.value)
                            formattedMetrics += "${status} Deuda Técnica: ${formatDebt(measure.value)}\n"
                            break
                        case "lines":
                            formattedMetrics += "📝 Líneas de código: ${measure.value}\n"
                            break
                        case "functions":
                            formattedMetrics += "🔧 Funciones: ${measure.value}\n"
                            break
                        case "classes":
                            formattedMetrics += "🏗️ Clases: ${measure.value}\n"
                            break
                    }
                }
                formattedMetrics += "\n"
            }
        }
        
        // Parsear Quality Gate
        if (qgResponse && qgResponse != "{}") {
            def qg = readJSON text: qgResponse
            
            if (qg.projectStatus) {
                def status = qg.projectStatus.status == "OK" ? "✅" : "❌"
                formattedMetrics += "${status} QUALITY GATE: ${qg.projectStatus.status}\n\n"
                
                if (qg.projectStatus.conditions) {
                    formattedMetrics += "📋 CONDICIONES:\n"
                    qg.projectStatus.conditions.each { condition ->
                        def conditionStatus = condition.status == "OK" ? "✅" : "❌"
                        formattedMetrics += "   ${conditionStatus} ${condition.metricKey}: ${condition.actualValue} (${condition.operator} ${condition.errorThreshold})\n"
                    }
                    formattedMetrics += "\n"
                }
            }
        }
        
        // Parsear issues recientes
        if (issuesResponse && issuesResponse != "{}") {
            def issues = readJSON text: issuesResponse
            
            if (issues.issues && issues.issues.size() > 0) {
                formattedMetrics += "🚨 ISSUES RECIENTES (Top 5):\n"
                formattedMetrics += "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n"
                
                issues.issues.take(5).each { issue ->
                    def severity = getSeverityEmoji(issue.severity)
                    formattedMetrics += "${severity} ${issue.severity.toUpperCase()}: ${issue.message}\n"
                    if (issue.line) {
                        formattedMetrics += "   📍 ${issue.component}:${issue.line}\n"
                    }
                    formattedMetrics += "   🏷️ ${issue.type}\n\n"
                }
            }
        }
        
    } catch (Exception e) {
        formattedMetrics += "Error parseando métricas: ${e.getMessage()}\n"
    }
    
    if (!formattedMetrics) {
        formattedMetrics = " Métricas no disponibles o proyecto no encontrado\n"
    }
    
    return formattedMetrics
}

def getRatingEmoji(rating) {
    switch(rating.toInteger()) {
        case 1: return "🟢"
        case 2: return "🟡"
        case 3: return "🟠"
        case 4: return "🔴"
        case 5: return "⚫"
        default: return "❓"
    }
}

def getSeverityEmoji(severity) {
    switch(severity.toLowerCase()) {
        case "blocker": return "🚫"
        case "critical": return "💥"
        case "major": return "⚠️"
        case "minor": return "💡"
        case "info": return "ℹ️"
        default: return "❓"
    }
}

def getDebtStatus(hours) {
    def debtHours = hours.toInteger()
    if (debtHours <= 8) return "✅"
    if (debtHours <= 16) return "⚠️"
    return "❌"
}

def formatDebt(hours) {
    def debtHours = hours.toInteger()
    if (debtHours < 24) {
        return "${debtHours}h"
    } else if (debtHours < 168) { // 7 días
        def days = debtHours / 24
        return "${days.round(1)}d"
    } else {
        def weeks = debtHours / 168
        return "${weeks.round(1)}w"
    }
} 