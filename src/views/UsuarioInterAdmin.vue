<template>
  <div class="usuariointer-container">
    <div class="header">Gestión de Usuarios Interconexión</div>

    <!-- Botón para agregar un usuario interconexion -->
    <button class="add-button" @click="abrirFormulario">Agregar Usuario Interconexión</button>

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
          <th>Acciones</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="usuarioInter in usuarioInterList" :key="usuarioInter.idInterconexion">
          <td>{{ usuarioInter.idInterconexion }}</td>


          <!-- Nombre -->
          <td>
            <input v-if="usuarioInter.editando" v-model="usuarioInter.usuario.nombreUsuario" />
            <span v-else>{{ usuarioInter.usuario.nombreUsuario }}</span>
          </td>

          <!-- Apellido -->
          <td>
            <input v-if="usuarioInter.editando" v-model="usuarioInter.apellido" />
            <span v-else>{{ usuarioInter.apellido }}</span>
          </td>

          <!-- Documento -->
          <td>
            <input v-if="usuarioInter.editando" v-model="usuarioInter.documento" />
            <span v-else>{{ usuarioInter.documento }}</span>
          </td>

          <!-- Fecha Nacimiento -->
          <td>
            <input v-if="usuarioInter.editando" v-model="usuarioInter.fechaNacimiento" type="date" />
            <span v-else>{{ usuarioInter.fechaNacimiento }}</span>
          </td>

          <!-- Género -->
          <td>
            <select v-if="usuarioInter.editando" v-model="usuarioInter.genero">
              <option value="Masculino">Masculino</option>
              <option value="Femenino">Femenino</option>
            </select>
            <span v-else>{{ usuarioInter.genero }}</span>
          </td>

          <!-- Teléfono -->
          <td>
            <input v-if="usuarioInter.editando" v-model="usuarioInter.telefono" />
            <span v-else>{{ usuarioInter.telefono }}</span>
          </td>

          <!-- Correo -->
          <td>
            <input v-if="usuarioInter.editando" v-model="usuarioInter.usuario.correo" />
            <span v-else>{{ usuarioInter.usuario.correo }}</span>
          </td>

          <!-- Contraseña con opción de mostrar -->
          <td>
            <input v-if="usuarioInter.editando" :type="mostrarContrasena ? 'text' : 'password'" v-model="usuarioInter.usuario.contrasena" />
            <span v-else>{{usuarioInter.usuario.contrasena }}</span>

          </td>

          <!-- Botones de acciones -->
          <td>
            <button class="edit-button" @click="usuarioInter.editando = !usuarioInter.editando">
              {{ usuarioInter.editando ? "Cancelar" : "Editar" }}
            </button>
            <button class="save-button" v-if="usuarioInter.editando" @click=" actualizarUsuario(usuarioInter)">Guardar</button>
            <button class="delete-button" @click="eliminarUsuario(usuarioInter.idInterconexion)">Eliminar</button>
          </td>
        </tr>
      </tbody>
    </table>

    <!-- Modal para Agregar usuario interconexion -->
    <div v-if="mostrarFormulario" class="modal-overlay">
      <div class="modal-content">
        <h2>Agregar Nuevo Usuario Interconexion</h2>
        <input v-model="nuevoUsuarioInter.nombreUsuario" placeholder="Nombre" />
        <input v-model="nuevoUsuarioInter.apellido" placeholder="Apellido" />
        <input v-model="nuevoUsuarioInter.documento" placeholder="Documento" />
        <input v-model="nuevoUsuarioInter.fechaNacimiento" type="date" />
        <select v-model="nuevoUsuarioInter.genero">
          <option value="Masculino">Masculino</option>
          <option value="Femenino">Femenino</option>
        </select>
        <input v-model="nuevoUsuarioInter.telefono" placeholder="Teléfono" />
        <input v-model="nuevoUsuarioInter.correo" placeholder="Correo" />
        <input v-model="nuevoUsuarioInter.password" type="password" placeholder="Contraseña" />

        <div class="modal-buttons">
          <button class="save-button" @click="registrarUsuarioInter">Guardar</button>
          <button class="cancel-button" @click="cerrarFormulario">Cancelar</button>
        </div>
      </div>
    </div>
  </div>
  </div>
</template>

<script>
import usuarioInterService from "@/services/usuarioInterService.js";
import emailjs from 'emailjs-com';

export default {
  data() {
    return {
      usuarioInterList: [],
      mostrarFormulario: false,
      mostrarContrasena: false,
      nuevoUsuarioInter: {
        nombreUsuario: "",
        apellido: "",
        documento: "",
        fechaNacimiento: "",
        genero: "Masculino",
        telefono: "",
        correo: "",
        password: "",
      },
    };
  },
  methods: {
    async obtenerUsuarioInter() {
      try {
        const response = await usuarioInterService.obtenerUsuarioInter();
        console.log("Respuesta de la API:", response.data);
        this.usuarioInterList = response.data.map(p => ({ ...p, editando: false }));
        console.log("Lista de usuarios después de asignar:", this.usuarioInterList);
      } catch (error) {
        console.error("Error obteniendo usuarios:", error);
        alert(" Error al cargar los usuarios.");
      }
    },



async actualizarUsuario(usuarioInter) {
      try {
        await usuarioInterService.actualizarUsuario(usuarioInter.idInterconexion, usuarioInter);
        usuarioInter.editando = false;
        alert(" usuario interconexion actualizado correctamente.");
      } catch (error) {
        console.error("Error al actualizar usuario interconexion, recuerda que no se puede usar el mismo correo electronico:", error);
        alert(" Error al actualizar el usuario interconexion, recuerda que no se puede usar el mismo correo electronico.");
      }
    },

    async eliminarUsuario(id) {
      if (!confirm("¿Estás seguro de eliminar este paciente?")) return;
      console.log("Intentando eliminar usuario con ID:", id);


      try {
        await usuarioInterService.eliminarUsuario(id);
        this.usuarioInterList = this.usuarioInterList.filter(p => p.idInterconexion !== id);
        alert(" usuario interconexion eliminado correctamente.");
      } catch (error) {
        console.error("Error al eliminar usuario interconexion:", error);
        alert("Error al eliminar el usuario interconexion.");
      }
    },


    abrirFormulario() {
      this.mostrarFormulario = true;
    },

    cerrarFormulario() {
      this.mostrarFormulario = false;
    },

    async registrarUsuarioInter() {
      try {
        await usuarioInterService.registrarUsuarioInter(this.nuevoUsuarioInter);
        this.enviarCorreo(this.nuevoUsuarioInter);
        this.cerrarFormulario();
        this.obtenerUsuarioInter();
        alert("Usuario registrado correctamente.");
      } catch (error) {
        console.error("Error al registrar usuario:", error);
        if (error.response && error.response.status === 400) {
          alert("Error: El correo ya está registrado.");
        } else {
          alert(" Error al registrar el usuario.");
        }
      }
    },

    enviarCorreo(usuarioInter) {
      emailjs.send("service_f70s6q3", "template_5cq4vng", {
        to_name: usuarioInter.nombreUsuario,
        to_email: usuarioInter.correo,
        message: `Tu cuenta ha sido creada con éxito.\nTu usuario es: ${usuarioInter.correo}\nContraseña: ${usuarioInter.password}, \nRol: usuarioInter `
      }, "SFAQ9kOAKVFMBgkSC")
      .then(() => {
        alert(" Correo de confirmación enviado.");
      }).catch((error) => {
        console.error("Error al enviar correo:", error);
        alert(" No se pudo enviar el correo.");
      });
    }
  },

  mounted() {
  this.obtenerUsuarioInter();
  console.log("Usuarios cargados:", this.usuarioInterList);
}

};
</script>


<style scoped>
.usuariointer-container {
  padding: 20px;
  background: #f9f9f9;
  color: #e0e1dd;
  border-radius: 10px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
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

.add-button {
  background-color: #DAFDBA;
  color: #012030;
  padding: 12px 18px;
  font-size: 16px;
  border-radius: 8px;
  cursor: pointer;
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
  vertical-align: middle;
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
