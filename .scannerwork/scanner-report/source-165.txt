package com.unis.repository;

import com.unis.model.Servicio;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Repository class for managing {@link Servicio} entities.
 * Provides CRUD operations and query methods using Panache.
 */
@ApplicationScoped
public class ServicioRepository implements PanacheRepository<Servicio> {
}
