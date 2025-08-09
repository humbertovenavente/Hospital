package com.unis.service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.unis.dto.ReporteAgregadoDTO;
import com.unis.dto.ReporteDetalladoDTO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

public class ReporteServiceTest {

    @Mock
    EntityManager entityManager;

    @Mock
    TypedQuery<ReporteAgregadoDTO> queryAgregado;

    @Mock
    TypedQuery<ReporteDetalladoDTO> queryDetallado;

    @InjectMocks
    ReporteService reporteService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testObtenerReporteAgregado() {
        Long idDoctor = 1L;
        LocalDate inicio = LocalDate.of(2024, 1, 1);
        LocalDate fin = LocalDate.of(2024, 1, 31);

        List<ReporteAgregadoDTO> datosMock = Arrays.asList(
            new ReporteAgregadoDTO(inicio, 10L, 6L, 4L)
        );

        when(entityManager.createQuery(anyString(), eq(ReporteAgregadoDTO.class))).thenReturn(queryAgregado);
        when(queryAgregado.setParameter(eq("idDoctor"), eq(idDoctor))).thenReturn(queryAgregado);
        when(queryAgregado.setParameter(eq("fechaInicio"), eq(inicio))).thenReturn(queryAgregado);
        when(queryAgregado.setParameter(eq("fechaFin"), eq(fin))).thenReturn(queryAgregado);
        when(queryAgregado.getResultList()).thenReturn(datosMock);

        List<ReporteAgregadoDTO> result = reporteService.obtenerReporteAgregado(idDoctor, inicio, fin);

        assertEquals(1, result.size());
        assertEquals(10L, result.get(0).getTotalConsultas());
    }

    @Test
    public void testObtenerReporteDetallado() {
        Long idDoctor = 1L;
        LocalDate inicio = LocalDate.of(2024, 2, 1);
        LocalDate fin = LocalDate.of(2024, 2, 28);

        List<ReporteDetalladoDTO> mockList = Arrays.asList(
            new ReporteDetalladoDTO(inicio, "10:00", "Pérez", "Seguro")
        );

        when(entityManager.createQuery(anyString(), eq(ReporteDetalladoDTO.class))).thenReturn(queryDetallado);
        when(queryDetallado.setParameter(eq("idDoctor"), eq(idDoctor))).thenReturn(queryDetallado);
        when(queryDetallado.setParameter(eq("fechaInicio"), eq(inicio))).thenReturn(queryDetallado);
        when(queryDetallado.setParameter(eq("fechaFin"), eq(fin))).thenReturn(queryDetallado);
        when(queryDetallado.getResultList()).thenReturn(mockList);

        List<ReporteDetalladoDTO> result = reporteService.obtenerReporteDetallado(idDoctor, inicio, fin);

        assertEquals(1, result.size());
        assertEquals("Pérez", result.get(0).getNombrePaciente());
    }
}
