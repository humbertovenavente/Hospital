package com.unis.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for FaqRepository
 */
public class FaqRepositoryTest {

    private FaqRepository faqRepository;

    @BeforeEach
    void setUp() {
        faqRepository = new FaqRepository();
    }

    @Test
    void testFaqRepositoryInstantiation() {
        assertNotNull(faqRepository);
        assertTrue(faqRepository instanceof FaqRepository);
    }

    @Test
    void testFaqRepositoryType() {
        assertEquals(FaqRepository.class, faqRepository.getClass());
    }
}
