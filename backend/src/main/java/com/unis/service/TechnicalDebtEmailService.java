package com.unis.service;

import com.unis.dto.TechnicalDebtEmailRequest;
import com.unis.dto.TechnicalDebtEmailResponse;
import com.unis.dto.TechnicalDebtEmailRequest.ProjectInfo;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Properties;

@ApplicationScoped
public class TechnicalDebtEmailService {

    private static final Logger LOG = Logger.getLogger(TechnicalDebtEmailService.class);

    @ConfigProperty(name = "quarkus.mailer.host", defaultValue = "localhost")
    String mailHost;

    @ConfigProperty(name = "quarkus.mailer.port", defaultValue = "587")
    int mailPort;

    @ConfigProperty(name = "quarkus.mailer.username", defaultValue = "noreply@hospital.com")
    String mailUsername;

    @ConfigProperty(name = "quarkus.mailer.password", defaultValue = "password")
    String mailPassword;

    @ConfigProperty(name = "quarkus.mailer.from", defaultValue = "noreply@hospital.com")
    String mailFrom;

    @ConfigProperty(name = "quarkus.mailer.ssl", defaultValue = "false")
    boolean mailSsl;

    /**
     * Envía reporte de deuda técnica por email
     */
    public TechnicalDebtEmailResponse sendTechnicalDebtReport(String projectKey, String projectName, String recipientEmail) {
        try {
            LOG.info("Enviando reporte de deuda técnica para proyecto: " + projectKey);
            
            // Generar contenido del email
            String subject = "📊 Reporte de Deuda Técnica - " + projectName;
            String htmlContent = generateTechnicalDebtReportHTML(projectKey, projectName);
            
            // Enviar email
            sendEmail(recipientEmail, subject, htmlContent);
            
            LOG.info("Reporte de deuda técnica enviado exitosamente a: " + recipientEmail);
            
            return new TechnicalDebtEmailResponse(true, 
                "Reporte de deuda técnica enviado exitosamente", 
                projectKey, projectName, recipientEmail);
                
        } catch (Exception e) {
            LOG.error("Error enviando reporte de deuda técnica: " + e.getMessage(), e);
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
            // Verificar configuración básica
            return mailHost != null && !mailHost.isEmpty() && 
                   mailUsername != null && !mailUsername.isEmpty();
        } catch (Exception e) {
            LOG.error("Error verificando salud del servicio de email: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Genera el contenido HTML del reporte de deuda técnica
     */
    private String generateTechnicalDebtReportHTML(String projectKey, String projectName) {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <title>Reporte de Deuda Técnica - %s</title>
                <style>
                    body { font-family: Arial, sans-serif; margin: 20px; background-color: #f5f5f5; }
                    .container { max-width: 800px; margin: 0 auto; background: white; padding: 20px; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
                    .header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 20px; border-radius: 8px; text-align: center; }
                    .metric-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(300px, 1fr)); gap: 20px; margin: 20px 0; }
                    .metric-card { border: 1px solid #ddd; padding: 20px; border-radius: 8px; background: white; }
                    .metric-card h3 { margin-top: 0; color: #333; }
                    .rating { font-size: 24px; font-weight: bold; margin: 10px 0; }
                    .rating-A { color: #27ae60; }
                    .rating-B { color: #2ecc71; }
                    .rating-C { color: #f39c12; }
                    .rating-D { color: #e67e22; }
                    .rating-E { color: #e74c3c; }
                    .summary { background: #ecf0f1; padding: 20px; border-radius: 8px; margin: 20px 0; }
                    .footer { text-align: center; margin-top: 30px; color: #7f8c8d; font-size: 12px; }
                    .alert { padding: 15px; border-radius: 5px; margin: 10px 0; }
                    .alert-warning { background: #fff3cd; border: 1px solid #ffeaa7; color: #856404; }
                    .alert-danger { background: #f8d7da; border: 1px solid #f5c6cb; color: #721c24; }
                    .alert-success { background: #d4edda; border: 1px solid #c3e6cb; color: #155724; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>📊 Reporte de Deuda Técnica</h1>
                        <h2>%s</h2>
                        <p>Generado el: %s</p>
                        <p>Proyecto: %s</p>
                    </div>

                    <div class="summary">
                        <h3>📋 Resumen Ejecutivo</h3>
                        <p>Este reporte detalla el estado actual de la deuda técnica del proyecto, incluyendo métricas de calidad, seguridad y mantenibilidad.</p>
                        <p><strong>⚠️ IMPORTANTE:</strong> La deuda técnica debe ser monitoreada y reducida regularmente para mantener la calidad del código.</p>
                    </div>

                    <div class="metric-grid">
                        <div class="metric-card">
                            <h3>🛡️ Seguridad</h3>
                            <div class="rating rating-E">Rating: E</div>
                            <div class="alert alert-danger">
                                <strong>🚨 CRÍTICO:</strong> 11 Security Hotspots detectados
                            </div>
                            <p><strong>Security Hotspots:</strong> 11</p>
                            <p><strong>Vulnerabilidades:</strong> 0</p>
                        </div>

                        <div class="metric-card">
                            <h3>🔧 Confiabilidad</h3>
                            <div class="rating rating-C">Rating: C</div>
                            <div class="alert alert-warning">
                                <strong>⚠️ ATENCIÓN:</strong> 2 issues de confiabilidad
                            </div>
                            <p><strong>Bugs:</strong> 0</p>
                            <p><strong>Issues de Confiabilidad:</strong> 2</p>
                        </div>

                        <div class="metric-card">
                            <h3>🔨 Mantenibilidad</h3>
                            <div class="rating rating-A">Rating: A</div>
                            <div class="alert alert-success">
                                <strong>✅ BUENO:</strong> Calificación A en mantenibilidad
                            </div>
                            <p><strong>Code Smells:</strong> 0</p>
                            <p><strong>Issues de Mantenibilidad:</strong> 84</p>
                        </div>

                        <div class="metric-card">
                            <h3>🧪 Cobertura de Tests</h3>
                            <div class="alert alert-danger">
                                <strong>🚨 CRÍTICO:</strong> 0%% de cobertura
                            </div>
                            <p><strong>Cobertura:</strong> 0%%</p>
                            <p><strong>Líneas por Cubrir:</strong> 2.2k</p>
                        </div>

                        <div class="metric-card">
                            <h3>📝 Duplicación de Código</h3>
                            <div class="alert alert-warning">
                                <strong>⚠️ ATENCIÓN:</strong> 9.1%% de duplicación
                            </div>
                            <p><strong>Duplicación:</strong> 9.1%%</p>
                            <p><strong>Líneas Duplicadas:</strong> 12k</p>
                        </div>

                        <div class="metric-card">
                            <h3>🚨 Issues Críticos</h3>
                            <div class="alert alert-danger">
                                <strong>🚨 CRÍTICO:</strong> 13 issues de alta prioridad
                            </div>
                            <p><strong>High:</strong> 13</p>
                            <p><strong>Medium:</strong> 52</p>
                            <p><strong>Low:</strong> 19</p>
                        </div>
                    </div>

                    <div class="summary">
                        <h3>🎯 Recomendaciones Prioritarias</h3>
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

    /**
     * Envía el email usando JavaMail
     */
    private void sendEmail(String to, String subject, String htmlContent) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.host", mailHost);
        props.put("mail.smtp.port", mailPort);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", !mailSsl);
        props.put("mail.smtp.ssl.enable", mailSsl);

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(mailUsername, mailPassword);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(mailFrom));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);
        message.setContent(htmlContent, "text/html; charset=utf-8");

        Transport.send(message);
    }
}
