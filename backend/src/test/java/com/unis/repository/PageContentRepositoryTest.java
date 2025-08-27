package com.unis.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for PageContentRepository
 */
public class PageContentRepositoryTest {

    private PageContentRepository pageContentRepository;

    @BeforeEach
    void setUp() {
        pageContentRepository = new PageContentRepository();
    }

    @Test
    void testPageContentRepositoryInstantiation() {
        assertNotNull(pageContentRepository);
        assertTrue(pageContentRepository instanceof PageContentRepository);
    }

    @Test
    void testPageContentRepositoryType() {
        assertEquals(PageContentRepository.class, pageContentRepository.getClass());
    }
}
