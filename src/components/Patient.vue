<template>
  <div class="recetas-paciente">
    <h2>Mis Recetas Médicas</h2>

    <div v-if="cargando">Cargando recetas...</div>
    <div v-else-if="recetas.length === 0">No tienes recetas activas registradas.</div>

    <div v-else>
      <div v-for="receta in recetas" :key="receta.idReceta" class="receta">
        <h3>{{ receta.diagnostico || 'Diagnóstico no especificado' }}</h3>
        <p><strong>Fecha:</strong> {{ formatFecha(receta.fecha) }}</p>
        <p><strong>Médico:</strong> {{ receta.doctorNombre || 'Desconocido' }}</p>
        <p><strong>Observaciones:</strong> {{ receta.observaciones || 'Sin observaciones' }}</p>
        <p><strong>Estado:</strong>
          <span :class="estadoClase(receta.estado)">
            {{ receta.estado || 'Sin estado' }}
          </span>
        </p>

        <h4>Medicamentos</h4>
        <ul v-if="receta.detalleMedicamentos && receta.detalleMedicamentos.length > 0">
          <li v-for="(med, index) in receta.detalleMedicamentos" :key="index">
            {{ med.principioActivo || 'Desconocido' }} -
            {{ med.dosis || 'Dosis no especificada' }} cada
            {{ med.frecuencia || 'Frecuencia no especificada' }} por
            {{ med.duracion || 'Duración no especificada' }} días
          </li>
        </ul>
        <p v-else>No hay medicamentos asociados a esta receta.</p>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      recetas: [],
      cargando: true
    };
  },
  async created() {
    await this.cargarRecetas();
  },
  methods: {
    async cargarRecetas() {
      try {
        const idPaciente = this.$store?.state?.user?.idPaciente;
        if (!idPaciente) {
          alert('ID del paciente no disponible. Asegúrate de iniciar sesión.');
          this.cargando = false;
          return;
        }

        // Llamada a la API correcta
        const response = await fetch("http://localhost:8080/recetas/paciente/${idPaciente}/activas");
        if (response.status === 204) {
          this.recetas = [];
          return;
        }
        if (!response.ok) {
          const errorData = await response.json();
          throw new Error(errorData.mensaje || 'Error al cargar recetas');
        }

        this.recetas = await response.json();
      } catch (error) {
        console.error('Error al cargar recetas:', error);
        alert('Error al obtener recetas. Intenta nuevamente.');
      } finally {
        this.cargando = false;
      }
    },
    formatFecha(fecha) {
      if (!fecha) return 'Fecha no disponible';
      return new Date(fecha).toLocaleDateString('es-ES', {
        year: 'numeric',
        month: 'long',
        day: 'numeric'
      });
    },
    estadoClase(estado) {
      const clases = {
        activa: 'estado-activa',
        completada: 'estado-completada',
        cancelada: 'estado-cancelada'
      };
      return clases[estado] || 'estado-desconocido';
    }
  }
};
</script>

<style scoped>
.recetas-paciente {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 10px;
  background-color: #eaf6ff;
}

.receta {
  margin-bottom: 15px;
  border: 1px solid #bbb;
  padding: 15px;
  border-radius: 10px;
  background-color: #fff;
}

.estado-activa {
  color: green;
  font-weight: bold;
}

.estado-completada {
  color: blue;
  font-weight: bold;
}

.estado-cancelada {
  color: red;
  font-weight: bold;
}
</style>
