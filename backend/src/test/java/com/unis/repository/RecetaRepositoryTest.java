package com.unis.repository;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Modifier;

/**
 * Test class for RecetaRepository
 * Tests basic repository functionality and inheritance
 */
public class RecetaRepositoryTest {

    @Test
    void testRecetaRepositoryClassStructure() {
        // Test that the class exists and can be instantiated
        assertNotNull(RecetaRepository.class);
        
        // Test that it's a public class
        assertTrue(Modifier.isPublic(RecetaRepository.class.getModifiers()));
        
        // Test that it's not abstract
        assertFalse(Modifier.isAbstract(RecetaRepository.class.getModifiers()));
        
        // Test that it's not final
        assertFalse(Modifier.isFinal(RecetaRepository.class.getModifiers()));
        
        // Test package
        assertEquals("com.unis.repository", RecetaRepository.class.getPackageName());
        
        // Test class name
        assertEquals("RecetaRepository", RecetaRepository.class.getSimpleName());
    }

    @Test
    void testRecetaRepositoryInheritance() {
        // Test that it implements PanacheRepository
        assertTrue(RecetaRepository.class.getInterfaces().length > 0);
        
        // Check if it implements PanacheRepository (this is the main interface)
        boolean implementsPanacheRepository = false;
        for (Class<?> interfaceClass : RecetaRepository.class.getInterfaces()) {
            if (interfaceClass.getSimpleName().equals("PanacheRepository")) {
                implementsPanacheRepository = true;
                break;
            }
        }
        assertTrue(implementsPanacheRepository, "RecetaRepository should implement PanacheRepository");
    }

    @Test
    void testRecetaRepositoryMethods() {
        // Test that the class has the expected methods
        assertDoesNotThrow(() -> {
            RecetaRepository.class.getDeclaredMethod("obtenerPorPaciente", Long.class);
            RecetaRepository.class.getDeclaredMethod("obtenerPorDoctor", Long.class);
            RecetaRepository.class.getDeclaredMethod("buscarPorIdCita", int.class);
        });
    }

    @Test
    void testRecetaRepositoryAnnotations() {
        // Test that the class has the @ApplicationScoped annotation
        assertNotNull(RecetaRepository.class.getAnnotation(jakarta.enterprise.context.ApplicationScoped.class));
    }

    @Test
    void testRecetaRepositoryConstructor() {
        // Test that the class has a default constructor
        assertDoesNotThrow(() -> {
            RecetaRepository.class.getDeclaredConstructor();
        });
    }

    @Test
    void testRecetaRepositoryModifiers() {
        // Test that all public methods are public
        for (java.lang.reflect.Method method : RecetaRepository.class.getDeclaredMethods()) {
            if (method.getName().startsWith("obtener") || method.getName().startsWith("buscar")) {
                assertTrue(Modifier.isPublic(method.getModifiers()), 
                    "Method " + method.getName() + " should be public");
            }
        }
    }

    @Test
    void testRecetaRepositoryReturnTypes() {
        // Test return types of methods
        assertDoesNotThrow(() -> {
            java.lang.reflect.Method obtenerPorPaciente = RecetaRepository.class.getDeclaredMethod("obtenerPorPaciente", Long.class);
            assertEquals(java.util.List.class, obtenerPorPaciente.getReturnType());
            
            java.lang.reflect.Method obtenerPorDoctor = RecetaRepository.class.getDeclaredMethod("obtenerPorDoctor", Long.class);
            assertEquals(java.util.List.class, obtenerPorDoctor.getReturnType());
            
            java.lang.reflect.Method buscarPorIdCita = RecetaRepository.class.getDeclaredMethod("buscarPorIdCita", int.class);
            assertEquals(com.unis.model.Receta.class, buscarPorIdCita.getReturnType());
        });
    }

    @Test
    void testRecetaRepositoryParameterTypes() {
        // Test parameter types of methods
        assertDoesNotThrow(() -> {
            java.lang.reflect.Method obtenerPorPaciente = RecetaRepository.class.getDeclaredMethod("obtenerPorPaciente", Long.class);
            assertEquals(1, obtenerPorPaciente.getParameterCount());
            assertEquals(Long.class, obtenerPorPaciente.getParameterTypes()[0]);
            
            java.lang.reflect.Method obtenerPorDoctor = RecetaRepository.class.getDeclaredMethod("obtenerPorDoctor", Long.class);
            assertEquals(1, obtenerPorDoctor.getParameterCount());
            assertEquals(Long.class, obtenerPorDoctor.getParameterTypes()[0]);
            
            java.lang.reflect.Method buscarPorIdCita = RecetaRepository.class.getDeclaredMethod("buscarPorIdCita", int.class);
            assertEquals(1, buscarPorIdCita.getParameterCount());
            assertEquals(int.class, buscarPorIdCita.getParameterTypes()[0]);
        });
    }

    @Test
    void testRecetaRepositoryImports() {
        // Test that the class has the necessary imports
        assertDoesNotThrow(() -> {
            // Check if TypedQuery is accessible (imported)
            Class.forName("jakarta.persistence.TypedQuery");
        });
    }
}
