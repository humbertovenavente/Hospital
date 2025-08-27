package com.unis.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for CorsFilter
 */
public class CorsFilterTest {

    private CorsFilter corsFilter;

    @BeforeEach
    void setUp() {
        corsFilter = new CorsFilter();
    }

    @Test
    void testCorsFilterInstantiation() {
        assertNotNull(corsFilter);
        assertTrue(corsFilter instanceof CorsFilter);
    }

    @Test
    void testCorsFilterType() {
        assertEquals(CorsFilter.class, corsFilter.getClass());
    }

    @Test
    void testCorsFilterImplementsContainerResponseFilter() {
        assertTrue(corsFilter instanceof jakarta.ws.rs.container.ContainerResponseFilter);
    }

    @Test
    void testCorsFilterHasProviderAnnotation() {
        assertTrue(CorsFilter.class.isAnnotationPresent(jakarta.ws.rs.ext.Provider.class));
    }

    @Test
    void testCorsFilterHasApplicationScopedAnnotation() {
        assertTrue(CorsFilter.class.isAnnotationPresent(jakarta.enterprise.context.ApplicationScoped.class));
    }
}
