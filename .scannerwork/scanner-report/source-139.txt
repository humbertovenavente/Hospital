package com.unis.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * Entity representing a service.
 * <p>
 * This entity is used to manage services offered in the system. Each service
 * includes details such as its name, cost, and whether it is covered by insurance.
 * </p>
 * 
 * <p>
 * Services can have a hierarchical structure, where a service may have a parent
 * service and multiple sub-services. This relationship is managed using a
 * self-referencing one-to-many mapping.
 * </p>
 * 
 * <p>
 * The entity also supports lazy loading for sub-services to optimize performance.
 * </p>
 */
@Entity
@Table(name = "SERVICIO")
public class Servicio extends PanacheEntity {

    /** 
     * The name of the service.
     * <p>
     * This field stores the name of the service being offered.
     * </p>
     */
    @Column(nullable = false)
    public String nombre;

    /** 
     * The cost of the service.
     * <p>
     * This field stores the monetary cost of the service.
     * </p>
     */
    @Column(nullable = false)
    public double costo;

    /** 
     * Indicates whether the service is covered by insurance.
     * <p>
     * This field specifies if the service is eligible for insurance coverage.
     * </p>
     */
    @Column(nullable = false)
    public boolean cubiertoSeguro;

    /** 
     * The parent service, if applicable.
     * <p>
     * This field establishes a many-to-one relationship with the parent service.
     * </p>
     */
    @ManyToOne
    @JoinColumn(name = "PARENT_ID")
    @JsonBackReference
    public Servicio servicioPadre;

    /** 
     * The set of sub-services associated with this service.
     * <p>
     * This field establishes a one-to-many relationship with sub-services.
     * </p>
     */
    @OneToMany(mappedBy = "servicioPadre", cascade = CascadeType.MERGE, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    public Set<Servicio> subServicios = new HashSet<>();

    // Getters

    /** 
     * @return the ID of the parent service, if applicable.
     * <p>
     * Returns null if the service does not have a parent.
     * </p>
     */
    public Long getParentId() {
        return servicioPadre != null ? servicioPadre.id : null;
    }

    /** 
     * @return the unique identifier of the service.
     */
    public Long getId() {
        return this.id;
    }

    /** 
     * @return the unique identifier of the service.
     * <p>
     * This method is an alias for {@link #getId()}.
     * </p>
     */
    public Long getIdServicio() {
        return this.id;
    }

    // equals y hashCode basados en el id

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Servicio)) return false;
        Servicio that = (Servicio) o;
        return this.id != null && this.id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
