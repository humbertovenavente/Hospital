package com.unis.service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.unis.dto.MedicinasReporteDTO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.StoredProcedureQuery;

public class ReporteMedicinaServiceTest {

    @Mock
    EntityManager em;

    @Mock
    StoredProcedureQuery storedProcedureQuery;

    @InjectMocks
    ReporteMedicinaService reporteMedicinaService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testObtenerReporte() {
        Date inicio = new Date();
        Date fin = new Date();
        int limite = 10;

        List<Object[]> mockResults = Arrays.asList(
            new Object[]{1, "Paracetamol", 350},
            new Object[]{2, "Ibuprofeno", 275},
            new Object[]{3, "Amoxicilina", 200}
        );

        when(em.createStoredProcedureQuery("REPORTE_MEDICINAS_POPULARES")).thenReturn(storedProcedureQuery);
        when(storedProcedureQuery.registerStoredProcedureParameter(anyString(), any(), any())).thenReturn(null);
        when(storedProcedureQuery.setParameter(anyString(), any())).thenReturn(storedProcedureQuery);
        when(storedProcedureQuery.execute()).thenReturn(true);
        when(storedProcedureQuery.getResultList()).thenReturn(mockResults);

        List<MedicinasReporteDTO> result = reporteMedicinaService.obtenerReporte(inicio, fin, limite);

        assertEquals(3, result.size());
        assertEquals("Paracetamol", result.get(0).principioActivo);
        assertEquals(275, result.get(1).totalRecetas);
        assertEquals(3, result.get(2).popularidad);
    }
}
