package com.unis.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

/**
 * Test class for FichaTecnica entity
 */
public class FichaTecnicaTest {

    private FichaTecnica fichaTecnica;
    private LocalDate testDate;
    private PacienteFT testPaciente;

    @BeforeEach
    void setUp() {
        fichaTecnica = new FichaTecnica();
        testDate = LocalDate.of(2023, 6, 15);
        
        testPaciente = new PacienteFT();
        testPaciente.setIdPaciente(1L);
        testPaciente.setDocumento("12345678");
    }

    @Test
    void testFichaTecnicaInstantiation() {
        assertNotNull(fichaTecnica);
        assertTrue(fichaTecnica instanceof FichaTecnica);
    }

    @Test
    void testFichaTecnicaIdFicha() {
        // Test setIdFicha
        fichaTecnica.setIdFicha(100L);
        assertEquals(100L, fichaTecnica.getIdFicha());
        
        // Test setIdFicha with null
        fichaTecnica.setIdFicha(null);
        assertNull(fichaTecnica.getIdFicha());
        
        // Test setIdFicha with zero
        fichaTecnica.setIdFicha(0L);
        assertEquals(0L, fichaTecnica.getIdFicha());
        
        // Test setIdFicha with negative value
        fichaTecnica.setIdFicha(-1L);
        assertEquals(-1L, fichaTecnica.getIdFicha());
        
        // Test setIdFicha with large value
        fichaTecnica.setIdFicha(Long.MAX_VALUE);
        assertEquals(Long.MAX_VALUE, fichaTecnica.getIdFicha());
    }

    @Test
    void testFichaTecnicaIdServicio() {
        // Test setIdServicio
        fichaTecnica.setIdServicio(200L);
        assertEquals(200L, fichaTecnica.getIdServicio());
        
        // Test setIdServicio with null
        fichaTecnica.setIdServicio(null);
        assertNull(fichaTecnica.getIdServicio());
        
        // Test setIdServicio with zero
        fichaTecnica.setIdServicio(0L);
        assertEquals(0L, fichaTecnica.getIdServicio());
        
        // Test setIdServicio with negative value
        fichaTecnica.setIdServicio(-1L);
        assertEquals(-1L, fichaTecnica.getIdServicio());
        
        // Test setIdServicio with large value
        fichaTecnica.setIdServicio(Long.MAX_VALUE);
        assertEquals(Long.MAX_VALUE, fichaTecnica.getIdServicio());
    }

    @Test
    void testFichaTecnicaFechaCreacion() {
        // Test setFechaCreacion
        fichaTecnica.setFechaCreacion(testDate);
        assertEquals(testDate, fichaTecnica.getFechaCreacion());
        
        // Test setFechaCreacion with null
        fichaTecnica.setFechaCreacion(null);
        assertNull(fichaTecnica.getFechaCreacion());
        
        // Test setFechaCreacion with different dates
        LocalDate pastDate = LocalDate.of(1950, 1, 1);
        fichaTecnica.setFechaCreacion(pastDate);
        assertEquals(pastDate, fichaTecnica.getFechaCreacion());
        
        LocalDate futureDate = LocalDate.of(2025, 12, 31);
        fichaTecnica.setFechaCreacion(futureDate);
        assertEquals(futureDate, fichaTecnica.getFechaCreacion());
        
        // Test with current date
        LocalDate currentDate = LocalDate.now();
        fichaTecnica.setFechaCreacion(currentDate);
        assertEquals(currentDate, fichaTecnica.getFechaCreacion());
    }

    @Test
    void testFichaTecnicaHistorialServicios() {
        // Test setHistorialServicios
        fichaTecnica.setHistorialServicios("Consulta general, análisis de sangre");
        assertEquals("Consulta general, análisis de sangre", fichaTecnica.getHistorialServicios());
        
        // Test setHistorialServicios with null
        fichaTecnica.setHistorialServicios(null);
        assertNull(fichaTecnica.getHistorialServicios());
        
        // Test setHistorialServicios with empty string
        fichaTecnica.setHistorialServicios("");
        assertEquals("", fichaTecnica.getHistorialServicios());
        
        // Test setHistorialServicios with different content
        fichaTecnica.setHistorialServicios("Radiografía de tórax");
        assertEquals("Radiografía de tórax", fichaTecnica.getHistorialServicios());
        
        fichaTecnica.setHistorialServicios("Ecocardiograma, electrocardiograma");
        assertEquals("Ecocardiograma, electrocardiograma", fichaTecnica.getHistorialServicios());
        
        // Test setHistorialServicios with special characters
        fichaTecnica.setHistorialServicios("Consulta: Dr. García - Urgencias");
        assertEquals("Consulta: Dr. García - Urgencias", fichaTecnica.getHistorialServicios());
    }

    @Test
    void testFichaTecnicaNumeroAfiliacion() {
        // Test setNumeroAfiliacion
        fichaTecnica.setNumeroAfiliacion("AFF-123456");
        assertEquals("AFF-123456", fichaTecnica.getNumeroAfiliacion());
        
        // Test setNumeroAfiliacion with null
        fichaTecnica.setNumeroAfiliacion(null);
        assertNull(fichaTecnica.getNumeroAfiliacion());
        
        // Test setNumeroAfiliacion with empty string
        fichaTecnica.setNumeroAfiliacion("");
        assertEquals("", fichaTecnica.getNumeroAfiliacion());
        
        // Test setNumeroAfiliacion with different formats
        fichaTecnica.setNumeroAfiliacion("987654321");
        assertEquals("987654321", fichaTecnica.getNumeroAfiliacion());
        
        fichaTecnica.setNumeroAfiliacion("ABC-123-DEF");
        assertEquals("ABC-123-DEF", fichaTecnica.getNumeroAfiliacion());
        
        fichaTecnica.setNumeroAfiliacion("AFF-2023-001");
        assertEquals("AFF-2023-001", fichaTecnica.getNumeroAfiliacion());
    }

    @Test
    void testFichaTecnicaCodigoSeguro() {
        // Test setCodigoSeguro
        fichaTecnica.setCodigoSeguro("SEG-001");
        assertEquals("SEG-001", fichaTecnica.getCodigoSeguro());
        
        // Test setCodigoSeguro with null
        fichaTecnica.setCodigoSeguro(null);
        assertNull(fichaTecnica.getCodigoSeguro());
        
        // Test setCodigoSeguro with empty string
        fichaTecnica.setCodigoSeguro("");
        assertEquals("", fichaTecnica.getCodigoSeguro());
        
        // Test setCodigoSeguro with different formats
        fichaTecnica.setCodigoSeguro("12345");
        assertEquals("12345", fichaTecnica.getCodigoSeguro());
        
        fichaTecnica.setCodigoSeguro("SEG-2023-ABC");
        assertEquals("SEG-2023-ABC", fichaTecnica.getCodigoSeguro());
        
        fichaTecnica.setCodigoSeguro("PLAN-BASICO");
        assertEquals("PLAN-BASICO", fichaTecnica.getCodigoSeguro());
    }

    @Test
    void testFichaTecnicaCarnetSeguro() {
        // Test setCarnetSeguro
        fichaTecnica.setCarnetSeguro("CARN-789");
        assertEquals("CARN-789", fichaTecnica.getCarnetSeguro());
        
        // Test setCarnetSeguro with null
        fichaTecnica.setCarnetSeguro(null);
        assertNull(fichaTecnica.getCarnetSeguro());
        
        // Test setCarnetSeguro with empty string
        fichaTecnica.setCarnetSeguro("");
        assertEquals("", fichaTecnica.getCarnetSeguro());
        
        // Test setCarnetSeguro with different formats
        fichaTecnica.setCarnetSeguro("123456789");
        assertEquals("123456789", fichaTecnica.getCarnetSeguro());
        
        fichaTecnica.setCarnetSeguro("CARN-2023-001");
        assertEquals("CARN-2023-001", fichaTecnica.getCarnetSeguro());
        
        fichaTecnica.setCarnetSeguro("TARJETA-AZUL");
        assertEquals("TARJETA-AZUL", fichaTecnica.getCarnetSeguro());
    }

    @Test
    void testFichaTecnicaPaciente() {
        // Test setPaciente
        fichaTecnica.setPaciente(testPaciente);
        assertEquals(testPaciente, fichaTecnica.getPaciente());
        
        // Test setPaciente with null
        fichaTecnica.setPaciente(null);
        assertNull(fichaTecnica.getPaciente());
        
        // Test setPaciente with different patient
        PacienteFT anotherPaciente = new PacienteFT();
        anotherPaciente.setIdPaciente(999L);
        anotherPaciente.setDocumento("98765432");
        fichaTecnica.setPaciente(anotherPaciente);
        assertEquals(anotherPaciente, fichaTecnica.getPaciente());
        
        // Test setPaciente with patient having different properties
        PacienteFT complexPaciente = new PacienteFT();
        complexPaciente.setIdPaciente(777L);
        complexPaciente.setDocumento("DOC-777");
        complexPaciente.setIdUsuario(888L);
        fichaTecnica.setPaciente(complexPaciente);
        assertEquals(complexPaciente, fichaTecnica.getPaciente());
    }

    @Test
    void testFichaTecnicaCompleteObject() {
        // Test setting all fields
        fichaTecnica.setIdFicha(1000L);
        fichaTecnica.setIdServicio(500L);
        fichaTecnica.setFechaCreacion(testDate);
        fichaTecnica.setHistorialServicios("Consulta inicial, análisis completo");
        fichaTecnica.setNumeroAfiliacion("AFF-1000-500");
        fichaTecnica.setCodigoSeguro("SEG-1000");
        fichaTecnica.setCarnetSeguro("CARN-1000");
        fichaTecnica.setPaciente(testPaciente);
        
        // Verify all fields
        assertEquals(1000L, fichaTecnica.getIdFicha());
        assertEquals(500L, fichaTecnica.getIdServicio());
        assertEquals(testDate, fichaTecnica.getFechaCreacion());
        assertEquals("Consulta inicial, análisis completo", fichaTecnica.getHistorialServicios());
        assertEquals("AFF-1000-500", fichaTecnica.getNumeroAfiliacion());
        assertEquals("SEG-1000", fichaTecnica.getCodigoSeguro());
        assertEquals("CARN-1000", fichaTecnica.getCarnetSeguro());
        assertEquals(testPaciente, fichaTecnica.getPaciente());
    }

    @Test
    void testFichaTecnicaFieldModification() {
        // Set initial values
        fichaTecnica.setIdFicha(1L);
        fichaTecnica.setHistorialServicios("Original");
        fichaTecnica.setPaciente(testPaciente);
        
        // Modify values
        fichaTecnica.setIdFicha(2L);
        fichaTecnica.setHistorialServicios("Modificado");
        PacienteFT newPaciente = new PacienteFT();
        newPaciente.setIdPaciente(999L);
        fichaTecnica.setPaciente(newPaciente);
        
        // Verify modifications
        assertEquals(2L, fichaTecnica.getIdFicha());
        assertEquals("Modificado", fichaTecnica.getHistorialServicios());
        assertEquals(newPaciente, fichaTecnica.getPaciente());
    }

    @Test
    void testFichaTecnicaEdgeCases() {
        // Test with very long strings
        String veryLongString = "A".repeat(1000);
        fichaTecnica.setHistorialServicios(veryLongString);
        fichaTecnica.setNumeroAfiliacion(veryLongString);
        fichaTecnica.setCodigoSeguro(veryLongString);
        fichaTecnica.setCarnetSeguro(veryLongString);
        
        assertEquals(veryLongString, fichaTecnica.getHistorialServicios());
        assertEquals(veryLongString, fichaTecnica.getNumeroAfiliacion());
        assertEquals(veryLongString, fichaTecnica.getCodigoSeguro());
        assertEquals(veryLongString, fichaTecnica.getCarnetSeguro());
        
        // Test with special characters
        String specialChars = "!@#$%^&*()_+-=[]{}|;':\",./<>?";
        fichaTecnica.setHistorialServicios(specialChars);
        fichaTecnica.setNumeroAfiliacion(specialChars);
        fichaTecnica.setCodigoSeguro(specialChars);
        fichaTecnica.setCarnetSeguro(specialChars);
        
        assertEquals(specialChars, fichaTecnica.getHistorialServicios());
        assertEquals(specialChars, fichaTecnica.getNumeroAfiliacion());
        assertEquals(specialChars, fichaTecnica.getCodigoSeguro());
        assertEquals(specialChars, fichaTecnica.getCarnetSeguro());
        
        // Test with numbers in string fields
        String numbersOnly = "123456789";
        fichaTecnica.setHistorialServicios(numbersOnly);
        fichaTecnica.setNumeroAfiliacion(numbersOnly);
        fichaTecnica.setCodigoSeguro(numbersOnly);
        fichaTecnica.setCarnetSeguro(numbersOnly);
        
        assertEquals(numbersOnly, fichaTecnica.getHistorialServicios());
        assertEquals(numbersOnly, fichaTecnica.getNumeroAfiliacion());
        assertEquals(numbersOnly, fichaTecnica.getCodigoSeguro());
        assertEquals(numbersOnly, fichaTecnica.getCarnetSeguro());
    }

    @Test
    void testFichaTecnicaNullHandling() {
        // Test that all fields can handle null values properly
        fichaTecnica.setIdFicha(null);
        fichaTecnica.setIdServicio(null);
        fichaTecnica.setFechaCreacion(null);
        fichaTecnica.setHistorialServicios(null);
        fichaTecnica.setNumeroAfiliacion(null);
        fichaTecnica.setCodigoSeguro(null);
        fichaTecnica.setCarnetSeguro(null);
        fichaTecnica.setPaciente(null);
        
        // Verify all fields are null
        assertNull(fichaTecnica.getIdFicha());
        assertNull(fichaTecnica.getIdServicio());
        assertNull(fichaTecnica.getFechaCreacion());
        assertNull(fichaTecnica.getHistorialServicios());
        assertNull(fichaTecnica.getNumeroAfiliacion());
        assertNull(fichaTecnica.getCodigoSeguro());
        assertNull(fichaTecnica.getCarnetSeguro());
        assertNull(fichaTecnica.getPaciente());
    }
}

