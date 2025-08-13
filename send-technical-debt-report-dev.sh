#!/bin/bash

# Script para enviar reporte de deuda técnica en desarrollo
echo "📧 Enviando reporte de deuda técnica para DEV..."

# Configuración
API_URL="http://localhost:8060"
PROJECT_KEY="hospital-backend-dev"
PROJECT_NAME="Hospital Backend - DEV [RAMA DEV]"
RECIPIENT_EMAIL="jflores@unis.edu.gt"

# Verificar que el backend esté disponible
echo "🔍 Verificando disponibilidad del backend..."
for i in {1..30}; do
    if curl -f $API_URL/q/health >/dev/null 2>&1; then
        echo "✅ Backend está disponible"
        break
    fi
    echo "⏳ Esperando que el backend esté disponible... ($i/30)"
    sleep 5
    
    if [ $i -eq 30 ]; then
        echo "❌ Error: Backend no está disponible después de 30 intentos"
        exit 1
    fi
done

# Enviar reporte de deuda técnica
echo "📤 Enviando reporte de deuda técnica..."
response=$(curl -s -X POST "$API_URL/api/email/technical-debt" \
     -H "Content-Type: application/json" \
     -d "{
         \"projectKey\": \"$PROJECT_KEY\",
         \"projectName\": \"$PROJECT_NAME\",
         \"recipientEmail\": \"$RECIPIENT_EMAIL\"
     }")

# Verificar respuesta
if [ $? -eq 0 ]; then
    echo "✅ Reporte de deuda técnica enviado exitosamente"
    echo "📋 Respuesta: $response"
else
    echo "❌ Error enviando reporte de deuda técnica"
    echo "📋 Respuesta: $response"
    exit 1
fi

echo ""
echo "📊 Detalles del reporte:"
echo "  🔧 Proyecto: $PROJECT_NAME"
echo "  🎯 Destinatario: $RECIPIENT_EMAIL"
echo "  🌐 SonarQube: http://localhost:9000/dashboard?id=$PROJECT_KEY"
echo "  📧 Email enviado desde: humbertovenavente7@gmail.com"
echo ""
echo "✨ ¡Reporte de deuda técnica DEV enviado correctamente!"
