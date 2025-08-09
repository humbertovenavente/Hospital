import axios from "axios";
import API_URL from "../config"; //  Importar config dinámico (IP automática)

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
