package com.unis.resource;

import java.util.List;
import java.util.Optional;

import com.unis.model.UsuarioInter;
import com.unis.service.UsuarioInterService;

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
 * REST resource for managing intermediate users.
 */
@Path("/usuariointer")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsuarioInterResource {

    @Inject
    UsuarioInterService usuarioInterService;

    /**
     * Retrieves all intermediate users.
     *
     * @return a list of all intermediate users
     */
    @GET
    public List<UsuarioInter> obtenerTodos() {
        return usuarioInterService.getAllUsuarios();
    }

    /**
     * Retrieves an intermediate user by its ID.
     *
     * @param id the ID of the intermediate user
     * @return a response containing the user or a NOT_FOUND status
     */
    @GET
    @Path("/{id}")
    public Response obtenerPorId(@PathParam("id") Long id) {
        Optional<UsuarioInter> usuario = usuarioInterService.getUsuarioById(id);
        return usuario.isPresent() ? Response.ok(usuario.get()).build() : Response.status(Response.Status.NOT_FOUND).build();
    }

    /**
     * Creates a new intermediate user.
     *
     * @param usuario the user to be created
     * @return a response indicating the creation status
     */
    @POST
    public Response crearUsuario(UsuarioInter usuario) {
        usuarioInterService.registrarUsuario(usuario);
        return Response.status(Response.Status.CREATED).build();
    }

    /**
     * Updates an existing intermediate user.
     *
     * @param id the ID of the user to be updated
     * @param usuario the updated user data
     * @return a response indicating the update status
     */
    @PUT
    @Path("/{id}")
    public Response actualizarUsuario(@PathParam("id") Long id, UsuarioInter usuario) {
        usuarioInterService.actualizarUsuario(id, usuario);
        return Response.ok().build();
    }

    /**
     * Deletes an intermediate user by its ID.
     *
     * @param id the ID of the user to be deleted
     * @return a response indicating the deletion status
     */
    @DELETE
    @Path("/{id}")
    public Response eliminarUsuario(@PathParam("id") Long id) {
        usuarioInterService.eliminarUsuario(id);
        return Response.ok().build();
    }
}
