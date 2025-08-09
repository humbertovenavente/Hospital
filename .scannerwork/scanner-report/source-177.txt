package com.unis.repository;

import com.unis.model.Doctor;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Repository class for managing {@link Doctor} entities.
 * Provides CRUD operations and query methods using Panache.
 */
@ApplicationScoped
public class DoctorRepository implements PanacheRepository<Doctor> {
}
