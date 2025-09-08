package com.unis.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for RolRepository
 */
public class RolRepositoryTest {

    private RolRepository rolRepository;

    @BeforeEach
    void setUp() {
        rolRepository = new RolRepository();
    }

    @Test
    void testRolRepositoryInstantiation() {
        assertNotNull(rolRepository);
        assertTrue(rolRepository instanceof RolRepository);
    }

    @Test
    void testRolRepositoryType() {
        assertEquals(RolRepository.class, rolRepository.getClass());
    }
}
