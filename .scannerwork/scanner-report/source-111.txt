package com.unis.resource;

import java.util.List;

import com.unis.model.FichaTecnica;
import com.unis.service.FichaTecnicaService;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 * REST resource for managing technical sheets (fichas técnicas).
 */
@Path("/fichas-tecnicas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FichaTecnicaResource {

    @Inject
    FichaTecnicaService fichaTecnicaService;

    /**
     * Retrieves all technical sheets.
     *
     * @return a list of all technical sheets
     */
    @GET
    public List<FichaTecnica> obtenerTodasLasFichas() {
        return fichaTecnicaService.getAllFichas();
    }

    /**
     * Registers a new technical sheet.
     *
     * @param ficha the technical sheet to be registered
     */
    @POST
    public void registrarFicha(FichaTecnica ficha) {
        fichaTecnicaService.registrarFicha(ficha);
    }
}
