package com.unis.resource;

import java.util.Optional;

import com.unis.model.PacienteAcc;
import com.unis.service.PacienteAccService;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * REST resource for managing patient accounts.
 */
@Path("/pacientes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PacienteAccResource {

    @Inject
    PacienteAccService pacienteAccService;

    /**
     * Retrieves a patient account by its ID.
     *
     * @param id the ID of the patient account
     * @return a response containing the patient account or a NOT_FOUND status
     */
    @GET
    @Path("/{id}")
    public Response getPacienteById(@PathParam("id") Long id) {
        Optional<PacienteAcc> paciente = pacienteAccService.getPacienteById(id);
        return paciente.isPresent() ? Response.ok(paciente.get()).build() : Response.status(Response.Status.NOT_FOUND).build();
    }

    /**
     * Updates an existing patient account.
     *
     * @param id the ID of the patient account to be updated
     * @param paciente the updated patient account data
     * @return a response indicating the update status
     */
    @PUT
    @Path("/{id}")
    public Response updatePaciente(@PathParam("id") Long id, PacienteAcc paciente) {
        pacienteAccService.updatePaciente(id, paciente);
        return Response.ok().build();
    }
}
