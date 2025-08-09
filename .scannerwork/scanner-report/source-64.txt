package com.unis.service;

import java.util.List;

import com.unis.model.Rol;
import com.unis.model.Usuario;
import com.unis.repository.RolRepository;
import com.unis.repository.UsuarioRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

/**
 * Servicio que implementa la lógica de negocio relacionada con los usuarios.
 * Se encarga de la gestión, registro, autenticación y administración (activación y asignación de roles)
 * de los usuarios.
 */
@ApplicationScoped
public class UsuarioService {

    @Inject
    UsuarioRepository usuarioRepository;

    @Inject
    RolRepository rolRepository;

    /**
     * Retorna la lista de todos los usuarios registrados.
     *
     * @return Lista de objetos {@link Usuario}.
     */
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.listAll();
    }

    /**
     * Obtiene un usuario según el correo electrónico.
     *
     * @param correo Correo del usuario.
     * @return Objeto {@link Usuario} encontrado o null si no existe.
     */
    public Usuario obtenerUsuarioPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }

    /**
     * Registra un nuevo usuario verificando que el correo no esté duplicado.
     *
     * @param usuario Objeto {@link Usuario} a registrar.
     * @throws WebApplicationException si el correo ya está registrado.
     */
    @Transactional
    public void registrarUsuario(Usuario usuario) {
        Usuario usuarioExistente = usuarioRepository.findByCorreo(usuario.getCorreo());
        if (usuarioExistente != null) {
            throw new WebApplicationException("El correo ya está registrado", Response.Status.BAD_REQUEST);
        }
        usuarioRepository.persist(usuario);
    }

    /**
     * Retorna la lista de usuarios que se encuentran inactivos (estado = 0).
     *
     * @return Lista de usuarios inactivos.
     */
    public List<Usuario> listarUsuariosInactivos() {
        return usuarioRepository.find("estado", 0).list();
    }

    /**
     * Activa un usuario asignándole un rol y cambiando su estado a activo (1).
     *
     * @param idUsuario Identificador del usuario a activar.
     * @param idRol     Identificador del rol a asignar.
     * @return Usuario actualizado con el rol asignado y estado activo.
     * @throws WebApplicationException si el usuario no existe o si el rol no es válido.
     */
    @Transactional
    public Usuario activarUsuario(Long idUsuario, Long idRol) {
        Usuario usuario = usuarioRepository.findById(idUsuario);
        if (usuario == null) {
            throw new WebApplicationException("Usuario no encontrado", Response.Status.NOT_FOUND);
        }
        Rol rol = rolRepository.findById(idRol);
        if (rol == null) {
            throw new WebApplicationException("Rol no válido", Response.Status.BAD_REQUEST);
        }
        usuario.setRol(rol);
        usuario.setEstado(1);
        return usuario;
    }

    /**
     * Desactiva un usuario cambiando su estado a 0.
     *
     * @param idUsuario Identificador del usuario a desactivar.
     * @return Usuario actualizado con estado inactivo (0).
     * @throws WebApplicationException si el usuario no existe.
     */
    @Transactional
    public Usuario desactivarUsuario(Long idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario);
        if (usuario == null) {
            throw new WebApplicationException("Usuario no encontrado", Response.Status.NOT_FOUND);
        }
        usuario.setEstado(0);
        return usuario;
    }

    /**
     * Retorna la lista de todos los roles disponibles.
     *
     * @return Lista de objetos {@link Rol}.
     */
    public List<Rol> listarRoles() {
        return rolRepository.listAll();
    }
}
