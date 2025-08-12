<template>
  <div class="container">
    <h2>Solicitar Convenio con Aseguradora</h2>

    <form @submit.prevent="enviar">
      <div class="mb-3">
        <label>Nombre del hospital</label>
        <input v-model="form.nombre" class="form-control" required />
      </div>

      <div class="mb-3">
        <label>Dirección</label>
        <input v-model="form.direccion" class="form-control" required />
      </div>

      <div class="mb-3">
        <label>Teléfono</label>
        <input v-model="form.telefono" class="form-control" required />
      </div>

      <div class="mb-3">
        <label>Aseguradora (Seguro)</label>
        <select v-model="form.aseguradora" class="form-select" required>
          <option disabled value="">Seleccione un seguro</option>
          <option v-for="aseg in seguros" :key="aseg.nombre" :value="aseg">
            {{ aseg.nombre }}
          </option>
        </select>
      </div>

      <button class="btn btn-primary" type="submit">Enviar Solicitud</button>
    </form>

    <div v-if="mensaje" class="alert alert-info mt-3">{{ mensaje }}</div>

    <div v-if="estadoSolicitud" class="card mt-4 p-3">
      <h5>Estado de la solicitud enviada</h5>
      <p><strong>Aseguradora:</strong> {{ estadoSolicitud.aseguradora }}</p>
      <p><strong>Estado:</strong>
        <span :class="estadoSolicitud.estado === 'aprobado' ? 'text-success' : estadoSolicitud.estado === 'rechazado' ? 'text-danger' : 'text-warning'">
          {{ estadoSolicitud.estado }}
        </span>
      </p>
    </div>

    <hr class="my-4" />
    <h4>Historial de Solicitudes</h4>
    <table class="table table-dark table-bordered mt-3">
      <thead>
        <tr>
          <th>#</th>
          <th>Aseguradora</th>
          <th>Estado</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(solicitud, index) in historialSolicitudes" :key="solicitud._id || index">
          <td>{{ index + 1 }}</td>
          <td>{{ solicitud.aseguradora }}</td>
          <td>
            <span :class="{
              'text-success': solicitud.estado === 'aprobado',
              'text-danger': solicitud.estado === 'rechazado',
              'text-warning': solicitud.estado === 'pendiente'
            }">
              {{ solicitud.estado }}
            </span>
          </td>
        </tr>
      </tbody>
    </table>

    <hr class="my-5" />
    <h4>Registrar nueva Aseguradora</h4>
    <form @submit.prevent="registrarAseguradora">
      <div class="mb-3">
        <label>Nombre de la Aseguradora</label>
        <input v-model="nuevaAseguradora.nombre" class="form-control" required />
      </div>

      <div class="mb-3">
        <label>URL base de conexión</label>
        <input v-model="nuevaAseguradora.url" class="form-control" required />
      </div>

      <button class="btn btn-success">Registrar Aseguradora</button>
    </form>

    <div v-if="mensajeRegistro" class="alert alert-info mt-3">{{ mensajeRegistro }}</div>

    <hr class="my-5" />
    <h4>Administrar Aseguradoras Registradas</h4>
    <table class="table table-striped mt-3">
      <thead>
        <tr>
          <th>#</th>
          <th>Nombre</th>
          <th>URL Base</th>
          <th>Acciones</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(aseg, index) in seguros" :key="index">
          <td>{{ index + 1 }}</td>
          <td><input v-model="aseg.nombre" class="form-control" /></td>
          <td><input v-model="aseg.url" class="form-control" /></td>
          <td>
            <button class="btn btn-sm btn-primary me-2" @click="editarAseguradora(index)">Guardar</button>
            <button class="btn btn-sm btn-danger" @click="eliminarAseguradora(index)">Eliminar</button>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";
import API_URL from "@/config";

interface Aseguradora {
  id?: number;
  nombre: string;
  url: string;
}

const form = ref({ nombre: "", direccion: "", telefono: "", aseguradora: null as Aseguradora | null });
const mensaje = ref("");
const estadoSolicitud = ref<unknown>(null);
const seguros = ref<Aseguradora[]>([]);
const historialSolicitudes = ref<unknown[]>([]);
const nuevaAseguradora = ref({ nombre: "", url: "" });
const mensajeRegistro = ref("");

const editarAseguradora = async (index: number) => {
  const aseg = seguros.value[index];
  try {
    const res = await fetch(`${API_URL}/api/conexiones-aseguradoras/${aseg.id}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ nombre: aseg.nombre, url: aseg.url })
    });
    mensajeRegistro.value = res.ok ? "Aseguradora actualizada correctamente." : "Error al actualizar aseguradora.";
  } catch (err) {
    console.error(err);
    mensajeRegistro.value = "Error de conexión al actualizar.";
  }
};

const eliminarAseguradora = async (index: number) => {
  const aseg = seguros.value[index];
  try {
    const res = await fetch(`${API_URL}/api/conexiones-aseguradoras/${aseg.id}`, {
      method: "DELETE"
    });
    if (res.ok) {
      seguros.value.splice(index, 1);
      mensajeRegistro.value = "Aseguradora eliminada.";
    } else {
      mensajeRegistro.value = "Error al eliminar aseguradora.";
    }
  } catch (err) {
    console.error(err);
    mensajeRegistro.value = "Error de conexión al eliminar.";
  }
};

const registrarAseguradora = async () => {
  try {
    const res = await fetch(`${API_URL}/api/conexiones-aseguradoras/registrar`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(nuevaAseguradora.value)
    });
    if (res.ok) {
      mensajeRegistro.value = "Aseguradora registrada con éxito.";
      nuevaAseguradora.value = { nombre: "", url: "" };
      await cargarSeguros();
      await cargarHistorial();
    } else if (res.status === 409) {
      mensajeRegistro.value = "La aseguradora ya existe.";
    } else {
      mensajeRegistro.value = "Error al registrar aseguradora.";
    }
  } catch (err) {
    console.error(err);
    mensajeRegistro.value = "Error de conexión.";
  }
};

const cargarSeguros = async () => {
  seguros.value = [];
  try {
    const res = await fetch(`${API_URL}/api/conexiones-aseguradoras`);
    if (!res.ok) throw new Error("Fallo al obtener aseguradoras");
    const aseguradoras = await res.json();
    seguros.value = aseguradoras.map((a: unknown) => ({ id: a.id, nombre: a.nombre, url: a.urlBase }));
  } catch (err) {
    console.error("Error al cargar aseguradoras:", err);
  }
};

const cargarHistorial = async () => {
  historialSolicitudes.value = [];
  try {
    const res = await fetch(`${API_URL}/api/conexiones-aseguradoras`);
    const aseguradoras = await res.json();
    const resultados: unknown[] = [];
    for (const a of aseguradoras) {
      try {
        const resHist = await fetch(`${a.urlBase}/solicitudes-atencion`);
        if (resHist.ok) {
          const data = await resHist.json();
          resultados.push(...data);
        }
      } catch {
        console.error("Error cargando historial desde:", a.urlBase);
      }
    }
    historialSolicitudes.value = resultados;
  } catch (err) {
    console.error("Error al cargar historial:", err);
  }
};

const enviar = async () => {
  const aseg = form.value.aseguradora as unknown;
  if (!aseg?.url) {
    mensaje.value = "No se encontró la URL de la aseguradora seleccionada.";
    return;
  }
  const yaAprobada = historialSolicitudes.value.some((s: unknown) => s.estado === "aprobado" && s.aseguradora === aseg.nombre);
  if (yaAprobada) {
    mensaje.value = "Ya existe una solicitud aprobada.";
    return;
  }
  const urlDestino = `${aseg.url}/solicitudes/hospital`;
  try {
    const res = await fetch(urlDestino, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        nombre: form.value.nombre,
        direccion: form.value.direccion,
        telefono: form.value.telefono,
        aseguradora: aseg.nombre,
        estado: "pendiente",
        origen: "hospital"
      })
    });
    if (res.ok) {
      const data = await res.json();
      mensaje.value = "Solicitud enviada a la aseguradora.";
      estadoSolicitud.value = data;
      form.value = { nombre: "", direccion: "", telefono: "", aseguradora: null as unknown };
      await cargarHistorial();
    } else {
      mensaje.value = "Error al enviar solicitud.";
    }
  } catch (err) {
    console.error(err);
    mensaje.value = "Error de conexión.";
  }
};

onMounted(() => {
  cargarSeguros();
  cargarHistorial();
});
</script>
