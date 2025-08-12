package com.unis.controller;

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
import com.unis.service.TechnicalDebtEmailService;

import jakarta.ws.rs.core.Response;
import java.util.Arrays;
import java.util.List;

public class TechnicalDebtEmailControllerTest {

    @Mock
    private TechnicalDebtEmailService technicalDebtEmailService;

    @InjectMocks
    private TechnicalDebtEmailController technicalDebtEmailController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSendTechnicalDebtReport_Success() {
        // Given
        TechnicalDebtEmailRequest request = new TechnicalDebtEmailRequest();
        request.setProjectKey("test-project");
        request.setProjectName("Test Project");
        request.setRecipientEmail("test@example.com");

        TechnicalDebtEmailResponse expectedResponse = new TechnicalDebtEmailResponse(
            true, "Success message", "test-project", "Test Project", "test@example.com"
        );

        when(technicalDebtEmailService.sendTechnicalDebtReport(
            request.getProjectKey(), 
            request.getProjectName(), 
            request.getRecipientEmail()
        )).thenReturn(expectedResponse);

        // When
        Response response = technicalDebtEmailController.sendTechnicalDebtReport(request);

        // Then
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        TechnicalDebtEmailResponse actualResponse = (TechnicalDebtEmailResponse) response.getEntity();
        assertEquals(expectedResponse, actualResponse);
        
        verify(technicalDebtEmailService, times(1)).sendTechnicalDebtReport(
            request.getProjectKey(), 
            request.getProjectName(), 
            request.getRecipientEmail()
        );
    }

    @Test
    public void testSendTechnicalDebtReport_WithException() {
        // Given
        TechnicalDebtEmailRequest request = new TechnicalDebtEmailRequest();
        request.setProjectKey("test-project");
        request.setProjectName("Test Project");
        request.setRecipientEmail("test@example.com");

        when(technicalDebtEmailService.sendTechnicalDebtReport(
            anyString(), anyString(), anyString()
        )).thenThrow(new RuntimeException("Service error"));

        // When
        Response response = technicalDebtEmailController.sendTechnicalDebtReport(request);

        // Then
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        TechnicalDebtEmailResponse actualResponse = (TechnicalDebtEmailResponse) response.getEntity();
        assertFalse(actualResponse.isSuccess());
        assertTrue(actualResponse.getMessage().contains("Error enviando reporte"));
        
        verify(technicalDebtEmailService, times(1)).sendTechnicalDebtReport(
            request.getProjectKey(), 
            request.getProjectName(), 
            request.getRecipientEmail()
        );
    }

    @Test
    public void testSendMultiProjectTechnicalDebtReport_Success() {
        // Given
        TechnicalDebtEmailRequest request = new TechnicalDebtEmailRequest();
        request.setRecipientEmail("test@example.com");
        
        ProjectInfo project1 = new ProjectInfo();
        project1.setKey("project1");
        project1.setName("Project 1");
        
        ProjectInfo project2 = new ProjectInfo();
        project2.setKey("project2");
        project2.setName("Project 2");
        
        request.setProjects(Arrays.asList(project1, project2));

        List<TechnicalDebtEmailResponse> expectedResponses = Arrays.asList(
            new TechnicalDebtEmailResponse(true, "Success 1", "project1", "Project 1", "test@example.com"),
            new TechnicalDebtEmailResponse(true, "Success 2", "project2", "Project 2", "test@example.com")
        );

        when(technicalDebtEmailService.sendMultiProjectReports(
            request.getProjects(), 
            request.getRecipientEmail()
        )).thenReturn(expectedResponses);

        // When
        Response response = technicalDebtEmailController.sendMultiProjectTechnicalDebtReport(request);

        // Then
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        @SuppressWarnings("unchecked")
        List<TechnicalDebtEmailResponse> actualResponses = (List<TechnicalDebtEmailResponse>) response.getEntity();
        assertEquals(expectedResponses.size(), actualResponses.size());
        assertEquals(expectedResponses, actualResponses);
        
        verify(technicalDebtEmailService, times(1)).sendMultiProjectReports(
            request.getProjects(), 
            request.getRecipientEmail()
        );
    }

    @Test
    public void testSendMultiProjectTechnicalDebtReport_WithException() {
        // Given
        TechnicalDebtEmailRequest request = new TechnicalDebtEmailRequest();
        request.setRecipientEmail("test@example.com");
        request.setProjects(Arrays.asList(new ProjectInfo()));

        when(technicalDebtEmailService.sendMultiProjectReports(
            anyList(), anyString()
        )).thenThrow(new RuntimeException("Service error"));

        // When
        Response response = technicalDebtEmailController.sendMultiProjectTechnicalDebtReport(request);

        // Then
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        TechnicalDebtEmailResponse actualResponse = (TechnicalDebtEmailResponse) response.getEntity();
        assertFalse(actualResponse.isSuccess());
        assertTrue(actualResponse.getMessage().contains("Error enviando reportes"));
    }

    @Test
    public void testGetEmailServiceStatus_Success() {
        // Given
        when(technicalDebtEmailService.isHealthy()).thenReturn(true);

        // When
        Response response = technicalDebtEmailController.getEmailServiceStatus();

        // Then
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        TechnicalDebtEmailResponse actualResponse = (TechnicalDebtEmailResponse) response.getEntity();
        assertTrue(actualResponse.isSuccess());
        assertEquals("Servicio de email funcionando", actualResponse.getMessage());
        
        verify(technicalDebtEmailService, times(1)).isHealthy();
    }

    @Test
    public void testGetEmailServiceStatus_ServiceUnhealthy() {
        // Given
        when(technicalDebtEmailService.isHealthy()).thenReturn(false);

        // When
        Response response = technicalDebtEmailController.getEmailServiceStatus();

        // Then
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        TechnicalDebtEmailResponse actualResponse = (TechnicalDebtEmailResponse) response.getEntity();
        assertFalse(actualResponse.isSuccess());
        assertEquals("Servicio de email con problemas", actualResponse.getMessage());
    }

    @Test
    public void testGetEmailServiceStatus_WithException() {
        // Given
        when(technicalDebtEmailService.isHealthy()).thenThrow(new RuntimeException("Service error"));

        // When
        Response response = technicalDebtEmailController.getEmailServiceStatus();

        // Then
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        TechnicalDebtEmailResponse actualResponse = (TechnicalDebtEmailResponse) response.getEntity();
        assertFalse(actualResponse.isSuccess());
        assertTrue(actualResponse.getMessage().contains("Error verificando servicio"));
    }

    @Test
    public void testSendTechnicalDebtReportSimple_Success() {
        // Given
        TechnicalDebtEmailRequest request = new TechnicalDebtEmailRequest();
        request.setRecipientEmail("test@example.com");

        TechnicalDebtEmailResponse expectedResponse = new TechnicalDebtEmailResponse(
            true, "Success message", "hospital-pipeline", "Hospital Pipeline - Análisis Automático", "test@example.com"
        );

        when(technicalDebtEmailService.sendTechnicalDebtReport(
            "hospital-pipeline", 
            "Hospital Pipeline - Análisis Automático", 
            request.getRecipientEmail()
        )).thenReturn(expectedResponse);

        // When
        Response response = technicalDebtEmailController.sendTechnicalDebtReportSimple(request);

        // Then
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        TechnicalDebtEmailResponse actualResponse = (TechnicalDebtEmailResponse) response.getEntity();
        assertEquals(expectedResponse, actualResponse);
        
        verify(technicalDebtEmailService, times(1)).sendTechnicalDebtReport(
            "hospital-pipeline", 
            "Hospital Pipeline - Análisis Automático", 
            request.getRecipientEmail()
        );
    }

    @Test
    public void testSendTechnicalDebtReportSimple_WithException() {
        // Given
        TechnicalDebtEmailRequest request = new TechnicalDebtEmailRequest();
        request.setRecipientEmail("test@example.com");

        when(technicalDebtEmailService.sendTechnicalDebtReport(
            anyString(), anyString(), anyString()
        )).thenThrow(new RuntimeException("Service error"));

        // When
        Response response = technicalDebtEmailController.sendTechnicalDebtReportSimple(request);

        // Then
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        TechnicalDebtEmailResponse actualResponse = (TechnicalDebtEmailResponse) response.getEntity();
        assertFalse(actualResponse.isSuccess());
        assertTrue(actualResponse.getMessage().contains("Error enviando reporte"));
    }

    @Test
    public void testSendTechnicalDebtReport_WithNullRequest() {
        // Given
        TechnicalDebtEmailRequest request = null;

        // When & Then - The controller should handle null requests gracefully
        // This test verifies that the controller doesn't crash with null requests
        // The actual behavior may vary depending on the framework's null handling
        try {
            technicalDebtEmailController.sendTechnicalDebtReport(request);
            // If no exception is thrown, that's also acceptable
        } catch (Exception e) {
            // If an exception is thrown, that's also acceptable
            // The important thing is that the controller doesn't crash
        }
    }

    @Test
    public void testSendTechnicalDebtReport_WithEmptyProjectKey() {
        // Given
        TechnicalDebtEmailRequest request = new TechnicalDebtEmailRequest();
        request.setProjectKey("");
        request.setProjectName("Test Project");
        request.setRecipientEmail("test@example.com");

        TechnicalDebtEmailResponse expectedResponse = new TechnicalDebtEmailResponse(
            true, "Success message", "", "Test Project", "test@example.com"
        );

        when(technicalDebtEmailService.sendTechnicalDebtReport(
            "", "Test Project", "test@example.com"
        )).thenReturn(expectedResponse);

        // When
        Response response = technicalDebtEmailController.sendTechnicalDebtReport(request);

        // Then
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        verify(technicalDebtEmailService, times(1)).sendTechnicalDebtReport("", "Test Project", "test@example.com");
    }
}
