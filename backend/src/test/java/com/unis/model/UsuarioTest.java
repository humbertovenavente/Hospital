package com.unis.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

/**
 * Test class for Usuario entity.
 * Tests all getters, setters, and business logic functionality.
 */
class UsuarioTest {

    private Usuario usuario;
    private Date fechaCreacion;
    private Rol rol;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        fechaCreacion = new Date(120, 0, 15); // 15 de enero de 2020
        rol = new Rol();
    }

    @Test
    void testDefaultConstructor() {
        assertNotNull(usuario);
        assertNull(usuario.getId());
        assertNull(usuario.getNombreUsuario());
        assertNull(usuario.getCorreo());
        assertNull(usuario.getContrasena());
        assertNull(usuario.getRol());
        assertEquals(0, usuario.getEstado());
        assertNotNull(usuario.getFechaCreaction());
    }

    @Test
    void testSetAndGetId() {
        Long id = 123L;
        usuario.setId(id);
        assertEquals(id, usuario.getId());
    }

    @Test
    void testSetAndGetIdWithNull() {
        usuario.setId(null);
        assertNull(usuario.getId());
    }

    @Test
    void testSetAndGetIdWithZero() {
        usuario.setId(0L);
        assertEquals(0L, usuario.getId());
    }

    @Test
    void testSetAndGetIdWithNegative() {
        usuario.setId(-1L);
        assertEquals(-1L, usuario.getId());
    }

    @Test
    void testSetAndGetNombreUsuario() {
        String nombreUsuario = "juan_perez";
        usuario.setNombreUsuario(nombreUsuario);
        assertEquals(nombreUsuario, usuario.getNombreUsuario());
    }

    @Test
    void testSetAndGetNombreUsuarioWithNull() {
        usuario.setNombreUsuario(null);
        assertNull(usuario.getNombreUsuario());
    }

    @Test
    void testSetAndGetNombreUsuarioWithEmptyString() {
        usuario.setNombreUsuario("");
        assertEquals("", usuario.getNombreUsuario());
    }

    @Test
    void testSetAndGetCorreo() {
        String correo = "juan.perez@email.com";
        usuario.setCorreo(correo);
        assertEquals(correo, usuario.getCorreo());
    }

    @Test
    void testSetAndGetCorreoWithNull() {
        usuario.setCorreo(null);
        assertNull(usuario.getCorreo());
    }

    @Test
    void testSetAndGetCorreoWithEmptyString() {
        usuario.setCorreo("");
        assertEquals("", usuario.getCorreo());
    }

    @Test
    void testSetAndGetContrasena() {
        String contrasena = "password123";
        usuario.setContrasena(contrasena);
        assertEquals(contrasena, usuario.getContrasena());
    }

    @Test
    void testSetAndGetContrasenaWithNull() {
        usuario.setContrasena(null);
        assertNull(usuario.getContrasena());
    }

    @Test
    void testSetAndGetContrasenaWithEmptyString() {
        usuario.setContrasena("");
        assertEquals("", usuario.getContrasena());
    }

    @Test
    void testSetAndGetRol() {
        usuario.setRol(rol);
        assertEquals(rol, usuario.getRol());
    }

    @Test
    void testSetAndGetRolWithNull() {
        usuario.setRol(null);
        assertNull(usuario.getRol());
    }

    @Test
    void testSetAndGetEstado() {
        int estado = 1;
        usuario.setEstado(estado);
        assertEquals(estado, usuario.getEstado());
    }

    @Test
    void testSetAndGetEstadoWithZero() {
        usuario.setEstado(0);
        assertEquals(0, usuario.getEstado());
    }

    @Test
    void testSetAndGetEstadoWithNegative() {
        usuario.setEstado(-1);
        assertEquals(-1, usuario.getEstado());
    }

    @Test
    void testSetAndGetEstadoWithLargeNumber() {
        usuario.setEstado(999);
        assertEquals(999, usuario.getEstado());
    }

    @Test
    void testSetAndGetFechaCreaction() {
        usuario.setFechaCreaction(fechaCreacion);
        assertEquals(fechaCreacion, usuario.getFechaCreaction());
    }

    @Test
    void testSetAndGetFechaCreactionWithNull() {
        usuario.setFechaCreaction(null);
        assertNull(usuario.getFechaCreaction());
    }

    @Test
    void testMultipleSetters() {
        // Configurar todos los campos
        usuario.setId(999L);
        usuario.setNombreUsuario("admin_user");
        usuario.setCorreo("admin@hospital.com");
        usuario.setContrasena("admin123");
        usuario.setRol(rol);
        usuario.setEstado(1);
        usuario.setFechaCreaction(fechaCreacion);

        // Verificar todos los campos
        assertEquals(999L, usuario.getId());
        assertEquals("admin_user", usuario.getNombreUsuario());
        assertEquals("admin@hospital.com", usuario.getCorreo());
        assertEquals("admin123", usuario.getContrasena());
        assertEquals(rol, usuario.getRol());
        assertEquals(1, usuario.getEstado());
        assertEquals(fechaCreacion, usuario.getFechaCreaction());
    }

    @Test
    void testDataIntegrity() {
        // Verificar que los datos no cambien después de múltiples lecturas
        usuario.setId(123L);
        Long id1 = usuario.getId();
        Long id2 = usuario.getId();
        assertEquals(id1, id2);

        usuario.setNombreUsuario("Test");
        String nombre1 = usuario.getNombreUsuario();
        String nombre2 = usuario.getNombreUsuario();
        assertEquals(nombre1, nombre2);
    }

    @Test
    void testEdgeCaseValues() {
        // Test con valores extremos
        usuario.setId(Long.MAX_VALUE);
        usuario.setNombreUsuario("A");
        usuario.setCorreo("");
        usuario.setContrasena(" ");
        usuario.setEstado(Integer.MAX_VALUE);

        assertEquals(Long.MAX_VALUE, usuario.getId());
        assertEquals("A", usuario.getNombreUsuario());
        assertEquals("", usuario.getCorreo());
        assertEquals(" ", usuario.getContrasena());
        assertEquals(Integer.MAX_VALUE, usuario.getEstado());
    }

    @Test
    void testNullHandling() {
        // Test de manejo de valores nulos
        usuario.setId(null);
        usuario.setNombreUsuario(null);
        usuario.setCorreo(null);
        usuario.setContrasena(null);
        usuario.setRol(null);
        usuario.setFechaCreaction(null);

        assertNull(usuario.getId());
        assertNull(usuario.getNombreUsuario());
        assertNull(usuario.getCorreo());
        assertNull(usuario.getContrasena());
        assertNull(usuario.getRol());
        assertNull(usuario.getFechaCreaction());
    }

    @Test
    void testRepeatedUpdates() {
        // Actualizar el mismo valor múltiples veces
        for (int i = 0; i < 5; i++) {
            usuario.setNombreUsuario("Usuario" + i);
        }

        assertEquals("Usuario4", usuario.getNombreUsuario());
    }

    @Test
    void testFieldIndependence() {
        // Verificar que los campos son independientes
        usuario.setNombreUsuario("Usuario1");
        usuario.setCorreo("email1@test.com");

        // Cambiar solo un campo
        usuario.setNombreUsuario("Usuario2");

        assertEquals("Usuario2", usuario.getNombreUsuario());
        assertEquals("email1@test.com", usuario.getCorreo());
    }

    @Test
    void testSpecialCharactersInStrings() {
        String specialNombre = "user-name_123";
        String specialCorreo = "test+tag@domain.co.uk";
        String specialContrasena = "P@ssw0rd!";
        
        usuario.setNombreUsuario(specialNombre);
        usuario.setCorreo(specialCorreo);
        usuario.setContrasena(specialContrasena);
        
        assertEquals(specialNombre, usuario.getNombreUsuario());
        assertEquals(specialCorreo, usuario.getCorreo());
        assertEquals(specialContrasena, usuario.getContrasena());
    }

    @Test
    void testEstadoValidation() {
        // Test con diferentes valores de estado
        int[] estados = {0, 1, 2, 100, -1, Integer.MAX_VALUE, Integer.MIN_VALUE};
        
        for (int estado : estados) {
            usuario.setEstado(estado);
            assertEquals(estado, usuario.getEstado());
        }
    }

    @Test
    void testLongStringValues() {
        // Test con strings largos
        String longNombre = "a".repeat(50);
        String longCorreo = "b".repeat(100);
        String longContrasena = "c".repeat(100);
        
        usuario.setNombreUsuario(longNombre);
        usuario.setCorreo(longCorreo);
        usuario.setContrasena(longContrasena);
        
        assertEquals(longNombre, usuario.getNombreUsuario());
        assertEquals(longCorreo, usuario.getCorreo());
        assertEquals(longContrasena, usuario.getContrasena());
    }

    @Test
    void testDateOperations() {
        // Test con diferentes fechas
        Date[] fechas = {
            new Date(0), // 1 de enero de 1970
            new Date(), // fecha actual
            new Date(Long.MAX_VALUE), // fecha máxima
            new Date(Long.MIN_VALUE) // fecha mínima
        };
        
        for (Date fecha : fechas) {
            usuario.setFechaCreaction(fecha);
            assertEquals(fecha, usuario.getFechaCreaction());
        }
    }
}
