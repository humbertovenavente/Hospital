
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
 * Entity representing a doctor (alternative schema).
 * <p>
 * Includes personal and professional details, hospital association, and user account linkage.
 * This version uses a one-to-one relationship with the {@link UserAcc} entity.
 * </p>
 * 
 * @author Herman
 */


@Entity
@Table(name = "DOCTOR")
public class DoctorAcc implements Serializable {

    /** The unique identifier of the doctor. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_DOCTOR")
    private Long idDoctor;

    /** The user account associated with the doctor. */
    @OneToOne
    @JoinColumn(name = "ID_USUARIO", nullable = false)
    private UserAcc usuario;

    /** The last name of the doctor. */
    @Column(name = "APELLIDO", nullable = false, length = 100)
    private String apellido;

    /** The document identifier of the doctor. */
    @Column(name = "DOCUMENTO", nullable = false, length = 20)
    private String documento;

    /** The birth date of the doctor. */
    @Column(name = "FECHA_NACIMIENTO")
    @Temporal(TemporalType.DATE)
    private Date fechaNacimiento;

    /** The gender of the doctor. */
    @Column(name = "GENERO", length = 10)
    private String genero;

    /** The phone number of the doctor. */
    @Column(name = "TELEFONO", length = 15)
    private String telefono;

    /** The ID of the hospital where the doctor works. */
    @Column(name = "ID_HOSPITAL")
    private Long idHospital;

    /** The medical specialty of the doctor. */
    @Column(name = "ESPECIALIDAD", length = 100)
    private String especialidad;

    /** The collegiate number of the doctor. */
    @Column(name = "NUMERO_COLEGIADO", length = 50)
    private String numeroColegiado;

    /** The working hours of the doctor. */
    @Column(name = "HORARIO_ATENCION", length = 255)
    private String horarioAtencion;

    /** The graduation date of the doctor. */
    @Column(name = "FECHA_GRADUACION")
    @Temporal(TemporalType.DATE)
    private Date fechaGraduacion;

    /** The university where the doctor graduated. */
    @Column(name = "UNIVERSIDAD_GRADUACION", length = 150)
    private String universidadGraduacion;

    /** The availability of the doctor (e.g., full-time, part-time). */
    @Column(name = "DISPONIBILIDAD", length = 255)
    private String disponibilidad;

    // =======================
    // Getters and Setters
    // =======================

    /** @return the doctor's ID */
    public Long getIdDoctor() {
        return idDoctor;
    }

    /** @param idDoctor the doctor's ID to set */
    public void setIdDoctor(Long idDoctor) {
        this.idDoctor = idDoctor;
    }

    /** @return the associated user account */
    public UserAcc getUsuario() {
        return usuario;
    }

    /** @param usuario the user account to associate */
    public void setUsuario(UserAcc usuario) {
        this.usuario = usuario;
    }

    /** @return the doctor's last name */
    public String getApellido() {
        return apellido;
    }

    /** @param apellido the last name to set */
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    /** @return the doctor's document ID */
    public String getDocumento() {
        return documento;
    }

    /** @param documento the document ID to set */
    public void setDocumento(String documento) {
        this.documento = documento;
    }

    /** @return the doctor's birth date */
    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    /** @param fechaNacimiento the birth date to set */
    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    /** @return the doctor's gender */
    public String getGenero() {
        return genero;
    }

    /** @param genero the gender to set */
    public void setGenero(String genero) {
        this.genero = genero;
    }

    /** @return the doctor's phone number */
    public String getTelefono() {
        return telefono;
    }

    /** @param telefono the phone number to set */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /** @return the ID of the hospital where the doctor works */
    public Long getIdHospital() {
        return idHospital;
    }

    /** @param idHospital the hospital ID to set */
    public void setIdHospital(Long idHospital) {
        this.idHospital = idHospital;
    }

    /** @return the doctor's specialty */
    public String getEspecialidad() {
        return especialidad;
    }

    /** @param especialidad the specialty to set */
    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    /** @return the collegiate number */
    public String getNumeroColegiado() {
        return numeroColegiado;
    }

    /** @param numeroColegiado the collegiate number to set */
    public void setNumeroColegiado(String numeroColegiado) {
        this.numeroColegiado = numeroColegiado;
    }

    /** @return the working schedule */
    public String getHorarioAtencion() {
        return horarioAtencion;
    }

    /** @param horarioAtencion the working schedule to set */
    public void setHorarioAtencion(String horarioAtencion) {
        this.horarioAtencion = horarioAtencion;
    }

    /** @return the graduation date */
    public Date getFechaGraduacion() {
        return fechaGraduacion;
    }

    /** @param fechaGraduacion the graduation date to set */
    public void setFechaGraduacion(Date fechaGraduacion) {
        this.fechaGraduacion = fechaGraduacion;
    }

    /** @return the university of graduation */
    public String getUniversidadGraduacion() {
        return universidadGraduacion;
    }

    /** @param universidadGraduacion the university to set */
    public void setUniversidadGraduacion(String universidadGraduacion) {
        this.universidadGraduacion = universidadGraduacion;
    }

    /** @return the doctor's availability */
    public String getDisponibilidad() {
        return disponibilidad;
    }

    /** @param disponibilidad the availability to set */
    public void setDisponibilidad(String disponibilidad) {
        this.disponibilidad = disponibilidad;
    }
}
