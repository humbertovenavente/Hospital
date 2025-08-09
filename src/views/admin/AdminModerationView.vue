<template>
    <div class="moderation-view">
      <h1>Moderación de Contenido</h1>

      <!-- 🔹 Contenido Dinámico (PageContent) -->
      <section>
        <h2>Páginas Informativas</h2>
        <div v-if="pageContents.length === 0" class="empty">No hay contenido pendiente.</div>
        <div v-for="item in pageContents" :key="item.idContent" class="card">
          <h3>{{ item.contentTitle }}</h3>
          <p><strong>Página:</strong> {{ item.pageName }}</p>
          <p v-html="item.contentBody"></p>
          <img v-if="item.image" :src="`data:image/jpeg;base64,${item.image}`" alt="Imagen" />
          <div class="actions">
            <button @click="aprobarPageContent(item.idContent)">✅ Aprobar</button>
            <button @click="rechazarPageContent(item.idContent)">❌ Rechazar</button>
          </div>
        </div>
      </section>

      <!-- 🔹 FAQ -->
      <section>
        <h2>Preguntas Frecuentes (FAQ)</h2>
        <div v-if="faqs.length === 0" class="empty">No hay preguntas pendientes.</div>
        <div v-for="faq in faqs" :key="faq.id" class="card">
          <h3>{{ faq.pregunta }}</h3>
          <p><strong>Autor:</strong> {{ faq.autor }}</p>
          <p><strong>Respuesta:</strong> {{ faq.respuesta || '---' }}</p>
          <div class="actions">
            <button @click="aprobarFaq(faq.id)">✅ Aprobar</button>
            <button @click="rechazarFaq(faq.id)">❌ Rechazar</button>
          </div>
        </div>
      </section>

      <!-- 🔹 Historia -->
      <section>
        <h2>Historias Institucionales</h2>
        <div v-if="historias.length === 0" class="empty">No hay historias pendientes.</div>
        <div v-for="h in historias" :key="h.id" class="card">
          <h3>{{ h.nombreEntidad }}</h3>
          <p><strong>Historia:</strong> {{ h.historia }}</p>
          <p><strong>Méritos:</strong> {{ h.meritos }}</p>
          <p><strong>Línea del Tiempo:</strong> {{ h.lineaDelTiempo }}</p>
          <div class="actions">
            <button @click="aprobarHistoria(h.id)">✅ Aprobar</button>
            <button @click="rechazarHistoria(h.id)">❌ Rechazar</button>
          </div>
        </div>
      </section>
    </div>
  </template>

  <script setup>
  import { sendEmail } from "@/services/rechazoModeracion";
  import { ref, onMounted } from "vue";
  import {
    getPendingContent,
    publishContent,
    rejectContent
  } from "@/services/pageContentService";
  import {
    obtenerPendientesModeracion as obtenerFaqPendientes,
    aprobarPregunta,
    rechazarPregunta
  } from "@/services/faqService";
  import {
    getHistoriasPendientes,
    aprobarHistoria as aprobarHistoriaAPI,
    rechazarHistoria as rechazarHistoriaAPI
  } from "@/services/historiaService";

  const pageContents = ref([]);
  const faqs = ref([]);
  const historias = ref([]);

  // 📥 Cargar todo el contenido pendiente
  const cargarTodo = async () => {
    pageContents.value = await getPendingContent();
    faqs.value = await obtenerFaqPendientes();
    historias.value = await getHistoriasPendientes();
  };

  onMounted(cargarTodo);

  // ✅ Aprobar PageContent
  const aprobarPageContent = async (id) => {
    await publishContent(id);
    await cargarTodo();
  };

  // ❌ Rechazar PageContent
  const rechazarPageContent = async (id) => {
  const motivo = prompt("Motivo del rechazo:");
  if (!motivo) return;

  const item = pageContents.value.find(c => c.idContent === id);
  const email = item?.editorEmail;

  if (!email) {
    alert("❌ No se encontró el correo del editor.");
    return;
  }

  const draftLink = `http://192.168.1.4:5173/drafts/${id}?email=${encodeURIComponent(email)}`;

  await rejectContent(id, motivo);

  await sendEmail(email, "rejected", {
    comentario: motivo,
    pagina: item.pageName,
    link: draftLink
  });

  alert("Contenido rechazado y notificación enviada.");
  await cargarTodo();
};

  // ✅ FAQ
  const aprobarFaq = async (id) => {
    await aprobarPregunta(id);
    await cargarTodo();
  };

const rechazarFaq = async (id) => {
  const motivo = prompt("Motivo del rechazo:");
  if (!motivo) return;

  const faq = faqs.value.find(f => f.id === id);
  const email = faq?.editadoPor;

  if (!email) {
    alert("❌ No se encontró el correo del editor.");
    return;
  }

  await rechazarPregunta(id, motivo);

  await sendEmail(email, "rejected", {
  comentario: motivo,
  pagina: "FAQ",
  link: `http://192.168.1.4:5173/drafts/${id}?email=${encodeURIComponent(email)}` // antes era admin-faq
});


  alert("Pregunta rechazada y correo enviado.");
  await cargarTodo();
};


  // ✅ Historia
  const aprobarHistoria = async (id) => {
    await aprobarHistoriaAPI(id);
    await cargarTodo();
  };

  const rechazarHistoria = async (id) => {
  const motivo = prompt("Motivo del rechazo:");
  if (!motivo) return;

  const historia = historias.value.find(h => h.id === id);
  const email = historia?.editorEmail;

  if (!email) {
    alert("❌ No se encontró el correo del editor.");
    return;
  }

  await rechazarHistoriaAPI(id, motivo);

  await sendEmail(email, "rejected", {
  comentario: motivo,
  pagina: "Historia Institucional",
  link: `http://192.168.1.4:5173/drafts/${id}?email=${encodeURIComponent(email)}` // antes era admin-historia
});


  alert("Historia rechazada y correo enviado.");
  await cargarTodo();
};



  </script>

  <style scoped>
  .moderation-view {
    max-width: 900px;
    margin: 0 auto;
    padding: 2rem;
    color: white;
  }

  h1 {
    text-align: center;
    font-size: 2rem;
    margin-bottom: 2rem;
    color: #00abbc;
  }

  section {
    margin-bottom: 3rem;
  }

  .card {
    background: #13678a;
    padding: 1rem;
    border-radius: 8px;
    margin-bottom: 1rem;
    border: 1px solid #00abbc;
  }

  .card h3 {
    margin-top: 0;
    color: #fff;
  }

  .card img {
    max-width: 100%;
    margin: 1rem 0;
    border-radius: 5px;
  }

  .actions {
    display: flex;
    gap: 1rem;
    margin-top: 1rem;
  }

  button {
    padding: 0.5rem 1rem;
    border: none;
    border-radius: 5px;
    font-weight: bold;
    cursor: pointer;
    color: white;
  }

  button:hover {
    opacity: 0.9;
  }

  button:nth-child(1) {
    background-color: #00bb77;
  }

  button:nth-child(2) {
    background-color: #ff4c4c;
  }

  .empty {
    font-style: italic;
    color: #ccc;
  }
  </style>
