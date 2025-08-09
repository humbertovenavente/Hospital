
package com.unis.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
/**
 * Entity representing a hospital request.
 * <p>
 * This entity is used to manage requests related to hospitals in the system.
 * It includes details such as the hospital's name, address, phone number,
 * associated insurance company, and the status of the request.
 * </p>
 * 
 * <p>
 * Requests can originate from hospitals and are tracked with an initial
 * status of "pendiente" (pending).
 * </p>
 */
@Entity
@Table(name = "SOLICITUD_HOSPITAL")
public class SolicitudHospital extends PanacheEntityBase {

    /** The unique identifier of the hospital request. */
    @Id
    @SequenceGenerator(
        name = "solicitudHospitalSeq",
        sequenceName = "SOLICITUD_HOSPITAL_SEQ",
        allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "solicitudHospitalSeq")
    public Long id;

    /** 
     * The name of the hospital.
     * <p>
     * This field stores the name of the hospital making the request.
     * </p>
     */
    @Column(nullable = false)
    public String nombre;

    /** 
     * The address of the hospital.
     * <p>
     * This field stores the physical address of the hospital.
     * </p>
     */
    @Column(nullable = false)
    public String direccion;

    /** 
     * The phone number of the hospital.
     * <p>
     * This field stores the contact phone number of the hospital.
     * </p>
     */
    @Column(nullable = false)
    public String telefono;

    /** 
     * The insurance company associated with the hospital.
     * <p>
     * This field stores the name of the insurance company linked to the hospital.
     * </p>
     */
    @Column(nullable = false)
    public String aseguradora;

    /** 
     * The status of the request (e.g., "pendiente").
     * <p>
     * This field indicates the current status of the hospital's request.
     * </p>
     */
    @Column(nullable = false)
    public String estado = "pendiente";

    /** 
     * The origin of the request (e.g., "hospital").
     * <p>
     * This field specifies the source of the request.
     * </p>
     */
    @Column(nullable = false)
    public String origen = "hospital";
}

