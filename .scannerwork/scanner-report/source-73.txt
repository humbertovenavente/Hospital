package com.unis.service;

import java.time.LocalDate;
import java.util.List;

import com.unis.dto.ReporteAgregadoDTO;
import com.unis.dto.ReporteDetalladoDTO;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

/**
 * Servicio para generar reportes relacionados con las citas médicas.
 */
@ApplicationScoped
public class ReporteService {

    @Inject
    EntityManager entityManager;

    /**
     * Obtiene el reporte agrupado por día.
     *
     * @param idDoctor    ID del doctor para filtrar.
     * @param fechaInicio Fecha de inicio del rango.
     * @param fechaFin    Fecha final del rango.
     * @return Lista de {@link ReporteAgregadoDTO} ordenada por fecha.
     */
    @Transactional
    public List<ReporteAgregadoDTO> obtenerReporteAgregado(Long idDoctor, LocalDate fechaInicio, LocalDate fechaFin) {
        String jpql = "SELECT new com.unis.dto.ReporteAgregadoDTO(" +
                      "c.fecha, COUNT(c), " +
                      "SUM(CASE WHEN c.idAseguradora IS NOT NULL THEN 1 ELSE 0 END), " +
                      "SUM(CASE WHEN c.idAseguradora IS NULL THEN 1 ELSE 0 END)) " +
                      "FROM Cita c " +
                      "WHERE c.idDoctor = :idDoctor " +
                      "AND c.fecha BETWEEN :fechaInicio AND :fechaFin " +
                      "GROUP BY c.fecha " +
                      "ORDER BY c.fecha ASC";
        List<ReporteAgregadoDTO> resultados = entityManager.createQuery(jpql, ReporteAgregadoDTO.class)
                .setParameter("idDoctor", idDoctor)
                .setParameter("fechaInicio", fechaInicio)
                .setParameter("fechaFin", fechaFin)
                .getResultList();
        System.out.println("Obtenidos " + resultados.size() + " registros para Reporte Agrupado.");
        resultados.forEach(dto -> System.out.println("Fecha: " + dto.getFecha() + " - Consultas: " 
                    + dto.getTotalConsultas() + ", Seguro: " + dto.getTotalSeguro() 
                    + ", Directo: " + dto.getTotalDirecto()));
        return resultados;
    }

    /**
     * Obtiene el reporte detallado (listado individual).
     *
     * @param idDoctor    ID del doctor para filtrar.
     * @param fechaInicio Fecha de inicio del rango.
     * @param fechaFin    Fecha final del rango.
     * @return Lista de {@link ReporteDetalladoDTO} ordenada por fecha y hora.
     */
    @Transactional
    public List<ReporteDetalladoDTO> obtenerReporteDetallado(Long idDoctor, LocalDate fechaInicio, LocalDate fechaFin) {
        String jpql = "SELECT new com.unis.dto.ReporteDetalladoDTO(" +
                      "c.fecha, c.horaInicio, p.apellido, " +
                      "CASE WHEN c.idAseguradora IS NOT NULL THEN 'Seguro' ELSE 'Directo' END) " +
                      "FROM Cita c JOIN c.paciente p " +
                      "WHERE c.idDoctor = :idDoctor " +
                      "AND c.fecha BETWEEN :fechaInicio AND :fechaFin " +
                      "ORDER BY c.fecha ASC, c.horaInicio ASC";
        return entityManager.createQuery(jpql, ReporteDetalladoDTO.class)
                .setParameter("idDoctor", idDoctor)
                .setParameter("fechaInicio", fechaInicio)
                .setParameter("fechaFin", fechaFin)
                .getResultList();
    }
}
