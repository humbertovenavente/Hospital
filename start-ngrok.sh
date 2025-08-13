#!/bin/bash

# Script para iniciar ngrok para Jenkins
# Este script se ejecutará como servicio de systemd

# Cambiar al directorio del usuario
cd /home/jose

# Usar la configuración de ngrok del usuario jose
export HOME=/home/jose
export XDG_CONFIG_HOME=/home/jose/.config

# Iniciar ngrok en el puerto 8082 (Jenkins)
/usr/local/bin/ngrok http 8082

# Mantener el script ejecutándose
sleep infinity
