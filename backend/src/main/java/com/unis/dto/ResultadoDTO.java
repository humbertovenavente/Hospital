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
    private String documento;

    /** The diagnosis provided during the appointment. */
    private String diagnostico;

    /** The detailed results of the appointment. */
    private String resultados;

    /** The date when the results were recorded. */
    private LocalDate fecha;

    /** The ID of the associated appointment. */
    private Long idCita;

    // Getters
    public String getDocumento() { return documento; }
    public String getDiagnostico() { return diagnostico; }
    public String getResultados() { return resultados; }
    public LocalDate getFecha() { return fecha; }
    public Long getIdCita() { return idCita; }

    // Setters
    public void setDocumento(String documento) { this.documento = documento; }
    public void setDiagnostico(String diagnostico) { this.diagnostico = diagnostico; }
    public void setResultados(String resultados) { this.resultados = resultados; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public void setIdCita(Long idCita) { this.idCita = idCita; }
}
