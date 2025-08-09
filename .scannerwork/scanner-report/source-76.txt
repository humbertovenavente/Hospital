package com.unis.service;

import java.util.Optional;

import com.unis.model.EmpleadoAcc;
import com.unis.repository.EmpleadoAccRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

/**
 * Servicio para gestionar las operaciones relacionadas con los accesos de empleados.
 */
@ApplicationScoped
public class EmpleadoAccService {

    @Inject
    EmpleadoAccRepository empleadoAccRepository;

    /**
     * Obtiene un empleado por su ID de usuario.
     *
     * @param id El ID del usuario asociado al empleado.
     * @return Un Optional que contiene el empleado si se encuentra.
     */
    public Optional<EmpleadoAcc> getEmpleadoById(Long id) {
        return empleadoAccRepository.find("usuario.idUsuario", id).firstResultOptional();
    }

    /**
     * Actualiza los datos de un empleado existente.
     *
     * @param id           El ID del usuario asociado al empleado.
     * @param empleadoData Los nuevos datos del empleado.
     */
    @Transactional
    public void updateEmpleado(Long id, EmpleadoAcc empleadoData) {
        Optional<EmpleadoAcc> existingEmpleado = empleadoAccRepository.find("usuario.idUsuario", id).firstResultOptional();
        if (existingEmpleado.isPresent()) {
            EmpleadoAcc empleado = existingEmpleado.get();
            empleado.setApellido(empleadoData.getApellido());
            empleado.setDocumento(empleadoData.getDocumento());
            empleado.setFechaNacimiento(empleadoData.getFechaNacimiento());
            empleado.setGenero(empleadoData.getGenero());
            empleado.setTelefono(empleadoData.getTelefono());
            empleado.setPuesto(empleadoData.getPuesto());
            empleadoAccRepository.persist(empleado);
        }
    }
}
