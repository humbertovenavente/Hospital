import axios from "axios";
import API_URL from "../config"; // Ajusta la ruta según la ubicación del archivo

export const obtenerCitas = async () => {
  const response = await axios.get(`${API_URL}/citas`);
  return response.data;
};

export const obtenerPaciente = async () => {
  const response = await axios.get(`${API_URL}/paciente`);
  return response.data;
};

export const obtenerDoctor = async () => {
  const response = await axios.get(`${API_URL}/doctor`);
  return response.data;
};

export const obtenerMedicamentos = async () => {
  const response = await axios.get(`${API_URL}/medicamentos`);
  return response.data;
};
