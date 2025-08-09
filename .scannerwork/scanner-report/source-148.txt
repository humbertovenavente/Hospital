
package com.unis.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Entity representing the relationship between a prescription and its associated medications.
 * <p>
 * This entity holds the details of each medication prescribed as part of a medical prescription,
 * including dosage, frequency, and duration.
 * </p>
 * 
 * <p>
 * It links to both the {@link Receta} and {@link Medicamento} entities and includes metadata 
 * such as the diagnosis related to the prescription.
 * </p>
 * 
 * @author Herman
 */

@Entity
@Table(name = "RECETAMEDICAMENTO")
public class RecetaMedicamento {

    /** The unique identifier of the prescription-medication relationship. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_RECETAMEDICAMENTO", nullable = false)
    private Long idRecetaMedicamento;

    /** The prescription associated with this medication. */
    @ManyToOne
    @JoinColumn(name = "ID_RECETA", nullable = false)
    @JsonBackReference
    private Receta receta;

    /** The medication associated with this prescription. */
    @ManyToOne
    @JoinColumn(name = "ID_MEDICAMENTO", nullable = false)
    private Medicamento medicamento;

    /** The prescribed dosage for this medication. */
    @Column(name = "DOSIS", length = 50)
    private String dosis;

    /** The frequency of intake for this medication. */
    @Column(name = "FRECUENCIA", length = 50)
    private String frecuencia;

    /** The duration for which the medication must be taken. */
    @Column(name = "DURACION", length = 50)
    private String duracion;

    /** The diagnosis associated with the prescription. */
    @Column(name = "DIAGNOSTICO", length = 255)
    private String diagnostico;

    // ==============================
    // Helper Methods
    // ==============================

    /**
     * Returns the ID of the associated prescription.
     *
     * @return the prescription ID, or null if not set
     */
    public Long getIdReceta() {
        return receta != null ? receta.getIdReceta() : null;
    }

    /**
     * Returns the ID of the associated medication.
     *
     * @return the medication ID, or null if not set
     */
    public Long getIdMedicamento() {
        return medicamento != null ? medicamento.getIdMedicamento() : null;
    }

    // ==============================
    // Getters and Setters
    // ==============================

    public Long getIdRecetaMedicamento() {
        return idRecetaMedicamento;
    }

    public void setIdRecetaMedicamento(Long idRecetaMedicamento) {
        this.idRecetaMedicamento = idRecetaMedicamento;
    }

    public Receta getReceta() {
        return receta;
    }

    public void setReceta(Receta receta) {
        this.receta = receta;
    }

    public Medicamento getMedicamento() {
        return medicamento;
    }

    public void setMedicamento(Medicamento medicamento) {
        this.medicamento = medicamento;
    }

    public String getDosis() {
        return dosis;
    }

    public void setDosis(String dosis) {
        this.dosis = dosis;
    }

    public String getFrecuencia() {
        return frecuencia;
    }

    public void setFrecuencia(String frecuencia) {
        this.frecuencia = frecuencia;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }
}
