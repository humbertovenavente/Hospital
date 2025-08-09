package com.unis.resource;

import java.util.List;
import java.util.Optional;

import com.unis.model.UsuarioInterAcc;
import com.unis.service.UsuarioInterAccService;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * REST resource for managing intermediate user accounts.
 */
@Path("/usuariosinter")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsuarioInterAccResource {

    @Inject
    UsuarioInterAccService usuarioInterAccService;

    /**
     * Retrieves all intermediate user accounts.
     *
     * @return a list of all intermediate user accounts
     */
    @GET
    public List<UsuarioInterAcc> obtenerTodos() {
        return usuarioInterAccService.getAllUsuariosInterAcc();
    }

    /**
     * Retrieves an intermediate user account by its ID.
     *
     * @param id the ID of the intermediate user account
     * @return a response containing the user account or a NOT_FOUND status
     */
    @GET
    @Path("/{id}")
    public Response obtenerPorId(@PathParam("id") Long id) {
        Optional<UsuarioInterAcc> usuario = usuarioInterAccService.getUsuarioInterAccById(id);
        return usuario.isPresent() ? Response.ok(usuario.get()).build() : Response.status(Response.Status.NOT_FOUND).build();
    }

    /**
     * Updates an existing intermediate user account.
     *
     * @param id the ID of the user account to be updated
     * @param usuarioInterAcc the updated user account data
     * @return a response indicating the update status
     */
    @PUT
    @Path("/{id}")
    public Response actualizarUsuario(@PathParam("id") Long id, UsuarioInterAcc usuarioInterAcc) {
        usuarioInterAccService.actualizarUsuarioInterAcc(id, usuarioInterAcc);
        return Response.ok().build();
    }

    /**
     * Deletes an intermediate user account by its ID.
     *
     * @param id the ID of the user account to be deleted
     * @return a response indicating the deletion status
     */
    @DELETE
    @Path("/{id}")
    public Response eliminarUsuario(@PathParam("id") Long id) {
        usuarioInterAccService.eliminarUsuarioInterAcc(id);
        return Response.ok().build();
    }
}
