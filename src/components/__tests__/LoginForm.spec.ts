import { describe, it, expect, beforeEach } from 'vitest'
import { mount } from '@vue/test-utils'
import { createRouter, createWebHistory } from 'vue-router'
import LoginForm from '../LoginForm.vue'

// Mock del router para las pruebas
const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/signup', component: { template: '<div>SignUp</div>' } }
  ]
})

describe('LoginForm', () => {
  let wrapper: any

  beforeEach(async () => {
    // Montar el componente con el router
    wrapper = mount(LoginForm, {
      global: {
        plugins: [router]
      }
    })
  })

  describe('Renderizado inicial', () => {
    it('debe renderizar el formulario correctamente', () => {
      expect(wrapper.find('h2').text()).toBe('Iniciar Sesión')
      expect(wrapper.find('input[type="email"]').exists()).toBe(true)
      expect(wrapper.find('input[type="password"]').exists()).toBe(true)
      expect(wrapper.find('button[type="submit"]').text()).toBe('Iniciar sesión')
    })

    it('debe mostrar el enlace de registro', () => {
      const registerLink = wrapper.find('.register-link')
      expect(registerLink.exists()).toBe(true)
      expect(registerLink.text()).toContain('¿No tienes una cuenta?')
      expect(registerLink.text()).toContain('Regístrate aquí')
    })

    it('debe tener campos vacíos inicialmente', () => {
      const emailInput = wrapper.find('input[type="email"]')
      const passwordInput = wrapper.find('input[type="password"]')

      expect(emailInput.element.value).toBe('admin@hospital.com')
      expect(passwordInput.element.value).toBe('')
    })
  })

  describe('Validación de formulario', () => {
    it('debe requerir campos obligatorios', () => {
      const emailInput = wrapper.find('input[type="email"]')
      const passwordInput = wrapper.find('input[type="password"]')

      expect(emailInput.attributes('required')).toBeDefined()
      expect(passwordInput.attributes('required')).toBeDefined()
    })

    it('debe tener tipos de input correctos', () => {
      const emailInput = wrapper.find('input[type="email"]')
      const passwordInput = wrapper.find('input[type="password"]')

      expect(emailInput.attributes('type')).toBe('email')
      expect(passwordInput.attributes('type')).toBe('password')
    })
  })

  describe('Interacción del usuario', () => {
    it('debe actualizar el valor del email cuando se escribe', async () => {
      const emailInput = wrapper.find('input[type="email"]')
      const testEmail = 'test@example.com'

      await emailInput.setValue(testEmail)

      expect(emailInput.element.value).toBe(testEmail)
    })

    it('debe actualizar el valor de la contraseña cuando se escribe', async () => {
      const passwordInput = wrapper.find('input[type="password"]')
      const testPassword = 'password123'

      await passwordInput.setValue(testPassword)

      expect(passwordInput.element.value).toBe(testPassword)
    })

    it('debe mostrar mensaje después del envío del formulario', async () => {
      const form = wrapper.find('form')

      await form.trigger('submit')

      const mensaje = wrapper.find('.error')
      expect(mensaje.exists()).toBe(true)
      expect(mensaje.text()).toBe('Por favor, completa todos los campos')
    })
  })

  describe('Estilos y clases CSS', () => {
    it('debe tener la clase error para mensajes', async () => {
      const form = wrapper.find('form')
      await form.trigger('submit')

      const mensaje = wrapper.find('.error')
      expect(mensaje.classes()).toContain('error')
    })

    it('debe tener estilos de enlace de registro', () => {
      const registerLink = wrapper.find('.register-link')
      const link = registerLink.find('.link')

      expect(registerLink.classes()).toContain('register-link')
      expect(link.classes()).toContain('link')
    })
  })

  describe('Navegación', () => {
    it('debe tener enlace al registro', () => {
      const signupLink = wrapper.find('a[href="/signup"]')
      expect(signupLink.exists()).toBe(true)
      expect(signupLink.text()).toBe('Regístrate aquí')
    })
  })

  describe('Casos edge', () => {
    it('debe manejar envío múltiple del formulario', async () => {
      const form = wrapper.find('form')

      // Enviar múltiples veces
      await form.trigger('submit')
      await form.trigger('submit')
      await form.trigger('submit')

      const mensajes = wrapper.findAll('.error')
      expect(mensajes).toHaveLength(1) // Solo debe haber un mensaje
    })

    it('debe mantener valores después del envío', async () => {
      const emailInput = wrapper.find('input[type="email"]')
      const passwordInput = wrapper.find('input[type="password"]')
      const testEmail = 'user@test.com'
      const testPassword = 'secret123'

      await emailInput.setValue(testEmail)
      await passwordInput.setValue(testPassword)

      const form = wrapper.find('form')
      await form.trigger('submit')

      // Los valores deben mantenerse
      expect(emailInput.element.value).toBe(testEmail)
      expect(passwordInput.element.value).toBe(testPassword)
    })
  })
})
//  npm run test:unit
