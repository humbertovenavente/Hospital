import axios from 'axios';
import API_URL from "../config"; //  Importa la configuración correcta

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
