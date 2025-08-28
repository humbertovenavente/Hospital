package com.unis.service;

import com.unis.model.Rol;
import com.unis.model.Usuario;
import com.unis.repository.RolRepository;
import com.unis.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private RolRepository rolRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario testUsuario;
    private Rol testRol;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        testUsuario = new Usuario();
        testUsuario.setCorreo("test@example.com");
        testUsuario.setId(1L);
        
        testRol = new Rol();
        testRol.setId(1L);
    }

    @Test
    void testListarUsuarios_Success() {
        // Arrange
        List<Usuario> usuarios = Arrays.asList(testUsuario);
        when(usuarioRepository.listAll()).thenReturn(usuarios);

        // Act
        List<Usuario> result = usuarioService.listarUsuarios();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testUsuario, result.get(0));
        verify(usuarioRepository).listAll();
    }

    @Test
    void testListarUsuarios_EmptyList() {
        // Arrange
        when(usuarioRepository.listAll()).thenReturn(Collections.emptyList());

        // Act
        List<Usuario> result = usuarioService.listarUsuarios();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(usuarioRepository).listAll();
    }

    @Test
    void testObtenerUsuarioPorCorreo_Success() {
        // Arrange
        String correo = "test@example.com";
        when(usuarioRepository.findByCorreo(correo)).thenReturn(testUsuario);

        // Act
        Usuario result = usuarioService.obtenerUsuarioPorCorreo(correo);

        // Assert
        assertNotNull(result);
        assertEquals(testUsuario, result);
        verify(usuarioRepository).findByCorreo(correo);
    }

    @Test
    void testObtenerUsuarioPorCorreo_NotFound() {
        // Arrange
        String correo = "nonexistent@example.com";
        when(usuarioRepository.findByCorreo(correo)).thenReturn(null);

        // Act
        Usuario result = usuarioService.obtenerUsuarioPorCorreo(correo);

        // Assert
        assertNull(result);
        verify(usuarioRepository).findByCorreo(correo);
    }

    @Test
    void testRegistrarUsuario_Success() {
        // Arrange
        when(usuarioRepository.findByCorreo(anyString())).thenReturn(null);
        doNothing().when(usuarioRepository).persist(any(Usuario.class));

        // Act & Assert
        assertDoesNotThrow(() -> usuarioService.registrarUsuario(testUsuario));
        verify(usuarioRepository).findByCorreo(anyString());
        verify(usuarioRepository).persist(testUsuario);
    }

    @Test
    void testRegistrarUsuario_DuplicateEmail() {
        // Arrange
        when(usuarioRepository.findByCorreo(anyString())).thenReturn(testUsuario);

        // Act & Assert
        WebApplicationException exception = assertThrows(WebApplicationException.class, 
            () -> usuarioService.registrarUsuario(testUsuario));
        assertEquals(400, exception.getResponse().getStatus());
        assertEquals("El correo ya está registrado", exception.getMessage());
        verify(usuarioRepository).findByCorreo(anyString());
        verify(usuarioRepository, never()).persist(any(Usuario.class));
    }

    @Test
    void testActivarUsuario_Success() {
        // Arrange
        Long idUsuario = 1L;
        Long idRol = 1L;
        when(usuarioRepository.findById(idUsuario)).thenReturn(testUsuario);
        when(rolRepository.findById(idRol)).thenReturn(testRol);

        // Act
        Usuario result = usuarioService.activarUsuario(idUsuario, idRol);

        // Assert
        assertNotNull(result);
        assertEquals(testUsuario, result);
        verify(usuarioRepository).findById(idUsuario);
        verify(rolRepository).findById(idRol);
    }

    @Test
    void testActivarUsuario_UsuarioNotFound() {
        // Arrange
        Long idUsuario = 999L;
        Long idRol = 1L;
        when(usuarioRepository.findById(idUsuario)).thenReturn(null);

        // Act & Assert
        WebApplicationException exception = assertThrows(WebApplicationException.class, 
            () -> usuarioService.activarUsuario(idUsuario, idRol));
        assertEquals(404, exception.getResponse().getStatus());
        assertEquals("Usuario no encontrado", exception.getMessage());
        verify(usuarioRepository).findById(idUsuario);
        verify(rolRepository, never()).findById(anyLong());
    }

    @Test
    void testActivarUsuario_RolInvalido() {
        // Arrange
        Long idUsuario = 1L;
        Long idRol = 999L;
        when(usuarioRepository.findById(idUsuario)).thenReturn(testUsuario);
        when(rolRepository.findById(idRol)).thenReturn(null);

        // Act & Assert
        WebApplicationException exception = assertThrows(WebApplicationException.class, 
            () -> usuarioService.activarUsuario(idUsuario, idRol));
        assertEquals(400, exception.getResponse().getStatus());
        assertEquals("Rol no válido", exception.getMessage());
        verify(usuarioRepository).findById(idUsuario);
        verify(rolRepository).findById(idRol);
    }
}
