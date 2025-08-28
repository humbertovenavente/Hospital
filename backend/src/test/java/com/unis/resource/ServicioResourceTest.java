package com.unis.resource;

import com.unis.model.Servicio;
import com.unis.service.ServicioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import jakarta.ws.rs.core.Response;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * Test class for ServicioResource.
 * Tests all REST endpoints and business logic.
 */
class ServicioResourceTest {

    @Mock
    private ServicioService servicioService;

    @InjectMocks
    private ServicioResource servicioResource;

    private Servicio testServicio;
    private List<Servicio> testServicios;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        testServicio = new Servicio();
        testServicio.id = 1L;
        testServicio.nombre = "Servicio Test";
        testServicio.costo = 100.0;
        testServicio.cubiertoSeguro = true;
        
        testServicios = Arrays.asList(testServicio);
    }

    @Test
    void testListarServicios_Success() {
        // Arrange
        when(servicioService.listarTodos()).thenReturn(testServicios);

        // Act
        List<Servicio> result = servicioResource.listarServicios();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testServicio, result.get(0));

        // Verify service was called
        verify(servicioService, times(1)).listarTodos();
    }

    @Test
    void testListarServicios_EmptyList() {
        // Arrange
        when(servicioService.listarTodos()).thenReturn(Arrays.asList());

        // Act
        List<Servicio> result = servicioResource.listarServicios();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

        // Verify service was called
        verify(servicioService, times(1)).listarTodos();
    }

    @Test
    void testListarServicios_MultipleServices() {
        // Arrange
        Servicio servicio2 = new Servicio();
        servicio2.id = 2L;
        servicio2.nombre = "Servicio 2";
        
        List<Servicio> multipleServicios = Arrays.asList(testServicio, servicio2);
        when(servicioService.listarTodos()).thenReturn(multipleServicios);

        // Act
        List<Servicio> result = servicioResource.listarServicios();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(testServicio, result.get(0));
        assertEquals(servicio2, result.get(1));

        // Verify service was called
        verify(servicioService, times(1)).listarTodos();
    }

    @Test
    void testListarSubServicios_Success() {
        // Arrange
        when(servicioService.listarSubServicios(1L)).thenReturn(testServicios);

        // Act
        List<Servicio> result = servicioResource.listarSubServicios(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testServicio, result.get(0));

        // Verify service was called
        verify(servicioService, times(1)).listarSubServicios(1L);
    }

    @Test
    void testListarSubServicios_EmptyList() {
        // Arrange
        when(servicioService.listarSubServicios(1L)).thenReturn(Arrays.asList());

        // Act
        List<Servicio> result = servicioResource.listarSubServicios(1L);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

        // Verify service was called
        verify(servicioService, times(1)).listarSubServicios(1L);
    }

    @Test
    void testListarSubServicios_WithZeroId() {
        // Arrange
        when(servicioService.listarSubServicios(0L)).thenReturn(Arrays.asList());

        // Act
        List<Servicio> result = servicioResource.listarSubServicios(0L);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

        // Verify service was called
        verify(servicioService, times(1)).listarSubServicios(0L);
    }

    @Test
    void testListarSubServicios_WithNegativeId() {
        // Arrange
        when(servicioService.listarSubServicios(-1L)).thenReturn(Arrays.asList());

        // Act
        List<Servicio> result = servicioResource.listarSubServicios(-1L);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

        // Verify service was called
        verify(servicioService, times(1)).listarSubServicios(-1L);
    }

    @Test
    void testAgregarServicio_Success() {
        // Arrange
        // Asegurarnos de que testServicio no tenga parentId para que pase las validaciones
        testServicio.servicioPadre = null;
        when(servicioService.agregarServicio(any(Servicio.class), any())).thenReturn(testServicio);

        // Act
        Response response = servicioResource.agregarServicio(testServicio);

        // Verify service was called
        verify(servicioService, times(1)).agregarServicio(any(Servicio.class), any());

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertNotNull(response.getEntity());
        assertTrue(response.getEntity() instanceof Servicio);

        // Verify service was called
        verify(servicioService, times(1)).agregarServicio(any(Servicio.class), any());
    }

    @Test
    void testAgregarServicio_WithNullServicio() {
        // Act
        Response response = servicioResource.agregarServicio(null);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity().toString().contains("El nombre del servicio es obligatorio"));

        // Verify service was not called
        verify(servicioService, never()).agregarServicio(any(Servicio.class), anyLong());
    }

    @Test
    void testAgregarServicio_WithEmptyNombre() {
        // Arrange
        Servicio emptyServicio = new Servicio();
        emptyServicio.nombre = "";

        // Act
        Response response = servicioResource.agregarServicio(emptyServicio);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity().toString().contains("El nombre del servicio es obligatorio"));

        // Verify service was not called
        verify(servicioService, never()).agregarServicio(any(Servicio.class), anyLong());
    }

    @Test
    void testAgregarServicio_WithWhitespaceNombre() {
        // Arrange
        Servicio whitespaceServicio = new Servicio();
        whitespaceServicio.nombre = "   ";

        // Act
        Response response = servicioResource.agregarServicio(whitespaceServicio);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity().toString().contains("El nombre del servicio es obligatorio"));

        // Verify service was not called
        verify(servicioService, never()).agregarServicio(any(Servicio.class), anyLong());
    }

    @Test
    void testAgregarServicio_WithNullNombre() {
        // Arrange
        Servicio nullNombreServicio = new Servicio();
        nullNombreServicio.nombre = null;

        // Act
        Response response = servicioResource.agregarServicio(nullNombreServicio);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity().toString().contains("El nombre del servicio es obligatorio"));

        // Verify service was not called
        verify(servicioService, never()).agregarServicio(any(Servicio.class), anyLong());
    }

    @Test
    void testAgregarServicio_WithParentId_Success() {
        // Arrange
        Servicio parentServicio = new Servicio();
        parentServicio.id = 999L;
        parentServicio.nombre = "Servicio Padre";
        
        testServicio.servicioPadre = parentServicio;
        when(servicioService.buscarPorId(999L)).thenReturn(parentServicio);
        when(servicioService.agregarServicio(any(Servicio.class), anyLong())).thenReturn(testServicio);

        // Act
        Response response = servicioResource.agregarServicio(testServicio);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertNotNull(response.getEntity());
        assertTrue(response.getEntity() instanceof Servicio);

        // Verify service was called
        verify(servicioService, times(1)).buscarPorId(999L);
        verify(servicioService, times(1)).agregarServicio(any(Servicio.class), any());
    }

    @Test
    void testAgregarServicio_WithInvalidParentId() {
        // Arrange
        // Configuramos un servicio padre inválido
        Servicio servicioPadre = new Servicio();
        servicioPadre.id = 999L;
        testServicio.servicioPadre = servicioPadre;
        when(servicioService.buscarPorId(999L)).thenReturn(null);

        // Act
        Response response = servicioResource.agregarServicio(testServicio);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity().toString().contains("El servicio padre no existe"));

        // Verify service was called
        verify(servicioService, times(1)).buscarPorId(999L);
        verify(servicioService, never()).agregarServicio(any(Servicio.class), anyLong());
    }

    @Test
    void testAgregarServicio_WithException() {
        // Arrange
        // Configuramos un servicio padre válido para que pase las validaciones
        Servicio servicioPadre = new Servicio();
        servicioPadre.id = 999L;
        testServicio.servicioPadre = servicioPadre;
        when(servicioService.buscarPorId(999L)).thenReturn(servicioPadre);
        // Luego configuramos para que lance excepción cuando se llame al servicio
        when(servicioService.agregarServicio(any(Servicio.class), any()))
                .thenThrow(new RuntimeException("Error de base de datos"));

        // Act
        Response response = servicioResource.agregarServicio(testServicio);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity().toString().contains("Error al agregar servicio"));

        // Verify service was called
        verify(servicioService, times(1)).agregarServicio(any(Servicio.class), any());
    }

    @Test
    void testAgregarSubServicio_Success() {
        // Arrange
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("subServicioId", 123L);
        doNothing().when(servicioService).agregarSubServicio(1L, 123L);

        // Act
        Response response = servicioResource.agregarSubServicio(1L, requestBody);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity().toString().contains("Subservicio agregado correctamente"));

        // Verify service was called
        verify(servicioService, times(1)).agregarSubServicio(1L, 123L);
    }

    @Test
    void testAgregarSubServicio_MissingSubServicioId() {
        // Arrange
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("otherField", "value");

        // Act
        Response response = servicioResource.agregarSubServicio(1L, requestBody);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity().toString().contains("El JSON debe contener 'subServicioId'"));

        // Verify service was not called
        verify(servicioService, never()).agregarSubServicio(anyLong(), anyLong());
    }

    @Test
    void testAgregarSubServicio_InvalidSubServicioIdType() {
        // Arrange
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("subServicioId", "invalid");

        // Act
        Response response = servicioResource.agregarSubServicio(1L, requestBody);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity().toString().contains("subServicioId debe ser un número válido"));

        // Verify service was not called
        verify(servicioService, never()).agregarSubServicio(anyLong(), anyLong());
    }

    @Test
    void testAgregarSubServicio_WithException() {
        // Arrange
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("subServicioId", 123L);
        doThrow(new RuntimeException("Error de base de datos"))
                .when(servicioService).agregarSubServicio(1L, 123L);

        // Act
        Response response = servicioResource.agregarSubServicio(1L, requestBody);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity().toString().contains("Error al agregar subservicio"));

        // Verify service was called
        verify(servicioService, times(1)).agregarSubServicio(1L, 123L);
    }

    @Test
    void testAgregarSubServicio_WithZeroId() {
        // Arrange
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("subServicioId", 123L);
        doNothing().when(servicioService).agregarSubServicio(0L, 123L);

        // Act
        Response response = servicioResource.agregarSubServicio(0L, requestBody);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        // Verify service was called
        verify(servicioService, times(1)).agregarSubServicio(0L, 123L);
    }

    @Test
    void testAgregarSubServicio_WithNegativeId() {
        // Arrange
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("subServicioId", 123L);
        doNothing().when(servicioService).agregarSubServicio(-1L, 123L);

        // Act
        Response response = servicioResource.agregarSubServicio(-1L, requestBody);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        // Verify service was called
        verify(servicioService, times(1)).agregarSubServicio(-1L, 123L);
    }

    @Test
    void testEliminarServicio_Success() {
        // Arrange
        doNothing().when(servicioService).eliminarServicio(1L);

        // Act
        Response response = servicioResource.eliminarServicio(1L);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        // Verify service was called
        verify(servicioService, times(1)).eliminarServicio(1L);
    }

    @Test
    void testEliminarServicio_WithException() {
        // Arrange
        doThrow(new RuntimeException("Error de base de datos"))
                .when(servicioService).eliminarServicio(1L);

        // Act
        Response response = servicioResource.eliminarServicio(1L);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity().toString().contains("Error al eliminar el servicio"));

        // Verify service was called
        verify(servicioService, times(1)).eliminarServicio(1L);
    }

    @Test
    void testEliminarServicio_WithZeroId() {
        // Arrange
        doNothing().when(servicioService).eliminarServicio(0L);

        // Act
        Response response = servicioResource.eliminarServicio(0L);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        // Verify service was called
        verify(servicioService, times(1)).eliminarServicio(0L);
    }

    @Test
    void testEliminarServicio_WithNegativeId() {
        // Arrange
        doNothing().when(servicioService).eliminarServicio(-1L);

        // Act
        Response response = servicioResource.eliminarServicio(-1L);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        // Verify service was called
        verify(servicioService, times(1)).eliminarServicio(-1L);
    }

    @Test
    void testEliminarRelacion_Success() {
        // Arrange
        when(servicioService.eliminarRelacion(1L, 123L)).thenReturn(true);

        // Act
        Response response = servicioResource.eliminarRelacion(1L, 123L);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity().toString().contains("Relación eliminada correctamente"));

        // Verify service was called
        verify(servicioService, times(1)).eliminarRelacion(1L, 123L);
    }

    @Test
    void testEliminarRelacion_NotFound() {
        // Arrange
        when(servicioService.eliminarRelacion(1L, 123L)).thenReturn(false);

        // Act
        Response response = servicioResource.eliminarRelacion(1L, 123L);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity().toString().contains("La relación no existe o ya fue eliminada"));

        // Verify service was called
        verify(servicioService, times(1)).eliminarRelacion(1L, 123L);
    }

    @Test
    void testEliminarRelacion_WithException() {
        // Arrange
        when(servicioService.eliminarRelacion(1L, 123L))
                .thenThrow(new RuntimeException("Error de base de datos"));

        // Act
        Response response = servicioResource.eliminarRelacion(1L, 123L);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity().toString().contains("Error al eliminar relación"));

        // Verify service was called
        verify(servicioService, times(1)).eliminarRelacion(1L, 123L);
    }

    @Test
    void testEliminarRelacion_WithZeroIds() {
        // Arrange
        when(servicioService.eliminarRelacion(0L, 0L)).thenReturn(true);

        // Act
        Response response = servicioResource.eliminarRelacion(0L, 0L);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        // Verify service was called
        verify(servicioService, times(1)).eliminarRelacion(0L, 0L);
    }

    @Test
    void testEliminarRelacion_WithNegativeIds() {
        // Arrange
        when(servicioService.eliminarRelacion(-1L, -1L)).thenReturn(true);

        // Act
        Response response = servicioResource.eliminarRelacion(-1L, -1L);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        // Verify service was called
        verify(servicioService, times(1)).eliminarRelacion(-1L, -1L);
    }

    @Test
    void testEliminarRelacion_WithMaximumIds() {
        // Arrange
        when(servicioService.eliminarRelacion(Long.MAX_VALUE, Long.MAX_VALUE)).thenReturn(true);

        // Act
        Response response = servicioResource.eliminarRelacion(Long.MAX_VALUE, Long.MAX_VALUE);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        // Verify service was called
        verify(servicioService, times(1)).eliminarRelacion(Long.MAX_VALUE, Long.MAX_VALUE);
    }

    @Test
    void testEliminarRelacion_WithMinimumIds() {
        // Arrange
        when(servicioService.eliminarRelacion(Long.MIN_VALUE, Long.MIN_VALUE)).thenReturn(true);

        // Act
        Response response = servicioResource.eliminarRelacion(Long.MIN_VALUE, Long.MIN_VALUE);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        // Verify service was called
        verify(servicioService, times(1)).eliminarRelacion(Long.MIN_VALUE, Long.MIN_VALUE);
    }
}
