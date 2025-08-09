<template>
  <div class="admin-portal">
    <h1>Servicios médicos</h1>

    <!-- Menú de navegación con botones -->
    <div class="gestion-menu">
      <!--  <button @click="router.push('/recetas')" class="gestion-button">Recetas médicas</button>  -->
      <button @click="router.push('/admin/doctores-citas')" class="gestion-button">Citas médicas</button>
      <button @click="router.push('/admin/doctores-agenda')" class="gestion-button">Agenda médica</button>
      <button @click="router.push('/historial-pagos-pacientes')" class="gestion-button">Historial de pagos del paciente</button>
      <button @click="router.push('/reportes-pacientes')" class="gestion-button">Reportes del paciente</button>
    </div>

    <!-- Vista dinámica según la ruta -->
    <router-view />
  </div>
</template>

<script lang="ts" setup>
import { useRouter } from "vue-router";

const router = useRouter(); //
</script>

<style scoped>
.faq-button, .gestion-button {
  background-color: #007bff;
  color: white;
  border: none;
  padding: 10px 15px;
  font-size: 16px;
  border-radius: 5px;
  cursor: pointer;
  margin-top: 10px;
  display: block;
  width: 100%;
  text-align: center;
}

.faq-button:hover, .gestion-button:hover {
  background-color: #0056b3;
}

.admin-portal {
  padding: 20px;
}

.gestion-menu {
  display: flex;
  flex-direction: column;
  gap: 10px;
  max-width: 300px;
  margin-bottom: 20px;
}
</style>
