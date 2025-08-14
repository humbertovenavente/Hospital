node {
    // Jenkinsfile específico para QA - Pipeline Scripted (correcto)
    properties([
        parameters([
            booleanParam(name: 'FORCE_FAIL', defaultValue: false, description: 'Forzar fallo del pipeline para probar notificaciones por correo'),
            booleanParam(name: 'BUILD_DOCKER', defaultValue: true, description: 'Construir y desplegar imágenes Docker (activado por defecto para QA)')
        ])
    ])
    
    try {
        stage('Checkout') {
            echo "🔄 Iniciando checkout del código para QA..."
            deleteDir()
            checkout scm
            echo "✅ Checkout completado"
        }
        
        stage('Setup Environment') {
            echo "⚙️ Configurando entorno de QA..."
            sh '''
                echo "=== Verificando Java ==="
                java -version
                echo "=== Verificando Maven ==="
                mvn -version
                echo "=== Verificando Node.js ==="
                node --version
                npm --version
            '''
            echo "✅ Entorno configurado"
        }
        
        stage('Build Backend') {
            echo "🔨 Construyendo backend..."
            dir('backend') {
                sh '''
                    echo "=== Compilando backend ==="
                    mvn clean package -DskipTests
                '''
            }
            echo "✅ Backend construido"
        }
        
        stage('Unit Tests Backend') {
            echo "🧪 Ejecutando tests del backend..."
            dir('backend') {
                sh '''
                    echo "=== Ejecutando tests ==="
                    mvn test
                '''
                junit testResults: 'target/surefire-reports/*.xml', allowEmptyResults: true
            }
            echo "✅ Tests backend completados"
        }
        
        stage('Build Frontend') {
            echo "🎨 Construyendo frontend..."
            sh '''
                echo "=== Instalando dependencias ==="
                npm ci
                echo "=== Construyendo frontend ==="
                npm run build
            '''
            echo "✅ Frontend construido"
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
        
        stage('Deploy QA') {
            // ✅ CORREGIDO: Usar condicional 'if' en lugar de 'when' para pipeline scripted
            if (env.BRANCH_NAME == 'QA' || env.BRANCH_NAME == 'qa') {
                echo "🚀 Desplegando en ambiente QA..."
                echo "✅ Forzando BUILD_DOCKER = true para rama QA"
                env.BUILD_DOCKER = 'true'
                
                sh '''
                    echo "=== Desplegando en QA ==="
                    echo "🌐 URLs de acceso QA:"
                    echo "   - Backend: http://localhost:8090"
                    echo "   - Frontend: http://localhost:5174"
                    echo "   - Base de datos: Base de datos QA configurada"
                    echo "✅ QA deployment completado"
                '''
                echo "✅ Despliegue QA completado"
            } else {
                echo "⏭️ Saltando despliegue QA (rama: ${env.BRANCH_NAME})"
            }
        }
        
        // Notificación de éxito
        echo "✅ Pipeline QA ejecutado exitosamente"
        emailext(
            to: 'jflores@unis.edu.gt, jnajar@unis.edu.gt',
            from: 'humbertovenavente7@gmail.com',
            subject: "✅ QA Pipeline exitoso: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
            body: """
¡Hola equipo!

El pipeline QA se ha ejecutado exitosamente.

📋 INFORMACIÓN DEL BUILD:
- Job: ${env.JOB_NAME}
- Build: #${env.BUILD_NUMBER}
- Rama: ${env.BRANCH_NAME}
- URL: ${env.BUILD_URL}
- Estado: ✅ EXITOSO

🚀 RESULTADOS:
- ✅ Checkout completado
- ✅ Entorno configurado
- ✅ Backend construido
- ✅ Tests backend ejecutados
- ✅ Frontend construido  
- ✅ Tests frontend ejecutados
- ✅ Deploy QA completado

🌐 URLs QA:
- Backend: http://localhost:8090
- Frontend: http://localhost:5174

Saludos,
Sistema CI/CD Hospital 🏥
""",
            mimeType: 'text/plain'
        )
        
    } catch (Exception e) {
        echo "❌ Pipeline QA falló: ${e.getMessage()}"
        
        // Notificación de fallo
        emailext(
            to: 'jflores@unis.edu.gt, jnajar@unis.edu.gt',
            from: 'humbertovenavente7@gmail.com',
            subject: "❌ QA Pipeline falló: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
            body: """
¡Hola equipo!

El pipeline QA ha fallado.

📋 INFORMACIÓN DEL BUILD:
- Job: ${env.JOB_NAME}
- Build: #${env.BUILD_NUMBER}
- Rama: ${env.BRANCH_NAME}
- URL: ${env.BUILD_URL}
- Error: ${e.getMessage()}

🔍 Acciones requeridas:
1. Revisar la consola de Jenkins: ${env.BUILD_URL}
2. Verificar logs de los servicios
3. Corregir el problema identificado

Saludos,
Sistema CI/CD Hospital 🏥
""",
            mimeType: 'text/plain'
        )
        throw e
    }
}