<template>
  <div class="container mx-auto px-6 py-10">
    <h1 class="text-4xl font-bold text-center text-blue-500 my-10">Cátálogo de doctores</h1>

    <!-- Cargando datos -->
    <div v-if="loading" class="text-center text-gray-600 text-lg">Cargando doctores...</div>

    <!-- Error al cargar -->
    <div v-if="error" class="text-red-500 text-center text-lg">{{ error }}</div>

    <!-- Grid de doctores -->
    <div v-else class="doctor-grid">
      <div v-for="(doctor, index) in doctores" :key="doctor.idDoctor" class="card">
        <div class="card-content">
          <img
            :src="doctor.fotografia || '/default-doctor.jpg'"
            alt="Foto del doctor"
            class="doctor-image"
          />
          <h2 class="doctor-name">{{ doctor.nombreUsuario }} {{ doctor.apellido }}</h2>
          <p class="doctor-specialty">{{ doctor.especialidad }}</p>
          <p class="doctor-id">ID: #{{ doctor.idDoctor }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import catalogoService from '@/services/catalogoService';

export default {
  data() {
    return {
      doctores: [],
      loading: true,
      error: null
    };
  },
  async created() {
    try {
      this.doctores = await catalogoService.getCatalogoDoctores();
      console.log("Datos de doctores en catálogo:", this.doctores);
    } catch (err) {
      this.error = "Error al cargar los doctores.";
      console.error(err);
    } finally {
      this.loading = false;
    }
  }
};
</script>

<style scoped>
.container {
  max-width: 1400px;
  margin: auto;
}

/* 🔹 Establece 3 columnas fijas */
.doctor-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr); /* 3 columnas de tamaño igual */
  gap: 40px; /* Espacio entre tarjetas */
  justify-content: center;
}

.card {
  background: white;
  border-radius: 15px;
  box-shadow: 0px 5px 15px rgba(0, 0, 0, 0.15);
  padding: 25px;
  text-align: center;
  transition: transform 0.3s ease-in-out;
}

.card:hover {
  transform: translateY(-7px);
}

.card-content {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.doctor-image {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  object-fit: cover;
  margin-bottom: 20px;
  border: 3px solid #3498db;
}

.doctor-name {
  font-size: 20px;
  font-weight: bold;
  color: #2c3e50;
  margin-bottom: 8px;
}

.doctor-specialty {
  color: #555;
  font-size: 16px;
  font-weight: 500;
  margin-bottom: 5px;
}

.doctor-id {
  font-size: 14px;
  color: #777;
  margin-top: 5px;
}

/* 🔹 Asegurar que en pantallas pequeñas solo haya 1 o 2 columnas */
@media (max-width: 1024px) {
  .doctor-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 640px) {
  .doctor-grid {
    grid-template-columns: repeat(1, 1fr);
  }
}
</style>
