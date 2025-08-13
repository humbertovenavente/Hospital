package com.unis.resource;

import com.unis.model.Paciente;
import com.unis.service.PacienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import jakarta.ws.rs.core.Response;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PacienteResourceTest {

    @Mock
    PacienteService pacienteService;

    @InjectMocks
    PacienteResource pacienteResource;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testObtenerTodosLosPacientes() {
        // Arrange
        List<Paciente> pacientes = Arrays.asList(new Paciente(), new Paciente());
        when(pacienteService.getAllPacientes()).thenReturn(pacientes);

        // Act
        List<Paciente> resultado = pacienteResource.obtenerTodosLosPacientes();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
    }

    @Test
    public void testObtenerPacientePorIdExitoso() {
        // Arrange
        Paciente paciente = new Paciente();
        paciente.setIdPaciente(1L);
        when(pacienteService.getPacienteById(1L)).thenReturn(Optional.of(paciente));

        // Act
        Response response = pacienteResource.obtenerPaciente(1L);

        // Assert
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void testObtenerPacientePorIdNoEncontrado() {
        // Arrange
        when(pacienteService.getPacienteById(1L)).thenReturn(Optional.empty());

        // Act
        Response response = pacienteResource.obtenerPaciente(1L);

        // Assert
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

    @Test
    public void testRegistrarPacienteExitoso() {
        // Arrange
        Paciente paciente = new Paciente();
        doNothing().when(pacienteService).registrarPaciente(paciente);

        // Act
        Response response = pacienteResource.registrarPaciente(paciente);

        // Assert
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
    }

    @Test
    public void testActualizarPacienteExitoso() {
        // Arrange
        Paciente paciente = new Paciente();
        when(pacienteService.actualizarPaciente(1L, paciente)).thenReturn(true);

        // Act
        Response response = pacienteResource.actualizarPaciente(1L, paciente);

        // Assert
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void testActualizarPacienteNoEncontrado() {
        // Arrange
        Paciente paciente = new Paciente();
        when(pacienteService.actualizarPaciente(1L, paciente)).thenReturn(false);

        // Act
        Response response = pacienteResource.actualizarPaciente(1L, paciente);

        // Assert
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

    @Test
    public void testEliminarPacienteExitoso() {
        // Arrange
        when(pacienteService.eliminarPaciente(1L)).thenReturn(true);

        // Act
        Response response = pacienteResource.eliminarPaciente(1L);

        // Assert
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void testEliminarPacienteNoEncontrado() {
        // Arrange
        when(pacienteService.eliminarPaciente(1L)).thenReturn(false);

        // Act
        Response response = pacienteResource.eliminarPaciente(1L);

        // Assert
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }
}
