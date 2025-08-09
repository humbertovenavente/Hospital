<template>
  <div class="container">
    <div class="header">📦 Control de Medicamentos</div>

    <!-- Formulario para crear o editar medicamento -->
    <div class="section">
      <form @submit.prevent="saveMedicamento">
        <div class="form-group">
          <label for="principioActivo">Principio Activo:</label>
          <input
            id="principioActivo"
            v-model="medicamento.principioActivo"
            required
          />
        </div>
        <div class="form-group">
          <label for="concentracion">Concentración:</label>
          <input id="concentracion" v-model="medicamento.concentracion" required />
        </div>
        <div class="form-group">
          <label for="presentacion">Presentación:</label>
          <input id="presentacion" v-model="medicamento.presentacion" required />
        </div>
        <div class="form-group">
          <label for="formaFarmaceutica">Forma Farmacéutica:</label>
          <input id="formaFarmaceutica" v-model="medicamento.formaFarmaceutica" required />
        </div>
        <div class="form-group">
          <label for="ventaLibre">Venta Libre (0 = requiere receta, 1 = venta libre):</label>
          <input
            id="ventaLibre"
            type="number"
            v-model.number="medicamento.ventaLibre"
            min="0"
            max="1"
            required
          />
        </div>
        <div class="form-actions">
          <button type="submit" class="btn-update">
            {{ isEditing ? "Actualizar" : "Crear" }}
          </button>
          <button v-if="isEditing" type="button" @click="resetForm">
            Cancelar
          </button>
        </div>
      </form>
    </div>

    <hr />

    <!-- Tabla de medicamentos -->
    <div class="section">
      <table>
        <thead>
          <tr>
            <th>Principio Activo</th>
            <th>Concentración</th>
            <th>Presentación</th>
            <th>Forma Farmacéutica</th>
            <th>Venta Libre</th>
            <th>Acciones</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="med in medicamentos" :key="med.idMedicamento">
            <td>{{ med.principioActivo }}</td>
            <td>{{ med.concentracion }}</td>
            <td>{{ med.presentacion }}</td>
            <td>{{ med.formaFarmaceutica }}</td>
            <td>{{ med.ventaLibre === 1 ? "Venta Libre" : "Requiere receta" }}</td>
            <td>
              <button @click="editMedicamento(med)">Editar</button>
              <button @click="deleteMedicamento(med.idMedicamento)">Eliminar</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

  </div>
</template>

<script>
import MedicamentoService from '@/services/MedicamentoService';

export default {
  name: 'MedicamentoCrud',
  data() {
    return {
      medicamentos: [],
      medicamento: {
        idMedicamento: null,
        principioActivo: '',
        concentracion: '',
        presentacion: '',
        formaFarmaceutica: '',
        ventaLibre: 0,
      },
      isEditing: false,
    };
  },
  methods: {
    fetchMedicamentos() {
      MedicamentoService.getAll()
        .then(response => {
          this.medicamentos = response.data;
        })
        .catch(error => {
          console.error('Error al obtener medicamentos:', error);
          alert("Error al cargar los medicamentos");
        });
    },
    saveMedicamento() {
      if (this.isEditing) {
        MedicamentoService.update(this.medicamento.idMedicamento, this.medicamento)
          .then(() => {
            this.fetchMedicamentos();
            this.resetForm();
            alert("Medicamento actualizado exitosamente");
          })
          .catch(error => {
            console.error('Error al actualizar medicamento:', error);
            alert("Error al actualizar el medicamento");
          });
      } else {
        MedicamentoService.create(this.medicamento)
          .then(() => {
            this.fetchMedicamentos();
            this.resetForm();
            alert("Medicamento creado exitosamente");
          })
          .catch(error => {
            console.error('Error al crear medicamento:', error);
            alert("Error al crear el medicamento");
          });
      }
    },
    deleteMedicamento(id) {
      MedicamentoService.delete(id)
        .then(() => {
          this.fetchMedicamentos();
          alert("Medicamento eliminado correctamente");
        })
        .catch(error => {
          console.error('Error al eliminar medicamento:', error);
          alert("Error al eliminar el medicamento");
        });
    },
    editMedicamento(med) {
      this.medicamento = { ...med };
      this.isEditing = true;
    },
    resetForm() {
      this.medicamento = {
        idMedicamento: null,
        principioActivo: '',
        concentracion: '',
        presentacion: '',
        formaFarmaceutica: '',
        ventaLibre: 0,
      };
      this.isEditing = false;
    }
  },
  mounted() {
    this.fetchMedicamentos();
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

form {
  margin-bottom: 15px;
}

.form-group {
  margin-bottom: 10px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
}

.form-group input {
  width: 100%;
  padding: 8px;
  box-sizing: border-box;
  border: 1px solid #DAFDBA;
  border-radius: 4px;
}

.form-actions {
  margin-top: 10px;
}

.btn-update {
  padding: 10px 15px;
  background-color: #DAFDBA;
  color: #01324b;
  border: none;
  cursor: pointer;
  border-radius: 5px;
}

.btn-update:hover {
  background-color: #2e7d32;
}

table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 15px;
}

th,
td {
  padding: 12px;
  border: 1px solid #DAFDBA;
  text-align: center;
  vertical-align: middle;
}

th {
  background: #01324b;
}
</style>
