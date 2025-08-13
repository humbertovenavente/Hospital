package com.unis.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

public class FaqTest {

    private Faq faq;

    @BeforeEach
    void setUp() {
        faq = new Faq();
    }

    @Test
    void testConstructorPorDefecto() {
        assertNotNull(faq);
    }

    @Test
    void testGettersAndSetters() {
        // Arrange
        Long id = 1L;
        String pregunta = "¿Cómo agendo una cita?";
        String respuesta = "Puedes agendar una cita a través de nuestra aplicación web o llamando al hospital.";
        String autor = "Sistema";
        LocalDateTime fechaCreacion = LocalDateTime.now();
        String editadoPor = "admin";
        String status = "PUBLICADO";
        String rejectionReason = null;

        // Act
        faq.setPregunta(pregunta);
        faq.setRespuesta(respuesta);
        faq.setAutor(autor);
        faq.setFechaCreacion(fechaCreacion);
        faq.setEditadoPor(editadoPor);
        faq.setStatus(status);
        faq.setRejectionReason(rejectionReason);

        // Assert
        assertEquals(pregunta, faq.getPregunta());
        assertEquals(respuesta, faq.getRespuesta());
        assertEquals(autor, faq.getAutor());
        assertEquals(fechaCreacion, faq.getFechaCreacion());
        assertEquals(editadoPor, faq.getEditadoPor());
        assertEquals(status, faq.getStatus());
        assertEquals(rejectionReason, faq.getRejectionReason());
    }

    @Test
    void testSetYGetId() {
        // Arrange
        Long id = 999L;

        // Act
        // Note: Faq class doesn't have setId method

        // Assert
        // Note: Faq class doesn't have getId method
    }

    @Test
    void testSetYGetPregunta() {
        // Arrange
        String pregunta = "¿Cuáles son los horarios de atención?";

        // Act
        faq.setPregunta(pregunta);

        // Assert
        assertEquals(pregunta, faq.getPregunta());
    }

    @Test
    void testSetYGetRespuesta() {
        // Arrange
        String respuesta = "Los horarios de atención son de lunes a viernes de 8:00 AM a 6:00 PM.";

        // Act
        faq.setRespuesta(respuesta);

        // Assert
        assertEquals(respuesta, faq.getRespuesta());
    }

    @Test
    void testSetYGetAutor() {
        // Arrange
        String autor = "Dr. García";

        // Act
        faq.setAutor(autor);

        // Assert
        assertEquals(autor, faq.getAutor());
    }

    @Test
    void testSetYGetFechaCreacion() {
        // Arrange
        LocalDateTime fechaCreacion = LocalDateTime.of(2024, 12, 25, 10, 30);

        // Act
        faq.setFechaCreacion(fechaCreacion);

        // Assert
        assertEquals(fechaCreacion, faq.getFechaCreacion());
    }

    @Test
    void testSetYGetEditadoPor() {
        // Arrange
        String editadoPor = "moderador";

        // Act
        faq.setEditadoPor(editadoPor);

        // Assert
        assertEquals(editadoPor, faq.getEditadoPor());
    }

    @Test
    void testSetYGetStatus() {
        // Arrange
        String status = "PROCESO";

        // Act
        faq.setStatus(status);

        // Assert
        assertEquals(status, faq.getStatus());
    }

    @Test
    void testSetYGetRejectionReason() {
        // Arrange
        String rejectionReason = "Contenido inapropiado";

        // Act
        faq.setRejectionReason(rejectionReason);

        // Assert
        assertEquals(rejectionReason, faq.getRejectionReason());
    }

    @Test
    void testSetIdNull() {
        // Act
        // Note: Faq class doesn't have setId method

        // Assert
        // Note: Faq class doesn't have getId method
    }

    @Test
    void testSetPreguntaNull() {
        // Act
        faq.setPregunta(null);

        // Assert
        assertNull(faq.getPregunta());
    }

    @Test
    void testSetRespuestaNull() {
        // Act
        faq.setRespuesta(null);

        // Assert
        assertNull(faq.getRespuesta());
    }

    @Test
    void testSetAutorNull() {
        // Act
        faq.setAutor(null);

        // Assert
        assertNull(faq.getAutor());
    }

    @Test
    void testSetFechaCreacionNull() {
        // Act
        faq.setFechaCreacion(null);

        // Assert
        assertNull(faq.getFechaCreacion());
    }

    @Test
    void testSetEditadoPorNull() {
        // Act
        faq.setEditadoPor(null);

        // Assert
        assertNull(faq.getEditadoPor());
    }

    @Test
    void testSetStatusNull() {
        // Act
        faq.setStatus(null);

        // Assert
        assertNull(faq.getStatus());
    }

    @Test
    void testSetRejectionReasonNull() {
        // Act
        faq.setRejectionReason(null);

        // Assert
        assertNull(faq.getRejectionReason());
    }

    @Test
    void testSetPreguntaVacio() {
        // Arrange
        String preguntaVacia = "";

        // Act
        faq.setPregunta(preguntaVacia);

        // Assert
        assertEquals(preguntaVacia, faq.getPregunta());
        assertTrue(faq.getPregunta().isEmpty());
    }

    @Test
    void testSetRespuestaVacio() {
        // Arrange
        String respuestaVacia = "";

        // Act
        faq.setRespuesta(respuestaVacia);

        // Assert
        assertEquals(respuestaVacia, faq.getRespuesta());
        assertTrue(faq.getRespuesta().isEmpty());
    }

    @Test
    void testSetAutorVacio() {
        // Arrange
        String autorVacio = "";

        // Act
        faq.setAutor(autorVacio);

        // Assert
        assertEquals(autorVacio, faq.getAutor());
        assertTrue(faq.getAutor().isEmpty());
    }

    @Test
    void testSetEditadoPorVacio() {
        // Arrange
        String editadoPorVacio = "";

        // Act
        faq.setEditadoPor(editadoPorVacio);

        // Assert
        assertEquals(editadoPorVacio, faq.getEditadoPor());
        assertTrue(faq.getEditadoPor().isEmpty());
    }

    @Test
    void testSetStatusVacio() {
        // Arrange
        String statusVacio = "";

        // Act
        faq.setStatus(statusVacio);

        // Assert
        assertEquals(statusVacio, faq.getStatus());
        assertTrue(faq.getStatus().isEmpty());
    }

    @Test
    void testSetRejectionReasonVacio() {
        // Arrange
        String rejectionReasonVacio = "";

        // Act
        faq.setRejectionReason(rejectionReasonVacio);

        // Assert
        assertEquals(rejectionReasonVacio, faq.getRejectionReason());
        assertTrue(faq.getRejectionReason().isEmpty());
    }
}
