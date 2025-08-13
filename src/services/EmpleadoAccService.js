import axios from "axios";
// URL de la API para QA
const API_URL = 'http://localhost:8090';

const EMPLEADOS_API = `${API_URL}/empleados`;

export default {
  getEmpleadoById(userId) {
    return axios.get(`${EMPLEADOS_API}/${userId}`);
  },

  updateEmpleado(userId, empleadoData) {
    return axios.put(`${EMPLEADOS_API}/${userId}`, empleadoData);
  }
};
