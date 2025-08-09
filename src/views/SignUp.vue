<script setup>
import { ref } from "vue";
import { registerUser } from "@/services/authService.js";
import { useRouter } from "vue-router";

const router = useRouter();
const nombreUsuario = ref("");
const correo = ref("");
const contrasena = ref("");
const mensaje = ref("");
const errorMensaje = ref("");

const registrar = async () => {
  mensaje.value = "";
  errorMensaje.value = "";

  try {
    await registerUser(nombreUsuario.value, correo.value, contrasena.value);
    mensaje.value = "Registro exitoso. Revisa tu correo. Redirigiendo al login...";

    // Espera 2 segundos y redirige a la página de login
    setTimeout(() => {
      router.push("/login");
    }, 2000);
  } catch (error) {
    errorMensaje.value = "Error al registrar usuario. Inténtalo nuevamente.";
  }
};
</script>

<template>
  <form @submit.prevent="registrar">
    <h2>Registrarse</h2>
    <input v-model="nombreUsuario" placeholder="Nombre de usuario" required />
    <input v-model="correo" type="email" placeholder="Correo" required />
    <input v-model="contrasena" type="password" placeholder="Contraseña" required />
    <button type="submit">Registrarse</button>

    <p v-if="mensaje" class="success">{{ mensaje }}</p>
    <p v-if="errorMensaje" class="error">{{ errorMensaje }}</p>

    <p>
      Already have an account?
      <RouterLink to="/login">Click here</RouterLink>
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
  background-color: #28A745;
  color: white;
  cursor: pointer;
}

.success {
  color: green;
  font-weight: bold;
}

.error {
  color: red;
  font-weight: bold;
}
</style>
