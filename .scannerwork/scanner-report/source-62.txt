package com.unis.service;

import java.util.List;

import com.unis.model.Servicio;
import com.unis.repository.ServicioRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;

/**
 * Servicio para gestionar las operaciones relacionadas con los servicios.
 */
@ApplicationScoped
public class ServicioService {

    @Inject
    ServicioRepository servicioRepository;

    @Inject
    EntityManager entityManager; //  Se usa para `merge()`

    /**
     * Lista todos los servicios registrados.
     *
     * @return Una lista de servicios.
     */
    public List<Servicio> listarTodos() {
        return servicioRepository.listAll();
    }

    /**
     * Busca un servicio por su ID.
     *
     * @param id El ID del servicio.
     * @return El servicio correspondiente al ID, o null si no se encuentra.
     */
    public Servicio buscarPorId(Long id) {
        return servicioRepository.findById(id);
    }

    /**
     * Agrega un nuevo servicio al sistema.
     *
     * @param servicio Los datos del servicio a agregar.
     * @param parentId El ID del servicio padre, si aplica.
     * @return El servicio agregado.
     */
    @Transactional
    public Servicio agregarServicio(Servicio servicio, Long parentId) {
        if (parentId != null) {
            Servicio servicioPadre = servicioRepository.findById(parentId);
            if (servicioPadre != null) {
                servicio.servicioPadre = servicioPadre;
            }
        }
        servicioRepository.persist(servicio);
        return servicio;
    }

    /**
     * Elimina un servicio del sistema.
     *
     * @param id El ID del servicio a eliminar.
     */
    @Transactional
    public void eliminarServicio(Long id) {
        Servicio servicio = servicioRepository.findById(id);
        if (servicio != null) {
            servicioRepository.delete(servicio);
        }
    }

    /**
     * Lista los subservicios asociados a un servicio padre.
     *
     * @param id El ID del servicio padre.
     * @return Una lista de subservicios.
     */
    public List<Servicio> listarSubServicios(Long id) {
        Servicio servicioPadre = servicioRepository.findById(id);
        return servicioPadre != null ? servicioPadre.subServicios.stream().toList() : List.of();
    }

    /**
     * Agrega un subservicio a un servicio padre.
     *
     * @param servicioPadreId El ID del servicio padre.
     * @param subServicioId   El ID del subservicio.
     * @throws WebApplicationException Si no se encuentran los servicios.
     */
    @Transactional
    public void agregarSubServicio(Long servicioPadreId, Long subServicioId) {
        Servicio servicioPadre = servicioRepository.findById(servicioPadreId);
        Servicio subServicio = servicioRepository.findById(subServicioId);

        if (servicioPadre == null || subServicio == null) {
            throw new WebApplicationException("Servicio o Subservicio no encontrado", 404);
        }

    // ✅ Asignar el servicioPadre al subServicio explícitamente
    subServicio.servicioPadre = servicioPadre;

    // ✅ Guardar los cambios en la base de datos
    entityManager.merge(subServicio);
}
@Transactional
public boolean eliminarRelacion(Long servicioPadreId, Long subServicioId) {
    Servicio subServicio = servicioRepository.findById(subServicioId);

        if (subServicio != null && subServicio.servicioPadre != null && subServicio.servicioPadre.id.equals(servicioPadreId)) {
            subServicio.servicioPadre = null; // 🔹 Elimina la relación
            entityManager.merge(subServicio); // 🔹 Guarda el cambio en la base de datos
            return true;
        }
        return false; // 🔹 Si la relación no existía, devolver false
    }

}
