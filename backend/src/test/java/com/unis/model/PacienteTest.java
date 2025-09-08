package com.unis.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Test class for Paciente entity.
 * Tests all getters, setters, and business logic functionality.
 */
class PacienteTest {

    private Paciente paciente;
    private Date fechaNacimiento;
    private Usuario usuario;
    private List<Cita> citas;
    private byte[] fotografia;

    @BeforeEach
    void setUp() {
        paciente = new Paciente();
        fechaNacimiento = new Date(90, 0, 15); // 15 de enero de 1990
        usuario = new Usuario();
        citas = new ArrayList<>();
        fotografia = "foto_test".getBytes();
    }

    @Test
    void testDefaultConstructor() {
        assertNotNull(paciente);
        assertNull(paciente.getIdPaciente());
        assertNull(paciente.getIdUsuario());
        assertNull(paciente.getApellido());
        assertNull(paciente.getDocumento());
        assertNull(paciente.getFechaNacimiento());
        assertNull(paciente.getGenero());
        assertNull(paciente.getTelefono());
        assertNull(paciente.getFotografia());
        assertNull(paciente.getUsuario());
        assertNotNull(paciente.getCitas());
        assertTrue(paciente.getCitas().isEmpty());
    }

    @Test
    void testSetAndGetIdPaciente() {
        Long idPaciente = 123L;
        paciente.setIdPaciente(idPaciente);
        assertEquals(idPaciente, paciente.getIdPaciente());
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
    void testSetAndGetIdUsuario() {
        Long idUsuario = 456L;
        paciente.setIdUsuario(idUsuario);
        assertEquals(idUsuario, paciente.getIdUsuario());
    }

    @Test
    void testSetAndGetIdUsuarioWithNull() {
        paciente.setIdUsuario(null);
        assertNull(paciente.getIdUsuario());
    }

    @Test
    void testSetAndGetApellido() {
        String apellido = "García";
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
    void testSetAndGetTelefono() {
        String telefono = "50212345678";
        paciente.setTelefono(telefono);
        assertEquals(telefono, paciente.getTelefono());
    }

    @Test
    void testSetAndGetTelefonoWithNull() {
        paciente.setTelefono(null);
        assertNull(paciente.getTelefono());
    }

    @Test
    void testSetAndGetFotografia() {
        paciente.setFotografia(fotografia);
        assertArrayEquals(fotografia, paciente.getFotografia());
    }

    @Test
    void testSetAndGetFotografiaWithNull() {
        paciente.setFotografia(null);
        assertNull(paciente.getFotografia());
    }

    @Test
    void testSetAndGetFotografiaWithEmptyArray() {
        byte[] emptyFoto = new byte[0];
        paciente.setFotografia(emptyFoto);
        assertArrayEquals(emptyFoto, paciente.getFotografia());
    }

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
    void testSetAndGetCitas() {
        paciente.setCitas(citas);
        assertEquals(citas, paciente.getCitas());
    }

    @Test
    void testSetAndGetCitasWithNull() {
        paciente.setCitas(null);
        assertNull(paciente.getCitas());
    }

    @Test
    void testSetAndGetCitasWithEmptyList() {
        List<Cita> emptyCitas = new ArrayList<>();
        paciente.setCitas(emptyCitas);
        assertEquals(emptyCitas, paciente.getCitas());
        assertTrue(paciente.getCitas().isEmpty());
    }

    @Test
    void testMultipleSetters() {
        // Configurar todos los campos
        paciente.setIdPaciente(999L);
        paciente.setIdUsuario(888L);
        paciente.setApellido("López");
        paciente.setDocumento("87654321");
        paciente.setFechaNacimiento(fechaNacimiento);
        paciente.setGenero("Masculino");
        paciente.setTelefono("50287654321");
        paciente.setFotografia(fotografia);
        paciente.setUsuario(usuario);
        paciente.setCitas(citas);

        // Verificar todos los campos
        assertEquals(999L, paciente.getIdPaciente());
        assertEquals(888L, paciente.getIdUsuario());
        assertEquals("López", paciente.getApellido());
        assertEquals("87654321", paciente.getDocumento());
        assertEquals(fechaNacimiento, paciente.getFechaNacimiento());
        assertEquals("Masculino", paciente.getGenero());
        assertEquals("50287654321", paciente.getTelefono());
        assertArrayEquals(fotografia, paciente.getFotografia());
        assertEquals(usuario, paciente.getUsuario());
        assertEquals(citas, paciente.getCitas());
    }

    @Test
    void testDataIntegrity() {
        // Verificar que los datos no cambien después de múltiples lecturas
        paciente.setIdPaciente(123L);
        Long id1 = paciente.getIdPaciente();
        Long id2 = paciente.getIdPaciente();
        assertEquals(id1, id2);

        paciente.setApellido("Test");
        String apellido1 = paciente.getApellido();
        String apellido2 = paciente.getApellido();
        assertEquals(apellido1, apellido2);
    }

    @Test
    void testEdgeCaseValues() {
        // Test con valores extremos
        paciente.setIdPaciente(Long.MAX_VALUE);
        paciente.setIdUsuario(Long.MIN_VALUE);
        paciente.setApellido("A");
        paciente.setDocumento("");
        paciente.setTelefono(" ");
        paciente.setGenero("Género muy largo con muchos caracteres para probar límites");

        assertEquals(Long.MAX_VALUE, paciente.getIdPaciente());
        assertEquals(Long.MIN_VALUE, paciente.getIdUsuario());
        assertEquals("A", paciente.getApellido());
        assertEquals("", paciente.getDocumento());
        assertEquals(" ", paciente.getTelefono());
        assertEquals("Género muy largo con muchos caracteres para probar límites", paciente.getGenero());
    }

    @Test
    void testNullHandling() {
        // Test de manejo de valores nulos
        paciente.setIdPaciente(null);
        paciente.setIdUsuario(null);
        paciente.setApellido(null);
        paciente.setDocumento(null);
        paciente.setFechaNacimiento(null);
        paciente.setGenero(null);
        paciente.setTelefono(null);
        paciente.setFotografia(null);
        paciente.setUsuario(null);
        paciente.setCitas(null);

        assertNull(paciente.getIdPaciente());
        assertNull(paciente.getIdUsuario());
        assertNull(paciente.getApellido());
        assertNull(paciente.getDocumento());
        assertNull(paciente.getFechaNacimiento());
        assertNull(paciente.getGenero());
        assertNull(paciente.getTelefono());
        assertNull(paciente.getFotografia());
        assertNull(paciente.getUsuario());
        assertNull(paciente.getCitas());
    }

    @Test
    void testRepeatedUpdates() {
        // Actualizar el mismo valor múltiples veces
        for (int i = 0; i < 5; i++) {
            paciente.setIdPaciente((long) i);
            paciente.setApellido("Apellido" + i);
        }

        assertEquals(4L, paciente.getIdPaciente());
        assertEquals("Apellido4", paciente.getApellido());
    }

    @Test
    void testFieldIndependence() {
        // Verificar que los campos son independientes
        paciente.setIdPaciente(1L);
        paciente.setApellido("Apellido1");
        paciente.setGenero("Género1");

        // Cambiar solo un campo
        paciente.setIdPaciente(2L);

        assertEquals(2L, paciente.getIdPaciente());
        assertEquals("Apellido1", paciente.getApellido());
        assertEquals("Género1", paciente.getGenero());
    }

    @Test
    void testCitasListOperations() {
        List<Cita> testCitas = new ArrayList<>();
        Cita cita1 = new Cita();
        Cita cita2 = new Cita();
        
        testCitas.add(cita1);
        testCitas.add(cita2);
        
        paciente.setCitas(testCitas);
        
        assertEquals(2, paciente.getCitas().size());
        assertTrue(paciente.getCitas().contains(cita1));
        assertTrue(paciente.getCitas().contains(cita2));
    }

    @Test
    void testSpecialCharactersInStrings() {
        String specialApellido = "O'Connor-Smith";
        String specialGenero = "No binario & Otros";
        String specialTelefono = "502-1234-5678";
        
        paciente.setApellido(specialApellido);
        paciente.setGenero(specialGenero);
        paciente.setTelefono(specialTelefono);
        
        assertEquals(specialApellido, paciente.getApellido());
        assertEquals(specialGenero, paciente.getGenero());
        assertEquals(specialTelefono, paciente.getTelefono());
    }

    @Test
    void testFotografiaOperations() {
        // Test con diferentes tipos de fotografía
        byte[] foto1 = "foto1".getBytes();
        byte[] foto2 = "foto2".getBytes();
        
        paciente.setFotografia(foto1);
        assertArrayEquals(foto1, paciente.getFotografia());
        
        paciente.setFotografia(foto2);
        assertArrayEquals(foto2, paciente.getFotografia());
        
        // Verificar que la foto anterior no persiste
        assertNotEquals(foto1, paciente.getFotografia());
    }

    @Test
    void testDocumentoValidation() {
        // Test con diferentes formatos de documento
        String[] documentos = {"12345678", "ABC12345", "123-456-789", "1234567890123"};
        
        for (String doc : documentos) {
            paciente.setDocumento(doc);
            assertEquals(doc, paciente.getDocumento());
        }
    }

    @Test
    void testTelefonoValidation() {
        // Test con diferentes formatos de teléfono
        String[] telefonos = {"50212345678", "+50212345678", "1234-5678", "(502) 1234-5678"};
        
        for (String tel : telefonos) {
            paciente.setTelefono(tel);
            assertEquals(tel, paciente.getTelefono());
        }
    }
}
