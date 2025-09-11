package com.unis.resource;

import com.unis.dto.RecetaDTO;
import com.unis.model.Receta;
import com.unis.model.RecetaMedicamento;
import com.unis.model.Paciente;
import com.unis.service.RecetaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import jakarta.ws.rs.core.Response;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Test class for RecetaResource.
 * Tests all REST endpoints and business logic.
 */
class RecetaResourceTest {

    @Mock
    private RecetaService recetaService;

    @InjectMocks
    private RecetaResource recetaResource;

    private Receta testReceta;
    private RecetaMedicamento testRecetaMedicamento;
    private Paciente testPaciente;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        testPaciente = new Paciente();
        testPaciente.setIdPaciente(1L);
        testPaciente.setApellido("Juan Pérez");
        
        testReceta = new Receta();
        testReceta.setIdReceta(1L);
        testReceta.setIdCita(1L);
        testReceta.setFechaCreacion(new Date());
        testReceta.setIdPaciente(1L);
        testReceta.setIdDoctor(1L);
        testReceta.setCodigoReceta("REC123");
        testReceta.setAnotaciones("Test anotaciones");
        testReceta.setNotasEspeciales("Test notas especiales");
        
        testRecetaMedicamento = new RecetaMedicamento();
        testRecetaMedicamento.setIdRecetaMedicamento(1L);
        testRecetaMedicamento.setDosis("1 tableta");
        testRecetaMedicamento.setFrecuencia("Cada 8 horas");
    }

    @Test
    void testCrearReceta_Success() {
        // Arrange
        when(recetaService.crearReceta(any(Receta.class))).thenReturn(testReceta);

        // Act
        Response response = recetaResource.crearReceta(testReceta);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(testReceta, response.getEntity());

        // Verify service was called
        verify(recetaService, times(1)).crearReceta(any(Receta.class));
    }

    @Test
    void testCrearReceta_WithException() {
        // Arrange
        when(recetaService.crearReceta(any(Receta.class)))
                .thenThrow(new RuntimeException("Error de base de datos"));

        // Act
        Response response = recetaResource.crearReceta(testReceta);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity().toString().contains("Error al crear la receta"));

        // Verify service was called
        verify(recetaService, times(1)).crearReceta(any(Receta.class));
    }

    @Test
    void testCrearReceta_WithNullReceta() {
        // Arrange
        when(recetaService.crearReceta(null))
                .thenThrow(new RuntimeException("Receta no puede ser null"));

        // Act
        Response response = recetaResource.crearReceta(null);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity().toString().contains("Error al crear la receta"));

        // Verify service was called
        verify(recetaService, times(1)).crearReceta(null);
    }

    @Test
    void testCrearReceta_WithEmptyReceta() {
        // Arrange
        Receta emptyReceta = new Receta();
        when(recetaService.crearReceta(any(Receta.class))).thenReturn(emptyReceta);

        // Act
        Response response = recetaResource.crearReceta(emptyReceta);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(emptyReceta, response.getEntity());

        // Verify service was called
        verify(recetaService, times(1)).crearReceta(any(Receta.class));
    }

    @Test
    void testObtenerRecetaPorIdCita_Success() {
        // Arrange
        when(recetaService.buscarPorIdCita(1)).thenReturn(testReceta);

        // Act
        Response response = recetaResource.obtenerRecetaPorIdCita(1);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(testReceta, response.getEntity());

        // Verify service was called
        verify(recetaService, times(1)).buscarPorIdCita(1);
    }

    @Test
    void testObtenerRecetaPorIdCita_NotFound() {
        // Arrange
        when(recetaService.buscarPorIdCita(999)).thenReturn(null);

        // Act
        Response response = recetaResource.obtenerRecetaPorIdCita(999);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity().toString().contains("Receta no encontrada"));

        // Verify service was called
        verify(recetaService, times(1)).buscarPorIdCita(999);
    }

    @Test
    void testObtenerRecetaPorIdCita_WithZeroId() {
        // Arrange
        when(recetaService.buscarPorIdCita(0)).thenReturn(null);

        // Act
        Response response = recetaResource.obtenerRecetaPorIdCita(0);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity().toString().contains("Receta no encontrada"));

        // Verify service was called
        verify(recetaService, times(1)).buscarPorIdCita(0);
    }

    @Test
    void testObtenerRecetaPorIdCita_WithNegativeId() {
        // Arrange
        when(recetaService.buscarPorIdCita(-1)).thenReturn(null);

        // Act
        Response response = recetaResource.obtenerRecetaPorIdCita(-1);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity().toString().contains("Receta no encontrada"));

        // Verify service was called
        verify(recetaService, times(1)).buscarPorIdCita(-1);
    }

    @Test
    void testActualizarReceta_Success() {
        // Arrange
        when(recetaService.actualizarReceta(anyLong(), any(Receta.class))).thenReturn(testReceta);

        // Act
        Response response = recetaResource.actualizarReceta(1L, testReceta);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(testReceta, response.getEntity());

        // Verify service was called
        verify(recetaService, times(1)).actualizarReceta(1L, testReceta);
    }

    @Test
    void testActualizarReceta_WithException() {
        // Arrange
        when(recetaService.actualizarReceta(anyLong(), any(Receta.class)))
                .thenThrow(new RuntimeException("Error de base de datos"));

        // Act
        Response response = recetaResource.actualizarReceta(1L, testReceta);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity().toString().contains("Error al actualizar la receta"));

        // Verify service was called
        verify(recetaService, times(1)).actualizarReceta(1L, testReceta);
    }

    @Test
    void testActualizarReceta_WithNullReceta() {
        // Arrange
        when(recetaService.actualizarReceta(eq(1L), eq(null)))
                .thenThrow(new RuntimeException("Receta no puede ser null"));

        // Act
        Response response = recetaResource.actualizarReceta(1L, null);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity().toString().contains("Error al actualizar la receta"));

        // Verify service was called
        verify(recetaService, times(1)).actualizarReceta(eq(1L), eq(null));
    }

    @Test
    void testActualizarReceta_WithZeroId() {
        // Arrange
        when(recetaService.actualizarReceta(eq(0L), eq(testReceta)))
                .thenThrow(new RuntimeException("ID inválido"));

        // Act
        Response response = recetaResource.actualizarReceta(0L, testReceta);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity().toString().contains("Error al actualizar la receta"));

        // Verify service was called
        verify(recetaService, times(1)).actualizarReceta(eq(0L), eq(testReceta));
    }

    @Test
    void testActualizarReceta_WithNegativeId() {
        // Arrange
        when(recetaService.actualizarReceta(eq(-1L), eq(testReceta)))
                .thenThrow(new RuntimeException("ID inválido"));

        // Act
        Response response = recetaResource.actualizarReceta(-1L, testReceta);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity().toString().contains("Error al actualizar la receta"));

        // Verify service was called
        verify(recetaService, times(1)).actualizarReceta(eq(-1L), eq(testReceta));
    }

    @Test
    void testAgregarMedicamento_Success() {
        // Arrange
        when(recetaService.agregarMedicamento(any(RecetaMedicamento.class))).thenReturn(testRecetaMedicamento);

        // Act
        Response response = recetaResource.agregarMedicamento(testRecetaMedicamento);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(testRecetaMedicamento, response.getEntity());

        // Verify service was called
        verify(recetaService, times(1)).agregarMedicamento(any(RecetaMedicamento.class));
    }

    @Test
    void testAgregarMedicamento_WithException() {
        // Arrange
        when(recetaService.agregarMedicamento(any(RecetaMedicamento.class)))
                .thenThrow(new RuntimeException("Error de base de datos"));

        // Act
        Response response = recetaResource.agregarMedicamento(testRecetaMedicamento);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity().toString().contains("Error al agregar medicamento"));

        // Verify service was called
        verify(recetaService, times(1)).agregarMedicamento(any(RecetaMedicamento.class));
    }

    @Test
    void testAgregarMedicamento_WithNullMedicamento() {
        // Arrange
        when(recetaService.agregarMedicamento(null))
                .thenThrow(new RuntimeException("Medicamento no puede ser null"));

        // Act
        Response response = recetaResource.agregarMedicamento(null);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity().toString().contains("Error al agregar medicamento"));

        // Verify service was called
        verify(recetaService, times(1)).agregarMedicamento(null);
    }

    @Test
    void testAgregarMedicamento_WithEmptyMedicamento() {
        // Arrange
        RecetaMedicamento emptyMedicamento = new RecetaMedicamento();
        when(recetaService.agregarMedicamento(any(RecetaMedicamento.class)))
                .thenThrow(new RuntimeException("Medicamento incompleto"));

        // Act
        Response response = recetaResource.agregarMedicamento(emptyMedicamento);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity().toString().contains("Error al agregar medicamento"));

        // Verify service was called
        verify(recetaService, times(1)).agregarMedicamento(any(RecetaMedicamento.class));
    }

    @Test
    void testObtenerRecetaPorCodigo_Success() {
        // Arrange
        // Crear una receta con paciente para este test específico
        Receta recetaConPaciente = new Receta();
        recetaConPaciente.setIdReceta(1L);
        recetaConPaciente.setIdCita(1L);
        recetaConPaciente.setFechaCreacion(new Date());
        recetaConPaciente.setIdPaciente(1L);
        recetaConPaciente.setIdDoctor(1L);
        recetaConPaciente.setCodigoReceta("REC123");
        recetaConPaciente.setAnotaciones("Test anotaciones");
        recetaConPaciente.setNotasEspeciales("Test notas especiales");
        
        // Usar reflection para establecer el paciente
        try {
            java.lang.reflect.Field pacienteField = Receta.class.getDeclaredField("paciente");
            pacienteField.setAccessible(true);
            pacienteField.set(recetaConPaciente, testPaciente);
        } catch (Exception e) {
            fail("No se pudo establecer el paciente en la receta");
        }
        
        when(recetaService.buscarPorCodigo("REC123")).thenReturn(recetaConPaciente);

        // Act
        Response response = recetaResource.obtenerRecetaPorCodigo("REC123");

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity() instanceof RecetaDTO);

        // Verify service was called
        verify(recetaService, times(1)).buscarPorCodigo("REC123");
    }

    @Test
    void testObtenerRecetaPorCodigo_NotFound() {
        // Arrange
        when(recetaService.buscarPorCodigo("INVALID")).thenReturn(null);

        // Act
        Response response = recetaResource.obtenerRecetaPorCodigo("INVALID");

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity().toString().contains("Receta no encontrada"));

        // Verify service was called
        verify(recetaService, times(1)).buscarPorCodigo("INVALID");
    }

    @Test
    void testObtenerRecetaPorCodigo_WithEmptyCode() {
        // Arrange
        when(recetaService.buscarPorCodigo("")).thenReturn(null);

        // Act
        Response response = recetaResource.obtenerRecetaPorCodigo("");

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity().toString().contains("Receta no encontrada"));

        // Verify service was called
        verify(recetaService, times(1)).buscarPorCodigo("");
    }

    @Test
    void testObtenerRecetaPorCodigo_WithNullCode() {
        // Arrange
        when(recetaService.buscarPorCodigo(null)).thenReturn(null);

        // Act
        Response response = recetaResource.obtenerRecetaPorCodigo(null);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity().toString().contains("Receta no encontrada"));

        // Verify service was called
        verify(recetaService, times(1)).buscarPorCodigo(null);
    }

    @Test
    void testObtenerRecetaPorCodigo_WithSpecialCharacters() {
        // Arrange
        when(recetaService.buscarPorCodigo("REC@#$%")).thenReturn(null);

        // Act
        Response response = recetaResource.obtenerRecetaPorCodigo("REC@#$%");

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity().toString().contains("Receta no encontrada"));

        // Verify service was called
        verify(recetaService, times(1)).buscarPorCodigo("REC@#$%");
    }

    @Test
    void testObtenerRecetaPorCodigo_WithNumbers() {
        // Arrange
        when(recetaService.buscarPorCodigo("123456")).thenReturn(null);

        // Act
        Response response = recetaResource.obtenerRecetaPorCodigo("123456");

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity().toString().contains("Receta no encontrada"));

        // Verify service was called
        verify(recetaService, times(1)).buscarPorCodigo("123456");
    }

    @Test
    void testObtenerRecetaPorCodigo_WithLongCode() {
        // Arrange
        String longCode = "A".repeat(1000);
        when(recetaService.buscarPorCodigo(longCode)).thenReturn(null);

        // Act
        Response response = recetaResource.obtenerRecetaPorCodigo(longCode);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity().toString().contains("Receta no encontrada"));

        // Verify service was called
        verify(recetaService, times(1)).buscarPorCodigo(longCode);
    }

    @Test
    void testValidarSeguro() {
        // Act
        recetaResource.validarSeguro("Seguro válido");

        // Assert - This method only prints to console, so we just verify it doesn't throw an exception
        assertDoesNotThrow(() -> recetaResource.validarSeguro("Test seguro"));
    }

    @Test
    void testValidarSeguro_WithNull() {
        // Act & Assert
        assertDoesNotThrow(() -> recetaResource.validarSeguro(null));
    }

    @Test
    void testValidarSeguro_WithEmptyString() {
        // Act & Assert
        assertDoesNotThrow(() -> recetaResource.validarSeguro(""));
    }

    @Test
    void testValidarSeguro_WithSpecialCharacters() {
        // Act & Assert
        assertDoesNotThrow(() -> recetaResource.validarSeguro("Seguro@#$%^&*()"));
    }

    @Test
    void testValidarSeguro_WithNumbers() {
        // Act & Assert
        assertDoesNotThrow(() -> recetaResource.validarSeguro("Seguro123"));
    }

    @Test
    void testValidarSeguro_WithUnicode() {
        // Act & Assert
        assertDoesNotThrow(() -> recetaResource.validarSeguro("Seguroñáéíóú"));
    }
}
