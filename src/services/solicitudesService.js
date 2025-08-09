// src/services/solicitudesService.js
import axios from "axios";

// Función para enviar solicitud hospitalaria a la aseguradora (URL dinámica)
export const enviarSolicitudHospital = async ({ afiliado, servicio, monto, hospital, aseguradoraUrl }) => {
  try {
    const payload = {
      afiliado,
      servicio,
      hospital,
      monto,
      aseguradora: aseguradoraUrl
    };

    const response = await axios.post(`${aseguradoraUrl}/solicitudes-atencion`, payload);
    return response.data;

  } catch (error) {
    console.error("Error en enviarSolicitudHospital:", error.response?.data || error.message);
    throw error;
  }
};
