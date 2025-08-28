package com.unis.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

/**
 * Test class for UserAcc entity
 */
public class UserAccTest {

    private UserAcc userAcc;
    private Date testDate;

    @BeforeEach
    void setUp() {
        userAcc = new UserAcc();
        testDate = new Date();
    }

    @Test
    void testUserAccInstantiation() {
        assertNotNull(userAcc);
        assertTrue(userAcc instanceof UserAcc);
    }

    @Test
    void testUserAccIdUsuario() {
        // Test setIdUsuario
        userAcc.setIdUsuario(100L);
        assertEquals(100L, userAcc.getIdUsuario());
        
        // Test setIdUsuario with null
        userAcc.setIdUsuario(null);
        assertNull(userAcc.getIdUsuario());
        
        // Test setIdUsuario with zero
        userAcc.setIdUsuario(0L);
        assertEquals(0L, userAcc.getIdUsuario());
        
        // Test setIdUsuario with negative value
        userAcc.setIdUsuario(-1L);
        assertEquals(-1L, userAcc.getIdUsuario());
        
        // Test setIdUsuario with large value
        userAcc.setIdUsuario(Long.MAX_VALUE);
        assertEquals(Long.MAX_VALUE, userAcc.getIdUsuario());
    }

    @Test
    void testUserAccNombreUsuario() {
        // Test setNombreUsuario
        userAcc.setNombreUsuario("juan_perez");
        assertEquals("juan_perez", userAcc.getNombreUsuario());
        
        // Test setNombreUsuario with null
        userAcc.setNombreUsuario(null);
        assertNull(userAcc.getNombreUsuario());
        
        // Test setNombreUsuario with empty string
        userAcc.setNombreUsuario("");
        assertEquals("", userAcc.getNombreUsuario());
        
        // Test setNombreUsuario with different usernames
        userAcc.setNombreUsuario("maria_garcia");
        assertEquals("maria_garcia", userAcc.getNombreUsuario());
        
        userAcc.setNombreUsuario("admin123");
        assertEquals("admin123", userAcc.getNombreUsuario());
        
        // Test setNombreUsuario with special characters
        userAcc.setNombreUsuario("user-name_123");
        assertEquals("user-name_123", userAcc.getNombreUsuario());
    }

    @Test
    void testUserAccContrasena() {
        // Test setContrasena
        userAcc.setContrasena("password123");
        assertEquals("password123", userAcc.getContrasena());
        
        // Test setContrasena with null
        userAcc.setContrasena(null);
        assertNull(userAcc.getContrasena());
        
        // Test setContrasena with empty string
        userAcc.setContrasena("");
        assertEquals("", userAcc.getContrasena());
        
        // Test setContrasena with different passwords
        userAcc.setContrasena("securePass456");
        assertEquals("securePass456", userAcc.getContrasena());
        
        userAcc.setContrasena("MyP@ssw0rd!");
        assertEquals("MyP@ssw0rd!", userAcc.getContrasena());
        
        // Test setContrasena with special characters
        userAcc.setContrasena("p@ssw0rd#123$");
        assertEquals("p@ssw0rd#123$", userAcc.getContrasena());
    }

    @Test
    void testUserAccRolId() {
        // Test setRolId
        userAcc.setRolId(1);
        assertEquals(1, userAcc.getRolId());
        
        // Test setRolId with zero
        userAcc.setRolId(0);
        assertEquals(0, userAcc.getRolId());
        
        // Test setRolId with negative value
        userAcc.setRolId(-1);
        assertEquals(-1, userAcc.getRolId());
        
        // Test setRolId with different values
        userAcc.setRolId(2);
        assertEquals(2, userAcc.getRolId());
        
        userAcc.setRolId(999);
        assertEquals(999, userAcc.getRolId());
        
        // Test setRolId with large value
        userAcc.setRolId(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, userAcc.getRolId());
    }

    @Test
    void testUserAccCorreo() {
        // Test setCorreo
        userAcc.setCorreo("juan@example.com");
        assertEquals("juan@example.com", userAcc.getCorreo());
        
        // Test setCorreo with null
        userAcc.setCorreo(null);
        assertNull(userAcc.getCorreo());
        
        // Test setCorreo with empty string
        userAcc.setCorreo("");
        assertEquals("", userAcc.getCorreo());
        
        // Test setCorreo with different emails
        userAcc.setCorreo("maria@hospital.com");
        assertEquals("maria@hospital.com", userAcc.getCorreo());
        
        userAcc.setCorreo("admin@unis.edu.gt");
        assertEquals("admin@unis.edu.gt", userAcc.getCorreo());
        
        // Test setCorreo with special characters
        userAcc.setCorreo("user+tag@domain.co.uk");
        assertEquals("user+tag@domain.co.uk", userAcc.getCorreo());
    }

    @Test
    void testUserAccEstado() {
        // Test setEstado
        userAcc.setEstado(1);
        assertEquals(1, userAcc.getEstado());
        
        // Test setEstado with zero
        userAcc.setEstado(0);
        assertEquals(0, userAcc.getEstado());
        
        // Test setEstado with negative value
        userAcc.setEstado(-1);
        assertEquals(-1, userAcc.getEstado());
        
        // Test setEstado with different values
        userAcc.setEstado(2);
        assertEquals(2, userAcc.getEstado());
        
        userAcc.setEstado(999);
        assertEquals(999, userAcc.getEstado());
        
        // Test setEstado with large value
        userAcc.setEstado(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, userAcc.getEstado());
    }

    @Test
    void testUserAccFechaCreacion() {
        // Test setFechaCreacion
        userAcc.setFechaCreacion(testDate);
        assertEquals(testDate, userAcc.getFechaCreacion());
        
        // Test setFechaCreacion with null
        userAcc.setFechaCreacion(null);
        assertNull(userAcc.getFechaCreacion());
        
        // Test setFechaCreacion with different dates
        Date pastDate = new Date(0L); // January 1, 1970
        userAcc.setFechaCreacion(pastDate);
        assertEquals(pastDate, userAcc.getFechaCreacion());
        
        Date futureDate = new Date(System.currentTimeMillis() + 86400000L); // Tomorrow
        userAcc.setFechaCreacion(futureDate);
        assertEquals(futureDate, userAcc.getFechaCreacion());
        
        // Test with current date
        Date currentDate = new Date();
        userAcc.setFechaCreacion(currentDate);
        assertEquals(currentDate, userAcc.getFechaCreacion());
    }

    @Test
    void testUserAccIdHospital() {
        // Test setIdHospital
        userAcc.setIdHospital(500L);
        assertEquals(500L, userAcc.getIdHospital());
        
        // Test setIdHospital with null
        userAcc.setIdHospital(null);
        assertNull(userAcc.getIdHospital());
        
        // Test setIdHospital with zero
        userAcc.setIdHospital(0L);
        assertEquals(0L, userAcc.getIdHospital());
        
        // Test setIdHospital with negative value
        userAcc.setIdHospital(-1L);
        assertEquals(-1L, userAcc.getIdHospital());
        
        // Test setIdHospital with large value
        userAcc.setIdHospital(Long.MAX_VALUE);
        assertEquals(Long.MAX_VALUE, userAcc.getIdHospital());
    }

    @Test
    void testUserAccCompleteObject() {
        // Test setting all fields
        userAcc.setIdUsuario(1000L);
        userAcc.setNombreUsuario("testuser");
        userAcc.setContrasena("testpass123");
        userAcc.setRolId(3);
        userAcc.setCorreo("test@example.com");
        userAcc.setEstado(1);
        userAcc.setFechaCreacion(testDate);
        userAcc.setIdHospital(200L);
        
        // Verify all fields
        assertEquals(1000L, userAcc.getIdUsuario());
        assertEquals("testuser", userAcc.getNombreUsuario());
        assertEquals("testpass123", userAcc.getContrasena());
        assertEquals(3, userAcc.getRolId());
        assertEquals("test@example.com", userAcc.getCorreo());
        assertEquals(1, userAcc.getEstado());
        assertEquals(testDate, userAcc.getFechaCreacion());
        assertEquals(200L, userAcc.getIdHospital());
    }

    @Test
    void testUserAccFieldModification() {
        // Set initial values
        userAcc.setIdUsuario(1L);
        userAcc.setNombreUsuario("Original");
        userAcc.setCorreo("original@example.com");
        
        // Modify values
        userAcc.setIdUsuario(2L);
        userAcc.setNombreUsuario("Modificado");
        userAcc.setCorreo("modificado@example.com");
        
        // Verify modifications
        assertEquals(2L, userAcc.getIdUsuario());
        assertEquals("Modificado", userAcc.getNombreUsuario());
        assertEquals("modificado@example.com", userAcc.getCorreo());
    }

    @Test
    void testUserAccEdgeCases() {
        // Test with very long strings
        String veryLongString = "A".repeat(1000);
        userAcc.setNombreUsuario(veryLongString);
        userAcc.setContrasena(veryLongString);
        userAcc.setCorreo(veryLongString);
        
        assertEquals(veryLongString, userAcc.getNombreUsuario());
        assertEquals(veryLongString, userAcc.getContrasena());
        assertEquals(veryLongString, userAcc.getCorreo());
        
        // Test with special characters
        String specialChars = "!@#$%^&*()_+-=[]{}|;':\",./<>?";
        userAcc.setNombreUsuario(specialChars);
        userAcc.setContrasena(specialChars);
        userAcc.setCorreo(specialChars);
        
        assertEquals(specialChars, userAcc.getNombreUsuario());
        assertEquals(specialChars, userAcc.getContrasena());
        assertEquals(specialChars, userAcc.getCorreo());
        
        // Test with numbers in string fields
        String numbersOnly = "123456789";
        userAcc.setNombreUsuario(numbersOnly);
        userAcc.setContrasena(numbersOnly);
        userAcc.setCorreo(numbersOnly);
        
        assertEquals(numbersOnly, userAcc.getNombreUsuario());
        assertEquals(numbersOnly, userAcc.getContrasena());
        assertEquals(numbersOnly, userAcc.getCorreo());
    }

    @Test
    void testUserAccNullHandling() {
        // Test that all fields can handle null values properly
        userAcc.setIdUsuario(null);
        userAcc.setNombreUsuario(null);
        userAcc.setContrasena(null);
        userAcc.setCorreo(null);
        userAcc.setFechaCreacion(null);
        userAcc.setIdHospital(null);
        
        // Verify all fields are null
        assertNull(userAcc.getIdUsuario());
        assertNull(userAcc.getNombreUsuario());
        assertNull(userAcc.getContrasena());
        assertNull(userAcc.getCorreo());
        assertNull(userAcc.getFechaCreacion());
        assertNull(userAcc.getIdHospital());
    }
}

