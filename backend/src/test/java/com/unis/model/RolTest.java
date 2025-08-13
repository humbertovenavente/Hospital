package com.unis.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RolTest {

    @Test
    public void testRolConstructor() {
        // Arrange & Act
        Rol rol = new Rol();
        
        // Assert
        assertNotNull(rol);
    }

    @Test
    public void testRolSettersAndGetters() {
        // Arrange
        Rol rol = new Rol();
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
    public void testRolToString() {
        // Arrange
        Rol rol = new Rol();
        rol.setId(1L);

        // Act
        String result = rol.toString();

        // Assert
        assertNotNull(result);
    }

    @Test
    public void testRolNotNull() {
        // Arrange
        Rol rol = new Rol();

        // Act & Assert
        assertNotNull(rol);
        assertNotNull(rol.toString());
    }

    @Test
    public void testRolId() {
        // Arrange
        Rol rol = new Rol();
        Long id = 999L;

        // Act
        rol.setId(id);

        // Assert
        assertEquals(id, rol.getId());
    }
}
