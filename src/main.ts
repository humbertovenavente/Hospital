import './assets/main.css'
import { createApp } from 'vue'
import { createPinia } from 'pinia'
import axios from 'axios'
// URL de la API para QA
const config = {
  API_URL: 'http://localhost:8090'
};
import App from './App.vue'
import router from './router'

axios.defaults.baseURL = config.API_URL

const app = createApp(App)

app.use(createPinia())
app.use(router)

app.mount('#app')
