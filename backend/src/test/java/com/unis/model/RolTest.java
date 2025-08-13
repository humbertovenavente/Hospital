package com.unis.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class RolTest {

    private Rol rol;

    @BeforeEach
    void setUp() {
        rol = new Rol();
    }

    @Test
    void testConstructorPorDefecto() {
        assertNotNull(rol);
    }

    @Test
    void testGettersAndSetters() {
        // Arrange
        Long id = 1L;
        String roleName = "ADMIN";

        // Act
        rol.setId(id);
        rol.setRoleName(roleName);

        // Assert
        assertEquals(id, rol.getId());
        assertEquals(roleName, rol.getRoleName());
    }

    @Test
    void testSetYGetId() {
        // Arrange
        Long id = 999L;

        // Act
        rol.setId(id);

        // Assert
        assertEquals(id, rol.getId());
    }

    @Test
    void testSetYGetRoleName() {
        // Arrange
        String roleName = "DOCTOR";

        // Act
        rol.setRoleName(roleName);

        // Assert
        assertEquals(roleName, rol.getRoleName());
    }

    @Test
    void testSetIdNull() {
        // Act
        rol.setId(null);

        // Assert
        assertNull(rol.getId());
    }

    @Test
    void testSetRoleNameNull() {
        // Act
        rol.setRoleName(null);

        // Assert
        assertNull(rol.getRoleName());
    }

    @Test
    void testSetRoleNameVacio() {
        // Arrange
        String roleNameVacio = "";

        // Act
        rol.setRoleName(roleNameVacio);

        // Assert
        assertEquals(roleNameVacio, rol.getRoleName());
        assertTrue(rol.getRoleName().isEmpty());
    }
}
