<script setup>
import { ref, onMounted } from "vue";
import axios from "axios";
import API_URL from "../config"; // Importamos API_URL desde config.ts

const userId = localStorage.getItem("userId");
const user = ref(null);
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

// Cargar datos del usuario
onMounted(async () => {
  if (!userId) {
    errorMensaje.value = "Usuario no encontrado. Intenta iniciar sesión nuevamente.";
    loading.value = false;
    return;
  }

  try {
    const response = await axios.get(`${API_URL}/usuarios/${userId}`);
    if (response.data) {
      user.value = response.data;
    } else {
      errorMensaje.value = "No se encontraron datos del usuario.";
    }
  } catch (error) {
    console.error("Error cargando datos del usuario:", error);
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
  if (!user.value) return;

  try {
    await axios.put(`${API_URL}/usuarios/${userId}`, {
      nombreUsuario: user.value.nombreUsuario,
      correo: user.value.correo,
      contrasena: user.value.contrasena,
    });

    alert("Perfil actualizado correctamente.");
    isEditing.value = false; // Salir del modo edición
  } catch (error) {
    console.error("Error actualizando perfil:", error);
    alert("Hubo un error al actualizar el perfil.");
  }
};

// Exportar datos del usuario en JSON
const exportData = () => {
  if (!user.value) return;
  const jsonData = JSON.stringify(user.value, null, 2);
  const blob = new Blob([jsonData], { type: "application/json" });
  const link = document.createElement("a");
  link.href = URL.createObjectURL(blob);
  link.download = `AdminData_${userId}.json`;
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
  if (!importedData.value) {
    alert("No hay datos importados para aplicar.");
    return;
  }

  try {
    await axios.put(`${API_URL}/usuarios/${userId}`, importedData.value);
    alert("Datos importados correctamente.");
    user.value = importedData.value; // Actualizar el usuario con los datos importados
    importedData.value = null; // Limpiar los datos importados después de la actualización
  } catch (error) {
    console.error("Error aplicando datos importados:", error);
    alert("Hubo un error al importar los datos.");
  }
};
</script>

<template>
  <div class="account-container">
    <div class="header">Mi Cuenta (Administrador)</div>

    <!--  Mostrar mensaje de carga -->
    <p v-if="loading">Cargando datos...</p>

    <!--  Mostrar mensaje de error si hay problemas -->
    <p v-if="errorMensaje" class="error">{{ errorMensaje }}</p>

    <!--  Mostrar el formulario si los datos están disponibles -->
    <form v-if="user" @submit.prevent="updateProfile">
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

      <button v-if="isEditing" type="submit">Guardar Cambios</button>
      <button v-else type="button" @click="toggleEdit">Editar</button>
    </form>

    <!-- Botones de Exportación e Importación -->
    <div class="export-import">
      <button @click="exportData">Exportar Datos (JSON)</button>
      <input type="file" @change="importData" accept="application/json"/>
    </div>

    <!-- Vista previa de los datos importados -->
    <div v-if="importedData" class="imported-data">
      <h3>Datos Importados:</h3>
      <pre>{{ importedData }}</pre>
      <button @click="applyImportedData">Guardar Datos Importados</button>
    </div>
  </div>
</template>

<style scoped>
/*  Estilos del contenedor */
.account-container {
  max-width: 400px;
  margin: auto;
  padding: 20px;
  background: white;
  border-radius: 8px;
  box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
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
/*  Estilo de los inputs */
input {
  width: 100%;
  padding: 10px;
  margin: 5px 0;
  border: 1px solid #ccc;
  border-radius: 5px;
}

/*  Estilo para campos no editables */
.readonly-field {
  background-color: #e0e0e0;
  color: #555;
}

/* Estilo de etiquetas */
label {
  display: block;
  margin-top: 10px;
  font-size: 14px;
  color: #333;
  font-weight: bold;
}

/* Botones */
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

/* Mensaje de error */
.error {
  color: red;
  font-weight: bold;
  margin-top: 10px;
}

/* Contenedor de Exportación e Importación */
.export-import {
  margin-top: 20px;
  text-align: center;
}

input[type="file"] {
  margin-top: 10px;
}

/* Vista previa de datos importados */
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
