package com.unis.service;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.unis.dto.MedicinasReporteDTO;

public class ReporteMedicinaExcelServiceTest {

    @Mock
    ReporteMedicinaService reporteService;

    @InjectMocks
    ReporteMedicinaExcelService excelService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGenerarExcel() throws Exception {
        // Datos de entrada
        Date inicio = new Date(System.currentTimeMillis() - 86400000); // ayer
        Date fin = new Date(); // hoy
        int limite = 5;
        String usuario = "admin@hospital.com";

        // Datos simulados
        List<MedicinasReporteDTO> mockDatos = Arrays.asList(
            new MedicinasReporteDTO(1, "Paracetamol", 50),
            new MedicinasReporteDTO(2, "Ibuprofeno", 30)
        );

        // Configurar mock
        when(reporteService.obtenerReporte(inicio, fin, limite)).thenReturn(mockDatos);

        // Ejecutar
        byte[] excelBytes = excelService.generarExcel(inicio, fin, limite, usuario);

        // Verificar que el Excel no sea nulo ni vacío
        assertNotNull(excelBytes);
        assertTrue(excelBytes.length > 0);

        // Validar contenido del Excel
        try (Workbook workbook = new XSSFWorkbook(new ByteArrayInputStream(excelBytes))) {
            Sheet sheet = workbook.getSheetAt(0);
            assertNotNull(sheet);

            // Verificamos encabezados de columnas
            Row header = sheet.getRow(4); // porque hay 3 filas arriba + 1 vacía
            assertEquals("Popularidad", header.getCell(0).getStringCellValue());
            assertEquals("Principio Activo", header.getCell(1).getStringCellValue());
            assertEquals("Total Recetas", header.getCell(2).getStringCellValue());

            // Verificamos primera fila de datos
            Row row1 = sheet.getRow(5);
            assertEquals(1, (int) row1.getCell(0).getNumericCellValue());
            assertEquals("Paracetamol", row1.getCell(1).getStringCellValue());
            assertEquals(50, (int) row1.getCell(2).getNumericCellValue());

            // Verificamos segunda fila de datos
            Row row2 = sheet.getRow(6);
            assertEquals(2, (int) row2.getCell(0).getNumericCellValue());
            assertEquals("Ibuprofeno", row2.getCell(1).getStringCellValue());
            assertEquals(30, (int) row2.getCell(2).getNumericCellValue());
        }
    }
}
