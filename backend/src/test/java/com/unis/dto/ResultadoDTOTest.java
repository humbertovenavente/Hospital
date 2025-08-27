package com.unis.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

/**
 * Test class for ResultadoDTO.
 * Tests all public fields and functionality.
 */
class ResultadoDTOTest {

    private ResultadoDTO resultadoDTO;
    private String documento;
    private String diagnostico;
    private String resultados;
    private LocalDate fecha;
    private Long idCita;

    @BeforeEach
    void setUp() {
        resultadoDTO = new ResultadoDTO();
        documento = "DOC123456";
        diagnostico = "Paciente sano";
        resultados = "Examen normal";
        fecha = LocalDate.of(2024, 6, 15);
        idCita = 123L;
    }

    @Test
    void testDefaultConstructor() {
        assertNotNull(resultadoDTO);
        assertNull(resultadoDTO.documento);
        assertNull(resultadoDTO.diagnostico);
        assertNull(resultadoDTO.resultados);
        assertNull(resultadoDTO.fecha);
        assertNull(resultadoDTO.idCita);
    }

    @Test
    void testSetAndGetDocumento() {
        resultadoDTO.documento = documento;
        assertEquals(documento, resultadoDTO.documento);
    }

    @Test
    void testSetAndGetDocumentoWithNull() {
        resultadoDTO.documento = null;
        assertNull(resultadoDTO.documento);
    }

    @Test
    void testSetAndGetDocumentoWithEmptyString() {
        resultadoDTO.documento = "";
        assertEquals("", resultadoDTO.documento);
    }

    @Test
    void testSetAndGetDocumentoWithSpecialCharacters() {
        String specialDoc = "DOC-ESPECIAL_123!@#";
        resultadoDTO.documento = specialDoc;
        assertEquals(specialDoc, resultadoDTO.documento);
    }

    @Test
    void testSetAndGetDiagnostico() {
        resultadoDTO.diagnostico = diagnostico;
        assertEquals(diagnostico, resultadoDTO.diagnostico);
    }

    @Test
    void testSetAndGetDiagnosticoWithNull() {
        resultadoDTO.diagnostico = null;
        assertNull(resultadoDTO.diagnostico);
    }

    @Test
    void testSetAndGetDiagnosticoWithEmptyString() {
        resultadoDTO.diagnostico = "";
        assertEquals("", resultadoDTO.diagnostico);
    }

    @Test
    void testSetAndGetDiagnosticoWithLongText() {
        String longDiagnostico = "Diagnóstico muy detallado con mucha información médica " +
                                "que incluye múltiples síntomas y observaciones del paciente " +
                                "durante la consulta médica realizada en el hospital.";
        resultadoDTO.diagnostico = longDiagnostico;
        assertEquals(longDiagnostico, resultadoDTO.diagnostico);
    }

    @Test
    void testSetAndGetResultados() {
        resultadoDTO.resultados = resultados;
        assertEquals(resultados, resultadoDTO.resultados);
    }

    @Test
    void testSetAndGetResultadosWithNull() {
        resultadoDTO.resultados = null;
        assertNull(resultadoDTO.resultados);
    }

    @Test
    void testSetAndGetResultadosWithEmptyString() {
        resultadoDTO.resultados = "";
        assertEquals("", resultadoDTO.resultados);
    }

    @Test
    void testSetAndGetResultadosWithSpecialCharacters() {
        String specialResultados = "Resultados: ✓ Normal, ✗ Anormal, ⚠️ Pendiente";
        resultadoDTO.resultados = specialResultados;
        assertEquals(specialResultados, resultadoDTO.resultados);
    }

    @Test
    void testSetAndGetFecha() {
        resultadoDTO.fecha = fecha;
        assertEquals(fecha, resultadoDTO.fecha);
    }

    @Test
    void testSetAndGetFechaWithNull() {
        resultadoDTO.fecha = null;
        assertNull(resultadoDTO.fecha);
    }

    @Test
    void testSetAndGetFechaWithPastDate() {
        LocalDate pastDate = LocalDate.of(2020, 1, 1);
        resultadoDTO.fecha = pastDate;
        assertEquals(pastDate, resultadoDTO.fecha);
    }

    @Test
    void testSetAndGetFechaWithFutureDate() {
        LocalDate futureDate = LocalDate.of(2025, 12, 31);
        resultadoDTO.fecha = futureDate;
        assertEquals(futureDate, resultadoDTO.fecha);
    }

    @Test
    void testSetAndGetFechaWithMinDate() {
        resultadoDTO.fecha = LocalDate.MIN;
        assertEquals(LocalDate.MIN, resultadoDTO.fecha);
    }

    @Test
    void testSetAndGetFechaWithMaxDate() {
        resultadoDTO.fecha = LocalDate.MAX;
        assertEquals(LocalDate.MAX, resultadoDTO.fecha);
    }

    @Test
    void testSetAndGetIdCita() {
        resultadoDTO.idCita = idCita;
        assertEquals(idCita, resultadoDTO.idCita);
    }

    @Test
    void testSetAndGetIdCitaWithNull() {
        resultadoDTO.idCita = null;
        assertNull(resultadoDTO.idCita);
    }

    @Test
    void testSetAndGetIdCitaWithZero() {
        resultadoDTO.idCita = 0L;
        assertEquals(0L, resultadoDTO.idCita);
    }

    @Test
    void testSetAndGetIdCitaWithNegative() {
        resultadoDTO.idCita = -1L;
        assertEquals(-1L, resultadoDTO.idCita);
    }

    @Test
    void testSetAndGetIdCitaWithMaxValue() {
        resultadoDTO.idCita = Long.MAX_VALUE;
        assertEquals(Long.MAX_VALUE, resultadoDTO.idCita);
    }

    @Test
    void testSetAndGetIdCitaWithMinValue() {
        resultadoDTO.idCita = Long.MIN_VALUE;
        assertEquals(Long.MIN_VALUE, resultadoDTO.idCita);
    }

    @Test
    void testMultipleFields() {
        // Configurar todos los campos
        resultadoDTO.documento = documento;
        resultadoDTO.diagnostico = diagnostico;
        resultadoDTO.resultados = resultados;
        resultadoDTO.fecha = fecha;
        resultadoDTO.idCita = idCita;

        // Verificar todos los campos
        assertEquals(documento, resultadoDTO.documento);
        assertEquals(diagnostico, resultadoDTO.diagnostico);
        assertEquals(resultados, resultadoDTO.resultados);
        assertEquals(fecha, resultadoDTO.fecha);
        assertEquals(idCita, resultadoDTO.idCita);
    }

    @Test
    void testDataIntegrity() {
        // Verificar que los datos no cambien después de múltiples lecturas
        resultadoDTO.documento = documento;
        String doc1 = resultadoDTO.documento;
        String doc2 = resultadoDTO.documento;
        assertEquals(doc1, doc2);

        resultadoDTO.fecha = fecha;
        LocalDate fecha1 = resultadoDTO.fecha;
        LocalDate fecha2 = resultadoDTO.fecha;
        assertEquals(fecha1, fecha2);
    }

    @Test
    void testEdgeCaseValues() {
        // Test con valores extremos
        resultadoDTO.documento = "A";
        resultadoDTO.diagnostico = "";
        resultadoDTO.resultados = " ";
        resultadoDTO.fecha = LocalDate.MIN;
        resultadoDTO.idCita = Long.MIN_VALUE;

        assertEquals("A", resultadoDTO.documento);
        assertEquals("", resultadoDTO.diagnostico);
        assertEquals(" ", resultadoDTO.resultados);
        assertEquals(LocalDate.MIN, resultadoDTO.fecha);
        assertEquals(Long.MIN_VALUE, resultadoDTO.idCita);
    }

    @Test
    void testNullHandling() {
        // Test de manejo de valores nulos
        resultadoDTO.documento = null;
        resultadoDTO.diagnostico = null;
        resultadoDTO.resultados = null;
        resultadoDTO.fecha = null;
        resultadoDTO.idCita = null;

        assertNull(resultadoDTO.documento);
        assertNull(resultadoDTO.diagnostico);
        assertNull(resultadoDTO.resultados);
        assertNull(resultadoDTO.fecha);
        assertNull(resultadoDTO.idCita);
    }

    @Test
    void testRepeatedUpdates() {
        // Actualizar el mismo valor múltiples veces
        for (int i = 0; i < 5; i++) {
            resultadoDTO.documento = "DOC" + i;
            resultadoDTO.idCita = (long) i;
        }

        assertEquals("DOC4", resultadoDTO.documento);
        assertEquals(4L, resultadoDTO.idCita);
    }

    @Test
    void testFieldIndependence() {
        // Verificar que los campos son independientes
        resultadoDTO.documento = "DOC1";
        resultadoDTO.diagnostico = "DIAG1";
        resultadoDTO.resultados = "RES1";
        resultadoDTO.fecha = LocalDate.of(2024, 1, 1);
        resultadoDTO.idCita = 1L;

        // Cambiar solo un campo
        resultadoDTO.documento = "DOC2";

        assertEquals("DOC2", resultadoDTO.documento);
        assertEquals("DIAG1", resultadoDTO.diagnostico);
        assertEquals("RES1", resultadoDTO.resultados);
        assertEquals(LocalDate.of(2024, 1, 1), resultadoDTO.fecha);
        assertEquals(1L, resultadoDTO.idCita);
    }
}
