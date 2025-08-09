package com.unis.service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.unis.dto.ModeracionReporteDTO;

public class ReporteModeracionExcelServiceTest {

    @Mock
    ReporteModeracionService reporteModeracionService;

    @InjectMocks
    ReporteModeracionExcelService excelService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGenerarExcelConDatos() throws Exception {
        // Datos de prueba
        List<ModeracionReporteDTO> datos = Arrays.asList(
            new ModeracionReporteDTO(1, "usuario1@example.com", 3),
            new ModeracionReporteDTO(2, "usuario2@example.com", 5)
        );

        Date inicio = new Date(1700000000000L);
        Date fin = new Date(1705000000000L);
        int limite = 10;

        // Mock del servicio
        when(reporteModeracionService.obtenerUsuariosConRechazos(inicio, fin, limite)).thenReturn(datos);

        // Ejecutar método
        byte[] resultado = excelService.generarExcel(inicio, fin, limite);

        // Verificar
        assertNotNull(resultado, "El archivo generado no debe ser null");
        assertTrue(resultado.length > 0, "El archivo debe tener contenido");
        verify(reporteModeracionService, times(1)).obtenerUsuariosConRechazos(inicio, fin, limite);
    }

    @Test
    public void testGenerarExcelConListaVacia() throws Exception {
        // Lista vacía
        List<ModeracionReporteDTO> datos = List.of();

        Date inicio = new Date(1700000000000L);
        Date fin = new Date(1705000000000L);
        int limite = 10;

        when(reporteModeracionService.obtenerUsuariosConRechazos(inicio, fin, limite)).thenReturn(datos);

        byte[] resultado = excelService.generarExcel(inicio, fin, limite);

        assertNotNull(resultado, "Incluso sin datos, el archivo debe generarse");
        assertTrue(resultado.length > 0, "Debe contener al menos las cabeceras");
        verify(reporteModeracionService, times(1)).obtenerUsuariosConRechazos(inicio, fin, limite);
    }
}
