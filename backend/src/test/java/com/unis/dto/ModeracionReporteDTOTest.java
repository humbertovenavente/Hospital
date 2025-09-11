package com.unis.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ModeracionReporteDTO.
 * Tests all getters, setters, and constructor functionality.
 */
class ModeracionReporteDTOTest {

    private ModeracionReporteDTO moderacionDTO;
    private int numeroOrden;
    private String usuario;
    private int totalRechazos;

    @BeforeEach
    void setUp() {
        numeroOrden = 1;
        usuario = "moderador@hospital.com";
        totalRechazos = 5;
        moderacionDTO = new ModeracionReporteDTO(numeroOrden, usuario, totalRechazos);
    }

    @Test
    void testDefaultConstructor() {
        ModeracionReporteDTO emptyDTO = new ModeracionReporteDTO();
        assertNotNull(emptyDTO);
        assertEquals(0, emptyDTO.getNumeroOrden());
        assertNull(emptyDTO.getUsuario());
        assertEquals(0, emptyDTO.getTotalRechazos());
    }

    @Test
    void testParameterizedConstructor() {
        assertNotNull(moderacionDTO);
        assertEquals(numeroOrden, moderacionDTO.getNumeroOrden());
        assertEquals(usuario, moderacionDTO.getUsuario());
        assertEquals(totalRechazos, moderacionDTO.getTotalRechazos());
    }

    @Test
    void testConstructorWithZeroValues() {
        ModeracionReporteDTO zeroDTO = new ModeracionReporteDTO(0, "", 0);
        assertEquals(0, zeroDTO.getNumeroOrden());
        assertEquals("", zeroDTO.getUsuario());
        assertEquals(0, zeroDTO.getTotalRechazos());
    }

    @Test
    void testConstructorWithNegativeValues() {
        ModeracionReporteDTO negativeDTO = new ModeracionReporteDTO(-1, "test", -5);
        assertEquals(-1, negativeDTO.getNumeroOrden());
        assertEquals("test", negativeDTO.getUsuario());
        assertEquals(-5, negativeDTO.getTotalRechazos());
    }

    @Test
    void testGetNumeroOrden() {
        assertEquals(numeroOrden, moderacionDTO.getNumeroOrden());
    }

    @Test
    void testSetNumeroOrden() {
        int newNumeroOrden = 10;
        moderacionDTO.setNumeroOrden(newNumeroOrden);
        assertEquals(newNumeroOrden, moderacionDTO.getNumeroOrden());
    }

    @Test
    void testSetNumeroOrdenWithZero() {
        moderacionDTO.setNumeroOrden(0);
        assertEquals(0, moderacionDTO.getNumeroOrden());
    }

    @Test
    void testSetNumeroOrdenWithNegative() {
        moderacionDTO.setNumeroOrden(-5);
        assertEquals(-5, moderacionDTO.getNumeroOrden());
    }

    @Test
    void testSetNumeroOrdenWithMaxValue() {
        moderacionDTO.setNumeroOrden(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, moderacionDTO.getNumeroOrden());
    }

    @Test
    void testSetNumeroOrdenWithMinValue() {
        moderacionDTO.setNumeroOrden(Integer.MIN_VALUE);
        assertEquals(Integer.MIN_VALUE, moderacionDTO.getNumeroOrden());
    }

    @Test
    void testGetUsuario() {
        assertEquals(usuario, moderacionDTO.getUsuario());
    }

    @Test
    void testSetUsuario() {
        String newUsuario = "nuevo.moderador@hospital.com";
        moderacionDTO.setUsuario(newUsuario);
        assertEquals(newUsuario, moderacionDTO.getUsuario());
    }

    @Test
    void testSetUsuarioWithNull() {
        moderacionDTO.setUsuario(null);
        assertNull(moderacionDTO.getUsuario());
    }

    @Test
    void testSetUsuarioWithEmptyString() {
        moderacionDTO.setUsuario("");
        assertEquals("", moderacionDTO.getUsuario());
    }

    @Test
    void testSetUsuarioWithSpecialCharacters() {
        String specialUsuario = "moderador.especial@hospital-gt.com";
        moderacionDTO.setUsuario(specialUsuario);
        assertEquals(specialUsuario, moderacionDTO.getUsuario());
    }

    @Test
    void testSetUsuarioWithSpaces() {
        String spacedUsuario = "  moderador  ";
        moderacionDTO.setUsuario(spacedUsuario);
        assertEquals(spacedUsuario, moderacionDTO.getUsuario());
    }

    @Test
    void testGetTotalRechazos() {
        assertEquals(totalRechazos, moderacionDTO.getTotalRechazos());
    }

    @Test
    void testSetTotalRechazos() {
        int newTotalRechazos = 15;
        moderacionDTO.setTotalRechazos(newTotalRechazos);
        assertEquals(newTotalRechazos, moderacionDTO.getTotalRechazos());
    }

    @Test
    void testSetTotalRechazosWithZero() {
        moderacionDTO.setTotalRechazos(0);
        assertEquals(0, moderacionDTO.getTotalRechazos());
    }

    @Test
    void testSetTotalRechazosWithNegative() {
        moderacionDTO.setTotalRechazos(-3);
        assertEquals(-3, moderacionDTO.getTotalRechazos());
    }

    @Test
    void testSetTotalRechazosWithMaxValue() {
        moderacionDTO.setTotalRechazos(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, moderacionDTO.getTotalRechazos());
    }

    @Test
    void testSetTotalRechazosWithMinValue() {
        moderacionDTO.setTotalRechazos(Integer.MIN_VALUE);
        assertEquals(Integer.MIN_VALUE, moderacionDTO.getTotalRechazos());
    }

    @Test
    void testMultipleSetters() {
        ModeracionReporteDTO multiDTO = new ModeracionReporteDTO();
        
        multiDTO.setNumeroOrden(999);
        multiDTO.setUsuario("sistema@hospital.com");
        multiDTO.setTotalRechazos(50);

        assertEquals(999, multiDTO.getNumeroOrden());
        assertEquals("sistema@hospital.com", multiDTO.getUsuario());
        assertEquals(50, multiDTO.getTotalRechazos());
    }

    @Test
    void testEdgeCaseValues() {
        ModeracionReporteDTO edgeDTO = new ModeracionReporteDTO();
        
        edgeDTO.setNumeroOrden(Integer.MAX_VALUE);
        edgeDTO.setUsuario("a");
        edgeDTO.setTotalRechazos(Integer.MIN_VALUE);

        assertEquals(Integer.MAX_VALUE, edgeDTO.getNumeroOrden());
        assertEquals("a", edgeDTO.getUsuario());
        assertEquals(Integer.MIN_VALUE, edgeDTO.getTotalRechazos());
    }

    @Test
    void testDataIntegrity() {
        // Verificar que los datos no cambien después de múltiples lecturas
        int numero1 = moderacionDTO.getNumeroOrden();
        int numero2 = moderacionDTO.getNumeroOrden();
        assertEquals(numero1, numero2);

        String usuario1 = moderacionDTO.getUsuario();
        String usuario2 = moderacionDTO.getUsuario();
        assertEquals(usuario1, usuario2);

        int rechazos1 = moderacionDTO.getTotalRechazos();
        int rechazos2 = moderacionDTO.getTotalRechazos();
        assertEquals(rechazos1, rechazos2);
    }

    @Test
    void testConstructorWithNullUsuario() {
        ModeracionReporteDTO nullUsuarioDTO = new ModeracionReporteDTO(1, null, 5);
        assertEquals(1, nullUsuarioDTO.getNumeroOrden());
        assertNull(nullUsuarioDTO.getUsuario());
        assertEquals(5, nullUsuarioDTO.getTotalRechazos());
    }

    @Test
    void testConstructorWithEmptyUsuario() {
        ModeracionReporteDTO emptyUsuarioDTO = new ModeracionReporteDTO(1, "", 5);
        assertEquals(1, emptyUsuarioDTO.getNumeroOrden());
        assertEquals("", emptyUsuarioDTO.getUsuario());
        assertEquals(5, emptyUsuarioDTO.getTotalRechazos());
    }

    @Test
    void testAllFieldsUpdate() {
        // Actualizar todos los campos y verificar
        moderacionDTO.setNumeroOrden(100);
        moderacionDTO.setUsuario("admin@hospital.com");
        moderacionDTO.setTotalRechazos(25);

        assertEquals(100, moderacionDTO.getNumeroOrden());
        assertEquals("admin@hospital.com", moderacionDTO.getUsuario());
        assertEquals(25, moderacionDTO.getTotalRechazos());
    }

    @Test
    void testRepeatedUpdates() {
        // Actualizar el mismo valor múltiples veces
        for (int i = 0; i < 5; i++) {
            moderacionDTO.setNumeroOrden(i);
            moderacionDTO.setTotalRechazos(i * 2);
        }

        assertEquals(4, moderacionDTO.getNumeroOrden());
        assertEquals(8, moderacionDTO.getTotalRechazos());
    }
}
