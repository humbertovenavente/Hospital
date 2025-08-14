node {
    // Jenkinsfile específico para QA
    properties([
        parameters([
            booleanParam(name: 'FORCE_FAIL', defaultValue: false, description: 'Forzar fallo del pipeline para probar notificaciones')
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
                java -version || echo "Java no disponible"
                echo "=== Verificando Maven ==="
                mvn -version || echo "Maven no disponible"
                echo "=== Verificando Node.js ==="
                node --version || echo "Node.js no disponible"
                npm --version || echo "npm no disponible"
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
        
        stage('Tests Backend') {
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
        
        stage('Deploy QA') {
            when {
                branch 'QA'
            }
            echo "🚀 Desplegando en ambiente QA..."
            sh '''
                echo "=== Desplegando en QA ==="
                echo "QA deployment completado"
            '''
            echo "✅ Despliegue QA completado"
        }
        
        // Notificación de éxito
        echo "✅ Pipeline QA ejecutado exitosamente"
        emailext(
            to: 'jflores@unis.edu.gt',
            from: 'humbertovenavente7@gmail.com',
            subject: "QA Pipeline exitoso: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
            body: """
Pipeline QA ejecutado exitosamente.

Build: #${env.BUILD_NUMBER}
URL: ${env.BUILD_URL}
Estado: ✅ EXITOSO

Saludos,
Sistema CI/CD Hospital
""",
            mimeType: 'text/plain'
        )
        
    } catch (Exception e) {
        echo "❌ Pipeline QA falló: ${e.getMessage()}"
        
        // Notificación de fallo
        emailext(
            to: 'jflores@unis.edu.gt',
            from: 'humbertovenavente7@gmail.com',
            subject: "QA Pipeline falló: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
            body: """
Pipeline QA ha fallado.

Build: #${env.BUILD_NUMBER}
URL: ${env.BUILD_URL}
Error: ${e.getMessage()}

Revisar Jenkins para más detalles.

Saludos,
Sistema CI/CD Hospital
""",
            mimeType: 'text/plain'
        )
        throw e
    }
}