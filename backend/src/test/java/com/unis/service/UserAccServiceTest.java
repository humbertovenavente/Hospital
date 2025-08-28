package com.unis.service;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.anyLong;
import org.mockito.MockitoAnnotations;

import com.unis.model.UserAcc;
import com.unis.repository.DoctorAccRepository;
import com.unis.repository.EmpleadoAccRepository;
import com.unis.repository.PacienteAccRepository;
import com.unis.repository.UserAccRepository;
import com.unis.repository.UsuarioInterAccRepository;

class UserAccServiceTest {

    @Mock
    UserAccRepository userAccRepository;

    @Mock
    DoctorAccRepository doctorAccRepository;

    @Mock
    EmpleadoAccRepository empleadoAccRepository;

    @Mock
    PacienteAccRepository pacienteAccRepository;

    @Mock
    UsuarioInterAccRepository usuarioInterAccRepository;

    @InjectMocks
    UserAccService userAccService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUserByIdFound() {
        UserAcc user = new UserAcc();
        user.setNombreUsuario("testUser");
        when(userAccRepository.findByIdOptional(1L)).thenReturn(Optional.of(user));

        Optional<UserAcc> result = userAccService.getUserById(1L);
        assertTrue(result.isPresent());
        assertEquals("testUser", result.get().getNombreUsuario());
    }

    @Test
    void testGetUserByIdNotFound() {
        when(userAccRepository.findByIdOptional(1L)).thenReturn(Optional.empty());

        Optional<UserAcc> result = userAccService.getUserById(1L);
        assertFalse(result.isPresent());
    }

    @Test
    void testUpdateUser() {
        UserAcc existingUser = new UserAcc();
        existingUser.setNombreUsuario("oldUser");
        existingUser.setCorreo("old@example.com");

        UserAcc updatedUser = new UserAcc();
        updatedUser.setNombreUsuario("newUser");
        updatedUser.setCorreo("new@example.com");
        updatedUser.setContrasena("newPassword");

        when(userAccRepository.findById(1L)).thenReturn(existingUser);

        userAccService.updateUser(1L, updatedUser);

        assertEquals("newUser", existingUser.getNombreUsuario());
        assertEquals("new@example.com", existingUser.getCorreo());
        assertEquals("newPassword", existingUser.getContrasena());
        verify(userAccRepository, times(1)).persist(existingUser);
    }

    @Test
    void testUpdateUserNotFound() {
        UserAcc updatedUser = new UserAcc();
        updatedUser.setNombreUsuario("newUser");
        updatedUser.setCorreo("new@example.com");

        when(userAccRepository.findById(1L)).thenReturn(null);

        userAccService.updateUser(1L, updatedUser);

        // Should not persist anything if user not found
        verify(userAccRepository, never()).persist(any(UserAcc.class));
    }

    @Test
    void testChangeUserRoleFromDoctor() {
        UserAcc user = new UserAcc();
        user.setRolId(2); // Doctor role

        when(userAccRepository.findById(1L)).thenReturn(user);

        userAccService.changeUserRole(1L, 3); // Change to empleado role

        assertEquals(3, user.getRolId());
        verify(doctorAccRepository, times(1)).delete("idUsuario", 1L);
        verify(empleadoAccRepository, never()).delete(anyString(), anyLong());
        verify(pacienteAccRepository, never()).delete(anyString(), anyLong());
        verify(usuarioInterAccRepository, never()).delete(anyString(), anyLong());
        verify(userAccRepository, times(1)).persist(user);
    }

    @Test
    void testChangeUserRoleFromEmpleado() {
        UserAcc user = new UserAcc();
        user.setRolId(3); // Empleado role

        when(userAccRepository.findById(1L)).thenReturn(user);

        userAccService.changeUserRole(1L, 4); // Change to paciente role

        assertEquals(4, user.getRolId());
        verify(empleadoAccRepository, times(1)).delete("idUsuario", 1L);
        verify(doctorAccRepository, never()).delete(anyString(), anyLong());
        verify(pacienteAccRepository, never()).delete(anyString(), anyLong());
        verify(usuarioInterAccRepository, never()).delete(anyString(), anyLong());
        verify(userAccRepository, times(1)).persist(user);
    }

    @Test
    void testChangeUserRoleFromPaciente() {
        UserAcc user = new UserAcc();
        user.setRolId(4); // Paciente role

        when(userAccRepository.findById(1L)).thenReturn(user);

        userAccService.changeUserRole(1L, 5); // Change to usuarioInter role

        assertEquals(5, user.getRolId());
        verify(pacienteAccRepository, times(1)).delete("idUsuario", 1L);
        verify(doctorAccRepository, never()).delete(anyString(), anyLong());
        verify(empleadoAccRepository, never()).delete(anyString(), anyLong());
        verify(usuarioInterAccRepository, never()).delete(anyString(), anyLong());
        verify(userAccRepository, times(1)).persist(user);
    }

    @Test
    void testChangeUserRoleFromUsuarioInter() {
        UserAcc user = new UserAcc();
        user.setRolId(5); // UsuarioInter role

        when(userAccRepository.findById(1L)).thenReturn(user);

        userAccService.changeUserRole(1L, 2); // Change to doctor role

        assertEquals(2, user.getRolId());
        verify(usuarioInterAccRepository, times(1)).delete("idUsuario", 1L);
        verify(doctorAccRepository, never()).delete(anyString(), anyLong());
        verify(empleadoAccRepository, never()).delete(anyString(), anyLong());
        verify(pacienteAccRepository, never()).delete(anyString(), anyLong());
        verify(userAccRepository, times(1)).persist(user);
    }

    @Test
    void testChangeUserRoleFromUnknownRole() {
        UserAcc user = new UserAcc();
        user.setRolId(99); // Unknown role

        when(userAccRepository.findById(1L)).thenReturn(user);

        userAccService.changeUserRole(1L, 2); // Change to doctor role

        assertEquals(2, user.getRolId());
        // Should not delete anything for unknown role
        verify(doctorAccRepository, never()).delete(anyString(), anyLong());
        verify(empleadoAccRepository, never()).delete(anyString(), anyLong());
        verify(pacienteAccRepository, never()).delete(anyString(), anyLong());
        verify(usuarioInterAccRepository, never()).delete(anyString(), anyLong());
        verify(userAccRepository, times(1)).persist(user);
    }

    @Test
    void testChangeUserRoleUserNotFound() {
        when(userAccRepository.findById(1L)).thenReturn(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userAccService.changeUserRole(1L, 3);
        });

        assertEquals("Usuario no encontrado.", exception.getMessage());
        
        // Should not delete anything if user not found
        verify(doctorAccRepository, never()).delete(anyString(), anyLong());
        verify(empleadoAccRepository, never()).delete(anyString(), anyLong());
        verify(pacienteAccRepository, never()).delete(anyString(), anyLong());
        verify(usuarioInterAccRepository, never()).delete(anyString(), anyLong());
        verify(userAccRepository, never()).persist(any(UserAcc.class));
    }
}
