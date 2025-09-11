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
<<<<<<< HEAD
    }

    @Test
    void testCamposPublicos() {
        // Arrange
        String dpi = "1234567890101";
        String nombre = "Juan";
        String apellido = "Pérez";
        LocalDate fecha = LocalDate.now();
        String horaInicio = "09:00";
        String horaFin = "10:00";
        String motivo = "Consulta de rutina";
        Long idHospital = 1L;
        Long idServicio = 1L;
        Long idAseguradora = 1L;
        String numeroAutorizacion = "AUTH001";

        // Act
        citaDTO.setDpi(dpi);
        citaDTO.setNombre(nombre);
        citaDTO.setApellido(apellido);
        citaDTO.setFecha(fecha);
        citaDTO.setHoraInicio(horaInicio);
        citaDTO.setHoraFin(horaFin);
        citaDTO.setMotivo(motivo);
        citaDTO.setIdHospital(idHospital);
        citaDTO.setIdServicio(idServicio);
        citaDTO.setIdAseguradora(idAseguradora);
        citaDTO.setNumeroAutorizacion(numeroAutorizacion);

        // Assert
        assertEquals(dpi, citaDTO.getDpi());
        assertEquals(nombre, citaDTO.getNombre());
        assertEquals(apellido, citaDTO.getApellido());
        assertEquals(fecha, citaDTO.getFecha());
        assertEquals(horaInicio, citaDTO.getHoraInicio());
        assertEquals(horaFin, citaDTO.getHoraFin());
        assertEquals(motivo, citaDTO.getMotivo());
        assertEquals(idHospital, citaDTO.getIdHospital());
        assertEquals(idServicio, citaDTO.getIdServicio());
        assertEquals(idAseguradora, citaDTO.getIdAseguradora());
        assertEquals(numeroAutorizacion, citaDTO.getNumeroAutorizacion());
    }

    @Test
    void testCamposConValoresNulos() {
        // Act
        citaDTO.setDpi(null);
        citaDTO.setNombre(null);
        citaDTO.setApellido(null);
        citaDTO.setFecha(null);
        citaDTO.setHoraInicio(null);
        citaDTO.setHoraFin(null);
        citaDTO.setMotivo(null);
        citaDTO.setIdHospital(null);
        citaDTO.setIdServicio(null);
        citaDTO.setIdAseguradora(null);
        citaDTO.setNumeroAutorizacion(null);

        // Assert
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
=======
        assertNull(citaDTO.dpi);
        assertNull(citaDTO.nombre);
        assertNull(citaDTO.apellido);
        assertNull(citaDTO.fecha);
        assertNull(citaDTO.horaInicio);
        assertNull(citaDTO.horaFin);
        assertNull(citaDTO.motivo);
        assertNull(citaDTO.idHospital);
        assertNull(citaDTO.idServicio);
        assertNull(citaDTO.idAseguradora);
        assertNull(citaDTO.numeroAutorizacion);
>>>>>>> 648dadd5258233ca3ac4f2574bd67d8933f60087
    }
    
    @Test
<<<<<<< HEAD
    void testCamposConValoresVacios() {
        // Act
        citaDTO.setDpi("");
        citaDTO.setNombre("");
        citaDTO.setApellido("");
        citaDTO.setMotivo("");
        citaDTO.setNumeroAutorizacion("");

        // Assert
        assertEquals("", citaDTO.getDpi());
        assertEquals("", citaDTO.getNombre());
        assertEquals("", citaDTO.getApellido());
        assertEquals("", citaDTO.getMotivo());
        assertEquals("", citaDTO.getNumeroAutorizacion());
    }

    @Test
    void testCamposConValoresEspeciales() {
        // Act
        citaDTO.setDpi("1234567890101");
        citaDTO.setNombre("María José");
        citaDTO.setApellido("García-López");
        citaDTO.setMotivo("Consulta de emergencia");
        citaDTO.setNumeroAutorizacion("AUTH-2024-001");

        // Assert
        assertEquals("1234567890101", citaDTO.getDpi());
        assertEquals("María José", citaDTO.getNombre());
        assertEquals("García-López", citaDTO.getApellido());
        assertEquals("Consulta de emergencia", citaDTO.getMotivo());
        assertEquals("AUTH-2024-001", citaDTO.getNumeroAutorizacion());
=======
    void testSetAndGetDpi() {
        citaDTO.dpi = "1234567890101";
        assertEquals("1234567890101", citaDTO.dpi);
        
        citaDTO.dpi = null;
        assertNull(citaDTO.dpi);
        
        citaDTO.dpi = "";
        assertEquals("", citaDTO.dpi);
        
        citaDTO.dpi = "DPI-123";
        assertEquals("DPI-123", citaDTO.dpi);
    }
    
    @Test
    void testSetAndGetNombre() {
        citaDTO.nombre = "Juan";
        assertEquals("Juan", citaDTO.nombre);
        
        citaDTO.nombre = null;
        assertNull(citaDTO.nombre);
        
        citaDTO.nombre = "";
        assertEquals("", citaDTO.nombre);
        
        citaDTO.nombre = "María José";
        assertEquals("María José", citaDTO.nombre);
    }
    
    @Test
    void testSetAndGetApellido() {
        citaDTO.apellido = "Pérez";
        assertEquals("Pérez", citaDTO.apellido);
        
        citaDTO.apellido = null;
        assertNull(citaDTO.apellido);
        
        citaDTO.apellido = "";
        assertEquals("", citaDTO.apellido);
        
        citaDTO.apellido = "García López";
        assertEquals("García López", citaDTO.apellido);
    }
    
    @Test
    void testSetAndGetFecha() {
        citaDTO.fecha = testDate;
        assertEquals(testDate, citaDTO.fecha);
        
        citaDTO.fecha = null;
        assertNull(citaDTO.fecha);
        
        LocalDate pastDate = LocalDate.of(2020, 1, 1);
        citaDTO.fecha = pastDate;
        assertEquals(pastDate, citaDTO.fecha);
        
        LocalDate futureDate = LocalDate.of(2030, 12, 31);
        citaDTO.fecha = futureDate;
        assertEquals(futureDate, citaDTO.fecha);
    }
    
    @Test
    void testSetAndGetHoraInicio() {
        citaDTO.horaInicio = "09:00";
        assertEquals("09:00", citaDTO.horaInicio);
        
        citaDTO.horaInicio = null;
        assertNull(citaDTO.horaInicio);
        
        citaDTO.horaInicio = "";
        assertEquals("", citaDTO.horaInicio);
        
        citaDTO.horaInicio = "14:30";
        assertEquals("14:30", citaDTO.horaInicio);
    }
    
    @Test
    void testSetAndGetHoraFin() {
        citaDTO.horaFin = "10:00";
        assertEquals("10:00", citaDTO.horaFin);
        
        citaDTO.horaFin = null;
        assertNull(citaDTO.horaFin);
        
        citaDTO.horaFin = "";
        assertEquals("", citaDTO.horaFin);
        
        citaDTO.horaFin = "15:30";
        assertEquals("15:30", citaDTO.horaFin);
    }
    
    @Test
    void testSetAndGetMotivo() {
        citaDTO.motivo = "Consulta general";
        assertEquals("Consulta general", citaDTO.motivo);
        
        citaDTO.motivo = null;
        assertNull(citaDTO.motivo);
        
        citaDTO.motivo = "";
        assertEquals("", citaDTO.motivo);
        
        citaDTO.motivo = "Revisión de resultados";
        assertEquals("Revisión de resultados", citaDTO.motivo);
    }
    
    @Test
    void testSetAndGetIdHospital() {
        citaDTO.idHospital = 1L;
        assertEquals(1L, citaDTO.idHospital);
        
        citaDTO.idHospital = null;
        assertNull(citaDTO.idHospital);
        
        citaDTO.idHospital = 0L;
        assertEquals(0L, citaDTO.idHospital);
        
        citaDTO.idHospital = 999L;
        assertEquals(999L, citaDTO.idHospital);
    }
    
    @Test
    void testSetAndGetIdServicio() {
        citaDTO.idServicio = 1L;
        assertEquals(1L, citaDTO.idServicio);
        
        citaDTO.idServicio = null;
        assertNull(citaDTO.idServicio);
        
        citaDTO.idServicio = 0L;
        assertEquals(0L, citaDTO.idServicio);
        
        citaDTO.idServicio = 100L;
        assertEquals(100L, citaDTO.idServicio);
    }
    
    @Test
    void testSetAndGetIdAseguradora() {
        citaDTO.idAseguradora = 1L;
        assertEquals(1L, citaDTO.idAseguradora);
        
        citaDTO.idAseguradora = null;
        assertNull(citaDTO.idAseguradora);
        
        citaDTO.idAseguradora = 0L;
        assertEquals(0L, citaDTO.idAseguradora);
        
        citaDTO.idAseguradora = 50L;
        assertEquals(50L, citaDTO.idAseguradora);
    }
    
    @Test
    void testSetAndGetNumeroAutorizacion() {
        citaDTO.numeroAutorizacion = "AUTH-12345";
        assertEquals("AUTH-12345", citaDTO.numeroAutorizacion);
        
        citaDTO.numeroAutorizacion = null;
        assertNull(citaDTO.numeroAutorizacion);
        
        citaDTO.numeroAutorizacion = "";
        assertEquals("", citaDTO.numeroAutorizacion);
        
        citaDTO.numeroAutorizacion = "AUT-2024-001";
        assertEquals("AUT-2024-001", citaDTO.numeroAutorizacion);
    }
    
    @Test
    void testCompleteObject() {
        // Set all fields
        citaDTO.dpi = "1234567890101";
        citaDTO.nombre = "Juan Carlos";
        citaDTO.apellido = "Pérez García";
        citaDTO.fecha = testDate;
        citaDTO.horaInicio = "09:00";
        citaDTO.horaFin = "10:00";
        citaDTO.motivo = "Consulta de rutina";
        citaDTO.idHospital = 1L;
        citaDTO.idServicio = 5L;
        citaDTO.idAseguradora = 2L;
        citaDTO.numeroAutorizacion = "AUTH-2024-001";
        
        // Verify all fields
        assertEquals("1234567890101", citaDTO.dpi);
        assertEquals("Juan Carlos", citaDTO.nombre);
        assertEquals("Pérez García", citaDTO.apellido);
        assertEquals(testDate, citaDTO.fecha);
        assertEquals("09:00", citaDTO.horaInicio);
        assertEquals("10:00", citaDTO.horaFin);
        assertEquals("Consulta de rutina", citaDTO.motivo);
        assertEquals(1L, citaDTO.idHospital);
        assertEquals(5L, citaDTO.idServicio);
        assertEquals(2L, citaDTO.idAseguradora);
        assertEquals("AUTH-2024-001", citaDTO.numeroAutorizacion);
>>>>>>> 648dadd5258233ca3ac4f2574bd67d8933f60087
    }
    
    @Test
    void testFieldModification() {
        // Set initial values
        citaDTO.dpi = "1111111111111";
        citaDTO.nombre = "María";
        citaDTO.idHospital = 1L;
        
        // Modify values
        citaDTO.dpi = "2222222222222";
        citaDTO.nombre = "Ana";
        citaDTO.idHospital = 2L;
        
        // Verify modifications
        assertEquals("2222222222222", citaDTO.dpi);
        assertEquals("Ana", citaDTO.nombre);
        assertEquals(2L, citaDTO.idHospital);
    }
    
    @Test
    void testEdgeCases() {
        // Test with very long strings
        String longString = "A".repeat(1000);
        citaDTO.motivo = longString;
        assertEquals(longString, citaDTO.motivo);
        
        // Test with special characters
        citaDTO.nombre = "José María & Ana";
        assertEquals("José María & Ana", citaDTO.nombre);
        
        // Test with numbers in strings
        citaDTO.apellido = "García123";
        assertEquals("García123", citaDTO.apellido);
        
        // Test with extreme date values
        LocalDate minDate = LocalDate.MIN;
        LocalDate maxDate = LocalDate.MAX;
        citaDTO.fecha = minDate;
        assertEquals(minDate, citaDTO.fecha);
        citaDTO.fecha = maxDate;
        assertEquals(maxDate, citaDTO.fecha);
    }
}
