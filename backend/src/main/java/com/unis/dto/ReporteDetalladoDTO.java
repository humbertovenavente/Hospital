package com.unis.dto;

import java.time.LocalDate;
/**
 * Data Transfer Object (DTO) representing detailed report data.
 * <p>
 * Provides individual-level data for each medical consultation,
 * including date, time, patient name, and payment type.
 * </p>
 */
public class ReporteDetalladoDTO {
    /** The date of the consultation. */
    private LocalDate fecha;

    /** The time of the consultation. */
    private String horaConsulta;

    /** The name of the patient. */
    private String nombrePaciente;

    /** The type of payment used. */
    private String tipoPago;

    /**
     * Constructs a new ReporteDetalladoDTO with the specified details.
     *
     * @param fecha the date of the consultation
     * @param horaConsulta the time of the consultation
     * @param nombrePaciente the name of the patient
     * @param tipoPago the type of payment used
     */
    public ReporteDetalladoDTO(LocalDate fecha, String horaConsulta, String nombrePaciente, String tipoPago) {
        this.fecha = fecha;
        this.horaConsulta = horaConsulta;
        this.nombrePaciente = nombrePaciente;
        this.tipoPago = tipoPago;
    }

    /**
     * Gets the date of the consultation.
     *
     * @return the date of the consultation
     */
    public LocalDate getFecha() {
        return fecha;
    }

    /**
     * Sets the date of the consultation.
     *
     * @param fecha the date of the consultation
     */
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    /**
     * Gets the time of the consultation.
     *
     * @return the time of the consultation
     */
    public String getHoraConsulta() {
        return horaConsulta;
    }

    /**
     * Sets the time of the consultation.
     *
     * @param horaConsulta the time of the consultation
     */
    public void setHoraConsulta(String horaConsulta) {
        this.horaConsulta = horaConsulta;
    }

    /**
     * Gets the name of the patient.
     *
     * @return the name of the patient
     */
    public String getNombrePaciente() {
        return nombrePaciente;
    }

    /**
     * Sets the name of the patient.
     *
     * @param nombrePaciente the name of the patient
     */
    public void setNombrePaciente(String nombrePaciente) {
        this.nombrePaciente = nombrePaciente;
    }

    /**
     * Gets the type of payment used.
     *
     * @return the type of payment used
     */
    public String getTipoPago() {
        return tipoPago;
    }

    /**
     * Sets the type of payment used.
     *
     * @param tipoPago the type of payment used
     */
    public void setTipoPago(String tipoPago) {
        this.tipoPago = tipoPago;
    }
}
