package com.unis.controller;

import com.unis.model.Rol;
import com.unis.model.Usuario;
import com.unis.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import jakarta.ws.rs.core.Response;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UsuarioControllerTest {

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private UsuarioController usuarioController;

    private Usuario testUsuario;
    private Rol testRol;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        testUsuario = new Usuario();
        testUsuario.setId(1L);
        testUsuario.setCorreo("test@example.com");
        testUsuario.setContrasena("password123");
        
        testRol = new Rol();
        testRol.setId(1L);
        testRol.setRoleName("ADMIN");
    }

    @Test
    void testListarUsuarios() {
        // Arrange
        List<Usuario> usuariosEsperados = Arrays.asList(testUsuario);
        when(usuarioService.listarUsuarios()).thenReturn(usuariosEsperados);

        // Act
        List<Usuario> resultado = usuarioController.listarUsuarios();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(usuarioService).listarUsuarios();
    }

    @Test
    void testRegistrarUsuario() {
        // Arrange
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setCorreo("nuevo@example.com");
        nuevoUsuario.setContrasena("nueva123");

        // Act
        Response response = usuarioController.registrarUsuario(nuevoUsuario);

        // Assert
        assertNotNull(response);
        assertEquals(201, response.getStatus());
        assertEquals(nuevoUsuario, response.getEntity());
        verify(usuarioService).registrarUsuario(nuevoUsuario);
    }

    @Test
    void testLoginExitoso() {
        // Arrange
        Usuario loginUsuario = new Usuario();
        loginUsuario.setCorreo("test@example.com");
        loginUsuario.setContrasena("password123");
        
        when(usuarioService.obtenerUsuarioPorCorreo("test@example.com")).thenReturn(testUsuario);

        // Act
        Response response = usuarioController.login(loginUsuario);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatus());
        assertEquals(testUsuario, response.getEntity());
        verify(usuarioService).obtenerUsuarioPorCorreo("test@example.com");
    }

    @Test
    void testLoginUsuarioNoEncontrado() {
        // Arrange
        Usuario loginUsuario = new Usuario();
        loginUsuario.setCorreo("noexiste@example.com");
        loginUsuario.setContrasena("password123");
        
        when(usuarioService.obtenerUsuarioPorCorreo("noexiste@example.com")).thenReturn(null);

        // Act
        Response response = usuarioController.login(loginUsuario);

        // Assert
        assertNotNull(response);
        assertEquals(401, response.getStatus());
        assertEquals("Credenciales incorrectas", response.getEntity());
        verify(usuarioService).obtenerUsuarioPorCorreo("noexiste@example.com");
    }

    @Test
    void testLoginContrasenaIncorrecta() {
        // Arrange
        Usuario loginUsuario = new Usuario();
        loginUsuario.setCorreo("test@example.com");
        loginUsuario.setContrasena("contrasenaIncorrecta");
        
        when(usuarioService.obtenerUsuarioPorCorreo("test@example.com")).thenReturn(testUsuario);

        // Act
        Response response = usuarioController.login(loginUsuario);

        // Assert
        assertNotNull(response);
        assertEquals(401, response.getStatus());
        assertEquals("Credenciales incorrectas", response.getEntity());
        verify(usuarioService).obtenerUsuarioPorCorreo("test@example.com");
    }

    @Test
    void testListarUsuariosInactivos() {
        // Arrange
        List<Usuario> usuariosInactivos = Arrays.asList(testUsuario);
        when(usuarioService.listarUsuariosInactivos()).thenReturn(usuariosInactivos);

        // Act
        List<Usuario> resultado = usuarioController.listarUsuariosInactivos();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(usuarioService).listarUsuariosInactivos();
    }

    @Test
    void testListarRoles() {
        // Arrange
        List<Rol> rolesEsperados = Arrays.asList(testRol);
        when(usuarioService.listarRoles()).thenReturn(rolesEsperados);

        // Act
        List<Rol> resultado = usuarioController.listarRoles();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(usuarioService).listarRoles();
    }

    @Test
    void testActivarUsuarioExitoso() {
        // Arrange
        Long usuarioId = 1L;
        Long rolId = 1L;
        UsuarioController.ActivarUsuarioDTO dto = new UsuarioController.ActivarUsuarioDTO();
        dto.setRolId(rolId);
        
        when(usuarioService.activarUsuario(usuarioId, rolId)).thenReturn(testUsuario);

        // Act
        Response response = usuarioController.activarUsuario(usuarioId, dto);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatus());
        assertEquals(testUsuario, response.getEntity());
        verify(usuarioService).activarUsuario(usuarioId, rolId);
    }

    @Test
    void testActivarUsuarioError() {
        // Arrange
        Long usuarioId = 1L;
        Long rolId = 1L;
        UsuarioController.ActivarUsuarioDTO dto = new UsuarioController.ActivarUsuarioDTO();
        dto.setRolId(rolId);
        
        when(usuarioService.activarUsuario(usuarioId, rolId))
            .thenThrow(new RuntimeException("Error al activar usuario"));

        // Act
        Response response = usuarioController.activarUsuario(usuarioId, dto);

        // Assert
        assertNotNull(response);
        assertEquals(400, response.getStatus());
        assertEquals("Error al activar usuario", response.getEntity());
        verify(usuarioService).activarUsuario(usuarioId, rolId);
    }

    @Test
    void testDesactivarUsuarioExitoso() {
        // Arrange
        Long usuarioId = 1L;
        when(usuarioService.desactivarUsuario(usuarioId)).thenReturn(testUsuario);

        // Act
        Response response = usuarioController.desactivarUsuario(usuarioId);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatus());
        assertEquals(testUsuario, response.getEntity());
        verify(usuarioService).desactivarUsuario(usuarioId);
    }

    @Test
    void testDesactivarUsuarioError() {
        // Arrange
        Long usuarioId = 1L;
        when(usuarioService.desactivarUsuario(usuarioId))
            .thenThrow(new RuntimeException("Error al desactivar usuario"));

        // Act
        Response response = usuarioController.desactivarUsuario(usuarioId);

        // Assert
        assertNotNull(response);
        assertEquals(400, response.getStatus());
        assertEquals("Error al desactivar usuario", response.getEntity());
        verify(usuarioService).desactivarUsuario(usuarioId);
    }

    @Test
    void testActivarUsuarioDTOSetterGetter() {
        // Arrange
        UsuarioController.ActivarUsuarioDTO dto = new UsuarioController.ActivarUsuarioDTO();
        Long rolId = 5L;

        // Act
        dto.setRolId(rolId);

        // Assert
        assertEquals(rolId, dto.getRolId());
    }
}
