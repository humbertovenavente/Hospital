package com.unis.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.unis.model.FichaTecnica;
import com.unis.repository.FichaTecnicaRepository;

public class FichaTecnicaServiceTest {

    @Mock
    FichaTecnicaRepository fichaTecnicaRepository;

    @InjectMocks
    FichaTecnicaService fichaTecnicaService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test para obtener todas las fichas técnicas
    @Test
    public void testGetAllFichas() {
        List<FichaTecnica> expectedFichas = Arrays.asList(new FichaTecnica(), new FichaTecnica());
        when(fichaTecnicaRepository.listAll()).thenReturn(expectedFichas);

        List<FichaTecnica> result = fichaTecnicaService.getAllFichas();
        assertEquals(expectedFichas, result, "La lista de fichas técnicas debe coincidir con la esperada");
    }

    // Test para obtener una ficha técnica por ID cuando se encuentra
    @Test
    public void testGetFichaByIdFound() {
        Long id = 1L;
        FichaTecnica ficha = new FichaTecnica();
        when(fichaTecnicaRepository.findByIdOptional(id)).thenReturn(Optional.of(ficha));

        Optional<FichaTecnica> result = fichaTecnicaService.getFichaById(id);
        assertTrue(result.isPresent(), "La ficha técnica debería existir");
        assertEquals(ficha, result.get(), "La ficha técnica obtenida debe ser la esperada");
    }

    // Test para obtener una ficha técnica por ID cuando no se encuentra
    @Test
    public void testGetFichaByIdNotFound() {
        Long id = 1L;
        when(fichaTecnicaRepository.findByIdOptional(id)).thenReturn(Optional.empty());

        Optional<FichaTecnica> result = fichaTecnicaService.getFichaById(id);
        assertFalse(result.isPresent(), "La ficha técnica no debería encontrarse");
    }

    // Test para registrar una ficha técnica
    @Test
    public void testRegistrarFicha() {
        FichaTecnica ficha = new FichaTecnica();
        // Act
        fichaTecnicaService.registrarFicha(ficha);
        // Assert: se verifica que se haya llamado al método persist del repositorio
        verify(fichaTecnicaRepository, times(1)).persist(ficha);
    }
}
