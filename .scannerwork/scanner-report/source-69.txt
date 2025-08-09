package com.unis.service;

import java.util.List;

import com.unis.model.Historia;
import com.unis.repository.HistoriaRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

/**
 * Servicio para gestionar las operaciones relacionadas con las historias.
 */
@ApplicationScoped
public class HistoriaService {

    @Inject
    HistoriaRepository historiaRepository;

    /**
     * Lista todas las historias registradas.
     *
     * @return Una lista de historias.
     */
    public List<Historia> listar() {
        return historiaRepository.listAll();
    }

    /**
     * Lista las historias filtradas por estado.
     *
     * @param estado El estado de las historias (PROCESO, PUBLICADO, RECHAZADO).
     * @return Una lista de historias con el estado especificado.
     */
    public List<Historia> listarPorEstado(String estado) {
        return historiaRepository.findByStatus(estado);
    }

    /**
     * Obtiene una historia por su ID.
     *
     * @param id El ID de la historia.
     * @return La historia correspondiente al ID, o null si no se encuentra.
     */
    public Historia obtenerPorId(Long id) {
        return historiaRepository.findById(id);
    }

    /**
     * Crea una nueva historia.
     *
     * @param historia Los datos de la historia a crear.
     * @return La historia creada.
     * @throws IllegalArgumentException Si el email del editor no está presente.
     */
    @Transactional
    public Historia crear(Historia historia) {
        if (historia.getEditorEmail() == null) {
            throw new IllegalArgumentException("El email del editor es obligatorio.");
        }
        historia.setStatus("PROCESO");
        historiaRepository.persist(historia);
        return historia;
    }

    /**
     * Actualiza una historia existente.
     *
     * @param id                El ID de la historia a actualizar.
     * @param historiaActualizada Los nuevos datos de la historia.
     * @return La historia actualizada.
     * @throws NotFoundException Si no se encuentra la historia.
     */
    @Transactional
    public Historia actualizar(Long id, Historia historiaActualizada) {
        Historia historia = historiaRepository.findById(id);
        if (historia == null) {
            throw new NotFoundException("Historia no encontrada");
        }

        historia.setNombreEntidad(historiaActualizada.getNombreEntidad());
        historia.setHistoria(historiaActualizada.getHistoria());
        historia.setMeritos(historiaActualizada.getMeritos());
        historia.setLineaDelTiempo(historiaActualizada.getLineaDelTiempo());
        historia.setStatus(historiaActualizada.getStatus());
        historia.setRejectionReason(historiaActualizada.getRejectionReason());
        historia.setEditorEmail(historiaActualizada.getEditorEmail());

        return historia;
    }

    /**
     * Aprueba una historia cambiando su estado a PUBLICADO.
     *
     * @param id El ID de la historia a aprobar.
     * @return La historia aprobada.
     * @throws NotFoundException Si no se encuentra la historia.
     */
    @Transactional
    public Historia aprobar(Long id) {
        Historia historia = historiaRepository.findById(id);
        if (historia == null) {
            throw new NotFoundException("Historia no encontrada");
        }
        historia.setStatus("PUBLICADO");
        historia.setRejectionReason(null);
        return historia;
    }

    /**
     * Rechaza una historia con un motivo.
     *
     * @param id     El ID de la historia a rechazar.
     * @param motivo El motivo del rechazo.
     * @return La historia rechazada.
     * @throws NotFoundException Si no se encuentra la historia.
     */
    @Transactional
    public Historia rechazar(Long id, String motivo) {
        Historia historia = historiaRepository.findById(id);
        if (historia == null) {
            throw new NotFoundException("Historia no encontrada");
        }
        historia.setStatus("RECHAZADO");
        historia.setRejectionReason(motivo);
        return historia;
    }

    /**
     * Elimina una historia por su ID.
     *
     * @param id El ID de la historia a eliminar.
     * @return true si la eliminación fue exitosa, false en caso contrario.
     */
    @Transactional
    public boolean eliminar(Long id) {
        return historiaRepository.deleteById(id);
    }
}
