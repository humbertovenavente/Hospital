<template>
  <div class="container">
    <h1>Gestión de Imágenes de Doctor</h1>
    <table>
      <thead>
        <tr>
          <th>ID Doctor</th>
          <th>Número Colegiado</th>
          <th>Foto Título</th>
          <th>Fotografía</th>
          <th>Acciones</th>
        </tr>
      </thead>
      <tbody>
        <tr v-if="doctor">
          <td>{{ doctor.idDoctor }}</td>
          <td>{{ doctor.numeroColegiado }}</td>
          <td><img :src="doctor.fotoTitulo" width="100" /></td>
          <td><img :src="doctor.fotografia" width="100" /></td>
          <td>
            <input type="file" @change="handleFileUpload" />
            <button @click="updateImage">Actualizar</button>
            <button @click="deleteImage">Eliminar</button>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
import doctorImageService from "../services/doctorImageService";

export default {
  data() {
    return {
      doctorId: 1,
      doctor: null,
      selectedFile: null
    };
  },
  methods: {
    async loadDoctorData() {
      const response = await doctorImageService.getDoctorImage(this.doctorId);
      this.doctor = response.data;
    },
    handleFileUpload(event) {
      this.selectedFile = event.target.files[0];
    },
    async updateImage() {
      await doctorImageService.updateDoctorImage(this.doctorId, this.selectedFile);
      this.loadDoctorData();
    },
    async deleteImage() {
      await doctorImageService.deleteDoctorImage(this.doctorId);
      this.loadDoctorData();
    }
  },
  mounted() {
    this.loadDoctorData();
  }
};
</script>
