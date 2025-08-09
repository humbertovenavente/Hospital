package com.unis.service;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.unis.model.UsuarioInterAcc;
import com.unis.repository.UsuarioInterAccRepository;

import jakarta.transaction.Transactional;

public class UsuarioInterAccServiceTest {

    @InjectMocks
    UsuarioInterAccService usuarioInterAccService;

    @Mock
    UsuarioInterAccRepository usuarioInterAccRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllUsuariosInterAcc() {
        List<UsuarioInterAcc> usuarios = List.of(new UsuarioInterAcc(), new UsuarioInterAcc());
        when(usuarioInterAccRepository.listAll()).thenReturn(usuarios);

        List<UsuarioInterAcc> result = usuarioInterAccService.getAllUsuariosInterAcc();
        assertEquals(2, result.size());
        verify(usuarioInterAccRepository, times(1)).listAll();
    }

    @Test
    public void testGetUsuarioInterAccById() {
        UsuarioInterAcc usuario = new UsuarioInterAcc();
        when(usuarioInterAccRepository.findByIdOptional(1L)).thenReturn(Optional.of(usuario));

        Optional<UsuarioInterAcc> result = usuarioInterAccService.getUsuarioInterAccById(1L);
        assertTrue(result.isPresent());
        verify(usuarioInterAccRepository, times(1)).findByIdOptional(1L);
    }

    @Test
    @Transactional
    public void testActualizarUsuarioInterAcc() {
        UsuarioInterAcc usuarioExistente = new UsuarioInterAcc();
        UsuarioInterAcc usuarioActualizado = new UsuarioInterAcc();
        usuarioActualizado.setApellido("Nuevo Apellido");

        when(usuarioInterAccRepository.findByIdOptional(1L)).thenReturn(Optional.of(usuarioExistente));

        usuarioInterAccService.actualizarUsuarioInterAcc(1L, usuarioActualizado);

        assertEquals("Nuevo Apellido", usuarioExistente.getApellido());
        verify(usuarioInterAccRepository, times(1)).findByIdOptional(1L);
        verify(usuarioInterAccRepository, times(1)).persist(usuarioExistente);
    }

    @Test
    @Transactional
    public void testEliminarUsuarioInterAcc() {
        usuarioInterAccService.eliminarUsuarioInterAcc(1L);
        verify(usuarioInterAccRepository, times(1)).deleteById(1L);
    }
}
