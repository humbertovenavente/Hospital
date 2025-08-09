import axios from "axios";
import API_URL from "../config"; //  Correcto, traemos el config

const API = `${API_URL}/hospital/solicitudes`;

export const enviarSolicitudHospital = async (hospital) => {
  const res = await axios.post(API, hospital);
  return res.data;
};
