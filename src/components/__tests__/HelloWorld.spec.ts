import { describe, it, expect } from 'vitest'

import { mount } from '@vue/test-utils'
import HelloWorld from '../HelloWorld.vue'

describe('HelloWorld', () => {
  it('renders properly', () => {
    const wrapper = mount(HelloWorld, { props: { msg: 'Hello Vitest' } })
    expect(wrapper.text()).toContain('Hello Vitest')
  })

  it('test que falla intencionalmente para probar notificaciones', () => {
    // Test que falla intencionalmente para probar las notificaciones mejoradas de Drone CI
    console.log('Ejecutando test del frontend que fallará intencionalmente...')
    expect(true).toBe(false) // Esto fallará intencionalmente
  })
})
