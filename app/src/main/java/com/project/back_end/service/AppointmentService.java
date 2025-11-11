package com.project.back_end.service;

import com.project.back_end.dto.AppointmentDto;
import com.project.back_end.model.Appointment;
import com.project.back_end.model.Doctor;
import com.project.back_end.model.Patient;
import com.project.back_end.repository.AppointmentRepository;
import com.project.back_end.repository.DoctorRepository;
import com.project.back_end.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class for Appointment-related business logic.
 * Handles CRUD operations and appointment-specific queries.
 */
@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    /**
     * Get all appointments.
     *
     * @return List of all appointments
     */
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    /**
     * Get appointment by ID.
     *
     * @param id the appointment ID
     * @return Optional containing the appointment if found
     */
    public Optional<Appointment> getAppointmentById(Long id) {
        return appointmentRepository.findById(id);
    }

    /**
     * Save or update appointment.
     *
     * @param appointment the appointment to save
     * @return saved appointment
     */
    public Appointment saveAppointment(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }

    /**
     * Delete appointment by ID.
     *
     * @param id the appointment ID
     */
    public void deleteAppointment(Long id) {
        appointmentRepository.deleteById(id);
    }

    /**
     * Get appointments by doctor ID.
     *
     * @param doctorId the doctor ID
     * @return List of appointments
     */
    public List<Appointment> getAppointmentsByDoctorId(Long doctorId) {
        return appointmentRepository.findByDoctorId(doctorId);
    }

    /**
     * Get appointments by patient ID.
     *
     * @param patientId the patient ID
     * @return List of appointments
     */
    public List<Appointment> getAppointmentsByPatientId(Long patientId) {
        return appointmentRepository.findByPatientId(patientId);
    }

    /**
     * Get appointments by status.
     *
     * @param status the status
     * @return List of appointments
     */
    public List<Appointment> getAppointmentsByStatus(String status) {
        return appointmentRepository.findByStatus(status);
    }

    /**
     * Get appointments in date range.
     *
     * @param start the start date
     * @param end the end date
     * @return List of appointments
     */
    public List<Appointment> getAppointmentsByDateRange(LocalDateTime start, LocalDateTime end) {
        return appointmentRepository.findByAppointmentDateTimeBetween(start, end);
    }

    /**
     * Create appointment from DTO.
     *
     * @param dto the appointment DTO
     * @return saved appointment
     * @throws RuntimeException if doctor or patient not found
     */
    public Appointment createAppointment(AppointmentDto dto) {
        Doctor doctor = doctorRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + dto.getDoctorId()));

        Patient patient = patientRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + dto.getPatientId()));

        Appointment appointment = new Appointment();
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setAppointmentDateTime(dto.getAppointmentDateTime());
        appointment.setDurationMinutes(dto.getDurationMinutes());
        appointment.setStatus(dto.getStatus());
        appointment.setReason(dto.getReason());
        appointment.setNotes(dto.getNotes());
        appointment.setCreatedAt(LocalDateTime.now());

        return appointmentRepository.save(appointment);
    }

    /**
     * Convert Appointment to AppointmentDto.
     *
     * @param appointment the appointment
     * @return AppointmentDto
     */
    public AppointmentDto toDto(Appointment appointment) {
        AppointmentDto dto = new AppointmentDto();
        dto.setId(appointment.getId());
        dto.setDoctorId(appointment.getDoctor().getId());
        dto.setPatientId(appointment.getPatient().getId());
        dto.setDoctorName(appointment.getDoctor().getName());
        dto.setPatientName(appointment.getPatient().getName());
        dto.setDoctorSpecialty(appointment.getDoctor().getSpecialty());
        dto.setAppointmentDateTime(appointment.getAppointmentDateTime());
        dto.setDurationMinutes(appointment.getDurationMinutes());
        dto.setStatus(appointment.getStatus());
        dto.setReason(appointment.getReason());
        dto.setNotes(appointment.getNotes());
        return dto;
    }

    /**
     * Get appointments by doctor and date range.
     *
     * @param doctorId the doctor ID
     * @param start the start date
     * @param end the end date
     * @return List of appointment DTOs
     */
    public List<AppointmentDto> getAppointmentsByDoctorAndDateRange(Long doctorId, LocalDateTime start, LocalDateTime end) {
        return appointmentRepository.findByDoctorAndDateRange(doctorId, start, end)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Get appointments by patient and date range.
     *
     * @param patientId the patient ID
     * @param start the start date
     * @param end the end date
     * @return List of appointment DTOs
     */
    public List<AppointmentDto> getAppointmentsByPatientAndDateRange(Long patientId, LocalDateTime start, LocalDateTime end) {
        return appointmentRepository.findByPatientAndDateRange(patientId, start, end)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
