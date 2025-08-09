package com.unis.resource;

import java.util.Optional;

import com.unis.model.EmpleadoAcc;
import com.unis.service.EmpleadoAccService;

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
 * REST resource for managing employee accounts.
 */
@Path("/empleados")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EmpleadoAccResource {

    @Inject
    EmpleadoAccService empleadoAccService;

    /**
     * Retrieves an employee account by its ID.
     *
     * @param id the ID of the employee account
     * @return a response containing the employee account or a NOT_FOUND status
     */
    @GET
    @Path("/{id}")
    public Response getEmpleadoById(@PathParam("id") Long id) {
        Optional<EmpleadoAcc> empleado = empleadoAccService.getEmpleadoById(id);
        return empleado.isPresent() ? Response.ok(empleado.get()).build() : Response.status(Response.Status.NOT_FOUND).build();
    }

    /**
     * Updates an existing employee account.
     *
     * @param id the ID of the employee account to be updated
     * @param empleado the updated employee account data
     * @return a response indicating the update status
     */
    @PUT
    @Path("/{id}")
    public Response updateEmpleado(@PathParam("id") Long id, EmpleadoAcc empleado) {
        empleadoAccService.updateEmpleado(id, empleado);
        return Response.ok().build();
    }
}
