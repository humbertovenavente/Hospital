package com.unis.controller;

import java.time.LocalTime;
import java.util.List;

import com.unis.model.Cita;
import com.unis.service.CitaService;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * Controller class responsible for managing appointment-related operations.
 * <p>
 * Handles the orchestration between the service layer and upper layers such as
 * REST resources or other application components.
 * </p>
 */
@ApplicationScoped
public class CitaController {

    @Inject
    CitaService citaService;

    /**
     * Retrieves all appointments from the system.
     *
     * @return a list of all appointments
     */
    public List<Cita> obtenerCitas() {
        return citaService.obtenerCitas();
    }

    /**
     * Retrieves an appointment by its ID.
     *
     * @param id the ID of the appointment
     * @return the appointment object, or null if not found
     */
    public Cita obtenerCitaPorId(Long id) {
        return citaService.obtenerCitaPorId(id);
    }

    /**
     * Schedules a new appointment with validation.
     * Validates that the appointment time is within allowed hours (8:00 to 16:30)
     * and that the end time is after the start time.
     *
     * @param cita the appointment to schedule
     * @throws IllegalArgumentException if the time is invalid or improperly formatted
     */
    public void agendarCita(Cita cita) {
        try {
            LocalTime horaInicio = LocalTime.parse(cita.getHoraInicio());
            LocalTime horaFin = LocalTime.parse(cita.getHoraFin());

            if (horaInicio.isBefore(LocalTime.of(8, 0)) || horaFin.isAfter(LocalTime.of(16, 30))) {
                throw new IllegalArgumentException("⚠️ Las citas solo pueden ser entre 8:00 AM y 4:30 PM.");
            }

            if (!horaFin.isAfter(horaInicio)) {
                throw new IllegalArgumentException("⚠️ La hora de fin debe ser posterior a la hora de inicio.");
            }

            citaService.agendarCita(cita);
        } catch (Exception e) {
            throw new IllegalArgumentException("⚠️ Formato de hora incorrecto. Use el formato HH:mm");
        }
    }

    /**
     * Cancels an appointment based on its ID.
     *
     * @param id the ID of the appointment to cancel
     */
    public void cancelarCita(Long id) {
        citaService.cancelarCita(id);
    }
}
