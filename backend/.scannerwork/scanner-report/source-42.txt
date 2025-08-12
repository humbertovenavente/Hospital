package com.unis.controller;

import java.util.List;

import com.unis.model.Rol;
import com.unis.model.Usuario;
import com.unis.service.UsuarioService;

import jakarta.inject.Inject;
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
 * REST controller for managing user-related operations.
 * <p>
 * Handles user registration, login, activation, role assignment,
 * listing of active/inactive users, and role retrieval.
 * </p>
 */
@Path("/usuarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsuarioController {

    @Inject
    UsuarioService usuarioService;

    @GET
    public List<Usuario> listarUsuarios() {
        return usuarioService.listarUsuarios();
    }

    @POST
    @Path("/registro")
    @Transactional
    public Response registrarUsuario(Usuario usuario) {
        usuarioService.registrarUsuario(usuario);
        return Response.ok(usuario).status(201).build();
    }

    @POST
    @Path("/login")
    public Response login(Usuario usuario) {
        Usuario usuarioEncontrado = usuarioService.obtenerUsuarioPorCorreo(usuario.getCorreo());
        if (usuarioEncontrado != null && usuarioEncontrado.getContrasena().equals(usuario.getContrasena())) {
            return Response.ok(usuarioEncontrado).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED)
                       .entity("Credenciales incorrectas")
                       .build();
    }

    @GET
    @Path("/inactivos")
    public List<Usuario> listarUsuariosInactivos() {
        return usuarioService.listarUsuariosInactivos();
    }

    @GET
    @Path("/roles")
    public List<Rol> listarRoles() {
        return usuarioService.listarRoles();
    }

    @PUT
    @Path("/{id}/activar")
    @Transactional
    public Response activarUsuario(@PathParam("id") Long id, ActivarUsuarioDTO dto) {
        try {
            Usuario usuarioActivado = usuarioService.activarUsuario(id, dto.getRolId());
            return Response.ok(usuarioActivado).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity(e.getMessage())
                           .build();
        }
    }

    // Nuevo endpoint para desactivar usuario
    @PUT
    @Path("/{id}/desactivar")
    @Transactional
    public Response desactivarUsuario(@PathParam("id") Long id) {
        try {
            Usuario usuarioDesactivado = usuarioService.desactivarUsuario(id);
            return Response.ok(usuarioDesactivado).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity(e.getMessage())
                           .build();
        }
    }

    public static class ActivarUsuarioDTO {
        private Long rolId;

        public Long getRolId() {
            return rolId;
        }

        public void setRolId(Long rolId) {
            this.rolId = rolId;
        }
    }
}
