package com.project.back_end.repository;

import com.project.back_end.model.Appointment;
import com.project.back_end.model.Doctor;
import com.project.back_end.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface for Appointment entity.
 * Provides database access methods for appointment operations.
 */
@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    /**
     * Find appointments by doctor.
     *
     * @param doctor the doctor
     * @return List of appointments for the doctor
     */
    List<Appointment> findByDoctor(Doctor doctor);

    /**
     * Find appointments by patient.
     *
     * @param patient the patient
     * @return List of appointments for the patient
     */
    List<Appointment> findByPatient(Patient patient);

    /**
     * Find appointments by doctor ID.
     *
     * @param doctorId the doctor ID
     * @return List of appointments for the doctor
     */
    List<Appointment> findByDoctorId(Long doctorId);

    /**
     * Find appointments by patient ID.
     *
     * @param patientId the patient ID
     * @return List of appointments for the patient
     */
    List<Appointment> findByPatientId(Long patientId);

    /**
     * Find appointments by status.
     *
     * @param status the status
     * @return List of appointments with the given status
     */
    List<Appointment> findByStatus(String status);

    /**
     * Find appointments between date range.
     *
     * @param start the start date
     * @param end the end date
     * @return List of appointments in the date range
     */
    List<Appointment> findByAppointmentDateTimeBetween(LocalDateTime start, LocalDateTime end);

    /**
     * Find appointments by doctor and date range.
     *
     * @param doctorId the doctor ID
     * @param start the start date
     * @param end the end date
     * @return List of appointments
     */
    @Query("SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId AND a.appointmentDateTime BETWEEN :start AND :end")
    List<Appointment> findByDoctorAndDateRange(@Param("doctorId") Long doctorId,
                                                @Param("start") LocalDateTime start,
                                                @Param("end") LocalDateTime end);

    /**
     * Find appointments by patient and date range.
     *
     * @param patientId the patient ID
     * @param start the start date
     * @param end the end date
     * @return List of appointments
     */
    @Query("SELECT a FROM Appointment a WHERE a.patient.id = :patientId AND a.appointmentDateTime BETWEEN :start AND :end")
    List<Appointment> findByPatientAndDateRange(@Param("patientId") Long patientId,
                                                 @Param("start") LocalDateTime start,
                                                 @Param("end") LocalDateTime end);
}
