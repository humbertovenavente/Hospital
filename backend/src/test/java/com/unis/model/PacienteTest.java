package com.unis.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

public class PacienteTest {

    private Paciente paciente;

    @BeforeEach
    void setUp() {
        paciente = new Paciente();
    }

    @Test
    void testConstructorPorDefecto() {
        assertNotNull(paciente);
    }

    @Test
    void testGettersAndSetters() {
        // Arrange
        Long idPaciente = 1L;
        Long idUsuario = 100L;
        String apellido = "Pérez";
        String documento = "12345678";
        Date fechaNacimiento = new Date();
        String genero = "Masculino";
        String telefono = "502-1234-5678";
        byte[] fotografia = "foto".getBytes();

        // Act
        paciente.setIdPaciente(idPaciente);
        paciente.setIdUsuario(idUsuario);
        paciente.setApellido(apellido);
        paciente.setDocumento(documento);
        paciente.setFechaNacimiento(fechaNacimiento);
        paciente.setGenero(genero);
        paciente.setTelefono(telefono);
        paciente.setFotografia(fotografia);

        // Assert
        assertEquals(idPaciente, paciente.getIdPaciente());
        assertEquals(idUsuario, paciente.getIdUsuario());
        assertEquals(apellido, paciente.getApellido());
        assertEquals(documento, paciente.getDocumento());
        assertEquals(fechaNacimiento, paciente.getFechaNacimiento());
        assertEquals(genero, paciente.getGenero());
        assertEquals(telefono, paciente.getTelefono());
        assertEquals(fotografia, paciente.getFotografia());
    }

    @Test
    void testSetYGetIdPaciente() {
        // Arrange
        Long idPaciente = 999L;

        // Act
        paciente.setIdPaciente(idPaciente);

        // Assert
        assertEquals(idPaciente, paciente.getIdPaciente());
    }

    @Test
    void testSetYGetIdUsuario() {
        // Arrange
        Long idUsuario = 200L;

        // Act
        paciente.setIdUsuario(idUsuario);

        // Assert
        assertEquals(idUsuario, paciente.getIdUsuario());
    }

    @Test
    void testSetYGetApellido() {
        // Arrange
        String apellido = "López";

        // Act
        paciente.setApellido(apellido);

        // Assert
        assertEquals(apellido, paciente.getApellido());
    }

    @Test
    void testSetYGetDocumento() {
        // Arrange
        String documento = "87654321";

        // Act
        paciente.setDocumento(documento);

        // Assert
        assertEquals(documento, paciente.getDocumento());
    }

    @Test
    void testSetYGetFechaNacimiento() {
        // Arrange
        Date fechaNacimiento = new Date();

        // Act
        paciente.setFechaNacimiento(fechaNacimiento);

        // Assert
        assertEquals(fechaNacimiento, paciente.getFechaNacimiento());
    }

    @Test
    void testSetYGetGenero() {
        // Arrange
        String genero = "Femenino";

        // Act
        paciente.setGenero(genero);

        // Assert
        assertEquals(genero, paciente.getGenero());
    }

    @Test
    void testSetYGetTelefono() {
        // Arrange
        String telefono = "502-9876-5432";

        // Act
        paciente.setTelefono(telefono);

        // Assert
        assertEquals(telefono, paciente.getTelefono());
    }

    @Test
    void testSetYGetFotografia() {
        // Arrange
        byte[] fotografia = "nueva_foto".getBytes();

        // Act
        paciente.setFotografia(fotografia);

        // Assert
        assertEquals(fotografia, paciente.getFotografia());
    }

    @Test
    void testSetYGetUsuario() {
        // Arrange
        Usuario usuario = new Usuario();

        // Act
        paciente.setUsuario(usuario);

        // Assert
        assertEquals(usuario, paciente.getUsuario());
    }

    @Test
    void testSetYGetCitas() {
        // Arrange
        Cita cita1 = new Cita();
        Cita cita2 = new Cita();

        // Act
        paciente.getCitas().add(cita1);
        paciente.getCitas().add(cita2);

        // Assert
        assertEquals(2, paciente.getCitas().size());
        assertTrue(paciente.getCitas().contains(cita1));
        assertTrue(paciente.getCitas().contains(cita2));
    }

    @Test
    void testGetNombre() {
        // Arrange
        paciente.setApellido("García");
        paciente.setDocumento("DOC123");

        // Act
        String nombre = paciente.getNombre();

        // Assert
        assertNotNull(nombre);
        assertTrue(nombre.contains("García"));
        assertTrue(nombre.contains("DOC123"));
    }

    @Test
    void testSetIdPacienteNull() {
        // Act
        paciente.setIdPaciente(null);

        // Assert
        assertNull(paciente.getIdPaciente());
    }

    @Test
    void testSetIdUsuarioNull() {
        // Act
        paciente.setIdUsuario(null);

        // Assert
        assertNull(paciente.getIdUsuario());
    }

    @Test
    void testSetApellidoNull() {
        // Act
        paciente.setApellido(null);

        // Assert
        assertNull(paciente.getApellido());
    }

    @Test
    void testSetDocumentoNull() {
        // Act
        paciente.setDocumento(null);

        // Assert
        assertNull(paciente.getDocumento());
    }

    @Test
    void testSetFechaNacimientoNull() {
        // Act
        paciente.setFechaNacimiento(null);

        // Assert
        assertNull(paciente.getFechaNacimiento());
    }

    @Test
    void testSetGeneroNull() {
        // Act
        paciente.setGenero(null);

        // Assert
        assertNull(paciente.getGenero());
    }

    @Test
    void testSetTelefonoNull() {
        // Act
        paciente.setTelefono(null);

        // Assert
        assertNull(paciente.getTelefono());
    }

    @Test
    void testSetFotografiaNull() {
        // Act
        paciente.setFotografia(null);

        // Assert
        assertNull(paciente.getFotografia());
    }

    @Test
    void testSetUsuarioNull() {
        // Act
        paciente.setUsuario(null);

        // Assert
        assertNull(paciente.getUsuario());
    }

    @Test
    void testSetCitasNull() {
        // Act
        paciente.setCitas(null);

        // Assert
        assertNull(paciente.getCitas());
    }

    @Test
    void testSetApellidoVacio() {
        // Arrange
        String apellidoVacio = "";

        // Act
        paciente.setApellido(apellidoVacio);

        // Assert
        assertEquals(apellidoVacio, paciente.getApellido());
        assertTrue(paciente.getApellido().isEmpty());
    }

    @Test
    void testSetDocumentoVacio() {
        // Arrange
        String documentoVacio = "";

        // Act
        paciente.setDocumento(documentoVacio);

        // Assert
        assertEquals(documentoVacio, paciente.getDocumento());
        assertTrue(paciente.getDocumento().isEmpty());
    }

    @Test
    void testSetGeneroVacio() {
        // Arrange
        String generoVacio = "";

        // Act
        paciente.setGenero(generoVacio);

        // Assert
        assertEquals(generoVacio, paciente.getGenero());
        assertTrue(paciente.getGenero().isEmpty());
    }

    @Test
    void testSetTelefonoVacio() {
        // Arrange
        String telefonoVacio = "";

        // Act
        paciente.setTelefono(telefonoVacio);

        // Assert
        assertEquals(telefonoVacio, paciente.getTelefono());
        assertTrue(paciente.getTelefono().isEmpty());
    }
}
