package com.unis.resource;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unis.model.Cita;
import com.unis.model.Doctor;
import com.unis.service.CitaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import jakarta.ws.rs.core.Response;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CitaResourceTest {

    @Mock
    CitaService citaService;

    @InjectMocks
    CitaResource citaResource;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
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

    @Test
    public void testActualizarCitaExitoso() {
        // Arrange
        Long id = 1L;
        Cita citaActualizada = new Cita();
        doNothing().when(citaService).actualizarCita(id, citaActualizada);

        // Act
        Response response = citaResource.actualizarCita(id, citaActualizada);

        // Assert
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals("✅ Cita actualizada con éxito", response.getEntity());
        verify(citaService, times(1)).actualizarCita(id, citaActualizada);
    }

    @Test
    public void testActualizarCitaNoEncontrada() {
        // Arrange
        Long id = 1L;
        Cita citaActualizada = new Cita();
        doThrow(new IllegalArgumentException("Cita no encontrada"))
            .when(citaService).actualizarCita(id, citaActualizada);

        // Act
        Response response = citaResource.actualizarCita(id, citaActualizada);

        // Assert
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertEquals("⚠️ Error: Cita no encontrada", response.getEntity());
        verify(citaService, times(1)).actualizarCita(id, citaActualizada);
    }

    @Test
    public void testCancelarCitaExitoso() {
        // Arrange
        Long id = 1L;
        doNothing().when(citaService).cancelarCita(id);

        // Act
        Response response = citaResource.cancelarCita(id);

        // Assert
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals("Cita cancelada", response.getEntity());
        verify(citaService, times(1)).cancelarCita(id);
    }

    @Test
    public void testCancelarCitaNoEncontrada() {
        // Arrange
        Long id = 1L;
        doThrow(new IllegalArgumentException("Cita no encontrada"))
            .when(citaService).cancelarCita(id);

        // Act
        Response response = citaResource.cancelarCita(id);

        // Assert
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertEquals("⚠️ Error: Cita no encontrada", response.getEntity());
        verify(citaService, times(1)).cancelarCita(id);
    }

    @Test
    public void testCancelarCitaConErrorGenerico() {
        // Arrange
        Long id = 1L;
        doThrow(new RuntimeException("Error interno"))
            .when(citaService).cancelarCita(id);

        // Act
        Response response = citaResource.cancelarCita(id);

        // Assert
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertEquals("⚠️ Error: Error interno", response.getEntity());
        verify(citaService, times(1)).cancelarCita(id);
    }

    @Test
    public void testReasignarDoctorExitoso() throws Exception {
        // Arrange
        Long idCita = 1L;
        Long idDoctor = 2L;
        Doctor nuevoDoctor = new Doctor();
        nuevoDoctor.setIdDoctor(idDoctor);
        
        JsonNode body = objectMapper.createObjectNode()
            .put("idDoctor", idDoctor);
        
        when(citaService.buscarDoctorPorId(idDoctor)).thenReturn(nuevoDoctor);
        doNothing().when(citaService).reasignarDoctor(idCita, nuevoDoctor);

        // Act
        Response response = citaResource.reasignarDoctor(idCita, body);

        // Assert
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals("Doctor reasignado con éxito", response.getEntity());
        verify(citaService, times(1)).buscarDoctorPorId(idDoctor);
        verify(citaService, times(1)).reasignarDoctor(idCita, nuevoDoctor);
    }

    @Test
    public void testReasignarDoctorNoEncontrado() throws Exception {
        // Arrange
        Long idCita = 1L;
        Long idDoctor = 2L;
        
        JsonNode body = objectMapper.createObjectNode()
            .put("idDoctor", idDoctor);
        
        when(citaService.buscarDoctorPorId(idDoctor)).thenReturn(null);

        // Act
        Response response = citaResource.reasignarDoctor(idCita, body);

        // Assert
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertEquals("⚠️ Doctor no encontrado", response.getEntity());
        verify(citaService, times(1)).buscarDoctorPorId(idDoctor);
        verify(citaService, never()).reasignarDoctor(any(), any());
    }

    @Test
    public void testReasignarDoctorConErrorDeReasignacion() throws Exception {
        // Arrange
        Long idCita = 1L;
        Long idDoctor = 2L;
        Doctor nuevoDoctor = new Doctor();
        nuevoDoctor.setIdDoctor(idDoctor);
        
        JsonNode body = objectMapper.createObjectNode()
            .put("idDoctor", idDoctor);
        
        when(citaService.buscarDoctorPorId(idDoctor)).thenReturn(nuevoDoctor);
        doThrow(new IllegalArgumentException("Error en reasignación"))
            .when(citaService).reasignarDoctor(idCita, nuevoDoctor);

        // Act
        Response response = citaResource.reasignarDoctor(idCita, body);

        // Assert
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertEquals("Error en reasignación", response.getEntity());
        verify(citaService, times(1)).buscarDoctorPorId(idDoctor);
        verify(citaService, times(1)).reasignarDoctor(idCita, nuevoDoctor);
    }

    @Test
    public void testReasignarDoctorConErrorGenerico() throws Exception {
        // Arrange
        Long idCita = 1L;
        Long idDoctor = 2L;
        Doctor nuevoDoctor = new Doctor();
        nuevoDoctor.setIdDoctor(idDoctor);
        
        JsonNode body = objectMapper.createObjectNode()
            .put("idDoctor", idDoctor);
        
        when(citaService.buscarDoctorPorId(idDoctor)).thenReturn(nuevoDoctor);
        doThrow(new RuntimeException("Error interno"))
            .when(citaService).reasignarDoctor(idCita, nuevoDoctor);

        // Act
        Response response = citaResource.reasignarDoctor(idCita, body);

        // Assert
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals("❌ Error en la reasignación", response.getEntity());
        verify(citaService, times(1)).buscarDoctorPorId(idDoctor);
        verify(citaService, times(1)).reasignarDoctor(idCita, nuevoDoctor);
    }

    @Test
    public void testProcesarCitaExitoso() throws Exception {
        // Arrange
        Long idCita = 1L;
        String diagnostico = "Diagnóstico test";
        String resultados = "Resultados test";
        
        JsonNode body = objectMapper.createObjectNode()
            .put("diagnostico", diagnostico)
            .put("resultados", resultados);
        
        doNothing().when(citaService).procesarCitaYEnviarResultados(idCita, diagnostico, resultados);

        // Act
        Response response = citaResource.procesarCita(idCita, body);

        // Assert
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(" Cita procesada y resultados enviados", response.getEntity());
        verify(citaService, times(1)).procesarCitaYEnviarResultados(idCita, diagnostico, resultados);
    }

    @Test
    public void testProcesarCitaNoEncontrada() throws Exception {
        // Arrange
        Long idCita = 1L;
        String diagnostico = "Diagnóstico test";
        String resultados = "Resultados test";
        
        JsonNode body = objectMapper.createObjectNode()
            .put("diagnostico", diagnostico)
            .put("resultados", resultados);
        
        doThrow(new IllegalArgumentException("Cita no encontrada"))
            .when(citaService).procesarCitaYEnviarResultados(idCita, diagnostico, resultados);

        // Act
        Response response = citaResource.procesarCita(idCita, body);

        // Assert
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertEquals("Cita no encontrada", response.getEntity());
        verify(citaService, times(1)).procesarCitaYEnviarResultados(idCita, diagnostico, resultados);
    }

    @Test
    public void testProcesarCitaConErrorGenerico() throws Exception {
        // Arrange
        Long idCita = 1L;
        String diagnostico = "Diagnóstico test";
        String resultados = "Resultados test";
        
        JsonNode body = objectMapper.createObjectNode()
            .put("diagnostico", diagnostico)
            .put("resultados", resultados);
        
        doThrow(new RuntimeException("Error interno"))
            .when(citaService).procesarCitaYEnviarResultados(idCita, diagnostico, resultados);

        // Act
        Response response = citaResource.procesarCita(idCita, body);

        // Assert
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        assertEquals("Error al procesar la cita", response.getEntity());
        verify(citaService, times(1)).procesarCitaYEnviarResultados(idCita, diagnostico, resultados);
    }

    @Test
    public void testRecibirDesdeAseguradoraExitoso() {
        // Arrange
        JsonObject dto = Json.createObjectBuilder()
            .add("documento", "12345678")
            .add("nombre", "Juan")
            .add("apellido", "Pérez")
            .add("fecha", "2024-01-15")
            .add("horaInicio", "09:00")
            .add("horaFin", "10:00")
            .add("motivo", "Consulta general")
            .build();
        
        doNothing().when(citaService).crearCitaDesdeJson(dto);

        // Act
        Response response = citaResource.recibirDesdeAseguradora(dto);

        // Assert
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertEquals("✅ Cita recibida correctamente", response.getEntity());
        verify(citaService, times(1)).crearCitaDesdeJson(dto);
    }

    @Test
    public void testRecibirDesdeAseguradoraConError() {
        // Arrange
        JsonObject dto = Json.createObjectBuilder()
            .add("documento", "12345678")
            .build();
        
        doThrow(new IllegalArgumentException("Error en datos"))
            .when(citaService).crearCitaDesdeJson(dto);

        // Act
        Response response = citaResource.recibirDesdeAseguradora(dto);

        // Assert
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals("Error al guardar cita: Error en datos", response.getEntity());
        verify(citaService, times(1)).crearCitaDesdeJson(dto);
    }

    @Test
    public void testRecibirDesdeAseguradoraConErrorGenerico() {
        // Arrange
        JsonObject dto = Json.createObjectBuilder()
            .add("documento", "12345678")
            .build();
        
        doThrow(new RuntimeException("Error interno"))
            .when(citaService).crearCitaDesdeJson(dto);

        // Act
        Response response = citaResource.recibirDesdeAseguradora(dto);

        // Assert
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals("Error al guardar cita: Error interno", response.getEntity());
        verify(citaService, times(1)).crearCitaDesdeJson(dto);
    }
}
