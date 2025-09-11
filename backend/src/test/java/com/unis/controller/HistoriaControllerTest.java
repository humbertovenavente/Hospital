package com.unis.controller;

import com.unis.model.Historia;
import com.unis.service.HistoriaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import jakarta.ws.rs.core.Response;

/**
 * Test class for HistoriaController
 */
public class HistoriaControllerTest {

    @Mock
    private HistoriaService historiaService;

    @InjectMocks
    private HistoriaController historiaController;

    private Historia testHistoria;
    private List<Historia> testHistorias;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        testHistoria = new Historia();
        testHistoria.setNombreEntidad("Hospital Test");
<<<<<<< HEAD
        testHistoria.setContenidoHistoria("Historia del hospital");
        testHistoria.setMeritos("Méritos del hospital");
        testHistoria.setLineaDelTiempo("Línea del tiempo");
        testHistoria.setStatus("PROCESO");
=======
        testHistoria.setHistoria("Historia de prueba");
>>>>>>> 648dadd5258233ca3ac4f2574bd67d8933f60087
        testHistoria.setEditorEmail("editor@test.com");
        testHistoria.setStatus("PROCESO");
        
        testHistorias = Arrays.asList(testHistoria);
    }

    @Test
    void testHistoriaControllerInstantiation() {
        assertNotNull(historiaController);
        assertTrue(historiaController instanceof HistoriaController);
    }

    @Test
    void testGetHistorias_Success() {
        // Arrange
        when(historiaService.listar()).thenReturn(testHistorias);

        // Act
        List<Historia> result = historiaController.getHistorias();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testHistoria, result.get(0));
        verify(historiaService).listar();
    }

    @Test
    void testGetHistorias_EmptyList() {
        // Arrange
        when(historiaService.listar()).thenReturn(Arrays.asList());

        // Act
        List<Historia> result = historiaController.getHistorias();

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
        assertTrue(result.isEmpty());
        verify(historiaService).listar();
    }

    @Test
    void testGetHistoriasPublicadas_Success() {
        // Arrange
        when(historiaService.listarPorEstado("PUBLICADO")).thenReturn(testHistorias);

        // Act
        List<Historia> result = historiaController.getHistoriasPublicadas();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testHistoria, result.get(0));
        verify(historiaService).listarPorEstado("PUBLICADO");
    }

    @Test
    void testGetHistoria_Success() {
        // Arrange
        when(historiaService.obtenerPorId(1L)).thenReturn(testHistoria);

        // Act
        Response response = historiaController.getHistoria(1L);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(testHistoria, response.getEntity());
        verify(historiaService).obtenerPorId(1L);
    }

    @Test
    void testGetHistoria_NotFound() {
        // Arrange
        when(historiaService.obtenerPorId(999L)).thenReturn(null);

        // Act
        Response response = historiaController.getHistoria(999L);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        verify(historiaService).obtenerPorId(999L);
    }

    @Test
    void testCreateHistoria_Success() {
        // Arrange
        Historia nuevaHistoria = new Historia();
        nuevaHistoria.setEditorEmail("nuevo@test.com");
        nuevaHistoria.setNombreEntidad("Nueva Entidad");
        
        when(historiaService.crear(any(Historia.class))).thenReturn(nuevaHistoria);

        // Act
        Response response = historiaController.createHistoria(nuevaHistoria);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertEquals(nuevaHistoria, response.getEntity());
        assertEquals("PROCESO", nuevaHistoria.getStatus());
        verify(historiaService).crear(nuevaHistoria);
    }

    @Test
    void testCreateHistoria_WithoutEditorEmail() {
        // Arrange
        Historia historiaSinEmail = new Historia();
        historiaSinEmail.setNombreEntidad("Entidad sin email");

        // Act
        Response response = historiaController.createHistoria(historiaSinEmail);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals("El correo del editor (editorEmail) es requerido.", response.getEntity());
        verify(historiaService, never()).crear(any(Historia.class));
    }

    @Test
    void testCreateHistoria_WithEmptyEditorEmail() {
        // Arrange
        Historia historiaEmailVacio = new Historia();
        historiaEmailVacio.setEditorEmail("   ");
        historiaEmailVacio.setNombreEntidad("Entidad con email vacío");

        // Act
        Response response = historiaController.createHistoria(historiaEmailVacio);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals("El correo del editor (editorEmail) es requerido.", response.getEntity());
        verify(historiaService, never()).crear(any(Historia.class));
    }

    @Test
    void testUpdateHistoria_Success() {
        // Arrange
        Historia historiaExistente = new Historia();
        historiaExistente.setStatus("PROCESO");
        
        Historia historiaActualizada = new Historia();
        historiaActualizada.setEditorEmail("actualizado@test.com");
        historiaActualizada.setNombreEntidad("Entidad Actualizada");
        historiaActualizada.setHistoria("Nueva historia");
        
        when(historiaService.obtenerPorId(1L)).thenReturn(historiaExistente);
        when(historiaService.actualizar(eq(1L), any(Historia.class))).thenReturn(historiaExistente);

        // Act
        Response response = historiaController.updateHistoria(1L, historiaActualizada);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(historiaExistente, response.getEntity());
        assertEquals("Entidad Actualizada", historiaExistente.getNombreEntidad());
        assertEquals("Nueva historia", historiaExistente.getHistoria());
        assertEquals("actualizado@test.com", historiaExistente.getEditorEmail());
        verify(historiaService).obtenerPorId(1L);
        verify(historiaService).actualizar(eq(1L), eq(historiaExistente));
    }

    @Test
    void testUpdateHistoria_NotFound() {
        // Arrange
        Historia historiaActualizada = new Historia();
        historiaActualizada.setEditorEmail("actualizado@test.com");
        
        when(historiaService.obtenerPorId(999L)).thenReturn(null);

        // Act
        Response response = historiaController.updateHistoria(999L, historiaActualizada);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        verify(historiaService).obtenerPorId(999L);
        verify(historiaService, never()).actualizar(anyLong(), any(Historia.class));
    }

    @Test
    void testUpdateHistoria_WithoutEditorEmail() {
        // Arrange
        Historia historiaExistente = new Historia();
        
        Historia historiaActualizada = new Historia();
        historiaActualizada.setNombreEntidad("Entidad sin email");
        
        when(historiaService.obtenerPorId(1L)).thenReturn(historiaExistente);

        // Act
        Response response = historiaController.updateHistoria(1L, historiaActualizada);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals("El correo del editor (editorEmail) es requerido.", response.getEntity());
        verify(historiaService).obtenerPorId(1L);
        verify(historiaService, never()).actualizar(anyLong(), any(Historia.class));
    }

    @Test
    void testDeleteHistoria_Success() {
        // Arrange
        when(historiaService.eliminar(1L)).thenReturn(true);

        // Act
        Response response = historiaController.deleteHistoria(1L);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
        verify(historiaService).eliminar(1L);
    }

    @Test
    void testDeleteHistoria_NotFound() {
        // Arrange
        when(historiaService.eliminar(999L)).thenReturn(false);

        // Act
        Response response = historiaController.deleteHistoria(999L);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        verify(historiaService).eliminar(999L);
    }

    @Test
    void testGetPendientesModeracion_Success() {
        // Arrange
        when(historiaService.listarPorEstado("PROCESO")).thenReturn(testHistorias);

        // Act
        List<Historia> result = historiaController.getPendientesModeracion();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testHistoria, result.get(0));
        verify(historiaService).listarPorEstado("PROCESO");
    }

    @Test
    void testAprobarHistoria_Success() {
        // Arrange
        Historia historia = new Historia();
        historia.setStatus("PROCESO");
        historia.setRejectionReason("Razón anterior");
        
        when(historiaService.obtenerPorId(1L)).thenReturn(historia);
        when(historiaService.actualizar(eq(1L), any(Historia.class))).thenReturn(historia);

        // Act
        Response response = historiaController.aprobarHistoria(1L);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(historia, response.getEntity());
        assertEquals("PUBLICADO", historia.getStatus());
        assertNull(historia.getRejectionReason());
        verify(historiaService).obtenerPorId(1L);
        verify(historiaService).actualizar(eq(1L), eq(historia));
    }

    @Test
    void testAprobarHistoria_NotFound() {
        // Arrange
        when(historiaService.obtenerPorId(999L)).thenReturn(null);

        // Act
        Response response = historiaController.aprobarHistoria(999L);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        verify(historiaService).obtenerPorId(999L);
        verify(historiaService, never()).actualizar(anyLong(), any(Historia.class));
    }

    @Test
    void testRechazarHistoria_Success() {
        // Arrange
        Historia historia = new Historia();
        historia.setStatus("PROCESO");
        
        when(historiaService.obtenerPorId(1L)).thenReturn(historia);
        when(historiaService.actualizar(eq(1L), any(Historia.class))).thenReturn(historia);

        // Act
        Response response = historiaController.rechazarHistoria(1L, "Razón de rechazo");

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(historia, response.getEntity());
        assertEquals("RECHAZADO", historia.getStatus());
        assertEquals("Razón de rechazo", historia.getRejectionReason());
        verify(historiaService).obtenerPorId(1L);
        verify(historiaService).actualizar(eq(1L), eq(historia));
    }

    @Test
    void testRechazarHistoria_NotFound() {
        // Arrange
        when(historiaService.obtenerPorId(999L)).thenReturn(null);

        // Act
        Response response = historiaController.rechazarHistoria(999L, "Razón de rechazo");

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        verify(historiaService).obtenerPorId(999L);
        verify(historiaService, never()).actualizar(anyLong(), any(Historia.class));
    }

    @Test
    void testRechazarHistoria_WithEmptyMotivo() {
        // Arrange
        when(historiaService.obtenerPorId(1L)).thenReturn(testHistoria);
        when(historiaService.actualizar(eq(1L), any(Historia.class))).thenReturn(testHistoria);

        // Act
        Response response = historiaController.rechazarHistoria(1L, "");

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals("RECHAZADO", testHistoria.getStatus());
        assertEquals("", testHistoria.getRejectionReason());
        verify(historiaService).actualizar(eq(1L), any(Historia.class));
    }

    // === TESTS ADICIONALES PARA MEJORAR COBERTURA ===

    @Test
    void testCreateHistoria_WithNullEditorEmail() {
        // Arrange
        Historia historiaSinEditor = new Historia();
        historiaSinEditor.setNombreEntidad("Hospital Test");
        historiaSinEditor.setHistoria("Historia de prueba");
        historiaSinEditor.setEditorEmail(null);

        // Act
        Response response = historiaController.createHistoria(historiaSinEditor);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals("El correo del editor (editorEmail) es requerido.", response.getEntity());
    }

    @Test
    void testUpdateHistoria_WithNullHistoria() {
        // Arrange
        when(historiaService.obtenerPorId(1L)).thenReturn(testHistoria);

        // Act & Assert - El controlador no valida si historiaActualizada es null
        assertThrows(NullPointerException.class, () -> {
            historiaController.updateHistoria(1L, null);
        });
    }

    @Test
    void testUpdateHistoria_WithNullEditorEmail() {
        // Arrange
        when(historiaService.obtenerPorId(1L)).thenReturn(testHistoria);
        Historia historiaActualizada = new Historia();
        historiaActualizada.setNombreEntidad("Hospital Actualizado");
        historiaActualizada.setHistoria("Historia actualizada");
        historiaActualizada.setEditorEmail(null);

        // Act
        Response response = historiaController.updateHistoria(1L, historiaActualizada);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals("El correo del editor (editorEmail) es requerido.", response.getEntity());
    }

    @Test
    void testUpdateHistoria_WithEmptyEditorEmail() {
        // Arrange
        when(historiaService.obtenerPorId(1L)).thenReturn(testHistoria);
        Historia historiaActualizada = new Historia();
        historiaActualizada.setNombreEntidad("Hospital Actualizado");
        historiaActualizada.setHistoria("Historia actualizada");
        historiaActualizada.setEditorEmail("   ");

        // Act
        Response response = historiaController.updateHistoria(1L, historiaActualizada);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals("El correo del editor (editorEmail) es requerido.", response.getEntity());
    }

    @Test
    void testAprobarHistoria_WithNullMotivo() {
        // Arrange
        when(historiaService.obtenerPorId(1L)).thenReturn(testHistoria);
        when(historiaService.actualizar(eq(1L), any(Historia.class))).thenReturn(testHistoria);

        // Act
        Response response = historiaController.aprobarHistoria(1L);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals("PUBLICADO", testHistoria.getStatus());
        assertNull(testHistoria.getRejectionReason());
        verify(historiaService).actualizar(eq(1L), any(Historia.class));
    }

    @Test
    void testRechazarHistoria_WithNullMotivo() {
        // Arrange
        when(historiaService.obtenerPorId(1L)).thenReturn(testHistoria);
        when(historiaService.actualizar(eq(1L), any(Historia.class))).thenReturn(testHistoria);

        // Act
        Response response = historiaController.rechazarHistoria(1L, null);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals("RECHAZADO", testHistoria.getStatus());
        assertNull(testHistoria.getRejectionReason());
        verify(historiaService).actualizar(eq(1L), any(Historia.class));
    }

    @Test
    void testDeleteHistoria_WithException() {
        // Arrange
        when(historiaService.obtenerPorId(1L)).thenReturn(testHistoria);
        when(historiaService.eliminar(1L)).thenReturn(false);

        // Act
        Response response = historiaController.deleteHistoria(1L);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        // El controlador no retorna mensaje de error, solo el status
        verify(historiaService).eliminar(1L);
    }

    @Test
    void testHistoriaControllerAnnotations() {
        // Test that the class has the expected annotations
        assertNotNull(HistoriaController.class.getAnnotation(jakarta.ws.rs.Path.class));
        assertNotNull(HistoriaController.class.getAnnotation(jakarta.ws.rs.Produces.class));
        assertNotNull(HistoriaController.class.getAnnotation(jakarta.ws.rs.Consumes.class));
        
        // Test Path annotation value
        jakarta.ws.rs.Path pathAnnotation = HistoriaController.class.getAnnotation(jakarta.ws.rs.Path.class);
        assertEquals("/historias", pathAnnotation.value());
        
        // Test Produces annotation value
        jakarta.ws.rs.Produces producesAnnotation = HistoriaController.class.getAnnotation(jakarta.ws.rs.Produces.class);
        assertEquals(1, producesAnnotation.value().length);
        assertEquals("application/json", producesAnnotation.value()[0]);
        
        // Test Consumes annotation value
        jakarta.ws.rs.Consumes consumesAnnotation = HistoriaController.class.getAnnotation(jakarta.ws.rs.Consumes.class);
        assertEquals(1, consumesAnnotation.value().length);
        assertEquals("application/json", consumesAnnotation.value()[0]);
    }

    @Test
    void testHistoriaControllerDependencies() {
        // Test that the service dependency is properly injected
        assertNotNull(historiaController.historiaService);
        assertEquals(historiaService, historiaController.historiaService);
    }
}

