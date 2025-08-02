<template>
  <div class="contact-container">
    <!-- Hero Section -->
    <div class="hero">
      <div class="overlay">
        <h1>Contacto</h1>
        <p>Estamos aquí para ayudarte. Contáctanos para cualquier consulta médica o información adicional.</p>
      </div>
    </div>



    <!-- Sección de Contacto -->
    <div class="contact-section">
      <h2>Información de Contacto</h2>
      <p>Puedes comunicarte con nosotros a través de los siguientes medios:</p>

      <div class="contact-details">
        <div class="contact-item">
          <i class="fas fa-phone"></i>
          <p><strong>Teléfono:</strong> +502 1234 5678</p>
        </div>
        <div class="contact-item">
          <i class="fas fa-envelope"></i>
          <p><strong>Correo:</strong> contacto@hospitalgt.com</p>
        </div>
        <div class="contact-item">
          <i class="fas fa-map-marker-alt"></i>
          <p><strong>Dirección:</strong> 7a. Avenida 9-20, Zona 11, Guatemala</p>
        </div>
      </div>
    </div>


  </div>
</template>


<style scoped>
/* Estilos Generales */
.contact-container {
  color: white;
  background: #0d1b2a;
  padding: 0;
  margin: 0;
}

/* Hero Section */
.hero {
  background: url("https://drcastelazooncologia.com/wp-content/uploads/2020/10/proctologo-oncologo-en-cdmx.jpg") no-repeat center center/cover;
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

/* Información de Contacto */
.contact-info {
  padding: 50px 20px;
  max-width: 1000px;
  margin: 40px auto;
  background: #1b263b;
  border-radius: 10px;
  box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.2);
  text-align: center;
}

.contact-info h2 {
  font-size: 2rem;
  margin-bottom: 15px;
}

.contact-info p {
  font-size: 1.2rem;
  opacity: 0.9;
  line-height: 1.6;
}

/* Estilos para el mapa */
.map-container {
  margin-top: 20px;
  border-radius: 10px;
  overflow: hidden;
}

/* Sección de Información de Contacto */
.contact-section {
  text-align: center;
  padding: 50px 20px;
  max-width: 800px;
  margin: 40px auto;
  background: #1b263b;
  border-radius: 10px;
  box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.2);
}

.contact-details {
  display: flex;
  flex-direction: column;
  gap: 20px;
  margin-top: 20px;
}

.contact-item {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  font-size: 1.2rem;
}

.contact-item i {
  font-size: 1.5rem;
  color: #00a6fb;
}

/* Formulario de Contacto */
.contact-form {
  text-align: center;
  padding: 50px 20px;
  max-width: 600px;
  margin: 40px auto;
  background: #1b263b;
  border-radius: 10px;
  box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.2);
}

.contact-form h2 {
  font-size: 2rem;
  margin-bottom: 15px;
}

.contact-form form {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.contact-form input,
.contact-form textarea {
  padding: 12px;
  border-radius: 8px;
  border: none;
  font-size: 1rem;
  width: 100%;
}

.contact-form textarea {
  resize: none;
  height: 120px;
}

.contact-form button {
  background: #00a6fb;
  color: white;
  border: none;
  padding: 12px;
  border-radius: 8px;
  font-size: 1.2rem;
  cursor: pointer;
  transition: 0.3s;
}

.contact-form button:hover {
  background: #0061a8;
}

/* Responsive */
@media (max-width: 768px) {
  .hero h1 {
    font-size: 3rem;
  }

  .hero p {
    font-size: 2rem;
  }
}
</style>
