#!/bin/bash

echo "🔐 RESETEAR CONTRASEÑA DE JENKINS"
echo "=================================="
echo ""

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${YELLOW}⚠️  ADVERTENCIA: Este script reseteará la contraseña del usuario 'jose' en Jenkins${NC}"
echo ""

# Solicitar nueva contraseña
read -s -p "Ingresa la nueva contraseña para el usuario 'jose': " NEW_PASSWORD
echo ""
read -s -p "Confirma la nueva contraseña: " CONFIRM_PASSWORD
echo ""

if [ "$NEW_PASSWORD" != "$CONFIRM_PASSWORD" ]; then
    echo -e "${RED}❌ Las contraseñas no coinciden${NC}"
    exit 1
fi

if [ -z "$NEW_PASSWORD" ]; then
    echo -e "${RED}❌ La contraseña no puede estar vacía${NC}"
    exit 1
fi

echo ""
echo -e "${BLUE}🔄 Reseteando contraseña...${NC}"

# Detener Jenkins
echo "1. Deteniendo Jenkins..."
sudo systemctl stop jenkins

# Crear archivo de configuración temporal
echo "2. Creando configuración temporal..."
sudo tee /tmp/jenkins-security.groovy > /dev/null << EOF
import jenkins.model.*
import hudson.security.*

def instance = Jenkins.getInstance()
def realm = new HudsonPrivateSecurityRealm(false)
realm.createAccount("jose", "$NEW_PASSWORD")
instance.setSecurityRealm(realm)

def strategy = new GlobalMatrixAuthorizationStrategy()
strategy.add(Jenkins.ADMINISTER, "jose")
instance.setAuthorizationStrategy(strategy)

instance.save()
println "Contraseña reseteada exitosamente para el usuario 'jose'"
EOF

# Ejecutar script de configuración
echo "3. Aplicando nueva configuración..."
sudo java -jar /usr/share/jenkins/jenkins.war --executors=0 --httpPort=8081 --argumentsRealm.passwd.jose="$NEW_PASSWORD" --argumentsRealm.roles.jose=admin &

# Esperar a que Jenkins inicie
echo "4. Esperando que Jenkins inicie..."
sleep 10

# Detener el proceso temporal
sudo pkill -f "jenkins.war"

# Reiniciar Jenkins normalmente
echo "5. Reiniciando Jenkins..."
sudo systemctl start jenkins

# Esperar a que esté listo
echo "6. Esperando que Jenkins esté listo..."
sleep 15

# Verificar que Jenkins esté funcionando
if curl -s http://localhost:8081 > /dev/null; then
    echo -e "${GREEN}✅ Contraseña reseteada exitosamente${NC}"
    echo ""
    echo -e "${YELLOW}📋 NUEVAS CREDENCIALES:${NC}"
    echo "   Usuario: jose"
    echo "   Contraseña: $NEW_PASSWORD"
    echo ""
    echo -e "${YELLOW}🌐 ACCESO:${NC}"
    echo "   URL: http://localhost:8081"
    echo ""
    echo -e "${GREEN}✅ Jenkins está funcionando correctamente${NC}"
else
    echo -e "${RED}❌ Error al reiniciar Jenkins${NC}"
    echo "   Verifica los logs: sudo journalctl -u jenkins -f"
fi

# Limpiar archivo temporal
sudo rm -f /tmp/jenkins-security.groovy

echo ""
echo -e "${GREEN}✅ Proceso completado${NC}"


