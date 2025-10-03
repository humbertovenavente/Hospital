import axios from "axios";
// URL de la API para QA
const API_URL = 'http://34.61.228.49:8030';

const API = `${API_URL}/hospital/solicitudes`;

export const enviarSolicitudHospital = async (hospital) => {
  const res = await axios.post(API, hospital);
  return res.data;
};
