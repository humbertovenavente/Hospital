package com.unis.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for FichaTecnicaRepository
 */
public class FichaTecnicaRepositoryTest {

    private FichaTecnicaRepository fichaTecnicaRepository;

    @BeforeEach
    void setUp() {
        fichaTecnicaRepository = new FichaTecnicaRepository();
    }

    @Test
    void testFichaTecnicaRepositoryInstantiation() {
        assertNotNull(fichaTecnicaRepository);
        assertTrue(fichaTecnicaRepository instanceof FichaTecnicaRepository);
    }

    @Test
    void testFichaTecnicaRepositoryType() {
        assertEquals(FichaTecnicaRepository.class, fichaTecnicaRepository.getClass());
    }
}
