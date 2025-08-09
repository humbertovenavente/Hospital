package com.unis.repository;

import java.util.List;

import com.unis.model.PageContent;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Repository class for managing {@link PageContent} entities.
 * Provides CRUD operations and query methods using Panache.
 */
@ApplicationScoped
public class PageContentRepository implements PanacheRepository<PageContent> {

    /**
     * Finds all published {@link PageContent} entities for a specific page.
     *
     * @param pageName the name of the page
     * @return a list of published {@link PageContent} entities for the given page
     */
    public List<PageContent> findPublishedByPage(String pageName) {
        return list("pageName = ?1 and status = ?2", pageName, "PUBLICADO");
    }

    /**
     * Finds all draft {@link PageContent} entities.
     *
     * @return a list of draft {@link PageContent} entities
     */
    public List<PageContent> findDrafts() {
        return list("status = ?1", "PROCESO");
    }

    /**
     * Finds all {@link PageContent} entities with the specified status.
     *
     * @param status the status to filter by
     * @return a list of {@link PageContent} entities with the given status
     */
    public List<PageContent> findByStatus(String status) {
        return list("status = ?1", status);
    }

    /**
     * Finds a {@link PageContent} entity by its ID.
     *
     * @param id the ID of the {@link PageContent} to find
     * @return the {@link PageContent} entity with the given ID, or null if not found
     */
    public PageContent findById(Long id) {
        return find("idContent", id).firstResult();
    }
}
