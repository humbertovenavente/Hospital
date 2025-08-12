package com.unis.resource;

import java.util.Optional;

import com.unis.model.DoctorAcc;
import com.unis.service.DoctorAccService;

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
 * REST resource for managing doctor accounts.
 */
@Path("/doctores")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DoctorAccResource {

    @Inject
    DoctorAccService doctorAccService;

    /**
     * Retrieves a doctor account by its ID.
     *
     * @param id the ID of the doctor account
     * @return a response containing the doctor account or a NOT_FOUND status
     */
    @GET
    @Path("/{id}")
    public Response getDoctorById(@PathParam("id") Long id) {
        Optional<DoctorAcc> doctor = doctorAccService.getDoctorById(id);
        return doctor.isPresent() ? Response.ok(doctor.get()).build() : Response.status(Response.Status.NOT_FOUND).build();
    }

    /**
     * Updates an existing doctor account.
     *
     * @param id the ID of the doctor account to be updated
     * @param doctor the updated doctor account data
     * @return a response indicating the update status
     */
    @PUT
    @Path("/{id}")
    public Response updateDoctor(@PathParam("id") Long id, DoctorAcc doctor) {
        doctorAccService.updateDoctor(id, doctor);
        return Response.ok().build();
    }
}
