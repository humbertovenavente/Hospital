package com.unis.repository;

import com.unis.model.Agenda;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Repository class for managing {@link Agenda} entities.
 * Provides CRUD operations and query methods using Panache.
 */
@ApplicationScoped
public class AgendaRepository implements PanacheRepository<Agenda> {
   
}
