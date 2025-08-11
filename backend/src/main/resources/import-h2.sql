-- Script de inicialización de la base de datos H2 para DESARROLLO
-- Datos de prueba para la aplicación Hospital

-- Insertar roles básicos
INSERT INTO rol (id, nombre, descripcion) VALUES (1, 'ADMIN', 'Administrador del sistema');
INSERT INTO rol (id, nombre, descripcion) VALUES (2, 'DOCTOR', 'Médico del hospital');
INSERT INTO rol (id, nombre, descripcion) VALUES (3, 'PACIENTE', 'Paciente del hospital');
INSERT INTO rol (id, nombre, descripcion) VALUES (4, 'EMPLEADO', 'Empleado del hospital');
INSERT INTO rol (id, nombre, descripcion) VALUES (5, 'USUARIO_INTER', 'Usuario de interconexión');

-- Insertar usuarios de prueba
INSERT INTO usuario (id, nombre, apellido, correo, contrasena, activo, rol_id) VALUES 
(1, 'Admin', 'Sistema', 'admin@hospital.com', 'admin123', true, 1);

INSERT INTO usuario (id, nombre, apellido, correo, contrasena, activo, rol_id) VALUES 
(2, 'Doctor', 'Test', 'doctor@hospital.com', 'doctor123', true, 2);

INSERT INTO usuario (id, nombre, apellido, correo, contrasena, activo, rol_id) VALUES 
(3, 'Paciente', 'Test', 'paciente@hospital.com', 'paciente123', true, 3);

INSERT INTO usuario (id, nombre, apellido, correo, contrasena, activo, rol_id) VALUES 
(4, 'Empleado', 'Test', 'empleado@hospital.com', 'empleado123', true, 4);

-- Insertar hospital de prueba
INSERT INTO hospital (id, nombre, direccion, telefono, email) VALUES 
(1, 'Hospital Test', 'Dirección Test 123', '123-456-7890', 'info@hospitaltest.com');

-- Insertar servicios básicos
INSERT INTO servicio (id, nombre, descripcion, precio, activo) VALUES 
(1, 'Consulta General', 'Consulta médica general', 50.00, true);

INSERT INTO servicio (id, nombre, descripcion, precio, activo) VALUES 
(2, 'Consulta Especializada', 'Consulta médica especializada', 80.00, true);

-- Insertar aseguradora de prueba
INSERT INTO aseguradora (id, nombre, codigo, activo) VALUES 
(1, 'Aseguradora Test', 'TEST001', true);

