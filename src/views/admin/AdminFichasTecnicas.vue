<template>
  <div class="container">
    <div class="header">ADMINISTRACIÓN DE FICHAS TÉCNICAS</div>

    <div class="actions">
      <button @click="abrirModalCrear">Crear Nueva Ficha</button>
    </div>

    <div class="section">
      <h3>LISTADO DE PACIENTES CON FICHAS TÉCNICAS</h3>
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>Nombre</th>
            <th>DPI</th>
            <th>Afiliación</th>
            <th>Código Seguro</th>
            <th>Carnet Seguro</th>
            <th>Fecha Creación</th>
            <th>Acciones</th>
          </tr>
        </thead>
        <tbody>
          <tr v-if="loading"><td colspan="8">Cargando...</td></tr>
          <tr v-else-if="error"><td colspan="8" style="color:red;">{{ error }}</td></tr>
          <tr v-else-if="fichasFiltradas.length === 0"><td colspan="8">Sin registros.</td></tr>
          <tr v-for="ficha in fichasFiltradas" :key="ficha.idFicha">
            <td>{{ ficha.paciente.idPaciente }}</td>
            <td>{{ ficha.paciente.usuario.nombreUsuario }}</td>
            <td>{{ ficha.paciente.documento }}</td>
            <td>{{ ficha.numeroAfiliacion }}</td>
            <td>{{ ficha.codigoSeguro }}</td>
            <td>{{ ficha.carnetSeguro }}</td>
            <td>{{ ficha.fechaCreacion }}</td>
            <td>
              <button @click="abrirModalEditar(ficha)">Editar</button>
              <button @click="eliminarFicha(ficha.idFicha)">Borrar</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Modal para crear/editar ficha -->
    <div v-if="mostrarModal" class="modal">
      <div class="modal-content">
        <h3>{{ fichaEnEdicion.idFicha ? 'Editar Ficha Técnica' : 'Nueva Ficha Técnica' }}</h3>

        <!-- Buscador de pacientes -->
        <label>Buscar Paciente</label>
        <input v-model="busquedaPaciente" placeholder="Buscar por nombre o DPI" @input="filtrarPacientes" />
        <ul v-if="pacientesFiltrados.length">
          <li v-for="paciente in pacientesFiltrados" :key="paciente.idPaciente" @click="seleccionarPaciente(paciente)">
            {{ paciente.usuario.nombreUsuario }} - {{ paciente.documento }}
          </li>
        </ul>

        <label>Paciente ID</label>
        <input v-model="fichaEnEdicion.idPaciente" disabled />

        <label>Servicio ID</label>
        <input v-model="fichaEnEdicion.idServicio" placeholder="ID del servicio" />

        <label>Afiliación</label>
        <input v-model="fichaEnEdicion.numeroAfiliacion" placeholder="Número de afiliación" />

        <label>Código Seguro</label>
        <input v-model="fichaEnEdicion.codigoSeguro" placeholder="Código del seguro" />

        <label>Carnet Seguro</label>
        <input v-model="fichaEnEdicion.carnetSeguro" placeholder="Carnet del seguro" />

        <label>Historial de Servicios</label>
        <textarea v-model="fichaEnEdicion.historialServicios" placeholder="Historial de servicios"></textarea>

        <div class="modal-buttons">
          <button @click="guardarFicha">Guardar</button>
          <button class="btn-cancelar" @click="cerrarModal">Cancelar</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, computed, onMounted } from "vue";
import fichasApi from "@/services/fichasApi";
import pacienteService from "@/services/pacienteService";

export default {
  setup() {
    const fichas = ref([]);
    const pacientes = ref([]);
    const pacientesFiltrados = ref([]);
    const busquedaPaciente = ref("");

    const loading = ref(true);
    const error = ref(null);
    const mostrarModal = ref(false);

    const fichaEnEdicion = ref({
      idFicha: null,
      idPaciente: "",
      idServicio: "",
      numeroAfiliacion: "",
      codigoSeguro: "",
      carnetSeguro: "",
      historialServicios: "",
    });

    const cargarFichas = async () => {
      try {
        fichas.value = await fichasApi.getAllFichas();
      } catch (err) {
        error.value = "Error al cargar fichas.";
      } finally {
        loading.value = false;
      }
    };

    const cargarPacientes = async () => {
      try {
        const res = await pacienteService.getAllPaciente();
        pacientes.value = res.data;
      } catch (err) {
        console.error("Error al cargar pacientes:", err);
      }
    };

    onMounted(() => {
      cargarFichas();
      cargarPacientes();
    });

    const fichasFiltradas = computed(() => fichas.value);

    const abrirModalCrear = () => {
      fichaEnEdicion.value = {
        idFicha: null,
        idPaciente: "",
        idServicio: "",
        numeroAfiliacion: "",
        codigoSeguro: "",
        carnetSeguro: "",
        historialServicios: "",
      };
      busquedaPaciente.value = "";
      pacientesFiltrados.value = [];
      mostrarModal.value = true;
    };

    const abrirModalEditar = (ficha) => {
      fichaEnEdicion.value = {
        idFicha: ficha.idFicha,
        idPaciente: ficha.paciente.idPaciente,
        idServicio: ficha.idServicio,
        numeroAfiliacion: ficha.numeroAfiliacion,
        codigoSeguro: ficha.codigoSeguro,
        carnetSeguro: ficha.carnetSeguro,
        historialServicios: ficha.historialServicios,
      };
      mostrarModal.value = true;
    };

    const cerrarModal = () => {
      mostrarModal.value = false;
    };

    const guardarFicha = async () => {
      try {
        if (fichaEnEdicion.value.idFicha) {
          await fichasApi.updateFicha(fichaEnEdicion.value.idFicha, fichaEnEdicion.value);
        } else {
          await fichasApi.registrarFicha(fichaEnEdicion.value);
        }
        await cargarFichas();
        cerrarModal();
      } catch (err) {
        alert("Error al guardar la ficha.");
        console.error(err);
      }
    };

    const eliminarFicha = async (idFicha) => {
      if (confirm("¿Deseas eliminar esta ficha?")) {
        try {
          await fichasApi.deleteFicha(idFicha);
          await cargarFichas();
        } catch (err) {
          alert("Error al eliminar ficha.");
        }
      }
    };

    const filtrarPacientes = () => {
      const query = busquedaPaciente.value.toLowerCase();
      pacientesFiltrados.value = pacientes.value.filter(p =>
        p.usuario.nombreUsuario.toLowerCase().includes(query) ||
        p.documento.toLowerCase().includes(query)
      );
    };

    const seleccionarPaciente = (paciente) => {
      fichaEnEdicion.value.idPaciente = paciente.idPaciente;
      busquedaPaciente.value = `${paciente.usuario.nombreUsuario} (${paciente.documento})`;
      pacientesFiltrados.value = [];
    };

    return {
      fichasFiltradas,
      loading,
      error,
      mostrarModal,
      fichaEnEdicion,
      abrirModalCrear,
      abrirModalEditar,
      cerrarModal,
      guardarFicha,
      eliminarFicha,
      pacientesFiltrados,
      busquedaPaciente,
      filtrarPacientes,
      seleccionarPaciente,
    };
  },
};
</script>

<style scoped>
/* Estilos de Contenedor */
.container {
  max-width: 95%; /* Extender a casi todo el ancho de la pantalla */
  margin: auto;
  font-family: Arial, sans-serif;
  background: #f9f9f9;
  color: #e0e1dd;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.4);
}

/* Estilo del Título */
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

/* Estilo de Botones */
.actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px;
}
.search-container {
  display: flex;
  align-items: center;
  justify-content: flex-end; /* Alinea el buscador a la derecha */
  gap: 10px; /* Espaciado entre el input y el botón */
  width: 100%;
}
input {
  padding: 7px;
  width: 250px;
  background: #B2F2BB;
  border-radius: 5px;
  border: none;
  text-align: center;
}

button {
  background: #B2F2BB;
  color: #012030;
  padding: 10px 15px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

button:hover {
  background: #B2F2BB;
}

/* Sección de Tablas */
.section {
  border: 1px solid #45C4B0;
  padding: 15px;
  margin: 10px 0;
  background: #13678a;
  border-radius: 8px;
}

/* Ajuste de la tabla para mayor espacio */
table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 10px;
  background: #13678A;
  color: white;
  table-layout: fixed; /* Mantiene el ancho fijo y evita distorsiones */
}

th, td {
  border: 1px solid #DAFDBA;
  padding: 12px;
  text-align: center;
  min-width: 100px; /* Evita que las columnas se achiquen demasiado */
  white-space: nowrap; /* Evita que el texto se divida en varias líneas */
}

th {
  background: #01324b;
}

/* Ajuste de los botones dentro de la celda de Acciones */
td .actions {
  display: flex;
  justify-content: center;
  gap: 8px; /* Espacio entre botones */
}

td .actions button {
  padding: 6px 10px;
  font-size: 14px;
  display: flex;
  align-items: center;
}

/* Estado de Ficha Activa/Inactiva */
.activa {
  color: #DAFDBA;
  font-weight: bold;
}

.inactiva {
  color: #ff8a7d;
  font-weight: bold;
}




</style>
