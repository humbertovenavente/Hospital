
package com.unis.dto;

import java.util.List;


/**
 * Generic response object for reports.
 * <p>
 * Contains a header and a list of results of a given type.
 * This class is used to wrap responses from aggregated or detailed reports.
 * </p>
 *
 * @param <T> The type of data contained in the report.
 */

public class ReporteResponse<T> {
    /** The header of the report. */
    private String encabezado;

    /** The data contained in the report. */
    private List<T> datos;

    /**
     * Constructs a new ReporteResponse.
     *
     * @param encabezado The header of the report.
     * @param datos      The data contained in the report.
     */
    public ReporteResponse(String encabezado, List<T> datos) {
        this.encabezado = encabezado;
        this.datos = datos;
    }

    /** @return The header of the report. */
    public String getEncabezado() {
        return encabezado;
    }

    /** @param encabezado The header of the report. */
    public void setEncabezado(String encabezado) {
        this.encabezado = encabezado;
    }

    /** @return The data contained in the report. */
    public List<T> getDatos() {
        return datos;
    }

    /** @param datos The data contained in the report. */
    public void setDatos(List<T> datos) {
        this.datos = datos;
    }
}
