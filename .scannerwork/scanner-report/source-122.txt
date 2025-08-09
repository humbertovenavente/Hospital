package com.unis.resource;

import java.util.HashMap;
import java.util.Map;

import com.unis.model.Usuario;
import com.unis.service.UsuarioService;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * REST resource for managing users.
 */
@Path("/api/usuarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsuarioResource {

    @Inject
    UsuarioService usuarioService;

    /**
     * Registers a new user.
     *
     * @param usuario the user to be registered
     * @return a response indicating the registration status
     */
    @POST
    @Path("/registro")
    @Transactional
    public Response registrarUsuario(Usuario usuario) {
        usuarioService.registrarUsuario(usuario);

        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("mensaje", "Usuario registrado con éxito");

        return Response.status(Response.Status.CREATED).entity(respuesta).build();
    }
}
