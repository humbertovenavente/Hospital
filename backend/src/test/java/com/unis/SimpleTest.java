package com.unis;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SimpleTest {

    @Test
    void testBasico() {
        // Test básico para verificar que la infraestructura funciona
        assertTrue(true);
        assertEquals(2, 1 + 1);
        assertNotNull("test");
    }

    @Test
    void testString() {
        String mensaje = "Hola Mundo";
        assertNotNull(mensaje);
        assertEquals("Hola Mundo", mensaje);
        assertTrue(mensaje.length() > 0);
    }

    @Test
    void testNumeros() {
        int a = 5;
        int b = 3;
        assertEquals(8, a + b);
        assertEquals(2, a - b);
        assertEquals(15, a * b);
    }

    @Test
    void testFalloIntencional() {
        // Test que falla intencionalmente para probar las notificaciones de Drone CI
        System.out.println("Ejecutando test que fallará intencionalmente...");
        assertTrue(false, "Este test falla intencionalmente para probar las notificaciones mejoradas de Drone CI");
    }
}
