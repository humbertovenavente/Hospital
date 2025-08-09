package com.unis.resource;

import java.util.List;

import com.unis.model.PageContent;
import com.unis.service.PageContentService;

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
 * REST resource for managing page content.
 */
@Path("/api/page-content")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PageContentResource {

    @Inject
    PageContentService service;

    /**
     * Retrieves published content for a specific page.
     *
     * @param pageName the name of the page
     * @return a list of published content for the page
     */
    @GET
    @Path("/{pageName}")
    public List<PageContent> getPublished(@PathParam("pageName") String pageName) {
        return service.getPublishedContent(pageName);
    }

    /**
     * Retrieves content in draft status.
     *
     * @return a list of draft content
     */
    @GET
    @Path("/drafts")
    public List<PageContent> getDrafts() {
        return service.getDraftContent();
    }

    /**
     * Retrieves content pending moderation.
     *
     * @return a list of content in "PROCESO" status
     */
    @GET
    @Path("/pendientes")
    public List<PageContent> getPendientesModeracion() {
        return service.getByStatus("PROCESO");
    }

    /**
     * Retrieves content by its ID.
     *
     * @param id the ID of the content
     * @return a response containing the content or a NOT_FOUND status
     */
    @GET
    @Path("/contenido/{id}")
    public Response getById(@PathParam("id") Long id) {
        PageContent content = service.findById(id);
        if (content == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(content).build();
    }

    /**
     * Creates new content in "PROCESO" status.
     *
     * @param content the content to be created
     * @return a response containing the created content
     */
    @POST
    @Transactional
    public Response create(PageContent content) {
        content.setStatus("PROCESO");
        PageContent created = service.create(content);
        return Response.ok(created).status(Response.Status.CREATED).build();
    }

    /**
     * Updates existing content.
     *
     * @param id the ID of the content to be updated
     * @param content the updated content data
     * @return a response containing the updated content
     */
    @PUT
    @Path("/{id}")
    @Transactional
    public Response update(@PathParam("id") Long id, PageContent content) {
        PageContent updated = service.update(id, content);
        return Response.ok(updated).build();
    }

    /**
     * Publishes (approves) content.
     *
     * @param id the ID of the content to be published
     * @return a response containing the published content
     */
    @PUT
    @Path("/{id}/publish")
    @Transactional
    public Response publish(@PathParam("id") Long id) {
        PageContent published = service.publish(id);
        return Response.ok(published).build();
    }

    /**
     * Rejects content and saves the reason.
     *
     * @param id the ID of the content to be rejected
     * @param motivo the reason for rejection
     * @return a response containing the rejected content
     */
    @PUT
    @Path("/{id}/reject")
    @Transactional
    public Response reject(@PathParam("id") Long id, @QueryParam("motivo") String motivo) {
        PageContent rejected = service.reject(id, motivo);
        return Response.ok(rejected).build();
    }

    /**
     * Deletes content by its ID.
     *
     * @param id the ID of the content to be deleted
     * @return a response indicating the deletion status
     */
    @DELETE
    @Path("/{id}")
    @Transactional
    public Response delete(@PathParam("id") Long id) {
        service.delete(id);
        return Response.noContent().build();
    }
}
