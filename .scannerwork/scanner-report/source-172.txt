package com.unis.repository;

import java.util.List;

import com.unis.model.Historia;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Repository class for managing {@link Historia} entities.
 * Provides CRUD operations and query methods using Panache.
 */
@ApplicationScoped
public class HistoriaRepository implements PanacheRepository<Historia> {

    /**
     * Finds all {@link Historia} entities with the specified status.
     *
     * @param status the status to filter by
     * @return a list of {@link Historia} entities with the given status
     */
    public List<Historia> findByStatus(String status) {
        return list("status", status);
    }

    /**
     * Finds a {@link Historia} entity by its ID.
     *
     * @param id the ID of the {@link Historia} to find
     * @return the {@link Historia} entity with the given ID, or null if not found
     */
    public Historia findById(Long id) {
        return find("id", id).firstResult();
    }
}
