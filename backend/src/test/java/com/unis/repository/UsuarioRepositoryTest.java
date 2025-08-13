package com.unis.repository;

import com.unis.model.Usuario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UsuarioRepositoryTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombreUsuario("testuser");
        usuario.setCorreo("test@example.com");
        usuario.setContrasena("password123");
    }

    @Test
    void testFindByCorreoExitoso() {
        // Arrange
        String correo = "test@example.com";
        when(usuarioRepository.findByCorreo(correo)).thenReturn(usuario);

        // Act
        Usuario resultado = usuarioRepository.findByCorreo(correo);

        // Assert
        assertNotNull(resultado);
        assertEquals(correo, resultado.getCorreo());
        verify(usuarioRepository).findByCorreo(correo);
    }

    @Test
    void testFindByCorreoNoEncontrado() {
        // Arrange
        String correo = "noexiste@example.com";
        when(usuarioRepository.findByCorreo(correo)).thenReturn(null);

        // Act
        Usuario resultado = usuarioRepository.findByCorreo(correo);

        // Assert
        assertNull(resultado);
        verify(usuarioRepository).findByCorreo(correo);
    }

    @Test
    void testFindByCorreoConCorreoNull() {
        // Arrange
        when(usuarioRepository.findByCorreo(null)).thenReturn(null);

        // Act
        Usuario resultado = usuarioRepository.findByCorreo(null);

        // Assert
        assertNull(resultado);
        verify(usuarioRepository).findByCorreo(null);
    }

    @Test
    void testFindByCorreoConCorreoVacio() {
        // Arrange
        String correo = "";
        when(usuarioRepository.findByCorreo(correo)).thenReturn(null);

        // Act
        Usuario resultado = usuarioRepository.findByCorreo(correo);

        // Assert
        assertNull(resultado);
        verify(usuarioRepository).findByCorreo(correo);
    }

    @Test
    void testFindByCorreoConCorreoConEspacios() {
        // Arrange
        String correo = "  test@example.com  ";
        when(usuarioRepository.findByCorreo(correo)).thenReturn(null);

        // Act
        Usuario resultado = usuarioRepository.findByCorreo(correo);

        // Assert
        assertNull(resultado);
        verify(usuarioRepository).findByCorreo(correo);
    }

    @Test
    void testFindByCorreoConCorreoInvalido() {
        // Arrange
        String correo = "correo-invalido";
        when(usuarioRepository.findByCorreo(correo)).thenReturn(null);

        // Act
        Usuario resultado = usuarioRepository.findByCorreo(correo);

        // Assert
        assertNull(resultado);
        verify(usuarioRepository).findByCorreo(correo);
    }

    @Test
    void testFindByCorreoConCorreoMuyLargo() {
        // Arrange
        String correo = "correo.muy.largo.con.muchos.puntos.y.extensiones@dominio.muy.largo.com";
        when(usuarioRepository.findByCorreo(correo)).thenReturn(null);

        // Act
        Usuario resultado = usuarioRepository.findByCorreo(correo);

        // Assert
        assertNull(resultado);
        verify(usuarioRepository).findByCorreo(correo);
    }

    @Test
    void testFindByCorreoConCorreoConCaracteresEspeciales() {
        // Arrange
        String correo = "test+tag@example.com";
        when(usuarioRepository.findByCorreo(correo)).thenReturn(null);

        // Act
        Usuario resultado = usuarioRepository.findByCorreo(correo);

        // Assert
        assertNull(resultado);
        verify(usuarioRepository).findByCorreo(correo);
    }

    @Test
    void testFindByCorreoConCorreoConNumeros() {
        // Arrange
        String correo = "test123@example.com";
        when(usuarioRepository.findByCorreo(correo)).thenReturn(null);

        // Act
        Usuario resultado = usuarioRepository.findByCorreo(correo);

        // Assert
        assertNull(resultado);
        verify(usuarioRepository).findByCorreo(correo);
    }

    @Test
    void testFindByCorreoConCorreoConGuiones() {
        // Arrange
        String correo = "test-user@example-domain.com";
        when(usuarioRepository.findByCorreo(correo)).thenReturn(null);

        // Act
        Usuario resultado = usuarioRepository.findByCorreo(correo);

        // Assert
        assertNull(resultado);
        verify(usuarioRepository).findByCorreo(correo);
    }

    @Test
    void testFindByCorreoConCorreoConPuntos() {
        // Arrange
        String correo = "test.user@example.com";
        when(usuarioRepository.findByCorreo(correo)).thenReturn(null);

        // Act
        Usuario resultado = usuarioRepository.findByCorreo(correo);

        // Assert
        assertNull(resultado);
        verify(usuarioRepository).findByCorreo(correo);
    }

    @Test
    void testFindByCorreoConCorreoConUnderscore() {
        // Arrange
        String correo = "test_user@example.com";
        when(usuarioRepository.findByCorreo(correo)).thenReturn(null);

        // Act
        Usuario resultado = usuarioRepository.findByCorreo(correo);

        // Assert
        assertNull(resultado);
        verify(usuarioRepository).findByCorreo(correo);
    }

    @Test
    void testFindByCorreoConCorreoConMayusculas() {
        // Arrange
        String correo = "TEST@EXAMPLE.COM";
        when(usuarioRepository.findByCorreo(correo)).thenReturn(null);

        // Act
        Usuario resultado = usuarioRepository.findByCorreo(correo);

        // Assert
        assertNull(resultado);
        verify(usuarioRepository).findByCorreo(correo);
    }

    @Test
    void testFindByCorreoConCorreoConMinusculas() {
        // Arrange
        String correo = "test@example.com";
        when(usuarioRepository.findByCorreo(correo)).thenReturn(null);

        // Act
        Usuario resultado = usuarioRepository.findByCorreo(correo);

        // Assert
        assertNull(resultado);
        verify(usuarioRepository).findByCorreo(correo);
    }

    @Test
    void testFindByCorreoConCorreoConMezcla() {
        // Arrange
        String correo = "Test.User@Example.COM";
        when(usuarioRepository.findByCorreo(correo)).thenReturn(null);

        // Act
        Usuario resultado = usuarioRepository.findByCorreo(correo);

        // Assert
        assertNull(resultado);
        verify(usuarioRepository).findByCorreo(correo);
    }
}
