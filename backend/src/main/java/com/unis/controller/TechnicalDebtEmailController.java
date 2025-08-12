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

    @Inject
    TechnicalDebtEmailService technicalDebtEmailService;

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
}
