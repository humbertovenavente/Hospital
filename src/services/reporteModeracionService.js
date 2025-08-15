import axios from 'axios';
// URL de la API para QA
const API_URL = 'http://localhost:8060';

export async function obtenerReporteModeracion(fechaInicio, fechaFin, limite = 10) {
  try {
    const response = await axios.get(`${API_URL}/api/reporte-moderacion/usuarios`, {
      params: {
        fechaInicio,
        fechaFin,
        limite
      }
    });
    return response.data;
  } catch (error) {
    console.error('Error obteniendo reporte de moderación:', error.response?.data || error.message);
    throw error;
  }
}
