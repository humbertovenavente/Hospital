package com.unis.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for HistoriaRepository
 */
public class HistoriaRepositoryTest {

    private HistoriaRepository historiaRepository;

    @BeforeEach
    void setUp() {
        historiaRepository = new HistoriaRepository();
    }

    @Test
    void testHistoriaRepositoryInstantiation() {
        assertNotNull(historiaRepository);
        assertTrue(historiaRepository instanceof HistoriaRepository);
    }

    @Test
    void testHistoriaRepositoryType() {
        assertEquals(HistoriaRepository.class, historiaRepository.getClass());
    }
}
