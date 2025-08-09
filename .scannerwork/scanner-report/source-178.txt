package com.unis.repository;

import com.unis.model.Medicamento;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Repository class for managing {@link Medicamento} entities.
 * Provides CRUD operations and query methods using Panache.
 */
@ApplicationScoped
public class MedicamentoRepository implements PanacheRepository<Medicamento> {

    /**
     * Finds a {@link Medicamento} entity by its active ingredient.
     *
     * @param principioActivo the active ingredient to search for
     * @return the {@link Medicamento} entity with the given active ingredient, or null if not found
     */
    public Medicamento buscarPorNombre(String principioActivo) {
        return find("principioActivo", principioActivo).firstResult();
    }
}
