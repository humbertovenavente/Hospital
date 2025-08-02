<script setup>
import { ref } from "vue";
import { loginUser } from "@/services/authService.js";
import { useRouter } from "vue-router";
import { setUser } from "@/stores/authStore"; // Estado global

const router = useRouter();
const correo = ref("");
const contrasena = ref("");
const errorMensaje = ref("");

const login = async () => {
  errorMensaje.value = "";

  try {
    const { id, roleId } = await loginUser(correo.value, contrasena.value);

    if (id && roleId) {
      setUser(id, roleId, router);

      // ✅ Guarda el email del usuario en localStorage
      localStorage.setItem("usuarioEmail", correo.value);
    } else {
      errorMensaje.value = "Error en los datos del usuario.";
    }
  } catch (error) {
    errorMensaje.value = "Error al iniciar sesión. Inténtalo nuevamente.";
  }
};

</script>

<template>
  <form @submit.prevent="login">
    <h2>Iniciar sesión</h2>
    <input v-model="correo" type="email" placeholder="Correo" required />
    <input v-model="contrasena" type="password" placeholder="Contraseña" required />
    <button type="submit">Iniciar sesión</button>

    <p v-if="errorMensaje" class="error">{{ errorMensaje }}</p>
    <p>
      Don't have an account? <RouterLink to="/signup">CLICK HERE</RouterLink>
    </p>
  </form>
</template>

<style scoped>
form {
  display: flex;
  flex-direction: column;
  gap: 10px;
  text-align: center;
}

input, button {
  padding: 10px;
  border-radius: 5px;
  border: 1px solid #ddd;
}

button {
  background-color: #007BFF;
  color: white;
  cursor: pointer;
}
</style>
