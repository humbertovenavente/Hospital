package com.unis.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;

/**
 * Test class for UsuarioInter entity
 */
public class UsuarioInterTest {

    private UsuarioInter usuarioInter;
    private Date testDate;
    private Usuario testUsuario;

    @BeforeEach
    void setUp() throws ParseException {
        usuarioInter = new UsuarioInter();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        testDate = sdf.parse("1990-05-15");
        
        testUsuario = new Usuario();
        testUsuario.setNombreUsuario("Juan Pérez");
    }

    @Test
    void testUsuarioInterInstantiation() {
        assertNotNull(usuarioInter);
        assertTrue(usuarioInter instanceof UsuarioInter);
    }

    @Test
    void testUsuarioInterIdInterconexion() {
        // Test setIdInterconexion
        usuarioInter.setIdInterconexion(100L);
        assertEquals(100L, usuarioInter.getIdInterconexion());
        
        // Test setIdInterconexion with null
        usuarioInter.setIdInterconexion(null);
        assertNull(usuarioInter.getIdInterconexion());
        
        // Test setIdInterconexion with zero
        usuarioInter.setIdInterconexion(0L);
        assertEquals(0L, usuarioInter.getIdInterconexion());
        
        // Test setIdInterconexion with negative value
        usuarioInter.setIdInterconexion(-1L);
        assertEquals(-1L, usuarioInter.getIdInterconexion());
        
        // Test setIdInterconexion with large value
        usuarioInter.setIdInterconexion(Long.MAX_VALUE);
        assertEquals(Long.MAX_VALUE, usuarioInter.getIdInterconexion());
    }

    @Test
    void testUsuarioInterApellido() {
        // Test setApellido
        usuarioInter.setApellido("García");
        assertEquals("García", usuarioInter.getApellido());
        
        // Test setApellido with null
        usuarioInter.setApellido(null);
        assertNull(usuarioInter.getApellido());
        
        // Test setApellido with empty string
        usuarioInter.setApellido("");
        assertEquals("", usuarioInter.getApellido());
        
        // Test setApellido with different names
        usuarioInter.setApellido("López");
        assertEquals("López", usuarioInter.getApellido());
        
        usuarioInter.setApellido("Martínez");
        assertEquals("Martínez", usuarioInter.getApellido());
        
        // Test setApellido with special characters
        usuarioInter.setApellido("O'Connor");
        assertEquals("O'Connor", usuarioInter.getApellido());
        
        usuarioInter.setApellido("García-López");
        assertEquals("García-López", usuarioInter.getApellido());
    }

    @Test
    void testUsuarioInterDocumento() {
        // Test setDocumento
        usuarioInter.setDocumento("12345678");
        assertEquals("12345678", usuarioInter.getDocumento());
        
        // Test setDocumento with null
        usuarioInter.setDocumento(null);
        assertNull(usuarioInter.getDocumento());
        
        // Test setDocumento with empty string
        usuarioInter.setDocumento("");
        assertEquals("", usuarioInter.getDocumento());
        
        // Test setDocumento with different formats
        usuarioInter.setDocumento("ABC-123456");
        assertEquals("ABC-123456", usuarioInter.getDocumento());
        
        usuarioInter.setDocumento("98765432-1");
        assertEquals("98765432-1", usuarioInter.getDocumento());
        
        // Test setDocumento with letters only
        usuarioInter.setDocumento("ABCDEFGH");
        assertEquals("ABCDEFGH", usuarioInter.getDocumento());
        
        // Test setDocumento with mixed format
        usuarioInter.setDocumento("12-34-56-78");
        assertEquals("12-34-56-78", usuarioInter.getDocumento());
    }

    @Test
    void testUsuarioInterFechaNacimiento() {
        // Test setFechaNacimiento
        usuarioInter.setFechaNacimiento(testDate);
        assertEquals(testDate, usuarioInter.getFechaNacimiento());
        
        // Test setFechaNacimiento with null
        usuarioInter.setFechaNacimiento(null);
        assertNull(usuarioInter.getFechaNacimiento());
        
        // Test setFechaNacimiento with different dates
        Date pastDate = new Date(0L); // January 1, 1970
        usuarioInter.setFechaNacimiento(pastDate);
        assertEquals(pastDate, usuarioInter.getFechaNacimiento());
        
        Date futureDate = new Date(System.currentTimeMillis() + 86400000L); // Tomorrow
        usuarioInter.setFechaNacimiento(futureDate);
        assertEquals(futureDate, usuarioInter.getFechaNacimiento());
        
        // Test with current date
        Date currentDate = new Date();
        usuarioInter.setFechaNacimiento(currentDate);
        assertEquals(currentDate, usuarioInter.getFechaNacimiento());
    }

    @Test
    void testUsuarioInterGenero() {
        // Test setGenero
        usuarioInter.setGenero("Masculino");
        assertEquals("Masculino", usuarioInter.getGenero());
        
        // Test setGenero with null
        usuarioInter.setGenero(null);
        assertNull(usuarioInter.getGenero());
        
        // Test setGenero with empty string
        usuarioInter.setGenero("");
        assertEquals("", usuarioInter.getGenero());
        
        // Test setGenero with different values
        usuarioInter.setGenero("Femenino");
        assertEquals("Femenino", usuarioInter.getGenero());
        
        usuarioInter.setGenero("No binario");
        assertEquals("No binario", usuarioInter.getGenero());
        
        usuarioInter.setGenero("Prefiero no decir");
        assertEquals("Prefiero no decir", usuarioInter.getGenero());
        
        // Test setGenero with abbreviations
        usuarioInter.setGenero("M");
        assertEquals("M", usuarioInter.getGenero());
        
        usuarioInter.setGenero("F");
        assertEquals("F", usuarioInter.getGenero());
    }

    @Test
    void testUsuarioInterTelefono() {
        // Test setTelefono
        usuarioInter.setTelefono("+34 123 456 789");
        assertEquals("+34 123 456 789", usuarioInter.getTelefono());
        
        // Test setTelefono with null
        usuarioInter.setTelefono(null);
        assertNull(usuarioInter.getTelefono());
        
        // Test setTelefono with empty string
        usuarioInter.setTelefono("");
        assertEquals("", usuarioInter.getTelefono());
        
        // Test setTelefono with different formats
        usuarioInter.setTelefono("123-456-7890");
        assertEquals("123-456-7890", usuarioInter.getTelefono());
        
        usuarioInter.setTelefono("(123) 456-7890");
        assertEquals("(123) 456-7890", usuarioInter.getTelefono());
        
        usuarioInter.setTelefono("123.456.7890");
        assertEquals("123.456.7890", usuarioInter.getTelefono());
        
        // Test setTelefono with international format
        usuarioInter.setTelefono("+1-234-567-8900");
        assertEquals("+1-234-567-8900", usuarioInter.getTelefono());
        
        // Test setTelefono with short number
        usuarioInter.setTelefono("123");
        assertEquals("123", usuarioInter.getTelefono());
    }

    @Test
    void testUsuarioInterIdHospital() {
        // Test setIdHospital
        usuarioInter.setIdHospital(500L);
        assertEquals(500L, usuarioInter.getIdHospital());
        
        // Test setIdHospital with null
        usuarioInter.setIdHospital(null);
        assertNull(usuarioInter.getIdHospital());
        
        // Test setIdHospital with zero
        usuarioInter.setIdHospital(0L);
        assertEquals(0L, usuarioInter.getIdHospital());
        
        // Test setIdHospital with negative value
        usuarioInter.setIdHospital(-1L);
        assertEquals(-1L, usuarioInter.getIdHospital());
        
        // Test setIdHospital with large value
        usuarioInter.setIdHospital(Long.MAX_VALUE);
        assertEquals(Long.MAX_VALUE, usuarioInter.getIdHospital());
    }

    @Test
    void testUsuarioInterUsuario() {
        // Test setUsuario
        usuarioInter.setUsuario(testUsuario);
        assertEquals(testUsuario, usuarioInter.getUsuario());
        
        // Test setUsuario with null
        usuarioInter.setUsuario(null);
        assertNull(usuarioInter.getUsuario());
        
        // Test setUsuario with different user
        Usuario anotherUsuario = new Usuario();
        anotherUsuario.setId(999L);
        anotherUsuario.setNombreUsuario("María García");
        usuarioInter.setUsuario(anotherUsuario);
        assertEquals(anotherUsuario, usuarioInter.getUsuario());
        
        // Test setUsuario with user having different properties
        Usuario complexUsuario = new Usuario();
        complexUsuario.setId(777L);
        complexUsuario.setNombreUsuario("Carlos Rodríguez");
        complexUsuario.setCorreo("carlos@example.com");
        usuarioInter.setUsuario(complexUsuario);
        assertEquals(complexUsuario, usuarioInter.getUsuario());
    }

    @Test
    void testUsuarioInterCompleteObject() {
        // Test setting all fields
        usuarioInter.setIdInterconexion(1000L);
        usuarioInter.setApellido("Rodríguez");
        usuarioInter.setDocumento("DOC-1000-ROD");
        usuarioInter.setFechaNacimiento(testDate);
        usuarioInter.setGenero("Masculino");
        usuarioInter.setTelefono("+34 987 654 321");
        usuarioInter.setIdHospital(200L);
        usuarioInter.setUsuario(testUsuario);
        
        // Verify all fields
        assertEquals(1000L, usuarioInter.getIdInterconexion());
        assertEquals("Rodríguez", usuarioInter.getApellido());
        assertEquals("DOC-1000-ROD", usuarioInter.getDocumento());
        assertEquals(testDate, usuarioInter.getFechaNacimiento());
        assertEquals("Masculino", usuarioInter.getGenero());
        assertEquals("+34 987 654 321", usuarioInter.getTelefono());
        assertEquals(200L, usuarioInter.getIdHospital());
        assertEquals(testUsuario, usuarioInter.getUsuario());
    }

    @Test
    void testUsuarioInterFieldModification() {
        // Set initial values
        usuarioInter.setIdInterconexion(1L);
        usuarioInter.setApellido("Original");
        usuarioInter.setGenero("Original");
        usuarioInter.setUsuario(testUsuario);
        
        // Modify values
        usuarioInter.setIdInterconexion(2L);
        usuarioInter.setApellido("Modificado");
        usuarioInter.setGenero("Modificado");
        Usuario newUsuario = new Usuario();
        newUsuario.setId(999L);
        usuarioInter.setUsuario(newUsuario);
        
        // Verify modifications
        assertEquals(2L, usuarioInter.getIdInterconexion());
        assertEquals("Modificado", usuarioInter.getApellido());
        assertEquals("Modificado", usuarioInter.getGenero());
        assertEquals(newUsuario, usuarioInter.getUsuario());
    }

    @Test
    void testUsuarioInterEdgeCases() {
        // Test with very long strings
        String veryLongString = "A".repeat(1000);
        usuarioInter.setApellido(veryLongString);
        usuarioInter.setDocumento(veryLongString);
        usuarioInter.setGenero(veryLongString);
        usuarioInter.setTelefono(veryLongString);
        
        assertEquals(veryLongString, usuarioInter.getApellido());
        assertEquals(veryLongString, usuarioInter.getDocumento());
        assertEquals(veryLongString, usuarioInter.getGenero());
        assertEquals(veryLongString, usuarioInter.getTelefono());
        
        // Test with special characters
        String specialChars = "!@#$%^&*()_+-=[]{}|;':\",./<>?";
        usuarioInter.setApellido(specialChars);
        usuarioInter.setDocumento(specialChars);
        usuarioInter.setGenero(specialChars);
        usuarioInter.setTelefono(specialChars);
        
        assertEquals(specialChars, usuarioInter.getApellido());
        assertEquals(specialChars, usuarioInter.getDocumento());
        assertEquals(specialChars, usuarioInter.getGenero());
        assertEquals(specialChars, usuarioInter.getTelefono());
        
        // Test with numbers in string fields
        String numbersOnly = "123456789";
        usuarioInter.setApellido(numbersOnly);
        usuarioInter.setDocumento(numbersOnly);
        usuarioInter.setGenero(numbersOnly);
        usuarioInter.setTelefono(numbersOnly);
        
        assertEquals(numbersOnly, usuarioInter.getApellido());
        assertEquals(numbersOnly, usuarioInter.getDocumento());
        assertEquals(numbersOnly, usuarioInter.getGenero());
        assertEquals(numbersOnly, usuarioInter.getTelefono());
    }

    @Test
    void testUsuarioInterNullHandling() {
        // Test that all fields can handle null values properly
        usuarioInter.setIdInterconexion(null);
        usuarioInter.setApellido(null);
        usuarioInter.setDocumento(null);
        usuarioInter.setFechaNacimiento(null);
        usuarioInter.setGenero(null);
        usuarioInter.setTelefono(null);
        usuarioInter.setIdHospital(null);
        usuarioInter.setUsuario(null);
        
        // Verify all fields are null
        assertNull(usuarioInter.getIdInterconexion());
        assertNull(usuarioInter.getApellido());
        assertNull(usuarioInter.getDocumento());
        assertNull(usuarioInter.getFechaNacimiento());
        assertNull(usuarioInter.getGenero());
        assertNull(usuarioInter.getTelefono());
        assertNull(usuarioInter.getIdHospital());
        assertNull(usuarioInter.getUsuario());
    }
}
