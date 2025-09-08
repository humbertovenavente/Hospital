package com.unis.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Test class for ReporteResponse.
 * Tests all getters, setters, and constructor functionality.
 */
class ReporteResponseTest {

    private ReporteResponse<String> reporteResponse;
    private String encabezado;
    private List<String> datos;

    @BeforeEach
    void setUp() {
        encabezado = "Reporte de Pacientes";
        datos = Arrays.asList("Paciente 1", "Paciente 2", "Paciente 3");
        reporteResponse = new ReporteResponse<>(encabezado, datos);
    }

    @Test
    void testConstructorWithValidParameters() {
        assertNotNull(reporteResponse);
        assertEquals(encabezado, reporteResponse.getEncabezado());
        assertEquals(datos, reporteResponse.getDatos());
    }

    @Test
    void testConstructorWithNullEncabezado() {
        ReporteResponse<String> nullHeaderResponse = new ReporteResponse<>(null, datos);
        assertNull(nullHeaderResponse.getEncabezado());
        assertEquals(datos, nullHeaderResponse.getDatos());
    }

    @Test
    void testConstructorWithNullDatos() {
        ReporteResponse<String> nullDataResponse = new ReporteResponse<>(encabezado, null);
        assertEquals(encabezado, nullDataResponse.getEncabezado());
        assertNull(nullDataResponse.getDatos());
    }

    @Test
    void testConstructorWithEmptyList() {
        List<String> emptyList = Collections.emptyList();
        ReporteResponse<String> emptyDataResponse = new ReporteResponse<>(encabezado, emptyList);
        assertEquals(encabezado, emptyDataResponse.getEncabezado());
        assertEquals(emptyList, emptyDataResponse.getDatos());
        assertTrue(emptyDataResponse.getDatos().isEmpty());
    }

    @Test
    void testConstructorWithSingleElementList() {
        List<String> singleElementList = Collections.singletonList("Solo un elemento");
        ReporteResponse<String> singleElementResponse = new ReporteResponse<>(encabezado, singleElementList);
        assertEquals(encabezado, singleElementResponse.getEncabezado());
        assertEquals(singleElementList, singleElementResponse.getDatos());
        assertEquals(1, singleElementResponse.getDatos().size());
    }

    @Test
    void testGetEncabezado() {
        assertEquals(encabezado, reporteResponse.getEncabezado());
    }

    @Test
    void testSetEncabezado() {
        String newEncabezado = "Nuevo Encabezado";
        reporteResponse.setEncabezado(newEncabezado);
        assertEquals(newEncabezado, reporteResponse.getEncabezado());
    }

    @Test
    void testSetEncabezadoWithNull() {
        reporteResponse.setEncabezado(null);
        assertNull(reporteResponse.getEncabezado());
    }

    @Test
    void testSetEncabezadoWithEmptyString() {
        reporteResponse.setEncabezado("");
        assertEquals("", reporteResponse.getEncabezado());
    }

    @Test
    void testSetEncabezadoWithSpecialCharacters() {
        String specialEncabezado = "Encabezado Especial: 123!@#$%^&*()";
        reporteResponse.setEncabezado(specialEncabezado);
        assertEquals(specialEncabezado, reporteResponse.getEncabezado());
    }

    @Test
    void testSetEncabezadoWithSpaces() {
        String spacedEncabezado = "  Encabezado con espacios  ";
        reporteResponse.setEncabezado(spacedEncabezado);
        assertEquals(spacedEncabezado, reporteResponse.getEncabezado());
    }

    @Test
    void testGetDatos() {
        assertEquals(datos, reporteResponse.getDatos());
        assertEquals(3, reporteResponse.getDatos().size());
        assertTrue(reporteResponse.getDatos().contains("Paciente 1"));
        assertTrue(reporteResponse.getDatos().contains("Paciente 2"));
        assertTrue(reporteResponse.getDatos().contains("Paciente 3"));
    }

    @Test
    void testSetDatos() {
        List<String> newDatos = Arrays.asList("Nuevo 1", "Nuevo 2");
        reporteResponse.setDatos(newDatos);
        assertEquals(newDatos, reporteResponse.getDatos());
        assertEquals(2, reporteResponse.getDatos().size());
    }

    @Test
    void testSetDatosWithNull() {
        reporteResponse.setDatos(null);
        assertNull(reporteResponse.getDatos());
    }

    @Test
    void testSetDatosWithEmptyList() {
        List<String> emptyList = Collections.emptyList();
        reporteResponse.setDatos(emptyList);
        assertEquals(emptyList, reporteResponse.getDatos());
        assertTrue(reporteResponse.getDatos().isEmpty());
    }

    @Test
    void testSetDatosWithLargeList() {
        List<String> largeList = Arrays.asList(
            "Elemento 1", "Elemento 2", "Elemento 3", "Elemento 4", "Elemento 5",
            "Elemento 6", "Elemento 7", "Elemento 8", "Elemento 9", "Elemento 10"
        );
        reporteResponse.setDatos(largeList);
        assertEquals(largeList, reporteResponse.getDatos());
        assertEquals(10, reporteResponse.getDatos().size());
    }

    @Test
    void testMultipleSetters() {
        ReporteResponse<String> multiResponse = new ReporteResponse<>("Original", Arrays.asList("Original"));
        
        multiResponse.setEncabezado("Nuevo Encabezado");
        multiResponse.setDatos(Arrays.asList("Nuevo 1", "Nuevo 2", "Nuevo 3"));

        assertEquals("Nuevo Encabezado", multiResponse.getEncabezado());
        assertEquals(3, multiResponse.getDatos().size());
        assertTrue(multiResponse.getDatos().contains("Nuevo 1"));
    }

    @Test
    void testDataIntegrity() {
        // Verificar que los datos no cambien después de múltiples lecturas
        String encabezado1 = reporteResponse.getEncabezado();
        String encabezado2 = reporteResponse.getEncabezado();
        assertEquals(encabezado1, encabezado2);

        List<String> datos1 = reporteResponse.getDatos();
        List<String> datos2 = reporteResponse.getDatos();
        assertEquals(datos1, datos2);
    }

    @Test
    void testEdgeCaseValues() {
        ReporteResponse<String> edgeResponse = new ReporteResponse<>("", Collections.emptyList());
        
        edgeResponse.setEncabezado("A");
        edgeResponse.setDatos(Collections.singletonList(""));

        assertEquals("A", edgeResponse.getEncabezado());
        assertEquals(1, edgeResponse.getDatos().size());
        assertEquals("", edgeResponse.getDatos().get(0));
    }

    @Test
    void testGenericTypeWithDifferentTypes() {
        // Test con Integer
        ReporteResponse<Integer> intResponse = new ReporteResponse<>("Reporte de Números", Arrays.asList(1, 2, 3));
        assertEquals("Reporte de Números", intResponse.getEncabezado());
        assertEquals(Arrays.asList(1, 2, 3), intResponse.getDatos());

        // Test con Boolean
        ReporteResponse<Boolean> boolResponse = new ReporteResponse<>("Reporte de Estados", Arrays.asList(true, false));
        assertEquals("Reporte de Estados", boolResponse.getEncabezado());
        assertEquals(Arrays.asList(true, false), boolResponse.getDatos());
    }

    @Test
    void testRepeatedUpdates() {
        // Actualizar el mismo valor múltiples veces
        for (int i = 0; i < 5; i++) {
            reporteResponse.setEncabezado("Encabezado " + i);
            reporteResponse.setDatos(Arrays.asList("Dato " + i));
        }

        assertEquals("Encabezado 4", reporteResponse.getEncabezado());
        assertEquals(1, reporteResponse.getDatos().size());
        assertEquals("Dato 4", reporteResponse.getDatos().get(0));
    }

    @Test
    void testFieldIndependence() {
        // Verificar que los campos son independientes
        reporteResponse.setEncabezado("Encabezado Original");
        reporteResponse.setDatos(Arrays.asList("Dato Original"));

        // Cambiar solo el encabezado
        reporteResponse.setEncabezado("Nuevo Encabezado");

        assertEquals("Nuevo Encabezado", reporteResponse.getEncabezado());
        assertEquals(Arrays.asList("Dato Original"), reporteResponse.getDatos());

        // Cambiar solo los datos
        reporteResponse.setDatos(Arrays.asList("Nuevo Dato"));

        assertEquals("Nuevo Encabezado", reporteResponse.getEncabezado());
        assertEquals(Arrays.asList("Nuevo Dato"), reporteResponse.getDatos());
    }

    @Test
    void testNullHandling() {
        // Test de manejo de valores nulos
        reporteResponse.setEncabezado(null);
        reporteResponse.setDatos(null);

        assertNull(reporteResponse.getEncabezado());
        assertNull(reporteResponse.getDatos());
    }
}
