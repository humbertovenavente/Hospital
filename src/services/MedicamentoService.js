import axios from 'axios';
import API_URL from '@/config';

const BASE_URL = `${API_URL}/medicamentos`;

const MedicamentoService = {
  getAll() {
    return axios.get(BASE_URL);
  },
  getById(id) {
    return axios.get(`${BASE_URL}/${id}`);
  },
  create(medicamento) {
    return axios.post(BASE_URL, medicamento);
  },
  update(id, medicamento) {
    return axios.put(`${BASE_URL}/${id}`, medicamento);
  },
  delete(id) {
    return axios.delete(`${BASE_URL}/${id}`);
  },
};

export default MedicamentoService;
