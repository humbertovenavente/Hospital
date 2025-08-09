import axios from "axios";
import API_URL from "../config"; // Ajusta la ruta si es necesario

const FICHA_API = `${API_URL}/fichas-tecnicas`;

export default {
  /**
   * Obtiene todas las fichas técnicas desde el backend.
   * @returns {Promise<Array>} Lista de fichas técnicas.
   */
  async getAllFichas() {
    try {
      const response = await axios.get(FICHA_API);
      return response.data;
    } catch (error) {
      console.error("Error al obtener la lista de fichas técnicas:", error);
      throw error;
    }
  },

  /**
   * Obtiene una ficha técnica específica por ID de paciente.
   * @param {number} idPaciente - ID del paciente.
   * @returns {Promise<Object>} Datos de la ficha técnica del paciente.
   */
  async getFichaByPaciente(idPaciente) {
    try {
      const response = await axios.get(`${FICHA_API}/${idPaciente}`);
      return response.data;
    } catch (error) {
      console.error(`Error al obtener la ficha del paciente ID ${idPaciente}:`, error);
      throw error;
    }
  },

  /**
   * Crea una nueva ficha técnica.
   * @param {Object} ficha - Datos de la nueva ficha técnica.
   * @returns {Promise<Object>} Ficha técnica creada.
   */
  async registrarFicha(ficha) {
    return axios.post(FICHA_API, {
      paciente: { idPaciente: ficha.idPaciente },
      idServicio: ficha.idServicio,
      fechaCreacion: ficha.fechaCreacion,
      historialServicios: ficha.historialServicios,
      numeroAfiliacion: ficha.numeroAfiliacion,
      codigoSeguro: ficha.codigoSeguro,
      carnetSeguro: ficha.carnetSeguro,
    }, {
      headers: { "Content-Type": "application/json" }
    });
  },

  /**
   * Actualiza una ficha técnica existente.
   * @param {number} idFicha - ID de la ficha técnica.
   * @param {Object} ficha - Datos actualizados de la ficha técnica.
   * @returns {Promise<Object>} Ficha técnica actualizada.
   */
  async updateFicha(idFicha, ficha) {
    return axios.put(`${FICHA_API}/${idFicha}`, {
      paciente: { idPaciente: ficha.idPaciente },
      idServicio: ficha.idServicio,
      fechaCreacion: ficha.fechaCreacion,
      historialServicios: ficha.historialServicios,
      numeroAfiliacion: ficha.numeroAfiliacion,
      codigoSeguro: ficha.codigoSeguro,
      carnetSeguro: ficha.carnetSeguro,
    }, {
      headers: { "Content-Type": "application/json" }
    });
  },

  /**
   * Elimina una ficha técnica por su ID.
   * @param {number} idFicha - ID de la ficha técnica a eliminar.
   */
  async deleteFicha(idFicha) {
    return axios.delete(`${FICHA_API}/${idFicha}`);
  }
};
