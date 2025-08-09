<script setup lang="ts">
import { ref, onMounted } from 'vue';

const bannerImages = ref([
  '/banner.jpg',
  '/banner2.jpg',
  '/banner.jpg'
]);
const currentBannerIndex = ref(0);

const specialties = ref([
  { name: 'Oncología' },
  { name: 'Maternidad' },
  { name: 'Cardiología' },
  { name: 'Cirugía' }
]);

const doctors = ref([
  { name: 'Dr. Juan Pérez', specialty: 'Cardiólogo', image: '/public/doctor1.jpeg' },
  { name: 'Dra. Ana López', specialty: 'Pediatra', image: '/public/doctor1.jpeg' },
  { name: 'Dr. Carlos Gómez', specialty: 'Ortopedista', image: '/public/doctor1.jpeg' },
  { name: 'Dra. María Rodríguez', specialty: 'Dermatóloga', image: '/public/doctor1.jpeg' },
  { name: 'Dr. Andrés Fuentes', specialty: 'Neurólogo', image: '/public/doctor1.jpeg' },
  { name: 'Dra. Sofía Morales', specialty: 'Oftalmóloga', image: '/public/doctor1.jpeg' },
  { name: 'Dr. Fernando Díaz', specialty: 'Gastroenterólogo', image: '/public/doctor1.jpeg' },
  { name: 'Dra. Gabriela Santos', specialty: 'Endocrinóloga', image: '/public/doctor1.jpeg' },
  { name: 'Dr. Esteban Ruiz', specialty: 'Reumatólogo', image: '/public/doctor1.jpeg' },
  { name: 'Dra. Carolina Vega', specialty: 'Ginecóloga', image: '/public/doctor1.jpeg' },
  { name: 'Dr. Oscar Navarro', specialty: 'Urólogo', image: '/public/doctor1.jpeg' },
  { name: 'Dra. Paula Ramírez', specialty: 'Psiquiatra', image: '/public/doctor1.jpeg' }
]);

const scrollIndex = ref(0);
const totalDoctors = doctors.value.length;
const doctorsPerPage = 4;

const autoScrollBanner = () => {
  setInterval(() => {
    currentBannerIndex.value = (currentBannerIndex.value + 1) % bannerImages.value.length;
  }, 3000);
};

const autoScrollDoctors = () => {
  setInterval(() => {
    scrollIndex.value = (scrollIndex.value + doctorsPerPage) % totalDoctors;
  }, 3000);
};

onMounted(() => {
  autoScrollBanner();
  autoScrollDoctors();
});
</script>

<template>
  <!-- Banner con 3 imágenes en carrusel automático -->
  <div class="banner-container">
    <img :src="bannerImages[currentBannerIndex]" alt="Centro Médico" class="banner-image" />
    <div class="banner-text">Guatemala UNIS:<br>Centro Médico UNIS</div>
  </div>

  <div class="content">
    <!-- Especialidades Destacadas -->
    <div class="specialties-container">
      <h2><span class="highlight">Servicios Destacados</span></h2>
      <div class="carousel">
        <div v-for="(specialty, index) in specialties" :key="index" class="specialty-card">
          <h3>{{ specialty.name }}</h3>
          <button class="view-more">Ver más</button>
        </div>
      </div>
    </div>

    <!-- Doctores Destacados (Carrusel automático) -->
    <div class="doctors-container">
      <h2><span class="highlight">Doctores Destacados</span></h2>
      <div class="doctor-carousel">
        <div class="doctor-list" :style="{ transform: `translateX(-${scrollIndex * 25}%)` }">
          <div v-for="(doctor, index) in doctors" :key="index" class="doctor-card">
            <img :src="doctor.image" alt="Foto del doctor" class="doctor-image" />
            <h3>{{ doctor.name }}</h3>
            <p>{{ doctor.specialty }}</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
/* Banner debajo del navbar */
.banner-container {
  position: relative;
  width: 100%;
  height: 350px;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-top: -77px; /* Ajuste para dejar espacio debajo del navbar */
}
.banner-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  filter: brightness(0.7);
  transition: opacity 1s ease-in-out;
}
.banner-text {
  position: absolute;
  text-align: center;
  color: white;
  font-size: 30px;
  font-weight: bold;
}

/* Ajuste del contenido para que no quede detrás del banner */
.content {
  margin-top: 20px;
}

/* Especialidades Destacadas */
.specialties-container {
  text-align: center;
  margin-top: 30px;
  color: black;
  width: 100%;
}
.highlight {
  color: #007bff;
  font-weight: bold;
}
.carousel {
  display: flex;
  justify-content: center;
  gap: 20px;
  padding: 20px;
  flex-wrap: wrap;
}
.specialty-card {
  background: white;
  padding: 20px;
  border-radius: 10px;
  text-align: center;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
  min-width: 180px;
  flex: 1 1 20%;
}
.view-more {
  background: #007bff;
  color: white;
  border: none;
  padding: 10px 15px;
  border-radius: 5px;
  cursor: pointer;
  margin-top: 10px;
  font-size: 16px;
}
.view-more:hover {
  background: #0056b3;
}

/* Carrusel de Doctores */
.doctors-container {
  text-align: center;
  margin-top: 30px;
  width: 100%;
  color: black;
}
.doctor-carousel {
  overflow: hidden;
  width: 100%;
  display: flex;
  justify-content: center;
}
.doctor-list {
  display: flex;
  transition: transform 1s ease-in-out;
  width: 100%;
}
.doctor-card {
  background: white;
  padding: 20px;
  border-radius: 10px;
  text-align: center;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
  min-width: 25%;
  margin-right: 10px;
}
.doctor-image {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  object-fit: cover;
}
</style>
