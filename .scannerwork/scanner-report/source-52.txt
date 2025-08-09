package com.unis.service;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test; // Se usa para simular la consulta de Panache
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.unis.model.Agenda;
import com.unis.repository.AgendaRepository;

import io.quarkus.hibernate.orm.panache.PanacheQuery;

public class AgendaServiceTest {

    @Mock
    AgendaRepository agendaRepository;

    @Mock
    PanacheQuery<Agenda> panacheQuery; // Simula el objeto que retorna agendaRepository.find(...)

    @InjectMocks
    AgendaService agendaService; // Se inyectarán los mocks en esta instancia

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testObtenerAgendasPorDoctor() {
        // Dado
        Long idDoctor = 1L;
        Agenda agenda1 = new Agenda();
        Agenda agenda2 = new Agenda();
        List<Agenda> expectedAgendas = Arrays.asList(agenda1, agenda2);

        // Simulamos que al llamar a find se retorna el panacheQuery mockeado
        when(agendaRepository.find("idDoctor", idDoctor)).thenReturn(panacheQuery);
        // Y que al llamar a list() sobre ese query se retorna la lista esperada
        when(panacheQuery.list()).thenReturn(expectedAgendas);

        // Cuando
        List<Agenda> actualAgendas = agendaService.obtenerAgendasPorDoctor(idDoctor);

        // Entonces
        assertEquals(expectedAgendas, actualAgendas, "Las agendas obtenidas deben ser las esperadas");
    }

    @Test
    public void testCrearAgenda() {
        // Dado
        Agenda agenda = new Agenda();
        // Configuramos que al llamar a persist no haga nada
        doNothing().when(agendaRepository).persist(agenda);

        // Cuando
        agendaService.crearAgenda(agenda);

        // Entonces
        verify(agendaRepository, times(1)).persist(agenda);
    }

    @Test
    public void testActualizarAgendaWhenAgendaExists() {
        // Dado
        Long id = 1L;
        Agenda agendaExistente = new Agenda();
        // Asignamos valores iniciales
        agendaExistente.setDiasAtencion("Lunes");
        agendaExistente.setHoraInicio("08:00");
        agendaExistente.setHoraFin("17:00");
        agendaExistente.setNotas("Notas viejas");

        Agenda agendaActualizada = new Agenda();
        // Valores que queremos actualizar
        agendaActualizada.setDiasAtencion("Martes");
        agendaActualizada.setHoraInicio("09:00");
        agendaActualizada.setHoraFin("18:00");
        agendaActualizada.setNotas("Notas nuevas");

        // Simulamos que al buscar por id se encuentra la agenda existente
        when(agendaRepository.findById(id)).thenReturn(agendaExistente);

        // Cuando
        agendaService.actualizarAgenda(id, agendaActualizada);

        // Entonces: verificamos que se hayan actualizado los campos
        assertEquals("Martes", agendaExistente.getDiasAtencion());
        assertEquals("09:00", agendaExistente.getHoraInicio());
        assertEquals("18:00", agendaExistente.getHoraFin());
        assertEquals("Notas nuevas", agendaExistente.getNotas());
    }

    @Test
    public void testActualizarAgendaWhenAgendaDoesNotExist() {
        // Dado
        Long id = 1L;
        Agenda agendaActualizada = new Agenda();

        // Simulamos que no se encuentra ninguna agenda para el id
        when(agendaRepository.findById(id)).thenReturn(null);

        // Cuando y Entonces: se espera una excepción al intentar actualizar
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            agendaService.actualizarAgenda(id, agendaActualizada);
        });
        assertTrue(exception.getMessage().contains("No se encontró la agenda con id: " + id));
    }
}
