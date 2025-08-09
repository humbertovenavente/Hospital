<template>
  <div class="admin-recetas">
    <h2>Reporte de Medicinas Más Recetadas</h2>

    <form @submit.prevent="cargarReporte" class="formulario-fechas">
      <label>Fecha inicio:</label>
      <input type="date" v-model="inicio" required />

      <label>Fecha fin:</label>
      <input type="date" v-model="fin" required />

      <label>Límite (opcional):</label>
      <input type="number" v-model="limite" min="1" placeholder="Sin límite" />

      <button type="submit" class="btn-editar">Buscar</button>
      <button type="button" class="btn-excel" @click="descargarExcel">Descargar Excel</button>
    </form>

    <div v-if="error" class="error">{{ error }}</div>

    <div v-if="reporte.length" class="encabezado-reporte">
      <h3>Encabezado del Reporte</h3>
      <p><strong>Fecha de generación:</strong> {{ fechaGeneracion }}</p>
      <p><strong>Usuario:</strong> {{ usuario }}</p>
      <p><strong>Parámetros:</strong> Inicio: {{ inicio }} | Fin: {{ fin }} | Límite: {{ limite || 'Sin límite' }}</p>
    </div>

    <table v-if="reporte.length">
      <thead>
        <tr>
          <th>#</th>
          <th>Principio Activo</th>
          <th>Total Recetas</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="item in reporte" :key="item.popularidad">
          <td>{{ item.popularidad }}</td>
          <td>{{ item.principioActivo }}</td>
          <td>{{ item.totalRecetas }}</td>
        </tr>
      </tbody>
    </table>

    <div v-else class="mensaje-vacio">
      No hay datos para mostrar. Intenta cambiar el rango de fechas.
    </div>
  </div>
</template>

<script>
import API_URL from "@/config";

export default {
  data() {
    return {
      inicio: "",
      fin: "",
      limite: null,
      reporte: [],
      error: null,
      usuario: "",
      fechaGeneracion: "",
    };
  },
  methods: {
    async cargarReporte() {
      this.error = null;
      try {
        const url = new URL(`${API_URL}/reporte-medicinas`);
        url.searchParams.append("inicio", this.inicio);
        url.searchParams.append("fin", this.fin);
        if (this.limite) url.searchParams.append("limite", this.limite);

        const response = await fetch(url);
        if (!response.ok) throw new Error("No se pudo cargar el reporte");

        this.reporte = await response.json();
        this.usuario = localStorage.getItem("usuarioEmail") || "admin@hospital.com";
        this.fechaGeneracion = new Date().toLocaleString();
      } catch (err) {
        console.error(err);
        this.error = "Hubo un error al cargar el reporte.";
      }
    },

    async descargarExcel() {
      try {
        const usuario = localStorage.getItem("usuarioEmail") || "admin@hospital.com";

        const params = new URLSearchParams({
          inicio: this.inicio,
          fin: this.fin,
          limite: this.limite || 0,
          usuario,
        });

        const response = await fetch(`${API_URL}/reporte-medicinas/excel?${params.toString()}`);
        if (!response.ok) throw new Error("Error generando el Excel");

        const blob = await response.blob();
        const url = window.URL.createObjectURL(blob);

        const a = document.createElement("a");
        a.href = url;
        a.download = "medicinas_reporte.xlsx";
        a.click();
        window.URL.revokeObjectURL(url);
      } catch (err) {
        console.error(err);
        alert("Error al descargar el Excel");
      }
    },
  },
};
</script>

<style scoped>
.admin-recetas {
  padding: 20px;
  background-color: #f4fafd;
}

.formulario-fechas {
  display: flex;
  gap: 1rem;
  margin-bottom: 1.5rem;
  align-items: center;
  flex-wrap: wrap;
}

input,
button {
  padding: 8px;
  border-radius: 4px;
  border: 1px solid #ccc;
}

.btn-editar {
  background-color: #00ABBD;
  color: #fff;
  border: none;
  cursor: pointer;
}

.btn-editar:hover {
  background-color: #0099DD;
}

.btn-excel {
  background-color: #026E81;
  color: #fff;
  border: none;
  cursor: pointer;
}

.btn-excel:hover {
  background-color: #0099DD;
}

table {
  width: 100%;
  border-collapse: collapse;
  color: #026E81;
}

th,
td {
  padding: 10px;
  border: 1px solid #A1C7E0;
  text-align: center;
}

th {
  background-color: #36c4dd;
}

tr:nth-child(even) {
  background-color: #f6fdfd;
}

.encabezado-reporte {
  background-color: #A1C7E0;
  padding: 15px;
  border-radius: 6px;
  margin-bottom: 15px;
  color: #026E81;
  font-size: 14px;
}

.encabezado-reporte p {
  margin: 4px 0;
}

.mensaje-vacio {
  color: #555;
  text-align: center;
  margin-top: 20px;
}

.error {
  color: #FF9933;
  margin-bottom: 10px;
  font-weight: bold;
}
</style>
