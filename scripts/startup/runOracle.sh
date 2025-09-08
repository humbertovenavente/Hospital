#!/bin/bash

# Script de inicio para Oracle Database
set -e

# Función para log
log() {
    echo "[$(date +'%Y-%m-%d %H:%M:%S')] $1"
}

# Función para esperar a que Oracle esté listo
wait_for_oracle() {
    log "Esperando a que Oracle esté listo..."
    while ! sqlplus -L "system/${ORACLE_PWD}@localhost:1521/${ORACLE_SID}" AS SYSDBA @/opt/oracle/scripts/startup/healthcheck.sql > /dev/null 2>&1; do
        sleep 5
    done
    log "Oracle está listo!"
}

# Función para crear la base de datos si no existe
create_database() {
    if [ ! -f "/opt/oracle/oradata/${ORACLE_SID}/.db_created" ]; then
        log "Creando base de datos ${ORACLE_SID}..."
        
        # Crear directorios necesarios
        mkdir -p /opt/oracle/oradata/${ORACLE_SID}
        mkdir -p /opt/oracle/backup/${ORACLE_SID}
        
        # Crear la base de datos usando DBCA
        dbca -silent -createDatabase \
            -templateName General_Purpose.dbc \
            -gdbName ${ORACLE_SID} \
            -sid ${ORACLE_SID} \
            -responseFile NO_VALUE \
            -characterSet ${ORACLE_CHARACTERSET} \
            -sysPassword ${ORACLE_PWD} \
            -systemPassword ${ORACLE_PWD} \
            -createAsContainerDatabase false \
            -databaseType MULTIPURPOSE \
            -memoryPercentage 40 \
            -storageType FS \
            -datafileDestination /opt/oracle/oradata/${ORACLE_SID} \
            -redoLogFileSize 100 \
            -emConfiguration NONE \
            -listeners LISTENER \
            -sampleSchema false \
            -enableArchive false \
            -recoveryAreaDestination /opt/oracle/backup/${ORACLE_SID} \
            -recoveryAreaSize 2048
        
        # Marcar como creada
        touch "/opt/oracle/oradata/${ORACLE_SID}/.db_created"
        log "Base de datos ${ORACLE_SID} creada exitosamente"
    else
        log "Base de datos ${ORACLE_SID} ya existe, iniciando..."
    fi
}

# Función para importar datos si existen
import_data() {
    if [ -f "/opt/oracle/import/import_${ORACLE_SID}.sql" ]; then
        log "Importando datos para ${ORACLE_SID}..."
        sqlplus "system/${ORACLE_PWD}@localhost:1521/${ORACLE_SID}" AS SYSDBA @/opt/oracle/import/import_${ORACLE_SID}.sql
        log "Datos importados exitosamente para ${ORACLE_SID}"
    fi
}

# Función principal
main() {
    log "Iniciando Oracle Database ${ORACLE_SID}..."
    
    # Configurar variables de entorno
    export ORACLE_SID=${ORACLE_SID}
    export ORACLE_HOME=${ORACLE_HOME}
    export PATH=${ORACLE_HOME}/bin:${PATH}
    export LD_LIBRARY_PATH=${ORACLE_HOME}/lib:${LD_LIBRARY_PATH}
    
    # Crear base de datos si no existe
    create_database
    
    # Iniciar listener
    log "Iniciando listener..."
    lsnrctl start
    
    # Iniciar base de datos
    log "Iniciando base de datos..."
    sqlplus "system/${ORACLE_PWD}@localhost:1521/${ORACLE_SID}" AS SYSDBA <<EOF
STARTUP
EXIT
EOF
    
    # Esperar a que Oracle esté listo
    wait_for_oracle
    
    # Importar datos si existen
    import_data
    
    log "Oracle Database ${ORACLE_SID} iniciado exitosamente"
    
    # Mantener el contenedor corriendo
    tail -f /dev/null
}

# Ejecutar función principal
main "$@"



