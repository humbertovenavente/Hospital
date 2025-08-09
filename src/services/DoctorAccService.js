import axios from "axios";
import API_URL from "../config"; // Ajusta la ruta según sea necesario

const DOCTORES_API = `${API_URL}/doctores`;

export default {
  getDoctorById(userId) {
    return axios.get(`${DOCTORES_API}/${userId}`);
  },

  updateDoctor(userId, doctorData) {
    return axios.put(`${DOCTORES_API}/${userId}`, doctorData);
  }
};
