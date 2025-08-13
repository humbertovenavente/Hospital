package com.unis.service;

import com.unis.model.Paciente;
import com.unis.model.Usuario;
import com.unis.repository.PacienteRepository;
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

public class PacienteServiceTest {

    @Mock
    private PacienteRepository pacienteRepository;

    @Mock
    private EntityManager entityManager;

    @Mock
    private Query query;

    @InjectMocks
    private PacienteService pacienteService;

    private Paciente testPaciente;
    private Usuario testUsuario;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        testUsuario = new Usuario();
        testUsuario.setNombreUsuario("Juan");
        testUsuario.setCorreo("juan@hospital.com");
        testUsuario.setContrasena("password123");
        
        testPaciente = new Paciente();
        testPaciente.setUsuario(testUsuario);
        testPaciente.setApellido("Pérez");
        testPaciente.setDocumento("12345678");
        testPaciente.setTelefono("123456789");
    }

    @Test
    void testGetAllPacientes() {
        // Arrange
        List<Paciente> pacientesEsperados = Arrays.asList(testPaciente);
        when(pacienteRepository.listAll()).thenReturn(pacientesEsperados);

        // Act
        List<Paciente> resultado = pacienteService.getAllPacientes();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(pacienteRepository).listAll();
    }

    @Test
    void testGetPacienteById() {
        // Arrange
        Long id = 1L;
        when(pacienteRepository.findByIdOptional(id)).thenReturn(Optional.of(testPaciente));

        // Act
        Optional<Paciente> resultado = pacienteService.getPacienteById(id);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(testPaciente, resultado.get());
        verify(pacienteRepository).findByIdOptional(id);
    }

    @Test
    void testGetPacienteByIdNoEncontrado() {
        // Arrange
        Long id = 999L;
        when(pacienteRepository.findByIdOptional(id)).thenReturn(Optional.empty());

        // Act
        Optional<Paciente> resultado = pacienteService.getPacienteById(id);

        // Assert
        assertFalse(resultado.isPresent());
        verify(pacienteRepository).findByIdOptional(id);
    }

    @Test
    void testRegistrarPacienteExitoso() {
        // Arrange
        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        when(query.setParameter(anyInt(), any())).thenReturn(query);
        when(query.executeUpdate()).thenReturn(1);

        // Act & Assert
        assertDoesNotThrow(() -> pacienteService.registrarPaciente(testPaciente));
        verify(entityManager).createNativeQuery(anyString());
        verify(query, times(8)).setParameter(anyInt(), any());
        verify(query).executeUpdate();
    }

    @Test
    void testRegistrarPacienteCorreoDuplicado() {
        // Arrange
        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        when(query.setParameter(anyInt(), any())).thenReturn(query);
        when(query.executeUpdate()).thenThrow(new RuntimeException("ORA-20001: Error"));

        // Act & Assert
        WebApplicationException exception = assertThrows(
            WebApplicationException.class,
            () -> pacienteService.registrarPaciente(testPaciente)
        );
        
        assertEquals(400, exception.getResponse().getStatus());
        assertEquals(" Error: El correo ya está registrado.", exception.getMessage());
    }

    @Test
    void testRegistrarPacienteErrorInterno() {
        // Arrange
        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        when(query.setParameter(anyInt(), any())).thenReturn(query);
        when(query.executeUpdate()).thenThrow(new RuntimeException("Error de base de datos"));

        // Act & Assert
        WebApplicationException exception = assertThrows(
            WebApplicationException.class,
            () -> pacienteService.registrarPaciente(testPaciente)
        );
        
        assertEquals(500, exception.getResponse().getStatus());
        assertEquals(" Error interno del servidor.", exception.getMessage());
    }

    @Test
    void testActualizarPacienteExitoso() {
        // Arrange
        Long id = 1L;
        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        when(query.setParameter(anyInt(), any())).thenReturn(query);
        when(query.executeUpdate()).thenReturn(1);

        // Act
        boolean resultado = pacienteService.actualizarPaciente(id, testPaciente);

        // Assert
        assertTrue(resultado);
        verify(entityManager).createNativeQuery(anyString());
        verify(query, times(9)).setParameter(anyInt(), any());
        verify(query).executeUpdate();
    }

    @Test
    void testActualizarPacienteCorreoDuplicado() {
        // Arrange
        Long id = 1L;
        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        when(query.setParameter(anyInt(), any())).thenReturn(query);
        when(query.executeUpdate()).thenThrow(new RuntimeException("El correo ya está registrado en el sistema"));

        // Act & Assert
        WebApplicationException exception = assertThrows(
            WebApplicationException.class,
            () -> pacienteService.actualizarPaciente(id, testPaciente)
        );
        
        assertEquals(400, exception.getResponse().getStatus());
        assertEquals("Error: El correo ya está registrado en el sistema.", exception.getMessage());
    }

    @Test
    void testActualizarPacienteConError() {
        // Arrange
        Long id = 1L;
        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        when(query.setParameter(anyInt(), any())).thenReturn(query);
        when(query.executeUpdate()).thenThrow(new RuntimeException("Error de base de datos"));

        // Act
        boolean resultado = pacienteService.actualizarPaciente(id, testPaciente);

        // Assert
        assertFalse(resultado);
    }

    @Test
    void testEliminarPacienteExitoso() {
        // Arrange
        Long id = 1L;
        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        when(query.setParameter(anyInt(), any())).thenReturn(query);
        when(query.executeUpdate()).thenReturn(1);

        // Act
        boolean resultado = pacienteService.eliminarPaciente(id);

        // Assert
        assertTrue(resultado);
        verify(entityManager).createNativeQuery(anyString());
        verify(query).setParameter(1, id);
        verify(query).executeUpdate();
    }

    @Test
    void testEliminarPacienteConError() {
        // Arrange
        Long id = 1L;
        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        when(query.setParameter(anyInt(), any())).thenReturn(query);
        when(query.executeUpdate()).thenThrow(new RuntimeException("Error de base de datos"));

        // Act
        boolean resultado = pacienteService.eliminarPaciente(id);

        // Assert
        assertFalse(resultado);
    }
}
