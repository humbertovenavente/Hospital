#!/bin/bash

# Script para solucionar problemas de webhook en Drone CI

set -e

echo "🔧 Solucionando problemas de webhook en Drone CI..."

# Verificar que Drone esté ejecutándose
if ! docker ps | grep -q drone-server; then
    echo "❌ Error: Drone server no está ejecutándose"
    echo "Ejecuta: docker-compose -f docker-compose.drone-local.yml up -d"
    exit 1
fi

echo "✅ Drone server está ejecutándose"

# Verificar configuración de GitHub OAuth
echo "🔍 Verificando configuración de GitHub OAuth..."

# Verificar que las variables de entorno estén configuradas
if [ -z "$DRONE_GITHUB_CLIENT_ID" ] || [ -z "$DRONE_GITHUB_CLIENT_SECRET" ]; then
    echo "❌ Error: Variables de GitHub OAuth no configuradas"
    echo "Configura las siguientes variables en docker-compose.drone-local.yml:"
    echo "  - DRONE_GITHUB_CLIENT_ID"
    echo "  - DRONE_GITHUB_CLIENT_SECRET"
    exit 1
fi

echo "✅ Variables de GitHub OAuth configuradas"

# Verificar que el repositorio esté configurado en GitHub
echo "🔍 Verificando configuración del repositorio en GitHub..."

# Verificar que el webhook esté configurado en GitHub
echo "📋 Pasos para configurar el webhook en GitHub:"
echo ""
echo "1. Ve a https://github.com/humbertovenavente/Hospital/settings/hooks"
echo "2. Haz clic en 'Add webhook'"
echo "3. Configura:"
echo "   - Payload URL: http://34.10.223.20:8001/hook"
echo "   - Content type: application/json"
echo "   - Secret: (deja vacío o usa el RPC_SECRET)"
echo "   - Events: Push, Pull Request"
echo "4. Haz clic en 'Add webhook'"
echo ""

# Verificar conectividad
echo "🔍 Verificando conectividad..."

# Verificar que el servidor responda
if curl -s http://localhost:8001/api/user > /dev/null; then
    echo "✅ Servidor Drone responde correctamente"
else
    echo "❌ Error: Servidor Drone no responde"
    exit 1
fi

# Verificar logs de errores
echo "🔍 Verificando logs de errores..."
docker logs drone-server-local --tail 10 | grep -i error || echo "✅ No se encontraron errores recientes"

echo ""
echo "🎯 Soluciones para el error 'Validation Failed':"
echo ""
echo "1. **Verificar configuración de GitHub OAuth:**"
echo "   - Asegúrate de que DRONE_GITHUB_CLIENT_ID y DRONE_GITHUB_CLIENT_SECRET estén correctos"
echo "   - Verifica que la aplicación OAuth esté configurada en GitHub"
echo ""
echo "2. **Configurar webhook en GitHub:**"
echo "   - Ve a la configuración del repositorio en GitHub"
echo "   - Agrega un webhook apuntando a tu servidor Drone"
echo ""
echo "3. **Reiniciar Drone:**"
echo "   - docker-compose -f docker-compose.drone-local.yml restart"
echo ""
echo "4. **Verificar permisos del repositorio:**"
echo "   - Asegúrate de que la aplicación OAuth tenga permisos para acceder al repositorio"
echo ""
echo "5. **Activar repositorio manualmente:**"
echo "   - Ve a http://localhost:8001"
echo "   - Haz login con GitHub"
echo "   - Ve al repositorio Hospital"
echo "   - Haz clic en 'ACTIVATE REPOSITORY'"
echo ""

echo "🚀 Una vez solucionado, ejecuta:"
echo "   ./configure-drone-secrets.sh"



