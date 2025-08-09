package com.unis.service;

import java.util.Date;

import com.unis.model.Medicamento;
import com.unis.model.Receta;
import com.unis.model.RecetaMedicamento;
import com.unis.repository.RecetaRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

/**
 * Servicio para gestionar las operaciones relacionadas con las recetas médicas.
 */
@ApplicationScoped
public class RecetaService {

    @Inject
    EntityManager em;

    @Inject
    RecetaRepository recetaRepository;

    /**
     * Crea una nueva receta médica.
     *
     * @param receta Los datos de la receta a crear.
     * @return La receta creada.
     * @throws RuntimeException Si faltan datos obligatorios o ocurre un error al guardar.
     */
    @Transactional
    public Receta crearReceta(Receta receta) {
        try {
            System.out.println("📌 Iniciando creación de receta...");

            // Validaciones de datos obligatorios
            if (receta.getIdDoctor() == null || receta.getIdPaciente() == null) {
                throw new RuntimeException("❌ Error: idDoctor e idPaciente son obligatorios.");
            }
            if (receta.getCodigoReceta() == null || receta.getCodigoReceta().isEmpty()) {
                throw new RuntimeException("❌ Error: Código de receta es obligatorio.");
            }

            // Validar que el idPaciente esté presente en la receta recibida
            if (receta.getIdPaciente() == null) {
                throw new RuntimeException("❌ Error: La receta no contiene un idPaciente.");
            }

            // Log para depuración
            System.out.println("📥 Receta recibida desde el hospital: " + receta);

            // Asignar fecha de creación si es null
            if (receta.getFechaCreacion() == null) {
                receta.setFechaCreacion(new Date());
            }

            // Guardar la receta en la base de datos
            em.persist(receta);
            em.flush(); // 💡 Importante para obtener el ID generado

            System.out.println("✅ Receta guardada con ID: " + receta.getIdReceta());
            return receta;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("❌ Error al guardar la receta: " + e.getMessage());
        }
    }

    /**
     * Busca una receta por el ID de la cita asociada.
     *
     * @param idCita El ID de la cita.
     * @return La receta asociada a la cita, o null si no se encuentra.
     */
    @Transactional
    public Receta buscarPorIdCita(int idCita) {
        Receta receta = recetaRepository.find("idCita", idCita).firstResult();
        if (receta != null) {
            // 💡 Forzar carga de medicamentos antes de devolver la receta
            receta.getMedicamentos().size(); // Esto obliga a Hibernate a traer la lista
        }
        return receta;
    }

    /**
     * Actualiza una receta existente.
     *
     * @param idReceta         El ID de la receta a actualizar.
     * @param recetaActualizada Los nuevos datos de la receta.
     * @return La receta actualizada.
     * @throws RuntimeException Si no se encuentra la receta o ocurre un error al actualizar.
     */
    @Transactional
    public Receta actualizarReceta(Long idReceta, Receta recetaActualizada) {
        try {
            System.out.println("📌 Iniciando actualización de receta con ID: " + idReceta);

            Receta recetaExistente = em.find(Receta.class, idReceta);
            if (recetaExistente == null) {
                throw new RuntimeException("❌ Error: No se encontró la receta con ID " + idReceta);
            }

            // ⚡ Actualizar solo los campos editables
            recetaExistente.setAnotaciones(recetaActualizada.getAnotaciones());
            recetaExistente.setNotasEspeciales(recetaActualizada.getNotasEspeciales());

            // ⚡ Eliminar medicamentos anteriores
            recetaExistente.getMedicamentos().clear();
            em.flush(); // 🔥 Necesario para aplicar el cambio antes de agregar nuevos medicamentos

            // ⚡ Agregar medicamentos actualizados
            for (RecetaMedicamento med : recetaActualizada.getMedicamentos()) {
                Medicamento medicamento = em.find(Medicamento.class, med.getMedicamento().getIdMedicamento());
                if (medicamento == null) {
                    throw new RuntimeException("❌ Error: No se encontró el medicamento con ID " + med.getMedicamento().getIdMedicamento());
                }

                med.setReceta(recetaExistente);
                med.setMedicamento(medicamento);

                // ⚠️ Usar merge en lugar de persist para evitar error de detached entity
                em.merge(med);
                recetaExistente.getMedicamentos().add(med);
            }

            // 💾 Guardar cambios en la receta
            em.merge(recetaExistente);

            System.out.println("✅ Receta actualizada correctamente con ID: " + idReceta);
            return recetaExistente;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("❌ Error al actualizar la receta: " + e.getMessage());
        }
    }

    /**
     * Agrega un medicamento a una receta existente.
     *
     * @param recetaMedicamento Los datos del medicamento a agregar.
     * @return El medicamento agregado a la receta.
     * @throws RuntimeException Si faltan datos obligatorios o ocurre un error al agregar.
     */
    @Transactional
    public RecetaMedicamento agregarMedicamento(RecetaMedicamento recetaMedicamento) {
        try {
            System.out.println("📌 Iniciando adición de medicamento...");

            // Validaciones
            if (recetaMedicamento.getIdReceta() == null || recetaMedicamento.getIdMedicamento() == null) {
                throw new RuntimeException("❌ Error: ID de receta y ID de medicamento son obligatorios.");
            }

            Receta receta = em.find(Receta.class, recetaMedicamento.getIdReceta());
            if (receta == null) {
                throw new RuntimeException("❌ Error: No se encontró la receta con ID " + recetaMedicamento.getIdReceta());
            }

            Medicamento medicamento = em.find(Medicamento.class, recetaMedicamento.getIdMedicamento());
            if (medicamento == null) {
                throw new RuntimeException("❌ Error: No se encontró el medicamento con ID " + recetaMedicamento.getIdMedicamento());
            }

            recetaMedicamento.setReceta(receta);
            recetaMedicamento.setMedicamento(medicamento);

            // Guardar en la BD
            em.persist(recetaMedicamento);
            em.flush();

            System.out.println("✅ Medicamento agregado correctamente a la receta con ID " + receta.getIdReceta());
            return recetaMedicamento;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("❌ Error al agregar medicamento: " + e.getMessage());
        }
    }

    /**
     * Busca una receta por su código único.
     *
     * @param codigoReceta El código de la receta.
     * @return La receta correspondiente al código, o null si no se encuentra.
     */
    public Receta buscarPorCodigo(String codigoReceta) {
        Receta receta = recetaRepository.find("codigoReceta", codigoReceta).firstResult();
        if (receta != null) {
            // Forzar la carga de datos relacionados, como el idPaciente
            receta.getIdPaciente(); // Asegura que el idPaciente esté cargado
            System.out.println("📋 Receta encontrada: " + receta); // Log para depuración
        }
        return receta;
    }
}
