package com.unis.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

public class CitaDTOTest {

    private CitaDTO citaDTO;

    @BeforeEach
    void setUp() {
        citaDTO = new CitaDTO();
    }

    @Test
    void testConstructorPorDefecto() {
        assertNotNull(citaDTO);
    }

    @Test
    void testCamposPublicos() {
        // Arrange
        String dpi = "1234567890101";
        String nombre = "Juan";
        String apellido = "Pérez";
        LocalDate fecha = LocalDate.now();
        String horaInicio = "09:00";
        String horaFin = "10:00";
        String motivo = "Consulta de rutina";
        Long idHospital = 1L;
        Long idServicio = 1L;
        Long idAseguradora = 1L;
        String numeroAutorizacion = "AUTH001";

        // Act
        citaDTO.dpi = dpi;
        citaDTO.nombre = nombre;
        citaDTO.apellido = apellido;
        citaDTO.fecha = fecha;
        citaDTO.horaInicio = horaInicio;
        citaDTO.horaFin = horaFin;
        citaDTO.motivo = motivo;
        citaDTO.idHospital = idHospital;
        citaDTO.idServicio = idServicio;
        citaDTO.idAseguradora = idAseguradora;
        citaDTO.numeroAutorizacion = numeroAutorizacion;

        // Assert
        assertEquals(dpi, citaDTO.dpi);
        assertEquals(nombre, citaDTO.nombre);
        assertEquals(apellido, citaDTO.apellido);
        assertEquals(fecha, citaDTO.fecha);
        assertEquals(horaInicio, citaDTO.horaInicio);
        assertEquals(horaFin, citaDTO.horaFin);
        assertEquals(motivo, citaDTO.motivo);
        assertEquals(idHospital, citaDTO.idHospital);
        assertEquals(idServicio, citaDTO.idServicio);
        assertEquals(idAseguradora, citaDTO.idAseguradora);
        assertEquals(numeroAutorizacion, citaDTO.numeroAutorizacion);
    }

    @Test
    void testCamposConValoresNulos() {
        // Act
        citaDTO.dpi = null;
        citaDTO.nombre = null;
        citaDTO.apellido = null;
        citaDTO.fecha = null;
        citaDTO.horaInicio = null;
        citaDTO.horaFin = null;
        citaDTO.motivo = null;
        citaDTO.idHospital = null;
        citaDTO.idServicio = null;
        citaDTO.idAseguradora = null;
        citaDTO.numeroAutorizacion = null;

        // Assert
        assertNull(citaDTO.dpi);
        assertNull(citaDTO.nombre);
        assertNull(citaDTO.apellido);
        assertNull(citaDTO.fecha);
        assertNull(citaDTO.horaInicio);
        assertNull(citaDTO.horaFin);
        assertNull(citaDTO.motivo);
        assertNull(citaDTO.idHospital);
        assertNull(citaDTO.idServicio);
        assertNull(citaDTO.idAseguradora);
        assertNull(citaDTO.numeroAutorizacion);
    }

    @Test
    void testCamposConValoresVacios() {
        // Act
        citaDTO.dpi = "";
        citaDTO.nombre = "";
        citaDTO.apellido = "";
        citaDTO.motivo = "";
        citaDTO.numeroAutorizacion = "";

        // Assert
        assertEquals("", citaDTO.dpi);
        assertEquals("", citaDTO.nombre);
        assertEquals("", citaDTO.apellido);
        assertEquals("", citaDTO.motivo);
        assertEquals("", citaDTO.numeroAutorizacion);
    }

    @Test
    void testCamposConValoresEspeciales() {
        // Act
        citaDTO.dpi = "1234567890101";
        citaDTO.nombre = "María José";
        citaDTO.apellido = "García-López";
        citaDTO.motivo = "Consulta de emergencia";
        citaDTO.numeroAutorizacion = "AUTH-2024-001";

        // Assert
        assertEquals("1234567890101", citaDTO.dpi);
        assertEquals("María José", citaDTO.nombre);
        assertEquals("García-López", citaDTO.apellido);
        assertEquals("Consulta de emergencia", citaDTO.motivo);
        assertEquals("AUTH-2024-001", citaDTO.numeroAutorizacion);
    }
}
