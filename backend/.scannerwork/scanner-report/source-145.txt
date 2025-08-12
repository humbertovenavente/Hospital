package com.unis.service;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.unis.model.Usuario;
import com.unis.model.UsuarioInter;
import com.unis.repository.UsuarioInterconexionRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;

public class UsuarioInterServiceTest {

    @InjectMocks
    UsuarioInterService usuarioInterService;

    @Mock
    UsuarioInterconexionRepository usuarioInterconexionRepository;

    @Mock
    EntityManager entityManager;

    @Mock
    Query query;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        when(query.setParameter(anyInt(), any())).thenReturn(query);
    }

    @Test
    void testGetAllUsuarios() {
        usuarioInterService.getAllUsuarios();
        verify(usuarioInterconexionRepository, times(1)).listAll();
    }

    @Test
    void testGetUsuarioById() {
        Long id = 1L;
        when(usuarioInterconexionRepository.findByIdOptional(id)).thenReturn(Optional.of(new UsuarioInter()));
        Optional<UsuarioInter> result = usuarioInterService.getUsuarioById(id);
        assertTrue(result.isPresent());
        verify(usuarioInterconexionRepository, times(1)).findByIdOptional(id);
    }

    @Test
    @Transactional
    void testRegistrarUsuario() {
        UsuarioInter usuario = new UsuarioInter();
        usuario.setUsuario(new Usuario());
        usuarioInterService.registrarUsuario(usuario);
        verify(entityManager, times(1)).createNativeQuery(anyString());
    }

    @Test
    @Transactional
    void testActualizarUsuario() {
        Long id = 1L;
        UsuarioInter usuario = new UsuarioInter();
        usuario.setUsuario(new Usuario());
        usuarioInterService.actualizarUsuario(id, usuario);
        verify(entityManager, times(1)).createNativeQuery(anyString());
    }

    @Test
    @Transactional
    void testEliminarUsuario() {
        Long id = 1L;
        usuarioInterService.eliminarUsuario(id);
        verify(entityManager, times(1)).createNativeQuery(anyString());
    }
}
