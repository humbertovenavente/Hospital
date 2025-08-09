<template>
    <!-- Contenido Principal -->
    <div class="home-container">
      <!-- Hero Section -->
      <div class="hero">
        <h1>Guatemala UNIS: Centro Médico UNIS</h1>
      </div>

      <!-- Carrusel de Doctores Destacados -->
      <section class="section">
        <h2>Doctores Destacados</h2>
        <div class="carousel">
          <div v-for="(doctor, index) in doctors" :key="index" class="doctor-card">
            <img :src="doctor.image" :alt="doctor.name" />
            <h3>{{ doctor.name }}</h3>
            <p>{{ doctor.specialty }}</p>
          </div>
        </div>
      </section>

      <!-- Banner de Imágenes Administrables -->
      <section class="banner">
        <h2>Centro Médico Guatemala</h2>
        <img src="https://www.hok.com/wp-content/uploads/2022/05/iu-health-bloomington-exeterior-2-1900-1536x1027.jpg" alt="Chart">
      </section>

      <!-- Listado de Servicios Médicos -->
      <section class="section">
        <h2>Servicios Médicos</h2>
        <div class="services-grid">
          <div v-for="(service, index) in services" :key="index" class="service-card">
            <h3>{{ service.name }}</h3>
            <button>Ver más</button>
          </div>
        </div>
      </section>

      <!-- Chart de los 5 Servicios Destacados -->
      <section class="chart-section">
        <h2>Especialidades Destacadas</h2>
        <img src="https://www.hok.com/wp-content/uploads/2022/05/iu-health-bloomington-exeterior-2-1900-1536x1027.jpg" alt="Chart">
      </section>

      <!-- Grid de Fotos -->
      <section class="photo-grid">
        <h2>Galería de Imágenes</h2>
        <div class="grid">
          <img v-for="(photo, index) in photos" :key="index" :src="photo" alt="Hospital">
        </div>
      </section>

      <!-- Video Informativo -->
      <section class="video-section">
        <h2>Video Informativo</h2>
        <iframe width="100%" height="315" src="https://www.youtube.com/embed/samplevideo" frameborder="0" allowfullscreen></iframe>
      </section>
    </div>

</template>

<script>
export default {
  data() {
    return {
      hospitalImage: "",
      doctors: [],
      services: [
        { name: "Oncología" },
        { name: "Maternidad" },
        { name: "Cardiología" },
        { name: "Cirugía" }
      ],
      photos: []
    };
  },
  async created() {

    try {
      const resDoctors = await fetch(
        `https://www.hok.com/wp-content/uploads/2022/05/iu-health-bloomington-exeterior-2-1900-1536x1027.jpg`
      );
      const dataDoctors = await resDoctors.json();
      this.doctors = dataDoctors.results.map((item, index) => ({
        name: `Doctor ${index + 1}`,
        specialty: ["Cardiología", "Pediatría", "Neurología"][index],
        image: item.urls.small
      }));

      const resHospital = await fetch(
        `https://www.hok.com/wp-content/uploads/2022/05/iu-health-bloomington-exeterior-2-1900-1536x1027.jpg`
      );
      const dataHospital = await resHospital.json();
      this.hospitalImage = dataHospital.results[0].urls.small;

      const resPhotos = await fetch(
        `https://www.hok.com/wp-content/uploads/2022/05/iu-health-bloomington-exeterior-2-1900-1536x1027.jpg`
      );
      const dataPhotos = await resPhotos.json();
      this.photos = dataPhotos.results.map((item) => item.urls.small);
    } catch (error) {
      console.error("Error al obtener imágenes", error);
    }
  }
};
</script>
<style scoped>
/* --------------------------------------
   RESET BÁSICO
-------------------------------------- */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

/* --------------------------------------
   FUENTES Y VARIABLES (Opcional)
-------------------------------------- */
:root {
  --color-bg1: #142b40;
  --color-bg2: #1f3a5b;
  --color-card: #1b263b;
  --color-text: #ffffff;
  --color-primary: #0d1b2a;
  --color-primary-hover: #415a77;
  --color-section: rgba(0, 0, 0, 0.3);
  --font-main: 'Poppins', sans-serif;
}

/* --------------------------------------
   CONTENEDOR PRINCIPAL
-------------------------------------- */
.home-container {
  /* Fondo degradado para toda la página */
  min-height: 100vh;
  background: linear-gradient(135deg, var(--color-bg1), var(--color-bg2));
  color: var(--color-text);
  text-align: center;
  font-family: var(--font-main);
  /* Espacio inferior para evitar "cortes" en la última sección */
  padding-bottom: 2rem;
}

/* --------------------------------------
   HERO SECTION
-------------------------------------- */
.hero {
  width: 100%;
  height: 60vh; /* Un poco más alto para destacar */
  background: url("https://www.hok.com/wp-content/uploads/2022/05/iu-health-bloomington-exeterior-2-1900-1536x1027.jpg")
    no-repeat center center/cover;
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  /* Sombra interna para realzar el contenedor */
  box-shadow: inset 0 0 80px rgba(0, 0, 0, 0.7);
}

.hero::after {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.4);
}

.hero h1 {
  position: relative;
  z-index: 1;
  background: rgba(0, 0, 0, 0.6);
  padding: 1.2rem 2rem;
  border-radius: 10px;
  font-size: 2.2rem;
  text-shadow: 2px 2px 8px rgba(0, 0, 0, 0.6);
  animation: fadeInDown 1s ease forwards;
}

/* Animación para el texto principal */
@keyframes fadeInDown {
  0% {
    opacity: 0;
    transform: translateY(-15px);
  }
  100% {
    opacity: 1;
    transform: translateY(0);
  }
}

/* --------------------------------------
   SECCIONES GENERALES
-------------------------------------- */
.section,
.banner,
.chart-section,
.photo-grid,
.video-section {
  margin-top: 3rem;
  animation: fadeIn 1.2s ease-in-out;
}

/* Título de sección */
.section h2,
.banner h2,
.chart-section h2,
.photo-grid h2,
.video-section h2 {
  font-size: 1.8rem;
  margin-bottom: 1.5rem;
  text-transform: uppercase;
  letter-spacing: 1px;
  color: var(--color-text);
  position: relative;
  display: inline-block;
}

/* Línea decorativa bajo el título */
.section h2::after,
.banner h2::after,
.chart-section h2::after,
.photo-grid h2::after,
.video-section h2::after {
  content: "";
  width: 60%;
  height: 2px;
  display: block;
  margin: 0.5rem auto 0;
}

/* Animación genérica de aparición */
@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* --------------------------------------
   CARRUSEL DE DOCTORES
-------------------------------------- */
.carousel {
  display: flex;
  gap: 20px;
  overflow-x: auto;
  padding: 0 1rem;
  scroll-behavior: smooth;
  margin-top: 1rem;
}

.doctor-card {
  background: var(--color-card);
  padding: 1rem;
  border-radius: 10px;
  text-align: center;
  min-width: 160px;
  transition: transform 0.3s, box-shadow 0.3s;
}

.doctor-card:hover {
  transform: translateY(-5px);
  box-shadow: 0px 10px 20px rgba(0, 0, 0, 0.4);
}

.doctor-card img {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  object-fit: cover;
  margin-bottom: 0.5rem;
  border: 2px solid #fff;
}

/* --------------------------------------
   BANNER
-------------------------------------- */
.banner {
  margin: 3rem auto 2rem;
}

.banner h2 {
  margin-bottom: 1rem;
}

.banner img {
  width: 80%;
  max-width: 900px;
  border-radius: 10px;
  box-shadow: 0px 4px 20px rgba(0, 0, 0, 0.5);
  transition: transform 0.3s;
}

.banner img:hover {
  transform: scale(1.02);
}

/* --------------------------------------
   SERVICIOS GRID
-------------------------------------- */
.services-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
  gap: 20px;
  margin: 1rem 1rem 0 1rem;
}

.service-card {
  background: #fff;
  color: #333;
  padding: 1.5rem;
  border-radius: 10px;
  text-align: center;
  transition: transform 0.3s, box-shadow 0.3s;
  position: relative;
}

.service-card:hover {
  transform: translateY(-5px);
  box-shadow: 0px 4px 15px rgba(0, 0, 0, 0.2);
}

.service-card h3 {
  margin-bottom: 1rem;
  font-weight: 600;
}

.service-card button {
  margin-top: 0.5rem;
  background: var(--color-primary);
  color: #fff;
  padding: 0.6rem 1.2rem;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background 0.3s;
  font-weight: 500;
}

.service-card button:hover {
  background: var(--color-primary-hover);
}

/* --------------------------------------
   CHART SECTION
-------------------------------------- */
.chart-section {
  margin: 3rem 0 2rem;
}

.chart-section img {
  width: 80%;
  max-width: 700px;
  border-radius: 10px;
  box-shadow: 0px 4px 20px rgba(0, 0, 0, 0.5);
  transition: transform 0.3s;
}

.chart-section img:hover {
  transform: scale(1.02);
}

/* --------------------------------------
   GRID DE FOTOS
-------------------------------------- */
.photo-grid {
  margin: 3rem 0;
}

.photo-grid .grid {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
  justify-content: center;
}

.photo-grid img {
  border-radius: 5px;
  width: 150px;
  height: 100px;
  object-fit: cover;
  transition: transform 0.3s, box-shadow 0.3s;
  border: 2px solid #fff;
}

.photo-grid img:hover {
  transform: scale(1.05);
  box-shadow: 0px 4px 10px rgba(255, 255, 255, 0.3);
}

/* --------------------------------------
   VIDEO SECTION
-------------------------------------- */
.video-section {
  margin: 3rem 0;
}

.video-section iframe {
  border-radius: 10px;
  box-shadow: 0px 4px 20px rgba(0, 0, 0, 0.5);
  width: 80%;
  max-width: 900px;
  transition: transform 0.3s;
}

.video-section iframe:hover {
  transform: scale(1.01);
}

/* --------------------------------------
   RESPONSIVE
-------------------------------------- */
@media (max-width: 768px) {
  .hero {
    height: 50vh;
  }

  .hero h1 {
    font-size: 1.6rem;
    padding: 0.8rem 1.2rem;
  }

  .doctor-card {
    min-width: 130px;
  }

  .services-grid {
    grid-template-columns: 1fr 1fr;
  }

  .banner img,
  .chart-section img,
  .video-section iframe {
    width: 90%;
  }
}
</style>
