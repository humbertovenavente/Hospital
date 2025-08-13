package com.unis.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

public class ReporteAgregadoDTOTest {

    private ReporteAgregadoDTO reporteAgregadoDTO;

    @BeforeEach
    void setUp() {
        reporteAgregadoDTO = new ReporteAgregadoDTO(LocalDate.now(), 0L, 0L, 0L);
    }

    @Test
    void testConstructorConParametros() {
        // Arrange
        LocalDate fecha = LocalDate.of(2024, 12, 25);
        Long totalConsultas = 100L;
        Long totalSeguro = 70L;
        Long totalDirecto = 30L;

        // Act
        ReporteAgregadoDTO dto = new ReporteAgregadoDTO(fecha, totalConsultas, totalSeguro, totalDirecto);

        // Assert
        assertEquals(fecha, dto.getFecha());
        assertEquals(totalConsultas, dto.getTotalConsultas());
        assertEquals(totalSeguro, dto.getTotalSeguro());
        assertEquals(totalDirecto, dto.getTotalDirecto());
    }

    @Test
    void testGettersAndSetters() {
        // Arrange
        LocalDate fecha = LocalDate.of(2024, 12, 25);
        Long totalConsultas = 150L;
        Long totalSeguro = 100L;
        Long totalDirecto = 50L;

        // Act
        reporteAgregadoDTO.setFecha(fecha);
        reporteAgregadoDTO.setTotalConsultas(totalConsultas);
        reporteAgregadoDTO.setTotalSeguro(totalSeguro);
        reporteAgregadoDTO.setTotalDirecto(totalDirecto);

        // Assert
        assertEquals(fecha, reporteAgregadoDTO.getFecha());
        assertEquals(totalConsultas, reporteAgregadoDTO.getTotalConsultas());
        assertEquals(totalSeguro, reporteAgregadoDTO.getTotalSeguro());
        assertEquals(totalDirecto, reporteAgregadoDTO.getTotalDirecto());
    }

    @Test
    void testSetFecha() {
        LocalDate fecha = LocalDate.of(2024, 1, 1);
        reporteAgregadoDTO.setFecha(fecha);
        assertEquals(fecha, reporteAgregadoDTO.getFecha());
    }

    @Test
    void testSetTotalConsultas() {
        Long totalConsultas = 200L;
        reporteAgregadoDTO.setTotalConsultas(totalConsultas);
        assertEquals(totalConsultas, reporteAgregadoDTO.getTotalConsultas());
    }

    @Test
    void testSetTotalSeguro() {
        Long totalSeguro = 120L;
        reporteAgregadoDTO.setTotalSeguro(totalSeguro);
        assertEquals(totalSeguro, reporteAgregadoDTO.getTotalSeguro());
    }

    @Test
    void testSetTotalDirecto() {
        Long totalDirecto = 80L;
        reporteAgregadoDTO.setTotalDirecto(totalDirecto);
        assertEquals(totalDirecto, reporteAgregadoDTO.getTotalDirecto());
    }

    @Test
    void testSetFechaNull() {
        reporteAgregadoDTO.setFecha(null);
        assertNull(reporteAgregadoDTO.getFecha());
    }

    @Test
    void testSetTotalConsultasNull() {
        reporteAgregadoDTO.setTotalConsultas(null);
        assertNull(reporteAgregadoDTO.getTotalConsultas());
    }

    @Test
    void testSetTotalSeguroNull() {
        reporteAgregadoDTO.setTotalSeguro(null);
        assertNull(reporteAgregadoDTO.getTotalSeguro());
    }

    @Test
    void testSetTotalDirectoNull() {
        reporteAgregadoDTO.setTotalDirecto(null);
        assertNull(reporteAgregadoDTO.getTotalDirecto());
    }

    @Test
    void testSetTotalConsultasZero() {
        Long totalConsultas = 0L;
        reporteAgregadoDTO.setTotalConsultas(totalConsultas);
        assertEquals(totalConsultas, reporteAgregadoDTO.getTotalConsultas());
    }

    @Test
    void testSetTotalSeguroZero() {
        Long totalSeguro = 0L;
        reporteAgregadoDTO.setTotalSeguro(totalSeguro);
        assertEquals(totalSeguro, reporteAgregadoDTO.getTotalSeguro());
    }

    @Test
    void testSetTotalDirectoZero() {
        Long totalDirecto = 0L;
        reporteAgregadoDTO.setTotalDirecto(totalDirecto);
        assertEquals(totalDirecto, reporteAgregadoDTO.getTotalDirecto());
    }

    @Test
    void testSetTotalConsultasNegativo() {
        Long totalConsultas = -10L;
        reporteAgregadoDTO.setTotalConsultas(totalConsultas);
        assertEquals(totalConsultas, reporteAgregadoDTO.getTotalConsultas());
    }

    @Test
    void testSetTotalSeguroNegativo() {
        Long totalSeguro = -5L;
        reporteAgregadoDTO.setTotalSeguro(totalSeguro);
        assertEquals(totalSeguro, reporteAgregadoDTO.getTotalSeguro());
    }

    @Test
    void testSetTotalDirectoNegativo() {
        Long totalDirecto = -3L;
        reporteAgregadoDTO.setTotalDirecto(totalDirecto);
        assertEquals(totalDirecto, reporteAgregadoDTO.getTotalDirecto());
    }
}
