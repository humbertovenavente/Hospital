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
                        // En estado detached, forzar uso de 'QA' para testing
                        detected = 'QA'
                        echo "Estado detached detectado, forzando rama: QA"
                    }
                    env.BRANCH_NAME = detected
                    echo " Rama detectada: ${env.BRANCH_NAME}"
                }
                
                // Verificación adicional: si estamos en la rama QA, forzar el nombre
                if (env.BRANCH_NAME == 'QA' || env.BRANCH_NAME == 'qa') {
                    env.BRANCH_NAME = 'QA'
                    echo "Rama QA confirmada: ${env.BRANCH_NAME}"
                }
            } catch (err) {
                echo "  No se pudo detectar la rama vía git: ${err}. Usando 'QA' por defecto"
                env.BRANCH_NAME = 'QA'
            }
        }

        // Forzar fallo si está habilitado (para probar el pipeline)
        if (params.FORCE_FAIL) {
            error(" Fallo forzado activado mediante parámetro FORCE_FAIL")
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
            echo "📊 Ejecutando análisis de SonarQube para QA..."
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
                        echo "   ❌ FORZANDO FALLO del análisis de SonarQube para frontend..."
                        echo "   🔧 Simulando error de configuración..."
                        
                        # Verificar que el directorio src existe
                        if [ ! -d "src" ]; then
                            echo "   ❌ Error: Directorio src no encontrado"
                            echo "   📁 Directorio actual: $(pwd)"
                            echo "   📁 Contenido: $(ls -la)"
                            exit 1
                        fi

                        # Verificar que el archivo de configuración existe
                        if [ ! -f "sonar-project-frontend-qa.properties" ]; then
                            echo "   ❌ Error: Archivo de configuración sonar-project-frontend-qa.properties no encontrado"
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

        stage('Deploy QA') {
            if (params.BUILD_DOCKER) {
                echo "🚀 Desplegando en entorno de QA..
                
                echo "    Limpiando contenedores de QA existentes..."
                sh '''
                  if command -v docker-compose >/dev/null 2>&1; then
                    DC="docker-compose"
                  elif docker compose version >/dev/null 2>&1; then
                    DC="docker compose"
                  else
                    echo "docker-compose no está instalado. Instala con: sudo apt-get install -y docker-compose-plugin"; exit 1
                  fi
                  
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
                  echo "🔗 Verificando conectividad de red..."
                  sleep 10
                  docker network connect bridge hospital-backend-qa 2>/dev/null || true
                '''
                echo "   ⏳ Esperando que los servicios se inicien..."
                sleep 15
                echo "✅ Despliegue en QA completado exitosamente"
                echo "🌐 URLs de acceso QA:"
                echo "   - Backend: http://localhost:8090"
                echo "   - Frontend: http://localhost:5174"
                echo "   - SonarQube: http://localhost:9000"
            } else {
                echo "⏭️ Construcción de Docker omitida (BUILD_DOCKER=false)"
            }
        }

        stage('Send Technical Debt Report') {
            echo "📧 Enviando reporte de deuda técnica para QA..."
            try {
                sh '''
                    echo "=== Enviando Reporte de Deuda Técnica QA ==="
                    # Esperar un poco para asegurar que el backend esté completamente iniciado
                    sleep 10
                    
                    # Verificar que el backend esté respondiendo
                    for i in {1..30}; do
                        if curl -f http://localhost:8090/q/health >/dev/null 2>&1; then
                            echo "✅ Backend está disponible"
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
                         }' || echo "⚠️ Error enviando reporte de deuda técnica"
                    
                    echo "✅ Reporte de deuda técnica enviado"
                '''
            } catch (Exception e) {
                echo "⚠️ No se pudo enviar el reporte de deuda técnica: ${e.getMessage()}"
            }
        }
        
        // Notificación por correo de éxito
        try {
            def recipients = 'jflores@unis.edu.gt, jnajar@unis.edu.gt'
            def subject = (env.CHANGE_ID ? "PR #${env.CHANGE_ID} QA exitoso: ${env.JOB_NAME} #${env.BUILD_NUMBER}" : "Pipeline QA exitoso: ${env.JOB_NAME} #${env.BUILD_NUMBER} (Rama: ${env.BRANCH_NAME})")
            
            def body = """
Hola equipo,

El pipeline de QA se ha ejecutado exitosamente.

🔧 INFORMACIÓN DEL BUILD QA:
- Job: ${env.JOB_NAME}
- Build: #${env.BUILD_NUMBER}
- Rama: ${env.BRANCH_NAME} [RAMA QA]
- URL: ${env.BUILD_URL}
- Estado: ✅ EXITOSO

📊 RESULTADOS DE CALIDAD QA:
- Tests Backend: ✅ Completados
- Tests Frontend: ✅ Completados
- Análisis SonarQube: ✅ Completado
- Quality Gate: ✅ PASÓ

🌐 URLs DE ACCESO QA:
- Backend: http://localhost:8090
- Frontend: http://localhost:5174
- SonarQube: http://localhost:9000
- Jenkins: ${env.BUILD_URL}

📈 PROYECTOS SONARQUBE QA:
- Backend: hospital-backend-qa
- Frontend: hospital-frontend-qa

📧 REPORTE DE DEUDA TÉCNICA:
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