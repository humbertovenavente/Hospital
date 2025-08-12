package com.unis.service;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.unis.model.Faq;
import com.unis.repository.FaqRepository;

import jakarta.persistence.EntityManager;

public class FaqServiceTest {

    @Mock
    FaqRepository faqRepository;

    @Mock
    EntityManager entityManager;

    @InjectMocks
    FaqService faqService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(faqRepository.getEntityManager()).thenReturn(entityManager);
    }

    @Test
    public void testGuardarPregunta() {
        Faq faq = new Faq();
        faq.setEditadoPor("admin@hospital.com");

        faqService.guardarPregunta(faq);

        verify(faqRepository, times(1)).persist(any(Faq.class));
    }

    @Test
    public void testGuardarPreguntaSinEditor() {
        Faq faq = new Faq();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            faqService.guardarPregunta(faq);
        });

        assertEquals("El campo 'editadoPor' es obligatorio.", exception.getMessage());
        verify(faqRepository, never()).persist(any(Faq.class));
    }

    @Test
    public void testListarPreguntas() {
        List<Faq> expectedFaqs = Arrays.asList(new Faq(), new Faq());
        when(faqRepository.listAll()).thenReturn(expectedFaqs);

        List<Faq> result = faqService.listarPreguntas();
        assertEquals(expectedFaqs, result);
    }

    @Test
    public void testBuscarPorId() {
        Faq faq = new Faq();
        when(faqRepository.findById(1L)).thenReturn(faq);

        Faq result = faqService.buscarPorId(1L);
        assertEquals(faq, result);
    }

    @Test
    public void testActualizarFaq() {
        Faq faq = new Faq();
        faq.setEditadoPor("admin@hospital.com");

        faqService.actualizarFaq(faq);

        verify(entityManager, times(1)).merge(faq);
    }

    @Test
    public void testActualizarFaqSinEditor() {
        Faq faq = new Faq();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            faqService.actualizarFaq(faq);
        });

        assertEquals("El campo 'editadoPor' es obligatorio para actualizar.", exception.getMessage());
        verify(faqRepository, never()).persist(any(Faq.class));
    }

    @Test
public void testEliminarFaqFound() {
    when(faqRepository.deleteById(1L)).thenReturn(true); 

    boolean result = faqService.eliminarFaq(1L);

    assertTrue(result);
    verify(faqRepository, times(1)).deleteById(1L);
}


   @Test
public void testEliminarFaqNotFound() {
    when(faqRepository.deleteById(1L)).thenReturn(false);

    boolean result = faqService.eliminarFaq(1L);

    assertFalse(result);
    verify(faqRepository, times(1)).deleteById(1L);
}

}
