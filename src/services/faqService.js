import axios from "axios";

// URL de la API para QA
const API_URL = 'http://localhost:8080';

// ✅ Crear una nueva pregunta (se guarda con estado PROCESO)
export const enviarPregunta = async (pregunta, autor) => {
  try {
    const editadoPor = localStorage.getItem("usuarioEmail");

    if (!editadoPor) throw new Error("No se encontró el email del editor en localStorage");

    const response = await axios.post(`${API_URL}/faq/crear`, {
      pregunta,
      autor,
      respuesta: null,
      editadoPor,
      status: "PROCESO",
    });
    return response.data;
  } catch (error) {
    console.error("🚨 Error al enviar la pregunta:", error.response?.data || error.message);
    throw error;
  }
};

// ✅ Obtener todas las preguntas (usado por admin)
export const obtenerPreguntas = async () => {
  try {
    const response = await axios.get(`${API_URL}/faq`);
    return response.data;
  } catch (error) {
    console.error("🚨 Error al obtener preguntas:", error.response?.data || error.message);
    throw error;
  }
};

// ✅ Obtener preguntas en estado PROCESO (moderación)
export const obtenerPendientesModeracion = async () => {
  try {
    const response = await axios.get(`${API_URL}/faq/pendientes`);
    return response.data;
  } catch (error) {
    console.error("🚨 Error al obtener preguntas pendientes:", error.response?.data || error.message);
    throw error;
  }
};

// ✅ Aprobar una pregunta (cambia estado a PUBLICADO)
export const aprobarPregunta = async (id) => {
  try {
    await axios.put(`${API_URL}/faq/aprobar/${id}`);
  } catch (error) {
    console.error("🚨 Error al aprobar la pregunta:", error.response?.data || error.message);
    throw error;
  }
};

// ✅ Rechazar una pregunta (cambia estado a RECHAZADO)
export const rechazarPregunta = async (id, motivo) => {
  try {
    await axios.put(`${API_URL}/faq/rechazar/${id}?motivo=${encodeURIComponent(motivo)}`);
  } catch (error) {
    console.error("🚨 Error al rechazar la pregunta:", error.response?.data || error.message);
    throw error;
  }
};

// ✅ Responder una pregunta (admin)
export const responderPregunta = async (id, respuesta) => {
  try {
    await axios.put(`${API_URL}/faq/responder/${id}`, { respuesta });
  } catch (error) {
    console.error("🚨 Error al responder la pregunta:", error.response?.data || error.message);
    throw error;
  }
};

// ✅ Editar una pregunta existente
export const editarPregunta = async (
  id,
  pregunta,
  respuesta,
  status = "PROCESO",
  rejectionReason = null,
  editadoPor = null // ✅ nuevo parámetro
) => {
  try {
    // Si no viene como parámetro, intenta tomarlo del localStorage
    if (!editadoPor) {
      editadoPor = localStorage.getItem("usuarioEmail");
    }

    if (!editadoPor) throw new Error("No se encontró el email del editor");

    const response = await axios.put(`${API_URL}/faq/editar/${id}`, {
      pregunta,
      respuesta,
      editadoPor,
      status,
      rejectionReason,
    });

    return response.data;
  } catch (error) {
    console.error("🚨 Error al editar la pregunta:", error.response?.data || error.message);
    throw error;
  }
};


// ✅ Eliminar una pregunta
export const eliminarPregunta = async (id) => {
  try {
    await axios.delete(`${API_URL}/faq/eliminar/${id}`);
  } catch (error) {
    console.error("🚨 Error al eliminar la pregunta:", error.response?.data || error.message);
    throw error;
  }
};

// ✅ Obtener preguntas publicadas (visibles para el usuario final)
export const obtenerPreguntasPublicadas = async () => {
  try {
    const response = await axios.get(`${API_URL}/faq/publicadas`);
    return response.data;
  } catch (error) {
    console.error("🚨 Error al obtener preguntas publicadas:", error.response?.data || error.message);
    throw error;
  }
};

// ✅ Obtener pregunta por ID (usado en la vista de corrección desde correo)
export const obtenerPreguntaPorId = async (id) => {
  try {
    const response = await axios.get(`${API_URL}/faq/${id}`);
    return response.data;
  } catch (error) {
    console.error("🚨 Error al obtener la pregunta por ID:", error.response?.data || error.message);
    throw error;
  }
};
