package com.unis.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Test class for Doctor entity.
 * Tests all getters, setters, and business logic functionality.
 */
class DoctorTest {

    private Doctor doctor;
    private Date fechaNacimiento;
    private Date fechaGraduacion;
    private Usuario usuario;
    private List<Cita> citas;

    @BeforeEach
    void setUp() {
        doctor = new Doctor();
        fechaNacimiento = new Date(90, 0, 15); // 15 de enero de 1990
        fechaGraduacion = new Date(115, 5, 20); // 20 de junio de 2015
        usuario = new Usuario();
        citas = new ArrayList<>();
    }

    @Test
    void testDefaultConstructor() {
        assertNotNull(doctor);
        assertNull(doctor.getIdDoctor());
        assertNull(doctor.getIdUsuario());
        assertNull(doctor.getApellido());
        assertNull(doctor.getDocumento());
        assertNull(doctor.getFechaNacimiento());
        assertNull(doctor.getGenero());
        assertNull(doctor.getTelefono());
        assertNull(doctor.getEspecialidad());
        assertNull(doctor.getNumeroColegiado());
        assertNull(doctor.getHorarioAtencion());
        assertNull(doctor.getFechaGraduacion());
        assertNull(doctor.getUniversidadGraduacion());
        assertNull(doctor.getUsuario());
        assertNotNull(doctor.getCitas());
        assertTrue(doctor.getCitas().isEmpty());
    }

    @Test
    void testSetAndGetIdDoctor() {
        Long idDoctor = 123L;
        doctor.setIdDoctor(idDoctor);
        assertEquals(idDoctor, doctor.getIdDoctor());
    }

    @Test
    void testSetAndGetIdDoctorWithNull() {
        doctor.setIdDoctor(null);
        assertNull(doctor.getIdDoctor());
    }

    @Test
    void testSetAndGetIdDoctorWithZero() {
        doctor.setIdDoctor(0L);
        assertEquals(0L, doctor.getIdDoctor());
    }

    @Test
    void testSetAndGetIdDoctorWithNegative() {
        doctor.setIdDoctor(-1L);
        assertEquals(-1L, doctor.getIdDoctor());
    }

    @Test
    void testSetAndGetIdUsuario() {
        Long idUsuario = 456L;
        doctor.setIdUsuario(idUsuario);
        assertEquals(idUsuario, doctor.getIdUsuario());
    }

    @Test
    void testSetAndGetIdUsuarioWithNull() {
        doctor.setIdUsuario(null);
        assertNull(doctor.getIdUsuario());
    }

    @Test
    void testSetAndGetApellido() {
        String apellido = "García";
        doctor.setApellido(apellido);
        assertEquals(apellido, doctor.getApellido());
    }

    @Test
    void testSetAndGetApellidoWithNull() {
        doctor.setApellido(null);
        assertNull(doctor.getApellido());
    }

    @Test
    void testSetAndGetApellidoWithEmptyString() {
        doctor.setApellido("");
        assertEquals("", doctor.getApellido());
    }

    @Test
    void testSetAndGetDocumento() {
        String documento = "12345678";
        doctor.setDocumento(documento);
        assertEquals(documento, doctor.getDocumento());
    }

    @Test
    void testSetAndGetDocumentoWithNull() {
        doctor.setDocumento(null);
        assertNull(doctor.getDocumento());
    }

    @Test
    void testSetAndGetFechaNacimiento() {
        doctor.setFechaNacimiento(fechaNacimiento);
        assertEquals(fechaNacimiento, doctor.getFechaNacimiento());
    }

    @Test
    void testSetAndGetFechaNacimientoWithNull() {
        doctor.setFechaNacimiento(null);
        assertNull(doctor.getFechaNacimiento());
    }

    @Test
    void testSetAndGetGenero() {
        String genero = "Masculino";
        doctor.setGenero(genero);
        assertEquals(genero, doctor.getGenero());
    }

    @Test
    void testSetAndGetGeneroWithNull() {
        doctor.setGenero(null);
        assertNull(doctor.getGenero());
    }

    @Test
    void testSetAndGetTelefono() {
        String telefono = "50212345678";
        doctor.setTelefono(telefono);
        assertEquals(telefono, doctor.getTelefono());
    }

    @Test
    void testSetAndGetTelefonoWithNull() {
        doctor.setTelefono(null);
        assertNull(doctor.getTelefono());
    }

    @Test
    void testSetAndGetEspecialidad() {
        String especialidad = "Cardiología";
        doctor.setEspecialidad(especialidad);
        assertEquals(especialidad, doctor.getEspecialidad());
    }

    @Test
    void testSetAndGetEspecialidadWithNull() {
        doctor.setEspecialidad(null);
        assertNull(doctor.getEspecialidad());
    }

    @Test
    void testSetAndGetNumeroColegiado() {
        String numeroColegiado = "COL12345";
        doctor.setNumeroColegiado(numeroColegiado);
        assertEquals(numeroColegiado, doctor.getNumeroColegiado());
    }

    @Test
    void testSetAndGetNumeroColegiadoWithNull() {
        doctor.setNumeroColegiado(null);
        assertNull(doctor.getNumeroColegiado());
    }

    @Test
    void testSetAndGetHorarioAtencion() {
        String horarioAtencion = "Lunes a Viernes 8:00 - 17:00";
        doctor.setHorarioAtencion(horarioAtencion);
        assertEquals(horarioAtencion, doctor.getHorarioAtencion());
    }

    @Test
    void testSetAndGetHorarioAtencionWithNull() {
        doctor.setHorarioAtencion(null);
        assertNull(doctor.getHorarioAtencion());
    }

    @Test
    void testSetAndGetFechaGraduacion() {
        doctor.setFechaGraduacion(fechaGraduacion);
        assertEquals(fechaGraduacion, doctor.getFechaGraduacion());
    }

    @Test
    void testSetAndGetFechaGraduacionWithNull() {
        doctor.setFechaGraduacion(null);
        assertNull(doctor.getFechaGraduacion());
    }

    @Test
    void testSetAndGetUniversidadGraduacion() {
        String universidad = "Universidad de San Carlos de Guatemala";
        doctor.setUniversidadGraduacion(universidad);
        assertEquals(universidad, doctor.getUniversidadGraduacion());
    }

    @Test
    void testSetAndGetUniversidadGraduacionWithNull() {
        doctor.setUniversidadGraduacion(null);
        assertNull(doctor.getUniversidadGraduacion());
    }

    @Test
    void testSetAndGetUsuario() {
        doctor.setUsuario(usuario);
        assertEquals(usuario, doctor.getUsuario());
    }

    @Test
    void testSetAndGetUsuarioWithNull() {
        doctor.setUsuario(null);
        assertNull(doctor.getUsuario());
    }

    @Test
    void testSetAndGetCitas() {
        doctor.setCitas(citas);
        assertEquals(citas, doctor.getCitas());
    }

    @Test
    void testSetAndGetCitasWithNull() {
        doctor.setCitas(null);
        assertNull(doctor.getCitas());
    }

    @Test
    void testSetAndGetCitasWithEmptyList() {
        List<Cita> emptyCitas = new ArrayList<>();
        doctor.setCitas(emptyCitas);
        assertEquals(emptyCitas, doctor.getCitas());
        assertTrue(doctor.getCitas().isEmpty());
    }

    @Test
    void testMultipleSetters() {
        // Configurar todos los campos
        doctor.setIdDoctor(999L);
        doctor.setIdUsuario(888L);
        doctor.setApellido("López");
        doctor.setDocumento("87654321");
        doctor.setFechaNacimiento(fechaNacimiento);
        doctor.setGenero("Femenino");
        doctor.setTelefono("50287654321");
        doctor.setEspecialidad("Neurología");
        doctor.setNumeroColegiado("COL99999");
        doctor.setHorarioAtencion("Lunes a Sábado 9:00 - 18:00");
        doctor.setFechaGraduacion(fechaGraduacion);
        doctor.setUniversidadGraduacion("Universidad Francisco Marroquín");
        doctor.setUsuario(usuario);
        doctor.setCitas(citas);

        // Verificar todos los campos
        assertEquals(999L, doctor.getIdDoctor());
        assertEquals(888L, doctor.getIdUsuario());
        assertEquals("López", doctor.getApellido());
        assertEquals("87654321", doctor.getDocumento());
        assertEquals(fechaNacimiento, doctor.getFechaNacimiento());
        assertEquals("Femenino", doctor.getGenero());
        assertEquals("50287654321", doctor.getTelefono());
        assertEquals("Neurología", doctor.getEspecialidad());
        assertEquals("COL99999", doctor.getNumeroColegiado());
        assertEquals("Lunes a Sábado 9:00 - 18:00", doctor.getHorarioAtencion());
        assertEquals(fechaGraduacion, doctor.getFechaGraduacion());
        assertEquals("Universidad Francisco Marroquín", doctor.getUniversidadGraduacion());
        assertEquals(usuario, doctor.getUsuario());
        assertEquals(citas, doctor.getCitas());
    }

    @Test
    void testDataIntegrity() {
        // Verificar que los datos no cambien después de múltiples lecturas
        doctor.setIdDoctor(123L);
        Long id1 = doctor.getIdDoctor();
        Long id2 = doctor.getIdDoctor();
        assertEquals(id1, id2);

        doctor.setApellido("Test");
        String apellido1 = doctor.getApellido();
        String apellido2 = doctor.getApellido();
        assertEquals(apellido1, apellido2);
    }

    @Test
    void testEdgeCaseValues() {
        // Test con valores extremos
        doctor.setIdDoctor(Long.MAX_VALUE);
        doctor.setIdUsuario(Long.MIN_VALUE);
        doctor.setApellido("A");
        doctor.setDocumento("");
        doctor.setTelefono(" ");
        doctor.setEspecialidad("Especialidad muy larga con muchos caracteres para probar límites");

        assertEquals(Long.MAX_VALUE, doctor.getIdDoctor());
        assertEquals(Long.MIN_VALUE, doctor.getIdUsuario());
        assertEquals("A", doctor.getApellido());
        assertEquals("", doctor.getDocumento());
        assertEquals(" ", doctor.getTelefono());
        assertEquals("Especialidad muy larga con muchos caracteres para probar límites", doctor.getEspecialidad());
    }

    @Test
    void testNullHandling() {
        // Test de manejo de valores nulos
        doctor.setIdDoctor(null);
        doctor.setIdUsuario(null);
        doctor.setApellido(null);
        doctor.setDocumento(null);
        doctor.setFechaNacimiento(null);
        doctor.setGenero(null);
        doctor.setTelefono(null);
        doctor.setEspecialidad(null);
        doctor.setNumeroColegiado(null);
        doctor.setHorarioAtencion(null);
        doctor.setFechaGraduacion(null);
        doctor.setUniversidadGraduacion(null);
        doctor.setUsuario(null);
        doctor.setCitas(null);

        assertNull(doctor.getIdDoctor());
        assertNull(doctor.getIdUsuario());
        assertNull(doctor.getApellido());
        assertNull(doctor.getDocumento());
        assertNull(doctor.getFechaNacimiento());
        assertNull(doctor.getGenero());
        assertNull(doctor.getTelefono());
        assertNull(doctor.getEspecialidad());
        assertNull(doctor.getNumeroColegiado());
        assertNull(doctor.getHorarioAtencion());
        assertNull(doctor.getFechaGraduacion());
        assertNull(doctor.getUniversidadGraduacion());
        assertNull(doctor.getUsuario());
        assertNull(doctor.getCitas());
    }

    @Test
    void testRepeatedUpdates() {
        // Actualizar el mismo valor múltiples veces
        for (int i = 0; i < 5; i++) {
            doctor.setIdDoctor((long) i);
            doctor.setApellido("Apellido" + i);
        }

        assertEquals(4L, doctor.getIdDoctor());
        assertEquals("Apellido4", doctor.getApellido());
    }

    @Test
    void testFieldIndependence() {
        // Verificar que los campos son independientes
        doctor.setIdDoctor(1L);
        doctor.setApellido("Apellido1");
        doctor.setEspecialidad("Especialidad1");

        // Cambiar solo un campo
        doctor.setIdDoctor(2L);

        assertEquals(2L, doctor.getIdDoctor());
        assertEquals("Apellido1", doctor.getApellido());
        assertEquals("Especialidad1", doctor.getEspecialidad());
    }

    @Test
    void testCitasListOperations() {
        List<Cita> testCitas = new ArrayList<>();
        Cita cita1 = new Cita();
        Cita cita2 = new Cita();
        
        testCitas.add(cita1);
        testCitas.add(cita2);
        
        doctor.setCitas(testCitas);
        
        assertEquals(2, doctor.getCitas().size());
        assertTrue(doctor.getCitas().contains(cita1));
        assertTrue(doctor.getCitas().contains(cita2));
    }

    @Test
    void testSpecialCharactersInStrings() {
        String specialApellido = "O'Connor-Smith";
        String specialEspecialidad = "Cardiología & Cirugía";
        String specialHorario = "Lunes-Viernes (8:00-17:00)";
        
        doctor.setApellido(specialApellido);
        doctor.setEspecialidad(specialEspecialidad);
        doctor.setHorarioAtencion(specialHorario);
        
        assertEquals(specialApellido, doctor.getApellido());
        assertEquals(specialEspecialidad, doctor.getEspecialidad());
        assertEquals(specialHorario, doctor.getHorarioAtencion());
    }
}
