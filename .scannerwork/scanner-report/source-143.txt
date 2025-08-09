package com.unis.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entity representing a doctor's schedule.
 * <p>
 * Represents the working agenda of a doctor, including available days,
 * working hours, and additional notes.
 * </p>
 * 
 * @author Herman
 */
@Entity
@Table(name = "AGENDA")
public class Agenda {

    /** The unique identifier of the schedule. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** The ID of the doctor associated with the schedule. */
    @Column(name = "ID_DOCTOR", nullable = false)
    private Long idDoctor;

    /** The days of attention in string format, e.g., "Monday,Tuesday,Wednesday". */
    @Column(name = "DIAS_ATENCION", nullable = false)
    private String diasAtencion;

    /** The start time of the schedule. */
    @Column(name = "HORA_INICIO", nullable = false)
    private String horaInicio;

    /** The end time of the schedule. */
    @Column(name = "HORA_FIN", nullable = false)
    private String horaFin;

    /** Additional notes for the schedule. */
    @Column(name = "NOTAS")
    private String notas;

    /**
     * Gets the unique identifier of the schedule.
     *
     * @return the schedule ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Gets the ID of the associated doctor.
     *
     * @return the doctor ID
     */
    public Long getIdDoctor() {
        return idDoctor;
    }

    /**
     * Sets the ID of the associated doctor.
     *
     * @param idDoctor the doctor ID to set
     */
    public void setIdDoctor(Long idDoctor) {
        this.idDoctor = idDoctor;
    }

    /**
     * Gets the days of attention.
     *
     * @return the days of attention (e.g., "Monday,Tuesday")
     */
    public String getDiasAtencion() {
        return diasAtencion;
    }

    /**
     * Sets the days of attention.
     *
     * @param diasAtencion the days of attention
     */
    public void setDiasAtencion(String diasAtencion) {
        this.diasAtencion = diasAtencion;
    }

    /**
     * Gets the start time of the schedule.
     *
     * @return the start time (e.g., "08:00")
     */
    public String getHoraInicio() {
        return horaInicio;
    }

    /**
     * Sets the start time of the schedule.
     *
     * @param horaInicio the start time
     */
    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    /**
     * Gets the end time of the schedule.
     *
     * @return the end time (e.g., "16:30")
     */
    public String getHoraFin() {
        return horaFin;
    }

    /**
     * Sets the end time of the schedule.
     *
     * @param horaFin the end time
     */
    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
    }

    /**
     * Gets additional notes for the schedule.
     *
     * @return notes or comments
     */
    public String getNotas() {
        return notas;
    }

    /**
     * Sets additional notes for the schedule.
     *
     * @param notas notes or comments to set
     */
    public void setNotas(String notas) {
        this.notas = notas;
    }
}
