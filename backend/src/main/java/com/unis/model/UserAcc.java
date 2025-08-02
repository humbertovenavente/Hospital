
package com.unis.model;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
/**
 * Entity representing a user account.
 * <p>
 * This entity is used to manage user accounts in the system. It includes
 * details such as username, password, email, role, and account status.
 * Each user account can be associated with a doctor, employee, or patient entity.
 * </p>
 * 
 * <p>
 * The entity also tracks the creation date and the hospital associated with the user.
 * </p>
 * 
 * <p>
 * Relationships with other entities are managed using one-to-one mappings.
 * </p>
 * 
 * <p>
 * This entity is critical for authentication and authorization processes.
 * </p>
 */
@Entity
@Table(name = "USUARIO")
public class UserAcc implements Serializable {

    /** The unique identifier of the user account. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_USUARIO")
    private Long idUsuario;

    /** 
     * The username of the user account.
     * <p>
     * This field stores the user's login name.
     * </p>
     */
    @Column(name = "NOMBRE_USUARIO", nullable = false, length = 50)
    private String nombreUsuario;

    /** 
     * The password of the user account.
     * <p>
     * This field stores the user's encrypted password.
     * </p>
     */
    @Column(name = "CONTRASENA", nullable = false, length = 128)
    private String contrasena;

    /** 
     * The role ID associated with the user account.
     * <p>
     * This field links the user to a specific role in the system.
     * </p>
     */
    @Column(name = "ROL_ID", nullable = false)
    private int rolId;

    /** 
     * The email address of the user account.
     * <p>
     * This field stores the user's email, which must be unique in the system.
     * </p>
     */
    @Column(name = "CORREO", nullable = false, length = 100)
    private String correo;

    /** 
     * The status of the user account (e.g., active or inactive).
     * <p>
     * This field indicates whether the user account is currently active.
     * </p>
     */
    @Column(name = "ESTADO")
    private int estado;

    /** 
     * The creation date of the user account.
     * <p>
     * This field stores the timestamp when the user account was created.
     * </p>
     */
    @Column(name = "FECHA_CREACTION")
    @Temporal(TemporalType.DATE)
    private Date fechaCreacion;

    /** 
     * The ID of the hospital associated with the user account.
     * <p>
     * This field links the user to a specific hospital in the system.
     * </p>
     */
    @Column(name = "IDHOSPITAL")
    private Long idHospital;

    /** 
     * The doctor entity associated with the user account.
     * <p>
     * This field establishes a one-to-one relationship with the doctor entity.
     * </p>
     */
    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    private DoctorAcc doctor;

    /** 
     * The employee entity associated with the user account.
     * <p>
     * This field establishes a one-to-one relationship with the employee entity.
     * </p>
     */
    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    private EmpleadoAcc empleado;

    /** 
     * The patient entity associated with the user account.
     * <p>
     * This field establishes a one-to-one relationship with the patient entity.
     * </p>
     */
    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    private PacienteAcc paciente;

    // Getters and Setters

    /** @return the unique identifier of the user account. */
    public Long getIdUsuario() {
        return idUsuario;
    }

    /** @param idUsuario the unique identifier of the user account. */
    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    /** @return the username of the user account. */
    public String getNombreUsuario() {
        return nombreUsuario;
    }

    /** @param nombreUsuario the username of the user account. */
    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    /** @return the password of the user account. */
    public String getContrasena() {
        return contrasena;
    }

    /** @param contrasena the password of the user account. */
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    /** @return the role ID associated with the user account. */
    public int getRolId() {
        return rolId;
    }

    /** @param rolId the role ID associated with the user account. */
    public void setRolId(int rolId) {
        this.rolId = rolId;
    }

    /** @return the email address of the user account. */
    public String getCorreo() {
        return correo;
    }

    /** @param correo the email address of the user account. */
    public void setCorreo(String correo) {
        this.correo = correo;
    }

    /** @return the status of the user account. */
    public int getEstado() {
        return estado;
    }

    /** @param estado the status of the user account. */
    public void setEstado(int estado) {
        this.estado = estado;
    }

    /** @return the creation date of the user account. */
    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    /** @param fechaCreacion the creation date of the user account. */
    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    /** @return the ID of the hospital associated with the user account. */
    public Long getIdHospital() {
        return idHospital;
    }

    /** @param idHospital the ID of the hospital associated with the user account. */
    public void setIdHospital(Long idHospital) {
        this.idHospital = idHospital;
    }
}
