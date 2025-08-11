<template>
  <div class="page-container">
    <div v-if="pageContent.length === 0" class="empty">
      No hay contenido publicado disponible.
    </div>

    <div v-else class="section" v-for="section in pageContent" :key="section.idContent">
      <div class="header">{{ section.contentTitle }}</div>
      <div v-html="section.contentBody"></div>

      <img
        v-if="section.image"
        :src="section.image"
        alt="Imagen de sección"
        class="page-image"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue';
import { useRoute } from 'vue-router';
import { getPublishedContent } from '@/services/pageContentService';

const route = useRoute();
const pageName = ref(route.params.pageName);
const pageContent = ref([]);

const loadContent = async () => {
  try {
    const data = await getPublishedContent(pageName.value);

    pageContent.value = data.map(section => ({
      ...section,
      image: section.image ? `data:image/jpeg;base64,${section.image}` : null
    }));
  } catch (error) {
    console.error("❌ Error cargando contenido:", error);
    pageContent.value = [];
  }
};

onMounted(loadContent);
watch(() => route.params.pageName, (newName) => {
  pageName.value = newName;
  loadContent();
});
</script>

<style scoped>
.page-container {
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
  margin: 20px 0;
  background: #13678a;
  border-radius: 8px;
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

.page-image {
  max-width: 100%;
  height: auto;
  margin-top: 15px;
  border-radius: 6px;
  box-shadow: 0 2px 6px rgba(0,0,0,0.2);
}

.empty {
  color: #eee;
  background-color: #0b3d5d;
  text-align: center;
  padding: 2rem;
  border-radius: 10px;
  font-size: 18px;
}
</style>
