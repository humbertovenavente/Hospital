package com.unis.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;

public class ResultadoDTOTest {
    
    private ResultadoDTO resultadoDTO;
    private LocalDate testDate;
    
    @BeforeEach
    void setUp() {
        resultadoDTO = new ResultadoDTO();
        testDate = LocalDate.of(2024, 6, 15);
    }
    
    @Test
    void testDefaultConstructor() {
        assertNotNull(resultadoDTO);
        assertNull(resultadoDTO.getDocumento());
        assertNull(resultadoDTO.getDiagnostico());
        assertNull(resultadoDTO.getResultados());
        assertNull(resultadoDTO.getFecha());
        assertNull(resultadoDTO.getIdCita());
    }
    
    @Test
    void testSetAndGetDocumento() {
        resultadoDTO.setDocumento("DOC-001");
        assertEquals("DOC-001", resultadoDTO.getDocumento());
        
        resultadoDTO.setDocumento(null);
        assertNull(resultadoDTO.getDocumento());
        
        resultadoDTO.setDocumento("");
        assertEquals("", resultadoDTO.getDocumento());
        
        resultadoDTO.setDocumento("RESULTADO-2024-001");
        assertEquals("RESULTADO-2024-001", resultadoDTO.getDocumento());
    }
    
    @Test
    void testSetAndGetDiagnostico() {
        resultadoDTO.setDiagnostico("Gripe común");
        assertEquals("Gripe común", resultadoDTO.getDiagnostico());
        
        resultadoDTO.setDiagnostico(null);
        assertNull(resultadoDTO.getDiagnostico());
        
        resultadoDTO.setDiagnostico("");
        assertEquals("", resultadoDTO.getDiagnostico());
        
        resultadoDTO.setDiagnostico("Hipertensión arterial");
        assertEquals("Hipertensión arterial", resultadoDTO.getDiagnostico());
    }
    
    @Test
    void testSetAndGetResultados() {
        resultadoDTO.setResultados("Hemograma completo normal");
        assertEquals("Hemograma completo normal", resultadoDTO.getResultados());
        
        resultadoDTO.setResultados(null);
        assertNull(resultadoDTO.getResultados());
        
        resultadoDTO.setResultados("");
        assertEquals("", resultadoDTO.getResultados());
        
        resultadoDTO.setResultados("Presión arterial: 140/90 mmHg");
        assertEquals("Presión arterial: 140/90 mmHg", resultadoDTO.getResultados());
    }
    
    @Test
    void testSetAndGetFecha() {
        resultadoDTO.setFecha(testDate);
        assertEquals(testDate, resultadoDTO.getFecha());
        
        resultadoDTO.setFecha(null);
        assertNull(resultadoDTO.getFecha());
        
        LocalDate pastDate = LocalDate.of(2020, 1, 1);
        resultadoDTO.setFecha(pastDate);
        assertEquals(pastDate, resultadoDTO.getFecha());
        
        LocalDate futureDate = LocalDate.of(2030, 12, 31);
        resultadoDTO.setFecha(futureDate);
        assertEquals(futureDate, resultadoDTO.getFecha());
    }
    
    @Test
    void testSetAndGetIdCita() {
        resultadoDTO.setIdCita(1L);
        assertEquals(1L, resultadoDTO.getIdCita());
        
        resultadoDTO.setIdCita(null);
        assertNull(resultadoDTO.getIdCita());
        
        resultadoDTO.setIdCita(0L);
        assertEquals(0L, resultadoDTO.getIdCita());
        
        resultadoDTO.setIdCita(999L);
        assertEquals(999L, resultadoDTO.getIdCita());
    }
    
    @Test
    void testCompleteObject() {
        // Set all fields
        resultadoDTO.setDocumento("RES-2024-001");
        resultadoDTO.setDiagnostico("Diabetes tipo 2");
        resultadoDTO.setResultados("Glucosa en ayunas: 180 mg/dL");
        resultadoDTO.setFecha(testDate);
        resultadoDTO.setIdCita(123L);
        
        // Verify all fields
        assertEquals("RES-2024-001", resultadoDTO.getDocumento());
        assertEquals("Diabetes tipo 2", resultadoDTO.getDiagnostico());
        assertEquals("Glucosa en ayunas: 180 mg/dL", resultadoDTO.getResultados());
        assertEquals(testDate, resultadoDTO.getFecha());
        assertEquals(123L, resultadoDTO.getIdCita());
    }
    
    @Test
    void testFieldModification() {
        // Set initial values
        resultadoDTO.setDocumento("INITIAL");
        resultadoDTO.setDiagnostico("Initial Diagnosis");
        resultadoDTO.setIdCita(1L);
        
        // Modify values
        resultadoDTO.setDocumento("MODIFIED");
        resultadoDTO.setDiagnostico("Modified Diagnosis");
        resultadoDTO.setIdCita(2L);
        
        // Verify modifications
        assertEquals("MODIFIED", resultadoDTO.getDocumento());
        assertEquals("Modified Diagnosis", resultadoDTO.getDiagnostico());
        assertEquals(2L, resultadoDTO.getIdCita());
    }
    
    @Test
    void testEdgeCases() {
        // Test with very long strings
        String longString = "A".repeat(1000);
        resultadoDTO.setResultados(longString);
        assertEquals(longString, resultadoDTO.getResultados());
        
        // Test with special characters
        resultadoDTO.setDiagnostico("Cáncer de pulmón (estadio III)");
        assertEquals("Cáncer de pulmón (estadio III)", resultadoDTO.getDiagnostico());
        
        // Test with numbers in strings
        resultadoDTO.setDocumento("RESULTADO-2024-001");
        assertEquals("RESULTADO-2024-001", resultadoDTO.getDocumento());
        
        // Test with extreme date values
        LocalDate minDate = LocalDate.MIN;
        LocalDate maxDate = LocalDate.MAX;
        resultadoDTO.setFecha(minDate);
        assertEquals(minDate, resultadoDTO.getFecha());
        resultadoDTO.setFecha(maxDate);
        assertEquals(maxDate, resultadoDTO.getFecha());
    }
    
    @Test
    void testNullHandling() {
        // Test setting all fields to null
        resultadoDTO.setDocumento(null);
        resultadoDTO.setDiagnostico(null);
        resultadoDTO.setResultados(null);
        resultadoDTO.setFecha(null);
        resultadoDTO.setIdCita(null);
        
        // Verify all are null
        assertNull(resultadoDTO.getDocumento());
        assertNull(resultadoDTO.getDiagnostico());
        assertNull(resultadoDTO.getResultados());
        assertNull(resultadoDTO.getFecha());
        assertNull(resultadoDTO.getIdCita());
    }
    
    @Test
    void testEmptyStringHandling() {
        // Test setting all fields to empty strings
        resultadoDTO.setDocumento("");
        resultadoDTO.setDiagnostico("");
        resultadoDTO.setResultados("");
        
        // Verify all are empty strings
        assertEquals("", resultadoDTO.getDocumento());
        assertEquals("", resultadoDTO.getDiagnostico());
        assertEquals("", resultadoDTO.getResultados());
    }
    
    @Test
    void testMedicalScenario() {
        // Test a realistic medical scenario
        resultadoDTO.setDocumento("LAB-2024-001");
        resultadoDTO.setDiagnostico("Hipertensión arterial controlada");
        resultadoDTO.setResultados("Presión arterial: 120/80 mmHg, ECG normal");
        resultadoDTO.setFecha(LocalDate.of(2024, 6, 15));
        resultadoDTO.setIdCita(456L);
        
        // Verify the medical scenario
        assertEquals("LAB-2024-001", resultadoDTO.getDocumento());
        assertEquals("Hipertensión arterial controlada", resultadoDTO.getDiagnostico());
        assertEquals("Presión arterial: 120/80 mmHg, ECG normal", resultadoDTO.getResultados());
        assertEquals(LocalDate.of(2024, 6, 15), resultadoDTO.getFecha());
        assertEquals(456L, resultadoDTO.getIdCita());
    }
}