package com.unis.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.unis.model.Doctor;
import com.unis.model.Usuario;
import com.unis.repository.DoctorRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

public class DoctorServiceTest {

    @Mock
    DoctorRepository doctorRepository;

    @Mock
    EntityManager entityManager;

    @Mock
    Query query;

    @InjectMocks
    DoctorService doctorService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test para getAllDoctores
    @Test
    public void testGetAllDoctores() {
        List<Doctor> doctors = Arrays.asList(new Doctor(), new Doctor());
        when(doctorRepository.listAll()).thenReturn(doctors);

        List<Doctor> result = doctorService.getAllDoctores();
        assertEquals(doctors, result);
    }

    // Test para getDoctorById cuando se encuentra el doctor
    @Test
    public void testGetDoctorByIdFound() {
        Doctor doctor = new Doctor();
        when(doctorRepository.findByIdOptional(1L)).thenReturn(Optional.of(doctor));

        Optional<Doctor> result = doctorService.getDoctorById(1L);
        assertTrue(result.isPresent());
        assertEquals(doctor, result.get());
    }

    // Test para getDoctorById cuando no se encuentra
    @Test
    public void testGetDoctorByIdNotFound() {
        when(doctorRepository.findByIdOptional(1L)).thenReturn(Optional.empty());

        Optional<Doctor> result = doctorService.getDoctorById(1L);
        assertFalse(result.isPresent());
    }

    // Test para registrarDoctor exitosamente
    @Test
    public void testRegistrarDoctorSuccess() {
        Doctor doctor = new Doctor();
        // Configuramos los datos necesarios del doctor y su usuario.
        Usuario usuario = new Usuario();
        usuario.setNombreUsuario("nombreUsuario");
        usuario.setCorreo("correo@example.com");
        usuario.setContrasena("contrasena");
        doctor.setUsuario(usuario);
        doctor.setApellido("apellido");
        doctor.setDocumento("documento");
        doctor.setFechaNacimiento(new java.util.Date(100000L));
        doctor.setGenero("M");
        doctor.setTelefono("123456789");
        doctor.setEspecialidad("Cardiología");
        doctor.setNumeroColegiado("12345");
        doctor.setHorarioAtencion("Lunes a Viernes");
        doctor.setFechaGraduacion(new java.util.Date(200000L));
        doctor.setUniversidadGraduacion("Universidad X");

        // Simulamos la creación de la consulta nativa
        when(entityManager.createNativeQuery(any(String.class))).thenReturn(query);
        // Encadenamos setParameter
        when(query.setParameter(anyInt(), any())).thenReturn(query);
        // Simulamos que la ejecución se realiza correctamente
        when(query.executeUpdate()).thenReturn(1);

        // Act: No debe lanzar excepción
        assertDoesNotThrow(() -> doctorService.registrarDoctor(doctor));
        // Verificamos que se haya llamado a executeUpdate una vez
        verify(query, times(1)).executeUpdate();
    }

    // Test para registrarDoctor con error (simulando excepción en executeUpdate)
    @Test
    public void testRegistrarDoctorError() {
        Doctor doctor = new Doctor();
        Usuario usuario = new Usuario();
        usuario.setNombreUsuario("nombreUsuario");
        usuario.setCorreo("correo@example.com");
        usuario.setContrasena("contrasena");
        doctor.setUsuario(usuario);
        doctor.setApellido("apellido");
        doctor.setDocumento("documento");
        doctor.setFechaNacimiento(new java.util.Date(100000L));
        doctor.setGenero("M");
        doctor.setTelefono("123456789");
        doctor.setEspecialidad("Cardiología");
        doctor.setNumeroColegiado("12345");
        doctor.setHorarioAtencion("Lunes a Viernes");
        doctor.setFechaGraduacion(new java.util.Date(200000L));
        doctor.setUniversidadGraduacion("Universidad X");

        when(entityManager.createNativeQuery(any(String.class))).thenReturn(query);
        when(query.setParameter(anyInt(), any())).thenReturn(query);
        when(query.executeUpdate()).thenThrow(new RuntimeException("Error"));

        WebApplicationException exception = assertThrows(WebApplicationException.class, () -> {
            doctorService.registrarDoctor(doctor);
        });
        assertEquals("Error en la transacción.", exception.getMessage());
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), exception.getResponse().getStatus());
    }

    // Test para actualizarDoctor exitosamente
    @Test
    public void testActualizarDoctorSuccess() {
        Doctor updateData = new Doctor();
        Usuario usuario = new Usuario();
        usuario.setNombreUsuario("nuevoNombre");
        usuario.setCorreo("nuevoCorreo");
        usuario.setContrasena("nuevaContrasena");
        updateData.setUsuario(usuario);
        updateData.setApellido("nuevoApellido");
        updateData.setDocumento("nuevoDocumento");
        updateData.setFechaNacimiento(new java.util.Date(300000L));
        updateData.setGenero("F");
        updateData.setTelefono("987654321");
        updateData.setEspecialidad("Neurología");
        updateData.setNumeroColegiado("67890");
        updateData.setHorarioAtencion("Martes a Sábados");
        updateData.setFechaGraduacion(new java.util.Date(400000L));
        updateData.setUniversidadGraduacion("Universidad Y");

        when(entityManager.createNativeQuery(any(String.class))).thenReturn(query);
        when(query.setParameter(anyInt(), any())).thenReturn(query);
        when(query.executeUpdate()).thenReturn(1);

        boolean result = doctorService.actualizarDoctor(1L, updateData);
        assertTrue(result);
        verify(query, times(1)).executeUpdate();
    }

    // Test para actualizarDoctor con error
    @Test
    public void testActualizarDoctorError() {
        Doctor updateData = new Doctor();
        Usuario usuario = new Usuario();
        usuario.setNombreUsuario("nuevoNombre");
        usuario.setCorreo("nuevoCorreo");
        usuario.setContrasena("nuevaContrasena");
        updateData.setUsuario(usuario);
        // Otros campos se pueden configurar si se requiere

        when(entityManager.createNativeQuery(any(String.class))).thenReturn(query);
        when(query.setParameter(anyInt(), any())).thenReturn(query);
        when(query.executeUpdate()).thenThrow(new RuntimeException("Error"));

        boolean result = doctorService.actualizarDoctor(1L, updateData);
        assertFalse(result);
    }

    // Test para eliminarDoctor exitosamente
    @Test
    public void testEliminarDoctorSuccess() {
        when(entityManager.createNativeQuery(any(String.class))).thenReturn(query);
        when(query.setParameter(anyInt(), any())).thenReturn(query);
        when(query.executeUpdate()).thenReturn(1);

        boolean result = doctorService.eliminarDoctor(1L);
        assertTrue(result);
        verify(query, times(1)).executeUpdate();
    }

    // Test para eliminarDoctor con error
    @Test
    public void testEliminarDoctorError() {
        when(entityManager.createNativeQuery(any(String.class))).thenReturn(query);
        when(query.setParameter(anyInt(), any())).thenReturn(query);
        when(query.executeUpdate()).thenThrow(new RuntimeException("Error"));

        boolean result = doctorService.eliminarDoctor(1L);
        assertFalse(result);
    }
}
