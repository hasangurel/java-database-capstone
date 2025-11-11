package com.project.back_end.service;

import com.project.back_end.model.Doctor;
import com.project.back_end.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for Doctor-related business logic.
 * Handles CRUD operations and doctor-specific queries.
 */
@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    /**
     * Get all doctors.
     *
     * @return List of all doctors
     */
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    /**
     * Get doctor by ID.
     *
     * @param id the doctor ID
     * @return Optional containing the doctor if found
     */
    public Optional<Doctor> getDoctorById(Long id) {
        return doctorRepository.findById(id);
    }

    /**
     * Get doctor by username.
     *
     * @param username the username
     * @return Optional containing the doctor if found
     */
    public Optional<Doctor> getDoctorByUsername(String username) {
        return doctorRepository.findByUsername(username);
    }

    /**
     * Save or update doctor.
     *
     * @param doctor the doctor to save
     * @return saved doctor
     */
    public Doctor saveDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    /**
     * Delete doctor by ID.
     *
     * @param id the doctor ID
     */
    public void deleteDoctor(Long id) {
        doctorRepository.deleteById(id);
    }

    /**
     * Find doctors by specialty.
     *
     * @param specialty the specialty
     * @return List of doctors
     */
    public List<Doctor> findBySpecialty(String specialty) {
        return doctorRepository.findBySpecialty(specialty);
    }

    /**
     * Search doctors by name.
     *
     * @param name the search term
     * @return List of doctors
     */
    public List<Doctor> searchByName(String name) {
        return doctorRepository.findByNameContainingIgnoreCase(name);
    }

    /**
     * Find active doctors.
     *
     * @return List of active doctors
     */
    public List<Doctor> findActiveDoctors() {
        return doctorRepository.findByIsActive(true);
    }

    /**
     * Find doctors by specialty and available time.
     *
     * @param specialty the specialty
     * @param timeSlot the time slot
     * @return List of doctors
     */
    public List<Doctor> findBySpecialtyAndTime(String specialty, String timeSlot) {
        return doctorRepository.findBySpecialtyAndAvailableTime(specialty, timeSlot);
    }

    /**
     * Authenticate doctor.
     *
     * @param username the username
     * @param password the password
     * @return Optional containing the doctor if authenticated
     */
    public Optional<Doctor> authenticate(String username, String password) {
        Optional<Doctor> doctor = doctorRepository.findByUsername(username);
        if (doctor.isPresent() && doctor.get().getPassword().equals(password)) {
            return doctor;
        }
        return Optional.empty();
    }

    /**
     * Check if doctor exists by username.
     *
     * @param username the username
     * @return true if exists
     */
    public boolean existsByUsername(String username) {
        return doctorRepository.existsByUsername(username);
    }
}
