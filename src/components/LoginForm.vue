<template>
  <form @submit.prevent="login">
    <h2>Iniciar Sesión</h2>
    <input v-model="correo" placeholder="Correo" type="email" required />
    <input v-model="contrasena" type="password" placeholder="Contraseña" required />
    <button type="submit" :disabled="loading">
      {{ loading ? 'Iniciando sesión...' : 'Iniciar sesión' }}
    </button>

    <p v-if="mensaje" class="error">{{ mensaje }}</p>

    <!-- 🔹 Enlace para registrarse con colores personalizados -->
    <p class="register-link">
      <span class="text">¿No tienes una cuenta?</span>
      <RouterLink to="/signup" class="link">Regístrate aquí</RouterLink>
    </p>
  </form>
</template>

<script setup lang="ts">
import { ref } from "vue";
import { useRouter } from "vue-router";
import { loginUser } from "../services/authService";
import { setUser } from "../stores/authStore";

const router = useRouter();
const correo = ref("admin@hospital.com");
const contrasena = ref("");
const mensaje = ref("");
const loading = ref(false);

const login = async () => {
  if (!correo.value || !contrasena.value) {
    mensaje.value = "Por favor, completa todos los campos";
    return;
  }

  loading.value = true;
  mensaje.value = "";

  try {
    const { id, roleId } = await loginUser(correo.value, contrasena.value);
    setUser(id, roleId, router);
  } catch (error: any) {
    mensaje.value = error.message || "Error al iniciar sesión";
  } finally {
    loading.value = false;
  }
};
</script>

<style scoped>
/* 🔹 Estilo del formulario */
form {
  display: flex;
  flex-direction: column;
  gap: 10px;
  text-align: center;
}

/* 🔹 Estilos para los inputs y botones */
input, button {
  padding: 10px;
  border-radius: 5px;
  border: 1px solid #ddd;
}

button {
  background-color: #28A745;
  color: white;
  cursor: pointer;
}

/* 🔹 Estilo para el mensaje de error */
.error {
  color: red;
  font-weight: bold;
}

/* 🔹 Estilo para el texto de "¿No tienes una cuenta?" */
.register-link {
  margin-top: 15px;
  font-size: 14px;
}

/* 🔹 Color rojo/naranja para "¿No tienes una cuenta?" */
.register-link .text {
  color: #ff5733; /* Naranja/Rojo */
  font-weight: bold;
}

/* 🔹 Color azul para "Regístrate aquí" */
.register-link .link {
  color: #007BFF; /* Azul */
  text-decoration: none;
  font-weight: bold;
}

/* 🔹 Efecto hover para el enlace */
.register-link .link:hover {
  text-decoration: underline;
  color: #0056b3; /* Azul más oscuro al pasar el mouse */
}
</style>
