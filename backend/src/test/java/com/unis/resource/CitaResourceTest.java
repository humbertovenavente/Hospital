package com.unis.resource;

import com.unis.model.Cita;
import com.unis.service.CitaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import jakarta.ws.rs.core.Response;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CitaResourceTest {

    @Mock
    CitaService citaService;

    @InjectMocks
    CitaResource citaResource;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testObtenerCitas() {
        // Arrange
        List<Cita> citas = Arrays.asList(new Cita(), new Cita());
        when(citaService.obtenerCitas()).thenReturn(citas);

        // Act
        List<Cita> resultado = citaResource.obtenerCitas();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
    }

    @Test
    public void testObtenerCitaPorId() {
        // Arrange
        Cita cita = new Cita();
        cita.setIdCita(1L);
        when(citaService.obtenerCitaPorId(1L)).thenReturn(cita);

        // Act
        Cita resultado = citaResource.obtenerCita(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdCita());
    }

    @Test
    public void testAgendarCitaExitoso() {
        // Arrange
        Cita cita = new Cita();
        cita.setHoraInicio("09:00");
        cita.setHoraFin("10:00");
        doNothing().when(citaService).agendarCita(cita);

        // Act
        Response response = citaResource.agendarCita(cita);

        // Assert
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
    }

    @Test
    public void testAgendarCitaSinHoraInicio() {
        // Arrange
        Cita cita = new Cita();
        cita.setHoraFin("10:00");

        // Act
        Response response = citaResource.agendarCita(cita);

        // Assert
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

    @Test
    public void testAgendarCitaSinHoraFin() {
        // Arrange
        Cita cita = new Cita();
        cita.setHoraInicio("09:00");

        // Act
        Response response = citaResource.agendarCita(cita);

        // Assert
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

    @Test
    public void testAgendarCitaHoraInvalida() {
        // Arrange
        Cita cita = new Cita();
        cita.setHoraInicio("10:00");
        cita.setHoraFin("09:00");

        // Act
        Response response = citaResource.agendarCita(cita);

        // Assert
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }
}
