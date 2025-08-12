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
 * Filtro CORS simple y efectivo
 */
@Provider
@ApplicationScoped
public class CorsFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        
        // Permitir origen específico del frontend
        responseContext.getHeaders().add("Access-Control-Allow-Origin", "http://localhost:5173");
        
        // Permitir credenciales
        responseContext.getHeaders().add("Access-Control-Allow-Credentials", "true");
        
        // Permitir todos los métodos
        responseContext.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        
        // Permitir todas las cabeceras
        responseContext.getHeaders().add("Access-Control-Allow-Headers", "*");
        
        // Manejar OPTIONS (preflight)
        if ("OPTIONS".equalsIgnoreCase(requestContext.getMethod())) {
            responseContext.setStatus(200);
        }
    }
}
