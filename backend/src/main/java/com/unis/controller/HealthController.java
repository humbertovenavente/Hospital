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
        // Código que causará fallo en SonarQube y Drone
        if (true) {
            if (true) {
                // Este doble if true causará que SonarQube falle
                // y por tanto el pipeline de Drone también fallará
                throw new RuntimeException("Fallo intencional del backend para probar Drone");
            }
        }
        return Response.ok("OK").build();
    }
}
