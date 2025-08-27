package com.unis.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

/**
 * Test class for ReporteAgregadoDTO.
 * Tests all getters, setters, and constructor functionality.
 */
class ReporteAgregadoDTOTest {

    private ReporteAgregadoDTO reporteDTO;
    private LocalDate testDate;
    private Long totalConsultas;
    private Long totalSeguro;
    private Long totalDirecto;

    @BeforeEach
    void setUp() {
        testDate = LocalDate.of(2024, 1, 15);
        totalConsultas = 100L;
        totalSeguro = 60L;
        totalDirecto = 40L;
        reporteDTO = new ReporteAgregadoDTO(testDate, totalConsultas, totalSeguro, totalDirecto);
    }

    @Test
    void testConstructorWithValidParameters() {
        assertNotNull(reporteDTO);
        assertEquals(testDate, reporteDTO.getFecha());
        assertEquals(totalConsultas, reporteDTO.getTotalConsultas());
        assertEquals(totalSeguro, reporteDTO.getTotalSeguro());
        assertEquals(totalDirecto, reporteDTO.getTotalDirecto());
    }

    @Test
    void testConstructorWithNullValues() {
        ReporteAgregadoDTO nullDTO = new ReporteAgregadoDTO(null, null, null, null);
        assertNull(nullDTO.getFecha());
        assertNull(nullDTO.getTotalConsultas());
        assertNull(nullDTO.getTotalSeguro());
        assertNull(nullDTO.getTotalDirecto());
    }

    @Test
    void testConstructorWithZeroValues() {
        ReporteAgregadoDTO zeroDTO = new ReporteAgregadoDTO(testDate, 0L, 0L, 0L);
        assertEquals(testDate, zeroDTO.getFecha());
        assertEquals(0L, zeroDTO.getTotalConsultas());
        assertEquals(0L, zeroDTO.getTotalSeguro());
        assertEquals(0L, zeroDTO.getTotalDirecto());
    }

    @Test
    void testGetFecha() {
        assertEquals(testDate, reporteDTO.getFecha());
    }

    @Test
    void testSetFecha() {
        LocalDate newDate = LocalDate.of(2024, 2, 20);
        reporteDTO.setFecha(newDate);
        assertEquals(newDate, reporteDTO.getFecha());
    }

    @Test
    void testSetFechaWithNull() {
        reporteDTO.setFecha(null);
        assertNull(reporteDTO.getFecha());
    }

    @Test
    void testGetTotalConsultas() {
        assertEquals(totalConsultas, reporteDTO.getTotalConsultas());
    }

    @Test
    void testSetTotalConsultas() {
        Long newTotal = 150L;
        reporteDTO.setTotalConsultas(newTotal);
        assertEquals(newTotal, reporteDTO.getTotalConsultas());
    }

    @Test
    void testSetTotalConsultasWithNull() {
        reporteDTO.setTotalConsultas(null);
        assertNull(reporteDTO.getTotalConsultas());
    }

    @Test
    void testGetTotalSeguro() {
        assertEquals(totalSeguro, reporteDTO.getTotalSeguro());
    }

    @Test
    void testSetTotalSeguro() {
        Long newSeguro = 80L;
        reporteDTO.setTotalSeguro(newSeguro);
        assertEquals(newSeguro, reporteDTO.getTotalSeguro());
    }

    @Test
    void testSetTotalSeguroWithNull() {
        reporteDTO.setTotalSeguro(null);
        assertNull(reporteDTO.getTotalSeguro());
    }

    @Test
    void testGetTotalDirecto() {
        assertEquals(totalDirecto, reporteDTO.getTotalDirecto());
    }

    @Test
    void testSetTotalDirecto() {
        Long newDirecto = 50L;
        reporteDTO.setTotalDirecto(newDirecto);
        assertEquals(newDirecto, reporteDTO.getTotalDirecto());
    }

    @Test
    void testSetTotalDirectoWithNull() {
        reporteDTO.setTotalDirecto(null);
        assertNull(reporteDTO.getTotalDirecto());
    }

    @Test
    void testMultipleSetters() {
        LocalDate newDate = LocalDate.of(2024, 3, 25);
        Long newConsultas = 200L;
        Long newSeguro = 120L;
        Long newDirecto = 80L;

        reporteDTO.setFecha(newDate);
        reporteDTO.setTotalConsultas(newConsultas);
        reporteDTO.setTotalSeguro(newSeguro);
        reporteDTO.setTotalDirecto(newDirecto);

        assertEquals(newDate, reporteDTO.getFecha());
        assertEquals(newConsultas, reporteDTO.getTotalConsultas());
        assertEquals(newSeguro, reporteDTO.getTotalSeguro());
        assertEquals(newDirecto, reporteDTO.getTotalDirecto());
    }

    @Test
    void testEdgeCaseValues() {
        ReporteAgregadoDTO edgeDTO = new ReporteAgregadoDTO(
            LocalDate.MIN, 
            Long.MAX_VALUE, 
            Long.MIN_VALUE, 
            0L
        );

        assertEquals(LocalDate.MIN, edgeDTO.getFecha());
        assertEquals(Long.MAX_VALUE, edgeDTO.getTotalConsultas());
        assertEquals(Long.MIN_VALUE, edgeDTO.getTotalSeguro());
        assertEquals(0L, edgeDTO.getTotalDirecto());
    }

    @Test
    void testDataIntegrity() {
        // Verificar que los datos no cambien después de múltiples lecturas
        LocalDate fecha1 = reporteDTO.getFecha();
        LocalDate fecha2 = reporteDTO.getFecha();
        assertEquals(fecha1, fecha2);

        Long consultas1 = reporteDTO.getTotalConsultas();
        Long consultas2 = reporteDTO.getTotalConsultas();
        assertEquals(consultas1, consultas2);
    }
}
