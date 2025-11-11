package com.project.back_end.repository;

import com.project.back_end.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Doctor entity.
 * Provides database access methods for doctor operations.
 */
@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    /**
     * Find doctor by username.
     *
     * @param username the username
     * @return Optional containing the doctor if found
     */
    Optional<Doctor> findByUsername(String username);

    /**
     * Find doctors by specialty.
     *
     * @param specialty the specialty
     * @return List of doctors with the given specialty
     */
    List<Doctor> findBySpecialty(String specialty);

    /**
     * Find doctors by name containing the search term (case-insensitive).
     *
     * @param name the search term
     * @return List of doctors matching the search
     */
    List<Doctor> findByNameContainingIgnoreCase(String name);

    /**
     * Find active doctors.
     *
     * @param isActive the active status
     * @return List of active doctors
     */
    List<Doctor> findByIsActive(Boolean isActive);

    /**
     * Find doctors by specialty and available time slot.
     *
     * @param specialty the specialty
     * @param timeSlot the available time slot
     * @return List of doctors matching criteria
     */
    @Query("SELECT d FROM Doctor d JOIN d.availableTimes t WHERE d.specialty = :specialty AND t = :timeSlot AND d.isActive = true")
    List<Doctor> findBySpecialtyAndAvailableTime(@Param("specialty") String specialty, @Param("timeSlot") String timeSlot);

    /**
     * Check if doctor exists by username.
     *
     * @param username the username
     * @return true if exists, false otherwise
     */
    boolean existsByUsername(String username);
}
