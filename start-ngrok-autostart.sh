#!/bin/bash

# Script simple para iniciar ngrok para Jenkins
echo "🚀 Iniciando ngrok para Jenkins..."

# Iniciar ngrok en segundo plano
ngrok http 8082 >/tmp/ngrok-jenkins.log 2>&1 &

# Esperar un poco
sleep 3

# Mostrar la URL
echo "✅ ngrok iniciado"
echo "📋 Revisar logs en /tmp/ngrok-jenkins.log"
echo "🌐 URL disponible en: http://localhost:4040"
