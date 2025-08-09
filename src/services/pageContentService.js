import axios from "axios";
import API_URL from "@/config";

// ✅ Obtener contenido publicado por nombre de página
export const getPublishedContent = async (pageName) => {
  try {
    const response = await axios.get(`${API_URL}/api/page-content/${pageName}`);
    return response.data;
  } catch (error) {
    console.error("Error al obtener contenido publicado:", error);
    throw error;
  }
};

// ✅ Obtener contenido en estado PROCESO (borradores)
export const getDraftContent = async () => {
  try {
    const response = await axios.get(`${API_URL}/api/page-content/drafts`);
    return response.data;
  } catch (error) {
    console.error("Error al obtener borradores:", error);
    throw error;
  }
};

export const getContentById = async (id) => {
  const response = await axios.get(`${API_URL}/api/page-content/contenido/${id}`);
  return response.data;
};


// ✅ Obtener contenido pendiente de moderación
export const getPendingContent = async () => {
  try {
    const response = await axios.get(`${API_URL}/api/page-content/pendientes`);
    return response.data;
  } catch (error) {
    console.error("Error al obtener contenido pendiente:", error);
    throw error;
  }
};

// ✅ Crear nuevo contenido
export const createContent = async (contentData) => {
  try {
    contentData.editorEmail =       localStorage.getItem("usuarioEmail");
    const response = await axios.post(`${API_URL}/api/page-content`, contentData);
    return response.data;
  } catch (error) {
    console.error("Error al crear contenido:", error);
    throw error;
  }
};

// ✅ Actualizar contenido existente por ID
export const updateContent = async (contentId, contentData) => {
  try {
    contentData.editorEmail =       localStorage.getItem("usuarioEmail");
    const response = await axios.put(`${API_URL}/api/page-content/${contentId}`, contentData);
    return response.data;
  } catch (error) {
    console.error("Error al actualizar contenido:", error);
    throw error;
  }
};

// ✅ Publicar contenido (cambiar estado a PUBLICADO)
export const publishContent = async (contentId) => {
  try {
    const response = await axios.put(`${API_URL}/api/page-content/${contentId}/publish`);
    return response.data;
  } catch (error) {
    console.error("Error al publicar contenido:", error);
    throw error;
  }
};

// ✅ Rechazar contenido con motivo
export const rejectContent = async (contentId, motivo) => {
  try {
    const response = await axios.put(
      `${API_URL}/api/page-content/${contentId}/reject?motivo=${encodeURIComponent(motivo)}`
    );
    return response.data;
  } catch (error) {
    console.error("Error al rechazar contenido:", error);
    throw error;
  }
};

// ✅ Eliminar contenido por ID
export const deleteContent = async (contentId) => {
  try {
    await axios.delete(`${API_URL}/api/page-content/${contentId}`);
  } catch (error) {
    console.error("Error al eliminar contenido:", error);
    throw error;
  }
};
