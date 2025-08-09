#!/bin/bash

echo "🐳 INSTALANDO DOCKER PLUGIN EN JENKINS"
echo "======================================"
echo ""

echo "📋 Pasos para instalar el Docker Plugin:"
echo ""

echo "1️⃣  Abre Jenkins en tu navegador: http://localhost:8081"
echo ""

echo "2️⃣  Ve a 'Manage Jenkins' > 'Manage Plugins'"
echo ""

echo "3️⃣  Haz clic en la pestaña 'Available'"
echo ""

echo "4️⃣  Busca 'Docker' en el campo de búsqueda"
echo ""

echo "5️⃣  Marca las siguientes opciones:"
echo "   • Docker Plugin"
echo "   • Docker Pipeline"
echo "   • Docker API Plugin"
echo ""

echo "6️⃣  Haz clic en 'Install without restart'"
echo ""

echo "7️⃣  Espera a que se complete la instalación"
echo ""

echo "8️⃣  Una vez instalado, ve a tu pipeline 'Hospital-CI-CD'"
echo ""

echo "9️⃣  Haz clic en 'Build Now' para ejecutar el pipeline nuevamente"
echo ""

echo "✅ El plugin Docker permitirá que el pipeline construya imágenes Docker"
echo ""

echo "🔍 Alternativamente, puedes instalar desde la línea de comandos:"
echo "   curl -X POST http://localhost:8081/pluginManager/installNecessaryPlugins -d '<jenkins><install plugin=docker@latest/></jenkins>'"
echo ""

echo "📊 ESTADO ACTUAL DEL PIPELINE:"
echo "   ✅ Checkout SCM - EXITOSO"
echo "   ✅ Checkout - EXITOSO"
echo "   ✅ Setup Tools - EXITOSO"
echo "   ✅ Build Backend - EXITOSO"
echo "   ✅ Test Backend - EXITOSO"
echo "   ✅ Build Frontend - EXITOSO"
echo "   ❌ Build Docker Images - FALLA (falta plugin Docker)"
echo "   ⏸️  Deploy to Development - PENDIENTE"
echo "   ⏸️  Deploy to QA - PENDIENTE"
echo "   ⏸️  Deploy to Production - PENDIENTE"
echo ""

echo "🎯 OBJETIVO: Instalar Docker Plugin para que docker.build funcione"
echo "" 