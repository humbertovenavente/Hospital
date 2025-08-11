import axios from "axios";
import API_URL from "../config"; // Ajusta la ruta según la ubicación del archivo

const USUARIOS_API = `${API_URL}/usuarios`;

export default {
  getUserById(userId) {
    return axios.get(`${USUARIOS_API}/${userId}`);
  },

  updateUser(userId, userData) {
    return axios.put(`${USUARIOS_API}/${userId}`, userData);
  }
};
