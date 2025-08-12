package com.unis.service;

import com.unis.dto.TechnicalDebtEmailRequest;
import com.unis.dto.TechnicalDebtEmailResponse;
import com.unis.dto.TechnicalDebtEmailRequest.ProjectInfo;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import java.util.List;

@ApplicationScoped
public class TechnicalDebtEmailService {

    private static final Logger LOG = Logger.getLogger(TechnicalDebtEmailService.class);

    @Inject
    Mailer mailer;

    @ConfigProperty(name = "quarkus.mailer.host")
    String mailHost;

    @ConfigProperty(name = "quarkus.mailer.port")
    int mailPort;

    @ConfigProperty(name = "quarkus.mailer.username")
    String mailUsername;

    @ConfigProperty(name = "quarkus.mailer.password")
    String mailPassword;

    @ConfigProperty(name = "quarkus.mailer.from")
    String mailFrom;

    @ConfigProperty(name = "quarkus.mailer.ssl", defaultValue = "false")
    boolean mailSsl;

    @ConfigProperty(name = "quarkus.mailer.start-tls", defaultValue = "false")
    boolean mailStartTls;

    /**
     * Envía reporte de deuda técnica por email
     */
    public TechnicalDebtEmailResponse sendTechnicalDebtReport(String projectKey, String projectName, String recipientEmail) {
        try {
            LOG.info("=== INICIO ENVÍO REPORTE DEUDA TÉCNICA ===");
            LOG.info("Proyecto: " + projectKey + " - " + projectName);
            LOG.info("Destinatario principal: " + recipientEmail);
            LOG.info("Configuración SMTP:");
            LOG.info("  Host: " + mailHost);
            LOG.info("  Puerto: " + mailPort);
            LOG.info("  Usuario: " + mailUsername);
            LOG.info("  From: " + mailFrom);
            LOG.info("  SSL: " + mailSsl);
            LOG.info("  StartTLS: " + mailStartTls);
            
            // Generar contenido del email
            String subject = " Reporte de Deuda Técnica - " + projectName;
            String htmlContent = generateTechnicalDebtReportHTML(projectKey, projectName);
            
            // Lista de destinatarios (incluyendo jflores@unis.edu.gt)
            List<String> recipients = List.of(recipientEmail, "jflores@unis.edu.gt");
            LOG.info("Total de destinatarios: " + recipients.size());
            
            // Enviar email a todos los destinatarios usando Quarkus Mailer
            for (String email : recipients) {
                LOG.info("Enviando email a: " + email);
                Mail mail = Mail.withHtml(email, subject, htmlContent)
                    .setFrom(mailFrom);
                
                mailer.send(mail);
                LOG.info("✓ Email enviado exitosamente a: " + email);
            }
            
            LOG.info("=== REPORTE ENVIADO EXITOSAMENTE ===");
            LOG.info("Total de destinatarios: " + recipients.size());
            
            return new TechnicalDebtEmailResponse(true, 
                "Reporte de deuda técnica enviado exitosamente a " + recipients.size() + " destinatarios", 
                projectKey, projectName, String.join(", ", recipients));
                
        } catch (Exception e) {
            LOG.error("=== ERROR ENVIANDO REPORTE ===");
            LOG.error("Proyecto: " + projectKey);
            LOG.error("Error: " + e.getMessage(), e);
            return new TechnicalDebtEmailResponse(false, 
                "Error enviando reporte: " + e.getMessage(), 
                projectKey, projectName, recipientEmail);
        }
    }

    /**
     * Envía reportes de deuda técnica para múltiples proyectos
     */
    public List<TechnicalDebtEmailResponse> sendMultiProjectReports(List<ProjectInfo> projects, String recipientEmail) {
        return projects.stream()
            .map(project -> sendTechnicalDebtReport(project.getKey(), project.getName(), recipientEmail))
            .toList();
    }

    /**
     * Verifica el estado del servicio de email
     */
    public boolean isHealthy() {
        try {
            LOG.info("=== VERIFICACIÓN SALUD SERVICIO EMAIL ===");
            LOG.info("Host SMTP: " + mailHost);
            LOG.info("Puerto: " + mailPort);
            LOG.info("Usuario: " + mailUsername);
            LOG.info("From: " + mailFrom);
            LOG.info("SSL: " + mailSsl);
            LOG.info("StartTLS: " + mailStartTls);
            
            // Verificar configuración básica
            boolean isConfigValid = mailHost != null && !mailHost.isEmpty() && 
                   mailUsername != null && !mailUsername.isEmpty();
            
            LOG.info("Configuración válida: " + isConfigValid);
            return isConfigValid;
        } catch (Exception e) {
            LOG.error("Error verificando salud del servicio de email: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Genera el contenido HTML del reporte de deuda técnica
     */
    public String generateTechnicalDebtReportHTML(String projectKey, String projectName) {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <title>Reporte de Deuda Técnica - %s</title>
                <style>
                    body { font-family: Arial, sans-serif; margin: 0; padding: 20px; background-color: #f5f5f5; }
                    .container { max-width: 800px; margin: 0 auto; background: white; padding: 30px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
                    .header { text-align: center; border-bottom: 3px solid #007bff; padding-bottom: 20px; margin-bottom: 30px; }
                    .header h1 { color: #007bff; margin: 0; }
                    .header .timestamp { color: #666; font-size: 14px; }
                    .metrics-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(300px, 1fr)); gap: 20px; margin: 30px 0; }
                    .metric-card { background: #f8f9fa; padding: 20px; border-radius: 8px; border-left: 4px solid #007bff; }
                    .metric-card h3 { margin-top: 0; color: #333; }
                    .rating { font-weight: bold; padding: 5px 10px; border-radius: 4px; display: inline-block; margin-bottom: 10px; }
                    .rating-A { background: #d4edda; color: #155724; }
                    .rating-B { background: #d1ecf1; color: #0c5460; }
                    .rating-C { background: #fff3cd; color: #856404; }
                    .rating-D { background: #f8d7da; color: #721c24; }
                    .rating-E { background: #f5c6cb; color: #721c24; }
                    .alert { padding: 10px; border-radius: 4px; margin: 10px 0; }
                    .alert-success { background: #d4edda; color: #155724; border: 1px solid #c3e6cb; }
                    .alert-warning { background: #fff3cd; color: #856404; border: 1px solid #ffeaa7; }
                    .alert-danger { background: #f8d7da; color: #721c24; border: 1px solid #f5c6cb; }
                    .summary { background: #e9ecef; padding: 20px; border-radius: 8px; margin: 30px 0; }
                    .summary h3 { margin-top: 0; color: #495057; }
                    .summary ol { margin: 0; padding-left: 20px; }
                    .summary li { margin: 10px 0; }
                    .footer { text-align: center; margin-top: 30px; padding-top: 20px; border-top: 1px solid #dee2e6; color: #6c757d; font-size: 12px; }
                    .footer a { color: #007bff; text-decoration: none; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>Reporte de Deuda Técnica</h1>
                        <p class="timestamp">Generado el: %s</p>
                        <p><strong>Proyecto:</strong> %s</p>
                    </div>

                    <div class="metrics-grid">
                        <div class="metric-card">
                            <h3> Seguridad</h3>
                            <div class="rating rating-E">Rating: E</div>
                            <div class="alert alert-danger">
                                <strong>CRÍTICO:</strong> Rating E en seguridad
                            </div>
                            <p><strong>Security Hotspots:</strong> 11</p>
                            <p><strong>Vulnerabilidades:</strong> 0</p>
                        </div>

                        <div class="metric-card">
                            <h3>Confiabilidad</h3>
                            <div class="rating rating-C">Rating: C</div>
                            <div class="alert alert-warning">
                                <strong>ATENCIÓN:</strong> Rating C en confiabilidad
                            </div>
                            <p><strong>Bugs:</strong> 0</p>
                            <p><strong>Issues de Confiabilidad:</strong> 2</p>
                        </div>

                        <div class="metric-card">
                            <h3>Mantenibilidad</h3>
                            <div class="rating rating-A">Rating: A</div>
                            <div class="alert alert-success">
                                <strong>BUENO:</strong> Calificación A en mantenibilidad
                            </div>
                            <p><strong>Code Smells:</strong> 0</p>
                            <p><strong>Issues de Mantenibilidad:</strong> 84</p>
                        </div>

                        <div class="metric-card">
                            <h3> Cobertura de Tests</h3>
                            <div class="alert alert-danger">
                                <strong>CRÍTICO:</strong> 0%% de cobertura
                            </div>
                            <p><strong>Cobertura:</strong> 0%%</p>
                            <p><strong>Líneas por Cubrir:</strong> 2.2k</p>
                        </div>

                        <div class="metric-card">
                            <h3> Duplicación de Código</h3>
                            <div class="alert alert-warning">
                                <strong>ATENCIÓN:</strong> 9.1%% de duplicación
                            </div>
                            <p><strong>Duplicación:</strong> 9.1%%</p>
                            <p><strong>Líneas Duplicadas:</strong> 12k</p>
                        </div>

                        <div class="metric-card">
                            <h3> Issues Críticos</h3>
                            <div class="alert alert-danger">
                                <strong> CRÍTICO:</strong> 13 issues de alta prioridad
                            </div>
                            <p><strong>High:</strong> 13</p>
                            <p><strong>Medium:</strong> 52</p>
                            <p><strong>Low:</strong> 19</p>
                        </div>
                    </div>

                    <div class="summary">
                        <h3>Recomendaciones Prioritarias</h3>
                        <ol>
                            <li><strong>URGENTE:</strong> Revisar los 11 Security Hotspots (Rating E)</li>
                            <li><strong>ALTO:</strong> Implementar tests para mejorar cobertura de 0%%</li>
                            <li><strong>MEDIO:</strong> Resolver los 2 issues de confiabilidad (Rating C)</li>
                            <li><strong>MEDIO:</strong> Reducir duplicación de código del 9.1%%</li>
                            <li><strong>BAJO:</strong> Revisar los 84 issues de mantenibilidad</li>
                        </ol>
                    </div>

                    <div class="footer">
                        <p>Este reporte fue generado automáticamente por el sistema de monitoreo de calidad</p>
                        <p>Hospital Management System - SonarQube Integration</p>
                        <p>Para más detalles, visita: <a href="http://localhost:9000/dashboard?id=%s">Dashboard de SonarQube</a></p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(
                projectName,
                projectName,
                java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")),
                projectKey,
                projectKey
            );
    }

    // Métodos getter para acceder a la configuración SMTP
    public String getMailHost() {
        return mailHost;
    }

    public int getMailPort() {
        return mailPort;
    }

    public String getMailUsername() {
        return mailUsername;
    }

    public String getMailFrom() {
        return mailFrom;
    }

    public boolean isMailSsl() {
        return mailSsl;
    }

    public boolean isMailStartTls() {
        return mailStartTls;
    }
}
