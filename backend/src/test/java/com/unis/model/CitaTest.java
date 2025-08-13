package com.unis.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CitaTest {

    @Test
    public void testCitaConstructor() {
        // Arrange & Act
        Cita cita = new Cita();
        
        // Assert
        assertNotNull(cita);
    }

    @Test
    public void testCitaSettersAndGetters() {
        // Arrange
        Cita cita = new Cita();
        Long id = 1L;
        String horaInicio = "09:00";
        String horaFin = "10:00";

        // Act
        cita.setIdCita(id);
        cita.setHoraInicio(horaInicio);
        cita.setHoraFin(horaFin);

        // Assert
        assertEquals(id, cita.getIdCita());
        assertEquals(horaInicio, cita.getHoraInicio());
        assertEquals(horaFin, cita.getHoraFin());
    }

    @Test
    public void testCitaToString() {
        // Arrange
        Cita cita = new Cita();
        cita.setIdCita(1L);
        cita.setHoraInicio("09:00");

        // Act
        String result = cita.toString();

        // Assert
        assertNotNull(result);
    }

    @Test
    public void testCitaNotNull() {
        // Arrange
        Cita cita = new Cita();

        // Act & Assert
        assertNotNull(cita);
        assertNotNull(cita.toString());
    }

    @Test
    public void testCitaId() {
        // Arrange
        Cita cita = new Cita();
        Long id = 999L;

        // Act
        cita.setIdCita(id);

        // Assert
        assertEquals(id, cita.getIdCita());
    }
}
