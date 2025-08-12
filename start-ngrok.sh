#!/bin/bash

# Script para iniciar ngrok automáticamente
# Colocar en ~/.config/autostart/ para inicio automático

echo "🚀 Iniciando túnel ngrok para Jenkins..."
echo "⏰ $(date)"

# Verificar si Jenkins está corriendo
if ! curl -f "http://localhost:8081" >/dev/null 2>&1; then
    echo "❌ Jenkins no está disponible en puerto 8081"
    echo "💡 Asegúrate de que Jenkins esté ejecutándose"
    exit 1
fi

# Verificar si ngrok ya está corriendo
if pgrep -f "ngrok http 8081" >/dev/null; then
    echo "✅ ngrok ya está ejecutándose"
    exit 0
fi

# Iniciar ngrok
echo "🔧 Iniciando túnel ngrok..."
ngrok http 8081 >/tmp/ngrok.log 2>&1 &

# Esperar a que se inicie
sleep 5

# Obtener la URL pública
NGROK_URL=$(curl -s http://localhost:4040/api/tunnels | jq -r '.tunnels[0].public_url' 2>/dev/null)

if [ "$NGROK_URL" != "null" ] && [ "$NGROK_URL" != "" ]; then
    echo "✅ Túnel ngrok iniciado exitosamente"
    echo "🌐 URL pública: $NGROK_URL"
    echo "🔗 Webhook URL: $NGROK_URL/github-webhook/"
    
    # Guardar la URL en un archivo para referencia
    echo "$NGROK_URL" > /tmp/ngrok-jenkins-url.txt
    
    # Mostrar notificación del sistema
    if command -v notify-send >/dev/null 2>&1; then
        notify-send "ngrok Jenkins" "Túnel iniciado: $NGROK_URL"
    fi
else
    echo "❌ Error iniciando túnel ngrok"
    echo "📋 Revisar logs en /tmp/ngrok.log"
fi
