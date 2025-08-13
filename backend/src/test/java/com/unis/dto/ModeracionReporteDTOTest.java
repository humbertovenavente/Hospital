package com.unis.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class ModeracionReporteDTOTest {

    private ModeracionReporteDTO moderacionReporteDTO;

    @BeforeEach
    void setUp() {
        moderacionReporteDTO = new ModeracionReporteDTO();
    }

    @Test
    void testConstructorPorDefecto() {
        assertNotNull(moderacionReporteDTO);
    }

    @Test
    void testConstructorConParametros() {
        // Arrange
        int numeroOrden = 1;
        String usuario = "admin";
        int totalRechazos = 5;

        // Act
        ModeracionReporteDTO dto = new ModeracionReporteDTO(numeroOrden, usuario, totalRechazos);

        // Assert
        assertEquals(numeroOrden, dto.getNumeroOrden());
        assertEquals(usuario, dto.getUsuario());
        assertEquals(totalRechazos, dto.getTotalRechazos());
    }

    @Test
    void testGettersAndSetters() {
        // Arrange
        int numeroOrden = 1;
        String usuario = "Juan Pérez";
        int totalRechazos = 10;

        // Act
        moderacionReporteDTO.setNumeroOrden(numeroOrden);
        moderacionReporteDTO.setUsuario(usuario);
        moderacionReporteDTO.setTotalRechazos(totalRechazos);

        // Assert
        assertEquals(numeroOrden, moderacionReporteDTO.getNumeroOrden());
        assertEquals(usuario, moderacionReporteDTO.getUsuario());
        assertEquals(totalRechazos, moderacionReporteDTO.getTotalRechazos());
    }

    @Test
    void testSetNumeroOrden() {
        int numeroOrden = 999;
        moderacionReporteDTO.setNumeroOrden(numeroOrden);
        assertEquals(numeroOrden, moderacionReporteDTO.getNumeroOrden());
    }

    @Test
    void testSetUsuario() {
        String usuario = "María López";
        moderacionReporteDTO.setUsuario(usuario);
        assertEquals(usuario, moderacionReporteDTO.getUsuario());
    }

    @Test
    void testSetTotalRechazos() {
        int totalRechazos = 25;
        moderacionReporteDTO.setTotalRechazos(totalRechazos);
        assertEquals(totalRechazos, moderacionReporteDTO.getTotalRechazos());
    }

    @Test
    void testSetUsuarioNull() {
        moderacionReporteDTO.setUsuario(null);
        assertNull(moderacionReporteDTO.getUsuario());
    }

    @Test
    void testSetUsuarioVacio() {
        String usuario = "";
        moderacionReporteDTO.setUsuario(usuario);
        assertEquals(usuario, moderacionReporteDTO.getUsuario());
    }

    @Test
    void testSetTotalRechazosZero() {
        int totalRechazos = 0;
        moderacionReporteDTO.setTotalRechazos(totalRechazos);
        assertEquals(totalRechazos, moderacionReporteDTO.getTotalRechazos());
    }

    @Test
    void testSetTotalRechazosNegativo() {
        int totalRechazos = -5;
        moderacionReporteDTO.setTotalRechazos(totalRechazos);
        assertEquals(totalRechazos, moderacionReporteDTO.getTotalRechazos());
    }
}
