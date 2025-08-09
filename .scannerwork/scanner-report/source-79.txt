package com.unis.service;

import java.util.List;
import java.util.Optional;

import com.unis.model.PacienteFT;
import com.unis.repository.PacienteFTRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

/**
 * Servicio para gestionar las operaciones relacionadas con las fichas técnicas de pacientes.
 */
@ApplicationScoped
public class PacienteFTService {

    @Inject
    PacienteFTRepository pacienteFTRepository;

    /**
     * Obtiene todas las fichas técnicas de pacientes registradas.
     *
     * @return Una lista de fichas técnicas de pacientes.
     */
    public List<PacienteFT> getAllPacientes() {
        return pacienteFTRepository.listAll();
    }

    /**
     * Obtiene una ficha técnica de paciente por su ID.
     *
     * @param id El ID de la ficha técnica.
     * @return Un Optional que contiene la ficha técnica si se encuentra.
     */
    public Optional<PacienteFT> getPacienteById(Long id) {
        return pacienteFTRepository.findByIdOptional(id);
    }

    /**
     * Registra una nueva ficha técnica de paciente.
     *
     * @param paciente Los datos de la ficha técnica a registrar.
     */
    @Transactional
    public void registrarPaciente(PacienteFT paciente) {
        pacienteFTRepository.persist(paciente);
    }
}
