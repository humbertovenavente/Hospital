package com.unis.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

/**
 * Test class for Cita entity.
 * Tests all getters, setters, and business logic functionality.
 */
class CitaTest {

    private Cita cita;

    @BeforeEach
    void setUp() {
        cita = new Cita();
    }

    @Test
    void testDefaultConstructor() {
        assertNotNull(cita);
        assertNull(cita.getIdCita());
        assertNull(cita.getDoctor());
        assertNull(cita.getPaciente());
        assertNull(cita.getIdDoctor());
        assertNull(cita.getIdPaciente());
        assertNull(cita.getNumeroAutorizacion());
        assertNull(cita.getFecha());
        assertNull(cita.getHoraInicio());
        assertNull(cita.getHoraFin());
        assertNull(cita.getIdHospital());
        assertNull(cita.getIdServicio());
        assertNull(cita.getIdAseguradora());
        assertNull(cita.getEstado());
        assertNull(cita.getMotivo());
        assertNull(cita.getDiagnostico());
        assertNull(cita.getResultados());
        assertNull(cita.getHospital());
        assertNull(cita.getServicio());
        assertNull(cita.getAseguradora());
    }

    @Test
    void testSetAndGetIdCita() {
        Long idCita = 123L;
        cita.setIdCita(idCita);
        assertEquals(idCita, cita.getIdCita());
    }

    @Test
    void testSetAndGetIdCitaWithNull() {
        cita.setIdCita(null);
        assertNull(cita.getIdCita());
    }

    @Test
    void testSetAndGetIdCitaWithZero() {
        cita.setIdCita(0L);
        assertEquals(0L, cita.getIdCita());
    }

    @Test
    void testSetAndGetIdCitaWithNegative() {
        cita.setIdCita(-1L);
        assertEquals(-1L, cita.getIdCita());
    }

    @Test
    void testSetAndGetIdDoctor() {
        Long idDoctor = 456L;
        cita.setIdDoctor(idDoctor);
        assertEquals(idDoctor, cita.getIdDoctor());
    }

    @Test
    void testSetAndGetIdDoctorWithNull() {
        cita.setIdDoctor(null);
        assertNull(cita.getIdDoctor());
    }

    @Test
    void testSetAndGetIdPaciente() {
        Long idPaciente = 789L;
        cita.setIdPaciente(idPaciente);
        assertEquals(idPaciente, cita.getIdPaciente());
    }

    @Test
    void testSetAndGetIdPacienteWithNull() {
        cita.setIdPaciente(null);
        assertNull(cita.getIdPaciente());
    }

    @Test
    void testSetAndGetNumeroAutorizacion() {
        String numeroAutorizacion = "AUTH123456";
        cita.setNumeroAutorizacion(numeroAutorizacion);
        assertEquals(numeroAutorizacion, cita.getNumeroAutorizacion());
    }

    @Test
    void testSetAndGetNumeroAutorizacionWithNull() {
        cita.setNumeroAutorizacion(null);
        assertNull(cita.getNumeroAutorizacion());
    }

    @Test
    void testSetAndGetNumeroAutorizacionWithEmptyString() {
        cita.setNumeroAutorizacion("");
        assertEquals("", cita.getNumeroAutorizacion());
    }

    @Test
    void testSetAndGetFecha() {
        LocalDate fecha = LocalDate.of(2024, 6, 15);
        cita.setFecha(fecha);
        assertEquals(fecha, cita.getFecha());
    }

    @Test
    void testSetAndGetFechaWithNull() {
        cita.setFecha(null);
        assertNull(cita.getFecha());
    }

    @Test
    void testSetAndGetFechaWithPastDate() {
        LocalDate pastDate = LocalDate.of(2020, 1, 1);
        cita.setFecha(pastDate);
        assertEquals(pastDate, cita.getFecha());
    }

    @Test
    void testSetAndGetFechaWithFutureDate() {
        LocalDate futureDate = LocalDate.of(2025, 12, 31);
        cita.setFecha(futureDate);
        assertEquals(futureDate, cita.getFecha());
    }

    @Test
    void testSetAndGetHoraInicio() {
        String horaInicio = "09:00";
        cita.setHoraInicio(horaInicio);
        assertEquals(horaInicio, cita.getHoraInicio());
    }

    @Test
    void testSetAndGetHoraInicioWithNull() {
        cita.setHoraInicio(null);
        assertNull(cita.getHoraInicio());
    }

    @Test
    void testSetAndGetHoraInicioWithEmptyString() {
        cita.setHoraInicio("");
        assertEquals("", cita.getHoraInicio());
    }

    @Test
    void testSetAndGetHoraFin() {
        String horaFin = "10:00";
        cita.setHoraFin(horaFin);
        assertEquals(horaFin, cita.getHoraFin());
    }

    @Test
    void testSetAndGetHoraFinWithNull() {
        cita.setHoraFin(null);
        assertNull(cita.getHoraFin());
    }

    @Test
    void testSetAndGetHoraFinWithEmptyString() {
        cita.setHoraFin("");
        assertEquals("", cita.getHoraFin());
    }

    @Test
    void testSetAndGetIdHospital() {
        Long idHospital = 100L;
        cita.setIdHospital(idHospital);
        assertEquals(idHospital, cita.getIdHospital());
    }

    @Test
    void testSetAndGetIdHospitalWithNull() {
        cita.setIdHospital(null);
        assertNull(cita.getIdHospital());
    }

    @Test
    void testSetAndGetIdServicio() {
        Long idServicio = 200L;
        cita.setIdServicio(idServicio);
        assertEquals(idServicio, cita.getIdServicio());
    }

    @Test
    void testSetAndGetIdServicioWithNull() {
        cita.setIdServicio(null);
        assertNull(cita.getIdServicio());
    }

    @Test
    void testSetAndGetIdAseguradora() {
        Long idAseguradora = 300L;
        cita.setIdAseguradora(idAseguradora);
        assertEquals(idAseguradora, cita.getIdAseguradora());
    }

    @Test
    void testSetAndGetIdAseguradoraWithNull() {
        cita.setIdAseguradora(null);
        assertNull(cita.getIdAseguradora());
    }

    @Test
    void testSetAndGetEstado() {
        EstadoCita estado = EstadoCita.PENDIENTE;
        cita.setEstado(estado);
        assertEquals(estado, cita.getEstado());
    }

    @Test
    void testSetAndGetEstadoWithNull() {
        cita.setEstado(null);
        assertNull(cita.getEstado());
    }

    @Test
    void testSetAndGetMotivo() {
        String motivo = "Consulta de rutina";
        cita.setMotivo(motivo);
        assertEquals(motivo, cita.getMotivo());
    }

    @Test
    void testSetAndGetMotivoWithNull() {
        cita.setMotivo(null);
        assertNull(cita.getMotivo());
    }

    @Test
    void testSetAndGetMotivoWithEmptyString() {
        cita.setMotivo("");
        assertEquals("", cita.getMotivo());
    }

    @Test
    void testSetAndGetDiagnostico() {
        String diagnostico = "Paciente sano";
        cita.setDiagnostico(diagnostico);
        assertEquals(diagnostico, cita.getDiagnostico());
    }

    @Test
    void testSetAndGetDiagnosticoWithNull() {
        cita.setDiagnostico(null);
        assertNull(cita.getDiagnostico());
    }

    @Test
    void testSetAndGetResultados() {
        String resultados = "Examen normal";
        cita.setResultados(resultados);
        assertEquals(resultados, cita.getResultados());
    }

    @Test
    void testSetAndGetResultadosWithNull() {
        cita.setResultados(null);
        assertNull(cita.getResultados());
    }

    @Test
    void testMultipleSetters() {
        // Configurar todos los campos
        cita.setIdCita(999L);
        cita.setIdDoctor(456L);
        cita.setIdPaciente(789L);
        cita.setNumeroAutorizacion("AUTH999");
        cita.setFecha(LocalDate.of(2024, 12, 25));
        cita.setHoraInicio("14:00");
        cita.setHoraFin("15:00");
        cita.setIdHospital(100L);
        cita.setIdServicio(200L);
        cita.setIdAseguradora(300L);
        cita.setEstado(EstadoCita.CONFIRMADA);
        cita.setMotivo("Consulta especial");
        cita.setDiagnostico("Diagnóstico preliminar");
        cita.setResultados("Resultados esperados");

        // Verificar todos los campos
        assertEquals(999L, cita.getIdCita());
        assertEquals(456L, cita.getIdDoctor());
        assertEquals(789L, cita.getIdPaciente());
        assertEquals("AUTH999", cita.getNumeroAutorizacion());
        assertEquals(LocalDate.of(2024, 12, 25), cita.getFecha());
        assertEquals("14:00", cita.getHoraInicio());
        assertEquals("15:00", cita.getHoraFin());
        assertEquals(100L, cita.getIdHospital());
        assertEquals(200L, cita.getIdServicio());
        assertEquals(300L, cita.getIdAseguradora());
        assertEquals(EstadoCita.CONFIRMADA, cita.getEstado());
        assertEquals("Consulta especial", cita.getMotivo());
        assertEquals("Diagnóstico preliminar", cita.getDiagnostico());
        assertEquals("Resultados esperados", cita.getResultados());
    }

    @Test
    void testDataIntegrity() {
        // Verificar que los datos no cambien después de múltiples lecturas
        cita.setIdCita(123L);
        Long id1 = cita.getIdCita();
        Long id2 = cita.getIdCita();
        assertEquals(id1, id2);

        cita.setMotivo("Test motivo");
        String motivo1 = cita.getMotivo();
        String motivo2 = cita.getMotivo();
        assertEquals(motivo1, motivo2);
    }

    @Test
    void testEdgeCaseValues() {
        // Test con valores extremos
        cita.setIdCita(Long.MAX_VALUE);
        cita.setIdDoctor(Long.MIN_VALUE);
        cita.setIdPaciente(0L);
        cita.setNumeroAutorizacion("A");
        cita.setFecha(LocalDate.MIN);
        cita.setHoraInicio("00:00");
        cita.setHoraFin("23:59");

        assertEquals(Long.MAX_VALUE, cita.getIdCita());
        assertEquals(Long.MIN_VALUE, cita.getIdDoctor());
        assertEquals(0L, cita.getIdPaciente());
        assertEquals("A", cita.getNumeroAutorizacion());
        assertEquals(LocalDate.MIN, cita.getFecha());
        assertEquals("00:00", cita.getHoraInicio());
        assertEquals("23:59", cita.getHoraFin());
    }

    @Test
    void testNullHandling() {
        // Test de manejo de valores nulos
        cita.setIdDoctor(null);
        cita.setIdPaciente(null);
        cita.setIdHospital(null);

        assertNull(cita.getIdDoctor());
        assertNull(cita.getIdPaciente());
        assertNull(cita.getIdHospital());
    }
}
