package com.unis.repository;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Modifier;

/**
 * Test class for PageContentRepository
 * Tests basic repository functionality and inheritance
 */
public class PageContentRepositoryTest {

    @Test
    void testPageContentRepositoryClassStructure() {
        // Test that the class exists and can be instantiated
        assertNotNull(PageContentRepository.class);
        
        // Test that it's a public class
        assertTrue(Modifier.isPublic(PageContentRepository.class.getModifiers()));
        
        // Test that it's not abstract
        assertFalse(Modifier.isAbstract(PageContentRepository.class.getModifiers()));
        
        // Test that it's not final
        assertFalse(Modifier.isFinal(PageContentRepository.class.getModifiers()));
        
        // Test package
        assertEquals("com.unis.repository", PageContentRepository.class.getPackageName());
        
        // Test class name
        assertEquals("PageContentRepository", PageContentRepository.class.getSimpleName());
    }

    @Test
    void testPageContentRepositoryInheritance() {
        // Test that it implements PanacheRepository
        assertTrue(PageContentRepository.class.getInterfaces().length > 0);
        
        // Check if it implements PanacheRepository (this is the main interface)
        boolean implementsPanacheRepository = false;
        for (Class<?> interfaceClass : PageContentRepository.class.getInterfaces()) {
            if (interfaceClass.getSimpleName().equals("PanacheRepository")) {
                implementsPanacheRepository = true;
                break;
            }
        }
        assertTrue(implementsPanacheRepository, "PageContentRepository should implement PanacheRepository");
    }

    @Test
    void testPageContentRepositoryMethods() {
        // Test that the class has the expected methods
        assertDoesNotThrow(() -> {
            PageContentRepository.class.getDeclaredMethod("findPublishedByPage", String.class);
            PageContentRepository.class.getDeclaredMethod("findDrafts");
            PageContentRepository.class.getDeclaredMethod("findByStatus", String.class);
            PageContentRepository.class.getDeclaredMethod("findById", Long.class);
        });
    }

    @Test
    void testPageContentRepositoryAnnotations() {
        // Test that the class has the @ApplicationScoped annotation
        assertNotNull(PageContentRepository.class.getAnnotation(jakarta.enterprise.context.ApplicationScoped.class));
    }

    @Test
    void testPageContentRepositoryConstructor() {
        // Test that the class has a default constructor
        assertDoesNotThrow(() -> {
            PageContentRepository.class.getDeclaredConstructor();
        });
    }

    @Test
    void testPageContentRepositoryModifiers() {
        // Test that all public methods are public
        for (java.lang.reflect.Method method : PageContentRepository.class.getDeclaredMethods()) {
            if (method.getName().startsWith("find")) {
                assertTrue(Modifier.isPublic(method.getModifiers()), 
                    "Method " + method.getName() + " should be public");
            }
        }
    }

    @Test
    void testPageContentRepositoryReturnTypes() {
        // Test return types of methods
        try {
            java.lang.reflect.Method findPublishedByPage = PageContentRepository.class.getDeclaredMethod("findPublishedByPage", String.class);
            assertEquals(java.util.List.class, findPublishedByPage.getReturnType());
            
            java.lang.reflect.Method findDrafts = PageContentRepository.class.getDeclaredMethod("findDrafts");
            assertEquals(java.util.List.class, findDrafts.getReturnType());
            
            java.lang.reflect.Method findByStatus = PageContentRepository.class.getDeclaredMethod("findByStatus", String.class);
            assertEquals(java.util.List.class, findByStatus.getReturnType());
            
            java.lang.reflect.Method findById = PageContentRepository.class.getDeclaredMethod("findById", Long.class);
            assertEquals(com.unis.model.PageContent.class, findById.getReturnType());
        } catch (NoSuchMethodException e) {
            fail("Method not found: " + e.getMessage());
        }
    }

    @Test
    void testPageContentRepositoryParameterTypes() {
        // Test parameter types of methods
        try {
            java.lang.reflect.Method findPublishedByPage = PageContentRepository.class.getDeclaredMethod("findPublishedByPage", String.class);
            assertEquals(1, findPublishedByPage.getParameterCount());
            assertEquals(String.class, findPublishedByPage.getParameterTypes()[0]);
            
            java.lang.reflect.Method findDrafts = PageContentRepository.class.getDeclaredMethod("findDrafts");
            assertEquals(0, findDrafts.getParameterCount());
            
            java.lang.reflect.Method findByStatus = PageContentRepository.class.getDeclaredMethod("findByStatus", String.class);
            assertEquals(1, findByStatus.getParameterCount());
            assertEquals(String.class, findByStatus.getParameterTypes()[0]);
            
            java.lang.reflect.Method findById = PageContentRepository.class.getDeclaredMethod("findById", Long.class);
            assertEquals(1, findById.getParameterCount());
            assertEquals(Long.class, findById.getParameterTypes()[0]);
        } catch (NoSuchMethodException e) {
            fail("Method not found: " + e.getMessage());
        }
    }
}
