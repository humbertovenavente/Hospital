import axios from "axios";
// URL de la API para QA
const API_URL = 'http://localhost:8060';

const API = `${API_URL}/hospital/solicitudes`;

export const enviarSolicitudHospital = async (hospital) => {
  const res = await axios.post(API, hospital);
  return res.data;
};
