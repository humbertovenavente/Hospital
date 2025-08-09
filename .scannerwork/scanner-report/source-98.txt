package com.unis.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.unis.dto.ModeracionReporteDTO;
import com.unis.service.ReporteModeracionService;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;

/**
 * REST controller for generating moderation-related reports.
 * <p>
 * Provides an endpoint to retrieve a ranked list of users with the highest number
 * of content rejections, filtered by date range and limited to a specific number of results.
 * </p>
 */
@Path("/reporte-moderacion")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReporteModeracionController {

    @Inject
    ReporteModeracionService service;

    /**
     * Retrieves a list of users with the most rejections within the given date range.
     *
     * @param inicio the start date in format yyyy-MM-dd
     * @param fin the end date in format yyyy-MM-dd
     * @param limite the maximum number of users to return (default is 10)
     * @return a list of {@link ModeracionReporteDTO} objects
     * @throws WebApplicationException if date parsing fails
     */
    @GET
    public List<ModeracionReporteDTO> obtener(
            @QueryParam("inicio") String inicio,
            @QueryParam("fin") String fin,
            @QueryParam("limite") @DefaultValue("10") int limite
    ) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date fechaInicio = sdf.parse(inicio);
            Date fechaFin = sdf.parse(fin);

            return service.obtenerUsuariosConRechazos(fechaInicio, fechaFin, limite);
        } catch (Exception e) {
            throw new WebApplicationException("Formato de fecha inválido", 400);
        }
    }
}
