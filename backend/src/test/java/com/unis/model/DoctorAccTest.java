package com.unis.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.io.Serializable;
import java.util.Date;

/**
 * Test class for DoctorAcc entity.
 * Tests all getters, setters, and edge cases to achieve 100% coverage.
 */
class DoctorAccTest {

    private DoctorAcc doctor;
    private UserAcc usuario;
    private Date fechaNacimiento;
    private Date fechaGraduacion;

    @BeforeEach
    void setUp() {
        doctor = new DoctorAcc();
        usuario = new UserAcc();
        usuario.setIdUsuario(1L);
        
        fechaNacimiento = new Date(80, 5, 15); // 15 de junio de 1980
        fechaGraduacion = new Date(105, 5, 20); // 20 de junio de 2005
    }

    @Test
    void testDefaultConstructor() {
        assertNotNull(doctor);
        assertNull(doctor.getIdDoctor());
        assertNull(doctor.getUsuario());
        assertNull(doctor.getApellido());
        assertNull(doctor.getDocumento());
        assertNull(doctor.getFechaNacimiento());
        assertNull(doctor.getGenero());
        assertNull(doctor.getTelefono());
        assertNull(doctor.getIdHospital());
        assertNull(doctor.getEspecialidad());
        assertNull(doctor.getNumeroColegiado());
        assertNull(doctor.getHorarioAtencion());
        assertNull(doctor.getFechaGraduacion());
        assertNull(doctor.getUniversidadGraduacion());
        assertNull(doctor.getDisponibilidad());
    }

    @Test
    void testImplementsSerializable() {
        assertTrue(doctor instanceof Serializable);
    }

    // ========== ID_DOCTOR Tests ==========
    
    @Test
    void testSetAndGetIdDoctor() {
        Long id = 123L;
        doctor.setIdDoctor(id);
        assertEquals(id, doctor.getIdDoctor());
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
    void testSetAndGetIdDoctorWithMaxValue() {
        Long maxValue = Long.MAX_VALUE;
        doctor.setIdDoctor(maxValue);
        assertEquals(maxValue, doctor.getIdDoctor());
    }

    // ========== USUARIO Tests ==========
    
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
    void testSetAndGetUsuarioWithNewInstance() {
        UserAcc newUsuario = new UserAcc();
        newUsuario.setIdUsuario(999L);
        doctor.setUsuario(newUsuario);
        assertEquals(newUsuario, doctor.getUsuario());
    }

    // ========== APELLIDO Tests ==========
    
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
    void testSetAndGetApellidoWithSpecialCharacters() {
        String apellido = "O'Connor";
        doctor.setApellido(apellido);
        assertEquals(apellido, doctor.getApellido());
    }

    @Test
    void testSetAndGetApellidoWithWhitespace() {
        String apellido = " De la Rosa ";
        doctor.setApellido(apellido);
        assertEquals(apellido, doctor.getApellido());
    }

    // ========== DOCUMENTO Tests ==========
    
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
    void testSetAndGetDocumentoWithEmptyString() {
        doctor.setDocumento("");
        assertEquals("", doctor.getDocumento());
    }

    @Test
    void testSetAndGetDocumentoWithLetters() {
        String documento = "ABC123456";
        doctor.setDocumento(documento);
        assertEquals(documento, doctor.getDocumento());
    }

    @Test
    void testSetAndGetDocumentoWithWhitespace() {
        String documento = " 123-456-789 ";
        doctor.setDocumento(documento);
        assertEquals(documento, doctor.getDocumento());
    }

    // ========== FECHA_NACIMIENTO Tests ==========
    
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
    void testSetAndGetFechaNacimientoWithNewDate() {
        Date newDate = new Date(90, 0, 1); // 1 de enero de 1990
        doctor.setFechaNacimiento(newDate);
        assertEquals(newDate, doctor.getFechaNacimiento());
    }

    @Test
    void testSetAndGetFechaNacimientoWithCurrentDate() {
        Date currentDate = new Date();
        doctor.setFechaNacimiento(currentDate);
        assertEquals(currentDate, doctor.getFechaNacimiento());
    }

    // ========== GENERO Tests ==========
    
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
    void testSetAndGetGeneroWithEmptyString() {
        doctor.setGenero("");
        assertEquals("", doctor.getGenero());
    }

    @Test
    void testSetAndGetGeneroWithDifferentValues() {
        String[] generos = {"Masculino", "Femenino", "No binario", "Prefiero no decir"};
        
        for (String gen : generos) {
            doctor.setGenero(gen);
            assertEquals(gen, doctor.getGenero());
        }
    }

    @Test
    void testSetAndGetGeneroWithWhitespace() {
        String genero = " Masculino ";
        doctor.setGenero(genero);
        assertEquals(genero, doctor.getGenero());
    }

    // ========== TELEFONO Tests ==========
    
    @Test
    void testSetAndGetTelefono() {
        String telefono = "+34 123 456 789";
        doctor.setTelefono(telefono);
        assertEquals(telefono, doctor.getTelefono());
    }

    @Test
    void testSetAndGetTelefonoWithNull() {
        doctor.setTelefono(null);
        assertNull(doctor.getTelefono());
    }

    @Test
    void testSetAndGetTelefonoWithEmptyString() {
        doctor.setTelefono("");
        assertEquals("", doctor.getTelefono());
    }

    @Test
    void testSetAndGetTelefonoWithDifferentFormats() {
        String[] telefonos = {
            "123-456-7890",
            "(123) 456-7890",
            "+1 123 456 7890",
            "123.456.7890"
        };
        
        for (String tel : telefonos) {
            doctor.setTelefono(tel);
            assertEquals(tel, doctor.getTelefono());
        }
    }

    @Test
    void testSetAndGetTelefonoWithWhitespace() {
        String telefono = " +34 123 456 789 ";
        doctor.setTelefono(telefono);
        assertEquals(telefono, doctor.getTelefono());
    }

    // ========== ID_HOSPITAL Tests ==========
    
    @Test
    void testSetAndGetIdHospital() {
        Long idHospital = 456L;
        doctor.setIdHospital(idHospital);
        assertEquals(idHospital, doctor.getIdHospital());
    }

    @Test
    void testSetAndGetIdHospitalWithNull() {
        doctor.setIdHospital(null);
        assertNull(doctor.getIdHospital());
    }

    @Test
    void testSetAndGetIdHospitalWithZero() {
        doctor.setIdHospital(0L);
        assertEquals(0L, doctor.getIdHospital());
    }

    @Test
    void testSetAndGetIdHospitalWithNegative() {
        doctor.setIdHospital(-1L);
        assertEquals(-1L, doctor.getIdHospital());
    }

    @Test
    void testSetAndGetIdHospitalWithMaxValue() {
        Long maxValue = Long.MAX_VALUE;
        doctor.setIdHospital(maxValue);
        assertEquals(maxValue, doctor.getIdHospital());
    }

    // ========== ESPECIALIDAD Tests ==========
    
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
    void testSetAndGetEspecialidadWithEmptyString() {
        doctor.setEspecialidad("");
        assertEquals("", doctor.getEspecialidad());
    }

    @Test
    void testSetAndGetEspecialidadWithDifferentSpecialties() {
        String[] especialidades = {
            "Cardiología",
            "Neurología",
            "Pediatría",
            "Cirugía General",
            "Dermatología",
            "Oftalmología"
        };
        
        for (String esp : especialidades) {
            doctor.setEspecialidad(esp);
            assertEquals(esp, doctor.getEspecialidad());
        }
    }

    @Test
    void testSetAndGetEspecialidadWithWhitespace() {
        String especialidad = " Cardiología ";
        doctor.setEspecialidad(especialidad);
        assertEquals(especialidad, doctor.getEspecialidad());
    }

    // ========== NUMERO_COLEGIADO Tests ==========
    
    @Test
    void testSetAndGetNumeroColegiado() {
        String numeroColegiado = "12345";
        doctor.setNumeroColegiado(numeroColegiado);
        assertEquals(numeroColegiado, doctor.getNumeroColegiado());
    }

    @Test
    void testSetAndGetNumeroColegiadoWithNull() {
        doctor.setNumeroColegiado(null);
        assertNull(doctor.getNumeroColegiado());
    }

    @Test
    void testSetAndGetNumeroColegiadoWithEmptyString() {
        doctor.setNumeroColegiado("");
        assertEquals("", doctor.getNumeroColegiado());
    }

    @Test
    void testSetAndGetNumeroColegiadoWithComplexFormat() {
        String numeroColegiado = "M-12345-2020";
        doctor.setNumeroColegiado(numeroColegiado);
        assertEquals(numeroColegiado, doctor.getNumeroColegiado());
    }

    @Test
    void testSetAndGetNumeroColegiadoWithWhitespace() {
        String numeroColegiado = " 12345 ";
        doctor.setNumeroColegiado(numeroColegiado);
        assertEquals(numeroColegiado, doctor.getNumeroColegiado());
    }

    // ========== HORARIO_ATENCION Tests ==========
    
    @Test
    void testSetAndGetHorarioAtencion() {
        String horario = "Lunes a Viernes: 8:00 - 18:00";
        doctor.setHorarioAtencion(horario);
        assertEquals(horario, doctor.getHorarioAtencion());
    }

    @Test
    void testSetAndGetHorarioAtencionWithNull() {
        doctor.setHorarioAtencion(null);
        assertNull(doctor.getHorarioAtencion());
    }

    @Test
    void testSetAndGetHorarioAtencionWithEmptyString() {
        doctor.setHorarioAtencion("");
        assertEquals("", doctor.getHorarioAtencion());
    }

    @Test
    void testSetAndGetHorarioAtencionWithComplexSchedule() {
        String horario = "Lunes: 8:00-12:00, 16:00-20:00\nMartes: 9:00-13:00\nMiércoles: 8:00-18:00";
        doctor.setHorarioAtencion(horario);
        assertEquals(horario, doctor.getHorarioAtencion());
    }

    @Test
    void testSetAndGetHorarioAtencionWithWhitespace() {
        String horario = " Lunes a Viernes: 8:00 - 18:00 ";
        doctor.setHorarioAtencion(horario);
        assertEquals(horario, doctor.getHorarioAtencion());
    }

    // ========== FECHA_GRADUACION Tests ==========
    
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
    void testSetAndGetFechaGraduacionWithNewDate() {
        Date newDate = new Date(110, 5, 15); // 15 de junio de 2010
        doctor.setFechaGraduacion(newDate);
        assertEquals(newDate, doctor.getFechaGraduacion());
    }

    @Test
    void testSetAndGetFechaGraduacionWithCurrentDate() {
        Date currentDate = new Date();
        doctor.setFechaGraduacion(currentDate);
        assertEquals(currentDate, doctor.getFechaGraduacion());
    }

    // ========== UNIVERSIDAD_GRADUACION Tests ==========
    
    @Test
    void testSetAndGetUniversidadGraduacion() {
        String universidad = "Universidad de Barcelona";
        doctor.setUniversidadGraduacion(universidad);
        assertEquals(universidad, doctor.getUniversidadGraduacion());
    }

    @Test
    void testSetAndGetUniversidadGraduacionWithNull() {
        doctor.setUniversidadGraduacion(null);
        assertNull(doctor.getUniversidadGraduacion());
    }

    @Test
    void testSetAndGetUniversidadGraduacionWithEmptyString() {
        doctor.setUniversidadGraduacion("");
        assertEquals("", doctor.getUniversidadGraduacion());
    }

    @Test
    void testSetAndGetUniversidadGraduacionWithDifferentUniversities() {
        String[] universidades = {
            "Universidad de Barcelona",
            "Universidad Autónoma de Madrid",
            "Universidad de Valencia",
            "Universidad de Granada",
            "Universidad Complutense de Madrid"
        };
        
        for (String univ : universidades) {
            doctor.setUniversidadGraduacion(univ);
            assertEquals(univ, doctor.getUniversidadGraduacion());
        }
    }

    @Test
    void testSetAndGetUniversidadGraduacionWithWhitespace() {
        String universidad = " Universidad de Barcelona ";
        doctor.setUniversidadGraduacion(universidad);
        assertEquals(universidad, doctor.getUniversidadGraduacion());
    }

    // ========== DISPONIBILIDAD Tests ==========
    
    @Test
    void testSetAndGetDisponibilidad() {
        String disponibilidad = "Tiempo completo";
        doctor.setDisponibilidad(disponibilidad);
        assertEquals(disponibilidad, doctor.getDisponibilidad());
    }

    @Test
    void testSetAndGetDisponibilidadWithNull() {
        doctor.setDisponibilidad(null);
        assertNull(doctor.getDisponibilidad());
    }

    @Test
    void testSetAndGetDisponibilidadWithEmptyString() {
        doctor.setDisponibilidad("");
        assertEquals("", doctor.getDisponibilidad());
    }

    @Test
    void testSetAndGetDisponibilidadWithDifferentTypes() {
        String[] disponibilidades = {
            "Tiempo completo",
            "Tiempo parcial",
            "Por horas",
            "Fines de semana",
            "Guardias nocturnas"
        };
        
        for (String disp : disponibilidades) {
            doctor.setDisponibilidad(disp);
            assertEquals(disp, doctor.getDisponibilidad());
        }
    }

    @Test
    void testSetAndGetDisponibilidadWithWhitespace() {
        String disponibilidad = " Tiempo completo ";
        doctor.setDisponibilidad(disponibilidad);
        assertEquals(disponibilidad, doctor.getDisponibilidad());
    }

    // ========== Comprehensive Tests ==========
    
    @Test
    void testCompleteDoctorSetup() {
        // Arrange
        Long id = 456L;
        String apellido = "Rodríguez";
        String documento = "87654321";
        String genero = "Femenino";
        String telefono = "+34 987 654 321";
        Long idHospital = 789L;
        String especialidad = "Neurología";
        String numeroColegiado = "N-98765-2015";
        String horarioAtencion = "Lunes a Viernes: 9:00 - 17:00";
        String universidad = "Universidad de Granada";
        String disponibilidad = "Tiempo completo";

        // Act
        doctor.setIdDoctor(id);
        doctor.setUsuario(usuario);
        doctor.setApellido(apellido);
        doctor.setDocumento(documento);
        doctor.setFechaNacimiento(fechaNacimiento);
        doctor.setGenero(genero);
        doctor.setTelefono(telefono);
        doctor.setIdHospital(idHospital);
        doctor.setEspecialidad(especialidad);
        doctor.setNumeroColegiado(numeroColegiado);
        doctor.setHorarioAtencion(horarioAtencion);
        doctor.setFechaGraduacion(fechaGraduacion);
        doctor.setUniversidadGraduacion(universidad);
        doctor.setDisponibilidad(disponibilidad);

        // Assert
        assertEquals(id, doctor.getIdDoctor());
        assertEquals(usuario, doctor.getUsuario());
        assertEquals(apellido, doctor.getApellido());
        assertEquals(documento, doctor.getDocumento());
        assertEquals(fechaNacimiento, doctor.getFechaNacimiento());
        assertEquals(genero, doctor.getGenero());
        assertEquals(telefono, doctor.getTelefono());
        assertEquals(idHospital, doctor.getIdHospital());
        assertEquals(especialidad, doctor.getEspecialidad());
        assertEquals(numeroColegiado, doctor.getNumeroColegiado());
        assertEquals(horarioAtencion, doctor.getHorarioAtencion());
        assertEquals(fechaGraduacion, doctor.getFechaGraduacion());
        assertEquals(universidad, doctor.getUniversidadGraduacion());
        assertEquals(disponibilidad, doctor.getDisponibilidad());
    }

    @Test
    void testMultipleUpdates() {
        // Arrange
        doctor.setEspecialidad("Cardiología");
        doctor.setTelefono("123-456-789");
        
        // Act - Update values
        doctor.setEspecialidad("Neurología");
        doctor.setTelefono("987-654-321");
        
        // Assert
        assertEquals("Neurología", doctor.getEspecialidad());
        assertEquals("987-654-321", doctor.getTelefono());
    }

    @Test
    void testResetToNull() {
        // Arrange
        doctor.setEspecialidad("Cardiología");
        doctor.setTelefono("123-456-789");
        
        // Act - Reset to null
        doctor.setEspecialidad(null);
        doctor.setTelefono(null);
        
        // Assert
        assertNull(doctor.getEspecialidad());
        assertNull(doctor.getTelefono());
    }

    @Test
    void testEdgeCaseValues() {
        // Arrange & Act - Set edge case values
        doctor.setIdDoctor(0L);
        doctor.setIdHospital(0L);
        doctor.setApellido("");
        doctor.setDocumento("   ");
        doctor.setEspecialidad("0");
        doctor.setNumeroColegiado("-1");
        
        // Assert
        assertEquals(0L, doctor.getIdDoctor());
        assertEquals(0L, doctor.getIdHospital());
        assertEquals("", doctor.getApellido());
        assertEquals("   ", doctor.getDocumento());
        assertEquals("0", doctor.getEspecialidad());
        assertEquals("-1", doctor.getNumeroColegiado());
    }

    @Test
    void testRelationshipUpdates() {
        // Arrange
        UserAcc nuevoUsuario = new UserAcc();
        nuevoUsuario.setIdUsuario(999L);
        
        // Act
        doctor.setUsuario(nuevoUsuario);
        
        // Assert
        assertEquals(nuevoUsuario, doctor.getUsuario());
        assertEquals(999L, doctor.getUsuario().getIdUsuario());
    }
}
