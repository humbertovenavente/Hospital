package com.unis.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DoctorTest {

    @Test
    public void testDoctorConstructor() {
        // Arrange & Act
        Doctor doctor = new Doctor();
        
        // Assert
        assertNotNull(doctor);
    }

    @Test
    public void testDoctorSettersAndGetters() {
        // Arrange
        Doctor doctor = new Doctor();
        Long id = 1L;
        String especialidad = "Cardiología";

        // Act
        doctor.setIdDoctor(id);
        doctor.setEspecialidad(especialidad);

        // Assert
        assertEquals(id, doctor.getIdDoctor());
        assertEquals(especialidad, doctor.getEspecialidad());
    }

    @Test
    public void testDoctorToString() {
        // Arrange
        Doctor doctor = new Doctor();
        doctor.setIdDoctor(1L);
        doctor.setEspecialidad("Cardiología");

        // Act
        String result = doctor.toString();

        // Assert
        assertNotNull(result);
    }

    @Test
    public void testDoctorNotNull() {
        // Arrange
        Doctor doctor = new Doctor();

        // Act & Assert
        assertNotNull(doctor);
        assertNotNull(doctor.toString());
    }

    @Test
    public void testDoctorId() {
        // Arrange
        Doctor doctor = new Doctor();
        Long id = 999L;

        // Act
        doctor.setIdDoctor(id);

        // Assert
        assertEquals(id, doctor.getIdDoctor());
    }
}
