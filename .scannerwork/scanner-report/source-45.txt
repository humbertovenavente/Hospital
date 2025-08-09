package com.unis.service;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.unis.model.Historia;
import com.unis.repository.HistoriaRepository;

import jakarta.ws.rs.NotFoundException;

public class HistoriaServiceTest {

    @Mock
    HistoriaRepository historiaRepository;

    @InjectMocks
    HistoriaService historiaService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testListar() {
        List<Historia> expected = Arrays.asList(new Historia(), new Historia());
        when(historiaRepository.listAll()).thenReturn(expected);

        List<Historia> result = historiaService.listar();
        assertEquals(expected, result);
    }

    @Test
    public void testObtenerPorIdFound() {
        Historia historia = new Historia();
        when(historiaRepository.findById(1L)).thenReturn(historia);

        Historia result = historiaService.obtenerPorId(1L);
        assertEquals(historia, result);
    }

    @Test
    public void testObtenerPorIdNotFound() {
        when(historiaRepository.findById(1L)).thenReturn(null);

        Historia result = historiaService.obtenerPorId(1L);
        assertNull(result);
    }

    @Test
    public void testCrearSuccess() {
        Historia historia = new Historia();
        historia.setEditorEmail("editor@test.com");

        Historia result = historiaService.crear(historia);

        verify(historiaRepository, times(1)).persist(historia);
        assertEquals("PROCESO", result.getStatus());
    }

    @Test
    public void testCrearSinEditorEmail() {
        Historia historia = new Historia();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            historiaService.crear(historia);
        });

        assertEquals("El email del editor es obligatorio.", exception.getMessage());
    }

    @Test
    public void testActualizarFound() {
        Historia existente = new Historia();
        Historia actualizada = new Historia();
        actualizada.setNombreEntidad("Nueva");
        actualizada.setHistoria("Hist");
        actualizada.setMeritos("Merito");
        actualizada.setLineaDelTiempo("Linea");
        actualizada.setStatus("PROCESO");
        actualizada.setRejectionReason("Ninguna");
        actualizada.setEditorEmail("editor@test.com");

        when(historiaRepository.findById(1L)).thenReturn(existente);

        Historia result = historiaService.actualizar(1L, actualizada);

        assertEquals("Nueva", result.getNombreEntidad());
        assertEquals("Hist", result.getHistoria());
        assertEquals("Merito", result.getMeritos());
        assertEquals("Linea", result.getLineaDelTiempo());
        assertEquals("PROCESO", result.getStatus());
        assertEquals("Ninguna", result.getRejectionReason());
        assertEquals("editor@test.com", result.getEditorEmail());
    }

    @Test
    public void testActualizarNotFound() {
        when(historiaRepository.findById(1L)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> {
            historiaService.actualizar(1L, new Historia());
        });
    }

    @Test
    public void testAprobarSuccess() {
        Historia historia = new Historia();
        historia.setStatus("PROCESO");
        historia.setRejectionReason("motivo anterior");

        when(historiaRepository.findById(1L)).thenReturn(historia);

        Historia result = historiaService.aprobar(1L);

        assertEquals("PUBLICADO", result.getStatus());
        assertNull(result.getRejectionReason());
    }

    @Test
    public void testAprobarNotFound() {
        when(historiaRepository.findById(1L)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> {
            historiaService.aprobar(1L);
        });
    }

    @Test
    public void testRechazarSuccess() {
        Historia historia = new Historia();

        when(historiaRepository.findById(1L)).thenReturn(historia);

        Historia result = historiaService.rechazar(1L, "Motivo de prueba");

        assertEquals("RECHAZADO", result.getStatus());
        assertEquals("Motivo de prueba", result.getRejectionReason());
    }

    @Test
    public void testRechazarNotFound() {
        when(historiaRepository.findById(1L)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> {
            historiaService.rechazar(1L, "Motivo");
        });
    }

    @Test
    public void testEliminarSuccess() {
        when(historiaRepository.deleteById(1L)).thenReturn(true);

        boolean result = historiaService.eliminar(1L);
        assertTrue(result);
    }

    @Test
    public void testEliminarFailure() {
        when(historiaRepository.deleteById(1L)).thenReturn(false);

        boolean result = historiaService.eliminar(1L);
        assertFalse(result);
    }
}
