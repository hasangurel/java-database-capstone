package com.project.back_end.repository;

import com.project.back_end.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Patient entity.
 * Provides database access methods for patient operations.
 */
@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    /**
     * Find patient by username.
     *
     * @param username the username
     * @return Optional containing the patient if found
     */
    Optional<Patient> findByUsername(String username);

    /**
     * Find patient by email.
     *
     * @param email the email
     * @return Optional containing the patient if found
     */
    Optional<Patient> findByEmail(String email);

    /**
     * Find patients by name containing the search term (case-insensitive).
     *
     * @param name the search term
     * @return List of patients matching the search
     */
    List<Patient> findByNameContainingIgnoreCase(String name);

    /**
     * Find active patients.
     *
     * @param isActive the active status
     * @return List of active patients
     */
    List<Patient> findByIsActive(Boolean isActive);

    /**
     * Check if patient exists by username.
     *
     * @param username the username
     * @return true if exists, false otherwise
     */
    boolean existsByUsername(String username);

    /**
     * Check if patient exists by email.
     *
     * @param email the email
     * @return true if exists, false otherwise
     */
    boolean existsByEmail(String email);
}
