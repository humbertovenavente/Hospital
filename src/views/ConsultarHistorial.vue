<template>
  <div class="container mt-4 text-white-custom">
    <h2>Consultar Historial del Paciente</h2>

    <div class="mb-3">
      <label>Buscar por DPI del paciente:</label>
      <input v-model="dpi" class="form-control" placeholder="Ingrese DPI" />
    </div>

    <div class="mb-3">
      <label>Aseguradora:</label>
      <select v-model="aseguradoraSeleccionadaUrl" class="form-select">
        <option disabled value="">Seleccione aseguradora</option>
        <option v-for="aseg in aseguradoras" :key="aseg.urlBase" :value="aseg.urlBase">
          {{ aseg.nombre }}
        </option>
      </select>
    </div>

    <button @click="buscarHistorial" class="btn">Buscar Cliente</button>

    <div v-if="cliente" class="mt-4">
      <h5>Cliente encontrado: {{ cliente.nombre }} {{ cliente.apellido }}</h5>
      <p><strong>Afiliación:</strong> {{ cliente.numeroAfiliacion }}</p>
      <p><strong>Póliza:</strong> {{ cliente.polizaNombre }}</p>

      <table class="tabla-historial mt-3">
        <thead>
          <tr>
            <th>HOSPITAL</th>
            <th>SERVICIO</th>
            <th>FECHA</th>
            <th>COSTO (HOSPITAL)</th>
            <th>COSTO (ASEGURADORA)</th>
            <th>COPAGO</th>
            <th>ESTADO</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in historial" :key="item._id">
            <td>{{ item.hospital?.nombre || "Desconocido" }}</td>
            <td>{{ item.servicio?.nombre || "Desconocido" }}</td>
            <td>{{ new Date(item.fechaServicio).toLocaleDateString() }}</td>
            <td>Q{{ item.costo }}</td>
            <td>Q{{ item.servicio?.precioAseguradora || "N/A" }}</td>
            <td>Q{{ item.copago }}</td>
            <td>
              <span :class="item.estadoCopago === 'pagado' ? 'estado-pagado' : 'estado-pendiente'">
                {{ item.estadoCopago }}
              </span>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <div v-else-if="buscado">
      <p class="text-danger">Cliente no encontrado.</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";
import API_URL from "@/config";

const dpi = ref("");
const aseguradoras = ref([]);
const aseguradoraSeleccionadaUrl = ref("");
const cliente = ref(null);
const historial = ref([]);
const buscado = ref(false);

const buscarHistorial = async () => {
  cliente.value = null;
  historial.value = [];
  buscado.value = false;

  if (!dpi.value || !aseguradoraSeleccionadaUrl.value) return;

  try {
    const res = await fetch(`${aseguradoraSeleccionadaUrl.value}/clientes/buscar-por-documento/${dpi.value}`);
    if (!res.ok) throw new Error("No encontrado");

    const fetchedCliente = await res.json();
    cliente.value = fetchedCliente;
    historial.value = fetchedCliente.historialServicios || [];
    buscado.value = true;
  } catch (error) {
    console.error("Error en la búsqueda:", error.message);
    cliente.value = null;
    historial.value = [];
    buscado.value = true;
  }
};

const cargarAseguradoras = async () => {
  try {
    const res = await fetch(`${API_URL}/api/conexiones-aseguradoras`);
    if (res.ok) {
      const data = await res.json();
      aseguradoras.value = data.map(a => ({
        nombre: a.nombre,
        urlBase: a.urlBase
      }));
    }
  } catch (err) {
    console.error("Error al cargar aseguradoras:", err);
  }
};

onMounted(() => {
  cargarAseguradoras();
});
</script>

<style scoped>
.text-white-custom {
  color: white;
}
.table,
.tabla-historial {
  width: 100%;
  border-collapse: collapse;
}
.tabla-historial th,
.tabla-historial td {
  padding: 8px;
  border: 1px solid #ccc;
  color: white;
}
.tabla-historial thead {
  background-color: #444;
}
.tabla-historial tbody tr:nth-child(even) {
  background-color: #222;
}
.tabla-historial tbody tr:nth-child(odd) {
  background-color: #111;
}
.estado-pagado {
  color: #4caf50;
  font-weight: bold;
}
.estado-pendiente {
  color: #f44336;
  font-weight: bold;
}
.btn {
  padding: 8px 16px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
</style>
