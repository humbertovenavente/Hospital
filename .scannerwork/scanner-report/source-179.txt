package com.unis.repository;

import com.unis.model.Paciente;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Repository class for managing {@link Paciente} entities.
 * Provides CRUD operations and query methods using Panache.
 */
@ApplicationScoped
public class PacienteRepository implements PanacheRepository<Paciente> {
}
