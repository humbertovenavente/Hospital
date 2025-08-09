package com.unis.resource;

import java.util.List;

import com.unis.model.Agenda;
import com.unis.service.AgendaService;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * REST resource for managing agendas.
 */
@Path("/agendas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AgendaResource {

    @Inject
    AgendaService agendaService;

    /**
     * Retrieves a list of agendas for a specific doctor.
     *
     * @param idDoctor the ID of the doctor
     * @return a list of agendas associated with the doctor
     */
    @GET
    @Path("/doctor/{idDoctor}")
    public List<Agenda> obtenerAgendasPorDoctor(@PathParam("idDoctor") Long idDoctor) {
        return agendaService.obtenerAgendasPorDoctor(idDoctor);
    }

    /**
     * Creates a new agenda.
     *
     * @param agenda the agenda to be created
     * @return a response containing the created agenda
     */
    @POST
    public Response crearAgenda(Agenda agenda) {
        agendaService.crearAgenda(agenda);
        return Response.status(Response.Status.CREATED).entity(agenda).build();
    }

    /**
     * Updates an existing agenda.
     *
     * @param id the ID of the agenda to be updated
     * @param agendaActualizada the updated agenda data
     * @return a response indicating the update status
     */
    @PUT
    @Path("/{id}")
    public Response actualizarAgenda(@PathParam("id") Long id, Agenda agendaActualizada) {
        agendaService.actualizarAgenda(id, agendaActualizada);
        return Response.ok("Agenda actualizada con éxito").build();
    }
}
