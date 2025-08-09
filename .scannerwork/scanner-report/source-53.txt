package com.unis.service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.unis.model.Paciente;
import com.unis.model.Usuario;
import com.unis.repository.PacienteRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

public class PacienteServiceTest {

    @Mock
    PacienteRepository pacienteRepository;

    @Mock
    EntityManager entityManager;

    @Mock
    Query query;

    @InjectMocks
    PacienteService pacienteService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test para obtener todos los pacientes
    @Test
    public void testGetAllPacientes() {
        List<Paciente> expected = Arrays.asList(new Paciente(), new Paciente());
        when(pacienteRepository.listAll()).thenReturn(expected);
        
        List<Paciente> result = pacienteService.getAllPacientes();
        assertEquals(expected, result);
    }

    // Test para obtener un paciente por ID (caso encontrado)
    @Test
    public void testGetPacienteByIdFound() {
        Paciente paciente = new Paciente();
        when(pacienteRepository.findByIdOptional(1L)).thenReturn(Optional.of(paciente));
        
        Optional<Paciente> result = pacienteService.getPacienteById(1L);
        assertTrue(result.isPresent());
        assertEquals(paciente, result.get());
    }

    // Test para obtener un paciente por ID (caso no encontrado)
    @Test
    public void testGetPacienteByIdNotFound() {
        when(pacienteRepository.findByIdOptional(1L)).thenReturn(Optional.empty());
        
        Optional<Paciente> result = pacienteService.getPacienteById(1L);
        assertFalse(result.isPresent());
    }

    // Test para registrar un paciente exitosamente
    @Test
    public void testRegistrarPacienteSuccess() {
        Paciente paciente = new Paciente();
        Usuario usuario = new Usuario();
        usuario.setNombreUsuario("nombre");
        usuario.setCorreo("correo@example.com");
        usuario.setContrasena("password");
        paciente.setUsuario(usuario);
        paciente.setApellido("apellido");
        paciente.setDocumento("documento");
        paciente.setFechaNacimiento(new Date(100000L));
        paciente.setGenero("M");
        paciente.setTelefono("123456789");
        
        // Simular el comportamiento de la consulta nativa
        when(entityManager.createNativeQuery(any(String.class))).thenReturn(query);
        when(query.setParameter(anyInt(), any())).thenReturn(query);
        when(query.executeUpdate()).thenReturn(1);
        
        // No debe lanzar excepción
        assertDoesNotThrow(() -> pacienteService.registrarPaciente(paciente));
        verify(query, times(1)).executeUpdate();
    }

    // Test para registrar un paciente cuando se detecta correo duplicado
    @Test
    public void testRegistrarPacienteDuplicateEmail() {
        Paciente paciente = new Paciente();
        Usuario usuario = new Usuario();
        usuario.setNombreUsuario("nombre");
        usuario.setCorreo("correo@example.com");
        usuario.setContrasena("password");
        paciente.setUsuario(usuario);
        paciente.setApellido("apellido");
        paciente.setDocumento("documento");
        paciente.setFechaNacimiento(new Date(100000L));
        paciente.setGenero("M");
        paciente.setTelefono("123456789");
        
        when(entityManager.createNativeQuery(any(String.class))).thenReturn(query);
        when(query.setParameter(anyInt(), any())).thenReturn(query);
        // Simulamos una excepción que indique duplicado (contiene "ORA-20001")
        when(query.executeUpdate()).thenThrow(new RuntimeException("ORA-20001: duplicate email"));
        
        WebApplicationException ex = assertThrows(WebApplicationException.class, () -> {
            pacienteService.registrarPaciente(paciente);
        });
        assertEquals(" Error: El correo ya está registrado.", ex.getMessage());
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), ex.getResponse().getStatus());
    }

    // Test para registrar un paciente con error interno
    @Test
    public void testRegistrarPacienteInternalError() {
        Paciente paciente = new Paciente();
        Usuario usuario = new Usuario();
        usuario.setNombreUsuario("nombre");
        usuario.setCorreo("correo@example.com");
        usuario.setContrasena("password");
        paciente.setUsuario(usuario);
        paciente.setApellido("apellido");
        paciente.setDocumento("documento");
        paciente.setFechaNacimiento(new Date(100000L));
        paciente.setGenero("M");
        paciente.setTelefono("123456789");
        
        when(entityManager.createNativeQuery(any(String.class))).thenReturn(query);
        when(query.setParameter(anyInt(), any())).thenReturn(query);
        // Simulamos una excepción distinta a la de correo duplicado
        when(query.executeUpdate()).thenThrow(new RuntimeException("Other error"));
        
        WebApplicationException ex = assertThrows(WebApplicationException.class, () -> {
            pacienteService.registrarPaciente(paciente);
        });
        assertEquals(" Error interno del servidor.", ex.getMessage());
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), ex.getResponse().getStatus());
    }

    // Test para actualizar un paciente exitosamente
    @Test
    public void testActualizarPacienteSuccess() {
        Paciente paciente = new Paciente();
        Usuario usuario = new Usuario();
        usuario.setNombreUsuario("nuevoNombre");
        usuario.setCorreo("nuevoCorreo@example.com");
        usuario.setContrasena("nuevoPassword");
        paciente.setUsuario(usuario);
        paciente.setApellido("nuevoApellido");
        paciente.setDocumento("nuevoDocumento");
        paciente.setFechaNacimiento(new Date(200000L));
        paciente.setGenero("F");
        paciente.setTelefono("987654321");
        
        when(entityManager.createNativeQuery(any(String.class))).thenReturn(query);
        when(query.setParameter(anyInt(), any())).thenReturn(query);
        when(query.executeUpdate()).thenReturn(1);
        
        boolean result = pacienteService.actualizarPaciente(1L, paciente);
        assertTrue(result);
        verify(query, times(1)).executeUpdate();
    }

    // Test para actualizar un paciente con correo duplicado
    @Test
    public void testActualizarPacienteDuplicateEmail() {
        Paciente paciente = new Paciente();
        Usuario usuario = new Usuario();
        usuario.setNombreUsuario("nuevoNombre");
        usuario.setCorreo("nuevoCorreo@example.com");
        usuario.setContrasena("nuevoPassword");
        paciente.setUsuario(usuario);
        paciente.setApellido("nuevoApellido");
        paciente.setDocumento("nuevoDocumento");
        paciente.setFechaNacimiento(new Date(200000L));
        paciente.setGenero("F");
        paciente.setTelefono("987654321");
        
        when(entityManager.createNativeQuery(any(String.class))).thenReturn(query);
        when(query.setParameter(anyInt(), any())).thenReturn(query);
        // Simulamos que se lanza una excepción que contenga el mensaje de correo duplicado
        when(query.executeUpdate()).thenThrow(new RuntimeException("El correo ya está registrado en el sistema"));
        
        WebApplicationException ex = assertThrows(WebApplicationException.class, () -> {
            pacienteService.actualizarPaciente(1L, paciente);
        });
        assertEquals("Error: El correo ya está registrado en el sistema.", ex.getMessage());
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), ex.getResponse().getStatus());
    }

    // Test para actualizar un paciente con otro error (debe retornar false)
    @Test
    public void testActualizarPacienteOtherError() {
        Paciente paciente = new Paciente();
        Usuario usuario = new Usuario();
        usuario.setNombreUsuario("nuevoNombre");
        usuario.setCorreo("nuevoCorreo@example.com");
        usuario.setContrasena("nuevoPassword");
        paciente.setUsuario(usuario);
        paciente.setApellido("nuevoApellido");
        paciente.setDocumento("nuevoDocumento");
        paciente.setFechaNacimiento(new Date(200000L));
        paciente.setGenero("F");
        paciente.setTelefono("987654321");
        
        when(entityManager.createNativeQuery(any(String.class))).thenReturn(query);
        when(query.setParameter(anyInt(), any())).thenReturn(query);
        when(query.executeUpdate()).thenThrow(new RuntimeException("Some other error"));
        
        boolean result = pacienteService.actualizarPaciente(1L, paciente);
        assertFalse(result);
    }

    // Test para eliminar un paciente exitosamente
    @Test
    public void testEliminarPacienteSuccess() {
        when(entityManager.createNativeQuery(any(String.class))).thenReturn(query);
        when(query.setParameter(anyInt(), any())).thenReturn(query);
        when(query.executeUpdate()).thenReturn(1);
        
        boolean result = pacienteService.eliminarPaciente(1L);
        assertTrue(result);
        verify(query, times(1)).executeUpdate();
    }

    // Test para eliminar un paciente con error
    @Test
    public void testEliminarPacienteError() {
        when(entityManager.createNativeQuery(any(String.class))).thenReturn(query);
        when(query.setParameter(anyInt(), any())).thenReturn(query);
        when(query.executeUpdate()).thenThrow(new RuntimeException("Error"));
        
        boolean result = pacienteService.eliminarPaciente(1L);
        assertFalse(result);
    }
}
