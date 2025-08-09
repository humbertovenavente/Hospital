package com.unis.repository;

import com.unis.model.UsuarioInter;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Repository class for managing {@link UsuarioInter} entities.
 * Provides CRUD operations and query methods using Panache.
 */
@ApplicationScoped
public class UsuarioInterconexionRepository implements PanacheRepository<UsuarioInter> {
}

