package com.unis.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.List;

import com.unis.model.Aseguradora;
import com.unis.model.Cita;
import com.unis.model.Doctor;
import com.unis.model.EstadoCita;
import com.unis.model.FichaTecnica;
import com.unis.model.Paciente;
import com.unis.model.PacienteFT;
import com.unis.model.Rol;
import com.unis.model.Usuario;
import com.unis.repository.CitaRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.JsonObject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

/**
 * Servicio para gestionar las operaciones relacionadas con las citas médicas.
 */
@ApplicationScoped
public class CitaService {

    @Inject
    CitaRepository citaRepository;

    @Inject
    DoctorService doctorService;

    @PersistenceContext
    EntityManager entityManager;

    /**
     * Obtiene todas las citas registradas.
     *
     * @return Una lista de citas.
     */
    public List<Cita> obtenerCitas() {
        return citaRepository.listAll();
    }

    /**
     * Obtiene una cita específica por su ID.
     *
     * @param id El ID de la cita.
     * @return La cita correspondiente al ID, o null si no se encuentra.
     */
    public Cita obtenerCitaPorId(Long id) {
        return citaRepository.findById(id);
    }

    /**
     * Busca un doctor por su ID.
     *
     * @param id El ID del doctor.
     * @return El doctor correspondiente al ID, o null si no se encuentra.
     */
    public Doctor buscarDoctorPorId(Long id) {
        return doctorService.getDoctorById(id).orElse(null);
    }

    /**
     * Agenda una nueva cita médica.
     *
     * @param cita La cita a agendar.
     * @throws IllegalArgumentException Si el ID del doctor o paciente no está presente,
     *                                  o si no se encuentran en la base de datos.
     */
    @Transactional
    public void agendarCita(Cita cita) {
        if (cita.getIdDoctor() == null || cita.getIdPaciente() == null) {
            throw new IllegalArgumentException("El ID del doctor y paciente son obligatorios.");
        }

        Doctor doctor = entityManager.find(Doctor.class, cita.getIdDoctor());
        Paciente paciente = entityManager.find(Paciente.class, cita.getIdPaciente());

        if (doctor == null || paciente == null) {
            throw new IllegalArgumentException("Doctor o paciente no encontrados.");
        }

        cita.setDoctor(doctor);
        cita.setPaciente(paciente);
        citaRepository.persist(cita);
    }

    /**
     * Cancela una cita existente.
     *
     * @param id El ID de la cita a cancelar.
     * @throws IllegalArgumentException Si no se encuentra la cita.
     */
    @Transactional
    public void cancelarCita(Long id) {
        Cita cita = citaRepository.findById(id);
        if (cita != null) {
            cita.setEstado(EstadoCita.CANCELADA);
        } else {
            throw new IllegalArgumentException("Cita no encontrada");
        }
    }

    /**
     * Actualiza una cita existente con los datos proporcionados.
     *
     * @param id               El ID de la cita a actualizar.
     * @param citaActualizada  Los datos actualizados de la cita.
     * @throws IllegalArgumentException Si no se encuentra la cita.
     */
    @Transactional
    public void actualizarCita(Long id, Cita citaActualizada) {
        Cita cita = citaRepository.findById(id);
        if (cita == null) {
            throw new IllegalArgumentException("Cita no encontrada");
        }
        if (citaActualizada.getEstado() != null) cita.setEstado(citaActualizada.getEstado());
        if (citaActualizada.getDiagnostico() != null) cita.setDiagnostico(citaActualizada.getDiagnostico());
        if (citaActualizada.getResultados() != null) cita.setResultados(citaActualizada.getResultados());
    }

    /**
     * Marca una cita como finalizada.
     *
     * @param id El ID de la cita a procesar.
     * @throws IllegalArgumentException Si no se encuentra la cita.
     */
    @Transactional
    public void procesarCita(Long id) {
        Cita cita = citaRepository.findById(id);
        if (cita == null) {
            throw new IllegalArgumentException("Cita no encontrada");
        }
        cita.setEstado(EstadoCita.FINALIZADA);
    }

    /**
     * Procesa una cita y envía los resultados a la aseguradora.
     *
     * @param id          El ID de la cita.
     * @param diagnostico El diagnóstico de la cita.
     * @param resultados  Los resultados de la cita.
     * @throws IllegalArgumentException Si no se encuentra la cita.
     */
    @Transactional
    public void procesarCitaYEnviarResultados(Long id, String diagnostico, String resultados) {
        Cita cita = citaRepository.findById(id);
        if (cita == null) throw new IllegalArgumentException("Cita no encontrada");

        cita.setDiagnostico(diagnostico);
        cita.setResultados(resultados);
        cita.setEstado(EstadoCita.FINALIZADA);

        enviarResultadosAAseguradora(cita);
    }

    private void enviarResultadosAAseguradora(Cita cita) {
        try {
            JsonObject json = jakarta.json.Json.createObjectBuilder()
                .add("idCita", cita.getIdCita())
                .add("documento", cita.getPaciente().getDocumento())
                .add("nombre", cita.getPaciente().getUsuario().getNombreUsuario())
                .add("apellido", cita.getPaciente().getApellido())
                .add("diagnostico", cita.getDiagnostico())
                .add("resultados", cita.getResultados())
                .add("fecha", cita.getFecha().toString())
                .add("doctor", cita.getDoctor().getUsuario().getNombreUsuario())
                .build();

            System.out.println("Enviando resultado: " + json);

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:5001/api/resultados"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json.toString()))
                .build();

            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(response -> System.out.println("✅ Resultado enviado: " + response.statusCode()));
        } catch (Exception e) {
            System.err.println("Error enviando resultados: " + e.getMessage());
        }
    }

    @Transactional
    public void reasignarDoctor(Long idCita, Doctor nuevoDoctor) {
        Cita cita = citaRepository.findById(idCita);
        if (cita == null || nuevoDoctor == null) {
            throw new IllegalArgumentException("Cita o doctor no válidos");
        }
        cita.setDoctor(nuevoDoctor);
    }

    /**
     * Crea una nueva cita a partir de un objeto JSON.
     *
     * @param dto El objeto JSON con los datos de la cita.
     * @throws IllegalArgumentException Si faltan datos obligatorios o si no se encuentran
     *                                  entidades relacionadas en la base de datos.
     */
    @Transactional
    public void crearCitaDesdeJson(JsonObject dto) {
        String documento = dto.getString("documento", null);
        String nombre = dto.getString("nombre", "Desconocido");
        String apellido = dto.getString("apellido", "");
        String nombreAseguradora = dto.getString("nombreAseguradora", null);
        String numeroAfiliacion = dto.getString("numeroAfiliacion", null);
        String codigoSeguro = dto.getString("codigoSeguro", null);
        String carnetSeguro = dto.getString("carnetSeguro", null);
    
        if (documento == null || documento.isEmpty()) {
            throw new IllegalArgumentException("El campo 'documento' es obligatorio");
        }
    
        // Verificar si ya existe un paciente con ese documento
        Paciente paciente = entityManager
            .createQuery("SELECT p FROM Paciente p WHERE p.documento = :doc", Paciente.class)
            .setParameter("doc", documento)
            .getResultStream()
            .findFirst()
            .orElse(null);
    
        if (paciente == null) {
            //  Crear nuevo usuario
            Usuario usuario = new Usuario();
            usuario.setNombreUsuario(nombre);
            usuario.setCorreo("auto_" + documento + "@hospital.com");
            usuario.setContrasena("1234");
    
            //  Asignar rol paciente (ID 4)
            Rol rolPaciente = entityManager.find(Rol.class, 4L);
            usuario.setRol(rolPaciente);
    
            entityManager.persist(usuario);
    
            //  Crear nuevo paciente
           //  Crear nuevo paciente
paciente = new Paciente();
paciente.setDocumento(documento);
paciente.setApellido(apellido);
paciente.setUsuario(usuario);
paciente.setIdUsuario(usuario.getId());

entityManager.persist(paciente);
System.out.println(" Usuario y paciente creados automáticamente");

//  Necesitamos obtener el paciente como PacienteFT para la ficha técnica
PacienteFT pacienteFT = entityManager
    .createQuery("SELECT p FROM PacienteFT p WHERE p.documento = :doc", PacienteFT.class)
    .setParameter("doc", documento)
    .getSingleResult();

//  Crear ficha técnica asociada al nuevo paciente
FichaTecnica ficha = new FichaTecnica();
ficha.setPaciente(pacienteFT);
ficha.setFechaCreacion(LocalDate.now());
ficha.setHistorialServicios("");
ficha.setNumeroAfiliacion(numeroAfiliacion);
ficha.setCodigoSeguro(codigoSeguro);
ficha.setCarnetSeguro(carnetSeguro);

entityManager.persist(ficha);
System.out.println("Ficha técnica creada automáticamente");

        }
    
        //  Crear cita
        Cita cita = new Cita();
        cita.setPaciente(paciente);
        cita.setIdPaciente(paciente.getIdPaciente());
        cita.setFecha(LocalDate.parse(dto.getString("fecha")));
        cita.setHoraInicio(dto.getString("horaInicio"));
        cita.setHoraFin(dto.getString("horaFin"));
        cita.setMotivo(dto.getString("motivo"));
        cita.setNumeroAutorizacion(dto.getString("numeroAutorizacion", "AUTO-GEN"));
        cita.setEstado(EstadoCita.CONFIRMADA);
    
        //  Asociar aseguradora si viene
        if (nombreAseguradora != null && !nombreAseguradora.isBlank()) {
            Aseguradora aseguradora = entityManager
                .createQuery("SELECT a FROM Aseguradora a WHERE UPPER(a.nombre) = :nombre", Aseguradora.class)
                .setParameter("nombre", nombreAseguradora.toUpperCase())
                .getResultStream()
                .findFirst()
                .orElse(null);
    
            if (aseguradora == null) {
                aseguradora = new Aseguradora();
                aseguradora.setNombre(nombreAseguradora);
                entityManager.persist(aseguradora);
                System.out.println("Aseguradora creada automáticamente");
            }
    
            cita.setAseguradora(aseguradora);
            cita.setIdAseguradora(aseguradora.getId());
        }
    
        citaRepository.persist(cita);
        System.out.println(" Cita guardada correctamente");
    }

    /**
     * Envía los resultados de una cita a la aseguradora correspondiente.
     *
     * @param cita La cita cuyos resultados se enviarán.
     */
}