package com.unis.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for AseguradoraConexionRepository
 */
public class AseguradoraConexionRepositoryTest {

    private AseguradoraConexionRepository aseguradoraConexionRepository;

    @BeforeEach
    void setUp() {
        aseguradoraConexionRepository = new AseguradoraConexionRepository();
    }

    @Test
    void testAseguradoraConexionRepositoryInstantiation() {
        assertNotNull(aseguradoraConexionRepository);
        assertTrue(aseguradoraConexionRepository instanceof AseguradoraConexionRepository);
    }

    @Test
    void testAseguradoraConexionRepositoryType() {
        assertEquals(AseguradoraConexionRepository.class, aseguradoraConexionRepository.getClass());
    }
}
