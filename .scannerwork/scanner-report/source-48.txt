package com.unis.service;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.unis.model.Medicamento;
import com.unis.repository.MedicamentoRepository;

public class MedicamentoServiceTest {

    @Mock
    MedicamentoRepository medicamentoRepository;

    @InjectMocks
    MedicamentoService medicamentoService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test para listar todos los medicamentos
    @Test
    public void testListarTodos() {
        List<Medicamento> expected = Arrays.asList(new Medicamento(), new Medicamento());
        when(medicamentoRepository.listAll()).thenReturn(expected);

        List<Medicamento> result = medicamentoService.listarTodos();
        assertEquals(expected, result, "La lista de medicamentos debe coincidir con la esperada");
    }

    // Test para obtener un medicamento por ID cuando se encuentra
    @Test
    public void testObtenerPorIdFound() {
        Long id = 1L;
        Medicamento medicamento = new Medicamento();
        when(medicamentoRepository.findById(id)).thenReturn(medicamento);

        Medicamento result = medicamentoService.obtenerPorId(id);
        assertEquals(medicamento, result, "El medicamento obtenido debe ser el esperado");
    }

    // Test para obtener un medicamento por ID cuando no se encuentra
    @Test
    public void testObtenerPorIdNotFound() {
        Long id = 1L;
        when(medicamentoRepository.findById(id)).thenReturn(null);

        Medicamento result = medicamentoService.obtenerPorId(id);
        assertNull(result, "Si no se encuentra el medicamento se debe retornar null");
    }
}
