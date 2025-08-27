package com.unis.resource;

import com.unis.model.Doctor;
import com.unis.service.DoctorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import jakarta.ws.rs.core.Response;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * Test class for DoctorResource.
 * Tests all REST endpoints and business logic.
 */
class DoctorResourceTest {

    @Mock
    private DoctorService doctorService;

    @InjectMocks
    private DoctorResource doctorResource;

    private Doctor testDoctor;
    private List<Doctor> testDoctores;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        testDoctor = new Doctor();
        testDoctor.setIdDoctor(1L);
        testDoctor.setApellido("Dr. Juan Pérez");
        testDoctor.setEspecialidad("Cardiología");
        testDoctor.setNumeroColegiado("LIC123456");
        testDoctor.setTelefono("12345678");
        
        testDoctores = Arrays.asList(testDoctor);
    }

    @Test
    void testObtenerTodosLosDoctores_Success() {
        // Arrange
        when(doctorService.getAllDoctores()).thenReturn(testDoctores);

        // Act
        List<Doctor> result = doctorResource.obtenerTodosLosDoctores();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testDoctor, result.get(0));

        // Verify service was called
        verify(doctorService, times(1)).getAllDoctores();
    }

    @Test
    void testObtenerTodosLosDoctores_EmptyList() {
        // Arrange
        when(doctorService.getAllDoctores()).thenReturn(Arrays.asList());

        // Act
        List<Doctor> result = doctorResource.obtenerTodosLosDoctores();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

        // Verify service was called
        verify(doctorService, times(1)).getAllDoctores();
    }

    @Test
    void testObtenerTodosLosDoctores_MultipleDoctors() {
        // Arrange
        Doctor doctor2 = new Doctor();
        doctor2.setIdDoctor(2L);
        doctor2.setApellido("Dr. María García");
        doctor2.setEspecialidad("Neurología");
        
        List<Doctor> multipleDoctores = Arrays.asList(testDoctor, doctor2);
        when(doctorService.getAllDoctores()).thenReturn(multipleDoctores);

        // Act
        List<Doctor> result = doctorResource.obtenerTodosLosDoctores();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(testDoctor, result.get(0));
        assertEquals(doctor2, result.get(1));

        // Verify service was called
        verify(doctorService, times(1)).getAllDoctores();
    }

    @Test
    void testObtenerDoctor_Success() {
        // Arrange
        when(doctorService.getDoctorById(1L)).thenReturn(Optional.of(testDoctor));

        // Act
        Response response = doctorResource.obtenerDoctor(1L);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(testDoctor, response.getEntity());

        // Verify service was called
        verify(doctorService, times(1)).getDoctorById(1L);
    }

    @Test
    void testObtenerDoctor_NotFound() {
        // Arrange
        when(doctorService.getDoctorById(999L)).thenReturn(Optional.empty());

        // Act
        Response response = doctorResource.obtenerDoctor(999L);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());

        // Verify service was called
        verify(doctorService, times(1)).getDoctorById(999L);
    }

    @Test
    void testObtenerDoctor_WithZeroId() {
        // Arrange
        when(doctorService.getDoctorById(0L)).thenReturn(Optional.empty());

        // Act
        Response response = doctorResource.obtenerDoctor(0L);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());

        // Verify service was called
        verify(doctorService, times(1)).getDoctorById(0L);
    }

    @Test
    void testObtenerDoctor_WithNegativeId() {
        // Arrange
        when(doctorService.getDoctorById(-1L)).thenReturn(Optional.empty());

        // Act
        Response response = doctorResource.obtenerDoctor(-1L);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());

        // Verify service was called
        verify(doctorService, times(1)).getDoctorById(-1L);
    }

    @Test
    void testObtenerDoctor_WithNullId() {
        // Arrange
        when(doctorService.getDoctorById(null)).thenReturn(Optional.empty());

        // Act
        Response response = doctorResource.obtenerDoctor(null);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());

        // Verify service was called
        verify(doctorService, times(1)).getDoctorById(null);
    }

    @Test
    void testRegistrarDoctor_Success() {
        // Arrange
        doNothing().when(doctorService).registrarDoctor(any(Doctor.class));

        // Act
        Response response = doctorResource.registrarDoctor(testDoctor);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());

        // Verify service was called
        verify(doctorService, times(1)).registrarDoctor(any(Doctor.class));
    }

    @Test
    void testRegistrarDoctor_WithNullDoctor() {
        // Arrange
        doNothing().when(doctorService).registrarDoctor(null);

        // Act
        Response response = doctorResource.registrarDoctor(null);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());

        // Verify service was called
        verify(doctorService, times(1)).registrarDoctor(null);
    }

    @Test
    void testRegistrarDoctor_WithEmptyDoctor() {
        // Arrange
        Doctor emptyDoctor = new Doctor();
        doNothing().when(doctorService).registrarDoctor(any(Doctor.class));

        // Act
        Response response = doctorResource.registrarDoctor(emptyDoctor);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());

        // Verify service was called
        verify(doctorService, times(1)).registrarDoctor(any(Doctor.class));
    }

    @Test
    void testRegistrarDoctor_WithPartialDoctor() {
        // Arrange
        Doctor partialDoctor = new Doctor();
        partialDoctor.setApellido("Dr. Test");
        doNothing().when(doctorService).registrarDoctor(any(Doctor.class));

        // Act
        Response response = doctorResource.registrarDoctor(partialDoctor);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());

        // Verify service was called
        verify(doctorService, times(1)).registrarDoctor(any(Doctor.class));
    }

    @Test
    void testRegistrarDoctor_WithSpecialCharacters() {
        // Arrange
        Doctor specialDoctor = new Doctor();
        specialDoctor.setApellido("Dr. @#$%^&*()");
        specialDoctor.setEspecialidad("Especialidad@#$%");
        specialDoctor.setNumeroColegiado("LIC@#$%");
        specialDoctor.setTelefono("123@456");
        doNothing().when(doctorService).registrarDoctor(any(Doctor.class));

        // Act
        Response response = doctorResource.registrarDoctor(specialDoctor);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());

        // Verify service was called
        verify(doctorService, times(1)).registrarDoctor(any(Doctor.class));
    }

    @Test
    void testRegistrarDoctor_WithUnicodeCharacters() {
        // Arrange
        Doctor unicodeDoctor = new Doctor();
        unicodeDoctor.setApellido("Dr. José María");
        unicodeDoctor.setEspecialidad("Cardiología");
        unicodeDoctor.setNumeroColegiado("LICÑÁÉÍÓÚ");
        unicodeDoctor.setTelefono("12345678");
        doNothing().when(doctorService).registrarDoctor(any(Doctor.class));

        // Act
        Response response = doctorResource.registrarDoctor(unicodeDoctor);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());

        // Verify service was called
        verify(doctorService, times(1)).registrarDoctor(any(Doctor.class));
    }

    @Test
    void testActualizarDoctor_Success() {
        // Arrange
        when(doctorService.actualizarDoctor(1L, testDoctor)).thenReturn(true);

        // Act
        Response response = doctorResource.actualizarDoctor(1L, testDoctor);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        // Verify service was called
        verify(doctorService, times(1)).actualizarDoctor(1L, testDoctor);
    }

    @Test
    void testActualizarDoctor_NotFound() {
        // Arrange
        when(doctorService.actualizarDoctor(999L, testDoctor)).thenReturn(false);

        // Act
        Response response = doctorResource.actualizarDoctor(999L, testDoctor);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());

        // Verify service was called
        verify(doctorService, times(1)).actualizarDoctor(999L, testDoctor);
    }

    @Test
    void testActualizarDoctor_WithZeroId() {
        // Arrange
        when(doctorService.actualizarDoctor(0L, testDoctor)).thenReturn(false);

        // Act
        Response response = doctorResource.actualizarDoctor(0L, testDoctor);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());

        // Verify service was called
        verify(doctorService, times(1)).actualizarDoctor(0L, testDoctor);
    }

    @Test
    void testActualizarDoctor_WithNegativeId() {
        // Arrange
        when(doctorService.actualizarDoctor(-1L, testDoctor)).thenReturn(false);

        // Act
        Response response = doctorResource.actualizarDoctor(-1L, testDoctor);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());

        // Verify service was called
        verify(doctorService, times(1)).actualizarDoctor(-1L, testDoctor);
    }

    @Test
    void testActualizarDoctor_WithNullDoctor() {
        // Arrange
        when(doctorService.actualizarDoctor(1L, null)).thenReturn(false);

        // Act
        Response response = doctorResource.actualizarDoctor(1L, null);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());

        // Verify service was called
        verify(doctorService, times(1)).actualizarDoctor(1L, null);
    }

    @Test
    void testActualizarDoctor_WithEmptyDoctor() {
        // Arrange
        Doctor emptyDoctor = new Doctor();
        when(doctorService.actualizarDoctor(1L, emptyDoctor)).thenReturn(false);

        // Act
        Response response = doctorResource.actualizarDoctor(1L, emptyDoctor);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());

        // Verify service was called
        verify(doctorService, times(1)).actualizarDoctor(1L, emptyDoctor);
    }

    @Test
    void testEliminarDoctor_Success() {
        // Arrange
        when(doctorService.eliminarDoctor(1L)).thenReturn(true);

        // Act
        Response response = doctorResource.eliminarDoctor(1L);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        // Verify service was called
        verify(doctorService, times(1)).eliminarDoctor(1L);
    }

    @Test
    void testEliminarDoctor_NotFound() {
        // Arrange
        when(doctorService.eliminarDoctor(999L)).thenReturn(false);

        // Act
        Response response = doctorResource.eliminarDoctor(999L);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());

        // Verify service was called
        verify(doctorService, times(1)).eliminarDoctor(999L);
    }

    @Test
    void testEliminarDoctor_WithZeroId() {
        // Arrange
        when(doctorService.eliminarDoctor(0L)).thenReturn(false);

        // Act
        Response response = doctorResource.eliminarDoctor(0L);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());

        // Verify service was called
        verify(doctorService, times(1)).eliminarDoctor(0L);
    }

    @Test
    void testEliminarDoctor_WithNegativeId() {
        // Arrange
        when(doctorService.eliminarDoctor(-1L)).thenReturn(false);

        // Act
        Response response = doctorResource.eliminarDoctor(-1L);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());

        // Verify service was called
        verify(doctorService, times(1)).eliminarDoctor(-1L);
    }

    @Test
    void testEliminarDoctor_WithNullId() {
        // Arrange
        when(doctorService.eliminarDoctor(null)).thenReturn(false);

        // Act
        Response response = doctorResource.eliminarDoctor(null);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());

        // Verify service was called
        verify(doctorService, times(1)).eliminarDoctor(null);
    }

    @Test
    void testEliminarDoctor_WithMaximumId() {
        // Arrange
        when(doctorService.eliminarDoctor(Long.MAX_VALUE)).thenReturn(false);

        // Act
        Response response = doctorResource.eliminarDoctor(Long.MAX_VALUE);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());

        // Verify service was called
        verify(doctorService, times(1)).eliminarDoctor(Long.MAX_VALUE);
    }

    @Test
    void testEliminarDoctor_WithMinimumId() {
        // Arrange
        when(doctorService.eliminarDoctor(Long.MIN_VALUE)).thenReturn(false);

        // Act
        Response response = doctorResource.eliminarDoctor(Long.MIN_VALUE);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());

        // Verify service was called
        verify(doctorService, times(1)).eliminarDoctor(Long.MIN_VALUE);
    }
}
