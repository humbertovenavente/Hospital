<template>
    <div class="admin-recetas">
      <router-link to="/admin/doctores-crear-recetas" class="btn-editar">Crear Receta</router-link>

      <h2>Administración de Recetas</h2>
      <table>
        <thead>
          <tr>
            <th>Código de receta</th>
            <th>Fecha</th>
            <th>Médico</th>
            <th>Observaciones</th>
            <th>Notas especiales</th>
            <th>Detalle medicamentos</th>
            <th>Estado</th>
            <th>Acciones</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="receta in recetas" :key="receta.idReceta">
            <td>{{ receta.codigoReceta }}</td>
            <td>{{ formatFecha(receta.fecha) }}</td>
            <td>{{ receta.idDoctor }}</td>
            <td>{{ receta.observaciones || "Sin observaciones" }}</td>
            <td>{{ receta.notasEspeciales || "Sin notas" }}</td>
            <td>
              <button @click="toggleMedicamentos(receta.idReceta)">📜 Ver Detalle</button>
              <div v-if="medicamentosVisibles[receta.idReceta]">
                <table class="medicamentos-table">
                  <thead>
                    <tr>
                      <th>Principio Activo</th>
                      <th>Concentración</th>
                      <th>Presentación</th>
                      <th>Forma Farmacéutica</th>
                      <th>Dosis</th>
                      <th>Frecuencia</th>
                      <th>Duración</th>
                      <th>Acciones</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr v-for="med in receta.detalleMedicamentos" :key="med.idMedicamento">
                      <td>{{ med.principioActivo }}</td>
                      <td>{{ med.concentracion }} mg</td>
                      <td>{{ med.presentacion }}</td>
                      <td>{{ med.formaFarmaceutica }}</td>
                      <td>{{ med.dosis }}</td>
                      <td>{{ med.frecuencia }} horas</td>
                      <td>{{ med.duracion }} días</td>
                      <td>
                        <button @click="editarMedicamento(med)">✏️ Editar</button>
                        <button @click="eliminarMedicamento(med.idMedicamento)" class="btn-eliminar">🗑 Eliminar</button>
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </td>
            <td>
              <span :class="estadoClase(receta.estado)">
                {{ receta.estado }}
              </span>
            </td>
            <td>
              <button @click="editarReceta(receta)" class="btn-editar">✏️ Editar</button>
              <button @click="eliminarReceta(receta.idReceta)" class="btn-eliminar">🗑 Eliminar</button>
            </td>
          </tr>
        </tbody>
      </table>

      <!-- Formulario de edición de receta -->
      <div v-if="recetaSeleccionada" class="modal">
        <div class="modal-content">
          <h3>Editar Receta</h3>
          <form @submit.prevent="actualizarReceta">
            <label>Diagnóstico:</label>
            <input v-model="recetaSeleccionada.diagnostico" required />

            <label>Estado:</label>
            <select v-model="recetaSeleccionada.estado">
              <option value="activa">Activa</option>
              <option value="completada">Completada</option>
              <option value="cancelada">Cancelada</option>
            </select>

            <button type="submit" class="btn-guardar">💾 Guardar Cambios</button>
            <button @click="cerrarModal" class="btn-cancelar">❌ Cancelar</button>
          </form>
        </div>
      </div>

      <!-- Formulario de edición de medicamento -->
      <div v-if="medicamentoSeleccionado" class="modal">
        <div class="modal-content">
          <h3>Editar Medicamento</h3>
          <form @submit.prevent="actualizarMedicamento">
            <label>Principio Activo:</label>
            <input v-model="medicamentoSeleccionado.principioActivo" required />

            <label>Concentración:</label>
            <input v-model="medicamentoSeleccionado.concentracion" required />

            <label>Presentación:</label>
            <input v-model="medicamentoSeleccionado.presentacion" required />

            <label>Forma Farmacéutica:</label>
            <input v-model="medicamentoSeleccionado.formaFarmaceutica" required />

            <label>Dosis:</label>
            <input v-model="medicamentoSeleccionado.dosis" required />

            <label>Frecuencia:</label>
            <input v-model="medicamentoSeleccionado.frecuencia" required />

            <label>Duración:</label>
            <input v-model="medicamentoSeleccionado.duracion" required />

            <button type="submit" class="btn-guardar">💾 Guardar Medicamento</button>
            <button @click="cerrarModalMedicamento" class="btn-cancelar">❌ Cancelar</button>
          </form>
        </div>
      </div>
    </div>
  </template>

<script lang="ts">
// URL de la API para QA
const API_URL = 'http://localhost:8080';

export default {
  data() {
    return {
      recetas: [],
      recetaSeleccionada: null,
      medicamentoSeleccionado: null,
      medicamentosVisibles: {} // Controla la visibilidad de los medicamentos por receta
    };
  },
  async created() {
    await this.cargarRecetas();
  },
  methods: {
    async cargarRecetas() {
      try {
        const response = await fetch(`${API_URL}/recetas`);
        if (!response.ok) throw new Error("Error al cargar recetas.");
        this.recetas = await response.json();
      } catch (error) {
        console.error(error);
        alert("Error al obtener las recetas.");
      }
    },
    toggleMedicamentos(idReceta) {
      this.$set(this.medicamentosVisibles, idReceta, !this.medicamentosVisibles[idReceta]);
    },
    editarReceta(receta) {
      this.recetaSeleccionada = { ...receta };
    },
    editarMedicamento(medicamento) {
      this.medicamentoSeleccionado = { ...medicamento };
    },
    async actualizarReceta() {
      try {
        const response = await fetch(`${API_URL}/recetas/editar/${this.recetaSeleccionada.idReceta}`, {
          method: "PUT",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(this.recetaSeleccionada)
        });

        if (!response.ok) throw new Error("Error al actualizar la receta.");
        alert("Receta actualizada correctamente.");
        this.recetaSeleccionada = null;
        await this.cargarRecetas();
      } catch (error) {
        console.error(error);
        alert("Error al actualizar la receta.");
      }
    },
    async actualizarMedicamento() {
      try {
        const response = await fetch(`${API_URL}/medicamentos/editar/${this.medicamentoSeleccionado.idMedicamento}`, {
          method: "PUT",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(this.medicamentoSeleccionado)
        });

        if (!response.ok) throw new Error("Error al actualizar el medicamento.");
        alert("Medicamento actualizado correctamente.");
        this.medicamentoSeleccionado = null;
        await this.cargarRecetas();
      } catch (error) {
        console.error(error);
        alert("Error al actualizar el medicamento.");
      }
    },
    async eliminarReceta(idReceta) {
      if (!confirm("¿Estás seguro de que quieres eliminar esta receta?")) return;

      try {
        const response = await fetch(`${API_URL}/recetas/eliminar/${idReceta}`, {
          method: "DELETE"
        });

        if (!response.ok) throw new Error("Error al eliminar la receta.");
        alert("Receta eliminada correctamente.");
        await this.cargarRecetas();
      } catch (error) {
        console.error(error);
        alert("Error al eliminar la receta.");
      }
    },
    cerrarModal() {
      this.recetaSeleccionada = null;
    },
    cerrarModalMedicamento() {
      this.medicamentoSeleccionado = null;
    }
  }
};
</script>


  <style scoped>
  .admin-recetas {
    padding: 20px;
  }
  table {
    width: 100%;
    border-collapse: collapse;
  }
  th, td {
    padding: 10px;
    border: 1px solid #ddd;
    text-align: center;
  }
  th {
    background-color: #333;
    color: white;
  }
  .btn-editar, .btn-eliminar {
    padding: 5px 10px;
    margin: 5px;
    cursor: pointer;
    border: none;
    border-radius: 4px;
  }
  .btn-editar {
    background-color: #007bff;
    color: white;
  }
  .btn-eliminar {
    background-color: #dc3545;
    color: white;
  }
  .modal {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.5);
    display: flex;
    justify-content: center;
    align-items: center;
  }
  .modal-content {
    background: white;
    padding: 20px;
    border-radius: 10px;
  }
  .btn-guardar {
    background-color: #28a745;
    color: white;
    padding: 8px 12px;
    border: none;
    cursor: pointer;
  }
  .btn-cancelar {
    background-color: #dc3545;
    color: white;
    padding: 8px 12px;
    border: none;
    cursor: pointer;
  }
  </style>
