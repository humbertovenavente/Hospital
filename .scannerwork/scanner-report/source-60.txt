package com.unis.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.unis.model.SolicitudHospital;

/**
 * Prueba unitaria para {@link SolicitudHospitalService}.
 */
public class SolicitudHospitalServiceTest {

    @Mock
    SolicitudHospitalAPI aseguradoraClient;

    @InjectMocks
    SolicitudHospitalService solicitudService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Prueba que una solicitud válida se envíe correctamente a la aseguradora.
     */
    @Test
    public void testEnviarSolicitudConDatosValidos() {
        SolicitudHospital solicitud = new SolicitudHospital();
        solicitud.nombre = "Hospital Central";
        solicitud.direccion = "Zona 10";
        solicitud.telefono = "12345678";
        solicitud.aseguradora = "UnisSeguro";
        solicitud.estado = "pendiente";
        solicitud.origen = "Hospital";

        // Simula que se devuelve la misma solicitud
        when(aseguradoraClient.enviarSolicitud(any(SolicitudHospital.class))).thenReturn(solicitud);

        solicitudService.enviarSolicitud(solicitud);

        verify(aseguradoraClient, times(1)).enviarSolicitud(solicitud);
    }

    /**
     * Prueba que una solicitud con datos incompletos no sea enviada.
     */
    @Test
    public void testEnviarSolicitudConDatosIncompletos() {
        SolicitudHospital solicitud = new SolicitudHospital(); // Campos vacíos

        solicitudService.enviarSolicitud(solicitud);

        verify(aseguradoraClient, never()).enviarSolicitud(any());
    }

    /**
     * Prueba que se maneje la excepción al fallar el envío.
     */
    @Test
    public void testEnviarSolicitudConErrorEnCliente() {
        SolicitudHospital solicitud = new SolicitudHospital();
        solicitud.nombre = "Hospital Prueba";
        solicitud.direccion = "Zona 1";
        solicitud.telefono = "98765432";
        solicitud.aseguradora = "TestSeguro";
        solicitud.estado = "pendiente";
        solicitud.origen = "Hospital";

        // Simula excepción al llamar a la API
        doThrow(new RuntimeException("Error al contactar aseguradora"))
            .when(aseguradoraClient).enviarSolicitud(any());

        solicitudService.enviarSolicitud(solicitud);

        verify(aseguradoraClient, times(1)).enviarSolicitud(any());
    }
}
