package com.unis.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

/**
 * Test class for PacienteFT entity
 */
public class PacienteFTTest {

    private PacienteFT paciente;
    private LocalDate testDate;
    private Usuario testUsuario;
    private FichaTecnica testFichaTecnica;

    @BeforeEach
    void setUp() {
        paciente = new PacienteFT();
        testDate = LocalDate.of(1990, 5, 15);
        
        testUsuario = new Usuario();
        testUsuario.setNombreUsuario("Juan Pérez");
        
        testFichaTecnica = new FichaTecnica();
        testFichaTecnica.setIdFicha(1L);
    }

    @Test
    void testPacienteFTInstantiation() {
        assertNotNull(paciente);
        assertTrue(paciente instanceof PacienteFT);
    }

    @Test
    void testPacienteFTIdPaciente() {
        // Test setIdPaciente
        paciente.setIdPaciente(100L);
        assertEquals(100L, paciente.getIdPaciente());
        
        // Test setIdPaciente with null
        paciente.setIdPaciente(null);
        assertNull(paciente.getIdPaciente());
        
        // Test setIdPaciente with zero
        paciente.setIdPaciente(0L);
        assertEquals(0L, paciente.getIdPaciente());
        
        // Test setIdPaciente with negative value
        paciente.setIdPaciente(-1L);
        assertEquals(-1L, paciente.getIdPaciente());
    }

    @Test
    void testPacienteFTIdUsuario() {
        // Test setIdUsuario
        paciente.setIdUsuario(200L);
        assertEquals(200L, paciente.getIdUsuario());
        
        // Test setIdUsuario with null
        paciente.setIdUsuario(null);
        assertNull(paciente.getIdUsuario());
        
        // Test setIdUsuario with zero
        paciente.setIdUsuario(0L);
        assertEquals(0L, paciente.getIdUsuario());
        
        // Test setIdUsuario with negative value
        paciente.setIdUsuario(-1L);
        assertEquals(-1L, paciente.getIdUsuario());
    }

    @Test
    void testPacienteFTDocumento() {
        // Test setDocumento
        paciente.setDocumento("12345678");
        assertEquals("12345678", paciente.getDocumento());
        
        // Test setDocumento with null
        paciente.setDocumento(null);
        assertNull(paciente.getDocumento());
        
        // Test setDocumento with empty string
        paciente.setDocumento("");
        assertEquals("", paciente.getDocumento());
        
        // Test setDocumento with different formats
        paciente.setDocumento("ABC-123456");
        assertEquals("ABC-123456", paciente.getDocumento());
        
        paciente.setDocumento("98765432-1");
        assertEquals("98765432-1", paciente.getDocumento());
    }

    @Test
    void testPacienteFTFechaNacimiento() {
        // Test setFechaNacimiento
        paciente.setFechaNacimiento(testDate);
        assertEquals(testDate, paciente.getFechaNacimiento());
        
        // Test setFechaNacimiento with null
        paciente.setFechaNacimiento(null);
        assertNull(paciente.getFechaNacimiento());
        
        // Test setFechaNacimiento with different dates
        LocalDate pastDate = LocalDate.of(1950, 1, 1);
        paciente.setFechaNacimiento(pastDate);
        assertEquals(pastDate, paciente.getFechaNacimiento());
        
        LocalDate futureDate = LocalDate.of(2025, 12, 31);
        paciente.setFechaNacimiento(futureDate);
        assertEquals(futureDate, paciente.getFechaNacimiento());
        
        // Test with current date
        LocalDate currentDate = LocalDate.now();
        paciente.setFechaNacimiento(currentDate);
        assertEquals(currentDate, paciente.getFechaNacimiento());
    }

    @Test
    void testPacienteFTFotografia() {
        // Test setFotografia
        byte[] testPhoto = {1, 2, 3, 4, 5};
        paciente.setFotografia(testPhoto);
        assertArrayEquals(testPhoto, paciente.getFotografia());
        
        // Test setFotografia with null
        paciente.setFotografia(null);
        assertNull(paciente.getFotografia());
        
        // Test setFotografia with empty array
        byte[] emptyPhoto = {};
        paciente.setFotografia(emptyPhoto);
        assertArrayEquals(emptyPhoto, paciente.getFotografia());
        
        // Test setFotografia with large array
        byte[] largePhoto = new byte[1000];
        for (int i = 0; i < largePhoto.length; i++) {
            largePhoto[i] = (byte) (i % 256);
        }
        paciente.setFotografia(largePhoto);
        assertArrayEquals(largePhoto, paciente.getFotografia());
    }

    @Test
    void testPacienteFTUsuario() {
        // Test setUsuario
        paciente.setUsuario(testUsuario);
        assertEquals(testUsuario, paciente.getUsuario());
        
        // Test setUsuario with null
        paciente.setUsuario(null);
        assertNull(paciente.getUsuario());
        
        // Test setUsuario with different user
        Usuario anotherUsuario = new Usuario();
        anotherUsuario.setId(999L);
        anotherUsuario.setNombreUsuario("María García");
        paciente.setUsuario(anotherUsuario);
        assertEquals(anotherUsuario, paciente.getUsuario());
    }

    @Test
    void testPacienteFTFichasTecnicas() {
        // Test setFichasTecnicas
        List<FichaTecnica> testFichas = Arrays.asList(testFichaTecnica);
        paciente.setFichasTecnicas(testFichas);
        assertEquals(testFichas, paciente.getFichasTecnicas());
        assertEquals(1, paciente.getFichasTecnicas().size());
        
        // Test setFichasTecnicas with null
        paciente.setFichasTecnicas(null);
        assertNull(paciente.getFichasTecnicas());
        
        // Test setFichasTecnicas with empty list
        List<FichaTecnica> emptyFichas = new ArrayList<>();
        paciente.setFichasTecnicas(emptyFichas);
        assertEquals(emptyFichas, paciente.getFichasTecnicas());
        assertEquals(0, paciente.getFichasTecnicas().size());
        
        // Test setFichasTecnicas with multiple items
        FichaTecnica ficha2 = new FichaTecnica();
        ficha2.setIdFicha(2L);
        List<FichaTecnica> multipleFichas = Arrays.asList(testFichaTecnica, ficha2);
        paciente.setFichasTecnicas(multipleFichas);
        assertEquals(multipleFichas, paciente.getFichasTecnicas());
        assertEquals(2, paciente.getFichasTecnicas().size());
    }

    @Test
    void testPacienteFTCompleteObject() {
        // Test setting all fields
        paciente.setIdPaciente(500L);
        paciente.setIdUsuario(600L);
        paciente.setDocumento("DOC-500-600");
        paciente.setFechaNacimiento(testDate);
        paciente.setFotografia(new byte[]{10, 20, 30});
        paciente.setUsuario(testUsuario);
        paciente.setFichasTecnicas(Arrays.asList(testFichaTecnica));
        
        // Verify all fields
        assertEquals(500L, paciente.getIdPaciente());
        assertEquals(600L, paciente.getIdUsuario());
        assertEquals("DOC-500-600", paciente.getDocumento());
        assertEquals(testDate, paciente.getFechaNacimiento());
        assertArrayEquals(new byte[]{10, 20, 30}, paciente.getFotografia());
        assertEquals(testUsuario, paciente.getUsuario());
        assertEquals(1, paciente.getFichasTecnicas().size());
        assertEquals(testFichaTecnica, paciente.getFichasTecnicas().get(0));
    }

    @Test
    void testPacienteFTFieldModification() {
        // Set initial values
        paciente.setIdPaciente(1L);
        paciente.setDocumento("Original");
        paciente.setUsuario(testUsuario);
        
        // Modify values
        paciente.setIdPaciente(2L);
        paciente.setDocumento("Modificado");
        Usuario newUsuario = new Usuario();
        newUsuario.setId(999L);
        paciente.setUsuario(newUsuario);
        
        // Verify modifications
        assertEquals(2L, paciente.getIdPaciente());
        assertEquals("Modificado", paciente.getDocumento());
        assertEquals(newUsuario, paciente.getUsuario());
    }

    @Test
    void testPacienteFTEdgeCases() {
        // Test with very long document
        String veryLongDoc = "A".repeat(1000);
        paciente.setDocumento(veryLongDoc);
        assertEquals(veryLongDoc, paciente.getDocumento());
        
        // Test with special characters in document
        paciente.setDocumento("DOC-123 & Special@#$%");
        assertEquals("DOC-123 & Special@#$%", paciente.getDocumento());
        
        // Test with numbers in document
        paciente.setDocumento("123456789");
        assertEquals("123456789", paciente.getDocumento());
        
        // Test with mixed case
        paciente.setDocumento("Doc-ABC-123");
        assertEquals("Doc-ABC-123", paciente.getDocumento());
    }

    @Test
    void testPacienteFTNullHandling() {
        // Test that all fields can handle null values properly
        paciente.setIdPaciente(null);
        paciente.setIdUsuario(null);
        paciente.setDocumento(null);
        paciente.setFechaNacimiento(null);
        paciente.setFotografia(null);
        paciente.setUsuario(null);
        paciente.setFichasTecnicas(null);
        
        // Verify all fields are null
        assertNull(paciente.getIdPaciente());
        assertNull(paciente.getIdUsuario());
        assertNull(paciente.getDocumento());
        assertNull(paciente.getFechaNacimiento());
        assertNull(paciente.getFotografia());
        assertNull(paciente.getUsuario());
        assertNull(paciente.getFichasTecnicas());
    }
}
