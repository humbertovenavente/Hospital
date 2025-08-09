<template>
  <div class="container">
    <!-- Título -->
    <div class="header">CREAR NUEVA FICHA TÉCNICA</div>

    <!-- Opciones de Gestión -->
    <div class="actions">
      <button @click="crearFicha">Crear Nueva Ficha</button>
      <button @click="editarFicha">Editar Ficha</button>
      <button @click="eliminarFicha">Eliminar Ficha</button>
      <button @click="verFicha"> Ver Ficha</button>
    </div>

    <!-- Listado de Fichas Técnicas -->
    <div class="section">
      <h3>📋 LISTADO DE FICHAS TÉCNICAS</h3>
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>Nombre</th>
            <th>Fecha Creada</th>
            <th>Última Modif.</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="ficha in fichas" :key="ficha.id">
            <td>{{ ficha.id }}</td>
            <td>{{ ficha.nombre }}</td>
            <td>{{ ficha.fechaCreada }}</td>
            <td>{{ ficha.ultimaModif }}</td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Datos del Paciente -->
    <div class="section">
      <h3>🧑‍⚕️ DATOS DEL PACIENTE</h3>
      <div class="form-group">
        <label>Nombre:</label>
        <input v-model="paciente.nombre">
        <label>ID:</label>
        <input v-model="paciente.id">
      </div>
      <div class="form-group">
        <label>Fecha de Nacimiento:</label>
        <input type="date" v-model="paciente.fechaNacimiento">
      </div>
      <div class="form-group">
        <label>Fotografía:</label>
        <input type="file">
      </div>
      <div class="form-group">
        <label>Documento de Identificación:</label>
        <input v-model="paciente.documentoIdentidad">
      </div>
      <div class="form-group">
        <label>Número de Afiliación al Seguro:</label>
        <input v-model="paciente.afiliacionSeguro">
        <label>Código de Seguro:</label>
        <input v-model="paciente.codigoSeguro">
      </div>
      <div class="form-group">
        <label>Número de Carnet de Seguro:</label>
        <input v-model="paciente.afiliacionSeguro">
        <label>Código de Seguro:</label>
        <input v-model="paciente.codigoSeguro">
      </div>
    </div>

    <!-- Historial de Servicios Médicos -->
    <div class="section">
      <h3>🏥 HISTORIAL DE SERVICIOS MÉDICOS</h3>
      <table>
        <thead>
          <tr>
            <th>Fecha</th>
            <th>Servicio</th>
            <th>Médico</th>
            <th>Costo</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="servicio in historial" :key="servicio.fecha">
            <td>{{ servicio.fecha }}</td>
            <td>{{ servicio.servicio }}</td>
            <td>{{ servicio.medico }}</td>
            <td>Q{{ servicio.costo }}</td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Comentarios Médicos -->
    <div class="section">
      <h3>💬 COMENTARIOS MÉDICOS Y SEGUIMIENTO</h3>
      <div v-for="comentario in comentarios" :key="comentario.id" class="comentario">
        <b>{{ comentario.autor }}</b> ({{ comentario.fecha }}): {{ comentario.texto }}
      </div>
      <button @click="agregarComentario">💬 Agregar Comentario</button>
    </div>

    <!-- Botones de Acción -->
    <div class="actions">
      <button @click="guardarFicha">💾 Guardar</button>
      <button @click="editarFicha">🖊️ Editar</button>
      <button @click="eliminarFicha">🗑️ Eliminar</button>
      <button @click="exportarPDF">📄 Exportar PDF</button>
      <button @click="volver">⬅️ Volver</button>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      buscarId: "",
      fichas: [
        { id: "001", nombre: "Juan Pérez", fechaCreada: "12/02/2025", ultimaModif: "18/02/2025" },
        { id: "002", nombre: "María López", fechaCreada: "14/02/2025", ultimaModif: "17/02/2025" },
        { id: "003", nombre: "Carlos Gómez", fechaCreada: "16/02/2025", ultimaModif: "20/02/2025" },
      ],
      paciente: {
        nombre: "Juan Pérez",
        id: "001",
        fechaNacimiento: "1990-01-15",
        documentoIdentidad: "DPI-123456789",
        afiliacionSeguro: "987654",
        codigoSeguro: "A1B2C3",
      },
      historial: [
        { fecha: "10/02/2025", servicio: "Consulta General", medico: "Dr. Pérez", costo: "200.00" },
        { fecha: "15/02/2025", servicio: "Examen de Sangre", medico: "Dr. Gómez", costo: "150.00" },
        { fecha: "20/02/2025", servicio: "Cirugía", medico: "Dr. López", costo: "5000.00" },
      ],
      comentarios: [
        { id: 1, autor: "Dr. Pérez", fecha: "10/02/2025", texto: "El paciente muestra mejoría." },
        { id: 2, autor: "Enfermera López", fecha: "11/02/2025", texto: "Se han ajustado los medicamentos según lo indicado." },
      ],
    };
  },
  methods: {
    crearFicha() { alert("Crear nueva ficha en desarrollo."); },
    editarFicha() { alert("Editar ficha en desarrollo."); },
    eliminarFicha() { alert("Eliminar ficha en desarrollo."); },
    verFicha() { alert("Ver ficha en desarrollo."); },
    buscarFicha() { alert(`Buscando ficha con ID: ${this.buscarId}`); },
    agregarComentario() { alert("Agregar comentario en desarrollo."); },
    guardarFicha() { alert("Guardar ficha en desarrollo."); },
    exportarPDF() { alert("Exportar a PDF en desarrollo."); },
    volver() { alert("Volviendo a la lista."); },
  }
};
</script>

<style scoped>

.container {
  max-width: 95%;
  margin:  auto;
  font-family: Arial, sans-serif;
  background: #f9f9f9;
  color: #e0e1dd;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.4);
}

/* ============================= */
/* Cabecera (Título)            */
/* ============================= */
.header {
  text-align: center;
  font-size: 22px;
  font-weight: bold;
  background: #45C4B0;
  color: white;
  padding: 12px;
  border-radius: 6px;
  margin-bottom: 1rem;
  box-shadow: 0 1px 4px rgba(0,0,0,0.3);
}

/* ============================= */
/* Secciones (Card / Box)       */
/* ============================= */
.section {
  background: var(--color-teal-oscuro);
  padding: 15px;
  margin: 15px 0;
  border-radius: 5px;
  border: 1px solid var(--color-teal-medio);
  box-shadow: 0 1px 4px rgba(0,0,0,0.3);
}

/* ============================= */
/* Barra de acciones            */
/* ============================= */
.actions {
  display: flex;
  flex-wrap: wrap;
  display: flex;
  gap: 0.5rem;
  justify-content: space-around;
  margin-bottom: 1rem;
}

/* ============================= */
/* Botones Generales            */
/* ============================= */
button {
  background: #B2F2BB;
  color: #012030;
  padding: 10px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-weight: 600;
  transition: background-color 0.2s, transform 0.2s;
  display: flex;
  align-items: center;
  gap: 0.3rem;
}

button:hover {
  background: #DAFDBA;
}
button:active {
  transform: scale(0.97);
}

/* Botón de Eliminar (rojo) */
button.eliminar {
  background: #ff8a7d;  /* Rojo Alizarin */
  color: #fff;
}
button.eliminar:hover {
  background: #ff8a7d;  /* Rojo más oscuro */
}

/* ============================= */
/* Tablas                        */
/* ============================= */
table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 10px;
  background: #13678A;
  color: white;
  border-radius: 5px;
  overflow: hidden;
  box-shadow: 0 1px 4px rgba(0,0,0,0.3);
}

/* Encabezados de tabla */
th {
  background: #01324b;
  font-weight: bold;
  padding: 12px;
  text-align: center;
  border: 1px solid #DAFDBA;
  white-space: nowrap;
}

/* Celdas */
td {
  border: 1px solid var(--color-teal-medio);
  padding: 10px;
  text-align: center;
  white-space: nowrap;
  border: 1px solid #DAFDBA;
}

/* Zebra rows (opcional) */
tbody tr:nth-child(even) {
  background-color: rgba(255,255,255,0.04);
}

/* ============================= */
/* Inputs, selects, textareas   */
/* ============================= */
input, select, textarea {
  width: 100%;
  padding: 8px;
  margin-top: 5px;
  border: 1px solid var(--color-teal-medio);
  background: var(--color-azul-noche);
  color: var(--color-verde-pastel);
  border-radius: 5px;
  font-size: 1rem;
  box-sizing: border-box;
}

.search-container {
  display: flex;
  align-items: center;
  justify-content: flex-end; /* Alinea el buscador a la derecha */
  gap: 10px; /* Espaciado entre el input y el botón */
  width: 100%;
}

/* ============================= */
/* Comentarios                  */
/* ============================= */
.comentario {
  background: var(--color-azul-noche);
  padding: 10px;
  margin: 5px 0;
  border-left: 4px solid var(--color-verde-menta);
  border-radius: 5px;
}
.comentario b {
  color: var(--color-verde-pastel);
}
</style>


