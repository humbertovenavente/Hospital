<template>
  <div class="admin-historia">
    <div class="header">Editar Historia de la Institución</div>

    <form @submit.prevent="guardarComoBorrador">
      <div class="form-group">
        <label for="nombreEntidad">Nombre de la Entidad:</label>
        <input type="text" id="nombreEntidad" v-model="historia.nombreEntidad" class="form-input" />
      </div>

      <div class="form-group">
        <label for="historia">Historia:</label>
        <textarea id="historia" v-model="historia.historia" class="form-textarea"></textarea>
      </div>

      <div class="form-group" v-if="historia.status === 'RECHAZADO'">
        <label>Motivo de Rechazo:</label>
        <textarea v-model="historia.rejectionReason" class="form-textarea" placeholder="Motivo del rechazo..."></textarea>
      </div>

      <hr class="divider" />

      <h2>Méritos</h2>
      <div v-for="(merito, index) in meritosData" :key="index" class="merito-item">
        <div class="merito-header">
          <input type="text" v-model="merito.title" class="form-input" placeholder="Título del mérito" />
          <button type="button" class="btn-delete" @click="removeMerito(index)">Eliminar</button>
        </div>
        <textarea v-model="merito.description" class="form-textarea" placeholder="Descripción del mérito"></textarea>
      </div>
      <button type="button" class="btn-add" @click="addMerito">Agregar mérito</button>

      <hr class="divider" />

      <h2>Línea del Tiempo</h2>
      <div v-for="(evento, index) in timelineEvents" :key="index" class="timeline-event">
        <div class="event-header">
          <input type="text" v-model="evento.year" class="form-input" placeholder="Año" />
          <button type="button" class="btn-delete" @click="removeEvent(index)">Eliminar</button>
        </div>
        <input type="text" v-model="evento.title" class="form-input" placeholder="Título del evento" />
        <textarea v-model="evento.description" class="form-textarea" placeholder="Descripción del evento"></textarea>
      </div>
      <button type="button" class="btn-add" @click="addEvent">Agregar evento</button>

      <hr class="divider" />

      <div class="button-row">
        <button type="submit" class="btn-save">Guardar como Borrador</button>
        <button type="button" class="btn-submit" @click="enviarARevision">Enviar a Moderación</button>
        <button type="button" class="btn-publish" @click="publicarDirectamente">Publicar</button>
      </div>
    </form>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import {
  crearHistoria,
  actualizarHistoria
} from "@/services/historiaService";

const historia = ref({
  id: null,
  nombreEntidad: "",
  historia: "",
  meritos: "",
  lineaDelTiempo: "",
  status: "PROCESO",
  rejectionReason: ""
});

const timelineEvents = ref([]);
const meritosData = ref([]);

const fetchHistoria = async () => {
  try {
    const res = await fetch(`${import.meta.env.VITE_API_URL || "/api"}/historias`);
    const data = await res.json();
    if (data && data.length > 0) {
      historia.value = data[0];
      timelineEvents.value = JSON.parse(historia.value.lineaDelTiempo || "[]");

      if (typeof historia.value.meritos === "string" && historia.value.meritos.trim().startsWith("[")) {
        meritosData.value = JSON.parse(historia.value.meritos);
      } else {
        meritosData.value = [];
      }
    }
  } catch (err) {
    console.error("❌ Error al obtener historia:", err);
  }
};

const saveHistoria = async () => {
  historia.value.lineaDelTiempo = JSON.stringify(timelineEvents.value);
  historia.value.meritos = JSON.stringify(meritosData.value);

  try {
    if (historia.value.id) {
      await actualizarHistoria(historia.value.id, historia.value);
    } else {
      const creada = await crearHistoria(historia.value);
      historia.value = creada;
    }
  } catch (err) {
    console.error("❌ Error al guardar historia:", err);
    alert("No se pudo guardar la historia.");
  }
};

const guardarComoBorrador = async () => {
  historia.value.status = "PROCESO";
  historia.value.editorEmail = localStorage.getItem("usuarioEmail");
  await saveHistoria();
  alert("✅ Historia guardada como borrador.");
};

const enviarARevision = async () => {
  historia.value.status = "PROCESO";
  historia.value.rejectionReason = null;
  historia.value.editorEmail = localStorage.getItem("usuarioEmail");
  await saveHistoria();
  alert("📤 Historia enviada a moderación.");
};

const publicarDirectamente = async () => {
  historia.value.status = "PUBLICADO";
  historia.value.rejectionReason = null;
  historia.value.editorEmail = localStorage.getItem("usuarioEmail");
  await saveHistoria();
  alert("✅ Historia publicada directamente.");
};

const addMerito = () => meritosData.value.push({ title: "", description: "" });
const removeMerito = (index) => meritosData.value.splice(index, 1);
const addEvent = () => timelineEvents.value.push({ year: "", title: "", description: "" });
const removeEvent = (index) => timelineEvents.value.splice(index, 1);

onMounted(fetchHistoria);
</script>



<style scoped>
.admin-historia {
  background: #f9f9f9;
  color: #e0e1dd;
  min-height: 100vh;
  padding: 2rem;
  border-radius: 10px;
  max-width: 900px;
  margin: 0 auto;
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
h2 {
  margin-top: 1.5rem;
  margin-bottom: 1rem;
  color: #b3f5e3;
}
.form-group {
  margin-bottom: 1rem;
  display: flex;
  flex-direction: column;
}
.form-group label {
  font-weight: bold;
  margin-bottom: 0.3rem;
  color: #f9f9f9;
}
.form-input,
.form-textarea {
  background-color: #13678a;
  color: #f9f9f9;
  border: 1px solid #45C4B0;
  padding: 0.6rem;
  border-radius: 4px;
  font-size: 1rem;
}
.form-textarea {
  resize: vertical;
}
.divider {
  border: 0;
  border-top: 1px solid #b3f5e3;
  margin: 2rem 0;
}
.merito-item {
  background-color: #0b1b2b;
  padding: 1rem;
  margin-bottom: 1rem;
  border: 1px solid #b3f5e3;
  border-radius: 4px;
}
.merito-header,
.event-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 0.5rem;
}
.btn-add,
.btn-delete,
.btn-save,
.btn-submit,
.btn-publish {
  cursor: pointer;
  border: none;
  padding: 0.6rem 1rem;
  border-radius: 4px;
  color: #fff;
  font-weight: bold;
}
.btn-add {
  background-color: #f0ad4e;
  margin-top: 1rem;
}
.btn-delete {
  background-color: #ff8a7d;
}
.btn-save {
  background-color: #6c757d;
  margin-top: 1rem;
}
.btn-submit {
  background-color: #007bff;
}
.btn-publish {
  background-color: #28a745;
}
.button-row {
  display: flex;
  flex-wrap: wrap;
  gap: 1rem;
  margin-top: 1rem;
}
</style>
