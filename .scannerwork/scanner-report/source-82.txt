package com.unis.service;

import java.sql.Timestamp;
import java.util.List;

import com.unis.model.PageContent;
import com.unis.repository.PageContentRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

/**
 * Servicio para gestionar las operaciones relacionadas con el contenido de las páginas.
 */
@ApplicationScoped
public class PageContentService {

    @Inject
    PageContentRepository repository;

    /**
     * Obtiene el contenido publicado para una página específica.
     *
     * @param pageName El nombre de la página.
     * @return Una lista de contenido publicado para la página.
     */
    public List<PageContent> getPublishedContent(String pageName) {
        return repository.findPublishedByPage(pageName);
    }

    /**
     * Obtiene el contenido en estado borrador (PROCESO).
     *
     * @return Una lista de contenido en estado borrador.
     */
    public List<PageContent> getDraftContent() {
        return repository.findDrafts();
    }

    /**
     * Obtiene el contenido filtrado por estado.
     *
     * @param status El estado del contenido (PROCESO, PUBLICADO, RECHAZADO).
     * @return Una lista de contenido con el estado especificado.
     */
    public List<PageContent> getByStatus(String status) {
        return repository.findByStatus(status);
    }

    /**
     * Busca un contenido por su ID.
     *
     * @param id El ID del contenido.
     * @return El contenido correspondiente al ID, o null si no se encuentra.
     */
    public PageContent findById(Long id) {
        return repository.findById(id);
    }

    /**
     * Crea un nuevo contenido.
     *
     * @param content Los datos del contenido a crear.
     * @return El contenido creado.
     * @throws IllegalArgumentException Si falta el email del editor.
     */
    @Transactional
    public PageContent create(PageContent content) {
        content.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
        if (content.getStatus() == null) {
            content.setStatus("PROCESO");
        }
        if (content.getEditorEmail() == null) {
            throw new IllegalArgumentException("Editor email es requerido");
        }
        repository.persist(content);
        return content;
    }

    /**
     * Actualiza un contenido existente.
     *
     * @param id             El ID del contenido a actualizar.
     * @param updatedContent Los nuevos datos del contenido.
     * @return El contenido actualizado.
     * @throws NotFoundException Si no se encuentra el contenido.
     */
    @Transactional
    public PageContent update(Long id, PageContent updatedContent) {
        PageContent existing = repository.findById(id);
        if (existing == null) {
            throw new NotFoundException("Contenido no encontrado");
        }

        // 🔁 Si el contenido actual está PUBLICADO, crear una nueva versión en estado PROCESO
        if ("PUBLICADO".equals(existing.getStatus())) {
            PageContent nuevo = new PageContent();
            nuevo.setPageName(updatedContent.getPageName());
            nuevo.setSectionName(updatedContent.getSectionName());
            nuevo.setContentTitle(updatedContent.getContentTitle());
            nuevo.setContentBody(updatedContent.getContentBody());
            nuevo.setImage(updatedContent.getImage());
            nuevo.setModifiedBy(updatedContent.getModifiedBy());
            nuevo.setEditorEmail(updatedContent.getEditorEmail());
            nuevo.setStatus("PROCESO");
            nuevo.setRejectionReason(null);
            nuevo.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
            repository.persist(nuevo);
            return nuevo;
        }

        // Si está en PROCESO o RECHAZADO, actualizamos el mismo registro
        existing.setPageName(updatedContent.getPageName());
        existing.setSectionName(updatedContent.getSectionName());
        existing.setContentTitle(updatedContent.getContentTitle());
        existing.setContentBody(updatedContent.getContentBody());
        existing.setImage(updatedContent.getImage());
        existing.setModifiedBy(updatedContent.getModifiedBy());
        existing.setEditorEmail(updatedContent.getEditorEmail());
        existing.setStatus(updatedContent.getStatus());
        existing.setRejectionReason(updatedContent.getRejectionReason());
        existing.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));

        return existing;
    }

    /**
     * Publica un contenido cambiando su estado a PUBLICADO.
     *
     * @param id El ID del contenido a publicar.
     * @return El contenido publicado.
     * @throws NotFoundException Si no se encuentra el contenido.
     */
    @Transactional
    public PageContent publish(Long id) {
        PageContent existing = repository.findById(id);
        if (existing == null) {
            throw new NotFoundException("Contenido no encontrado");
        }

        existing.setStatus("PUBLICADO");
        existing.setRejectionReason(null);
        existing.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
        return existing;
    }

    /**
     * Rechaza un contenido con un motivo.
     *
     * @param id     El ID del contenido a rechazar.
     * @param motivo El motivo del rechazo.
     * @return El contenido rechazado.
     * @throws NotFoundException Si no se encuentra el contenido.
     */
    @Transactional
    public PageContent reject(Long id, String motivo) {
        PageContent existing = repository.findById(id);
        if (existing == null) {
            throw new NotFoundException("Contenido no encontrado");
        }

        existing.setStatus("RECHAZADO");
        existing.setRejectionReason(motivo);
        existing.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
        return existing;
    }

    /**
     * Elimina un contenido por su ID.
     *
     * @param id El ID del contenido a eliminar.
     */
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
