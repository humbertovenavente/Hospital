package com.unis.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.io.Serializable;
import java.util.Date;

/**
 * Test class for EmpleadoAcc entity.
 * Tests all getters, setters, and edge cases to achieve 100% coverage.
 */
class EmpleadoAccTest {

    private EmpleadoAcc empleado;
    private UserAcc usuario;
    private Date fechaNacimiento;

    @BeforeEach
    void setUp() {
        empleado = new EmpleadoAcc();
        usuario = new UserAcc();
        usuario.setIdUsuario(1L);
        fechaNacimiento = new Date(85, 3, 20); // 20 de abril de 1985
    }

    @Test
    void testDefaultConstructor() {
        assertNotNull(empleado);
        assertNull(empleado.getIdEmpleado());
        assertNull(empleado.getUsuario());
        assertNull(empleado.getApellido());
        assertNull(empleado.getDocumento());
        assertNull(empleado.getFechaNacimiento());
        assertNull(empleado.getGenero());
        assertNull(empleado.getTelefono());
        assertNull(empleado.getIdHospital());
        assertNull(empleado.getPuesto());
    }

    @Test
    void testImplementsSerializable() {
        assertTrue(empleado instanceof Serializable);
    }

    // ========== ID_EMPLEADO Tests ==========
    
    @Test
    void testSetAndGetIdEmpleado() {
        Long id = 123L;
        empleado.setIdEmpleado(id);
        assertEquals(id, empleado.getIdEmpleado());
    }

    @Test
    void testSetAndGetIdEmpleadoWithNull() {
        empleado.setIdEmpleado(null);
        assertNull(empleado.getIdEmpleado());
    }

    @Test
    void testSetAndGetIdEmpleadoWithZero() {
        empleado.setIdEmpleado(0L);
        assertEquals(0L, empleado.getIdEmpleado());
    }

    @Test
    void testSetAndGetIdEmpleadoWithNegative() {
        empleado.setIdEmpleado(-1L);
        assertEquals(-1L, empleado.getIdEmpleado());
    }

    @Test
    void testSetAndGetIdEmpleadoWithMaxValue() {
        Long maxValue = Long.MAX_VALUE;
        empleado.setIdEmpleado(maxValue);
        assertEquals(maxValue, empleado.getIdEmpleado());
    }

    // ========== USUARIO Tests ==========
    
    @Test
    void testSetAndGetUsuario() {
        empleado.setUsuario(usuario);
        assertEquals(usuario, empleado.getUsuario());
    }

    @Test
    void testSetAndGetUsuarioWithNull() {
        empleado.setUsuario(null);
        assertNull(empleado.getUsuario());
    }

    @Test
    void testSetAndGetUsuarioWithNewInstance() {
        UserAcc newUsuario = new UserAcc();
        newUsuario.setIdUsuario(999L);
        empleado.setUsuario(newUsuario);
        assertEquals(newUsuario, empleado.getUsuario());
    }

    // ========== APELLIDO Tests ==========
    
    @Test
    void testSetAndGetApellido() {
        String apellido = "López";
        empleado.setApellido(apellido);
        assertEquals(apellido, empleado.getApellido());
    }

    @Test
    void testSetAndGetApellidoWithNull() {
        empleado.setApellido(null);
        assertNull(empleado.getApellido());
    }

    @Test
    void testSetAndGetApellidoWithEmptyString() {
        empleado.setApellido("");
        assertEquals("", empleado.getApellido());
    }

    @Test
    void testSetAndGetApellidoWithSpecialCharacters() {
        String apellido = "O'Reilly";
        empleado.setApellido(apellido);
        assertEquals(apellido, empleado.getApellido());
    }

    @Test
    void testSetAndGetApellidoWithWhitespace() {
        String apellido = " De la Cruz ";
        empleado.setApellido(apellido);
        assertEquals(apellido, empleado.getApellido());
    }

    // ========== DOCUMENTO Tests ==========
    
    @Test
    void testSetAndGetDocumento() {
        String documento = "87654321";
        empleado.setDocumento(documento);
        assertEquals(documento, empleado.getDocumento());
    }

    @Test
    void testSetAndGetDocumentoWithNull() {
        empleado.setDocumento(null);
        assertNull(empleado.getDocumento());
    }

    @Test
    void testSetAndGetDocumentoWithEmptyString() {
        empleado.setDocumento("");
        assertEquals("", empleado.getDocumento());
    }

    @Test
    void testSetAndGetDocumentoWithLetters() {
        String documento = "XYZ987654";
        empleado.setDocumento(documento);
        assertEquals(documento, empleado.getDocumento());
    }

    @Test
    void testSetAndGetDocumentoWithWhitespace() {
        String documento = " 987-654-321 ";
        empleado.setDocumento(documento);
        assertEquals(documento, empleado.getDocumento());
    }

    // ========== FECHA_NACIMIENTO Tests ==========
    
    @Test
    void testSetAndGetFechaNacimiento() {
        empleado.setFechaNacimiento(fechaNacimiento);
        assertEquals(fechaNacimiento, empleado.getFechaNacimiento());
    }

    @Test
    void testSetAndGetFechaNacimientoWithNull() {
        empleado.setFechaNacimiento(null);
        assertNull(empleado.getFechaNacimiento());
    }

    @Test
    void testSetAndGetFechaNacimientoWithNewDate() {
        Date newDate = new Date(90, 0, 1); // 1 de enero de 1990
        empleado.setFechaNacimiento(newDate);
        assertEquals(newDate, empleado.getFechaNacimiento());
    }

    @Test
    void testSetAndGetFechaNacimientoWithCurrentDate() {
        Date currentDate = new Date();
        empleado.setFechaNacimiento(currentDate);
        assertEquals(currentDate, empleado.getFechaNacimiento());
    }

    // ========== GENERO Tests ==========
    
    @Test
    void testSetAndGetGenero() {
        String genero = "Femenino";
        empleado.setGenero(genero);
        assertEquals(genero, empleado.getGenero());
    }

    @Test
    void testSetAndGetGeneroWithNull() {
        empleado.setGenero(null);
        assertNull(empleado.getGenero());
    }

    @Test
    void testSetAndGetGeneroWithEmptyString() {
        empleado.setGenero("");
        assertEquals("", empleado.getGenero());
    }

    @Test
    void testSetAndGetGeneroWithDifferentValues() {
        String[] generos = {"Masculino", "Femenino", "No binario", "Prefiero no decir"};
        
        for (String gen : generos) {
            empleado.setGenero(gen);
            assertEquals(gen, empleado.getGenero());
        }
    }

    @Test
    void testSetAndGetGeneroWithWhitespace() {
        String genero = " Femenino ";
        empleado.setGenero(genero);
        assertEquals(genero, empleado.getGenero());
    }

    // ========== TELEFONO Tests ==========
    
    @Test
    void testSetAndGetTelefono() {
        String telefono = "+34 987 654 321";
        empleado.setTelefono(telefono);
        assertEquals(telefono, empleado.getTelefono());
    }

    @Test
    void testSetAndGetTelefonoWithNull() {
        empleado.setTelefono(null);
        assertNull(empleado.getTelefono());
    }

    @Test
    void testSetAndGetTelefonoWithEmptyString() {
        empleado.setTelefono("");
        assertEquals("", empleado.getTelefono());
    }

    @Test
    void testSetAndGetTelefonoWithDifferentFormats() {
        String[] telefonos = {
            "987-654-3210",
            "(987) 654-3210",
            "+34 987 654 3210",
            "987.654.3210"
        };
        
        for (String tel : telefonos) {
            empleado.setTelefono(tel);
            assertEquals(tel, empleado.getTelefono());
        }
    }

    @Test
    void testSetAndGetTelefonoWithWhitespace() {
        String telefono = " +34 987 654 321 ";
        empleado.setTelefono(telefono);
        assertEquals(telefono, empleado.getTelefono());
    }

    // ========== ID_HOSPITAL Tests ==========
    
    @Test
    void testSetAndGetIdHospital() {
        Long idHospital = 456L;
        empleado.setIdHospital(idHospital);
        assertEquals(idHospital, empleado.getIdHospital());
    }

    @Test
    void testSetAndGetIdHospitalWithNull() {
        empleado.setIdHospital(null);
        assertNull(empleado.getIdHospital());
    }

    @Test
    void testSetAndGetIdHospitalWithZero() {
        empleado.setIdHospital(0L);
        assertEquals(0L, empleado.getIdHospital());
    }

    @Test
    void testSetAndGetIdHospitalWithNegative() {
        empleado.setIdHospital(-1L);
        assertEquals(-1L, empleado.getIdHospital());
    }

    @Test
    void testSetAndGetIdHospitalWithMaxValue() {
        Long maxValue = Long.MAX_VALUE;
        empleado.setIdHospital(maxValue);
        assertEquals(maxValue, empleado.getIdHospital());
    }

    // ========== PUESTO Tests ==========
    
    @Test
    void testSetAndGetPuesto() {
        String puesto = "Enfermero";
        empleado.setPuesto(puesto);
        assertEquals(puesto, empleado.getPuesto());
    }

    @Test
    void testSetAndGetPuestoWithNull() {
        empleado.setPuesto(null);
        assertNull(empleado.getPuesto());
    }

    @Test
    void testSetAndGetPuestoWithEmptyString() {
        empleado.setPuesto("");
        assertEquals("", empleado.getPuesto());
    }

    @Test
    void testSetAndGetPuestoWithDifferentPositions() {
        String[] puestos = {
            "Enfermero",
            "Auxiliar de Enfermería",
            "Técnico de Laboratorio",
            "Recepcionista",
            "Administrativo",
            "Mantenimiento",
            "Seguridad"
        };
        
        for (String p : puestos) {
            empleado.setPuesto(p);
            assertEquals(p, empleado.getPuesto());
        }
    }

    @Test
    void testSetAndGetPuestoWithWhitespace() {
        String puesto = " Enfermero ";
        empleado.setPuesto(puesto);
        assertEquals(puesto, empleado.getPuesto());
    }

    // ========== Comprehensive Tests ==========
    
    @Test
    void testCompleteEmpleadoSetup() {
        // Arrange
        Long id = 789L;
        String apellido = "Martínez";
        String documento = "11223344";
        String genero = "Masculino";
        String telefono = "+34 555 666 777";
        Long idHospital = 123L;
        String puesto = "Técnico de Laboratorio";

        // Act
        empleado.setIdEmpleado(id);
        empleado.setUsuario(usuario);
        empleado.setApellido(apellido);
        empleado.setDocumento(documento);
        empleado.setFechaNacimiento(fechaNacimiento);
        empleado.setGenero(genero);
        empleado.setTelefono(telefono);
        empleado.setIdHospital(idHospital);
        empleado.setPuesto(puesto);

        // Assert
        assertEquals(id, empleado.getIdEmpleado());
        assertEquals(usuario, empleado.getUsuario());
        assertEquals(apellido, empleado.getApellido());
        assertEquals(documento, empleado.getDocumento());
        assertEquals(fechaNacimiento, empleado.getFechaNacimiento());
        assertEquals(genero, empleado.getGenero());
        assertEquals(telefono, empleado.getTelefono());
        assertEquals(idHospital, empleado.getIdHospital());
        assertEquals(puesto, empleado.getPuesto());
    }

    @Test
    void testMultipleUpdates() {
        // Arrange
        empleado.setPuesto("Enfermero");
        empleado.setTelefono("123-456-789");
        
        // Act - Update values
        empleado.setPuesto("Auxiliar de Enfermería");
        empleado.setTelefono("987-654-321");
        
        // Assert
        assertEquals("Auxiliar de Enfermería", empleado.getPuesto());
        assertEquals("987-654-321", empleado.getTelefono());
    }

    @Test
    void testResetToNull() {
        // Arrange
        empleado.setPuesto("Enfermero");
        empleado.setTelefono("123-456-789");
        
        // Act - Reset to null
        empleado.setPuesto(null);
        empleado.setTelefono(null);
        
        // Assert
        assertNull(empleado.getPuesto());
        assertNull(empleado.getTelefono());
    }

    @Test
    void testEdgeCaseValues() {
        // Arrange & Act - Set edge case values
        empleado.setIdEmpleado(0L);
        empleado.setIdHospital(0L);
        empleado.setApellido("");
        empleado.setDocumento("   ");
        empleado.setPuesto("0");
        
        // Assert
        assertEquals(0L, empleado.getIdEmpleado());
        assertEquals(0L, empleado.getIdHospital());
        assertEquals("", empleado.getApellido());
        assertEquals("   ", empleado.getDocumento());
        assertEquals("0", empleado.getPuesto());
    }

    @Test
    void testRelationshipUpdates() {
        // Arrange
        UserAcc nuevoUsuario = new UserAcc();
        nuevoUsuario.setIdUsuario(888L);
        
        // Act
        empleado.setUsuario(nuevoUsuario);
        
        // Assert
        assertEquals(nuevoUsuario, empleado.getUsuario());
        assertEquals(888L, empleado.getUsuario().getIdUsuario());
    }

    @Test
    void testAllFieldsTogether() {
        // Arrange
        Long id = 999L;
        String apellido = "González";
        String documento = "99887766";
        String genero = "No binario";
        String telefono = "+34 111 222 333";
        Long idHospital = 999L;
        String puesto = "Administrativo";
        Date fecha = new Date(88, 7, 15); // 15 de agosto de 1988

        // Act
        empleado.setIdEmpleado(id);
        empleado.setUsuario(usuario);
        empleado.setApellido(apellido);
        empleado.setDocumento(documento);
        empleado.setFechaNacimiento(fecha);
        empleado.setGenero(genero);
        empleado.setTelefono(telefono);
        empleado.setIdHospital(idHospital);
        empleado.setPuesto(puesto);

        // Assert - Verify all fields are set correctly
        assertEquals(id, empleado.getIdEmpleado());
        assertEquals(usuario, empleado.getUsuario());
        assertEquals(apellido, empleado.getApellido());
        assertEquals(documento, empleado.getDocumento());
        assertEquals(fecha, empleado.getFechaNacimiento());
        assertEquals(genero, empleado.getGenero());
        assertEquals(telefono, empleado.getTelefono());
        assertEquals(idHospital, empleado.getIdHospital());
        assertEquals(puesto, empleado.getPuesto());
    }
}
