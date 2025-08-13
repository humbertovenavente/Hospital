import axios from "axios";
// URL de la API para QA
const API_URL = 'http://localhost:8090';

// nviar solicitud hospitalaria a Quarkus
export const enviarSolicitudHospital = async (hospital) => {
  try {
    const response = await axios.post(`${API_URL}/hospital/solicitudes`, hospital);
    return response.data;
  } catch (error) {
    console.error("Error al enviar solicitud hospital:", error.response?.data || error.message);
    throw error;
  }
};
