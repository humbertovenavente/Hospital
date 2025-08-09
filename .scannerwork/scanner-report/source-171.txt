package com.unis.repository;

import java.util.List;

import com.unis.model.Faq;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Repository class for managing {@link Faq} entities.
 * Provides CRUD operations and query methods using Panache.
 */
@ApplicationScoped
public class FaqRepository implements PanacheRepository<Faq> {

    /**
     * Finds all {@link Faq} entities with the specified status.
     *
     * @param status the status to filter by
     * @return a list of {@link Faq} entities with the given status
     */
    public List<Faq> findByStatus(String status) {
        return list("status = ?1", status);
    }

    /**
     * Finds a {@link Faq} entity by its ID.
     *
     * @param id the ID of the {@link Faq} to find
     * @return the {@link Faq} entity with the given ID, or null if not found
     */
    public Faq findById(Long id) {
        return find("id = ?1", id).firstResult();
    }
}
