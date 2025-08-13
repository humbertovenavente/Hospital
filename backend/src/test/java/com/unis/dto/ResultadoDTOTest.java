package com.unis.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

public class ResultadoDTOTest {

    private ResultadoDTO resultadoDTO;

    @BeforeEach
    void setUp() {
        resultadoDTO = new ResultadoDTO();
    }

    @Test
    void testConstructorPorDefecto() {
        assertNotNull(resultadoDTO);
    }

    @Test
    void testSetYGetDocumento() {
        // Arrange
        String documento = "DOC123456";

        // Act
        resultadoDTO.documento = documento;

        // Assert
        assertEquals(documento, resultadoDTO.documento);
    }

    @Test
    void testSetYGetDiagnostico() {
        // Arrange
        String diagnostico = "Gripe común";

        // Act
        resultadoDTO.diagnostico = diagnostico;

        // Assert
        assertEquals(diagnostico, resultadoDTO.diagnostico);
    }

    @Test
    void testSetYGetResultados() {
        // Arrange
        String resultados = "Paciente presenta síntomas de gripe";

        // Act
        resultadoDTO.resultados = resultados;

        // Assert
        assertEquals(resultados, resultadoDTO.resultados);
    }

    @Test
    void testSetYGetFecha() {
        // Arrange
        LocalDate fecha = LocalDate.of(2024, 12, 25);

        // Act
        resultadoDTO.fecha = fecha;

        // Assert
        assertEquals(fecha, resultadoDTO.fecha);
    }

    @Test
    void testSetYGetIdCita() {
        // Arrange
        Long idCita = 123L;

        // Act
        resultadoDTO.idCita = idCita;

        // Assert
        assertEquals(idCita, resultadoDTO.idCita);
    }

    @Test
    void testSetDocumentoNull() {
        // Act
        resultadoDTO.documento = null;

        // Assert
        assertNull(resultadoDTO.documento);
    }

    @Test
    void testSetDiagnosticoNull() {
        // Act
        resultadoDTO.diagnostico = null;

        // Assert
        assertNull(resultadoDTO.diagnostico);
    }

    @Test
    void testSetResultadosNull() {
        // Act
        resultadoDTO.resultados = null;

        // Assert
        assertNull(resultadoDTO.resultados);
    }

    @Test
    void testSetFechaNull() {
        // Act
        resultadoDTO.fecha = null;

        // Assert
        assertNull(resultadoDTO.fecha);
    }

    @Test
    void testSetIdCitaNull() {
        // Act
        resultadoDTO.idCita = null;

        // Assert
        assertNull(resultadoDTO.idCita);
    }

    @Test
    void testSetDocumentoVacio() {
        // Arrange
        String documentoVacio = "";

        // Act
        resultadoDTO.documento = documentoVacio;

        // Assert
        assertEquals(documentoVacio, resultadoDTO.documento);
        assertTrue(resultadoDTO.documento.isEmpty());
    }

    @Test
    void testSetDiagnosticoVacio() {
        // Arrange
        String diagnosticoVacio = "";

        // Act
        resultadoDTO.diagnostico = diagnosticoVacio;

        // Assert
        assertEquals(diagnosticoVacio, resultadoDTO.diagnostico);
        assertTrue(resultadoDTO.diagnostico.isEmpty());
    }

    @Test
    void testSetResultadosVacio() {
        // Arrange
        String resultadosVacios = "";

        // Act
        resultadoDTO.resultados = resultadosVacios;

        // Assert
        assertEquals(resultadosVacios, resultadoDTO.resultados);
        assertTrue(resultadoDTO.resultados.isEmpty());
    }
}
