<template>
  <div class="schedule-portal">
    <h2>Portal de Gestión de Citas</h2>

    <!-- Tabla de citas -->
    <div class="schedule-list">
      <table>
        <thead>
          <tr>
            <th v-if="isAdmin">ID Cita</th>
            <th>Paciente</th>
            <th v-if="isAdmin">Doctor</th>
            <th>Fecha</th>
            <th>Hora Inicio</th>
            <th>Hora Fin</th>
            <th>Estado</th>
            <th>Acciones</th>
            <th>Receta</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="cita in citas" :key="cita.idCita">
            <td v-if="isAdmin">{{ cita.idCita }}</td>
            <td>
  {{ cita.paciente && cita.paciente.usuario
      ? cita.paciente.usuario.nombreUsuario + ' ' + (cita.paciente.apellido || '')
      : 'Sin asignar' }}
</td>

<td v-if="isAdmin">
  {{ cita.doctor && cita.doctor.usuario
      ? cita.doctor.usuario.nombreUsuario + ' ' + (cita.doctor.apellido || '')
      : 'Sin asignar' }}
</td>


            <td>{{ cita.fecha }}</td>
            <td>{{ cita.horaInicio }}</td>
            <td>{{ cita.horaFin }}</td>
            <td>{{ cita.estado }}</td>
            <td>
             <!-- Procesar: permitir si está CONFIRMADA o PENDIENTE -->
<button v-if="['PENDIENTE', 'CONFIRMADA'].includes(cita.estado)" @click="abrirModalProcesar(cita)">Procesar</button>

<!-- Cancelar: solo si está PENDIENTE -->
<button v-if="cita.estado === 'PENDIENTE'" @click="cancelarCita(cita)">Cancelar</button>

<!-- Reasignar: permitir si está CONFIRMADA o PENDIENTE y es admin -->
<button v-if="isAdmin && ['PENDIENTE', 'CONFIRMADA'].includes(cita.estado)" @click="abrirModalReasignar(cita)">Reasignar</button>

            </td>
            <td>
              <button @click="gestionarReceta(cita)">Gestionar Receta</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Modal para gestionar receta -->
    <div v-if="mostrarModalReceta" class="modal">
      <div class="modal-content">
        <h3>Gestión de Receta</h3>
        <p><strong>Cita ID:</strong> {{ citaSeleccionada.idCita }}</p>

        <div v-if="recetaExistente">
          <p><strong>Receta ID:</strong> {{ receta.idReceta }}</p>
          <p><strong>Código Receta:</strong> {{ receta.codigoReceta }}</p>
          <p><strong>Notas:</strong> {{ receta.anotaciones }}</p>
          <p><strong>Notas Especiales:</strong> {{ receta.notasEspeciales }}</p>

          <h4>Medicamentos:</h4>
          <ul v-if="receta.medicamentos.length > 0">
            <li v-for="med in receta.medicamentos" :key="med.idRecetaMedicamento">
              {{ med.medicamento.principioActivo }} - {{ med.dosis }} ({{ med.frecuencia }})
            </li>
          </ul>
          <p v-else>Sin medicamentos registrados.</p>

          <button @click="mostrarAgregarMedicamento = true">Agregar Medicamento</button>
        </div>

        <div v-else>
          <h3>Nueva Receta</h3>
          <input v-model="nuevaReceta.codigoReceta" placeholder="Código de Receta" required />

          <p style="color: gray;">Paciente: {{ citaSeleccionada.paciente?.nombre || 'No asignado' }} {{ citaSeleccionada.paciente?.apellido || '' }}</p>
          <p style="color: gray;">Doctor: {{ citaSeleccionada.doctor?.nombre || 'No asignado' }} {{ citaSeleccionada.doctor?.apellido || '' }}</p>

          <textarea v-model="nuevaReceta.anotaciones" placeholder="Anotaciones"></textarea>
          <textarea v-model="nuevaReceta.notasEspeciales" placeholder="Notas Especiales"></textarea>

          <button @click="crearRecetaDesdeCita">Generar Receta</button>
        </div>

        <div v-if="mostrarAgregarMedicamento">
          <h3>Agregar Medicamento</h3>
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

          <button @click="agregarMedicamento">Añadir Medicamento</button>
        </div>
        <button @click="abrirModalEditarReceta"> Editar Receta</button>
              <!-- Modal para Editar Receta -->
<div v-if="mostrarModalEditar" class="modal">
  <div class="modal-content">

    <h3> Editar Receta</h3>

    <!-- Código de la receta (No editable) -->
    <label>Código Receta</label>
    <input type="text" v-model="recetaEditable.codigoReceta" disabled />

    <!-- Editar anotaciones -->
    <label>Anotaciones</label>
    <textarea v-model="recetaEditable.anotaciones"></textarea>

    <!-- Editar notas especiales -->
    <label>Notas Especiales</label>
    <textarea v-model="recetaEditable.notasEspeciales"></textarea>

    <!-- Lista de medicamentos con scroll interno -->
    <div class="medicamentos-container">
      <h4>Medicamentos</h4>
      <div v-for="(med, index) in recetaEditable.medicamentos" :key="med.idRecetaMedicamento" class="medicamento-item">

        <label>Medicamento {{ index + 1 }}</label>
        <select v-model="med.medicamento.idMedicamento">
          <option v-for="medicamento in medicamentos" :key="medicamento.idMedicamento" :value="medicamento.idMedicamento">
            {{ medicamento.principioActivo }} ({{ medicamento.concentracion }})
          </option>
        </select>

        <label>Dosis</label>
        <input type="text" v-model="med.dosis" />

        <label>Frecuencia</label>
        <input type="text" v-model="med.frecuencia" />

        <label>Duración</label>
        <input type="text" v-model="med.duracion" />

        <label>Diagnóstico</label>
        <input type="text" v-model="med.diagnostico" />

        <!-- Botón para eliminar medicamento -->
        <button class="btn-eliminar" @click="eliminarMedicamento(index)">Eliminar</button>
      </div>
    </div>

    <!-- Botón para agregar un nuevo medicamento -->
    <button class="btn-agregar" @click="agregarMedicamentoVacio">Agregar Medicamento</button>

    <!-- Botones de acción -->
    <div class="modal-buttons">
      <button class="btn-guardar" @click="guardarEdicionReceta">Guardar Cambios</button>
      <button class="btn-cancelar" @click="cerrarModalEditar">Cancelar</button>
    </div>

  </div>
</div>



        <button @click="cerrarModalReceta">Cerrar</button>
      </div>
    </div>
  </div>

<!-- Modal para Procesar -->
<div v-if="mostrarModalProcesar" class="modal">
  <div class="modal-content">
    <h3>Procesar Cita</h3>
    <p>¿Deseas marcar la cita <strong>{{ citaSeleccionada.idCita }}</strong> como procesada?</p>

<label for="diagnostico">Diagnóstico:</label>
<input type="text" id="diagnostico" v-model="diagnosticoProcesar" required />

<label for="resultados">Resultados:</label>
<textarea id="resultados" v-model="resultadosProcesar" required></textarea>

    <div class="modal-buttons">
      <button class="btn-guardar" @click="procesarCita">Confirmar</button>
      <button class="btn-cancelar" @click="cerrarModalProcesar">Cancelar</button>
    </div>
  </div>
</div>

<!-- Modal para Reasignar -->
<div v-if="mostrarModalReasignar" class="modal">
  <div class="modal-content">
    <h3>Reasignar Cita</h3>
    <p>ID Cita: {{ citaSeleccionada.idCita }}</p>

    <label for="nuevoDoctor">Nuevo Doctor:</label>
    <select v-model="nuevoDoctorId">
      <option disabled value="">Seleccione un doctor</option>
      <option v-for="doc in doctores" :key="doc.idDoctor" :value="doc.idDoctor">
        {{ doc.nombre }} {{ doc.apellido }}
      </option>
    </select>

    <div class="modal-buttons">
      <button class="btn-guardar" @click="reasignarCita">Guardar</button>
      <button class="btn-cancelar" @click="cerrarModalReasignar">Cancelar</button>
    </div>
  </div>
</div>

</template>

<script>
import axios from 'axios';
import API_URL from '@/config';
import recetaService from "@/services/RecetaService.js";
import { userRole, userId } from '@/stores/authStore';

export default {
  data() {
    return {
      citas: [],
      mostrarModalProcesar: false,
      mostrarModalReasignar: false,
nuevoDoctorId: "",
diagnosticoProcesar: "",
resultadosProcesar: "",
doctores: [],
      mostrarModalEditar: false,  // Controla el modal de edición
    recetaEditable: {},  // Guarda la receta en edición
      mostrarModalReceta: false,
      mostrarAgregarMedicamento: false,
      recetaExistente: false,
      citaSeleccionada: {},
      receta: {},
      nuevaReceta: {
        codigoReceta: "",
        idCita: null,
        idPaciente: null,
        idDoctor: null,
        anotaciones: "",
        notasEspeciales: "",
      },
      medicamentoSeleccionado: "",
      dosis: "",
      frecuencia: "",
      duracion: "",
      diagnostico: "",
      medicamentos: [],
    };
  },
  computed: {
    isAdmin() {
      return userRole.value === 1;
    },
    isDoctor() {
      return userRole.value === 2;
    }
  },
  mounted() {
    console.log("mounted() ejecutado.");
    this.obtenerCitas();
    this.obtenerMedicamentos();
    this.obtenerDoctores();
  },
  methods: {

    async obtenerDoctores() {
  try {
    const response = await axios.get(`${API_URL}/doctor`);
    this.doctores = response.data;
    console.log("Doctores cargados:", this.doctores);
  } catch (error) {
    console.error("Error al obtener doctores:", error);
    alert("No se pudieron cargar los doctores.");
  }
},

async cancelarCita(cita) {
  if (!confirm("¿Estás seguro de que deseas cancelar esta cita?")) return;

  try {
    await axios.put(`${API_URL}/citas/${cita.idCita}/cancelar`);
    alert("Cita cancelada correctamente.");
    this.obtenerCitas();
  } catch (error) {
    console.error("Error al cancelar la cita:", error);
    alert("No se pudo cancelar la cita.");
  }
},

async reasignarCita() {
  if (!this.nuevoDoctorId) {
    alert("Debe seleccionar un doctor.");
    return;
  }
  try {
    await axios.put(`${API_URL}/citas/${this.citaSeleccionada.idCita}/reasignar`, {
      idDoctor: this.nuevoDoctorId
    });
    alert("Cita reasignada exitosamente.");
    this.obtenerCitas();
  } catch (error) {
    console.error("Error al reasignar cita:", error);
  } finally {
    this.cerrarModalReasignar();
  }
},

async procesarCita() {
  try {
    await axios.put(`${API_URL}/citas/${this.citaSeleccionada.idCita}/procesar`, {
  diagnostico: this.diagnosticoProcesar,
  resultados: this.resultadosProcesar
});

    alert('Cita procesada exitosamente');
    this.obtenerCitas();
  } catch (error) {
    console.error('Error al procesar cita:', error);
  } finally {
    this.cerrarModalProcesar();
  }
},






    abrirModalProcesar(cita) {
  this.citaSeleccionada = cita;
  this.mostrarModalProcesar = true;
},
cerrarModalProcesar() {
  this.mostrarModalProcesar = false;
  this.diagnosticoProcesar = "";
this.resultadosProcesar = "";

},


abrirModalReasignar(cita) {
  this.citaSeleccionada = cita;
  this.nuevoDoctorId = '';
  this.mostrarModalReasignar = true;
},
cerrarModalReasignar() {
  this.mostrarModalReasignar = false;
},
async reasignarCita() {
  if (!this.nuevoDoctorId) {
    alert("Debe seleccionar un doctor.");
    return;
  }
  try {
    await axios.put(`${API_URL}/citas/${this.citaSeleccionada.idCita}/reasignar`, {
      idDoctor: this.nuevoDoctorId
    });
    alert("Cita reasignada exitosamente.");
    this.obtenerCitas();
  } catch (error) {
    console.error("Error al reasignar cita:", error);
  } finally {
    this.cerrarModalReasignar();
  }
},

  abrirModalEditarReceta() {
    console.log("Abriendo modal de edición...");
    this.recetaEditable = JSON.parse(JSON.stringify(this.receta)); // Clonamos para no afectar directamente la receta
    this.mostrarModalEditar = true;
  },

  cerrarModalEditar() {
    console.log(" Cerrando modal de edición...");
    this.mostrarModalEditar = false;
  },

  agregarMedicamentoVacio() {
    console.log("Agregando medicamento vacío...");
    this.recetaEditable.medicamentos.push({
      idRecetaMedicamento: null,
      medicamento: { idMedicamento: null },
      dosis: "",
      frecuencia: "",
      duracion: "",
      diagnostico: ""
    });
  },

  eliminarMedicamento(index) {
    console.log(" Eliminando medicamento en índice:", index);
    this.recetaEditable.medicamentos.splice(index, 1);
  },

  async guardarEdicionReceta() {
    console.log("Guardando edición de receta...");

    if (!this.recetaEditable.idReceta) {
      alert(" Error: ID de receta inválido.");
      return;
    }

    const recetaEditada = {
      anotaciones: this.recetaEditable.anotaciones,
      notasEspeciales: this.recetaEditable.notasEspeciales,
      medicamentos: this.recetaEditable.medicamentos.map(med => ({
        idRecetaMedicamento: med.idRecetaMedicamento,
        medicamento: { idMedicamento: med.medicamento.idMedicamento },
        dosis: med.dosis,
        frecuencia: med.frecuencia,
        duracion: med.duracion,
        diagnostico: med.diagnostico
      }))
    };

    console.log("Enviando actualización:", JSON.stringify(recetaEditada, null, 2));

    try {
      const response = await axios.put(`${API_URL}/recetas/${this.recetaEditable.idReceta}`, recetaEditada);
      console.log(" Receta actualizada correctamente:", response.data);

      this.receta = response.data; // Actualizar la receta en la UI
      this.mostrarModalEditar = false; // Cerrar el modal
      alert(" Receta actualizada con éxito.");
    } catch (error) {
      console.error("Error al actualizar la receta:", error);
      alert("Error al actualizar la receta.");
    }
  },

    cerrarModalReceta() {
  console.log(" Cerrando modal de receta...");
  this.mostrarModalReceta = false;
  this.mostrarAgregarMedicamento = false;
  this.recetaExistente = false;
  this.receta = {};
  this.nuevaReceta = {
    codigoReceta: "",
    idCita: null,
    idPaciente: null,
    idDoctor: null,
    anotaciones: "",
    notasEspeciales: "",
  };
},

    async obtenerCitas() {
      try {
        console.log("Llamando a obtenerCitas()...");
        let response = await axios.get(`${API_URL}/citas`);
        this.citas = response.data;
        console.log("Citas obtenidas:", this.citas);
      } catch (error) {
        console.error(" Error al obtener las citas:", error);
        alert("No se pudieron cargar las citas.");
      }
    },

    async obtenerMedicamentos() {
  try {
    console.log("Cargando medicamentos...");
    const response = await axios.get(`${API_URL}/medicamentos`);
    this.medicamentos = response.data;
    console.log("Medicamentos cargados:", this.medicamentos);
  } catch (error) {
    console.error("Error al obtener los medicamentos:", error);
    alert("No se pudieron cargar los medicamentos.");
  }
},

async agregarMedicamento() {
  // Validar que todos los campos tengan datos
  if (!this.medicamentoSeleccionado || !this.dosis || !this.frecuencia || !this.duracion) {
    alert(" Todos los campos son obligatorios.");
    return;
  }

  //  Validar que la receta tenga un ID válido
  if (!this.receta.idReceta) {
    alert(" Error: No hay una receta seleccionada.");
    console.error(" No hay ID de receta al agregar medicamento.");
    return;
  }

  // Validar que el medicamento tenga un ID válido
  if (!this.medicamentoSeleccionado) {
    alert("Error: No se ha seleccionado un medicamento.");
    console.error("ID de medicamento es null.");
    return;
  }

  // Estructura correcta que el backend espera recibir
  const medicamentoData = {
    receta: { idReceta: this.receta.idReceta },
    medicamento: { idMedicamento: this.medicamentoSeleccionado },
    dosis: this.dosis,
    frecuencia: this.frecuencia,
    duracion: this.duracion,
    diagnostico: this.diagnostico || null,
  };

  //  Mostrar los datos antes de enviarlos
  console.log("Enviando medicamento al backend:", JSON.stringify(medicamentoData, null, 2));

  try {
    const response = await axios.post(`${API_URL}/recetas/medicamentos`, medicamentoData);
    console.log("Medicamento agregado correctamente:", response.data);

    alert(" Medicamento agregado correctamente.");

    // Cargar medicamentos después de agregar uno nuevo
    await this.cargarMedicamentosPorReceta();

    // Resetear los campos después de agregar el medicamento
    this.medicamentoSeleccionado = "";
    this.dosis = "";
    this.frecuencia = "";
    this.duracion = "";
    this.diagnostico = "";

  } catch (error) {
    console.error(" Error al agregar medicamento:", error);

    if (error.response) {
      console.error(" Respuesta del servidor:", error.response.data);
      alert(`Error al agregar el medicamento: ${error.response.data}`);
    } else {
      alert("Error al agregar el medicamento.");
    }
  }
},

async cargarMedicamentosPorReceta() {
  if (!this.receta.idReceta) {
    console.error(" No se puede cargar medicamentos: idReceta es null.");
    return;
  }

  try {
    console.log("Cargando medicamentos de la receta con ID:", this.receta.idReceta);
    const response = await axios.get(`${API_URL}/recetas/${this.receta.idReceta}/medicamentos`);
    this.receta.medicamentos = response.data || [];
    console.log("Medicamentos actualizados:", this.receta.medicamentos);
  } catch (error) {
    console.error(" Error al cargar medicamentos:", error);
  }
},

async editarReceta() {
  console.log("Editando receta con ID:", this.receta.idReceta);

  if (!this.receta.idReceta) {
    alert("No se puede actualizar: ID de receta es inválido.");
    return;
  }

  //  Estructura que el backend espera recibir
  const recetaEditada = {
    anotaciones: this.receta.anotaciones,
    notasEspeciales: this.receta.notasEspeciales,
    medicamentos: this.receta.medicamentos.map(med => ({
      idRecetaMedicamento: med.idRecetaMedicamento,
      medicamento: { idMedicamento: med.medicamento.idMedicamento },
      dosis: med.dosis,
      frecuencia: med.frecuencia,
      duracion: med.duracion,
      diagnostico: med.diagnostico
    }))
  };
  console.log(" Enviando actualización:", JSON.stringify(recetaEditada, null, 2));

  try {
    const response = await axios.put(`${API_URL}/recetas/${this.receta.idReceta}`, recetaEditada);
    console.log(" Receta actualizada correctamente:", response.data);

    //  Actualizar UI con los datos editados
    this.receta = response.data;

    alert("Receta actualizada con éxito.");
    this.mostrarModalReceta = false; // Cerrar el modal
  } catch (error) {
    console.error("Error al actualizar la receta:", error);
    alert("Error al actualizar la receta.");
  }
},



    async gestionarReceta(cita) {
      console.log(" Gestionando receta para cita:", cita);

      this.citaSeleccionada = { ...cita };
      this.nuevaReceta.idCita = cita.idCita;

      // Validar que el idPaciente esté presente antes de asignarlo
      if (!cita.paciente?.idPaciente) {
        console.error("Error: La cita no tiene un idPaciente válido.");
        alert("Error: La cita no tiene un paciente asignado correctamente.");
        return;
      }
      this.nuevaReceta.idPaciente = cita.paciente.idPaciente;

      this.nuevaReceta.idDoctor = cita.doctor?.idDoctor || null;

      if (!this.nuevaReceta.idPaciente || !this.nuevaReceta.idDoctor) {
        console.error(" Error: idPaciente o idDoctor son null.");
        alert("Error: La cita no tiene paciente o doctor asignado.");
        return;
      }

      try {
        console.log(" Buscando receta existente...");
        const response = await axios.get(`${API_URL}/recetas/cita/${cita.idCita}`);
        this.receta = response.data || {};
        this.recetaExistente = Boolean(response.data);
        console.log("Receta encontrada:", this.receta);
      } catch (error) {
        console.warn("No hay receta registrada para esta cita.");
        this.recetaExistente = false;
      }

      this.mostrarModalReceta = true;
    },

    async crearRecetaDesdeCita() {
  console.log("Creando receta con:", JSON.stringify(this.nuevaReceta, null, 2));

  try {
    const respuesta = await recetaService.crearReceta(this.nuevaReceta);

    if (!respuesta.idReceta) {
      console.error("Error: El backend no devolvió un ID de receta válido.");
      alert("Error al crear la receta. Inténtalo de nuevo.");
      return;
    }

    this.receta = { ...respuesta, medicamentos: respuesta.medicamentos || [] };
    this.recetaExistente = true;
    this.mostrarAgregarMedicamento = true;

    console.log(" Receta creada con éxito:", this.receta);
  } catch (error) {
    console.error(" Error al generar la receta:", error);
    alert("Error al generar la receta.");
  }
}



  }
};
</script>



<style>
/* =========================
   Variables globales
========================= */
:root {
  --color-verde-claro:  #DAFDBA;
  --color-menta:        #172018;
  --color-teal:         #259c8a;
  --color-teal-oscuro:  #13678A;
  --color-azul-noche:   #45C4B0;

  --color-texto-claro:  #FFFFFF;
  --color-texto-oscuro: #1C1C1C;
}

/* =========================
   Estilo general del body
========================= */
body {
  margin: 0;
  padding: 0;
  background-color: var(--color-azul-noche); /* Fondo total de la página */
  font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
}

/* =========================
   Contenedor principal
   Extendemos ancho y damos
   un look tipo "card"
========================= */
.schedule-portal {
  margin: 2rem auto;
  padding: 2rem;
  max-width: 90%; /* Ajusta si quieres más (100%) o menos (por ejemplo 80%) */
  background: linear-gradient(to bottom, var(--color-teal-oscuro), var(--color-azul-noche));
  border-radius: 12px;
  box-shadow: 0 4px 8px rgba(0,0,0,0.4);
}

/* Título centrado con algo de estilo */
.schedule-portal h2 {
  margin-top: 0;
  margin-bottom: 1rem;
  text-align: center;
  font-size: 2rem;
  color: var(--color-menta);
  text-shadow: 1px 1px 2px rgba(0,0,0,0.5);
}

/* =========================
   Tabla de citas
========================= */
.schedule-list {
  overflow-x: auto; /* por si se desborda en pantallas pequeñas */
}

/* Contenedor de la tabla */
.schedule-list table {
  width: 100%;
  border-collapse: collapse;
  border-radius: 8px; /* esquinas redondeadas */
  overflow: hidden;   /* para que el thead respete el radio */
  box-shadow: 0 2px 6px rgba(0,0,0,0.3);
}

/* Encabezado */
.schedule-list thead {
  background-color: var(--color-teal);
}
.schedule-list thead th {
  padding: 1rem;
  font-weight: 600;
  text-align: left;
  font-size: 1rem;
  border-bottom: 2px solid var(--color-teal-oscuro);
}

/* Cuerpo */
.schedule-list tbody tr {
  background-color: var(--color-azul-noche);
  transition: background-color 0.2s;
}
.schedule-list td {
  padding: 0.75rem 1rem;
  font-size: 0.95rem;
  color: var(--color-verde-claro);
  border-bottom: 1px solid var(--color-teal-oscuro);
}

/* Efecto hover en filas */
.schedule-list tbody tr:hover {
  background-color: var(--color-teal-oscuro);
}

/* =========================
   BOTONES
========================= */

/* Quita estilos básicos */
button {
  border: none;
  border-radius: 4px;
  padding: 0.5rem 0.75rem;
  margin-right: 5px;
  cursor: pointer;
  font-weight: 600;
  transition: filter 0.2s, background-color 0.2s;
}

/* Botón para "Procesar" */
.btn-procesar {
  background-color: #2ecc71; /* verde clarito (puedes cambiar) */
  color: #fff;
}
.btn-procesar:hover {
  filter: brightness(110%);
}

/* Botón para "Cancelar" */
.btn-cancelar {
  background-color: #e74c3c; /* rojo */
  color: #fff;
}
.btn-cancelar:hover {
  filter: brightness(110%);
}

/* Botón para "Reasignar" */
.btn-reasignar {
  background-color: #f1c40f; /* amarillo/mostaza */
  color: #1C1C1C;
}
.btn-reasignar:hover {
  filter: brightness(110%);
}

/* =========================
   MODALES (Procesar / Reasignar)
========================= */
.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.6);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 999;
}

.modal-content {
  background: var(--color-verde-claro);
  color: var(--color-texto-oscuro);
  padding: 20px;
  border-radius: 10px;
  width: 400px;
  max-width: 90%;
  box-shadow: 0 2px 6px rgba(0,0,0,0.3);
}

.form-group {
  margin-bottom: 15px;
}
.form-group label {
  display: block;
  margin-bottom: 5px;
  font-weight: 600;
}
input, textarea, select {
  width: 100%;
  padding: 8px;
  margin-top: 5px;
  border-radius: 5px;
  border: 1px solid var(--color-teal-oscuro);
  font-size: 1rem;
}

.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.6);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 999;
}

.modal-content {
  background: #DAFDBA;
  color: #1C1C1C;
  padding: 20px;
  border-radius: 10px;
  width: 500px;
  max-width: 90%;
  max-height: 90vh;
  overflow-y: auto;
  box-shadow: 0 2px 6px rgba(0,0,0,0.3);
}

/* Sección de medicamentos con scroll */
.medicamentos-container {
  max-height: 300px; /* Ajusta según sea necesario */
  overflow-y: auto;
  padding: 10px;
  border: 1px solid #13678A;
  border-radius: 8px;
  margin-bottom: 10px;
  background-color: #B2F2BB;
}

/* Estilos individuales para cada medicamento */
.medicamento-item {
  padding: 10px;
  margin-bottom: 10px;
  background: #B2F2BB;
  border: 1px solid #45C4B0;
  border-radius: 8px;
}

/* Botones */
.modal-buttons {
  display: flex;
  justify-content: space-between;
  margin-top: 10px;
}

.btn-agregar {
  background-color: #45C4B0;
  color: white;
  border: none;
  padding: 8px 10px;
  cursor: pointer;
  width: 100%;
  margin-top: 10px;
  border-radius: 5px;
}

.btn-eliminar {
  background-color: #e74c3c;
  color: white;
  border: none;
  padding: 5px 8px;
  cursor: pointer;
  margin-top: 5px;
  border-radius: 5px;
}

.btn-guardar {
  background-color: #2ecc71;
  color: white;
  border: none;
  padding: 10px 15px;
  cursor: pointer;
  border-radius: 5px;
}

.btn-cancelar {
  background-color: #e74c3c;
  color: white;
  border: none;
  padding: 10px 15px;
  cursor: pointer;
  border-radius: 5px;
}


</style>
