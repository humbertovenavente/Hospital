#!/bin/bash

# Script para diagnosticar problemas de activación del repositorio en Drone

set -e

echo "🔍 Diagnosticando problemas de activación del repositorio en Drone CI..."

# Verificar que Drone esté ejecutándose
echo "1. Verificando que Drone esté ejecutándose..."
if docker ps | grep -q drone-server; then
    echo "✅ Drone server está ejecutándose"
else
    echo "❌ Drone server no está ejecutándose"
    echo "Ejecuta: docker-compose -f docker-compose.drone-local.yml --env-file drone-local.env up -d"
    exit 1
fi

# Verificar conectividad
echo ""
echo "2. Verificando conectividad del servidor..."
if curl -s http://localhost:8001/api/user > /dev/null; then
    echo "✅ Servidor Drone responde correctamente"
else
    echo "❌ Servidor Drone no responde"
    exit 1
fi

# Verificar configuración de GitHub OAuth
echo ""
echo "3. Verificando configuración de GitHub OAuth..."
CLIENT_ID=$(docker exec drone-server-local env | grep DRONE_GITHUB_CLIENT_ID | cut -d'=' -f2)
CLIENT_SECRET=$(docker exec drone-server-local env | grep DRONE_GITHUB_CLIENT_SECRET | cut -d'=' -f2)

if [ -n "$CLIENT_ID" ] && [ -n "$CLIENT_SECRET" ]; then
    echo "✅ Variables de GitHub OAuth configuradas:"
    echo "   - CLIENT_ID: ${CLIENT_ID:0:10}..."
    echo "   - CLIENT_SECRET: ${CLIENT_SECRET:0:10}..."
else
    echo "❌ Variables de GitHub OAuth no configuradas"
    exit 1
fi

# Verificar logs de errores recientes
echo ""
echo "4. Verificando logs de errores recientes..."
echo "Últimos 20 logs del servidor Drone:"
docker logs drone-server-local --tail 20 | grep -E "(error|Error|ERROR|failed|Failed|FAILED)" || echo "✅ No se encontraron errores recientes"

# Verificar configuración del repositorio
echo ""
echo "5. Verificando configuración del repositorio..."
echo "Verificando si el repositorio existe en GitHub..."

# Verificar que el token de GitHub sea válido
echo ""
echo "6. Verificando token de GitHub..."
if curl -s "https://api.github.com/user" -H "Authorization: token Mg5tpUEVScyUNBGv3o5GyVbnz6ctsD2A" | grep -q "login"; then
    echo "✅ Token de GitHub es válido"
else
    echo "❌ Token de GitHub no es válido o ha expirado"
    echo "Por favor, verifica el token en: https://github.com/settings/tokens"
fi

# Verificar permisos del token
echo ""
echo "7. Verificando permisos del token de GitHub..."
REPO_ACCESS=$(curl -s "https://api.github.com/repos/humbertovenavente/Hospital" -H "Authorization: token Mg5tpUEVScyUNBGv3o5GyVbnz6ctsD2A" | grep -o '"permissions"' || echo "no_permissions")
if [ "$REPO_ACCESS" = "permissions" ]; then
    echo "✅ Token tiene acceso al repositorio"
else
    echo "❌ Token no tiene acceso al repositorio o el repositorio no existe"
fi

# Verificar configuración de webhooks
echo ""
echo "8. Verificando configuración de webhooks..."
echo "Webhooks configurados en el repositorio:"
curl -s "https://api.github.com/repos/humbertovenavente/Hospital/hooks" -H "Authorization: token Mg5tpUEVScyUNBGv3o5GyVbnz6ctsD2A" | grep -o '"url":"[^"]*"' || echo "No se encontraron webhooks"

echo ""
echo "🎯 Soluciones recomendadas:"
echo ""
echo "1. **Verificar token de GitHub:**"
echo "   - Ve a https://github.com/settings/tokens"
echo "   - Verifica que el token tenga los permisos necesarios:"
echo "     - repo (acceso completo al repositorio)"
echo "     - admin:repo_hook (crear webhooks)"
echo "     - admin:org_hook (si es un repositorio de organización)"
echo ""
echo "2. **Verificar aplicación OAuth de GitHub:**"
echo "   - Ve a https://github.com/settings/applications"
echo "   - Verifica que la aplicación OAuth esté configurada correctamente"
echo "   - Authorization callback URL debe ser: http://localhost:8001/login"
echo ""
echo "3. **Configurar webhook manualmente:**"
echo "   - Ve a https://github.com/humbertovenavente/Hospital/settings/hooks"
echo "   - Agrega un webhook con:"
echo "     - Payload URL: http://localhost:8001/hook"
echo "     - Content type: application/json"
echo "     - Events: Push, Pull Request"
echo ""
echo "4. **Reiniciar Drone:**"
echo "   - docker-compose -f docker-compose.drone-local.yml --env-file drone-local.env restart"
echo ""
echo "5. **Activar repositorio manualmente:**"
echo "   - Ve a http://localhost:8001"
echo "   - Haz login con GitHub"
echo "   - Ve al repositorio Hospital"
echo "   - Haz clic en 'ACTIVATE REPOSITORY'"
echo ""
echo "6. **Verificar logs en tiempo real:**"
echo "   - docker logs -f drone-server-local"


