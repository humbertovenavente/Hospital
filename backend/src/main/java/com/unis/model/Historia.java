
package com.unis.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
/**
 * Entity representing the historical background of an institution or organization.
 * <p>
 * This class stores comprehensive historical information, notable achievements,
 * and a timeline of relevant events. It also includes moderation metadata such as
 * status, rejection reasons, and the editor responsible for the latest update.
 * </p>
 * 
 * <p><b>Database Table:</b> HISTORIA</p>
 * 
 * @author Herman
 */
@Entity
@Table(name = "HISTORIA")
public class Historia {

    /** The unique identifier of the history record. */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "historia_seq")
    @SequenceGenerator(name = "historia_seq", sequenceName = "HISTORIA_SEQ", allocationSize = 1)
    private Long id;

    /** The name of the institution or entity. */
    private String nombreEntidad;

    /** A detailed narrative of the entity's history. */
    @Lob
    private String historia;

    /** Achievements and recognitions of the entity. */
    @Lob
    private String meritos;

    /** A chronological timeline of the entity's development. */
    @Lob
    private String lineaDelTiempo;

    /** The publication status of the history record. */
    @Column(name = "STATUS", length = 20)
    private String status;

    /** The reason provided if the history entry was rejected. */
    @Column(name = "REJECTION_REASON", length = 500)
    private String rejectionReason;

    /** The email address of the editor responsible for the last update. */
    @Column(name = "EDITOR_EMAIL", length = 150)
    private String editorEmail;

    // ========================
    // Getters and Setters
    // ========================

    /** @return the unique identifier of the history record. */
    public Long getId() {
        return id;
    }

    /** @param id the unique identifier of the history record. */
    public void setId(Long id) {
        this.id = id;
    }

    /** @return the name of the entity associated with the history. */
    public String getNombreEntidad() {
        return nombreEntidad;
    }

    /** @param nombreEntidad the name of the entity associated with the history. */
    public void setNombreEntidad(String nombreEntidad) {
        this.nombreEntidad = nombreEntidad;
    }

    /** @return the detailed history of the entity. */
    public String getHistoria() {
        return historia;
    }

    /** @param historia the detailed history of the entity. */
    public void setHistoria(String historia) {
        this.historia = historia;
    }

    /** @return the merits or achievements of the entity. */
    public String getMeritos() {
        return meritos;
    }

    /** @param meritos the merits or achievements of the entity. */
    public void setMeritos(String meritos) {
        this.meritos = meritos;
    }

    /** @return the timeline of the entity's history. */
    public String getLineaDelTiempo() {
        return lineaDelTiempo;
    }

    /** @param lineaDelTiempo the timeline of the entity's history. */
    public void setLineaDelTiempo(String lineaDelTiempo) {
        this.lineaDelTiempo = lineaDelTiempo;
    }

    /** @return the status of the history record. */
    public String getStatus() {
        return status;
    }

    /** @param status the status of the history record. */
    public void setStatus(String status) {
        this.status = status;
    }

    /** @return the reason for rejection, if applicable. */
    public String getRejectionReason() {
        return rejectionReason;
    }

    /** @param rejectionReason the reason for rejection, if applicable. */
    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    /** @return the email of the editor who last modified the history record. */
    public String getEditorEmail() {
        return editorEmail;
    }

    /** @param editorEmail the email of the editor who last modified the history record. */
    public void setEditorEmail(String editorEmail) {
        this.editorEmail = editorEmail;
    }
}
