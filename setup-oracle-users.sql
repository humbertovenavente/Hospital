-- Script para configurar usuarios en Oracle XE para diferentes entornos
-- Este script se ejecuta automáticamente al iniciar el contenedor Oracle
-- Conectar como SYSDBA

-- Crear usuario C##PROYECTO (para Desarrollo)
CREATE USER "C##PROYECTO" IDENTIFIED BY Unis;

-- Crear usuario Hospital2 (para QA)
CREATE USER "Hospital2" IDENTIFIED BY Unis;

-- Crear usuario Hospital3 (para Producción)
CREATE USER "Hospital3" IDENTIFIED BY Unis;

-- Dar permisos completos a C##PROYECTO
GRANT CONNECT, RESOURCE, DBA, CREATE SESSION, CREATE TABLE, CREATE VIEW, CREATE PROCEDURE, CREATE SEQUENCE, CREATE TRIGGER, UNLIMITED TABLESPACE TO "C##PROYECTO";

-- Dar permisos completos a Hospital2
GRANT CONNECT, RESOURCE, DBA, CREATE SESSION, CREATE TABLE, CREATE VIEW, CREATE PROCEDURE, CREATE SEQUENCE, CREATE TRIGGER, UNLIMITED TABLESPACE TO "Hospital2";

-- Dar permisos completos a Hospital3
GRANT CONNECT, RESOURCE, DBA, CREATE SESSION, CREATE TABLE, CREATE VIEW, CREATE PROCEDURE, CREATE SEQUENCE, CREATE TRIGGER, UNLIMITED TABLESPACE TO "Hospital3";

-- Crear tablaspace para cada usuario (opcional, para mejor organización)
CREATE TABLESPACE C##PROYECTO_DATA DATAFILE 'C##PROYECTO_DATA.dbf' SIZE 100M AUTOEXTEND ON;
CREATE TABLESPACE Hospital2_DATA DATAFILE 'Hospital2_DATA.dbf' SIZE 100M AUTOEXTEND ON;
CREATE TABLESPACE Hospital3_DATA DATAFILE 'Hospital3_DATA.dbf' SIZE 100M AUTOEXTEND ON;

-- Asignar tablaspace por defecto a cada usuario
ALTER USER "C##PROYECTO" DEFAULT TABLESPACE C##PROYECTO_DATA;
ALTER USER "Hospital2" DEFAULT TABLESPACE Hospital2_DATA;
ALTER USER "Hospital3" DEFAULT TABLESPACE Hospital3_DATA;

-- Verificar usuarios creados
SELECT username, account_status, default_tablespace FROM dba_users WHERE username IN ('C##PROYECTO', 'Hospital2', 'Hospital3');

-- Salir
EXIT;
