package com.unis.dto;

import java.time.LocalDate;

/**
 * Data Transfer Object (DTO) representing the results of a medical appointment.
 * <p>
 * Includes details such as the diagnosis, result notes, the appointment date,
 * and references to the original appointment ID.
 * </p>
 */
public class ResultadoDTO {

    /** The document associated with the results. */
    public String documento;

    /** The diagnosis provided during the appointment. */
    public String diagnostico;

    /** The detailed results of the appointment. */
    public String resultados;

    /** The date when the results were recorded. */
    public LocalDate fecha;

    /** The ID of the associated appointment. */
    public Long idCita;
}
