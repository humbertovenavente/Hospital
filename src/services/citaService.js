import axios from "axios";
// URL de la API para QA
const API_URL = 'http://34.61.228.49:8030';

const CITA_API = `${API_URL}/citas`;

const citaService = {
  async obtenerCitas() {
    try {
      const response = await axios.get(CITA_API);
      return response.data;
    } catch (error) {
      console.error("Error al obtener citas:", error);
      throw error;
    }
  },
  async agendarCita(cita) {
    try {
      console.log("📌 Enviando cita al backend:", JSON.stringify(cita, null, 2)); // 🔍 DEBUG
      await axios.post(CITA_API, cita);
    } catch (error) {
      console.error("❌ Error al agendar cita:", error);
      throw error;
    }
  }
  ,
  async cancelarCita(id) {
    try {
      await axios.put(`${CITA_API}/${id}/cancelar`);
    } catch (error) {
      console.error("Error al cancelar cita:", error);
      throw error;
    }
  },

  async procesarCita(id) {
    try {
      await axios.put(`${CITA_API}/${id}/procesar`);
    } catch (error) {
      console.error("Error al procesar cita:", error);
      throw error;
    }
  },

  async reasignarCita(id, idDoctor) {
    try {
      await axios.put(`${CITA_API}/${id}/reasignar`, { idDoctor });
    } catch (error) {
      console.error("Error al reasignar cita:", error);
      throw error;
    }
  },



};

export default citaService;
