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

import com.unis.model.PacienteFT;
import com.unis.repository.PacienteFTRepository;

public class PacienteFTServiceTest {

    @Mock
    PacienteFTRepository pacienteFTRepository;

    @InjectMocks
    PacienteFTService pacienteFTService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test para obtener todos los pacientes (getAllPacientes)
    @Test
    public void testGetAllPacientes() {
        List<PacienteFT> expectedList = Arrays.asList(new PacienteFT(), new PacienteFT());
        when(pacienteFTRepository.listAll()).thenReturn(expectedList);

        List<PacienteFT> result = pacienteFTService.getAllPacientes();
        assertEquals(expectedList, result, "La lista de pacientes debe coincidir con la esperada");
    }

    // Test para obtener un paciente por ID cuando se encuentra
    @Test
    public void testGetPacienteByIdFound() {
        Long id = 1L;
        PacienteFT paciente = new PacienteFT();
        when(pacienteFTRepository.findByIdOptional(id)).thenReturn(Optional.of(paciente));

        Optional<PacienteFT> result = pacienteFTService.getPacienteById(id);
        assertTrue(result.isPresent(), "El paciente debería encontrarse");
        assertEquals(paciente, result.get(), "El paciente obtenido debe ser el esperado");
    }

    // Test para obtener un paciente por ID cuando no se encuentra
    @Test
    public void testGetPacienteByIdNotFound() {
        Long id = 1L;
        when(pacienteFTRepository.findByIdOptional(id)).thenReturn(Optional.empty());

        Optional<PacienteFT> result = pacienteFTService.getPacienteById(id);
        assertFalse(result.isPresent(), "No se debe encontrar un paciente para el ID dado");
    }

    // Test para registrar un paciente (registrarPaciente)
    @Test
    public void testRegistrarPaciente() {
        PacienteFT paciente = new PacienteFT();
        // Llamamos al método a testear
        pacienteFTService.registrarPaciente(paciente);
        // Verificamos que se llame al método persist del repositorio
        verify(pacienteFTRepository, times(1)).persist(paciente);
    }
}
