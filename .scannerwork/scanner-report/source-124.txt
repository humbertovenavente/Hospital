
package com.unis.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

/**
 * Entity representing a Frequently Asked Question (FAQ).
 * <p>
 * Contains details about user-submitted questions and their corresponding answers,
 * along with metadata such as author, edit history, and moderation status.
 * </p>
 * 
 * <p><b>Status options:</b></p>
 * <ul>
 *   <li>PROCESO - In review by moderators.</li>
 *   <li>PUBLICADO - Approved and visible to users.</li>
 *   <li>RECHAZADO - Rejected due to issues (see {@code rejectionReason}).</li>
 * </ul>
 * 
 * @author Herman
 */

@Entity
@Table(name = "FAQ")
public class Faq {

    /** The unique identifier of the FAQ. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** The question text. */
    private String pregunta;

    /** The answer text. */
    @Lob
    private String respuesta;

    /** The author of the FAQ. */
    private String autor;

    /** The creation date and time of the FAQ. */
    private LocalDateTime fechaCreacion;

    /** The user who last edited the FAQ. */
    private String editadoPor;

    /** The status of the FAQ (e.g., PROCESO, PUBLICADO, RECHAZADO). */
    @Column(name = "STATUS", length = 20)
    private String status;

    /** The reason for rejection, if applicable. */
    @Column(name = "REJECTION_REASON", length = 500)
    private String rejectionReason;

    /**
     * Automatically sets the creation date and time before saving the FAQ.
     */
    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = LocalDateTime.now();
    }

    // ========================
    // Getters and Setters
    // ========================

    /** @return the unique identifier of the FAQ. */
    public Long getId() {
        return id;
    }

    /** @return the question text. */
    public String getPregunta() {
        return pregunta;
    }

    /** @param pregunta the question text. */
    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    /** @return the answer text. */
    public String getRespuesta() {
        return respuesta;
    }

    /** @param respuesta the answer text. */
    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    /** @return the author of the FAQ. */
    public String getAutor() {
        return autor;
    }

    /** @param autor the author of the FAQ. */
    public void setAutor(String autor) {
        this.autor = autor;
    }

    /** @return the creation date and time. */
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    /** @param fechaCreacion the creation date and time. */
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    /** @return the last editor of the FAQ. */
    public String getEditadoPor() {
        return editadoPor;
    }

    /** @param editadoPor the last editor of the FAQ. */
    public void setEditadoPor(String editadoPor) {
        this.editadoPor = editadoPor;
    }

    /** @return the moderation status. */
    public String getStatus() {
        return status;
    }

    /** @param status the moderation status. */
    public void setStatus(String status) {
        this.status = status;
    }

    /** @return the reason for rejection, if any. */
    public String getRejectionReason() {
        return rejectionReason;
    }

    /** @param rejectionReason the reason for rejection. */
    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }
}
