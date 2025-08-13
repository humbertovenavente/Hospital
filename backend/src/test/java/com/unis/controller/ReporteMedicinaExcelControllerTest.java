package com.unis.controller;

import com.unis.service.ReporteMedicinaExcelService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import jakarta.ws.rs.core.Response;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ReporteMedicinaExcelControllerTest {

    @Mock
    private ReporteMedicinaExcelService excelService;

    @InjectMocks
    private ReporteMedicinaExcelController controller;

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
        String usuario = "test@hospital.com";
        byte[] excelData = "excel content".getBytes();
        
        when(excelService.generarExcel(any(), any(), eq(limite), eq(usuario))).thenReturn(excelData);

        // Act
        Response response = controller.descargarExcel(fechaInicio, fechaFin, limite, usuario);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatus());
        assertEquals(excelData, response.getEntity());
        verify(excelService).generarExcel(any(), any(), eq(limite), eq(usuario));
    }

    @Test
    void testDescargarExcelConLimiteDefault() throws Exception {
        // Arrange
        String fechaInicio = "2024-01-01";
        String fechaFin = "2024-01-31";
        String usuario = "admin@hospital.com";
        byte[] excelData = "excel content".getBytes();
        
        when(excelService.generarExcel(any(), any(), eq(10), eq(usuario))).thenReturn(excelData);

        // Act
        Response response = controller.descargarExcel(fechaInicio, fechaFin, 10, usuario);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatus());
        verify(excelService).generarExcel(any(), any(), eq(10), eq(usuario));
    }

    @Test
    void testDescargarExcelFormatoFechaInvalido() throws Exception {
        // Arrange
        String fechaInicio = "fecha-invalida";
        String fechaFin = "2024-01-31";
        int limite = 10;
        String usuario = "test@hospital.com";

        // Act
        Response response = controller.descargarExcel(fechaInicio, fechaFin, limite, usuario);

        // Assert
        assertNotNull(response);
        assertEquals(400, response.getStatus());
        assertEquals("Error generando reporte", response.getEntity());
        verify(excelService, never()).generarExcel(any(), any(), anyInt(), anyString());
    }
}
