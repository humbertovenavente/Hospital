package com.unis.controller;

import com.unis.model.Faq;
import com.unis.service.FaqService;
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
 * Test class for FaqController
 */
public class FaqControllerTest {

    @Mock
    private FaqService faqService;

    @InjectMocks
    private FaqController faqController;

    private Faq testFaq;
    private List<Faq> testFaqs;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        testFaq = new Faq();
        testFaq.setPregunta("¿Cuál es la pregunta de prueba?");
        testFaq.setRespuesta("Esta es la respuesta de prueba");
        testFaq.setAutor("Autor de Prueba");
        testFaq.setEditadoPor("editor@test.com");
        testFaq.setStatus("PROCESO");
        
        testFaqs = Arrays.asList(testFaq);
    }

    @Test
    void testFaqControllerInstantiation() {
        assertNotNull(faqController);
        assertTrue(faqController instanceof FaqController);
    }

    @Test
    void testListarPreguntas_Success() {
        // Arrange
        when(faqService.listarPreguntas()).thenReturn(testFaqs);

        // Act
        List<Faq> result = faqController.listarPreguntas();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testFaq, result.get(0));
        verify(faqService).listarPreguntas();
    }

    @Test
    void testListarPreguntas_EmptyList() {
        // Arrange
        when(faqService.listarPreguntas()).thenReturn(Arrays.asList());

        // Act
        List<Faq> result = faqController.listarPreguntas();

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
        assertTrue(result.isEmpty());
        verify(faqService).listarPreguntas();
    }

    @Test
    void testObtenerFaqPorId_Success() {
        // Arrange
        when(faqService.buscarPorId(1L)).thenReturn(testFaq);

        // Act
        Response response = faqController.obtenerFaqPorId(1L);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(testFaq, response.getEntity());
        verify(faqService).buscarPorId(1L);
    }

    @Test
    void testObtenerFaqPorId_NotFound() {
        // Arrange
        when(faqService.buscarPorId(999L)).thenReturn(null);

        // Act
        Response response = faqController.obtenerFaqPorId(999L);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        verify(faqService).buscarPorId(999L);
    }

    @Test
    void testGuardarPregunta_Success() {
        // Arrange
        Faq nuevaFaq = new Faq();
        nuevaFaq.setPregunta("¿Nueva pregunta?");
        nuevaFaq.setRespuesta("Nueva respuesta");
        nuevaFaq.setEditadoPor("nuevo@test.com");
        
        doNothing().when(faqService).guardarPregunta(any(Faq.class));

        // Act
        Response response = faqController.guardarPregunta(nuevaFaq);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertEquals(nuevaFaq, response.getEntity());
        assertEquals("PROCESO", nuevaFaq.getStatus());
        verify(faqService).guardarPregunta(nuevaFaq);
    }

    @Test
    void testGuardarPregunta_WithoutPregunta() {
        // Arrange
        Faq faqSinPregunta = new Faq();
        faqSinPregunta.setRespuesta("Solo respuesta");
        faqSinPregunta.setEditadoPor("editor@test.com");

        // Act
        Response response = faqController.guardarPregunta(faqSinPregunta);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals("La pregunta no puede estar vacía.", response.getEntity());
        verify(faqService, never()).guardarPregunta(any(Faq.class));
    }

    @Test
    void testGuardarPregunta_WithEmptyPregunta() {
        // Arrange
        Faq faqPreguntaVacia = new Faq();
        faqPreguntaVacia.setPregunta("   ");
        faqPreguntaVacia.setEditadoPor("editor@test.com");

        // Act
        Response response = faqController.guardarPregunta(faqPreguntaVacia);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals("La pregunta no puede estar vacía.", response.getEntity());
        verify(faqService, never()).guardarPregunta(any(Faq.class));
    }

    @Test
    void testGuardarPregunta_WithoutEditadoPor() {
        // Arrange
        Faq faqSinEditor = new Faq();
        faqSinEditor.setPregunta("¿Pregunta válida?");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> faqController.guardarPregunta(faqSinEditor));
        verify(faqService, never()).guardarPregunta(any(Faq.class));
    }

    @Test
    void testEditarPregunta_Success() {
        // Arrange
        Faq faqExistente = new Faq();
        faqExistente.setStatus("PROCESO");
        
        Faq faqActualizada = new Faq();
        faqActualizada.setPregunta("¿Pregunta actualizada?");
        faqActualizada.setRespuesta("Respuesta actualizada");
        faqActualizada.setEditadoPor("actualizado@test.com");
        
        when(faqService.buscarPorId(1L)).thenReturn(faqExistente);
        doNothing().when(faqService).actualizarFaq(any(Faq.class));

        // Act
        Response response = faqController.editarPregunta(1L, faqActualizada);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(faqExistente, response.getEntity());
        assertEquals("¿Pregunta actualizada?", faqExistente.getPregunta());
        assertEquals("Respuesta actualizada", faqExistente.getRespuesta());
        assertEquals("actualizado@test.com", faqExistente.getEditadoPor());
        verify(faqService).buscarPorId(1L);
        verify(faqService).actualizarFaq(faqExistente);
    }

    @Test
    void testEditarPregunta_NotFound() {
        // Arrange
        Faq faqActualizada = new Faq();
        faqActualizada.setEditadoPor("actualizado@test.com");
        
        when(faqService.buscarPorId(999L)).thenReturn(null);

        // Act
        Response response = faqController.editarPregunta(999L, faqActualizada);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertEquals("Pregunta no encontrada", response.getEntity());
        verify(faqService).buscarPorId(999L);
        verify(faqService, never()).actualizarFaq(any(Faq.class));
    }

    @Test
    void testEditarPregunta_WithoutEditadoPor() {
        // Arrange
        Faq faqExistente = new Faq();
        
        Faq faqActualizada = new Faq();
        faqActualizada.setPregunta("¿Pregunta sin editor?");
        
        when(faqService.buscarPorId(1L)).thenReturn(faqExistente);

        // Act
        Response response = faqController.editarPregunta(1L, faqActualizada);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals("El campo 'editadoPor' es obligatorio.", response.getEntity());
        verify(faqService).buscarPorId(1L);
        verify(faqService, never()).actualizarFaq(any(Faq.class));
    }

    @Test
    void testEditarPregunta_WithEmptyEditadoPor() {
        // Arrange
        Faq faqExistente = new Faq();
        
        Faq faqActualizada = new Faq();
        faqActualizada.setPregunta("¿Pregunta con editor vacío?");
        faqActualizada.setEditadoPor("   ");
        
        when(faqService.buscarPorId(1L)).thenReturn(faqExistente);

        // Act
        Response response = faqController.editarPregunta(1L, faqActualizada);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals("El campo 'editadoPor' es obligatorio.", response.getEntity());
        verify(faqService).buscarPorId(1L);
        verify(faqService, never()).actualizarFaq(any(Faq.class));
    }

    @Test
    void testEliminarPregunta_Success() {
        // Arrange
        when(faqService.eliminarFaq(1L)).thenReturn(true);

        // Act
        Response response = faqController.eliminarPregunta(1L);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        verify(faqService).eliminarFaq(1L);
    }

    @Test
    void testEliminarPregunta_NotFound() {
        // Arrange
        when(faqService.eliminarFaq(999L)).thenReturn(false);

        // Act
        Response response = faqController.eliminarPregunta(999L);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertEquals("Pregunta no encontrada", response.getEntity());
        verify(faqService).eliminarFaq(999L);
    }

    @Test
    void testListarPendientes_Success() {
        // Arrange
        when(faqService.listarPorEstado("PROCESO")).thenReturn(testFaqs);

        // Act
        List<Faq> result = faqController.listarPendientes();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testFaq, result.get(0));
        verify(faqService).listarPorEstado("PROCESO");
    }

    @Test
    void testListarPublicadas_Success() {
        // Arrange
        when(faqService.listarPorEstado("PUBLICADO")).thenReturn(testFaqs);

        // Act
        List<Faq> result = faqController.listarPublicadas();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testFaq, result.get(0));
        verify(faqService).listarPorEstado("PUBLICADO");
    }

    @Test
    void testAprobarPregunta_Success() {
        // Arrange
        Faq faq = new Faq();
        faq.setStatus("PROCESO");
        faq.setRejectionReason("Razón anterior");
        
        when(faqService.buscarPorId(1L)).thenReturn(faq);
        doNothing().when(faqService).actualizarFaq(any(Faq.class));

        // Act
        Response response = faqController.aprobarPregunta(1L);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(faq, response.getEntity());
        assertEquals("PUBLICADO", faq.getStatus());
        assertNull(faq.getRejectionReason());
        verify(faqService).buscarPorId(1L);
        verify(faqService).actualizarFaq(faq);
    }

    @Test
    void testAprobarPregunta_NotFound() {
        // Arrange
        when(faqService.buscarPorId(999L)).thenReturn(null);

        // Act
        Response response = faqController.aprobarPregunta(999L);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        verify(faqService).buscarPorId(999L);
        verify(faqService, never()).actualizarFaq(any(Faq.class));
    }

    @Test
    void testRechazarPregunta_Success() {
        // Arrange
        Faq faq = new Faq();
        faq.setStatus("PROCESO");
        
        when(faqService.buscarPorId(1L)).thenReturn(faq);
        doNothing().when(faqService).actualizarFaq(any(Faq.class));

        // Act
        Response response = faqController.rechazarPregunta(1L, "Razón de rechazo");

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(faq, response.getEntity());
        assertEquals("RECHAZADO", faq.getStatus());
        assertEquals("Razón de rechazo", faq.getRejectionReason());
        verify(faqService).buscarPorId(1L);
        verify(faqService).actualizarFaq(faq);
    }

    @Test
    void testRechazarPregunta_NotFound() {
        // Arrange
        when(faqService.buscarPorId(999L)).thenReturn(null);

        // Act
        Response response = faqController.rechazarPregunta(999L, "Razón de rechazo");

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        verify(faqService).buscarPorId(999L);
        verify(faqService, never()).actualizarFaq(any(Faq.class));
    }

    @Test
    void testFaqControllerAnnotations() {
        // Test that the class has the expected annotations
        assertNotNull(FaqController.class.getAnnotation(jakarta.ws.rs.Path.class));
        assertNotNull(FaqController.class.getAnnotation(jakarta.ws.rs.Produces.class));
        assertNotNull(FaqController.class.getAnnotation(jakarta.ws.rs.Consumes.class));
        
        // Test Path annotation value
        jakarta.ws.rs.Path pathAnnotation = FaqController.class.getAnnotation(jakarta.ws.rs.Path.class);
        assertEquals("/faq", pathAnnotation.value());
        
        // Test Produces annotation value
        jakarta.ws.rs.Produces producesAnnotation = FaqController.class.getAnnotation(jakarta.ws.rs.Produces.class);
        assertEquals(1, producesAnnotation.value().length);
        assertEquals("application/json", producesAnnotation.value()[0]);
        
        // Test Consumes annotation value
        jakarta.ws.rs.Consumes consumesAnnotation = FaqController.class.getAnnotation(jakarta.ws.rs.Consumes.class);
        assertEquals(1, consumesAnnotation.value().length);
        assertEquals("application/json", consumesAnnotation.value()[0]);
    }

    @Test
    void testFaqControllerDependencies() {
        // Test that the service dependency is properly injected
        assertNotNull(faqController.faqService);
        assertEquals(faqService, faqController.faqService);
    }

    @Test
    void testRechazarPregunta_WithEmptyMotivo() {
        // Arrange
        when(faqService.buscarPorId(1L)).thenReturn(testFaq);
        doNothing().when(faqService).actualizarFaq(any(Faq.class));

        // Act
        Response response = faqController.rechazarPregunta(1L, "");

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals("RECHAZADO", testFaq.getStatus());
        assertEquals("", testFaq.getRejectionReason());
        verify(faqService).actualizarFaq(testFaq);
    }

    // === TESTS ADICIONALES PARA MEJORAR COBERTURA ===

    @Test
    void testGuardarPregunta_WithNullPregunta() {
        // Arrange
        Faq faqNull = new Faq();
        faqNull.setPregunta(null);
        faqNull.setEditadoPor("editor@test.com");

        // Act
        Response response = faqController.guardarPregunta(faqNull);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals("La pregunta no puede estar vacía.", response.getEntity());
    }

    @Test
    void testGuardarPregunta_WithNullEditadoPor() {
        // Arrange
        Faq faqSinEditor = new Faq();
        faqSinEditor.setPregunta("Pregunta válida");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            faqController.guardarPregunta(faqSinEditor);
        });
    }

    @Test
    void testEditarPregunta_WithNullFaq() {
        // Arrange
        when(faqService.buscarPorId(1L)).thenReturn(testFaq);

        // Act & Assert - El controlador no valida si faqActualizada es null
        assertThrows(NullPointerException.class, () -> {
            faqController.editarPregunta(1L, null);
        });
    }

    @Test
    void testEditarPregunta_WithEmptyPregunta() {
        // Arrange
        when(faqService.buscarPorId(1L)).thenReturn(testFaq);
        Faq faqActualizada = new Faq();
        faqActualizada.setPregunta("   ");
        faqActualizada.setEditadoPor("nuevo@test.com");

        // Act
        Response response = faqController.editarPregunta(1L, faqActualizada);

        // Assert - El controlador no valida si la pregunta está vacía
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        verify(faqService).actualizarFaq(any(Faq.class));
    }

    @Test
    void testEditarPregunta_WithNullEditadoPor() {
        // Arrange
        when(faqService.buscarPorId(1L)).thenReturn(testFaq);
        Faq faqActualizada = new Faq();
        faqActualizada.setPregunta("Pregunta válida");
        faqActualizada.setEditadoPor(null);

        // Act
        Response response = faqController.editarPregunta(1L, faqActualizada);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals("El campo 'editadoPor' es obligatorio.", response.getEntity());
    }

    @Test
    void testAprobarPregunta_WithNullMotivo() {
        // Arrange
        when(faqService.buscarPorId(1L)).thenReturn(testFaq);
        doNothing().when(faqService).actualizarFaq(any(Faq.class));

        // Act
        Response response = faqController.aprobarPregunta(1L);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals("PUBLICADO", testFaq.getStatus());
        assertNull(testFaq.getRejectionReason());
        verify(faqService).actualizarFaq(testFaq);
    }

    @Test
    void testRechazarPregunta_WithNullMotivo() {
        // Arrange
        when(faqService.buscarPorId(1L)).thenReturn(testFaq);
        doNothing().when(faqService).actualizarFaq(any(Faq.class));

        // Act
        Response response = faqController.rechazarPregunta(1L, null);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals("RECHAZADO", testFaq.getStatus());
        assertNull(testFaq.getRejectionReason());
        verify(faqService).actualizarFaq(testFaq);
    }

    @Test
    void testEliminarPregunta_WithException() {
        // Arrange
        when(faqService.buscarPorId(1L)).thenReturn(testFaq);
        when(faqService.eliminarFaq(1L)).thenReturn(false);

        // Act
        Response response = faqController.eliminarPregunta(1L);

        // Assert - El controlador retorna 404 cuando eliminarFaq retorna false
        assertNotNull(response);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertEquals("Pregunta no encontrada", response.getEntity());
    }
}
