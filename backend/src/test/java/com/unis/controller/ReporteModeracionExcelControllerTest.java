package com.unis.controller;

import com.unis.service.ReporteModeracionExcelService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import jakarta.ws.rs.core.Response;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ReporteModeracionExcelControllerTest {

    @Mock
    private ReporteModeracionExcelService excelService;

    @InjectMocks
    private ReporteModeracionExcelController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDescargarExcelExitoso() throws Exception {
        // Arrange
        String fechaInicio = "2024-01-01";
        String fechaFin = "2024-01-31";
        int limite = 5;
        byte[] excelData = "excel content".getBytes();
        
        when(excelService.generarExcel(any(), any(), eq(limite))).thenReturn(excelData);

        // Act
        Response response = controller.descargarExcel(fechaInicio, fechaFin, limite);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatus());
        assertEquals(excelData, response.getEntity());
        verify(excelService).generarExcel(any(), any(), eq(limite));
    }

    @Test
    void testDescargarExcelConLimiteDefault() throws Exception {
        // Arrange
        String fechaInicio = "2024-01-01";
        String fechaFin = "2024-01-31";
        byte[] excelData = "excel content".getBytes();
        
        when(excelService.generarExcel(any(), any(), eq(10))).thenReturn(excelData);

        // Act
        Response response = controller.descargarExcel(fechaInicio, fechaFin, 10);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatus());
        verify(excelService).generarExcel(any(), any(), eq(10));
    }

    @Test
    void testDescargarExcelFormatoFechaInvalido() throws Exception {
        // Arrange
        String fechaInicio = "fecha-invalida";
        String fechaFin = "2024-01-31";
        int limite = 10;

        // Act
        Response response = controller.descargarExcel(fechaInicio, fechaFin, limite);

        // Assert
        assertNotNull(response);
        assertEquals(400, response.getStatus());
        assertEquals("Error generando reporte", response.getEntity());
        verify(excelService, never()).generarExcel(any(), any(), anyInt());
    }
}
