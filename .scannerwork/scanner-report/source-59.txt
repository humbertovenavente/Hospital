package com.unis.service;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.unis.model.Medicamento;
import com.unis.model.Receta;
import com.unis.model.RecetaMedicamento;
import com.unis.repository.RecetaRepository;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import jakarta.persistence.EntityManager;

public class RecetaServiceTest {

    @Mock
    EntityManager em;

    @Mock
    RecetaRepository recetaRepository;

    @Mock
    PanacheQuery<Receta> panacheQuery; // Para buscar por idCita

    @Mock
    PanacheQuery<Receta> panacheQueryCodigo; // Para buscar por código

    @InjectMocks
    RecetaService recetaService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ----- Tests para crearReceta -----

    @Test
    public void testCrearRecetaSuccess() {
        Receta receta = new Receta();
        receta.setIdDoctor(1L);
        receta.setIdPaciente(2L);
        receta.setCodigoReceta("REC123");
        receta.setFechaCreacion(null); // Se asigna en el método

        doNothing().when(em).persist(receta);
        doNothing().when(em).flush();

        Receta result = recetaService.crearReceta(receta);

        verify(em, times(1)).persist(receta);
        verify(em, times(1)).flush();
        assertNotNull(result.getFechaCreacion(), "La fecha de creación debe asignarse automáticamente");
        assertEquals(receta, result);
    }

    @Test
    public void testCrearRecetaMissingDoctorOrPaciente() {
        Receta receta = new Receta();
        receta.setCodigoReceta("REC123");
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            recetaService.crearReceta(receta);
        });
        assertTrue(ex.getMessage().contains("idDoctor e idPaciente son obligatorios"));
    }

    @Test
    public void testCrearRecetaMissingCodigo() {
        Receta receta = new Receta();
        receta.setIdDoctor(1L);
        receta.setIdPaciente(2L);
        receta.setCodigoReceta("");
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            recetaService.crearReceta(receta);
        });
        assertTrue(ex.getMessage().contains("Código de receta es obligatorio"));
    }

    // ----- Test para buscarPorIdCita -----

    @Test
    public void testBuscarPorIdCitaFound() {
        int idCita = 100;
        Receta receta = new Receta();
        receta.setMedicamentos(new ArrayList<>());
        when(recetaRepository.find("idCita", idCita)).thenReturn(panacheQuery);
        when(panacheQuery.firstResult()).thenReturn(receta);

        Receta result = recetaService.buscarPorIdCita(idCita);
        assertNotNull(result, "La receta debe encontrarse");
    }

    @Test
    public void testBuscarPorIdCitaNotFound() {
        int idCita = 100;
        when(recetaRepository.find("idCita", idCita)).thenReturn(panacheQuery);
        when(panacheQuery.firstResult()).thenReturn(null);

        Receta result = recetaService.buscarPorIdCita(idCita);
        assertNull(result, "La receta no debe encontrarse");
    }

    // ----- Test para actualizarReceta -----

    @Test
public void testActualizarRecetaSuccess() {
    Long idReceta = 1L;

    // Crear un medicamento existente
    Medicamento medicamento = new Medicamento();
    medicamento.setIdMedicamento(10L);

    // Crear un RecetaMedicamento con el Medicamento asignado
    RecetaMedicamento recetaMedicamento = new RecetaMedicamento();
    recetaMedicamento.setMedicamento(medicamento);

    // Crear la receta existente
    Receta recetaExistente = new Receta();
    recetaExistente.setAnotaciones("OldAnotaciones");
    recetaExistente.setNotasEspeciales("OldNotas");
    recetaExistente.setMedicamentos(new ArrayList<>(Arrays.asList(recetaMedicamento)));

    // Crear la receta actualizada
    Receta recetaActualizada = new Receta();
    recetaActualizada.setAnotaciones("NewAnotaciones");
    recetaActualizada.setNotasEspeciales("NewNotas");
    recetaActualizada.setMedicamentos(Arrays.asList(recetaMedicamento));

    // Simular la búsqueda de la receta y el medicamento en la base de datos
    when(em.find(Receta.class, idReceta)).thenReturn(recetaExistente);
    when(em.find(Medicamento.class, 10L)).thenReturn(medicamento);
    when(em.merge(any())).thenAnswer(invocation -> invocation.getArgument(0));
    doNothing().when(em).flush();

    // Ejecutar la actualización
    Receta result = recetaService.actualizarReceta(idReceta, recetaActualizada);

    // Verificaciones
    assertEquals("NewAnotaciones", result.getAnotaciones());
    assertEquals("NewNotas", result.getNotasEspeciales());
    assertEquals(1, result.getMedicamentos().size());
    assertEquals(medicamento, result.getMedicamentos().get(0).getMedicamento());
}


    @Test
    public void testActualizarRecetaNotFound() {
        Long idReceta = 1L;
        Receta recetaActualizada = new Receta();
        when(em.find(Receta.class, idReceta)).thenReturn(null);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            recetaService.actualizarReceta(idReceta, recetaActualizada);
        });
        assertTrue(ex.getMessage().contains("No se encontró la receta con ID"));
    }

    // ----- Test para agregarMedicamento -----

    @Test
    public void testAgregarMedicamentoSuccess() {
        // Creamos un objeto anónimo para RecetaMedicamento que simule getIdReceta() y getIdMedicamento()
        RecetaMedicamento rm = new RecetaMedicamento() {
            @Override
            public Long getIdReceta() {
                return 1L;
            }
            @Override
            public Long getIdMedicamento() {
                return 10L;
            }
        };

        Receta receta = new Receta();
        receta.setIdReceta(1L);
        Medicamento med = new Medicamento();
        med.setIdMedicamento(10L);

        when(em.find(Receta.class, 1L)).thenReturn(receta);
        when(em.find(Medicamento.class, 10L)).thenReturn(med);
        doNothing().when(em).persist(rm);
        doNothing().when(em).flush();

        RecetaMedicamento result = recetaService.agregarMedicamento(rm);
        assertNotNull(result, "El objeto devuelto no debe ser nulo");
        assertEquals(receta, result.getReceta(), "La receta asociada debe ser la encontrada");
        assertEquals(med, result.getMedicamento(), "El medicamento asociado debe ser el encontrado");
        verify(em, times(1)).persist(rm);
        verify(em, times(1)).flush();
    }

    @Test
    public void testAgregarMedicamentoRecetaNotFound() {
        RecetaMedicamento rm = new RecetaMedicamento() {
            @Override
            public Long getIdReceta() {
                return 1L;
            }
            @Override
            public Long getIdMedicamento() {
                return 10L;
            }
        };
        when(em.find(Receta.class, 1L)).thenReturn(null);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            recetaService.agregarMedicamento(rm);
        });
        assertTrue(ex.getMessage().contains("No se encontró la receta con ID"));
    }

    @Test
    public void testAgregarMedicamentoMedicamentoNotFound() {
        RecetaMedicamento rm = new RecetaMedicamento() {
            @Override
            public Long getIdReceta() {
                return 1L;
            }
            @Override
            public Long getIdMedicamento() {
                return 10L;
            }
        };
        Receta receta = new Receta();
        receta.setIdReceta(1L);
        when(em.find(Receta.class, 1L)).thenReturn(receta);
        when(em.find(Medicamento.class, 10L)).thenReturn(null);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            recetaService.agregarMedicamento(rm);
        });
        assertTrue(ex.getMessage().contains("No se encontró el medicamento con ID"));
    }

    // ----- Test para buscarPorCodigo -----

    @Test
    public void testBuscarPorCodigo() {
        String codigo = "REC123";
        Receta receta = new Receta();
        when(recetaRepository.find("codigoReceta", codigo)).thenReturn(panacheQueryCodigo);
        when(panacheQueryCodigo.firstResult()).thenReturn(receta);

        Receta result = recetaService.buscarPorCodigo(codigo);
        assertEquals(receta, result, "La receta devuelta debe ser la esperada");
    }
}
