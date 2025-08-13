import axios from "axios";
// URL de la API para QA
const API_URL = 'http://localhost:8060';

const USUARIOS_INTER_API = `${API_URL}/usuariosinter`;

export default {
  getUsuarioInterById(userId) {
    return axios.get(`${USUARIOS_INTER_API}/${userId}`);
  },

  updateUsuarioInter(userId, usuarioInterData) {
    return axios.put(`${USUARIOS_INTER_API}/${userId}`, usuarioInterData);
  }
};
