<template>
  <div class="report-container">
    <h1>Generar Reporte de Consultas por Doctor</h1>
    <div class="card form-card">
      <form @submit.prevent="onGenerarReporte">
        <div class="form-group">
          <label for="doctor">Doctor:</label>
          <select v-model.number="reporteRequest.idDoctor" id="doctor" required>
            <option value="" disabled>Seleccione doctor</option>
            <!-- La lista se obtiene de forma dinámica -->
            <option v-for="doctor in doctores" :key="doctor.idDoctor" :value="doctor.idDoctor">
              <!-- Muestra el nombre del doctor si existe, o su nombre/apellido -->
              {{ doctor.usuario && doctor.usuario.nombreUsuario
                  ? doctor.usuario.nombreUsuario
                  : (doctor.nombre || doctor.apellido) }}
            </option>
          </select>
        </div>
        <div class="form-group">
          <label for="fechaInicio">Fecha Inicio:</label>
          <input type="date" id="fechaInicio" v-model="reporteRequest.fechaInicio" required />
        </div>
        <div class="form-group">
          <label for="fechaFin">Fecha Fin:</label>
          <input type="date" id="fechaFin" v-model="reporteRequest.fechaFin" required />
        </div>
        <div class="form-group radio-group">
          <label>Tipo de Reporte:</label>
          <div class="radio-options">
            <label>
              <input type="radio" value="AGRUPADO" v-model="reporteRequest.tipoReporte" /> Agrupado por día
            </label>
            <label>
              <input type="radio" value="DETALLADO" v-model="reporteRequest.tipoReporte" /> Listado individual
            </label>
          </div>
        </div>
        <div class="button-group">
          <button type="submit" class="btn-primary">Generar Reporte</button>
          <button type="button" class="btn-secondary" @click="descargarExcel">Descargar Excel</button>
        </div>
      </form>
    </div>

    <!-- Se muestra el reporte solo si hay respuesta -->
    <div v-if="reporteResponse" class="card report-card">
      <div class="report-header">
        <h2>Encabezado</h2>
        <div class="header-text">
          <p v-for="(line, index) in headerLines" :key="index">{{ line }}</p>
        </div>
      </div>
      <div class="report-data">
        <h2>Datos del Reporte</h2>
        <div v-if="reporteResponse.datos && reporteResponse.datos.length">
          <table class="styled-table">
            <thead>
              <tr>
                <th v-for="(col, index) in tableColumns" :key="index">{{ col }}</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(dato, index) in reporteResponse.datos" :key="index">
                <td v-for="(value, key, i) in dato" :key="i">{{ value }}</td>
              </tr>
            </tbody>
          </table>
        </div>
        <div v-else class="no-data">
          <p>No se encontraron datos para los parámetros seleccionados.</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, reactive, onMounted, watch } from 'vue';
import axios from 'axios';
import { generarReporte } from '@/services/ReporteService';
import API_URL from '@/config';

export default {
  name: 'ReporteView',
  setup() {
    // 1) Lista de doctores
    const doctores = ref([]);
    const getDoctores = async () => {
      try {
        const response = await axios.get(`${API_URL}/doctor`);
        doctores.value = response.data;
      } catch (error) {
        console.error('Error al obtener los doctores', error);
      }
    };

    // 2) Parámetros para el reporte (ahora con usuario)
    const reporteRequest = reactive({
      idDoctor: null,
      fechaInicio: '',
      fechaFin: '',
      tipoReporte: 'AGRUPADO',
     usuario: ''
    });

    // 3) Respuesta del servidor
    const reporteResponse = ref(null);

    // 4) Columnas de la tabla
    const tableColumns = ref([]);
    watch(() => reporteResponse.value, (nuevoReporte) => {
      if (nuevoReporte?.datos?.length) {
        tableColumns.value = Object.keys(nuevoReporte.datos[0]);
      } else {
        tableColumns.value = [];
      }
    });

    // 5) Líneas del encabezado
    const headerLines = ref([]);
    watch(() => reporteResponse.value, (nuevoReporte) => {
      headerLines.value = nuevoReporte?.encabezado
        ? nuevoReporte.encabezado.split('\n')
        : [];
    });

    // 6) Obtener el usuario actual y asignarlo a reporteRequest.usuario
    const obtenerUsuarioActual = async () => {
      try {
        // Supongamos que guardas el ID en localStorage
        const userId = localStorage.getItem('userId');
        const { data } = await axios.get(`${API_URL}/usuarios/${userId}`);
        reporteRequest.usuario = data.nombreUsuario;
      } catch (e) {
        console.warn('No se pudo obtener usuario, quedará Anónimo', e);
        reporteRequest.usuario = '[Anónimo]';
      }
    };

    onMounted(() => {
      getDoctores();
      obtenerUsuarioActual();
    });

    // 7) Generar reporte (envía usuario junto al resto de parámetros)
    const onGenerarReporte = async () => {
      try {
        console.log('Enviando payload:', JSON.stringify(reporteRequest));
        const resultado = await generarReporte(reporteRequest);
        reporteResponse.value = resultado;
      } catch (error) {
        console.error('Error al generar el reporte', error);
      }
    };

    // 8) Descargar Excel (igual que antes)
    const descargarExcel = () => {
      const params = new URLSearchParams({
        idDoctor: reporteRequest.idDoctor,
        fechaInicio: reporteRequest.fechaInicio,
        fechaFin: reporteRequest.fechaFin,
        tipoReporte: reporteRequest.tipoReporte,
        usuario:     reporteRequest.usuario
      }).toString();
      window.location.href = `${API_URL}/api/reportes/consultas/excel?${params}`;
    };

    return {
      doctores,
      reporteRequest,
      reporteResponse,
      tableColumns,
      headerLines,
      onGenerarReporte,
      descargarExcel
    };
  }
};
</script>



<style scoped>
.report-container {
  max-width: 900px;
  margin: 2rem auto;
  padding: 2rem;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  color: #333;
}

h1 {
  text-align: center;
  margin-bottom: 1.5rem;
}

h2 {
  margin-top: 0;
  color: #444;
}

.card {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  padding: 1.5rem;
  margin-bottom: 2rem;
}

.form-card {
  margin-bottom: 2.5rem;
}

.form-group {
  margin-bottom: 1.25rem;
  display: flex;
  flex-direction: column;
}

.form-group label {
  font-weight: bold;
  margin-bottom: 0.5rem;
}

.form-group input,
.form-group select {
  padding: 0.5rem;
  font-size: 1rem;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.radio-group {
  display: flex;
  flex-direction: column;
}

.radio-options {
  display: flex;
  gap: 1.5rem;
  margin-top: 0.5rem;
}

.button-group {
  display: flex;
  gap: 1rem;
  margin-top: 1rem;
}

.btn-primary {
  background-color: #007bff;
  color: #fff;
  border: none;
  padding: 0.75rem 1.25rem;
  font-size: 1rem;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s ease;
  align-self: flex-start;
}

.btn-primary:hover {
  background-color: #0056b3;
}

.btn-secondary {
  background-color: #6c757d;
  color: #fff;
  border: none;
  padding: 0.75rem 1.25rem;
  font-size: 1rem;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s ease;
  align-self: flex-start;
}

.btn-secondary:hover {
  background-color: #5a6268;
}

.report-header {
  margin-bottom: 1rem;
  border-bottom: 2px solid #ddd;
  padding-bottom: 1rem;
}

.header-text p {
  margin: 0.25rem 0;
  font-family: monospace;
  color: #555;
}

.styled-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 1rem;
}

.styled-table th,
.styled-table td {
  border: 1px solid #ddd;
  padding: 0.75rem;
  text-align: left;
}

.styled-table th {
  background-color: #007bff;
  color: #fff;
}

.styled-table tr:nth-child(even) {
  background-color: #f2f2f2;
}

@media (max-width: 600px) {
  .radio-options {
    flex-direction: column;
    gap: 0.5rem;
  }
  .form-group input,
  .form-group select {
    font-size: 0.95rem;
  }
}
</style>
