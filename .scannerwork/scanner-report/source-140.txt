
package com.unis.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


/**
 * Entity representing a user role.
 * <p>
 * This entity defines different roles that users can have within the system.
 * Roles are used to manage permissions and access control for different parts
 * of the application.
 * </p>
 * 
 * <p>
 * Example roles might include: ADMIN, DOCTOR, PATIENT, etc.
 * </p>
 * 
 * @author Herman
 */
@Entity
@Table(name = "ROL")
public class Rol {

    /** The unique identifier of the role. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_ROL")
    private Long id;

    /** The name of the role (e.g., "ADMIN", "DOCTOR", "PATIENT"). */
    @Column(name = "ROLE_NAME", nullable = false, length = 50)
    private String roleName;

    // ==============================
    // Getters and Setters
    // ==============================

    /**
     * Gets the unique identifier of the role.
     *
     * @return the role ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the role.
     *
     * @param id the role ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the name of the role.
     *
     * @return the role name
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     * Sets the name of the role.
     *
     * @param roleName the role name
     */
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
