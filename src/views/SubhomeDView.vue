<template>
  <div class="subhome-container">
    <h1>Especialidades y Recursos</h1>

    <!-- Carrusel de Doctores Destacados -->
    <div class="section">
      <h2>Doctores Destacados</h2>
      <div class="carousel">
        <div v-for="(doctor, index) in doctors" :key="index" class="doctor-card">
          <img :src="doctor.image" :alt="doctor.name" />
          <h3>{{ doctor.name }}</h3>
          <p>{{ doctor.specialty }}</p>
        </div>
      </div>
    </div>

    <!-- Banner de Imágenes Administrables -->
    <section class="banner">
      <h2>Centro Médico Guatemala</h2>
      <img :src="hospitalImage" alt="Hospital">
    </section>

    <!-- Tarjetas de Especialidades -->
    <div class="section specialties">
      <h2>Especialidades Destacadas</h2>
      <div class="specialty-cards">
        <div v-for="(specialty, index) in specialties" :key="index" class="specialty-card">
          <img :src="specialty.image" alt="Especialidad">
          <h3>{{ specialty.name }}</h3>
          <button>Ver más</button>
        </div>
      </div>
    </div>

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
      <iframe width="100%" height="315" src="https://www.youtube.com/embed/fhX8z7oHThE" frameborder="0" allowfullscreen></iframe>
    </section>
  </div>
</template>


<script>
export default {
  data() {
    return {
      hospitalImage: "https://www.hok.com/wp-content/uploads/2022/05/iu-health-bloomington-exeterior-2-1900-1536x1027.jpg",
      doctors: [
        { name: "Dr. Juan Pérez", specialty: "Cardiólogo", image: "https://www.hok.com/wp-content/uploads/2022/05/iu-health-bloomington-exeterior-2-1900-1536x1027.jpg" },
        { name: "Dra. Ana Gómez", specialty: "Pediatra", image: "https://www.hok.com/wp-content/uploads/2022/05/iu-health-bloomington-exeterior-2-1900-1536x1027.jpg" }
      ],
      specialties: [
        { name: "Cardiología", image: "https://www.hok.com/wp-content/uploads/2022/05/iu-health-bloomington-exeterior-2-1900-1536x1027.jpg" },
        { name: "Neurología", image: "https://www.hok.com/wp-content/uploads/2022/05/iu-health-bloomington-exeterior-2-1900-1536x1027.jpg" },
        { name: "Oncología", image: "https://www.hok.com/wp-content/uploads/2022/05/iu-health-bloomington-exeterior-2-1900-1536x1027.jpg" },
        { name: "Maternidad", image: "https://www.hok.com/wp-content/uploads/2022/05/iu-health-bloomington-exeterior-2-1900-1536x1027.jpg" }
      ],
      services: [
        { name: "Oncología" },
        { name: "Maternidad" },
        { name: "Cardiología" },
        { name: "Cirugía" }
      ],
      photos: [
        "https://www.hok.com/wp-content/uploads/2022/05/iu-health-bloomington-exeterior-2-1900-1536x1027.jpg",
        "https://www.hok.com/wp-content/uploads/2022/05/iu-health-bloomington-exeterior-2-1900-1536x1027.jpg",
        "https://www.hok.com/wp-content/uploads/2022/05/iu-health-bloomington-exeterior-2-1900-1536x1027.jpg"
      ]
    };
  }
};
</script>

<style scoped>
/* Estilos Generales */
.subhome-container {
  width: 100vw;
  margin: 0;
  padding: 0;
  background: #0d1b2a;
  color: white;
  text-align: center;
}

/* Secciones */
.section {
  padding: 50px 10%;
}

h1 {
  font-size: 3rem;
  margin-bottom: 20px;
}

h2 {
  font-size: 2rem;
  margin-bottom: 15px;
}

/* Carrusel de Doctores */
.carousel {
  display: flex;
  gap: 20px;
  overflow-x: auto;
  padding: 10px;
  justify-content: center;
}

.doctor-card {
  background: #324a5f;
  padding: 20px;
  border-radius: 10px;
  text-align: center;
  width: 200px;
  transition: transform 0.3s ease-in-out;
}

.doctor-card:hover {
  transform: scale(1.1);
}

.doctor-card img {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  object-fit: cover;
}

/* Banner */
.banner {
  text-align: center;
  margin: 40px 0;
}

.banner img {
  width: 100%;
  max-height: 400px;
  object-fit: cover;
  border-radius: 10px;
}

/* Especialidades */
.specialty-cards {
  display: flex;
  gap: 20px;
  justify-content: center;
  flex-wrap: wrap;
}

.specialty-card {
  background: #1b2c3f;
  padding: 20px;
  border-radius: 10px;
  text-align: center;
  width: 220px;
  transition: transform 0.3s ease-in-out;
}

.specialty-card:hover {
  transform: scale(1.05);
}

.specialty-card img {
  width: 100px;
  height: 100px;
  border-radius: 50%;
}

/* Servicios */
.services-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
}

.service-card {
  background: #1e3a5f;
  padding: 20px;
  border-radius: 10px;
  text-align: center;
}

.service-card button {
  background: #00a6fb;
  color: white;
  padding: 10px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.service-card button:hover {
  background: #007bbf;
}

/* Chart */
.chart-section img {
  width: 80%;
  border-radius: 10px;
}

/* Grid de Fotos */
.photo-grid .grid {
  display: flex;
  gap: 15px;
  justify-content: center;
  flex-wrap: wrap;
}

.photo-grid img {
  border-radius: 10px;
  width: 180px;
  height: 120px;
  object-fit: cover;
  transition: transform 0.3s ease-in-out;
}

.photo-grid img:hover {
  transform: scale(1.05);
}

/* Video */
.video-section iframe {
  border-radius: 10px;
  width: 80%;
  max-width: 800px;
}

/* Responsive */
@media (max-width: 768px) {
  h1 {
    font-size: 2.5rem;
  }
  h2 {
    font-size: 1.8rem;
  }
  .doctor-card, .specialty-card, .service-card {
    width: 100%;
  }
}
</style>

