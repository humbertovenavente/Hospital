
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
 * Entity representing a user.
 * <p>
 * This entity is used to manage user accounts in the system. It includes
 * details such as username, email, password, role, and account status.
 * </p>
 */

@Entity
@Table(name = "USUARIO")
public class Usuario {

    /** The unique identifier of the user. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_USUARIO")
    private Long id;

    /** 
     * The username of the user.
     * <p>
     * This field stores the user's login name.
     * </p>
     */
    @Column(name = "NOMBRE_USUARIO", nullable = false, length = 50)
    private String nombreUsuario;

    /** 
     * The email address of the user.
     * <p>
     * This field stores the user's email, which must be unique in the system.
     * </p>
     */
    @Column(name = "CORREO", nullable = false, unique = true, length = 100)
    private String correo;

    /** 
     * The password of the user.
     * <p>
     * This field stores the user's encrypted password.
     * </p>
     */
    @Column(name = "CONTRASENA", nullable = false, length = 100)
    private String contrasena;

    /** 
     * The role associated with the user.
     * <p>
     * This field establishes a many-to-one relationship with the role entity.
     * </p>
     */
    @ManyToOne
    @JoinColumn(name = "ROL_ID", nullable = true)
    private Rol rol;

    /** 
     * The status of the user (e.g., active or inactive).
     * <p>
     * This field indicates whether the user account is currently active.
     * </p>
     */
    @Column(name = "ESTADO", nullable = false)
    private int estado = 0;

    /** 
     * The creation date of the user record.
     * <p>
     * This field stores the timestamp when the user account was created.
     * </p>
     */
    @Column(name = "FECHA_CREACTION", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreaction = new Date();

    // Getters and Setters

    /** @return the unique identifier of the user. */
    public Long getId() {
        return id;
    }

    /** @param id the unique identifier of the user. */
    public void setId(Long id) {
        this.id = id;
    }

    /** @return the username of the user. */
    public String getNombreUsuario() {
        return nombreUsuario;
    }

    /** @param nombreUsuario the username of the user. */
    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    /** @return the email address of the user. */
    public String getCorreo() {
        return correo;
    }

    /** @param correo the email address of the user. */
    public void setCorreo(String correo) {
        this.correo = correo;
    }

    /** @return the password of the user. */
    public String getContrasena() {
        return contrasena;
    }

    /** @param contrasena the password of the user. */
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    /** @return the role associated with the user. */
    public Rol getRol() {
        return rol;
    }

    /** @param rol the role associated with the user. */
    public void setRol(Rol rol) {
        this.rol = rol;
    }

    /** @return the status of the user. */
    public int getEstado() {
        return estado;
    }

    /** @param estado the status of the user. */
    public void setEstado(int estado) {
        this.estado = estado;
    }

    /** @return the creation date of the user record. */
    public Date getFechaCreaction() {
        return fechaCreaction;
    }

    /** @param fechaCreaction the creation date of the user record. */
    public void setFechaCreaction(Date fechaCreaction) {
        this.fechaCreaction = fechaCreaction;
    }
}
