#!/bin/bash

# Script para copiar la configuración de Oracle compartido a las ramas dev y QA
# Este script automatiza todo el proceso de git

set -e

echo "🔄 Iniciando copia de configuración a ramas dev y QA..."
echo "======================================================"

# Función para verificar si hay cambios pendientes
check_working_tree() {
    if ! git diff-index --quiet HEAD --; then
        echo "❌ Error: Hay cambios pendientes en el working tree"
        echo "   Por favor, haz commit de los cambios antes de continuar"
        git status --short
        return 1
    fi
    return 0
}

# Función para hacer commit de los cambios
commit_changes() {
    echo "📝 Haciendo commit de los cambios de Oracle compartido..."
    
    # Agregar todos los archivos modificados
    git add .
    
    # Hacer commit con mensaje descriptivo
    git commit -m "🏥 Configuración de Oracle compartido entre entornos

- Modificado docker-compose.yml para usar Oracle compartido
- Actualizado docker-compose.dev.yml para usar usuario C##PROYECTO
- Actualizado docker-compose.qa.yml para usar usuario Hospital2
- Creado script setup-oracle-users.sql para configuración automática
- Creado script deploy-all-environments.sh para despliegue automático
- Creado nginx.dev.conf para entorno de desarrollo
- Creado README-ORACLE-COMPARTIDO.md con documentación completa

Usuarios configurados:
- C##PROYECTO (Desarrollo)
- Hospital2 (QA)
- Hospital3 (Producción)"
    
    echo "✅ Commit realizado exitosamente"
}

# Función para copiar configuración a una rama
copy_to_branch() {
    local target_branch=$1
    local current_branch=$(git branch --show-current)
    
    echo "🔄 Copiando configuración a rama: $target_branch"
    
    # Verificar si la rama existe localmente
    if git show-ref --verify --quiet refs/heads/$target_branch; then
        echo "   Rama $target_branch existe localmente, actualizando..."
        git checkout $target_branch
        git pull origin $target_branch
    else
        echo "   Rama $target_branch no existe localmente, creando desde origin..."
        git checkout -b $target_branch origin/$target_branch
    fi
    
    # Hacer merge de la rama prod (que tiene la configuración)
    echo "   Haciendo merge desde rama prod..."
    if git merge prod --no-edit; then
        echo "   ✅ Merge exitoso en rama $target_branch"
    else
        echo "   ⚠️  Conflictos de merge detectados en rama $target_branch"
        echo "   Resolviendo conflictos automáticamente..."
        
        # Resolver conflictos automáticamente (mantener versión de prod)
        git checkout --theirs .
        git add .
        git commit -m "🔧 Resuelto conflicto de merge con rama prod - Oracle compartido"
        
        echo "   ✅ Conflictos resueltos automáticamente"
    fi
    
    # Push a la rama remota
    echo "   Haciendo push a origin/$target_branch..."
    git push origin $target_branch
    
    echo "   ✅ Configuración copiada exitosamente a rama $target_branch"
}

# Función para verificar el estado final
verify_branches() {
    echo ""
    echo "🔍 Verificando estado de todas las ramas..."
    echo ""
    
    # Mostrar estado de todas las ramas
    git branch -vv
    
    echo ""
    echo "📊 Resumen de commits por rama:"
    echo ""
    
    # Mostrar últimos commits de cada rama
    for branch in prod dev QA; do
        if git show-ref --verify --quiet refs/heads/$branch; then
            echo "🌿 Rama $branch:"
            git log --oneline -3 $branch
            echo ""
        fi
    done
}

# Función principal
main() {
    echo "📋 Verificando estado del repositorio..."
    
    # Verificar que estemos en la rama prod
    if [ "$(git branch --show-current)" != "prod" ]; then
        echo "❌ Error: Debes estar en la rama 'prod' para ejecutar este script"
        echo "   Rama actual: $(git branch --show-current)"
        echo "   Ejecuta: git checkout prod"
        exit 1
    fi
    
    # Verificar working tree limpio
    if ! check_working_tree; then
        exit 1
    fi
    
    # Hacer commit de los cambios si no se ha hecho
    if ! git diff-index --quiet HEAD --; then
        commit_changes
    else
        echo "✅ Working tree está limpio, continuando..."
    fi
    
    # Hacer push de la rama prod
    echo "🚀 Haciendo push de la rama prod..."
    git push origin prod
    
    # Copiar configuración a rama dev
    copy_to_branch "dev"
    
    # Copiar configuración a rama QA
    copy_to_branch "QA"
    
    # Volver a la rama prod
    echo "🔄 Volviendo a la rama prod..."
    git checkout prod
    
    # Verificar estado final
    verify_branches
    
    echo ""
    echo "🎉 ¡Configuración copiada exitosamente a todas las ramas!"
    echo ""
    echo "📋 Resumen de lo realizado:"
    echo "   ✅ Rama prod: Configuración de Oracle compartido implementada"
    echo "   ✅ Rama dev: Configuración copiada y sincronizada"
    echo "   ✅ Rama QA: Configuración copiada y sincronizada"
    echo ""
    echo "🔗 Para verificar, puedes hacer checkout a cualquier rama:"
    echo "   git checkout dev    # Para rama de desarrollo"
    echo "   git checkout QA     # Para rama de QA"
    echo "   git checkout prod   # Para volver a producción"
}

# Ejecutar función principal
main
