package com.unis.resource;

import java.util.List;

import com.unis.model.RecetaMedicamento;
import com.unis.service.RecetaMedicamentoService;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * REST resource for managing medications in prescriptions.
 */
@Path("/receta-medicamentos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RecetaMedicamentoResource {

    @Inject
    RecetaMedicamentoService recetaMedicamentoService;

    /**
     * Retrieves a list of medications for a specific prescription.
     *
     * @param idReceta the ID of the prescription
     * @return a list of medications associated with the prescription
     */
    @GET
    @Path("/{idReceta}")
    public List<RecetaMedicamento> listarPorReceta(@PathParam("idReceta") Long idReceta) {
        return recetaMedicamentoService.listarPorReceta(idReceta);
    }

    /**
     * Adds a medication to a prescription.
     *
     * @param recetaMedicamento the medication to be added
     * @return a response indicating the addition status
     */
    @POST
    public Response agregarMedicamento(RecetaMedicamento recetaMedicamento) {
        recetaMedicamentoService.agregarMedicamentoAReceta(recetaMedicamento);
        return Response.status(Response.Status.CREATED).build();
    }

    /**
     * Deletes a medication from a prescription.
     *
     * @param idRecetaMedicamento the ID of the medication to be deleted
     * @return a response indicating the deletion status
     */
    @DELETE
    @Path("/{idRecetaMedicamento}")
    public Response eliminarMedicamento(@PathParam("idRecetaMedicamento") Long idRecetaMedicamento) {
        boolean eliminado = recetaMedicamentoService.eliminar(idRecetaMedicamento);
        return eliminado ? Response.ok().build() : Response.status(Response.Status.NOT_FOUND).build();
    }
}
