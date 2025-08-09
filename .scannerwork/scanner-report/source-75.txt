package com.unis.service;

import java.util.Optional;

import com.unis.model.PacienteAcc;
import com.unis.repository.PacienteAccRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

/**
 * Servicio para gestionar las operaciones relacionadas con los accesos de pacientes.
 */
@ApplicationScoped
public class PacienteAccService {

    @Inject
    PacienteAccRepository pacienteAccRepository;

    /**
     * Obtiene un paciente por su ID de usuario.
     *
     * @param id El ID del usuario asociado al paciente.
     * @return Un Optional que contiene el paciente si se encuentra.
     */
    public Optional<PacienteAcc> getPacienteById(Long id) {
        return pacienteAccRepository.find("usuario.idUsuario", id).firstResultOptional();
    }

    /**
     * Actualiza los datos de un paciente existente.
     *
     * @param id           El ID del usuario asociado al paciente.
     * @param pacienteData Los nuevos datos del paciente.
     */
    @Transactional
    public void updatePaciente(Long id, PacienteAcc pacienteData) {
        Optional<PacienteAcc> existingPaciente = pacienteAccRepository.find("usuario.idUsuario", id).firstResultOptional();
        if (existingPaciente.isPresent()) {
            PacienteAcc paciente = existingPaciente.get();
            paciente.setApellido(pacienteData.getApellido());
            paciente.setDocumento(pacienteData.getDocumento());
            paciente.setFechaNacimiento(pacienteData.getFechaNacimiento());
            paciente.setGenero(pacienteData.getGenero());
            paciente.setTelefono(pacienteData.getTelefono());
            pacienteAccRepository.persist(paciente);
        }
    }
}
