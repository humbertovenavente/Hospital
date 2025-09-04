#!/bin/bash

# Script para migrar SonarQube local a la nube
echo "🔄 Migrando SonarQube local a la nube..."

# Verificar si SonarQube local está ejecutándose
if ! curl -s --connect-timeout 5 http://localhost:9000 > /dev/null; then
    echo "❌ SonarQube local no está ejecutándose"
    echo "   Inicia SonarQube local primero: docker-compose up -d"
    exit 1
fi

echo "✅ SonarQube local detectado"

# Crear script de exportación de datos
cat > export-sonarqube-data.sh << 'EOF'
#!/bin/bash
echo "📤 Exportando datos de SonarQube local..."

# Exportar proyectos
echo "📊 Exportando proyectos..."
curl -u admin:admin "http://localhost:9000/api/projects/search" > projects.json

# Exportar reglas de calidad
echo "📋 Exportando reglas de calidad..."
curl -u admin:admin "http://localhost:9000/api/qualityprofiles/search" > quality-profiles.json

# Exportar configuraciones
echo "⚙️ Exportando configuraciones..."
curl -u admin:admin "http://localhost:9000/api/settings/values" > settings.json

echo "✅ Datos exportados correctamente"
EOF

chmod +x export-sonarqube-data.sh

echo "📋 Pasos para migrar:"
echo "1. Ejecuta en tu computadora local: ./export-sonarqube-data.sh"
echo "2. Sube los archivos JSON al servidor"
echo "3. Ejecuta en el servidor: ./setup-sonarqube-cloud.sh"
echo "4. Importa los datos en el nuevo SonarQube"

echo ""
echo "🔑 La llave sqa_8f9c9ffeaf833e1486015527efadabc251e75755 funcionará en ambos"
echo "🌐 SonarQube local: http://localhost:9000"
echo "🌐 SonarQube nube: http://104.197.237.11:9000"
