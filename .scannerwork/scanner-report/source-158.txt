package com.unis.dto;
/**
 * Data Transfer Object (DTO) representing moderation report data.
 * <p>
 * Used for summarizing moderation actions such as rejections
 * and tracking the user responsible for them.
 * </p>
 */
public class ModeracionReporteDTO {
    /** The order number of the moderation report. */
    private int numeroOrden;

    /** The user associated with the moderation report. */
    private String usuario;

    /** The total number of rejections. */
    private int totalRechazos;

    /**
     * Default constructor for ModeracionReporteDTO.
     */
    public ModeracionReporteDTO() {}

    /**
     * Constructor for ModeracionReporteDTO with parameters.
     * 
     * @param numeroOrden the order number of the moderation report
     * @param usuario the user associated with the moderation report
     * @param totalRechazos the total number of rejections
     */
    public ModeracionReporteDTO(int numeroOrden, String usuario, int totalRechazos) {
        this.numeroOrden = numeroOrden;
        this.usuario = usuario;
        this.totalRechazos = totalRechazos;
    }

    /**
     * Gets the order number of the moderation report.
     * 
     * @return the order number
     */
    public int getNumeroOrden() { return numeroOrden; }

    /**
     * Sets the order number of the moderation report.
     * 
     * @param numeroOrden the order number to set
     */
    public void setNumeroOrden(int numeroOrden) { this.numeroOrden = numeroOrden; }

    /**
     * Gets the user associated with the moderation report.
     * 
     * @return the user
     */
    public String getUsuario() { return usuario; }

    /**
     * Sets the user associated with the moderation report.
     * 
     * @param usuario the user to set
     */
    public void setUsuario(String usuario) { this.usuario = usuario; }

    /**
     * Gets the total number of rejections.
     * 
     * @return the total number of rejections
     */
    public int getTotalRechazos() { return totalRechazos; }

    /**
     * Sets the total number of rejections.
     * 
     * @param totalRechazos the total number of rejections to set
     */
    public void setTotalRechazos(int totalRechazos) { this.totalRechazos = totalRechazos; }
}
