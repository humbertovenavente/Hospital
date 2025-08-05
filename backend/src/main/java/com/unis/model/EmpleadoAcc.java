
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
 * Entity representing an employee (alternative schema).
 * <p>
 * Stores personal information, employment details, and references a user account and hospital.
 * This version uses {@link UserAcc} for the associated user.
 * </p>
 * 
 * @author Herman
 */
@Entity
@Table(name = "EMPLEADOS")
public class EmpleadoAcc implements Serializable {

    /** The unique identifier of the employee. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_EMPLEADO")
    private Long idEmpleado;

    /** The user account associated with the employee. */
    @OneToOne
    @JoinColumn(name = "ID_USUARIO", nullable = false)
    private UserAcc usuario;

    /** The last name of the employee. */
    @Column(name = "APELLIDO", nullable = false, length = 100)
    private String apellido;

    /** The document identifier of the employee. */
    @Column(name = "DOCUMENTO", nullable = false, length = 20)
    private String documento;

    /** The birth date of the employee. */
    @Column(name = "FECHA_NACIMIENTO")
    @Temporal(TemporalType.DATE)
    private Date fechaNacimiento;

    /** The gender of the employee. */
    @Column(name = "GENERO", length = 10)
    private String genero;

    /** The phone number of the employee. */
    @Column(name = "TELEFONO", length = 15)
    private String telefono;

    /** The ID of the hospital where the employee works. */
    @Column(name = "ID_HOSPITAL")
    private Long idHospital;

    /** The job position of the employee. */
    @Column(name = "PUESTO", length = 50)
    private String puesto;

    // =======================
    // Getters and Setters
    // =======================

    /** @return the unique identifier of the employee */
    public Long getIdEmpleado() {
        return idEmpleado;
    }

    /** @param idEmpleado the employee ID to set */
    public void setIdEmpleado(Long idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    /** @return the user account associated with the employee */
    public UserAcc getUsuario() {
        return usuario;
    }

    /** @param usuario the user account to associate */
    public void setUsuario(UserAcc usuario) {
        this.usuario = usuario;
    }

    /** @return the last name of the employee */
    public String getApellido() {
        return apellido;
    }

    /** @param apellido the last name to set */
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    /** @return the document identifier */
    public String getDocumento() {
        return documento;
    }

    /** @param documento the document identifier to set */
    public void setDocumento(String documento) {
        this.documento = documento;
    }

    /** @return the birth date */
    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    /** @param fechaNacimiento the birth date to set */
    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    /** @return the gender */
    public String getGenero() {
        return genero;
    }

    /** @param genero the gender to set */
    public void setGenero(String genero) {
        this.genero = genero;
    }

    /** @return the phone number */
    public String getTelefono() {
        return telefono;
    }

    /** @param telefono the phone number to set */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /** @return the hospital ID where the employee works */
    public Long getIdHospital() {
        return idHospital;
    }

    /** @param idHospital the hospital ID to set */
    public void setIdHospital(Long idHospital) {
        this.idHospital = idHospital;
    }

    /** @return the job position of the employee */
    public String getPuesto() {
        return puesto;
    }

    /** @param puesto the job position to set */
    public void setPuesto(String puesto) {
        this.puesto = puesto;
    }
}
