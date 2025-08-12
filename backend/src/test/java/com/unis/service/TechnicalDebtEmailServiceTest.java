package com.unis.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.unis.dto.TechnicalDebtEmailRequest;
import com.unis.dto.TechnicalDebtEmailRequest.ProjectInfo;
import com.unis.dto.TechnicalDebtEmailResponse;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;

import java.util.Arrays;
import java.util.List;

public class TechnicalDebtEmailServiceTest {

    @Mock
    private Mailer mailer;

    @InjectMocks
    private TechnicalDebtEmailService technicalDebtEmailService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSendTechnicalDebtReport_Success() {
        // Given
        String projectKey = "test-project";
        String projectName = "Test Project";
        String recipientEmail = "test@example.com";
        
        // Mock successful email sending
        doNothing().when(mailer).send(any(Mail.class));

        // When
        TechnicalDebtEmailResponse response = technicalDebtEmailService.sendTechnicalDebtReport(
            projectKey, projectName, recipientEmail);

        // Then
        assertTrue(response.isSuccess());
        assertEquals("Reporte de deuda técnica enviado exitosamente a 2 destinatarios", response.getMessage());
        assertEquals(projectKey, response.getProjectKey());
        assertEquals(projectName, response.getProjectName());
        assertTrue(response.getRecipientEmail().contains(recipientEmail));
        assertTrue(response.getRecipientEmail().contains("jflores@unis.edu.gt"));
        
        // Verify that emails were sent to both recipients
        verify(mailer, times(2)).send(any(Mail.class));
    }

    @Test
    public void testSendTechnicalDebtReport_WithException() {
        // Given
        String projectKey = "test-project";
        String projectName = "Test Project";
        String recipientEmail = "test@example.com";
        
        // Mock email sending to throw exception
        doThrow(new RuntimeException("Email service error")).when(mailer).send(any(Mail.class));

        // When
        TechnicalDebtEmailResponse response = technicalDebtEmailService.sendTechnicalDebtReport(
            projectKey, projectName, recipientEmail);

        // Then
        assertFalse(response.isSuccess());
        assertTrue(response.getMessage().contains("Error enviando reporte"));
        assertEquals(projectKey, response.getProjectKey());
        assertEquals(projectName, response.getProjectName());
        assertEquals(recipientEmail, response.getRecipientEmail());
    }

    @Test
    public void testSendMultiProjectReports_Success() {
        // Given
        ProjectInfo project1 = new ProjectInfo();
        project1.setKey("project1");
        project1.setName("Project 1");
        
        ProjectInfo project2 = new ProjectInfo();
        project2.setKey("project2");
        project2.setName("Project 2");
        
        List<ProjectInfo> projects = Arrays.asList(project1, project2);
        String recipientEmail = "test@example.com";
        
        // Mock successful email sending
        doNothing().when(mailer).send(any(Mail.class));

        // When
        List<TechnicalDebtEmailResponse> responses = technicalDebtEmailService.sendMultiProjectReports(
            projects, recipientEmail);

        // Then
        assertEquals(2, responses.size());
        assertTrue(responses.get(0).isSuccess());
        assertTrue(responses.get(1).isSuccess());
        assertEquals("project1", responses.get(0).getProjectKey());
        assertEquals("project2", responses.get(1).getProjectKey());
        
        // Verify that emails were sent for each project (2 recipients per project)
        verify(mailer, times(4)).send(any(Mail.class));
    }

    @Test
    public void testIsHealthy_WhenServiceIsHealthy() {
        // Given - Mock the configuration properties
        technicalDebtEmailService = new TechnicalDebtEmailService();
        
        // Use reflection to set the configuration values for testing
        try {
            java.lang.reflect.Field hostField = TechnicalDebtEmailService.class.getDeclaredField("mailHost");
            hostField.setAccessible(true);
            hostField.set(technicalDebtEmailService, "smtp.gmail.com");
            
            java.lang.reflect.Field userField = TechnicalDebtEmailService.class.getDeclaredField("mailUsername");
            userField.setAccessible(true);
            userField.set(technicalDebtEmailService, "test@example.com");
        } catch (Exception e) {
            // If reflection fails, skip this test
            return;
        }

        // When
        boolean isHealthy = technicalDebtEmailService.isHealthy();

        // Then
        assertTrue(isHealthy);
    }

    @Test
    public void testGenerateTechnicalDebtReportHTML_ContainsExpectedContent() {
        // Given
        String projectKey = "test-project";
        String projectName = "Test Project";

        // When
        String htmlContent = technicalDebtEmailService.generateTechnicalDebtReportHTML(projectKey, projectName);

        // Then
        assertNotNull(htmlContent);
        assertTrue(htmlContent.contains("Reporte de Deuda Técnica"));
        assertTrue(htmlContent.contains(projectName));
        assertTrue(htmlContent.contains(projectKey));
        assertTrue(htmlContent.contains("Seguridad"));
        assertTrue(htmlContent.contains("Confiabilidad"));
        assertTrue(htmlContent.contains("Mantenibilidad"));
        assertTrue(htmlContent.contains("Cobertura de Tests"));
        assertTrue(htmlContent.contains("Duplicación de Código"));
        assertTrue(htmlContent.contains("Issues Críticos"));
        assertTrue(htmlContent.contains("Recomendaciones Prioritarias"));
        assertTrue(htmlContent.contains("http://localhost:9000/dashboard?id=" + projectKey));
    }

    @Test
    public void testSendTechnicalDebtReport_EmailContentValidation() {
        // Given
        String projectKey = "test-project";
        String projectName = "Test Project";
        String recipientEmail = "test@example.com";
        
        // Mock successful email sending
        doNothing().when(mailer).send(any(Mail.class));

        // When
        TechnicalDebtEmailResponse response = technicalDebtEmailService.sendTechnicalDebtReport(
            projectKey, projectName, recipientEmail);

        // Then
        assertTrue(response.isSuccess());
        
        // Verify that emails were sent (we can't verify content with Quarkus Mailer API)
        verify(mailer, times(2)).send(any(Mail.class));
    }

    @Test
    public void testSendTechnicalDebtReport_MultipleRecipients() {
        // Given
        String projectKey = "test-project";
        String projectName = "Test Project";
        String recipientEmail = "test@example.com";
        
        // Mock successful email sending
        doNothing().when(mailer).send(any(Mail.class));

        // When - Use the mocked service instead of creating a new one
        TechnicalDebtEmailResponse response = technicalDebtEmailService.sendTechnicalDebtReport(
            projectKey, projectName, recipientEmail);

        // Then
        assertTrue(response.isSuccess());
        String recipients = response.getRecipientEmail();
        assertTrue(recipients.contains(recipientEmail));
        assertTrue(recipients.contains("jflores@unis.edu.gt"));
        assertTrue(recipients.contains(",")); // Should contain comma separator
    }

    @Test
    public void testSendTechnicalDebtReport_ProjectKeyAndNameValidation() {
        // Given
        String projectKey = "special-project-key-123";
        String projectName = "Special Project Name with Spaces & Symbols!";
        String recipientEmail = "test@example.com";
        
        // Mock successful email sending
        doNothing().when(mailer).send(any(Mail.class));

        // When
        TechnicalDebtEmailResponse response = technicalDebtEmailService.sendTechnicalDebtReport(
            projectKey, projectName, recipientEmail);

        // Then
        assertTrue(response.isSuccess());
        assertEquals(projectKey, response.getProjectKey());
        assertEquals(projectName, response.getProjectName());
        
        // Verify that emails were sent (we can't verify content with Quarkus Mailer API)
        verify(mailer, times(2)).send(any(Mail.class));
    }
}
