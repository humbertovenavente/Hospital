import axios from "axios";
import API_URL from "../config";

const API_ENDPOINT = `${API_URL}/api/servicios`;

export const getServicios = async () => {
  const response = await axios.get(API_ENDPOINT);
  return response.data;
};

export const addServicio = async (servicio, parentId = null) => {
  try {
    // Si `parentId` existe, lo incluimos en el body
    const requestBody = parentId ? { ...servicio, parentId } : servicio;

    const response = await axios.post(API_ENDPOINT, requestBody);
    return response.data;
  } catch (error) {
    console.error("Error al agregar servicio:", error.response ? error.response.data : error);
    throw error;
  }
};

export const deleteServicio = async (id) => {
  await axios.delete(`${API_ENDPOINT}/${id}`);
};

export const addSubServicio = async (parentId, subServicio) => {
  if (!subServicio || !subServicio.id) {
    console.error("Error: El subservicio debe tener un ID válido.");
    return;
  }

  try {
    // 🔹 Solo enviar el ID, no el objeto completo
    const requestBody = { subServicioId: subServicio.id };

    const response = await axios.post(`${API_ENDPOINT}/${parentId}/subservicios`, requestBody);
    return response.data;
  } catch (error) {
    console.error("Error al agregar subservicio:", error.response ? error.response.data : error);
    throw error;
  }
};







export const getSubServicios = async (id) => {
  const response = await axios.get(`${API_ENDPOINT}/${id}/subservicios`);
  return response.data;
};

export const deleteSubServicio = async (id, subServicioId) => {
  await axios.delete(`${API_ENDPOINT}/${id}/subservicios/${subServicioId}`);
};
