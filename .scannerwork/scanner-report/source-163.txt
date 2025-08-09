package com.unis.repository;

import com.unis.model.UsuarioInterAcc;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Repository class for managing {@link UsuarioInterAcc} entities.
 * Provides CRUD operations and query methods using Panache.
 */
@ApplicationScoped
public class UsuarioInterAccRepository implements PanacheRepository<UsuarioInterAcc> {
}
