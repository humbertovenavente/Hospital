package com.unis.controller;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/health")
public class HealthController {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response healthCheck() {
        // Error intencional para hacer fallar la compilación
        if (true) {
            return Response.ok("OK").build();
        }
        // Esta línea causará error de compilación - falta punto y coma
        return Response.ok("OK").build()
    }
}
