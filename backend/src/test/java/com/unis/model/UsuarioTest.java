package com.unis.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

public class UsuarioTest {

    private Usuario usuario;
    private Rol rol;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        rol = new Rol();
        rol.setRoleName("ADMIN");
    }

    @Test
    void testConstructorPorDefecto() {
        assertNotNull(usuario);
    }

    @Test
    void testGettersAndSetters() {
        // Arrange
        Long id = 1L;
        String nombreUsuario = "admin";
        String correo = "admin@hospital.com";
        String contrasena = "password123";
        int estado = 1;
        Date fechaCreaction = new Date();

        // Act
        usuario.setId(id);
        usuario.setNombreUsuario(nombreUsuario);
        usuario.setCorreo(correo);
        usuario.setContrasena(contrasena);
        usuario.setRol(rol);
        usuario.setEstado(estado);
        usuario.setFechaCreaction(fechaCreaction);

        // Assert
        assertEquals(id, usuario.getId());
        assertEquals(nombreUsuario, usuario.getNombreUsuario());
        assertEquals(correo, usuario.getCorreo());
        assertEquals(contrasena, usuario.getContrasena());
        assertEquals(rol, usuario.getRol());
        assertEquals(estado, usuario.getEstado());
        assertEquals(fechaCreaction, usuario.getFechaCreaction());
    }

    @Test
    void testSetYGetId() {
        // Arrange
        Long id = 999L;

        // Act
        usuario.setId(id);

        // Assert
        assertEquals(id, usuario.getId());
    }

    @Test
    void testSetYGetNombreUsuario() {
        // Arrange
        String nombreUsuario = "doctor";

        // Act
        usuario.setNombreUsuario(nombreUsuario);

        // Assert
        assertEquals(nombreUsuario, usuario.getNombreUsuario());
    }

    @Test
    void testSetYGetCorreo() {
        // Arrange
        String correo = "doctor@hospital.com";

        // Act
        usuario.setCorreo(correo);

        // Assert
        assertEquals(correo, usuario.getCorreo());
    }

    @Test
    void testSetYGetContrasena() {
        // Arrange
        String contrasena = "newpassword";

        // Act
        usuario.setContrasena(contrasena);

        // Assert
        assertEquals(contrasena, usuario.getContrasena());
    }

    @Test
    void testSetYGetRol() {
        // Arrange
        Rol nuevoRol = new Rol();
        nuevoRol.setRoleName("DOCTOR");

        // Act
        usuario.setRol(nuevoRol);

        // Assert
        assertEquals(nuevoRol, usuario.getRol());
    }

    @Test
    void testSetYGetEstado() {
        // Arrange
        int estado = 0;

        // Act
        usuario.setEstado(estado);

        // Assert
        assertEquals(estado, usuario.getEstado());
    }

    @Test
    void testSetYGetFechaCreaction() {
        // Arrange
        Date fechaCreaction = new Date();

        // Act
        usuario.setFechaCreaction(fechaCreaction);

        // Assert
        assertEquals(fechaCreaction, usuario.getFechaCreaction());
    }

    @Test
    void testSetIdNull() {
        // Act
        usuario.setId(null);

        // Assert
        assertNull(usuario.getId());
    }

    @Test
    void testSetNombreUsuarioNull() {
        // Act
        usuario.setNombreUsuario(null);

        // Assert
        assertNull(usuario.getNombreUsuario());
    }

    @Test
    void testSetCorreoNull() {
        // Act
        usuario.setCorreo(null);

        // Assert
        assertNull(usuario.getCorreo());
    }

    @Test
    void testSetContrasenaNull() {
        // Act
        usuario.setContrasena(null);

        // Assert
        assertNull(usuario.getContrasena());
    }

    @Test
    void testSetRolNull() {
        // Act
        usuario.setRol(null);

        // Assert
        assertNull(usuario.getRol());
    }

    @Test
    void testSetFechaCreactionNull() {
        // Act
        usuario.setFechaCreaction(null);

        // Assert
        assertNull(usuario.getFechaCreaction());
    }

    @Test
    void testSetNombreUsuarioVacio() {
        // Arrange
        String nombreUsuarioVacio = "";

        // Act
        usuario.setNombreUsuario(nombreUsuarioVacio);

        // Assert
        assertEquals(nombreUsuarioVacio, usuario.getNombreUsuario());
        assertTrue(usuario.getNombreUsuario().isEmpty());
    }

    @Test
    void testSetCorreoVacio() {
        // Arrange
        String correoVacio = "";

        // Act
        usuario.setCorreo(correoVacio);

        // Assert
        assertEquals(correoVacio, usuario.getCorreo());
        assertTrue(usuario.getCorreo().isEmpty());
    }

    @Test
    void testSetContrasenaVacio() {
        // Arrange
        String contrasenaVacio = "";

        // Act
        usuario.setContrasena(contrasenaVacio);

        // Assert
        assertEquals(contrasenaVacio, usuario.getContrasena());
        assertTrue(usuario.getContrasena().isEmpty());
    }
}
