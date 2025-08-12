#!/bin/bash

# Script manual para copiar configuración de Oracle compartido a ramas dev y QA
# Este script te guía paso a paso

echo "🔄 Script Manual para Copiar Configuración a Ramas dev y QA"
echo "=========================================================="
echo ""

# Verificar rama actual
current_branch=$(git branch --show-current)
echo "📍 Rama actual: $current_branch"
echo ""

if [ "$current_branch" != "prod" ]; then
    echo "⚠️  ADVERTENCIA: No estás en la rama 'prod'"
    echo "   Para continuar, ejecuta: git checkout prod"
    echo ""
    read -p "¿Quieres cambiar a la rama prod ahora? (y/n): " -n 1 -r
    echo
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        git checkout prod
        echo "✅ Cambiado a rama prod"
    else
        echo "❌ Abortando operación"
        exit 1
    fi
fi

echo ""
echo "📋 Pasos para copiar la configuración:"
echo "======================================"
echo ""

# Paso 1: Commit de cambios
echo "1️⃣  PASO 1: Hacer commit de los cambios actuales"
echo "   Ejecuta estos comandos:"
echo "   git add ."
echo "   git commit -m '🏥 Configuración Oracle compartido'"
echo "   git push origin prod"
echo ""

read -p "¿Has completado el paso 1? (y/n): " -n 1 -r
echo
if [[ ! $REPLY =~ ^[Yy]$ ]]; then
    echo "❌ Completa el paso 1 antes de continuar"
    exit 1
fi

# Paso 2: Rama dev
echo ""
echo "2️⃣  PASO 2: Copiar a rama dev"
echo "   Ejecuta estos comandos:"
echo "   git checkout dev"
echo "   git pull origin dev"
echo "   git merge prod"
echo "   git push origin dev"
echo ""

read -p "¿Quieres que ejecute automáticamente el paso 2? (y/n): " -n 1 -r
echo
if [[ $REPLY =~ ^[Yy]$ ]]; then
    echo "🔄 Ejecutando paso 2 automáticamente..."
    git checkout dev
    git pull origin dev
    if git merge prod --no-edit; then
        echo "✅ Merge exitoso en rama dev"
    else
        echo "⚠️  Conflictos detectados, resolviendo automáticamente..."
        git checkout --theirs .
        git add .
        git commit -m "🔧 Resuelto conflicto con rama prod"
    fi
    git push origin dev
    echo "✅ Configuración copiada a rama dev"
else
    echo "📝 Ejecuta manualmente los comandos del paso 2"
fi

# Paso 3: Rama QA
echo ""
echo "3️⃣  PASO 3: Copiar a rama QA"
echo "   Ejecuta estos comandos:"
echo "   git checkout QA"
echo "   git pull origin QA"
echo "   git merge prod"
echo "   git push origin QA"
echo ""

read -p "¿Quieres que ejecute automáticamente el paso 3? (y/n): " -n 1 -r
echo
if [[ $REPLY =~ ^[Yy]$ ]]; then
    echo "🔄 Ejecutando paso 3 automáticamente..."
    git checkout QA
    git pull origin QA
    if git merge prod --no-edit; then
        echo "✅ Merge exitoso en rama QA"
    else
        echo "⚠️  Conflictos detectados, resolviendo automáticamente..."
        git checkout --theirs .
        git add .
        git commit -m "🔧 Resuelto conflicto con rama prod"
    fi
    git push origin QA
    echo "✅ Configuración copiada a rama QA"
else
    echo "📝 Ejecuta manualmente los comandos del paso 3"
fi

# Paso 4: Volver a prod
echo ""
echo "4️⃣  PASO 4: Volver a rama prod"
echo "   Ejecuta: git checkout prod"
echo ""

read -p "¿Quieres que ejecute automáticamente el paso 4? (y/n): " -n 1 -r
echo
if [[ $REPLY =~ ^[Yy]$ ]]; then
    git checkout prod
    echo "✅ Vuelto a rama prod"
else
    echo "📝 Ejecuta manualmente: git checkout prod"
fi

echo ""
echo "🎉 ¡Proceso completado!"
echo ""
echo "📋 Resumen:"
echo "   ✅ Rama prod: Configuración implementada"
echo "   ✅ Rama dev: Configuración copiada"
echo "   ✅ Rama QA: Configuración copiada"
echo ""
echo "🔍 Para verificar el estado:"
echo "   git branch -a"
echo "   git log --oneline -3 dev"
echo "   git log --oneline -3 QA"
