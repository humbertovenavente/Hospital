package com.unis.dto;

import java.util.List;

public class TechnicalDebtEmailRequest {
    private String projectKey;
    private String projectName;
    private String recipientEmail;
    private List<ProjectInfo> projects;

    // Constructor por defecto
    public TechnicalDebtEmailRequest() {}

    // Constructor para proyecto único
    public TechnicalDebtEmailRequest(String projectKey, String projectName, String recipientEmail) {
        this.projectKey = projectKey;
        this.projectName = projectName;
        this.recipientEmail = recipientEmail;
    }

    // Constructor para múltiples proyectos
    public TechnicalDebtEmailRequest(List<ProjectInfo> projects, String recipientEmail) {
        this.projects = projects;
        this.recipientEmail = recipientEmail;
    }

    // Getters y Setters
    public String getProjectKey() {
        return projectKey;
    }

    public void setProjectKey(String projectKey) {
        this.projectKey = projectKey;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getRecipientEmail() {
        return recipientEmail;
    }

    public void setRecipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
    }

    public List<ProjectInfo> getProjects() {
        return projects;
    }

    public void setProjects(List<ProjectInfo> projects) {
        this.projects = projects;
    }

    // Clase interna para información de proyectos
    public static class ProjectInfo {
        private String key;
        private String name;

        public ProjectInfo() {}

        public ProjectInfo(String key, String name) {
            this.key = key;
            this.name = name;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
