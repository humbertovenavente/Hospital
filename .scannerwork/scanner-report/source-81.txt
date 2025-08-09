package com.unis.service;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import com.unis.model.SolicitudHospital;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

/**
 * Servicio para gestionar las solicitudes de hospitales hacia aseguradoras y MongoDB.
 */
@ApplicationScoped
public class SolicitudHospitalService {

    @Inject
    @RestClient
    SolicitudHospitalAPI aseguradoraClient;

    /**
     * Envía una solicitud de hospital a la aseguradora y MongoDB.
     *
     * @param solicitud Los datos de la solicitud a enviar.
     */
    public void enviarSolicitud(SolicitudHospital solicitud) {
        try {
            // Validar datos antes de enviar
            if (solicitud.nombre == null || solicitud.nombre.isEmpty() ||
                solicitud.direccion == null || solicitud.direccion.isEmpty() ||
                solicitud.telefono == null || solicitud.telefono.isEmpty() ||
                solicitud.aseguradora == null || solicitud.aseguradora.isEmpty()) {
                System.err.println(" Datos incompletos. No se puede enviar la solicitud: " + solicitud);
                return;
            }

            System.out.println("Enviando solicitud a la aseguradora: " + solicitud);
            System.out.println("Datos enviados: " +
                "Nombre: " + solicitud.nombre + ", " +
                "Dirección: " + solicitud.direccion + ", " +
                "Teléfono: " + solicitud.telefono + ", " +
                "Aseguradora: " + solicitud.aseguradora + ", " +
                "Estado: " + solicitud.estado);

            // Enviar solicitud a la aseguradora
            aseguradoraClient.enviarSolicitud(solicitud);
            System.out.println("Solicitud enviada correctamente a la aseguradora.");

            // Enviar la solicitud a MongoDB
            enviarSolicitudAMongo(solicitud);
        } catch (Exception e) {
            System.err.println(" Error al enviar solicitud a la aseguradora o MongoDB: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Envía una solicitud de hospital a MongoDB.
     *
     * @param solicitud Los datos de la solicitud a enviar.
     */
    private void enviarSolicitudAMongo(SolicitudHospital solicitud) {
        try {
            String urlDestino = "";
    
            if ("Aseguradora Uno".equalsIgnoreCase(solicitud.aseguradora)) {
                urlDestino = "http://localhost:5001/api/solicitudes/hospital";
            } else if ("Aseguradora DOS".equalsIgnoreCase(solicitud.aseguradora)) {
                urlDestino = "http://localhost:5022/api/solicitudes/hospital";
            } else {
                System.err.println("No se encontró aseguradora válida para enviar");
                return;
            }
    
            URL url = new URL(urlDestino);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
    
            String input = String.format(
                "{\"nombre\":\"%s\",\"direccion\":\"%s\",\"telefono\":\"%s\",\"aseguradora\":\"%s\",\"estado\":\"%s\",\"origen\":\"%s\"}",
                solicitud.nombre, solicitud.direccion, solicitud.telefono, solicitud.aseguradora, solicitud.estado, solicitud.origen
            );
    
            System.out.println("Enviando solicitud a " + urlDestino + " con datos: " + input);
    
            try (OutputStream os = conn.getOutputStream()) {
                os.write(input.getBytes());
                os.flush();
            }
    
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                System.err.println("Error al enviar solicitud: " + conn.getResponseMessage());
            } else {
                System.out.println("Solicitud enviada correctamente a la aseguradora.");
            }
    
            conn.disconnect();
        } catch (Exception e) {
            System.err.println("Error al enviar solicitud: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Transactional
public void actualizarEstado(String id, String nuevoEstado) {
    try {
        String urlDestino = "";

        // Elegir a qué aseguradora enviar según la aseguradora del hospital (puedes personalizar)
        if ("Aseguradora Uno".equalsIgnoreCase(nuevoEstado)) {
            urlDestino = "http://localhost:5033/api/solicitudes/hospital/" + id + "/estado";
        } else if ("Aseguradora TRES".equalsIgnoreCase(nuevoEstado)) {
            urlDestino = "http://localhost:5033/api/solicitudes/hospital/" + id + "/estado";
        } else {
            // Si el estado no es el nombre de la aseguradora, solo enviar a la original (ejemplo básico)
            urlDestino = "http://localhost:5033/api/solicitudes/hospital/" + id + "/estado";
        }

        URL url = new URL(urlDestino);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("PUT");
        conn.setRequestProperty("Content-Type", "application/json");

        String input = String.format("{\"estado\":\"%s\"}", nuevoEstado);

        System.out.println("Actualizando estado en " + urlDestino + " con datos: " + input);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(input.getBytes());
            os.flush();
        }

        if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
            System.err.println("Error al actualizar estado: " + conn.getResponseMessage());
        } else {
            System.out.println("Estado actualizado correctamente en aseguradora.");
        }

        conn.disconnect();
    } catch (Exception e) {
        System.err.println(" Error al actualizar estado: " + e.getMessage());
        e.printStackTrace();
    }
}

}