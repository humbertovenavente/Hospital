package com.unis.service;

import java.util.List;

import com.unis.model.Medicamento;
import com.unis.repository.MedicamentoRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

/**
 * Servicio para gestionar las operaciones relacionadas con los medicamentos.
 */
@ApplicationScoped
public class MedicamentoService {

    @Inject
    MedicamentoRepository medicamentoRepository;

    /**
     * Lista todos los medicamentos registrados.
     *
     * @return Una lista de medicamentos.
     */
    public List<Medicamento> listarTodos() {
        return medicamentoRepository.listAll();
    }

    /**
     * Obtiene un medicamento por su ID.
     *
     * @param id El ID del medicamento.
     * @return El medicamento correspondiente al ID, o null si no se encuentra.
     */
    public Medicamento obtenerPorId(Long id) {
        return medicamentoRepository.findById(id);
    }

    /**
     * Crea un nuevo medicamento.
     *
     * @param medicamento Los datos del medicamento a crear.
     * @return El medicamento creado.
     */
    @Transactional
    public Medicamento crearMedicamento(Medicamento medicamento) {
        medicamentoRepository.persist(medicamento);
        return medicamento;
    }

    /**
     * Actualiza un medicamento existente.
     *
     * @param id              El ID del medicamento a actualizar.
     * @param medicamentoNuevo Los nuevos datos del medicamento.
     * @return El medicamento actualizado, o null si no se encuentra.
     */
    @Transactional
    public Medicamento actualizarMedicamento(Long id, Medicamento medicamentoNuevo) {
        Medicamento existente = medicamentoRepository.findById(id);
        if (existente == null) {
            return null;
        }
        // Actualizar los campos del medicamento existente
        existente.setPrincipioActivo(medicamentoNuevo.getPrincipioActivo());
        existente.setConcentracion(medicamentoNuevo.getConcentracion());
        existente.setPresentacion(medicamentoNuevo.getPresentacion());
        existente.setFormaFarmaceutica(medicamentoNuevo.getFormaFarmaceutica());
        existente.setVentaLibre(medicamentoNuevo.getVentaLibre());
        return existente;
    }

    /**
     * Elimina un medicamento por su ID.
     *
     * @param id El ID del medicamento a eliminar.
     * @return true si la eliminación fue exitosa, false en caso contrario.
     */
    @Transactional
    public boolean eliminarMedicamento(Long id) {
        return medicamentoRepository.deleteById(id);
    }
}
