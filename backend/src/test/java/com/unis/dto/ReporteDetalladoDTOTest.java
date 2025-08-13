package com.unis.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

public class ReporteDetalladoDTOTest {

    private ReporteDetalladoDTO reporteDetalladoDTO;

    @BeforeEach
    void setUp() {
        reporteDetalladoDTO = new ReporteDetalladoDTO(LocalDate.now(), "10:00", "Test", "Seguro");
    }

    @Test
    void testConstructorConParametros() {
        // Arrange
        LocalDate fecha = LocalDate.of(2024, 12, 25);
        String horaConsulta = "14:30";
        String nombrePaciente = "Juan Pérez";
        String tipoPago = "Directo";

        // Act
        ReporteDetalladoDTO dto = new ReporteDetalladoDTO(fecha, horaConsulta, nombrePaciente, tipoPago);

        // Assert
        assertEquals(fecha, dto.getFecha());
        assertEquals(horaConsulta, dto.getHoraConsulta());
        assertEquals(nombrePaciente, dto.getNombrePaciente());
        assertEquals(tipoPago, dto.getTipoPago());
    }

    @Test
    void testGettersAndSetters() {
        // Arrange
        LocalDate fecha = LocalDate.of(2024, 12, 25);
        String horaConsulta = "15:45";
        String nombrePaciente = "María López";
        String tipoPago = "Seguro";

        // Act
        reporteDetalladoDTO.setFecha(fecha);
        reporteDetalladoDTO.setHoraConsulta(horaConsulta);
        reporteDetalladoDTO.setNombrePaciente(nombrePaciente);
        reporteDetalladoDTO.setTipoPago(tipoPago);

        // Assert
        assertEquals(fecha, reporteDetalladoDTO.getFecha());
        assertEquals(horaConsulta, reporteDetalladoDTO.getHoraConsulta());
        assertEquals(nombrePaciente, reporteDetalladoDTO.getNombrePaciente());
        assertEquals(tipoPago, reporteDetalladoDTO.getTipoPago());
    }

    @Test
    void testSetFecha() {
        LocalDate fecha = LocalDate.of(2024, 1, 1);
        reporteDetalladoDTO.setFecha(fecha);
        assertEquals(fecha, reporteDetalladoDTO.getFecha());
    }

    @Test
    void testSetHoraConsulta() {
        String horaConsulta = "09:15";
        reporteDetalladoDTO.setHoraConsulta(horaConsulta);
        assertEquals(horaConsulta, reporteDetalladoDTO.getHoraConsulta());
    }

    @Test
    void testSetNombrePaciente() {
        String nombrePaciente = "Carlos Rodríguez";
        reporteDetalladoDTO.setNombrePaciente(nombrePaciente);
        assertEquals(nombrePaciente, reporteDetalladoDTO.getNombrePaciente());
    }

    @Test
    void testSetTipoPago() {
        String tipoPago = "Mixto";
        reporteDetalladoDTO.setTipoPago(tipoPago);
        assertEquals(tipoPago, reporteDetalladoDTO.getTipoPago());
    }

    @Test
    void testSetFechaNull() {
        reporteDetalladoDTO.setFecha(null);
        assertNull(reporteDetalladoDTO.getFecha());
    }

    @Test
    void testSetHoraConsultaNull() {
        reporteDetalladoDTO.setHoraConsulta(null);
        assertNull(reporteDetalladoDTO.getHoraConsulta());
    }

    @Test
    void testSetNombrePacienteNull() {
        reporteDetalladoDTO.setNombrePaciente(null);
        assertNull(reporteDetalladoDTO.getNombrePaciente());
    }

    @Test
    void testSetTipoPagoNull() {
        reporteDetalladoDTO.setTipoPago(null);
        assertNull(reporteDetalladoDTO.getTipoPago());
    }

    @Test
    void testSetHoraConsultaVacia() {
        String horaConsulta = "";
        reporteDetalladoDTO.setHoraConsulta(horaConsulta);
        assertEquals(horaConsulta, reporteDetalladoDTO.getHoraConsulta());
    }

    @Test
    void testSetNombrePacienteVacio() {
        String nombrePaciente = "";
        reporteDetalladoDTO.setNombrePaciente(nombrePaciente);
        assertEquals(nombrePaciente, reporteDetalladoDTO.getNombrePaciente());
    }

    @Test
    void testSetTipoPagoVacio() {
        String tipoPago = "";
        reporteDetalladoDTO.setTipoPago(tipoPago);
        assertEquals(tipoPago, reporteDetalladoDTO.getTipoPago());
    }

    @Test
    void testSetHoraConsultaConEspacios() {
        String horaConsulta = "  16:00  ";
        reporteDetalladoDTO.setHoraConsulta(horaConsulta);
        assertEquals(horaConsulta, reporteDetalladoDTO.getHoraConsulta());
    }

    @Test
    void testSetNombrePacienteConEspacios() {
        String nombrePaciente = "  Ana García  ";
        reporteDetalladoDTO.setNombrePaciente(nombrePaciente);
        assertEquals(nombrePaciente, reporteDetalladoDTO.getNombrePaciente());
    }

    @Test
    void testSetTipoPagoConEspacios() {
        String tipoPago = "  Seguro  ";
        reporteDetalladoDTO.setTipoPago(tipoPago);
        assertEquals(tipoPago, reporteDetalladoDTO.getTipoPago());
    }
}
