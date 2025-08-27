package com.unis.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import jakarta.ws.rs.core.Response;

/**
 * Test class for HealthController.
 * Tests the health check endpoint functionality.
 */
class HealthControllerTest {

    private HealthController healthController;

    @BeforeEach
    void setUp() {
        healthController = new HealthController();
    }

    @Test
    void testHealthCheckReturnsOK() {
        // Act
        Response response = healthController.healthCheck();
        
        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatus());
        assertEquals("OK", response.getEntity());
    }

    @Test
    void testHealthCheckResponseNotNull() {
        Response response = healthController.healthCheck();
        assertNotNull(response);
    }

    @Test
    void testHealthCheckResponseStatus() {
        Response response = healthController.healthCheck();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    void testHealthCheckResponseEntity() {
        Response response = healthController.healthCheck();
        String entity = (String) response.getEntity();
        assertEquals("OK", entity);
    }

    @Test
    void testHealthCheckResponseType() {
        Response response = healthController.healthCheck();
        assertTrue(response.getEntity() instanceof String);
    }

    @Test
    void testHealthCheckMultipleCalls() {
        // Verificar que múltiples llamadas devuelven el mismo resultado
        Response response1 = healthController.healthCheck();
        Response response2 = healthController.healthCheck();
        
        assertEquals(response1.getStatus(), response2.getStatus());
        assertEquals(response1.getEntity(), response2.getEntity());
    }

    @Test
    void testHealthCheckResponseHeaders() {
        Response response = healthController.healthCheck();
        assertNotNull(response.getHeaders());
    }

    @Test
    void testHealthCheckResponseMetadata() {
        Response response = healthController.healthCheck();
        assertNotNull(response.getMetadata());
    }

    @Test
    void testHealthCheckResponseEntityNotNull() {
        Response response = healthController.healthCheck();
        assertNotNull(response.getEntity());
    }

    @Test
    void testHealthCheckResponseEntityString() {
        Response response = healthController.healthCheck();
        String entity = (String) response.getEntity();
        assertNotNull(entity);
        assertFalse(entity.isEmpty());
        assertEquals("OK", entity);
    }

    @Test
    void testHealthCheckResponseEntityLength() {
        Response response = healthController.healthCheck();
        String entity = (String) response.getEntity();
        assertEquals(2, entity.length());
    }

    @Test
    void testHealthCheckResponseEntityContent() {
        Response response = healthController.healthCheck();
        String entity = (String) response.getEntity();
        assertTrue(entity.contains("OK"));
        assertFalse(entity.contains("ERROR"));
        assertFalse(entity.contains("FAIL"));
    }

    @Test
    void testHealthCheckResponseStatusInfo() {
        Response response = healthController.healthCheck();
        // Verificar que el StatusInfo no sea null y tenga el código correcto
        assertNotNull(response.getStatusInfo());
        assertEquals(200, response.getStatusInfo().getStatusCode());
    }

    @Test
    void testHealthCheckResponseStatusFamily() {
        Response response = healthController.healthCheck();
        // Verificar que la familia del status sea SUCCESSFUL
        assertEquals(Response.Status.Family.SUCCESSFUL, response.getStatusInfo().getFamily());
    }

    @Test
    void testHealthCheckResponseHasEntity() {
        Response response = healthController.healthCheck();
        assertTrue(response.hasEntity());
    }

    @Test
    void testHealthCheckResponseEntityType() {
        Response response = healthController.healthCheck();
        Class<?> entityType = response.getEntity().getClass();
        assertEquals(String.class, entityType);
    }

    @Test
    void testHealthCheckResponseEntityImmutability() {
        Response response1 = healthController.healthCheck();
        Response response2 = healthController.healthCheck();
        
        // Los objetos Response son inmutables, cada llamada debe crear uno nuevo
        assertNotSame(response1, response2);
    }

    @Test
    void testHealthCheckResponseConsistency() {
        // Verificar consistencia en múltiples ejecuciones
        for (int i = 0; i < 5; i++) {
            Response response = healthController.healthCheck();
            assertEquals(200, response.getStatus());
            assertEquals("OK", response.getEntity());
        }
    }

    @Test
    void testHealthCheckResponsePerformance() {
        // Test básico de rendimiento
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < 100; i++) {
            healthController.healthCheck();
        }
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        // Debe completarse en menos de 1 segundo
        assertTrue(duration < 1000, "Health check debe ser rápido, tomó: " + duration + "ms");
    }
}
