package com.unis.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.unis.service.ReporteMedicinaExcelService;

import jakarta.inject.Inject;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;

/**
 * REST controller for exporting medicine report data as an Excel file.
 * <p>
 * Generates and serves a downloadable `.xlsx` report containing medicine-related statistics
 * within a specified date range, limited by a maximum result count, and attributed to a user.
 * </p>
 */
@Path("/reporte-medicinas/excel")
public class ReporteMedicinaExcelController {

    @Inject
    ReporteMedicinaExcelService excelService;

    /**
     * Downloads the medicine report as an Excel file.
     *
     * @param inicio the start date in format yyyy-MM-dd
     * @param fin the end date in format yyyy-MM-dd
     * @param limite the maximum number of rows to include in the report (default is 10)
     * @param usuario the user requesting the report (used for metadata)
     * @return a {@link Response} containing the Excel file or an error message
     */
    @GET
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response descargarExcel(
            @QueryParam("inicio") String inicio,
            @QueryParam("fin") String fin,
            @QueryParam("limite") @DefaultValue("10") int limite,
            @QueryParam("usuario") @DefaultValue("admin@hospital.com") String usuario
    ) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date fechaInicio = sdf.parse(inicio);
            Date fechaFin = sdf.parse(fin);

            byte[] excel = excelService.generarExcel(fechaInicio, fechaFin, limite, usuario);

            return Response.ok(excel)
                    .header("Content-Disposition", "attachment; filename=medicinas_reporte.xlsx")
                    .build();

        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Error generando reporte").build();
        }
    }
}
