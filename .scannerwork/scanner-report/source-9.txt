<template>
  <div class="admin-usuarios">
    <div class="header">Administración de Usuarios</div>

    <div class="section">
    <table>
      <thead>
        <tr>
          <th>ID</th>
          <th>Nombre</th>
          <th>Correo</th>
          <th>Estado</th>
          <th>Rol a asignar</th>
          <th>Acciones</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="usuario in usuarios" :key="usuario.id">
          <td>{{ usuario.id }}</td>
          <td>{{ usuario.nombreUsuario }}</td>
          <td>{{ usuario.correo }}</td>
          <td>
            <span :class="usuario.estado === 1 ? 'activo' : 'inactivo'">
              {{ usuario.estado === 1 ? 'Activo' : 'Inactivo' }}
            </span>
          </td>
          <td>
  <span v-if="usuario.estado === 1">
    {{ usuario.rol ? usuario.rol.roleName : "Sin rol asignado" }}
  </span>
  <span v-else>Sin rol asignado</span>
</td>
<td v-if="usuario.estado === 0">
  <select v-model="usuario.rolSeleccionado">
    <option disabled value="">Seleccione un rol</option>
    <option v-for="rol in roles" :key="rol.id" :value="rol.id">
      {{ rol.roleName }}
    </option>
  </select>
</td>


          <td>
            <button v-if="usuario.estado === 0" @click="activarUsuario(usuario)">
              Activar
            </button>
            <button v-else class="desactivar" @click="desactivarUsuario(usuario)">
              Desactivar
            </button>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import axios from "axios";
import emailjs from "emailjs-com";

const usuarios = ref([]);
const roles = ref([]);

// Configuración de EmailJS
const SERVICE_ID = "service_f70s6q3";
const TEMPLATE_ID = "template_5cq4vng";
const PUBLIC_KEY = "SFAQ9kOAKVFMBgkSC";

/**
 * Obtiene la lista de usuarios desde el backend.
 */
async function fetchUsuarios() {
  try {
    const response = await axios.get("/usuarios");
    usuarios.value = response.data.map(usuario => ({
      ...usuario,
      rolSeleccionado: null // Para permitir selección de rol antes de activar
    }));
  } catch (error) {
    console.error("Error al cargar usuarios:", error);
  }
}

/**
 * Obtiene la lista de roles desde el backend.
 */
async function fetchRoles() {
  try {
    const response = await axios.get("/usuarios/roles");
    roles.value = response.data;
  } catch (error) {
    console.error("Error al cargar roles:", error);
  }
}

/**
 * Envía un correo de activación con el rol asignado.
 */
const sendActivationEmail = async (userEmail, userName, roleName) => {
  try {
    const templateParams = {
      to_email: userEmail,
      to_name: userName,
      message: `Your account has been activated. Your assigned role is: ${roleName}`
    };

    await emailjs.send(SERVICE_ID, TEMPLATE_ID, templateParams, PUBLIC_KEY);
    console.log("Correo de activación enviado con éxito");
  } catch (error) {
    console.error("Error enviando correo de activación:", error);
  }
};

/**
 * Activa un usuario y le envía un correo de confirmación con su rol.
 */
 async function activarUsuario(usuario) {
  if (!usuario.rolSeleccionado) {
    alert("Seleccione un rol para activar el usuario.");
    return;
  }
  try {
    // Obtener el nombre del rol seleccionado
    const rolAsignado = roles.value.find(rol => rol.id === usuario.rolSeleccionado);

    if (!rolAsignado) {
      alert("Error: No se encontró el rol seleccionado.");
      return;
    }

    // Activar usuario en el backend y obtener el usuario actualizado
    const response = await axios.put(`/usuarios/${usuario.id}/activar`, {
      rolId: usuario.rolSeleccionado
    });

    if (response.data) {
      // 🔹 Actualizar el usuario en la UI con los datos devueltos por el backend
      usuario.estado = 1;
      usuario.rol = rolAsignado; // Asignar el rol correctamente en el frontend

      // Enviar correo de activación
      await sendActivationEmail(usuario.correo, usuario.nombreUsuario, rolAsignado.roleName);

      alert("Usuario activado correctamente.");
    } else {
      alert("Error: No se pudo activar el usuario.");
    }
  } catch (error) {
    console.error("Error al activar usuario:", error);
    alert("Ocurrió un error al activar el usuario.");
  }
}


/**
 * Desactiva un usuario cambiando su estado a inactivo.
 */
 async function desactivarUsuario(usuario) {
  try {
    await axios.put(`/usuarios/${usuario.id}/desactivar`);
    usuario.estado = 0; // Marcar como inactivo
    usuario.rol = null; // 🔹 Limpiar el rol para que se pueda volver a asignar

    alert("Usuario desactivado correctamente.");
  } catch (error) {
    console.error("Error al desactivar usuario:", error);
    alert("Ocurrió un error al desactivar el usuario.");
  }
}


// Cargar datos cuando el componente se monta
onMounted(() => {
  fetchUsuarios();
  fetchRoles();
});
</script>

<style scoped>
.admin-usuarios {
  padding: 20px;
  background: #f9f9f9;
  color: #e0e1dd;
  border-radius: 10px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
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
  margin-top: 20px;
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

td select {
  width: 100%;
  padding: 5px;
  background: #222;
  color: white;
  border: 1px solid #444;
  border-radius: 5px;
  text-align: center;
}

td button {
  padding: 8px 15px;
  background-color: #76f59c;
  border: none;
  color: white;
  cursor: pointer;
  border-radius: 5px;
  width: 100%; /* Hacer que el botón ocupe el ancho completo de la celda */
}

button.desactivar {
  background-color: #fa6757;
}

button:hover {
  opacity: 0.8;
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

/* Estilos para Estado */
.activo {
  color: #DAFDBA;
  font-weight: bold;
}

.inactivo {
  color: #ff8a7d;
  font-weight: bold;
}

/* Ajustar el ancho de las columnas */
th:nth-child(1), td:nth-child(1) { width: 5%; } /* ID */
th:nth-child(2), td:nth-child(2) { width: 20%; } /* Nombre */
th:nth-child(3), td:nth-child(3) { width: 25%; } /* Correo */
th:nth-child(4), td:nth-child(4) { width: 10%; } /* Estado */
th:nth-child(5), td:nth-child(5) { width: 20%; } /* Rol */
th:nth-child(6), td:nth-child(6) { width: 10%; } /* Acciones */

</style>
