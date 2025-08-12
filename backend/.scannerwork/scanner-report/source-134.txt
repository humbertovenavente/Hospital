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

import com.unis.model.DoctorAcc;
import com.unis.repository.DoctorAccRepository;

import io.quarkus.hibernate.orm.panache.PanacheQuery;

public class DoctorAccServiceTest {

    @Mock
    DoctorAccRepository doctorAccRepository;

    @Mock
    PanacheQuery<DoctorAcc> panacheQuery;

    @InjectMocks
    DoctorAccService doctorAccService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Prueba cuando se encuentra el doctor por ID
    @Test
    public void testGetDoctorByIdFound() {
        Long id = 1L;
        DoctorAcc doctor = new DoctorAcc();
        when(doctorAccRepository.find("usuario.idUsuario", id)).thenReturn(panacheQuery);
        when(panacheQuery.firstResultOptional()).thenReturn(Optional.of(doctor));

        Optional<DoctorAcc> result = doctorAccService.getDoctorById(id);

        assertTrue(result.isPresent());
        assertEquals(doctor, result.get());
    }

    // Prueba cuando no se encuentra el doctor por ID
    @Test
    public void testGetDoctorByIdNotFound() {
        Long id = 1L;
        when(doctorAccRepository.find("usuario.idUsuario", id)).thenReturn(panacheQuery);
        when(panacheQuery.firstResultOptional()).thenReturn(Optional.empty());

        Optional<DoctorAcc> result = doctorAccService.getDoctorById(id);

        assertFalse(result.isPresent());
    }

    // Prueba para actualizar el doctor exitosamente
    @Test
    public void testUpdateDoctorSuccessful() {
        Long id = 1L;
        DoctorAcc existingDoctor = new DoctorAcc();
        // Asignar valores iniciales usando Date en lugar de String
        existingDoctor.setApellido("OldApellido");
        existingDoctor.setDocumento("OldDocumento");
        existingDoctor.setFechaNacimiento(new Date(1000000000L)); // Fecha antigua
        existingDoctor.setGenero("OldGenero");
        existingDoctor.setTelefono("OldTelefono");
        existingDoctor.setEspecialidad("OldEspecialidad");
        existingDoctor.setNumeroColegiado("OldNumeroColegiado");
        existingDoctor.setFechaGraduacion(new Date(1000000000L)); // Fecha antigua
        existingDoctor.setUniversidadGraduacion("OldUniversidadGraduacion");
        existingDoctor.setDisponibilidad("OldDisponibilidad");

        DoctorAcc updateData = new DoctorAcc();
        // Nuevos valores para actualizar, usando Date
        updateData.setApellido("NewApellido");
        updateData.setDocumento("NewDocumento");
        updateData.setFechaNacimiento(new Date(2000000000L)); // Nueva fecha
        updateData.setGenero("NewGenero");
        updateData.setTelefono("NewTelefono");
        updateData.setEspecialidad("NewEspecialidad");
        updateData.setNumeroColegiado("NewNumeroColegiado");
        updateData.setFechaGraduacion(new Date(2000000000L)); // Nueva fecha
        updateData.setUniversidadGraduacion("NewUniversidadGraduacion");
        updateData.setDisponibilidad("NewDisponibilidad");

        when(doctorAccRepository.find("usuario.idUsuario", id)).thenReturn(panacheQuery);
        when(panacheQuery.firstResultOptional()).thenReturn(Optional.of(existingDoctor));

        // Act
        doctorAccService.updateDoctor(id, updateData);

        // Assert: verificar que se hayan actualizado los campos
        assertEquals("NewApellido", existingDoctor.getApellido());
        assertEquals("NewDocumento", existingDoctor.getDocumento());
        assertEquals(new Date(2000000000L), existingDoctor.getFechaNacimiento());
        assertEquals("NewGenero", existingDoctor.getGenero());
        assertEquals("NewTelefono", existingDoctor.getTelefono());
        assertEquals("NewEspecialidad", existingDoctor.getEspecialidad());
        assertEquals("NewNumeroColegiado", existingDoctor.getNumeroColegiado());
        assertEquals(new Date(2000000000L), existingDoctor.getFechaGraduacion());
        assertEquals("NewUniversidadGraduacion", existingDoctor.getUniversidadGraduacion());
        assertEquals("NewDisponibilidad", existingDoctor.getDisponibilidad());

        // Para resolver la ambigüedad, se hace cast explícito
        verify(doctorAccRepository, times(1)).persist((DoctorAcc) existingDoctor);
    }

    // Prueba para actualizar cuando no se encuentra el doctor (no se realiza persistencia)
    @Test
    public void testUpdateDoctorNotFound() {
        Long id = 1L;
        DoctorAcc updateData = new DoctorAcc();
        // Configura los datos de actualización según sea necesario

        when(doctorAccRepository.find("usuario.idUsuario", id)).thenReturn(panacheQuery);
        when(panacheQuery.firstResultOptional()).thenReturn(Optional.empty());

        // Act
        doctorAccService.updateDoctor(id, updateData);

        // Assert: verifica que no se haya llamado a persist, ya que no se encontró el doctor
        verify(doctorAccRepository, never()).persist((DoctorAcc) any());
    }
}
