
package com.unis.dto;
/**
 * Data Transfer Object (DTO) representing medicine report data.
 * <p>
 * Contains summarized data for medication usage and prescription statistics.
 * </p>
 */
public class MedicinasReporteDTO {
    /** The popularity rank of the medicine. */
    private int popularidad;

    /** The active ingredient of the medicine. */
    private String principioActivo;

    /** The total number of prescriptions for the medicine. */
    private int totalRecetas;

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

    // Getters
    public int getPopularidad() { return popularidad; }
    public String getPrincipioActivo() { return principioActivo; }
    public int getTotalRecetas() { return totalRecetas; }

    // Setters
    public void setPopularidad(int popularidad) { this.popularidad = popularidad; }
    public void setPrincipioActivo(String principioActivo) { this.principioActivo = principioActivo; }
    public void setTotalRecetas(int totalRecetas) { this.totalRecetas = totalRecetas; }
}
