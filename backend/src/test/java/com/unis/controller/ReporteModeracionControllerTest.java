package com.unis.controller;

import com.unis.dto.ModeracionReporteDTO;
import com.unis.service.ReporteModeracionService;
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

public class ReporteModeracionControllerTest {

    @Mock
    private ReporteModeracionService service;

    @InjectMocks
    private ReporteModeracionController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testObtenerReporteExitoso() {
        // Arrange
        String fechaInicio = "2024-01-01";
        String fechaFin = "2024-01-31";
        int limite = 5;
        List<ModeracionReporteDTO> reporteEsperado = Arrays.asList(new ModeracionReporteDTO());
        
        when(service.obtenerUsuariosConRechazos(any(), any(), eq(limite))).thenReturn(reporteEsperado);

        // Act
        List<ModeracionReporteDTO> resultado = controller.obtener(fechaInicio, fechaFin, limite);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(service).obtenerUsuariosConRechazos(any(), any(), eq(limite));
    }

    @Test
    void testObtenerReporteConLimiteDefault() {
        // Arrange
        String fechaInicio = "2024-01-01";
        String fechaFin = "2024-01-31";
        List<ModeracionReporteDTO> reporteEsperado = Arrays.asList(new ModeracionReporteDTO());
        
        when(service.obtenerUsuariosConRechazos(any(), any(), eq(10))).thenReturn(reporteEsperado);

        // Act
        List<ModeracionReporteDTO> resultado = controller.obtener(fechaInicio, fechaFin, 10);

        // Assert
        assertNotNull(resultado);
        verify(service).obtenerUsuariosConRechazos(any(), any(), eq(10));
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
        verify(service, never()).obtenerUsuariosConRechazos(any(), any(), anyInt());
    }
}

