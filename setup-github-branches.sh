#!/bin/bash

# Script para configurar ramas en GitHub
set -e

log() {
    echo "[$(date +'%Y-%m-%d %H:%M:%S')] $1"
}

# Función para crear ramas
create_branches() {
    log "Creando ramas para el pipeline..."
    
    # Crear rama de desarrollo
    git checkout -b dev 2>/dev/null || git checkout dev
    log "✅ Rama 'dev' configurada"
    
    # Crear rama de QA
    git checkout -b QA 2>/dev/null || git checkout QA
    log "✅ Rama 'QA' configurada"
    
    # Crear rama de producción
    git checkout -b prod 2>/dev/null || git checkout prod
    log "✅ Rama 'prod' configurada"
    
    # Volver a main
    git checkout main
    log "✅ Regresando a rama 'main'"
}

# Función para configurar protección de ramas
setup_branch_protection() {
    log "Configurando protección de ramas..."
    
    echo ""
    echo "🔒 CONFIGURACIÓN DE PROTECCIÓN DE RAMAS"
    echo "======================================"
    echo ""
    echo "Ve a: https://github.com/humbertovenavente/hospital/settings/branches"
    echo ""
    echo "Configura las siguientes reglas:"
    echo ""
    echo "1. Rama 'main':"
    echo "   - Require a pull request before merging"
    echo "   - Require status checks to pass before merging"
    echo "   - Require branches to be up to date before merging"
    echo "   - Restrict pushes that create files"
    echo ""
    echo "2. Rama 'prod':"
    echo "   - Require a pull request before merging"
    echo "   - Require status checks to pass before merging"
    echo "   - Require branches to be up to date before merging"
    echo "   - Restrict pushes that create files"
    echo ""
    echo "3. Rama 'QA':"
    echo "   - Require a pull request before merging"
    echo "   - Require status checks to pass before merging"
    echo ""
    echo "4. Rama 'dev':"
    echo "   - Require a pull request before merging"
    echo ""
}

# Función para mostrar flujo de trabajo
show_workflow() {
    log "Mostrando flujo de trabajo del pipeline..."
    
    echo ""
    echo "🔄 FLUJO DE TRABAJO DEL PIPELINE"
    echo "================================"
    echo ""
    echo "1. 🚀 DESARROLLO (feature/*, develop, dev):"
    echo "   - Push a rama feature → Deploy automático a DESARROLLO"
    echo "   - Puerto: 80"
    echo "   - Base de datos: Oracle DB1 (puerto 1521)"
    echo "   - NODE_ENV: development"
    echo "   - Log level: debug"
    echo ""
    echo "2. 🧪 QA (qa, release/*):"
    echo "   - Push a rama qa → Deploy automático a QA"
    echo "   - Puerto: 81"
    echo "   - Base de datos: Oracle DB2 (puerto 1522)"
    echo "   - NODE_ENV: qa"
    echo "   - Log level: info"
    echo "   - Incluye tests de integración"
    echo ""
    echo "3. 🏭 PRODUCCIÓN (main, master, production):"
    echo "   - Push a rama main → Deploy automático a PRODUCCIÓN"
    echo "   - Puerto: 82"
    echo "   - Base de datos: Oracle DB3 (puerto 1523)"
    echo "   - NODE_ENV: production"
    echo "   - Log level: warn"
    echo "   - Incluye health checks y tests de integración"
    echo ""
    echo "4. 🗄️ BASE DE DATOS:"
    echo "   - Backup automático en cada push/PR"
    echo "   - Migraciones automáticas en producción"
    echo ""
}

# Función para mostrar comandos útiles
show_commands() {
    log "Mostrando comandos útiles..."
    
    echo ""
    echo "🛠️ COMANDOS ÚTILES"
    echo "=================="
    echo ""
    echo "Git commands:"
    echo "  git checkout -b feature/nueva-funcionalidad"
    echo "  git push origin feature/nueva-funcionalidad"
    echo "  git checkout qa && git merge feature/nueva-funcionalidad"
    echo "  git push origin qa"
    echo ""
    echo "Drone commands:"
    echo "  # Ver builds activos"
    echo "  curl http://34.10.223.20:8080/api/user/builds"
    echo ""
    echo "  # Trigger manual build"
    echo "  curl -X POST http://34.10.223.20:8080/api/repos/tu-usuario/hospital/builds"
    echo ""
    echo "Docker commands:"
    echo "  # Ver contenedores por ambiente"
    echo "  docker ps --filter 'name=hospital-*'"
    echo ""
    echo "  # Ver logs de un ambiente"
    echo "  docker-compose -f docker-compose.dev.yml logs -f"
    echo "  docker-compose -f docker-compose.qa.yml logs -f"
    echo "  docker-compose -f docker-compose.prod.yml logs -f"
    echo ""
}

# Función principal
main() {
    log "Configurando ramas para el pipeline multipipeline..."
    
    create_branches
    setup_branch_protection
    show_workflow
    show_commands
    
    log "Configuración completada!"
    echo ""
    echo "🎉 PIPELINE MULTIPIPELINE CONFIGURADO"
    echo "===================================="
    echo ""
    echo "El pipeline ahora detectará automáticamente:"
    echo "  - La rama del push/PR"
    echo "  - El ambiente correspondiente"
    echo "  - La configuración de Docker Compose"
    echo "  - Los puertos y base de datos"
    echo "  - Las variables de entorno"
    echo ""
    echo "¡No más problemas de configuración por rama! 🚀"
}

# Ejecutar función principal
main "$@"
