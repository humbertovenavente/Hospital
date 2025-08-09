package com.unis.service;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.unis.dto.MedicinasReporteDTO;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * Servicio para generar reportes en formato Excel sobre las medicinas más populares.
 */
@ApplicationScoped
public class ReporteMedicinaExcelService {

    @Inject
    ReporteMedicinaService reporteService;

    /**
     * Genera un reporte en formato Excel con las medicinas más populares.
     *
     * @param inicio  La fecha de inicio del intervalo.
     * @param fin     La fecha de fin del intervalo.
     * @param limite  El número máximo de resultados a incluir.
     * @param usuario El usuario que solicita el reporte.
     * @return Un arreglo de bytes que representa el archivo Excel generado.
     * @throws Exception Si ocurre un error durante la generación del archivo.
     */
    public byte[] generarExcel(Date inicio, Date fin, int limite, String usuario) throws Exception {
        List<MedicinasReporteDTO> datos = reporteService.obtenerReporte(inicio, fin, limite);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Medicinas Populares");

        int rowNum = 0;

        // Encabezado del reporte
        Row row = sheet.createRow(rowNum++);
        row.createCell(0).setCellValue("Fecha de generación:");
        row.createCell(1).setCellValue(sdf.format(new Date()));

        row = sheet.createRow(rowNum++);
        row.createCell(0).setCellValue("Usuario:");
        row.createCell(1).setCellValue(usuario);

        row = sheet.createRow(rowNum++);
        row.createCell(0).setCellValue("Parámetros:");
        row.createCell(1).setCellValue("Inicio: " + sdf.format(inicio) +
                " | Fin: " + sdf.format(fin) +
                " | Límite: " + limite);

        sheet.createRow(rowNum++); // Línea vacía

        // Títulos de columnas
        Row header = sheet.createRow(rowNum++);
        header.createCell(0).setCellValue("Popularidad");
        header.createCell(1).setCellValue("Principio Activo");
        header.createCell(2).setCellValue("Total Recetas");

        // Datos
        for (MedicinasReporteDTO dto : datos) {
            Row dataRow = sheet.createRow(rowNum++);
            dataRow.createCell(0).setCellValue(dto.popularidad);
            dataRow.createCell(1).setCellValue(dto.principioActivo);
            dataRow.createCell(2).setCellValue(dto.totalRecetas);
        }

        // Exportar como byte[]
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();
        return out.toByteArray();
    }
}
