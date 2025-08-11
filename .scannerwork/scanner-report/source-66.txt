<template>
  <div class="container">
    <div class="header">Módulo de Reportes y Analítica</div>
    <div class="filters">
      <input type="date" v-model="fechaInicio">
      <input type="date" v-model="fechaFin">
      <select v-model="doctor">
        <option value="">Todos los doctores</option>
        <option v-for="doc in doctores" :key="doc">{{ doc }}</option>
      </select>
      <select v-model="servicio">
        <option value="">Todos los servicios</option>
        <option v-for="srv in servicios" :key="srv">{{ srv }}</option>
      </select>
      <input type="text" v-model="paciente" placeholder="Nombre del paciente">
      <button class="btn-generate" @click="generarReporte">Generar Reporte</button>
      <button class="btn-download" @click="descargarPDF">Descargar PDF</button>
    </div>

    <div class="section">
    <table>
      <thead>
        <tr>
          <th>Fecha</th>
          <th>Doctor</th>
          <th>Servicio</th>
          <th>Paciente</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="reporte in reportesFiltrados" :key="reporte.id">
          <td>{{ reporte.fecha }}</td>
          <td>{{ reporte.doctor }}</td>
          <td>{{ reporte.servicio }}</td>
          <td>{{ reporte.paciente }}</td>
        </tr>
      </tbody>
    </table>
  </div>
</div>
</template>

<script>
export default {
  data() {
    return {
      fechaInicio: "",
      fechaFin: "",
      doctor: "",
      servicio: "",
      paciente: "",
      doctores: ["Dr. Pérez", "Dra. Gómez", "Dr. Sánchez"],
      servicios: ["Consulta General", "Rayos X", "Cirugía"],
      reportes: [
        { id: 1, fecha: "2025-03-01", doctor: "Dr. Pérez", servicio: "Consulta General", paciente: "Juan López" },
        { id: 2, fecha: "2025-03-02", doctor: "Dra. Gómez", servicio: "Rayos X", paciente: "Ana Martínez" },
        { id: 3, fecha: "2025-03-03", doctor: "Dr. Pérez", servicio: "Rayos X", paciente: "Carlos Ramírez" }
      ]
    };
  },
  computed: {
    reportesFiltrados() {
      return this.reportes.filter(r =>
        (!this.fechaInicio || r.fecha >= this.fechaInicio) &&
        (!this.fechaFin || r.fecha <= this.fechaFin) &&
        (!this.doctor || r.doctor === this.doctor) &&
        (!this.servicio || r.servicio === this.servicio) &&
        (!this.paciente || r.paciente.toLowerCase().includes(this.paciente.toLowerCase()))
      );
    }
  },
  methods: {
    generarReporte() {
      alert("Generando Reporte...");
    },
    descargarPDF() {
      alert("Descargando Reporte en PDF...");
    }
  }
};
</script>

<style scoped>
.container {
  padding: 20px;
  background: #f9f9f9;
  color: #e0e1dd;
  border-radius: 10px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
}

.header {
  text-align: center;
  font-size: 22px;
  font-weight: bold;
  background: #45C4B0;
  color: white;
  padding: 12px;
  border-radius: 5px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.4);
}


.section {
  border: 1px solid #45C4B0;
  padding: 15px;
  margin: 10px 0;
  background: #13678a;
  border-radius: 8px;
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

.btn-generate, .btn-download {
  padding: 5px 10px;
  border: none;
  cursor: pointer;
  border-radius: 4px;
}

.btn-generate {
  background-color: #DAFDBA;
  color: #012030;
}

.btn-download {
  background-color: #abff92;
  color: #012030;
}

.report-table {
  width: 100%;
  border-collapse: collapse;
  background: white;
}

th, td {
  padding: 12px;
  border: 1px solid #DAFDBA;
  text-align: center; /* Alinear al centro */
  vertical-align: middle;
}

th {
  background: #01324b;
}
</style>
