import axios from "axios";
// URL de la API para QA
const API_URL = 'http://34.46.73.44:8030';

const DOCTORES_API = `${API_URL}/doctores`;

export default {
  getDoctorById(userId) {
    return axios.get(`${DOCTORES_API}/${userId}`);
  },

  updateDoctor(userId, doctorData) {
    return axios.put(`${DOCTORES_API}/${userId}`, doctorData);
  }
};
