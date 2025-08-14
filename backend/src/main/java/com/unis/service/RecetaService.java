package com.unis.service;

import java.util.Date;
import java.util.logging.Logger;

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

    private static final Logger logger = Logger.getLogger(RecetaService.class.getName());
    
    private static final String ERROR_ID_DOCTOR_PACIENTE_OBLIGATORIOS = "❌ Error: idDoctor e idPaciente son obligatorios.";
    private static final String ERROR_CODIGO_RECETA_OBLIGATORIO = "❌ Error: Código de receta es obligatorio.";
    private static final String ERROR_ID_PACIENTE_FALTANTE = "❌ Error: La receta no contiene un idPaciente.";
    private static final String ERROR_GUARDAR_RECETA = "❌ Error al guardar la receta: ";
    private static final String ERROR_RECETA_NO_ENCONTRADA = "❌ Error: No se encontró la receta con ID ";
    private static final String ERROR_MEDICAMENTO_NO_ENCONTRADO = "❌ Error: No se encontró el medicamento con ID ";

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
            logger.info("📌 Iniciando creación de receta...");

            // Validaciones de datos obligatorios
            if (receta.getIdDoctor() == null || receta.getIdPaciente() == null) {
                throw new IllegalArgumentException(ERROR_ID_DOCTOR_PACIENTE_OBLIGATORIOS);
            }
            if (receta.getCodigoReceta() == null || receta.getCodigoReceta().isEmpty()) {
                throw new IllegalArgumentException(ERROR_CODIGO_RECETA_OBLIGATORIO);
            }

            // Validar que el idPaciente esté presente en la receta recibida
            if (receta.getIdPaciente() == null) {
                throw new IllegalArgumentException(ERROR_ID_PACIENTE_FALTANTE);
            }

            // Log para depuración
            logger.info("📥 Receta recibida desde el hospital: " + receta);

            // Asignar fecha de creación si es null
            if (receta.getFechaCreacion() == null) {
                receta.setFechaCreacion(new Date());
            }

            // Guardar la receta en la base de datos
            em.persist(receta);
            em.flush(); // 💡 Importante para obtener el ID generado

            logger.info("✅ Receta guardada con ID: " + receta.getIdReceta());
            return receta;
        } catch (Exception e) {
            logger.severe("Error al guardar la receta: " + e.getMessage());
            throw new RuntimeException(ERROR_GUARDAR_RECETA + e.getMessage());
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
            logger.info("📌 Iniciando actualización de receta con ID: " + idReceta);

            Receta recetaExistente = em.find(Receta.class, idReceta);
            if (recetaExistente == null) {
                throw new IllegalArgumentException(ERROR_RECETA_NO_ENCONTRADA + idReceta);
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
                    throw new IllegalArgumentException(ERROR_MEDICAMENTO_NO_ENCONTRADO + med.getMedicamento().getIdMedicamento());
                }

                med.setReceta(recetaExistente);
                med.setMedicamento(medicamento);

                // ⚠️ Usar merge en lugar de persist para evitar error de detached entity
                em.merge(med);
                recetaExistente.getMedicamentos().add(med);
            }

            // 💾 Guardar cambios en la receta
            em.merge(recetaExistente);

            logger.info("✅ Receta actualizada correctamente con ID: " + idReceta);
            return recetaExistente;
        } catch (Exception e) {
            logger.severe("Error al actualizar la receta: " + e.getMessage());
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
            logger.info("📌 Iniciando adición de medicamento...");

            // Validaciones
            if (recetaMedicamento.getIdReceta() == null || recetaMedicamento.getIdMedicamento() == null) {
                throw new IllegalArgumentException("❌ Error: ID de receta y ID de medicamento son obligatorios.");
            }

            Receta receta = em.find(Receta.class, recetaMedicamento.getIdReceta());
            if (receta == null) {
                throw new IllegalArgumentException(ERROR_RECETA_NO_ENCONTRADA + recetaMedicamento.getIdReceta());
            }

            Medicamento medicamento = em.find(Medicamento.class, recetaMedicamento.getIdMedicamento());
            if (medicamento == null) {
                throw new IllegalArgumentException(ERROR_MEDICAMENTO_NO_ENCONTRADO + recetaMedicamento.getIdMedicamento());
            }

            recetaMedicamento.setReceta(receta);
            recetaMedicamento.setMedicamento(medicamento);

            // Guardar en la BD
            em.persist(recetaMedicamento);
            em.flush();

            logger.info("✅ Medicamento agregado correctamente a la receta con ID " + receta.getIdReceta());
            return recetaMedicamento;
        } catch (Exception e) {
            logger.severe("Error al agregar medicamento: " + e.getMessage());
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
            logger.info("📋 Receta encontrada: " + receta); // Log para depuración
        }
        return receta;
    }
}
