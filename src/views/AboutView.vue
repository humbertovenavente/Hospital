<template>
  <div class="about-container">
    <!-- Hero Section -->
    <div class="hero">
      <div class="overlay">
        <h1>Hospitales de Guatemala</h1>
        <p>Comprometidos con la excelencia en la atención médica y el bienestar de nuestros pacientes.</p>
      </div>
    </div>

    <!-- Información General -->
    <div class="about-section">
      <h2>¿Quiénes Somos?</h2>
      <p>
        Somos una red de hospitales en Guatemala dedicada a brindar atención médica de alta calidad con tecnología avanzada y un enfoque humano.
        Nuestra misión es garantizar el acceso a servicios de salud con estándares internacionales, priorizando el bienestar de nuestros pacientes.
      </p>
      <img src="https://costar.brightspotcdn.com/64/81/8e4104c14dedab09ed9b095ba622/hospital.jpg" alt="Hospital Roosevelt" class="about-image" />
    </div>

    <!-- Historia del Hospital -->
    <div class="about-section history">
      <div class="history-content">
        <h2>Nuestra Historia</h2>
        <p>
          Fundados en 1950, hemos crecido hasta convertirnos en una de las principales redes de salud en Guatemala, con hospitales modernos
          como el *Hospital Roosevelt, **San Juan de Dios, y **Centros Médicos Privados*. Nuestro compromiso ha sido innovar en tecnología
          médica y mejorar la calidad de vida de nuestros pacientes.
        </p>
      </div>
      <img src="https://www.hok.com/wp-content/uploads/2022/05/iu-health-bloomington-exeterior-2-1900-1536x1027.jpg" alt="Hospital San Juan de Dios" class="about-image" />
    </div>

    <!-- Nuestra Misión y Visión -->
    <div class="about-section">
      <h2>Nuestra Misión</h2>
      <p>
        Proporcionar servicios médicos accesibles y de calidad, asegurando que cada paciente reciba la mejor atención médica posible.
      </p>
      <h2>Nuestra Visión</h2>
      <p>
        Ser la red hospitalaria más innovadora y confiable de Guatemala, con un enfoque en la salud integral y la tecnología de punta.
      </p>
    </div>

    <!-- Nuestro Equipo -->
    <div class="about-section team-section">
      <h2>Nuestro Equipo Médico</h2>
      <div class="team">
        <div class="team-member" v-for="(member, index) in team" :key="index">
          <img :src="member.image" alt="Team Member" />
          <h3>{{ member.name }}</h3>
          <p>{{ member.role }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      team: [
        { name: "Dr. Carlos Ramírez", role: "Cirujano General", image: "https://randomuser.me/api/portraits/men/32.jpg" },
        { name: "Dra. María López", role: "Cardióloga", image: "https://randomuser.me/api/portraits/women/44.jpg" },
        { name: "Dr. Fernando Castillo", role: "Neurólogo", image: "https://randomuser.me/api/portraits/men/50.jpg" },
        { name: "Dra. Andrea Méndez", role: "Pediatra", image: "https://randomuser.me/api/portraits/women/30.jpg" }
      ]
    };
  }
};
</script>

<style scoped>
/* Estilos Generales */
.about-container {
  color: white;
  background: #0d1b2a;
  padding: 0;
  margin: 0;
}

/* Hero Section */
.hero {
  background: url("https://asdf.amerins.com/uploads/Hospital_General_de_Massachusetts_488eabdcf1.jpeg") no-repeat center center/cover;
  height: 60vh;
  display: flex;
  align-items: center;
  justify-content: center;
  text-align: center;
  position: relative;
}

.hero .overlay {
  background: rgba(0, 0, 0, 0.6);
  padding: 50px;
  border-radius: 10px;
  max-width: 600px;
}

.hero h1 {
  font-size: 2.5rem;
  font-weight: bold;
  margin-bottom: 10px;
}

.hero p {
  font-size: 1.2rem;
  opacity: 0.9;
}

/* Secciones de información */
.about-section {
  padding: 50px 20px;
  max-width: 1000px;
  margin: 40px auto;
  background: #1b263b;
  border-radius: 10px;
  box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.2);
  text-align: center;
}

.about-section h2 {
  font-size: 2rem;
  margin-bottom: 15px;
}

.about-section p {
  font-size: 1.2rem;
  opacity: 0.9;
  line-height: 1.6;
}

/* Imagenes dentro del contenido */
.about-image {
  width: 100%;
  max-width: 700px;
  border-radius: 10px;
  margin-top: 20px;
}

/* Historia con diseño de dos columnas */
.history {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  text-align: left;
}

.history-content {
  flex: 1;
  padding: 20px;
}

.history img {
  flex: 1;
  max-width: 500px;
  border-radius: 10px;
}

/* Sección del equipo médico */
.team-section {
  text-align: center;
}

.team {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
  justify-items: center;
  padding: 20px;
}

.team-member {
  background: #324a5f;
  padding: 20px;
  border-radius: 10px;
  text-align: center;
  transition: transform 0.3s ease-in-out, box-shadow 0.3s ease-in-out;
}

.team-member:hover {
  transform: scale(1.05);
  box-shadow: 0px 6px 12px rgba(255, 255, 255, 0.2);
}

.team-member img {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  border: 3px solid white;
  margin-bottom: 10px;
}

h1, h2 {
  font-family: "Poppins", sans-serif;
}

h3 {
  margin: 10px 0 5px;
  font-size: 1.3rem;
}

p {
  font-size: 1rem;
  opacity: 0.9;
}

/* Responsive */
@media (max-width: 768px) {
  .hero h1 {
    font-size: 2rem;
  }

  .hero p {
    font-size: 1rem;
  }

  .history {
    flex-direction: column;
    text-align: center;
  }

  .history img {
    max-width: 100%;
  }
}
</style>
