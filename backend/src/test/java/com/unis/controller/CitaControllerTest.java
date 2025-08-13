package com.unis.controller;

import com.unis.model.Cita;
import com.unis.service.CitaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CitaControllerTest {

    @Mock
    private CitaService citaService;

    @InjectMocks
    private CitaController citaController;

    private Cita testCita;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        testCita = new Cita();
        testCita.setHoraInicio("09:00");
        testCita.setHoraFin("10:00");
    }

    @Test
    void testObtenerCitas() {
        // Arrange
        List<Cita> citasEsperadas = Arrays.asList(new Cita(), new Cita());
        when(citaService.obtenerCitas()).thenReturn(citasEsperadas);

        // Act
        List<Cita> resultado = citaController.obtenerCitas();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(citaService).obtenerCitas();
    }

    @Test
    void testObtenerCitaPorId() {
        // Arrange
        Long id = 1L;
        Cita citaEsperada = new Cita();
        when(citaService.obtenerCitaPorId(id)).thenReturn(citaEsperada);

        // Act
        Cita resultado = citaController.obtenerCitaPorId(id);

        // Assert
        assertNotNull(resultado);
        verify(citaService).obtenerCitaPorId(id);
    }

    @Test
    void testAgendarCitaExitoso() {
        // Arrange
        Cita cita = new Cita();
        cita.setHoraInicio("09:00");
        cita.setHoraFin("10:00");

        // Act & Assert
        assertDoesNotThrow(() -> citaController.agendarCita(cita));
        verify(citaService).agendarCita(cita);
    }

    @Test
    void testAgendarCitaHoraInicioMuyTemprana() {
        // Arrange
        Cita cita = new Cita();
        cita.setHoraInicio("07:00");
        cita.setHoraFin("08:00");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> citaController.agendarCita(cita)
        );
        assertEquals("⚠️ Las citas solo pueden ser entre 8:00 AM y 4:30 PM.", exception.getMessage());
        verify(citaService, never()).agendarCita(any());
    }

    @Test
    void testAgendarCitaHoraFinMuyTarde() {
        // Arrange
        Cita cita = new Cita();
        cita.setHoraInicio("15:00");
        cita.setHoraFin("17:00");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> citaController.agendarCita(cita)
        );
        assertEquals("⚠️ Las citas solo pueden ser entre 8:00 AM y 4:30 PM.", exception.getMessage());
        verify(citaService, never()).agendarCita(any());
    }

    @Test
    void testAgendarCitaHoraFinAntesQueInicio() {
        // Arrange
        Cita cita = new Cita();
        cita.setHoraInicio("10:00");
        cita.setHoraFin("09:00");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> citaController.agendarCita(cita)
        );
        assertEquals("⚠️ La hora de fin debe ser posterior a la hora de inicio.", exception.getMessage());
        verify(citaService, never()).agendarCita(any());
    }

    @Test
    void testAgendarCitaHoraFinIgualQueInicio() {
        // Arrange
        Cita cita = new Cita();
        cita.setHoraInicio("10:00");
        cita.setHoraFin("10:00");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> citaController.agendarCita(cita)
        );
        assertEquals("⚠️ La hora de fin debe ser posterior a la hora de inicio.", exception.getMessage());
        verify(citaService, never()).agendarCita(any());
    }

    @Test
    void testAgendarCitaFormatoHoraIncorrecto() {
        // Arrange
        Cita cita = new Cita();
        cita.setHoraInicio("25:00");
        cita.setHoraFin("26:00");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> citaController.agendarCita(cita)
        );
        assertEquals("⚠️ Formato de hora incorrecto. Use el formato HH:mm", exception.getMessage());
        verify(citaService, never()).agendarCita(any());
    }

    @Test
    void testCancelarCita() {
        // Arrange
        Long id = 1L;

        // Act
        citaController.cancelarCita(id);

        // Assert
        verify(citaService).cancelarCita(id);
    }

    @Test
    void testAgendarCitaHorarioLimiteInferior() {
        // Arrange
        Cita cita = new Cita();
        cita.setHoraInicio("08:00");
        cita.setHoraFin("09:00");

        // Act & Assert
        assertDoesNotThrow(() -> citaController.agendarCita(cita));
        verify(citaService).agendarCita(cita);
    }

    @Test
    void testAgendarCitaHorarioLimiteSuperior() {
        // Arrange
        Cita cita = new Cita();
        cita.setHoraInicio("15:30");
        cita.setHoraFin("16:30");

        // Act & Assert
        assertDoesNotThrow(() -> citaController.agendarCita(cita));
        verify(citaService).agendarCita(cita);
    }
}
