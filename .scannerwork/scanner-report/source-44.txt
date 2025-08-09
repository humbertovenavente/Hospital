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

import com.unis.model.Empleado;
import com.unis.model.Usuario;
import com.unis.repository.EmpleadoRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

public class EmpleadoServiceTest {

    @Mock
    EmpleadoRepository empleadoRepository;

    @Mock
    EntityManager entityManager;

    @Mock
    Query query;

    @InjectMocks
    EmpleadoService empleadoService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test para obtener todos los empleados
    @Test
    public void testGetAllEmpleados() {
        List<Empleado> empleados = Arrays.asList(new Empleado(), new Empleado());
        when(empleadoRepository.listAll()).thenReturn(empleados);

        List<Empleado> result = empleadoService.getAllEmpleados();
        assertEquals(empleados, result);
    }

    // Test para obtener empleado por ID (caso encontrado)
    @Test
    public void testGetEmpleadoByIdFound() {
        Empleado empleado = new Empleado();
        when(empleadoRepository.findByIdOptional(1L)).thenReturn(Optional.of(empleado));

        Optional<Empleado> result = empleadoService.getEmpleadoById(1L);
        assertTrue(result.isPresent());
        assertEquals(empleado, result.get());
    }

    // Test para obtener empleado por ID (caso no encontrado)
    @Test
    public void testGetEmpleadoByIdNotFound() {
        when(empleadoRepository.findByIdOptional(1L)).thenReturn(Optional.empty());

        Optional<Empleado> result = empleadoService.getEmpleadoById(1L);
        assertFalse(result.isPresent());
    }

    // Test para registrar empleado exitosamente
    @Test
    public void testRegistrarEmpleadoSuccess() {
        Empleado empleado = new Empleado();
        // Crear y configurar un usuario real
        Usuario usuario = new Usuario();
        usuario.setNombreUsuario("nombreUsuario");
        usuario.setCorreo("correo@example.com");
        usuario.setContrasena("contrasena");
        empleado.setUsuario(usuario);
        
        empleado.setApellido("apellido");
        empleado.setDocumento("documento");
        empleado.setFechaNacimiento(new Date(100000L));
        empleado.setGenero("M");
        empleado.setTelefono("123456789");
        empleado.setPuesto("PuestoX");

        // Simular la creación de la consulta nativa
        when(entityManager.createNativeQuery(any(String.class))).thenReturn(query);
        when(query.setParameter(anyInt(), any())).thenReturn(query);
        when(query.executeUpdate()).thenReturn(1);

        // No debe lanzar excepción
        assertDoesNotThrow(() -> empleadoService.registrarEmpleado(empleado));
        verify(query, times(1)).executeUpdate();
    }

    // Test para registrar empleado con error por correo duplicado
    @Test
    public void testRegistrarEmpleadoDuplicateEmail() {
        Empleado empleado = new Empleado();
        Usuario usuario = new Usuario();
        usuario.setNombreUsuario("nombreUsuario");
        usuario.setCorreo("correo@example.com");
        usuario.setContrasena("contrasena");
        empleado.setUsuario(usuario);
        
        empleado.setApellido("apellido");
        empleado.setDocumento("documento");
        empleado.setFechaNacimiento(new Date(100000L));
        empleado.setGenero("M");
        empleado.setTelefono("123456789");
        empleado.setPuesto("PuestoX");

        when(entityManager.createNativeQuery(any(String.class))).thenReturn(query);
        when(query.setParameter(anyInt(), any())).thenReturn(query);
        // Simular excepción con mensaje que contiene "ORA-20001"
        when(query.executeUpdate()).thenThrow(new RuntimeException("ORA-20001: duplicate email"));

        WebApplicationException exception = assertThrows(WebApplicationException.class, () -> {
            empleadoService.registrarEmpleado(empleado);
        });
        assertEquals("Error: El correo ya está registrado.", exception.getMessage());
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), exception.getResponse().getStatus());
    }

    // Test para actualizar empleado exitosamente
    @Test
    public void testActualizarEmpleadoSuccess() {
        Empleado empleado = new Empleado();
        Usuario usuario = new Usuario();
        usuario.setNombreUsuario("nuevoNombre");
        usuario.setCorreo("nuevoCorreo@example.com");
        usuario.setContrasena("nuevaContrasena");
        empleado.setUsuario(usuario);
        
        empleado.setApellido("nuevoApellido");
        empleado.setDocumento("nuevoDocumento");
        empleado.setFechaNacimiento(new Date(200000L));
        empleado.setGenero("F");
        empleado.setTelefono("987654321");
        empleado.setPuesto("NuevoPuesto");

        when(entityManager.createNativeQuery(any(String.class))).thenReturn(query);
        when(query.setParameter(anyInt(), any())).thenReturn(query);
        when(query.executeUpdate()).thenReturn(1);

        boolean result = empleadoService.actualizarEmpleado(1L, empleado);
        assertTrue(result);
        verify(query, times(1)).executeUpdate();
    }

    // Test para actualizar empleado con error de correo duplicado
    @Test
    public void testActualizarEmpleadoDuplicateEmail() {
        Empleado empleado = new Empleado();
        Usuario usuario = new Usuario();
        usuario.setNombreUsuario("nuevoNombre");
        usuario.setCorreo("nuevoCorreo@example.com");
        usuario.setContrasena("nuevaContrasena");
        empleado.setUsuario(usuario);
        
        empleado.setApellido("nuevoApellido");
        empleado.setDocumento("nuevoDocumento");
        empleado.setFechaNacimiento(new Date(200000L));
        empleado.setGenero("F");
        empleado.setTelefono("987654321");
        empleado.setPuesto("NuevoPuesto");

        when(entityManager.createNativeQuery(any(String.class))).thenReturn(query);
        when(query.setParameter(anyInt(), any())).thenReturn(query);
        // Simular excepción con mensaje indicando que el correo ya está registrado
        when(query.executeUpdate()).thenThrow(new RuntimeException("El correo ya está registrado en el sistema"));

        WebApplicationException exception = assertThrows(WebApplicationException.class, () -> {
            empleadoService.actualizarEmpleado(1L, empleado);
        });
        assertEquals("Error: El correo ya está registrado en el sistema.", exception.getMessage());
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), exception.getResponse().getStatus());
    }

    // Test para actualizar empleado con otro error (debe lanzar excepción)
@Test
public void testActualizarEmpleadoOtherError() {
    Empleado empleado = new Empleado();
    Usuario usuario = new Usuario();
    usuario.setNombreUsuario("nuevoNombre");
    usuario.setCorreo("nuevoCorreo@example.com");
    usuario.setContrasena("nuevaContrasena");
    empleado.setUsuario(usuario);

    empleado.setApellido("nuevoApellido");
    empleado.setDocumento("nuevoDocumento");
    empleado.setFechaNacimiento(new Date(200000L));
    empleado.setGenero("F");
    empleado.setTelefono("987654321");
    empleado.setPuesto("NuevoPuesto");

    when(entityManager.createNativeQuery(any(String.class))).thenReturn(query);
    when(query.setParameter(anyInt(), any())).thenReturn(query);
    when(query.executeUpdate()).thenThrow(new RuntimeException("Otro error"));

    WebApplicationException exception = assertThrows(WebApplicationException.class, () -> {
        empleadoService.actualizarEmpleado(1L, empleado);
    });

    assertEquals("Error al actualizar el empleado: Otro error", exception.getMessage());
}


    // Test para eliminar empleado exitosamente
    @Test
    public void testEliminarEmpleadoSuccess() {
        when(entityManager.createNativeQuery(any(String.class))).thenReturn(query);
        when(query.setParameter(anyInt(), any())).thenReturn(query);
        when(query.executeUpdate()).thenReturn(1);

        boolean result = empleadoService.eliminarEmpleado(1L);
        assertTrue(result);
        verify(query, times(1)).executeUpdate();
    }

    // Test para eliminar empleado con error (debe lanzar excepción)
@Test
public void testEliminarEmpleadoError() {
    when(entityManager.createNativeQuery(any(String.class))).thenReturn(query);
    when(query.setParameter(anyInt(), any())).thenReturn(query);
    when(query.executeUpdate()).thenThrow(new RuntimeException("Error"));

    WebApplicationException exception = assertThrows(WebApplicationException.class, () -> {
        empleadoService.eliminarEmpleado(1L);
    });

    assertEquals("Error al eliminar el empleado: Error", exception.getMessage());
}

}
