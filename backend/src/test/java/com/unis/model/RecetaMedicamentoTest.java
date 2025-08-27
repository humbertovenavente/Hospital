package com.unis.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for RecetaMedicamento entity.
 * Tests all getters, setters, and helper methods to achieve 100% coverage.
 */
class RecetaMedicamentoTest {

    private RecetaMedicamento recetaMedicamento;
    private Receta receta;
    private Medicamento medicamento;

    @BeforeEach
    void setUp() {
        recetaMedicamento = new RecetaMedicamento();
        
        receta = new Receta();
        receta.setIdReceta(1L);
        
        medicamento = new Medicamento();
        medicamento.setIdMedicamento(100L);
    }

    @Test
    void testDefaultConstructor() {
        assertNotNull(recetaMedicamento);
        assertNull(recetaMedicamento.getIdRecetaMedicamento());
        assertNull(recetaMedicamento.getReceta());
        assertNull(recetaMedicamento.getMedicamento());
        assertNull(recetaMedicamento.getDosis());
        assertNull(recetaMedicamento.getFrecuencia());
        assertNull(recetaMedicamento.getDuracion());
        assertNull(recetaMedicamento.getDiagnostico());
    }

    // ========== ID_RECETAMEDICAMENTO Tests ==========
    
    @Test
    void testSetAndGetIdRecetaMedicamento() {
        Long id = 123L;
        recetaMedicamento.setIdRecetaMedicamento(id);
        assertEquals(id, recetaMedicamento.getIdRecetaMedicamento());
    }

    @Test
    void testSetAndGetIdRecetaMedicamentoWithNull() {
        recetaMedicamento.setIdRecetaMedicamento(null);
        assertNull(recetaMedicamento.getIdRecetaMedicamento());
    }

    @Test
    void testSetAndGetIdRecetaMedicamentoWithZero() {
        recetaMedicamento.setIdRecetaMedicamento(0L);
        assertEquals(0L, recetaMedicamento.getIdRecetaMedicamento());
    }

    @Test
    void testSetAndGetIdRecetaMedicamentoWithNegative() {
        recetaMedicamento.setIdRecetaMedicamento(-1L);
        assertEquals(-1L, recetaMedicamento.getIdRecetaMedicamento());
    }

    @Test
    void testSetAndGetIdRecetaMedicamentoWithMaxValue() {
        Long maxValue = Long.MAX_VALUE;
        recetaMedicamento.setIdRecetaMedicamento(maxValue);
        assertEquals(maxValue, recetaMedicamento.getIdRecetaMedicamento());
    }

    // ========== RECETA Tests ==========
    
    @Test
    void testSetAndGetReceta() {
        recetaMedicamento.setReceta(receta);
        assertEquals(receta, recetaMedicamento.getReceta());
    }

    @Test
    void testSetAndGetRecetaWithNull() {
        recetaMedicamento.setReceta(null);
        assertNull(recetaMedicamento.getReceta());
    }

    @Test
    void testSetAndGetRecetaWithNewInstance() {
        Receta newReceta = new Receta();
        newReceta.setIdReceta(999L);
        recetaMedicamento.setReceta(newReceta);
        assertEquals(newReceta, recetaMedicamento.getReceta());
    }

    // ========== MEDICAMENTO Tests ==========
    
    @Test
    void testSetAndGetMedicamento() {
        recetaMedicamento.setMedicamento(medicamento);
        assertEquals(medicamento, recetaMedicamento.getMedicamento());
    }

    @Test
    void testSetAndGetMedicamentoWithNull() {
        recetaMedicamento.setMedicamento(null);
        assertNull(recetaMedicamento.getMedicamento());
    }

    @Test
    void testSetAndGetMedicamentoWithNewInstance() {
        Medicamento newMedicamento = new Medicamento();
        newMedicamento.setIdMedicamento(888L);
        recetaMedicamento.setMedicamento(newMedicamento);
        assertEquals(newMedicamento, recetaMedicamento.getMedicamento());
    }

    // ========== DOSIS Tests ==========
    
    @Test
    void testSetAndGetDosis() {
        String dosis = "1 tableta";
        recetaMedicamento.setDosis(dosis);
        assertEquals(dosis, recetaMedicamento.getDosis());
    }

    @Test
    void testSetAndGetDosisWithNull() {
        recetaMedicamento.setDosis(null);
        assertNull(recetaMedicamento.getDosis());
    }

    @Test
    void testSetAndGetDosisWithEmptyString() {
        recetaMedicamento.setDosis("");
        assertEquals("", recetaMedicamento.getDosis());
    }

    @Test
    void testSetAndGetDosisWithComplexDosage() {
        String dosis = "2 cápsulas cada 8 horas";
        recetaMedicamento.setDosis(dosis);
        assertEquals(dosis, recetaMedicamento.getDosis());
    }

    @Test
    void testSetAndGetDosisWithWhitespace() {
        String dosis = " 1 tableta ";
        recetaMedicamento.setDosis(dosis);
        assertEquals(dosis, recetaMedicamento.getDosis());
    }

    // ========== FRECUENCIA Tests ==========
    
    @Test
    void testSetAndGetFrecuencia() {
        String frecuencia = "Cada 8 horas";
        recetaMedicamento.setFrecuencia(frecuencia);
        assertEquals(frecuencia, recetaMedicamento.getFrecuencia());
    }

    @Test
    void testSetAndGetFrecuenciaWithNull() {
        recetaMedicamento.setFrecuencia(null);
        assertNull(recetaMedicamento.getFrecuencia());
    }

    @Test
    void testSetAndGetFrecuenciaWithEmptyString() {
        recetaMedicamento.setFrecuencia("");
        assertEquals("", recetaMedicamento.getFrecuencia());
    }

    @Test
    void testSetAndGetFrecuenciaWithDifferentPatterns() {
        String[] frecuencias = {
            "Cada 12 horas",
            "3 veces al día",
            "Antes de cada comida",
            "Una vez por semana"
        };
        
        for (String freq : frecuencias) {
            recetaMedicamento.setFrecuencia(freq);
            assertEquals(freq, recetaMedicamento.getFrecuencia());
        }
    }

    @Test
    void testSetAndGetFrecuenciaWithWhitespace() {
        String frecuencia = " Cada 6 horas ";
        recetaMedicamento.setFrecuencia(frecuencia);
        assertEquals(frecuencia, recetaMedicamento.getFrecuencia());
    }

    // ========== DURACION Tests ==========
    
    @Test
    void testSetAndGetDuracion() {
        String duracion = "7 días";
        recetaMedicamento.setDuracion(duracion);
        assertEquals(duracion, recetaMedicamento.getDuracion());
    }

    @Test
    void testSetAndGetDuracionWithNull() {
        recetaMedicamento.setDuracion(null);
        assertNull(recetaMedicamento.getDuracion());
    }

    @Test
    void testSetAndGetDuracionWithEmptyString() {
        recetaMedicamento.setDuracion("");
        assertEquals("", recetaMedicamento.getDuracion());
    }

    @Test
    void testSetAndGetDuracionWithDifferentFormats() {
        String[] duraciones = {
            "10 días",
            "2 semanas",
            "1 mes",
            "Hasta completar el tratamiento",
            "Indefinido"
        };
        
        for (String dur : duraciones) {
            recetaMedicamento.setDuracion(dur);
            assertEquals(dur, recetaMedicamento.getDuracion());
        }
    }

    @Test
    void testSetAndGetDuracionWithWhitespace() {
        String duracion = " 5 días ";
        recetaMedicamento.setDuracion(duracion);
        assertEquals(duracion, recetaMedicamento.getDuracion());
    }

    // ========== DIAGNOSTICO Tests ==========
    
    @Test
    void testSetAndGetDiagnostico() {
        String diagnostico = "Hipertensión arterial";
        recetaMedicamento.setDiagnostico(diagnostico);
        assertEquals(diagnostico, recetaMedicamento.getDiagnostico());
    }

    @Test
    void testSetAndGetDiagnosticoWithNull() {
        recetaMedicamento.setDiagnostico(null);
        assertNull(recetaMedicamento.getDiagnostico());
    }

    @Test
    void testSetAndGetDiagnosticoWithEmptyString() {
        recetaMedicamento.setDiagnostico("");
        assertEquals("", recetaMedicamento.getDiagnostico());
    }

    @Test
    void testSetAndGetDiagnosticoWithLongText() {
        String diagnostico = "Diabetes mellitus tipo 2 con complicaciones microvasculares, incluyendo retinopatía diabética no proliferativa y nefropatía diabética en estadio temprano";
        recetaMedicamento.setDiagnostico(diagnostico);
        assertEquals(diagnostico, recetaMedicamento.getDiagnostico());
    }

    @Test
    void testSetAndGetDiagnosticoWithSpecialCharacters() {
        String diagnostico = "Síndrome de Sjögren con manifestaciones extra-glandulares";
        recetaMedicamento.setDiagnostico(diagnostico);
        assertEquals(diagnostico, recetaMedicamento.getDiagnostico());
    }

    @Test
    void testSetAndGetDiagnosticoWithWhitespace() {
        String diagnostico = " Hipertensión arterial ";
        recetaMedicamento.setDiagnostico(diagnostico);
        assertEquals(diagnostico, recetaMedicamento.getDiagnostico());
    }

    // ========== HELPER METHODS Tests ==========
    
    @Test
    void testGetIdReceta_WithRecetaSet() {
        recetaMedicamento.setReceta(receta);
        assertEquals(1L, recetaMedicamento.getIdReceta());
    }

    @Test
    void testGetIdReceta_WithNullReceta() {
        recetaMedicamento.setReceta(null);
        assertNull(recetaMedicamento.getIdReceta());
    }

    @Test
    void testGetIdReceta_WithRecetaWithoutId() {
        Receta recetaSinId = new Receta();
        recetaMedicamento.setReceta(recetaSinId);
        assertNull(recetaMedicamento.getIdReceta());
    }

    @Test
    void testGetIdMedicamento_WithMedicamentoSet() {
        recetaMedicamento.setMedicamento(medicamento);
        assertEquals(100L, recetaMedicamento.getIdMedicamento());
    }

    @Test
    void testGetIdMedicamento_WithNullMedicamento() {
        recetaMedicamento.setMedicamento(null);
        assertNull(recetaMedicamento.getIdMedicamento());
    }

    @Test
    void testGetIdMedicamento_WithMedicamentoWithoutId() {
        Medicamento medicamentoSinId = new Medicamento();
        recetaMedicamento.setMedicamento(medicamentoSinId);
        assertNull(recetaMedicamento.getIdMedicamento());
    }

    // ========== Comprehensive Tests ==========
    
    @Test
    void testCompleteRecetaMedicamentoSetup() {
        // Arrange
        Long id = 456L;
        String dosis = "1 tableta";
        String frecuencia = "Cada 8 horas";
        String duracion = "7 días";
        String diagnostico = "Dolor de cabeza";

        // Act
        recetaMedicamento.setIdRecetaMedicamento(id);
        recetaMedicamento.setReceta(receta);
        recetaMedicamento.setMedicamento(medicamento);
        recetaMedicamento.setDosis(dosis);
        recetaMedicamento.setFrecuencia(frecuencia);
        recetaMedicamento.setDuracion(duracion);
        recetaMedicamento.setDiagnostico(diagnostico);

        // Assert
        assertEquals(id, recetaMedicamento.getIdRecetaMedicamento());
        assertEquals(receta, recetaMedicamento.getReceta());
        assertEquals(medicamento, recetaMedicamento.getMedicamento());
        assertEquals(dosis, recetaMedicamento.getDosis());
        assertEquals(frecuencia, recetaMedicamento.getFrecuencia());
        assertEquals(duracion, recetaMedicamento.getDuracion());
        assertEquals(diagnostico, recetaMedicamento.getDiagnostico());
        
        // Test helper methods
        assertEquals(1L, recetaMedicamento.getIdReceta());
        assertEquals(100L, recetaMedicamento.getIdMedicamento());
    }

    @Test
    void testMultipleUpdates() {
        // Arrange
        recetaMedicamento.setDosis("1 tableta");
        recetaMedicamento.setFrecuencia("Cada 8 horas");
        
        // Act - Update values
        recetaMedicamento.setDosis("2 tabletas");
        recetaMedicamento.setFrecuencia("Cada 12 horas");
        
        // Assert
        assertEquals("2 tabletas", recetaMedicamento.getDosis());
        assertEquals("Cada 12 horas", recetaMedicamento.getFrecuencia());
    }

    @Test
    void testResetToNull() {
        // Arrange
        recetaMedicamento.setDosis("1 tableta");
        recetaMedicamento.setFrecuencia("Cada 8 horas");
        
        // Act - Reset to null
        recetaMedicamento.setDosis(null);
        recetaMedicamento.setFrecuencia(null);
        
        // Assert
        assertNull(recetaMedicamento.getDosis());
        assertNull(recetaMedicamento.getFrecuencia());
    }

    @Test
    void testEdgeCaseValues() {
        // Arrange & Act - Set edge case values
        recetaMedicamento.setIdRecetaMedicamento(0L);
        recetaMedicamento.setDosis("");
        recetaMedicamento.setFrecuencia("   ");
        recetaMedicamento.setDuracion("0");
        recetaMedicamento.setDiagnostico("-1");
        
        // Assert
        assertEquals(0L, recetaMedicamento.getIdRecetaMedicamento());
        assertEquals("", recetaMedicamento.getDosis());
        assertEquals("   ", recetaMedicamento.getFrecuencia());
        assertEquals("0", recetaMedicamento.getDuracion());
        assertEquals("-1", recetaMedicamento.getDiagnostico());
    }

    @Test
    void testRelationshipUpdates() {
        // Arrange
        Receta nuevaReceta = new Receta();
        nuevaReceta.setIdReceta(999L);
        
        Medicamento nuevoMedicamento = new Medicamento();
        nuevoMedicamento.setIdMedicamento(888L);
        
        // Act
        recetaMedicamento.setReceta(nuevaReceta);
        recetaMedicamento.setMedicamento(nuevoMedicamento);
        
        // Assert
        assertEquals(nuevaReceta, recetaMedicamento.getReceta());
        assertEquals(nuevoMedicamento, recetaMedicamento.getMedicamento());
        assertEquals(999L, recetaMedicamento.getIdReceta());
        assertEquals(888L, recetaMedicamento.getIdMedicamento());
    }
}
