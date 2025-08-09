package com.unis.repository;

import com.unis.model.DoctorAcc;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Repository class for managing {@link DoctorAcc} entities.
 * Provides CRUD operations and query methods using Panache.
 */
@ApplicationScoped
public class DoctorAccRepository implements PanacheRepository<DoctorAcc> {
}
