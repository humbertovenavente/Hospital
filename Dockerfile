FROM oraclelinux:8-slim

# Variables de entorno para Oracle
ENV ORACLE_BASE=/opt/oracle
ENV ORACLE_HOME=/opt/oracle/product/19c/dbhomeXE
ENV ORACLE_SID=XE
ENV PATH=$ORACLE_HOME/bin:$PATH
ENV LD_LIBRARY_PATH=$ORACLE_HOME/lib:$LD_LIBRARY_PATH

# Instalar dependencias
RUN dnf update -y && \
    dnf install -y oracle-database-preinstall-19c \
                   oracle-instantclient-19c-basic \
                   oracle-instantclient-19c-sqlplus \
                   wget \
                   unzip \
                   tar \
                   gzip \
                   && dnf clean all

# Crear directorios necesarios
RUN mkdir -p $ORACLE_BASE && \
    mkdir -p $ORACLE_HOME && \
    mkdir -p /opt/oracle/scripts/startup && \
    mkdir -p /opt/oracle/scripts/setup

# Copiar scripts de configuración
COPY scripts/setup/* /opt/oracle/scripts/setup/
COPY scripts/startup/* /opt/oracle/scripts/startup/

# Dar permisos de ejecución
RUN chmod +x /opt/oracle/scripts/startup/* && \
    chmod +x /opt/oracle/scripts/setup/*

# Puerto por defecto
EXPOSE 1521

# Comando de inicio
CMD ["/opt/oracle/scripts/startup/runOracle.sh"] 