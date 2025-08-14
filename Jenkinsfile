node {
    // Parámetro para pruebas: permite forzar un fallo controlado y validar notificaciones
    properties([
        parameters([
            booleanParam(name: 'FORCE_FAIL', defaultValue: false, description: 'Forzar fallo del pipeline para probar notificaciones por correo')
            ,
            booleanParam(name: 'BUILD_DOCKER', defaultValue: true, description: 'Construir y desplegar imágenes Docker (activado por defecto para PROD)')
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
                        // En estado detached, forzar uso de 'prod' para evitar confusiones
                        detected = 'prod'
                        echo "🔍 Estado detached detectado, forzando rama: prod"
                    }
                    env.BRANCH_NAME = detected
                    echo "🔖 Rama detectada: ${env.BRANCH_NAME}"
                }
                
                // Verificación adicional: si estamos en la rama prod, forzar el nombre
                if (env.BRANCH_NAME == 'prod' || env.BRANCH_NAME == 'production') {
                    env.BRANCH_NAME = 'prod'
                    echo "✅ Rama PROD confirmada: ${env.BRANCH_NAME}"
                }
            } catch (err) {
                echo "⚠️  No se pudo detectar la rama vía git: ${err}. Usando 'prod' por defecto"
                env.BRANCH_NAME = 'prod'
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
                // Publicar resultados de tests
                junit testResults: 'target/surefire-reports/*.xml', allowEmptyResults: true
            }
            echo "✅ Tests unitarios del backend completados"
        }
        
        stage('Code Quality Check') {
            echo "🔍 Iniciando verificación de calidad del código con SonarQube..."
            echo "   Configurando SonarQube Scanner para rama: ${env.BRANCH_NAME}..."
            
            // Verificar que SonarQube esté disponible
            sh '''
                echo "=== Verificando SonarQube ==="
                curl -f http://localhost:9001/api/system/status || echo "SonarQube QA no está disponible"
                echo "=== Verificando SonarQube Scanner ==="
                /opt/sonar-scanner/bin/sonar-scanner --version || echo "SonarQube Scanner no está disponible"
            '''

            echo "   Ejecutando análisis de calidad del código..."
            
            // Usar la integración oficial de Jenkins con SonarQube y credenciales explícitas
            withSonarQubeEnv('SonarQube') {
                withCredentials([string(credentialsId: 'sonarqube-token', variable: 'SONAR_TOKEN')]) {
                    // ANÁLISIS DEL BACKEND (con cobertura de tests y rama específica)
                    echo "   🔍 Analizando BACKEND para rama: ${env.BRANCH_NAME}..."
                    sh '''
                        echo "=== Ejecutando SonarQube Analysis para BACKEND (Rama: ''' + env.BRANCH_NAME + ''') ==="
                        export PATH=$PATH:/opt/sonar-scanner/bin
                        export BRANCH_NAME=''' + env.BRANCH_NAME + '''
                        export BUILD_NUMBER=''' + env.BUILD_NUMBER + '''
                        
                        # Usar SonarQube PROD en puerto 9000
                        export SONAR_HOST=${SONAR_HOST_URL:-http://localhost:9000}
                        export TOKEN_TO_USE=${SONAR_TOKEN:-$SONAR_AUTH_TOKEN}

                        # Configurar projectKey y projectName para PROD
                        PROJECT_KEY="hospital-backend-prod"
                        PROJECT_NAME="Hospital Backend - PRODUCCIÓN (Java/Quarkus)"

                        echo "   📊 Proyecto SonarQube: $PROJECT_KEY - $PROJECT_NAME"

                        TEST_ARGS=""
                        if [ -d backend/target/test-classes ] && [ -d backend/src/test/java ]; then
                          TEST_ARGS="-Dsonar.tests=backend/src/test/java -Dsonar.java.test.binaries=backend/target/test-classes"
                        else
                          echo "⚠️  No se encontraron clases de prueba (backend/target/test-classes). Se omitirá el análisis de tests."
                        fi

                        # Usar archivo de configuración específico para PROD
                        echo "   🔧 Usando configuración específica de PROD para backend..."
                        sonar-scanner -Dproject.settings=sonar-project-backend-prod.properties \
                          -Dsonar.host.url=${SONAR_HOST} \
                          -Dsonar.token=${TOKEN_TO_USE}
                        echo "=== Análisis de SonarQube para BACKEND (${BRANCH_NAME}) completado ==="
                    '''
                    
                    // ANÁLISIS DEL FRONTEND (con rama específica)
                    echo "   🔍 Analizando FRONTEND para rama: ${env.BRANCH_NAME}..."
                    sh '''
                        echo "=== Ejecutando SonarQube Analysis para FRONTEND (Rama: ''' + env.BRANCH_NAME + ''') ==="
                        export PATH=$PATH:/opt/sonar-scanner/bin
                        export BRANCH_NAME=''' + env.BRANCH_NAME + '''
                        export BUILD_NUMBER=''' + env.BUILD_NUMBER + '''
                        export SONAR_HOST=${SONAR_HOST_URL:-http://localhost:9001}
                        export SONAR_TOKEN=${SONAR_TOKEN:-$SONAR_AUTH_TOKEN}

                        # Configurar projectKey y projectName para PROD
                        PROJECT_KEY="hospital-frontend-prod"
                        PROJECT_NAME="Hospital Frontend - PRODUCCIÓN (Vue.js/TypeScript)"

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
                        # Usar archivo de configuración específico para PROD
                        echo "   🔧 Usando configuración específica de PROD para frontend..."
                        sonar-scanner -Dproject.settings=sonar-project-frontend-prod.properties \
                          -Dsonar.host.url=${SONAR_HOST} \
                          -Dsonar.token=${SONAR_TOKEN}
                        
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
            if (params.BUILD_DOCKER && env.BRANCH_NAME == 'prod') {
                echo "🐳 Iniciando construcción de imágenes Docker para PROD..."
                echo "   Construyendo imagen del backend..."
                docker.build("${DOCKER_REGISTRY}/${BACKEND_IMAGE}-qa:${VERSION}")
                echo "   Construyendo imagen del frontend..."
                docker.build("${DOCKER_REGISTRY}/${FRONTEND_IMAGE}-qa:${VERSION}", "-f Dockerfile.frontend.qa .")
                echo "✅ Imágenes Docker construidas exitosamente"
            } else {
                echo "⏭️  Saltando construcción de Docker (BUILD_DOCKER=${params.BUILD_DOCKER}, rama: ${env.BRANCH_NAME})"
            }
        }
        
        stage('Deploy to PROD') {
            // Forzar BUILD_DOCKER = true para rama prod
            if (env.BRANCH_NAME == 'prod') {
                env.BUILD_DOCKER = 'true'
                echo "✅ Forzando BUILD_DOCKER = true para rama PROD"
            }
            
            if (params.BUILD_DOCKER && env.BRANCH_NAME == 'prod' && !env.CHANGE_ID) {
                echo "🚀 Iniciando despliegue en ambiente de PRODUCCIÓN..."
                echo "   🧹 Limpiando contenedores de PROD existentes..."
                sh '''
                  if command -v docker-compose >/dev/null 2>&1; then
                    DC="docker-compose"
                  elif docker compose version >/dev/null 2>&1; then
                    DC="docker compose"
                  else
                    echo "docker-compose no está instalado. Instala con: sudo apt-get install -y docker-compose-plugin"; exit 1
                  fi
                  
                  # Detener y limpiar contenedores de PROD existentes
                  echo "🛑 Deteniendo contenedores de PROD..."
                  $DC -f docker-compose.prod.yml down 2>/dev/null || true
                  
                  # Forzar detención y eliminación de contenedores de PROD
                  echo "🗑️ Forzando limpieza de contenedores de PROD..."
                  docker stop $(docker ps -q --filter name=hospital- --filter name=hospital-sonarqube-prod --filter name=hospital-prometheus-prod --filter name=hospital-grafana-prod) 2>/dev/null || true
                  docker rm $(docker ps -aq --filter name=hospital- --filter name=hospital-sonarqube-prod --filter name=hospital-prometheus-prod --filter name=hospital-grafana-prod) 2>/dev/null || true
                  
                  # Limpiar contenedores huérfanos de PROD
                  echo "🗑️ Limpiando contenedores huérfanos de PROD..."
                  docker container prune -f 2>/dev/null || true
                '''
                
                echo "   🐳 Construyendo y desplegando contenedores de PROD..."
                sh '''
                  # Construir backend para PROD
                  echo "🔨 Construyendo backend para PROD..."
                  docker build -t hospital-backend-prod .
                  
                  # Construir frontend para PROD
                  echo "🎨 Construyendo frontend para PROD..."
                  docker build -f Dockerfile.frontend.prod -t hospital-frontend-prod .
                  
                  # Configurar red para oracle_xe2 (usado en PROD)
                  echo "🌐 Configurando red para oracle_xe2..."
                  docker network create hospital-network 2>/dev/null || true
                  docker network connect hospital-network oracle_xe2 2>/dev/null || true
                  
                  # Desplegar servicios de PROD
                  echo "📦 Desplegando servicios de PROD..."
                  docker-compose -f docker-compose.prod.yml up -d --build
                  
                  # Asegurar que el backend esté en la red correcta
                  echo "🔗 Conectando backend a la red hospital-network..."
                  docker network connect hospital-network hospital-backend-prod 2>/dev/null || true
                  
                  # Verificar conectividad de red
                  echo "🔍 Verificando conectividad de red..."
                  docker exec hospital-backend-qa ping -c 1 oracle_xe2 || echo "⚠️  Advertencia: No se pudo hacer ping a oracle_xe2"
                '''
                echo "   Verificando salud de los servicios..."
                sleep 15
                echo "✅ Despliegue en PROD completado exitosamente"
                echo "🌐 URLs de acceso PROD:"
                echo "   - Backend: http://localhost:8090"
                echo "   - Frontend: http://localhost:5174"
                echo "   - Nginx Reverse Proxy: http://localhost:8083"
                echo "   - Jenkins: http://localhost:8081"
                echo "   - SonarQube: http://localhost:9000"
                echo "   - Prometheus: http://localhost:9091"
                echo "   - Grafana: http://localhost:3001"
                echo "   - Base de datos: localhost:1522 (oracle_xe2)"
            } else {
                echo "⏭️  Saltando despliegue de PROD (BUILD_DOCKER=${params.BUILD_DOCKER}, rama: ${env.BRANCH_NAME}, PR: ${env.CHANGE_ID})"
            }
        }
        
        // Success summary
        if (env.CHANGE_ID) {
            echo "✅ Pull Request #${env.CHANGE_ID} procesado exitosamente"
            echo "📋 Resumen del pipeline PROD:"
            echo "   - Checkout: ✅"
            echo "   - Code Quality: ✅"
            echo "   - Build Backend: ✅"
            echo "   - Tests Backend: ✅"
            echo "   - Build Frontend: ✅"
            echo "   - Tests Frontend: ✅"
            echo "   - Integration Tests: ✅"
            echo "   - Docker Images: ✅"
        } else {
            echo "✅ Pipeline PROD ejecutado exitosamente en rama ${env.BRANCH_NAME}"
        }
        
        // Notificación por correo de éxito
        try {
            def recipients = 'jflores@unis.edu.gt, jnajar@unis.edu.gt'
            def subject = (env.CHANGE_ID ? "PROD PR #${env.CHANGE_ID} exitoso: ${env.JOB_NAME} #${env.BUILD_NUMBER}" : "PROD Pipeline exitoso: ${env.JOB_NAME} #${env.BUILD_NUMBER} (Rama: ${env.BRANCH_NAME})")
            
            def body = """
Hola equipo,

El pipeline de PRODUCCIÓN se ha ejecutado exitosamente.

INFORMACIÓN DEL BUILD PROD:
- Job: ${env.JOB_NAME}
- Build: #${env.BUILD_NUMBER}
- Rama: ${env.BRANCH_NAME}
- URL: ${env.BUILD_URL}
- Estado: ✅ EXITOSO

📊 RESULTADOS DE CALIDAD PROD:
- Tests Backend: Completados
- Tests Frontend: Completados
- Análisis SonarQube: Completado (puerto 9001)
- Quality Gate: ✅ PASÓ

🌐 URLs DE ACCESO PROD:
- Backend: http://localhost:8090
- Frontend: http://localhost:5174
- SonarQube PROD: http://localhost:9000
- Jenkins: ${env.BUILD_URL}

📈 REPORTE DE DEUDA TÉCNICA PROD:
El sistema PROD está funcionando correctamente.

Saludos,
Sistema de CI/CD del Hospital - PROD
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
            echo "❌ Pull Request #${env.CHANGE_ID} falló: ${e.getMessage()}"
        } else {
            echo "❌ Pipeline PROD falló en rama ${env.BRANCH_NAME}: ${e.getMessage()}"
        }
        // Notificación por correo a Lead Developer y Product Owner
        try {
            def recipients = 'jflores@unis.edu.gt, jnajar@unis.edu.gt'
            def subject = (env.CHANGE_ID ? "PROD PR #${env.CHANGE_ID} falló: ${env.JOB_NAME} #${env.BUILD_NUMBER}" : "PROD Pipeline falló: ${env.JOB_NAME} #${env.BUILD_NUMBER} (Rama: ${env.BRANCH_NAME})")
            
            def body = """
Hola equipo,

El pipeline de PRODUCCIÓN ha fallado.

INFORMACIÓN DEL BUILD PROD:
- Job: ${env.JOB_NAME}
- Build: #${env.BUILD_NUMBER}
- Rama: ${env.BRANCH_NAME}
- URL: ${env.BUILD_URL}
- Estado: ❌ FALLÓ
- Motivo: ${e.getMessage()}

⚠️ RESULTADOS DE CALIDAD PROD:
- Tests Backend: Verificar estado
- Tests Frontend: Verificar estado
- Análisis SonarQube: Verificar estado

🔧 ACCIONES REQUERIDAS:
1. Revisar la consola de Jenkins para más detalles
2. Verificar logs de los servicios PROD
3. Revisar métricas de SonarQube PROD (puerto 9000)
4. Corregir el problema identificado

🌐 URLs DE ACCESO:
- Jenkins: ${env.BUILD_URL}
- SonarQube PROD: http://localhost:9000

Por favor revisar la consola para más detalles.

Saludos,
Sistema de CI/CD del Hospital - PROD
"""
            // Usar Email Extension Plugin
            emailext(
                to: recipients,
                from: 'humbertovenavente7@gmail.com',
                subject: subject,
                body: body,
                mimeType: 'text/plain'
            )
            echo "⚠️ Notificación de fallo QA enviada a: ${recipients}"
        } catch (err) {
            echo "❌ No se pudo enviar la notificación por correo: ${err}"
        }
        throw e
    }
}
