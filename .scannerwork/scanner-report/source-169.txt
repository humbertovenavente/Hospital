package com.unis.repository;

import com.unis.model.AseguradoraConexion;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AseguradoraConexionRepository implements PanacheRepository<AseguradoraConexion> {
    public AseguradoraConexion findByNombre(String nombre) {
        return find("nombre", nombre).firstResult();
    }
}
