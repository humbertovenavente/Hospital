<template>
  <div class="empleado-container">
    <div class="header">Gestión de Empleados</div>

    <!-- Botón para agregar un empleado -->
    <button class="add-button" @click="abrirFormulario">Agregar Empleado</button>

    <div class="section">
    <table>
      <thead>
        <tr>
          <th>ID</th>
          <th>Nombre</th>
          <th>Apellido</th>
          <th>Documento</th>
          <th>Fecha Nacimiento</th>
          <th>Género</th>
          <th>Teléfono</th>
          <th>Correo</th>
          <th>Contraseña</th>
          <th>Puesto</th>
          <th>Acciones</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="empleado in empleadoList" :key="empleado.idEmpleado">
          <td>{{ empleado.idEmpleado }}</td>
          <td>
            <input v-if="empleado.editando" v-model="empleado.usuario.nombreUsuario" />
            <span v-else>{{ empleado.usuario.nombreUsuario }}</span>
          </td>
          <td>
            <input v-if="empleado.editando" v-model="empleado.apellido" />
            <span v-else>{{ empleado.apellido }}</span>
          </td>
          <td>
            <input v-if="empleado.editando" v-model="empleado.documento" />
            <span v-else>{{ empleado.documento }}</span>
          </td>
          <td>
            <input v-if="empleado.editando" v-model="empleado.fechaNacimiento" type="date" />
            <span v-else>{{ empleado.fechaNacimiento }}</span>
          </td>
          <td>
            <select v-if="empleado.editando" v-model="empleado.genero">
              <option value="Masculino">Masculino</option>
              <option value="Femenino">Femenino</option>
            </select>
            <span v-else>{{ empleado.genero }}</span>
          </td>
          <td>
            <input v-if="empleado.editando" v-model="empleado.telefono" />
            <span v-else>{{ empleado.telefono }}</span>
          </td>
          <td>
            <input v-if="empleado.editando" v-model="empleado.usuario.correo" />
            <span v-else>{{ empleado.usuario.correo }}</span>
          </td>
          <td>
            <input v-if="empleado.editando" :type="mostrarContrasena ? 'text' : 'password'" v-model="empleado.usuario.contrasena" />
            <span v-else>{{ empleado.usuario.contrasena }}</span>
          </td>
          <td>
            <input v-if="empleado.editando" v-model="empleado.puesto" />
            <span v-else>{{ empleado.puesto }}</span>
          </td>
          <td>
            <button class="edit-button" @click="empleado.editando = !empleado.editando">
              {{ empleado.editando ? "Cancelar" : "Editar" }}
            </button>
            <button class="save-button" v-if="empleado.editando" @click="updateEmpleado(empleado)">Guardar</button>
            <button class="delete-button" @click="deleteEmpleado(empleado.idEmpleado)">Eliminar</button>
          </td>
        </tr>
      </tbody>
    </table>

    <!-- Modal para Agregar Empleado -->
    <div v-if="mostrarFormulario" class="modal-overlay">
      <div class="modal-content">
        <h2>Agregar Nuevo Empleado</h2>
        <input v-model="nuevoEmpleado.nombreUsuario" placeholder="Nombre" />
        <input v-model="nuevoEmpleado.apellido" placeholder="Apellido" />
        <input v-model="nuevoEmpleado.documento" placeholder="Documento" />
        <input v-model="nuevoEmpleado.fechaNacimiento" type="date" />
        <select v-model="nuevoEmpleado.genero">
          <option value="Masculino">Masculino</option>
          <option value="Femenino">Femenino</option>
        </select>
        <input v-model="nuevoEmpleado.telefono" placeholder="Teléfono" />
        <input v-model="nuevoEmpleado.correo" placeholder="Correo" />
        <input v-model="nuevoEmpleado.password" type="password" placeholder="Contraseña" />
        <input v-model="nuevoEmpleado.puesto" placeholder="Puesto" />

        <div class="modal-buttons">
          <button class="save-button" @click="registrarEmpleado">Guardar</button>
          <button class="cancel-button" @click="cerrarFormulario">Cancelar</button>
        </div>
      </div>
    </div>
  </div>
</div>
</template>

<script>
import empleadoService from "@/services/empleadoService.js";
import emailjs from "emailjs-com";

export default {
  data() {
    return {
      empleadoList: [],
      mostrarFormulario: false,
      mostrarContrasena: false,
      nuevoEmpleado: {
        nombreUsuario: "",
        apellido: "",
        documento: "",
        fechaNacimiento: "",
        genero: "Masculino",
        telefono: "",
        correo: "",
        password: "",
        puesto: "",
      },
    };
  },
  methods: {
    async getAllEmpleados() {
      try {
        const response = await empleadoService.getAllEmpleados();
        this.empleadoList = response.data.map(e => ({ ...e, editando: false }));
      } catch (error) {
        console.error("Error obteniendo empleados:", error);
        alert("Error al cargar los empleados.");
      }
    },

    async updateEmpleado(empleado) {
      try {
        await empleadoService.updateEmpleado(empleado.idEmpleado, empleado);
        empleado.editando = false;
        alert("Empleado actualizado correctamente.");
      } catch (error) {
        console.error("Error al actualizar empleado:", error);
        alert("Error al actualizar el empleado.");
      }
    },

    async deleteEmpleado(id) {
      if (!confirm("¿Estás seguro de eliminar este empleado?")) return;

      try {
        await empleadoService.deleteEmpleado(id);
        this.empleadoList = this.empleadoList.filter(e => e.idEmpleado !== id);
        alert("Empleado eliminado correctamente.");
      } catch (error) {
        console.error("Error al eliminar empleado:", error);
        alert("Error al eliminar el empleado.");
      }
    },

    abrirFormulario() {
      this.mostrarFormulario = true;
    },

    cerrarFormulario() {
      this.mostrarFormulario = false;
    },

    async registrarEmpleado() {
      try {
        await empleadoService.registrarEmpleado(this.nuevoEmpleado);
        this.enviarCorreo(this.nuevoEmpleado);
        this.cerrarFormulario();
        this.getAllEmpleados();
        alert("Empleado registrado correctamente.");
      } catch (error) {
        console.error("Error al registrar empleado:", error);
        if (error.response && error.response.status === 400) {
          alert("Error: El correo ya está registrado.");
        } else {
          alert("Error al registrar el empleado.");
        }
      }
    },

    enviarCorreo(empleado) {
      emailjs
        .send(
          "service_f70s6q3",
          "template_5cq4vng",
          {
            to_name: empleado.nombreUsuario,
            to_email: empleado.correo,
            message: `Tu cuenta ha sido creada con éxito.\nTu usuario es: ${empleado.correo}\nContraseña: ${empleado.password}, \nRol: Empleado`,
          },
          "SFAQ9kOAKVFMBgkSC"
        )
        .then(() => {
          alert("Correo de confirmación enviado.");
        })
        .catch(error => {
          console.error("Error al enviar correo:", error);
          alert("No se pudo enviar el correo.");
        });
    },
  },

  mounted() {
    this.getAllEmpleados();
  },
};
</script>


<style scoped>
.empleado-container {
  padding: 20px;
  background: #f9f9f9;
  color: #e0e1dd;
  border-radius: 10px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
}

.add-button {
  background-color: #DAFDBA;
  color: #012030;
  padding: 12px 18px;
  font-size: 16px;
  border-radius: 8px;
  cursor: pointer;
}

.header {
  text-align: center;
  font-size: 22px;
  font-weight: bold;
  background: #45C4B0;
  color: white;
  padding: 12px;
  border-radius: 5px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.4);
}

.section {
  border: 1px solid #45C4B0;
  padding: 15px;
  margin: 10px 0;
  background: #13678a;
  border-radius: 8px;
}

table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 15px;
}

th, td {
  padding: 12px;
  border: 1px solid #DAFDBA;
  text-align: center; /* Alinear al centro */
  vertical-align: middle; /* Centrar contenido verticalmente */
}

th {
  background: #01324b;
}

input, select {
  padding: 8px;
  font-size: 16px;
  width: 100%;
}

.save-button, .edit-button, .delete-button {
  padding: 10px;
  border-radius: 5px;
  font-size: 14px;
  cursor: pointer;
}

.edit-button { background-color: #f0ad4e; color: white; }
.save-button { background-color: #007bff; color: white; }
.delete-button { background-color: #ff8a7d; color: white; }
</style>
