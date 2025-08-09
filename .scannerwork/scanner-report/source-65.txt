package com.unis.service;

import java.util.List;

import com.unis.model.Faq;
import com.unis.repository.FaqRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

/**
 * Servicio para gestionar las operaciones relacionadas con las preguntas frecuentes (FAQs).
 */
@ApplicationScoped
public class FaqService {

    @Inject
    FaqRepository faqRepository;

    /**
     * Guarda una nueva pregunta frecuente.
     *
     * @param faq Los datos de la pregunta frecuente a guardar.
     * @throws IllegalArgumentException Si el campo 'editadoPor' no está presente.
     */
    @Transactional
    public void guardarPregunta(Faq faq) {
        if (faq.getEditadoPor() == null || faq.getEditadoPor().isBlank()) {
            throw new IllegalArgumentException("El campo 'editadoPor' es obligatorio.");
        }
        faqRepository.persist(faq);
    }

    /**
     * Lista todas las preguntas frecuentes registradas.
     *
     * @return Una lista de preguntas frecuentes.
     */
    public List<Faq> listarPreguntas() {
        return faqRepository.listAll();
    }

    /**
     * Busca una pregunta frecuente por su ID.
     *
     * @param id El ID de la pregunta frecuente.
     * @return La pregunta frecuente correspondiente al ID, o null si no se encuentra.
     */
    public Faq buscarPorId(Long id) {
        return faqRepository.findById(id);
    }

    /**
     * Actualiza una pregunta frecuente existente.
     *
     * @param faq Los nuevos datos de la pregunta frecuente.
     * @throws IllegalArgumentException Si el campo 'editadoPor' no está presente.
     */
    @Transactional
    public void actualizarFaq(Faq faq) {
        if (faq.getEditadoPor() == null || faq.getEditadoPor().isBlank()) {
            throw new IllegalArgumentException("El campo 'editadoPor' es obligatorio para actualizar.");
        }
        faqRepository.getEntityManager().merge(faq);
    }

    /**
     * Elimina una pregunta frecuente por su ID.
     *
     * @param id El ID de la pregunta frecuente a eliminar.
     * @return true si la eliminación fue exitosa, false en caso contrario.
     */
    @Transactional
    public boolean eliminarFaq(Long id) {
        return faqRepository.deleteById(id);
    }

    /**
     * Lista las preguntas frecuentes filtradas por estado.
     *
     * @param estado El estado de las preguntas frecuentes (PROCESO, PUBLICADO, RECHAZADO).
     * @return Una lista de preguntas frecuentes con el estado especificado.
     */
    public List<Faq> listarPorEstado(String estado) {
        return faqRepository.findByStatus(estado);
    }
}
