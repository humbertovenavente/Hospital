import axios from "axios";
// URL de la API para QA
const API_URL = 'http://localhost:8090';

const EMPLEADO_API = `${API_URL}/empleado`;

export default {
  getAllEmpleados() {
    return axios.get(EMPLEADO_API);
  },

  async registrarEmpleado(empleado) {
    return await axios.post(EMPLEADO_API, {
      usuario: {
        nombreUsuario: empleado.nombreUsuario,
        correo: empleado.correo,
        contrasena: empleado.password,
        rol: { id: 3 } // ROL_ID = 3 para empleados
      },
      apellido: empleado.apellido,
      documento: empleado.documento,
      fechaNacimiento: empleado.fechaNacimiento,
      genero: empleado.genero,
      telefono: empleado.telefono,
      puesto: empleado.puesto,
    }, {
      headers: { "Content-Type": "application/json" }
    });
  },

  updateEmpleado(id, empleado) {
    return axios.put(`${EMPLEADO_API}/${id}`, empleado);
  },

  deleteEmpleado(id) {
    return axios.delete(`${EMPLEADO_API}/${id}`);
  }
};
