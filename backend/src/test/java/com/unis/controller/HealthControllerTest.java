package com.unis.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import jakarta.ws.rs.core.Response;
import static org.junit.jupiter.api.Assertions.*;

public class HealthControllerTest {

    private HealthController healthController;

    @BeforeEach
    void setUp() {
        healthController = new HealthController();
    }

    @Test
    void testHealthCheck() {
        // Act
        Response response = healthController.healthCheck();
        
        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatus());
        assertEquals("OK", response.getEntity());
    }

    @Test
    void testHealthCheckResponseType() {
        // Act
        Response response = healthController.healthCheck();
        
        // Assert
        assertNotNull(response);
        // Verificar que la respuesta sea exitosa
        assertEquals(200, response.getStatus());
        assertEquals("OK", response.getEntity());
    }
}
