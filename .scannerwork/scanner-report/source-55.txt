package com.unis.service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.unis.dto.ModeracionReporteDTO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

public class ReporteModeracionServiceTest {

    @Mock
    EntityManager entityManager;

    @Mock
    Query mockQuery;

    @InjectMocks
    ReporteModeracionService reporteService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testObtenerUsuariosConRechazos() {
        Date inicio = new Date();
        Date fin = new Date();
        int limite = 10;

        Object[] row1 = new Object[] { "usuario1@correo.com", 3 };
        Object[] row2 = new Object[] { "usuario2@correo.com", 2 };
        List<Object[]> mockResults = Arrays.asList(row1, row2);

        when(entityManager.createNativeQuery(anyString())).thenReturn(mockQuery);
        when(mockQuery.setParameter(eq("fechaInicio"), any())).thenReturn(mockQuery);
        when(mockQuery.setParameter(eq("fechaFin"), any())).thenReturn(mockQuery);
        when(mockQuery.setParameter(eq("limite"), eq(limite))).thenReturn(mockQuery);
        when(mockQuery.getResultList()).thenReturn(mockResults);

        List<ModeracionReporteDTO> result = reporteService.obtenerUsuariosConRechazos(inicio, fin, limite);

        assertEquals(2, result.size());

        ModeracionReporteDTO dto1 = result.get(0);
        assertEquals(1, dto1.getNumeroOrden());
        assertEquals("usuario1@correo.com", dto1.getUsuario());
        assertEquals(3, dto1.getTotalRechazos());

        ModeracionReporteDTO dto2 = result.get(1);
        assertEquals(2, dto2.getNumeroOrden());
        assertEquals("usuario2@correo.com", dto2.getUsuario());
        assertEquals(2, dto2.getTotalRechazos());

        verify(entityManager).createNativeQuery(anyString());
        verify(mockQuery).setParameter("fechaInicio", inicio);
        verify(mockQuery).setParameter("fechaFin", fin);
        verify(mockQuery).setParameter("limite", limite);
        verify(mockQuery).getResultList();
    }
}
