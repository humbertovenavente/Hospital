import axios from "axios";
import API_URL from "../config"; // Ajusta la ruta según la ubicación del archivo

const USUARIO_INTER_API = `${API_URL}/usuariointer`;

export default {
  obtenerUsuarioInter() {
    return axios.get(USUARIO_INTER_API);
  },

  async registrarUsuarioInter(usuarioInter) {
    return await axios.post(USUARIO_INTER_API, {
      usuario: {
        nombreUsuario: usuarioInter.nombreUsuario,
        correo: usuarioInter.correo,
        contrasena: usuarioInter.password,
        rol: { id: 5 }
      },
      apellido: usuarioInter.apellido,
      documento: usuarioInter.documento,
      fechaNacimiento: usuarioInter.fechaNacimiento,
      genero: usuarioInter.genero,
      telefono: usuarioInter.telefono,
    }, {
      headers: { "Content-Type": "application/json" }
    });
  },

  actualizarUsuario(id, usuarioInter) {
    return axios.put(`${USUARIO_INTER_API}/${id}`, usuarioInter);
  },

  eliminarUsuario(id) {
    return axios.delete(`${USUARIO_INTER_API}/${id}`);
  },
};
