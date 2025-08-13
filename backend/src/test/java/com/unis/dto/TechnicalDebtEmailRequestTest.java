package com.unis.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

public class TechnicalDebtEmailRequestTest {

    private TechnicalDebtEmailRequest request;

    @BeforeEach
    void setUp() {
        request = new TechnicalDebtEmailRequest();
    }

    @Test
    void testConstructorPorDefecto() {
        assertNotNull(request);
    }

    @Test
    void testConstructorProyectoUnico() {
        // Arrange
        String projectKey = "TEST-PROJECT";
        String projectName = "Proyecto de Prueba";
        String recipientEmail = "test@example.com";

        // Act
        TechnicalDebtEmailRequest request = new TechnicalDebtEmailRequest(projectKey, projectName, recipientEmail);

        // Assert
        assertEquals(projectKey, request.getProjectKey());
        assertEquals(projectName, request.getProjectName());
        assertEquals(recipientEmail, request.getRecipientEmail());
        assertNull(request.getProjects());
    }

    @Test
    void testConstructorMultiplesProyectos() {
        // Arrange
        List<TechnicalDebtEmailRequest.ProjectInfo> projects = Arrays.asList(
            new TechnicalDebtEmailRequest.ProjectInfo("PROJ1", "Proyecto 1"),
            new TechnicalDebtEmailRequest.ProjectInfo("PROJ2", "Proyecto 2")
        );
        String recipientEmail = "test@example.com";

        // Act
        TechnicalDebtEmailRequest request = new TechnicalDebtEmailRequest(projects, recipientEmail);

        // Assert
        assertEquals(projects, request.getProjects());
        assertEquals(recipientEmail, request.getRecipientEmail());
        assertNull(request.getProjectKey());
        assertNull(request.getProjectName());
    }

    @Test
    void testGettersAndSetters() {
        // Arrange
        String projectKey = "TEST-KEY";
        String projectName = "Test Project";
        String recipientEmail = "test@example.com";
        List<TechnicalDebtEmailRequest.ProjectInfo> projects = Arrays.asList(
            new TechnicalDebtEmailRequest.ProjectInfo("KEY1", "Name1")
        );

        // Act
        request.setProjectKey(projectKey);
        request.setProjectName(projectName);
        request.setRecipientEmail(recipientEmail);
        request.setProjects(projects);

        // Assert
        assertEquals(projectKey, request.getProjectKey());
        assertEquals(projectName, request.getProjectName());
        assertEquals(recipientEmail, request.getRecipientEmail());
        assertEquals(projects, request.getProjects());
    }

    @Test
    void testSetProjectKey() {
        String projectKey = "NEW-KEY";
        request.setProjectKey(projectKey);
        assertEquals(projectKey, request.getProjectKey());
    }

    @Test
    void testSetProjectName() {
        String projectName = "New Project Name";
        request.setProjectName(projectName);
        assertEquals(projectName, request.getProjectName());
    }

    @Test
    void testSetRecipientEmail() {
        String recipientEmail = "new@example.com";
        request.setRecipientEmail(recipientEmail);
        assertEquals(recipientEmail, request.getRecipientEmail());
    }

    @Test
    void testSetProjects() {
        List<TechnicalDebtEmailRequest.ProjectInfo> projects = Arrays.asList(
            new TechnicalDebtEmailRequest.ProjectInfo("KEY2", "Name2")
        );
        request.setProjects(projects);
        assertEquals(projects, request.getProjects());
    }

    @Test
    void testSetProjectKeyNull() {
        request.setProjectKey(null);
        assertNull(request.getProjectKey());
    }

    @Test
    void testSetProjectNameNull() {
        request.setProjectName(null);
        assertNull(request.getProjectName());
    }

    @Test
    void testSetRecipientEmailNull() {
        request.setRecipientEmail(null);
        assertNull(request.getRecipientEmail());
    }

    @Test
    void testSetProjectsNull() {
        request.setProjects(null);
        assertNull(request.getProjects());
    }

    @Test
    void testProjectInfoConstructorPorDefecto() {
        TechnicalDebtEmailRequest.ProjectInfo projectInfo = new TechnicalDebtEmailRequest.ProjectInfo();
        assertNotNull(projectInfo);
    }

    @Test
    void testProjectInfoConstructorConParametros() {
        String key = "TEST-KEY";
        String name = "Test Name";
        TechnicalDebtEmailRequest.ProjectInfo projectInfo = new TechnicalDebtEmailRequest.ProjectInfo(key, name);
        
        assertEquals(key, projectInfo.getKey());
        assertEquals(name, projectInfo.getName());
    }

    @Test
    void testProjectInfoGettersAndSetters() {
        TechnicalDebtEmailRequest.ProjectInfo projectInfo = new TechnicalDebtEmailRequest.ProjectInfo();
        
        String key = "NEW-KEY";
        String name = "New Name";
        
        projectInfo.setKey(key);
        projectInfo.setName(name);
        
        assertEquals(key, projectInfo.getKey());
        assertEquals(name, projectInfo.getName());
    }

    @Test
    void testProjectInfoSetKeyNull() {
        TechnicalDebtEmailRequest.ProjectInfo projectInfo = new TechnicalDebtEmailRequest.ProjectInfo();
        projectInfo.setKey(null);
        assertNull(projectInfo.getKey());
    }

    @Test
    void testProjectInfoSetNameNull() {
        TechnicalDebtEmailRequest.ProjectInfo projectInfo = new TechnicalDebtEmailRequest.ProjectInfo();
        projectInfo.setName(null);
        assertNull(projectInfo.getName());
    }
}
