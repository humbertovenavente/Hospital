<template>
  <div class="historia-container">
    <div class="historia" v-if="historia">
      <h1>{{ historia.nombreEntidad }}</h1>

      <div class="historia-layout">
        <!-- Sección de Historia -->
        <section class="historia-info">
          <h2>Historia</h2>
          <div v-html="historia.historia"></div>
        </section>

        <!-- Sección de Méritos -->
        <section class="meritos-info">
          <h2>Méritos</h2>
          <div class="meritos-grid">
            <div v-for="(merito, index) in meritosData" :key="index" class="merito-card">
              <span class="merito-icon">✔️</span>
              <div class="merito-content">
                <h4 class="merito-title">{{ merito.title }}</h4>
                <p class="merito-description">{{ merito.description }}</p>
              </div>
            </div>
          </div>
        </section>
      </div>

      <!-- Sección Línea del Tiempo -->
      <section class="timeline-section">
        <h2>Línea del Tiempo</h2>
        <div class="timeline">
          <div v-for="(evento, index) in timelineData" :key="index" class="timeline-item">
            <div class="timeline-dot"></div>
            <div class="timeline-content">
              <h3 class="timeline-year">{{ evento.year }}</h3>
              <h4 class="timeline-title">{{ evento.title }}</h4>
              <p class="timeline-description">{{ evento.description }}</p>
            </div>
          </div>
        </div>
      </section>
    </div>

    <div v-else class="no-data">
      <p>No hay historia publicada disponible.</p>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import axios from "axios";
import API_URL from "@/config";

const historia = ref(null);
const timelineData = ref([]);
const meritosData = ref([]);

const config = {
  headers: {
    "Content-Type": "application/json"
  }
};

onMounted(async () => {
  try {
    const response = await axios.get(`${API_URL}/historias/publicadas`, config);
    const data = response.data;

    if (Array.isArray(data) && data.length > 0) {
      historia.value = data[0];

      // Parsear JSON de línea del tiempo
      try {
        timelineData.value = JSON.parse(historia.value.lineaDelTiempo || "[]");
      } catch {
        timelineData.value = [];
      }

      // Parsear JSON de méritos
      try {
        meritosData.value =
          typeof historia.value.meritos === "string" &&
          historia.value.meritos.trim().startsWith("[")
            ? JSON.parse(historia.value.meritos)
            : [];
      } catch {
        meritosData.value = [];
      }
    }
  } catch (error) {
    console.error("Error al obtener historia publicada:", error);
  }
});
</script>

<style scoped>
.historia-container {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  min-height: 100vh;
  background-color: #0b3d5d;
  padding: 4rem 2rem;
}

.historia {
  width: 100%;
  max-width: 1400px;
  background-color: #012030;
  color: #DAFDBA;
  padding: 3rem;
  border-radius: 12px;
  box-shadow: 0px 6px 12px rgba(0, 0, 0, 0.3);
}

.historia h1 {
  text-align: center;
  font-size: 2.5rem;
  font-weight: bold;
  margin-bottom: 40px;
  color: #A4F0C4;
}

.historia-layout {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(350px, 1fr));
  gap: 2rem;
}

.historia-info {
  background-color: #13678A;
  padding: 25px;
  border-radius: 10px;
  box-shadow: 2px 4px 8px rgba(0, 0, 0, 0.2);
}

.historia-info h2, .meritos-info h2 {
  text-align: center;
  margin-bottom: 15px;
  color: #A4F0C4;
  font-size: 1.8rem;
  font-weight: bold;
}

.historia-info div {
  font-size: 1.2rem;
  line-height: 1.9;
  text-align: justify;
  color: #DAFDBA;
}

.meritos-info {
  background-color: #13678A;
  padding: 25px;
  border-radius: 10px;
  box-shadow: 2px 4px 8px rgba(0, 0, 0, 0.2);
}

.meritos-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 1.5rem;
}

.merito-card {
  background-color: #A4F0C4;
  color: #012030;
  padding: 15px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  gap: 10px;
  box-shadow: 2px 4px 8px rgba(0, 0, 0, 0.3);
}

.merito-icon {
  font-size: 1.5rem;
  color: #13678A;
}

.timeline-section {
  margin-top: 50px;
}

.timeline {
  width: 100%;
  max-width: 1100px;
  padding-left: 50px;
  margin: 0 auto;
  border-left: 4px solid #45C4B0;
  padding-top: 20px;
}

.timeline-item {
  position: relative;
  padding-left: 50px;
  margin-bottom: 40px;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}

.timeline-dot {
  width: 20px;
  height: 20px;
  background-color: #45C4B0;
  border-radius: 50%;
  position: absolute;
  left: -10px;
  top: 15px;
}

.no-data {
  color: #fff;
  text-align: center;
  font-size: 1.2rem;
}
</style>
