package com.unis.service;

import java.util.Optional;

import com.unis.model.DoctorAcc;
import com.unis.repository.DoctorAccRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

/**
 * Servicio para gestionar las operaciones relacionadas con los accesos de doctores.
 */
@ApplicationScoped
public class DoctorAccService {

    @Inject
    DoctorAccRepository doctorAccRepository;

    /**
     * Obtiene un doctor por su ID de usuario.
     *
     * @param id El ID del usuario asociado al doctor.
     * @return Un Optional que contiene el doctor si se encuentra.
     */
    public Optional<DoctorAcc> getDoctorById(Long id) {
        return doctorAccRepository.find("usuario.idUsuario", id).firstResultOptional();
    }

    /**
     * Actualiza los datos de un doctor existente.
     *
     * @param id         El ID del usuario asociado al doctor.
     * @param doctorData Los nuevos datos del doctor.
     */
    @Transactional
    public void updateDoctor(Long id, DoctorAcc doctorData) {
        Optional<DoctorAcc> existingDoctor = doctorAccRepository.find("usuario.idUsuario", id).firstResultOptional();
        if (existingDoctor.isPresent()) {
            DoctorAcc doctor = existingDoctor.get();
            doctor.setApellido(doctorData.getApellido());
            doctor.setDocumento(doctorData.getDocumento());
            doctor.setFechaNacimiento(doctorData.getFechaNacimiento());
            doctor.setGenero(doctorData.getGenero());
            doctor.setTelefono(doctorData.getTelefono());
            doctor.setEspecialidad(doctorData.getEspecialidad());
            doctor.setNumeroColegiado(doctorData.getNumeroColegiado());
            doctor.setFechaGraduacion(doctorData.getFechaGraduacion());
            doctor.setUniversidadGraduacion(doctorData.getUniversidadGraduacion());
            doctor.setDisponibilidad(doctorData.getDisponibilidad());
            doctorAccRepository.persist(doctor);
        }
    }
}
