-- Script de inicialización de la base de datos Hospital
-- Crear usuarios necesarios para la aplicación

-- Primero crear usuarios en el PDB XEPDB1
ALTER SESSION SET CONTAINER = XEPDB1;

-- Crear usuario C##PROYECTO en el PDB
CREATE USER C##PROYECTO IDENTIFIED BY Unis;

-- Otorgar privilegios necesarios al usuario C##PROYECTO
GRANT CONNECT, RESOURCE, DBA TO C##PROYECTO;
GRANT CREATE SESSION TO C##PROYECTO;
GRANT UNLIMITED TABLESPACE TO C##PROYECTO;
GRANT CREATE TABLE TO C##PROYECTO;
GRANT CREATE VIEW TO C##PROYECTO;
GRANT CREATE SEQUENCE TO C##PROYECTO;
GRANT CREATE PROCEDURE TO C##PROYECTO;
GRANT CREATE TRIGGER TO C##PROYECTO;

-- Crear usuario Hospital3 en el PDB
CREATE USER Hospital3 IDENTIFIED BY Unis;

-- Otorgar privilegios al usuario Hospital3
GRANT CONNECT, RESOURCE TO Hospital3;
GRANT CREATE SESSION TO Hospital3;
GRANT UNLIMITED TABLESPACE TO Hospital3;

-- Volver al contenedor raíz
ALTER SESSION SET CONTAINER = CDB$ROOT;

-- Confirmar cambios
COMMIT;
