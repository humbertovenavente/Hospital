
package com.unis.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
/**
 * Entity representing an insurance company.
 * <p>
 * Stores basic information about insurance companies that can be linked to patients or services.
 * </p>
 * 
 * @author Herman
 */
@Entity
@Table(name = "ASEGURADORA")
public class Aseguradora {

    /** The unique identifier of the insurance company. */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "aseguradora_seq")
    @SequenceGenerator(name = "aseguradora_seq", sequenceName = "SEQ_ASEGURADORA", allocationSize = 1)
    @Column(name = "ID_ASEGURADORA")
    private Long id;

    /** The name of the insurance company. */
    @Column(name = "NOMBRE", nullable = false, length = 100)
    private String nombre;

    /**
     * Gets the unique identifier of the insurance company.
     *
     * @return the insurance company ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the insurance company.
     *
     * @param id the insurance company ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the name of the insurance company.
     *
     * @return the name of the insurance company
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Sets the name of the insurance company.
     *
     * @param nombre the name to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
