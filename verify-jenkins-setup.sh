#!/bin/bash

# Script para verificar la configuración de Jenkins
echo "🔍 Verificando configuración de Jenkins..."

# Verificar que Jenkins esté ejecutándose
echo "📋 1. Verificando que Jenkins esté ejecutándose..."
if ss -tlnp | grep 8081 > /dev/null; then
    echo "✅ Jenkins está ejecutándose en el puerto 8081"
else
    echo "❌ Jenkins NO está ejecutándose en el puerto 8081"
    exit 1
fi

# Verificar permisos de Docker
echo ""
echo "📋 2. Verificando permisos de Docker..."
if groups jenkins | grep docker > /dev/null; then
    echo "✅ Usuario jenkins está en el grupo docker"
else
    echo "❌ Usuario jenkins NO está en el grupo docker"
fi

if [ -r /var/run/docker.sock ]; then
    echo "✅ Jenkins puede leer el socket de Docker"
else
    echo "❌ Jenkins NO puede leer el socket de Docker"
fi

# Verificar Java
echo ""
echo "📋 3. Verificando Java..."
if java -version 2>&1 | grep "17" > /dev/null; then
    echo "✅ Java 17 está instalado"
else
    echo "❌ Java 17 NO está instalado"
fi

# Verificar Maven
echo ""
echo "📋 4. Verificando Maven..."
if mvn -version > /dev/null 2>&1; then
    echo "✅ Maven está instalado"
else
    echo "❌ Maven NO está instalado"
fi

# Verificar Node.js
echo ""
echo "📋 5. Verificando Node.js..."
if node --version > /dev/null 2>&1; then
    echo "✅ Node.js está instalado"
else
    echo "❌ Node.js NO está instalado"
fi

# Verificar Docker
echo ""
echo "📋 6. Verificando Docker..."
if docker --version > /dev/null 2>&1; then
    echo "✅ Docker está instalado"
else
    echo "❌ Docker NO está instalado"
fi

echo ""
echo "📋 7. Información del pipeline:"
echo "   - URL de Jenkins: http://localhost:8081"
echo "   - Repositorio: https://github.com/humbertovenavente/Hospital.git"
echo "   - Branch: dev"
echo "   - Script: Jenkinsfile"
echo ""
echo "📋 8. Próximos pasos:"
echo "   1. Instalar plugin JUnit: ./install-junit-plugin.sh"
echo "   2. Crear pipeline job en Jenkins"
echo "   3. Ejecutar el pipeline"
echo ""
echo "🔗 URLs útiles:"
echo "   - Jenkins: http://localhost:8081"
echo "   - Plugins: http://localhost:8081/pluginManager/available"
echo "   - New Job: http://localhost:8081/newJob" 