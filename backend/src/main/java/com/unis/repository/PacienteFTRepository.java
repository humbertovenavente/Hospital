package com.unis.repository;

import com.unis.model.PacienteFT;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Repository class for managing {@link PacienteFT} entities.
 * Provides CRUD operations and query methods using Panache.
 */
@ApplicationScoped
public class PacienteFTRepository implements PanacheRepository<PacienteFT> {
}
