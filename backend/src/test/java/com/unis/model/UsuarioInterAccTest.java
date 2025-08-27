package com.unis.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.io.Serializable;
import java.util.Date;

/**
 * Test class for UsuarioInterAcc entity.
 * Tests all getters, setters, and edge cases to achieve 100% coverage.
 */
class UsuarioInterAccTest {

    private UsuarioInterAcc usuarioInter;
    private Usuario usuario;
    private Date fechaNacimiento;

    @BeforeEach
    void setUp() {
        usuarioInter = new UsuarioInterAcc();
        usuario = new Usuario();
        usuario.setId(1L);
        fechaNacimiento = new Date(92, 5, 20); // 20 de junio de 1992
    }

    @Test
    void testDefaultConstructor() {
        assertNotNull(usuarioInter);
        assertNull(usuarioInter.getIdInterconexion());
        assertNull(usuarioInter.getUsuario());
        assertNull(usuarioInter.getApellido());
        assertNull(usuarioInter.getDocumento());
        assertNull(usuarioInter.getFechaNacimiento());
        assertNull(usuarioInter.getGenero());
        assertNull(usuarioInter.getTelefono());
        assertNull(usuarioInter.getIdHospital());
    }

    @Test
    void testImplementsSerializable() {
        assertTrue(usuarioInter instanceof Serializable);
    }

    // ========== ID_INTERCONEXION Tests ==========
    
    @Test
    void testSetAndGetIdInterconexion() {
        Long id = 123L;
        usuarioInter.setIdInterconexion(id);
        assertEquals(id, usuarioInter.getIdInterconexion());
    }

    @Test
    void testSetAndGetIdInterconexionWithNull() {
        usuarioInter.setIdInterconexion(null);
        assertNull(usuarioInter.getIdInterconexion());
    }

    @Test
    void testSetAndGetIdInterconexionWithZero() {
        usuarioInter.setIdInterconexion(0L);
        assertEquals(0L, usuarioInter.getIdInterconexion());
    }

    @Test
    void testSetAndGetIdInterconexionWithNegative() {
        usuarioInter.setIdInterconexion(-1L);
        assertEquals(-1L, usuarioInter.getIdInterconexion());
    }

    @Test
    void testSetAndGetIdInterconexionWithMaxValue() {
        Long maxValue = Long.MAX_VALUE;
        usuarioInter.setIdInterconexion(maxValue);
        assertEquals(maxValue, usuarioInter.getIdInterconexion());
    }

    // ========== USUARIO Tests ==========
    
    @Test
    void testSetAndGetUsuario() {
        usuarioInter.setUsuario(usuario);
        assertEquals(usuario, usuarioInter.getUsuario());
    }

    @Test
    void testSetAndGetUsuarioWithNull() {
        usuarioInter.setUsuario(null);
        assertNull(usuarioInter.getUsuario());
    }

    @Test
    void testSetAndGetUsuarioWithNewInstance() {
        Usuario newUsuario = new Usuario();
        newUsuario.setId(999L);
        usuarioInter.setUsuario(newUsuario);
        assertEquals(newUsuario, usuarioInter.getUsuario());
    }

    // ========== APELLIDO Tests ==========
    
    @Test
    void testSetAndGetApellido() {
        String apellido = "Fernández";
        usuarioInter.setApellido(apellido);
        assertEquals(apellido, usuarioInter.getApellido());
    }

    @Test
    void testSetAndGetApellidoWithNull() {
        usuarioInter.setApellido(null);
        assertNull(usuarioInter.getApellido());
    }

    @Test
    void testSetAndGetApellidoWithEmptyString() {
        usuarioInter.setApellido("");
        assertEquals("", usuarioInter.getApellido());
    }

    @Test
    void testSetAndGetApellidoWithSpecialCharacters() {
        String apellido = "O'Connor";
        usuarioInter.setApellido(apellido);
        assertEquals(apellido, usuarioInter.getApellido());
    }

    @Test
    void testSetAndGetApellidoWithWhitespace() {
        String apellido = " De la Vega ";
        usuarioInter.setApellido(apellido);
        assertEquals(apellido, usuarioInter.getApellido());
    }

    @Test
    void testSetAndGetApellidoWithLongName() {
        String apellido = "García-López de la Rosa y Martínez";
        usuarioInter.setApellido(apellido);
        assertEquals(apellido, usuarioInter.getApellido());
    }

    // ========== DOCUMENTO Tests ==========
    
    @Test
    void testSetAndGetDocumento() {
        String documento = "87654321";
        usuarioInter.setDocumento(documento);
        assertEquals(documento, usuarioInter.getDocumento());
    }

    @Test
    void testSetAndGetDocumentoWithNull() {
        usuarioInter.setDocumento(null);
        assertNull(usuarioInter.getDocumento());
    }

    @Test
    void testSetAndGetDocumentoWithEmptyString() {
        usuarioInter.setDocumento("");
        assertEquals("", usuarioInter.getDocumento());
    }

    @Test
    void testSetAndGetDocumentoWithLetters() {
        String documento = "XYZ987654";
        usuarioInter.setDocumento(documento);
        assertEquals(documento, usuarioInter.getDocumento());
    }

    @Test
    void testSetAndGetDocumentoWithSpecialFormat() {
        String documento = "12.345.678-9";
        usuarioInter.setDocumento(documento);
        assertEquals(documento, usuarioInter.getDocumento());
    }

    @Test
    void testSetAndGetDocumentoWithWhitespace() {
        String documento = " 987-654-321 ";
        usuarioInter.setDocumento(documento);
        assertEquals(documento, usuarioInter.getDocumento());
    }

    // ========== FECHA_NACIMIENTO Tests ==========
    
    @Test
    void testSetAndGetFechaNacimiento() {
        usuarioInter.setFechaNacimiento(fechaNacimiento);
        assertEquals(fechaNacimiento, usuarioInter.getFechaNacimiento());
    }

    @Test
    void testSetAndGetFechaNacimientoWithNull() {
        usuarioInter.setFechaNacimiento(null);
        assertNull(usuarioInter.getFechaNacimiento());
    }

    @Test
    void testSetAndGetFechaNacimientoWithNewDate() {
        Date newDate = new Date(95, 0, 1); // 1 de enero de 1995
        usuarioInter.setFechaNacimiento(newDate);
        assertEquals(newDate, usuarioInter.getFechaNacimiento());
    }

    @Test
    void testSetAndGetFechaNacimientoWithCurrentDate() {
        Date currentDate = new Date();
        usuarioInter.setFechaNacimiento(currentDate);
        assertEquals(currentDate, usuarioInter.getFechaNacimiento());
    }

    @Test
    void testSetAndGetFechaNacimientoWithPastDate() {
        Date pastDate = new Date(80, 0, 1); // 1 de enero de 1980
        usuarioInter.setFechaNacimiento(pastDate);
        assertEquals(pastDate, usuarioInter.getFechaNacimiento());
    }

    // ========== GENERO Tests ==========
    
    @Test
    void testSetAndGetGenero() {
        String genero = "Masculino";
        usuarioInter.setGenero(genero);
        assertEquals(genero, usuarioInter.getGenero());
    }

    @Test
    void testSetAndGetGeneroWithNull() {
        usuarioInter.setGenero(null);
        assertNull(usuarioInter.getGenero());
    }

    @Test
    void testSetAndGetGeneroWithEmptyString() {
        usuarioInter.setGenero("");
        assertEquals("", usuarioInter.getGenero());
    }

    @Test
    void testSetAndGetGeneroWithDifferentValues() {
        String[] generos = {"Masculino", "Femenino", "No binario", "Prefiero no decir"};
        
        for (String gen : generos) {
            usuarioInter.setGenero(gen);
            assertEquals(gen, usuarioInter.getGenero());
        }
    }

    @Test
    void testSetAndGetGeneroWithWhitespace() {
        String genero = " Masculino ";
        usuarioInter.setGenero(genero);
        assertEquals(genero, usuarioInter.getGenero());
    }

    // ========== TELEFONO Tests ==========
    
    @Test
    void testSetAndGetTelefono() {
        String telefono = "+34 555 666 777";
        usuarioInter.setTelefono(telefono);
        assertEquals(telefono, usuarioInter.getTelefono());
    }

    @Test
    void testSetAndGetTelefonoWithNull() {
        usuarioInter.setTelefono(null);
        assertNull(usuarioInter.getTelefono());
    }

    @Test
    void testSetAndGetTelefonoWithEmptyString() {
        usuarioInter.setTelefono("");
        assertEquals("", usuarioInter.getTelefono());
    }

    @Test
    void testSetAndGetTelefonoWithDifferentFormats() {
        String[] telefonos = {
            "555-666-7777",
            "(555) 666-7777",
            "+1 555 666 7777",
            "555.666.7777",
            "+34 555 666 777"
        };
        
        for (String tel : telefonos) {
            usuarioInter.setTelefono(tel);
            assertEquals(tel, usuarioInter.getTelefono());
        }
    }

    @Test
    void testSetAndGetTelefonoWithWhitespace() {
        String telefono = " +34 555 666 777 ";
        usuarioInter.setTelefono(telefono);
        assertEquals(telefono, usuarioInter.getTelefono());
    }

    // ========== ID_HOSPITAL Tests ==========
    
    @Test
    void testSetAndGetIdHospital() {
        Long idHospital = 456L;
        usuarioInter.setIdHospital(idHospital);
        assertEquals(idHospital, usuarioInter.getIdHospital());
    }

    @Test
    void testSetAndGetIdHospitalWithNull() {
        usuarioInter.setIdHospital(null);
        assertNull(usuarioInter.getIdHospital());
    }

    @Test
    void testSetAndGetIdHospitalWithZero() {
        usuarioInter.setIdHospital(0L);
        assertEquals(0L, usuarioInter.getIdHospital());
    }

    @Test
    void testSetAndGetIdHospitalWithNegative() {
        usuarioInter.setIdHospital(-1L);
        assertEquals(-1L, usuarioInter.getIdHospital());
    }

    @Test
    void testSetAndGetIdHospitalWithMaxValue() {
        Long maxValue = Long.MAX_VALUE;
        usuarioInter.setIdHospital(maxValue);
        assertEquals(maxValue, usuarioInter.getIdHospital());
    }

    // ========== Comprehensive Tests ==========
    
    @Test
    void testCompleteUsuarioInterSetup() {
        // Arrange
        Long id = 789L;
        String apellido = "Pérez";
        String documento = "11223344";
        String genero = "Femenino";
        String telefono = "+34 111 222 333";
        Long idHospital = 123L;

        // Act
        usuarioInter.setIdInterconexion(id);
        usuarioInter.setUsuario(usuario);
        usuarioInter.setApellido(apellido);
        usuarioInter.setDocumento(documento);
        usuarioInter.setFechaNacimiento(fechaNacimiento);
        usuarioInter.setGenero(genero);
        usuarioInter.setTelefono(telefono);
        usuarioInter.setIdHospital(idHospital);

        // Assert
        assertEquals(id, usuarioInter.getIdInterconexion());
        assertEquals(usuario, usuarioInter.getUsuario());
        assertEquals(apellido, usuarioInter.getApellido());
        assertEquals(documento, usuarioInter.getDocumento());
        assertEquals(fechaNacimiento, usuarioInter.getFechaNacimiento());
        assertEquals(genero, usuarioInter.getGenero());
        assertEquals(telefono, usuarioInter.getTelefono());
        assertEquals(idHospital, usuarioInter.getIdHospital());
    }

    @Test
    void testMultipleUpdates() {
        // Arrange
        usuarioInter.setApellido("González");
        usuarioInter.setTelefono("123-456-789");
        
        // Act - Update values
        usuarioInter.setApellido("Rodríguez");
        usuarioInter.setTelefono("987-654-321");
        
        // Assert
        assertEquals("Rodríguez", usuarioInter.getApellido());
        assertEquals("987-654-321", usuarioInter.getTelefono());
    }

    @Test
    void testResetToNull() {
        // Arrange
        usuarioInter.setApellido("González");
        usuarioInter.setTelefono("123-456-789");
        
        // Act - Reset to null
        usuarioInter.setApellido(null);
        usuarioInter.setTelefono(null);
        
        // Assert
        assertNull(usuarioInter.getApellido());
        assertNull(usuarioInter.getTelefono());
    }

    @Test
    void testEdgeCaseValues() {
        // Arrange & Act - Set edge case values
        usuarioInter.setIdInterconexion(0L);
        usuarioInter.setIdHospital(0L);
        usuarioInter.setApellido("");
        usuarioInter.setDocumento("   ");
        usuarioInter.setGenero("0");
        usuarioInter.setTelefono("-1");
        
        // Assert
        assertEquals(0L, usuarioInter.getIdInterconexion());
        assertEquals(0L, usuarioInter.getIdHospital());
        assertEquals("", usuarioInter.getApellido());
        assertEquals("   ", usuarioInter.getDocumento());
        assertEquals("0", usuarioInter.getGenero());
        assertEquals("-1", usuarioInter.getTelefono());
    }

    @Test
    void testRelationshipUpdates() {
        // Arrange
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setId(888L);
        
        // Act
        usuarioInter.setUsuario(nuevoUsuario);
        
        // Assert
        assertEquals(nuevoUsuario, usuarioInter.getUsuario());
        assertEquals(888L, usuarioInter.getUsuario().getId());
    }

    @Test
    void testAllFieldsTogether() {
        // Arrange
        Long id = 999L;
        String apellido = "López";
        String documento = "99887766";
        String genero = "No binario";
        String telefono = "+34 444 555 666";
        Long idHospital = 999L;
        Date fecha = new Date(88, 7, 15); // 15 de agosto de 1988

        // Act
        usuarioInter.setIdInterconexion(id);
        usuarioInter.setUsuario(usuario);
        usuarioInter.setApellido(apellido);
        usuarioInter.setDocumento(documento);
        usuarioInter.setFechaNacimiento(fecha);
        usuarioInter.setGenero(genero);
        usuarioInter.setTelefono(telefono);
        usuarioInter.setIdHospital(idHospital);

        // Assert - Verify all fields are set correctly
        assertEquals(id, usuarioInter.getIdInterconexion());
        assertEquals(usuario, usuarioInter.getUsuario());
        assertEquals(apellido, usuarioInter.getApellido());
        assertEquals(documento, usuarioInter.getDocumento());
        assertEquals(fecha, usuarioInter.getFechaNacimiento());
        assertEquals(genero, usuarioInter.getGenero());
        assertEquals(telefono, usuarioInter.getTelefono());
        assertEquals(idHospital, usuarioInter.getIdHospital());
    }

    @Test
    void testDataIntegrity() {
        // Arrange
        String originalApellido = "González";
        String originalTelefono = "123-456-789";
        
        // Act
        usuarioInter.setApellido(originalApellido);
        usuarioInter.setTelefono(originalTelefono);
        
        // Assert - Verify data doesn't change after multiple reads
        String apellido1 = usuarioInter.getApellido();
        String apellido2 = usuarioInter.getApellido();
        String telefono1 = usuarioInter.getTelefono();
        String telefono2 = usuarioInter.getTelefono();
        
        assertEquals(apellido1, apellido2);
        assertEquals(telefono1, telefono2);
        assertEquals(originalApellido, apellido1);
        assertEquals(originalTelefono, telefono1);
    }
}
