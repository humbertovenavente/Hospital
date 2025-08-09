import axios from 'axios';
import  API_URL  from '@/config';

/**
 * Envía una solicitud al endpoint para generar el reporte.
 * @param {Object} request - Objeto con los parámetros del reporte.
 *   Debe tener: idDoctor, fechaInicio (YYYY-MM-DD), fechaFin (YYYY-MM-DD), y tipoReporte ("AGRUPADO" o "DETALLADO")
 * @returns {Promise<Object>} La respuesta de la API con el reporte.
 */
export function generarReporte(request) {
  return axios
    .post(`${API_URL}/api/reportes/consultas`, request)
    .then(response => response.data)
    .catch(error => {
      console.error('Error en el servicio de reporte:', error);
      throw error;
    });
}
