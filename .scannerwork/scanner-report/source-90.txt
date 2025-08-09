package com.unis.service;

import java.util.List;

import com.unis.model.Agenda;
import com.unis.repository.AgendaRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

/**
 * Servicio para gestionar las operaciones relacionadas con las agendas.
 */
@ApplicationScoped
public class AgendaService {

    @Inject
    AgendaRepository agendaRepository;

    /**
     * Obtiene una lista de agendas asociadas a un doctor específico.
     *
     * @param idDoctor El ID del doctor.
     * @return Una lista de agendas asociadas al doctor.
     */
    public List<Agenda> obtenerAgendasPorDoctor(Long idDoctor) {
        return agendaRepository.find("idDoctor", idDoctor).list();
    }

    /**
     * Crea una nueva agenda en el sistema.
     *
     * @param agenda La agenda a crear.
     */
    @Transactional
    public void crearAgenda(Agenda agenda) {
        agendaRepository.persist(agenda);
    }

    /**
     * Actualiza una agenda existente con los datos proporcionados.
     *
     * @param id               El ID de la agenda a actualizar.
     * @param agendaActualizada Los datos actualizados de la agenda.
     * @throws IllegalArgumentException Si no se encuentra una agenda con el ID proporcionado.
     */
    @Transactional
    public void actualizarAgenda(Long id, Agenda agendaActualizada) {
        Agenda agenda = agendaRepository.findById(id);
        if (agenda == null) {
            throw new IllegalArgumentException("No se encontró la agenda con id: " + id);
        }
        agenda.setDiasAtencion(agendaActualizada.getDiasAtencion());
        agenda.setHoraInicio(agendaActualizada.getHoraInicio());
        agenda.setHoraFin(agendaActualizada.getHoraFin());
        agenda.setNotas(agendaActualizada.getNotas());
        // Los cambios se persisten al finalizar la transacción.
    }
}
