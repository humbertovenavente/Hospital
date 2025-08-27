package com.unis.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

/**
 * Test class for ReporteRequest.
 * Tests all getters, setters, and constructor functionality.
 */
class ReporteRequestTest {

    private ReporteRequest reporteRequest;
    private Long idDoctor;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String tipoReporte;
    private String usuario;

    @BeforeEach
    void setUp() {
        idDoctor = 123L;
        fechaInicio = LocalDate.of(2024, 1, 1);
        fechaFin = LocalDate.of(2024, 1, 31);
        tipoReporte = "AGRUPADO";
        usuario = "admin@hospital.com";
        
        reporteRequest = new ReporteRequest();
        reporteRequest.setIdDoctor(idDoctor);
        reporteRequest.setFechaInicio(fechaInicio);
        reporteRequest.setFechaFin(fechaFin);
        reporteRequest.setTipoReporte(tipoReporte);
        reporteRequest.setUsuario(usuario);
    }

    @Test
    void testDefaultConstructor() {
        ReporteRequest emptyRequest = new ReporteRequest();
        assertNotNull(emptyRequest);
        assertNull(emptyRequest.getIdDoctor());
        assertNull(emptyRequest.getFechaInicio());
        assertNull(emptyRequest.getFechaFin());
        assertNull(emptyRequest.getTipoReporte());
        assertNull(emptyRequest.getUsuario());
    }

    @Test
    void testGetIdDoctor() {
        assertEquals(idDoctor, reporteRequest.getIdDoctor());
    }

    @Test
    void testSetIdDoctor() {
        Long newIdDoctor = 456L;
        reporteRequest.setIdDoctor(newIdDoctor);
        assertEquals(newIdDoctor, reporteRequest.getIdDoctor());
    }

    @Test
    void testSetIdDoctorWithNull() {
        reporteRequest.setIdDoctor(null);
        assertNull(reporteRequest.getIdDoctor());
    }

    @Test
    void testSetIdDoctorWithZero() {
        reporteRequest.setIdDoctor(0L);
        assertEquals(0L, reporteRequest.getIdDoctor());
    }

    @Test
    void testSetIdDoctorWithNegative() {
        reporteRequest.setIdDoctor(-1L);
        assertEquals(-1L, reporteRequest.getIdDoctor());
    }

    @Test
    void testGetFechaInicio() {
        assertEquals(fechaInicio, reporteRequest.getFechaInicio());
    }

    @Test
    void testSetFechaInicio() {
        LocalDate newFechaInicio = LocalDate.of(2024, 2, 1);
        reporteRequest.setFechaInicio(newFechaInicio);
        assertEquals(newFechaInicio, reporteRequest.getFechaInicio());
    }

    @Test
    void testSetFechaInicioWithNull() {
        reporteRequest.setFechaInicio(null);
        assertNull(reporteRequest.getFechaInicio());
    }

    @Test
    void testSetFechaInicioWithPastDate() {
        LocalDate pastDate = LocalDate.of(2020, 1, 1);
        reporteRequest.setFechaInicio(pastDate);
        assertEquals(pastDate, reporteRequest.getFechaInicio());
    }

    @Test
    void testGetFechaFin() {
        assertEquals(fechaFin, reporteRequest.getFechaFin());
    }

    @Test
    void testSetFechaFin() {
        LocalDate newFechaFin = LocalDate.of(2024, 2, 28);
        reporteRequest.setFechaFin(newFechaFin);
        assertEquals(newFechaFin, reporteRequest.getFechaFin());
    }

    @Test
    void testSetFechaFinWithNull() {
        reporteRequest.setFechaFin(null);
        assertNull(reporteRequest.getFechaFin());
    }

    @Test
    void testSetFechaFinWithFutureDate() {
        LocalDate futureDate = LocalDate.of(2025, 12, 31);
        reporteRequest.setFechaFin(futureDate);
        assertEquals(futureDate, reporteRequest.getFechaFin());
    }

    @Test
    void testGetTipoReporte() {
        assertEquals(tipoReporte, reporteRequest.getTipoReporte());
    }

    @Test
    void testSetTipoReporte() {
        String newTipoReporte = "DETALLADO";
        reporteRequest.setTipoReporte(newTipoReporte);
        assertEquals(newTipoReporte, reporteRequest.getTipoReporte());
    }

    @Test
    void testSetTipoReporteWithNull() {
        reporteRequest.setTipoReporte(null);
        assertNull(reporteRequest.getTipoReporte());
    }

    @Test
    void testSetTipoReporteWithEmptyString() {
        reporteRequest.setTipoReporte("");
        assertEquals("", reporteRequest.getTipoReporte());
    }

    @Test
    void testSetTipoReporteWithSpecialCharacters() {
        String specialTipo = "REPORTE_ESPECIAL_123!@#";
        reporteRequest.setTipoReporte(specialTipo);
        assertEquals(specialTipo, reporteRequest.getTipoReporte());
    }

    @Test
    void testGetUsuario() {
        assertEquals(usuario, reporteRequest.getUsuario());
    }

    @Test
    void testSetUsuario() {
        String newUsuario = "doctor@hospital.com";
        reporteRequest.setUsuario(newUsuario);
        assertEquals(newUsuario, reporteRequest.getUsuario());
    }

    @Test
    void testSetUsuarioWithNull() {
        reporteRequest.setUsuario(null);
        assertNull(reporteRequest.getUsuario());
    }

    @Test
    void testSetUsuarioWithEmptyString() {
        reporteRequest.setUsuario("");
        assertEquals("", reporteRequest.getUsuario());
    }

    @Test
    void testSetUsuarioWithSpecialCharacters() {
        String specialUsuario = "usuario.especial@hospital-gt.com";
        reporteRequest.setUsuario(specialUsuario);
        assertEquals(specialUsuario, reporteRequest.getUsuario());
    }

    @Test
    void testMultipleSetters() {
        ReporteRequest multiRequest = new ReporteRequest();
        
        multiRequest.setIdDoctor(999L);
        multiRequest.setFechaInicio(LocalDate.of(2024, 6, 1));
        multiRequest.setFechaFin(LocalDate.of(2024, 6, 30));
        multiRequest.setTipoReporte("MENSUAL");
        multiRequest.setUsuario("sistema@hospital.com");

        assertEquals(999L, multiRequest.getIdDoctor());
        assertEquals(LocalDate.of(2024, 6, 1), multiRequest.getFechaInicio());
        assertEquals(LocalDate.of(2024, 6, 30), multiRequest.getFechaFin());
        assertEquals("MENSUAL", multiRequest.getTipoReporte());
        assertEquals("sistema@hospital.com", multiRequest.getUsuario());
    }

    @Test
    void testEdgeCaseValues() {
        ReporteRequest edgeRequest = new ReporteRequest();
        
        edgeRequest.setIdDoctor(Long.MAX_VALUE);
        edgeRequest.setFechaInicio(LocalDate.MIN);
        edgeRequest.setFechaFin(LocalDate.MAX);
        edgeRequest.setTipoReporte("A");
        edgeRequest.setUsuario("a");

        assertEquals(Long.MAX_VALUE, edgeRequest.getIdDoctor());
        assertEquals(LocalDate.MIN, edgeRequest.getFechaInicio());
        assertEquals(LocalDate.MAX, edgeRequest.getFechaFin());
        assertEquals("A", edgeRequest.getTipoReporte());
        assertEquals("a", edgeRequest.getUsuario());
    }

    @Test
    void testDataIntegrity() {
        // Verificar que los datos no cambien después de múltiples lecturas
        Long id1 = reporteRequest.getIdDoctor();
        Long id2 = reporteRequest.getIdDoctor();
        assertEquals(id1, id2);

        String usuario1 = reporteRequest.getUsuario();
        String usuario2 = reporteRequest.getUsuario();
        assertEquals(usuario1, usuario2);
    }

    @Test
    void testDateRangeValidation() {
        // Test con fechas válidas
        LocalDate start = LocalDate.of(2024, 1, 1);
        LocalDate end = LocalDate.of(2024, 1, 31);
        
        reporteRequest.setFechaInicio(start);
        reporteRequest.setFechaFin(end);
        
        assertEquals(start, reporteRequest.getFechaInicio());
        assertEquals(end, reporteRequest.getFechaFin());
    }

    @Test
    void testSameDateStartAndEnd() {
        LocalDate sameDate = LocalDate.of(2024, 1, 15);
        reporteRequest.setFechaInicio(sameDate);
        reporteRequest.setFechaFin(sameDate);
        
        assertEquals(sameDate, reporteRequest.getFechaInicio());
        assertEquals(sameDate, reporteRequest.getFechaFin());
    }
}
