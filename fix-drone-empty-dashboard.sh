#!/bin/bash

# Script para diagnosticar y solucionar el problema del dashboard vacío de Drone
# Ejecutar en la instancia de GCP

set -e

echo "🔍 Diagnosticando problema del dashboard vacío de Drone..."
echo ""

# 1. Verificar contenedores de Drone
echo "1️⃣ Verificando contenedores de Drone..."
echo "=========================================="
docker ps -a | grep -E "drone-server|drone-runner" || echo "❌ No se encontraron contenedores de Drone"
echo ""

# 2. Verificar logs del servidor de Drone
echo "2️⃣ Revisando logs del servidor de Drone (últimas 50 líneas)..."
echo "==============================================================="
docker logs drone-server --tail 50 2>&1 | grep -i -E "error|github|oauth|sync" || echo "No se encontraron errores relacionados"
echo ""

# 3. Verificar variables de entorno
echo "3️⃣ Verificando configuración de GitHub OAuth..."
echo "================================================="
if [ -f "drone.env" ]; then
    echo "✅ Archivo drone.env encontrado"
    echo "Client ID: $(grep DRONE_GITHUB_CLIENT_ID drone.env | cut -d'=' -f2)"
    echo "Server Host: $(grep DRONE_SERVER_HOST drone.env | cut -d'=' -f2)"
    echo "Server Proto: $(grep DRONE_SERVER_PROTO drone.env | cut -d'=' -f2)"
else
    echo "❌ Archivo drone.env no encontrado"
fi
echo ""

# 4. Verificar que el puerto 8002 esté escuchando
echo "4️⃣ Verificando que el puerto 8002 esté accesible..."
echo "===================================================="
if curl -f -s http://localhost:8002 > /dev/null 2>&1; then
    echo "✅ Drone está respondiendo en el puerto 8002"
else
    echo "❌ Drone no está respondiendo en el puerto 8002"
fi
echo ""

# 5. Verificar configuración de la aplicación OAuth de GitHub
echo "5️⃣ INSTRUCCIONES PARA SOLUCIONAR EL PROBLEMA:"
echo "=============================================="
echo ""
echo "El dashboard está vacío porque Drone necesita:"
echo "1. Que hagas login/autorización con GitHub"
echo "2. Que la aplicación OAuth de GitHub tenga el callback URL correcto"
echo ""
echo "📋 PASOS A SEGUIR:"
echo ""
echo "A) Verificar/Configurar la aplicación OAuth de GitHub:"
echo "   1. Ve a: https://github.com/settings/developers"
echo "   2. Busca la aplicación 'Drone CI/CD - Hospital' o crea una nueva"
echo "   3. Verifica que el 'Authorization callback URL' sea:"
echo "      http://34.61.228.49:8002/login"
echo "   4. Anota el Client ID y Client Secret"
echo ""
echo "B) Reiniciar Drone con la configuración correcta:"
echo "   1. Edita drone.env si es necesario"
echo "   2. Reinicia los contenedores:"
echo "      docker-compose -f docker-compose.drone.yml down"
echo "      docker-compose -f docker-compose.drone.yml --env-file drone.env up -d"
echo ""
echo "C) Acceder a Drone y autorizar:"
echo "   1. Ve a: http://34.61.228.49:8002"
echo "   2. Haz clic en 'Login with GitHub'"
echo "   3. Autoriza la aplicación"
echo "   4. Después del login, ve a la configuración del repositorio"
echo "   5. Activa el repositorio 'humbertovenavente/Hospital'"
echo ""
echo "D) Verificar sincronización:"
echo "   1. Haz clic en el botón 'SYNC' en el dashboard"
echo "   2. Espera unos segundos"
echo "   3. Los repositorios deberían aparecer"
echo ""

# 6. Ofrecer reiniciar Drone
read -p "¿Deseas reiniciar los contenedores de Drone ahora? (s/n): " -n 1 -r
echo
if [[ $REPLY =~ ^[Ss]$ ]]; then
    echo "🔄 Reiniciando contenedores de Drone..."
    if [ -f "docker-compose.drone.yml" ] && [ -f "drone.env" ]; then
        docker-compose -f docker-compose.drone.yml down
        sleep 2
        docker-compose -f docker-compose.drone.yml --env-file drone.env up -d
        echo "✅ Contenedores reiniciados"
        echo "⏳ Espera 30 segundos para que Drone se inicie completamente..."
        sleep 30
        echo "🌐 Accede a: http://34.61.228.49:8002"
    else
        echo "❌ No se encontraron los archivos necesarios"
    fi
fi

echo ""
echo "✅ Diagnóstico completado"
echo ""

