package com.unis.controller;

import com.unis.dto.TechnicalDebtEmailRequest;
import com.unis.dto.TechnicalDebtEmailResponse;
import com.unis.service.TechnicalDebtEmailService;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

@Path("/api/email")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Technical Debt Email", description = "Servicios para envío de reportes de deuda técnica")
@RegisterForReflection
public class TechnicalDebtEmailController {

    private final TechnicalDebtEmailService technicalDebtEmailService;

    /**
     * Constructor for dependency injection.
     *
     * @param technicalDebtEmailService the service for sending technical debt emails
     */
    @Inject
    public TechnicalDebtEmailController(TechnicalDebtEmailService technicalDebtEmailService) {
        this.technicalDebtEmailService = technicalDebtEmailService;
    }

    @POST
    @Path("/technical-debt")
    @Operation(summary = "Envía reporte de deuda técnica por email", 
               description = "Envía un reporte detallado de deuda técnica a un email específico")
    public Response sendTechnicalDebtReport(TechnicalDebtEmailRequest request) {
        try {
            TechnicalDebtEmailResponse response = technicalDebtEmailService.sendTechnicalDebtReport(
                request.getProjectKey(),
                request.getProjectName(),
                request.getRecipientEmail()
            );
            
            return Response.ok(response).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new TechnicalDebtEmailResponse(false, "Error enviando reporte: " + e.getMessage()))
                .build();
        }
    }

    @POST
    @Path("/technical-debt/multi-project")
    @Operation(summary = "Envía reportes de deuda técnica para múltiples proyectos", 
               description = "Envía reportes de deuda técnica para varios proyectos en un solo email")
    public Response sendMultiProjectTechnicalDebtReport(TechnicalDebtEmailRequest request) {
        try {
            List<TechnicalDebtEmailResponse> responses = technicalDebtEmailService.sendMultiProjectReports(
                request.getProjects(),
                request.getRecipientEmail()
            );
            
            return Response.ok(responses).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new TechnicalDebtEmailResponse(false, "Error enviando reportes: " + e.getMessage()))
                .build();
        }
    }

    @GET
    @Path("/technical-debt/status")
    @Operation(summary = "Verifica el estado del servicio de email", 
               description = "Verifica si el servicio de email está funcionando correctamente")
    public Response getEmailServiceStatus() {
        try {
            boolean isHealthy = technicalDebtEmailService.isHealthy();
            return Response.ok(new TechnicalDebtEmailResponse(isHealthy, 
                isHealthy ? "Servicio de email funcionando" : "Servicio de email con problemas"))
                .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new TechnicalDebtEmailResponse(false, "Error verificando servicio: " + e.getMessage()))
                .build();
        }
    }

    @POST
    @Path("/technical-debt/send-report")
    @Operation(summary = "Envía reporte de deuda técnica por email (simplificado para pipeline)", 
               description = "Envía un reporte de deuda técnica usando solo el email del destinatario")
    public Response sendTechnicalDebtReportSimple(TechnicalDebtEmailRequest request) {
        try {
            // Usar valores por defecto para el pipeline
            String projectKey = "hospital-pipeline";
            String projectName = "Hospital Pipeline - Análisis Automático";
            
            TechnicalDebtEmailResponse response = technicalDebtEmailService.sendTechnicalDebtReport(
                projectKey,
                projectName,
                request.getRecipientEmail()
            );
            
            return Response.ok(response).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new TechnicalDebtEmailResponse(false, "Error enviando reporte: " + e.getMessage()))
                .build();
        }
    }

    @GET
    @Path("/technical-debt/test-smtp")
    @Operation(summary = "Prueba la conectividad SMTP", 
               description = "Prueba la conexión SMTP con Gmail para verificar la configuración")
    public Response testSmtpConnection() {
        try {
            // Crear un email de prueba simple
            // String testSubject = "Prueba SMTP - Deuda Técnica";
            // String testContent = """
            //     <html>
            //     <body>
            //         <h2>Prueba de Conectividad SMTP</h2>
            //         <p>Este es un email de prueba para verificar que la configuración SMTP funciona correctamente.</p>
            //         <p><strong>Timestamp:</strong> %s</p>
            //         <p><strong>Servidor:</strong> %s</p>
            //         <p><strong>Puerto:</strong> %d</p>
            //         <p><strong>Usuario:</strong> %s</p>
            //         <hr>
            //         <p><em>Hospital Management System - Test SMTP</em></p>
            //     </body>
            //     </html>
            //     """.formatted(
            //         java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")),
            //         technicalDebtEmailService.getMailHost(),
            //         technicalDebtEmailService.getMailPort(),
            //         technicalDebtEmailService.getMailUsername()
            //     );
            
            // Enviar email de prueba a jflores@unis.edu.gt
            TechnicalDebtEmailResponse response = technicalDebtEmailService.sendTechnicalDebtReport(
                "test-smtp",
                "Prueba SMTP",
                "jflores@unis.edu.gt"
            );
            
            if (response.isSuccess()) {
                return Response.ok(response).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
            }
            
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new TechnicalDebtEmailResponse(false, "Error en prueba SMTP: " + e.getMessage()))
                .build();
        }
    }
}
