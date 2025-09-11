package com.unis.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for PacienteRepository
 */
public class PacienteRepositoryTest {

    private PacienteRepository pacienteRepository;

    @BeforeEach
    void setUp() {
        pacienteRepository = new PacienteRepository();
    }

    @Test
    void testPacienteRepositoryInstantiation() {
        assertNotNull(pacienteRepository);
        assertTrue(pacienteRepository instanceof PacienteRepository);
    }

    @Test
    void testPacienteRepositoryType() {
        assertEquals(PacienteRepository.class, pacienteRepository.getClass());
    }
}
