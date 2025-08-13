package com.unis.controller;

import com.unis.model.Faq;
import com.unis.service.FaqService;
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

public class FaqControllerTest {

    @Mock
    private FaqService faqService;

    @InjectMocks
    private FaqController faqController;

    private Faq testFaq;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        testFaq = new Faq();
        testFaq.setPregunta("¿Cuál es el horario de atención?");
        testFaq.setRespuesta("De 8:00 AM a 4:30 PM");
        testFaq.setAutor("Admin");
        testFaq.setStatus("PROCESO");
        testFaq.setEditadoPor("admin@hospital.com");
    }

    @Test
    void testListarPreguntas() {
        // Arrange
        List<Faq> expectedFaqs = Arrays.asList(testFaq);
        when(faqService.listarPreguntas()).thenReturn(expectedFaqs);

        // Act
        List<Faq> result = faqController.listarPreguntas();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testFaq, result.get(0));
        verify(faqService).listarPreguntas();
    }

    @Test
    void testObtenerFaqPorId_WhenFound() {
        // Arrange
        when(faqService.buscarPorId(1L)).thenReturn(testFaq);

        // Act
        Response response = faqController.obtenerFaqPorId(1L);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatus());
        assertEquals(testFaq, response.getEntity());
        verify(faqService).buscarPorId(1L);
    }

    @Test
    void testObtenerFaqPorId_WhenNotFound() {
        // Arrange
        when(faqService.buscarPorId(999L)).thenReturn(null);

        // Act
        Response response = faqController.obtenerFaqPorId(999L);

        // Assert
        assertNotNull(response);
        assertEquals(404, response.getStatus());
        verify(faqService).buscarPorId(999L);
    }

    @Test
    void testGuardarPregunta_Success() {
        // Act
        Response response = faqController.guardarPregunta(testFaq);

        // Assert
        assertNotNull(response);
        assertEquals(201, response.getStatus());
        assertEquals(testFaq, response.getEntity());
        assertEquals("PROCESO", testFaq.getStatus());
        verify(faqService).guardarPregunta(testFaq);
    }

    @Test
    void testGuardarPregunta_EmptyQuestion() {
        // Arrange
        Faq invalidFaq = new Faq();
        invalidFaq.setPregunta("");
        invalidFaq.setEditadoPor("admin@hospital.com");

        // Act
        Response response = faqController.guardarPregunta(invalidFaq);

        // Assert
        assertNotNull(response);
        assertEquals(400, response.getStatus());
        assertEquals("La pregunta no puede estar vacía.", response.getEntity());
    }

    @Test
    void testGuardarPregunta_NullEditor() {
        // Arrange
        Faq invalidFaq = new Faq();
        invalidFaq.setPregunta("Pregunta válida");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            faqController.guardarPregunta(invalidFaq);
        });
    }

    @Test
    void testEditarPregunta_Success() {
        // Arrange
        Faq faqToUpdate = new Faq();
        faqToUpdate.setPregunta("Pregunta actualizada");
        faqToUpdate.setEditadoPor("editor@hospital.com");
        
        when(faqService.buscarPorId(1L)).thenReturn(testFaq);

        // Act
        Response response = faqController.editarPregunta(1L, faqToUpdate);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatus());
        assertEquals("Pregunta actualizada", testFaq.getPregunta());
        assertEquals("editor@hospital.com", testFaq.getEditadoPor());
        verify(faqService).actualizarFaq(testFaq);
    }

    @Test
    void testEditarPregunta_NotFound() {
        // Arrange
        Faq faqToUpdate = new Faq();
        faqToUpdate.setEditadoPor("editor@hospital.com");
        
        when(faqService.buscarPorId(999L)).thenReturn(null);

        // Act
        Response response = faqController.editarPregunta(999L, faqToUpdate);

        // Assert
        assertNotNull(response);
        assertEquals(404, response.getStatus());
        assertEquals("Pregunta no encontrada", response.getEntity());
    }

    @Test
    void testEditarPregunta_NullEditor() {
        // Arrange
        Faq faqToUpdate = new Faq();
        faqToUpdate.setPregunta("Pregunta actualizada");
        
        when(faqService.buscarPorId(1L)).thenReturn(testFaq);

        // Act
        Response response = faqController.editarPregunta(1L, faqToUpdate);

        // Assert
        assertNotNull(response);
        assertEquals(400, response.getStatus());
        assertEquals("El campo 'editadoPor' es obligatorio.", response.getEntity());
    }

    @Test
    void testEliminarPregunta_Success() {
        // Arrange
        when(faqService.eliminarFaq(1L)).thenReturn(true);

        // Act
        Response response = faqController.eliminarPregunta(1L);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatus());
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
        assertEquals(404, response.getStatus());
        assertEquals("Pregunta no encontrada", response.getEntity());
        verify(faqService).eliminarFaq(999L);
    }

    @Test
    void testListarPendientes() {
        // Arrange
        List<Faq> expectedFaqs = Arrays.asList(testFaq);
        when(faqService.listarPorEstado("PROCESO")).thenReturn(expectedFaqs);

        // Act
        List<Faq> result = faqController.listarPendientes();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(faqService).listarPorEstado("PROCESO");
    }

    @Test
    void testListarPublicadas() {
        // Arrange
        List<Faq> expectedFaqs = Arrays.asList(testFaq);
        when(faqService.listarPorEstado("PUBLICADO")).thenReturn(expectedFaqs);

        // Act
        List<Faq> result = faqController.listarPublicadas();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(faqService).listarPorEstado("PUBLICADO");
    }

    @Test
    void testAprobarPregunta_Success() {
        // Arrange
        when(faqService.buscarPorId(1L)).thenReturn(testFaq);

        // Act
        Response response = faqController.aprobarPregunta(1L);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatus());
        assertEquals("PUBLICADO", testFaq.getStatus());
        assertNull(testFaq.getRejectionReason());
        verify(faqService).actualizarFaq(testFaq);
    }

    @Test
    void testAprobarPregunta_NotFound() {
        // Arrange
        when(faqService.buscarPorId(999L)).thenReturn(null);

        // Act
        Response response = faqController.aprobarPregunta(999L);

        // Assert
        assertNotNull(response);
        assertEquals(404, response.getStatus());
    }

    @Test
    void testRechazarPregunta_Success() {
        // Arrange
        when(faqService.buscarPorId(1L)).thenReturn(testFaq);
        String motivo = "Información incorrecta";

        // Act
        Response response = faqController.rechazarPregunta(1L, motivo);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatus());
        assertEquals("RECHAZADO", testFaq.getStatus());
        assertEquals(motivo, testFaq.getRejectionReason());
        verify(faqService).actualizarFaq(testFaq);
    }

    @Test
    void testRechazarPregunta_NotFound() {
        // Arrange
        when(faqService.buscarPorId(999L)).thenReturn(null);

        // Act
        Response response = faqController.rechazarPregunta(999L, "Motivo");

        // Assert
        assertNotNull(response);
        assertEquals(404, response.getStatus());
    }
}
