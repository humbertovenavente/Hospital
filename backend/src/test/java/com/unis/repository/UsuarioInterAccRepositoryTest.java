package com.unis.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for UsuarioInterAccRepository
 */
public class UsuarioInterAccRepositoryTest {

    private UsuarioInterAccRepository usuarioInterAccRepository;

    @BeforeEach
    void setUp() {
        usuarioInterAccRepository = new UsuarioInterAccRepository();
    }

    @Test
    void testUsuarioInterAccRepositoryInstantiation() {
        assertNotNull(usuarioInterAccRepository);
        assertTrue(usuarioInterAccRepository instanceof UsuarioInterAccRepository);
    }

    @Test
    void testUsuarioInterAccRepositoryType() {
        assertEquals(UsuarioInterAccRepository.class, usuarioInterAccRepository.getClass());
    }
}
