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
        
        // Ejecutar en todos los ambientes (dev, qa, prod)
        // if (!"qa".equals(profile)) {
        //     return;
        // }
        
        // Obtener el origen de la petición
        String origin = requestContext.getHeaderString("Origin");
        
        // Lista de orígenes permitidos para todos los ambientes
        String[] allowedOrigins = {
            "http://localhost:8083",  // Nginx reverse proxy
            "http://localhost:5174",  // Frontend directo
            "http://localhost:5173",  // Frontend dev
            "http://localhost:8061",  // Frontend dev cloud
            "http://localhost:8031",  // Frontend qa cloud
            "http://localhost:8021",  // Frontend prod cloud
            "http://34.46.73.44:8061", // Frontend dev cloud externo
            "http://34.46.73.44:8031", // Frontend qa cloud externo
            "http://34.46.73.44:8021"  // Frontend prod cloud externo
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
