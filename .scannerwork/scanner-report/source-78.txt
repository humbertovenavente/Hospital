package com.unis.service;

import java.util.List;

import com.unis.model.RecetaMedicamento;
import com.unis.repository.RecetaMedicamentoRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

/**
 * Servicio para gestionar las operaciones relacionadas con los medicamentos de recetas.
 */
@ApplicationScoped
public class RecetaMedicamentoService {

    @Inject
    RecetaMedicamentoRepository recetaMedicamentoRepository;

    /**
     * Lista los medicamentos asociados a una receta específica.
     *
     * @param idReceta El ID de la receta.
     * @return Una lista de medicamentos asociados a la receta.
     */
    public List<RecetaMedicamento> listarPorReceta(Long idReceta) {
        return recetaMedicamentoRepository.listarPorReceta(idReceta);
    }

    /**
     * Lista los medicamentos asociados a una receta específica junto con el nombre del paciente.
     *
     * @param idReceta El ID de la receta.
     * @return Una lista de objetos que contienen los datos del medicamento y el nombre del paciente.
     */
    public List<Object[]> listarPorRecetaConNombre(Long idReceta) {
        return recetaMedicamentoRepository.listarPorRecetaConNombre(idReceta);
    }

    /**
     * Agrega un medicamento a una receta.
     *
     * @param recetaMedicamento Los datos del medicamento a agregar.
     */
    @Transactional
    public void agregarMedicamentoAReceta(RecetaMedicamento recetaMedicamento) {
        recetaMedicamentoRepository.persist(recetaMedicamento);
    }

    /**
     * Elimina un medicamento de una receta por su ID.
     *
     * @param id El ID del medicamento en la receta.
     * @return true si la eliminación fue exitosa, false en caso contrario.
     */
    @Transactional
    public boolean eliminar(Long id) {
        return recetaMedicamentoRepository.deleteById(id);
    }
}
