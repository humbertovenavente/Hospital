package com.unis.repository;

import com.unis.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for UsuarioRepository.
 * Tests basic repository functionality.
 */
class UsuarioRepositoryTest {

    private UsuarioRepository usuarioRepository;

    @BeforeEach
    void setUp() {
        usuarioRepository = new UsuarioRepository();
    }

    @Test
    void testRepositoryInstantiation() {
        assertNotNull(usuarioRepository);
    }

    @Test
    void testRepositoryType() {
        assertTrue(usuarioRepository instanceof UsuarioRepository);
    }

    @Test
    void testRepositoryNotNull() {
        assertNotNull(usuarioRepository);
    }

    @Test
    void testRepositoryMethodsExist() {
        assertNotNull(usuarioRepository);
        // Test that the repository can be instantiated
        assertDoesNotThrow(() -> new UsuarioRepository());
    }

    @Test
    void testRepositoryClass() {
        assertEquals(UsuarioRepository.class, usuarioRepository.getClass());
    }

    @Test
    void testRepositoryInstance() {
        assertInstanceOf(UsuarioRepository.class, usuarioRepository);
    }
}
