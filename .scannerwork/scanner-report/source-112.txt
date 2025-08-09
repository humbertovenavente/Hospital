package com.unis.resource;

import com.unis.model.AseguradoraConexion;
import com.unis.repository.AseguradoraConexionRepository;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Map;
import java.util.List;

@Path("/api/conexiones-aseguradoras")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AseguradoraConexionResource {

    @Inject
    AseguradoraConexionRepository repository;

    @GET
    public List<AseguradoraConexion> getTodas() {
        return repository.listAll();
    }

    @GET
    @Path("/url/{nombre}")
    public Response getUrlPorNombre(@PathParam("nombre") String nombre) {
        AseguradoraConexion conexion = repository.findByNombre(nombre);
        if (conexion == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Conexión no encontrada"))
                    .build();
        }

        return Response.ok(Map.of("url", conexion.getUrlBase())).build();
    }

    @PUT
@Path("/{id}")
@Transactional
public Response actualizar(@PathParam("id") Long id, Map<String, String> body) {
    AseguradoraConexion existente = repository.findById(id);
    if (existente == null) {
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    existente.setNombre(body.getOrDefault("nombre", existente.getNombre()));
    existente.setUrlBase(body.getOrDefault("url", existente.getUrlBase()));
    return Response.ok(Map.of("message", "Actualizado correctamente")).build();
}

@DELETE
@Path("/{id}")
@Transactional
public Response eliminar(@PathParam("id") Long id) {
    boolean eliminado = repository.deleteById(id);
    if (!eliminado) {
        return Response.status(Response.Status.NOT_FOUND).build();
    }
    return Response.ok(Map.of("message", "Eliminado correctamente")).build();
}


    @POST
    @Path("/registrar")
    @Transactional
    public Response registrarAseguradora(Map<String, String> body) {
        try {
            String nombre = body.get("nombre");
            String url = body.get("url");

            if (nombre == null || url == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Nombre y URL requeridos")
                        .build();
            }

            AseguradoraConexion existente = repository.findByNombre(nombre);
            if (existente != null) {
                return Response.status(Response.Status.CONFLICT)
                        .entity("Ya existe")
                        .build();
            }

            AseguradoraConexion nueva = new AseguradoraConexion();
            nueva.setNombre(nombre);
            nueva.setUrlBase(url);

            repository.persist(nueva);
            return Response.status(Response.Status.CREATED)
                    .entity(Map.of("message", "Registrada con éxito"))
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError()
                    .entity("Error interno al registrar")
                    .build();
        }
    }
}
