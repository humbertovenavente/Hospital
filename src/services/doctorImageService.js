import axios from "axios";
import API_URL from "../config";

const DOCTOR_IMAGES_API = `${API_URL}/api/doctor-images`;

export default {
  async getDoctorImage(idDoctor) {
    return await axios.get(`${DOCTOR_IMAGES_API}/${idDoctor}`);
  },

  async updateDoctorImage(idDoctor, file, fileTitle) {
    const formData = new FormData();
    formData.append("fotografia", file);
    formData.append("fotoTitulo", fileTitle);

    return await axios.put(`${DOCTOR_IMAGES_API}/${idDoctor}/update`, formData, {
      headers: { "Content-Type": "multipart/form-data" }
    });
  },

  async deleteDoctorImage(idDoctor) {
    return await axios.delete(`${DOCTOR_IMAGES_API}/${idDoctor}/delete`);
  }
};
