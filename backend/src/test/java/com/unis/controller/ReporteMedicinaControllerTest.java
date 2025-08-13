package com.unis.controller;

import com.unis.dto.MedicinasReporteDTO;
import com.unis.service.ReporteMedicinaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import jakarta.ws.rs.WebApplicationException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ReporteMedicinaControllerTest {

    @Mock
    private ReporteMedicinaService service;

    @InjectMocks
    private ReporteMedicinaController controller;

    private MedicinasReporteDTO testMedicina;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        testMedicina = new MedicinasReporteDTO(1, "Paracetamol", 100);
    }

    @Test
    void testObtenerReporteExitoso() {
        // Arrange
        String fechaInicio = "2024-01-01";
        String fechaFin = "2024-01-31";
        int limite = 5;
        List<MedicinasReporteDTO> reporteEsperado = Arrays.asList(testMedicina);
        
        when(service.obtenerReporte(any(), any(), eq(limite))).thenReturn(reporteEsperado);

        // Act
        List<MedicinasReporteDTO> resultado = controller.obtener(fechaInicio, fechaFin, limite);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(testMedicina, resultado.get(0));
        verify(service).obtenerReporte(any(), any(), eq(limite));
    }

    @Test
    void testObtenerReporteConLimiteDefault() {
        // Arrange
        String fechaInicio = "2024-01-01";
        String fechaFin = "2024-01-31";
        List<MedicinasReporteDTO> reporteEsperado = Arrays.asList(testMedicina);
        
        when(service.obtenerReporte(any(), any(), eq(10))).thenReturn(reporteEsperado);

        // Act
        List<MedicinasReporteDTO> resultado = controller.obtener(fechaInicio, fechaFin, 10);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(service).obtenerReporte(any(), any(), eq(10)); // Limite default es 10
    }

    @Test
    void testObtenerReporteFormatoFechaInvalido() {
        // Arrange
        String fechaInicio = "fecha-invalida";
        String fechaFin = "2024-01-31";
        int limite = 10;

        // Act & Assert
        WebApplicationException exception = assertThrows(
            WebApplicationException.class,
            () -> controller.obtener(fechaInicio, fechaFin, limite)
        );
        
        assertEquals(400, exception.getResponse().getStatus());
        assertEquals("Formato de fecha inválido", exception.getMessage());
        verify(service, never()).obtenerReporte(any(), any(), anyInt());
    }

    @Test
    void testObtenerReporteConLimitePersonalizado() {
        // Arrange
        String fechaInicio = "2024-01-01";
        String fechaFin = "2024-01-31";
        int limite = 25;
        List<MedicinasReporteDTO> reporteEsperado = Arrays.asList(testMedicina);
        
        when(service.obtenerReporte(any(), any(), eq(limite))).thenReturn(reporteEsperado);

        // Act
        List<MedicinasReporteDTO> resultado = controller.obtener(fechaInicio, fechaFin, limite);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(service).obtenerReporte(any(), any(), eq(limite));
    }

    @Test
    void testObtenerReporteListaVacia() {
        // Arrange
        String fechaInicio = "2024-01-01";
        String fechaFin = "2024-01-31";
        int limite = 10;
        List<MedicinasReporteDTO> reporteVacio = Arrays.asList();
        
        when(service.obtenerReporte(any(), any(), eq(limite))).thenReturn(reporteVacio);

        // Act
        List<MedicinasReporteDTO> resultado = controller.obtener(fechaInicio, fechaFin, limite);

        // Assert
        assertNotNull(resultado);
        assertEquals(0, resultado.size());
        verify(service).obtenerReporte(any(), any(), eq(limite));
    }
}
