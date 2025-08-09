
package com.unis.model;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
/**
 * Entity representing a patient.
 * <p>
 * This entity maps to the <strong>PACIENTE</strong> table and holds patient demographic data 
 * and hospital association information. It is designed to be used with user account linkage through {@link UserAcc}.
 * </p>
 *
 * <p>
 * The patient is uniquely identified by a generated ID and must be linked to a user account.
 * </p>
 * 
 * @author Herman
 */
@Entity
@Table(name = "PACIENTE")
public class PacienteAcc implements Serializable {

    /** The unique identifier of the patient. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PACIENTE")
    private Long idPaciente;

    /** The user account associated with the patient. */
    @OneToOne
    @JoinColumn(name = "ID_USUARIO", nullable = false)
    private UserAcc usuario;

    /** The last name of the patient. */
    @Column(name = "APELLIDO", nullable = false, length = 100)
    private String apellido;

    /** The document identifier of the patient. */
    @Column(name = "DOCUMENTO", nullable = false, length = 20)
    private String documento;

    /** The birth date of the patient. */
    @Column(name = "FECHA_NACIMIENTO")
    @Temporal(TemporalType.DATE)
    private Date fechaNacimiento;

    /** The gender of the patient. */
    @Column(name = "GENERO", length = 10)
    private String genero;

    /** The phone number of the patient. */
    @Column(name = "TELEFONO", length = 15)
    private String telefono;

    /** The ID of the hospital associated with the patient. */
    @Column(name = "ID_HOSPITAL")
    private Long idHospital;

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
     * Gets the user account associated with the patient.
     * 
     * @return the associated user
     */
    public UserAcc getUsuario() {
        return usuario;
    }

    /**
     * Sets the user account associated with the patient.
     * 
     * @param usuario the associated user to set
     */
    public void setUsuario(UserAcc usuario) {
        this.usuario = usuario;
    }

    /**
     * Gets the last name of the patient.
     * 
     * @return the patient's last name
     */
    public String getApellido() {
        return apellido;
    }

    /**
     * Sets the last name of the patient.
     * 
     * @param apellido the last name to set
     */
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    /**
     * Gets the document identifier of the patient.
     * 
     * @return the patient's document ID
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
    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    /**
     * Sets the birth date of the patient.
     * 
     * @param fechaNacimiento the birth date to set
     */
    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    /**
     * Gets the gender of the patient.
     * 
     * @return the gender
     */
    public String getGenero() {
        return genero;
    }

    /**
     * Sets the gender of the patient.
     * 
     * @param genero the gender to set
     */
    public void setGenero(String genero) {
        this.genero = genero;
    }

    /**
     * Gets the phone number of the patient.
     * 
     * @return the phone number
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Sets the phone number of the patient.
     * 
     * @param telefono the phone number to set
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * Gets the hospital ID associated with the patient.
     * 
     * @return the hospital ID
     */
    public Long getIdHospital() {
        return idHospital;
    }

    /**
     * Sets the hospital ID associated with the patient.
     * 
     * @param idHospital the hospital ID to set
     */
    public void setIdHospital(Long idHospital) {
        this.idHospital = idHospital;
    }
}
