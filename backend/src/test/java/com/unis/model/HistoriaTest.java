package com.unis.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class HistoriaTest {

    private Historia historia;

    @BeforeEach
    void setUp() {
        historia = new Historia();
    }

    @Test
    void testConstructorPorDefecto() {
        assertNotNull(historia);
    }

    @Test
    void testGettersAndSetters() {
        // Arrange
        Long id = 1L;
        String nombreEntidad = "Hospital General";
        String historiaTexto = "Fundado en 1950, el Hospital General ha servido a la comunidad por más de 70 años.";
        String meritos = "Acreditado por la OMS, Premio a la Excelencia Médica 2020";
        String lineaDelTiempo = "1950: Fundación, 1980: Primera ampliación, 2020: Renovación completa";
        String status = "PUBLICADO";
        String rejectionReason = null;
        String editorEmail = "editor@hospital.com";

        // Act
        historia.setId(id);
        historia.setNombreEntidad(nombreEntidad);
        historia.setContenidoHistoria(historiaTexto);
        historia.setMeritos(meritos);
        historia.setLineaDelTiempo(lineaDelTiempo);
        historia.setStatus(status);
        historia.setRejectionReason(rejectionReason);
        historia.setEditorEmail(editorEmail);

        // Assert
        assertEquals(id, historia.getId());
        assertEquals(nombreEntidad, historia.getNombreEntidad());
        assertEquals(historiaTexto, historia.getContenidoHistoria());
        assertEquals(meritos, historia.getMeritos());
        assertEquals(lineaDelTiempo, historia.getLineaDelTiempo());
        assertEquals(status, historia.getStatus());
        assertEquals(rejectionReason, historia.getRejectionReason());
        assertEquals(editorEmail, historia.getEditorEmail());
    }

    @Test
    void testSetYGetId() {
        // Arrange
        Long id = 999L;

        // Act
        historia.setId(id);

        // Assert
        assertEquals(id, historia.getId());
    }

    @Test
    void testSetYGetNombreEntidad() {
        // Arrange
        String nombreEntidad = "Clínica Especializada";

        // Act
        historia.setNombreEntidad(nombreEntidad);

        // Assert
        assertEquals(nombreEntidad, historia.getNombreEntidad());
    }

    @Test
    void testSetYGetHistoria() {
        // Arrange
        String historiaTexto = "Esta es la historia de una clínica especializada en cardiología.";

        // Act
        historia.setContenidoHistoria(historiaTexto);

        // Assert
        assertEquals(historiaTexto, historia.getContenidoHistoria());
    }

    @Test
    void testSetYGetMeritos() {
        // Arrange
        String meritos = "Centro de referencia en cardiología, Premio a la Innovación 2021";

        // Act
        historia.setMeritos(meritos);

        // Assert
        assertEquals(meritos, historia.getMeritos());
    }

    @Test
    void testSetYGetLineaDelTiempo() {
        // Arrange
        String lineaDelTiempo = "2000: Fundación, 2010: Primera expansión, 2021: Nueva sede";

        // Act
        historia.setLineaDelTiempo(lineaDelTiempo);

        // Assert
        assertEquals(lineaDelTiempo, historia.getLineaDelTiempo());
    }

    @Test
    void testSetYGetStatus() {
        // Arrange
        String status = "PROCESO";

        // Act
        historia.setStatus(status);

        // Assert
        assertEquals(status, historia.getStatus());
    }

    @Test
    void testSetYGetRejectionReason() {
        // Arrange
        String rejectionReason = "Información no verificada";

        // Act
        historia.setRejectionReason(rejectionReason);

        // Assert
        assertEquals(rejectionReason, historia.getRejectionReason());
    }

    @Test
    void testSetYGetEditorEmail() {
        // Arrange
        String editorEmail = "nuevoeditor@hospital.com";

        // Act
        historia.setEditorEmail(editorEmail);

        // Assert
        assertEquals(editorEmail, historia.getEditorEmail());
    }

    @Test
    void testSetIdNull() {
        // Act
        historia.setId(null);

        // Assert
        assertNull(historia.getId());
    }

    @Test
    void testSetNombreEntidadNull() {
        // Act
        historia.setNombreEntidad(null);

        // Assert
        assertNull(historia.getNombreEntidad());
    }

    @Test
    void testSetHistoriaNull() {
        // Act
        historia.setContenidoHistoria(null);

        // Assert
        assertNull(historia.getContenidoHistoria());
    }

    @Test
    void testSetMeritosNull() {
        // Act
        historia.setMeritos(null);

        // Assert
        assertNull(historia.getMeritos());
    }

    @Test
    void testSetLineaDelTiempoNull() {
        // Act
        historia.setLineaDelTiempo(null);

        // Assert
        assertNull(historia.getLineaDelTiempo());
    }

    @Test
    void testSetStatusNull() {
        // Act
        historia.setStatus(null);

        // Assert
        assertNull(historia.getStatus());
    }

    @Test
    void testSetRejectionReasonNull() {
        // Act
        historia.setRejectionReason(null);

        // Assert
        assertNull(historia.getRejectionReason());
    }

    @Test
    void testSetEditorEmailNull() {
        // Act
        historia.setEditorEmail(null);

        // Assert
        assertNull(historia.getEditorEmail());
    }

    @Test
    void testSetNombreEntidadVacio() {
        // Arrange
        String nombreEntidadVacio = "";

        // Act
        historia.setNombreEntidad(nombreEntidadVacio);

        // Assert
        assertEquals(nombreEntidadVacio, historia.getNombreEntidad());
        assertTrue(historia.getNombreEntidad().isEmpty());
    }

    @Test
    void testSetHistoriaVacio() {
        // Arrange
        String historiaVacio = "";

        // Act
        historia.setContenidoHistoria(historiaVacio);

        // Assert
        assertEquals(historiaVacio, historia.getContenidoHistoria());
        assertTrue(historia.getContenidoHistoria().isEmpty());
    }

    @Test
    void testSetMeritosVacio() {
        // Arrange
        String meritosVacio = "";

        // Act
        historia.setMeritos(meritosVacio);

        // Assert
        assertEquals(meritosVacio, historia.getMeritos());
        assertTrue(historia.getMeritos().isEmpty());
    }

    @Test
    void testSetLineaDelTiempoVacio() {
        // Arrange
        String lineaDelTiempoVacio = "";

        // Act
        historia.setLineaDelTiempo(lineaDelTiempoVacio);

        // Assert
        assertEquals(lineaDelTiempoVacio, historia.getLineaDelTiempo());
        assertTrue(historia.getLineaDelTiempo().isEmpty());
    }

    @Test
    void testSetStatusVacio() {
        // Arrange
        String statusVacio = "";

        // Act
        historia.setStatus(statusVacio);

        // Assert
        assertEquals(statusVacio, historia.getStatus());
        assertTrue(historia.getStatus().isEmpty());
    }

    @Test
    void testSetRejectionReasonVacio() {
        // Arrange
        String rejectionReasonVacio = "";

        // Act
        historia.setRejectionReason(rejectionReasonVacio);

        // Assert
        assertEquals(rejectionReasonVacio, historia.getRejectionReason());
        assertTrue(historia.getRejectionReason().isEmpty());
    }

    @Test
    void testSetEditorEmailVacio() {
        // Arrange
        String editorEmailVacio = "";

        // Act
        historia.setEditorEmail(editorEmailVacio);

        // Assert
        assertEquals(editorEmailVacio, historia.getEditorEmail());
        assertTrue(historia.getEditorEmail().isEmpty());
    }
}
