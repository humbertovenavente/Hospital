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
import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 * Filtro CORS simple y efectivo
 * Solo se ejecuta en ambiente de QA
 */
@Provider
@ApplicationScoped
public class CorsFilter implements ContainerResponseFilter {
    
    @ConfigProperty(name = "quarkus.profile", defaultValue = "dev")
    String profile;

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        
        // Solo ejecutar en ambiente de QA
        if (!"qa".equals(profile)) {
            return;
        }
        
        // Obtener el origen de la petición
        String origin = requestContext.getHeaderString("Origin");
        
        // Lista de orígenes permitidos para QA
        String[] allowedOrigins = {
            "http://localhost:8083",  // Nginx reverse proxy
            "http://localhost:5174"   // Frontend directo
        };
        
        // Verificar si el origen está permitido
        boolean isAllowed = false;
        for (String allowedOrigin : allowedOrigins) {
            if (allowedOrigin.equals(origin)) {
                isAllowed = true;
                break;
            }
        }
        
        // Solo agregar headers CORS si el origen está permitido
        if (isAllowed) {
            responseContext.getHeaders().add("Access-Control-Allow-Origin", origin);
            responseContext.getHeaders().add("Access-Control-Allow-Credentials", "true");
            responseContext.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            responseContext.getHeaders().add("Access-Control-Allow-Headers", "*");
        }
        
        // Manejar OPTIONS (preflight)
        if ("OPTIONS".equalsIgnoreCase(requestContext.getMethod())) {
            responseContext.setStatus(200);
        }
    }
}
