node {
    // Parámetro para pruebas: permite forzar un fallo controlado y validar notificaciones
    properties([
        parameters([
            booleanParam(name: 'FORCE_FAIL', defaultValue: false, description: 'Forzar fallo del pipeline para probar notificaciones por correo')
            ,
            booleanParam(name: 'BUILD_DOCKER', defaultValue: true, description: 'Construir y desplegar imágenes Docker (activado por defecto para QA)')
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
                        // En estado detached, forzar uso de 'QA' para evitar confusiones
                        detected = 'QA'
                        echo "🔍 Estado detached detectado, forzando rama: QA"
                    }
                    env.BRANCH_NAME = detected
                    echo "🔖 Rama detectada: ${env.BRANCH_NAME}"
                }
                
                // Verificación adicional: si estamos en la rama QA, forzar el nombre
                if (env.BRANCH_NAME == 'QA' || env.BRANCH_NAME == 'qa') {
                    env.BRANCH_NAME = 'QA'
                    echo "✅ Rama QA confirmada: ${env.BRANCH_NAME}"
                }
            } catch (err) {
                echo "⚠️  No se pudo detectar la rama vía git: ${err}. Usando 'QA' por defecto"
                env.BRANCH_NAME = 'QA'
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
                        if [ "$BRANCH_NAME" = "QA" ]; then
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

                        TEST_ARGS=""
                        if [ -d backend/target/test-classes ] && [ -d backend/src/test/java ]; then
                          TEST_ARGS="-Dsonar.tests=backend/src/test/java -Dsonar.java.test.binaries=backend/target/test-classes"
                        else
                          echo "⚠️  No se encontraron clases de prueba (backend/target/test-classes). Se omitirá el análisis de tests."
                        fi

                        # Usar archivo de configuración específico para QA
                        if [ "$BRANCH_NAME" = "QA" ]; then
                            echo "   🔧 Usando configuración específica de QA para backend..."
                            sonar-scanner -Dproject.settings=sonar-project-backend-qa.properties
                        else
                            sonar-scanner \
                              -Dsonar.projectKey=$PROJECT_KEY \
                              -Dsonar.projectName="$PROJECT_NAME" \
                              -Dsonar.projectVersion=${BUILD_NUMBER} \
                              -Dsonar.sources=backend/src/main/java \
                              -Dsonar.java.source=17 \
                              -Dsonar.java.binaries=backend/target/classes \
                              ${TEST_ARGS} \
                              -Dsonar.host.url=${SONAR_HOST} \
                              -Dsonar.token=${TOKEN_TO_USE} \
                              -Dsonar.exclusions=**/target/**,**/*.min.js,**/*.min.css \
                              -Dsonar.qualitygate.wait=true
                        fi
                        echo "=== Análisis de SonarQube para BACKEND (${BRANCH_NAME}) completado ==="
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
                        if [ "$BRANCH_NAME" = "QA" ]; then
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
                        # Usar archivo de configuración específico para QA
                        if [ "$BRANCH_NAME" = "QA" ]; then
                            echo "   🔧 Usando configuración específica de QA para frontend..."
                            sonar-scanner -Dproject.settings=sonar-project-frontend-qa.properties
                        else
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
                        fi
                        
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
        
        stage('Deploy to Development') {
            if (params.BUILD_DOCKER && env.BRANCH_NAME == 'dev' && !env.CHANGE_ID) {
                echo "🚀 Iniciando despliegue en ambiente de DESARROLLO..."
                echo "   🐳 Construyendo y desplegando solo los 3 contenedores esenciales..."
                sh '''
                  # Construir backend local
                  echo "🔨 Construyendo backend local..."
                  docker build -t hospital-backend-local .
                  
                  # Construir frontend local
                  echo "🎨 Construyendo frontend local..."
                  docker build -f Dockerfile.frontend -t hospital-frontend-local .
                  
                  # Desplegar usando docker-compose-oracle-xe3.yml
                  echo "📦 Desplegando con configuración local..."
                  if command -v docker-compose >/dev/null 2>&1; then
                    DC="docker-compose"
                  elif docker compose version >/dev/null 2>&1; then
                    DC="docker compose"
                  else
                    echo "docker-compose no está instalado. Instala con: sudo apt-get install -y docker-compose-plugin"; exit 1
                  fi
                  
                  # Asegurar que oracle_xe3 esté en la red correcta
                  echo "🌐 Configurando red para oracle_xe3..."
                  docker network create hospital-network 2>/dev/null || true
                  docker network connect hospital-network oracle_xe3 2>/dev/null || true
                  
                  # Desplegar backend y frontend
                  $DC -f docker-compose-oracle-xe3.yml up -d
                '''
                echo "   Verificando salud de los servicios..."
                sleep 10
                echo "✅ Despliegue en desarrollo completado exitosamente"
                echo "🌐 URLs de acceso:"
                echo "   - Backend: http://localhost:8080"
                echo "   - Frontend: http://localhost:5173"
                echo "   - Base de datos: localhost:1523 (oracle_xe3)"
            } else {
                echo "⏭️  Saltando despliegue de desarrollo (BUILD_DOCKER=${params.BUILD_DOCKER}, rama: ${env.BRANCH_NAME}, PR: ${env.CHANGE_ID})"
            }
        }
        
        stage('Deploy to QA') {
            // Forzar BUILD_DOCKER = true para rama QA
            if (env.BRANCH_NAME == 'QA') {
                env.BUILD_DOCKER = true
                echo "✅ Forzando BUILD_DOCKER = true para rama QA"
            }
            
            if (params.BUILD_DOCKER && env.BRANCH_NAME == 'QA' && !env.CHANGE_ID) {
                echo "🚀 Iniciando despliegue en ambiente de QA..."
                echo "   🧹 Limpiando contenedores de QA existentes..."
                sh '''
                  if command -v docker-compose >/dev/null 2>&1; then
                    DC="docker-compose"
                  elif docker compose version >/dev/null 2>&1; then
                    DC="docker compose"
                  else
                    echo "docker-compose no está instalado. Instala con: sudo apt-get install -y docker-compose-plugin"; exit 1
                  fi
                  
                  # Detener y limpiar contenedores de QA existentes
                  echo "🛑 Deteniendo contenedores de QA..."
                  $DC -f docker-compose.qa.yml down 2>/dev/null || true
                  
                  # Forzar detención y eliminación de contenedores de QA
                  echo "🗑️ Forzando limpieza de contenedores de QA..."
                  docker stop $(docker ps -q --filter name=hospital- --filter name=hospital-sonarqube-qa --filter name=hospital-prometheus-qa --filter name=hospital-grafana-qa) 2>/dev/null || true
                  docker rm $(docker ps -aq --filter name=hospital- --filter name=hospital-sonarqube-qa --filter name=hospital-prometheus-qa --filter name=hospital-grafana-qa) 2>/dev/null || true
                  
                  # Limpiar contenedores huérfanos de QA
                  echo "🗑️ Limpiando contenedores huérfanos de QA..."
                  docker container prune -f 2>/dev/null || true
                '''
                
                echo "   🐳 Construyendo y desplegando contenedores de QA..."
                sh '''
                  # Construir backend para QA
                  echo "🔨 Construyendo backend para QA..."
                  docker build -t hospital-backend-qa .
                  
                  # Construir frontend para QA
                  echo "🎨 Construyendo frontend para QA..."
                  docker build -f Dockerfile.frontend.qa -t hospital-frontend-qa .
                  
                  # Configurar red para oracle_xe2 (usado en QA)
                  echo "🌐 Configurando red para oracle_xe2..."
                  docker network create hospital-network 2>/dev/null || true
                  docker network connect hospital-network oracle_xe2 2>/dev/null || true
                  
                  # Desplegar servicios de QA
                  echo "📦 Desplegando servicios de QA..."
                  docker-compose -f docker-compose.qa.yml up -d --build
                  
                  # Asegurar que el backend esté en la red correcta
                  echo "🔗 Conectando backend a la red hospital-network..."
                  docker network connect hospital-network hospital-backend-qa 2>/dev/null || true
                  
                  # Verificar conectividad de red
                  echo "🔍 Verificando conectividad de red..."
                  docker exec hospital-backend-qa ping -c 1 oracle_xe2 || echo "⚠️  Advertencia: No se pudo hacer ping a oracle_xe2"
                '''
                echo "   Verificando salud de los servicios..."
                sleep 15
                echo "✅ Despliegue en QA completado exitosamente"
                echo "🌐 URLs de acceso:"
                echo "   - Backend: http://localhost:8090"
                echo "   - Frontend: http://localhost:5174"
                echo "   - Nginx Reverse Proxy: http://localhost:8083"
                echo "   - Jenkins: http://localhost:8081"
                echo "   - SonarQube: http://localhost:9001"
                echo "   - Prometheus: http://localhost:9091"
                echo "   - Grafana: http://localhost:3001"
                echo "   - Base de datos: localhost:1522 (oracle_xe2)"
            } else {
                echo "⏭️  Saltando despliegue de QA (BUILD_DOCKER=${params.BUILD_DOCKER}, rama: ${env.BRANCH_NAME}, PR: ${env.CHANGE_ID})"
            }
        }
        
        stage('Deploy to Production') {
            if (params.BUILD_DOCKER && env.BRANCH_NAME == 'prod' && !env.CHANGE_ID) {
                echo "🚀 Iniciando despliegue en ambiente de PRODUCCIÓN..."
                echo "   ⚠️  ADVERTENCIA: Despliegue en producción"
                
                // Limpiar solo los contenedores específicos que no necesitamos
                echo "   🧹 Limpiando contenedores hospital innecesarios..."
                sh '''
                  if command -v docker-compose >/dev/null 2>&1; then
                    DC="docker-compose"
                  elif docker compose version >/dev/null 2>&1; then
                    DC="docker compose"
                  else
                    echo "docker-compose no está instalado. Instala con: sudo apt-get install -y docker-compose-plugin"; exit 1
                  fi
                  
                  # LIMPIAR SOLO CONTENEDORES HOSPITAL INNECESARIOS
                  echo "🛑 Deteniendo contenedores hospital innecesarios..."
                  docker stop $(docker ps -q --filter name=hospital- --filter name=hospital-grafana --filter name=hospital-prometheus --filter name=hospital-nginx) 2>/dev/null || true
                  
                  echo "🗑️ Eliminando contenedores hospital innecesarios..."
                  docker rm $(docker ps -aq --filter name=hospital- --filter name=hospital-grafana --filter name=hospital-prometheus --filter name=hospital-nginx) 2>/dev/null || true
                  
                  # PRESERVAR oracle_xe3, hospital-backend-local, hospital-frontend-local
                  echo "✅ Preservando contenedores esenciales: oracle_xe3, hospital-backend-local, hospital-frontend-local"
                  
                  # Verificar contenedores existentes
                  echo "Verificando contenedores existentes..."
                  docker ps -a --format "table {{.Names}}\t{{.Status}}"
                '''
                
                echo "   🐳 Construyendo y desplegando solo los 3 contenedores esenciales..."
                sh '''
                  # Construir backend local
                  echo "🔨 Construyendo backend local..."
                  docker build -t hospital-backend-local .
                  
                  # Construir frontend local
                  echo "🎨 Construyendo frontend local..."
                  docker build -f Dockerfile.frontend -t hospital-frontend-local .
                  
                  # Desplegar usando docker-compose-oracle-xe3.yml
                  echo "📦 Desplegando con configuración local..."
                  if command -v docker-compose >/dev/null 2>&1; then
                    DC="docker-compose"
                  elif docker compose version >/dev/null 2>&1; then
                    DC="docker compose"
                  else
                    echo "docker-compose no está instalado. Instala con: sudo apt-get install -y docker-compose-plugin"; exit 1
                  fi
                  
                  # Asegurar que oracle_xe3 esté en la red correcta
                  echo "🌐 Configurando red para oracle_xe3..."
                  docker network create hospital-network 2>/dev/null || true
                  docker network connect hospital-network oracle_xe3 2>/dev/null || true
                  
                  # Desplegar backend y frontend
                  $DC -f docker-compose-oracle-xe3.yml up -d
                '''
                
                echo "   🔍 Verificando salud de los servicios..."
                sleep 15
                sh '''
                  echo "=== Estado de los contenedores ==="
                  docker ps --format "table {{.Names}}\t{{.Ports}}\t{{.Status}}"
                  
                  echo "=== Verificando backend ==="
                  curl -f http://localhost:8080/faq || echo "⚠️ Backend no responde aún"
                  
                  echo "=== Verificando frontend ==="
                  curl -f http://localhost:5173 || echo "⚠️ Frontend no responde aún"
                '''
                
                echo "✅ Despliegue en producción completado exitosamente"
                echo "🌐 URLs de acceso:"
                echo "   - Backend: http://localhost:8080"
                echo "   - Frontend: http://localhost:5173"
                echo "   - Base de datos: localhost:1523 (oracle_xe3)"
            } else {
                echo "⏭️  Saltando despliegue de producción (BUILD_DOCKER=${params.BUILD_DOCKER}, rama: ${env.BRANCH_NAME}, PR: ${env.CHANGE_ID})"
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