package com.unis.resource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.unis.dto.ReporteAgregadoDTO;
import com.unis.dto.ReporteDetalladoDTO;
import com.unis.dto.ReporteRequest;
import com.unis.dto.ReporteResponse;
import com.unis.model.Doctor;
import com.unis.service.DoctorService;
import com.unis.service.ReporteService;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.StreamingOutput;

/**
 * REST resource for generating and exporting reports.
 */
@Path("/api/reportes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReporteResource {

    @Inject
    ReporteService reporteService;

    @Inject
    DoctorService doctorService;

    /**
     * Generates a report in JSON format based on the provided request parameters.
     *
     * @param request the report request containing parameters
     * @return a response containing the generated report
     */
    @POST
    @Path("/consultas")
    public Response generarReporte(ReporteRequest request) {

        // 1) Validación de parámetros
        if (request.getIdDoctor() == null
         || request.getFechaInicio() == null
         || request.getFechaFin() == null
         || request.getFechaInicio().isAfter(request.getFechaFin())) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("Parámetros inválidos")
                           .build();
        }

        // 2) Tomar usuario desde el cuerpo
        String usuario = request.getUsuario() != null
                ? request.getUsuario()
                : "[Anónimo]";

        // 3) Obtener nombre del doctor
        String doctorTxt = "Doctor ID = " + request.getIdDoctor();
        Optional<Doctor> optDoc = doctorService.getDoctorById(request.getIdDoctor());
        if (optDoc.isPresent()) {
            Doctor d = optDoc.get();
            String nombreDoc;
            if (d.getUsuario() != null && d.getUsuario().getNombreUsuario() != null) {
                nombreDoc = d.getUsuario().getNombreUsuario();
            } else if (d.getApellido() != null) {
                nombreDoc = d.getApellido();
            } else {
                nombreDoc = "[Desconocido]";
            }
            doctorTxt = "Doctor: " + nombreDoc;
        }

        // 4) Construir encabezado
        String encabezado = "Reporte generado el: " + LocalDateTime.now() + "\n" +
                            "Usuario: " + usuario + "\n" +
                            "Parámetros: " + doctorTxt +
                            ", Fecha Inicio = " + request.getFechaInicio() +
                            ", Fecha Fin = " + request.getFechaFin() +
                            ", Tipo Reporte = " + request.getTipoReporte() + "\n";

        // 5) Generar datos y devolver JSON
        if ("AGRUPADO".equalsIgnoreCase(request.getTipoReporte())) {
            List<ReporteAgregadoDTO> datos = reporteService.obtenerReporteAgregado(
                    request.getIdDoctor(),
                    request.getFechaInicio(),
                    request.getFechaFin());
            return Response.ok(new ReporteResponse<>(encabezado, datos)).build();
        } else {
            List<ReporteDetalladoDTO> datos = reporteService.obtenerReporteDetallado(
                    request.getIdDoctor(),
                    request.getFechaInicio(),
                    request.getFechaFin());
            return Response.ok(new ReporteResponse<>(encabezado, datos)).build();
        }
    }

    /**
     * Exports a report to Excel format based on the provided query parameters.
     *
     * @param idDoctor the ID of the doctor
     * @param fechaInicioStr the start date of the report
     * @param fechaFinStr the end date of the report
     * @param tipoReporte the type of report (e.g., "AGRUPADO")
     * @param usuarioParam the user generating the report
     * @return a response containing the Excel file
     */
    @GET
    @Path("/consultas/excel")
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response descargarReporteExcel(
            @QueryParam("idDoctor")    Long idDoctor,
            @QueryParam("fechaInicio") String fechaInicioStr,
            @QueryParam("fechaFin")    String fechaFinStr,
            @QueryParam("tipoReporte") String tipoReporte,
            @QueryParam("usuario")     String usuarioParam) {

        LocalDate fechaInicio = LocalDate.parse(fechaInicioStr);
        LocalDate fechaFin    = LocalDate.parse(fechaFinStr);

        String usuario = obtenerUsuario(usuarioParam);
        String doctorTxt = obtenerNombreDoctor(idDoctor);
        List<?> reporte = obtenerDatosReporte(idDoctor, fechaInicio, fechaFin, tipoReporte);

        Workbook workbook = crearWorkbookExcel(reporte, usuario, doctorTxt, fechaInicio, fechaFin, tipoReporte);

        StreamingOutput stream = out -> {
            try (workbook) {
                workbook.write(out);
            }
        };

        return Response.ok(stream)
                       .header("Content-Disposition", "attachment; filename=\"Reporte.xlsx\"")
                       .build();
    }

    private String obtenerUsuario(String usuarioParam) {
        return usuarioParam != null ? usuarioParam : "[Anónimo]";
    }

    private String obtenerNombreDoctor(Long idDoctor) {
        String doctorTxt = "Doctor ID = " + idDoctor;
        Optional<Doctor> optDoc = doctorService.getDoctorById(idDoctor);
        if (optDoc.isPresent()) {
            Doctor d = optDoc.get();
            String nombreDoc;
            if (d.getUsuario() != null && d.getUsuario().getNombreUsuario() != null) {
                nombreDoc = d.getUsuario().getNombreUsuario();
            } else if (d.getApellido() != null) {
                nombreDoc = d.getApellido();
            } else {
                nombreDoc = "[Desconocido]";
            }
            doctorTxt = "Doctor: " + nombreDoc;
        }
        return doctorTxt;
    }

    private List<?> obtenerDatosReporte(Long idDoctor, LocalDate fechaInicio, LocalDate fechaFin, String tipoReporte) {
        if ("AGRUPADO".equalsIgnoreCase(tipoReporte)) {
            return reporteService.obtenerReporteAgregado(idDoctor, fechaInicio, fechaFin);
        } else {
            return reporteService.obtenerReporteDetallado(idDoctor, fechaInicio, fechaFin);
        }
    }

    private Workbook crearWorkbookExcel(List<?> reporte, String usuario, String doctorTxt, 
                                      LocalDate fechaInicio, LocalDate fechaFin, String tipoReporte) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Reporte");
        int rownum = 0;

        rownum = crearEncabezadosExcel(sheet, rownum, usuario, doctorTxt, fechaInicio, fechaFin, tipoReporte);

        if (!reporte.isEmpty()) {
            rownum = crearDatosExcel(sheet, rownum, reporte);
            autoajustarColumnas(sheet, reporte);
        } else {
            crearFilaSinDatos(sheet, rownum);
        }

        return workbook;
    }

    private int crearEncabezadosExcel(Sheet sheet, int rownum, String usuario, String doctorTxt, 
                                    LocalDate fechaInicio, LocalDate fechaFin, String tipoReporte) {
        Row r1 = sheet.createRow(rownum++);
        r1.createCell(0).setCellValue("Reporte generado el: " + LocalDateTime.now());
        Row r2 = sheet.createRow(rownum++);
        r2.createCell(0).setCellValue("Usuario: " + usuario);
        Row r3 = sheet.createRow(rownum++);
        r3.createCell(0).setCellValue("Parámetros: " + doctorTxt +
                ", Fecha Inicio = " + fechaInicio +
                ", Fecha Fin = " + fechaFin +
                ", Tipo Reporte = " + tipoReporte);
        rownum++; // fila en blanco
        return rownum;
    }

    private int crearDatosExcel(Sheet sheet, int rownum, List<?> reporte) {
        Object primer = reporte.get(0);
        java.lang.reflect.Field[] fields = primer.getClass().getDeclaredFields();

        Row hdr = sheet.createRow(rownum++);
        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
            hdr.createCell(i).setCellValue(fields[i].getName());
        }

        for (Object obj : reporte) {
            Row row = sheet.createRow(rownum++);
            for (int i = 0; i < fields.length; i++) {
                Object val;
                try {
                    val = fields[i].get(obj);
                } catch (IllegalAccessException e) {
                    val = "Error";
                }
                row.createCell(i).setCellValue(val != null ? val.toString() : "");
            }
        }
        return rownum;
    }

    private void autoajustarColumnas(Sheet sheet, List<?> reporte) {
        if (!reporte.isEmpty()) {
            Object primer = reporte.get(0);
            java.lang.reflect.Field[] fields = primer.getClass().getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                sheet.autoSizeColumn(i);
            }
        }
    }

    private void crearFilaSinDatos(Sheet sheet, int rownum) {
        Row nr = sheet.createRow(rownum);
        nr.createCell(0).setCellValue("No se encontraron datos para los parámetros seleccionados.");
    }
}
