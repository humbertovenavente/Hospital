import './assets/main.css'
import { createApp } from 'vue'
import { createPinia } from 'pinia'
import axios from 'axios'
import API_URL from "@/config";
import App from './App.vue'
import router from './router'

axios.defaults.baseURL = API_URL

const app = createApp(App)

app.use(createPinia())
app.use(router)

app.mount('#app')
