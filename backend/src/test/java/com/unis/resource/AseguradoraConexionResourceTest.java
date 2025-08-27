package com.unis.resource;

import com.unis.model.AseguradoraConexion;
import com.unis.repository.AseguradoraConexionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import jakarta.ws.rs.core.Response;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Test class for AseguradoraConexionResource.
 * Tests all REST endpoints and business logic to achieve 100% coverage.
 */
class AseguradoraConexionResourceTest {

    @Mock
    private AseguradoraConexionRepository repository;

    @InjectMocks
    private AseguradoraConexionResource resource;

    private AseguradoraConexion testConexion;
    private Map<String, String> testBody;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        testConexion = new AseguradoraConexion();
        testConexion.setId(1L);
        testConexion.setNombre("TestAseguradora");
        testConexion.setUrlBase("https://test.aseguradora.com");
        
        testBody = new HashMap<>();
        testBody.put("nombre", "NuevaAseguradora");
        testBody.put("url", "https://nueva.aseguradora.com");
    }

    // ========== GET /api/conexiones-aseguradoras ==========
    
    @Test
    void testGetTodas_Success() {
        // Arrange
        List<AseguradoraConexion> conexiones = Arrays.asList(testConexion);
        when(repository.listAll()).thenReturn(conexiones);

        // Act
        List<AseguradoraConexion> result = resource.getTodas();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testConexion, result.get(0));
        
        // Verify repository was called
        verify(repository, times(1)).listAll();
    }

    @Test
    void testGetTodas_EmptyList() {
        // Arrange
        when(repository.listAll()).thenReturn(new ArrayList<>());

        // Act
        List<AseguradoraConexion> result = resource.getTodas();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        
        // Verify repository was called
        verify(repository, times(1)).listAll();
    }

    @Test
    void testGetTodas_NullList() {
        // Arrange
        when(repository.listAll()).thenReturn(null);

        // Act
        List<AseguradoraConexion> result = resource.getTodas();

        // Assert
        assertNull(result);
        
        // Verify repository was called
        verify(repository, times(1)).listAll();
    }

    // ========== GET /api/conexiones-aseguradoras/url/{nombre} ==========
    
    @Test
    void testGetUrlPorNombre_Success() {
        // Arrange
        when(repository.findByNombre("TestAseguradora")).thenReturn(testConexion);

        // Act
        Response response = resource.getUrlPorNombre("TestAseguradora");

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        
        @SuppressWarnings("unchecked")
        Map<String, String> entity = (Map<String, String>) response.getEntity();
        assertNotNull(entity);
        assertEquals("https://test.aseguradora.com", entity.get("url"));
        
        // Verify repository was called
        verify(repository, times(1)).findByNombre("TestAseguradora");
    }

    @Test
    void testGetUrlPorNombre_NotFound() {
        // Arrange
        when(repository.findByNombre("Inexistente")).thenReturn(null);

        // Act
        Response response = resource.getUrlPorNombre("Inexistente");

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        
        @SuppressWarnings("unchecked")
        Map<String, String> entity = (Map<String, String>) response.getEntity();
        assertNotNull(entity);
        assertEquals("Conexión no encontrada", entity.get("error"));
        
        // Verify repository was called
        verify(repository, times(1)).findByNombre("Inexistente");
    }

    @Test
    void testGetUrlPorNombre_WithNull() {
        // Arrange
        when(repository.findByNombre(null)).thenReturn(null);

        // Act
        Response response = resource.getUrlPorNombre(null);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        
        @SuppressWarnings("unchecked")
        Map<String, String> entity = (Map<String, String>) response.getEntity();
        assertNotNull(entity);
        assertEquals("Conexión no encontrada", entity.get("error"));
        
        // Verify repository was called
        verify(repository, times(1)).findByNombre(null);
    }

    // ========== PUT /api/conexiones-aseguradoras/{id} ==========
    
    @Test
    void testActualizar_Success() {
        // Arrange
        when(repository.findById(1L)).thenReturn(testConexion);
        
        Map<String, String> updateBody = new HashMap<>();
        updateBody.put("nombre", "UpdatedAseguradora");
        updateBody.put("url", "https://updated.aseguradora.com");

        // Act
        Response response = resource.actualizar(1L, updateBody);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        
        @SuppressWarnings("unchecked")
        Map<String, String> entity = (Map<String, String>) response.getEntity();
        assertNotNull(entity);
        assertEquals("Actualizado correctamente", entity.get("message"));
        
        // Verify repository was called
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void testActualizar_NotFound() {
        // Arrange
        when(repository.findById(999L)).thenReturn(null);
        
        Map<String, String> updateBody = new HashMap<>();
        updateBody.put("nombre", "UpdatedAseguradora");

        // Act
        Response response = resource.actualizar(999L, updateBody);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        
        // Verify repository was called
        verify(repository, times(1)).findById(999L);
    }

    @Test
    void testActualizar_WithPartialBody() {
        // Arrange
        when(repository.findById(1L)).thenReturn(testConexion);
        
        Map<String, String> partialBody = new HashMap<>();
        partialBody.put("nombre", "OnlyNameUpdate");

        // Act
        Response response = resource.actualizar(1L, partialBody);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        
        @SuppressWarnings("unchecked")
        Map<String, String> entity = (Map<String, String>) response.getEntity();
        assertNotNull(entity);
        assertEquals("Actualizado correctamente", entity.get("message"));
        
        // Verify repository was called
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void testActualizar_WithEmptyBody() {
        // Arrange
        when(repository.findById(1L)).thenReturn(testConexion);
        
        Map<String, String> emptyBody = new HashMap<>();

        // Act
        Response response = resource.actualizar(1L, emptyBody);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        
        @SuppressWarnings("unchecked")
        Map<String, String> entity = (Map<String, String>) response.getEntity();
        assertNotNull(entity);
        assertEquals("Actualizado correctamente", entity.get("message"));
        
        // Verify repository was called
        verify(repository, times(1)).findById(1L);
    }

    // ========== DELETE /api/conexiones-aseguradoras/{id} ==========
    
    @Test
    void testEliminar_Success() {
        // Arrange
        when(repository.deleteById(1L)).thenReturn(true);

        // Act
        Response response = resource.eliminar(1L);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        
        @SuppressWarnings("unchecked")
        Map<String, String> entity = (Map<String, String>) response.getEntity();
        assertNotNull(entity);
        assertEquals("Eliminado correctamente", entity.get("message"));
        
        // Verify repository was called
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void testEliminar_NotFound() {
        // Arrange
        when(repository.deleteById(999L)).thenReturn(false);

        // Act
        Response response = resource.eliminar(999L);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        
        // Verify repository was called
        verify(repository, times(1)).deleteById(999L);
    }

    // ========== POST /api/conexiones-aseguradoras/registrar ==========
    
    @Test
    void testRegistrarAseguradora_Success() {
        // Arrange
        when(repository.findByNombre("NuevaAseguradora")).thenReturn(null);

        // Act
        Response response = resource.registrarAseguradora(testBody);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        
        @SuppressWarnings("unchecked")
        Map<String, String> entity = (Map<String, String>) response.getEntity();
        assertNotNull(entity);
        assertEquals("Registrada con éxito", entity.get("message"));
        
        // Verify repository was called
        verify(repository, times(1)).findByNombre("NuevaAseguradora");
    }

    @Test
    void testRegistrarAseguradora_MissingNombre() {
        // Arrange
        Map<String, String> invalidBody = new HashMap<>();
        invalidBody.put("url", "https://test.com");

        // Act
        Response response = resource.registrarAseguradora(invalidBody);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals("Nombre y URL requeridos", response.getEntity());
        
        // Verify repository was not called
        verify(repository, never()).findByNombre(any());
    }

    @Test
    void testRegistrarAseguradora_MissingUrl() {
        // Arrange
        Map<String, String> invalidBody = new HashMap<>();
        invalidBody.put("nombre", "TestAseguradora");

        // Act
        Response response = resource.registrarAseguradora(invalidBody);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals("Nombre y URL requeridos", response.getEntity());
        
        // Verify repository was not called
        verify(repository, never()).findByNombre(any());
    }

    @Test
    void testRegistrarAseguradora_BothMissing() {
        // Arrange
        Map<String, String> invalidBody = new HashMap<>();

        // Act
        Response response = resource.registrarAseguradora(invalidBody);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals("Nombre y URL requeridos", response.getEntity());
        
        // Verify repository was not called
        verify(repository, never()).findByNombre(any());
    }

    @Test
    void testRegistrarAseguradora_AlreadyExists() {
        // Arrange
        when(repository.findByNombre("NuevaAseguradora")).thenReturn(testConexion);

        // Act
        Response response = resource.registrarAseguradora(testBody);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.CONFLICT.getStatusCode(), response.getStatus());
        assertEquals("Ya existe", response.getEntity());
        
        // Verify repository was called
        verify(repository, times(1)).findByNombre("NuevaAseguradora");
    }

    @Test
    void testRegistrarAseguradora_WithNullBody() {
        // Act
        Response response = resource.registrarAseguradora(null);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        assertEquals("Error interno al registrar", response.getEntity());
        
        // Verify repository was not called
        verify(repository, never()).findByNombre(any());
    }

    @Test
    void testRegistrarAseguradora_WithNullValues() {
        // Arrange
        Map<String, String> nullBody = new HashMap<>();
        nullBody.put("nombre", null);
        nullBody.put("url", null);

        // Act
        Response response = resource.registrarAseguradora(nullBody);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals("Nombre y URL requeridos", response.getEntity());
        
        // Verify repository was not called
        verify(repository, never()).findByNombre(any());
    }

    @Test
    void testRegistrarAseguradora_Exception() {
        // Arrange
        when(repository.findByNombre("NuevaAseguradora")).thenThrow(new RuntimeException("Database error"));

        // Act
        Response response = resource.registrarAseguradora(testBody);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        assertEquals("Error interno al registrar", response.getEntity());
        
        // Verify repository was called
        verify(repository, times(1)).findByNombre("NuevaAseguradora");
    }
}
