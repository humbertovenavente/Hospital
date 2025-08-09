
package com.unis.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
/**
 * Entity representing a hospital within the system.
 * <p>
 * Stores core hospital details such as contact information, registration state,
 * and creation timestamp. Also includes the reference to its MongoDB representation,
 * if applicable.
 * </p>
 * 
 * <p><b>Database Table:</b> HOSPITAL</p>
 * 
 * @author Herman
 */
@Entity
@Table(name = "HOSPITAL")
public class Hospital {

    /** The unique identifier of the hospital. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_HOSPITAL")
    private Long id;

    /** The name of the hospital. */
    @Column(name = "NOMBRE", nullable = false, length = 100)
    private String nombre;

    /** The physical address of the hospital. */
    @Column(name = "DIRECCION", nullable = false, length = 200)
    private String direccion;

    /** The phone number of the hospital. */
    @Column(name = "TELEFONO", length = 15)
    private String telefono;

    /** The official email address of the hospital. */
    @Column(name = "CORREO", length = 100)
    private String correo;

    /** The current operational status of the hospital (e.g., active, inactive). */
    @Column(name = "ESTADO", length = 1)
    private String estado;

    /** The timestamp when the hospital record was created. */
    @Column(name = "FECHA_CREACION")
    private LocalDateTime fechaCreacion;

    /** The corresponding MongoDB ID if the hospital is mirrored in a NoSQL database. */
    @Column(name = "MONGO_ID", length = 50)
    private String mongoId;

    // ========================
    // Getters and Setters
    // ========================

    /** @return the unique identifier of the hospital. */
    public Long getId() {
        return id;
    }

    /** @param id the unique identifier of the hospital. */
    public void setId(Long id) {
        this.id = id;
    }

    /** @return the name of the hospital. */
    public String getNombre() {
        return nombre;
    }

    /** @param nombre the name of the hospital. */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /** @return the address of the hospital. */
    public String getDireccion() {
        return direccion;
    }

    /** @param direccion the address of the hospital. */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    /** @return the phone number of the hospital. */
    public String getTelefono() {
        return telefono;
    }

    /** @param telefono the phone number of the hospital. */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /** @return the email address of the hospital. */
    public String getCorreo() {
        return correo;
    }

    /** @param correo the email address of the hospital. */
    public void setCorreo(String correo) {
        this.correo = correo;
    }

    /** @return the status of the hospital. */
    public String getEstado() {
        return estado;
    }

    /** @param estado the status of the hospital. */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /** @return the creation date and time of the hospital record. */
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    /** @param fechaCreacion the creation date and time of the hospital record. */
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    /** @return the MongoDB identifier associated with the hospital. */
    public String getMongoId() {
        return mongoId;
    }

    /** @param mongoId the MongoDB identifier associated with the hospital. */
    public void setMongoId(String mongoId) {
        this.mongoId = mongoId;
    }
}
