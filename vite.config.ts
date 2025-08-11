import { fileURLToPath, URL } from 'node:url'

import { defineConfig, loadEnv } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueJsx from '@vitejs/plugin-vue-jsx'
import vueDevTools from 'vite-plugin-vue-devtools'
import tailwindcss from '@tailwindcss/vite'

// https://vite.dev/config/
export default defineConfig(({ command, mode }) => {
  // Cargar variables de entorno basadas en el modo
  const env = loadEnv(mode, process.cwd(), '')
  
  return {
    plugins: [
      vue(),
      vueJsx(),
      vueDevTools(),
      tailwindcss(),
    ],
    resolve: {
      alias: {
        '@': fileURLToPath(new URL('./src', import.meta.url))
      },
    },
    // Configuración específica para QA
    server: {
      port: mode === 'qa' ? 5175 : 5174,
      host: true
    },
    // Variables de entorno para el frontend
    define: {
      __APP_ENV__: JSON.stringify(mode),
      __API_URL__: JSON.stringify(
        mode === 'qa' ? 'http://localhost:8082' : 'http://localhost:8080'
      )
    }
  }
})
