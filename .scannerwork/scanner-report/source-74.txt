// ReporteModeracionService
package com.unis.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.unis.dto.ModeracionReporteDTO;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

/**
 * Servicio para generar reportes relacionados con la moderación de contenido.
 */
@ApplicationScoped
public class ReporteModeracionService {

    @Inject
    EntityManager em;

    /**
     * Obtiene un reporte de usuarios con rechazos en un intervalo de tiempo.
     *
     * @param inicio La fecha de inicio del intervalo.
     * @param fin    La fecha de fin del intervalo.
     * @param limite El número máximo de resultados a incluir.
     * @return Una lista de objetos {@link ModeracionReporteDTO} con los datos del reporte.
     */
    public List<ModeracionReporteDTO> obtenerUsuariosConRechazos(Date inicio, Date fin, int limite) {
        String sql = "SELECT * FROM (\n" +
                "  SELECT editor AS usuario, COUNT(*) AS total_rechazos\n" +
                "  FROM (\n" +
                "    SELECT editor_email AS editor, last_modified_date AS fecha FROM page_content\n" +
                "    WHERE status = 'RECHAZADO' AND TRIM(editor_email) IS NOT NULL AND LENGTH(TRIM(editor_email)) > 0\n" +
                "    UNION ALL\n" +
                "    SELECT editadopor AS editor, fecha_creacion AS fecha FROM faq\n" +
                "    WHERE status = 'RECHAZADO' AND TRIM(editadopor) IS NOT NULL AND LENGTH(TRIM(editadopor)) > 0\n" +
                "    UNION ALL\n" +
                "    SELECT editor_email AS editor, last_modified_date AS fecha FROM historia\n" +
                "    WHERE status = 'RECHAZADO' AND TRIM(editor_email) IS NOT NULL AND LENGTH(TRIM(editor_email)) > 0\n" +
                "  )\n" +
                "  WHERE fecha BETWEEN :fechaInicio AND :fechaFin\n" +
                "  GROUP BY editor\n" +
                "  ORDER BY total_rechazos DESC\n" +
                ") WHERE ROWNUM <= :limite";

        Query nativeQuery = em.createNativeQuery(sql);
        nativeQuery.setParameter("fechaInicio", inicio);
        nativeQuery.setParameter("fechaFin", fin);
        nativeQuery.setParameter("limite", limite);

        List<Object[]> results = nativeQuery.getResultList();
        List<ModeracionReporteDTO> lista = new ArrayList<>();
        int i = 1;
        for (Object[] row : results) {
            String usuario = (String) row[0];
            int total = ((Number) row[1]).intValue();
            lista.add(new ModeracionReporteDTO(i++, usuario, total));
        }

        return lista;
    }
}
