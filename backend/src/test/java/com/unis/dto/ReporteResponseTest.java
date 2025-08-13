package com.unis.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

public class ReporteResponseTest {

    private ReporteResponse<String> reporteResponse;

    @BeforeEach
    void setUp() {
        reporteResponse = new ReporteResponse<>("Test Header", Arrays.asList("data1", "data2"));
    }

    @Test
    void testConstructorConParametros() {
        // Arrange
        String encabezado = "Reporte de Ventas";
        List<String> datos = Arrays.asList("Venta 1", "Venta 2", "Venta 3");

        // Act
        ReporteResponse<String> response = new ReporteResponse<>(encabezado, datos);

        // Assert
        assertEquals(encabezado, response.getEncabezado());
        assertEquals(datos, response.getDatos());
    }

    @Test
    void testGettersAndSetters() {
        // Arrange
        String nuevoEncabezado = "Nuevo Encabezado";
        List<String> nuevosDatos = Arrays.asList("nuevo1", "nuevo2");

        // Act
        reporteResponse.setEncabezado(nuevoEncabezado);
        reporteResponse.setDatos(nuevosDatos);

        // Assert
        assertEquals(nuevoEncabezado, reporteResponse.getEncabezado());
        assertEquals(nuevosDatos, reporteResponse.getDatos());
    }

    @Test
    void testSetEncabezado() {
        // Arrange
        String encabezado = "Encabezado de Prueba";

        // Act
        reporteResponse.setEncabezado(encabezado);

        // Assert
        assertEquals(encabezado, reporteResponse.getEncabezado());
    }

    @Test
    void testSetDatos() {
        // Arrange
        List<String> datos = Arrays.asList("dato1", "dato2", "dato3");

        // Act
        reporteResponse.setDatos(datos);

        // Assert
        assertEquals(datos, reporteResponse.getDatos());
        assertEquals(3, reporteResponse.getDatos().size());
    }

    @Test
    void testSetDatosVacio() {
        // Arrange
        List<String> datosVacios = Arrays.asList();

        // Act
        reporteResponse.setDatos(datosVacios);

        // Assert
        assertEquals(datosVacios, reporteResponse.getDatos());
        assertTrue(reporteResponse.getDatos().isEmpty());
    }

    @Test
    void testSetDatosNull() {
        // Act
        reporteResponse.setDatos(null);

        // Assert
        assertNull(reporteResponse.getDatos());
    }

    @Test
    void testSetEncabezadoNull() {
        // Act
        reporteResponse.setEncabezado(null);

        // Assert
        assertNull(reporteResponse.getEncabezado());
    }
}
