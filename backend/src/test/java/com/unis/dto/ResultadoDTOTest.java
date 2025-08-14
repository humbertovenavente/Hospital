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
        resultadoDTO.setDocumento(documento);

        // Assert
        assertEquals(documento, resultadoDTO.getDocumento());
    }

    @Test
    void testSetYGetDiagnostico() {
        // Arrange
        String diagnostico = "Gripe común";

        // Act
        resultadoDTO.setDiagnostico(diagnostico);

        // Assert
        assertEquals(diagnostico, resultadoDTO.getDiagnostico());
    }

    @Test
    void testSetYGetResultados() {
        // Arrange
        String resultados = "Paciente presenta síntomas de gripe";

        // Act
        resultadoDTO.setResultados(resultados);

        // Assert
        assertEquals(resultados, resultadoDTO.getResultados());
    }

    @Test
    void testSetYGetFecha() {
        // Arrange
        LocalDate fecha = LocalDate.of(2024, 12, 25);

        // Act
        resultadoDTO.setFecha(fecha);

        // Assert
        assertEquals(fecha, resultadoDTO.getFecha());
    }

    @Test
    void testSetYGetIdCita() {
        // Arrange
        Long idCita = 123L;

        // Act
        resultadoDTO.setIdCita(idCita);

        // Assert
        assertEquals(idCita, resultadoDTO.getIdCita());
    }

    @Test
    void testSetDocumentoNull() {
        // Act
        resultadoDTO.setDocumento(null);

        // Assert
        assertNull(resultadoDTO.getDocumento());
    }

    @Test
    void testSetDiagnosticoNull() {
        // Act
        resultadoDTO.setDiagnostico(null);

        // Assert
        assertNull(resultadoDTO.getDiagnostico());
    }

    @Test
    void testSetResultadosNull() {
        // Act
        resultadoDTO.setResultados(null);

        // Assert
        assertNull(resultadoDTO.getResultados());
    }

    @Test
    void testSetFechaNull() {
        // Act
        resultadoDTO.setFecha(null);

        // Assert
        assertNull(resultadoDTO.getFecha());
    }

    @Test
    void testSetIdCitaNull() {
        // Act
        resultadoDTO.setIdCita(null);

        // Assert
        assertNull(resultadoDTO.getIdCita());
    }

    @Test
    void testSetDocumentoVacio() {
        // Arrange
        String documentoVacio = "";

        // Act
        resultadoDTO.setDocumento(documentoVacio);

        // Assert
        assertEquals(documentoVacio, resultadoDTO.getDocumento());
        assertTrue(resultadoDTO.getDocumento().isEmpty());
    }

    @Test
    void testSetDiagnosticoVacio() {
        // Arrange
        String diagnosticoVacio = "";

        // Act
        resultadoDTO.setDiagnostico(diagnosticoVacio);

        // Assert
        assertEquals(diagnosticoVacio, resultadoDTO.getDiagnostico());
        assertTrue(resultadoDTO.getDiagnostico().isEmpty());
    }

    @Test
    void testSetResultadosVacio() {
        // Arrange
        String resultadosVacios = "";

        // Act
        resultadoDTO.setResultados(resultadosVacios);

        // Assert
        assertEquals(resultadosVacios, resultadoDTO.getResultados());
        assertTrue(resultadoDTO.getResultados().isEmpty());
    }
}
