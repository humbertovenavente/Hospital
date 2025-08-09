package com.unis.resource;

import java.util.List;
import java.util.Optional;

import com.unis.model.Paciente;
import com.unis.service.PacienteService;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * REST resource for managing patients.
 */
@Path("/paciente")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PacienteResource {

    @Inject
    PacienteService pacienteService;

    /**
     * Retrieves all patients.
     *
     * @return a list of all patients
     */
    @GET
    public List<Paciente> obtenerTodosLosPacientes() {
        return pacienteService.getAllPacientes();
    }

    /**
     * Retrieves a specific patient by its ID.
     *
     * @param id the ID of the patient
     * @return a response containing the patient or a NOT_FOUND status
     */
    @GET
    @Path("/{id}")
    public Response obtenerPaciente(@PathParam("id") Long id) {
        Optional<Paciente> paciente = pacienteService.getPacienteById(id);
        return paciente.isPresent() ? Response.ok(paciente.get()).build() : Response.status(Response.Status.NOT_FOUND).build();
    }

    /**
     * Registers a new patient.
     *
     * @param paciente the patient to be registered
     * @return a response indicating the registration status
     */
    @POST
    public Response registrarPaciente(Paciente paciente) {
        pacienteService.registrarPaciente(paciente);
        return Response.status(Response.Status.CREATED).build();
    }

    /**
     * Updates an existing patient.
     *
     * @param id the ID of the patient to be updated
     * @param paciente the updated patient data
     * @return a response indicating the update status
     */
    @PUT
    @Path("/{id}")
    public Response actualizarPaciente(@PathParam("id") Long id, Paciente paciente) {
        boolean actualizado = pacienteService.actualizarPaciente(id, paciente);
        return actualizado ? Response.ok().build() : Response.status(Response.Status.NOT_FOUND).build();
    }

    /**
     * Deletes a patient by its ID.
     *
     * @param id the ID of the patient to be deleted
     * @return a response indicating the deletion status
     */
    @DELETE
    @Path("/{id}")
    public Response eliminarPaciente(@PathParam("id") Long id) {
        boolean eliminado = pacienteService.eliminarPaciente(id);
        return eliminado ? Response.ok().build() : Response.status(Response.Status.NOT_FOUND).build();
    }
}
