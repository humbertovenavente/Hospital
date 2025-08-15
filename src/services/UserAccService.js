import axios from "axios";
// URL de la API para QA
const API_URL = 'http://localhost:8060';

const USUARIOS_API = `${API_URL}/usuarios`;

export default {
  getUserById(userId) {
    return axios.get(`${USUARIOS_API}/${userId}`);
  },

  updateUser(userId, userData) {
    return axios.put(`${USUARIOS_API}/${userId}`, userData);
  }
};
