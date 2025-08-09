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

import com.unis.model.EmpleadoAcc;
import com.unis.repository.EmpleadoAccRepository;

import io.quarkus.hibernate.orm.panache.PanacheQuery;

public class EmpleadoAccServiceTest {

    @Mock
    EmpleadoAccRepository empleadoAccRepository;

    @Mock
    PanacheQuery<EmpleadoAcc> panacheQuery;

    @InjectMocks
    EmpleadoAccService empleadoAccService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test para getEmpleadoById cuando se encuentra el empleado
    @Test
    public void testGetEmpleadoByIdFound() {
        Long id = 1L;
        EmpleadoAcc empleado = new EmpleadoAcc();
        when(empleadoAccRepository.find("usuario.idUsuario", id)).thenReturn(panacheQuery);
        when(panacheQuery.firstResultOptional()).thenReturn(Optional.of(empleado));

        Optional<EmpleadoAcc> result = empleadoAccService.getEmpleadoById(id);
        assertTrue(result.isPresent());
        assertEquals(empleado, result.get());
    }

    // Test para getEmpleadoById cuando no se encuentra el empleado
    @Test
    public void testGetEmpleadoByIdNotFound() {
        Long id = 1L;
        when(empleadoAccRepository.find("usuario.idUsuario", id)).thenReturn(panacheQuery);
        when(panacheQuery.firstResultOptional()).thenReturn(Optional.empty());

        Optional<EmpleadoAcc> result = empleadoAccService.getEmpleadoById(id);
        assertFalse(result.isPresent());
    }

    // Test para actualizarEmpleado cuando se encuentra el empleado
    @Test
    public void testUpdateEmpleadoFound() {
        Long id = 1L;
        EmpleadoAcc existingEmpleado = new EmpleadoAcc();
        // Asignar valores iniciales
        existingEmpleado.setApellido("OldApellido");
        existingEmpleado.setDocumento("OldDocumento");
        existingEmpleado.setFechaNacimiento(new Date(100000L));
        existingEmpleado.setGenero("OldGenero");
        existingEmpleado.setTelefono("OldTelefono");
        existingEmpleado.setPuesto("OldPuesto");

        EmpleadoAcc updateData = new EmpleadoAcc();
        // Nuevos valores para actualizar
        updateData.setApellido("NewApellido");
        updateData.setDocumento("NewDocumento");
        updateData.setFechaNacimiento(new Date(200000L));
        updateData.setGenero("NewGenero");
        updateData.setTelefono("NewTelefono");
        updateData.setPuesto("NewPuesto");

        when(empleadoAccRepository.find("usuario.idUsuario", id)).thenReturn(panacheQuery);
        when(panacheQuery.firstResultOptional()).thenReturn(Optional.of(existingEmpleado));

        // Act: Se actualiza el empleado
        empleadoAccService.updateEmpleado(id, updateData);

        // Assert: se verifican que se hayan actualizado los campos
        assertEquals("NewApellido", existingEmpleado.getApellido());
        assertEquals("NewDocumento", existingEmpleado.getDocumento());
        assertEquals(new Date(200000L), existingEmpleado.getFechaNacimiento());
        assertEquals("NewGenero", existingEmpleado.getGenero());
        assertEquals("NewTelefono", existingEmpleado.getTelefono());
        assertEquals("NewPuesto", existingEmpleado.getPuesto());

        verify(empleadoAccRepository, times(1)).persist(existingEmpleado);
    }

    // Test para actualizarEmpleado cuando no se encuentra (no se realiza persistencia)
    @Test
    public void testUpdateEmpleadoNotFound() {
        Long id = 1L;
        EmpleadoAcc updateData = new EmpleadoAcc();
        // Configuramos los datos de actualización si fuera necesario

        when(empleadoAccRepository.find("usuario.idUsuario", id)).thenReturn(panacheQuery);
        when(panacheQuery.firstResultOptional()).thenReturn(Optional.empty());

        // Act: Se intenta actualizar sin encontrar el empleado
        empleadoAccService.updateEmpleado(id, updateData);

        // Assert: Verificamos que no se haya llamado a persist
        verify(empleadoAccRepository, never()).persist(any(EmpleadoAcc.class));
    }
}
