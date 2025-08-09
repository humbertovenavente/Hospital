
package com.unis.model;
/**
 * Enum representing the possible states of a medical appointment.
 * <p>
 * This enumeration is used to track the lifecycle of a medical appointment
 * within the hospital system.
 * </p>
 * 
 * <ul>
 *   <li>{@link #PENDIENTE} - The appointment is pending confirmation.</li>
 *   <li>{@link #CONFIRMADA} - The appointment has been confirmed.</li>
 *   <li>{@link #CANCELADA} - The appointment has been canceled.</li>
 *   <li>{@link #FINALIZADA} - The appointment has been completed.</li>
 * </ul>
 * 
 * @author Herman
 */
public enum EstadoCita {

    /** The appointment is pending confirmation. */
    PENDIENTE,

    /** The appointment has been confirmed. */
    CONFIRMADA,

    /** The appointment has been canceled. */
    CANCELADA,

    /** The appointment has been completed. */
    FINALIZADA;
}
