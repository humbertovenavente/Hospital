<template>
  <div class="recetas-container">
    <h1>Gestión de Recetas</h1>

    <div v-if="!recetaGenerada" class="form-receta">
      <h3>Nueva Receta</h3>

      <input v-model="nuevaReceta.codigoReceta" placeholder="Código de Receta" required />

      <select v-model="nuevaReceta.idCita" @change="cargarDatosDeCita">
        <option value="" disabled>Seleccione Cita</option>
        <option v-for="cita in citas" :key="cita.idCita" :value="cita.idCita">
          Cita ID: {{ cita.idCita }} - Paciente ID: {{ cita.idPaciente }}
        </option>
      </select>

      <p style="color: gray;">Receta ID: {{ nuevaReceta.idReceta }}</p>
      <p style="color: gray;">Paciente Asignado (ID): {{ nuevaReceta.idPaciente || 'No asignado' }}</p>
      <p style="color: gray;">Doctor Asignado (ID): {{ nuevaReceta.idDoctor || 'No asignado' }}</p>

      <textarea v-model="nuevaReceta.anotaciones" placeholder="Anotaciones"></textarea>
      <textarea v-model="nuevaReceta.notasEspeciales" placeholder="Notas Especiales"></textarea>

      <button @click="generarReceta" class="btn-guardar">Generar Receta</button>
    </div>

    <div v-if="recetaGenerada" class="form-medicamentos">
      <h3>Agregar Medicamentos</h3>
      <p style="font-weight: bold;">Receta ID: {{ nuevaReceta.idReceta }}</p>

      <select v-model="medicamentoSeleccionado">
        <option value="" disabled>Seleccione Medicamento</option>
        <option v-for="medicamento in medicamentos" :key="medicamento.idMedicamento" :value="medicamento.idMedicamento">
          {{ medicamento.principioActivo }} ({{ medicamento.concentracion }} - {{ medicamento.formaFarmaceutica }})
        </option>
      </select>

      <input v-model="dosis" placeholder="Dosis" required />
      <input v-model="frecuencia" placeholder="Frecuencia" required />
      <input v-model="duracion" placeholder="Duración" required />
      <input v-model="diagnostico" placeholder="Diagnóstico" />

      <button @click="agregarMedicamento" class="btn-secundario">Añadir Medicamento</button>

      <p v-if="mensajeExito" class="mensaje-exito">{{ mensajeExito }}</p>

      <h3 class="titulo-medicamentos">Medicamentos en la Receta</h3>
      <p><strong>Receta ID:</strong> {{ nuevaReceta.idReceta }}</p>

      <ul v-if="medicamentosReceta.length > 0">
        <li v-for="(med, index) in medicamentosReceta" :key="index">
          {{ med.medicamento.principioActivo }} | Dosis: {{ med.dosis }}, Frecuencia: {{ med.frecuencia }}, Duración: {{ med.duracion }}
        </li>
      </ul>

      <button @click="regresarARecetas" class="btn-regresar">Regresar a Recetas</button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import recetaService from "../services/RecetaService.js";
import { obtenerCitas, obtenerMedicamentos } from "@/services/selectService.js";
import { useRouter } from "vue-router";

const recetas = ref([]);
const recetaGenerada = ref(false);
const nuevaReceta = ref({
  idReceta: null,
  codigoReceta: "",
  idCita: "",
  idPaciente: "",
  idDoctor: "",
  anotaciones: "",
  notasEspeciales: "",
  medicamentos: [],
});

const citas = ref([]);
const medicamentos = ref([]);
const medicamentosReceta = ref([]);
const mensajeExito = ref("");

const medicamentoSeleccionado = ref("");
const dosis = ref("");
const frecuencia = ref("");
const duracion = ref("");
const diagnostico = ref("");

const router = useRouter();

onMounted(async () => {
  citas.value = await obtenerCitas();
  medicamentos.value = await obtenerMedicamentos();
});

const cargarDatosDeCita = () => {
  const citaSeleccionada = citas.value.find(cita => cita.idCita === nuevaReceta.value.idCita);
  if (citaSeleccionada) {
    nuevaReceta.value.idPaciente = citaSeleccionada.idPaciente;
    nuevaReceta.value.idDoctor = citaSeleccionada.idDoctor;
  }
};

const generarReceta = async () => {
  try {
    const respuesta = await recetaService.crearReceta(nuevaReceta.value);
    nuevaReceta.value.idReceta = respuesta.idReceta;
    recetaGenerada.value = true;
    alert("Receta guardada con éxito");
    await cargarMedicamentosPorReceta();
  } catch (error) {
    console.error("Error generando receta:", error);
  }
};

const cargarMedicamentosPorReceta = async () => {
  if (nuevaReceta.value.idReceta) {
    medicamentosReceta.value = await recetaService.obtenerMedicamentosPorReceta(nuevaReceta.value.idReceta);
  }
};

const agregarMedicamento = async () => {
  if (!medicamentoSeleccionado.value || !dosis.value || !frecuencia.value || !duracion.value) {
    alert("Todos los campos son obligatorios");
    return;
  }

  const medicamentoData = {
    idReceta: nuevaReceta.value.idReceta,
    idMedicamento: medicamentoSeleccionado.value,
    dosis: dosis.value,
    frecuencia: frecuencia.value,
    duracion: duracion.value,
    diagnostico: diagnostico.value || null,
  };

  try {
    await recetaService.agregarMedicamento(medicamentoData);
    mensajeExito.value = "✅ Medicamento agregado correctamente";

    // Limpiar los campos después de agregar el medicamento
    medicamentoSeleccionado.value = "";
    dosis.value = "";
    frecuencia.value = "";
    duracion.value = "";
    diagnostico.value = "";

    await cargarMedicamentosPorReceta();

    setTimeout(() => {
      mensajeExito.value = "";
    }, 3000);
  } catch (error) {
    console.error("Error agregando medicamento:", error);
  }
};

const regresarARecetas = () => {
  router.push("/recetas");
};
</script>

<style scoped>
.recetas-container {
  max-width: 600px;
  margin: 20px auto;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 10px;
  background-color: #f9f9f9;
}
h1, h3 {
  text-align: center;
}
.btn-guardar, .btn-secundario {
  display: block;
  width: 100%;
  margin: 10px 0;
  padding: 10px;
  border: none;
  border-radius: 5px;
  background-color: #007bff;
  color: white;
  cursor: pointer;
}
.btn-guardar:hover, .btn-secundario:hover {
  background-color: #0056b3;
}
.btn-regresar {
  display: block;
  width: 100%;
  margin-top: 20px;
  padding: 10px;
  border: none;
  border-radius: 5px;
  background-color: #28a745;
  color: white;
  cursor: pointer;
}
.btn-regresar:hover {
  background-color: #218838;
}
.mensaje-exito {
  color: green;
  font-weight: bold;
  text-align: center;
}
.titulo-medicamentos {
  text-align: center;
  font-weight: bold;
}
</style>
