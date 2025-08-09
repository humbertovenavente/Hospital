package com.unis.repository;

import com.unis.model.Rol;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Repository class for managing {@link Rol} entities.
 * Provides CRUD operations and query methods using Panache.
 */
@ApplicationScoped
public class RolRepository implements PanacheRepository<Rol> {
    // Métodos CRUD básicos ya están implementados en PanacheRepository.
    // Si necesitas consultas personalizadas, agrégalas aquí.
}
