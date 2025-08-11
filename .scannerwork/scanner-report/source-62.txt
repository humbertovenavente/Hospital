<template>
  <div class="formulario-receta">
    <h2>{{ modoEdicion ? 'Editar' : 'Crear' }} Receta Médica</h2>

    <form @submit.prevent="enviarReceta">
      <!-- Seleccionar Hospital -->
      <div class="form-group">
        <label>Hospital</label>
        <select v-model="receta.idHospital" required>
          <option v-for="hospital in hospitales" :key="hospital.id" :value="hospital.id">
            {{ hospital.nombre }} (Código: {{ hospital.codigo }})
          </option>
        </select>
      </div>

      <!-- Checkbox para seguro -->
      <div class="form-group">
        <label>¿Tiene seguro?</label>
        <input type="checkbox" v-model="tieneSeguro" />
      </div>

      <!-- Seleccionar Seguro (si aplica) -->
      <div class="form-group" v-if="tieneSeguro">
        <label>Seguro</label>
        <select v-model="receta.idSeguro">
          <option v-for="seguro in seguros" :key="seguro.id" :value="seguro.id">
            {{ seguro.nombre }} (Código: {{ seguro.codigo }})
          </option>
        </select>
      </div>

      <!-- DPI del paciente -->
      <div class="form-group">
        <label>DPI del Paciente</label>
        <input v-model="receta.idPaciente" placeholder="DPI del Paciente" required />
      </div>

      <!-- Código de Receta (readonly) -->
      <div class="form-group">
        <label>Código de la Receta</label>
        <input v-model="receta.codigoReceta" readonly />
      </div>

      <!-- Fecha de la receta -->
      <div class="form-group">
        <label>Fecha</label>
        <input v-model="receta.fecha" type="date" required />
      </div>

      <!-- Médico -->
      <div class="form-group">
        <label>ID del Médico</label>
        <input v-model="receta.idDoctor" required />
      </div>

      <!-- Observaciones / Notas -->
      <div class="form-group">
        <label>Observaciones</label>
        <textarea v-model="receta.observaciones"></textarea>
      </div>

      <div class="form-group">
        <label>Notas Especiales</label>
        <textarea v-model="receta.notasEspeciales"></textarea>
      </div>

      <!-- Lista de Medicamentos -->
      <h3>Medicamentos</h3>
      <div v-for="(med, index) in receta.detalleMedicamentos" :key="index" class="medicamento-item">
        <input v-model="med.idMedicamento" placeholder="ID Medicamento" required />
        <input v-model="med.principioActivo" placeholder="Principio Activo" required />
        <input v-model="med.concentracion" placeholder="Concentración (mg)" required />
        <input v-model="med.presentacion" placeholder="Presentación" required />
        <input v-model="med.formaFarmaceutica" placeholder="Forma Farmacéutica" required />
        <input v-model="med.dosis" placeholder="Dosis" required />
        <input v-model="med.frecuencia" placeholder="Frecuencia (cada X horas)" required />
        <input v-model.number="med.duracion" placeholder="Duración (días)" required />
        <button @click.prevent="eliminarMedicamento(index)">❌</button>
      </div>

      <!-- Botones -->
      <button @click.prevent="agregarMedicamento">➕ Agregar Medicamento</button>
      <button type="submit">💾 {{ modoEdicion ? 'Actualizar' : 'Crear' }} Receta</button>
    </form>
  </div>
</template>

<script>
import API_URL from '@/config';

export default {
  data() {
    return {
      hospitales: [],
      seguros: [],
      tieneSeguro: false,
      receta: {
        idHospital: '',
        idSeguro: '',
        idPaciente: '',
        codigoReceta: '',
        fecha: new Date().toISOString().slice(0, 10),
        idDoctor: '',
        observaciones: '',
        notasEspeciales: '',
        detalleMedicamentos: []
      }
    };
  },
  watch: {
    receta: {
      handler() {
        this.generarCodigoReceta();
      },
      deep: true
    },
    tieneSeguro() {
      if (!this.tieneSeguro) {
        this.receta.idSeguro = '';
      }
      this.generarCodigoReceta();
    }
  },
  async created() {
    await this.cargarRecetas();
    await this.cargarMedicamentos();
  },
  methods: {
    async cargarRecetas() {
      try {
        const response = await fetch(`${API_URL}/recetas`);
        const recetas = await response.json();
        this.hospitales = recetas.map(r => ({ id: r.idHospital, codigo: `H-${r.idHospital}` }));
        this.seguros = recetas.map(r => ({ id: r.idSeguro, codigo: `S-${r.idSeguro}` }));
      } catch (error) {
        console.error('Error cargando recetas:', error);
      }
    },
    async cargarMedicamentos() {
      try {
        const response = await fetch(`${API_URL}/medicamentos`);
        this.medicamentos = await response.json();
      } catch (error) {
        console.error('Error cargando medicamentos:', error);
      }
    },
    generarCodigoReceta() {
      if (!this.receta.idHospital || !this.receta.idPaciente) {
        this.receta.codigoReceta = '';
        return;
      }

      const hospital = this.hospitales.find(h => h.id === this.receta.idHospital);
      const seguro = this.seguros.find(s => s.id === this.receta.idSeguro);

      let codigo = hospital ? hospital.codigo : '';
      if (this.tieneSeguro && seguro) {
        codigo += `-${seguro.codigo}`;
      }
      codigo += `-${this.receta.idPaciente}`;
      this.receta.codigoReceta = codigo;
    },
    agregarMedicamento() {
      this.receta.detalleMedicamentos.push({
        idMedicamento: '',
        principioActivo: '',
        concentracion: '',
        presentacion: '',
        formaFarmaceutica: '',
        dosis: '',
        frecuencia: '',
        duracion: ''
      });
    },
    eliminarMedicamento(index) {
      this.receta.detalleMedicamentos.splice(index, 1);
    },
    async enviarReceta() {
      try {
        const metodo = this.modoEdicion ? 'PUT' : 'POST';
        const url = this.modoEdicion
          ? `${API_URL}/recetas/editar/${this.receta.idReceta}`
          : `${API_URL}/recetas/crear`;

        const response = await fetch(url, {
          method: metodo,
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(this.receta)
        });

        if (!response.ok) {
          throw new Error('Error en la solicitud');
        }

        alert(`Receta ${this.modoEdicion ? 'actualizada' : 'creada'} exitosamente.`);
        this.limpiarFormulario();
      } catch (error) {
        console.error('Error al enviar receta:', error);
        alert('Error al conectar con el servidor.');
      }
    },
    limpiarFormulario() {
      this.receta = {
        idHospital: '',
        idSeguro: '',
        idPaciente: '',
        codigoReceta: '',
        fecha: new Date().toISOString().slice(0, 10),
        idDoctor: '',
        observaciones: '',
        notasEspeciales: '',
        detalleMedicamentos: []
      };
      this.tieneSeguro = false;
    }
  }
};
</script>



<style scoped>
.formulario-receta {
  max-width: 700px;
  margin: 20px auto;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 12px;
  background-color: #f4f4f4;
}

.form-group {
  margin-bottom: 15px;
}

input,
select,
textarea {
  width: 100%;
  padding: 10px;
  margin-top: 5px;
  border-radius: 5px;
  border: 1px solid #ddd;
  box-sizing: border-box;
}

.medicamento-item {
  margin-bottom: 10px;
  border: 1px solid #ccc;
  padding: 10px;
  border-radius: 6px;
  background-color: #eef5ff;
  display: flex;
  gap: 5px;
  flex-wrap: wrap;
}

.btn-primario {
  background-color: #28a745;
  color: white;
  padding: 10px;
  border: none;
  margin-top: 15px;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.btn-primario:hover {
  background-color: #218838;
}

.btn-secundario {
  background-color: #007bff;
  color: white;
  padding: 10px;
  border: none;
  margin-top: 15px;
  margin-right: 10px;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.btn-secundario:hover {
  background-color: #0056b3;
}

.btn-eliminar {
  background-color: #dc3545;
  color: white;
  border: none;
  padding: 5px 10px;
  border-radius: 5px;
  cursor: pointer;
}

.btn-eliminar:hover {
  background-color: #c82333;
}
</style>
