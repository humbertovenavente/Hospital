package com.unis.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for MedicamentoRepository
 */
public class MedicamentoRepositoryTest {

    private MedicamentoRepository medicamentoRepository;

    @BeforeEach
    void setUp() {
        medicamentoRepository = new MedicamentoRepository();
    }

    @Test
    void testMedicamentoRepositoryInstantiation() {
        assertNotNull(medicamentoRepository);
        assertTrue(medicamentoRepository instanceof MedicamentoRepository);
    }

    @Test
    void testMedicamentoRepositoryType() {
        assertEquals(MedicamentoRepository.class, medicamentoRepository.getClass());
    }
}
