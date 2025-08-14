
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
    private String dpi;

    /** The first name of the patient. */
    private String nombre;

    /** The last name of the patient. */
    private String apellido;

    /** The date of the appointment. */
    private LocalDate fecha;

    /** The start time of the appointment. */
    private String horaInicio;

    /** The end time of the appointment. */
    private String horaFin;

    /** The reason for the appointment. */
    private String motivo;

    /** The ID of the hospital where the appointment is scheduled. */
    private Long idHospital;

    /** The ID of the service associated with the appointment. */
    private Long idServicio;

    /** The ID of the insurance company associated with the appointment. */
    private Long idAseguradora;

    /** The authorization number for the appointment, if applicable. */
    private String numeroAutorizacion;

    // Getters
    public String getDpi() { return dpi; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public LocalDate getFecha() { return fecha; }
    public String getHoraInicio() { return horaInicio; }
    public String getHoraFin() { return horaFin; }
    public String getMotivo() { return motivo; }
    public Long getIdHospital() { return idHospital; }
    public Long getIdServicio() { return idServicio; }
    public Long getIdAseguradora() { return idAseguradora; }
    public String getNumeroAutorizacion() { return numeroAutorizacion; }

    // Setters
    public void setDpi(String dpi) { this.dpi = dpi; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public void setHoraInicio(String horaInicio) { this.horaInicio = horaInicio; }
    public void setHoraFin(String horaFin) { this.horaFin = horaFin; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    public void setIdHospital(Long idHospital) { this.idHospital = idHospital; }
    public void setIdServicio(Long idServicio) { this.idServicio = idServicio; }
    public void setIdAseguradora(Long idAseguradora) { this.idAseguradora = idAseguradora; }
    public void setNumeroAutorizacion(String numeroAutorizacion) { this.numeroAutorizacion = numeroAutorizacion; }
}
