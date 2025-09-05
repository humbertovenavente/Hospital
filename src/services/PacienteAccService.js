import axios from "axios";
// URL de la API para QA
const API_URL = 'http://localhost:8080';

const PACIENTES_API = `${API_URL}/pacientes`;

export default {
  getPacienteById(userId) {
    return axios.get(`${PACIENTES_API}/${userId}`);
  },

  updatePaciente(userId, pacienteData) {
    return axios.put(`${PACIENTES_API}/${userId}`, pacienteData);
  }
};
