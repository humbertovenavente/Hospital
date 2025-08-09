package com.unis.repository;

import com.unis.model.FichaTecnica;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Repository class for managing {@link FichaTecnica} entities.
 * Provides CRUD operations and query methods using Panache.
 */
@ApplicationScoped
public class FichaTecnicaRepository implements PanacheRepository<FichaTecnica> {
}
