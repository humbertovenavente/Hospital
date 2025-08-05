#!/bin/bash

# Script para instalar el plugin JUnit en Jenkins
echo "🔧 Instalando plugin JUnit en Jenkins..."

# Verificar que Jenkins esté ejecutándose
if ! ss -tlnp | grep 8081 > /dev/null; then
    echo "❌ Jenkins no está ejecutándose en el puerto 8081"
    exit 1
fi

echo "📋 Instrucciones para instalar el plugin JUnit:"
echo ""
echo "1. Abre tu navegador y ve a: http://localhost:8081"
echo ""
echo "2. Ve a: Manage Jenkins > Manage Plugins"
echo ""
echo "3. En la pestaña 'Available', busca 'JUnit'"
echo ""
echo "4. Marca la casilla y haz clic en 'Install without restart'"
echo ""
echo "5. Espera a que se complete la instalación"
echo ""
echo "6. Una vez instalado, el pipeline debería funcionar correctamente"
echo ""
echo "🔗 URL directa: http://localhost:8081/pluginManager/available"
echo ""
echo "💡 Alternativa: También puedes instalar desde la línea de comandos si tienes credenciales:"
echo "   curl -X POST -u admin:PASSWORD http://localhost:8081/pluginManager/installNecessaryPlugins -d '<jenkins><install plugin=\"junit@latest\"/></jenkins>' -H 'Content-Type: text/xml'"
echo ""
echo "✅ Una vez instalado el plugin, ejecuta el pipeline nuevamente" 