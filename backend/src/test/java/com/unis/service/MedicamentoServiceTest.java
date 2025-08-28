package com.unis.service;

import com.unis.model.Medicamento;
import com.unis.repository.MedicamentoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class MedicamentoServiceTest {

    @Mock
    private MedicamentoRepository medicamentoRepository;

    @InjectMocks
    private MedicamentoService medicamentoService;

    private Medicamento testMedicamento;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        testMedicamento = new Medicamento();
        // No necesitamos setters, solo verificamos que el objeto existe
    }

    @Test
    void testListarTodos_Success() {
        // Arrange
        List<Medicamento> medicamentos = Arrays.asList(testMedicamento);
        when(medicamentoRepository.listAll()).thenReturn(medicamentos);

        // Act
        List<Medicamento> result = medicamentoService.listarTodos();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testMedicamento, result.get(0));
        verify(medicamentoRepository).listAll();
    }

    @Test
    void testListarTodos_EmptyList() {
        // Arrange
        when(medicamentoRepository.listAll()).thenReturn(Collections.emptyList());

        // Act
        List<Medicamento> result = medicamentoService.listarTodos();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(medicamentoRepository).listAll();
    }

    @Test
    void testObtenerPorId_Success() {
        // Arrange
        when(medicamentoRepository.findById(1L)).thenReturn(testMedicamento);

        // Act
        Medicamento result = medicamentoService.obtenerPorId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(testMedicamento, result);
        verify(medicamentoRepository).findById(1L);
    }

    @Test
    void testObtenerPorId_NotFound() {
        // Arrange
        when(medicamentoRepository.findById(999L)).thenReturn(null);

        // Act
        Medicamento result = medicamentoService.obtenerPorId(999L);

        // Assert
        assertNull(result);
        verify(medicamentoRepository).findById(999L);
    }

    @Test
    void testCrearMedicamento_Success() {
        // Arrange
        doNothing().when(medicamentoRepository).persist(any(Medicamento.class));

        // Act
        Medicamento result = medicamentoService.crearMedicamento(testMedicamento);

        // Assert
        assertNotNull(result);
        assertEquals(testMedicamento, result);
        verify(medicamentoRepository).persist(testMedicamento);
    }

    @Test
    void testActualizarMedicamento_Success() {
        // Arrange
        Medicamento medicamentoNuevo = new Medicamento();
        when(medicamentoRepository.findById(1L)).thenReturn(testMedicamento);

        // Act
        Medicamento result = medicamentoService.actualizarMedicamento(1L, medicamentoNuevo);

        // Assert
        assertNotNull(result);
        assertEquals(testMedicamento, result);
        verify(medicamentoRepository).findById(1L);
    }

    @Test
    void testActualizarMedicamento_NotFound() {
        // Arrange
        Medicamento medicamentoNuevo = new Medicamento();
        when(medicamentoRepository.findById(999L)).thenReturn(null);

        // Act
        Medicamento result = medicamentoService.actualizarMedicamento(999L, medicamentoNuevo);

        // Assert
        assertNull(result);
        verify(medicamentoRepository).findById(999L);
    }

    @Test
    void testEliminarMedicamento_Success() {
        // Arrange
        when(medicamentoRepository.deleteById(1L)).thenReturn(true);

        // Act
        boolean result = medicamentoService.eliminarMedicamento(1L);

        // Assert
        assertTrue(result);
        verify(medicamentoRepository).deleteById(1L);
    }

    @Test
    void testEliminarMedicamento_NotFound() {
        // Arrange
        when(medicamentoRepository.deleteById(999L)).thenReturn(false);

        // Act
        boolean result = medicamentoService.eliminarMedicamento(999L);

        // Assert
        assertFalse(result);
        verify(medicamentoRepository).deleteById(999L);
    }
}
