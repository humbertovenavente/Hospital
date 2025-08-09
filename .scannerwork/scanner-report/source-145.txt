package com.unis.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
/**
 * Entity representing a patient.
 * <p>
 * This class maps to the <strong>PACIENTE</strong> table in the database and stores personal
 * and medical-related information about patients, including user linkage, demographic data,
 * and associated medical appointments.
 * </p>
 * 
 * <p>Each patient is linked to a {@link Usuario} entity and can have multiple {@link Cita}
 * appointments.</p>
 * 
 * <p><b>Note:</b> The patient's photo is stored as a binary large object (BLOB).</p>
 * 
 * @author Herman
 */
@Entity
@Table(name = "PACIENTE")
public class Paciente {

    /** The unique identifier of the patient. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PACIENTE")
    private Long idPaciente;

    /** The unique identifier of the user associated with the patient. */
    @Column(name = "ID_USUARIO", nullable = false, unique = true)
    private Long idUsuario;

    /** The last name of the patient. */
    @Column(name = "APELLIDO", nullable = false)
    private String apellido;

    /** The document identifier of the patient. */
    @Column(name = "DOCUMENTO", nullable = false)
    private String documento;

    /** The birth date of the patient. */
    @Column(name = "FECHA_NACIMIENTO")
    @Temporal(TemporalType.DATE)
    private Date fechaNacimiento;

    /** The gender of the patient. */
    @Column(name = "GENERO")
    private String genero;

    /** The phone number of the patient. */
    @Column(name = "TELEFONO")
    private String telefono;

    /** The photograph of the patient (stored as BLOB). */
    @Column(name = "FOTOGRAFIA")
    @Lob
    private byte[] fotografia;

    /** The user entity associated with the patient. */
    @ManyToOne
    @JoinColumn(name = "ID_USUARIO", insertable = false, updatable = false)
    private Usuario usuario;

    /** The list of appointments associated with the patient. */
    @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Cita> citas = new ArrayList<>();

    // ========================
    // Getters and Setters
    // ========================

    /** @return the unique identifier of the patient. */
    public Long getIdPaciente() {
        return idPaciente;
    }

    /** @param idPaciente the unique identifier of the patient. */
    public void setIdPaciente(Long idPaciente) {
        this.idPaciente = idPaciente;
    }

    /** @return the unique identifier of the user associated with the patient. */
    public Long getIdUsuario() {
        return idUsuario;
    }

    /** @param idUsuario the unique identifier of the user associated with the patient. */
    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    /** @return the last name of the patient. */
    public String getApellido() {
        return apellido;
    }

    /** @param apellido the last name of the patient. */
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    /** @return the document identifier of the patient. */
    public String getDocumento() {
        return documento;
    }

    /** @param documento the document identifier of the patient. */
    public void setDocumento(String documento) {
        this.documento = documento;
    }

    /** @return the birth date of the patient. */
    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    /** @param fechaNacimiento the birth date of the patient. */
    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    /** @return the gender of the patient. */
    public String getGenero() {
        return genero;
    }

    /** @param genero the gender of the patient. */
    public void setGenero(String genero) {
        this.genero = genero;
    }

    /** @return the phone number of the patient. */
    public String getTelefono() {
        return telefono;
    }

    /** @param telefono the phone number of the patient. */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /** @return the photograph of the patient. */
    public byte[] getFotografia() {
        return fotografia;
    }

    /** @param fotografia the photograph of the patient. */
    public void setFotografia(byte[] fotografia) {
        this.fotografia = fotografia;
    }

    /** @return the user entity associated with the patient. */
    public Usuario getUsuario() {
        return usuario;
    }

    /** @param usuario the user entity associated with the patient. */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    /** @return the list of appointments associated with the patient. */
    public List<Cita> getCitas() {
        return citas;
    }

    /** @param citas the list of appointments associated with the patient. */
    public void setCitas(List<Cita> citas) {
        this.citas = citas;
    }

    /** @return the full name of the patient. */
    public String getNombre() {
        return this.apellido + " " + (this.documento != null ? this.documento : "");
    }
}
