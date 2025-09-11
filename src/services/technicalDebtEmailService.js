import axios from 'axios';

const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080';

/**
 * Servicio para enviar reportes de deuda técnica por email
 */
class TechnicalDebtEmailService {

  /**
   * Obtiene métricas de deuda técnica desde SonarQube
   * @param {string} projectKey - Clave del proyecto en SonarQube
   * @returns {Promise<Object>} Métricas de deuda técnica
   */
  async getTechnicalDebtMetrics(projectKey) {
    try {
      // Obtener métricas principales
      const metricsResponse = await axios.get(`${API_URL}/api/sonarqube/metrics/${projectKey}`);

      // Obtener issues por categoría
      const issuesResponse = await axios.get(`${API_URL}/api/sonarqube/issues/${projectKey}`);

      return {
        metrics: metricsResponse.data,
        issues: issuesResponse.data,
        timestamp: new Date().toISOString()
      };
    } catch (error) {
      console.error('Error obteniendo métricas de deuda técnica:', error);
      throw error;
    }
  }

  /**
   * Genera reporte HTML de deuda técnica
   * @param {Object} data - Datos de métricas e issues
   * @param {string} projectName - Nombre del proyecto
   * @returns {string} HTML del reporte
   */
  generateTechnicalDebtReport(data, projectName) {
    const { metrics, issues, timestamp } = data;

    return `
      <!DOCTYPE html>
      <html>
      <head>
        <meta charset="UTF-8">
        <title>Reporte de Deuda Técnica - ${projectName}</title>
        <style>
          body { font-family: Arial, sans-serif; margin: 20px; }
          .header { background: #2c3e50; color: white; padding: 20px; border-radius: 8px; }
          .metric-card { border: 1px solid #ddd; margin: 10px 0; padding: 15px; border-radius: 5px; }
          .critical { border-left: 5px solid #e74c3c; background: #fdf2f2; }
          .high { border-left: 5px solid #e67e22; background: #fef9f3; }
          .medium { border-left: 5px solid #f39c12; background: #fefcf0; }
          .low { border-left: 5px solid #27ae60; background: #f0f9f0; }
          .rating { font-size: 24px; font-weight: bold; }
          .rating-A { color: #27ae60; }
          .rating-B { color: #2ecc71; }
          .rating-C { color: #f39c12; }
          .rating-D { color: #e67e22; }
          .rating-E { color: #e74c3c; }
          .summary { background: #ecf0f1; padding: 15px; border-radius: 5px; margin: 20px 0; }
          .footer { text-align: center; margin-top: 30px; color: #7f8c8d; font-size: 12px; }
        </style>
      </head>
      <body>
        <div class="header">
          <h1>📊 Reporte de Deuda Técnica</h1>
          <h2>${projectName}</h2>
          <p>Generado el: ${new Date(timestamp).toLocaleString('es-ES')}</p>
        </div>

        <div class="summary">
          <h3>📋 Resumen Ejecutivo</h3>
          <p>Este reporte detalla el estado actual de la deuda técnica del proyecto, incluyendo métricas de calidad, seguridad y mantenibilidad.</p>
        </div>

        <div class="metric-card">
          <h3>🛡️ Seguridad</h3>
          <div class="rating rating-${metrics.securityRating || 'E'}">Rating: ${metrics.securityRating || 'E'}</div>
          <p><strong>Security Hotspots:</strong> ${metrics.securityHotspots || 0}</p>
          <p><strong>Vulnerabilidades:</strong> ${metrics.vulnerabilities || 0}</p>
        </div>

        <div class="metric-card">
          <h3>🔧 Confiabilidad</h3>
          <div class="rating rating-${metrics.reliabilityRating || 'C'}">Rating: ${metrics.reliabilityRating || 'C'}</div>
          <p><strong>Bugs:</strong> ${metrics.bugs || 0}</p>
          <p><strong>Issues de Confiabilidad:</strong> ${metrics.reliabilityIssues || 0}</p>
        </div>

        <div class="metric-card">
          <h3>🔨 Mantenibilidad</h3>
          <div class="rating rating-${metrics.maintainabilityRating || 'A'}">Rating: ${metrics.maintainabilityRating || 'A'}</div>
          <p><strong>Code Smells:</strong> ${metrics.codeSmells || 0}</p>
          <p><strong>Issues de Mantenibilidad:</strong> ${metrics.maintainabilityIssues || 0}</p>
          <p><strong>Deuda Técnica:</strong> ${metrics.technicalDebt || 'N/A'}</p>
        </div>

        <div class="metric-card">
          <h3>🧪 Cobertura de Tests</h3>
          <p><strong>Cobertura:</strong> ${metrics.coverage || 0}%</p>
          <p><strong>Líneas por Cubrir:</strong> ${metrics.uncoveredLines || 0}</p>
        </div>

        <div class="metric-card">
          <h3>📝 Duplicación de Código</h3>
          <p><strong>Duplicación:</strong> ${metrics.duplications || 0}%</p>
          <p><strong>Líneas Duplicadas:</strong> ${metrics.duplicatedLines || 0}</p>
        </div>

        <div class="metric-card">
          <h3>🚨 Issues Críticos</h3>
          <p><strong>High:</strong> ${issues.high || 0}</p>
          <p><strong>Medium:</strong> ${issues.medium || 0}</p>
          <p><strong>Low:</strong> ${issues.low || 0}</p>
        </div>

        <div class="footer">
          <p>Este reporte fue generado automáticamente por el sistema de monitoreo de calidad</p>
          <p>Hospital Management System - SonarQube Integration</p>
        </div>
      </body>
      </html>
    `;
  }

  /**
   * Envía reporte de deuda técnica por email
   * @param {string} projectKey - Clave del proyecto
   * @param {string} projectName - Nombre del proyecto
   * @param {string} recipientEmail - Email del destinatario
   * @returns {Promise<Object>} Resultado del envío
   */
  async sendTechnicalDebtReport(projectKey, projectName, recipientEmail) {
    try {
      // Obtener métricas
      const metricsData = await this.getTechnicalDebtMetrics(projectKey);

      // Generar reporte HTML
      const htmlReport = this.generateTechnicalDebtReport(metricsData, projectName);

      // Enviar email
      const emailData = {
        to: recipientEmail,
        subject: `📊 Reporte de Deuda Técnica - ${projectName}`,
        html: htmlReport,
        projectKey: projectKey,
        projectName: projectName
      };

      const response = await axios.post(`${API_URL}/api/email/technical-debt`, emailData);

      return {
        success: true,
        message: 'Reporte de deuda técnica enviado exitosamente',
        data: response.data
      };
    } catch (error) {
      console.error('Error enviando reporte de deuda técnica:', error);
      throw error;
    }
  }

  /**
   * Envía reporte de deuda técnica para múltiples proyectos
   * @param {Array} projects - Array de proyectos [{key, name}]
   * @param {string} recipientEmail - Email del destinatario
   * @returns {Promise<Array>} Resultados de envío
   */
  async sendMultiProjectTechnicalDebtReport(projects, recipientEmail) {
    const results = [];

    for (const project of projects) {
      try {
        const result = await this.sendTechnicalDebtReport(
          project.key,
          project.name,
          recipientEmail
        );
        results.push({ project: project.name, success: true, result });
      } catch (error) {
        results.push({ project: project.name, success: false, error: error.message });
      }
    }

    return results;
  }
}

export default new TechnicalDebtEmailService();
