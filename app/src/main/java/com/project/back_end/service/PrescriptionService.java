package com.project.back_end.service;

import com.project.back_end.model.Prescription;
import com.project.back_end.repository.PrescriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for Prescription-related business logic.
 * Handles CRUD operations for MongoDB prescription documents.
 */
@Service
public class PrescriptionService {

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    /**
     * Get all prescriptions.
     *
     * @return List of all prescriptions
     */
    public List<Prescription> getAllPrescriptions() {
        return prescriptionRepository.findAll();
    }

    /**
     * Get prescription by ID.
     *
     * @param id the prescription ID
     * @return Optional containing the prescription if found
     */
    public Optional<Prescription> getPrescriptionById(String id) {
        return prescriptionRepository.findById(id);
    }

    /**
     * Save or update prescription.
     *
     * @param prescription the prescription to save
     * @return saved prescription
     */
    public Prescription savePrescription(Prescription prescription) {
        return prescriptionRepository.save(prescription);
    }

    /**
     * Delete prescription by ID.
     *
     * @param id the prescription ID
     */
    public void deletePrescription(String id) {
        prescriptionRepository.deleteById(id);
    }

    /**
     * Get prescriptions by patient ID.
     *
     * @param patientId the patient ID
     * @return List of prescriptions
     */
    public List<Prescription> getPrescriptionsByPatientId(Long patientId) {
        return prescriptionRepository.findByPatientId(patientId);
    }

    /**
     * Get prescriptions by doctor ID.
     *
     * @param doctorId the doctor ID
     * @return List of prescriptions
     */
    public List<Prescription> getPrescriptionsByDoctorId(Long doctorId) {
        return prescriptionRepository.findByDoctorId(doctorId);
    }

    /**
     * Get prescriptions by appointment ID.
     *
     * @param appointmentId the appointment ID
     * @return List of prescriptions
     */
    public List<Prescription> getPrescriptionsByAppointmentId(Long appointmentId) {
        return prescriptionRepository.findByAppointmentId(appointmentId);
    }

    /**
     * Search prescriptions by patient name.
     *
     * @param patientName the patient name
     * @return List of prescriptions
     */
    public List<Prescription> searchByPatientName(String patientName) {
        return prescriptionRepository.findByPatientNameContainingIgnoreCase(patientName);
    }
}
