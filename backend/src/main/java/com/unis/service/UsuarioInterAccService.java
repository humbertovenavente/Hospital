package com.unis.service;

import java.util.List;
import java.util.Optional;

import com.unis.model.UsuarioInterAcc;
import com.unis.repository.UsuarioInterAccRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

/**
 * Servicio para gestionar las operaciones relacionadas con los accesos de usuarios interconectados.
 */
@ApplicationScoped
public class UsuarioInterAccService {

    @Inject
    UsuarioInterAccRepository usuarioInterAccRepository;

    /**
     * Obtiene todos los accesos de usuarios interconectados registrados.
     *
     * @return Una lista de accesos de usuarios interconectados.
     */
    public List<UsuarioInterAcc> getAllUsuariosInterAcc() {
        return usuarioInterAccRepository.listAll();
    }

    /**
     * Obtiene un acceso de usuario interconectado por su ID.
     *
     * @param id El ID del acceso de usuario interconectado.
     * @return Un Optional que contiene el acceso si se encuentra.
     */
    public Optional<UsuarioInterAcc> getUsuarioInterAccById(Long id) {
        return usuarioInterAccRepository.findByIdOptional(id);
    }

    /**
     * Actualiza los datos de un acceso de usuario interconectado existente.
     *
     * @param id              El ID del acceso a actualizar.
     * @param usuarioInterAcc Los nuevos datos del acceso.
     */
    @Transactional
    public void actualizarUsuarioInterAcc(Long id, UsuarioInterAcc usuarioInterAcc) {
        Optional<UsuarioInterAcc> usuarioExistente = usuarioInterAccRepository.findByIdOptional(id);
        if (usuarioExistente.isPresent()) {
            UsuarioInterAcc usuario = usuarioExistente.get();
            usuario.setApellido(usuarioInterAcc.getApellido());
            usuario.setDocumento(usuarioInterAcc.getDocumento());
            usuario.setFechaNacimiento(usuarioInterAcc.getFechaNacimiento());
            usuario.setGenero(usuarioInterAcc.getGenero());
            usuario.setTelefono(usuarioInterAcc.getTelefono());
            usuario.setIdHospital(usuarioInterAcc.getIdHospital());
            usuarioInterAccRepository.persist(usuario);
        }
    }

    /**
     * Elimina un acceso de usuario interconectado por su ID.
     *
     * @param id El ID del acceso a eliminar.
     */
    @Transactional
    public void eliminarUsuarioInterAcc(Long id) {
        usuarioInterAccRepository.deleteById(id);
    }
}

