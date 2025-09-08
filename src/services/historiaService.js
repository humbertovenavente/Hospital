import axios from "axios";
// URL de la API para QA
const API_URL = 'http://34.46.73.44:8030';

// 🔹 Obtener solo historias con estado PUBLICADO (vista pública)
export const getHistoriasPublicadas = async () => {
  try {
    const response = await axios.get(`${API_URL}/historias/publicadas`);
    return response.data;
  } catch (error) {
    console.error("Error al obtener historias publicadas:", error);
    throw error;
  }
};

// 🔹 Obtener historias pendientes (para moderador/admin)
export const getHistoriasPendientes = async () => {
  try {
    const response = await axios.get(`${API_URL}/historias/pendientes`);
    return response.data;
  } catch (error) {
    console.error("Error al obtener historias pendientes:", error);
    throw error;
  }
};

// ✅ Obtener historia por ID (para modo draft corregido)
export const getHistoriaById = async (id) => {
  try {
    const response = await axios.get(`${API_URL}/historias/${id}`);
    return response.data;
  } catch (error) {
    console.error("Error al obtener historia por ID:", error);
    throw error;
  }
};

// ✅ Crear historia (añadiendo editorEmail desde localStorage)
export const crearHistoria = async (data, editorEmail = null) => {
  try {
    data.editorEmail = editorEmail || localStorage.getItem("usuarioEmail");
    const response = await axios.post(`${API_URL}/historias`, data);
    return response.data;
  } catch (error) {
    console.error("Error al crear historia:", error);
    throw error;
  }
};

// ✅ Actualizar historia
export const actualizarHistoria = async (id, data, editorEmail = null) => {
  try {
    data.editorEmail = editorEmail || localStorage.getItem("usuarioEmail");
    const response = await axios.put(`${API_URL}/historias/${id}`, data);
    return response.data;
  } catch (error) {
    console.error("Error al actualizar historia:", error);
    throw error;
  }
};

// ✅ Aprobar historia
export const aprobarHistoria = async (id) => {
  try {
    const response = await axios.put(`${API_URL}/historias/aprobar/${id}`);
    return response.data;
  } catch (error) {
    console.error("Error al aprobar historia:", error);
    throw error;
  }
};

// ❌ Rechazar historia con motivo
export const rechazarHistoria = async (id, motivo) => {
  try {
    const response = await axios.put(`${API_URL}/historias/rechazar/${id}?motivo=${encodeURIComponent(motivo)}`);
    return response.data;
  } catch (error) {
    console.error("Error al rechazar historia:", error);
    throw error;
  }
};
