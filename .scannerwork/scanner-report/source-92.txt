package com.unis.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.unis.service.ReporteModeracionExcelService;

import jakarta.inject.Inject;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;

/**
 * REST controller for exporting moderation report data as an Excel file.
 * <p>
 * Provides a downloadable `.xlsx` report of users with the highest number of content rejections
 * in a given date range, limited by a specified number of entries.
 * </p>
 */
@Path("/reporte-moderacion/excel")
public class ReporteModeracionExcelController {

    @Inject
    ReporteModeracionExcelService excelService;

    /**
     * Downloads the moderation report as an Excel file.
     *
     * @param inicio the start date in format yyyy-MM-dd
     * @param fin the end date in format yyyy-MM-dd
     * @param limite the maximum number of users to include in the report (default is 10)
     * @return a {@link Response} with the Excel file, or a 400 error if date parsing fails
     */
    @GET
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response descargarExcel(
            @QueryParam("inicio") String inicio,
            @QueryParam("fin") String fin,
            @QueryParam("limite") @DefaultValue("10") int limite
    ) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date fechaInicio = sdf.parse(inicio);
            Date fechaFin = sdf.parse(fin);

            byte[] excel = excelService.generarExcel(fechaInicio, fechaFin, limite);

            return Response.ok(excel)
                    .header("Content-Disposition", "attachment; filename=moderacion_reporte.xlsx")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error generando reporte").build();
        }
    }
}
