package com.unis.repository;

import com.unis.model.Cita;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Repository class for managing {@link Cita} entities.
 * Provides CRUD operations and query methods using Panache.
 */
@ApplicationScoped
public class CitaRepository implements PanacheRepository<Cita> {
}
