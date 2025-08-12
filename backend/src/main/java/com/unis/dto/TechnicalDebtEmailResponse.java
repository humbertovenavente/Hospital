package com.unis.dto;

import java.time.LocalDateTime;

public class TechnicalDebtEmailResponse {
    private boolean success;
    private String message;
    private LocalDateTime timestamp;
    private String projectKey;
    private String projectName;
    private String recipientEmail;

    // Constructor por defecto
    public TechnicalDebtEmailResponse() {
        this.timestamp = LocalDateTime.now();
    }

    // Constructor básico
    public TechnicalDebtEmailResponse(boolean success, String message) {
        this();
        this.success = success;
        this.message = message;
    }

    // Constructor completo
    public TechnicalDebtEmailResponse(boolean success, String message, String projectKey, String projectName, String recipientEmail) {
        this(success, message);
        this.projectKey = projectKey;
        this.projectName = projectName;
        this.recipientEmail = recipientEmail;
    }

    // Getters y Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

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

    @Override
    public String toString() {
        return "TechnicalDebtEmailResponse{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", timestamp=" + timestamp +
                ", projectKey='" + projectKey + '\'' +
                ", projectName='" + projectName + '\'' +
                ", recipientEmail='" + recipientEmail + '\'' +
                '}';
    }
}
