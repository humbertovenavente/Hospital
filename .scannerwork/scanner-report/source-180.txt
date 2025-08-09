package com.unis.repository;

import com.unis.model.PacienteAcc;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Repository class for managing {@link PacienteAcc} entities.
 * Provides CRUD operations and query methods using Panache.
 */
@ApplicationScoped
public class PacienteAccRepository implements PanacheRepository<PacienteAcc> {
}
