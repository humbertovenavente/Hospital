package com.unis.service;

import com.unis.model.Doctor;
import com.unis.model.Usuario;
import com.unis.repository.DoctorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.ws.rs.WebApplicationException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DoctorServiceTest {

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private EntityManager entityManager;

    @Mock
    private Query query;

    @InjectMocks
    private DoctorService doctorService;

    private Doctor testDoctor;
    private Usuario testUsuario;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        testUsuario = new Usuario();
        testUsuario.setNombreUsuario("Dr. Juan");
        testUsuario.setCorreo("juan@hospital.com");
        testUsuario.setContrasena("password123");
        
        testDoctor = new Doctor();
        testDoctor.setUsuario(testUsuario);
        testDoctor.setApellido("Pérez");
        testDoctor.setDocumento("12345678");
        testDoctor.setEspecialidad("Cardiología");
        testDoctor.setNumeroColegiado("COL001");
        testDoctor.setHorarioAtencion("8:00-16:00");
        testDoctor.setFechaGraduacion(java.sql.Date.valueOf("2010-01-01"));
        testDoctor.setUniversidadGraduacion("Universidad Nacional");
    }

    @Test
    void testGetAllDoctores() {
        // Arrange
        List<Doctor> doctoresEsperados = Arrays.asList(testDoctor);
        when(doctorRepository.listAll()).thenReturn(doctoresEsperados);

        // Act
        List<Doctor> resultado = doctorService.getAllDoctores();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(doctorRepository).listAll();
    }

    @Test
    void testGetDoctorById() {
        // Arrange
        Long id = 1L;
        when(doctorRepository.findByIdOptional(id)).thenReturn(Optional.of(testDoctor));

        // Act
        Optional<Doctor> resultado = doctorService.getDoctorById(id);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(testDoctor, resultado.get());
        verify(doctorRepository).findByIdOptional(id);
    }

    @Test
    void testGetDoctorByIdNoEncontrado() {
        // Arrange
        Long id = 999L;
        when(doctorRepository.findByIdOptional(id)).thenReturn(Optional.empty());

        // Act
        Optional<Doctor> resultado = doctorService.getDoctorById(id);

        // Assert
        assertFalse(resultado.isPresent());
        verify(doctorRepository).findByIdOptional(id);
    }

    @Test
    void testRegistrarDoctorExitoso() {
        // Arrange
        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        when(query.setParameter(anyInt(), any())).thenReturn(query);
        when(query.executeUpdate()).thenReturn(1);

        // Act & Assert
        assertDoesNotThrow(() -> doctorService.registrarDoctor(testDoctor));
        verify(entityManager).createNativeQuery(anyString());
        verify(query, times(13)).setParameter(anyInt(), any());
        verify(query).executeUpdate();
    }

    @Test
    void testRegistrarDoctorConError() {
        // Arrange
        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        when(query.setParameter(anyInt(), any())).thenReturn(query);
        when(query.executeUpdate()).thenThrow(new RuntimeException("Error de base de datos"));

        // Act & Assert
        WebApplicationException exception = assertThrows(
            WebApplicationException.class,
            () -> doctorService.registrarDoctor(testDoctor)
        );
        
        assertEquals(500, exception.getResponse().getStatus());
        assertEquals("Error en la transacción.", exception.getMessage());
    }

    @Test
    void testActualizarDoctorExitoso() {
        // Arrange
        Long id = 1L;
        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        when(query.setParameter(anyInt(), any())).thenReturn(query);
        when(query.executeUpdate()).thenReturn(1);

        // Act
        boolean resultado = doctorService.actualizarDoctor(id, testDoctor);

        // Assert
        assertTrue(resultado);
        verify(entityManager).createNativeQuery(anyString());
        verify(query, times(14)).setParameter(anyInt(), any());
        verify(query).executeUpdate();
    }

    @Test
    void testActualizarDoctorConError() {
        // Arrange
        Long id = 1L;
        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        when(query.setParameter(anyInt(), any())).thenReturn(query);
        when(query.executeUpdate()).thenThrow(new RuntimeException("Error de base de datos"));

        // Act
        boolean resultado = doctorService.actualizarDoctor(id, testDoctor);

        // Assert
        assertFalse(resultado);
    }

    @Test
    void testEliminarDoctorExitoso() {
        // Arrange
        Long id = 1L;
        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        when(query.setParameter(anyInt(), any())).thenReturn(query);
        when(query.executeUpdate()).thenReturn(1);

        // Act
        boolean resultado = doctorService.eliminarDoctor(id);

        // Assert
        assertTrue(resultado);
        verify(entityManager).createNativeQuery(anyString());
        verify(query).setParameter(1, id);
        verify(query).executeUpdate();
    }

    @Test
    void testEliminarDoctorConError() {
        // Arrange
        Long id = 1L;
        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        when(query.setParameter(anyInt(), any())).thenReturn(query);
        when(query.executeUpdate()).thenThrow(new RuntimeException("Error de base de datos"));

        // Act
        boolean resultado = doctorService.eliminarDoctor(id);

        // Assert
        assertFalse(resultado);
    }
}
