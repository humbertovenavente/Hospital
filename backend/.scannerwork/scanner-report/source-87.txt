
package com.unis.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Entity representing a medical appointment.
 * <p>
 * Contains all relevant data regarding a patient's appointment, including doctor, patient,
 * date, time, hospital, service, insurance, diagnosis, and results.
 * </p>
 * 
 * @author Herman
 */
@Entity
@Table(name = "CITA")
public class Cita {

    /** The unique identifier of the appointment. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CITA")
    private Long idCita;

    /** The doctor associated with the appointment. */
    @ManyToOne
    @JoinColumn(name = "ID_DOCTOR", insertable = false, updatable = false)
    private Doctor doctor;

    /** The patient associated with the appointment. */
    @ManyToOne
    @JoinColumn(name = "ID_PACIENTE", insertable = false, updatable = false)
    private Paciente paciente;

    /** The ID of the doctor associated with the appointment. */
    @JsonProperty("idDoctor")
    @Column(name = "ID_DOCTOR")
    private Long idDoctor;

    /** The ID of the patient associated with the appointment. */
    @JsonProperty("idPaciente")
    @Column(name = "ID_PACIENTE")
    private Long idPaciente;

    /** The authorization number for the appointment, if applicable. */
    @Column(name = "NUMERO_AUTORIZACION")
    private String numeroAutorizacion;

    /** The date of the appointment. */
    @Column(name = "FECHA", nullable = false)
    private LocalDate fecha;

    /** The start time of the appointment. */
    @Column(name = "HORA_INICIO", nullable = false)
    private String horaInicio;

    /** The end time of the appointment. */
    @Column(name = "HORA_FIN", nullable = false)
    private String horaFin;

    /** The ID of the hospital where the appointment is scheduled. */
    @Column(name = "ID_HOSPITAL")
    private Long idHospital;

    /** The ID of the service associated with the appointment. */
    @Column(name = "ID_SERVICIO")
    private Long idServicio;

    /** The ID of the insurance company associated with the appointment. */
    @Column(name = "ID_ASEGURADORA")
    private Long idAseguradora;

    /** The status of the appointment. */
    @Enumerated(EnumType.STRING)
    @Column(name = "ESTADO", nullable = false)
    private EstadoCita estado;

    /** The reason for the appointment. */
    @Column(name = "MOTIVO")
    private String motivo;

    /** The diagnosis provided during the appointment. */
    @Column(name = "DIAGNOSTICO")
    private String diagnostico;

    /** The results of the appointment. */
    @Column(name = "RESULTADOS")
    private String resultados;

    /** The hospital entity associated with the appointment. */
    @ManyToOne
    @JoinColumn(name = "ID_HOSPITAL", insertable = false, updatable = false)
    private Hospital hospital;

    /** The service entity associated with the appointment. */
    @ManyToOne
    @JoinColumn(name = "ID_SERVICIO", insertable = false, updatable = false)
    private Servicio servicio;

    /** The insurance company entity associated with the appointment. */
    @ManyToOne
    @JoinColumn(name = "ID_ASEGURADORA", insertable = false, updatable = false)
    private Aseguradora aseguradora;

    // ======= Getters and Setters (documentados) =======

    /** @return the appointment ID */
    public Long getIdCita() {
        return idCita;
    }

    /** @param idCita the appointment ID to set */
    public void setIdCita(Long idCita) {
        this.idCita = idCita;
    }

    /** @return the doctor entity */
    public Doctor getDoctor() {
        return doctor;
    }

    /** @param doctor the doctor to assign */
    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
        if (doctor != null) {
            this.idDoctor = doctor.getIdDoctor();
        }
    }

    /** @return the patient entity */
    public Paciente getPaciente() {
        return paciente;
    }

    /** @param paciente the patient to assign */
    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
        if (paciente != null) {
            this.idPaciente = paciente.getIdPaciente();
        }
    }

    /** @return the ID of the doctor */
    public Long getIdDoctor() {
        return idDoctor;
    }

    /** @param idDoctor the doctor ID */
    public void setIdDoctor(Long idDoctor) {
        this.idDoctor = idDoctor;
    }

    /** @return the ID of the patient */
    public Long getIdPaciente() {
        return idPaciente;
    }

    /** @param idPaciente the patient ID */
    public void setIdPaciente(Long idPaciente) {
        this.idPaciente = idPaciente;
    }

    /** @return the authorization number */
    public String getNumeroAutorizacion() {
        return numeroAutorizacion;
    }

    /** @param numeroAutorizacion the authorization number to set */
    public void setNumeroAutorizacion(String numeroAutorizacion) {
        this.numeroAutorizacion = numeroAutorizacion;
    }

    /** @return the date of the appointment */
    public LocalDate getFecha() {
        return fecha;
    }

    /** @param fecha the date of the appointment */
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    /** @return the start time of the appointment */
    public String getHoraInicio() {
        return horaInicio;
    }

    /** @param horaInicio the start time */
    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    /** @return the end time of the appointment */
    public String getHoraFin() {
        return horaFin;
    }

    /** @param horaFin the end time */
    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
    }

    /** @return the hospital ID */
    public Long getIdHospital() {
        return idHospital;
    }

    /** @param idHospital the hospital ID */
    public void setIdHospital(Long idHospital) {
        this.idHospital = idHospital;
    }

    /** @return the service ID */
    public Long getIdServicio() {
        return idServicio;
    }

    /** @param idServicio the service ID */
    public void setIdServicio(Long idServicio) {
        this.idServicio = idServicio;
    }

    /** @return the insurance company ID */
    public Long getIdAseguradora() {
        return idAseguradora;
    }

    /** @param idAseguradora the insurance company ID */
    public void setIdAseguradora(Long idAseguradora) {
        this.idAseguradora = idAseguradora;
    }

    /** @return the appointment status */
    public EstadoCita getEstado() {
        return estado;
    }

    /** @param estado the status to set */
    public void setEstado(EstadoCita estado) {
        this.estado = estado;
    }

    /** @return the reason for the appointment */
    public String getMotivo() {
        return motivo;
    }

    /** @param motivo the reason for the appointment */
    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    /** @return the diagnosis for the appointment */
    public String getDiagnostico() {
        return diagnostico;
    }

    /** @param diagnostico the diagnosis to set */
    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    /** @return the results of the appointment */
    public String getResultados() {
        return resultados;
    }

    /** @param resultados the results to set */
    public void setResultados(String resultados) {
        this.resultados = resultados;
    }

    /** @return the hospital entity */
    public Hospital getHospital() {
        return hospital;
    }

    /** @param hospital the hospital to set */
    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }

    /** @return the service entity */
    public Servicio getServicio() {
        return servicio;
    }

    /** @param servicio the service to set */
    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
    }

    /** @return the insurance company entity */
    public Aseguradora getAseguradora() {
        return aseguradora;
    }

    /** @param aseguradora the insurance company to set */
    public void setAseguradora(Aseguradora aseguradora) {
        this.aseguradora = aseguradora;
    }
}
