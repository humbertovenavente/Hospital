package com.unis.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import jakarta.ws.rs.core.Response;

public class HealthControllerTest {
    
    private HealthController healthController;
    
    @BeforeEach
    void setUp() {
        healthController = new HealthController();
    }
    
    @Test
    void testHealthControllerInstantiation() {
        assertNotNull(healthController);
        assertTrue(healthController instanceof HealthController);
    }
    
    @Test
    void testHealthCheck_Success() {
        // Act
        Response response = healthController.healthCheck();
        
        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals("OK", response.getEntity());
    }
    
    @Test
    void testHealthCheck_ResponseType() {
        // Act
        Response response = healthController.healthCheck();
        
        // Assert
        assertNotNull(response);
        assertTrue(response instanceof Response);
    }
    
    @Test
    void testHealthCheck_ResponseStatus() {
        // Act
        Response response = healthController.healthCheck();
        
        // Assert
        assertEquals(200, response.getStatus());
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }
    
    @Test
    void testHealthCheck_ResponseEntity() {
        // Act
        Response response = healthController.healthCheck();
        
        // Assert
        assertNotNull(response.getEntity());
        assertEquals("OK", response.getEntity());
        assertTrue(response.getEntity() instanceof String);
    }
    
    @Test
    void testHealthCheck_ResponseHeaders() {
        // Act
        Response response = healthController.healthCheck();
        
        // Assert
        assertNotNull(response.getHeaders());
        // The response headers should not be null
        assertNotNull(response.getHeaders());
    }
    
    @Test
    void testHealthCheck_MultipleCalls() {
        // Act - Call multiple times
        Response response1 = healthController.healthCheck();
        Response response2 = healthController.healthCheck();
        Response response3 = healthController.healthCheck();
        
        // Assert - All responses should be identical
        assertNotNull(response1);
        assertNotNull(response2);
        assertNotNull(response3);
        
        assertEquals(Response.Status.OK.getStatusCode(), response1.getStatus());
        assertEquals(Response.Status.OK.getStatusCode(), response2.getStatus());
        assertEquals(Response.Status.OK.getStatusCode(), response3.getStatus());
        
        assertEquals("OK", response1.getEntity());
        assertEquals("OK", response2.getEntity());
        assertEquals("OK", response3.getEntity());
    }
    
    @Test
    void testHealthCheck_ResponseConsistency() {
        // Act
        Response response = healthController.healthCheck();
        
        // Assert - Verify response consistency
        assertNotNull(response);
        assertEquals(200, response.getStatus());
        assertEquals("OK", response.getEntity());
        
        // Verify the response object properties
        assertTrue(response.getStatus() == 200);
        assertTrue("OK".equals(response.getEntity()));
    }
    
    @Test
    void testHealthControllerAnnotations() {
        // Test that the class has the expected annotations
        assertNotNull(HealthController.class.getAnnotation(jakarta.ws.rs.Path.class));
        
        // Test Path annotation value
        jakarta.ws.rs.Path pathAnnotation = HealthController.class.getAnnotation(jakarta.ws.rs.Path.class);
        assertEquals("/health", pathAnnotation.value());
    }
    
    @Test
    void testHealthCheckMethodAnnotations() {
        try {
            // Test that the healthCheck method has the expected annotations
            java.lang.reflect.Method healthCheckMethod = HealthController.class.getMethod("healthCheck");
            
            assertNotNull(healthCheckMethod.getAnnotation(jakarta.ws.rs.GET.class));
            assertNotNull(healthCheckMethod.getAnnotation(jakarta.ws.rs.Produces.class));
            
            // Test Produces annotation value
            jakarta.ws.rs.Produces producesAnnotation = healthCheckMethod.getAnnotation(jakarta.ws.rs.Produces.class);
            assertEquals(1, producesAnnotation.value().length);
            assertEquals("text/plain", producesAnnotation.value()[0]);
            
        } catch (NoSuchMethodException e) {
            fail("healthCheck method not found: " + e.getMessage());
        }
    }
    
    @Test
    void testHealthCheck_ResponseObjectProperties() {
        // Act
        Response response = healthController.healthCheck();
        
        // Assert - Test various response properties
        assertNotNull(response);
        assertTrue(response.getStatus() >= 200 && response.getStatus() < 300); // Success status codes
        assertNotNull(response.getEntity());
        assertTrue(response.getEntity() instanceof String);
        assertEquals("OK", response.getEntity());
    }
    
    @Test
    void testHealthCheck_ResponseImmutability() {
        // Act
        Response response1 = healthController.healthCheck();
        Response response2 = healthController.healthCheck();
        
        // Assert - Each call should return a new response object
        assertNotNull(response1);
        assertNotNull(response2);
        assertNotSame(response1, response2); // Should be different objects
        
        // But both should have the same content
        assertEquals(response1.getStatus(), response2.getStatus());
        assertEquals(response1.getEntity(), response2.getEntity());
    }
}
