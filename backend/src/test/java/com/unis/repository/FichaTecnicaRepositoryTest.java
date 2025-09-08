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

    @Test
    void testFichaTecnicaRepositoryInheritance() {
        // Test that the repository extends the expected Panache classes
        assertTrue(fichaTecnicaRepository instanceof io.quarkus.hibernate.orm.panache.PanacheRepository);
        assertTrue(fichaTecnicaRepository instanceof io.quarkus.hibernate.orm.panache.PanacheRepositoryBase);
    }

    @Test
    void testFichaTecnicaRepositoryPackage() {
        // Test package information
        assertEquals("com.unis.repository", fichaTecnicaRepository.getClass().getPackageName());
    }

    @Test
    void testFichaTecnicaRepositoryName() {
        // Test class name
        assertEquals("FichaTecnicaRepository", fichaTecnicaRepository.getClass().getSimpleName());
    }

    @Test
    void testFichaTecnicaRepositoryModifiers() {
        // Test that the class is public
        assertTrue(java.lang.reflect.Modifier.isPublic(fichaTecnicaRepository.getClass().getModifiers()));
    }
}
