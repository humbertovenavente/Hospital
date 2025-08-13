package com.unis.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

public class DoctorTest {

    private Doctor doctor;

    @BeforeEach
    void setUp() {
        doctor = new Doctor();
    }

    @Test
    void testConstructorPorDefecto() {
        assertNotNull(doctor);
    }

    @Test
    void testGettersAndSetters() {
        // Arrange
        Long idDoctor = 1L;
        Long idUsuario = 100L;
        String apellido = "García";
        String documento = "12345678";
        String especialidad = "Cardiología";
        String numeroColegiado = "COL001";
        String horarioAtencion = "8:00-17:00";
        Date fechaGraduacion = new Date();

        // Act
        doctor.setIdDoctor(idDoctor);
        doctor.setIdUsuario(idUsuario);
        doctor.setApellido(apellido);
        doctor.setDocumento(documento);
        doctor.setEspecialidad(especialidad);
        doctor.setNumeroColegiado(numeroColegiado);
        doctor.setHorarioAtencion(horarioAtencion);
        doctor.setFechaGraduacion(fechaGraduacion);

        // Assert
        assertEquals(idDoctor, doctor.getIdDoctor());
        assertEquals(idUsuario, doctor.getIdUsuario());
        assertEquals(apellido, doctor.getApellido());
        assertEquals(documento, doctor.getDocumento());
        assertEquals(especialidad, doctor.getEspecialidad());
        assertEquals(numeroColegiado, doctor.getNumeroColegiado());
        assertEquals(horarioAtencion, doctor.getHorarioAtencion());
        assertEquals(fechaGraduacion, doctor.getFechaGraduacion());
    }

    @Test
    void testSetYGetIdDoctor() {
        // Arrange
        Long idDoctor = 999L;

        // Act
        doctor.setIdDoctor(idDoctor);

        // Assert
        assertEquals(idDoctor, doctor.getIdDoctor());
    }

    @Test
    void testSetYGetIdUsuario() {
        // Arrange
        Long idUsuario = 200L;

        // Act
        doctor.setIdUsuario(idUsuario);

        // Assert
        assertEquals(idUsuario, doctor.getIdUsuario());
    }

    @Test
    void testSetYGetApellido() {
        // Arrange
        String apellido = "Martínez";

        // Act
        doctor.setApellido(apellido);

        // Assert
        assertEquals(apellido, doctor.getApellido());
    }

    @Test
    void testSetYGetDocumento() {
        // Arrange
        String documento = "87654321";

        // Act
        doctor.setDocumento(documento);

        // Assert
        assertEquals(documento, doctor.getDocumento());
    }

    @Test
    void testSetYGetEspecialidad() {
        // Arrange
        String especialidad = "Neurología";

        // Act
        doctor.setEspecialidad(especialidad);

        // Assert
        assertEquals(especialidad, doctor.getEspecialidad());
    }

    @Test
    void testSetYGetNumeroColegiado() {
        // Arrange
        String numeroColegiado = "COL002";

        // Act
        doctor.setNumeroColegiado(numeroColegiado);

        // Assert
        assertEquals(numeroColegiado, doctor.getNumeroColegiado());
    }

    @Test
    void testSetYGetHorarioAtencion() {
        // Arrange
        String horarioAtencion = "9:00-18:00";

        // Act
        doctor.setHorarioAtencion(horarioAtencion);

        // Assert
        assertEquals(horarioAtencion, doctor.getHorarioAtencion());
    }

    @Test
    void testSetYGetFechaGraduacion() {
        // Arrange
        Date fechaGraduacion = new Date();

        // Act
        doctor.setFechaGraduacion(fechaGraduacion);

        // Assert
        assertEquals(fechaGraduacion, doctor.getFechaGraduacion());
    }

    @Test
    void testSetYGetGenero() {
        // Arrange
        String genero = "Masculino";

        // Act
        doctor.setGenero(genero);

        // Assert
        assertEquals(genero, doctor.getGenero());
    }

    @Test
    void testSetYGetTelefono() {
        // Arrange
        String telefono = "555-1234";

        // Act
        doctor.setTelefono(telefono);

        // Assert
        assertEquals(telefono, doctor.getTelefono());
    }

    @Test
    void testSetYGetFechaNacimiento() {
        // Arrange
        Date fechaNacimiento = new Date();

        // Act
        doctor.setFechaNacimiento(fechaNacimiento);

        // Assert
        assertEquals(fechaNacimiento, doctor.getFechaNacimiento());
    }

    @Test
    void testSetYGetUniversidadGraduacion() {
        // Arrange
        String universidad = "Universidad Nacional";

        // Act
        doctor.setUniversidadGraduacion(universidad);

        // Assert
        assertEquals(universidad, doctor.getUniversidadGraduacion());
    }

    @Test
    void testSetYGetUsuario() {
        // Arrange
        Usuario usuario = new Usuario();

        // Act
        doctor.setUsuario(usuario);

        // Assert
        assertEquals(usuario, doctor.getUsuario());
    }

    @Test
    void testSetYGetCitas() {
        // Arrange
        Cita cita1 = new Cita();
        Cita cita2 = new Cita();

        // Act
        doctor.getCitas().add(cita1);
        doctor.getCitas().add(cita2);

        // Assert
        assertEquals(2, doctor.getCitas().size());
        assertTrue(doctor.getCitas().contains(cita1));
        assertTrue(doctor.getCitas().contains(cita2));
    }

    @Test
    void testSetIdDoctorNull() {
        // Act
        doctor.setIdDoctor(null);

        // Assert
        assertNull(doctor.getIdDoctor());
    }

    @Test
    void testSetApellidoNull() {
        // Act
        doctor.setApellido(null);

        // Assert
        assertNull(doctor.getApellido());
    }

    @Test
    void testSetDocumentoNull() {
        // Act
        doctor.setDocumento(null);

        // Assert
        assertNull(doctor.getDocumento());
    }

    @Test
    void testSetEspecialidadNull() {
        // Act
        doctor.setEspecialidad(null);

        // Assert
        assertNull(doctor.getEspecialidad());
    }

    @Test
    void testSetNumeroColegiadoNull() {
        // Act
        doctor.setNumeroColegiado(null);

        // Assert
        assertNull(doctor.getNumeroColegiado());
    }

    @Test
    void testSetHorarioAtencionNull() {
        // Act
        doctor.setHorarioAtencion(null);

        // Assert
        assertNull(doctor.getHorarioAtencion());
    }

    @Test
    void testSetFechaGraduacionNull() {
        // Act
        doctor.setFechaGraduacion(null);

        // Assert
        assertNull(doctor.getFechaGraduacion());
    }

    @Test
    void testSetGeneroNull() {
        // Act
        doctor.setGenero(null);

        // Assert
        assertNull(doctor.getGenero());
    }

    @Test
    void testSetTelefonoNull() {
        // Act
        doctor.setTelefono(null);

        // Assert
        assertNull(doctor.getTelefono());
    }

    @Test
    void testSetFechaNacimientoNull() {
        // Act
        doctor.setFechaNacimiento(null);

        // Assert
        assertNull(doctor.getFechaNacimiento());
    }

    @Test
    void testSetUniversidadGraduacionNull() {
        // Act
        doctor.setUniversidadGraduacion(null);

        // Assert
        assertNull(doctor.getUniversidadGraduacion());
    }

    @Test
    void testSetUsuarioNull() {
        // Act
        doctor.setUsuario(null);

        // Assert
        assertNull(doctor.getUsuario());
    }

    @Test
    void testSetCitasNull() {
        // Act
        doctor.setCitas(null);

        // Assert
        assertNull(doctor.getCitas());
    }
}
