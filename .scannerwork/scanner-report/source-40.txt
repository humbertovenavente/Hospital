package com.unis.service;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.unis.model.Cita;
import com.unis.model.Doctor;
import com.unis.model.EstadoCita;
import com.unis.model.Paciente;
import com.unis.repository.CitaRepository;

import jakarta.persistence.EntityManager;

public class CitaServiceTest {

    @Mock
    CitaRepository citaRepository;

    @Mock
    EntityManager entityManager;

    @InjectMocks
    CitaService citaService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testObtenerCitas() {
        Cita cita1 = new Cita();
        Cita cita2 = new Cita();
        List<Cita> expectedCitas = Arrays.asList(cita1, cita2);
        when(citaRepository.listAll()).thenReturn(expectedCitas);

        List<Cita> actualCitas = citaService.obtenerCitas();

        assertEquals(expectedCitas, actualCitas);
    }

    @Test
    public void testObtenerCitaPorId() {
        Long id = 1L;
        Cita expectedCita = new Cita();
        when(citaRepository.findById(id)).thenReturn(expectedCita);

        Cita actualCita = citaService.obtenerCitaPorId(id);

        assertEquals(expectedCita, actualCita);
    }

    @Test
    public void testAgendarCitaSuccessful() {
        Cita cita = new Cita();
        cita.setIdDoctor(10L);
        cita.setIdPaciente(20L);

        Doctor doctor = new Doctor();
        Paciente paciente = new Paciente();

        when(entityManager.find(Doctor.class, 10L)).thenReturn(doctor);
        when(entityManager.find(Paciente.class, 20L)).thenReturn(paciente);

        citaService.agendarCita(cita);

        assertEquals(doctor, cita.getDoctor());
        assertEquals(paciente, cita.getPaciente());
        verify(citaRepository, times(1)).persist(cita);
    }

    @Test
    public void testAgendarCitaDoctorIdNull() {
        Cita cita = new Cita();
        cita.setIdDoctor(null);
        cita.setIdPaciente(20L);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            citaService.agendarCita(cita);
        });
        assertEquals("El ID del doctor y paciente son obligatorios.", exception.getMessage());
    }

    @Test
    public void testAgendarCitaPacienteIdNull() {
        Cita cita = new Cita();
        cita.setIdDoctor(10L);
        cita.setIdPaciente(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            citaService.agendarCita(cita);
        });
        assertEquals("El ID del doctor y paciente son obligatorios.", exception.getMessage());
    }

    @Test
    public void testAgendarCitaDoctorNotFound() {
        Cita cita = new Cita();
        cita.setIdDoctor(10L);
        cita.setIdPaciente(20L);

        when(entityManager.find(Doctor.class, 10L)).thenReturn(null);
        when(entityManager.find(Paciente.class, 20L)).thenReturn(new Paciente());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            citaService.agendarCita(cita);
        });
        assertEquals("Doctor o paciente no encontrados.", exception.getMessage());
    }

    @Test
    public void testAgendarCitaPacienteNotFound() {
        Cita cita = new Cita();
        cita.setIdDoctor(10L);
        cita.setIdPaciente(20L);

        when(entityManager.find(Doctor.class, 10L)).thenReturn(new Doctor());
        when(entityManager.find(Paciente.class, 20L)).thenReturn(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            citaService.agendarCita(cita);
        });
        assertEquals("Doctor o paciente no encontrados.", exception.getMessage());
    }

    @Test
    public void testCancelarCitaSuccessful() {
        Long id = 1L;
        Cita cita = new Cita();
        when(citaRepository.findById(id)).thenReturn(cita);

        citaService.cancelarCita(id);

        assertEquals(EstadoCita.CANCELADA, cita.getEstado());
    }

    @Test
    public void testCancelarCitaNotFound() {
        Long id = 1L;
        when(citaRepository.findById(id)).thenReturn(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            citaService.cancelarCita(id);
        });
        assertEquals("Cita no encontrada", exception.getMessage());
    }

    @Test
    public void testActualizarCitaSuccessful() {
        Long id = 1L;
        Cita citaExistente = new Cita();
        citaExistente.setEstado(EstadoCita.PENDIENTE);
        citaExistente.setDiagnostico("Diagnóstico antiguo");
        citaExistente.setResultados("Resultados antiguos");

        Cita citaActualizada = new Cita();
        citaActualizada.setEstado(EstadoCita.CONFIRMADA);
        citaActualizada.setDiagnostico("Nuevo diagnóstico");
        citaActualizada.setResultados("Nuevos resultados");

        when(citaRepository.findById(id)).thenReturn(citaExistente);

        citaService.actualizarCita(id, citaActualizada);

        assertEquals(EstadoCita.CONFIRMADA, citaExistente.getEstado());
        assertEquals("Nuevo diagnóstico", citaExistente.getDiagnostico());
        assertEquals("Nuevos resultados", citaExistente.getResultados());
    }

    @Test
    public void testActualizarCitaNotFound() {
        Long id = 1L;
        Cita citaActualizada = new Cita();
        when(citaRepository.findById(id)).thenReturn(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            citaService.actualizarCita(id, citaActualizada);
        });
        assertEquals("Cita no encontrada", exception.getMessage());
    }
}
