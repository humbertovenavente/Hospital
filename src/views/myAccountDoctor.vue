<script setup>
import { ref, onMounted } from "vue";
import axios from "axios";
import API_URL from "../config"; // Importamos API_URL desde config.ts

const userId = localStorage.getItem("userId");
const user = ref(null);
const doctor = ref(null);
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

// Cargar datos del usuario y del doctor
onMounted(async () => {
  if (!userId) {
    errorMensaje.value = "Usuario no encontrado. Intenta iniciar sesión nuevamente.";
    loading.value = false;
    return;
  }

  try {
    const userResponse = await axios.get(`${API_URL}/usuarios/${userId}`);
    const doctorResponse = await axios.get(`${API_URL}/doctores/${userId}`);

    if (userResponse.data && doctorResponse.data) {
      user.value = userResponse.data;
      doctor.value = doctorResponse.data;
    } else {
      errorMensaje.value = "No se encontraron datos del usuario o doctor.";
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
  if (!user.value || !doctor.value) return;

  try {
    await axios.put(`${API_URL}/usuarios/${userId}`, {
      nombreUsuario: user.value.nombreUsuario,
      correo: user.value.correo,
      contrasena: user.value.contrasena,
    });

    await axios.put(`${API_URL}/doctores/${userId}`, {
      apellido: doctor.value.apellido,
      documento: doctor.value.documento,
      fechaNacimiento: doctor.value.fechaNacimiento,
      genero: doctor.value.genero,
      telefono: doctor.value.telefono,
      especialidad: doctor.value.especialidad,
      numeroColegiado: doctor.value.numeroColegiado,
      horarioAtencion: doctor.value.horarioAtencion,
      fechaGraduacion: doctor.value.fechaGraduacion,
      universidadGraduacion: doctor.value.universidadGraduacion,
    });

    alert("Perfil actualizado correctamente.");
    isEditing.value = false; // Salir del modo edición
  } catch (error) {
    console.error("Error actualizando perfil:", error);
    alert("Hubo un error al actualizar el perfil.");
  }
};

// Exportar datos del usuario y del doctor en JSON
const exportData = () => {
  if (!user.value || !doctor.value) return;
  const jsonData = JSON.stringify({ usuario: user.value, doctor: doctor.value }, null, 2);
  const blob = new Blob([jsonData], { type: "application/json" });
  const link = document.createElement("a");
  link.href = URL.createObjectURL(blob);
  link.download = `DoctorData_${userId}.json`;
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
  if (!importedData.value || !importedData.value.usuario || !importedData.value.doctor) {
    alert("No hay datos importados válidos para aplicar.");
    return;
  }

  try {
    await axios.put(`${API_URL}/usuarios/${userId}`, importedData.value.usuario);
    await axios.put(`${API_URL}/doctores/${userId}`, importedData.value.doctor);
    alert("Datos importados correctamente.");
    user.value = importedData.value.usuario;
    doctor.value = importedData.value.doctor;
    importedData.value = null; // Limpiar los datos importados
  } catch (error) {
    console.error("Error aplicando datos importados:", error);
    alert("Hubo un error al importar los datos.");
  }
};
</script>

<template>
  <div class="account-container">
    <h2>Mi Cuenta (Doctor)</h2>

    <p v-if="loading">Cargando datos...</p>
    <p v-if="errorMensaje" class="error">{{ errorMensaje }}</p>

    <form v-if="user && doctor" @submit.prevent="updateProfile">
      <label><b>Nombre:</b></label>
      <input v-model="user.nombreUsuario" :disabled="!isEditing" required />

      <label><b>Correo:</b></label>
      <input v-model="user.correo" :disabled="!isEditing" required />

      <label><b>Rol:</b></label>
      <input :value="rolesMap[user.rolId]" disabled class="readonly-field" />

      <label><b>Especialidad:</b></label>
      <input v-model="doctor.especialidad" :disabled="!isEditing" required />

      <label><b>Número de Colegiado:</b></label>
      <input v-model="doctor.numeroColegiado" :disabled="!isEditing" required />

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
  max-width: 500px;
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

input[type="file"] {
  margin-top: 10px;
}

.imported-data {
  background: #f4f4f4;
  padding: 10px;
  margin-top: 15px;
  border-radius: 5px;
  font-size: 14px;
  max-height: 200px;
  overflow: auto;
}
</style>
