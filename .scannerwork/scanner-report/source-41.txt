package com.unis.service;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.unis.model.PacienteAcc;
import com.unis.repository.PacienteAccRepository;

import io.quarkus.hibernate.orm.panache.PanacheQuery;

public class PacienteAccServiceTest {

    @Mock
    PacienteAccRepository pacienteAccRepository;

    @Mock
    PanacheQuery<PacienteAcc> panacheQuery;

    @InjectMocks
    PacienteAccService pacienteAccService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test para obtener un paciente cuando se encuentra
    @Test
    public void testGetPacienteByIdFound() {
        Long id = 1L;
        PacienteAcc paciente = new PacienteAcc();
        when(pacienteAccRepository.find("usuario.idUsuario", id)).thenReturn(panacheQuery);
        when(panacheQuery.firstResultOptional()).thenReturn(Optional.of(paciente));

        Optional<PacienteAcc> result = pacienteAccService.getPacienteById(id);
        assertTrue(result.isPresent());
        assertEquals(paciente, result.get());
    }

    // Test para obtener un paciente cuando no se encuentra
    @Test
    public void testGetPacienteByIdNotFound() {
        Long id = 1L;
        when(pacienteAccRepository.find("usuario.idUsuario", id)).thenReturn(panacheQuery);
        when(panacheQuery.firstResultOptional()).thenReturn(Optional.empty());

        Optional<PacienteAcc> result = pacienteAccService.getPacienteById(id);
        assertFalse(result.isPresent());
    }

    // Test para actualizar un paciente cuando se encuentra
    @Test
    public void testUpdatePacienteFound() {
        Long id = 1L;
        PacienteAcc existingPaciente = new PacienteAcc();
        // Valores iniciales
        existingPaciente.setApellido("OldApellido");
        existingPaciente.setDocumento("OldDocumento");
        existingPaciente.setFechaNacimiento(new Date(100000L));
        existingPaciente.setGenero("M");
        existingPaciente.setTelefono("OldTelefono");

        PacienteAcc updateData = new PacienteAcc();
        // Nuevos valores para actualizar
        updateData.setApellido("NewApellido");
        updateData.setDocumento("NewDocumento");
        updateData.setFechaNacimiento(new Date(200000L));
        updateData.setGenero("F");
        updateData.setTelefono("NewTelefono");

        when(pacienteAccRepository.find("usuario.idUsuario", id)).thenReturn(panacheQuery);
        when(panacheQuery.firstResultOptional()).thenReturn(Optional.of(existingPaciente));

        // Act
        pacienteAccService.updatePaciente(id, updateData);

        // Assert: se han actualizado los campos
        assertEquals("NewApellido", existingPaciente.getApellido());
        assertEquals("NewDocumento", existingPaciente.getDocumento());
        assertEquals(new Date(200000L), existingPaciente.getFechaNacimiento());
        assertEquals("F", existingPaciente.getGenero());
        assertEquals("NewTelefono", existingPaciente.getTelefono());

        // Se verifica que se llamó a persist
        verify(pacienteAccRepository, times(1)).persist(existingPaciente);
    }

    // Test para actualizar un paciente cuando no se encuentra (no se realiza persistencia)
    @Test
    public void testUpdatePacienteNotFound() {
        Long id = 1L;
        PacienteAcc updateData = new PacienteAcc();

        when(pacienteAccRepository.find("usuario.idUsuario", id)).thenReturn(panacheQuery);
        when(panacheQuery.firstResultOptional()).thenReturn(Optional.empty());

        pacienteAccService.updatePaciente(id, updateData);
        verify(pacienteAccRepository, never()).persist(any(PacienteAcc.class));
    }
}
