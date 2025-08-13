package com.unis.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Date;
import com.unis.model.Receta;
import com.unis.model.RecetaMedicamento;

public class RecetaDTOTest {

    @Test
    void testConstructorConParametros() {
        // Arrange
        Receta receta = new Receta();
        receta.setIdReceta(1L);
        receta.setIdCita(1L);
        receta.setFechaCreacion(new Date());
        receta.setIdPaciente(1L);
        receta.setIdDoctor(1L);
        receta.setCodigoReceta("REC001");
        receta.setAnotaciones("Paciente con síntomas leves");
        receta.setNotasEspeciales("Tomar con alimentos");
        receta.setMedicamentos(Arrays.asList(new RecetaMedicamento(), new RecetaMedicamento()));
        
        String nombrePaciente = "Juan Pérez";

        // Act
        RecetaDTO recetaDTO = new RecetaDTO(receta, nombrePaciente);

        // Assert
        assertEquals(receta.getIdReceta(), recetaDTO.getIdReceta());
        assertEquals(receta.getIdCita(), recetaDTO.getIdCita());
        assertEquals(receta.getFechaCreacion(), recetaDTO.getFechaCreacion());
        assertEquals(receta.getIdPaciente(), recetaDTO.getIdPaciente());
        assertEquals(receta.getIdDoctor(), recetaDTO.getIdDoctor());
        assertEquals(receta.getCodigoReceta(), recetaDTO.getCodigoReceta());
        assertEquals(receta.getAnotaciones(), recetaDTO.getAnotaciones());
        assertEquals(receta.getNotasEspeciales(), recetaDTO.getNotasEspeciales());
        assertEquals(receta.getMedicamentos(), recetaDTO.getMedicamentos());
        assertEquals(nombrePaciente, recetaDTO.getNombrePaciente());
    }

    @Test
    void testGettersAndSetters() {
        // Arrange
        Receta receta = new Receta();
        receta.setIdReceta(1L);
        receta.setIdCita(1L);
        receta.setFechaCreacion(new Date());
        receta.setIdPaciente(1L);
        receta.setIdDoctor(1L);
        receta.setCodigoReceta("REC001");
        receta.setAnotaciones("Paciente con síntomas leves");
        receta.setNotasEspeciales("Tomar con alimentos");
        receta.setMedicamentos(Arrays.asList(new RecetaMedicamento(), new RecetaMedicamento()));
        
        String nombrePaciente = "Juan Pérez";
        RecetaDTO recetaDTO = new RecetaDTO(receta, nombrePaciente);

        // Act - Modificar algunos campos usando setters
        Long nuevoIdReceta = 999L;
        String nuevoCodigoReceta = "REC999";
        String nuevasAnotaciones = "Paciente con antecedentes de gastritis";
        List<RecetaMedicamento> nuevosMedicamentos = Arrays.asList(new RecetaMedicamento(), new RecetaMedicamento());
        
        recetaDTO.setIdReceta(nuevoIdReceta);
        recetaDTO.setCodigoReceta(nuevoCodigoReceta);
        recetaDTO.setAnotaciones(nuevasAnotaciones);
        recetaDTO.setMedicamentos(nuevosMedicamentos);

        // Assert
        assertEquals(nuevoIdReceta, recetaDTO.getIdReceta());
        assertEquals(nuevoCodigoReceta, recetaDTO.getCodigoReceta());
        assertEquals(nuevasAnotaciones, recetaDTO.getAnotaciones());
        assertEquals(nuevosMedicamentos, recetaDTO.getMedicamentos());
        assertEquals(nombrePaciente, recetaDTO.getNombrePaciente()); // Este campo no se puede modificar
    }

    @Test
    void testSetIdReceta() {
        // Arrange
        Receta receta = new Receta();
        RecetaDTO recetaDTO = new RecetaDTO(receta, "Test");
        
        Long idReceta = 999L;
        
        // Act
        recetaDTO.setIdReceta(idReceta);
        
        // Assert
        assertEquals(idReceta, recetaDTO.getIdReceta());
    }

    @Test
    void testSetIdCita() {
        // Arrange
        Receta receta = new Receta();
        RecetaDTO recetaDTO = new RecetaDTO(receta, "Test");
        
        Long idCita = 999L;
        
        // Act
        recetaDTO.setIdCita(idCita);
        
        // Assert
        assertEquals(idCita, recetaDTO.getIdCita());
    }

    @Test
    void testSetFechaCreacion() {
        // Arrange
        Receta receta = new Receta();
        RecetaDTO recetaDTO = new RecetaDTO(receta, "Test");
        
        Date fechaCreacion = new Date();
        
        // Act
        recetaDTO.setFechaCreacion(fechaCreacion);
        
        // Assert
        assertEquals(fechaCreacion, recetaDTO.getFechaCreacion());
    }

    @Test
    void testSetIdPaciente() {
        // Arrange
        Receta receta = new Receta();
        RecetaDTO recetaDTO = new RecetaDTO(receta, "Test");
        
        Long idPaciente = 999L;
        
        // Act
        recetaDTO.setIdPaciente(idPaciente);
        
        // Assert
        assertEquals(idPaciente, recetaDTO.getIdPaciente());
    }

    @Test
    void testSetIdDoctor() {
        // Arrange
        Receta receta = new Receta();
        RecetaDTO recetaDTO = new RecetaDTO(receta, "Test");
        
        Long idDoctor = 999L;
        
        // Act
        recetaDTO.setIdDoctor(idDoctor);
        
        // Assert
        assertEquals(idDoctor, recetaDTO.getIdDoctor());
    }

    @Test
    void testSetCodigoReceta() {
        // Arrange
        Receta receta = new Receta();
        RecetaDTO recetaDTO = new RecetaDTO(receta, "Test");
        
        String codigoReceta = "REC999";
        
        // Act
        recetaDTO.setCodigoReceta(codigoReceta);
        
        // Assert
        assertEquals(codigoReceta, recetaDTO.getCodigoReceta());
    }

    @Test
    void testSetAnotaciones() {
        // Arrange
        Receta receta = new Receta();
        RecetaDTO recetaDTO = new RecetaDTO(receta, "Test");
        
        String anotaciones = "Paciente con antecedentes de gastritis";
        
        // Act
        recetaDTO.setAnotaciones(anotaciones);
        
        // Assert
        assertEquals(anotaciones, recetaDTO.getAnotaciones());
    }

    @Test
    void testSetNotasEspeciales() {
        // Arrange
        Receta receta = new Receta();
        RecetaDTO recetaDTO = new RecetaDTO(receta, "Test");
        
        String notasEspeciales = "Tomar con alimentos";
        
        // Act
        recetaDTO.setNotasEspeciales(notasEspeciales);
        
        // Assert
        assertEquals(notasEspeciales, recetaDTO.getNotasEspeciales());
    }

    @Test
    void testSetMedicamentos() {
        // Arrange
        Receta receta = new Receta();
        RecetaDTO recetaDTO = new RecetaDTO(receta, "Test");
        
        List<RecetaMedicamento> medicamentos = Arrays.asList(new RecetaMedicamento(), new RecetaMedicamento(), new RecetaMedicamento());
        
        // Act
        recetaDTO.setMedicamentos(medicamentos);
        
        // Assert
        assertEquals(medicamentos, recetaDTO.getMedicamentos());
        assertEquals(3, recetaDTO.getMedicamentos().size());
    }

    @Test
    void testSetIdRecetaNull() {
        // Arrange
        Receta receta = new Receta();
        RecetaDTO recetaDTO = new RecetaDTO(receta, "Test");
        
        // Act
        recetaDTO.setIdReceta(null);
        
        // Assert
        assertNull(recetaDTO.getIdReceta());
    }

    @Test
    void testSetIdCitaNull() {
        // Arrange
        Receta receta = new Receta();
        RecetaDTO recetaDTO = new RecetaDTO(receta, "Test");
        
        // Act
        recetaDTO.setIdCita(null);
        
        // Assert
        assertNull(recetaDTO.getIdCita());
    }

    @Test
    void testSetFechaCreacionNull() {
        // Arrange
        Receta receta = new Receta();
        RecetaDTO recetaDTO = new RecetaDTO(receta, "Test");
        
        // Act
        recetaDTO.setFechaCreacion(null);
        
        // Assert
        assertNull(recetaDTO.getFechaCreacion());
    }

    @Test
    void testSetIdPacienteNull() {
        // Arrange
        Receta receta = new Receta();
        RecetaDTO recetaDTO = new RecetaDTO(receta, "Test");
        
        // Act
        recetaDTO.setIdPaciente(null);
        
        // Assert
        assertNull(recetaDTO.getIdPaciente());
    }

    @Test
    void testSetIdDoctorNull() {
        // Arrange
        Receta receta = new Receta();
        RecetaDTO recetaDTO = new RecetaDTO(receta, "Test");
        
        // Act
        recetaDTO.setIdDoctor(null);
        
        // Assert
        assertNull(recetaDTO.getIdDoctor());
    }

    @Test
    void testSetCodigoRecetaNull() {
        // Arrange
        Receta receta = new Receta();
        RecetaDTO recetaDTO = new RecetaDTO(receta, "Test");
        
        // Act
        recetaDTO.setCodigoReceta(null);
        
        // Assert
        assertNull(recetaDTO.getCodigoReceta());
    }

    @Test
    void testSetAnotacionesNull() {
        // Arrange
        Receta receta = new Receta();
        RecetaDTO recetaDTO = new RecetaDTO(receta, "Test");
        
        // Act
        recetaDTO.setAnotaciones(null);
        
        // Assert
        assertNull(recetaDTO.getAnotaciones());
    }

    @Test
    void testSetNotasEspecialesNull() {
        // Arrange
        Receta receta = new Receta();
        RecetaDTO recetaDTO = new RecetaDTO(receta, "Test");
        
        // Act
        recetaDTO.setNotasEspeciales(null);
        
        // Assert
        assertNull(recetaDTO.getNotasEspeciales());
    }

    @Test
    void testSetMedicamentosNull() {
        // Arrange
        Receta receta = new Receta();
        RecetaDTO recetaDTO = new RecetaDTO(receta, "Test");
        
        // Act
        recetaDTO.setMedicamentos(null);
        
        // Assert
        assertNull(recetaDTO.getMedicamentos());
    }

    @Test
    void testSetMedicamentosListaVacia() {
        // Arrange
        Receta receta = new Receta();
        RecetaDTO recetaDTO = new RecetaDTO(receta, "Test");
        
        List<RecetaMedicamento> medicamentos = Arrays.asList();
        
        // Act
        recetaDTO.setMedicamentos(medicamentos);
        
        // Assert
        assertEquals(medicamentos, recetaDTO.getMedicamentos());
        assertEquals(0, recetaDTO.getMedicamentos().size());
    }

    @Test
    void testSetCodigoRecetaVacio() {
        // Arrange
        Receta receta = new Receta();
        RecetaDTO recetaDTO = new RecetaDTO(receta, "Test");
        
        String codigoReceta = "";
        
        // Act
        recetaDTO.setCodigoReceta(codigoReceta);
        
        // Assert
        assertEquals(codigoReceta, recetaDTO.getCodigoReceta());
    }

    @Test
    void testSetAnotacionesVacia() {
        // Arrange
        Receta receta = new Receta();
        RecetaDTO recetaDTO = new RecetaDTO(receta, "Test");
        
        String anotaciones = "";
        
        // Act
        recetaDTO.setAnotaciones(anotaciones);
        
        // Assert
        assertEquals(anotaciones, recetaDTO.getAnotaciones());
    }

    @Test
    void testSetNotasEspecialesVacia() {
        // Arrange
        Receta receta = new Receta();
        RecetaDTO recetaDTO = new RecetaDTO(receta, "Test");
        
        String notasEspeciales = "";
        
        // Act
        recetaDTO.setNotasEspeciales(notasEspeciales);
        
        // Assert
        assertEquals(notasEspeciales, recetaDTO.getNotasEspeciales());
    }
}
