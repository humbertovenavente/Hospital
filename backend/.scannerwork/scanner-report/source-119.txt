package com.unis.repository;

import com.unis.model.EmpleadoAcc;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Repository class for managing {@link EmpleadoAcc} entities.
 * Provides CRUD operations and query methods using Panache.
 */
@ApplicationScoped
public class EmpleadoAccRepository implements PanacheRepository<EmpleadoAcc> {
}
