package com.unis.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;

public class ServicioTest {

    private Servicio servicio;

    @BeforeEach
    void setUp() {
        servicio = new Servicio();
    }

    @Test
    void testConstructorPorDefecto() {
        assertNotNull(servicio);
    }

    @Test
    void testGettersAndSetters() {
        // Arrange
        String nombre = "Consulta General";
        double costo = 50.0;
        boolean cubiertoSeguro = true;

        // Act
        servicio.nombre = nombre;
        servicio.costo = costo;
        servicio.cubiertoSeguro = cubiertoSeguro;

        // Assert
        assertEquals(nombre, servicio.nombre);
        assertEquals(costo, servicio.costo, 0.01);
        assertEquals(cubiertoSeguro, servicio.cubiertoSeguro);
    }

    @Test
    void testSetYGetId() {
        // Arrange
        Long id = 999L;

        // Act
        servicio.id = id;

        // Assert
        assertEquals(id, servicio.getId());
        assertEquals(id, servicio.getIdServicio());
    }

    @Test
    void testSetYGetNombre() {
        // Arrange
        String nombre = "Radiografía";

        // Act
        servicio.nombre = nombre;

        // Assert
        assertEquals(nombre, servicio.nombre);
    }

    @Test
    void testSetYGetCosto() {
        // Arrange
        double costo = 75.50;

        // Act
        servicio.costo = costo;

        // Assert
        assertEquals(costo, servicio.costo, 0.01);
    }

    @Test
    void testSetYGetCubiertoSeguro() {
        // Arrange
        boolean cubiertoSeguro = false;

        // Act
        servicio.cubiertoSeguro = cubiertoSeguro;

        // Assert
        assertEquals(cubiertoSeguro, servicio.cubiertoSeguro);
    }

    @Test
    void testSetYGetServicioPadre() {
        // Arrange
        Servicio servicioPadre = new Servicio();
        servicioPadre.id = 1L;

        // Act
        servicio.servicioPadre = servicioPadre;

        // Assert
        assertEquals(servicioPadre, servicio.servicioPadre);
        assertEquals(servicioPadre.id, servicio.getParentId());
    }

    @Test
    void testSetYGetSubServicios() {
        // Arrange
        Servicio subServicio1 = new Servicio();
        Servicio subServicio2 = new Servicio();
        Set<Servicio> subServicios = new HashSet<>();
        subServicios.add(subServicio1);
        subServicios.add(subServicio2);

        // Act
        servicio.subServicios = subServicios;

        // Assert
        assertEquals(subServicios, servicio.subServicios);
        assertEquals(2, servicio.subServicios.size());
    }

    @Test
    void testGetParentIdNull() {
        // Arrange
        servicio.servicioPadre = null;

        // Act
        Long parentId = servicio.getParentId();

        // Assert
        assertNull(parentId);
    }

    @Test
    void testSetIdNull() {
        // Act
        servicio.id = null;

        // Assert
        assertNull(servicio.getId());
        assertNull(servicio.getIdServicio());
    }

    @Test
    void testSetNombreNull() {
        // Act
        servicio.nombre = null;

        // Assert
        assertNull(servicio.nombre);
    }

    @Test
    void testSetServicioPadreNull() {
        // Act
        servicio.servicioPadre = null;

        // Assert
        assertNull(servicio.servicioPadre);
        assertNull(servicio.getParentId());
    }

    @Test
    void testSetSubServiciosNull() {
        // Act
        servicio.subServicios = null;

        // Assert
        assertNull(servicio.subServicios);
    }

    @Test
    void testSetNombreVacio() {
        // Arrange
        String nombreVacio = "";

        // Act
        servicio.nombre = nombreVacio;

        // Assert
        assertEquals(nombreVacio, servicio.nombre);
        assertTrue(servicio.nombre.isEmpty());
    }

    @Test
    void testEquals() {
        // Arrange
        Servicio servicio1 = new Servicio();
        servicio1.id = 1L;
        
        Servicio servicio2 = new Servicio();
        servicio2.id = 1L;
        
        Servicio servicio3 = new Servicio();
        servicio3.id = 2L;

        // Act & Assert
        assertEquals(servicio1, servicio2);
        assertNotEquals(servicio1, servicio3);
        assertNotEquals(servicio1, null);
        assertEquals(servicio1, servicio1);
    }

    @Test
    void testHashCode() {
        // Arrange
        Servicio servicio1 = new Servicio();
        servicio1.id = 1L;
        
        Servicio servicio2 = new Servicio();
        servicio2.id = 1L;

        // Act & Assert
        assertEquals(servicio1.hashCode(), servicio2.hashCode());
    }
}
