#!/bin/bash

echo "🚀 Configurando inicio automático de ngrok para Jenkins..."
echo "=========================================================="

# Crear directorio de autostart si no existe
AUTOSTART_DIR="$HOME/.config/autostart"
mkdir -p "$AUTOSTART_DIR"

# Crear archivo .desktop para autostart
cat > "$AUTOSTART_DIR/ngrok-jenkins.desktop" << EOF
[Desktop Entry]
Type=Application
Name=ngrok Jenkins Tunnel
Comment=Inicia túnel ngrok para Jenkins automáticamente
Exec=$HOME/Hospital-2/start-ngrok.sh
Terminal=false
X-GNOME-Autostart-enabled=true
Hidden=false
EOF

# Hacer ejecutable el script
chmod +x "$HOME/Hospital-2/start-ngrok.sh"

# Agregar alias al .bashrc
BASH_RC="$HOME/.bashrc"
if ! grep -q "ngrok-jenkins" "$BASH_RC"; then
    echo "" >> "$BASH_RC"
    echo "# ngrok Jenkins Tunnel" >> "$BASH_RC"
    echo "alias ngrok-jenkins='$HOME/Hospital-2/start-ngrok.sh'" >> "$BASH_RC"
    echo "alias ngrok-status='curl -s http://localhost:4040/api/tunnels | jq .'" >> "$BASH_RC"
    echo "alias ngrok-stop='pkill -f \"ngrok http 8081\"'" >> "$BASH_RC"
fi

echo ""
echo "✅ Configuración completada!"
echo ""
echo "🔧 Comandos disponibles:"
echo "   • ngrok-jenkins     - Iniciar túnel manualmente"
echo "   • ngrok-status      - Ver estado del túnel"
echo "   • ngrok-stop        - Detener túnel"
echo ""
echo "🚀 ngrok se iniciará automáticamente:"
echo "   • Al iniciar sesión (autostart)"
echo "   • Al abrir terminal (alias)"
echo ""
echo "📁 Archivos creados:"
echo "   • $AUTOSTART_DIR/ngrok-jenkins.desktop"
echo "   • $HOME/Hospital-2/start-ngrok.sh"
echo "   • Aliases en $BASH_RC"
echo ""
echo "💡 Para probar ahora:"
echo "   source ~/.bashrc"
echo "   ngrok-jenkins"
