package com.unis.controller;

import java.util.List;

import com.unis.model.Faq;
import com.unis.service.FaqService;

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
 * REST controller that manages Frequently Asked Questions (FAQ) content.
 * <p>
 * Provides endpoints for creating, editing, approving, rejecting,
 * listing, and deleting FAQ entries with moderation workflow.
 * </p>
 */
@Path("/faq")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FaqController {

    @Inject
    FaqService faqService;

    /**
     * Retrieves a list of all FAQ entries.
     *
     * @return list of FAQs
     */
    @GET
    public List<Faq> listarPreguntas() {
        return faqService.listarPreguntas();
    }

    /**
     * Retrieves a specific FAQ by its ID.
     *
     * @param id the ID of the FAQ
     * @return the FAQ if found, or 404 if not found
     */
    @GET
    @Path("/{id}")
    public Response obtenerFaqPorId(@PathParam("id") Long id) {
        Faq faq = faqService.buscarPorId(id);
        if (faq == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(faq).build();
    }

    /**
     * Creates a new FAQ entry with status "PROCESO".
     *
     * @param faq the FAQ object to be created
     * @return HTTP 201 with the created FAQ, or 400 if input is invalid
     */
    @POST
    @Path("/crear")
    @Transactional
    public Response guardarPregunta(Faq faq) {
        if (faq.getPregunta() == null || faq.getPregunta().trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("La pregunta no puede estar vacía.").build();
        }
        if (faq.getEditadoPor() == null) {
            throw new IllegalArgumentException("Editor email es requerido");
        }
        faq.setStatus("PROCESO");
        faqService.guardarPregunta(faq);
        return Response.ok(faq).status(Response.Status.CREATED).build();
    }

    /**
     * Edits an existing FAQ entry.
     *
     * @param id the ID of the FAQ to edit
     * @param faqActualizada the new data for the FAQ
     * @return updated FAQ or appropriate error response
     */
    @PUT
    @Path("/editar/{id}")
    @Transactional
    public Response editarPregunta(@PathParam("id") Long id, Faq faqActualizada) {
        Faq faq = faqService.buscarPorId(id);
        if (faq == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Pregunta no encontrada").build();
        }

        if (faqActualizada.getEditadoPor() == null || faqActualizada.getEditadoPor().trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("El campo 'editadoPor' es obligatorio.").build();
        }

        if (faqActualizada.getPregunta() != null) faq.setPregunta(faqActualizada.getPregunta());
        if (faqActualizada.getRespuesta() != null) faq.setRespuesta(faqActualizada.getRespuesta());
        if (faqActualizada.getAutor() != null) faq.setAutor(faqActualizada.getAutor());
        faq.setEditadoPor(faqActualizada.getEditadoPor());
        if (faqActualizada.getStatus() != null) faq.setStatus(faqActualizada.getStatus());
        if (faqActualizada.getRejectionReason() != null) faq.setRejectionReason(faqActualizada.getRejectionReason());

        faqService.actualizarFaq(faq);
        return Response.ok(faq).build();
    }

    /**
     * Deletes a FAQ entry by its ID.
     *
     * @param id the ID of the FAQ to delete
     * @return 200 OK if deleted, or 404 if not found
     */
    @DELETE
    @Path("/eliminar/{id}")
    @Transactional
    public Response eliminarPregunta(@PathParam("id") Long id) {
        boolean eliminado = faqService.eliminarFaq(id);
        if (!eliminado) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Pregunta no encontrada").build();
        }
        return Response.ok().build();
    }

    /**
     * Lists all FAQ entries with "PROCESO" status.
     *
     * @return list of pending FAQs
     */
    @GET
    @Path("/pendientes")
    public List<Faq> listarPendientes() {
        return faqService.listarPorEstado("PROCESO");
    }

    /**
     * Approves a FAQ entry and changes its status to "PUBLICADO".
     *
     * @param id the ID of the FAQ to approve
     * @return the updated FAQ or 404 if not found
     */
    @PUT
    @Path("/aprobar/{id}")
    @Transactional
    public Response aprobarPregunta(@PathParam("id") Long id) {
        Faq faq = faqService.buscarPorId(id);
        if (faq == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        faq.setStatus("PUBLICADO");
        faq.setRejectionReason(null);
        faqService.actualizarFaq(faq);
        return Response.ok(faq).build();
    }

    /**
     * Rejects a FAQ entry and sets a rejection reason.
     *
     * @param id the ID of the FAQ to reject
     * @param motivo the reason for rejection
     * @return the updated FAQ or 404 if not found
     */
    @PUT
    @Path("/rechazar/{id}")
    @Transactional
    public Response rechazarPregunta(@PathParam("id") Long id, @QueryParam("motivo") String motivo) {
        Faq faq = faqService.buscarPorId(id);
        if (faq == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        faq.setStatus("RECHAZADO");
        faq.setRejectionReason(motivo);
        faqService.actualizarFaq(faq);
        return Response.ok(faq).build();
    }
}
