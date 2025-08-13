package com.unis.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class TechnicalDebtEmailResponseTest {

    private TechnicalDebtEmailResponse response;

    @BeforeEach
    void setUp() {
        response = new TechnicalDebtEmailResponse();
    }

    @Test
    void testConstructorPorDefecto() {
        assertNotNull(response);
    }

    @Test
    void testConstructorConParametros() {
        // Arrange
        boolean success = true;
        String message = "Email enviado exitosamente";
        String projectKey = "TEST-PROJECT";
        String projectName = "Proyecto de Prueba";
        String recipientEmail = "test@example.com";

        // Act
        TechnicalDebtEmailResponse response = new TechnicalDebtEmailResponse(success, message, projectKey, projectName, recipientEmail);

        // Assert
        assertEquals(success, response.isSuccess());
        assertEquals(message, response.getMessage());
        assertEquals(projectKey, response.getProjectKey());
        assertEquals(projectName, response.getProjectName());
        assertEquals(recipientEmail, response.getRecipientEmail());
    }

    @Test
    void testGettersAndSetters() {
        // Arrange
        boolean success = false;
        String message = "Error al enviar email";
        String projectKey = "ERROR-PROJECT";
        String projectName = "Proyecto con Error";
        String recipientEmail = "error@example.com";

        // Act
        response.setSuccess(success);
        response.setMessage(message);
        response.setProjectKey(projectKey);
        response.setProjectName(projectName);
        response.setRecipientEmail(recipientEmail);

        // Assert
        assertEquals(success, response.isSuccess());
        assertEquals(message, response.getMessage());
        assertEquals(projectKey, response.getProjectKey());
        assertEquals(projectName, response.getProjectName());
        assertEquals(recipientEmail, response.getRecipientEmail());
    }

    @Test
    void testSetSuccess() {
        response.setSuccess(true);
        assertTrue(response.isSuccess());
        
        response.setSuccess(false);
        assertFalse(response.isSuccess());
    }

    @Test
    void testSetMessage() {
        String message = "Nuevo mensaje de prueba";
        response.setMessage(message);
        assertEquals(message, response.getMessage());
    }

    @Test
    void testSetProjectKey() {
        String projectKey = "NEW-PROJECT-KEY";
        response.setProjectKey(projectKey);
        assertEquals(projectKey, response.getProjectKey());
    }

    @Test
    void testSetProjectName() {
        String projectName = "Nuevo Proyecto";
        response.setProjectName(projectName);
        assertEquals(projectName, response.getProjectName());
    }

    @Test
    void testSetRecipientEmail() {
        String recipientEmail = "nuevo@example.com";
        response.setRecipientEmail(recipientEmail);
        assertEquals(recipientEmail, response.getRecipientEmail());
    }

    @Test
    void testSetMessageNull() {
        response.setMessage(null);
        assertNull(response.getMessage());
    }

    @Test
    void testSetProjectKeyNull() {
        response.setProjectKey(null);
        assertNull(response.getProjectKey());
    }

    @Test
    void testSetProjectNameNull() {
        response.setProjectName(null);
        assertNull(response.getProjectName());
    }

    @Test
    void testSetRecipientEmailNull() {
        response.setRecipientEmail(null);
        assertNull(response.getRecipientEmail());
    }

    @Test
    void testConstructorConExito() {
        TechnicalDebtEmailResponse successResponse = new TechnicalDebtEmailResponse(
            true, 
            "Operación exitosa", 
            "SUCCESS-PROJ", 
            "Proyecto Exitoso", 
            "success@example.com"
        );
        
        assertTrue(successResponse.isSuccess());
        assertEquals("Operación exitosa", successResponse.getMessage());
    }

    @Test
    void testConstructorConError() {
        TechnicalDebtEmailResponse errorResponse = new TechnicalDebtEmailResponse(
            false, 
            "Error en la operación", 
            "ERROR-PROJ", 
            "Proyecto con Error", 
            "error@example.com"
        );
        
        assertFalse(errorResponse.isSuccess());
        assertEquals("Error en la operación", errorResponse.getMessage());
    }

    @Test
    void testConstructorConMensajeVacio() {
        TechnicalDebtEmailResponse emptyMessageResponse = new TechnicalDebtEmailResponse(
            true, 
            "", 
            "EMPTY-PROJ", 
            "Proyecto Mensaje Vacío", 
            "empty@example.com"
        );
        
        assertEquals("", emptyMessageResponse.getMessage());
    }

    @Test
    void testConstructorConMensajeLargo() {
        String longMessage = "Este es un mensaje muy largo que contiene mucha información " +
                           "sobre el estado de la operación y puede incluir detalles técnicos " +
                           "que son importantes para el usuario final";
        
        TechnicalDebtEmailResponse longMessageResponse = new TechnicalDebtEmailResponse(
            true, 
            longMessage, 
            "LONG-PROJ", 
            "Proyecto Mensaje Largo", 
            "long@example.com"
        );
        
        assertEquals(longMessage, longMessageResponse.getMessage());
    }
}
