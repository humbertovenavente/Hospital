import axios from "axios";
import API_URL from "../config"; // Ajusta la ruta según la ubicación del archivo

const EMPLEADOS_API = `${API_URL}/empleados`;

export default {
  getEmpleadoById(userId) {
    return axios.get(`${EMPLEADOS_API}/${userId}`);
  },

  updateEmpleado(userId, empleadoData) {
    return axios.put(`${EMPLEADOS_API}/${userId}`, empleadoData);
  }
};
