import axios from "axios";
import API_URL from "../config"; // Ajusta la ruta según la ubicación del archivo

const USUARIOS_INTER_API = `${API_URL}/usuariosinter`;

export default {
  getUsuarioInterById(userId) {
    return axios.get(`${USUARIOS_INTER_API}/${userId}`);
  },

  updateUsuarioInter(userId, usuarioInterData) {
    return axios.put(`${USUARIOS_INTER_API}/${userId}`, usuarioInterData);
  }
};
