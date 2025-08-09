import axios from "axios";
import API_URL from "../config"; // Ajusta la ruta si es necesario

export default {
  async getCatalogoDoctores() {
    try {
      // Obtener lista de doctores
      const doctorResponse = await axios.get(`${API_URL}/doctor`);
      const doctorList = doctorResponse.data;

      // Obtener nombres de usuario para cada doctor
      const promises = doctorList.map(async (doctor) => {
        try {
          const userResponse = await axios.get(`${API_URL}/usuarios/${doctor.idUsuario}`);
          return {
            ...doctor,
            nombreUsuario: userResponse.data.nombreUsuario || "Sin Nombre"
          };
        } catch (error) {
          console.error(`Error obteniendo usuario para ID ${doctor.idUsuario}:`, error);
          return { ...doctor, nombreUsuario: "Desconocido" };
        }
      });

      return await Promise.all(promises);
    } catch (error) {
      console.error("Error obteniendo el catálogo de doctores:", error);
      return [];
    }
  }
};
