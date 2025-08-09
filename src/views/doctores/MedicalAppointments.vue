<template>
  <div class="cita-container">
    <h2>Gestión de Citas Médicas</h2>
    <div class="content-container">
      <!-- Sección del formulario -->
      <div class="form-container">
        <!-- Buscador de pacientes -->
        <div class="form-group">
          <label>Buscar Paciente</label>
          <input
            type="text"
            v-model="filtroPaciente"
            placeholder="Ingrese nombre o identificación"
            @input="buscarPaciente"
          />
          <ul v-if="pacientes.length">
            <li
              v-for="paciente in pacientes"
              :key="paciente.idPaciente"
              @click="seleccionarPaciente(paciente)"
            >
              {{ paciente.usuario?.nombreUsuario || "Desconocido" }} - {{ paciente.documento }}
            </li>
          </ul>
        </div>

        <!-- Formulario para agendar citas -->
        <form @submit.prevent="guardarCita">
          <div class="form-group">
            <label>Nombre del Paciente</label>
            <input type="text" v-model="cita.pacienteNombre" readonly />
          </div>
          <div class="form-group">
            <label>Número de Identificación</label>
            <input type="text" v-model="cita.identificacion" readonly />
          </div>
          <div class="form-group">
            <label>Seleccionar Doctor</label>
            <select v-model="cita.idDoctor" required>
              <option value="" disabled>Seleccione un doctor</option>
              <option
                v-for="doctor in doctores"
                :key="doctor.idDoctor"
                :value="doctor.idDoctor"
              >
                {{ doctor.usuario?.nombreUsuario || "Desconocido" }} - {{ doctor.especialidad || "Sin especialidad" }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label>Fecha de la Cita</label>
            <input type="date" v-model="cita.fecha" required />
          </div>
          <div class="form-group">
            <label>Hora de Inicio</label>
            <select v-model="cita.horaInicio" required>
              <option v-for="hora in horasDisponibles" :key="hora" :value="hora">{{ hora }}</option>
            </select>
          </div>
          <div class="form-group">
            <label>Hora de Fin</label>
            <select v-model="cita.horaFin" required>
              <option v-for="hora in horasDisponibles" :key="hora" :value="hora">{{ hora }}</option>
            </select>
          </div>
          <div class="form-group">
            <label>Notas Adicionales</label>
            <textarea v-model="cita.notas"></textarea>
          </div>

          <button type="submit">Agendar Cita</button>
        </form>
      </div>

      <!-- Sección del calendario -->
      <div class="calendar-container">
        <FullCalendar :options="calendarOptions" />
      </div>
    </div>

    <!-- Modal para mostrar detalles de la cita -->
    <div v-if="citaSeleccionada" class="modal">
      <div class="modal-content">
        <h3>Detalles de la Cita</h3>
        <p><strong>Paciente:</strong> {{ citaSeleccionada.paciente?.usuario?.nombreUsuario || "Desconocido" }}</p>
        <p><strong>Doctor:</strong> {{ citaSeleccionada.doctor?.usuario?.nombreUsuario || "Desconocido" }}</p>
        <p><strong>Fecha:</strong> {{ citaSeleccionada.fecha }}</p>
        <p><strong>Hora:</strong> {{ citaSeleccionada.horaInicio }} - {{ citaSeleccionada.horaFin }}</p>
        <p><strong>Notas:</strong> {{ citaSeleccionada.motivo || "Sin notas adicionales" }}</p>
        <button @click="citaSeleccionada = null">Cerrar</button>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from "vue";
import FullCalendar from "@fullcalendar/vue3";
import dayGridPlugin from "@fullcalendar/daygrid";
import timeGridPlugin from "@fullcalendar/timegrid";
import interactionPlugin from "@fullcalendar/interaction";

import citaService from "@/services/citaService.js";
import doctorService from "@/services/doctorService.js";
import pacienteService from "@/services/pacienteService.js";

export default {
  components: {
    FullCalendar,
  },
  setup() {
    const filtroPaciente = ref("");
    const pacientes = ref([]);
    const cita = ref({
      pacienteNombre: "",
      identificacion: "",
      idPaciente: "",
      idDoctor: "",
      fecha: "",
      horaInicio: "",
      horaFin: "",
      notas: "",
    });
    const doctores = ref([]);
    const citasExistentes = ref([]);
    const horasDisponibles = ref(generarHoras());
    const citaSeleccionada = ref(null);

    const calendarOptions = ref({
      plugins: [dayGridPlugin, timeGridPlugin, interactionPlugin],
      initialView: "dayGridMonth",
      editable: false,
      events: [],
      dayMaxEventRows: false,
      eventDisplay: "block",
      headerToolbar: {
        left: "prev,next today",
        center: "title",
        right: "dayGridMonth,timeGridWeek,timeGridDay",
      },
      height: "auto",
      contentHeight: "auto",
      eventClick: ({ event }) => {
        console.log("📌 Evento clickeado:", event);
        const citaData = citasExistentes.value.find(
          (c) =>
            c.fecha === event.startStr.split("T")[0] &&
            c.horaInicio === event.startStr.split("T")[1].slice(0, 5) &&
            c.horaFin === event.endStr.split("T")[1].slice(0, 5)
        );
        if (citaData) {
          console.log("✅ Cita encontrada:", citaData);
          citaSeleccionada.value = citaData;
        } else {
          console.warn("⚠️ No se encontró la cita correspondiente.");
        }
      },
    });

    async function cargarDoctores() {
      try {
        const response = await doctorService.getAllDoctors();
        console.log("📌 Respuesta de la API de doctores:", response);
        if (Array.isArray(response) && response.length > 0) {
          doctores.value = response;
          console.log("✅ Lista de doctores cargada en el frontend:", doctores.value);
        } else {
          console.warn("⚠️ No se encontraron doctores en la API.");
          doctores.value = [];
        }
      } catch (error) {
        console.error("❌ Error al obtener doctores:", error);
        doctores.value = [];
      }
    }

    async function cargarCitas() {
      try {
        const citas = await citaService.obtenerCitas();
        console.log("Citas desde la API:", citas);
        if (Array.isArray(citas)) {
          citasExistentes.value = citas;
          calendarOptions.value.events = citasExistentes.value.map((citaItem) => ({
            title: `Cita con ${citaItem.paciente?.usuario?.nombreUsuario || "Desconocido"}`,
            start: `${citaItem.fecha}T${citaItem.horaInicio}`,
            end: `${citaItem.fecha}T${citaItem.horaFin}`,
            color: "#00bfff",
            textColor: "#fff",
          }));
        }
      } catch (error) {
        console.error("❌ Error al obtener citas:", error);
      }
    }

    async function buscarPaciente() {
      if (filtroPaciente.value.length >= 3) {
        try {
          const response = await pacienteService.getAllPaciente();
          console.log("📌 Respuesta de la API de pacientes:", response);
          let pacientesData = response.data;
          if (typeof pacientesData === "string") {
            try {
              pacientesData = JSON.parse(pacientesData);
            } catch (error) {
              console.error("❌ Error al parsear JSON de pacientes:", error, "⚠️ Datos recibidos:", response.data);
              pacientes.value = [];
              return;
            }
          }
          if (Array.isArray(pacientesData)) {
            pacientes.value = pacientesData.filter(
              (p) =>
                p.usuario?.rol?.roleName === "Paciente" &&
                (p.documento.includes(filtroPaciente.value) ||
                  p.usuario?.nombreUsuario?.toLowerCase().includes(filtroPaciente.value.toLowerCase()))
            );
          } else {
            console.warn("⚠️ La API no devolvió un array de pacientes:", pacientesData);
            pacientes.value = [];
          }
        } catch (error) {
          console.error("❌ Error al buscar pacientes:", error);
          pacientes.value = [];
        }
      } else {
        pacientes.value = [];
      }
    }

    function seleccionarPaciente(paciente) {
      cita.value.pacienteNombre = paciente.usuario?.nombreUsuario || "Desconocido";
      cita.value.identificacion = paciente.documento;
      cita.value.idPaciente = paciente.idPaciente;
      pacientes.value = [];
      filtroPaciente.value = "";
    }

    function convertirAMinutos(horaStr) {
      const [horas, minutos] = horaStr.split(":").map(Number);
      return horas * 60 + minutos;
    }

    async function guardarCita() {
      try {
        console.log("📌 Enviando cita:", cita.value);
        const { fecha, idDoctor, horaInicio, horaFin, idPaciente, notas } = cita.value;
        const fechaHoy = new Date().toISOString().split("T")[0];

        if (fecha < fechaHoy) {
          alert("❌ No puedes agendar una cita en el pasado.");
          return;
        }

        if (!horasDisponibles.value.includes(horaInicio) || !horasDisponibles.value.includes(horaFin)) {
          alert("❌ Horario no permitido. Seleccione una hora válida.");
          return;
        }

        if (horaInicio >= horaFin) {
          alert("❌ La hora de fin debe ser posterior a la hora de inicio.");
          return;
        }

        const nuevaInicio = convertirAMinutos(horaInicio);
        const nuevaFin = convertirAMinutos(horaFin);

        const solapada = citasExistentes.value.some((c) => {
          if (c.fecha === fecha && c.idDoctor === idDoctor) {
            const existenteInicio = convertirAMinutos(c.horaInicio);
            const existenteFin = convertirAMinutos(c.horaFin);
            return nuevaInicio < existenteFin && nuevaFin > existenteInicio;
          }
          return false;
        });

        if (solapada) {
          alert("❌ La cita se solapa con otra existente.");
          return;
        }

        await citaService.agendarCita({
          fecha,
          idDoctor,
          horaInicio,
          horaFin,
          idPaciente,
          motivo: notas,
          idHospital: 22,
          estado: "PENDIENTE",
        });

        alert("✅ Cita agendada con éxito!");
        limpiarFormulario();
        cargarCitas();
      } catch (error) {
        console.error("❌ Error al agendar cita:", error);
        alert("Hubo un error al agendar la cita.");
      }
    }

    function limpiarFormulario() {
      cita.value = {
        pacienteNombre: "",
        identificacion: "",
        idPaciente: "",
        idDoctor: "",
        fecha: "",
        horaInicio: "",
        horaFin: "",
        notas: "",
      };
    }

    function generarHoras() {
      const horas = [];
      let hora = 8;
      let minutos = 0;
      while (hora < 16 || (hora === 16 && minutos === 0)) {
        horas.push(`${hora.toString().padStart(2, "0")}:${minutos.toString().padStart(2, "0")}`);
        minutos += 30;
        if (minutos === 60) {
          minutos = 0;
          hora++;
        }
      }
      return horas;
    }

    onMounted(() => {
      cargarDoctores();
      cargarCitas();
    });

    return {
      filtroPaciente,
      pacientes,
      cita,
      doctores,
      horasDisponibles,
      guardarCita,
      buscarPaciente,
      seleccionarPaciente,
      calendarOptions,
      citaSeleccionada,
    };
  },
};
</script>

<style scoped>
.cita-container {
  max-width: 1200px;
  margin: auto;
  padding: 20px;
}

h2 {
  text-align: center;
  margin-bottom: 20px;
}

/* Contenedor principal para separar formulario y calendario */
.content-container {
  display: flex;
  gap: 20px;
  align-items: flex-start;
  flex-wrap: wrap;
}

/* Contenedor del formulario */
.form-container {
  flex: 1;
  min-width: 300px;
  max-width: 400px;
  padding: 15px;
  border: 1px solid #ccc;
  border-radius: 10px;
}

/* Contenedor del calendario */
.calendar-container {
  flex: 2;
  min-width: 300px;
}

/* Estilos de formulario */
.form-group {
  margin-bottom: 15px;
}

input,
select,
textarea {
  width: 100%;
  padding: 8px;
  margin-top: 5px;
  border-radius: 5px;
  border: 1px solid #ddd;
}

button {
  margin-top: 10px;
  padding: 10px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

button:hover {  top: 0;
  background-color: #0056b3;
}

/* Estilos del modal */
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
  background: white;  padding: 20px;  border-radius: 10px;  width: 90%;  max-width: 500px;
}
</style>
