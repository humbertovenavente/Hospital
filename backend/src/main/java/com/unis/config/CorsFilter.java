/**
 * This package contains configuration classes for the application.
 */
package com.unis.config;

import java.io.IOException;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;

/**
 * A JAX-RS response filter that handles Cross-Origin Resource Sharing (CORS) headers.
 * This filter allows cross-origin requests by adding the necessary headers to the response.
 * It also handles preflight (OPTIONS) requests appropriately.
 */
@Provider
@ApplicationScoped
public class CorsFilter implements ContainerResponseFilter {

    /**
     * Adds CORS headers to the response and handles preflight (OPTIONS) requests.
     *
     * <p>The following headers are added to the response:</p>
     * <ul>
     *   <li>Access-Control-Allow-Origin: Allows requests from any origin (useful for development).</li>
     *   <li>Access-Control-Allow-Credentials: Indicates whether credentials are allowed.</li>
     *   <li>Access-Control-Allow-Methods: Specifies the allowed HTTP methods.</li>
     *   <li>Access-Control-Allow-Headers: Specifies the allowed headers in the request.</li>
     * </ul>
     *
     * <p>For OPTIONS requests, the response status is set to 204 (No Content) and no content is included in the response body.</p>
     *
     * @param requestContext  the context of the incoming request
     * @param responseContext the context of the outgoing response
     * @throws IOException if an I/O error occurs during filtering
     */
    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        responseContext.getHeaders().add("Access-Control-Allow-Origin", "*"); // Permitir cualquier origen (para desarrollo)
        responseContext.getHeaders().add("Access-Control-Allow-Credentials", "true");
        responseContext.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        responseContext.getHeaders().add("Access-Control-Allow-Headers", "Origin, Content-Type, Accept, Authorization");

        // Manejo adecuado para solicitudes OPTIONS (preflight)
        if ("OPTIONS".equalsIgnoreCase(requestContext.getMethod())) {
            responseContext.setStatus(204); // No Content (mejor para preflight)
            responseContext.setEntity(""); // Asegura que no haya contenido en la respuesta
        }
    }
}
