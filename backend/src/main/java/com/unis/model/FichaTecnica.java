
package com.unis.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

/**
 * Entity representing a technical record (Ficha Técnica) for a patient.
 * <p>
 * This entity holds extended medical and administrative information 
 * related to a patient's interaction with healthcare services.
 * It includes service history, insurance details, and a reference 
 * to the patient who owns this record.
 * </p>
 * 
 * <p><b>Database Table:</b> FICHATECNICA</p>
 * 
 * @author Herman
 */
@Entity
@Table(name = "FICHATECNICA")
public class FichaTecnica {

    /** The unique identifier of the technical record. */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ficha_seq_gen")
    @SequenceGenerator(name = "ficha_seq_gen", sequenceName = "FICHATECNICA_SEQ", allocationSize = 1)
    @Column(name = "ID_FICHA")
    private Long idFicha;

    /** The ID of the service associated with this record. */
    @Column(name = "ID_SERVICIO")
    private Long idServicio;

    /** The creation date of the technical record. */
    @Column(name = "FECHA_CREACION")
    private LocalDate fechaCreacion;

    /** The service history associated with this record. */
    @Column(name = "HISTORIAL_SERVICIOS")
    private String historialServicios;

    /** The affiliation number provided by the insurance. */
    @Column(name = "NUMEROAFILIACION")
    private String numeroAfiliacion;

    /** The insurance code identifying the coverage plan. */
    @Column(name = "CODIGOSEGURO")
    private String codigoSeguro;

    /** The insurance card number of the patient. */
    @Column(name = "CARNETSEGURO")
    private String carnetSeguro;

    /** The patient linked to this technical record. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_PACIENTE", referencedColumnName = "ID_PACIENTE", nullable = false)
    private PacienteFT paciente;

    // ========================
    // Getters and Setters
    // ========================

    /** @return the unique identifier of the technical record. */
    public Long getIdFicha() {
        return idFicha;
    }

    /** @param idFicha the unique identifier of the technical record. */
    public void setIdFicha(Long idFicha) {
        this.idFicha = idFicha;
    }

    /** @return the ID of the associated service. */
    public Long getIdServicio() {
        return idServicio;
    }

    /** @param idServicio the ID of the associated service. */
    public void setIdServicio(Long idServicio) {
        this.idServicio = idServicio;
    }

    /** @return the creation date of the technical record. */
    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    /** @param fechaCreacion the creation date of the technical record. */
    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    /** @return the service history associated with the technical record. */
    public String getHistorialServicios() {
        return historialServicios;
    }

    /** @param historialServicios the service history associated with the technical record. */
    public void setHistorialServicios(String historialServicios) {
        this.historialServicios = historialServicios;
    }

    /** @return the affiliation number associated with the patient. */
    public String getNumeroAfiliacion() {
        return numeroAfiliacion;
    }

    /** @param numeroAfiliacion the affiliation number associated with the patient. */
    public void setNumeroAfiliacion(String numeroAfiliacion) {
        this.numeroAfiliacion = numeroAfiliacion;
    }

    /** @return the insurance code associated with the patient. */
    public String getCodigoSeguro() {
        return codigoSeguro;
    }

    /** @param codigoSeguro the insurance code associated with the patient. */
    public void setCodigoSeguro(String codigoSeguro) {
        this.codigoSeguro = codigoSeguro;
    }

    /** @return the insurance card number associated with the patient. */
    public String getCarnetSeguro() {
        return carnetSeguro;
    }

    /** @param carnetSeguro the insurance card number associated with the patient. */
    public void setCarnetSeguro(String carnetSeguro) {
        this.carnetSeguro = carnetSeguro;
    }

    /** @return the patient associated with the technical record. */
    public PacienteFT getPaciente() {
        return paciente;
    }

    /** @param paciente the patient associated with the technical record. */
    public void setPaciente(PacienteFT paciente) {
        this.paciente = paciente;
    }
}
