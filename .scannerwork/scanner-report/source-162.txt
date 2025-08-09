package com.unis.repository;

import com.unis.model.UserAcc;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Repository class for managing {@link UserAcc} entities.
 * Provides CRUD operations and query methods using Panache.
 */
@ApplicationScoped
public class UserAccRepository implements PanacheRepository<UserAcc> {
}
