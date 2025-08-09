package com.unis.resource;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.unis.dto.ModeracionReporteDTO;
import com.unis.service.ReporteModeracionExcelService;
import com.unis.service.ReporteModeracionService;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.StreamingOutput;

/**
 * REST resource for generating moderation reports.
 */
@Path("/api/reporte-moderacion")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReporteModeracionResource {

    @Inject
    ReporteModeracionService service;

    @Inject
    ReporteModeracionExcelService excelService;

    /**
     * Retrieves a moderation report for users with rejections within a date range.
     *
     * @param inicio the start date of the report
     * @param fin the end date of the report
     * @param limite the maximum number of results
     * @return a response containing the moderation report
     */
    @GET
    @Path("/usuarios")
    public Response obtenerReporteUsuarios(
            @QueryParam("fechaInicio") String inicio,
            @QueryParam("fechaFin") String fin,
            @QueryParam("limite") @DefaultValue("10") int limite
    ) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date fechaInicio = sdf.parse(inicio);
            Date fechaFin = sdf.parse(fin);

            List<ModeracionReporteDTO> resultado = service.obtenerUsuariosConRechazos(fechaInicio, fechaFin, limite);
            return Response.ok(resultado).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Parámetros inválidos").build();
        }
    }

    /**
     * Exports a moderation report for users with rejections to Excel format.
     *
     * @param inicio the start date of the report
     * @param fin the end date of the report
     * @param limite the maximum number of results
     * @return a response containing the Excel file
     */
    @GET
    @Path("/usuarios/excel")
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response descargarExcelUsuarios(
            @QueryParam("fechaInicio") String inicio,
            @QueryParam("fechaFin") String fin,
            @QueryParam("limite") @DefaultValue("10") int limite
    ) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date fechaInicio = sdf.parse(inicio);
            Date fechaFin = sdf.parse(fin);

            byte[] excel = excelService.generarExcel(fechaInicio, fechaFin, limite);

            StreamingOutput stream = output -> {
                output.write(excel);
                output.flush();
            };

            return Response.ok(stream)
                    .header("Content-Disposition", "attachment; filename=\"reporte_usuarios_moderacion.xlsx\"")
                    .build();

        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error generando archivo Excel").build();
        }
    }
}
