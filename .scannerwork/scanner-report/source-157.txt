
package com.unis.dto;

import java.time.LocalDate;
/**
 * Data Transfer Object (DTO) representing a medical appointment.
 * <p>
 * Used to transfer patient and appointment-related data between layers.
 * </p>
 */
public class CitaDTO {
    /** The unique identifier of the patient (DPI). */
    public String dpi;

    /** The first name of the patient. */
    public String nombre;

    /** The last name of the patient. */
    public String apellido;

    /** The date of the appointment. */
    public LocalDate fecha;

    /** The start time of the appointment. */
    public String horaInicio;

    /** The end time of the appointment. */
    public String horaFin;

    /** The reason for the appointment. */
    public String motivo;

    /** The ID of the hospital where the appointment is scheduled. */
    public Long idHospital;

    /** The ID of the service associated with the appointment. */
    public Long idServicio;

    /** The ID of the insurance company associated with the appointment. */
    public Long idAseguradora;

    /** The authorization number for the appointment, if applicable. */
    public String numeroAutorizacion;
}
