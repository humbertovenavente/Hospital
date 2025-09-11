package com.unis.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for AgendaRepository
 */
public class AgendaRepositoryTest {

    private AgendaRepository agendaRepository;

    @BeforeEach
    void setUp() {
        agendaRepository = new AgendaRepository();
    }

    @Test
    void testAgendaRepositoryInstantiation() {
        assertNotNull(agendaRepository);
        assertTrue(agendaRepository instanceof AgendaRepository);
    }

    @Test
    void testAgendaRepositoryType() {
        assertEquals(AgendaRepository.class, agendaRepository.getClass());
    }
}
