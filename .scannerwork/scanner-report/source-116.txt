package com.unis.resource;

import java.util.List;
import java.util.Map;

import org.jboss.logging.Logger;

import com.unis.model.Servicio;
import com.unis.service.ServicioService;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
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
 * REST resource for managing services and their relationships.
 */
@Path("/api/servicios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ServicioResource {

    @Inject
    ServicioService servicioService;

    private static final Logger LOGGER = Logger.getLogger(ServicioResource.class);

    /**
     * Retrieves all services.
     *
     * @return a list of all services
     */
    @GET
    public List<Servicio> listarServicios() {
        return servicioService.listarTodos();
    }

    /**
     * Retrieves sub-services for a specific service.
     *
     * @param id the ID of the parent service
     * @return a list of sub-services
     */
    @GET
    @Path("/{id}/subservicios")
    public List<Servicio> listarSubServicios(@PathParam("id") Long id) {
        return servicioService.listarSubServicios(id);
    }

    /**
     * Adds a new service.
     *
     * @param servicio the service to be added
     * @return a response containing the created service
     */
    @POST
    @Transactional
    public Response agregarServicio(Servicio servicio) {
        try {
            if (servicio == null || servicio.nombre == null || servicio.nombre.trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\": \"El nombre del servicio es obligatorio.\"}").build();
            }

            Long parentId = servicio.getParentId();

            if (parentId != null) {
                Servicio servicioPadre = servicioService.buscarPorId(parentId);
                if (servicioPadre == null) {
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity("{\"error\": \"El servicio padre no existe.\"}").build();
                }
                servicio.servicioPadre = servicioPadre; // ✅ Asignamos el objeto directamente
            }

            Servicio nuevoServicio = servicioService.agregarServicio(servicio, parentId);
            return Response.status(Response.Status.CREATED).entity(nuevoServicio).build();

        } catch (Exception e) {
            LOGGER.error("Error al agregar servicio", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Error al agregar servicio: " + e.getMessage() + "\"}").build();
        }
    }

    /**
     * Adds a sub-service to a parent service.
     *
     * @param parentId the ID of the parent service
     * @param requestBody the request body containing the sub-service ID
     * @return a response indicating the addition status
     */
    @POST
    @Path("/{id}/subservicios")
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response agregarSubServicio(@PathParam("id") Long parentId, Map<String, Object> requestBody) {
        try {
            System.out.println("📥 Recibiendo solicitud: " + requestBody); // 🔍 Verifica qué recibe

            if (!requestBody.containsKey("subServicioId")) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\": \"El JSON debe contener 'subServicioId'.\"}").build();
            }

            Object subServicioIdObject = requestBody.get("subServicioId");
            if (!(subServicioIdObject instanceof Number)) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\": \"subServicioId debe ser un número válido.\"}").build();
            }

            Long subServicioId = ((Number) subServicioIdObject).longValue();
            servicioService.agregarSubServicio(parentId, subServicioId);

            return Response.ok("{\"message\": \"Subservicio agregado correctamente.\"}").build();
        } catch (Exception e) {
            LOGGER.error("Error al agregar subservicio", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Error al agregar subservicio: " + e.getMessage() + "\"}").build();
        }
    }

    /**
     * Deletes a service by its ID.
     *
     * @param id the ID of the service to be deleted
     * @return a response indicating the deletion status
     */
    @DELETE
    @Path("/{id}")
    @Transactional
    public Response eliminarServicio(@PathParam("id") Long id) {
        try {
            servicioService.eliminarServicio(id);
            return Response.ok().build();
        } catch (Exception e) {
            LOGGER.error("Error al eliminar servicio", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Error al eliminar el servicio.\"}").build();
        }
    }

    /**
     * Deletes the relationship between a parent service and a sub-service.
     *
     * @param servicioPadreId the ID of the parent service
     * @param subServicioId the ID of the sub-service
     * @return a response indicating the deletion status
     */
    @DELETE
    @Path("/{id}/subservicios/{subServicioId}")
    @Transactional
    public Response eliminarRelacion(@PathParam("id") Long servicioPadreId, @PathParam("subServicioId") Long subServicioId) {
        try {
            boolean eliminado = servicioService.eliminarRelacion(servicioPadreId, subServicioId);
            
            if (!eliminado) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\": \"La relación no existe o ya fue eliminada.\"}").build();
            }

            return Response.ok("{\"message\": \"Relación eliminada correctamente.\"}").build();
        } catch (Exception e) {
            LOGGER.error("Error al eliminar relación", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Error al eliminar relación: " + e.getMessage() + "\"}").build();
        }
    }
}

