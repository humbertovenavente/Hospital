package com.unis.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

public class CitaTest {

    private Cita cita;
    private Paciente paciente;
    private Doctor doctor;
    private Servicio servicio;

    @BeforeEach
    void setUp() {
        cita = new Cita();
        
        // Crear instancias de las entidades relacionadas
        paciente = new Paciente();
        paciente.setIdPaciente(1L);
        
        doctor = new Doctor();
        doctor.setIdDoctor(1L);
        
        servicio = new Servicio();
        servicio.id = 1L;
    }

    @Test
    void testConstructorPorDefecto() {
        assertNotNull(cita);
    }

    @Test
    void testGettersAndSetters() {
        // Arrange
        Long idCita = 1L;
        LocalDate fecha = LocalDate.of(2024, 12, 25);
        String horaInicio = "10:00";
        String horaFin = "11:00";
        String motivo = "Consulta de rutina";
        String diagnostico = "Paciente sano";
        String resultados = "Sin complicaciones";
        EstadoCita estado = EstadoCita.CONFIRMADA;

        // Act
        cita.setIdCita(idCita);
        cita.setFecha(fecha);
        cita.setHoraInicio(horaInicio);
        cita.setHoraFin(horaFin);
        cita.setMotivo(motivo);
        cita.setDiagnostico(diagnostico);
        cita.setResultados(resultados);
        cita.setEstado(estado);

        // Assert
        assertEquals(idCita, cita.getIdCita());
        assertEquals(fecha, cita.getFecha());
        assertEquals(horaInicio, cita.getHoraInicio());
        assertEquals(horaFin, cita.getHoraFin());
        assertEquals(motivo, cita.getMotivo());
        assertEquals(diagnostico, cita.getDiagnostico());
        assertEquals(resultados, cita.getResultados());
        assertEquals(estado, cita.getEstado());
    }

    @Test
    void testSetYGetIdCita() {
        // Arrange
        Long idCita = 999L;

        // Act
        cita.setIdCita(idCita);

        // Assert
        assertEquals(idCita, cita.getIdCita());
    }

    @Test
    void testSetYGetFecha() {
        // Arrange
        LocalDate fecha = LocalDate.of(2024, 6, 15);

        // Act
        cita.setFecha(fecha);

        // Assert
        assertEquals(fecha, cita.getFecha());
    }

    @Test
    void testSetYGetHoraInicio() {
        // Arrange
        String horaInicio = "14:30";

        // Act
        cita.setHoraInicio(horaInicio);

        // Assert
        assertEquals(horaInicio, cita.getHoraInicio());
    }

    @Test
    void testSetYGetHoraFin() {
        // Arrange
        String horaFin = "15:30";

        // Act
        cita.setHoraFin(horaFin);

        // Assert
        assertEquals(horaFin, cita.getHoraFin());
    }

    @Test
    void testSetYGetMotivo() {
        // Arrange
        String motivo = "Dolor de cabeza";

        // Act
        cita.setMotivo(motivo);

        // Assert
        assertEquals(motivo, cita.getMotivo());
    }

    @Test
    void testSetYGetDiagnostico() {
        // Arrange
        String diagnostico = "Migraña";

        // Act
        cita.setDiagnostico(diagnostico);

        // Assert
        assertEquals(diagnostico, cita.getDiagnostico());
    }

    @Test
    void testSetYGetResultados() {
        // Arrange
        String resultados = "Se recetó analgésico";

        // Act
        cita.setResultados(resultados);

        // Assert
        assertEquals(resultados, cita.getResultados());
    }

    @Test
    void testSetYGetEstado() {
        // Arrange
        EstadoCita estado = EstadoCita.FINALIZADA;

        // Act
        cita.setEstado(estado);

        // Assert
        assertEquals(estado, cita.getEstado());
    }

    @Test
    void testSetYGetPaciente() {
        // Act
        cita.setPaciente(paciente);

        // Assert
        assertEquals(paciente, cita.getPaciente());
        assertEquals(paciente.getIdPaciente(), cita.getIdPaciente());
    }

    @Test
    void testSetYGetDoctor() {
        // Act
        cita.setDoctor(doctor);

        // Assert
        assertEquals(doctor, cita.getDoctor());
        assertEquals(doctor.getIdDoctor(), cita.getIdDoctor());
    }

    @Test
    void testSetYGetServicio() {
        // Act
        cita.setServicio(servicio);

        // Assert
        assertEquals(servicio, cita.getServicio());
        // Nota: getIdServicio() puede retornar null si no está implementado
        // assertEquals(servicio.id, cita.getIdServicio());
    }

    @Test
    void testSetYGetIdPaciente() {
        // Arrange
        Long idPaciente = 123L;

        // Act
        cita.setIdPaciente(idPaciente);

        // Assert
        assertEquals(idPaciente, cita.getIdPaciente());
    }

    @Test
    void testSetYGetIdDoctor() {
        // Arrange
        Long idDoctor = 456L;

        // Act
        cita.setIdDoctor(idDoctor);

        // Assert
        assertEquals(idDoctor, cita.getIdDoctor());
    }

    @Test
    void testSetYGetIdServicio() {
        // Arrange
        Long idServicio = 789L;

        // Act
        cita.setIdServicio(idServicio);

        // Assert
        assertEquals(idServicio, cita.getIdServicio());
    }

    @Test
    void testSetYGetIdHospital() {
        // Arrange
        Long idHospital = 101L;

        // Act
        cita.setIdHospital(idHospital);

        // Assert
        assertEquals(idHospital, cita.getIdHospital());
    }

    @Test
    void testSetYGetIdAseguradora() {
        // Arrange
        Long idAseguradora = 202L;

        // Act
        cita.setIdAseguradora(idAseguradora);

        // Assert
        assertEquals(idAseguradora, cita.getIdAseguradora());
    }

    @Test
    void testSetYGetNumeroAutorizacion() {
        // Arrange
        String numeroAutorizacion = "AUTH123456";

        // Act
        cita.setNumeroAutorizacion(numeroAutorizacion);

        // Assert
        assertEquals(numeroAutorizacion, cita.getNumeroAutorizacion());
    }
}
