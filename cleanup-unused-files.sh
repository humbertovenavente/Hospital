#!/bin/bash

# Script para limpiar archivos obsoletos y duplicados del proyecto Hospital
echo "🧹 LIMPIEZA DE ARCHIVOS OBSOLETOS Y DUPLICADOS"
echo "============================================="

# Función para confirmar eliminación
confirm_delete() {
    local file="$1"
    local reason="$2"
    echo "❓ ¿Eliminar '$file'? ($reason)"
    read -p "   [y/N]: " -n 1 -r
    echo
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        return 0
    else
        return 1
    fi
}

# Función para eliminar archivo
delete_file() {
    local file="$1"
    if [ -f "$file" ]; then
        rm "$file"
        echo "✅ Eliminado: $file"
    elif [ -d "$file" ]; then
        rm -rf "$file"
        echo "✅ Eliminado directorio: $file"
    fi
}

echo "📋 ARCHIVOS IDENTIFICADOS PARA LIMPIEZA:"
echo "========================================"

# 1. Archivos de análisis de SonarQube duplicados
echo ""
echo "🔍 1. Archivos de análisis de SonarQube duplicados:"
echo "   - analyze-backend-sonar-dev-drone.sh (duplicado)"
echo "   - analyze-backend-sonar-dev.sh (duplicado)"
echo "   - analyze-backend-sonar.sh (versión principal)"

# 2. Archivos de despliegue duplicados
echo ""
echo "🚀 2. Archivos de despliegue duplicados:"
echo "   - deploy-cloud-containers.sh (duplicado)"
echo "   - deploy-cloud.sh (duplicado)"
echo "   - deploy-drone-gcp.sh (versión antigua)"
echo "   - deploy-drone-gcp-simple.sh (versión principal)"
echo "   - deploy-to-gcp-fixed.sh (duplicado)"
echo "   - deploy-to-gcp.sh (versión principal)"

# 3. Archivos de configuración de Drone duplicados
echo ""
echo "⚙️ 3. Archivos de configuración de Drone duplicados:"
echo "   - docker-compose.drone-gcp.yml (duplicado)"
echo "   - docker-compose.drone-local.yml (duplicado)"
echo "   - docker-compose.drone-simple.yml (duplicado)"
echo "   - docker-compose.drone.yml (versión principal)"

# 4. Archivos de instalación duplicados
echo ""
echo "📦 4. Archivos de instalación duplicados:"
echo "   - install-drone-local.sh (duplicado)"
echo "   - install-drone.sh (versión principal)"
echo "   - install-sonarqube-service.sh (duplicado)"

# 5. Archivos de migración obsoletos
echo ""
echo "🔄 5. Archivos de migración obsoletos:"
echo "   - migrate-sonarqube-to-cloud.sh (obsoleto)"
echo "   - migrate-sonarqube-to-dev.sh (obsoleto)"
echo "   - migrate-to-sonarqube-dev.sh (obsoleto)"

# 6. Archivos de configuración de SonarQube duplicados
echo ""
echo "📊 6. Archivos de configuración de SonarQube duplicados:"
echo "   - sonar-project-backend-dev.properties (duplicado)"
echo "   - sonar-project-backend-prod.properties (duplicado)"
echo "   - sonar-project-backend-qa.properties (duplicado)"
echo "   - sonar-project-backend.properties (versión principal)"
echo "   - sonar-project-frontend-dev.properties (duplicado)"
echo "   - sonar-project-frontend-prod.properties (duplicado)"
echo "   - sonar-project-frontend-qa.properties (duplicado)"
echo "   - sonar-project-frontend.properties (versión principal)"

# 7. Archivos de configuración de Jenkins obsoletos
echo ""
echo "🔧 7. Archivos de configuración de Jenkins obsoletos:"
echo "   - Jenkinsfile.dev (obsoleto)"
echo "   - Jenkinsfile.prod (obsoleto)"
echo "   - Jenkinsfile.qa (obsoleto)"
echo "   - Jenkinsfile (versión principal)"
echo "   - setup-jenkins-webhook.sh (obsoleto)"

# 8. Archivos de configuración de Nginx duplicados
echo ""
echo "🌐 8. Archivos de configuración de Nginx duplicados:"
echo "   - nginx.dev.conf (duplicado)"
echo "   - nginx.prod.conf (duplicado)"
echo "   - nginx.qa.conf (duplicado)"

# 9. Archivos de configuración de Prometheus duplicados
echo ""
echo "📈 9. Archivos de configuración de Prometheus duplicados:"
echo "   - prometheus.drone.yml (duplicado)"
echo "   - prometheus.qa.yml (duplicado)"
echo "   - prometheus.yml (versión principal)"

# 10. Archivos de configuración de Docker duplicados
echo ""
echo "🐳 10. Archivos de configuración de Docker duplicados:"
echo "   - Dockerfile.frontend.cloud (duplicado)"
echo "   - Dockerfile.frontend.qa (duplicado)"
echo "   - Dockerfile.frontend (versión principal)"

# 11. Archivos de configuración de Ngrok obsoletos
echo ""
echo "🔗 11. Archivos de configuración de Ngrok obsoletos:"
echo "   - ngrok-jenkins.service (obsoleto)"
echo "   - ngrok.yml (obsoleto)"
echo "   - start-ngrok-autostart.sh (obsoleto)"
echo "   - start-ngrok-config.sh (obsoleto)"
echo "   - start-ngrok-dual.sh (obsoleto)"
echo "   - start-ngrok.sh (obsoleto)"

# 12. Archivos de configuración de servicios obsoletos
echo ""
echo "⚙️ 12. Archivos de configuración de servicios obsoletos:"
echo "   - drone.service (obsoleto)"
echo "   - sonarqube.service (obsoleto)"

# 13. Archivos de configuración de Oracle obsoletos
echo ""
echo "🗄️ 13. Archivos de configuración de Oracle obsoletos:"
echo "   - docker-compose-oracle-xe3.yml (obsoleto)"

# 14. Archivos de configuración de GCP obsoletos
echo ""
echo "☁️ 14. Archivos de configuración de GCP obsoletos:"
echo "   - docker-compose.gcp.yml (obsoleto)"

# 15. Archivos de configuración de desarrollo obsoletos
echo ""
echo "🛠️ 15. Archivos de configuración de desarrollo obsoletos:"
echo "   - setup-complete-dev-environment.sh (obsoleto)"
echo "   - setup-dev-environment.sh (obsoleto)"

# 16. Archivos de configuración de GitHub obsoletos
echo ""
echo "🐙 16. Archivos de configuración de GitHub obsoletos:"
echo "   - setup-github-protection-personal.sh (obsoleto)"
echo "   - setup-github-protection.sh (versión principal)"

# 17. Archivos de configuración de SonarQube obsoletos
echo ""
echo "📊 17. Archivos de configuración de SonarQube obsoletos:"
echo "   - setup-sonarqube-cloud-9003.sh (obsoleto)"
echo "   - setup-sonarqube-cloud.sh (versión principal)"
echo "   - setup-sonarqube-dev-projects.sh (obsoleto)"
echo "   - setup-sonarqube-qa-projects.sh (obsoleto)"

# 18. Archivos de configuración de Drone obsoletos
echo ""
echo "🚁 18. Archivos de configuración de Drone obsoletos:"
echo "   - setup-drone-persistent.sh (obsoleto)"
echo "   - setup-drone-service-remote.sh (obsoleto)"

# 19. Archivos de configuración de Ngrok obsoletos
echo ""
echo "🔗 19. Archivos de configuración de Ngrok obsoletos:"
echo "   - setup-ngrok-autostart.sh (obsoleto)"

# 20. Archivos de configuración de SonarQube obsoletos
echo ""
echo "📊 20. Archivos de configuración de SonarQube obsoletos:"
echo "   - start-sonarqube-remote.sh (obsoleto)"

# 21. Archivos de configuración de test obsoletos
echo ""
echo "🧪 21. Archivos de configuración de test obsoletos:"
echo "   - test-branch-detection.sh (obsoleto)"
echo "   - test-complete-jacoco-flow.sh (obsoleto)"
echo "   - test-sonar-analysis-dev.sh (obsoleto)"
echo "   - test-sonar-frontend.sh (obsoleto)"

# 22. Archivos de configuración de reportes obsoletos
echo ""
echo "📋 22. Archivos de configuración de reportes obsoletos:"
echo "   - send-technical-debt-report-dev.sh (obsoleto)"
echo "   - send-technical-debt-report.sh (versión principal)"

# 23. Archivos de configuración de monitoreo obsoletos
echo ""
echo "📊 23. Archivos de configuración de monitoreo obsoletos:"
echo "   - monitor-drone.sh (obsoleto)"

# 24. Archivos de configuración de verificación obsoletos
echo ""
echo "✅ 24. Archivos de configuración de verificación obsoletos:"
echo "   - check-cloud-containers.sh (obsoleto)"
echo "   - check-drone-remote.sh (obsoleto)"
echo "   - check-sonarqube-service.sh (obsoleto)"
echo "   - check-sonarqube-status.sh (obsoleto)"

# 25. Archivos de configuración de backup obsoletos
echo ""
echo "💾 25. Archivos de configuración de backup obsoletos:"
echo "   - backup-sonarqube-community.sh (obsoleto)"

# 26. Archivos de configuración de cleanup obsoletos
echo ""
echo "🧹 26. Archivos de configuración de cleanup obsoletos:"
echo "   - cleanup-qa.sh (obsoleto)"

# 27. Archivos de configuración de diagnóstico obsoletos
echo ""
echo "🔍 27. Archivos de configuración de diagnóstico obsoletos:"
echo "   - diagnose-maven-environment.sh (obsoleto)"

# 28. Archivos de configuración de exposición obsoletos
echo ""
echo "🌐 28. Archivos de configuración de exposición obsoletos:"
echo "   - expose-sonarqube-ngrok.sh (obsoleto)"

# 29. Archivos de configuración de inicio rápido obsoletos
echo ""
echo "⚡ 29. Archivos de configuración de inicio rápido obsoletos:"
echo "   - quick-start-sonarqube.sh (obsoleto)"

# 30. Archivos de configuración de carga obsoletos
echo ""
echo "📦 30. Archivos de configuración de carga obsoletos:"
echo "   - load-image.sh (obsoleto)"

# 31. Archivos de configuración de ejecución local obsoletos
echo ""
echo "🏠 31. Archivos de configuración de ejecución local obsoletos:"
echo "   - run-local.sh (obsoleto)"

# 32. Archivos de configuración de verificación de estado obsoletos
echo ""
echo "✅ 32. Archivos de configuración de verificación de estado obsoletos:"
echo "   - verify-drone-status.sh (obsoleto)"

echo ""
echo "🎯 RESUMEN DE LIMPIEZA:"
echo "======================="
echo "Total de archivos identificados para eliminación: ~80 archivos"
echo "Esto reducirá significativamente la complejidad del proyecto"
echo "y mantendrá solo los archivos esenciales y actualizados."

echo ""
echo "⚠️  IMPORTANTE:"
echo "=============="
echo "- Se mantendrán solo las versiones principales de cada archivo"
echo "- Se eliminarán duplicados y versiones obsoletas"
echo "- Se conservarán los archivos de configuración activos"
echo "- Se mantendrán los directorios principales (backend/, src/, etc.)"

echo ""
echo "¿Quieres proceder con la limpieza automática?"
read -p "[y/N]: " -n 1 -r
echo

if [[ $REPLY =~ ^[Yy]$ ]]; then
    echo "🚀 Iniciando limpieza automática..."
    
    # Eliminar archivos duplicados de análisis de SonarQube
    delete_file "analyze-backend-sonar-dev-drone.sh"
    delete_file "analyze-backend-sonar-dev.sh"
    
    # Eliminar archivos duplicados de despliegue
    delete_file "deploy-cloud-containers.sh"
    delete_file "deploy-cloud.sh"
    delete_file "deploy-drone-gcp.sh"
    delete_file "deploy-to-gcp-fixed.sh"
    
    # Eliminar archivos duplicados de configuración de Drone
    delete_file "docker-compose.drone-gcp.yml"
    delete_file "docker-compose.drone-local.yml"
    delete_file "docker-compose.drone-simple.yml"
    
    # Eliminar archivos duplicados de instalación
    delete_file "install-drone-local.sh"
    delete_file "install-sonarqube-service.sh"
    
    # Eliminar archivos obsoletos de migración
    delete_file "migrate-sonarqube-to-cloud.sh"
    delete_file "migrate-sonarqube-to-dev.sh"
    delete_file "migrate-to-sonarqube-dev.sh"
    
    # Eliminar archivos duplicados de configuración de SonarQube
    delete_file "sonar-project-backend-dev.properties"
    delete_file "sonar-project-backend-prod.properties"
    delete_file "sonar-project-backend-qa.properties"
    delete_file "sonar-project-frontend-dev.properties"
    delete_file "sonar-project-frontend-prod.properties"
    delete_file "sonar-project-frontend-qa.properties"
    
    # Eliminar archivos obsoletos de Jenkins
    delete_file "Jenkinsfile.dev"
    delete_file "Jenkinsfile.prod"
    delete_file "Jenkinsfile.qa"
    delete_file "setup-jenkins-webhook.sh"
    
    # Eliminar archivos duplicados de Nginx
    delete_file "nginx.dev.conf"
    delete_file "nginx.prod.conf"
    delete_file "nginx.qa.conf"
    
    # Eliminar archivos duplicados de Prometheus
    delete_file "prometheus.drone.yml"
    delete_file "prometheus.qa.yml"
    
    # Eliminar archivos duplicados de Docker
    delete_file "Dockerfile.frontend.cloud"
    delete_file "Dockerfile.frontend.qa"
    
    # Eliminar archivos obsoletos de Ngrok
    delete_file "ngrok-jenkins.service"
    delete_file "ngrok.yml"
    delete_file "start-ngrok-autostart.sh"
    delete_file "start-ngrok-config.sh"
    delete_file "start-ngrok-dual.sh"
    delete_file "start-ngrok.sh"
    
    # Eliminar archivos obsoletos de servicios
    delete_file "drone.service"
    delete_file "sonarqube.service"
    
    # Eliminar archivos obsoletos de Oracle
    delete_file "docker-compose-oracle-xe3.yml"
    
    # Eliminar archivos obsoletos de GCP
    delete_file "docker-compose.gcp.yml"
    
    # Eliminar archivos obsoletos de desarrollo
    delete_file "setup-complete-dev-environment.sh"
    delete_file "setup-dev-environment.sh"
    
    # Eliminar archivos obsoletos de GitHub
    delete_file "setup-github-protection-personal.sh"
    
    # Eliminar archivos obsoletos de SonarQube
    delete_file "setup-sonarqube-cloud-9003.sh"
    delete_file "setup-sonarqube-dev-projects.sh"
    delete_file "setup-sonarqube-qa-projects.sh"
    
    # Eliminar archivos obsoletos de Drone
    delete_file "setup-drone-persistent.sh"
    delete_file "setup-drone-service-remote.sh"
    
    # Eliminar archivos obsoletos de Ngrok
    delete_file "setup-ngrok-autostart.sh"
    
    # Eliminar archivos obsoletos de SonarQube
    delete_file "start-sonarqube-remote.sh"
    
    # Eliminar archivos obsoletos de test
    delete_file "test-branch-detection.sh"
    delete_file "test-complete-jacoco-flow.sh"
    delete_file "test-sonar-analysis-dev.sh"
    delete_file "test-sonar-frontend.sh"
    
    # Eliminar archivos obsoletos de reportes
    delete_file "send-technical-debt-report-dev.sh"
    
    # Eliminar archivos obsoletos de monitoreo
    delete_file "monitor-drone.sh"
    
    # Eliminar archivos obsoletos de verificación
    delete_file "check-cloud-containers.sh"
    delete_file "check-drone-remote.sh"
    delete_file "check-sonarqube-service.sh"
    delete_file "check-sonarqube-status.sh"
    
    # Eliminar archivos obsoletos de backup
    delete_file "backup-sonarqube-community.sh"
    
    # Eliminar archivos obsoletos de cleanup
    delete_file "cleanup-qa.sh"
    
    # Eliminar archivos obsoletos de diagnóstico
    delete_file "diagnose-maven-environment.sh"
    
    # Eliminar archivos obsoletos de exposición
    delete_file "expose-sonarqube-ngrok.sh"
    
    # Eliminar archivos obsoletos de inicio rápido
    delete_file "quick-start-sonarqube.sh"
    
    # Eliminar archivos obsoletos de carga
    delete_file "load-image.sh"
    
    # Eliminar archivos obsoletos de ejecución local
    delete_file "run-local.sh"
    
    # Eliminar archivos obsoletos de verificación de estado
    delete_file "verify-drone-status.sh"
    
    echo ""
    echo "🎉 LIMPIEZA COMPLETADA EXITOSAMENTE!"
    echo "===================================="
    echo "✅ Se eliminaron ~80 archivos obsoletos y duplicados"
    echo "📁 El proyecto ahora está más limpio y organizado"
    echo "🚀 Solo se mantuvieron los archivos esenciales y actualizados"
    
else
    echo "❌ Limpieza cancelada por el usuario"
    echo "💡 Puedes ejecutar este script nuevamente cuando estés listo"
fi
