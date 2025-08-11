import axios from "axios";
import API_URL from "../config"; // Ajusta la ruta según la ubicación del archivo

const RECETAS_API = `${API_URL}/recetas`;

export default {
  // Obtener todas las recetas
  async obtenerTodasLasRecetas() {
    try {
      const response = await axios.get(RECETAS_API);
      return response.data;
    } catch (error) {
      console.error("Error obteniendo recetas:", error);
      return [];
    }
  },

  // Obtener receta por ID
  async obtenerRecetaPorId(id) {
    try {
      const response = await axios.get(`${RECETAS_API}/${id}`);
      return response.data;
    } catch (error) {
      console.error(`Error obteniendo la receta con ID ${id}:`, error);
      return null;
    }
  },

  // Crear una nueva receta con múltiples medicamentos
  async crearReceta(receta) {
    try {
      const requestData = {
        codigoReceta: receta.codigoReceta,
        idCita: receta.idCita,
        idPaciente: receta.idPaciente,
        idDoctor: receta.idDoctor,
        anotaciones: receta.anotaciones,
        notasEspeciales: receta.notasEspeciales
      };

      console.log("📌 JSON enviado:", JSON.stringify(requestData, null, 2)); // 📌 Debugging
      const response = await axios.post(RECETAS_API, requestData, {
        headers: { "Content-Type": "application/json" },
      });

      console.log("✅ Receta creada con ID:", response.data.idReceta); // ✅ Debugging
      return response.data;
    } catch (error) {
      console.error("❌ Error creando receta:", error.response?.data || error);
      throw error;
    }
  },

  // Eliminar una receta
  async eliminarReceta(id) {
    try {
      await axios.delete(`${RECETAS_API}/${id}`);
    } catch (error) {
      console.error(`Error eliminando la receta con ID ${id}:`, error);
      throw error;
    }
  },

  async obtenerMedicamentosPorReceta(idReceta) {
    try {
        const response = await axios.get(`${API_URL}/receta-medicamentos/${idReceta}`);
        return response.data;
    } catch (error) {
        console.error(`Error obteniendo medicamentos de la receta ${idReceta}:`, error);
        return [];
    }
},

  // ✅ Agregar un medicamento a una receta existente
  async agregarMedicamento(medicamento) {
    try {
      if (!medicamento.idReceta || !medicamento.idMedicamento) {
        throw new Error("❌ Error: idReceta y idMedicamento son obligatorios.");
      }

      const requestData = {
        receta: { idReceta: medicamento.idReceta },
        medicamento: { idMedicamento: medicamento.idMedicamento },
        dosis: medicamento.dosis,
        frecuencia: medicamento.frecuencia,
        duracion: medicamento.duracion,
        diagnostico: medicamento.diagnostico || null // Asegurar que puede ser nulo
      };

      console.log("📌 JSON enviado para agregar medicamento:", JSON.stringify(requestData, null, 2));

      const response = await axios.post(`${RECETAS_API}/medicamentos`, requestData, {
        headers: { "Content-Type": "application/json" },
      });

      console.log("✅ Medicamento agregado:", response.data);
      return response.data;
    } catch (error) {
      console.error("❌ Error agregando medicamento:", error.response?.data || error);
      throw error;
    }
  },
};
