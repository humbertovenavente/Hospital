package com.unis.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for PacienteFTRepository
 */
public class PacienteFTRepositoryTest {

    private PacienteFTRepository pacienteFTRepository;

    @BeforeEach
    void setUp() {
        pacienteFTRepository = new PacienteFTRepository();
    }

    @Test
    void testPacienteFTRepositoryInstantiation() {
        assertNotNull(pacienteFTRepository);
        assertTrue(pacienteFTRepository instanceof PacienteFTRepository);
    }

    @Test
    void testPacienteFTRepositoryType() {
        assertEquals(PacienteFTRepository.class, pacienteFTRepository.getClass());
    }
}
