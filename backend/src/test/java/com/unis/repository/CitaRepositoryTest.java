package com.unis.repository;

import com.unis.model.Cita;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for CitaRepository.
 * Tests basic repository functionality.
 */
class CitaRepositoryTest {

    private CitaRepository citaRepository;

    @BeforeEach
    void setUp() {
        citaRepository = new CitaRepository();
    }

    @Test
    void testRepositoryInstantiation() {
        assertNotNull(citaRepository);
    }

    @Test
    void testRepositoryType() {
        assertTrue(citaRepository instanceof CitaRepository);
    }

    @Test
    void testRepositoryNotNull() {
        assertNotNull(citaRepository);
    }
}
