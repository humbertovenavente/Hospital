import axios from 'axios';
import API_URL from "../config"; //  Importa tu config para que no esté quemado el localhost

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
