
package com.unis.model;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
/**
 * Entity representing a patient with technical records.
 * <p>
 * This entity stores patient information used for linking with technical
 * medical records (fichas técnicas) in the system. Each patient is
 * associated with a user account and can have multiple medical technical records.
 * </p>
 *
 * <p>
 * The technical records are eagerly fetched for reporting and auditing purposes.
 * </p>
 * 
 * @author Herman
 */
@Entity
@Table(name = "PACIENTE")
public class PacienteFT {

    /** The unique identifier of the patient. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PACIENTE")
    private Long idPaciente;

    /** The unique identifier of the user associated with the patient. */
    @Column(name = "ID_USUARIO", nullable = false)
    private Long idUsuario;

    /** The document identifier of the patient. */
    @Column(name = "DOCUMENTO", nullable = false)
    private String documento;

    /** The birth date of the patient. */
    @Column(name = "FECHA_NACIMIENTO")
    private LocalDate fechaNacimiento;

    /** The photograph of the patient. */
    @Lob
    @Column(name = "FOTOGRAFIA")
    private byte[] fotografia;

    /** The user entity associated with the patient. */
    @ManyToOne
    @JoinColumn(name = "ID_USUARIO", insertable = false, updatable = false)
    private Usuario usuario;

    /** The list of technical records associated with the patient. */
    @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonIgnore
    private List<FichaTecnica> fichasTecnicas;

    // ========================
    // Getters and Setters
    // ========================

    /**
     * Gets the unique identifier of the patient.
     * 
     * @return the patient ID
     */
    public Long getIdPaciente() {
        return idPaciente;
    }

    /**
     * Sets the unique identifier of the patient.
     * 
     * @param idPaciente the patient ID to set
     */
    public void setIdPaciente(Long idPaciente) {
        this.idPaciente = idPaciente;
    }

    /**
     * Gets the unique identifier of the user associated with the patient.
     * 
     * @return the user ID
     */
    public Long getIdUsuario() {
        return idUsuario;
    }

    /**
     * Sets the unique identifier of the user associated with the patient.
     * 
     * @param idUsuario the user ID to set
     */
    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    /**
     * Gets the document identifier of the patient.
     * 
     * @return the document ID
     */
    public String getDocumento() {
        return documento;
    }

    /**
     * Sets the document identifier of the patient.
     * 
     * @param documento the document ID to set
     */
    public void setDocumento(String documento) {
        this.documento = documento;
    }

    /**
     * Gets the birth date of the patient.
     * 
     * @return the birth date
     */
    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    /**
     * Sets the birth date of the patient.
     * 
     * @param fechaNacimiento the birth date to set
     */
    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    /**
     * Gets the photograph of the patient.
     * 
     * @return the photograph as a byte array
     */
    public byte[] getFotografia() {
        return fotografia;
    }

    /**
     * Sets the photograph of the patient.
     * 
     * @param fotografia the photograph as a byte array
     */
    public void setFotografia(byte[] fotografia) {
        this.fotografia = fotografia;
    }

    /**
     * Gets the user entity associated with the patient.
     * 
     * @return the user entity
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * Sets the user entity associated with the patient.
     * 
     * @param usuario the user entity to set
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    /**
     * Gets the list of technical records associated with the patient.
     * 
     * @return the list of {@link FichaTecnica}
     */
    public List<FichaTecnica> getFichasTecnicas() {
        return fichasTecnicas;
    }

    /**
     * Sets the list of technical records associated with the patient.
     * 
     * @param fichasTecnicas the list of {@link FichaTecnica} to set
     */
    public void setFichasTecnicas(List<FichaTecnica> fichasTecnicas) {
        this.fichasTecnicas = fichasTecnicas;
    }
}
