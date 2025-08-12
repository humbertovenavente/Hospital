package com.unis.resource;

import java.util.List;
import java.util.Optional;

import com.unis.model.Doctor;
import com.unis.service.DoctorService;

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
 * REST resource for managing doctors.
 */
@Path("/doctor")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DoctorResource {

    @Inject
    DoctorService doctorService;

    /**
     * Retrieves all doctors.
     *
     * @return a list of all doctors
     */
    @GET
    public List<Doctor> obtenerTodosLosDoctores() {
        return doctorService.getAllDoctores();
    }

    /**
     * Retrieves a specific doctor by its ID.
     *
     * @param id the ID of the doctor
     * @return a response containing the doctor or a NOT_FOUND status
     */
    @GET
    @Path("/{id}")
    public Response obtenerDoctor(@PathParam("id") Long id) {
        Optional<Doctor> doctor = doctorService.getDoctorById(id);
        return doctor.isPresent() ? Response.ok(doctor.get()).build() : Response.status(Response.Status.NOT_FOUND).build();
    }

    /**
     * Registers a new doctor.
     *
     * @param doctor the doctor to be registered
     * @return a response indicating the registration status
     */
    @POST
    public Response registrarDoctor(Doctor doctor) {
        doctorService.registrarDoctor(doctor);
        return Response.status(Response.Status.CREATED).build();
    }

    /**
     * Updates an existing doctor.
     *
     * @param id the ID of the doctor to be updated
     * @param doctor the updated doctor data
     * @return a response indicating the update status
     */
    @PUT
    @Path("/{id}")
    public Response actualizarDoctor(@PathParam("id") Long id, Doctor doctor) {
        boolean actualizado = doctorService.actualizarDoctor(id, doctor);
        return actualizado ? Response.ok().build() : Response.status(Response.Status.NOT_FOUND).build();
    }

    /**
     * Deletes a doctor by its ID.
     *
     * @param id the ID of the doctor to be deleted
     * @return a response indicating the deletion status
     */
    @DELETE
    @Path("/{id}")
    public Response eliminarDoctor(@PathParam("id") Long id) {
        boolean eliminado = doctorService.eliminarDoctor(id);
        return eliminado ? Response.ok().build() : Response.status(Response.Status.NOT_FOUND).build();
    }
}
