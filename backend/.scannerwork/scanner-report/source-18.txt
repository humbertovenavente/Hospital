// ReporteModeracionExcelService
package com.unis.service;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.unis.dto.ModeracionReporteDTO;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * Servicio para generar reportes en formato Excel sobre la moderación de contenido.
 */
@ApplicationScoped
public class ReporteModeracionExcelService {

    @Inject
    ReporteModeracionService service;

    /**
     * Genera un reporte en formato Excel con los usuarios que tienen rechazos en un intervalo de tiempo.
     *
     * @param inicio La fecha de inicio del intervalo.
     * @param fin    La fecha de fin del intervalo.
     * @param limite El número máximo de resultados a incluir.
     * @return Un arreglo de bytes que representa el archivo Excel generado.
     * @throws Exception Si ocurre un error durante la generación del archivo.
     */
    public byte[] generarExcel(Date inicio, Date fin, int limite) throws Exception {
        List<ModeracionReporteDTO> datos = service.obtenerUsuariosConRechazos(inicio, fin, limite);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Usuarios con rechazos");

        int rowNum = 0;

        // Encabezados
        Row meta1 = sheet.createRow(rowNum++);
        meta1.createCell(0).setCellValue("Fecha generación:");
        meta1.createCell(1).setCellValue(sdf.format(new Date()));

        Row meta2 = sheet.createRow(rowNum++);
        meta2.createCell(0).setCellValue("Intervalo:");
        meta2.createCell(1).setCellValue("Inicio: " + sdf.format(inicio) + " | Fin: " + sdf.format(fin));

        Row meta3 = sheet.createRow(rowNum++);
        meta3.createCell(0).setCellValue("Límite:");
        meta3.createCell(1).setCellValue(limite);

        sheet.createRow(rowNum++); // Línea en blanco

        Row header = sheet.createRow(rowNum++);
        header.createCell(0).setCellValue("#");
        header.createCell(1).setCellValue("Usuario");
        header.createCell(2).setCellValue("Total Rechazos");

        for (ModeracionReporteDTO dto : datos) {
            Row fila = sheet.createRow(rowNum++);
            fila.createCell(0).setCellValue(dto.getNumeroOrden());
            fila.createCell(1).setCellValue(dto.getUsuario());
            fila.createCell(2).setCellValue(dto.getTotalRechazos());
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();
        return out.toByteArray();
    }
}
