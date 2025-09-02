#!/bin/bash

# Script de diagnóstico para verificar el entorno de Maven
echo "🔍 Diagnóstico del entorno de Maven..."

echo "📋 Información del sistema:"
echo "Usuario actual: $(whoami)"
echo "Directorio actual: $(pwd)"
echo "UID: $(id -u)"
echo "GID: $(id -g)"

echo ""
echo "📁 Verificando directorios:"
echo "Home del usuario: $HOME"
echo "Directorio /tmp: $(ls -la /tmp | head -5)"
echo "Directorio /home/sonarqube: $(ls -la /home/sonarqube 2>/dev/null || echo 'No existe o sin permisos')"

echo ""
echo "🔧 Variables de entorno Maven:"
echo "MAVEN_HOME: ${MAVEN_HOME:-'No definido'}"
echo "M2_HOME: ${M2_HOME:-'No definido'}"
echo "MAVEN_OPTS: ${MAVEN_OPTS:-'No definido'}"

echo ""
echo "📦 Verificando Maven:"
echo "Maven wrapper: $(ls -la ./mvnw 2>/dev/null || echo 'No encontrado')"
echo "Maven directo: $(which mvn 2>/dev/null || echo 'No encontrado')"

echo ""
echo "🐳 Verificando Docker:"
echo "Docker socket: $(ls -la /var/run/docker.sock 2>/dev/null || echo 'No encontrado')"

echo ""
echo "🔐 Permisos de escritura:"
echo "Puede escribir en /tmp: $(touch /tmp/test-write 2>/dev/null && echo 'SÍ' && rm -f /tmp/test-write || echo 'NO')"
echo "Puede escribir en /home/sonarqube: $(touch /home/sonarqube/test-write 2>/dev/null && echo 'SÍ' && rm -f /home/sonarqube/test-write || echo 'NO')"

echo ""
echo "✅ Diagnóstico completado"
