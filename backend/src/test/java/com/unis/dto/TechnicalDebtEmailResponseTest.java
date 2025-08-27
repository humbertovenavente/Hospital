package com.unis.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

/**
 * Test class for TechnicalDebtEmailResponse DTO.
 * Tests all constructors, getters, setters, and methods to achieve 100% coverage.
 */
class TechnicalDebtEmailResponseTest {

    private TechnicalDebtEmailResponse response;
    private LocalDateTime testTimestamp;

    @BeforeEach
    void setUp() {
        testTimestamp = LocalDateTime.of(2024, 8, 26, 14, 30, 0);
    }

    @Test
    void testDefaultConstructor() {
        response = new TechnicalDebtEmailResponse();
        
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertNull(response.getMessage());
        assertNotNull(response.getTimestamp());
        assertNull(response.getProjectKey());
        assertNull(response.getProjectName());
        assertNull(response.getRecipientEmail());
        
        // Verify timestamp is set to current time (within reasonable range)
        LocalDateTime now = LocalDateTime.now();
        assertTrue(response.getTimestamp().isAfter(now.minusSeconds(5)));
        assertTrue(response.getTimestamp().isBefore(now.plusSeconds(5)));
    }

    @Test
    void testBasicConstructor() {
        response = new TechnicalDebtEmailResponse(true, "Test message");
        
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals("Test message", response.getMessage());
        assertNotNull(response.getTimestamp());
        assertNull(response.getProjectKey());
        assertNull(response.getProjectName());
        assertNull(response.getRecipientEmail());
    }

    @Test
    void testBasicConstructorWithFalseSuccess() {
        response = new TechnicalDebtEmailResponse(false, "Error message");
        
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals("Error message", response.getMessage());
        assertNotNull(response.getTimestamp());
    }

    @Test
    void testCompleteConstructor() {
        response = new TechnicalDebtEmailResponse(
            true, 
            "Success message", 
            "PROJ-001", 
            "Test Project", 
            "test@example.com"
        );
        
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals("Success message", response.getMessage());
        assertNotNull(response.getTimestamp());
        assertEquals("PROJ-001", response.getProjectKey());
        assertEquals("Test Project", response.getProjectName());
        assertEquals("test@example.com", response.getRecipientEmail());
    }

    @Test
    void testCompleteConstructorWithFalseSuccess() {
        response = new TechnicalDebtEmailResponse(
            false, 
            "Failure message", 
            "PROJ-002", 
            "Another Project", 
            "admin@example.com"
        );
        
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals("Failure message", response.getMessage());
        assertEquals("PROJ-002", response.getProjectKey());
        assertEquals("Another Project", response.getProjectName());
        assertEquals("admin@example.com", response.getRecipientEmail());
    }

    // ========== SUCCESS Tests ==========
    
    @Test
    void testSetAndGetSuccess() {
        response = new TechnicalDebtEmailResponse();
        
        response.setSuccess(true);
        assertTrue(response.isSuccess());
        
        response.setSuccess(false);
        assertFalse(response.isSuccess());
    }

    @Test
    void testSuccessEdgeCases() {
        response = new TechnicalDebtEmailResponse();
        
        // Test multiple toggles
        response.setSuccess(true);
        assertTrue(response.isSuccess());
        
        response.setSuccess(false);
        assertFalse(response.isSuccess());
        
        response.setSuccess(true);
        assertTrue(response.isSuccess());
    }

    // ========== MESSAGE Tests ==========
    
    @Test
    void testSetAndGetMessage() {
        response = new TechnicalDebtEmailResponse();
        
        String message = "Test message";
        response.setMessage(message);
        assertEquals(message, response.getMessage());
    }

    @Test
    void testSetAndGetMessageWithNull() {
        response = new TechnicalDebtEmailResponse();
        
        response.setMessage(null);
        assertNull(response.getMessage());
    }

    @Test
    void testSetAndGetMessageWithEmptyString() {
        response = new TechnicalDebtEmailResponse();
        
        response.setMessage("");
        assertEquals("", response.getMessage());
    }

    @Test
    void testSetAndGetMessageWithWhitespace() {
        response = new TechnicalDebtEmailResponse();
        
        response.setMessage("   ");
        assertEquals("   ", response.getMessage());
    }

    @Test
    void testSetAndGetMessageWithLongText() {
        response = new TechnicalDebtEmailResponse();
        
        String longMessage = "This is a very long message that contains multiple sentences and should test the ability of the DTO to handle lengthy text content without any issues.";
        response.setMessage(longMessage);
        assertEquals(longMessage, response.getMessage());
    }

    @Test
    void testSetAndGetMessageWithSpecialCharacters() {
        response = new TechnicalDebtEmailResponse();
        
        String specialMessage = "Message with special chars: áéíóú ñ @#$%^&*()_+-=[]{}|;':\",./<>?";
        response.setMessage(specialMessage);
        assertEquals(specialMessage, response.getMessage());
    }

    // ========== TIMESTAMP Tests ==========
    
    @Test
    void testSetAndGetTimestamp() {
        response = new TechnicalDebtEmailResponse();
        
        response.setTimestamp(testTimestamp);
        assertEquals(testTimestamp, response.getTimestamp());
    }

    @Test
    void testSetAndGetTimestampWithNull() {
        response = new TechnicalDebtEmailResponse();
        
        response.setTimestamp(null);
        assertNull(response.getTimestamp());
    }

    @Test
    void testSetAndGetTimestampWithCurrentTime() {
        response = new TechnicalDebtEmailResponse();
        
        LocalDateTime currentTime = LocalDateTime.now();
        response.setTimestamp(currentTime);
        assertEquals(currentTime, response.getTimestamp());
    }

    @Test
    void testSetAndGetTimestampWithPastTime() {
        response = new TechnicalDebtEmailResponse();
        
        LocalDateTime pastTime = LocalDateTime.of(2020, 1, 1, 0, 0, 0);
        response.setTimestamp(pastTime);
        assertEquals(pastTime, response.getTimestamp());
    }

    @Test
    void testSetAndGetTimestampWithFutureTime() {
        response = new TechnicalDebtEmailResponse();
        
        LocalDateTime futureTime = LocalDateTime.of(2030, 12, 31, 23, 59, 59);
        response.setTimestamp(futureTime);
        assertEquals(futureTime, response.getTimestamp());
    }

    // ========== PROJECT_KEY Tests ==========
    
    @Test
    void testSetAndGetProjectKey() {
        response = new TechnicalDebtEmailResponse();
        
        String projectKey = "PROJ-123";
        response.setProjectKey(projectKey);
        assertEquals(projectKey, response.getProjectKey());
    }

    @Test
    void testSetAndGetProjectKeyWithNull() {
        response = new TechnicalDebtEmailResponse();
        
        response.setProjectKey(null);
        assertNull(response.getProjectKey());
    }

    @Test
    void testSetAndGetProjectKeyWithEmptyString() {
        response = new TechnicalDebtEmailResponse();
        
        response.setProjectKey("");
        assertEquals("", response.getProjectKey());
    }

    @Test
    void testSetAndGetProjectKeyWithWhitespace() {
        response = new TechnicalDebtEmailResponse();
        
        response.setProjectKey("   ");
        assertEquals("   ", response.getProjectKey());
    }

    @Test
    void testSetAndGetProjectKeyWithDifferentFormats() {
        response = new TechnicalDebtEmailResponse();
        
        String[] projectKeys = {
            "PROJ-001",
            "BACKEND-2024",
            "FRONTEND_DEV",
            "MOBILE-APP",
            "API-GATEWAY"
        };
        
        for (String key : projectKeys) {
            response.setProjectKey(key);
            assertEquals(key, response.getProjectKey());
        }
    }

    // ========== PROJECT_NAME Tests ==========
    
    @Test
    void testSetAndGetProjectName() {
        response = new TechnicalDebtEmailResponse();
        
        String projectName = "Hospital Management System";
        response.setProjectName(projectName);
        assertEquals(projectName, response.getProjectName());
    }

    @Test
    void testSetAndGetProjectNameWithNull() {
        response = new TechnicalDebtEmailResponse();
        
        response.setProjectName(null);
        assertNull(response.getProjectName());
    }

    @Test
    void testSetAndGetProjectNameWithEmptyString() {
        response = new TechnicalDebtEmailResponse();
        
        response.setProjectName("");
        assertEquals("", response.getProjectName());
    }

    @Test
    void testSetAndGetProjectNameWithWhitespace() {
        response = new TechnicalDebtEmailResponse();
        
        response.setProjectName("   ");
        assertEquals("   ", response.getProjectName());
    }

    @Test
    void testSetAndGetProjectNameWithLongText() {
        response = new TechnicalDebtEmailResponse();
        
        String longName = "Comprehensive Hospital Management and Patient Care System with Advanced Analytics and Reporting Capabilities";
        response.setProjectName(longName);
        assertEquals(longName, response.getProjectName());
    }

    @Test
    void testSetAndGetProjectNameWithSpecialCharacters() {
        response = new TechnicalDebtEmailResponse();
        
        String specialName = "Hospital System v2.0 - Beta (Test Environment)";
        response.setProjectName(specialName);
        assertEquals(specialName, response.getProjectName());
    }

    // ========== RECIPIENT_EMAIL Tests ==========
    
    @Test
    void testSetAndGetRecipientEmail() {
        response = new TechnicalDebtEmailResponse();
        
        String email = "admin@hospital.com";
        response.setRecipientEmail(email);
        assertEquals(email, response.getRecipientEmail());
    }

    @Test
    void testSetAndGetRecipientEmailWithNull() {
        response = new TechnicalDebtEmailResponse();
        
        response.setRecipientEmail(null);
        assertNull(response.getRecipientEmail());
    }

    @Test
    void testSetAndGetRecipientEmailWithEmptyString() {
        response = new TechnicalDebtEmailResponse();
        
        response.setRecipientEmail("");
        assertEquals("", response.getRecipientEmail());
    }

    @Test
    void testSetAndGetRecipientEmailWithWhitespace() {
        response = new TechnicalDebtEmailResponse();
        
        response.setRecipientEmail("   ");
        assertEquals("   ", response.getRecipientEmail());
    }

    @Test
    void testSetAndGetRecipientEmailWithDifferentFormats() {
        response = new TechnicalDebtEmailResponse();
        
        String[] emails = {
            "user@example.com",
            "admin@hospital.org",
            "test.user+tag@domain.co.uk",
            "firstname.lastname@company-name.com",
            "123@456.789"
        };
        
        for (String email : emails) {
            response.setRecipientEmail(email);
            assertEquals(email, response.getRecipientEmail());
        }
    }

    // ========== TOSTRING Tests ==========
    
    @Test
    void testToStringWithDefaultValues() {
        response = new TechnicalDebtEmailResponse();
        
        String result = response.toString();
        assertNotNull(result);
        assertTrue(result.contains("TechnicalDebtEmailResponse"));
    }

    @Test
    void testToStringWithAllValuesSet() {
        response = new TechnicalDebtEmailResponse(
            true,
            "Success message",
            "PROJ-001",
            "Test Project",
            "test@example.com"
        );
        
        String result = response.toString();
        assertNotNull(result);
        assertTrue(result.contains("success=true"));
        assertTrue(result.contains("Success message"));
        assertTrue(result.contains("PROJ-001"));
    }

    @Test
    void testToStringWithSpecialCharacters() {
        response = new TechnicalDebtEmailResponse();
        response.setMessage("Message with \"quotes\" and 'apostrophes'");
        response.setProjectName("Project & Name");
        
        String result = response.toString();
        assertNotNull(result);
        assertTrue(result.contains("Message with \"quotes\" and 'apostrophes'"));
        assertTrue(result.contains("Project & Name"));
    }

    // ========== Comprehensive Tests ==========
    
    @Test
    void testCompleteSetupAndUpdate() {
        // Arrange
        response = new TechnicalDebtEmailResponse();
        
        // Act - Set initial values
        response.setSuccess(true);
        response.setMessage("Initial message");
        response.setTimestamp(testTimestamp);
        response.setProjectKey("INIT-001");
        response.setProjectName("Initial Project");
        response.setRecipientEmail("init@example.com");
        
        // Assert - Verify initial values
        assertTrue(response.isSuccess());
        assertEquals("Initial message", response.getMessage());
        assertEquals(testTimestamp, response.getTimestamp());
        assertEquals("INIT-001", response.getProjectKey());
        assertEquals("Initial Project", response.getProjectName());
        assertEquals("init@example.com", response.getRecipientEmail());
        
        // Act - Update values
        response.setSuccess(false);
        response.setMessage("Updated message");
        response.setProjectKey("UPD-002");
        response.setProjectName("Updated Project");
        response.setRecipientEmail("updated@example.com");
        
        // Assert - Verify updated values
        assertFalse(response.isSuccess());
        assertEquals("Updated message", response.getMessage());
        assertEquals("UPD-002", response.getProjectKey());
        assertEquals("Updated Project", response.getProjectName());
        assertEquals("updated@example.com", response.getRecipientEmail());
        
        // Timestamp should remain unchanged
        assertEquals(testTimestamp, response.getTimestamp());
    }

    @Test
    void testResetToNull() {
        // Arrange
        response = new TechnicalDebtEmailResponse(
            true,
            "Test message",
            "PROJ-001",
            "Test Project",
            "test@example.com"
        );
        
        // Act - Reset to null
        response.setMessage(null);
        response.setProjectKey(null);
        response.setProjectName(null);
        response.setRecipientEmail(null);
        
        // Assert
        assertNull(response.getMessage());
        assertNull(response.getProjectKey());
        assertNull(response.getProjectName());
        assertNull(response.getRecipientEmail());
        
        // Success and timestamp should remain unchanged
        assertTrue(response.isSuccess());
        assertNotNull(response.getTimestamp());
    }

    @Test
    void testEdgeCaseValues() {
        // Arrange
        response = new TechnicalDebtEmailResponse();
        
        // Act - Set edge case values
        response.setMessage("");
        response.setProjectKey("   ");
        response.setProjectName("0");
        response.setRecipientEmail("-1");
        
        // Assert
        assertEquals("", response.getMessage());
        assertEquals("   ", response.getProjectKey());
        assertEquals("0", response.getProjectName());
        assertEquals("-1", response.getRecipientEmail());
    }

    @Test
    void testMultipleInstances() {
        // Arrange & Act
        TechnicalDebtEmailResponse response1 = new TechnicalDebtEmailResponse(true, "Message 1");
        TechnicalDebtEmailResponse response2 = new TechnicalDebtEmailResponse(false, "Message 2");
        
        // Assert - Verify they are independent
        assertTrue(response1.isSuccess());
        assertFalse(response2.isSuccess());
        assertEquals("Message 1", response1.getMessage());
        assertEquals("Message 2", response2.getMessage());
        
        // Modify one should not affect the other
        response1.setMessage("Modified message 1");
        assertEquals("Modified message 1", response1.getMessage());
        assertEquals("Message 2", response2.getMessage());
    }
}
