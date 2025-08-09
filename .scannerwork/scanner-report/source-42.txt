package com.unis.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.unis.model.Servicio;
import com.unis.repository.ServicioRepository;

import jakarta.persistence.EntityManager;
import jakarta.ws.rs.WebApplicationException;

public class ServicioServiceTest {

    @Mock
    private ServicioRepository servicioRepository;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private ServicioService servicioService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testListarTodos() {
        Servicio s1 = new Servicio();
        Servicio s2 = new Servicio();
        List<Servicio> expected = Arrays.asList(s1, s2);
        when(servicioRepository.listAll()).thenReturn(expected);

        List<Servicio> result = servicioService.listarTodos();

        assertEquals(expected, result);
    }

    @Test
    public void testBuscarPorIdFound() {
        Servicio s = new Servicio();
        when(servicioRepository.findById(1L)).thenReturn(s);

        Servicio result = servicioService.buscarPorId(1L);

        assertEquals(s, result);
    }

    @Test
    public void testBuscarPorIdNotFound() {
        when(servicioRepository.findById(1L)).thenReturn(null);

        Servicio result = servicioService.buscarPorId(1L);

        assertNull(result);
    }

    @Test
    public void testAgregarServicioSinPadre() {
        Servicio s = new Servicio();
        doNothing().when(servicioRepository).persist(s);

        Servicio result = servicioService.agregarServicio(s, null);

        verify(servicioRepository, times(1)).persist(s);
        assertNull(s.servicioPadre);
        assertEquals(s, result);
    }

    @Test
    public void testAgregarServicioConPadreEncontrado() {
        Servicio s = new Servicio();
        Servicio padre = new Servicio();
        padre.id = 10L;

        when(servicioRepository.findById(10L)).thenReturn(padre);
        doNothing().when(servicioRepository).persist(s);

        Servicio result = servicioService.agregarServicio(s, 10L);

        assertEquals(padre, s.servicioPadre);
        verify(servicioRepository).persist(s);
        assertEquals(s, result);
    }

    @Test
    public void testAgregarServicioConPadreNoEncontrado() {
        Servicio s = new Servicio();

        when(servicioRepository.findById(10L)).thenReturn(null);
        doNothing().when(servicioRepository).persist(s);

        Servicio result = servicioService.agregarServicio(s, 10L);

        assertNull(s.servicioPadre);
        verify(servicioRepository).persist(s);
        assertEquals(s, result);
    }

    @Test
    public void testEliminarServicioFound() {
        Servicio s = new Servicio();
        when(servicioRepository.findById(1L)).thenReturn(s);
        doNothing().when(servicioRepository).delete(s);

        servicioService.eliminarServicio(1L);

        verify(servicioRepository).delete(s);
    }

    @Test
    public void testEliminarServicioNotFound() {
        when(servicioRepository.findById(1L)).thenReturn(null);

        servicioService.eliminarServicio(1L);

        verify(servicioRepository, never()).delete(any());
    }

    @Test
    public void testListarSubServiciosFound() {
        Servicio padre = new Servicio();
        Servicio sub1 = new Servicio();
        Servicio sub2 = new Servicio();
        padre.subServicios = new HashSet<>(Arrays.asList(sub1, sub2));

        when(servicioRepository.findById(1L)).thenReturn(padre);

        List<Servicio> result = servicioService.listarSubServicios(1L);

    assertIterableEquals(Arrays.asList(sub1, sub2), result);
    }

    @Test
    public void testListarSubServiciosNotFound() {
        when(servicioRepository.findById(1L)).thenReturn(null);

        List<Servicio> result = servicioService.listarSubServicios(1L);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testAgregarSubServicioSuccess() {
        Servicio padre = new Servicio();
        padre.id = 1L;
        Servicio sub = new Servicio();
        sub.id = 2L;

        when(servicioRepository.findById(1L)).thenReturn(padre);
        when(servicioRepository.findById(2L)).thenReturn(sub);
        when(entityManager.merge(sub)).thenReturn(sub);

        servicioService.agregarSubServicio(1L, 2L);

        assertEquals(padre, sub.servicioPadre);
        verify(entityManager).merge(sub);
    }

    @Test
    public void testAgregarSubServicioNotFound() {
        when(servicioRepository.findById(1L)).thenReturn(null);
        when(servicioRepository.findById(2L)).thenReturn(new Servicio());

        WebApplicationException ex = assertThrows(WebApplicationException.class, () -> {
            servicioService.agregarSubServicio(1L, 2L);
        });

        assertEquals("Servicio o Subservicio no encontrado", ex.getMessage());
        assertEquals(404, ex.getResponse().getStatus());
    }

    @Test
    public void testEliminarRelacionSuccess() {
        Servicio padre = new Servicio();
        padre.id = 1L;

        Servicio sub = new Servicio();
        sub.id = 2L;
        sub.servicioPadre = padre;

        when(servicioRepository.findById(2L)).thenReturn(sub);
        when(entityManager.merge(sub)).thenReturn(sub);

        boolean result = servicioService.eliminarRelacion(1L, 2L);

        assertTrue(result);
        assertNull(sub.servicioPadre);
        verify(entityManager).merge(sub);
    }

    @Test
    public void testEliminarRelacionFailure() {
        Servicio sub = new Servicio();
        sub.id = 2L;

        when(servicioRepository.findById(2L)).thenReturn(sub);

        boolean result = servicioService.eliminarRelacion(1L, 2L);

        assertFalse(result);
        verify(entityManager, never()).merge(any());
    }
}
