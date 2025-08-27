package com.unis.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.io.Serializable;
import java.util.Date;

/**
 * Test class for PacienteAcc entity.
 * Tests all getters, setters, and edge cases to achieve 100% coverage.
 */
class PacienteAccTest {

    private PacienteAcc paciente;
    private UserAcc usuario;
    private Date fechaNacimiento;

    @BeforeEach
    void setUp() {
        paciente = new PacienteAcc();
        usuario = new UserAcc();
        usuario.setIdUsuario(1L);
        fechaNacimiento = new Date(90, 2, 15); // 15 de marzo de 1990
    }

    @Test
    void testDefaultConstructor() {
        assertNotNull(paciente);
        assertNull(paciente.getIdPaciente());
        assertNull(paciente.getUsuario());
        assertNull(paciente.getApellido());
        assertNull(paciente.getDocumento());
        assertNull(paciente.getFechaNacimiento());
        assertNull(paciente.getGenero());
        assertNull(paciente.getTelefono());
        assertNull(paciente.getIdHospital());
    }

    @Test
    void testImplementsSerializable() {
        assertTrue(paciente instanceof Serializable);
    }

    // ========== ID_PACIENTE Tests ==========
    
    @Test
    void testSetAndGetIdPaciente() {
        Long id = 123L;
        paciente.setIdPaciente(id);
        assertEquals(id, paciente.getIdPaciente());
    }

    @Test
    void testSetAndGetIdPacienteWithNull() {
        paciente.setIdPaciente(null);
        assertNull(paciente.getIdPaciente());
    }

    @Test
    void testSetAndGetIdPacienteWithZero() {
        paciente.setIdPaciente(0L);
        assertEquals(0L, paciente.getIdPaciente());
    }

    @Test
    void testSetAndGetIdPacienteWithNegative() {
        paciente.setIdPaciente(-1L);
        assertEquals(-1L, paciente.getIdPaciente());
    }

    @Test
    void testSetAndGetIdPacienteWithMaxValue() {
        Long maxValue = Long.MAX_VALUE;
        paciente.setIdPaciente(maxValue);
        assertEquals(maxValue, paciente.getIdPaciente());
    }

    // ========== USUARIO Tests ==========
    
    @Test
    void testSetAndGetUsuario() {
        paciente.setUsuario(usuario);
        assertEquals(usuario, paciente.getUsuario());
    }

    @Test
    void testSetAndGetUsuarioWithNull() {
        paciente.setUsuario(null);
        assertNull(paciente.getUsuario());
    }

    @Test
    void testSetAndGetUsuarioWithNewInstance() {
        UserAcc newUsuario = new UserAcc();
        newUsuario.setIdUsuario(999L);
        paciente.setUsuario(newUsuario);
        assertEquals(newUsuario, paciente.getUsuario());
    }

    // ========== APELLIDO Tests ==========
    
    @Test
    void testSetAndGetApellido() {
        String apellido = "González";
        paciente.setApellido(apellido);
        assertEquals(apellido, paciente.getApellido());
    }

    @Test
    void testSetAndGetApellidoWithNull() {
        paciente.setApellido(null);
        assertNull(paciente.getApellido());
    }

    @Test
    void testSetAndGetApellidoWithEmptyString() {
        paciente.setApellido("");
        assertEquals("", paciente.getApellido());
    }

    @Test
    void testSetAndGetApellidoWithSpecialCharacters() {
        String apellido = "O'Connor";
        paciente.setApellido(apellido);
        assertEquals(apellido, paciente.getApellido());
    }

    @Test
    void testSetAndGetApellidoWithWhitespace() {
        String apellido = " De la Cruz ";
        paciente.setApellido(apellido);
        assertEquals(apellido, paciente.getApellido());
    }

    @Test
    void testSetAndGetApellidoWithLongName() {
        String apellido = "García-López de la Rosa y Martínez";
        paciente.setApellido(apellido);
        assertEquals(apellido, paciente.getApellido());
    }

    // ========== DOCUMENTO Tests ==========
    
    @Test
    void testSetAndGetDocumento() {
        String documento = "12345678";
        paciente.setDocumento(documento);
        assertEquals(documento, paciente.getDocumento());
    }

    @Test
    void testSetAndGetDocumentoWithNull() {
        paciente.setDocumento(null);
        assertNull(paciente.getDocumento());
    }

    @Test
    void testSetAndGetDocumentoWithEmptyString() {
        paciente.setDocumento("");
        assertEquals("", paciente.getDocumento());
    }

    @Test
    void testSetAndGetDocumentoWithLetters() {
        String documento = "ABC123456";
        paciente.setDocumento(documento);
        assertEquals(documento, paciente.getDocumento());
    }

    @Test
    void testSetAndGetDocumentoWithSpecialFormat() {
        String documento = "12.345.678-9";
        paciente.setDocumento(documento);
        assertEquals(documento, paciente.getDocumento());
    }

    @Test
    void testSetAndGetDocumentoWithWhitespace() {
        String documento = " 123-456-789 ";
        paciente.setDocumento(documento);
        assertEquals(documento, paciente.getDocumento());
    }

    // ========== FECHA_NACIMIENTO Tests ==========
    
    @Test
    void testSetAndGetFechaNacimiento() {
        paciente.setFechaNacimiento(fechaNacimiento);
        assertEquals(fechaNacimiento, paciente.getFechaNacimiento());
    }

    @Test
    void testSetAndGetFechaNacimientoWithNull() {
        paciente.setFechaNacimiento(null);
        assertNull(paciente.getFechaNacimiento());
    }

    @Test
    void testSetAndGetFechaNacimientoWithNewDate() {
        Date newDate = new Date(95, 0, 1); // 1 de enero de 1995
        paciente.setFechaNacimiento(newDate);
        assertEquals(newDate, paciente.getFechaNacimiento());
    }

    @Test
    void testSetAndGetFechaNacimientoWithCurrentDate() {
        Date currentDate = new Date();
        paciente.setFechaNacimiento(currentDate);
        assertEquals(currentDate, paciente.getFechaNacimiento());
    }

    @Test
    void testSetAndGetFechaNacimientoWithPastDate() {
        Date pastDate = new Date(80, 0, 1); // 1 de enero de 1980
        paciente.setFechaNacimiento(pastDate);
        assertEquals(pastDate, paciente.getFechaNacimiento());
    }

    // ========== GENERO Tests ==========
    
    @Test
    void testSetAndGetGenero() {
        String genero = "Femenino";
        paciente.setGenero(genero);
        assertEquals(genero, paciente.getGenero());
    }

    @Test
    void testSetAndGetGeneroWithNull() {
        paciente.setGenero(null);
        assertNull(paciente.getGenero());
    }

    @Test
    void testSetAndGetGeneroWithEmptyString() {
        paciente.setGenero("");
        assertEquals("", paciente.getGenero());
    }

    @Test
    void testSetAndGetGeneroWithDifferentValues() {
        String[] generos = {"Masculino", "Femenino", "No binario", "Prefiero no decir"};
        
        for (String gen : generos) {
            paciente.setGenero(gen);
            assertEquals(gen, paciente.getGenero());
        }
    }

    @Test
    void testSetAndGetGeneroWithWhitespace() {
        String genero = " Femenino ";
        paciente.setGenero(genero);
        assertEquals(genero, paciente.getGenero());
    }

    // ========== TELEFONO Tests ==========
    
    @Test
    void testSetAndGetTelefono() {
        String telefono = "+34 123 456 789";
        paciente.setTelefono(telefono);
        assertEquals(telefono, paciente.getTelefono());
    }

    @Test
    void testSetAndGetTelefonoWithNull() {
        paciente.setTelefono(null);
        assertNull(paciente.getTelefono());
    }

    @Test
    void testSetAndGetTelefonoWithEmptyString() {
        paciente.setTelefono("");
        assertEquals("", paciente.getTelefono());
    }

    @Test
    void testSetAndGetTelefonoWithDifferentFormats() {
        String[] telefonos = {
            "123-456-7890",
            "(123) 456-7890",
            "+1 123 456 7890",
            "123.456.7890",
            "+34 123 456 789"
        };
        
        for (String tel : telefonos) {
            paciente.setTelefono(tel);
            assertEquals(tel, paciente.getTelefono());
        }
    }

    @Test
    void testSetAndGetTelefonoWithWhitespace() {
        String telefono = " +34 123 456 789 ";
        paciente.setTelefono(telefono);
        assertEquals(telefono, paciente.getTelefono());
    }

    // ========== ID_HOSPITAL Tests ==========
    
    @Test
    void testSetAndGetIdHospital() {
        Long idHospital = 456L;
        paciente.setIdHospital(idHospital);
        assertEquals(idHospital, paciente.getIdHospital());
    }

    @Test
    void testSetAndGetIdHospitalWithNull() {
        paciente.setIdHospital(null);
        assertNull(paciente.getIdHospital());
    }

    @Test
    void testSetAndGetIdHospitalWithZero() {
        paciente.setIdHospital(0L);
        assertEquals(0L, paciente.getIdHospital());
    }

    @Test
    void testSetAndGetIdHospitalWithNegative() {
        paciente.setIdHospital(-1L);
        assertEquals(-1L, paciente.getIdHospital());
    }

    @Test
    void testSetAndGetIdHospitalWithMaxValue() {
        Long maxValue = Long.MAX_VALUE;
        paciente.setIdHospital(maxValue);
        assertEquals(maxValue, paciente.getIdHospital());
    }

    // ========== Comprehensive Tests ==========
    
    @Test
    void testCompletePacienteSetup() {
        // Arrange
        Long id = 789L;
        String apellido = "Rodríguez";
        String documento = "87654321";
        String genero = "Masculino";
        String telefono = "+34 987 654 321";
        Long idHospital = 123L;

        // Act
        paciente.setIdPaciente(id);
        paciente.setUsuario(usuario);
        paciente.setApellido(apellido);
        paciente.setDocumento(documento);
        paciente.setFechaNacimiento(fechaNacimiento);
        paciente.setGenero(genero);
        paciente.setTelefono(telefono);
        paciente.setIdHospital(idHospital);

        // Assert
        assertEquals(id, paciente.getIdPaciente());
        assertEquals(usuario, paciente.getUsuario());
        assertEquals(apellido, paciente.getApellido());
        assertEquals(documento, paciente.getDocumento());
        assertEquals(fechaNacimiento, paciente.getFechaNacimiento());
        assertEquals(genero, paciente.getGenero());
        assertEquals(telefono, paciente.getTelefono());
        assertEquals(idHospital, paciente.getIdHospital());
    }

    @Test
    void testMultipleUpdates() {
        // Arrange
        paciente.setApellido("González");
        paciente.setTelefono("123-456-789");
        
        // Act - Update values
        paciente.setApellido("Rodríguez");
        paciente.setTelefono("987-654-321");
        
        // Assert
        assertEquals("Rodríguez", paciente.getApellido());
        assertEquals("987-654-321", paciente.getTelefono());
    }

    @Test
    void testResetToNull() {
        // Arrange
        paciente.setApellido("González");
        paciente.setTelefono("123-456-789");
        
        // Act - Reset to null
        paciente.setApellido(null);
        paciente.setTelefono(null);
        
        // Assert
        assertNull(paciente.getApellido());
        assertNull(paciente.getTelefono());
    }

    @Test
    void testEdgeCaseValues() {
        // Arrange & Act - Set edge case values
        paciente.setIdPaciente(0L);
        paciente.setIdHospital(0L);
        paciente.setApellido("");
        paciente.setDocumento("   ");
        paciente.setGenero("0");
        paciente.setTelefono("-1");
        
        // Assert
        assertEquals(0L, paciente.getIdPaciente());
        assertEquals(0L, paciente.getIdHospital());
        assertEquals("", paciente.getApellido());
        assertEquals("   ", paciente.getDocumento());
        assertEquals("0", paciente.getGenero());
        assertEquals("-1", paciente.getTelefono());
    }

    @Test
    void testRelationshipUpdates() {
        // Arrange
        UserAcc nuevoUsuario = new UserAcc();
        nuevoUsuario.setIdUsuario(888L);
        
        // Act
        paciente.setUsuario(nuevoUsuario);
        
        // Assert
        assertEquals(nuevoUsuario, paciente.getUsuario());
        assertEquals(888L, paciente.getUsuario().getIdUsuario());
    }

    @Test
    void testAllFieldsTogether() {
        // Arrange
        Long id = 999L;
        String apellido = "Martínez";
        String documento = "11223344";
        String genero = "No binario";
        String telefono = "+34 555 666 777";
        Long idHospital = 999L;
        Date fecha = new Date(88, 7, 15); // 15 de agosto de 1988

        // Act
        paciente.setIdPaciente(id);
        paciente.setUsuario(usuario);
        paciente.setApellido(apellido);
        paciente.setDocumento(documento);
        paciente.setFechaNacimiento(fecha);
        paciente.setGenero(genero);
        paciente.setTelefono(telefono);
        paciente.setIdHospital(idHospital);

        // Assert - Verify all fields are set correctly
        assertEquals(id, paciente.getIdPaciente());
        assertEquals(usuario, paciente.getUsuario());
        assertEquals(apellido, paciente.getApellido());
        assertEquals(documento, paciente.getDocumento());
        assertEquals(fecha, paciente.getFechaNacimiento());
        assertEquals(genero, paciente.getGenero());
        assertEquals(telefono, paciente.getTelefono());
        assertEquals(idHospital, paciente.getIdHospital());
    }

    @Test
    void testDataIntegrity() {
        // Arrange
        String originalApellido = "González";
        String originalTelefono = "123-456-789";
        
        // Act
        paciente.setApellido(originalApellido);
        paciente.setTelefono(originalTelefono);
        
        // Assert - Verify data doesn't change after multiple reads
        String apellido1 = paciente.getApellido();
        String apellido2 = paciente.getApellido();
        String telefono1 = paciente.getTelefono();
        String telefono2 = paciente.getTelefono();
        
        assertEquals(apellido1, apellido2);
        assertEquals(telefono1, telefono2);
        assertEquals(originalApellido, apellido1);
        assertEquals(originalTelefono, telefono1);
    }
}
