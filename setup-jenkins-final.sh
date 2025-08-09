#!/bin/bash

echo "🚀 CONFIGURACIÓN FINAL DE JENKINS - HOSPITAL CI/CD"
echo "=================================================="
echo ""

# Verificar que Jenkins esté corriendo
echo "🔍 Verificando estado de Jenkins..."
if curl -s http://localhost:8081 > /dev/null; then
    echo "✅ Jenkins está corriendo en http://localhost:8081"
else
    echo "❌ Jenkins no está corriendo. Iniciando..."
    ./start-jenkins.sh
    sleep 5
fi

echo ""
echo "📋 PASOS PARA COMPLETAR LA CONFIGURACIÓN:"
echo ""

echo "1️⃣  INSTALAR PLUGIN JUNIT:"
echo "   • Abre tu navegador: http://localhost:8081"
echo "   • Ve a 'Manage Jenkins' > 'Manage Plugins'"
echo "   • Pestaña 'Available'"
echo "   • Busca 'JUnit'"
echo "   • Marca 'JUnit Plugin'"
echo "   • Haz clic en 'Install without restart'"
echo "   • Espera a que termine la instalación"
echo ""

echo "2️⃣  EJECUTAR PIPELINE:"
echo "   • Ve a tu pipeline 'Hospital-CI-CD'"
echo "   • Haz clic en 'Build Now'"
echo "   • El pipeline debería ejecutarse sin errores"
echo ""

echo "3️⃣  VERIFICAR RESULTADOS:"
echo "   • Los tests deberían pasar (155 tests)"
echo "   • Se deberían publicar los resultados de JUnit"
echo "   • El pipeline debería continuar hasta el final"
echo ""

echo "🔧 COMANDOS ÚTILES:"
echo "   • Ver logs de Jenkins: tail -f /var/log/jenkins/jenkins.log"
echo "   • Reiniciar Jenkins: ./stop-jenkins.sh && ./start-jenkins.sh"
echo "   • Ver estado: curl -s http://localhost:8081"
echo ""

echo "📊 ESTADO ACTUAL DEL PIPELINE:"
echo "   ✅ Checkout SCM - EXITOSO"
echo "   ✅ Checkout - EXITOSO"
echo "   ✅ Setup Tools - EXITOSO"
echo "   ✅ Build Backend - EXITOSO"
echo "   ❌ Test Backend - FALLA (falta plugin JUnit)"
echo "   ⏸️  Build Frontend - PENDIENTE"
echo "   ⏸️  Build Docker Images - PENDIENTE"
echo "   ⏸️  Deploy to Development - PENDIENTE"
echo "   ⏸️  Deploy to QA - PENDIENTE"
echo ""

echo "🎯 OBJETIVO: Instalar JUnit Plugin para que publishTestResults funcione"
echo ""
echo "✅ Una vez instalado el plugin, el pipeline debería ejecutarse completamente"
echo "" 