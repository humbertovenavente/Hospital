package com.unis.resource;

import com.unis.dto.ReporteAgregadoDTO;
import com.unis.dto.ReporteDetalladoDTO;
import com.unis.dto.ReporteRequest;
import com.unis.dto.ReporteResponse;
import com.unis.model.Doctor;
import com.unis.model.Usuario;
import com.unis.service.DoctorService;
import com.unis.service.ReporteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import jakarta.ws.rs.core.Response;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * Test class for ReporteResource.
 * Tests all REST endpoints and business logic.
 */
class ReporteResourceTest {

    @Mock
    private ReporteService reporteService;

    @Mock
    private DoctorService doctorService;

    @InjectMocks
    private ReporteResource reporteResource;

    private ReporteRequest testRequest;
    private Doctor testDoctor;
    private Usuario testUsuario;
    private List<ReporteAgregadoDTO> testReporteAgregado;
    private List<ReporteDetalladoDTO> testReporteDetallado;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        testRequest = new ReporteRequest();
        testRequest.setIdDoctor(1L);
        testRequest.setFechaInicio(LocalDate.of(2024, 1, 1));
        testRequest.setFechaFin(LocalDate.of(2024, 1, 31));
        testRequest.setTipoReporte("AGRUPADO");
        testRequest.setUsuario("testuser");
        
        testUsuario = new Usuario();
        testUsuario.setId(1L);
        testUsuario.setNombreUsuario("Dr. Juan Pérez");
        
        testDoctor = new Doctor();
        testDoctor.setIdDoctor(1L);
        testDoctor.setApellido("Dr. Juan Pérez");
        testDoctor.setUsuario(testUsuario);
        
        testReporteAgregado = Arrays.asList(new ReporteAgregadoDTO(LocalDate.of(2024, 1, 1), 10L, 6L, 4L));
        testReporteDetallado = Arrays.asList(new ReporteDetalladoDTO(LocalDate.of(2024, 1, 1), "09:00", "Juan Pérez", "Seguro"));
    }

    @Test
    void testGenerarReporte_Agrupado_Success() {
        // Arrange
        when(doctorService.getDoctorById(1L)).thenReturn(Optional.of(testDoctor));
        when(reporteService.obtenerReporteAgregado(anyLong(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(testReporteAgregado);

        // Act
        Response response = reporteResource.generarReporte(testRequest);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity() instanceof ReporteResponse);

        // Verify service was called
        verify(doctorService, times(1)).getDoctorById(1L);
        verify(reporteService, times(1)).obtenerReporteAgregado(1L, testRequest.getFechaInicio(), testRequest.getFechaFin());
    }

    @Test
    void testGenerarReporte_Detallado_Success() {
        // Arrange
        testRequest.setTipoReporte("DETALLADO");
        when(doctorService.getDoctorById(1L)).thenReturn(Optional.of(testDoctor));
        when(reporteService.obtenerReporteDetallado(anyLong(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(testReporteDetallado);

        // Act
        Response response = reporteResource.generarReporte(testRequest);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity() instanceof ReporteResponse);

        // Verify service was called
        verify(doctorService, times(1)).getDoctorById(1L);
        verify(reporteService, times(1)).obtenerReporteDetallado(1L, testRequest.getFechaInicio(), testRequest.getFechaFin());
    }

    @Test
    void testGenerarReporte_WithNullIdDoctor() {
        // Arrange
        testRequest.setIdDoctor(null);

        // Act
        Response response = reporteResource.generarReporte(testRequest);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals("Parámetros inválidos", response.getEntity());

        // Verify service was not called
        verify(doctorService, never()).getDoctorById(anyLong());
        verify(reporteService, never()).obtenerReporteAgregado(anyLong(), any(LocalDate.class), any(LocalDate.class));
    }

    @Test
    void testGenerarReporte_WithNullFechaInicio() {
        // Arrange
        testRequest.setFechaInicio(null);

        // Act
        Response response = reporteResource.generarReporte(testRequest);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals("Parámetros inválidos", response.getEntity());

        // Verify service was not called
        verify(doctorService, never()).getDoctorById(anyLong());
        verify(reporteService, never()).obtenerReporteAgregado(anyLong(), any(LocalDate.class), any(LocalDate.class));
    }

    @Test
    void testGenerarReporte_WithNullFechaFin() {
        // Arrange
        testRequest.setFechaFin(null);

        // Act
        Response response = reporteResource.generarReporte(testRequest);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals("Parámetros inválidos", response.getEntity());

        // Verify service was not called
        verify(doctorService, never()).getDoctorById(anyLong());
        verify(reporteService, never()).obtenerReporteAgregado(anyLong(), any(LocalDate.class), any(LocalDate.class));
    }

    @Test
    void testGenerarReporte_WithInvalidDateRange() {
        // Arrange
        testRequest.setFechaInicio(LocalDate.of(2024, 1, 31));
        testRequest.setFechaFin(LocalDate.of(2024, 1, 1));

        // Act
        Response response = reporteResource.generarReporte(testRequest);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals("Parámetros inválidos", response.getEntity());

        // Verify service was not called
        verify(doctorService, never()).getDoctorById(anyLong());
        verify(reporteService, never()).obtenerReporteAgregado(anyLong(), any(LocalDate.class), any(LocalDate.class));
    }

    @Test
    void testGenerarReporte_WithNullUsuario() {
        // Arrange
        testRequest.setUsuario(null);
        when(doctorService.getDoctorById(1L)).thenReturn(Optional.of(testDoctor));
        when(reporteService.obtenerReporteAgregado(anyLong(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(testReporteAgregado);

        // Act
        Response response = reporteResource.generarReporte(testRequest);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity() instanceof ReporteResponse);

        // Verify service was called
        verify(doctorService, times(1)).getDoctorById(1L);
        verify(reporteService, times(1)).obtenerReporteAgregado(1L, testRequest.getFechaInicio(), testRequest.getFechaFin());
    }

    @Test
    void testGenerarReporte_WithEmptyUsuario() {
        // Arrange
        testRequest.setUsuario("");
        when(doctorService.getDoctorById(1L)).thenReturn(Optional.of(testDoctor));
        when(reporteService.obtenerReporteAgregado(anyLong(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(testReporteAgregado);

        // Act
        Response response = reporteResource.generarReporte(testRequest);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity() instanceof ReporteResponse);

        // Verify service was called
        verify(doctorService, times(1)).getDoctorById(1L);
        verify(reporteService, times(1)).obtenerReporteAgregado(1L, testRequest.getFechaInicio(), testRequest.getFechaFin());
    }

    @Test
    void testGenerarReporte_WithWhitespaceUsuario() {
        // Arrange
        testRequest.setUsuario("   ");
        when(doctorService.getDoctorById(1L)).thenReturn(Optional.of(testDoctor));
        when(reporteService.obtenerReporteAgregado(anyLong(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(testReporteAgregado);

        // Act
        Response response = reporteResource.generarReporte(testRequest);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity() instanceof ReporteResponse);

        // Verify service was called
        verify(doctorService, times(1)).getDoctorById(1L);
        verify(reporteService, times(1)).obtenerReporteAgregado(1L, testRequest.getFechaInicio(), testRequest.getFechaFin());
    }

    @Test
    void testGenerarReporte_DoctorNotFound() {
        // Arrange
        when(doctorService.getDoctorById(1L)).thenReturn(Optional.empty());
        when(reporteService.obtenerReporteAgregado(anyLong(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(testReporteAgregado);

        // Act
        Response response = reporteResource.generarReporte(testRequest);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity() instanceof ReporteResponse);

        // Verify service was called
        verify(doctorService, times(1)).getDoctorById(1L);
        verify(reporteService, times(1)).obtenerReporteAgregado(1L, testRequest.getFechaInicio(), testRequest.getFechaFin());
    }

    @Test
    void testGenerarReporte_DoctorWithoutUsuario() {
        // Arrange
        testDoctor.setUsuario(null);
        when(doctorService.getDoctorById(1L)).thenReturn(Optional.of(testDoctor));
        when(reporteService.obtenerReporteAgregado(anyLong(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(testReporteAgregado);

        // Act
        Response response = reporteResource.generarReporte(testRequest);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity() instanceof ReporteResponse);

        // Verify service was called
        verify(doctorService, times(1)).getDoctorById(1L);
        verify(reporteService, times(1)).obtenerReporteAgregado(1L, testRequest.getFechaInicio(), testRequest.getFechaFin());
    }

    @Test
    void testGenerarReporte_DoctorWithoutNombreUsuario() {
        // Arrange
        testUsuario.setNombreUsuario(null);
        when(doctorService.getDoctorById(1L)).thenReturn(Optional.of(testDoctor));
        when(reporteService.obtenerReporteAgregado(anyLong(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(testReporteAgregado);

        // Act
        Response response = reporteResource.generarReporte(testRequest);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity() instanceof ReporteResponse);

        // Verify service was called
        verify(doctorService, times(1)).getDoctorById(1L);
        verify(reporteService, times(1)).obtenerReporteAgregado(1L, testRequest.getFechaInicio(), testRequest.getFechaFin());
    }

    @Test
    void testGenerarReporte_DoctorWithoutApellido() {
        // Arrange
        testDoctor.setApellido(null);
        testUsuario.setNombreUsuario(null);
        when(doctorService.getDoctorById(1L)).thenReturn(Optional.of(testDoctor));
        when(reporteService.obtenerReporteAgregado(anyLong(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(testReporteAgregado);

        // Act
        Response response = reporteResource.generarReporte(testRequest);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity() instanceof ReporteResponse);

        // Verify service was called
        verify(doctorService, times(1)).getDoctorById(1L);
        verify(reporteService, times(1)).obtenerReporteAgregado(1L, testRequest.getFechaInicio(), testRequest.getFechaFin());
    }

    @Test
    void testGenerarReporte_WithZeroIdDoctor() {
        // Arrange
        testRequest.setIdDoctor(0L);
        when(doctorService.getDoctorById(0L)).thenReturn(Optional.of(testDoctor));
        when(reporteService.obtenerReporteAgregado(anyLong(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(testReporteAgregado);

        // Act
        Response response = reporteResource.generarReporte(testRequest);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity() instanceof ReporteResponse);

        // Verify service was called
        verify(doctorService, times(1)).getDoctorById(0L);
        verify(reporteService, times(1)).obtenerReporteAgregado(0L, testRequest.getFechaInicio(), testRequest.getFechaFin());
    }

    @Test
    void testGenerarReporte_WithNegativeIdDoctor() {
        // Arrange
        testRequest.setIdDoctor(-1L);
        when(doctorService.getDoctorById(-1L)).thenReturn(Optional.of(testDoctor));
        when(reporteService.obtenerReporteAgregado(anyLong(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(testReporteAgregado);

        // Act
        Response response = reporteResource.generarReporte(testRequest);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity() instanceof ReporteResponse);

        // Verify service was called
        verify(doctorService, times(1)).getDoctorById(-1L);
        verify(reporteService, times(1)).obtenerReporteAgregado(-1L, testRequest.getFechaInicio(), testRequest.getFechaFin());
    }

    @Test
    void testGenerarReporte_WithMaximumIdDoctor() {
        // Arrange
        testRequest.setIdDoctor(Long.MAX_VALUE);
        when(doctorService.getDoctorById(Long.MAX_VALUE)).thenReturn(Optional.of(testDoctor));
        when(reporteService.obtenerReporteAgregado(anyLong(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(testReporteAgregado);

        // Act
        Response response = reporteResource.generarReporte(testRequest);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity() instanceof ReporteResponse);

        // Verify service was called
        verify(doctorService, times(1)).getDoctorById(Long.MAX_VALUE);
        verify(reporteService, times(1)).obtenerReporteAgregado(Long.MAX_VALUE, testRequest.getFechaInicio(), testRequest.getFechaFin());
    }

    @Test
    void testGenerarReporte_WithMinimumIdDoctor() {
        // Arrange
        testRequest.setIdDoctor(Long.MIN_VALUE);
        when(doctorService.getDoctorById(Long.MIN_VALUE)).thenReturn(Optional.of(testDoctor));
        when(reporteService.obtenerReporteAgregado(anyLong(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(testReporteAgregado);

        // Act
        Response response = reporteResource.generarReporte(testRequest);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity() instanceof ReporteResponse);

        // Verify service was called
        verify(doctorService, times(1)).getDoctorById(Long.MIN_VALUE);
        verify(reporteService, times(1)).obtenerReporteAgregado(Long.MIN_VALUE, testRequest.getFechaInicio(), testRequest.getFechaFin());
    }

    @Test
    void testGenerarReporte_WithSameDates() {
        // Arrange
        testRequest.setFechaInicio(LocalDate.of(2024, 1, 1));
        testRequest.setFechaFin(LocalDate.of(2024, 1, 1));
        when(doctorService.getDoctorById(1L)).thenReturn(Optional.of(testDoctor));
        when(reporteService.obtenerReporteAgregado(anyLong(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(testReporteAgregado);

        // Act
        Response response = reporteResource.generarReporte(testRequest);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity() instanceof ReporteResponse);

        // Verify service was called
        verify(doctorService, times(1)).getDoctorById(1L);
        verify(reporteService, times(1)).obtenerReporteAgregado(1L, testRequest.getFechaInicio(), testRequest.getFechaFin());
    }

    @Test
    void testGenerarReporte_WithBoundaryDates() {
        // Arrange
        testRequest.setFechaInicio(LocalDate.MIN);
        testRequest.setFechaFin(LocalDate.MAX);
        when(doctorService.getDoctorById(1L)).thenReturn(Optional.of(testDoctor));
        when(reporteService.obtenerReporteAgregado(anyLong(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(testReporteAgregado);

        // Act
        Response response = reporteResource.generarReporte(testRequest);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity() instanceof ReporteResponse);

        // Verify service was called
        verify(doctorService, times(1)).getDoctorById(1L);
        verify(reporteService, times(1)).obtenerReporteAgregado(1L, testRequest.getFechaInicio(), testRequest.getFechaFin());
    }

    @Test
    void testGenerarReporte_WithSpecialCharactersInUsuario() {
        // Arrange
        testRequest.setUsuario("user@#$%^&*()_+-=[]{}|;':\",./<>?");
        when(doctorService.getDoctorById(1L)).thenReturn(Optional.of(testDoctor));
        when(reporteService.obtenerReporteAgregado(anyLong(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(testReporteAgregado);

        // Act
        Response response = reporteResource.generarReporte(testRequest);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity() instanceof ReporteResponse);

        // Verify service was called
        verify(doctorService, times(1)).getDoctorById(1L);
        verify(reporteService, times(1)).obtenerReporteAgregado(1L, testRequest.getFechaInicio(), testRequest.getFechaFin());
    }

    @Test
    void testGenerarReporte_WithUnicodeCharactersInUsuario() {
        // Arrange
        testRequest.setUsuario("usér_námé_ñáéíóú");
        when(doctorService.getDoctorById(1L)).thenReturn(Optional.of(testDoctor));
        when(reporteService.obtenerReporteAgregado(anyLong(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(testReporteAgregado);

        // Act
        Response response = reporteResource.generarReporte(testRequest);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity() instanceof ReporteResponse);

        // Verify service was called
        verify(doctorService, times(1)).getDoctorById(1L);
        verify(reporteService, times(1)).obtenerReporteAgregado(1L, testRequest.getFechaInicio(), testRequest.getFechaFin());
    }

    @Test
    void testGenerarReporte_WithNumbersInUsuario() {
        // Arrange
        testRequest.setUsuario("user123");
        when(doctorService.getDoctorById(1L)).thenReturn(Optional.of(testDoctor));
        when(reporteService.obtenerReporteAgregado(anyLong(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(testReporteAgregado);

        // Act
        Response response = reporteResource.generarReporte(testRequest);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity() instanceof ReporteResponse);

        // Verify service was called
        verify(doctorService, times(1)).getDoctorById(1L);
        verify(reporteService, times(1)).obtenerReporteAgregado(1L, testRequest.getFechaInicio(), testRequest.getFechaFin());
    }

    @Test
    void testGenerarReporte_WithUnderscoresInUsuario() {
        // Arrange
        testRequest.setUsuario("user_name_test");
        when(doctorService.getDoctorById(1L)).thenReturn(Optional.of(testDoctor));
        when(reporteService.obtenerReporteAgregado(anyLong(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(testReporteAgregado);

        // Act
        Response response = reporteResource.generarReporte(testRequest);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity() instanceof ReporteResponse);

        // Verify service was called
        verify(doctorService, times(1)).getDoctorById(1L);
        verify(reporteService, times(1)).obtenerReporteAgregado(1L, testRequest.getFechaInicio(), testRequest.getFechaFin());
    }

    @Test
    void testGenerarReporte_WithHyphensInUsuario() {
        // Arrange
        testRequest.setUsuario("user-name-test");
        when(doctorService.getDoctorById(1L)).thenReturn(Optional.of(testDoctor));
        when(reporteService.obtenerReporteAgregado(anyLong(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(testReporteAgregado);

        // Act
        Response response = reporteResource.generarReporte(testRequest);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity() instanceof ReporteResponse);

        // Verify service was called
        verify(doctorService, times(1)).getDoctorById(1L);
        verify(reporteService, times(1)).obtenerReporteAgregado(1L, testRequest.getFechaInicio(), testRequest.getFechaFin());
    }

    @Test
    void testGenerarReporte_WithDotsInUsuario() {
        // Arrange
        testRequest.setUsuario("user.name.test");
        when(doctorService.getDoctorById(1L)).thenReturn(Optional.of(testDoctor));
        when(reporteService.obtenerReporteAgregado(anyLong(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(testReporteAgregado);

        // Act
        Response response = reporteResource.generarReporte(testRequest);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity() instanceof ReporteResponse);

        // Verify service was called
        verify(doctorService, times(1)).getDoctorById(1L);
        verify(reporteService, times(1)).obtenerReporteAgregado(1L, testRequest.getFechaInicio(), testRequest.getFechaFin());
    }

    @Test
    void testGenerarReporte_WithSpacesInUsuario() {
        // Arrange
        testRequest.setUsuario("user name test");
        when(doctorService.getDoctorById(1L)).thenReturn(Optional.of(testDoctor));
        when(reporteService.obtenerReporteAgregado(anyLong(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(testReporteAgregado);

        // Act
        Response response = reporteResource.generarReporte(testRequest);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity() instanceof ReporteResponse);

        // Verify service was called
        verify(doctorService, times(1)).getDoctorById(1L);
        verify(reporteService, times(1)).obtenerReporteAgregado(1L, testRequest.getFechaInicio(), testRequest.getFechaFin());
    }

    @Test
    void testGenerarReporte_WithMixedCaseInUsuario() {
        // Arrange
        testRequest.setUsuario("UserNameTest");
        when(doctorService.getDoctorById(1L)).thenReturn(Optional.of(testDoctor));
        when(reporteService.obtenerReporteAgregado(anyLong(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(testReporteAgregado);

        // Act
        Response response = reporteResource.generarReporte(testRequest);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity() instanceof ReporteResponse);

        // Verify service was called
        verify(doctorService, times(1)).getDoctorById(1L);
        verify(reporteService, times(1)).obtenerReporteAgregado(1L, testRequest.getFechaInicio(), testRequest.getFechaFin());
    }

    @Test
    void testGenerarReporte_WithBoundaryValuesInUsuario() {
        // Arrange
        testRequest.setUsuario("a"); // Minimum length
        when(doctorService.getDoctorById(1L)).thenReturn(Optional.of(testDoctor));
        when(reporteService.obtenerReporteAgregado(anyLong(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(testReporteAgregado);

        // Act
        Response response = reporteResource.generarReporte(testRequest);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity() instanceof ReporteResponse);

        // Verify service was called
        verify(doctorService, times(1)).getDoctorById(1L);
        verify(reporteService, times(1)).obtenerReporteAgregado(1L, testRequest.getFechaInicio(), testRequest.getFechaFin());
    }

    @Test
    void testGenerarReporte_WithLongUsuario() {
        // Arrange
        testRequest.setUsuario("very_long_username_that_exceeds_normal_length_limits_and_contains_many_characters");
        when(doctorService.getDoctorById(1L)).thenReturn(Optional.of(testDoctor));
        when(reporteService.obtenerReporteAgregado(anyLong(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(testReporteAgregado);

        // Act
        Response response = reporteResource.generarReporte(testRequest);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity() instanceof ReporteResponse);

        // Verify service was called
        verify(doctorService, times(1)).getDoctorById(1L);
        verify(reporteService, times(1)).obtenerReporteAgregado(1L, testRequest.getFechaInicio(), testRequest.getFechaFin());
    }

    @Test
    void testDescargarReporteExcel_Agrupado_Success() {
        // Arrange
        when(doctorService.getDoctorById(1L)).thenReturn(Optional.of(testDoctor));
        when(reporteService.obtenerReporteAgregado(anyLong(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(testReporteAgregado);

        // Act
        Response response = reporteResource.descargarReporteExcel(
            1L, "2024-01-01", "2024-01-31", "AGRUPADO", "testuser");

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertNotNull(response.getMediaType());
        assertEquals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", 
                    response.getMediaType().toString());
        assertNotNull(response.getHeaderString("Content-Disposition"));
        assertTrue(response.getHeaderString("Content-Disposition").contains("attachment"));
        assertTrue(response.getHeaderString("Content-Disposition").contains("Reporte.xlsx"));

        // Verify service was called
        verify(doctorService, times(1)).getDoctorById(1L);
        verify(reporteService, times(1)).obtenerReporteAgregado(1L, 
            LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 31));
    }

    @Test
    void testDescargarReporteExcel_Detallado_Success() {
        // Arrange
        when(doctorService.getDoctorById(1L)).thenReturn(Optional.of(testDoctor));
        when(reporteService.obtenerReporteDetallado(anyLong(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(testReporteDetallado);

        // Act
        Response response = reporteResource.descargarReporteExcel(
            1L, "2024-01-01", "2024-01-31", "DETALLADO", "testuser");

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertNotNull(response.getMediaType());
        assertEquals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", 
                    response.getMediaType().toString());

        // Verify service was called
        verify(doctorService, times(1)).getDoctorById(1L);
        verify(reporteService, times(1)).obtenerReporteDetallado(1L, 
            LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 31));
    }

    @Test
    void testDescargarReporteExcel_WithNullUsuario() {
        // Arrange
        when(doctorService.getDoctorById(1L)).thenReturn(Optional.of(testDoctor));
        when(reporteService.obtenerReporteAgregado(anyLong(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(testReporteAgregado);

        // Act
        Response response = reporteResource.descargarReporteExcel(
            1L, "2024-01-01", "2024-01-31", "AGRUPADO", null);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertNotNull(response.getMediaType());
        assertEquals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", 
                    response.getMediaType().toString());

        // Verify service was called
        verify(doctorService, times(1)).getDoctorById(1L);
        verify(reporteService, times(1)).obtenerReporteAgregado(1L, 
            LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 31));
    }

    @Test
    void testDescargarReporteExcel_WithEmptyUsuario() {
        // Arrange
        when(doctorService.getDoctorById(1L)).thenReturn(Optional.of(testDoctor));
        when(reporteService.obtenerReporteAgregado(anyLong(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(testReporteAgregado);

        // Act
        Response response = reporteResource.descargarReporteExcel(
            1L, "2024-01-01", "2024-01-31", "AGRUPADO", "");

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertNotNull(response.getMediaType());
        assertEquals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", 
                    response.getMediaType().toString());

        // Verify service was called
        verify(doctorService, times(1)).getDoctorById(1L);
        verify(reporteService, times(1)).obtenerReporteAgregado(1L, 
            LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 31));
    }

    @Test
    void testDescargarReporteExcel_DoctorNotFound() {
        // Arrange
        when(doctorService.getDoctorById(1L)).thenReturn(Optional.empty());
        when(reporteService.obtenerReporteAgregado(anyLong(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(testReporteAgregado);

        // Act
        Response response = reporteResource.descargarReporteExcel(
            1L, "2024-01-01", "2024-01-31", "AGRUPADO", "testuser");

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertNotNull(response.getMediaType());
        assertEquals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", 
                    response.getMediaType().toString());

        // Verify service was called
        verify(doctorService, times(1)).getDoctorById(1L);
        verify(reporteService, times(1)).obtenerReporteAgregado(1L, 
            LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 31));
    }

    @Test
    void testDescargarReporteExcel_WithEmptyReporte() {
        // Arrange
        when(doctorService.getDoctorById(1L)).thenReturn(Optional.of(testDoctor));
        when(reporteService.obtenerReporteAgregado(anyLong(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(Arrays.asList());

        // Act
        Response response = reporteResource.descargarReporteExcel(
            1L, "2024-01-01", "2024-01-31", "AGRUPADO", "testuser");

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertNotNull(response.getMediaType());
        assertEquals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", 
                    response.getMediaType().toString());

        // Verify service was called
        verify(doctorService, times(1)).getDoctorById(1L);
        verify(reporteService, times(1)).obtenerReporteAgregado(1L, 
            LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 31));
    }

    @Test
    void testDescargarReporteExcel_WithDifferentDateFormats() {
        // Arrange
        when(doctorService.getDoctorById(1L)).thenReturn(Optional.of(testDoctor));
        when(reporteService.obtenerReporteAgregado(anyLong(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(testReporteAgregado);

        // Act
        Response response = reporteResource.descargarReporteExcel(
            1L, "2024-12-25", "2024-12-31", "AGRUPADO", "testuser");

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertNotNull(response.getMediaType());
        assertEquals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", 
                    response.getMediaType().toString());

        // Verify service was called
        verify(doctorService, times(1)).getDoctorById(1L);
        verify(reporteService, times(1)).obtenerReporteAgregado(1L, 
            LocalDate.of(2024, 12, 25), LocalDate.of(2024, 12, 31));
    }
}
