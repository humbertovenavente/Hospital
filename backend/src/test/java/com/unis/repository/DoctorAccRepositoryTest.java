package com.unis.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for DoctorAccRepository
 */
public class DoctorAccRepositoryTest {

    private DoctorAccRepository doctorAccRepository;

    @BeforeEach
    void setUp() {
        doctorAccRepository = new DoctorAccRepository();
    }

    @Test
    void testDoctorAccRepositoryInstantiation() {
        assertNotNull(doctorAccRepository);
        assertTrue(doctorAccRepository instanceof DoctorAccRepository);
    }

    @Test
    void testDoctorAccRepositoryType() {
        assertEquals(DoctorAccRepository.class, doctorAccRepository.getClass());
    }
}
