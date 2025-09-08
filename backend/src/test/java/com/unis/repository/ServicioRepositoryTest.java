package com.unis.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ServicioRepository
 */
public class ServicioRepositoryTest {

    private ServicioRepository servicioRepository;

    @BeforeEach
    void setUp() {
        servicioRepository = new ServicioRepository();
    }

    @Test
    void testServicioRepositoryInstantiation() {
        assertNotNull(servicioRepository);
        assertTrue(servicioRepository instanceof ServicioRepository);
    }

    @Test
    void testServicioRepositoryType() {
        assertEquals(ServicioRepository.class, servicioRepository.getClass());
    }
}
