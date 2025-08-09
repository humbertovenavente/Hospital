package com.unis.service;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import com.unis.model.SolicitudHospital;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 * API REST para enviar solicitudes de hospitales a la aseguradora.
 */
@Path("/api/solicitudes/hospital")
@RegisterRestClient(configKey = "solicitud-hospital-api")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface SolicitudHospitalAPI {

    /**
     * Envía una solicitud de hospital a la aseguradora.
     *
     * @param hospital Los datos de la solicitud de hospital.
     * @return La solicitud enviada.
     */
    @POST
    SolicitudHospital enviarSolicitud(SolicitudHospital hospital);
}
