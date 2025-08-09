#!/bin/bash

# Script para detener Jenkins
echo "🛑 Deteniendo Jenkins..."

# Buscar procesos de Jenkins
JENKINS_PIDS=$(ps aux | grep jenkins.war | grep -v grep | awk '{print $2}')

if [ -z "$JENKINS_PIDS" ]; then
    echo "ℹ️  Jenkins no está ejecutándose"
else
    echo "🔍 Procesos de Jenkins encontrados: $JENKINS_PIDS"
    
    # Detener procesos de Jenkins
    for pid in $JENKINS_PIDS; do
        echo "🛑 Deteniendo proceso $pid..."
        sudo kill $pid
    done
    
    # Esperar a que se detengan
    sleep 5
    
    # Verificar si se detuvieron
    REMAINING_PIDS=$(ps aux | grep jenkins.war | grep -v grep | awk '{print $2}')
    if [ -z "$REMAINING_PIDS" ]; then
        echo "✅ Jenkins detenido correctamente"
    else
        echo "⚠️  Algunos procesos aún están ejecutándose: $REMAINING_PIDS"
        echo "🔧 Forzando detención..."
        for pid in $REMAINING_PIDS; do
            sudo kill -9 $pid
        done
        echo "✅ Jenkins detenido forzadamente"
    fi
fi

# Verificar que el puerto esté libre
if netstat -tlnp | grep 8081 > /dev/null; then
    echo "⚠️  El puerto 8081 aún está en uso"
else
    echo "✅ Puerto 8081 liberado"
fi 