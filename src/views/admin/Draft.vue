<template>
  <div class="draft-editor">
    <h2>✍️ Corregir contenido rechazado</h2>

    <div v-if="loading">Cargando contenido...</div>

    <div v-else>
      <p><strong>Página:</strong> {{ draftContent.pageName || draftFaq.pregunta || draftHistoria.nombreEntidad }}</p>
      <p><strong>Motivo del rechazo:</strong> {{ draftContent.rejectionReason || draftFaq.rejectionReason || draftHistoria.rejectionReason || "No especificado" }}</p>

      <!-- 🔹 PageContent -->
      <div v-if="draftContent.idContent" class="form-group">
        <label>Título:</label>
        <input v-model="draftContent.contentTitle" class="form-input" />

        <label>Sección:</label>
        <input v-model="draftContent.sectionName" class="form-input" />

        <label>Contenido (HTML):</label>
        <textarea v-model="draftContent.contentBody" rows="8" class="form-textarea"></textarea>
      </div>

      <!-- 🔹 FAQ -->
      <div v-else-if="draftFaq.id" class="form-group">
        <label>Pregunta:</label>
        <input v-model="draftFaq.pregunta" class="form-input" />

        <label>Respuesta:</label>
        <textarea v-model="draftFaq.respuesta" rows="5" class="form-textarea"></textarea>
      </div>

      <!-- 🔹 Historia -->
      <div v-else-if="draftHistoria.id" class="form-group">
        <label>Entidad:</label>
        <input v-model="draftHistoria.nombreEntidad" class="form-input" />

        <label>Historia:</label>
        <textarea v-model="draftHistoria.historia" rows="6" class="form-textarea"></textarea>

        <label>Méritos (JSON):</label>
        <textarea v-model="draftHistoria.meritos" rows="3" class="form-textarea"></textarea>

        <label>Línea del Tiempo (JSON):</label>
        <textarea v-model="draftHistoria.lineaDelTiempo" rows="3" class="form-textarea"></textarea>
      </div>

      <button class="btn-success" @click="reenviarPropuesta">📤 Reenviar propuesta</button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { getContentById, updateContent } from '@/services/pageContentService';
import { getHistoriaById, actualizarHistoria } from '@/services/historiaService';
import { obtenerPreguntas, editarPregunta } from '@/services/faqService';

const route = useRoute();
const router = useRouter();

const getEditorEmail = () => {
  return localStorage.getItem("usuarioEmail") || route.query.email || null;
};


const draftContent = ref({});
const draftFaq = ref({});
const draftHistoria = ref({});
const loading = ref(true);

onMounted(async () => {
  const id = route.params.id;
  try {
    try {
      const data = await getContentById(id);
      draftContent.value = data;
      return;
    } catch {}

    try {
      const historia = await getHistoriaById(id);
      draftHistoria.value = historia;
      return;
    } catch {}

    try {
      const preguntas = await obtenerPreguntas();
      const match = preguntas.find(p => p.id === Number(id));
      if (match) draftFaq.value = match;
    } catch {}
  } catch (error) {
    console.error("❌ Error al cargar contenido draft:", error);
    alert("No se pudo cargar el contenido.");
  } finally {
    loading.value = false;
  }
});

const reenviarPropuesta = async () => {
  try {
    const email = getEditorEmail();



    if (draftContent.value.idContent) {
      draftContent.value.status = "PROCESO";
      draftContent.value.rejectionReason = null;
      draftContent.value.editorEmail = email;
      await updateContent(draftContent.value.idContent, draftContent.value);
    } else if (draftFaq.value.id) {
      draftFaq.value.status = "PROCESO";
      draftFaq.value.rejectionReason = null;
      draftFaq.value.editadoPor = email;
      await editarPregunta(draftFaq.value.id, draftFaq.value.pregunta, draftFaq.value.respuesta, "PROCESO", null, email);
    } else if (draftHistoria.value.id) {
      draftHistoria.value.status = "PROCESO";
      draftHistoria.value.rejectionReason = null;
      draftHistoria.value.editorEmail = email;
      await actualizarHistoria(draftHistoria.value.id, draftHistoria.value, email);
;
    }

    alert("✅ Propuesta reenviada correctamente");
    router.push("/");
  } catch (err) {
    console.error("❌ Error al reenviar propuesta:", err);
    alert("No se pudo reenviar la propuesta.");
  }
};
</script>

<style scoped>
.draft-editor {
  max-width: 800px;
  margin: 0 auto;
  background: #f9f9f9;
  padding: 2rem;
  border-radius: 8px;
  color: #102538;
}

.form-group {
  margin-bottom: 1rem;
}

.form-input,
.form-textarea {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid #ccc;
  border-radius: 6px;
  font-size: 1rem;
}

textarea.form-textarea {
  font-family: monospace;
  resize: vertical;
}

.btn-success {
  background-color: #00bb77;
  color: white;
  padding: 0.6rem 1.2rem;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  margin-top: 1rem;
}
</style>
