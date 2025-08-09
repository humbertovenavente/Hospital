package com.unis.controller;

import java.util.List;

import com.unis.model.Historia;
import com.unis.service.HistoriaService;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * REST controller for managing institutional history entries.
 * <p>
 * Provides endpoints to create, update, approve, reject, list and delete
 * historical content related to entities such as hospitals, pharmacies, or insurers.
 * </p>
 */
@Path("/historias")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HistoriaController {

    @Inject
    HistoriaService historiaService;

    /**
     * Retrieves all history entries.
     *
     * @return a list of all {@link Historia} records
     */
    @GET
    public List<Historia> getHistorias() {
        return historiaService.listar();
    }

    /**
     * Retrieves all published history entries.
     *
     * @return a list of published {@link Historia} records
     */
    @GET
    @Path("/publicadas")
    public List<Historia> getHistoriasPublicadas() {
        return historiaService.listarPorEstado("PUBLICADO");
    }

    /**
     * Retrieves a single history entry by its ID.
     *
     * @param id the ID of the history entry
     * @return the history entry or 404 if not found
     */
    @GET
    @Path("/{id}")
    public Response getHistoria(@PathParam("id") Long id) {
        Historia historia = historiaService.obtenerPorId(id);
        if (historia == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(historia).build();
    }

    /**
     * Creates a new history entry with status "PROCESO".
     *
     * @param historia the history object to create
     * @return the created history with HTTP 201 or error if validation fails
     */
    @POST
    @Transactional
    public Response createHistoria(Historia historia) {
        if (historia.getEditorEmail() == null || historia.getEditorEmail().trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("El correo del editor (editorEmail) es requerido.").build();
        }

        historia.setStatus("PROCESO");
        Historia creado = historiaService.crear(historia);
        return Response.status(Response.Status.CREATED).entity(creado).build();
    }

    /**
     * Updates an existing history entry by its ID.
     *
     * @param id the ID of the history entry
     * @param historiaActualizada the new data for the history entry
     * @return the updated history or error response
     */
    @PUT
    @Path("/{id}")
    @Transactional
    public Response updateHistoria(@PathParam("id") Long id, Historia historiaActualizada) {
        Historia historia = historiaService.obtenerPorId(id);
        if (historia == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (historiaActualizada.getEditorEmail() == null || historiaActualizada.getEditorEmail().trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("El correo del editor (editorEmail) es requerido.").build();
        }

        if (historiaActualizada.getNombreEntidad() != null)
            historia.setNombreEntidad(historiaActualizada.getNombreEntidad());
        if (historiaActualizada.getHistoria() != null)
            historia.setHistoria(historiaActualizada.getHistoria());
        if (historiaActualizada.getMeritos() != null)
            historia.setMeritos(historiaActualizada.getMeritos());
        if (historiaActualizada.getLineaDelTiempo() != null)
            historia.setLineaDelTiempo(historiaActualizada.getLineaDelTiempo());
        if (historiaActualizada.getStatus() != null)
            historia.setStatus(historiaActualizada.getStatus());
        if (historiaActualizada.getRejectionReason() != null)
            historia.setRejectionReason(historiaActualizada.getRejectionReason());

        historia.setEditorEmail(historiaActualizada.getEditorEmail());

        historiaService.actualizar(id, historia);
        return Response.ok(historia).build();
    }

    /**
     * Deletes a history entry by its ID.
     *
     * @param id the ID of the history entry
     * @return 204 if deleted, 404 if not found
     */
    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteHistoria(@PathParam("id") Long id) {
        boolean eliminado = historiaService.eliminar(id);
        if (!eliminado) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.noContent().build();
    }

    /**
     * Retrieves all history entries pending moderation.
     *
     * @return list of {@link Historia} entries with status "PROCESO"
     */
    @GET
    @Path("/pendientes")
    public List<Historia> getPendientesModeracion() {
        return historiaService.listarPorEstado("PROCESO");
    }

    /**
     * Approves a history entry and sets its status to "PUBLICADO".
     *
     * @param id the ID of the history entry
     * @return the updated history or 404 if not found
     */
    @PUT
    @Path("/aprobar/{id}")
    @Transactional
    public Response aprobarHistoria(@PathParam("id") Long id) {
        Historia historia = historiaService.obtenerPorId(id);
        if (historia == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        historia.setStatus("PUBLICADO");
        historia.setRejectionReason(null);
        return Response.ok(historiaService.actualizar(id, historia)).build();
    }

    /**
     * Rejects a history entry with a given reason.
     *
     * @param id the ID of the history entry
     * @param motivo the reason for rejection
     * @return the updated history or 404 if not found
     */
    @PUT
    @Path("/rechazar/{id}")
    @Transactional
    public Response rechazarHistoria(@PathParam("id") Long id, @QueryParam("motivo") String motivo) {
        Historia historia = historiaService.obtenerPorId(id);
        if (historia == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        historia.setStatus("RECHAZADO");
        historia.setRejectionReason(motivo);
        return Response.ok(historiaService.actualizar(id, historia)).build();
    }
}
