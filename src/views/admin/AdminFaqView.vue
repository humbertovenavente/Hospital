<template>
  <div class="admin-faq">
    <div class="header">Administrador de Preguntas Frecuentes (FAQ)</div>

    <div class="faq-item" v-for="(faq, index) in faqs" :key="faq.id || index">
      <input
        v-model="faq.pregunta"
        placeholder="Pregunta"
        class="faq-input"
      />
      <textarea
        v-model="faq.respuesta"
        placeholder="Respuesta"
        class="faq-textarea"
      ></textarea>

      <div class="faq-actions">
        <button @click="guardar(faq)">💾 Guardar</button>
        <button @click="enviarAModeracion(faq)">📤 Enviar a Moderación</button>
      </div>
    </div>

    <button class="add-btn" @click="agregarFaq">➕ Agregar nueva pregunta</button>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import axios from "axios";
import API_URL from "@/config";

const faqs = ref([]);

const loadFaqs = async () => {
  try {
    const response = await axios.get(`${API_URL}/faq`);
    faqs.value = response.data;
  } catch (error) {
    console.error("Error al cargar FAQs:", error);
  }
};

const guardar = async (faq) => {
  try {
    const usuarioEmail = localStorage.getItem("usuarioEmail");
    if (!usuarioEmail) throw new Error("No se encontró el email del editor");

    faq.editadoPor = usuarioEmail;
    if (!faq.autor) {
      faq.autor = usuarioEmail;
    }

    faq.status = faq.status || "PROCESO";

    if (faq.id) {
      await axios.put(`${API_URL}/faq/editar/${faq.id}`, faq);
    } else {
      await axios.post(`${API_URL}/faq/crear`, faq);
    }

    alert("✅ Pregunta guardada correctamente.");
    await loadFaqs();
  } catch (error) {
    console.error("Error al guardar FAQ:", error);
    alert("❌ Error al guardar FAQ.");
  }
};

const enviarAModeracion = async (faq) => {
  try {
    const usuarioEmail = localStorage.getItem("usuarioEmail");
    if (!usuarioEmail) throw new Error("No se encontró el email del editor");

    faq.status = "PROCESO";
    faq.rejectionReason = null;
    faq.editadoPor = usuarioEmail;
    if (!faq.autor) {
      faq.autor = usuarioEmail;
    }

    if (faq.id) {
      await axios.put(`${API_URL}/faq/editar/${faq.id}`, faq);
    } else {
      await axios.post(`${API_URL}/faq/crear`, faq);
    }

    alert("📤 Pregunta enviada a moderación.");
    await loadFaqs();
  } catch (error) {
    console.error("Error al enviar FAQ:", error);
    alert("❌ No se pudo enviar la pregunta.");
  }
};

const agregarFaq = () => {
  const usuarioEmail = localStorage.getItem("usuarioEmail") || "";

  faqs.value.push({
    pregunta: "",
    respuesta: "",
    autor: usuarioEmail,
    editadoPor: usuarioEmail,
    status: "PROCESO",
  });
};

onMounted(loadFaqs);
</script>


<style scoped>
.admin-faq {
  max-width: 800px;
  margin: 0 auto;
  padding: 2rem;
  color: white;
}

.header {
  text-align: center;
  font-size: 24px;
  background-color: #026e81;
  padding: 1rem;
  border-radius: 8px;
  margin-bottom: 2rem;
}

.faq-item {
  background-color: #13678a;
  padding: 1rem;
  margin-bottom: 1rem;
  border-radius: 6px;
  border: 1px solid #00abbc;
}

.faq-input,
.faq-textarea {
  width: 100%;
  margin-bottom: 0.5rem;
  padding: 0.6rem;
  border-radius: 4px;
  border: 1px solid #ccc;
  background: #0f2e3d;
  color: white;
}

.faq-textarea {
  min-height: 80px;
  resize: vertical;
}

.faq-actions {
  display: flex;
  gap: 1rem;
  margin-top: 0.5rem;
}

.faq-actions button {
  padding: 0.5rem 1rem;
  border-radius: 4px;
  border: none;
  font-weight: bold;
  cursor: pointer;
  color: white;
}

.faq-actions button:first-child {
  background-color: #00bb77;
}

.faq-actions button:last-child {
  background-color: #ff9933;
}

.add-btn {
  margin-top: 1rem;
  background-color: #0099dd;
  padding: 0.7rem 1.2rem;
  border-radius: 6px;
  font-weight: bold;
  color: white;
  border: none;
  cursor: pointer;
}
.add-btn:hover {
  background-color: #007bbd;
}
</style>
