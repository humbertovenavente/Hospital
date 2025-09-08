package com.unis.resource;

import com.unis.model.FichaTecnica;
import com.unis.service.FichaTecnicaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for FichaTecnicaResource
 */
public class FichaTecnicaResourceTest {

    @Mock
    private FichaTecnicaService fichaTecnicaService;

    @InjectMocks
    private FichaTecnicaResource fichaTecnicaResource;

    private FichaTecnica testFicha;
    private List<FichaTecnica> testFichas;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        testFicha = new FichaTecnica();
        testFicha.setIdFicha(1L);
        testFicha.setHistorialServicios("Historial de prueba");
        testFicha.setNumeroAfiliacion("12345");
        
        testFichas = Arrays.asList(testFicha);
    }

    @Test
    void testFichaTecnicaResourceInstantiation() {
        assertNotNull(fichaTecnicaResource);
        assertTrue(fichaTecnicaResource instanceof FichaTecnicaResource);
    }

    @Test
    void testObtenerTodasLasFichas_Success() {
        // Arrange
        when(fichaTecnicaService.getAllFichas()).thenReturn(testFichas);

        // Act
        List<FichaTecnica> result = fichaTecnicaResource.obtenerTodasLasFichas();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testFicha, result.get(0));
        verify(fichaTecnicaService).getAllFichas();
    }

    @Test
    void testObtenerTodasLasFichas_EmptyList() {
        // Arrange
        when(fichaTecnicaService.getAllFichas()).thenReturn(Arrays.asList());

        // Act
        List<FichaTecnica> result = fichaTecnicaResource.obtenerTodasLasFichas();

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
        assertTrue(result.isEmpty());
        verify(fichaTecnicaService).getAllFichas();
    }

    @Test
    void testObtenerTodasLasFichas_MultipleFichas() {
        // Arrange
        FichaTecnica ficha2 = new FichaTecnica();
        ficha2.setIdFicha(2L);
        ficha2.setHistorialServicios("Historial de prueba 2");
        
        List<FichaTecnica> multipleFichas = Arrays.asList(testFicha, ficha2);
        when(fichaTecnicaService.getAllFichas()).thenReturn(multipleFichas);

        // Act
        List<FichaTecnica> result = fichaTecnicaResource.obtenerTodasLasFichas();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(testFicha, result.get(0));
        assertEquals(ficha2, result.get(1));
        verify(fichaTecnicaService).getAllFichas();
    }

    @Test
    void testObtenerTodasLasFichas_ServiceReturnsNull() {
        // Arrange
        when(fichaTecnicaService.getAllFichas()).thenReturn(null);

        // Act
        List<FichaTecnica> result = fichaTecnicaResource.obtenerTodasLasFichas();

        // Assert
        assertNull(result);
        verify(fichaTecnicaService).getAllFichas();
    }

    @Test
    void testRegistrarFicha_Success() {
        // Arrange
        doNothing().when(fichaTecnicaService).registrarFicha(any(FichaTecnica.class));

        // Act
        assertDoesNotThrow(() -> fichaTecnicaResource.registrarFicha(testFicha));

        // Assert
        verify(fichaTecnicaService).registrarFicha(testFicha);
    }

    @Test
    void testRegistrarFicha_WithNullFicha() {
        // Arrange
        doNothing().when(fichaTecnicaService).registrarFicha(any(FichaTecnica.class));

        // Act
        assertDoesNotThrow(() -> fichaTecnicaResource.registrarFicha(null));

        // Assert
        verify(fichaTecnicaService).registrarFicha(null);
    }

    @Test
    void testRegistrarFicha_WithEmptyFicha() {
        // Arrange
        FichaTecnica emptyFicha = new FichaTecnica();
        doNothing().when(fichaTecnicaService).registrarFicha(any(FichaTecnica.class));

        // Act
        assertDoesNotThrow(() -> fichaTecnicaResource.registrarFicha(emptyFicha));

        // Assert
        verify(fichaTecnicaService).registrarFicha(emptyFicha);
    }

    @Test
    void testRegistrarFicha_ServiceThrowsException() {
        // Arrange
        doThrow(new RuntimeException("Service error")).when(fichaTecnicaService).registrarFicha(any(FichaTecnica.class));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> fichaTecnicaResource.registrarFicha(testFicha));
        verify(fichaTecnicaService).registrarFicha(testFicha);
    }

    @Test
    void testFichaTecnicaResourceAnnotations() {
        // Test that the class has the expected annotations
        assertNotNull(FichaTecnicaResource.class.getAnnotation(jakarta.ws.rs.Path.class));
        assertNotNull(FichaTecnicaResource.class.getAnnotation(jakarta.ws.rs.Produces.class));
        assertNotNull(FichaTecnicaResource.class.getAnnotation(jakarta.ws.rs.Consumes.class));
        
        // Test Path annotation value
        jakarta.ws.rs.Path pathAnnotation = FichaTecnicaResource.class.getAnnotation(jakarta.ws.rs.Path.class);
        assertEquals("/fichas-tecnicas", pathAnnotation.value());
        
        // Test Produces annotation value
        jakarta.ws.rs.Produces producesAnnotation = FichaTecnicaResource.class.getAnnotation(jakarta.ws.rs.Produces.class);
        assertEquals(1, producesAnnotation.value().length);
        assertEquals("application/json", producesAnnotation.value()[0]);
        
        // Test Consumes annotation value
        jakarta.ws.rs.Consumes consumesAnnotation = FichaTecnicaResource.class.getAnnotation(jakarta.ws.rs.Consumes.class);
        assertEquals(1, consumesAnnotation.value().length);
        assertEquals("application/json", consumesAnnotation.value()[0]);
    }

    @Test
    void testFichaTecnicaResourceMethods() {
        // Test that the class has the expected methods
        assertDoesNotThrow(() -> {
            FichaTecnicaResource.class.getDeclaredMethod("obtenerTodasLasFichas");
            FichaTecnicaResource.class.getDeclaredMethod("registrarFicha", FichaTecnica.class);
        });
    }

    @Test
    void testFichaTecnicaResourceDependencies() {
        // Test that the service dependency is properly injected
        assertNotNull(fichaTecnicaResource.fichaTecnicaService);
        assertEquals(fichaTecnicaService, fichaTecnicaResource.fichaTecnicaService);
    }

    @Test
    void testFichaTecnicaResourceMethodModifiers() {
        // Test that public methods are public
        assertDoesNotThrow(() -> {
            java.lang.reflect.Method obtenerTodasLasFichas = FichaTecnicaResource.class.getDeclaredMethod("obtenerTodasLasFichas");
            assertTrue(java.lang.reflect.Modifier.isPublic(obtenerTodasLasFichas.getModifiers()));
            
            java.lang.reflect.Method registrarFicha = FichaTecnicaResource.class.getDeclaredMethod("registrarFicha", FichaTecnica.class);
            assertTrue(java.lang.reflect.Modifier.isPublic(registrarFicha.getModifiers()));
        });
    }

    @Test
    void testFichaTecnicaResourceMethodAnnotations() {
        // Test that methods have the expected annotations
        assertDoesNotThrow(() -> {
            java.lang.reflect.Method obtenerTodasLasFichas = FichaTecnicaResource.class.getDeclaredMethod("obtenerTodasLasFichas");
            assertNotNull(obtenerTodasLasFichas.getAnnotation(jakarta.ws.rs.GET.class));
            
            java.lang.reflect.Method registrarFicha = FichaTecnicaResource.class.getDeclaredMethod("registrarFicha", FichaTecnica.class);
            assertNotNull(registrarFicha.getAnnotation(jakarta.ws.rs.POST.class));
        });
    }
}
