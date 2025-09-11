package com.unis.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for UsuarioInterconexionRepository
 */
public class UsuarioInterconexionRepositoryTest {

    private UsuarioInterconexionRepository usuarioInterconexionRepository;

    @BeforeEach
    void setUp() {
        usuarioInterconexionRepository = new UsuarioInterconexionRepository();
    }

    @Test
    void testUsuarioInterconexionRepositoryInstantiation() {
        assertNotNull(usuarioInterconexionRepository);
        assertTrue(usuarioInterconexionRepository instanceof UsuarioInterconexionRepository);
    }

    @Test
    void testUsuarioInterconexionRepositoryType() {
        assertEquals(UsuarioInterconexionRepository.class, usuarioInterconexionRepository.getClass());
    }
}
