package com.unis.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for DoctorRepository
 */
public class DoctorRepositoryTest {

    private DoctorRepository doctorRepository;

    @BeforeEach
    void setUp() {
        doctorRepository = new DoctorRepository();
    }

    @Test
    void testDoctorRepositoryInstantiation() {
        assertNotNull(doctorRepository);
        assertTrue(doctorRepository instanceof DoctorRepository);
    }

    @Test
    void testDoctorRepositoryType() {
        assertEquals(DoctorRepository.class, doctorRepository.getClass());
    }
}
