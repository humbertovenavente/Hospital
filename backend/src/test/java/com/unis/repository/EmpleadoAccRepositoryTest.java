package com.unis.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for EmpleadoAccRepository
 */
public class EmpleadoAccRepositoryTest {

    private EmpleadoAccRepository empleadoAccRepository;

    @BeforeEach
    void setUp() {
        empleadoAccRepository = new EmpleadoAccRepository();
    }

    @Test
    void testEmpleadoAccRepositoryInstantiation() {
        assertNotNull(empleadoAccRepository);
        assertTrue(empleadoAccRepository instanceof EmpleadoAccRepository);
    }

    @Test
    void testEmpleadoAccRepositoryType() {
        assertEquals(EmpleadoAccRepository.class, empleadoAccRepository.getClass());
    }
}
