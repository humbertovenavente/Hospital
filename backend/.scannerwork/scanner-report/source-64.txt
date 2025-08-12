package com.unis.resource;

import java.util.List;
import java.util.Optional;

import com.unis.model.Empleado;
import com.unis.service.EmpleadoService;

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
 * REST resource for managing employees.
 */
@Path("/empleado")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EmpleadoResource {

    @Inject
    EmpleadoService empleadoService;

    /**
     * Retrieves all employees.
     *
     * @return a list of all employees
     */
    @GET
    public List<Empleado> obtenerTodosLosEmpleados() {
        return empleadoService.getAllEmpleados();
    }

    /**
     * Retrieves a specific employee by its ID.
     *
     * @param id the ID of the employee
     * @return a response containing the employee or a NOT_FOUND status
     */
    @GET
    @Path("/{id}")
    public Response obtenerEmpleado(@PathParam("id") Long id) {
        Optional<Empleado> empleado = empleadoService.getEmpleadoById(id);
        return empleado.isPresent() ? Response.ok(empleado.get()).build() : Response.status(Response.Status.NOT_FOUND).build();
    }

    /**
     * Registers a new employee.
     *
     * @param empleado the employee to be registered
     * @return a response indicating the registration status
     */
    @POST
    public Response registrarEmpleado(Empleado empleado) {
        empleadoService.registrarEmpleado(empleado);
        return Response.status(Response.Status.CREATED).build();
    }

    /**
     * Updates an existing employee.
     *
     * @param id the ID of the employee to be updated
     * @param empleado the updated employee data
     * @return a response indicating the update status
     */
    @PUT
    @Path("/{id}")
    public Response actualizarEmpleado(@PathParam("id") Long id, Empleado empleado) {
        boolean actualizado = empleadoService.actualizarEmpleado(id, empleado);
        return actualizado ? Response.ok().build() : Response.status(Response.Status.NOT_FOUND).build();
    }

    /**
     * Deletes an employee by its ID.
     *
     * @param id the ID of the employee to be deleted
     * @return a response indicating the deletion status
     */
    @DELETE
    @Path("/{id}")
    public Response eliminarEmpleado(@PathParam("id") Long id) {
        boolean eliminado = empleadoService.eliminarEmpleado(id);
        return eliminado ? Response.ok().build() : Response.status(Response.Status.NOT_FOUND).build();
    }
}