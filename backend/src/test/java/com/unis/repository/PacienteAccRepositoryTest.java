package com.unis.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for PacienteAccRepository
 */
public class PacienteAccRepositoryTest {

    private PacienteAccRepository pacienteAccRepository;

    @BeforeEach
    void setUp() {
        pacienteAccRepository = new PacienteAccRepository();
    }

    @Test
    void testPacienteAccRepositoryInstantiation() {
        assertNotNull(pacienteAccRepository);
        assertTrue(pacienteAccRepository instanceof PacienteAccRepository);
    }

    @Test
    void testPacienteAccRepositoryType() {
        assertEquals(PacienteAccRepository.class, pacienteAccRepository.getClass());
    }
}
