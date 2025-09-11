package com.unis.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Test class for Servicio entity
 */
public class ServicioTest {

    private Servicio servicio;
    private Servicio servicioPadre;
    private Servicio subServicio;

    @BeforeEach
    void setUp() {
        servicio = new Servicio();
        servicioPadre = new Servicio();
        subServicio = new Servicio();
        
        // Setup parent service
        servicioPadre.id = 1L;
        servicioPadre.nombre = "Servicio Padre";
        servicioPadre.costo = 100.0;
        servicioPadre.cubiertoSeguro = true;
        
        // Setup main service
        servicio.id = 2L;
        servicio.nombre = "Servicio Principal";
        servicio.costo = 50.0;
        servicio.cubiertoSeguro = false;
        servicio.servicioPadre = servicioPadre;
        
        // Setup sub service
        subServicio.id = 3L;
        subServicio.nombre = "Sub Servicio";
        subServicio.costo = 25.0;
        subServicio.cubiertoSeguro = true;
        subServicio.servicioPadre = servicio;
    }

    @Test
    void testServicioInstantiation() {
        assertNotNull(servicio);
        assertTrue(servicio instanceof Servicio);
    }

    @Test
    void testServicioId() {
        // Test getId
        assertEquals(2L, servicio.getId());
        
        // Test getIdServicio (alias)
        assertEquals(2L, servicio.getIdServicio());
        
        // Test with different ID
        servicio.id = 999L;
        assertEquals(999L, servicio.getId());
        assertEquals(999L, servicio.getIdServicio());
        
        // Test with null ID
        servicio.id = null;
        assertNull(servicio.getId());
        assertNull(servicio.getIdServicio());
    }

    @Test
    void testServicioNombre() {
        // Test nombre field
        assertEquals("Servicio Principal", servicio.nombre);
        
        // Test setting nombre
        servicio.nombre = "Nuevo Nombre";
        assertEquals("Nuevo Nombre", servicio.nombre);
        
        // Test with null
        servicio.nombre = null;
        assertNull(servicio.nombre);
        
        // Test with empty string
        servicio.nombre = "";
        assertEquals("", servicio.nombre);
        
        // Test with special characters
        servicio.nombre = "Servicio & Especial @ 123";
        assertEquals("Servicio & Especial @ 123", servicio.nombre);
    }

    @Test
    void testServicioCosto() {
        // Test costo field
        assertEquals(50.0, servicio.costo, 0.001);
        
        // Test setting costo
        servicio.costo = 75.5;
        assertEquals(75.5, servicio.costo, 0.001);
        
        // Test with zero
        servicio.costo = 0.0;
        assertEquals(0.0, servicio.costo, 0.001);
        
        // Test with negative value
        servicio.costo = -10.0;
        assertEquals(-10.0, servicio.costo, 0.001);
        
        // Test with large value
        servicio.costo = 999999.99;
        assertEquals(999999.99, servicio.costo, 0.001);
        
        // Test with decimal precision
        servicio.costo = 123.456789;
        assertEquals(123.456789, servicio.costo, 0.000001);
    }

    @Test
    void testServicioCubiertoSeguro() {
        // Test cubiertoSeguro field
        assertFalse(servicio.cubiertoSeguro);
        
        // Test setting cubiertoSeguro
        servicio.cubiertoSeguro = true;
        assertTrue(servicio.cubiertoSeguro);
        
        // Test setting back to false
        servicio.cubiertoSeguro = false;
        assertFalse(servicio.cubiertoSeguro);
    }

    @Test
    void testServicioServicioPadre() {
        // Test servicioPadre field
        assertEquals(servicioPadre, servicio.servicioPadre);
        
        // Test setting servicioPadre
        Servicio nuevoPadre = new Servicio();
        nuevoPadre.id = 999L;
        nuevoPadre.nombre = "Nuevo Padre";
        servicio.servicioPadre = nuevoPadre;
        assertEquals(nuevoPadre, servicio.servicioPadre);
        
        // Test setting to null
        servicio.servicioPadre = null;
        assertNull(servicio.servicioPadre);
        
        // Test with self (should be allowed but might cause issues)
        servicio.servicioPadre = servicio;
        assertEquals(servicio, servicio.servicioPadre);
    }

    @Test
    void testServicioSubServicios() {
        // Test subServicios field
        assertNotNull(servicio.subServicios);
        assertTrue(servicio.subServicios instanceof HashSet);
        
        // Test adding sub service
        servicio.subServicios.add(subServicio);
        assertEquals(1, servicio.subServicios.size());
        assertTrue(servicio.subServicios.contains(subServicio));
        
        // Test adding multiple sub services
        Servicio otroSub = new Servicio();
        otroSub.id = 4L;
        otroSub.nombre = "Otro Sub";
        servicio.subServicios.add(otroSub);
        assertEquals(2, servicio.subServicios.size());
        
        // Test removing sub service
        servicio.subServicios.remove(subServicio);
        assertEquals(1, servicio.subServicios.size());
        assertFalse(servicio.subServicios.contains(subServicio));
        
        // Test clearing all sub services
        servicio.subServicios.clear();
        assertEquals(0, servicio.subServicios.size());
        assertTrue(servicio.subServicios.isEmpty());
    }

    @Test
    void testServicioGetParentId() {
        // Test getParentId with parent
        assertEquals(1L, servicio.getParentId());
        
        // Test getParentId without parent
        servicio.servicioPadre = null;
        assertNull(servicio.getParentId());
        
        // Test getParentId with parent that has null ID
        Servicio padreSinId = new Servicio();
        padreSinId.id = null;
        servicio.servicioPadre = padreSinId;
        assertNull(servicio.getParentId());
    }

    @Test
    void testServicioEquals() {
        // Test equals with same object
        assertTrue(servicio.equals(servicio));
        
        // Test equals with null
        assertFalse(servicio.equals(null));
        
        // Test equals with different type
        assertFalse(servicio.equals("string"));
        
        // Test equals with different service (same ID)
        Servicio otroServicio = new Servicio();
        otroServicio.id = 2L;
        assertTrue(servicio.equals(otroServicio));
        
        // Test equals with different service (different ID)
        otroServicio.id = 999L;
        assertFalse(servicio.equals(otroServicio));
    }

    @Test
    void testServicioHashCode() {
        // Test hashCode with ID
        assertEquals(Long.valueOf(2L).hashCode(), servicio.hashCode());
        
        // Test hashCode with null ID
        servicio.id = null;
        assertEquals(0, servicio.hashCode());
        
        // Test hashCode consistency
        servicio.id = 123L;
        int hashCode1 = servicio.hashCode();
        int hashCode2 = servicio.hashCode();
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    void testServicioCompleteObject() {
        // Test setting all fields
        servicio.id = 1000L;
        servicio.nombre = "Servicio Completo";
        servicio.costo = 150.75;
        servicio.cubiertoSeguro = true;
        servicio.servicioPadre = servicioPadre;
        servicio.subServicios.add(subServicio);
        
        // Verify all fields
        assertEquals(1000L, servicio.getId());
        assertEquals("Servicio Completo", servicio.nombre);
        assertEquals(150.75, servicio.costo, 0.001);
        assertTrue(servicio.cubiertoSeguro);
        assertEquals(servicioPadre, servicio.servicioPadre);
        assertEquals(1, servicio.subServicios.size());
        assertTrue(servicio.subServicios.contains(subServicio));
    }

    @Test
    void testServicioFieldModification() {
        // Set initial values
        servicio.id = 1L;
        servicio.nombre = "Original";
        servicio.costo = 100.0;
        
        // Modify values
        servicio.id = 2L;
        servicio.nombre = "Modificado";
        servicio.costo = 200.0;
        
        // Verify modifications
        assertEquals(2L, servicio.getId());
        assertEquals("Modificado", servicio.nombre);
        assertEquals(200.0, servicio.costo, 0.001);
    }

    @Test
    void testServicioEdgeCases() {
        // Test with very long strings
        String veryLongString = "A".repeat(1000);
        servicio.nombre = veryLongString;
        assertEquals(veryLongString, servicio.nombre);
        
        // Test with special characters
        String specialChars = "!@#$%^&*()_+-=[]{}|;':\",./<>?";
        servicio.nombre = specialChars;
        assertEquals(specialChars, servicio.nombre);
        
        // Test with numbers in string fields
        String numbersOnly = "123456789";
        servicio.nombre = numbersOnly;
        assertEquals(numbersOnly, servicio.nombre);
        
        // Test with extreme cost values
        servicio.costo = Double.MAX_VALUE;
        assertEquals(Double.MAX_VALUE, servicio.costo, 0.001);
        
        servicio.costo = Double.MIN_VALUE;
        assertEquals(Double.MIN_VALUE, servicio.costo, 0.001);
        
        servicio.costo = Double.NaN;
        assertTrue(Double.isNaN(servicio.costo));
        
        servicio.costo = Double.POSITIVE_INFINITY;
        assertTrue(Double.isInfinite(servicio.costo));
    }

    @Test
    void testServicioNullHandling() {
        // Test that all fields can handle null values properly
        servicio.nombre = null;
        servicio.servicioPadre = null;
        servicio.subServicios = null;
        
        // Verify all fields are null
        assertNull(servicio.nombre);
        assertNull(servicio.servicioPadre);
        assertNull(servicio.subServicios);
        
        // Test getParentId with null parent
        assertNull(servicio.getParentId());
    }
}
