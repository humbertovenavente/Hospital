<template>
  <div class="admin-page-editor">
    <div class="header">Administrador de páginas informativas</div>

    <div class="section">
      <select v-model="selectedPage" @change="loadContent">
        <option disabled value="">Selecciona una página</option>
        <option v-for="page in pages" :key="page" :value="page">
          {{ page }}
        </option>
      </select>

      <div
        v-for="content in contents"
        :key="content.idContent || content.tempId"
        class="content-editor"
      >
        <input v-model="content.sectionName" placeholder="Nombre de sección" />
        <input v-model="content.contentTitle" placeholder="Título del contenido" />
        <textarea v-model="content.contentBody" placeholder="Contenido HTML"></textarea>

        <input type="file" @change="handleFileUpload($event, content)" accept="image/*" />

        <select v-model="content.status" disabled>
          <option value="PROCESO">Borrador</option>
          <option value="PUBLICADO">Publicado</option>
        </select>

        <div class="button-group">
          <button class="edit-button" @click="saveContent(content)">Guardar</button>
          <button class="moderate-button" @click="sendToModeration(content)">Enviar a Moderación</button>
        </div>
      </div>

      <button class="edit-button" @click="addContent">Añadir nueva sección</button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import {
  getPublishedContent,
  getDraftContent,
  updateContent,
  createContent
} from '@/services/pageContentService';

const selectedPage = ref('');
const pages = ref(['about', 'contact']);
const contents = ref([]);

const loadContent = async () => {
  if (!selectedPage.value) return;
  try {
    const published = await getPublishedContent(selectedPage.value);
    const drafts = await getDraftContent();
    const merged = [...published];

    drafts.forEach(d => {
      if (d.pageName === selectedPage.value && !merged.some(p => p.idContent === d.idContent)) {
        merged.push(d);
      }
    });

    contents.value = merged;
  } catch (error) {
    console.error("Error cargando contenido:", error);
  }
};

const handleFileUpload = (event, content) => {
  const file = event.target.files[0];
  if (file) {
    const reader = new FileReader();
    reader.onload = (e) => {
      content.image = e.target.result.split(',')[1]; // base64
    };
    reader.readAsDataURL(file);
  }
};

const saveContent = async (content) => {
  try {
    content.pageName = selectedPage.value;
    content.modifiedBy = Number(localStorage.getItem("userId"));

    let saved;
    if (content.idContent) {
      saved = await updateContent(content.idContent, content);
    } else {
      saved = await createContent(content);
    }

    // 🔁 Si el backend creó una nueva versión, actualiza el ID en el frontend
    if (saved?.idContent && saved?.idContent !== content.idContent) {
      content.idContent = saved.idContent;
    }

    delete content.tempId; // Limpia el tempId si lo tenía
    await loadContent();
    alert("Contenido guardado correctamente.");
  } catch (error) {
    console.error("Error al guardar contenido:", error);
    alert("Error al guardar contenido.");
  }
};


const sendToModeration = async (content) => {
  try {
    const contentToSend = {
      ...content,
      status: "PROCESO",
      pageName: selectedPage.value,
      modifiedBy: Number(localStorage.getItem("userId")),
      editorEmail:       localStorage.getItem("usuarioEmail")// asegurate que este valor exista
    };

    console.log("🟡 Enviando a moderación:", contentToSend); // 🔍 Aquí se imprime todo el contenido enviado

    let saved;
    if (contentToSend.idContent) {
      saved = await updateContent(contentToSend.idContent, contentToSend);
    } else {
      saved = await createContent(contentToSend);
    }

    if (saved?.idContent && saved?.idContent !== contentToSend.idContent) {
      console.log("🆕 Se creó una nueva versión:", saved.idContent);
      content.idContent = saved.idContent;
    }

    delete content.tempId;

    alert("Enviado a moderación correctamente.");
    await loadContent();
  } catch (error) {
    console.error("❌ Error al enviar a moderación:", error);
    alert("No se pudo enviar a moderación.");
  }
};



const addContent = () => {
  contents.value.push({
    tempId: Date.now(),
    pageName: selectedPage.value,
    sectionName: '',
    contentTitle: '',
    contentBody: '',
    image: null,
    status: 'PROCESO',
    modifiedBy: Number(localStorage.getItem('userId'))
  });
};

onMounted(loadContent);
</script>


<style scoped>
.admin-page-editor {
  background: #f9f9f9;
  color: #e0e1dd;
  min-height: 100vh;
  padding: 2rem;
  border-radius: 10px;
  max-width: 900px;
  margin: 0 auto;
}

.section {
  border: 1px solid #45C4B0;
  padding: 15px;
  margin: 10px 0;
  background: #13678a;
  border-radius: 8px;
}

input,
textarea,
select {
  display: block;
  width: 100%;
  margin: 0.5rem 0;
  padding: 8px;
  border-radius: 4px;
  border: 1px solid #ccc;
}

.button-group {
  display: flex;
  gap: 10px;
  margin-top: 10px;
}

.edit-button {
  padding: 0.6rem 1rem;
  background-color: #00ABBD;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
.edit-button:hover {
  background-color: #0099DD;
}

.moderate-button {
  padding: 0.6rem 1rem;
  background-color: #FF9933;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
.moderate-button:hover {
  background-color: #e57a00;
}

.header {
  text-align: center;
  font-size: 22px;
  font-weight: bold;
  background: #45C4B0;
  color: white;
  padding: 12px;
  border-radius: 5px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.4);
}
</style>
