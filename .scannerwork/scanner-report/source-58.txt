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
import static org.mockito.Mockito.times; // Add this import
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.unis.model.UserAcc;
import com.unis.repository.DoctorAccRepository;
import com.unis.repository.UserAccRepository;

class UserAccServiceTest {

    @Mock
    UserAccRepository userAccRepository;

    @Mock
    DoctorAccRepository doctorAccRepository;

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
    void testChangeUserRole() {
        UserAcc user = new UserAcc();
        user.setRolId(2); // Asumimos que el usuario tiene rol de doctor inicialmente

        when(userAccRepository.findById(1L)).thenReturn(user);

        userAccService.changeUserRole(1L, 3); // Cambiar rol a empleado

        assertEquals(3, user.getRolId());
        verify(doctorAccRepository, times(1)).delete("idUsuario", 1L);
        verify(userAccRepository, times(1)).persist(user);
    }

    @Test
    void testChangeUserRoleUserNotFound() {
        when(userAccRepository.findById(1L)).thenReturn(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userAccService.changeUserRole(1L, 3);
        });

        assertEquals("Usuario no encontrado.", exception.getMessage());
    }
}
