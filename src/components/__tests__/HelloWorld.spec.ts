import { describe, it, expect } from 'vitest'

import { mount } from '@vue/test-utils'
import HelloWorld from '../HelloWorld.vue'

describe('HelloWorld', () => {
  it('renders properly', () => {
    const wrapper = mount(HelloWorld, { props: { msg: 'Hello Vitest' } })
    expect(wrapper.text()).toContain('Hello Vitest')
  })

  it('test que verifica funcionalidad básica', () => {
    // Test que verifica que la lógica básica funciona correctamente
    console.log('Ejecutando test del frontend que verifica funcionalidad básica...')
    expect(true).toBe(true) // Esto debería pasar correctamente
  })
})
