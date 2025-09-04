#!/bin/bash

# Script para exponer SonarQube usando ngrok
echo "🌐 Exponiendo SonarQube con ngrok..."

# Verificar si ngrok está instalado
if ! command -v ngrok &> /dev/null; then
    echo "📦 Instalando ngrok..."
    curl -s https://ngrok-agent.s3.amazonaws.com/ngrok.asc | sudo tee /etc/apt/trusted.gpg.d/ngrok.asc >/dev/null
    echo "deb https://ngrok-agent.s3.amazonaws.com buster main" | sudo tee /etc/apt/sources.list.d/ngrok.list
    sudo apt update && sudo apt install ngrok
fi

# Verificar que SonarQube esté ejecutándose
if ! curl -s --connect-timeout 5 http://localhost:9000 > /dev/null; then
    echo "❌ SonarQube no está ejecutándose en localhost:9000"
    echo "   Ejecuta primero: ./quick-start-sonarqube.sh"
    exit 1
fi

echo "✅ SonarQube está ejecutándose localmente"

# Crear túnel ngrok
echo "🚇 Creando túnel ngrok..."
echo "   Esto expondrá SonarQube públicamente"
echo "   Presiona Ctrl+C para detener el túnel"

ngrok http 9000