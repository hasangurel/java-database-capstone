package com.project.back_end.service;

import com.project.back_end.model.Patient;
import com.project.back_end.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for Patient-related business logic.
 * Handles CRUD operations and patient-specific queries.
 */
@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    /**
     * Get all patients.
     *
     * @return List of all patients
     */
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    /**
     * Get patient by ID.
     *
     * @param id the patient ID
     * @return Optional containing the patient if found
     */
    public Optional<Patient> getPatientById(Long id) {
        return patientRepository.findById(id);
    }

    /**
     * Get patient by username.
     *
     * @param username the username
     * @return Optional containing the patient if found
     */
    public Optional<Patient> getPatientByUsername(String username) {
        return patientRepository.findByUsername(username);
    }

    /**
     * Save or update patient.
     *
     * @param patient the patient to save
     * @return saved patient
     */
    public Patient savePatient(Patient patient) {
        return patientRepository.save(patient);
    }

    /**
     * Delete patient by ID.
     *
     * @param id the patient ID
     */
    public void deletePatient(Long id) {
        patientRepository.deleteById(id);
    }

    /**
     * Search patients by name.
     *
     * @param name the search term
     * @return List of patients
     */
    public List<Patient> searchByName(String name) {
        return patientRepository.findByNameContainingIgnoreCase(name);
    }

    /**
     * Find active patients.
     *
     * @return List of active patients
     */
    public List<Patient> findActivePatients() {
        return patientRepository.findByIsActive(true);
    }

    /**
     * Authenticate patient.
     *
     * @param username the username
     * @param password the password
     * @return Optional containing the patient if authenticated
     */
    public Optional<Patient> authenticate(String username, String password) {
        Optional<Patient> patient = patientRepository.findByUsername(username);
        if (patient.isPresent() && patient.get().getPassword().equals(password)) {
            return patient;
        }
        return Optional.empty();
    }

    /**
     * Check if patient exists by username.
     *
     * @param username the username
     * @return true if exists
     */
    public boolean existsByUsername(String username) {
        return patientRepository.existsByUsername(username);
    }

    /**
     * Check if patient exists by email.
     *
     * @param email the email
     * @return true if exists
     */
    public boolean existsByEmail(String email) {
        return patientRepository.existsByEmail(email);
    }
}
