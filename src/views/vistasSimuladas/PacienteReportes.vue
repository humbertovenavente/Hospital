<template>
  <div class="container">
    <h2>Mis Reportes Médicos</h2>
    <div class="filters">
      <input type="date" v-model="fechaInicio">
      <input type="date" v-model="fechaFin">
      <select v-model="servicio">
        <option value="">Todos los servicios</option>
        <option v-for="srv in servicios" :key="srv">{{ srv }}</option>
      </select>
      <button class="btn-filter" @click="filtrarReportes">Filtrar</button>
    </div>

    <table class="report-table">
      <thead>
        <tr>
          <th>Fecha</th>
          <th>Servicio</th>
          <th>Doctor</th>
          <th>Detalle</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="reporte in reportesFiltrados" :key="reporte.id">
          <td>{{ reporte.fecha }}</td>
          <td>{{ reporte.servicio }}</td>
          <td>{{ reporte.doctor }}</td>
          <td><button class="btn-detail">Ver Detalle</button></td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
export default {
  data() {
    return {
      fechaInicio: "",
      fechaFin: "",
      servicio: "",
      servicios: ["Consulta General", "Rayos X", "Cirugía"],
      reportes: [
        { id: 1, fecha: "2025-03-01", servicio: "Consulta General", doctor: "Dr. Pérez" },
        { id: 2, fecha: "2025-03-02", servicio: "Rayos X", doctor: "Dra. Gómez" },
        { id: 3, fecha: "2025-03-03", servicio: "Cirugía", doctor: "Dr. Sánchez" }
      ]
    };
  },
  computed: {
    reportesFiltrados() {
      return this.reportes.filter(r =>
        (!this.fechaInicio || r.fecha >= this.fechaInicio) &&
        (!this.fechaFin || r.fecha <= this.fechaFin) &&
        (!this.servicio || r.servicio === this.servicio)
      );
    }
  },
  methods: {
    filtrarReportes() {
      alert("Filtrando reportes...");
    }
  }
};
</script>

<style scoped>
.container {
  width: 90%;
  margin: auto;
  padding: 20px;
  background-color: #f3f4f6;
  border-radius: 8px;
}

h2 {
  text-align: center;
  color: #2c3e50;
}

.filters {
  display: flex;
  gap: 10px;
  margin-bottom: 15px;
}

input, select {
  padding: 5px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.btn-filter, .btn-detail {
  padding: 5px 10px;
  border: none;
  cursor: pointer;
  border-radius: 4px;
}

.btn-filter {
  background-color: #1565c0;
  color: white;
}

.btn-detail {
  background-color: #27ae60;
  color: white;
}

.report-table {
  width: 100%;
  border-collapse: collapse;
  background: white;
}

th, td {
  padding: 12px;
  text-align: left;
  border-bottom: 1px solid #ddd;
}

th {
  background-color: #34495e;
  color: white;
}
</style>
