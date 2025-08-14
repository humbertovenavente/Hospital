package com.unis.controller;

import com.unis.model.Historia;
import com.unis.service.HistoriaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import jakarta.ws.rs.core.Response;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class HistoriaControllerTest {

    @Mock
    private HistoriaService historiaService;

    @InjectMocks
    private HistoriaController historiaController;

    private Historia testHistoria;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        testHistoria = new Historia();
        testHistoria.setId(1L);
        testHistoria.setNombreEntidad("Hospital Test");
        testHistoria.setContenidoHistoria("Historia del hospital");
        testHistoria.setMeritos("Méritos del hospital");
        testHistoria.setLineaDelTiempo("Línea del tiempo");
        testHistoria.setStatus("PROCESO");
        testHistoria.setEditorEmail("editor@test.com");
    }

    @Test
    void testGetHistorias() {
        // Arrange
        List<Historia> historiasEsperadas = Arrays.asList(testHistoria);
        when(historiaService.listar()).thenReturn(historiasEsperadas);

        // Act
        List<Historia> resultado = historiaController.getHistorias();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(historiaService).listar();
    }

    @Test
    void testGetHistoriasPublicadas() {
        // Arrange
        List<Historia> historiasPublicadas = Arrays.asList(testHistoria);
        when(historiaService.listarPorEstado("PUBLICADO")).thenReturn(historiasPublicadas);

        // Act
        List<Historia> resultado = historiaController.getHistoriasPublicadas();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(historiaService).listarPorEstado("PUBLICADO");
    }

    @Test
    void testGetHistoriaExitoso() {
        // Arrange
        Long id = 1L;
        when(historiaService.obtenerPorId(id)).thenReturn(testHistoria);

        // Act
        Response response = historiaController.getHistoria(id);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatus());
        assertEquals(testHistoria, response.getEntity());
        verify(historiaService).obtenerPorId(id);
    }

    @Test
    void testGetHistoriaNoEncontrada() {
        // Arrange
        Long id = 999L;
        when(historiaService.obtenerPorId(id)).thenReturn(null);

        // Act
        Response response = historiaController.getHistoria(id);

        // Assert
        assertNotNull(response);
        assertEquals(404, response.getStatus());
        verify(historiaService).obtenerPorId(id);
    }

    @Test
    void testCreateHistoriaExitoso() {
        // Arrange
        Historia nuevaHistoria = new Historia();
        nuevaHistoria.setNombreEntidad("Nueva Historia");
        nuevaHistoria.setEditorEmail("nuevo@test.com");
        
        when(historiaService.crear(any(Historia.class))).thenReturn(nuevaHistoria);

        // Act
        Response response = historiaController.createHistoria(nuevaHistoria);

        // Assert
        assertNotNull(response);
        assertEquals(201, response.getStatus());
        assertEquals("PROCESO", nuevaHistoria.getStatus());
        verify(historiaService).crear(nuevaHistoria);
    }

    @Test
    void testCreateHistoriaSinEditorEmail() {
        // Arrange
        Historia nuevaHistoria = new Historia();
        nuevaHistoria.setNombreEntidad("Nueva Historia");
        // Sin editorEmail

        // Act
        Response response = historiaController.createHistoria(nuevaHistoria);

        // Assert
        assertNotNull(response);
        assertEquals(400, response.getStatus());
        assertEquals("El correo del editor (editorEmail) es requerido.", response.getEntity());
        verify(historiaService, never()).crear(any());
    }

    @Test
    void testCreateHistoriaEditorEmailVacio() {
        // Arrange
        Historia nuevaHistoria = new Historia();
        nuevaHistoria.setNombreEntidad("Nueva Historia");
        nuevaHistoria.setEditorEmail("   "); // Solo espacios

        // Act
        Response response = historiaController.createHistoria(nuevaHistoria);

        // Assert
        assertNotNull(response);
        assertEquals(400, response.getStatus());
        assertEquals("El correo del editor (editorEmail) es requerido.", response.getEntity());
        verify(historiaService, never()).crear(any());
    }

    @Test
    void testUpdateHistoriaExitoso() {
        // Arrange
        Long id = 1L;
        Historia historiaActualizada = new Historia();
        historiaActualizada.setNombreEntidad("Historia Actualizada");
        historiaActualizada.setEditorEmail("actualizado@test.com");
        
        when(historiaService.obtenerPorId(id)).thenReturn(testHistoria);
        when(historiaService.actualizar(eq(id), any(Historia.class))).thenReturn(testHistoria);

        // Act
        Response response = historiaController.updateHistoria(id, historiaActualizada);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatus());
        verify(historiaService).obtenerPorId(id);
        verify(historiaService).actualizar(eq(id), any(Historia.class));
    }

    @Test
    void testUpdateHistoriaNoEncontrada() {
        // Arrange
        Long id = 999L;
        Historia historiaActualizada = new Historia();
        historiaActualizada.setEditorEmail("actualizado@test.com");
        
        when(historiaService.obtenerPorId(id)).thenReturn(null);

        // Act
        Response response = historiaController.updateHistoria(id, historiaActualizada);

        // Assert
        assertNotNull(response);
        assertEquals(404, response.getStatus());
        verify(historiaService).obtenerPorId(id);
        verify(historiaService, never()).actualizar(any(), any());
    }

    @Test
    void testUpdateHistoriaSinEditorEmail() {
        // Arrange
        Long id = 1L;
        Historia historiaActualizada = new Historia();
        historiaActualizada.setNombreEntidad("Historia Actualizada");
        // Sin editorEmail
        
        when(historiaService.obtenerPorId(id)).thenReturn(testHistoria);

        // Act
        Response response = historiaController.updateHistoria(id, historiaActualizada);

        // Assert
        assertNotNull(response);
        assertEquals(400, response.getStatus());
        assertEquals("El correo del editor (editorEmail) es requerido.", response.getEntity());
        verify(historiaService).obtenerPorId(id);
        verify(historiaService, never()).actualizar(any(), any());
    }

    @Test
    void testDeleteHistoriaExitoso() {
        // Arrange
        Long id = 1L;
        when(historiaService.eliminar(id)).thenReturn(true);

        // Act
        Response response = historiaController.deleteHistoria(id);

        // Assert
        assertNotNull(response);
        assertEquals(204, response.getStatus());
        verify(historiaService).eliminar(id);
    }

    @Test
    void testDeleteHistoriaNoEncontrada() {
        // Arrange
        Long id = 999L;
        when(historiaService.eliminar(id)).thenReturn(false);

        // Act
        Response response = historiaController.deleteHistoria(id);

        // Assert
        assertNotNull(response);
        assertEquals(404, response.getStatus());
        verify(historiaService).eliminar(id);
    }

    @Test
    void testGetPendientesModeracion() {
        // Arrange
        List<Historia> historiasPendientes = Arrays.asList(testHistoria);
        when(historiaService.listarPorEstado("PROCESO")).thenReturn(historiasPendientes);

        // Act
        List<Historia> resultado = historiaController.getPendientesModeracion();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(historiaService).listarPorEstado("PROCESO");
    }

    @Test
    void testAprobarHistoriaExitoso() {
        // Arrange
        Long id = 1L;
        when(historiaService.obtenerPorId(id)).thenReturn(testHistoria);
        when(historiaService.actualizar(eq(id), any(Historia.class))).thenReturn(testHistoria);

        // Act
        Response response = historiaController.aprobarHistoria(id);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatus());
        assertEquals("PUBLICADO", testHistoria.getStatus());
        assertNull(testHistoria.getRejectionReason());
        verify(historiaService).obtenerPorId(id);
        verify(historiaService).actualizar(eq(id), any(Historia.class));
    }

    @Test
    void testAprobarHistoriaNoEncontrada() {
        // Arrange
        Long id = 999L;
        when(historiaService.obtenerPorId(id)).thenReturn(null);

        // Act
        Response response = historiaController.aprobarHistoria(id);

        // Assert
        assertNotNull(response);
        assertEquals(404, response.getStatus());
        verify(historiaService).obtenerPorId(id);
        verify(historiaService, never()).actualizar(any(), any());
    }

    @Test
    void testRechazarHistoriaExitoso() {
        // Arrange
        Long id = 1L;
        String motivo = "Motivo de rechazo";
        when(historiaService.obtenerPorId(id)).thenReturn(testHistoria);
        when(historiaService.actualizar(eq(id), any(Historia.class))).thenReturn(testHistoria);

        // Act
        Response response = historiaController.rechazarHistoria(id, motivo);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatus());
        assertEquals("RECHAZADO", testHistoria.getStatus());
        assertEquals(motivo, testHistoria.getRejectionReason());
        verify(historiaService).obtenerPorId(id);
        verify(historiaService).actualizar(eq(id), any(Historia.class));
    }

    @Test
    void testRechazarHistoriaNoEncontrada() {
        // Arrange
        Long id = 999L;
        String motivo = "Motivo de rechazo";
        when(historiaService.obtenerPorId(id)).thenReturn(null);

        // Act
        Response response = historiaController.rechazarHistoria(id, motivo);

        // Assert
        assertNotNull(response);
        assertEquals(404, response.getStatus());
        verify(historiaService).obtenerPorId(id);
        verify(historiaService, never()).actualizar(any(), any());
    }

    @Test
    void testUpdateHistoriaConCamposNulos() {
        // Arrange
        Long id = 1L;
        Historia historiaActualizada = new Historia();
        historiaActualizada.setEditorEmail("actualizado@test.com");
        // Solo editorEmail, otros campos son null
        
        when(historiaService.obtenerPorId(id)).thenReturn(testHistoria);
        when(historiaService.actualizar(eq(id), any(Historia.class))).thenReturn(testHistoria);

        // Act
        Response response = historiaController.updateHistoria(id, historiaActualizada);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatus());
        verify(historiaService).obtenerPorId(id);
        verify(historiaService).actualizar(eq(id), any(Historia.class));
    }
}

