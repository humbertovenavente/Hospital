package com.unis.repository;

import java.util.List;

import com.unis.model.Receta;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.TypedQuery;

/**
 * Repository class for managing {@link Receta} entities.
 * Provides CRUD operations and query methods using Panache.
 */
@ApplicationScoped
public class RecetaRepository implements PanacheRepository<Receta> {

    /**
     * Retrieves all {@link Receta} entities associated with a specific patient.
     *
     * @param idPaciente the ID of the patient
     * @return a list of {@link Receta} entities linked to the given patient
     */
    public List<Receta> obtenerPorPaciente(Long idPaciente) {
        return list("idPaciente", idPaciente);
    }

    /**
     * Retrieves all {@link Receta} entities associated with a specific doctor.
     *
     * @param idDoctor the ID of the doctor
     * @return a list of {@link Receta} entities linked to the given doctor
     */
    public List<Receta> obtenerPorDoctor(Long idDoctor) {
        return list("idDoctor", idDoctor);
    }

    /**
     * Finds a {@link Receta} entity by the ID of its associated appointment.
     *
     * @param idCita the ID of the appointment
     * @return the {@link Receta} entity linked to the given appointment, or null if not found
     */
    public Receta buscarPorIdCita(int idCita) {
        TypedQuery<Receta> query = getEntityManager().createQuery(
            "SELECT r FROM Receta r LEFT JOIN FETCH r.medicamentos WHERE r.idCita = :idCita", Receta.class);
        query.setParameter("idCita", idCita);
        return query.getSingleResult();
    }
}
