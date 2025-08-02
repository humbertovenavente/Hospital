<template>
  <div class="admin-servicios">
    <h2 class="text-2xl font-bold mb-4">Administración de Servicios</h2>

    <!-- Formulario de Servicios -->
    <form @submit.prevent="registrarServicio" class="form">
      <fieldset>
        <legend class="text-lg font-semibold">Crear Nuevo Servicio</legend>

        <label for="nombre">Nombre del Servicio:</label>
        <input v-model="form.nombre" id="nombre" type="text" class="input" required />

        <label for="costo">Costo del Servicio:</label>
        <input v-model.number="form.costo" id="costo" type="number" class="input" step="0.01" required />

        <label class="checkbox-container">
          <input v-model="form.cubiertoSeguro" type="checkbox" />
          <span>¿Cubierto por Seguro?</span>
        </label>

        <label for="parentId">Servicio Padre (Opcional):</label>
        <select v-model="form.parentId" id="parentId" class="input">
          <option :value="null">Ninguno (Categoría principal)</option>
          <option v-for="s in servicios" :value="s.id" :key="s.id">{{ s.nombre }}</option>
        </select>

        <button type="submit" class="btn btn-primary w-full" :disabled="loading">
          {{ loading ? "Agregando..." : "Agregar Servicio" }}
        </button>
      </fieldset>
    </form>

    <!-- Lista de Servicios -->
    <ul v-if="servicios.length" class="servicios-list">
      <li v-for="servicio in servicios" :key="servicio.id" class="card">
        <div class="card-header">
          <h3>{{ servicio.nombre }}</h3>
          <span class="precio">${{ servicio.costo.toFixed(2) }}</span>
        </div>

        <div class="card-body">
          <p>
            <strong>Cubierto Seguro:</strong>
            <span :class="{'text-green': servicio.cubiertoSeguro, 'text-red': !servicio.cubiertoSeguro}">
              {{ servicio.cubiertoSeguro ? "Sí" : "No" }}
            </span>
          </p>
        </div>

        <div class="acciones">
          <button @click="removeServicio(servicio.id)" class="btn btn-danger">Eliminar</button>
        </div>

        <!-- Subservicios -->
        <div class="subservicio-container">
          <label for="subservicio">Añadir Subservicio:</label>
          <select v-model="subservicioSeleccionadoPorServicio[servicio.id]" class="input">
            <option :value="null">Selecciona un servicio</option>
            <option v-for="s in servicios" :value="s.id" :key="s.id">{{ s.nombre }}</option>
          </select>
          <button @click="agregarRelacion(servicio.id)" class="btn btn-secondary">Agregar</button>
        </div>

        <ul v-if="servicio.subServicios.length" class="subservicio-list">
          <li v-for="sub in servicio.subServicios" :key="sub.id" class="subservicio-item">
            <div class="subservicio-nombre">
              <span class="subservicio-icon">↳</span> {{ sub.nombre }}
            </div>
            <button @click="eliminarRelacion(servicio.id, sub.id)" class="btn btn-danger btn-small">Eliminar</button>
          </li>
        </ul>
      </li>
    </ul>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from "vue";
import {
  getServicios,
  addServicio,
  deleteServicio,
  addSubServicio,
  deleteSubServicio
} from "../../services/servicioService";

const servicios = ref([]);
const form = ref({
  nombre: "",
  costo: "",
  cubiertoSeguro: false,
  parentId: null,
});
const loading = ref(false);
const subservicioSeleccionadoPorServicio = reactive({});

const cargarServicios = async () => {
  try {
    servicios.value = await getServicios();
  } catch (error) {
    console.error("Error al cargar los servicios:", error);
  }
};

onMounted(cargarServicios);

const registrarServicio = async () => {
  if (!form.value.nombre || !form.value.costo) {
    alert("Todos los campos son obligatorios.");
    return;
  }

  try {
    loading.value = true;
    await addServicio(form.value);
    form.value = { nombre: "", costo: "", cubiertoSeguro: false, parentId: null };
    await cargarServicios();
  } catch (error) {
    console.error("Error al agregar servicio:", error);
  } finally {
    loading.value = false;
  }
};

const removeServicio = async (id) => {
  try {
    await deleteServicio(id);
    await cargarServicios();
  } catch (error) {
    console.error("Error al eliminar servicio:", error);
  }
};

const agregarRelacion = async (id) => {
  const subServicioId = subservicioSeleccionadoPorServicio[id];

  if (!subServicioId) {
    alert("Selecciona un servicio para agregar como subservicio.");
    return;
  }

  // Buscar el objeto completo del subservicio en la lista de servicios
  const subServicio = servicios.value.find(s => s.id === subServicioId);
  if (!subServicio) {
    alert("Error: No se encontró el servicio seleccionado.");
    return;
  }

  try {
    // 🔥 Enviar el objeto completo con nombre, costo y cubiertoSeguro
    await addSubServicio(id, {
      id: subServicio.id,
      nombre: subServicio.nombre,
      costo: subServicio.costo,
      cubiertoSeguro: subServicio.cubiertoSeguro
    });

    subservicioSeleccionadoPorServicio[id] = null;
    await cargarServicios();
  } catch (error) {
    console.error("Error al agregar subservicio:", error);
  }
};


const eliminarRelacion = async (id, subServicioId) => {
  const subServicio = servicios.value.find(s => s.id === subServicioId);
  if (!subServicio) {
    alert("Error: No se encontró el subservicio seleccionado.");
    return;
  }

  try {
    await deleteSubServicio(id, subServicio.id);
    await cargarServicios();
  } catch (error) {
    console.error("Error al eliminar subservicio:", error);
  }
};

</script>

<style scoped>
.admin-servicios {
  max-width: 750px;
  margin: auto;
  padding: 20px;
  font-family: "Arial", sans-serif;
  color: #fff;
}

.form {
  background: #1e1e1e;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.15);
  margin-bottom: 20px;
  display: flex;
  flex-direction: column;
  gap: 10px;
}

fieldset {
  border: 1px solid #666;
  padding: 15px;
  border-radius: 8px;
}

legend {
  color: #fff;
  font-weight: bold;
  padding: 5px;
}

.input {
  padding: 12px;
  width: 100%;
  border: 1px solid #666;
  border-radius: 5px;
  font-size: 16px;
  background: #2a2a2a;
  color: #fff;
}

.card {
  background: #2a2a2a;
  padding: 20px;
  border-radius: 12px;
  box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.12);
  margin-bottom: 15px;
  border-left: 5px solid #007bff;
  transition: all 0.3s ease;
}

.card:hover {
  transform: translateY(-3px);
}

.subservicio-container {
  display: flex;
  gap: 10px;
  margin-top: 10px;
}

.subservicio-list {
  padding-left: 25px;
  margin-top: 8px;
  border-left: 3px solid #007bff;
}

.subservicio-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: #3a3a3a;
  padding: 10px;
  border-radius: 6px;
  margin-bottom: 5px;
  border-left: 4px solid #6c757d;
  padding-left: 10px;
}

.subservicio-icon {
  color: #007bff;
  margin-right: 5px;
}
</style>
