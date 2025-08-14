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
        citaDTO.setDpi(dpi);
        citaDTO.setNombre(nombre);
        citaDTO.setApellido(apellido);
        citaDTO.setFecha(fecha);
        citaDTO.setHoraInicio(horaInicio);
        citaDTO.setHoraFin(horaFin);
        citaDTO.setMotivo(motivo);
        citaDTO.setIdHospital(idHospital);
        citaDTO.setIdServicio(idServicio);
        citaDTO.setIdAseguradora(idAseguradora);
        citaDTO.setNumeroAutorizacion(numeroAutorizacion);

        // Assert
        assertEquals(dpi, citaDTO.getDpi());
        assertEquals(nombre, citaDTO.getNombre());
        assertEquals(apellido, citaDTO.getApellido());
        assertEquals(fecha, citaDTO.getFecha());
        assertEquals(horaInicio, citaDTO.getHoraInicio());
        assertEquals(horaFin, citaDTO.getHoraFin());
        assertEquals(motivo, citaDTO.getMotivo());
        assertEquals(idHospital, citaDTO.getIdHospital());
        assertEquals(idServicio, citaDTO.getIdServicio());
        assertEquals(idAseguradora, citaDTO.getIdAseguradora());
        assertEquals(numeroAutorizacion, citaDTO.getNumeroAutorizacion());
    }

    @Test
    void testCamposConValoresNulos() {
        // Act
        citaDTO.setDpi(null);
        citaDTO.setNombre(null);
        citaDTO.setApellido(null);
        citaDTO.setFecha(null);
        citaDTO.setHoraInicio(null);
        citaDTO.setHoraFin(null);
        citaDTO.setMotivo(null);
        citaDTO.setIdHospital(null);
        citaDTO.setIdServicio(null);
        citaDTO.setIdAseguradora(null);
        citaDTO.setNumeroAutorizacion(null);

        // Assert
        assertNull(citaDTO.getDpi());
        assertNull(citaDTO.getNombre());
        assertNull(citaDTO.getApellido());
        assertNull(citaDTO.getFecha());
        assertNull(citaDTO.getHoraInicio());
        assertNull(citaDTO.getHoraFin());
        assertNull(citaDTO.getMotivo());
        assertNull(citaDTO.getIdHospital());
        assertNull(citaDTO.getIdServicio());
        assertNull(citaDTO.getIdAseguradora());
        assertNull(citaDTO.getNumeroAutorizacion());
    }

    @Test
    void testCamposConValoresVacios() {
        // Act
        citaDTO.setDpi("");
        citaDTO.setNombre("");
        citaDTO.setApellido("");
        citaDTO.setMotivo("");
        citaDTO.setNumeroAutorizacion("");

        // Assert
        assertEquals("", citaDTO.getDpi());
        assertEquals("", citaDTO.getNombre());
        assertEquals("", citaDTO.getApellido());
        assertEquals("", citaDTO.getMotivo());
        assertEquals("", citaDTO.getNumeroAutorizacion());
    }

    @Test
    void testCamposConValoresEspeciales() {
        // Act
        citaDTO.setDpi("1234567890101");
        citaDTO.setNombre("María José");
        citaDTO.setApellido("García-López");
        citaDTO.setMotivo("Consulta de emergencia");
        citaDTO.setNumeroAutorizacion("AUTH-2024-001");

        // Assert
        assertEquals("1234567890101", citaDTO.getDpi());
        assertEquals("María José", citaDTO.getNombre());
        assertEquals("García-López", citaDTO.getApellido());
        assertEquals("Consulta de emergencia", citaDTO.getMotivo());
        assertEquals("AUTH-2024-001", citaDTO.getNumeroAutorizacion());
    }
}
