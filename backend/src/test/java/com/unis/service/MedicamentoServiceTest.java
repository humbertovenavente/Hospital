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
    private Medicamento medicamentoNuevo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        testMedicamento = new Medicamento();
        testMedicamento.setPrincipioActivo("Paracetamol");
        testMedicamento.setConcentracion("500mg");
        testMedicamento.setPresentacion("Tableta");
        testMedicamento.setFormaFarmaceutica("Oral");
        testMedicamento.setVentaLibre(1); // 1 = venta libre
        
        medicamentoNuevo = new Medicamento();
        medicamentoNuevo.setPrincipioActivo("Ibuprofeno");
        medicamentoNuevo.setConcentracion("400mg");
        medicamentoNuevo.setPresentacion("Cápsula");
        medicamentoNuevo.setFormaFarmaceutica("Oral");
        medicamentoNuevo.setVentaLibre(0); // 0 = con receta
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
    void testListarTodos_NullList() {
        // Arrange
        when(medicamentoRepository.listAll()).thenReturn(null);

        // Act
        List<Medicamento> result = medicamentoService.listarTodos();

        // Assert
        assertNull(result);
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
    void testObtenerPorId_WithNullId() {
        // Arrange
        when(medicamentoRepository.findById(null)).thenReturn(null);

        // Act
        Medicamento result = medicamentoService.obtenerPorId(null);

        // Assert
        assertNull(result);
        verify(medicamentoRepository).findById(null);
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
    void testCrearMedicamento_WithNullMedicamento() {
        // Arrange
        doNothing().when(medicamentoRepository).persist(any(Medicamento.class));

        // Act
        Medicamento result = medicamentoService.crearMedicamento(null);

        // Assert
        assertNull(result);
        verify(medicamentoRepository, never()).persist(any(Medicamento.class));
    }

    @Test
    void testActualizarMedicamento_Success() {
        // Arrange
        when(medicamentoRepository.findById(1L)).thenReturn(testMedicamento);

        // Act
        Medicamento result = medicamentoService.actualizarMedicamento(1L, medicamentoNuevo);

        // Assert
        assertNotNull(result);
        assertEquals(testMedicamento, result);
        assertEquals("Ibuprofeno", testMedicamento.getPrincipioActivo());
        assertEquals("400mg", testMedicamento.getConcentracion());
        assertEquals("Cápsula", testMedicamento.getPresentacion());
        assertEquals("Oral", testMedicamento.getFormaFarmaceutica());
        assertEquals(0, testMedicamento.getVentaLibre()); // 0 = con receta
        verify(medicamentoRepository).findById(1L);
    }

    @Test
    void testActualizarMedicamento_NotFound() {
        // Arrange
        when(medicamentoRepository.findById(999L)).thenReturn(null);

        // Act
        Medicamento result = medicamentoService.actualizarMedicamento(999L, medicamentoNuevo);

        // Assert
        assertNull(result);
        verify(medicamentoRepository).findById(999L);
    }

    @Test
    void testActualizarMedicamento_WithNullId() {
        // Arrange
        when(medicamentoRepository.findById(null)).thenReturn(null);

        // Act
        Medicamento result = medicamentoService.actualizarMedicamento(null, medicamentoNuevo);

        // Assert
        assertNull(result);
        verify(medicamentoRepository).findById(null);
    }

    @Test
    void testActualizarMedicamento_WithNullMedicamentoNuevo() {
        // Arrange
        when(medicamentoRepository.findById(1L)).thenReturn(testMedicamento);

        // Act & Assert
        // El servicio actual no maneja null, por lo que esto debería lanzar NullPointerException
        assertThrows(NullPointerException.class, () -> {
            medicamentoService.actualizarMedicamento(1L, null);
        });
        verify(medicamentoRepository).findById(1L);
    }

    @Test
    void testActualizarMedicamento_WithPartialData() {
        // Arrange
        when(medicamentoRepository.findById(1L)).thenReturn(testMedicamento);
        
        Medicamento medicamentoParcial = new Medicamento();
        medicamentoParcial.setPrincipioActivo("Aspirina");
        medicamentoParcial.setConcentracion("100mg");
        // Solo algunos campos están establecidos, otros serán null

        // Act
        Medicamento result = medicamentoService.actualizarMedicamento(1L, medicamentoParcial);

        // Assert
        assertNotNull(result);
        assertEquals("Aspirina", testMedicamento.getPrincipioActivo());
        assertEquals("100mg", testMedicamento.getConcentracion());
        // Los campos no establecidos se convierten en null
        assertNull(testMedicamento.getPresentacion());
        assertNull(testMedicamento.getFormaFarmaceutica());
        assertNull(testMedicamento.getVentaLibre());
        verify(medicamentoRepository).findById(1L);
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

    @Test
    void testEliminarMedicamento_WithNullId() {
        // Arrange
        when(medicamentoRepository.deleteById(null)).thenReturn(false);

        // Act
        boolean result = medicamentoService.eliminarMedicamento(null);

        // Assert
        assertFalse(result);
        verify(medicamentoRepository).deleteById(null);
    }
}
