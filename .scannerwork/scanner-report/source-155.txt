package com.unis.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.unis.model.Receta;

/**
 * Data Transfer Object (DTO) for Receta.
 * This class is used to control the fields included in the JSON response.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecetaDTO {

    @JsonProperty("idReceta")
    private Long idReceta;

    @JsonProperty("idCita")
    private Long idCita;

    @JsonProperty("fechaCreacion")
    private Date fechaCreacion;

    @JsonProperty("idPaciente")
    private Long idPaciente;

    @JsonProperty("idDoctor")
    private Long idDoctor;

    @JsonProperty("codigoReceta")
    private String codigoReceta;

    @JsonProperty("anotaciones")
    private String anotaciones;

    @JsonProperty("notasEspeciales")
    private String notasEspeciales;

    @JsonProperty("medicamentos")
    private List<?> medicamentos;

    private String nombrePaciente;

    public RecetaDTO(Receta receta, String nombrePaciente) {
        this.idReceta = receta.getIdReceta();
        this.idCita = receta.getIdCita();
        this.fechaCreacion = receta.getFechaCreacion();
        this.idPaciente = receta.getIdPaciente();
        this.idDoctor = receta.getIdDoctor();
        this.codigoReceta = receta.getCodigoReceta();
        this.anotaciones = receta.getAnotaciones();
        this.notasEspeciales = receta.getNotasEspeciales();
        this.medicamentos = receta.getMedicamentos();
        this.nombrePaciente = nombrePaciente;
    }

    // Getters and Setters

    public Long getIdReceta() {
        return idReceta;
    }

    public void setIdReceta(Long idReceta) {
        this.idReceta = idReceta;
    }

    public Long getIdCita() {
        return idCita;
    }

    public void setIdCita(Long idCita) {
        this.idCita = idCita;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Long getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(Long idPaciente) {
        this.idPaciente = idPaciente;
    }

    public Long getIdDoctor() {
        return idDoctor;
    }

    public void setIdDoctor(Long idDoctor) {
        this.idDoctor = idDoctor;
    }

    public String getCodigoReceta() {
        return codigoReceta;
    }

    public void setCodigoReceta(String codigoReceta) {
        this.codigoReceta = codigoReceta;
    }

    public String getAnotaciones() {
        return anotaciones;
    }

    public void setAnotaciones(String anotaciones) {
        this.anotaciones = anotaciones;
    }

    public String getNotasEspeciales() {
        return notasEspeciales;
    }

    public void setNotasEspeciales(String notasEspeciales) {
        this.notasEspeciales = notasEspeciales;
    }

    public List<?> getMedicamentos() {
        return medicamentos;
    }

    public void setMedicamentos(List<?> medicamentos) {
        this.medicamentos = medicamentos;
    }

    public String getNombrePaciente() {
        return nombrePaciente;
    }
}