<template>
  <div class="schedule-portal">
    <h2>Citas por Aseguradora</h2>

    <table class="table">
      <thead>
        <tr>
          <th>ID Cita</th>
          <th>Paciente Aseguradora</th>
          <th>Aseguradora</th>
          <th>Motivo</th>
          <th>Fecha</th>
          <th>Hora Inicio</th>
          <th>Hora Fin</th>
          <th>Estado</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="cita in citasConfirmadas" :key="cita.idCita">
          <td>{{ cita.idCita }}</td>
          <td>
            {{ cita.paciente?.usuario?.nombreUsuario || "Sin nombre" }}
            {{ cita.paciente?.apellido || "" }}
          </td>
          <td>{{ cita.aseguradora?.nombre || "Sin aseguradora" }}</td>
          <td>{{ cita.motivo }}</td>
          <td>{{ cita.fecha }}</td>
          <td>{{ cita.horaInicio }}</td>
          <td>{{ cita.horaFin }}</td>
          <td>{{ cita.estado }}</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
import axios from "axios";
import API_URL from "@/config";

export default {
  data() {
    return {
      citas: [],
    };
  },
  computed: {
    citasConfirmadas() {
      return this.citas.filter((c) => c.estado === "CONFIRMADA");
    },
  },
  mounted() {
    this.obtenerCitas();
  },
  methods: {
    async obtenerCitas() {
      try {
        const response = await axios.get(`${API_URL}/citas`);
        this.citas = response.data;
      } catch (error) {
        console.error("❌ Error al obtener citas:", error);
      }
    },
  },
};
</script>

<style scoped>
.schedule-portal {
  padding: 2rem;
  background-color: #f9f9f9;
  border-radius: 12px;
}

h2 {
  text-align: center;
  margin-bottom: 1rem;
  color: #13678a;
}

.table {
  width: 100%;
  border-collapse: collapse;
  background-color: #fff;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.2);
}

th,
td {
  padding: 0.75rem;
  text-align: left;
  border-bottom: 1px solid #ccc;
}

th {
  background-color: #259c8a;
  color: white;
}

tbody tr:hover {
  background-color: #f0f0f0;
}
</style>
