package com.unis.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unis.dto.RecetaDTO;
import com.unis.model.Receta;
import com.unis.model.RecetaMedicamento;
import com.unis.service.RecetaService;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * REST resource for managing prescriptions (recetas).
 */
@Path("/recetas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RecetaResource {

    @Inject
    RecetaService recetaService;

    /**
     * Creates a new prescription.
     *
     * @param receta the prescription to be created
     * @return a response containing the created prescription
     */
    @POST
    public Response crearReceta(Receta receta) {
        try {
            Receta nuevaReceta = recetaService.crearReceta(receta);
            return Response.ok(nuevaReceta).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al crear la receta: " + e.getMessage())
                    .build();
        }
    }

    /**
     * Retrieves a prescription by the associated appointment ID.
     *
     * @param idCita the ID of the appointment
     * @return a response containing the prescription or a NOT_FOUND status
     */
    @GET
    @Path("/cita/{idCita}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerRecetaPorIdCita(@PathParam("idCita") int idCita) {
        Receta receta = recetaService.buscarPorIdCita(idCita);
        if (receta != null) {
            return Response.ok(receta).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Receta no encontrada para la cita con ID: " + idCita)
                    .build();
        }
    }

    /**
     * Updates an existing prescription.
     *
     * @param idReceta the ID of the prescription to be updated
     * @param recetaActualizada the updated prescription data
     * @return a response containing the updated prescription
     */
    @PUT
    @Path("/{idReceta}")  //  Asegura que se recibe el ID de la receta y NO el idCita
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response actualizarReceta(@PathParam("idReceta") Long idReceta, Receta recetaActualizada) {
        try {
            Receta recetaEditada = recetaService.actualizarReceta(idReceta, recetaActualizada);
            return Response.ok(recetaEditada).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al actualizar la receta: " + e.getMessage())
                    .build();
        }
    }

    /**
     * Adds a medication to a prescription.
     *
     * @param recetaMedicamento the medication to be added
     * @return a response containing the added medication
     */
    @POST
    @Path("/medicamentos")
    public Response agregarMedicamento(RecetaMedicamento recetaMedicamento) {
        try {
            RecetaMedicamento nuevoMed = recetaService.agregarMedicamento(recetaMedicamento);
            return Response.ok(nuevoMed).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al agregar medicamento: " + e.getMessage())
                    .build();
        }
    }

    /**
     * Retrieves a prescription by its code.
     *
     * @param codigoReceta the code of the prescription
     * @return a response containing the prescription or a NOT_FOUND status
     */
    @GET
    @Path("/{codigoReceta}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerRecetaPorCodigo(@PathParam("codigoReceta") String codigoReceta) {
        Receta receta = recetaService.buscarPorCodigo(codigoReceta);
        if (receta != null) {
            // Incluir el nombre del paciente en la respuesta
            String nombrePaciente = receta.getPaciente().getNombre();
            try {
                System.out.println("📋 Respuesta enviada al frontend: " + new ObjectMapper().writeValueAsString(new RecetaDTO(receta, nombrePaciente)));
            } catch (Exception e) {
                System.err.println("Error al serializar la respuesta: " + e.getMessage());
            }
            return Response.ok(new RecetaDTO(receta, nombrePaciente)).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Receta no encontrada con código: " + codigoReceta)
                    .build();
        }
    }

    /**
     * Validates the insurance for a prescription.
     *
     * @param validacionSeguro the insurance validation response
     */
    public void validarSeguro(String validacionSeguro) {
        System.out.println("📋 Validación con aseguradora: " + validacionSeguro);
    }
}
