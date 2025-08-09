
package com.unis.dto;
/**
 * Data Transfer Object (DTO) representing medicine report data.
 * <p>
 * Contains summarized data for medication usage and prescription statistics.
 * </p>
 */
public class MedicinasReporteDTO {
    /** The popularity rank of the medicine. */
    public int popularidad;

    /** The active ingredient of the medicine. */
    public String principioActivo;

    /** The total number of prescriptions for the medicine. */
    public int totalRecetas;

    /**
     * Constructs a new MedicinasReporteDTO with the specified details.
     *
     * @param popularidad the popularity rank of the medicine
     * @param principioActivo the active ingredient of the medicine
     * @param totalRecetas the total number of prescriptions for the medicine
     */
    public MedicinasReporteDTO(int popularidad, String principioActivo, int totalRecetas) {
        this.popularidad = popularidad;
        this.principioActivo = principioActivo;
        this.totalRecetas = totalRecetas;
    }
}
