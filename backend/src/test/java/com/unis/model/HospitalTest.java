package com.unis.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

/**
 * Test class for Hospital entity
 */
public class HospitalTest {

    private Hospital hospital;
    private LocalDateTime testDateTime;

    @BeforeEach
    void setUp() {
        hospital = new Hospital();
        testDateTime = LocalDateTime.now();
    }

    @Test
    void testHospitalInstantiation() {
        assertNotNull(hospital);
        assertTrue(hospital instanceof Hospital);
    }

    @Test
    void testHospitalId() {
        // Test setId
        hospital.setId(1L);
        assertEquals(1L, hospital.getId());
        
        // Test setId with null
        hospital.setId(null);
        assertNull(hospital.getId());
        
        // Test setId with zero
        hospital.setId(0L);
        assertEquals(0L, hospital.getId());
        
        // Test setId with negative value
        hospital.setId(-1L);
        assertEquals(-1L, hospital.getId());
    }

    @Test
    void testHospitalNombre() {
        // Test setNombre
        hospital.setNombre("Hospital General");
        assertEquals("Hospital General", hospital.getNombre());
        
        // Test setNombre with null
        hospital.setNombre(null);
        assertNull(hospital.getNombre());
        
        // Test setNombre with empty string
        hospital.setNombre("");
        assertEquals("", hospital.getNombre());
        
        // Test setNombre with special characters
        hospital.setNombre("Hospital San José & María");
        assertEquals("Hospital San José & María", hospital.getNombre());
    }

    @Test
    void testHospitalDireccion() {
        // Test setDireccion
        hospital.setDireccion("Calle Principal 123, Zona 1");
        assertEquals("Calle Principal 123, Zona 1", hospital.getDireccion());
        
        // Test setDireccion with null
        hospital.setDireccion(null);
        assertNull(hospital.getDireccion());
        
        // Test setDireccion with empty string
        hospital.setDireccion("");
        assertEquals("", hospital.getDireccion());
        
        // Test setDireccion with long address
        String longAddress = "Avenida Reforma 10-00, Zona 10, Ciudad de Guatemala, Guatemala, Centro América";
        hospital.setDireccion(longAddress);
        assertEquals(longAddress, hospital.getDireccion());
    }

    @Test
    void testHospitalTelefono() {
        // Test setTelefono
        hospital.setTelefono("502-1234-5678");
        assertEquals("502-1234-5678", hospital.getTelefono());
        
        // Test setTelefono with null
        hospital.setTelefono(null);
        assertNull(hospital.getTelefono());
        
        // Test setTelefono with empty string
        hospital.setTelefono("");
        assertEquals("", hospital.getTelefono());
        
        // Test setTelefono with different formats
        hospital.setTelefono("+502 1234 5678");
        assertEquals("+502 1234 5678", hospital.getTelefono());
        
        hospital.setTelefono("12345678");
        assertEquals("12345678", hospital.getTelefono());
    }

    @Test
    void testHospitalCorreo() {
        // Test setCorreo
        hospital.setCorreo("info@hospital.com");
        assertEquals("info@hospital.com", hospital.getCorreo());
        
        // Test setCorreo with null
        hospital.setCorreo(null);
        assertNull(hospital.getCorreo());
        
        // Test setCorreo with empty string
        hospital.setCorreo("");
        assertEquals("", hospital.getCorreo());
        
        // Test setCorreo with different email formats
        hospital.setCorreo("admin@hospital.gt");
        assertEquals("admin@hospital.gt", hospital.getCorreo());
        
        hospital.setCorreo("contacto+test@hospital.org");
        assertEquals("contacto+test@hospital.org", hospital.getCorreo());
    }

    @Test
    void testHospitalEstado() {
        // Test setEstado
        hospital.setEstado("A");
        assertEquals("A", hospital.getEstado());
        
        // Test setEstado with null
        hospital.setEstado(null);
        assertNull(hospital.getEstado());
        
        // Test setEstado with empty string
        hospital.setEstado("");
        assertEquals("", hospital.getEstado());
        
        // Test setEstado with different values
        hospital.setEstado("I");
        assertEquals("I", hospital.getEstado());
        
        hospital.setEstado("P");
        assertEquals("P", hospital.getEstado());
    }

    @Test
    void testHospitalFechaCreacion() {
        // Test setFechaCreacion
        hospital.setFechaCreacion(testDateTime);
        assertEquals(testDateTime, hospital.getFechaCreacion());
        
        // Test setFechaCreacion with null
        hospital.setFechaCreacion(null);
        assertNull(hospital.getFechaCreacion());
        
        // Test setFechaCreacion with different dates
        LocalDateTime pastDate = LocalDateTime.of(2020, 1, 1, 12, 0);
        hospital.setFechaCreacion(pastDate);
        assertEquals(pastDate, hospital.getFechaCreacion());
        
        LocalDateTime futureDate = LocalDateTime.of(2030, 12, 31, 23, 59);
        hospital.setFechaCreacion(futureDate);
        assertEquals(futureDate, hospital.getFechaCreacion());
    }

    @Test
    void testHospitalMongoId() {
        // Test setMongoId
        hospital.setMongoId("507f1f77bcf86cd799439011");
        assertEquals("507f1f77bcf86cd799439011", hospital.getMongoId());
        
        // Test setMongoId with null
        hospital.setMongoId(null);
        assertNull(hospital.getMongoId());
        
        // Test setMongoId with empty string
        hospital.setMongoId("");
        assertEquals("", hospital.getMongoId());
        
        // Test setMongoId with different formats
        hospital.setMongoId("hospital_123");
        assertEquals("hospital_123", hospital.getMongoId());
        
        hospital.setMongoId("HOSP-2024-001");
        assertEquals("HOSP-2024-001", hospital.getMongoId());
    }

    @Test
    void testHospitalCompleteObject() {
        // Test setting all fields
        hospital.setId(100L);
        hospital.setNombre("Hospital Completo");
        hospital.setDireccion("Dirección Completa 456");
        hospital.setTelefono("502-9876-5432");
        hospital.setCorreo("completo@hospital.com");
        hospital.setEstado("A");
        hospital.setFechaCreacion(testDateTime);
        hospital.setMongoId("complete_hospital_123");
        
        // Verify all fields
        assertEquals(100L, hospital.getId());
        assertEquals("Hospital Completo", hospital.getNombre());
        assertEquals("Dirección Completa 456", hospital.getDireccion());
        assertEquals("502-9876-5432", hospital.getTelefono());
        assertEquals("completo@hospital.com", hospital.getCorreo());
        assertEquals("A", hospital.getEstado());
        assertEquals(testDateTime, hospital.getFechaCreacion());
        assertEquals("complete_hospital_123", hospital.getMongoId());
    }

    @Test
    void testHospitalFieldModification() {
        // Set initial values
        hospital.setId(1L);
        hospital.setNombre("Hospital Original");
        hospital.setEstado("A");
        
        // Modify values
        hospital.setId(2L);
        hospital.setNombre("Hospital Modificado");
        hospital.setEstado("I");
        
        // Verify modifications
        assertEquals(2L, hospital.getId());
        assertEquals("Hospital Modificado", hospital.getNombre());
        assertEquals("I", hospital.getEstado());
    }

    @Test
    void testHospitalEdgeCases() {
        // Test with very long strings
        String veryLongName = "A".repeat(1000);
        hospital.setNombre(veryLongName);
        assertEquals(veryLongName, hospital.getNombre());
        
        // Test with special characters
        hospital.setNombre("Hôspital Español & Français");
        assertEquals("Hôspital Español & Français", hospital.getNombre());
        
        // Test with numbers in strings
        hospital.setNombre("Hospital 123");
        assertEquals("Hospital 123", hospital.getNombre());
    }
}

