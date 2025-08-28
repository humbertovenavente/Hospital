package com.unis.repository;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Modifier;

/**
 * Test class for FaqRepository
 * Tests basic repository functionality and inheritance
 */
public class FaqRepositoryTest {

    @Test
    void testFaqRepositoryClassStructure() {
        // Test that the class exists and can be instantiated
        assertNotNull(FaqRepository.class);
        
        // Test that it's a public class
        assertTrue(Modifier.isPublic(FaqRepository.class.getModifiers()));
        
        // Test that it's not abstract
        assertFalse(Modifier.isAbstract(FaqRepository.class.getModifiers()));
        
        // Test that it's not final
        assertFalse(Modifier.isFinal(FaqRepository.class.getModifiers()));
        
        // Test package
        assertEquals("com.unis.repository", FaqRepository.class.getPackageName());
        
        // Test class name
        assertEquals("FaqRepository", FaqRepository.class.getSimpleName());
    }

    @Test
    void testFaqRepositoryInheritance() {
        // Test that it implements PanacheRepository
        assertTrue(FaqRepository.class.getInterfaces().length > 0);
        
        // Check if it implements PanacheRepository (this is the main interface)
        boolean implementsPanacheRepository = false;
        for (Class<?> interfaceClass : FaqRepository.class.getInterfaces()) {
            if (interfaceClass.getSimpleName().equals("PanacheRepository")) {
                implementsPanacheRepository = true;
                break;
            }
        }
        assertTrue(implementsPanacheRepository, "FaqRepository should implement PanacheRepository");
    }

    @Test
    void testFaqRepositoryMethods() {
        // Test that the class has the expected methods
        assertDoesNotThrow(() -> {
            FaqRepository.class.getDeclaredMethod("findByStatus", String.class);
            FaqRepository.class.getDeclaredMethod("findById", Long.class);
        });
    }

    @Test
    void testFaqRepositoryAnnotations() {
        // Test that the class has the @ApplicationScoped annotation
        assertNotNull(FaqRepository.class.getAnnotation(jakarta.enterprise.context.ApplicationScoped.class));
    }

    @Test
    void testFaqRepositoryConstructor() {
        // Test that the class has a default constructor
        assertDoesNotThrow(() -> {
            FaqRepository.class.getDeclaredConstructor();
        });
    }

    @Test
    void testFaqRepositoryModifiers() {
        // Test that all public methods are public
        for (java.lang.reflect.Method method : FaqRepository.class.getDeclaredMethods()) {
            if (method.getName().startsWith("find")) {
                assertTrue(Modifier.isPublic(method.getModifiers()), 
                    "Method " + method.getName() + " should be public");
            }
        }
    }

    @Test
    void testFaqRepositoryReturnTypes() {
        // Test return types of methods
        assertDoesNotThrow(() -> {
            java.lang.reflect.Method findByStatus = FaqRepository.class.getDeclaredMethod("findByStatus", String.class);
            assertEquals(java.util.List.class, findByStatus.getReturnType());
            
            java.lang.reflect.Method findById = FaqRepository.class.getDeclaredMethod("findById", Long.class);
            assertEquals(com.unis.model.Faq.class, findById.getReturnType());
        });
    }

    @Test
    void testFaqRepositoryParameterTypes() {
        // Test parameter types of methods
        assertDoesNotThrow(() -> {
            java.lang.reflect.Method findByStatus = FaqRepository.class.getDeclaredMethod("findByStatus", String.class);
            assertEquals(1, findByStatus.getParameterCount());
            assertEquals(String.class, findByStatus.getParameterTypes()[0]);
            
            java.lang.reflect.Method findById = FaqRepository.class.getDeclaredMethod("findById", Long.class);
            assertEquals(1, findById.getParameterCount());
            assertEquals(Long.class, findById.getParameterTypes()[0]);
        });
    }

    @Test
    void testFaqRepositoryMethodCount() {
        // Test that the class has the expected number of methods (including inherited Panache methods)
        assertTrue(FaqRepository.class.getDeclaredMethods().length >= 2);
    }
}
