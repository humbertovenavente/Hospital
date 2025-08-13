package com.unis.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

public class MedicinasReporteDTOTest {

    private MedicinasReporteDTO medicinasReporteDTO;

    @BeforeEach
    void setUp() {
        medicinasReporteDTO = new MedicinasReporteDTO(1, "Paracetamol", 10);
    }

    @Test
    void testConstructorPorDefecto() {
        assertNotNull(medicinasReporteDTO);
    }

    @Test
    void testConstructorConParametros() {
        // Arrange
        int popularidad = 5;
        String principioActivo = "Ibuprofeno";
        int totalRecetas = 25;

        // Act
        MedicinasReporteDTO dto = new MedicinasReporteDTO(popularidad, principioActivo, totalRecetas);

        // Assert
        assertEquals(popularidad, dto.popularidad);
        assertEquals(principioActivo, dto.principioActivo);
        assertEquals(totalRecetas, dto.totalRecetas);
    }


}
