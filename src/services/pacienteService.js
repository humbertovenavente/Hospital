import axios from "axios";
import API_URL from "../config"; // Ajusta la ruta según la ubicación del archivo

const PACIENTE_API = `${API_URL}/paciente`;

export default {
  getAllPaciente() {
    return axios.get(PACIENTE_API);
  },

  async registrarPaciente(paciente) {
    return await axios.post(PACIENTE_API, {
      usuario: {
        nombreUsuario: paciente.nombreUsuario,
        correo: paciente.correo,
        contrasena: paciente.password,
        rol: { id: 4 } // ROL_ID = 4 para pacientes
      },
      apellido: paciente.apellido,
      documento: paciente.documento,
      fechaNacimiento: paciente.fechaNacimiento,
      genero: paciente.genero,
      telefono: paciente.telefono,
    }, {
      headers: { "Content-Type": "application/json" }
    });
  },

  updatePaciente(id, paciente) {
    return axios.put(`${PACIENTE_API}/${id}`, paciente);
  },

  deletePaciente(id) {
    return axios.delete(`${PACIENTE_API}/${id}`);
  }
};
