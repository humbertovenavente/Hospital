package com.unis.dto;

import java.time.LocalDate;


/**
 * Data Transfer Object (DTO) representing a request to generate reports.
 * <p>
 * Contains the criteria used to filter and determine the type of report to generate,
 * including doctor ID, date range, report type, and the requesting user.
 * </p>
 */

public class ReporteRequest {
    /** The ID of the doctor associated with the report. */
    private Long idDoctor;

    /** The start date for the report. */
    private LocalDate fechaInicio;

    /** The end date for the report. */
    private LocalDate fechaFin;

    /** The type of report ("AGRUPADO" or "DETALLADO"). */
    private String tipoReporte;

    /** The user requesting the report. */
    private String usuario;

    /** Default constructor. */
    public ReporteRequest() {}

    /**
     * Gets the ID of the doctor associated with the report.
     * 
     * @return the ID of the doctor
     */
    public Long getIdDoctor() {
        return idDoctor;
    }

    /**
     * Sets the ID of the doctor associated with the report.
     * 
     * @param idDoctor the ID of the doctor
     */
    public void setIdDoctor(Long idDoctor) {
        this.idDoctor = idDoctor;
    }

    /**
     * Gets the start date for the report.
     * 
     * @return the start date
     */
    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    /**
     * Sets the start date for the report.
     * 
     * @param fechaInicio the start date
     */
    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    /**
     * Gets the end date for the report.
     * 
     * @return the end date
     */
    public LocalDate getFechaFin() {
        return fechaFin;
    }

    /**
     * Sets the end date for the report.
     * 
     * @param fechaFin the end date
     */
    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    /**
     * Gets the type of report ("AGRUPADO" or "DETALLADO").
     * 
     * @return the type of report
     */
    public String getTipoReporte() {
        return tipoReporte;
    }

    /**
     * Sets the type of report ("AGRUPADO" or "DETALLADO").
     * 
     * @param tipoReporte the type of report
     */
    public void setTipoReporte(String tipoReporte) {
        this.tipoReporte = tipoReporte;
    }

    /**
     * Gets the user requesting the report.
     * 
     * @return the user
     */
    public String getUsuario() {
        return usuario;
    }

    /**
     * Sets the user requesting the report.
     * 
     * @param usuario the user
     */
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
}
