
package com.unis.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

/**
 * Entity representing a user for interconnection purposes.
 * <p>
 * This entity is used to manage user information for interconnection
 * between systems. It includes personal details, contact information,
 * and associations with hospitals.
 * </p>
 */
@Entity
@Table(name = "USUARIO_INTERCONEXION")
public class UsuarioInter {

    /** The unique identifier of the interconnection user. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_INTERCONEXION")
    private Long idInterconexion;

    /** 
     * The last name of the interconnection user.
     * <p>
     * This field stores the user's family name.
     * </p>
     */
    @Column(name = "APELLIDO", nullable = false)
    private String apellido;

    /** 
     * The document identifier of the interconnection user.
     * <p>
     * This is typically a national ID or passport number.
     * </p>
     */
    @Column(name = "DOCUMENTO", nullable = false)
    private String documento;

    /** 
     * The birth date of the interconnection user.
     * <p>
     * This field stores the user's date of birth.
     * </p>
     */
    @Column(name = "FECHA_NACIMIENTO")
    @Temporal(TemporalType.DATE)
    private Date fechaNacimiento;

    /** 
     * The gender of the interconnection user.
     * <p>
     * This field stores the user's gender (e.g., male, female, etc.).
     * </p>
     */
    @Column(name = "GENERO")
    private String genero;

    /** 
     * The phone number of the interconnection user.
     * <p>
     * This field stores the user's contact phone number.
     * </p>
     */
    @Column(name = "TELEFONO", length = 15)
    private String telefono;

    /** 
     * The ID of the hospital associated with the interconnection user.
     * <p>
     * This field links the user to a specific hospital in the system.
     * </p>
     */
    @Column(name = "ID_HOSPITAL")
    private Long idHospital;

    /** 
     * The user entity associated with the interconnection user.
     * <p>
     * This field establishes a many-to-one relationship with the main user entity.
     * </p>
     */
    @ManyToOne
    @JoinColumn(name = "ID_USUARIO", insertable = false, updatable = false)
    private Usuario usuario;

    // Getters and Setters

    /** @return the unique identifier of the interconnection user. */
    public Long getIdInterconexion() {
        return idInterconexion;
    }

    /** @param idInterconexion the unique identifier of the interconnection user. */
    public void setIdInterconexion(Long idInterconexion) {
        this.idInterconexion = idInterconexion;
    }

    /** @return the last name of the interconnection user. */
    public String getApellido() {
        return apellido;
    }

    /** @param apellido the last name of the interconnection user. */
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    /** @return the document identifier of the interconnection user. */
    public String getDocumento() {
        return documento;
    }

    /** @param documento the document identifier of the interconnection user. */
    public void setDocumento(String documento) {
        this.documento = documento;
    }

    /** @return the birth date of the interconnection user. */
    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    /** @param fechaNacimiento the birth date of the interconnection user. */
    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    /** @return the gender of the interconnection user. */
    public String getGenero() {
        return genero;
    }

    /** @param genero the gender of the interconnection user. */
    public void setGenero(String genero) {
        this.genero = genero;
    }

    /** @return the phone number of the interconnection user. */
    public String getTelefono() {
        return telefono;
    }

    /** @param telefono the phone number of the interconnection user. */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /** @return the ID of the hospital associated with the interconnection user. */
    public Long getIdHospital() {
        return idHospital;
    }

    /** @param idHospital the ID of the hospital associated with the interconnection user. */
    public void setIdHospital(Long idHospital) {
        this.idHospital = idHospital;
    }

    /** @return the user entity associated with the interconnection user. */
    public Usuario getUsuario() {
        return usuario;
    }

    /** @param usuario the user entity associated with the interconnection user. */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}

