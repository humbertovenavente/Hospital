<template>
  <form @submit.prevent="register">
    <input v-model="nombre" placeholder="Nombre" required />
    <input v-model="correo" placeholder="Correo" type="email" required />
    <input v-model="contrasena" type="password" placeholder="Contraseña" required />
    <button type="submit">Sign up</button>
    <p v-if="mensaje">{{ mensaje }}</p>
  </form>
</template>

<script setup>
import { ref } from "vue";

const nombre = ref("");
const correo = ref("");
const contrasena = ref("");
const mensaje = ref("");

const register = () => {
  mensaje.value = "Registro enviado (Falta conectar con backend)";
};
</script>

<style scoped>
form {
  display: flex;
  flex-direction: column;
  gap: 10px;
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
