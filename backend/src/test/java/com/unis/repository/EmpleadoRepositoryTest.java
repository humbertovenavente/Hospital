package com.unis.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for EmpleadoRepository
 */
public class EmpleadoRepositoryTest {

    private EmpleadoRepository empleadoRepository;

    @BeforeEach
    void setUp() {
        empleadoRepository = new EmpleadoRepository();
    }

    @Test
    void testEmpleadoRepositoryInstantiation() {
        assertNotNull(empleadoRepository);
        assertTrue(empleadoRepository instanceof EmpleadoRepository);
    }

    @Test
    void testEmpleadoRepositoryType() {
        assertEquals(EmpleadoRepository.class, empleadoRepository.getClass());
    }
}
