package com.unis.service;

import java.util.List;
import java.util.Optional;

import com.unis.model.FichaTecnica;
import com.unis.repository.FichaTecnicaRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

/**
 * Servicio para gestionar las operaciones relacionadas con las fichas técnicas.
 */
@ApplicationScoped
public class FichaTecnicaService {

    @Inject
    FichaTecnicaRepository fichaTecnicaRepository;

    /**
     * Obtiene todas las fichas técnicas registradas.
     *
     * @return Una lista de fichas técnicas.
     */
    public List<FichaTecnica> getAllFichas() {
        return fichaTecnicaRepository.listAll();
    }

    /**
     * Obtiene una ficha técnica por su ID.
     *
     * @param id El ID de la ficha técnica.
     * @return Un Optional que contiene la ficha técnica si se encuentra.
     */
    public Optional<FichaTecnica> getFichaById(Long id) {
        return fichaTecnicaRepository.findByIdOptional(id);
    }

    /**
     * Registra una nueva ficha técnica.
     *
     * @param ficha Los datos de la ficha técnica a registrar.
     */
    @Transactional
    public void registrarFicha(FichaTecnica ficha) {
        fichaTecnicaRepository.persist(ficha);
    }
}
