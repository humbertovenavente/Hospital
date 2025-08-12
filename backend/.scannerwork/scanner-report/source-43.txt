// CitaResource.java
package com.unis.resource;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.unis.model.Cita;
import com.unis.model.Doctor;
import com.unis.service.CitaService;

import jakarta.inject.Inject;
import jakarta.json.JsonObject;
import jakarta.transaction.Transactional;
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
 * REST resource for managing medical appointments (citas).
 */
@Path("/citas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CitaResource {

    @Inject
    CitaService citaService;

    /**
     * Retrieves all appointments.
     *
     * @return a list of all appointments
     */
    @GET
    public List<Cita> obtenerCitas() {
        return citaService.obtenerCitas();
    }

    /**
     * Retrieves a specific appointment by its ID.
     *
     * @param id the ID of the appointment
     * @return the appointment with the specified ID
     */
    @GET
    @Path("/{id}")
    public Cita obtenerCita(@PathParam("id") Long id) {
        return citaService.obtenerCitaPorId(id);
    }

    /**
     * Schedules a new appointment.
     *
     * @param cita the appointment to be scheduled
     * @return a response indicating the scheduling status
     */
    @POST
    public Response agendarCita(Cita cita) {
        if (cita.getHoraInicio() == null || cita.getHoraInicio().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("⚠️ Error: La hora de inicio es obligatoria").build();
        }
        if (cita.getHoraFin() == null || cita.getHoraFin().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("⚠️ Error: La hora de fin es obligatoria").build();
        }
        if (cita.getHoraInicio().compareTo(cita.getHoraFin()) >= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("⚠️ Error: La hora de fin debe ser posterior a la hora de inicio").build();
        }

        citaService.agendarCita(cita);
        return Response.status(Response.Status.CREATED)
                .entity("✅ Cita agendada con éxito").build();
    }

    /**
     * Updates an existing appointment.
     *
     * @param id the ID of the appointment to be updated
     * @param citaActualizada the updated appointment data
     * @return a response indicating the update status
     */
    @PUT
    @Path("/{id}")
    public Response actualizarCita(@PathParam("id") Long id, Cita citaActualizada) {
        try {
            citaService.actualizarCita(id, citaActualizada);
            return Response.ok("✅ Cita actualizada con éxito").build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("⚠️ Error: " + e.getMessage()).build();
        }
    }

    /**
     * Cancels an existing appointment.
     *
     * @param id the ID of the appointment to be canceled
     * @return a response indicating the cancellation status
     */
    @PUT
    @Path("/{id}/cancelar")
    public Response cancelarCita(@PathParam("id") Long id) {
        try {
            citaService.cancelarCita(id);
            return Response.ok("Cita cancelada").build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("⚠️ Error: " + e.getMessage()).build();
        }
    }

    /**
     * Reassigns a doctor to an existing appointment.
     *
     * @param id the ID of the appointment
     * @param body the JSON body containing the new doctor's ID
     * @return a response indicating the reassignment status
     */
    @PUT
    @Path("/{id}/reasignar")
    public Response reasignarDoctor(@PathParam("id") Long id, JsonNode body) {
        try {
            Long idDoctor = body.get("idDoctor").asLong();
            Doctor nuevoDoctor = citaService.buscarDoctorPorId(idDoctor);
            if (nuevoDoctor == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("⚠️ Doctor no encontrado").build();
            }

            citaService.reasignarDoctor(id, nuevoDoctor);
            return Response.ok("Doctor reasignado con éxito").build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST).entity("❌ Error en la reasignación").build();
        }
    }

    /**
     * Processes an appointment and sends results.
     *
     * @param id the ID of the appointment
     * @param body the JSON body containing diagnosis and results
     * @return a response indicating the processing status
     */
    @PUT
    @Path("/{id}/procesar")
    public Response procesarCita(@PathParam("id") Long id, JsonNode body) {
        try {
            String diagnostico = body.get("diagnostico").asText(null);
            String resultados = body.get("resultados").asText(null);
            citaService.procesarCitaYEnviarResultados(id, diagnostico, resultados);
            return Response.ok(" Cita procesada y resultados enviados").build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error al procesar la cita").build();
        }
    }

    /**
     * Receives an appointment from an external insurer.
     *
     * @param dto the JSON object containing appointment details
     * @return a response indicating the reception status
     */
    @POST
@Path("/externa")
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
public Response recibirDesdeAseguradora(JsonObject dto) {
    try {
        citaService.crearCitaDesdeJson(dto);
        return Response.status(Response.Status.CREATED).entity("✅ Cita recibida correctamente").build();
    } catch (Exception e) {
        e.printStackTrace();
        return Response.status(Response.Status.BAD_REQUEST)
            .entity("Error al guardar cita: " + e.getMessage()).build();
    }
}

}
