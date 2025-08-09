package com.unis.resource;

import java.util.List;

import com.unis.model.PacienteFT;
import com.unis.service.PacienteFTService;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 * REST resource for managing patients with technical files (PacienteFT).
 */
@Path("/pacientes-ft")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PacienteFTResource {

    @Inject
    PacienteFTService pacienteFTService;

    /**
     * Retrieves all patients with technical files.
     *
     * @return a list of all patients with technical files
     */
    @GET
    public List<PacienteFT> obtenerTodosLosPacientes() {
        return pacienteFTService.getAllPacientes();
    }

    /**
     * Registers a new patient with a technical file.
     *
     * @param paciente the patient with a technical file to be registered
     */
    @POST
    public void registrarPaciente(PacienteFT paciente) {
        pacienteFTService.registrarPaciente(paciente);
    }
}
