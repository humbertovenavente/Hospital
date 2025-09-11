package com.unis.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.io.Serializable;

/**
 * Test class for Medicamento entity.
 * Tests all getters, setters, and business logic functionality.
 */
class MedicamentoTest {

    private Medicamento medicamento;

    @BeforeEach
    void setUp() {
        medicamento = new Medicamento();
    }

    @Test
    void testDefaultConstructor() {
        assertNotNull(medicamento);
        assertNull(medicamento.getIdMedicamento());
        assertNull(medicamento.getPrincipioActivo());
        assertNull(medicamento.getConcentracion());
        assertNull(medicamento.getPresentacion());
        assertNull(medicamento.getFormaFarmaceutica());
        assertNull(medicamento.getVentaLibre());
    }

    @Test
    void testImplementsSerializable() {
        assertTrue(medicamento instanceof Serializable);
    }

    // ========== ID_MEDICAMENTO Tests ==========
    
    @Test
    void testSetAndGetIdMedicamento() {
        Long id = 123L;
        medicamento.setIdMedicamento(id);
        assertEquals(id, medicamento.getIdMedicamento());
    }

    @Test
    void testSetAndGetIdMedicamentoWithNull() {
        medicamento.setIdMedicamento(null);
        assertNull(medicamento.getIdMedicamento());
    }

    @Test
    void testSetAndGetIdMedicamentoWithZero() {
        medicamento.setIdMedicamento(0L);
        assertEquals(0L, medicamento.getIdMedicamento());
    }

    @Test
    void testSetAndGetIdMedicamentoWithNegative() {
        medicamento.setIdMedicamento(-1L);
        assertEquals(-1L, medicamento.getIdMedicamento());
    }

    @Test
    void testSetAndGetIdMedicamentoWithMaxValue() {
        Long maxValue = Long.MAX_VALUE;
        medicamento.setIdMedicamento(maxValue);
        assertEquals(maxValue, medicamento.getIdMedicamento());
    }

    @Test
    void testSetAndGetIdMedicamentoWithMinValue() {
        Long minValue = Long.MIN_VALUE;
        medicamento.setIdMedicamento(minValue);
        assertEquals(minValue, medicamento.getIdMedicamento());
    }

    // ========== PRINCIPIO_ACTIVO Tests ==========
    
    @Test
    void testSetAndGetPrincipioActivo() {
        String principioActivo = "Paracetamol";
        medicamento.setPrincipioActivo(principioActivo);
        assertEquals(principioActivo, medicamento.getPrincipioActivo());
    }

    @Test
    void testSetAndGetPrincipioActivoWithNull() {
        medicamento.setPrincipioActivo(null);
        assertNull(medicamento.getPrincipioActivo());
    }

    @Test
    void testSetAndGetPrincipioActivoWithEmptyString() {
        medicamento.setPrincipioActivo("");
        assertEquals("", medicamento.getPrincipioActivo());
    }

    @Test
    void testSetAndGetPrincipioActivoWithWhitespace() {
        medicamento.setPrincipioActivo("   ");
        assertEquals("   ", medicamento.getPrincipioActivo());
    }

    @Test
    void testSetAndGetPrincipioActivoWithSpecialCharacters() {
        String principioActivo = "Ácido Acetilsalicílico (AAS)";
        medicamento.setPrincipioActivo(principioActivo);
        assertEquals(principioActivo, medicamento.getPrincipioActivo());
    }

    @Test
    void testSetAndGetPrincipioActivoWithNumbers() {
        String principioActivo = "Vitamina B12";
        medicamento.setPrincipioActivo(principioActivo);
        assertEquals(principioActivo, medicamento.getPrincipioActivo());
    }

    @Test
    void testSetAndGetPrincipioActivoWithLongString() {
        String principioActivo = "Ácido 2-(4-isobutilfenil)propanoico";
        medicamento.setPrincipioActivo(principioActivo);
        assertEquals(principioActivo, medicamento.getPrincipioActivo());
    }

    // ========== CONCENTRACION Tests ==========
    
    @Test
    void testSetAndGetConcentracion() {
        String concentracion = "500mg";
        medicamento.setConcentracion(concentracion);
        assertEquals(concentracion, medicamento.getConcentracion());
    }

    @Test
    void testSetAndGetConcentracionWithNull() {
        medicamento.setConcentracion(null);
        assertNull(medicamento.getConcentracion());
    }

    @Test
    void testSetAndGetConcentracionWithEmptyString() {
        medicamento.setConcentracion("");
        assertEquals("", medicamento.getConcentracion());
    }

    @Test
    void testSetAndGetConcentracionWithComplexFormat() {
        String concentracion = "250mg/5ml";
        medicamento.setConcentracion(concentracion);
        assertEquals(concentracion, medicamento.getConcentracion());
    }

    @Test
    void testSetAndGetConcentracionWithPercentage() {
        String concentracion = "10%";
        medicamento.setConcentracion(concentracion);
        assertEquals(concentracion, medicamento.getConcentracion());
    }

    @Test
    void testSetAndGetConcentracionWithUnits() {
        String concentracion = "1000 UI";
        medicamento.setConcentracion(concentracion);
        assertEquals(concentracion, medicamento.getConcentracion());
    }

    @Test
    void testSetAndGetConcentracionWithWhitespace() {
        String concentracion = " 500 mg ";
        medicamento.setConcentracion(concentracion);
        assertEquals(concentracion, medicamento.getConcentracion());
    }

    // ========== PRESENTACION Tests ==========
    
    @Test
    void testSetAndGetPresentacion() {
        String presentacion = "Tableta";
        medicamento.setPresentacion(presentacion);
        assertEquals(presentacion, medicamento.getPresentacion());
    }

    @Test
    void testSetAndGetPresentacionWithNull() {
        medicamento.setPresentacion(null);
        assertNull(medicamento.getPresentacion());
    }

    @Test
    void testSetAndGetPresentacionWithEmptyString() {
        medicamento.setPresentacion("");
        assertEquals("", medicamento.getPresentacion());
    }

    @Test
    void testSetAndGetPresentacionWithCapsule() {
        String presentacion = "Cápsula";
        medicamento.setPresentacion(presentacion);
        assertEquals(presentacion, medicamento.getPresentacion());
    }

    @Test
    void testSetAndGetPresentacionWithSyrup() {
        String presentacion = "Jarabe";
        medicamento.setPresentacion(presentacion);
        assertEquals(presentacion, medicamento.getPresentacion());
    }

    @Test
    void testSetAndGetPresentacionWithInjection() {
        String presentacion = "Inyección";
        medicamento.setPresentacion(presentacion);
        assertEquals(presentacion, medicamento.getPresentacion());
    }

    @Test
    void testSetAndGetPresentacionWithCream() {
        String presentacion = "Crema";
        medicamento.setPresentacion(presentacion);
        assertEquals(presentacion, medicamento.getPresentacion());
    }

    @Test
    void testSetAndGetPresentacionWithWhitespace() {
        String presentacion = " Tableta ";
        medicamento.setPresentacion(presentacion);
        assertEquals(presentacion, medicamento.getPresentacion());
    }

    // ========== FORMA_FARMACEUTICA Tests ==========
    
    @Test
    void testSetAndGetFormaFarmaceutica() {
        String formaFarmaceutica = "Sólida";
        medicamento.setFormaFarmaceutica(formaFarmaceutica);
        assertEquals(formaFarmaceutica, medicamento.getFormaFarmaceutica());
    }

    @Test
    void testSetAndGetFormaFarmaceuticaWithNull() {
        medicamento.setFormaFarmaceutica(null);
        assertNull(medicamento.getFormaFarmaceutica());
    }

    @Test
    void testSetAndGetFormaFarmaceuticaWithEmptyString() {
        medicamento.setFormaFarmaceutica("");
        assertEquals("", medicamento.getFormaFarmaceutica());
    }

    @Test
    void testSetAndGetFormaFarmaceuticaWithLiquid() {
        String formaFarmaceutica = "Líquida";
        medicamento.setFormaFarmaceutica(formaFarmaceutica);
        assertEquals(formaFarmaceutica, medicamento.getFormaFarmaceutica());
    }

    @Test
    void testSetAndGetFormaFarmaceuticaWithTopical() {
        String formaFarmaceutica = "Tópica";
        medicamento.setFormaFarmaceutica(formaFarmaceutica);
        assertEquals(formaFarmaceutica, medicamento.getFormaFarmaceutica());
    }

    @Test
    void testSetAndGetFormaFarmaceuticaWithInhalation() {
        String formaFarmaceutica = "Inhalación";
        medicamento.setFormaFarmaceutica(formaFarmaceutica);
        assertEquals(formaFarmaceutica, medicamento.getFormaFarmaceutica());
    }

    @Test
    void testSetAndGetFormaFarmaceuticaWithWhitespace() {
        String formaFarmaceutica = " Sólida ";
        medicamento.setFormaFarmaceutica(formaFarmaceutica);
        assertEquals(formaFarmaceutica, medicamento.getFormaFarmaceutica());
    }

    // ========== VENTA_LIBRE Tests ==========
    
    @Test
    void testSetAndGetVentaLibre() {
        Integer ventaLibre = 1;
        medicamento.setVentaLibre(ventaLibre);
        assertEquals(ventaLibre, medicamento.getVentaLibre());
    }

    @Test
    void testSetAndGetVentaLibreWithNull() {
        medicamento.setVentaLibre(null);
        assertNull(medicamento.getVentaLibre());
    }

    @Test
    void testSetAndGetVentaLibreWithZero() {
        Integer ventaLibre = 0;
        medicamento.setVentaLibre(ventaLibre);
        assertEquals(ventaLibre, medicamento.getVentaLibre());
    }

    @Test
    void testSetAndGetVentaLibreWithOne() {
        Integer ventaLibre = 1;
        medicamento.setVentaLibre(ventaLibre);
        assertEquals(ventaLibre, medicamento.getVentaLibre());
    }

    @Test
    void testSetAndGetVentaLibreWithNegative() {
        Integer ventaLibre = -1;
        medicamento.setVentaLibre(ventaLibre);
        assertEquals(ventaLibre, medicamento.getVentaLibre());
    }

    @Test
    void testSetAndGetVentaLibreWithLargeNumber() {
        Integer ventaLibre = 999;
        medicamento.setVentaLibre(ventaLibre);
        assertEquals(ventaLibre, medicamento.getVentaLibre());
    }

    @Test
    void testSetAndGetVentaLibreWithMaxValue() {
        Integer ventaLibre = Integer.MAX_VALUE;
        medicamento.setVentaLibre(ventaLibre);
        assertEquals(ventaLibre, medicamento.getVentaLibre());
    }

    @Test
    void testSetAndGetVentaLibreWithMinValue() {
        Integer ventaLibre = Integer.MIN_VALUE;
        medicamento.setVentaLibre(ventaLibre);
        assertEquals(ventaLibre, medicamento.getVentaLibre());
    }

    // ========== Comprehensive Tests ==========
    
    @Test
    void testCompleteMedicamentoSetup() {
        // Arrange
        Long id = 456L;
        String principioActivo = "Ibuprofeno";
        String concentracion = "400mg";
        String presentacion = "Cápsula blanda";
        String formaFarmaceutica = "Sólida";
        Integer ventaLibre = 1;

        // Act
        medicamento.setIdMedicamento(id);
        medicamento.setPrincipioActivo(principioActivo);
        medicamento.setConcentracion(concentracion);
        medicamento.setPresentacion(presentacion);
        medicamento.setFormaFarmaceutica(formaFarmaceutica);
        medicamento.setVentaLibre(ventaLibre);

        // Assert
        assertEquals(id, medicamento.getIdMedicamento());
        assertEquals(principioActivo, medicamento.getPrincipioActivo());
        assertEquals(concentracion, medicamento.getConcentracion());
        assertEquals(presentacion, medicamento.getPresentacion());
        assertEquals(formaFarmaceutica, medicamento.getFormaFarmaceutica());
        assertEquals(ventaLibre, medicamento.getVentaLibre());
    }

    @Test
    void testMultipleUpdates() {
        // Arrange
        medicamento.setPrincipioActivo("Paracetamol");
        medicamento.setConcentracion("500mg");
        
        // Act - Update values
        medicamento.setPrincipioActivo("Ibuprofeno");
        medicamento.setConcentracion("400mg");
        
        // Assert
        assertEquals("Ibuprofeno", medicamento.getPrincipioActivo());
        assertEquals("400mg", medicamento.getConcentracion());
    }

    @Test
    void testResetToNull() {
        // Arrange
        medicamento.setPrincipioActivo("Paracetamol");
        medicamento.setConcentracion("500mg");
        
        // Act - Reset to null
        medicamento.setPrincipioActivo(null);
        medicamento.setConcentracion(null);
        
        // Assert
        assertNull(medicamento.getPrincipioActivo());
        assertNull(medicamento.getConcentracion());
    }

    @Test
    void testEdgeCaseValues() {
        // Arrange & Act - Set edge case values
        medicamento.setIdMedicamento(0L);
        medicamento.setPrincipioActivo("");
        medicamento.setConcentracion("   ");
        medicamento.setPresentacion("0");
        medicamento.setFormaFarmaceutica("-1");
        medicamento.setVentaLibre(0);
        
        // Assert
        assertEquals(0L, medicamento.getIdMedicamento());
        assertEquals("", medicamento.getPrincipioActivo());
        assertEquals("   ", medicamento.getConcentracion());
        assertEquals("0", medicamento.getPresentacion());
        assertEquals("-1", medicamento.getFormaFarmaceutica());
        assertEquals(0, medicamento.getVentaLibre());
    }
}
