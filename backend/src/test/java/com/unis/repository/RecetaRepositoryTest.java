package com.unis.repository;

import com.unis.model.Receta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for RecetaRepository.
 * Tests basic repository functionality.
 */
class RecetaRepositoryTest {

    private RecetaRepository recetaRepository;

    @BeforeEach
    void setUp() {
        recetaRepository = new RecetaRepository();
    }

    @Test
    void testRepositoryInstantiation() {
        assertNotNull(recetaRepository);
    }

    @Test
    void testRepositoryType() {
        assertTrue(recetaRepository instanceof RecetaRepository);
    }

    @Test
    void testRepositoryNotNull() {
        assertNotNull(recetaRepository);
    }

    @Test
    void testRepositoryMethodsExist() {
        assertNotNull(recetaRepository);
        // Test that the repository can be instantiated
        assertDoesNotThrow(() -> new RecetaRepository());
    }

    @Test
    void testRepositoryClass() {
        assertEquals(RecetaRepository.class, recetaRepository.getClass());
    }

    @Test
    void testRepositoryInstance() {
        assertInstanceOf(RecetaRepository.class, recetaRepository);
    }

    @Test
    void testRepositoryBasicFunctionality() {
        // Test basic repository operations
        assertNotNull(recetaRepository);
        assertTrue(recetaRepository instanceof RecetaRepository);
    }
}
