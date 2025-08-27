package com.unis.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

/**
 * Test class for Cita entity.
 * Tests all getters, setters, and edge cases to achieve 100% coverage.
 */
class CitaTest {

    private Cita cita;
    private Doctor doctor;
    private Paciente paciente;
    private Hospital hospital;
    private Servicio servicio;
    private Aseguradora aseguradora;
    private LocalDate fecha;
    private EstadoCita estado;

    @BeforeEach
    void setUp() {
        cita = new Cita();
        
        // Setup related entities
        doctor = new Doctor();
        doctor.setIdDoctor(1L);
        
        paciente = new Paciente();
        paciente.setIdPaciente(100L);
        
        hospital = new Hospital();
        hospital.setId(10L);
        
        servicio = new Servicio();
        servicio.id = 20L;
        
        aseguradora = new Aseguradora();
        aseguradora.setId(30L);
        
        fecha = LocalDate.of(2024, 8, 26);
        estado = EstadoCita.PENDIENTE;
    }

    @Test
    void testDefaultConstructor() {
        assertNotNull(cita);
        assertNull(cita.getIdCita());
        assertNull(cita.getDoctor());
        assertNull(cita.getPaciente());
        assertNull(cita.getIdDoctor());
        assertNull(cita.getIdPaciente());
        assertNull(cita.getNumeroAutorizacion());
        assertNull(cita.getFecha());
        assertNull(cita.getHoraInicio());
        assertNull(cita.getHoraFin());
        assertNull(cita.getIdHospital());
        assertNull(cita.getIdServicio());
        assertNull(cita.getIdAseguradora());
        assertNull(cita.getEstado());
        assertNull(cita.getMotivo());
        assertNull(cita.getDiagnostico());
        assertNull(cita.getResultados());
        assertNull(cita.getHospital());
        assertNull(cita.getServicio());
        assertNull(cita.getAseguradora());
    }

    // ========== ID_CITA Tests ==========
    
    @Test
    void testSetAndGetIdCita() {
        Long id = 123L;
        cita.setIdCita(id);
        assertEquals(id, cita.getIdCita());
    }

    @Test
    void testSetAndGetIdCitaWithNull() {
        cita.setIdCita(null);
        assertNull(cita.getIdCita());
    }

    @Test
    void testSetAndGetIdCitaWithZero() {
        cita.setIdCita(0L);
        assertEquals(0L, cita.getIdCita());
    }

    @Test
    void testSetAndGetIdCitaWithNegative() {
        cita.setIdCita(-1L);
        assertEquals(-1L, cita.getIdCita());
    }

    @Test
    void testSetAndGetIdCitaWithMaxValue() {
        Long maxValue = Long.MAX_VALUE;
        cita.setIdCita(maxValue);
        assertEquals(maxValue, cita.getIdCita());
    }

    // ========== DOCTOR Tests ==========
    
    @Test
    void testSetAndGetDoctor() {
        cita.setDoctor(doctor);
        assertEquals(doctor, cita.getDoctor());
        assertEquals(1L, cita.getIdDoctor());
    }

    @Test
    void testSetAndGetDoctorWithNull() {
        cita.setDoctor(null);
        assertNull(cita.getDoctor());
        assertNull(cita.getIdDoctor());
    }

    @Test
    void testSetAndGetDoctorWithNewInstance() {
        Doctor newDoctor = new Doctor();
        newDoctor.setIdDoctor(999L);
        cita.setDoctor(newDoctor);
        assertEquals(newDoctor, cita.getDoctor());
        assertEquals(999L, cita.getIdDoctor());
    }

    @Test
    void testSetAndGetDoctorWithDoctorWithoutId() {
        Doctor doctorSinId = new Doctor();
        cita.setDoctor(doctorSinId);
        assertEquals(doctorSinId, cita.getDoctor());
        assertNull(cita.getIdDoctor());
    }

    // ========== PACIENTE Tests ==========
    
    @Test
    void testSetAndGetPaciente() {
        cita.setPaciente(paciente);
        assertEquals(paciente, cita.getPaciente());
        assertEquals(100L, cita.getIdPaciente());
    }

    @Test
    void testSetAndGetPacienteWithNull() {
        cita.setPaciente(null);
        assertNull(cita.getPaciente());
        assertNull(cita.getIdPaciente());
    }

    @Test
    void testSetAndGetPacienteWithNewInstance() {
        Paciente newPaciente = new Paciente();
        newPaciente.setIdPaciente(888L);
        cita.setPaciente(newPaciente);
        assertEquals(newPaciente, cita.getPaciente());
        assertEquals(888L, cita.getIdPaciente());
    }

    @Test
    void testSetAndGetPacienteWithPacienteWithoutId() {
        Paciente pacienteSinId = new Paciente();
        cita.setPaciente(pacienteSinId);
        assertEquals(pacienteSinId, cita.getPaciente());
        assertNull(cita.getIdPaciente());
    }

    // ========== ID_DOCTOR Tests ==========
    
    @Test
    void testSetAndGetIdDoctor() {
        Long idDoctor = 456L;
        cita.setIdDoctor(idDoctor);
        assertEquals(idDoctor, cita.getIdDoctor());
    }

    @Test
    void testSetAndGetIdDoctorWithNull() {
        cita.setIdDoctor(null);
        assertNull(cita.getIdDoctor());
    }

    @Test
    void testSetAndGetIdDoctorWithZero() {
        cita.setIdDoctor(0L);
        assertEquals(0L, cita.getIdDoctor());
    }

    @Test
    void testSetAndGetIdDoctorWithNegative() {
        cita.setIdDoctor(-1L);
        assertEquals(-1L, cita.getIdDoctor());
    }

    // ========== ID_PACIENTE Tests ==========
    
    @Test
    void testSetAndGetIdPaciente() {
        Long idPaciente = 789L;
        cita.setIdPaciente(idPaciente);
        assertEquals(idPaciente, cita.getIdPaciente());
    }

    @Test
    void testSetAndGetIdPacienteWithNull() {
        cita.setIdPaciente(null);
        assertNull(cita.getIdPaciente());
    }

    @Test
    void testSetAndGetIdPacienteWithZero() {
        cita.setIdPaciente(0L);
        assertEquals(0L, cita.getIdPaciente());
    }

    @Test
    void testSetAndGetIdPacienteWithNegative() {
        cita.setIdPaciente(-1L);
        assertEquals(-1L, cita.getIdPaciente());
    }

    // ========== NUMERO_AUTORIZACION Tests ==========
    
    @Test
    void testSetAndGetNumeroAutorizacion() {
        String numero = "AUTH123456";
        cita.setNumeroAutorizacion(numero);
        assertEquals(numero, cita.getNumeroAutorizacion());
    }

    @Test
    void testSetAndGetNumeroAutorizacionWithNull() {
        cita.setNumeroAutorizacion(null);
        assertNull(cita.getNumeroAutorizacion());
    }

    @Test
    void testSetAndGetNumeroAutorizacionWithEmptyString() {
        cita.setNumeroAutorizacion("");
        assertEquals("", cita.getNumeroAutorizacion());
    }

    @Test
    void testSetAndGetNumeroAutorizacionWithSpecialCharacters() {
        String numero = "AUTH-123_456";
        cita.setNumeroAutorizacion(numero);
        assertEquals(numero, cita.getNumeroAutorizacion());
    }

    @Test
    void testSetAndGetNumeroAutorizacionWithWhitespace() {
        String numero = " AUTH123456 ";
        cita.setNumeroAutorizacion(numero);
        assertEquals(numero, cita.getNumeroAutorizacion());
    }

    // ========== FECHA Tests ==========
    
    @Test
    void testSetAndGetFecha() {
        cita.setFecha(fecha);
        assertEquals(fecha, cita.getFecha());
    }

    @Test
    void testSetAndGetFechaWithNull() {
        cita.setFecha(null);
        assertNull(cita.getFecha());
    }

    @Test
    void testSetAndGetFechaWithNewDate() {
        LocalDate newDate = LocalDate.of(2024, 12, 25);
        cita.setFecha(newDate);
        assertEquals(newDate, cita.getFecha());
    }

    @Test
    void testSetAndGetFechaWithPastDate() {
        LocalDate pastDate = LocalDate.of(2020, 1, 1);
        cita.setFecha(pastDate);
        assertEquals(pastDate, cita.getFecha());
    }

    @Test
    void testSetAndGetFechaWithFutureDate() {
        LocalDate futureDate = LocalDate.of(2030, 12, 31);
        cita.setFecha(futureDate);
        assertEquals(futureDate, cita.getFecha());
    }

    // ========== HORA_INICIO Tests ==========
    
    @Test
    void testSetAndGetHoraInicio() {
        String hora = "09:00";
        cita.setHoraInicio(hora);
        assertEquals(hora, cita.getHoraInicio());
    }

    @Test
    void testSetAndGetHoraInicioWithNull() {
        cita.setHoraInicio(null);
        assertNull(cita.getHoraInicio());
    }

    @Test
    void testSetAndGetHoraInicioWithEmptyString() {
        cita.setHoraInicio("");
        assertEquals("", cita.getHoraInicio());
    }

    @Test
    void testSetAndGetHoraInicioWithDifferentFormats() {
        String[] horas = {"09:00", "14:30", "16:45", "08:15"};
        
        for (String hora : horas) {
            cita.setHoraInicio(hora);
            assertEquals(hora, cita.getHoraInicio());
        }
    }

    @Test
    void testSetAndGetHoraInicioWithWhitespace() {
        String hora = " 09:00 ";
        cita.setHoraInicio(hora);
        assertEquals(hora, cita.getHoraInicio());
    }

    // ========== HORA_FIN Tests ==========
    
    @Test
    void testSetAndGetHoraFin() {
        String hora = "10:00";
        cita.setHoraFin(hora);
        assertEquals(hora, cita.getHoraFin());
    }

    @Test
    void testSetAndGetHoraFinWithNull() {
        cita.setHoraFin(null);
        assertNull(cita.getHoraFin());
    }

    @Test
    void testSetAndGetHoraFinWithEmptyString() {
        cita.setHoraFin("");
        assertEquals("", cita.getHoraFin());
    }

    @Test
    void testSetAndGetHoraFinWithDifferentFormats() {
        String[] horas = {"10:00", "15:30", "17:45", "09:15"};
        
        for (String hora : horas) {
            cita.setHoraFin(hora);
            assertEquals(hora, cita.getHoraFin());
        }
    }

    @Test
    void testSetAndGetHoraFinWithWhitespace() {
        String hora = " 10:00 ";
        cita.setHoraFin(hora);
        assertEquals(hora, cita.getHoraFin());
    }

    // ========== ID_HOSPITAL Tests ==========
    
    @Test
    void testSetAndGetIdHospital() {
        Long idHospital = 123L;
        cita.setIdHospital(idHospital);
        assertEquals(idHospital, cita.getIdHospital());
    }

    @Test
    void testSetAndGetIdHospitalWithNull() {
        cita.setIdHospital(null);
        assertNull(cita.getIdHospital());
    }

    @Test
    void testSetAndGetIdHospitalWithZero() {
        cita.setIdHospital(0L);
        assertEquals(0L, cita.getIdHospital());
    }

    @Test
    void testSetAndGetIdHospitalWithNegative() {
        cita.setIdHospital(-1L);
        assertEquals(-1L, cita.getIdHospital());
    }

    // ========== ID_SERVICIO Tests ==========
    
    @Test
    void testSetAndGetIdServicio() {
        Long idServicio = 456L;
        cita.setIdServicio(idServicio);
        assertEquals(idServicio, cita.getIdServicio());
    }

    @Test
    void testSetAndGetIdServicioWithNull() {
        cita.setIdServicio(null);
        assertNull(cita.getIdServicio());
    }

    @Test
    void testSetAndGetIdServicioWithZero() {
        cita.setIdServicio(0L);
        assertEquals(0L, cita.getIdServicio());
    }

    @Test
    void testSetAndGetIdServicioWithNegative() {
        cita.setIdServicio(-1L);
        assertEquals(-1L, cita.getIdServicio());
    }

    // ========== ID_ASEGURADORA Tests ==========
    
    @Test
    void testSetAndGetIdAseguradora() {
        Long idAseguradora = 789L;
        cita.setIdAseguradora(idAseguradora);
        assertEquals(idAseguradora, cita.getIdAseguradora());
    }

    @Test
    void testSetAndGetIdAseguradoraWithNull() {
        cita.setIdAseguradora(null);
        assertNull(cita.getIdAseguradora());
    }

    @Test
    void testSetAndGetIdAseguradoraWithZero() {
        cita.setIdAseguradora(0L);
        assertEquals(0L, cita.getIdAseguradora());
    }

    @Test
    void testSetAndGetIdAseguradoraWithNegative() {
        cita.setIdAseguradora(-1L);
        assertEquals(-1L, cita.getIdAseguradora());
    }

    // ========== ESTADO Tests ==========
    
    @Test
    void testSetAndGetEstado() {
        cita.setEstado(estado);
        assertEquals(estado, cita.getEstado());
    }

    @Test
    void testSetAndGetEstadoWithNull() {
        cita.setEstado(null);
        assertNull(cita.getEstado());
    }

    @Test
    void testSetAndGetEstadoWithDifferentValues() {
        EstadoCita[] estados = {
            EstadoCita.PENDIENTE,
            EstadoCita.CONFIRMADA,
            EstadoCita.CANCELADA,
            EstadoCita.FINALIZADA
        };
        
        for (EstadoCita est : estados) {
            cita.setEstado(est);
            assertEquals(est, cita.getEstado());
        }
    }

    // ========== MOTIVO Tests ==========
    
    @Test
    void testSetAndGetMotivo() {
        String motivo = "Consulta de rutina";
        cita.setMotivo(motivo);
        assertEquals(motivo, cita.getMotivo());
    }

    @Test
    void testSetAndGetMotivoWithNull() {
        cita.setMotivo(null);
        assertNull(cita.getMotivo());
    }

    @Test
    void testSetAndGetMotivoWithEmptyString() {
        cita.setMotivo("");
        assertEquals("", cita.getMotivo());
    }

    @Test
    void testSetAndGetMotivoWithLongText() {
        String motivo = "Consulta de seguimiento post-operatorio para evaluar la evolución de la cirugía realizada el mes pasado";
        cita.setMotivo(motivo);
        assertEquals(motivo, cita.getMotivo());
    }

    @Test
    void testSetAndGetMotivoWithWhitespace() {
        String motivo = " Consulta de rutina ";
        cita.setMotivo(motivo);
        assertEquals(motivo, cita.getMotivo());
    }

    // ========== DIAGNOSTICO Tests ==========
    
    @Test
    void testSetAndGetDiagnostico() {
        String diagnostico = "Hipertensión arterial";
        cita.setDiagnostico(diagnostico);
        assertEquals(diagnostico, cita.getDiagnostico());
    }

    @Test
    void testSetAndGetDiagnosticoWithNull() {
        cita.setDiagnostico(null);
        assertNull(cita.getDiagnostico());
    }

    @Test
    void testSetAndGetDiagnosticoWithEmptyString() {
        cita.setDiagnostico("");
        assertEquals("", cita.getDiagnostico());
    }

    @Test
    void testSetAndGetDiagnosticoWithLongText() {
        String diagnostico = "Diabetes mellitus tipo 2 con complicaciones microvasculares, incluyendo retinopatía diabética no proliferativa";
        cita.setDiagnostico(diagnostico);
        assertEquals(diagnostico, cita.getDiagnostico());
    }

    @Test
    void testSetAndGetDiagnosticoWithWhitespace() {
        String diagnostico = " Hipertensión arterial ";
        cita.setDiagnostico(diagnostico);
        assertEquals(diagnostico, cita.getDiagnostico());
    }

    // ========== RESULTADOS Tests ==========
    
    @Test
    void testSetAndGetResultados() {
        String resultados = "Paciente estable, continuar tratamiento";
        cita.setResultados(resultados);
        assertEquals(resultados, cita.getResultados());
    }

    @Test
    void testSetAndGetResultadosWithNull() {
        cita.setResultados(null);
        assertNull(cita.getResultados());
    }

    @Test
    void testSetAndGetResultadosWithEmptyString() {
        cita.setResultados("");
        assertEquals("", cita.getResultados());
    }

    @Test
    void testSetAndGetResultadosWithLongText() {
        String resultados = "Se realizó examen físico completo, análisis de sangre y radiografía de tórax. Los resultados muestran mejoría significativa en los parámetros respiratorios";
        cita.setResultados(resultados);
        assertEquals(resultados, cita.getResultados());
    }

    @Test
    void testSetAndGetResultadosWithWhitespace() {
        String resultados = " Paciente estable, continuar tratamiento ";
        cita.setResultados(resultados);
        assertEquals(resultados, cita.getResultados());
    }

    // ========== HOSPITAL Tests ==========
    
    @Test
    void testSetAndGetHospital() {
        cita.setHospital(hospital);
        assertEquals(hospital, cita.getHospital());
    }

    @Test
    void testSetAndGetHospitalWithNull() {
        cita.setHospital(null);
        assertNull(cita.getHospital());
    }

    @Test
    void testSetAndGetHospitalWithNewInstance() {
        Hospital newHospital = new Hospital();
        newHospital.setId(999L);
        cita.setHospital(newHospital);
        assertEquals(newHospital, cita.getHospital());
    }

    // ========== SERVICIO Tests ==========
    
    @Test
    void testSetAndGetServicio() {
        cita.setServicio(servicio);
        assertEquals(servicio, cita.getServicio());
    }

    @Test
    void testSetAndGetServicioWithNull() {
        cita.setServicio(null);
        assertNull(cita.getServicio());
    }

    @Test
    void testSetAndGetServicioWithNewInstance() {
        Servicio newServicio = new Servicio();
        newServicio.id = 888L;
        cita.setServicio(newServicio);
        assertEquals(newServicio, cita.getServicio());
    }

    // ========== ASEGURADORA Tests ==========
    
    @Test
    void testSetAndGetAseguradora() {
        cita.setAseguradora(aseguradora);
        assertEquals(aseguradora, cita.getAseguradora());
    }

    @Test
    void testSetAndGetAseguradoraWithNull() {
        cita.setAseguradora(null);
        assertNull(cita.getAseguradora());
    }

    @Test
    void testSetAndGetAseguradoraWithNewInstance() {
        Aseguradora newAseguradora = new Aseguradora();
        newAseguradora.setId(777L);
        cita.setAseguradora(newAseguradora);
        assertEquals(newAseguradora, cita.getAseguradora());
    }

    // ========== Comprehensive Tests ==========
    
    @Test
    void testCompleteCitaSetup() {
        // Arrange
        Long id = 999L;
        String numeroAutorizacion = "AUTH999888";
        String horaInicio = "14:00";
        String horaFin = "15:00";
        String motivo = "Consulta especializada";
        String diagnostico = "Dolor crónico";
        String resultados = "Se prescribe tratamiento";

        // Act
        cita.setIdCita(id);
        cita.setDoctor(doctor);
        cita.setPaciente(paciente);
        cita.setNumeroAutorizacion(numeroAutorizacion);
        cita.setFecha(fecha);
        cita.setHoraInicio(horaInicio);
        cita.setHoraFin(horaFin);
        cita.setIdHospital(10L);
        cita.setIdServicio(20L);
        cita.setIdAseguradora(30L);
        cita.setEstado(estado);
        cita.setMotivo(motivo);
        cita.setDiagnostico(diagnostico);
        cita.setResultados(resultados);
        cita.setHospital(hospital);
        cita.setServicio(servicio);
        cita.setAseguradora(aseguradora);

        // Assert
        assertEquals(id, cita.getIdCita());
        assertEquals(doctor, cita.getDoctor());
        assertEquals(paciente, cita.getPaciente());
        assertEquals(1L, cita.getIdDoctor());
        assertEquals(100L, cita.getIdPaciente());
        assertEquals(numeroAutorizacion, cita.getNumeroAutorizacion());
        assertEquals(fecha, cita.getFecha());
        assertEquals(horaInicio, cita.getHoraInicio());
        assertEquals(horaFin, cita.getHoraFin());
        assertEquals(10L, cita.getIdHospital());
        assertEquals(20L, cita.getIdServicio());
        assertEquals(30L, cita.getIdAseguradora());
        assertEquals(estado, cita.getEstado());
        assertEquals(motivo, cita.getMotivo());
        assertEquals(diagnostico, cita.getDiagnostico());
        assertEquals(resultados, cita.getResultados());
        assertEquals(hospital, cita.getHospital());
        assertEquals(servicio, cita.getServicio());
        assertEquals(aseguradora, cita.getAseguradora());
    }

    @Test
    void testMultipleUpdates() {
        // Arrange
        cita.setMotivo("Consulta inicial");
        cita.setHoraInicio("09:00");
        
        // Act - Update values
        cita.setMotivo("Consulta de seguimiento");
        cita.setHoraInicio("10:00");
        
        // Assert
        assertEquals("Consulta de seguimiento", cita.getMotivo());
        assertEquals("10:00", cita.getHoraInicio());
    }

    @Test
    void testResetToNull() {
        // Arrange
        cita.setMotivo("Consulta inicial");
        cita.setDiagnostico("Sin diagnóstico");
        
        // Act - Reset to null
        cita.setMotivo(null);
        cita.setDiagnostico(null);
        
        // Assert
        assertNull(cita.getMotivo());
        assertNull(cita.getDiagnostico());
    }

    @Test
    void testEdgeCaseValues() {
        // Arrange & Act - Set edge case values
        cita.setIdCita(0L);
        cita.setIdDoctor(0L);
        cita.setIdPaciente(0L);
        cita.setIdHospital(0L);
        cita.setIdServicio(0L);
        cita.setIdAseguradora(0L);
        cita.setMotivo("");
        cita.setDiagnostico("   ");
        cita.setResultados("0");
        
        // Assert
        assertEquals(0L, cita.getIdCita());
        assertEquals(0L, cita.getIdDoctor());
        assertEquals(0L, cita.getIdPaciente());
        assertEquals(0L, cita.getIdHospital());
        assertEquals(0L, cita.getIdServicio());
        assertEquals(0L, cita.getIdAseguradora());
        assertEquals("", cita.getMotivo());
        assertEquals("   ", cita.getDiagnostico());
        assertEquals("0", cita.getResultados());
    }

    @Test
    void testRelationshipUpdates() {
        // Arrange
        Doctor newDoctor = new Doctor();
        newDoctor.setIdDoctor(555L);
        
        Paciente newPaciente = new Paciente();
        newPaciente.setIdPaciente(666L);
        
        // Act
        cita.setDoctor(newDoctor);
        cita.setPaciente(newPaciente);
        
        // Assert
        assertEquals(newDoctor, cita.getDoctor());
        assertEquals(newPaciente, cita.getPaciente());
        assertEquals(555L, cita.getIdDoctor());
        assertEquals(666L, cita.getIdPaciente());
    }

    @Test
    void testAllFieldsTogether() {
        // Arrange
        Long id = 777L;
        String numeroAutorizacion = "AUTH777666";
        LocalDate fechaCita = LocalDate.of(2024, 9, 15);
        String horaInicio = "16:00";
        String horaFin = "17:00";
        String motivo = "Revisión post-tratamiento";
        String diagnostico = "Evolución favorable";
        String resultados = "Continuar con medicación actual";

        // Act
        cita.setIdCita(id);
        cita.setDoctor(doctor);
        cita.setPaciente(paciente);
        cita.setNumeroAutorizacion(numeroAutorizacion);
        cita.setFecha(fechaCita);
        cita.setHoraInicio(horaInicio);
        cita.setHoraFin(horaFin);
        cita.setIdHospital(15L);
        cita.setIdServicio(25L);
        cita.setIdAseguradora(35L);
        cita.setEstado(EstadoCita.FINALIZADA);
        cita.setMotivo(motivo);
        cita.setDiagnostico(diagnostico);
        cita.setResultados(resultados);
        cita.setHospital(hospital);
        cita.setServicio(servicio);
        cita.setAseguradora(aseguradora);

        // Assert - Verify all fields are set correctly
        assertEquals(id, cita.getIdCita());
        assertEquals(doctor, cita.getDoctor());
        assertEquals(paciente, cita.getPaciente());
        assertEquals(1L, cita.getIdDoctor());
        assertEquals(100L, cita.getIdPaciente());
        assertEquals(numeroAutorizacion, cita.getNumeroAutorizacion());
        assertEquals(fechaCita, cita.getFecha());
        assertEquals(horaInicio, cita.getHoraInicio());
        assertEquals(horaFin, cita.getHoraFin());
        assertEquals(15L, cita.getIdHospital());
        assertEquals(25L, cita.getIdServicio());
        assertEquals(35L, cita.getIdAseguradora());
        assertEquals(EstadoCita.FINALIZADA, cita.getEstado());
        assertEquals(motivo, cita.getMotivo());
        assertEquals(diagnostico, cita.getDiagnostico());
        assertEquals(resultados, cita.getResultados());
        assertEquals(hospital, cita.getHospital());
        assertEquals(servicio, cita.getServicio());
        assertEquals(aseguradora, cita.getAseguradora());
    }
}
