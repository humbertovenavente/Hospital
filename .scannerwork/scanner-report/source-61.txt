package com.unis.service;

import com.unis.model.Usuario;
import com.unis.repository.UsuarioRepository;
import com.unis.repository.RolRepository;

import jakarta.ws.rs.WebApplicationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UsuarioServiceTest {

    private UsuarioService usuarioService;
    private UsuarioRepository usuarioRepository;
    private RolRepository rolRepository;

    @BeforeEach
    void setUp() {
        usuarioRepository = mock(UsuarioRepository.class);
        rolRepository = mock(RolRepository.class);

        usuarioService = new UsuarioService();
        usuarioService.usuarioRepository = usuarioRepository;
        usuarioService.rolRepository = rolRepository;
    }

    @Test
    void registrarUsuario_cuandoCorreoNoExiste_deberiaPersistir() {
        Usuario nuevo = new Usuario();
        nuevo.setCorreo("nuevo@email.com");

        when(usuarioRepository.findByCorreo("nuevo@email.com")).thenReturn(null);

        assertDoesNotThrow(() -> usuarioService.registrarUsuario(nuevo));
        verify(usuarioRepository).persist(any(Usuario.class));
    }

    @Test
    void registrarUsuario_cuandoCorreoYaExiste_deberiaLanzarExcepcion() {
        Usuario existente = new Usuario();
        existente.setCorreo("repetido@email.com");

        when(usuarioRepository.findByCorreo("repetido@email.com")).thenReturn(existente);

        Usuario nuevo = new Usuario();
        nuevo.setCorreo("repetido@email.com");

        WebApplicationException ex = assertThrows(WebApplicationException.class, () -> {
            usuarioService.registrarUsuario(nuevo);
        });

        assertEquals("El correo ya está registrado", ex.getMessage());
        verify(usuarioRepository, never()).persist(any(Usuario.class));
    }
}
