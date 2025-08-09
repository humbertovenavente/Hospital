package com.unis.service;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.unis.model.RecetaMedicamento;
import com.unis.repository.RecetaMedicamentoRepository;

public class RecetaMedicamentoServiceTest {

    @Mock
    RecetaMedicamentoRepository recetaMedicamentoRepository;

    @InjectMocks
    RecetaMedicamentoService recetaMedicamentoService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test para listar medicamentos de una receta
    @Test
    public void testListarPorReceta() {
        Long idReceta = 1L;
        List<RecetaMedicamento> expectedList = Arrays.asList(new RecetaMedicamento(), new RecetaMedicamento());
        when(recetaMedicamentoRepository.listarPorReceta(idReceta)).thenReturn(expectedList);

        List<RecetaMedicamento> result = recetaMedicamentoService.listarPorReceta(idReceta);
        assertEquals(expectedList, result, "La lista de medicamentos debe coincidir con la esperada");
    }

    // Test para agregar un medicamento a una receta
    @Test
    public void testAgregarMedicamentoAReceta() {
        RecetaMedicamento recetaMedicamento = new RecetaMedicamento();
        doNothing().when(recetaMedicamentoRepository).persist(recetaMedicamento);

        recetaMedicamentoService.agregarMedicamentoAReceta(recetaMedicamento);
        verify(recetaMedicamentoRepository, times(1)).persist(recetaMedicamento);
    }

    // Test para eliminar un medicamento de una receta (caso exitoso)
    @Test
    public void testEliminarSuccess() {
        Long id = 1L;
        when(recetaMedicamentoRepository.deleteById(id)).thenReturn(true);

        boolean result = recetaMedicamentoService.eliminar(id);
        assertTrue(result, "La eliminación debe retornar true cuando es exitosa");
        verify(recetaMedicamentoRepository, times(1)).deleteById(id);
    }

    // Test para eliminar un medicamento de una receta (caso fallido)
    @Test
    public void testEliminarFailure() {
        Long id = 1L;
        when(recetaMedicamentoRepository.deleteById(id)).thenReturn(false);

        boolean result = recetaMedicamentoService.eliminar(id);
        assertFalse(result, "La eliminación debe retornar false cuando falla");
    }
}
