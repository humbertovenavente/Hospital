package com.unis.repository;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Modifier;

/**
 * Test class for HistoriaRepository
 * Tests basic repository functionality and inheritance
 */
public class HistoriaRepositoryTest {

    @Test
    void testHistoriaRepositoryClassStructure() {
        // Test that the class exists and can be instantiated
        assertNotNull(HistoriaRepository.class);
        
        // Test that it's a public class
        assertTrue(Modifier.isPublic(HistoriaRepository.class.getModifiers()));
        
        // Test that it's not abstract
        assertFalse(Modifier.isAbstract(HistoriaRepository.class.getModifiers()));
        
        // Test that it's not final
        assertFalse(Modifier.isFinal(HistoriaRepository.class.getModifiers()));
        
        // Test package
        assertEquals("com.unis.repository", HistoriaRepository.class.getPackageName());
        
        // Test class name
        assertEquals("HistoriaRepository", HistoriaRepository.class.getSimpleName());
    }

    @Test
    void testHistoriaRepositoryInheritance() {
        // Test that it implements PanacheRepository
        assertTrue(HistoriaRepository.class.getInterfaces().length > 0);
        
        // Check if it implements PanacheRepository (this is the main interface)
        boolean implementsPanacheRepository = false;
        for (Class<?> interfaceClass : HistoriaRepository.class.getInterfaces()) {
            if (interfaceClass.getSimpleName().equals("PanacheRepository")) {
                implementsPanacheRepository = true;
                break;
            }
        }
        assertTrue(implementsPanacheRepository, "HistoriaRepository should implement PanacheRepository");
    }

    @Test
    void testHistoriaRepositoryMethods() {
        // Test that the class has the expected methods
        assertDoesNotThrow(() -> {
            HistoriaRepository.class.getDeclaredMethod("findByStatus", String.class);
            HistoriaRepository.class.getDeclaredMethod("findById", Long.class);
        });
    }

    @Test
    void testHistoriaRepositoryAnnotations() {
        // Test that the class has the @ApplicationScoped annotation
        assertNotNull(HistoriaRepository.class.getAnnotation(jakarta.enterprise.context.ApplicationScoped.class));
    }

    @Test
    void testHistoriaRepositoryConstructor() {
        // Test that the class has a default constructor
        assertDoesNotThrow(() -> {
            HistoriaRepository.class.getDeclaredConstructor();
        });
    }

    @Test
    void testHistoriaRepositoryModifiers() {
        // Test that all public methods are public
        for (java.lang.reflect.Method method : HistoriaRepository.class.getDeclaredMethods()) {
            if (method.getName().startsWith("find")) {
                assertTrue(Modifier.isPublic(method.getModifiers()), 
                    "Method " + method.getName() + " should be public");
            }
        }
    }

    @Test
    void testHistoriaRepositoryReturnTypes() {
        // Test return types of methods
        assertDoesNotThrow(() -> {
            java.lang.reflect.Method findByStatus = HistoriaRepository.class.getDeclaredMethod("findByStatus", String.class);
            assertEquals(java.util.List.class, findByStatus.getReturnType());
            
            java.lang.reflect.Method findById = HistoriaRepository.class.getDeclaredMethod("findById", Long.class);
            assertEquals(com.unis.model.Historia.class, findById.getReturnType());
        });
    }

    @Test
    void testHistoriaRepositoryParameterTypes() {
        // Test parameter types of methods
        assertDoesNotThrow(() -> {
            java.lang.reflect.Method findByStatus = HistoriaRepository.class.getDeclaredMethod("findByStatus", String.class);
            assertEquals(1, findByStatus.getParameterCount());
            assertEquals(String.class, findByStatus.getParameterTypes()[0]);
            
            java.lang.reflect.Method findById = HistoriaRepository.class.getDeclaredMethod("findById", Long.class);
            assertEquals(1, findById.getParameterCount());
            assertEquals(Long.class, findById.getParameterTypes()[0]);
        });
    }

    @Test
    void testHistoriaRepositoryMethodCount() {
        // Test that the class has the expected number of methods (including inherited Panache methods)
        assertTrue(HistoriaRepository.class.getDeclaredMethods().length >= 2);
    }
}
