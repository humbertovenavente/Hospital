import axios from 'axios';
// URL de la API para QA
const API_URL = 'http://localhost:8060';

export async function obtenerReporteMedicinas(fechaInicio, fechaFin, limite = 10) {
  try {
    const response = await axios.get(`${API_URL}/reporte-medicinas`, {
      params: {
        inicio: fechaInicio,
        fin: fechaFin,
        limite: limite
      }
    });
    return response.data;
  } catch (error) {
    console.error('Error obteniendo reporte:', error.response?.data || error.message);
    throw error;
  }
}
