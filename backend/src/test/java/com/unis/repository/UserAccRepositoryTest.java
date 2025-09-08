package com.unis.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for UserAccRepository
 */
public class UserAccRepositoryTest {

    private UserAccRepository userAccRepository;

    @BeforeEach
    void setUp() {
        userAccRepository = new UserAccRepository();
    }

    @Test
    void testUserAccRepositoryInstantiation() {
        assertNotNull(userAccRepository);
        assertTrue(userAccRepository instanceof UserAccRepository);
    }

    @Test
    void testUserAccRepositoryType() {
        assertEquals(UserAccRepository.class, userAccRepository.getClass());
    }
}
