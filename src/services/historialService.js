// src/services/historialService.js
import axios from "axios";
import API_URL from "../config"; // Importa el config

const ASEGURADORA_API = `${API_URL}/clientes`;

/**
 * Envía el historial actualizado del cliente a la aseguradora.
 * @param {string} clienteId - ID del cliente
 * @param {object} data - Datos del historial a enviar
 * @returns {Promise<object>} Respuesta de la aseguradora
 */
export const enviarHistorialAseguradora = async (clienteId, data) => {
  try {
    const response = await axios.post(`${ASEGURADORA_API}/${clienteId}/historial`, data);
    return response.data;
  } catch (error) {
    console.error("Error al enviar historial a la aseguradora:", error.response?.data || error.message);
    throw error;
  }
};
