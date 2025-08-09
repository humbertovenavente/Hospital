package com.unis.dto;

import java.time.LocalDate;


/**
 * Data Transfer Object (DTO) representing aggregated report data.
 * <p>
 * Used to summarize consultation metrics for a given date,
 * including totals by payment method (insurance or direct).
 * </p>
 */


public class ReporteAgregadoDTO {
    /** The date of the report. */
    private LocalDate fecha;

    /** The total number of consultations. */
    private Long totalConsultas;

    /** The total number of consultations covered by insurance. */
    private Long totalSeguro;

    /** The total number of consultations paid directly. */
    private Long totalDirecto;

    /**
     * Constructs a new ReporteAgregadoDTO with the specified details.
     *
     * @param fecha the date of the report
     * @param totalConsultas the total number of consultations
     * @param totalSeguro the total number of consultations covered by insurance
     * @param totalDirecto the total number of consultations paid directly
     */
    public ReporteAgregadoDTO(LocalDate fecha, Long totalConsultas, Long totalSeguro, Long totalDirecto) {
        this.fecha = fecha;
        this.totalConsultas = totalConsultas;
        this.totalSeguro = totalSeguro;
        this.totalDirecto = totalDirecto;
    }

    /**
     * Gets the date of the report.
     *
     * @return the date of the report
     */
    public LocalDate getFecha() {
        return fecha;
    }

    /**
     * Sets the date of the report.
     *
     * @param fecha the date of the report
     */
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    /**
     * Gets the total number of consultations.
     *
     * @return the total number of consultations
     */
    public Long getTotalConsultas() {
        return totalConsultas;
    }

    /**
     * Sets the total number of consultations.
     *
     * @param totalConsultas the total number of consultations
     */
    public void setTotalConsultas(Long totalConsultas) {
        this.totalConsultas = totalConsultas;
    }

    /**
     * Gets the total number of consultations covered by insurance.
     *
     * @return the total number of consultations covered by insurance
     */
    public Long getTotalSeguro() {
        return totalSeguro;
    }

    /**
     * Sets the total number of consultations covered by insurance.
     *
     * @param totalSeguro the total number of consultations covered by insurance
     */
    public void setTotalSeguro(Long totalSeguro) {
        this.totalSeguro = totalSeguro;
    }

    /**
     * Gets the total number of consultations paid directly.
     *
     * @return the total number of consultations paid directly
     */
    public Long getTotalDirecto() {
        return totalDirecto;
    }

    /**
     * Sets the total number of consultations paid directly.
     *
     * @param totalDirecto the total number of consultations paid directly
     */
    public void setTotalDirecto(Long totalDirecto) {
        this.totalDirecto = totalDirecto;
    }
}
