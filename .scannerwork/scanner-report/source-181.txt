package com.unis.repository;

import com.unis.model.Usuario;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Repository class for managing {@link Usuario} entities.
 * Provides CRUD operations and query methods using Panache.
 */
@ApplicationScoped
public class UsuarioRepository implements PanacheRepository<Usuario> {
    
    /**
     * Finds a {@link Usuario} entity by its email.
     *
     * @param correo the email to search for
     * @return the {@link Usuario} entity with the given email, or null if not found
     */
    public Usuario findByCorreo(String correo) {
        return find("correo", correo).firstResult();
    }
}
