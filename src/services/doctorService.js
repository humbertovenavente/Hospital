import axios from "axios";
import API_URL from "../config"; // Ajusta la ruta si es necesario

const DOCTOR_API = `${API_URL}/doctor`;

export default {
  async getAllDoctors() {
    try {
      const response = await axios.get(DOCTOR_API);
      return response.data; // Extraer los datos correctamente
    } catch (error) {
      console.error("Error al obtener la lista de doctores:", error);
      throw error;
    }
  },

  async registrarDoctor(doctor) {
    return await axios.post(DOCTOR_API, {
      usuario: {
        nombreUsuario: doctor.nombreUsuario,
        correo: doctor.correo,
        contrasena: doctor.password,
        rol: { id: 2 }, // ROL_ID = 2 para Doctores
      },
      apellido: doctor.apellido,
      documento: doctor.documento,
      fechaNacimiento: doctor.fechaNacimiento,
      genero: doctor.genero,
      telefono: doctor.telefono,
      especialidad: doctor.especialidad,
      numeroColegiado: doctor.numeroColegiado,
      horarioAtencion: doctor.horarioAtencion,
      fechaGraduacion: doctor.fechaGraduacion,
      universidadGraduacion: doctor.universidadGraduacion,
    }, {
      headers: { "Content-Type": "application/json" }
    });
  },

  async updateDoctor(id, doctor) {
    return axios.put(`${DOCTOR_API}/${id}`, {
      usuario: {
        nombreUsuario: doctor.usuario.nombreUsuario,
        correo: doctor.usuario.correo,
        contrasena: doctor.usuario.contrasena,
        rol: { id: 2 }
      },
      apellido: doctor.apellido,
      documento: doctor.documento,
      fechaNacimiento: doctor.fechaNacimiento,
      genero: doctor.genero,
      telefono: doctor.telefono,
      especialidad: doctor.especialidad,
      numeroColegiado: doctor.numeroColegiado,
      horarioAtencion: doctor.horarioAtencion,
      fechaGraduacion: doctor.fechaGraduacion,
      universidadGraduacion: doctor.universidadGraduacion,
    }, {
      headers: { "Content-Type": "application/json" }
    });
  },

  async deleteDoctor(id) {
    return axios.delete(`${DOCTOR_API}/${id}`);
  }
};
