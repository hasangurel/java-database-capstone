package com.project.back_end.repository;

import com.project.back_end.model.Prescription;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Prescription document in MongoDB.
 * Provides database access methods for prescription operations.
 */
@Repository
public interface PrescriptionRepository extends MongoRepository<Prescription, String> {

    /**
     * Find prescriptions by patient ID.
     *
     * @param patientId the patient ID
     * @return List of prescriptions for the patient
     */
    List<Prescription> findByPatientId(Long patientId);

    /**
     * Find prescriptions by doctor ID.
     *
     * @param doctorId the doctor ID
     * @return List of prescriptions by the doctor
     */
    List<Prescription> findByDoctorId(Long doctorId);

    /**
     * Find prescription by appointment ID.
     *
     * @param appointmentId the appointment ID
     * @return List of prescriptions for the appointment
     */
    List<Prescription> findByAppointmentId(Long appointmentId);

    /**
     * Find prescriptions by patient name containing the search term.
     *
     * @param patientName the search term
     * @return List of prescriptions matching the search
     */
    List<Prescription> findByPatientNameContainingIgnoreCase(String patientName);
}
