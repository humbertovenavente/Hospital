<script setup>
import { ref, onMounted } from "vue";
import axios from "axios";
import API_URL from "../config"; // Importamos API_URL desde config.ts

const userId = localStorage.getItem("userId");
const user = ref(null);
const paciente = ref(null);
const loading = ref(true);
const errorMensaje = ref("");
const isEditing = ref(false);
const importedData = ref(null); // Datos importados

// Mapeo de roles por ID
const rolesMap = {
  1: "Administrador",
  2: "Doctor",
  3: "Empleado",
  4: "Paciente",
  5: "Usuario Interconexión",
};

// Mapeo de estados
const estadoMap = {
  1: "Activo",
  0: "Inactivo",
};

// Cargar datos del usuario y paciente
onMounted(async () => {
  if (!userId) {
    errorMensaje.value = "Usuario no encontrado. Intenta iniciar sesión nuevamente.";
    loading.value = false;
    return;
  }

  try {
    const userResponse = await axios.get(`${API_URL}/usuarios/${userId}`);
    const pacienteResponse = await axios.get(`${API_URL}/pacientes/${userId}`);

    if (userResponse.data && pacienteResponse.data) {
      user.value = userResponse.data;
      paciente.value = pacienteResponse.data;
    } else {
      errorMensaje.value = "No se encontraron datos del usuario.";
    }
  } catch (error) {
    console.error("Error cargando datos:", error);
    errorMensaje.value = "Error al cargar el perfil. Inténtalo más tarde.";
  } finally {
    loading.value = false;
  }
});

// Alternar modo edición
const toggleEdit = () => {
  isEditing.value = !isEditing.value;
};

// Actualizar perfil
const updateProfile = async () => {
  if (!user.value || !paciente.value) return;

  try {
    await axios.put(`${API_URL}/usuarios/${userId}`, {
      nombreUsuario: user.value.nombreUsuario,
      correo: user.value.correo,
      contrasena: user.value.contrasena,
    });

    await axios.put(`${API_URL}/pacientes/${userId}`, {
      apellido: paciente.value.apellido,
      documento: paciente.value.documento,
      fechaNacimiento: paciente.value.fechaNacimiento,
      genero: paciente.value.genero,
      telefono: paciente.value.telefono,
    });

    alert("Perfil actualizado correctamente.");
    isEditing.value = false; // Salir del modo edición
  } catch (error) {
    console.error("Error actualizando perfil:", error);
    alert("Hubo un error al actualizar el perfil.");
  }
};

// Exportar datos del usuario y del paciente en JSON
const exportData = () => {
  if (!user.value || !paciente.value) return;
  const jsonData = JSON.stringify({ usuario: user.value, paciente: paciente.value }, null, 2);
  const blob = new Blob([jsonData], { type: "application/json" });
  const link = document.createElement("a");
  link.href = URL.createObjectURL(blob);
  link.download = `PacienteData_${userId}.json`;
  link.click();
};

// Importar datos desde un archivo JSON
const importData = (event) => {
  const file = event.target.files[0];
  if (!file) return;
  const reader = new FileReader();
  reader.onload = (e) => {
    importedData.value = JSON.parse(e.target.result);
  };
  reader.readAsText(file);
};

// Confirmar y aplicar datos importados
const applyImportedData = async () => {
  if (!importedData.value || !importedData.value.usuario || !importedData.value.paciente) {
    alert("No hay datos importados válidos para aplicar.");
    return;
  }

  try {
    await axios.put(`${API_URL}/usuarios/${userId}`, importedData.value.usuario);
    await axios.put(`${API_URL}/pacientes/${userId}`, importedData.value.paciente);
    alert("Datos importados correctamente.");
    user.value = importedData.value.usuario;
    paciente.value = importedData.value.paciente;
    importedData.value = null; // Limpiar los datos importados
  } catch (error) {
    console.error("Error aplicando datos importados:", error);
    alert("Hubo un error al importar los datos.");
  }
};
</script>

<template>
  <div class="account-container">
    <h2>Mi Cuenta (Paciente)</h2>

    <p v-if="loading">Cargando datos...</p>
    <p v-if="errorMensaje" class="error">{{ errorMensaje }}</p>

    <form v-if="user && paciente" @submit.prevent="updateProfile">
      <label><b>Nombre:</b></label>
      <input v-model="user.nombreUsuario" :disabled="!isEditing" required />

      <label><b>Correo:</b></label>
      <input v-model="user.correo" :disabled="!isEditing" required />

      <label><b>Contraseña:</b></label>
      <input type="password" v-model="user.contrasena" :disabled="!isEditing" required />

      <label><b>Rol:</b></label>
      <input :value="rolesMap[user.rolId]" disabled class="readonly-field" />

      <label><b>Estado:</b></label>
      <input :value="estadoMap[user.estado]" disabled class="readonly-field" />

      <label><b>Fecha de Creación:</b></label>
      <input v-model="user.fechaCreacion" disabled class="readonly-field" />

      <label><b>ID Hospital:</b></label>
      <input v-model="user.idHospital" disabled class="readonly-field" />

      <hr />

      <label><b>Apellido:</b></label>
      <input v-model="paciente.apellido" :disabled="!isEditing" required />

      <label><b>Documento:</b></label>
      <input v-model="paciente.documento" :disabled="!isEditing" required />

      <label><b>Fecha de Nacimiento:</b></label>
      <input type="date" v-model="paciente.fechaNacimiento" :disabled="!isEditing" required />

      <label><b>Género:</b></label>
      <input v-model="paciente.genero" :disabled="!isEditing" required />

      <label><b>Teléfono:</b></label>
      <input v-model="paciente.telefono" :disabled="!isEditing" required />

      <button v-if="isEditing" type="submit">Guardar Cambios</button>
      <button v-else type="button" @click="toggleEdit">Editar</button>
    </form>

    <div class="export-import">
      <button @click="exportData">Exportar Datos (JSON)</button>
      <input type="file" @change="importData" accept="application/json"/>
    </div>

    <div v-if="importedData" class="imported-data">
      <h3>Datos Importados:</h3>
      <pre>{{ importedData }}</pre>
      <button @click="applyImportedData">Guardar Datos Importados</button>
    </div>
  </div>
</template>

<style scoped>
.account-container {
  max-width: 400px;
  margin: auto;
  padding: 20px;
  background: white;
  border-radius: 8px;
  box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
}

input {
  width: 100%;
  padding: 10px;
  margin: 5px 0;
  border: 1px solid #ccc;
  border-radius: 5px;
}

.readonly-field {
  background-color: #e0e0e0;
  color: #555;
}

label {
  display: block;
  margin-top: 10px;
  font-size: 14px;
  color: #333;
  font-weight: bold;
}

button {
  width: 100%;
  padding: 10px;
  background-color: #007BFF;
  color: white;
  border: none;
  cursor: pointer;
  border-radius: 5px;
  margin-top: 10px;
}

button:hover {
  background-color: #0056b3;
}

.error {
  color: red;
  font-weight: bold;
  margin-top: 10px;
}

.export-import {
  margin-top: 20px;
  text-align: center;
}

.imported-data {
  background: #f4f4f4;
  padding: 10px;
  margin-top: 15px;
  border-radius: 5px;
  max-height: 200px;
  overflow: auto;
}
</style>
