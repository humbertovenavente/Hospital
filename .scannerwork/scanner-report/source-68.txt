package com.unis.service;

import java.util.Optional;

import com.unis.model.UserAcc;
import com.unis.repository.DoctorAccRepository;
import com.unis.repository.EmpleadoAccRepository;
import com.unis.repository.PacienteAccRepository;
import com.unis.repository.UserAccRepository;
import com.unis.repository.UsuarioInterAccRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

/**
 * Servicio para gestionar las operaciones relacionadas con los accesos de usuarios.
 */
@ApplicationScoped
public class UserAccService {

    @Inject
    UserAccRepository userAccRepository;

    @Inject
    DoctorAccRepository doctorAccRepository;

    @Inject
    EmpleadoAccRepository empleadoAccRepository;

    @Inject
    PacienteAccRepository pacienteAccRepository;

    @Inject
    UsuarioInterAccRepository usuarioInterAccRepository;

    /**
     * Obtiene un usuario por su ID.
     *
     * @param id El ID del usuario.
     * @return Un Optional que contiene el usuario si se encuentra.
     */
    public Optional<UserAcc> getUserById(Long id) {
        return userAccRepository.findByIdOptional(id);
    }

    /**
     * Actualiza los datos de un usuario existente.
     *
     * @param id          El ID del usuario a actualizar.
     * @param updatedUser Los nuevos datos del usuario.
     */
    @Transactional
    public void updateUser(Long id, UserAcc updatedUser) {
        UserAcc existingUser = userAccRepository.findById(id);
        if (existingUser != null) {
            existingUser.setNombreUsuario(updatedUser.getNombreUsuario());
            existingUser.setCorreo(updatedUser.getCorreo());
            existingUser.setContrasena(updatedUser.getContrasena());
            userAccRepository.persist(existingUser);
        }
    }

    /**
     * Cambia el rol de un usuario y elimina los datos asociados al rol anterior.
     *
     * @param id       El ID del usuario.
     * @param nuevoRol El nuevo rol a asignar.
     * @throws IllegalArgumentException Si el usuario no se encuentra.
     */
    @Transactional
    public void changeUserRole(Long id, int nuevoRol) {
        UserAcc user = userAccRepository.findById(id);
        if (user == null) {
            throw new IllegalArgumentException("Usuario no encontrado.");
        }

        // Eliminar los datos antiguos dependiendo del rol anterior
        switch (user.getRolId()) {
            case 2:
                doctorAccRepository.delete("idUsuario", id);
                break;
            case 3:
                empleadoAccRepository.delete("idUsuario", id);
                break;
            case 4:
                pacienteAccRepository.delete("idUsuario", id);
                break;
            case 5:
                usuarioInterAccRepository.delete("idUsuario", id);
                break;
        }

        // Asignar nuevo rol
        user.setRolId(nuevoRol);
        userAccRepository.persist(user);
    }
}
