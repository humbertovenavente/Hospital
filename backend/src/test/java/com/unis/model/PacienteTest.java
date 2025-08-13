package com.unis.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PacienteTest {

    @Test
    public void testPacienteConstructor() {
        // Arrange & Act
        Paciente paciente = new Paciente();
        
        // Assert
        assertNotNull(paciente);
    }

    @Test
    public void testPacienteSettersAndGetters() {
        // Arrange
        Paciente paciente = new Paciente();
        Long id = 1L;
        String telefono = "12345678";

        // Act
        paciente.setIdPaciente(id);
        paciente.setTelefono(telefono);

        // Assert
        assertEquals(id, paciente.getIdPaciente());
        assertEquals(telefono, paciente.getTelefono());
    }

    @Test
    public void testPacienteToString() {
        // Arrange
        Paciente paciente = new Paciente();
        paciente.setIdPaciente(1L);

        // Act
        String result = paciente.toString();

        // Assert
        assertNotNull(result);
    }

    @Test
    public void testPacienteNotNull() {
        // Arrange
        Paciente paciente = new Paciente();

        // Act & Assert
        assertNotNull(paciente);
        assertNotNull(paciente.toString());
    }

    @Test
    public void testPacienteId() {
        // Arrange
        Paciente paciente = new Paciente();
        Long id = 999L;

        // Act
        paciente.setIdPaciente(id);

        // Assert
        assertEquals(id, paciente.getIdPaciente());
    }
}
