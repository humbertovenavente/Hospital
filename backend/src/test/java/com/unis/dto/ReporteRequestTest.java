package com.unis.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

public class ReporteRequestTest {

    private ReporteRequest reporteRequest;

    @BeforeEach
    void setUp() {
        reporteRequest = new ReporteRequest();
    }

    @Test
    void testConstructorPorDefecto() {
        assertNotNull(reporteRequest);
    }

    @Test
    void testGettersAndSetters() {
        // Arrange
        Long idDoctor = 1L;
        LocalDate fechaInicio = LocalDate.of(2024, 1, 1);
        LocalDate fechaFin = LocalDate.of(2024, 12, 31);
        String tipoReporte = "AGRUPADO";
        String usuario = "admin";

        // Act
        reporteRequest.setIdDoctor(idDoctor);
        reporteRequest.setFechaInicio(fechaInicio);
        reporteRequest.setFechaFin(fechaFin);
        reporteRequest.setTipoReporte(tipoReporte);
        reporteRequest.setUsuario(usuario);

        // Assert
        assertEquals(idDoctor, reporteRequest.getIdDoctor());
        assertEquals(fechaInicio, reporteRequest.getFechaInicio());
        assertEquals(fechaFin, reporteRequest.getFechaFin());
        assertEquals(tipoReporte, reporteRequest.getTipoReporte());
        assertEquals(usuario, reporteRequest.getUsuario());
    }

    @Test
    void testSetIdDoctor() {
        Long idDoctor = 999L;
        reporteRequest.setIdDoctor(idDoctor);
        assertEquals(idDoctor, reporteRequest.getIdDoctor());
    }

    @Test
    void testSetFechaInicio() {
        LocalDate fechaInicio = LocalDate.of(2024, 6, 15);
        reporteRequest.setFechaInicio(fechaInicio);
        assertEquals(fechaInicio, reporteRequest.getFechaInicio());
    }

    @Test
    void testSetFechaFin() {
        LocalDate fechaFin = LocalDate.of(2024, 6, 30);
        reporteRequest.setFechaFin(fechaFin);
        assertEquals(fechaFin, reporteRequest.getFechaFin());
    }

    @Test
    void testSetTipoReporte() {
        String tipoReporte = "DETALLADO";
        reporteRequest.setTipoReporte(tipoReporte);
        assertEquals(tipoReporte, reporteRequest.getTipoReporte());
    }

    @Test
    void testSetUsuario() {
        String usuario = "doctor123";
        reporteRequest.setUsuario(usuario);
        assertEquals(usuario, reporteRequest.getUsuario());
    }

    @Test
    void testSetIdDoctorNull() {
        reporteRequest.setIdDoctor(null);
        assertNull(reporteRequest.getIdDoctor());
    }

    @Test
    void testSetFechaInicioNull() {
        reporteRequest.setFechaInicio(null);
        assertNull(reporteRequest.getFechaInicio());
    }

    @Test
    void testSetFechaFinNull() {
        reporteRequest.setFechaFin(null);
        assertNull(reporteRequest.getFechaFin());
    }

    @Test
    void testSetTipoReporteNull() {
        reporteRequest.setTipoReporte(null);
        assertNull(reporteRequest.getTipoReporte());
    }

    @Test
    void testSetUsuarioNull() {
        reporteRequest.setUsuario(null);
        assertNull(reporteRequest.getUsuario());
    }

    @Test
    void testSetTipoReporteVacio() {
        String tipoReporte = "";
        reporteRequest.setTipoReporte(tipoReporte);
        assertEquals(tipoReporte, reporteRequest.getTipoReporte());
    }

    @Test
    void testSetUsuarioVacio() {
        String usuario = "";
        reporteRequest.setUsuario(usuario);
        assertEquals(usuario, reporteRequest.getUsuario());
    }

    @Test
    void testSetTipoReporteConEspacios() {
        String tipoReporte = "  AGRUPADO  ";
        reporteRequest.setTipoReporte(tipoReporte);
        assertEquals(tipoReporte, reporteRequest.getTipoReporte());
    }

    @Test
    void testSetUsuarioConEspacios() {
        String usuario = "  doctor456  ";
        reporteRequest.setUsuario(usuario);
        assertEquals(usuario, reporteRequest.getUsuario());
    }
}
