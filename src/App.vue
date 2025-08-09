<script setup lang="ts">
import { RouterLink, RouterView, useRouter } from "vue-router";
import { userRole, isLoggedIn, logout } from "@/stores/authStore";
import { onMounted } from "vue";

const router = useRouter();

onMounted(() => {
  const storedRole = localStorage.getItem("userRole");
  const storedUser = localStorage.getItem("userId");

  if (storedRole && storedUser) {
    userRole.value = parseInt(storedRole);
    isLoggedIn.value = true;
  }
});

const myAccountRoute = () => {
  switch (userRole.value) {
    case 1:
      return "/my-account-admin";
    case 2:
      return "/my-account-doctor";
    case 3:
      return "/my-account-empleado";
    case 4:
      return "/my-account-paciente";
    default:
      return "/login";
  }
};
</script>

<template>
  <header class="navbar">
    <div class="navbar-container">
      <div class="brand">
        <img alt="Vue logo" class="logo" src="@/assets/logo.svg" width="50" height="50" />
        <h1>Hospital dos</h1>
      </div>

      <nav class="nav-links">
        <RouterLink to="/">Home</RouterLink>
        <RouterLink to="/subhome1">Home2</RouterLink>
        <RouterLink to="/subhome2">Home3</RouterLink>
        <RouterLink :to="{ name: 'dynamic-pages', params: { pageName: 'about' } }">About</RouterLink>
<RouterLink :to="{ name: 'dynamic-pages', params: { pageName: 'contact' } }">Contact Us</RouterLink>

        <RouterLink to="/historia">Historia</RouterLink>
        <RouterLink to="/faq">FAQ</RouterLink>
        <RouterLink to="/doctores">Doctores</RouterLink>
        <RouterLink v-if="isLoggedIn" :to="myAccountRoute()">Mi Cuenta</RouterLink>
        <RouterLink v-if="userRole === 1" to="/admin-portal">Gestionar</RouterLink>
        <RouterLink v-if="userRole === 1" to="/servicios-medicos">Servicios Médicos</RouterLink>
        <RouterLink to="/solicitud-hospital">Solicitar Convenio</RouterLink>
        <RouterLink to="/registrar-atencion">Registrar Atención</RouterLink>
        <RouterLink to="/consultar-historial">Consultar Historial</RouterLink>
        <RouterLink v-if="userRole === 1"to="/citas-aseguradora">Citas por ASeguradora</RouterLink>


        <RouterLink v-if="!isLoggedIn" to="/login">Log in</RouterLink>
        <button v-if="isLoggedIn" @click="logout(router)" class="logout-btn">Cerrar sesión</button>
      </nav>
    </div>
  </header>

  <main class="app-main">
    <RouterView />
  </main>

  <footer class="footer">
    <div class="footer-container">
      <p>&copy; {{ new Date().getFullYear() }} Hospitals. Todos los derechos reservados.</p>
    </div>
  </footer>
</template>

<style scoped>
.navbar {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  background-color: #ffffff;
  border-bottom: 2px solid #e0e0e0;
  z-index: 1000;
  padding: 10px 0;
  box-shadow: 0px 2px 10px rgba(0, 0, 0, 0.1);
}

.navbar-container {
  max-width: 1500px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
}

.brand {
  display: flex;
  align-items: center;
  gap: 10px;
}

.brand h1 {
  font-size: 24px;
  font-weight: bold;
  color: #2c3e50;
  margin: 0;
}

.nav-links {
  display: flex;
  align-items: center;
  gap: 15px;
}

.nav-links a {
  text-decoration: none;
  color: #2c3e50;
  font-weight: 500;
  padding: 8px 12px;
  transition: color 0.3s ease-in-out;
}

.nav-links a:hover {
  color: #42b983;
}

.logout-btn {
  background-color: #d9534f;
  color: white;
  border: none;
  padding: 8px 15px;
  cursor: pointer;
  border-radius: 8px;
  font-weight: 500;
  transition: background 0.3s;
}

.logout-btn:hover {
  background-color: #c9302c;
}

.app-main {
  margin-top: 80px;
  min-height: calc(100vh - 140px);
  background-color: ;
  padding: 20px;
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: flex-start;
}

.footer {
  background-color: #13678a;
  color: white;
  text-align: center;
  padding: 15px 0;
  width: 100%;
}

.footer-container {
  max-width: 1200px;
  margin: auto;
}
</style>
