import axios from "axios";
import API_URL from "../config"; // Ajusta la ruta según la ubicación del archivo

const PACIENTES_API = `${API_URL}/pacientes`;

export default {
  getPacienteById(userId) {
    return axios.get(`${PACIENTES_API}/${userId}`);
  },

  updatePaciente(userId, pacienteData) {
    return axios.put(`${PACIENTES_API}/${userId}`, pacienteData);
  }
};
