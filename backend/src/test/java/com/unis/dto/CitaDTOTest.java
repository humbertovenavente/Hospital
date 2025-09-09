package com.unis.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;

public class CitaDTOTest {
    
    private CitaDTO citaDTO;
    private LocalDate testDate;
    
    @BeforeEach
    void setUp() {
        citaDTO = new CitaDTO();
        testDate = LocalDate.of(2024, 6, 15);
    }
    
    @Test
    void testDefaultConstructor() {
        assertNotNull(citaDTO);
        assertNull(citaDTO.getDpi());
        assertNull(citaDTO.getNombre());
        assertNull(citaDTO.getApellido());
        assertNull(citaDTO.getFecha());
        assertNull(citaDTO.getHoraInicio());
        assertNull(citaDTO.getHoraFin());
        assertNull(citaDTO.getMotivo());
        assertNull(citaDTO.getIdHospital());
        assertNull(citaDTO.getIdServicio());
        assertNull(citaDTO.getIdAseguradora());
        assertNull(citaDTO.getNumeroAutorizacion());
    }
    
    @Test
    void testSetAndGetDpi() {
        citaDTO.setDpi("1234567890101");
        assertEquals("1234567890101", citaDTO.getDpi());
        
        citaDTO.setDpi(null);
        assertNull(citaDTO.getDpi());
        
        citaDTO.setDpi("");
        assertEquals("", citaDTO.getDpi());
        
        citaDTO.setDpi("DPI-123");
        assertEquals("DPI-123", citaDTO.getDpi());
    }
    
    @Test
    void testSetAndGetNombre() {
        citaDTO.setNombre("Juan");
        assertEquals("Juan", citaDTO.getNombre());
        
        citaDTO.setNombre(null);
        assertNull(citaDTO.getNombre());
        
        citaDTO.setNombre("");
        assertEquals("", citaDTO.getNombre());
        
        citaDTO.setNombre("María José");
        assertEquals("María José", citaDTO.getNombre());
    }
    
    @Test
    void testSetAndGetApellido() {
        citaDTO.setApellido("Pérez");
        assertEquals("Pérez", citaDTO.getApellido());
        
        citaDTO.setApellido(null);
        assertNull(citaDTO.getApellido());
        
        citaDTO.setApellido("");
        assertEquals("", citaDTO.getApellido());
        
        citaDTO.setApellido("García López");
        assertEquals("García López", citaDTO.getApellido());
    }
    
    @Test
    void testSetAndGetFecha() {
        citaDTO.setFecha(testDate);
        assertEquals(testDate, citaDTO.getFecha());
        
        citaDTO.setFecha(null);
        assertNull(citaDTO.getFecha());
        
        LocalDate pastDate = LocalDate.of(2020, 1, 1);
        citaDTO.setFecha(pastDate);
        assertEquals(pastDate, citaDTO.getFecha());
        
        LocalDate futureDate = LocalDate.of(2030, 12, 31);
        citaDTO.setFecha(futureDate);
        assertEquals(futureDate, citaDTO.getFecha());
    }
    
    @Test
    void testSetAndGetHoraInicio() {
        citaDTO.setHoraInicio("09:00");
        assertEquals("09:00", citaDTO.getHoraInicio());
        
        citaDTO.setHoraInicio(null);
        assertNull(citaDTO.getHoraInicio());
        
        citaDTO.setHoraInicio("");
        assertEquals("", citaDTO.getHoraInicio());
        
        citaDTO.setHoraInicio("14:30");
        assertEquals("14:30", citaDTO.getHoraInicio());
    }
    
    @Test
    void testSetAndGetHoraFin() {
        citaDTO.setHoraFin("10:00");
        assertEquals("10:00", citaDTO.getHoraFin());
        
        citaDTO.setHoraFin(null);
        assertNull(citaDTO.getHoraFin());
        
        citaDTO.setHoraFin("");
        assertEquals("", citaDTO.getHoraFin());
        
        citaDTO.setHoraFin("15:30");
        assertEquals("15:30", citaDTO.getHoraFin());
    }
    
    @Test
    void testSetAndGetMotivo() {
        citaDTO.setMotivo("Consulta general");
        assertEquals("Consulta general", citaDTO.getMotivo());
        
        citaDTO.setMotivo(null);
        assertNull(citaDTO.getMotivo());
        
        citaDTO.setMotivo("");
        assertEquals("", citaDTO.getMotivo());
        
        citaDTO.setMotivo("Revisión de resultados");
        assertEquals("Revisión de resultados", citaDTO.getMotivo());
    }
    
    @Test
    void testSetAndGetIdHospital() {
        citaDTO.setIdHospital(1L);
        assertEquals(1L, citaDTO.getIdHospital());
        
        citaDTO.setIdHospital(null);
        assertNull(citaDTO.getIdHospital());
        
        citaDTO.setIdHospital(0L);
        assertEquals(0L, citaDTO.getIdHospital());
        
        citaDTO.setIdHospital(999L);
        assertEquals(999L, citaDTO.getIdHospital());
    }
    
    @Test
    void testSetAndGetIdServicio() {
        citaDTO.setIdServicio(1L);
        assertEquals(1L, citaDTO.getIdServicio());
        
        citaDTO.setIdServicio(null);
        assertNull(citaDTO.getIdServicio());
        
        citaDTO.setIdServicio(0L);
        assertEquals(0L, citaDTO.getIdServicio());
        
        citaDTO.setIdServicio(100L);
        assertEquals(100L, citaDTO.getIdServicio());
    }
    
    @Test
    void testSetAndGetIdAseguradora() {
        citaDTO.setIdAseguradora(1L);
        assertEquals(1L, citaDTO.getIdAseguradora());
        
        citaDTO.setIdAseguradora(null);
        assertNull(citaDTO.getIdAseguradora());
        
        citaDTO.setIdAseguradora(0L);
        assertEquals(0L, citaDTO.getIdAseguradora());
        
        citaDTO.setIdAseguradora(50L);
        assertEquals(50L, citaDTO.getIdAseguradora());
    }
    
    @Test
    void testSetAndGetNumeroAutorizacion() {
        citaDTO.setNumeroAutorizacion("AUTH-12345");
        assertEquals("AUTH-12345", citaDTO.getNumeroAutorizacion());
        
        citaDTO.setNumeroAutorizacion(null);
        assertNull(citaDTO.getNumeroAutorizacion());
        
        citaDTO.setNumeroAutorizacion("");
        assertEquals("", citaDTO.getNumeroAutorizacion());
        
        citaDTO.setNumeroAutorizacion("AUT-2024-001");
        assertEquals("AUT-2024-001", citaDTO.getNumeroAutorizacion());
    }
    
    @Test
    void testCompleteObject() {
        // Set all fields
        citaDTO.setDpi("1234567890101");
        citaDTO.setNombre("Juan Carlos");
        citaDTO.setApellido("Pérez García");
        citaDTO.setFecha(testDate);
        citaDTO.setHoraInicio("09:00");
        citaDTO.setHoraFin("10:00");
        citaDTO.setMotivo("Consulta de rutina");
        citaDTO.setIdHospital(1L);
        citaDTO.setIdServicio(5L);
        citaDTO.setIdAseguradora(2L);
        citaDTO.setNumeroAutorizacion("AUTH-2024-001");
        
        // Verify all fields
        assertEquals("1234567890101", citaDTO.getDpi());
        assertEquals("Juan Carlos", citaDTO.getNombre());
        assertEquals("Pérez García", citaDTO.getApellido());
        assertEquals(testDate, citaDTO.getFecha());
        assertEquals("09:00", citaDTO.getHoraInicio());
        assertEquals("10:00", citaDTO.getHoraFin());
        assertEquals("Consulta de rutina", citaDTO.getMotivo());
        assertEquals(1L, citaDTO.getIdHospital());
        assertEquals(5L, citaDTO.getIdServicio());
        assertEquals(2L, citaDTO.getIdAseguradora());
        assertEquals("AUTH-2024-001", citaDTO.getNumeroAutorizacion());
    }
    
    @Test
    void testFieldModification() {
        // Set initial values
        citaDTO.setDpi("1111111111111");
        citaDTO.setNombre("María");
        citaDTO.setIdHospital(1L);
        
        // Modify values
        citaDTO.setDpi("2222222222222");
        citaDTO.setNombre("Ana");
        citaDTO.setIdHospital(2L);
        
        // Verify modifications
        assertEquals("2222222222222", citaDTO.getDpi());
        assertEquals("Ana", citaDTO.getNombre());
        assertEquals(2L, citaDTO.getIdHospital());
    }
    
    @Test
    void testEdgeCases() {
        // Test with very long strings
        String longString = "A".repeat(1000);
        citaDTO.setMotivo(longString);
        assertEquals(longString, citaDTO.getMotivo());
        
        // Test with special characters
        citaDTO.setNombre("José María & Ana");
        assertEquals("José María & Ana", citaDTO.getNombre());
        
        // Test with numbers in strings
        citaDTO.setApellido("García123");
        assertEquals("García123", citaDTO.getApellido());
        
        // Test with extreme date values
        LocalDate minDate = LocalDate.MIN;
        LocalDate maxDate = LocalDate.MAX;
        citaDTO.setFecha(minDate);
        assertEquals(minDate, citaDTO.getFecha());
        citaDTO.setFecha(maxDate);
        assertEquals(maxDate, citaDTO.getFecha());
    }
}